package lv.adventus.seb.servlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
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
import lv.adventus.seb.UnifiedServiceResponse;
import lv.adventus.seb.UnifiedServiceErrors;
import lv.adventus.seb.ContactcenterFindCustomerByPhoneOrPersonalCode2Output;
import lv.adventus.seb.ContactcenterGiveDigipassChallenge2Output;

/**
 * Servlet implementation class FindCustomerByPhoneOrPersonalCode
 */
public class FindCustomerByPhoneOrPersonalCode extends HttpServlet {
	
    private static final String broker = "awebs:6526";
    private static final String usernameSonic = "tester1";
    private static final String passwordSonic = "tester";
    private static final String queue = "SEB_SERVICES";
    private static final long timeout = 60000;
	private String xmlrequest;
	private String xmlresponse;
	private String customerId;
	private String idCode;
	private String challengeCode;
	private String userName;
	private UnifiedServiceResponse usr;
	private Connector c;


    /**
     * Default constructor. 
     */
    public FindCustomerByPhoneOrPersonalCode() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
        PrintWriter out = response.getWriter();
	    response.setContentType("text/plain");
	    String userId = request.getParameter("id");
	    String connId = request.getParameter("connid");
	    
	    // check http request parameters
	    
	    if (userId==null)
	    {
	    	System.out.println("Parameter id not received");
	    	out.println("TECHNICALERROR");
	    	return;
	    }
	    else
	    {
	    	System.out.println("FindCustomerByPhoneOrPersonalCode: id = " + userId);
	    }
			
	    if (connId==null)
	    {
	    	System.out.println("Parameter connid not received");
	    	out.println("TECHNICALERROR");
	    	return;
	    }
	    else
	    {
	    	System.out.println("FindCustomerByPhoneOrPersonalCode: connid = " + connId);
	    }
	    
	    try
		{
	    	
/////////// invoke FindCustomerByPhoneOrPersonalCode service
	    	
			lv.adventus.seb.FindCustomerByPhoneOrPersonalCode fc = new lv.adventus.seb.FindCustomerByPhoneOrPersonalCode();
			fc.SetHeader();
			fc.SetHeaderUserId(userId);
			fc.SetHeaderRequestId(connId);
			fc.SetBody();
			fc.SetBody(userId, "");
			xmlrequest = fc.Marshal();

			c = new Connector(broker,usernameSonic,passwordSonic,queue, out, timeout);
			c.start();
		    this.usr = c.query(xmlrequest);
		    if(usr.getUnifiedServiceErrors() != null) return;
	
			ContactcenterFindCustomerByPhoneOrPersonalCode2Output fco =
		    	  (ContactcenterFindCustomerByPhoneOrPersonalCode2Output) usr.getUnifiedServiceBody().getAny().get(0);
		    this.customerId = fco.getFindCustomerResponse().getCustomerId();
		    this.idCode = fco.getFindCustomerResponse().getIdCode();
		    System.out.println(this.customerId);
		    System.out.println(this.idCode);
	  		c.exit();

/////////// invoke GiveDigipassChallenge service
		
  			lv.adventus.seb.GiveDigipassChallenge dc = new lv.adventus.seb.GiveDigipassChallenge();
  			dc.SetHeader();
  			dc.SetHeaderUserId(userId);
  			dc.SetHeaderRequestId(connId);
  			dc.SetBody();
  			dc.SetBody(this.customerId, this.idCode);
  			xmlrequest = dc.Marshal();

  			c = new Connector(broker,usernameSonic,passwordSonic,queue, out, timeout);
  			c.start();
  			this.usr = c.query(xmlrequest);
  			if(usr.getUnifiedServiceErrors() != null) return;
  			
  			ContactcenterGiveDigipassChallenge2Output gco = (ContactcenterGiveDigipassChallenge2Output) usr.getUnifiedServiceBody().getAny().get(0);
  			this.customerId = gco.getGiveChallengeResponse().getCustomerId();
  			this.challengeCode = gco.getGiveChallengeResponse().getChallengeCode(); 
  			this.userName = gco.getGiveChallengeResponse().getUsername();
  			this.idCode = gco.getGiveChallengeResponse().getIdCode();
  			System.out.println(this.customerId);
  			System.out.println(this.challengeCode);
  			System.out.println(this.userName);
  			System.out.println(this.idCode);
  			c.exit();
  			out.println("<"+this.challengeCode+">");
  			return;
        }
        catch (javax.jms.JMSException jmse)
        {
        	out.println("TECHNICALERROR");
        	System.out.println(jmse);
        	return;
        }
	    catch (javax.xml.bind.JAXBException e)
	    {
        	out.println("TECHNICALERROR");
        	System.out.println(e.getMessage());
        	return;
	    }
	}
}
