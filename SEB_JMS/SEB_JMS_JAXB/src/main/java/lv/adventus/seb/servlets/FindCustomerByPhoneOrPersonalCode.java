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

/**
 * Servlet implementation class FindCustomerByPhoneOrPersonalCode
 */
public class FindCustomerByPhoneOrPersonalCode extends HttpServlet {
	
    private static final String broker = "awebs:6526";
    private static final String username = "tester1";
    private static final String password = "tester";
    private static final String queue = "SEB_SERVICES";

    private javax.jms.QueueConnection connect = null;
    private javax.jms.QueueSession session = null;
    private javax.jms.QueueRequestor requestor = null;

	private static final long serialVersionUID = 1L;
	
	private String xmlrequest;
	

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
	    String paramName = "id";
	    String paramValue = request.getParameter(paramName);
	    if (paramValue==null)
	    {
	        out.write("Parameter " + paramName + " not found");
	    }
	    //out.close();
			
		try
		{
			lv.adventus.seb.FindCustomerByPhoneOrPersonalCode fc = new lv.adventus.seb.FindCustomerByPhoneOrPersonalCode();
			fc.SetHeader();
			fc.SetBody();
			xmlrequest = fc.Marshal();
		}
		catch (JAXBException e)
		{
			System.out.println(e.getMessage());
		}
		
		//start(broker,username,password,queue);
		out = response.getWriter();
	    response.setContentType("text/html");

	    out.println("<html>");
	    out.println("<head>");
	    out.println("<title>Hola</title>");
	    out.println("</head>");
	    out.println("<body>");
	    out.println("Hi man");
	    out.println("</body>");
	    out.println("</html>");
	}

		    /** Create JMS client for sending messages. */
    private void start ( String broker, String username, String password, String sQueue)
    {
        // Create a connection.
    	/*
        try
        {
            javax.jms.QueueConnectionFactory factory;
            factory = (new progress.message.jclient.QueueConnectionFactory (broker));
            connect = factory.createQueueConnection (username, password);
            session = connect.createQueueSession(false,javax.jms.Session.AUTO_ACKNOWLEDGE);
        }
        catch (javax.jms.JMSException jmse)
        {
            System.err.println("error: Cannot connect to Broker - " + broker);
            jmse.printStackTrace();
            System.exit(1);
        }

        // Create the Queue and QueueRequestor for sending requests.
        javax.jms.Queue queue = null;
        try
        {
            queue = session.createQueue (sQueue);
            requestor = new javax.jms.QueueRequestor(session, queue);

            // Now that all setup is complete, start the Connection.
            connect.start();
        }
        catch (javax.jms.JMSException jmse)
        {
            jmse.printStackTrace();
            exit();
        }

        try
        {
			javax.jms.TextMessage msg = session.createTextMessage();
			msg.setText( xmlrequest );
			// Instead of sending, we will use the QueueRequestor.
			javax.jms.Message response = requestor.request(msg);
			// The message should be a TextMessage.  Just report it.
			javax.jms.TextMessage textMessage = (javax.jms.TextMessage) response;
			System.out.println( "[Reply] " + textMessage.getText() );

        }
        catch ( javax.jms.JMSException jmse )
        {
            jmse.printStackTrace();
        }
        */
    	
    	System.out.println("Hi");
       
    }

    /** Cleanup resources cleanly and exit. */
    private void exit()
    {
        try
        {
            requestor.close();
            connect.close();
        }
        catch (javax.jms.JMSException jmse)
        {
            jmse.printStackTrace();
        }
    }
}
