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
		System.out.println("CheckAuthenticationCode http request received at: " + Connector.getTimestamp());
		response.setContentType("text/html; charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    String dpcode = request.getParameter("digipasscode");
	    String chcode = request.getParameter("challengecode");
//	    String userId = request.getParameter("userid");
	    String connId = request.getParameter("connid");
	    String username = request.getParameter("username");

	    // check http request parameters
	    
	    if (dpcode==null || username==null || connId==null || chcode == null)
	    {
	    	String pr = "";
	    	if(dpcode==null) pr+="digipasscode ";
	    	if(username==null) pr+="username ";
	    	if(connId==null) pr+="connid ";
	    	if(chcode==null) pr+="challengecode ";
	    	System.out.println("Expected parameter digipasscode not received: " + pr);
	    	out.print("error:TECHNICALERROR");
	    	out.flush();
	    	return;
	    }
	    else
	    {
	    	System.out.println("CheckAuthenticationCode: digipasscode = " + dpcode);
	    	System.out.println("CheckAuthenticationCode: challengecode = " + chcode);
	    	System.out.println("CheckAuthenticationCode: username = " + username);
	    	System.out.println("CheckAuthenticationCode: connid = " + connId);
	    }

// check PingPong service result
//    	ServletContext context = request.getSession().getServletContext();
// 	 	Boolean attribute = (Boolean)context.getAttribute("PingPong"); 
// 		if(attribute.booleanValue() == false || attribute == null)
// 		{
// 			System.out.println("CheckAuthenticationCode: PingPong returns 0. Processing stopped.");
// 			out.print("error:TECHNICALERROR");
// 			return;
// 		}
// 		else
// 		{
// 			System.out.println("CheckAuthenticationCode: PingPong returns 1. Processing continues.");
// 		}

	    try
		{

/////////// invoke CheckAuthenticationCode service
		
	    	System.out.println("CheckAuthenticationCode processing started at: " + Connector.getTimestamp());
  			lv.adventus.seb.CheckAuthenticationCode dc = new lv.adventus.seb.CheckAuthenticationCode();
  			dc.SetHeader();
  			dc.SetHeaderUserId(username);
  			dc.SetHeaderRequestId(connId + "2");
  			dc.SetBody();
  			dc.SetBody(dpcode,chcode,username);
  			xmlrequest = dc.Marshal();
  			// print XML
			System.out.println("CheckAuthenticationCode sent this XML:");
			System.out.println(XMLUtility.prettyFormat(xmlrequest));
  			
			System.out.println("CheckAuthenticationCode creating Connector at: " + Connector.getTimestamp());
  			c = new Connector(broker,usernameSonic,passwordSonic,queue, response, connectionTimeout);
  			c.SetHeader(dc.GetHeader());
  			System.out.println("CheckAuthenticationCode starting Connector at: " + Connector.getTimestamp());
  			c.start();
  			c.createMessage();
  			System.out.println("CheckAuthenticationCode Connector query begins at: " + Connector.getTimestamp());
  			this.usr = c.query(xmlrequest);
  			System.out.println("CheckAuthenticationCode Connector query ends at: " + Connector.getTimestamp());
		    if(this.usr == null)
		    {
		    	System.out.println("CheckAuthenticationCode: query returned null.");
	 			out.print("error:TECHNICALERROR");
	 			out.flush();
	 			return;
		    }

  			if(usr.getUnifiedServiceErrors() != null) return;
  			System.out.println("CheckAuthenticationCode parsing response started at: " + Connector.getTimestamp());
  			ContactcenterCheckAuthenticationCode2Output cac = (ContactcenterCheckAuthenticationCode2Output) usr.getUnifiedServiceBody().getAny().get(0);

  			this.authenticationCode = cac.getAuthenticationResponse().getAuthenticationCode();
  			this.userName = cac.getAuthenticationResponse().getUsername();
  			this.challengeCode = cac.getAuthenticationResponse().getChallengeCode();
  			System.out.println("Answer from JMS Broker:");
      	    System.out.println("CheckAuthenticationCode: digipasscode = " + this.authenticationCode);
      	    System.out.println("CheckAuthenticationCode: challengecode = " + this.challengeCode);
      	    System.out.println("CheckAuthenticationCode: username = " + this.userName);
      	    System.out.println("CheckAuthenticationCode parsing response completed at: " + Connector.getTimestamp());
      	    System.out.println("CheckAuthenticationCode Exit from Connector started at: " + Connector.getTimestamp());
  			c.exit();
  			System.out.println("CheckAuthenticationCode Exit from Connector completed at: " + Connector.getTimestamp());
  			System.out.println("CheckAuthenticationCode servlet output started at: " + Connector.getTimestamp());
  			out.print("result:OK");
  			System.out.println("CheckAuthenticationCode servlet output completed at: " + Connector.getTimestamp());
  			out.flush();
  			System.out.println("CheckAuthenticationCode servlet output flushed at: " + Connector.getTimestamp());
        }
        catch (javax.jms.JMSException jmse)
        {
        	out.print("error:TECHNICALERROR");
        	out.flush();
        	System.out.println(jmse);
        }
	    catch (javax.xml.bind.JAXBException e)
	    {
        	out.print("error:TECHNICALERROR");
        	out.flush();
        	System.out.println(e.getMessage());
	    }
	}
}
