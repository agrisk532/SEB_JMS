package lv.adventus.seb.util;

import java.util.Date;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import lv.adventus.seb.PingPong;
import lv.adventus.seb.UnifiedServiceResponse;
import lv.adventus.seb.PingPongService;

class PingPongTimerTask extends TimerTask
{
	private String broker;
	private String usernameSonic;
	private String passwordSonic;
	private String queue;
	private long connectionTimeout;
	private long ttl; // JMS parameter
	private long responseMsgTTL; // JMS parameter

	private ServletContext sc;
	private long timesCalled;
	private boolean debug;
	
	static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(PingPongTimerTask.class);
	
	public PingPongTimerTask(ServletContext sc, String broker, String username, String password,
			String queue, long timeout, long ttl, long responseMsgTTL, boolean debug) throws javax.xml.bind.JAXBException
	{
		this.sc = sc;
		this.broker = broker;
		this.usernameSonic = username;
		this.passwordSonic = password;
		this.queue = queue;
		this.connectionTimeout = timeout;
		this.ttl = ttl;
		this.responseMsgTTL = responseMsgTTL;
		this.timesCalled = 0;
		this.debug = debug;
	}
	
	@Override
	public void run()
	{
		try
		{
			String xmlrequest;
			UnifiedServiceResponse usr;
			PingPongService pps;
			String pongMessage;
			
			ConcurrentHashMap<String, Object> shared = (ConcurrentHashMap<String, Object>)sc.getAttribute("sharedData");
			String uid = String.valueOf(shared.get("PingPongUID")).concat(String.valueOf(timesCalled++));
			String pingPongStatusFileName =  String.valueOf(shared.get("PingPongStatusFileName"));

			pps = new lv.adventus.seb.PingPongService();
			pps.SetHeader();
			pps.SetHeaderRequestId(uid);
			pps.SetBody("Test", "");

			xmlrequest = pps.Marshal();
			// print XML
			if(debug)LOGGER.debug("PingPongService sent this XML:");
			if(debug)LOGGER.debug(XMLUtility.prettyFormat(xmlrequest));

			Connector c = new Connector(broker,usernameSonic,passwordSonic,queue,null,connectionTimeout,ttl,responseMsgTTL,false);
			if(debug)LOGGER.debug("PingPongTimerTask created Connector");
			c.SetHeader(pps.GetHeader());
			c.start();
			if(debug)LOGGER.debug("PingPongTimerTask started Connector");
			c.createMessage();
			if(debug)LOGGER.debug("PingPongTimerTask Connector query begins");
			usr = c.query(xmlrequest);
			if(debug)LOGGER.debug("PingPongTimerTask Connector query ends");
			if(debug)LOGGER.debug("PingPongTimerTask Exit from Connector started");
	  		c.exit();
	  		if(debug)LOGGER.debug("PingPongTimerTask Exit from Connector completed");

		    if(usr == null)
		    {
		    	LOGGER.error("PingPongTimerTask: Connector query returned null. Cannot continue.");
				shared.put("PingPong", Boolean.FALSE);
				Utility.stringToFile("0",pingPongStatusFileName); // for Zabbix
				return;
		    }

			if(usr.getUnifiedServiceErrors() != null)
			{
				LOGGER.error("PingPongTimerTask: PingPong service returned Errors. Cannot continue.");
				shared.put("PingPong", Boolean.FALSE);
				Utility.stringToFile("0",pingPongStatusFileName); // for Zabbix
				return;
			}

			PingPong pp = (PingPong) usr.getUnifiedServiceBody().getAny().get(0);
			pongMessage = pp.getPongMessage();

			LOGGER.debug("Answer from JMS Broker:");
			LOGGER.debug("system.PingPong_1: pongMessage = " + pongMessage);
			shared.put("PingPong", Boolean.TRUE);
			Utility.stringToFile("1",pingPongStatusFileName);	// for Zabbix
		}
		catch (javax.jms.JMSException e)
		{
			LOGGER.error("JMS exception in PingPongTimerTask()", e);
		}
		catch (javax.xml.bind.JAXBException e)
		{
			LOGGER.error("JAXB exception in PingPongTimerTask()", e);
		}
        catch (java.io.IOException e)
        {
        	LOGGER.error("IO exception in PingPongTimerTask()", e);
        }
	}
}

