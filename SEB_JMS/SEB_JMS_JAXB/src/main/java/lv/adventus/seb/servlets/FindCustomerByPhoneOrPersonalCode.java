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
    private lv.adventus.seb.util.QueueRequestor requestor = null;
    private long timeout = 60000; // SonicMQ response wait timeout in miliseconds

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
		
		start(broker,username,password,queue, out);

	}

		    /** Create JMS client for sending messages. */
    private void start ( String broker, String username, String password, String sQueue, PrintWriter out)
    {
        try
        {
            javax.jms.QueueConnectionFactory factory;
            factory = (new progress.message.jclient.QueueConnectionFactory (broker));
            connect = factory.createQueueConnection (username, password);
            session = connect.createQueueSession(false,javax.jms.Session.AUTO_ACKNOWLEDGE);
        }
        catch (javax.jms.JMSException jmse)
        {
        	out.println("TECHNICALERROR");
            System.out.println("error: Cannot connect to Broker - " + broker);
            jmse.printStackTrace();
            return;
        }

        // Create the Queue and QueueRequestor for sending requests.
        javax.jms.Queue queue = null;
        try
        {
            queue = session.createQueue (sQueue);
            requestor = new lv.adventus.seb.util.QueueRequestor(session, queue);
            connect.start();
        }
        catch (javax.jms.JMSException jmse)
        {
        	out.println("TECHNICALERROR");
            jmse.printStackTrace();
            return;
        }

        try
        {
        	// we assume response is SonicMQ XMLMessage
        	progress.message.jclient.XMLMessage msg = ((progress.message.jclient.Session) session).createXMLMessage();
			//javax.jms.TextMessage msg = session.createTextMessage();
			msg.setText( xmlrequest );
			// Instead of sending, we will use the QueueRequestor.
			javax.jms.Message response = requestor.request(msg, timeout);
			
			onMessage(response);
        }
        catch (javax.jms.JMSException jmse)
        {
        	out.println("TECHNICALERROR");
        	System.out.println(jmse);
        	return;
        }
    }
			
        /**
         * Handle the message
         * (as specified in the javax.jms.MessageListener interface).
         */
        public void onMessage( javax.jms.Message aMessage)
        {
            System.out.println();

            if (aMessage instanceof MultipartMessage)
            {
                System.out.println( "received MutipartMessage.... "  );
                unpackMM(aMessage, 0);
            }
            else
            {
                System.out.println( "received a JMS message ");
            }
            System.out.println("Received not a MultipartMessage.... ");

        }

        private void unpackMM(javax.jms.Message aMessage, int depth)
        {
            int n = depth;
            System.out.println();
            indent(n); System.out.println("******* Beginning of MultipartMessage ******");
            System.out.println();

            try
            {
                indent(n); System.out.println("Extend_type property = " + aMessage.getStringProperty(Constants.EXTENDED_TYPE));
                MultipartMessage mm = (MultipartMessage)aMessage;
                int partCount = mm.getPartCount();

                indent(n); System.out.println("partCount of this MultipartMessage = " + partCount);
                for (int i = 0; i < partCount; i++)
                {
                    System.out.println();
                    indent(n); System.out.println("--------Beginning of part " + (i+1));
                    Part part = mm.getPart(i);

                    indent(n); System.out.println("Part.contentType = " + part.getHeader().getContentType());
                    indent(n); System.out.println("Part.contentId = " + part.getHeader().getContentId());

                    if (mm.isMessagePart(i))
                    {
                        javax.jms.Message msg = mm.getMessageFromPart(i);
                        if (msg instanceof MultipartMessage)
                            unpackMM(msg, ++depth);
                        else
                            unpackJMSMessage(msg, n);
                    }
                    else
                    {
                        unpackPart(part, n);
                    }
                    indent(n); System.out.println("--------end of part " + (i+1));
                    System.out.println();
                }
                indent(n); System.out.println("******* End of MultipartMessage ******");
                System.out.println();
            }
             catch (javax.jms.JMSException jmse)
            {
                System.err.println(jmse);
            }
        }


        private void indent(int num)
        {
            for (int i = 0; i < num; i++)
            {
                System.out.print("\t");
            }
        }

        private void unpackJMSMessage(javax.jms.Message aMessage, int depth)
        {
            try
            {
                if (aMessage instanceof TextMessage)
                {
                    javax.jms.TextMessage tmsg = (javax.jms.TextMessage) aMessage;
                    indent(depth); System.out.println( "content in TextMessage... " + tmsg.getText() );
                }
                else if (aMessage instanceof BytesMessage)
                {
                    javax.jms.BytesMessage bmsg = (javax.jms.BytesMessage) aMessage;
                    indent(depth); System.out.println( "content in Bytesmessage... " + bmsg.readUTF());
                }
                else if (aMessage instanceof progress.message.jclient.XMLMessage)
                {
                    progress.message.jclient.XMLMessage msg = ((progress.message.jclient.Session)session).createXMLMessage();
                    indent(depth); System.out.println( "content in XMLmessage... " + msg.getText());
                }
                else
                {
                    indent(depth); System.out.println( "a JMS message... ");
                }
            }
             catch (javax.jms.JMSException jmse)
            {
                System.err.println(jmse);
            }
        }

        private void unpackPart(Part part, int depth)
        {
            byte[] content = part.getContentBytes();
            int size = content.length;
            String s = new String(content);
            indent(depth); System.out.println( "...size :  " + size);
            indent(depth); System.out.println( "...content :  ");
            System.out.println();
            indent(depth); System.out.println(s);
        }

        /** Cleanup resources cleanly and exit. */
        private void exit()
        {
            try
            {
                connect.close();
            }
            catch (javax.jms.JMSException jmse)
            {
                jmse.printStackTrace();
            }

            System.exit(0);
        }

}