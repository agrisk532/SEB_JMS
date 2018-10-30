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
import lv.adventus.seb.util.Utility;
import lv.adventus.seb.UnifiedServiceResponse;
import lv.adventus.seb.UnifiedServiceErrors;
import lv.adventus.seb.ContactcenterCheckAuthenticationCode2Output;


/**
 * Servlet implementation class CheckAuthenticationCode
 */
public class CheckAuthenticationCode extends ServletBase {
	
	static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(CheckAuthenticationCode.class);
    /**
     * Default constructor. 
     */
    public CheckAuthenticationCode() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String xmlrequest;
		String challengeCode;
		String userName;
		String digipassCode;
		String connectionId;

		Connector c;
		UnifiedServiceResponse usr;

	    String requestURI = request.getRequestURI().substring(1);	// remove slash
	    LOGGER.info(requestURI + " HTTP request received");
	    
		// set all response headers here
		response.setContentType("text/plain; charset=UTF-8");  // this must be set before response.getWriter()
		response.setHeader("Cache-Control", "no-cache");

	    digipassCode = request.getParameter("digipasscode");
	    if(digipassCode == null)
	    	digipassCode = request.getParameter("digipassCode");
	    
	    challengeCode = request.getParameter("challengecode");
	    if(challengeCode == null)
	    	challengeCode = request.getParameter("challengeCode");
	    
	    connectionId = request.getParameter("connid");
	    userName = request.getParameter("username");

	    // check HTTP GET request parameters

	    if (digipassCode==null || challengeCode == null || connectionId==null || userName==null)
	    {
	    	String pr = "";
	    	if(digipassCode==null)  pr += "digipasscode ";
	    	if(challengeCode==null) pr += "challengecode ";
	    	if(connectionId==null)  pr += "connid ";
	    	if(userName==null) 		pr += "username ";

	    	LOGGER.error(requestURI + " Expected parameter not received from HTTP GET request: " + pr + ". Processing stopped.");
        	Utility.ServletResponse(response, "error:TECHNICALERROR");
        	return;
	    }
	    else
	    {
	    	LOGGER.info(requestURI + " received HTTP GET parameters:");
	    	LOGGER.info("digipasscode = " + digipassCode);
	    	LOGGER.info("challengecode = " + challengeCode);
	    	LOGGER.info("connid = " + connectionId);
	    	LOGGER.info("username = " + userName);
	    }

		 // check PingPong service result

	    if(Utility.CheckPingPongStatus(request, response, requestURI) == false)
	    {
 			Utility.ServletResponse(response,"error:TECHNICALERROR");
	    	return;
	    }
	    
	 // PingPong ok, we continue
	    
	    try
		{

/////////// invoke CheckAuthenticationCode service
		
	    	LOGGER.info(requestURI + " processing started");
  			lv.adventus.seb.CheckAuthenticationCode dc = new lv.adventus.seb.CheckAuthenticationCode();
  			dc.SetHeader();
  			dc.SetHeaderUserId(userName);
  			dc.SetHeaderRequestId(connectionId + "2");
  			dc.SetHeaderCountryCode(countryCode);
  			dc.SetHeaderLanguage(language);
  			dc.SetBody();
  			dc.SetBody(digipassCode,challengeCode,userName);
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
  			LOGGER.debug(requestURI + " Connector query ends");
  			LOGGER.debug(requestURI + " exit from Connector started");
  			c.exit();
  			LOGGER.debug(requestURI + " exit from Connector completed");

		    if(usr == null)
		    {
		    	LOGGER.error(requestURI + ": JMS UnifiedServiceResponse is null. Processing stopped.");
	        	Utility.ServletResponse(response, "error:TECHNICALERROR");
	 			return;
		    }

  			if(usr.getUnifiedServiceErrors() != null) return; // errors already returned in servlet response from c.query()

  			ContactcenterCheckAuthenticationCode2Output cac = (ContactcenterCheckAuthenticationCode2Output) usr.getUnifiedServiceBody().getAny().get(0);
  			LOGGER.debug(requestURI + " UnifiedServiceResponse body extracted");
  			digipassCode = cac.getAuthenticationResponse().getAuthenticationCode();
  			userName = cac.getAuthenticationResponse().getUsername();
  			challengeCode = cac.getAuthenticationResponse().getChallengeCode();

  			LOGGER.info(requestURI + ": Answer from JMS Broker:");
      	    LOGGER.info("digipasscode = " + digipassCode);
      	    LOGGER.info("challengecode = " + challengeCode);
      	    LOGGER.info("userName = " + userName);

      	    LOGGER.debug(requestURI + " servlet output started");
      	    Utility.ServletResponse(response, "result:OK");
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
	}
}
