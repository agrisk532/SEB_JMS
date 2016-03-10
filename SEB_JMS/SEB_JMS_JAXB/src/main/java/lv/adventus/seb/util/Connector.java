package lv.adventus.seb.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import lv.adventus.seb.UnifiedServiceErrors;
import lv.adventus.seb.UnifiedServiceResponse;

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
    private String xmlresponse;
    
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
	
    public void start() throws javax.jms.JMSException
    {
    	javax.jms.QueueConnectionFactory factory;
        factory = (new progress.message.jclient.QueueConnectionFactory (broker));
        connect = factory.createQueueConnection (username, password);
        session = connect.createQueueSession(false,javax.jms.Session.AUTO_ACKNOWLEDGE);
        // Create the Queue and QueueRequestor for sending requests.
        javax.jms.Queue queue = null;
        queue = session.createQueue (sQueue);
        requestor = new lv.adventus.seb.util.QueueRequestor(session, queue);
        connect.start();
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
    public void exit() throws javax.jms.JMSException
    {
       	requestor.close();
        connect.close();
    }
    
    public UnifiedServiceResponse query(String xmlrequest) throws javax.jms.JMSException, javax.xml.bind.JAXBException
    {
    	progress.message.jclient.XMLMessage msg = ((progress.message.jclient.Session) this.getSession()).createXMLMessage();
		//javax.jms.TextMessage msg = session.createTextMessage();
		msg.setText( xmlrequest );
		// Instead of sending, we will use the QueueRequestor.
		javax.jms.Message responseMsg = this.getRequestor().request(msg, this.timeout);
		if(responseMsg == null)
		{
			throw new javax.jms.JMSException("No response from JMS broker");
		}
		MultipartMessageUtility mmu = new MultipartMessageUtility(this); 
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
		}
  		return usr;
    }
}
