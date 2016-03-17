package lv.adventus.seb.servlets;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import progress.message.jclient.MultipartMessage;
import progress.message.jclient.Part;
import progress.message.jclient.Header;
import progress.message.jclient.Constants;
import progress.message.jclient.TextMessage;
import progress.message.jclient.BytesMessage;

import lv.adventus.seb.util.Connector;
import lv.adventus.seb.util.MultipartMessageUtility;
import lv.adventus.seb.util.XMLUtility;
import lv.adventus.seb.UnifiedServiceHeader;
import lv.adventus.seb.UnifiedServiceResponse;
import lv.adventus.seb.UnifiedServiceErrors;
import lv.adventus.seb.ContactcenterFindCustomerByPhoneOrPersonalCode2Output;
import lv.adventus.seb.ContactcenterGiveDigipassChallenge2Output;

/**
 * Servlet implementation class FindCustomerByPhoneOrPersonalCode
 */
public class FindCustomerByPhoneOrPersonalCode extends ServletBase {
	

    /**
     * Default constructor. 
     */
    public FindCustomerByPhoneOrPersonalCode() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
        PrintWriter out = response.getWriter();
	    response.setContentType("text/plain");
	    String userId = request.getParameter("id");
	    String connId = "cc" + request.getParameter("connid");
	    
	    // check http request parameters
	    
	    if (userId==null || connId==null)
	    {
	    	String s = "";
	    	if(userId==null) s += "userId ";
	    	if(connId==null) s += "connId ";
	    	System.out.println("Parameter " + s + "not received");
	    	out.print("error:TECHNICALERROR");
	    	return;
	    }
	    else
	    {
//	    	System.out.println("FindCustomerByPhoneOrPersonalCode: id = " + userId);
//	    	System.out.println("FindCustomerByPhoneOrPersonalCode: connid = " + connId);
	    }
	    
	 // check PingPong service result
	    ServletContext context = request.getSession().getServletContext();
 	 	Boolean attribute = (Boolean)context.getAttribute("PingPong"); 
 		if(attribute.booleanValue() == false)
 		{
 			System.out.println("FindCustomerByPhoneOrPersonalCode: PingPong returns 0.");
 			out.print("error:TECHNICALERROR");
 			return;
 		}
	    
	    try
		{
	    	
/////////// invoke FindCustomerByPhoneOrPersonalCode service
	    	
			lv.adventus.seb.FindCustomerByPhoneOrPersonalCode fc = new lv.adventus.seb.FindCustomerByPhoneOrPersonalCode();
			fc.SetHeader();
			fc.SetHeaderUserId(userId);
			fc.SetHeaderRequestId(connId);
			fc.SetBody();
			fc.SetBody(userId, "");
			xmlrequest = fc.Marshal();
  			// print XML
			System.out.println("FindCustomerByPhoneOrPersonalCode sent this XML:");
			System.out.println(XMLUtility.prettyFormat(xmlrequest));

			c = new Connector(broker,usernameSonic,passwordSonic,queue, out, connectionTimeout);
			c.SetHeader(fc.GetHeader());
			c.start();
			c.createMessage();
		    this.usr = c.query(xmlrequest);
		    if(this.usr == null)
		    {
		    	System.out.println("FindCustomerByPhoneOrPersonalCode: query returned null.");
	 			out.print("error:TECHNICALERROR");
	 			return;
		    }
		    if(usr.getUnifiedServiceErrors() != null) return;
	
			ContactcenterFindCustomerByPhoneOrPersonalCode2Output fco =
		    	  (ContactcenterFindCustomerByPhoneOrPersonalCode2Output) usr.getUnifiedServiceBody().getAny().get(0);
		    this.customerId = fco.getFindCustomerResponse().getCustomerId();
		    this.idCode = fco.getFindCustomerResponse().getIdCode();
//		    System.out.println("Answer from JMS Broker:");
//		    System.out.println("FindCustomerByPhoneOrPersonalCode: customerId = " + this.customerId);
//		    System.out.println("FindCustomerByPhoneOrPersonalCode: idCode = " + this.idCode);
	  		c.exit();

/////////// invoke GiveDigipassChallenge service
		
  			lv.adventus.seb.GiveDigipassChallenge dc = new lv.adventus.seb.GiveDigipassChallenge();
  			dc.SetHeader();
  			dc.SetHeaderUserId(customerId);
  			dc.SetHeaderRequestId(connId + "1");  // to make requestId unique
  			dc.SetBody();
  	 		dc.SetBody(this.customerId, this.idCode);
  			xmlrequest = dc.Marshal();
  			// print XML
			System.out.println("GiveDigipassChallenge sent this XML:");
			System.out.println(XMLUtility.prettyFormat(xmlrequest));
  			
			c = new Connector(broker,usernameSonic,passwordSonic,queue, out, connectionTimeout);
			c.SetHeader(dc.GetHeader());
			c.start();
			c.createMessage();
		    this.usr = c.query(xmlrequest);
		    if(usr.getUnifiedServiceErrors() != null) return;
  			
  			ContactcenterGiveDigipassChallenge2Output gco = (ContactcenterGiveDigipassChallenge2Output) usr.getUnifiedServiceBody().getAny().get(0);
  			this.customerId = gco.getGiveChallengeResponse().getCustomerId();
  			this.challengeCode = gco.getGiveChallengeResponse().getChallengeCode(); 
  			this.userName = gco.getGiveChallengeResponse().getUsername();
  			this.idCode = gco.getGiveChallengeResponse().getIdCode();
//  			System.out.println("Answer from JMS Broker:");
//  			System.out.println("GiveDigipassChallenge: customerId = " + this.customerId);
//  			System.out.println("GiveDigipassChallenge: challengeCode = " + this.challengeCode);
//  			System.out.println("GiveDigipassChallenge: userName = " + this.userName);
//  			System.out.println("GiveDigipassChallenge: idCode = " + this.idCode);
  			c.exit();
  			// this will be read by Genesys routing server
  			out.print("challengecode:" + this.challengeCode + "|userid:" + this.userName);
  			return;
        }
        catch (javax.jms.JMSException jmse)
        {
        	out.print("error:TECHNICALERROR");
        	System.out.print("javax.jms.JMSException: ");
        	System.out.println(jmse);
        }
	    catch (javax.xml.bind.JAXBException e)
	    {
        	out.print("error:TECHNICALERROR");
        	System.out.print("javax.xml.bind.JAXBException: ");
        	System.out.println(e.getMessage());
	    }
        catch (java.io.IOException e)
        {
        	out.print("error:TECHNICALERROR");
        	System.out.print("java.io.IOException: ");
        	System.out.println(e);
        }
	}
}
