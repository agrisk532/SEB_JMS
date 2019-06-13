package lv.adventus.seb.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import lv.adventus.seb.UnifiedServiceErrors;
import lv.adventus.seb.UnifiedServiceResponse;
import progress.message.jclient.BytesMessage;
import progress.message.jclient.TextMessage;

import java.util.Date;
import java.sql.Timestamp;

public class Connector {
	
	private static String broker;
	private static String username;
	private static String password;
	private static String sQueue;
	private static long connectionTimeout; // response wait connectionTimeout
	private static long ttl; // JMS parameter
	private static long responseMsgTTL; // JMS parameter

    public javax.jms.QueueConnection connect = null;
    public javax.jms.QueueSession session = null;
    public lv.adventus.seb.util.QueueRequestor requestor = null;
    public String xmlresponse;
    public progress.message.jclient.XMLMessage msg;
    public lv.adventus.seb.UnifiedServiceHeader msgHeader;
    public lv.adventus.seb.UnifiedServiceResponse usr;
    public HttpServletResponse response;
    
	private static final long serialVersionUID = 1L;
	
	static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(Connector.class);
	
	public Connector(String broker, String username, String password, String sQueue, HttpServletResponse res, long connectionTimeout,
			long ttl, long responseMsgTTL) throws java.io.IOException
	
	{
		this.broker = broker;
		this.username = username;
		this.password = password;
		this.sQueue = sQueue;
		this.response = res;
		this.connectionTimeout = connectionTimeout;
		this.ttl = ttl;
		this.responseMsgTTL = responseMsgTTL;
	}
	
    public void start() throws javax.jms.JMSException
    {
    	javax.jms.QueueConnectionFactory factory;
    	LOGGER.debug("Started");
        factory = (new progress.message.jclient.QueueConnectionFactory (broker));
        LOGGER.debug("ConnectionFactory created");
        connect = factory.createQueueConnection (username, password);
        LOGGER.debug("Created connection");
        session = connect.createQueueSession(false,javax.jms.Session.AUTO_ACKNOWLEDGE);
        LOGGER.debug("Created session");
        // Create the Queue and QueueRequestor for sending requests.
        javax.jms.Queue queue = null;
        queue = session.createQueue (sQueue);
        LOGGER.debug("Created queue");
        requestor = new lv.adventus.seb.util.QueueRequestor(session, queue);
        LOGGER.debug("Created queue requestor");
        connect.start();
        LOGGER.debug("Connector started");
    }
    
    public lv.adventus.seb.util.QueueRequestor getRequestor()
    {
    	return requestor;
    }
    
    public javax.jms.QueueSession getSession()
    {
    	return session;
    }
    
    public progress.message.jclient.XMLMessage createMessage() throws javax.jms.JMSException
    {
    	this.msg = ((progress.message.jclient.Session) this.getSession()).createXMLMessage();
    	return this.msg;
    }
    
    public progress.message.jclient.XMLMessage getMessage()
    {
    	return this.msg;
    }
    
    /** Cleanup resources and exit. */
    public void exit() throws javax.jms.JMSException
    {
       	requestor.close();
        connect.close();
    }
    
