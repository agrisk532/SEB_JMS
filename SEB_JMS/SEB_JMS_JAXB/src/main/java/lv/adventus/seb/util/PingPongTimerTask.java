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
			if(debug)System.out.println("PingPongService sent this XML:");
			if(debug)System.out.println(XMLUtility.prettyFormat(xmlrequest));

			Connector c = new Connector(broker,usernameSonic,passwordSonic,queue,null,connectionTimeout,ttl,responseMsgTTL,false);
			if(debug)System.out.println("PingPongTimerTask created Connector at: " + Connector.getTimestamp());
			c.SetHeader(pps.GetHeader());
			c.start();
			if(debug)System.out.println("PingPongTimerTask started Connector at: " + Connector.getTimestamp());
			c.createMessage();
			if(debug)System.out.println("PingPongTimerTask Connector query begins at: " + Connector.getTimestamp());
			usr = c.query(xmlrequest);
			if(debug)System.out.println("PingPongTimerTask Connector query ends at: " + Connector.getTimestamp());
			if(debug)System.out.println("PingPongTimerTask Exit from Connector started at: " + Connector.getTimestamp());
	  		c.exit();
	  		if(debug)System.out.println("PingPongTimerTask Exit from Connector completed at: " + Connector.getTimestamp());

		    if(usr == null)
		    {
		    	System.out.println("PingPongTimerTask: Connector query returned null.");
				shared.put("PingPong", Boolean.FALSE);
				Utility.stringToFile("0",pingPongStatusFileName); // for Zabbix
				return;
		    }

			if(usr.getUnifiedServiceErrors() != null)
			{
				System.out.println("PingPongTimerTask: PingPong service returned Errors.");
				shared.put("PingPong", Boolean.FALSE);
				Utility.stringToFile("0",pingPongStatusFileName); // for Zabbix
				return;
			}

			PingPong pp = (PingPong) usr.getUnifiedServiceBody().getAny().get(0);
			pongMessage = pp.getPongMessage();

			System.out.println("Answer from JMS Broker:");
			System.out.println("system.PingPong_1: pongMessage = " + pongMessage);
			shared.put("PingPong", Boolean.TRUE);
			Utility.stringToFile("1",pingPongStatusFileName);	// for Zabbix
		}
		catch (javax.jms.JMSException e)
		{
			System.out.println("JMS exception in PingPongTimerTask()");
			//e.printStackTrace();
			sc.log("JMS exception in PingPongTimerTask()", e);
		}
		catch (javax.xml.bind.JAXBException e)
		{
			System.out.println("JAXB exception in PingPongTimerTask()");
			//e.printStackTrace();
			sc.log("JAXB exception in PingPongTimerTask()", e);
		}
        catch (java.io.IOException e)
        {
        	System.out.println("IO exception in PingPongTimerTask()");
        	//e.printStackTrace();
        	sc.log("IO exception in PingPongTimerTask()", e);
        }
	}
}

