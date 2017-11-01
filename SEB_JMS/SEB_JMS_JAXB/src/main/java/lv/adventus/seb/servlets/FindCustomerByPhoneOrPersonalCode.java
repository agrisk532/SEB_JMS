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
	
	static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(FindCustomerByPhoneOrPersonalCode.class);
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

		String requestURI = request.getRequestURI().substring(1);	// remove slash
		LOGGER.info("http request received");

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
	    	LOGGER.error(requestURI + "Expected parameter not received from HTTP GET request: " + pr);
        	Utility.ServletResponse(response, "error:TECHNICALERROR");
        	return;
	    }
	    else
	    {
	    	LOGGER.info(requestURI + " :id = " + userId);
	    	LOGGER.info(requestURI + " :connid = " + connId);
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

	    	LOGGER.info(requestURI + "processing started");
			lv.adventus.seb.FindCustomerByPhoneOrPersonalCode fc = new lv.adventus.seb.FindCustomerByPhoneOrPersonalCode();
			fc.SetHeader();
			fc.SetHeaderUserId(userId);
			fc.SetHeaderRequestId(connId);
			fc.SetBody();
			fc.SetBody(userId, "");
			xmlrequest = fc.Marshal();
  			// print XML
			if(debug)LOGGER.debug(requestURI + " sent this XML:");
			if(debug)LOGGER.debug(XMLUtility.prettyFormat(xmlrequest));

			c = new Connector(broker,usernameSonic,passwordSonic,queue, response, connectionTimeout, ttl, responseMsgTTL, debug);
			if(debug)LOGGER.debug(requestURI + " created Connector");
			c.SetHeader(fc.GetHeader());
			c.start();
			if(debug)LOGGER.debug(requestURI + " started Connector");
			c.createMessage();
			if(debug)LOGGER.debug(requestURI + " Connector query begins");
		    usr = c.query(xmlrequest);
		    if(debug)LOGGER.debug(requestURI + " Connector query ends");
		    if(debug)LOGGER.debug(requestURI + " Exit from Connector started");
	  		c.exit();
	  		if(debug)LOGGER.debug(requestURI + " Exit from Connector completed");

		    if(usr == null)
		    {
		    	LOGGER.error("JMS service query returned null.");
		    	Utility.ServletResponse(response, "error:TECHNICALERROR");
	 			return;
		    }
		    if(usr.getUnifiedServiceErrors() != null) return;  // errors already returned in c.query()

			ContactcenterFindCustomerByPhoneOrPersonalCode2Output fco =
			    	  (ContactcenterFindCustomerByPhoneOrPersonalCode2Output) usr.getUnifiedServiceBody().getAny().get(0);
			if(debug)LOGGER.debug(requestURI + " response body extracted");
		    customerId = fco.getFindCustomerResponse().getCustomerId();
		    idCode = fco.getFindCustomerResponse().getIdCode();
		    firstName = fco.getFindCustomerResponse().getFirstName();
		    lastName = fco.getFindCustomerResponse().getLastName();
		    userPhoneNumber = fco.getFindCustomerResponse().getUserPhoneNumber();

		    LOGGER.info(requestURI + "Answer from JMS Broker:");
		    LOGGER.info(requestURI + " :customerId = " + customerId);
		    LOGGER.info(requestURI + " :idCode = " + idCode);
		    LOGGER.info(requestURI + " :firstName = " + firstName);
		    LOGGER.info(requestURI + " :lastName = " + lastName);
		    LOGGER.info(requestURI + " :userPhoneNumber = " + userPhoneNumber);

/////////// invoke GiveDigipassChallenge service

		    if(Utility.CheckPingPongStatus(request, response, "GiveDigipassChallenge") == false)
		    {
	 			Utility.ServletResponse(response,"error:TECHNICALERROR");
		    	return;
		    }
		    
	    	LOGGER.info(requestURI + "GiveDigipassChallenge processing started");
  			lv.adventus.seb.GiveDigipassChallenge dc = new lv.adventus.seb.GiveDigipassChallenge();
  			dc.SetHeader();
  			dc.SetHeaderUserId(customerId);
  			dc.SetHeaderRequestId(connId + "1");  // to make requestId unique
  			dc.SetBody();
  	 		dc.SetBody(customerId, idCode);
  			xmlrequest = dc.Marshal();
  			// print XML
  			if(debug)LOGGER.debug(requestURI + "GiveDigipassChallenge sent this XML:");
  			if(debug)LOGGER.debug(XMLUtility.prettyFormat(xmlrequest));
  			
			c = new Connector(broker,usernameSonic,passwordSonic,queue, response, connectionTimeout, ttl, responseMsgTTL, false);
			if(debug)LOGGER.debug(requestURI + "GiveDigipassChallenge created Connector");
			c.SetHeader(dc.GetHeader());
			c.start();
			if(debug)LOGGER.debug(requestURI + "GiveDigipassChallenge started Connector");
			c.createMessage();
			if(debug)LOGGER.debug(requestURI + "GiveDigipassChallenge Connector query begins");
		    usr = c.query(xmlrequest);
		    if(debug)LOGGER.debug(requestURI + "GiveDigipassChallenge Connector query ends");
		    if(debug)LOGGER.debug(requestURI + "GiveDigipassChallenge Exit from Connector started");
		    c.exit();
		    if(debug)LOGGER.debug(requestURI + "GiveDigipassChallenge Exit from Connector completed");

		    if(usr == null)
		    {
		    	LOGGER.error(requestURI + "GiveDigipassChallenge: query returned null.");
	        	Utility.ServletResponse(response, "error:TECHNICALERROR");
	 			return;
		    }

		    if(usr.getUnifiedServiceErrors() != null) return;  // errors already returned in servlet response from c.query()
  			
  			ContactcenterGiveDigipassChallenge2Output gco = (ContactcenterGiveDigipassChallenge2Output) usr.getUnifiedServiceBody().getAny().get(0);
  			if(debug)LOGGER.debug(requestURI + "GiveDigipassChallenge response body extracted");
  			customerId = gco.getGiveChallengeResponse().getCustomerId();
  			challengeCode = gco.getGiveChallengeResponse().getChallengeCode(); 
  			userName = gco.getGiveChallengeResponse().getUsername();
  			idCode = gco.getGiveChallengeResponse().getIdCode();

  			LOGGER.info(requestURI + "Answer from JMS Broker:");
  			LOGGER.info("GiveDigipassChallenge: customerId = " + customerId);
  			LOGGER.info("GiveDigipassChallenge: challengeCode = " + challengeCode);
  			LOGGER.info("GiveDigipassChallenge: userName = " + userName);
  			LOGGER.info("GiveDigipassChallenge: idCode = " + idCode);

  			if(debug)LOGGER.debug(requestURI + "GiveDigipassChallenge servlet output started");
  			Utility.ServletResponse(response, "customerId:" + customerId + "|username:" + userName +
  					  "|idCode:" + idCode + "|firstName:" + firstName + 
  					  "|lastName:" + lastName + "|userPhoneNumber:" + userPhoneNumber + 
  					  "|challengecode:" + challengeCode);
  			if(debug)LOGGER.debug(requestURI + "GiveDigipassChallenge servlet output completed");
        }
        catch (javax.jms.JMSException jmse)
        {
        	Utility.ServletResponse(response, "error:TECHNICALERROR");
        	LOGGER.error(jmse);
        }
	    catch (javax.xml.bind.JAXBException e)
	    {
	    	Utility.ServletResponse(response, "error:TECHNICALERROR");
	    	LOGGER.error(e);
	    }
        catch (java.io.IOException e)
        {
	    	Utility.ServletResponse(response, "error:TECHNICALERROR");
	    	LOGGER.error(e);
        }
	}
}
