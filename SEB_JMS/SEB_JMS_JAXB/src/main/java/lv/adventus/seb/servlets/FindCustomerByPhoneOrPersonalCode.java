 package lv.adventus.seb.servlets;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;
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
import lv.adventus.seb.util.Utility;
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

		// set all response headers here
		response.setContentType("text/plain; charset=UTF-8");  // this must be set before response.getWriter()
		response.setHeader("Cache-Control", "no-cache");

	    //PrintWriter out = response.getWriter(); // will be done later 

	    String userId = request.getParameter("id");
	    String connId = "cc" + request.getParameter("connid");

	    // check http request parameters

	    if (userId==null || connId==null)
	    {
	    	String pr = "";
	    	if(userId == null) pr += "userId ";
	    	if(connId == null) pr += "connId ";
	    	System.out.println("Expected parameter not received from HTTP GET request: " + pr);
        	Utility.ServletResponse(response, "error:TECHNICALERROR");
        	return;
	    }
	    else
	    {
	    	System.out.println("FindCustomerByPhoneOrPersonalCode: id = " + userId);
	    	System.out.println("FindCustomerByPhoneOrPersonalCode: connid = " + connId);
	    }

	 // check PingPong service result

	    if(Utility.CheckPingPongStatus(request, response, "FindCustomerByPhoneOrPersonalCode") == false)
	    {
 			Utility.ServletResponse(response,"error:TECHNICALERROR");
	    	return;
	    }
	    
	 // PingPong ok, we continue
	    
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

			c = new Connector(broker,usernameSonic,passwordSonic,queue, response, connectionTimeout, ttl, responseMsgTTL);
			System.out.println("FindCustomerByPhoneOrPersonalCode created Connector at: " + Connector.getTimestamp());
			c.SetHeader(fc.GetHeader());
			c.start();
			System.out.println("FindCustomerByPhoneOrPersonalCode started Connector at: " + Connector.getTimestamp());
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
		    	Utility.ServletResponse(response, "error:TECHNICALERROR");
	 			return;
		    }
		    if(usr.getUnifiedServiceErrors() != null) return;  // errors already returned in c.query()

			ContactcenterFindCustomerByPhoneOrPersonalCode2Output fco =
			    	  (ContactcenterFindCustomerByPhoneOrPersonalCode2Output) usr.getUnifiedServiceBody().getAny().get(0);
		    System.out.println("FindCustomerByPhoneOrPersonalCode response body extracted at: " + Connector.getTimestamp());
		    customerId = fco.getFindCustomerResponse().getCustomerId();
		    idCode = fco.getFindCustomerResponse().getIdCode();
		    firstName = fco.getFindCustomerResponse().getFirstName();
		    lastName = fco.getFindCustomerResponse().getLastName();
		    userPhoneNumber = fco.getFindCustomerResponse().getUserPhoneNumber();

		    System.out.println("Answer from JMS Broker:");
		    System.out.println("FindCustomerByPhoneOrPersonalCode: customerId = " + customerId);
		    System.out.println("FindCustomerByPhoneOrPersonalCode: idCode = " + idCode);
		    System.out.println("FindCustomerByPhoneOrPersonalCode: firstName = " + firstName);
		    System.out.println("FindCustomerByPhoneOrPersonalCode: lastName = " + lastName);
		    System.out.println("FindCustomerByPhoneOrPersonalCode: userPhoneNumber = " + userPhoneNumber);

/////////// invoke GiveDigipassChallenge service

		    if(Utility.CheckPingPongStatus(request, response, "GiveDigipassChallenge") == false)
		    {
	 			Utility.ServletResponse(response,"error:TECHNICALERROR");
		    	return;
		    }
		    
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
  			
			c = new Connector(broker,usernameSonic,passwordSonic,queue, response, connectionTimeout, ttl, responseMsgTTL);
			System.out.println("GiveDigipassChallenge created Connector at: " + Connector.getTimestamp());
			c.SetHeader(dc.GetHeader());
			c.start();
			System.out.println("GiveDigipassChallenge started Connector at: " + Connector.getTimestamp());
			c.createMessage();
			System.out.println("GiveDigipassChallenge Connector query begins at: " + Connector.getTimestamp());
		    usr = c.query(xmlrequest);
		    System.out.println("GiveDigipassChallenge Connector query ends at: " + Connector.getTimestamp());
  			System.out.println("GiveDigipassChallenge Exit from Connector started at: " + Connector.getTimestamp());
		    c.exit();
	  		System.out.println("GiveDigipassChallenge Exit from Connector completed at: " + Connector.getTimestamp());

		    if(usr == null)
		    {
		    	System.out.println("GiveDigipassChallenge: query returned null.");
	        	Utility.ServletResponse(response, "error:TECHNICALERROR");
	 			return;
		    }

		    if(usr.getUnifiedServiceErrors() != null) return;  // errors already returned in servlet response from c.query()
  			
  			ContactcenterGiveDigipassChallenge2Output gco = (ContactcenterGiveDigipassChallenge2Output) usr.getUnifiedServiceBody().getAny().get(0);
  			System.out.println("GiveDigipassChallenge response body extracted at: " + Connector.getTimestamp());
  			customerId = gco.getGiveChallengeResponse().getCustomerId();
  			challengeCode = gco.getGiveChallengeResponse().getChallengeCode(); 
  			userName = gco.getGiveChallengeResponse().getUsername();
  			idCode = gco.getGiveChallengeResponse().getIdCode();

  			System.out.println("Answer from JMS Broker:");
  			System.out.println("GiveDigipassChallenge: customerId = " + customerId);
  			System.out.println("GiveDigipassChallenge: challengeCode = " + challengeCode);
  			System.out.println("GiveDigipassChallenge: userName = " + userName);
  			System.out.println("GiveDigipassChallenge: idCode = " + idCode);

  			System.out.println("GiveDigipassChallenge servlet output started at: " + Connector.getTimestamp());
  			Utility.ServletResponse(response, "customerId:" + customerId + "|username:" + userName +
  					  "|idCode:" + idCode + "|firstName:" + firstName + 
  					  "|lastName:" + lastName + "|userPhoneNumber:" + userPhoneNumber + 
  					  "|challengecode:" + challengeCode);
  			System.out.println("GiveDigipassChallenge servlet output completed at: " + Connector.getTimestamp());
        }
        catch (javax.jms.JMSException jmse)
        {
        	Utility.ServletResponse(response, "error:TECHNICALERROR");
        	System.out.println(jmse.getMessage());
        	sc.log("FindCustomerByPhoneOrPersonalCode servlet JMSException: " + jmse.getMessage(), jmse);
        }
	    catch (javax.xml.bind.JAXBException e)
	    {
	    	Utility.ServletResponse(response, "error:TECHNICALERROR");
	    	System.out.println(e.getMessage());
	    	sc.log("FindCustomerByPhoneOrPersonalCode servlet JAXBException: " + e.getMessage(), e);
	    }
        catch (java.io.IOException e)
        {
	    	Utility.ServletResponse(response, "error:TECHNICALERROR");
	    	System.out.println(e.getMessage());
	    	sc.log("FindCustomerByPhoneOrPersonalCode servlet IOException: " + e.getMessage(), e);
        }
	}
}