    public UnifiedServiceResponse query(String xmlrequest) throws javax.jms.JMSException, javax.xml.bind.JAXBException, java.io.IOException 
    {
    	LOGGER.debug("Entering Connector.query()");
		msg.setText( xmlrequest );
		if(this.msgHeader != null)
		{
			msg.setStringProperty("messageCategory", "Request");
			msg.setStringProperty("requestId", this.msgHeader.getRequestId());
			msg.setStringProperty("userId", this.msgHeader.getUserId());
			msg.setStringProperty("officerId", this.msgHeader.getOfficerId());
			msg.setStringProperty("serviceName", this.msgHeader.getServiceName());
			msg.setBooleanProperty("xsdBasedMessage", this.msgHeader.isXsdBasedRequest());
			msg.setStringProperty("TTL", String.valueOf(this.ttl));
			msg.setStringProperty("responseMsgTTL", String.valueOf(this.responseMsgTTL));
		}
		// Instead of sending, we will use the QueueRequestor.
		LOGGER.debug("Request to SonicMQ sent");
		javax.jms.Message responseMsg = this.getRequestor().request(msg, this.connectionTimeout);

		if(responseMsg == null)
		{
			//throw new javax.jms.JMSException("No response from JMS broker");
			LOGGER.error("No response from JMS broker. Connection and processing terminated.");
			this.exit();
			return null;
		}

		LOGGER.debug("Response from SonicMQ received:");
		//////
        if (responseMsg instanceof progress.message.jclient.XMLMessage)
        {
            progress.message.jclient.XMLMessage msg = (progress.message.jclient.XMLMessage)responseMsg;
            LOGGER.debug( "content in XMLmessage... " + msg.getText());
        }
        else if (responseMsg instanceof TextMessage)   
        {
            javax.jms.TextMessage tmsg = (javax.jms.TextMessage)responseMsg;
            LOGGER.debug( "content in TextMessage... " + tmsg.getText() );
        }
        else if (responseMsg instanceof BytesMessage)
        {
            javax.jms.BytesMessage bmsg = (javax.jms.BytesMessage)responseMsg;
            LOGGER.debug( "content in Bytesmessage... " + bmsg.readUTF());
        }
        else
        {
        	LOGGER.debug( "a JMS message... ");
        }		

		
		
		MultipartMessageUtility mmu = new MultipartMessageUtility(this); 
		mmu.onMessage(responseMsg);
		xmlresponse = mmu.getXMLMessage();

		
		//////
		//LOGGER.debug(responseBody);

		
//		LOGGER.info("XML Response:");
//		LOGGER.info(xmlresponse);
		
		if(xmlresponse.length() == 0)
		{
			LOGGER.error("xmlresponse from JMS broker is zero length. Cannot continue.");
			return null;
		}
		
//////  analyze xmlresponse ///////////////
		
		JAXBContext jaxbContext = JAXBContext.newInstance(UnifiedServiceResponse.class);
	    Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
	 // mangle namespaces in the xmlDocument
	    int pos = xmlresponse.indexOf("UnifiedServiceResponse") + (new String("UnifiedServiceResponse")).length();
	    int pos2 = xmlresponse.indexOf(">", pos);
	    
//		LOGGER.info("pos: " + pos);
//		LOGGER.info("pos2: " + pos2);
	    
	    xmlresponse =
	    	  xmlresponse.replace(xmlresponse.substring(pos, pos2), " xmlns=\"http://www.seb.ee/integration\"");
	    
// add missing lines to the XML response, if Sonic is not sending them
//	    
// for GiveDigipassChallenge()
//	    
//	    if(xmlresponse.indexOf("GiveDigipassChallenge") != -1)
//	    {
//	      StringBuilder sb = new StringBuilder(xmlresponse);
//	      String s1 = "<unifiedServiceBody>";
//	      String s2 = "<contactcenter:contactcenter.GiveDigipassChallenge_2_Output xmlns:cmn=\"http://www.seb.ee/common\" xmlns:contactcenter=\"http://www.seb.ee/contactcenter\">"; 
//	      int pos3 = sb.indexOf(s1);
//		    pos3 += s1.length();
//		    sb.insert(pos3, s2);
//		   
//		    s1 = "</contactcenter:GiveChallengeResponse>";
//		    s2 = "</contactcenter:contactcenter.GiveDigipassChallenge_2_Output>";
//		    pos3 = sb.indexOf(s1);
//		    pos3 += s1.length();
//		    sb.insert(pos3, s2);
//		    
//		    xmlresponse = sb.toString(); 
//	    }
//	    
// for CheckAuthenticationCode()
//
//	    if(xmlresponse.indexOf("CheckAuthenticationCode") != -1)
//	    {
//	      StringBuilder sb = new StringBuilder(xmlresponse);
//	      String s1 = "<unifiedServiceBody>";
//	      String s2 = "<contactcenter:contactcenter.CheckAuthenticationCode_2_Output xmlns:cmn=\"http://www.seb.ee/common\" xmlns:contactcenter=\"http://www.seb.ee/contactcenter\">"; 
//	      int pos3 = sb.indexOf(s1);
//		    pos3 += s1.length();
//		    sb.insert(pos3, s2);
//		   
//		    s1 = "</contactcenter:AuthenticationResponse>";
//		    s2 = "</contactcenter:contactcenter.CheckAuthenticationCode_2_Output>";
//		    pos3 = sb.indexOf(s1);
//		    pos3 += s1.length();
//		    sb.insert(pos3, s2);
//		    
//		    xmlresponse = sb.toString(); 
//	    }

//		LOGGER.info("Connector XML:");
//		LOGGER.info(XMLUtility.prettyFormat(xmlresponse));
	    
	    // unmarshal
		InputStream stream = new ByteArrayInputStream(xmlresponse.getBytes("UTF-8"));
//    	LOGGER.info("Stream available bytes: " + String.valueOf(stream.available()));
		LOGGER.debug("Connector unmarshaling started");
		try
		{
			this.usr = (UnifiedServiceResponse) unMarshaller.unmarshal(stream);
			if(response != null)	// for PingPongTimerTask() that it do not send output to non-existing servlet 
				ErrorHandler.SendErrors(response, usr);
			return this.usr;
		}
		catch (javax.xml.bind.UnmarshalException ex)
		{
			LOGGER.error("Error in received data. Connection and processing terminated.");
			this.exit();
			return null;
		}
    }
    
    public void SetHeader(lv.adventus.seb.UnifiedServiceHeader h)
    {
    	this.msgHeader = h;
    }
    // gets current timestamp
    public static Timestamp getTimestamp()
    {
    	Date date= new Date();
    	long time = date.getTime();
    	Timestamp ts = new Timestamp(time);
    	return ts;
    }
}
