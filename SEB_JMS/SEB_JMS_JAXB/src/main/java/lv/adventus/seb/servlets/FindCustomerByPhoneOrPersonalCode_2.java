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
 * Servlet implementation class FindCustomerByPhoneOrPersonalCode_2
 */
public class FindCustomerByPhoneOrPersonalCode_2 extends ServletBase {
	

	static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(FindCustomerByPhoneOrPersonalCode_2.class);
    /**
     * Default constructor. 
     */
    public FindCustomerByPhoneOrPersonalCode_2() {
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

		UnifiedServiceResponse usr;
		Connector c;

	    String requestURI = request.getRequestURI().substring(1);	// remove slash
		LOGGER.info(requestURI + " HTTP request received");

		// set all response headers here
		response.setContentType("text/plain; charset=UTF-8");  // this must be set before response.getWriter()
		response.setHeader("Cache-Control", "no-cache");

	    //PrintWriter out = response.getWriter(); // will be done later 

	    String userId = request.getParameter("idCode");
	    String connId = "cc" + request.getParameter("connid");

	    // check http request parameters

	    if (userId==null || connId==null)
	    {
	    	String pr = "";
	    	if(userId == null) pr += "idCode ";
	    	if(connId == null) pr += "connId ";
	    	LOGGER.error(requestURI + ": Expected parameter not received from HTTP GET request: " + pr + ". Processing stopped.");
        	Utility.ServletResponse(response, "error:TECHNICALERROR");
        	return;
	    }
	    else
	    {
	    	LOGGER.info(requestURI + " received HTTP GET parameters:");
	    	LOGGER.info("idCode = " + userId);
	    	LOGGER.info("connid = " + connId);
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
	    	
/////////// invoke FindCustomerByPhoneOrPersonalCode_2 service

	    	LOGGER.info(requestURI + " processing started");
			lv.adventus.seb.FindCustomerByPhoneOrPersonalCode fc = new lv.adventus.seb.FindCustomerByPhoneOrPersonalCode();
			fc.SetHeader();
			fc.SetHeaderUserId(userId);
			fc.SetHeaderRequestId(connId);
			fc.SetBody();
			fc.SetBody(userId, "");
			xmlrequest = fc.Marshal();
  			// print XML
			LOGGER.debug(requestURI + " sent this XML:");
			LOGGER.debug(XMLUtility.prettyFormat(xmlrequest));

			c = new Connector(broker,usernameSonic,passwordSonic,queue, response, connectionTimeout, ttl, responseMsgTTL);
			LOGGER.debug(requestURI + " created Connector");
			c.SetHeader(fc.GetHeader());
			c.start();
			LOGGER.debug(requestURI + " started Connector");
			c.createMessage();
			LOGGER.debug(requestURI + " Connector query begins");
		    usr = c.query(xmlrequest);
		    LOGGER.debug(requestURI + " Connector query ends");
		    LOGGER.debug(requestURI + " Exit from Connector started");
	  		c.exit();
	  		LOGGER.debug(requestURI + " Exit from Connector completed");

		    if(usr == null)
		    {
		    	LOGGER.error(requestURI + ": JMS UnifiedServiceResponse is null. Processing stopped.");
		    	Utility.ServletResponse(response, "error:TECHNICALERROR");
	 			return;
		    }
		    if(usr.getUnifiedServiceErrors() != null) return;  // errors already returned in c.query()

			ContactcenterFindCustomerByPhoneOrPersonalCode2Output fco =
			    	  (ContactcenterFindCustomerByPhoneOrPersonalCode2Output) usr.getUnifiedServiceBody().getAny().get(0);
			LOGGER.debug(requestURI + " UnifiedServiceResponse body extracted");
		    customerId = fco.getFindCustomerResponse().getCustomerId();
		    idCode = fco.getFindCustomerResponse().getIdCode();
		    firstName = fco.getFindCustomerResponse().getFirstName();
		    lastName = fco.getFindCustomerResponse().getLastName();
		    userPhoneNumber = fco.getFindCustomerResponse().getUserPhoneNumber();

		    LOGGER.info(requestURI + ": Answer from JMS Broker:");
		    LOGGER.info("customerId = " + customerId);
		    LOGGER.info("idCode = " + idCode);
		    LOGGER.info("firstName = " + firstName);
		    LOGGER.info("lastName = " + lastName);
		    LOGGER.info("userPhoneNumber = " + userPhoneNumber);
		    
		    LOGGER.debug(requestURI + " servlet output started");
  			Utility.ServletResponse(response, "customerId:" + customerId + 
  					  "|idCode:" + idCode + "|firstName:" + firstName + 
  					  "|lastName:" + lastName + "|userPhoneNumber:" + userPhoneNumber); 
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
