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
			lv.adventus.seb.FindCustomerByPhoneOrPersonalCode fc = new lv.adventus.seb.FindCustomerByPhoneOrPersonalCode();
			fc.SetHeader();
			fc.SetHeaderUserId(userId);
			fc.SetHeaderRequestId(connId);
			fc.SetBody();
			fc.SetBody(userId, "");
			xmlrequest = fc.Marshal();
		}
		catch (JAXBException e)
		{
			System.out.println(e.getMessage());
	    	out.println("TECHNICALERROR");
	    	return;
		}
		
		Connector c = new Connector(broker,usernameSonic,passwordSonic,queue, out, timeout);
		c.start();
	    //start(broker,username,password,queue, out);
	    try
        {
        	progress.message.jclient.XMLMessage msg = ((progress.message.jclient.Session) c.getSession()).createXMLMessage();
			//javax.jms.TextMessage msg = session.createTextMessage();
			msg.setText( xmlrequest );
			// Instead of sending, we will use the QueueRequestor.
			javax.jms.Message responseMsg = c.getRequestor().request(msg, timeout);
			if(responseMsg == null)
			{
				throw new javax.jms.JMSException("No response from JMS broker");
			}
			MultipartMessageUtility mmu = new MultipartMessageUtility(c); 
			mmu.onMessage(responseMsg);
			xmlresponse = mmu.getXMLMessage();
			
//////////  analyze xmlresponse ///////////////
			
			JAXBContext jaxbContext = JAXBContext.newInstance(UnifiedServiceResponse.class);
		    Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
		 // mangle namespaces in the xmlDocument
		      int pos = xmlresponse.indexOf("UnifiedServiceResponse") + (new String("UnifiedServiceResponse")).length();
		      int pos2 = xmlresponse.indexOf(">", pos);
		      xmlresponse =
		    		  xmlresponse.replace(xmlresponse.substring(pos, pos2), " xmlns=\"http://www.seb.ee/integration\"");
		      // unmarshal
		      InputStream stream = new ByteArrayInputStream(xmlresponse.getBytes());
		      UnifiedServiceResponse usr = (UnifiedServiceResponse) unMarshaller.unmarshal(stream); 
	    	  UnifiedServiceErrors errors = usr.getUnifiedServiceErrors();
	    	  if(errors != null)
	    	  {
	    		  String err = "There are errors";
	    		  for (int i = 0; i < errors.getError().size(); i++)
	    		  {
	    			  err = errors.getError().get(i).getErrorClass();
	    			  System.out.println(err);
	    			  System.out.println(errors.getError().get(i).getErrorCode());
	    			  System.out.println(errors.getError().get(i).getErrorObject().getValue());
	    		  }
	  	    	  out.println(err);
	  	    	  return;
	    	  }
		      ContactcenterFindCustomerByPhoneOrPersonalCode2Output o =
	    			  (ContactcenterFindCustomerByPhoneOrPersonalCode2Output) usr.getUnifiedServiceBody().getAny().get(0);
	    	  this.customerId = o.getFindCustomerResponse().getCustomerId();
	    	  this.idCode = o.getFindCustomerResponse().getIdCode();
	    	  System.out.println(this.customerId);
	    	  System.out.println(this.idCode);
  			  c.exit();
        }
	    catch (JAXBException e)
	    {
	    	System.err.println(e.getMessage());
	    }
        catch (javax.jms.JMSException jmse)
        {
        	out.println("TECHNICALERROR");
        	System.out.println(jmse);
        	return;
        }

	      // create request to the GiveDigipassChallenge service
		
  		try
  		{
  			lv.adventus.seb.GiveDigipassChallenge dc = new lv.adventus.seb.GiveDigipassChallenge();
  			dc.SetHeader();
  			dc.SetHeaderUserId(userId);
  			dc.SetHeaderRequestId(connId);
  			dc.SetBody();
  			dc.SetBody(this.customerId, this.idCode);
  			xmlrequest = dc.Marshal();
  		}
  		catch (JAXBException e)
  		{
  			System.out.println(e.getMessage());
  			out.println("TECHNICALERROR");
  			return;
  		}
  		
  		c = new Connector(broker,usernameSonic,passwordSonic,queue, out, timeout);
  		c.start();
  		//start(broker,username,password,queue, out);
  		try
  	    {
  	    	progress.message.jclient.XMLMessage msg = ((progress.message.jclient.Session) c.getSession()).createXMLMessage();
  			//javax.jms.TextMessage msg = session.createTextMessage();
  			msg.setText( xmlrequest );
  			// Instead of sending, we will use the QueueRequestor.
  			javax.jms.Message responseMsg = c.getRequestor().request(msg, timeout);
  			if(responseMsg == null)
  			{
  				throw new javax.jms.JMSException("No response from JMS broker");
  			}
  			MultipartMessageUtility mmu = new MultipartMessageUtility(c); 
  			mmu.onMessage(responseMsg);
  			xmlresponse = mmu.getXMLMessage();
  			
  	//////  analyze xmlresponse ///////////////
  			
  			JAXBContext jaxbContext = JAXBContext.newInstance(UnifiedServiceResponse.class);
  		    Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
  		 // mangle namespaces in the xmlDocument
  		      int pos = xmlresponse.indexOf("UnifiedServiceResponse") + (new String("UnifiedServiceResponse")).length();
  		      int pos2 = xmlresponse.indexOf(">", pos);
  		      xmlresponse =
  		    		  xmlresponse.replace(xmlresponse.substring(pos, pos2), " xmlns=\"http://www.seb.ee/integration\"");
  		      // unmarshal
  		      InputStream stream = new ByteArrayInputStream(xmlresponse.getBytes());
  		      UnifiedServiceResponse usr = (UnifiedServiceResponse) unMarshaller.unmarshal(stream); 
  			  UnifiedServiceErrors errors = usr.getUnifiedServiceErrors();
  			  if(errors != null)
  			  {
  				  String err = "There are errors";
  				  for (int i = 0; i < errors.getError().size(); i++)
  				  {
  					  err = errors.getError().get(i).getErrorClass();
  					  System.out.println(err);
  					  System.out.println(errors.getError().get(i).getErrorCode());
  					  System.out.println(errors.getError().get(i).getErrorObject().getValue());
  				  }
  		    	  out.println(err);
  		    	  return;
  			  }
  			  ContactcenterGiveDigipassChallenge2Output o = (ContactcenterGiveDigipassChallenge2Output) usr.getUnifiedServiceBody().getAny().get(0);
  			  this.customerId = o.getGiveChallengeResponse().getCustomerId();
  			  System.out.println(this.customerId);
  			  this.challengeCode = o.getGiveChallengeResponse().getChallengeCode(); 
  			  System.out.println(this.challengeCode);
  			  this.userName = o.getGiveChallengeResponse().getUsername();
  			  System.out.println(this.userName);
  			  this.idCode = o.getGiveChallengeResponse().getIdCode();
  			  System.out.println(this.idCode);
  			  c.exit();
  			  out.println("<"+this.challengeCode+">");
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
        	System.out.println(e);
        	return;
	    }
	}
}
