package lv.adventus.seb.servlets;

import java.io.IOException;

import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import progress.message.jclient.MultipartMessage;
import progress.message.jclient.Part;
import progress.message.jclient.Header;
import progress.message.jclient.Constants;
import progress.message.jclient.TextMessage;
import progress.message.jclient.BytesMessage;

import lv.adventus.seb.util.Connector;
import lv.adventus.seb.util.MultipartMessageUtility;

/**
 * Servlet implementation class FindCustomerByPhoneOrPersonalCode
 */
public class FindCustomerByPhoneOrPersonalCode extends HttpServlet {
	
    private static final String broker = "awebs:6526";
    private static final String username = "tester1";
    private static final String password = "tester";
    private static final String queue = "SEB_SERVICES";
    private static final long timeout = 60000;
	private String xmlrequest;
	private String xmlresponseFindCustomer;
	

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
		
		Connector c = new Connector(broker,username,password,queue, out, timeout);
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
			xmlresponseFindCustomer = mmu.getXMLMessage();
			
			// analyze xmlresponseFindCustomer
			
			c.exit();
        }
        catch (javax.jms.JMSException jmse)
        {
        	out.println("TECHNICALERROR");
        	System.out.println(jmse);
        	return;
        }
	}
}