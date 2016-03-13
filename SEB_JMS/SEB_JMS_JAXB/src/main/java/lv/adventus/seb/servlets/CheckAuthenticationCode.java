package lv.adventus.seb.servlets;

import java.io.ByteArrayInputStream;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

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
        PrintWriter out = response.getWriter();
	    response.setContentType("text/plain");
	    String dpcode = request.getParameter("digipasscode");
	    String chcode = request.getParameter("challengecode");
	    String userId = request.getParameter("userid");
	    String connId = request.getParameter("connid");

	    // check http request parameters
	    
	    if (dpcode==null || chcode==null || userId==null || connId==null)
	    {
	    	String pr = "";
	    	if(dpcode==null) pr+="digipasscode ";
	    	if(chcode==null) pr+="challengecode ";
	    	if(userId==null) pr+="userid ";
	    	if(connId==null) pr+="connid ";
	    	System.out.println("Expected parameter digipasscode not received: " + pr);
	    	out.println("result:TECHNICALERROR");
	    	return;
	    }
	    else
	    {
	    	System.out.println("CheckAuthenticationCode: digipasscode = " + dpcode);
	    	System.out.println("CheckAuthenticationCode: challengecode = " + chcode);
	    	System.out.println("CheckAuthenticationCode: userid = " + userId);
	    	System.out.println("CheckAuthenticationCode: connid = " + connId);
	    }

// check PingPong service result
		ServletContext context = getServletContext();
 		if(context.getAttribute("PingPong") == "0")
 		{
 			System.out.println("CheckAuthenticationCode: PingPong returns 0.");
 			out.println("result:TECHNICALERROR");
 			return;
 		}
	    
	    try
		{

/////////// invoke CheckAuthenticationCode service
		
  			lv.adventus.seb.CheckAuthenticationCode dc = new lv.adventus.seb.CheckAuthenticationCode();
  			dc.SetHeader();
  			dc.SetHeaderUserId(userId);
  			dc.SetHeaderRequestId(connId);
  			dc.SetBody();
  			dc.SetBody(dpcode,chcode,userId);
  			xmlrequest = dc.Marshal();
  			// print XML
			System.out.println("CheckAuthenticationCode sent this XML:");
			System.out.println(XMLUtility.prettyFormat(xmlrequest));
  			
  			c = new Connector(broker,usernameSonic,passwordSonic,queue, out, connectionTimeout);
  			c.start();
  			this.usr = c.query(xmlrequest);
		    if(this.usr == null)
		    {
		    	System.out.println("CheckAuthenticationCode: query returned null.");
	 			out.println("result:TECHNICALERROR");
	 			return;
		    }

  			if(usr.getUnifiedServiceErrors() != null) return;
  			
  			ContactcenterCheckAuthenticationCode2Output cac = (ContactcenterCheckAuthenticationCode2Output) usr.getUnifiedServiceBody().getAny().get(0);

  			this.authenticationCode = cac.getAuthenticationResponse().getAuthenticationCode();
  			this.userName = cac.getAuthenticationResponse().getUsername();
  			this.challengeCode = cac.getAuthenticationResponse().getChallengeCode();
  			System.out.println("Answer from JMS Broker:");
      	    System.out.println("CheckAuthenticationCode: digipasscode = " + this.authenticationCode);
      	    System.out.println("CheckAuthenticationCode: challengecode = " + this.challengeCode);
      	    System.out.println("CheckAuthenticationCode: username = " + this.userName);
  			
  			c.exit();
  			out.println("result:OK");
  			return;
        }
        catch (javax.jms.JMSException jmse)
        {
        	out.println("result:TECHNICALERROR");
        	System.out.println(jmse);
        	return;
        }
	    catch (javax.xml.bind.JAXBException e)
	    {
        	out.println("result:TECHNICALERROR");
        	System.out.println(e.getMessage());
        	return;
	    }
	}
}
