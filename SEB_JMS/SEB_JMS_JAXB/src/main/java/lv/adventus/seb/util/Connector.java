package lv.adventus.seb.util;

import java.io.PrintWriter;

public class Connector {
	
	private static String broker;
	private static String username;
	private static String password;
	private static String sQueue;
	private static PrintWriter out; // servlet response writer
	private static long timeout = 60000; // response wait timeout
	
    private javax.jms.QueueConnection connect = null;
    private javax.jms.QueueSession session = null;
    private lv.adventus.seb.util.QueueRequestor requestor = null;
    
	private static final long serialVersionUID = 1L;
	
	public Connector(String broker, String username, String password, String sQueue, PrintWriter out, long timeout)
	{
		this.broker = broker;
		this.username = username;
		this.password = password;
		this.sQueue = sQueue;
		this.out = out;
		this.timeout = timeout;
	}
	
    public void start()
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
    }
    
    public lv.adventus.seb.util.QueueRequestor getRequestor()
    {
    	return requestor;
    }
    
    public javax.jms.QueueSession getSession()
    {
    	return session;
    }
    
    /** Cleanup resources and exit. */
    public void exit()
    {
        try
        {
        	requestor.close();
            connect.close();
        }
        catch (javax.jms.JMSException jmse)
        {
        	out.println("TECHNICALERROR");
            jmse.printStackTrace();
            return;
        }
    }    
}
