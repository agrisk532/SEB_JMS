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
 * Servlet implementation class GiveDigipassChallenge_2
 */
public class GiveDigipassChallenge_2 extends ServletBase {
	
	static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(GiveDigipassChallenge_2.class);
    /**
     * Default constructor. 
     */
    public GiveDigipassChallenge_2() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String xmlrequest;
		String customerId;
		String connId;
		String idCode;
		String challengeCode;
		String userName;

		UnifiedServiceResponse usr;
		Connector c;

		String requestURI = request.getRequestURI().substring(1);	// remove slash
		LOGGER.info(requestURI + " http request received");

		// set all response headers here
		response.setContentType("text/plain; charset=UTF-8");  // this must be set before response.getWriter()
		response.setHeader("Cache-Control", "no-cache");

	    //PrintWriter out = response.getWriter(); // will be done later 

	    idCode = request.getParameter("idCode");
	    connId = "cc" + request.getParameter("connid");
	    customerId = request.getParameter("customerId");

	    // check http request parameters

	    if (idCode==null || connId==null || customerId==null)
	    {
	    	String pr = "";
	    	if(idCode == null) pr += "idCode ";
	    	if(connId == null) pr += "connId ";
	    	if(customerId == null) pr += "customerId ";
	    	LOGGER.error(requestURI + ": Expected parameter not received from HTTP GET request: " + pr);
        	Utility.ServletResponse(response, "error:TECHNICALERROR");
        	return;
	    }
	    else
	    {
	    	LOGGER.info(requestURI + ": idCode = " + idCode);
	    	LOGGER.info(requestURI + ": connid = " + connId);
	    	LOGGER.info(requestURI + ": customerId = " + customerId);
	    }

	    try
		{
	    	
/////////// invoke GiveDigipassChallenge service

		    if(Utility.CheckPingPongStatus(request, response, requestURI) == false)
		    {
	 			Utility.ServletResponse(response,"error:TECHNICALERROR");
		    	return;
		    }
		    
	    	LOGGER.info(requestURI + " processing started at: " + Connector.getTimestamp());
  			lv.adventus.seb.GiveDigipassChallenge dc = new lv.adventus.seb.GiveDigipassChallenge();
  			dc.SetHeader();
  			dc.SetHeaderUserId(customerId);
  			dc.SetHeaderRequestId(connId + "1");  // to make requestId unique
  			dc.SetBody();
  	 		dc.SetBody(customerId, idCode);
  			xmlrequest = dc.Marshal();
  			// print XML
  			if(debug)LOGGER.debug(requestURI + " sent this XML:");
  			if(debug)LOGGER.debug(XMLUtility.prettyFormat(xmlrequest));
  			
			c = new Connector(broker,usernameSonic,passwordSonic,queue, response, connectionTimeout, ttl, responseMsgTTL, false);
			if(debug)LOGGER.debug(requestURI + " created Connector");
			c.SetHeader(dc.GetHeader());
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
		    	LOGGER.error(requestURI + ": query returned null.");
	        	Utility.ServletResponse(response, "error:TECHNICALERROR");
	 			return;
		    }

		    if(usr.getUnifiedServiceErrors() != null) return;  // errors already returned in servlet response from c.query()
  			
  			ContactcenterGiveDigipassChallenge2Output gco = (ContactcenterGiveDigipassChallenge2Output) usr.getUnifiedServiceBody().getAny().get(0);
  			if(debug)LOGGER.debug(requestURI + " response body extracted");
  			customerId = gco.getGiveChallengeResponse().getCustomerId();
  			challengeCode = gco.getGiveChallengeResponse().getChallengeCode(); 
  			userName = gco.getGiveChallengeResponse().getUsername();
  			idCode = gco.getGiveChallengeResponse().getIdCode();

  			LOGGER.info(requestURI + ": Answer from JMS Broker:");
  			LOGGER.info(requestURI + ": customerId = " + customerId);
  			LOGGER.info(requestURI + ": challengeCode = " + challengeCode);
  			LOGGER.info(requestURI + ": userName = " + userName);
  			LOGGER.info(requestURI + ": idCode = " + idCode);

  			if(debug)LOGGER.debug(requestURI + " servlet output started");
  			Utility.ServletResponse(response, "customerId:" + customerId + "|username:" + userName +
  					  "|idCode:" + idCode +  
  					  "|challengeCode:" + challengeCode);
  			if(debug)LOGGER.debug(requestURI + " servlet output completed");
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
