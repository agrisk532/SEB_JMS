package lv.adventus.seb.servlets;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;

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
		String xmlrequest;
	
		String customerId;
		String idCode;
		String firstName;
		String lastName;
		String userPhoneNumber;
		String challengeCode;
		String userName;
		
		UnifiedServiceResponse usr;
		Connector c;

		System.out.println("FindCustomerByPhoneOrPersonalCode http request received at: " + Connector.getTimestamp());
		response.setContentType("text/html; charset=UTF-8");
	    PrintWriter out = response.getWriter();
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
	    	out.flush();
	    	return;
	    }
	    else
	    {
	    	System.out.println("FindCustomerByPhoneOrPersonalCode: id = " + userId);
	    	System.out.println("FindCustomerByPhoneOrPersonalCode: connid = " + connId);
	    }
	    
	 // check PingPong service result
//	    ServletContext context = request.getSession().getServletContext();
// 	 	Boolean attribute = (Boolean)context.getAttribute("PingPong"); 
// 		if(attribute.booleanValue() == false || attribute == null)
// 		{
// 			System.out.println("FindCustomerByPhoneOrPersonalCode: PingPong returns 0. Processing stopped.");
// 			out.print("error:TECHNICALERROR");
// 			return;
// 		}
// 		else
// 		{
// 			System.out.println("FindCustomerByPhoneOrPersonalCode: PingPong returns 1. Processing continues.");
// 		}
	    
	    try
		{
	    	
/////////// invoke FindCustomerByPhoneOrPersonalCode service

	    	System.out.println("FindCustomerByPhoneOrPersonalCode processing started at: " + Connector.getTimestamp());
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

			System.out.println("FindCustomerByPhoneOrPersonalCode creating Connector at: " + Connector.getTimestamp());
			c = new Connector(broker,usernameSonic,passwordSonic,queue, response, connectionTimeout);
			c.SetHeader(fc.GetHeader());
			System.out.println("FindCustomerByPhoneOrPersonalCode starting Connector at: " + Connector.getTimestamp());
			c.start();
			c.createMessage();
			System.out.println("FindCustomerByPhoneOrPersonalCode Connector query begins at: " + Connector.getTimestamp());
		    usr = c.query(xmlrequest);
		    System.out.println("FindCustomerByPhoneOrPersonalCode Connector query ends at: " + Connector.getTimestamp());
  			System.out.println("FindCustomerByPhoneOrPersonalCode Exit from Connector started at: " + Connector.getTimestamp());
	  		c.exit();
	  		System.out.println("FindCustomerByPhoneOrPersonalCode Exit from Connector completed at: " + Connector.getTimestamp());

		    if(usr == null)
		    {
		    	System.out.println("FindCustomerByPhoneOrPersonalCode: query returned null.");
	 			out.print("error:TECHNICALERROR");
	 			out.flush();
	 			return;
		    }
		    if(usr.getUnifiedServiceErrors() != null) return;

  			System.out.println("FindCustomerByPhoneOrPersonalCode parsing response started at: " + Connector.getTimestamp());
			ContactcenterFindCustomerByPhoneOrPersonalCode2Output fco =
		    	  (ContactcenterFindCustomerByPhoneOrPersonalCode2Output) usr.getUnifiedServiceBody().getAny().get(0);
		    customerId = fco.getFindCustomerResponse().getCustomerId();
		    idCode = fco.getFindCustomerResponse().getIdCode();
		    firstName = fco.getFindCustomerResponse().getFirstName();
		    lastName = fco.getFindCustomerResponse().getLastName();
		    userPhoneNumber = fco.getFindCustomerResponse().getUserPhoneNumber();
//		    System.out.println("Answer from JMS Broker:");
//		    System.out.println("FindCustomerByPhoneOrPersonalCode: customerId = " + customerId);
//		    System.out.println("FindCustomerByPhoneOrPersonalCode: idCode = " + idCode);


/////////// invoke GiveDigipassChallenge service
		
	    	System.out.println("GiveDigipassChallenge processing started at: " + Connector.getTimestamp());
  			lv.adventus.seb.GiveDigipassChallenge dc = new lv.adventus.seb.GiveDigipassChallenge();
  			dc.SetHeader();
  			dc.SetHeaderUserId(customerId);
  			dc.SetHeaderRequestId(connId + "1");  // to make requestId unique
  			dc.SetBody();
  	 		dc.SetBody(customerId, idCode);
  			xmlrequest = dc.Marshal();
  			// print XML
			System.out.println("GiveDigipassChallenge sent this XML:");
			System.out.println(XMLUtility.prettyFormat(xmlrequest));
  			
			System.out.println("GiveDigipassChallenge creating Connector at: " + Connector.getTimestamp());
			c = new Connector(broker,usernameSonic,passwordSonic,queue, response, connectionTimeout);
			c.SetHeader(dc.GetHeader());
			System.out.println("GiveDigipassChallenge starting Connector at: " + Connector.getTimestamp());
			c.start();
			c.createMessage();
			System.out.println("GiveDigipassChallenge Connector query begins at: " + Connector.getTimestamp());
		    usr = c.query(xmlrequest);
		    System.out.println("GiveDigipassChallenge Connector query ends at: " + Connector.getTimestamp());
  			System.out.println("GiveDigipassChallenge Exit from Connector started at: " + Connector.getTimestamp());
		    c.exit();
	  		System.out.println("GiveDigipassChallenge Exit from Connector completed at: " + Connector.getTimestamp());
		    
		    if(usr.getUnifiedServiceErrors() != null) return;
  			
		    System.out.println("GiveDigipassChallenge parsing response started at: " + Connector.getTimestamp());
  			ContactcenterGiveDigipassChallenge2Output gco = (ContactcenterGiveDigipassChallenge2Output) usr.getUnifiedServiceBody().getAny().get(0);
  			customerId = gco.getGiveChallengeResponse().getCustomerId();
  			challengeCode = gco.getGiveChallengeResponse().getChallengeCode(); 
  			userName = gco.getGiveChallengeResponse().getUsername();
  			idCode = gco.getGiveChallengeResponse().getIdCode();
//  			System.out.println("Answer from JMS Broker:");
//  			System.out.println("GiveDigipassChallenge: customerId = " + customerId);
//  			System.out.println("GiveDigipassChallenge: challengeCode = " + challengeCode);
//  			System.out.println("GiveDigipassChallenge: userName = " + userName);
//  			System.out.println("GiveDigipassChallenge: idCode = " + idCode);
  			// this will be read by Genesys routing server
  			System.out.println("GiveDigipassChallenge servlet output started at: " + Connector.getTimestamp());
  			out.print("customerId:" + customerId + "|username:" + userName +
  					  "|idCode:" + idCode + "|firstName:" + firstName + 
  					  "|lastName:" + lastName + "|userPhoneNumber:" + userPhoneNumber + 
  					  "|challengecode:" + challengeCode);
  			System.out.println("GiveDigipassChallenge servlet output completed at: " + Connector.getTimestamp());
  			out.flush();
  			System.out.println("GiveDigipassChallenge servlet output flushed at: " + Connector.getTimestamp());
        }
        catch (javax.jms.JMSException jmse)
        {
        	out.print("error:TECHNICALERROR");
        	out.flush();
        	System.out.print("javax.jms.JMSException: ");
        	System.out.println(jmse);
        }
	    catch (javax.xml.bind.JAXBException e)
	    {
        	out.print("error:TECHNICALERROR");
        	out.flush();
        	System.out.print("javax.xml.bind.JAXBException: ");
        	System.out.println(e.getMessage());
	    }
        catch (java.io.IOException e)
        {
        	out.print("error:TECHNICALERROR");
        	out.flush();
        	System.out.print("java.io.IOException: ");
        	System.out.println(e);
        }
	}
}
