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
		LOGGER.info(requestURI + " HTTP request received");

		// set all response headers here
		response.setContentType("text/plain; charset=UTF-8");  // this must be set before response.getWriter()
		response.setHeader("Cache-Control", "no-cache");

	    //PrintWriter out = response.getWriter(); // will be done later 

	    idCode = request.getParameter("idCode");
	    connId = request.getParameter("connid");
	    customerId = request.getParameter("customerId");

	    // check http request parameters

	    if (idCode==null || connId==null || customerId==null)
	    {
	    	String pr = "";
	    	if(idCode == null) pr += "idCode ";
	    	if(connId == null) pr += "connId ";
	    	if(customerId == null) pr += "customerId ";
	    	LOGGER.error(requestURI + ": Expected parameter not received from HTTP GET request: " + pr + ". Processing stopped.");
        	Utility.ServletResponse(response, "error:TECHNICALERROR");
        	return;
	    }
	    else
	    {
	    	LOGGER.info(requestURI + " received HTTP GET parameters:");
	    	LOGGER.info("idCode = " + idCode);
	    	LOGGER.info("connid = " + connId);
	    	LOGGER.info("customerId = " + customerId);
	    	connId = "cc" + connId + "1";	  // to make requestId unique
	    }

	    try
		{
	    	
/////////// invoke GiveDigipassChallenge service

		    if(Utility.CheckPingPongStatus(request, response, requestURI) == false)
		    {
	 			Utility.ServletResponse(response,"error:TECHNICALERROR");
		    	return;
		    }
		    
	    	LOGGER.info(requestURI + " processing started");
  			lv.adventus.seb.GiveDigipassChallenge dc = new lv.adventus.seb.GiveDigipassChallenge();
  			dc.SetHeader();
  			dc.SetHeaderUserId(customerId);
  			dc.SetHeaderRequestId(connId);
  			dc.SetHeaderCountryCode(countryCode);
  			dc.SetHeaderLanguage(language);
  			dc.SetBody();
  	 		dc.SetBody(customerId, idCode);
  			xmlrequest = dc.Marshal();
  			// print XML
  			LOGGER.debug(requestURI + " sent this XML:");
  			LOGGER.debug(XMLUtility.prettyFormat(xmlrequest));
  			
			c = new Connector(broker,usernameSonic,passwordSonic,queue, response, connectionTimeout, ttl, responseMsgTTL);
			LOGGER.debug(requestURI + " created Connector");
			c.SetHeader(dc.GetHeader());
			c.start();
			LOGGER.debug(requestURI + " started Connector");
			c.createMessage();
			LOGGER.debug(requestURI + " Connector query begins");
		    usr = c.query(xmlrequest);
		    if(usr != null)
		    {
		    	LOGGER.debug(requestURI + " Connector query ends");
		    	LOGGER.debug(requestURI + " Exit from Connector started");
		    	c.exit();
		    	LOGGER.debug(requestURI + " Exit from Connector completed");
		    }
		    if(usr == null)
		    {
		    	LOGGER.error(requestURI + ": JMS UnifiedServiceResponse is null. Processing stopped.");
	        	Utility.ServletResponse(response, "error:TECHNICALERROR");
	 			return;
		    }

		    if(usr.getUnifiedServiceErrors() != null) return;  // errors already returned in servlet response from c.query()
  			
  			ContactcenterGiveDigipassChallenge2Output gco = (ContactcenterGiveDigipassChallenge2Output) usr.getUnifiedServiceBody().getAny().get(0);
  			LOGGER.debug(requestURI + " response body extracted");
  			customerId = gco.getGiveChallengeResponse().getCustomerId();
  			challengeCode = gco.getGiveChallengeResponse().getChallengeCode(); 
  			userName = gco.getGiveChallengeResponse().getUsername();
  			idCode = gco.getGiveChallengeResponse().getIdCode();

  			LOGGER.info(requestURI + "Answer from JMS Broker:");
  			LOGGER.info("customerId = " + customerId);
  			LOGGER.info("challengeCode = " + challengeCode);
  			LOGGER.info("userName = " + userName);
  			LOGGER.info("idCode = " + idCode);

  			LOGGER.debug(requestURI + " servlet output started");
  			Utility.ServletResponse(response, "customerId:" + customerId + "|username:" + userName +
  					  "|idCode:" + idCode +  
  					  "|challengeCode:" + challengeCode);
  			LOGGER.debug(requestURI + " servlet output completed");
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
