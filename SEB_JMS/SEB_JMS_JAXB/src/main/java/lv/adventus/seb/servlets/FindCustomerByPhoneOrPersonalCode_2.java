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
		System.out.println(requestURI + " http request received at: " + Connector.getTimestamp());

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
	    	System.out.println(requestURI + ": Expected parameter not received from HTTP GET request: " + pr);
        	Utility.ServletResponse(response, "error:TECHNICALERROR");
        	return;
	    }
	    else
	    {
	    	System.out.println(requestURI + ": idCode = " + userId);
	    	System.out.println(requestURI + ": connid = " + connId);
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

	    	System.out.println(requestURI + " processing started at: " + Connector.getTimestamp());
			lv.adventus.seb.FindCustomerByPhoneOrPersonalCode fc = new lv.adventus.seb.FindCustomerByPhoneOrPersonalCode();
			fc.SetHeader();
			fc.SetHeaderUserId(userId);
			fc.SetHeaderRequestId(connId);
			fc.SetBody();
			fc.SetBody(userId, "");
			xmlrequest = fc.Marshal();
  			// print XML
			if(debug)System.out.println(requestURI + " sent this XML:");
			if(debug)System.out.println(XMLUtility.prettyFormat(xmlrequest));

			c = new Connector(broker,usernameSonic,passwordSonic,queue, response, connectionTimeout, ttl, responseMsgTTL, false);
			if(debug)System.out.println(requestURI + " created Connector at: " + Connector.getTimestamp());
			c.SetHeader(fc.GetHeader());
			c.start();
			if(debug)System.out.println(requestURI + " started Connector at: " + Connector.getTimestamp());
			c.createMessage();
			if(debug)System.out.println(requestURI + " Connector query begins at: " + Connector.getTimestamp());
		    usr = c.query(xmlrequest);
		    if(debug)System.out.println(requestURI + " Connector query ends at: " + Connector.getTimestamp());
		    if(debug)System.out.println(requestURI + " Exit from Connector started at: " + Connector.getTimestamp());
	  		c.exit();
	  		if(debug)System.out.println(requestURI + " Exit from Connector completed at: " + Connector.getTimestamp());

		    if(usr == null)
		    {
		    	System.out.println(requestURI + ": query returned null.");
		    	Utility.ServletResponse(response, "error:TECHNICALERROR");
	 			return;
		    }
		    if(usr.getUnifiedServiceErrors() != null) return;  // errors already returned in c.query()

			ContactcenterFindCustomerByPhoneOrPersonalCode2Output fco =
			    	  (ContactcenterFindCustomerByPhoneOrPersonalCode2Output) usr.getUnifiedServiceBody().getAny().get(0);
			if(debug)System.out.println(requestURI + " response body extracted at: " + Connector.getTimestamp());
		    customerId = fco.getFindCustomerResponse().getCustomerId();
		    idCode = fco.getFindCustomerResponse().getIdCode();
		    firstName = fco.getFindCustomerResponse().getFirstName();
		    lastName = fco.getFindCustomerResponse().getLastName();
		    userPhoneNumber = fco.getFindCustomerResponse().getUserPhoneNumber();

		    System.out.println(requestURI + ": Answer from JMS Broker:");
		    System.out.println("customerId = " + customerId);
		    System.out.println("idCode = " + idCode);
		    System.out.println("firstName = " + firstName);
		    System.out.println("lastName = " + lastName);
		    System.out.println("userPhoneNumber = " + userPhoneNumber);
		    
		    if(debug)System.out.println(requestURI + " servlet output started at: " + Connector.getTimestamp());
  			Utility.ServletResponse(response, "customerId:" + customerId + 
  					  "|idCode:" + idCode + "|firstName:" + firstName + 
  					  "|lastName:" + lastName + "|userPhoneNumber:" + userPhoneNumber); 
  			if(debug)System.out.println(requestURI + " servlet output completed at: " + Connector.getTimestamp());
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
