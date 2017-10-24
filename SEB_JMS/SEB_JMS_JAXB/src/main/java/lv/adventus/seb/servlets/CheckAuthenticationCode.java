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
	    System.out.println(requestURI + " http request received at: " + Connector.getTimestamp());
	    
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

	    	System.out.println(requestURI + " expected parameter not received from HTTP GET request: " + pr);
        	Utility.ServletResponse(response, "error:TECHNICALERROR");
        	return;
	    }
	    else
	    {
	    	System.out.println(requestURI + " received HTTP GET parameters:");
	    	System.out.println("digipasscode = " + digipassCode);
	    	System.out.println("challengecode = " + challengeCode);
	    	System.out.println("connid = " + connectionId);
	    	System.out.println("username = " + userName);
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
		
	    	System.out.println(requestURI + " processing started at: " + Connector.getTimestamp());
  			lv.adventus.seb.CheckAuthenticationCode dc = new lv.adventus.seb.CheckAuthenticationCode();
  			dc.SetHeader();
  			dc.SetHeaderUserId(userName);
  			dc.SetHeaderRequestId(connectionId + "2");
  			dc.SetBody();
  			dc.SetBody(digipassCode,challengeCode,userName);
  			xmlrequest = dc.Marshal();
  			// print XML
			System.out.println(requestURI + " sent this XML:");
			System.out.println(XMLUtility.prettyFormat(xmlrequest));
  			
			c = new Connector(broker,usernameSonic,passwordSonic,queue, response, connectionTimeout, ttl, responseMsgTTL, false);
			System.out.println(requestURI + " created Connector at: " + Connector.getTimestamp());
  			c.SetHeader(dc.GetHeader());
  			c.start();
  			System.out.println(requestURI + " started Connector at: " + Connector.getTimestamp());
  			c.createMessage();
  			System.out.println(requestURI + " Connector query begins at: " + Connector.getTimestamp());
  			usr = c.query(xmlrequest);
  			System.out.println(requestURI + " Connector query ends at: " + Connector.getTimestamp());
  			System.out.println(requestURI + " exit from Connector started at: " + Connector.getTimestamp());
  			c.exit();
  			System.out.println(requestURI + " exit from Connector completed at: " + Connector.getTimestamp());

		    if(usr == null)
		    {
		    	System.out.println(requestURI + ": query returned null.");
	        	Utility.ServletResponse(response, "error:TECHNICALERROR");
	 			return;
		    }

  			if(usr.getUnifiedServiceErrors() != null) return; // errors already returned in servlet response from c.query()

  			ContactcenterCheckAuthenticationCode2Output cac = (ContactcenterCheckAuthenticationCode2Output) usr.getUnifiedServiceBody().getAny().get(0);
  			System.out.println(requestURI + " response body extracted at: " + Connector.getTimestamp());
  			digipassCode = cac.getAuthenticationResponse().getAuthenticationCode();
  			userName = cac.getAuthenticationResponse().getUsername();
  			challengeCode = cac.getAuthenticationResponse().getChallengeCode();

  			System.out.println(requestURI + ": answer from JMS Broker:");
      	    System.out.println(requestURI + ": digipasscode = " + digipassCode);
      	    System.out.println(requestURI + ": challengecode = " + challengeCode);
      	    System.out.println(requestURI + ": userName = " + userName);

      	    System.out.println(requestURI + " servlet output started at: " + Connector.getTimestamp());
      	    Utility.ServletResponse(response, "result:OK");
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
	}
}
