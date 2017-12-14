package lv.adventus.seb.util;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.PropertyConfigurator;

public class MyServletContextListener implements ServletContextListener
{
    private PingPongTimerTask task = null;
	private String broker;
	private String usernameSonic;
	private String passwordSonic;
	private String queue;
	private long connectionTimeout;
    private long ttl = 30000L;
    private long responseMsgTTL = 30000L;
	private ServletContext sc;
	private long pingPongInterval;
	private String pingPongStatusFileName;
	private String pingPongOnOff = "1";  // 1 - on, 0 - off
	private ConcurrentHashMap<String, Object> shared;
	
	static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(MyServletContextListener.class);

    public MyServletContextListener()
    {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
	   	sc = sce.getServletContext();
	   	
        String log4jConfigFile = sc.getInitParameter("log4j-config-location");
        String fullPath = sc.getRealPath("") + java.io.File.separator + log4jConfigFile;
        PropertyConfigurator.configure(fullPath);
	   	
	   	LOGGER.info("ServletContextListener started");
    	broker = sc.getInitParameter("broker");
    	usernameSonic = sc.getInitParameter("usernameSonic");
    	passwordSonic = sc.getInitParameter("passwordSonic");
    	queue = sc.getInitParameter("queue");
    	connectionTimeout = Long.parseLong(sc.getInitParameter("connectionTimeout"));
    	ttl = Long.parseLong(sc.getInitParameter("TTL"));
    	responseMsgTTL = Long.parseLong(sc.getInitParameter("responseMsgTTL"));
    	pingPongInterval = Long.parseLong(sc.getInitParameter("pingPongInterval"));
    	pingPongStatusFileName = sc.getInitParameter("pingPongStatusFileName");
    	pingPongOnOff = sc.getInitParameter("PingPongOnOff");
    	
    	LOGGER.info("ServletContext parameters:");
    	LOGGER.info("broker: " + broker);
    	LOGGER.info("username: " + usernameSonic);
    	LOGGER.info("password: " + passwordSonic);
    	LOGGER.info("queue: " + queue);
    	LOGGER.info("connectionTimeout: " + connectionTimeout);
    	LOGGER.info("JMS Message Property TTL : " + ttl);
    	LOGGER.info("JMS Message Property responseMsgTTL : " + responseMsgTTL);
    	LOGGER.info("PingPongInterval: " + pingPongInterval);
    	LOGGER.info("PingPongStatusFileName: " + pingPongStatusFileName);
    	LOGGER.info("PingPongOnOff: " + pingPongOnOff);
    	
    	if(pingPongOnOff.equals("0"))
    	{
    		LOGGER.info("!!!!!!! PingPong service not used !!!!!!!");
    	}
    	
	   	int delay = 1000;
	   	Timer timer = new Timer();

	   	ConcurrentHashMap<String, Object> shared =
	   			(ConcurrentHashMap<String, Object>)sc.getAttribute("sharedData");
	   	if (shared == null)
	   	{
	   	    shared = new ConcurrentHashMap<String, Object>();
	   	    sc.setAttribute("sharedData", shared);
	   	}
	   	
	   	shared.put("PingPong", Boolean.TRUE);
	   	shared.put("PingPongOnOff", pingPongOnOff);
	   	
	   	try
	   	{
	   		Utility.stringToFile("1", pingPongStatusFileName);	// for Zabbix
	   	}
        catch (java.io.IOException e)
        {
        	LOGGER.error(e);
        }

	   	if(!pingPongOnOff.equals("0"))
	   	{
	   		try
	   		{
	   			this.task = new PingPongTimerTask(sc,broker,usernameSonic,passwordSonic,queue, connectionTimeout, ttl, responseMsgTTL);
	   		}
	   		catch(javax.xml.bind.JAXBException e)
	   		{
	   			LOGGER.error("PingPong service exception. Could not create the PingPongTimerTask.");
	   			LOGGER.error("Processing not possible.");
	   			shared.put("PingPong", Boolean.FALSE);
	   			return;
	   		}
	   		
	   		timer.schedule(this.task, delay, pingPongInterval);
	   		shared.put("timer", timer);
	   		shared.put("PingPongUID", String.valueOf(UUID.randomUUID())); // unique uid for PingPong, Genesys connection id for other servlets
	   		shared.put("PingPongStatusFileName", pingPongStatusFileName);
	   	}
	}
    

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
    	ServletContext servletContext = sce.getServletContext();
    	// get our timer from the Context
    	Timer timer = (Timer)servletContext.getAttribute ("timer");
    	// cancel all pending tasks in the timers queue
    	if (timer != null)
    	timer.cancel();
    	// remove the timer from the servlet context
    	servletContext.removeAttribute ("sharedData");
    	try
    	{
    		Utility.stringToFile("0",pingPongStatusFileName); // for Zabbix
    	}
        catch (java.io.IOException e)
        {
        	LOGGER.error(e);
        }
    	LOGGER.info("ServletContextListener destroyed");
    }
}
