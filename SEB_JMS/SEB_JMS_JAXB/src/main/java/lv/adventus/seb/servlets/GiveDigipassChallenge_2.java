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
		System.out.println(requestURI + " http request received at: " + Connector.getTimestamp());

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
	    	System.out.println(requestURI + ": Expected parameter not received from HTTP GET request: " + pr);
        	Utility.ServletResponse(response, "error:TECHNICALERROR");
        	return;
	    }
	    else
	    {
	    	System.out.println(requestURI + ": idCode = " + idCode);
	    	System.out.println(requestURI + ": connid = " + connId);
	    	System.out.println(requestURI + ": customerId = " + customerId);
	    }

	    try
		{
	    	
/////////// invoke GiveDigipassChallenge service

		    if(Utility.CheckPingPongStatus(request, response, requestURI) == false)
		    {
	 			Utility.ServletResponse(response,"error:TECHNICALERROR");
		    	return;
		    }
		    
	    	System.out.println(requestURI + " processing started at: " + Connector.getTimestamp());
  			lv.adventus.seb.GiveDigipassChallenge dc = new lv.adventus.seb.GiveDigipassChallenge();
  			dc.SetHeader();
  			dc.SetHeaderUserId(customerId);
  			dc.SetHeaderRequestId(connId + "1");  // to make requestId unique
  			dc.SetBody();
  	 		dc.SetBody(customerId, idCode);
  			xmlrequest = dc.Marshal();
  			// print XML
			System.out.println(requestURI + " sent this XML:");
			System.out.println(XMLUtility.prettyFormat(xmlrequest));
  			
			c = new Connector(broker,usernameSonic,passwordSonic,queue, response, connectionTimeout, ttl, responseMsgTTL);
			System.out.println(requestURI + " created Connector at: " + Connector.getTimestamp());
			c.SetHeader(dc.GetHeader());
			c.start();
			System.out.println(requestURI + " started Connector at: " + Connector.getTimestamp());
			c.createMessage();
			System.out.println(requestURI + " Connector query begins at: " + Connector.getTimestamp());
		    usr = c.query(xmlrequest);
		    System.out.println(requestURI + " Connector query ends at: " + Connector.getTimestamp());
  			System.out.println(requestURI + " Exit from Connector started at: " + Connector.getTimestamp());
		    c.exit();
	  		System.out.println(requestURI + " Exit from Connector completed at: " + Connector.getTimestamp());

		    if(usr == null)
		    {
		    	System.out.println(requestURI + ": query returned null.");
	        	Utility.ServletResponse(response, "error:TECHNICALERROR");
	 			return;
		    }

		    if(usr.getUnifiedServiceErrors() != null) return;  // errors already returned in servlet response from c.query()
  			
  			ContactcenterGiveDigipassChallenge2Output gco = (ContactcenterGiveDigipassChallenge2Output) usr.getUnifiedServiceBody().getAny().get(0);
  			System.out.println(requestURI + " response body extracted at: " + Connector.getTimestamp());
  			customerId = gco.getGiveChallengeResponse().getCustomerId();
  			challengeCode = gco.getGiveChallengeResponse().getChallengeCode(); 
  			userName = gco.getGiveChallengeResponse().getUsername();
  			idCode = gco.getGiveChallengeResponse().getIdCode();

  			System.out.println(requestURI + ": Answer from JMS Broker:");
  			System.out.println(requestURI + ": customerId = " + customerId);
  			System.out.println(requestURI + ": challengeCode = " + challengeCode);
  			System.out.println(requestURI + ": userName = " + userName);
  			System.out.println(requestURI + ": idCode = " + idCode);

  			System.out.println(requestURI + " servlet output started at: " + Connector.getTimestamp());
  			Utility.ServletResponse(response, "customerId:" + customerId + "|username:" + userName +
  					  "|idCode:" + idCode +  
  					  "|challengeCode:" + challengeCode);
  			System.out.println(requestURI + " servlet output completed at: " + Connector.getTimestamp());
        }
        catch (javax.jms.JMSException jmse)
        {
        	Utility.ServletResponse(response, "error:TECHNICALERROR");
        	System.out.println(jmse.getMessage());
        	sc.log(requestURI + " servlet JMSException: " + jmse.getMessage(), jmse);
        }
	    catch (javax.xml.bind.JAXBException e)
	    {
	    	Utility.ServletResponse(response, "error:TECHNICALERROR");
	    	System.out.println(e.getMessage());
	    	sc.log(requestURI + " servlet JAXBException: " + e.getMessage(), e);
	    }
        catch (java.io.IOException e)
        {
	    	Utility.ServletResponse(response, "error:TECHNICALERROR");
	    	System.out.println(e.getMessage());
	    	sc.log(requestURI + " servlet IOException: " + e.getMessage(), e);
        }
	}
}
