package lv.adventus.seb.util;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener
{
    private PingPongTimerTask task = null;
	private String broker;
	private String usernameSonic;
	private String passwordSonic;
	private String queue;
	private long connectionTimeout;
	private ServletContext sc;
	private long pingPongInterval;
	private ConcurrentHashMap<String, Object> shared;

    public MyServletContextListener()
    {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
	   	sc = sce.getServletContext();
	   	System.out.println("*********ServletContextListener started*********");
    	broker = sc.getInitParameter("broker");
    	usernameSonic = sc.getInitParameter("usernameSonic");
    	passwordSonic = sc.getInitParameter("passwordSonic");
    	queue = sc.getInitParameter("queue");
    	connectionTimeout = Long.parseLong(sc.getInitParameter("connectionTimeout"));
    	pingPongInterval = Long.parseLong(sc.getInitParameter("pingPongInterval"));
    	
    	System.out.println("ServletContext parameters:");
    	System.out.println("broker: " + broker);
    	System.out.println("username: " + usernameSonic);
    	System.out.println("password: " + passwordSonic);
    	System.out.println("queue: " + queue);
    	System.out.println("connectionTimeout: " + connectionTimeout);
    	System.out.println("PingPongInterval: " + pingPongInterval);
    	System.out.println();

	   	int delay = 1000;
	   	Timer timer = new Timer();
	   	try
	   	{
	   		this.task = new PingPongTimerTask(sc,broker,usernameSonic,passwordSonic,queue, connectionTimeout);
	   	}
	   	catch(javax.xml.bind.JAXBException e)
	   	{
	   		System.out.println("PingPong service exception. Could not create the timer task");
	   		System.out.println("PingPong service won't be used.");
	   		return;
	   	}

	   	timer.scheduleAtFixedRate(this.task, delay, pingPongInterval);
	   	sc.setAttribute ("timer", timer);
	   	
	   	ConcurrentHashMap<String, Object> shared =
	   			(ConcurrentHashMap<String, Object>)sc.getAttribute("sharedData");
	   	if (shared == null)
	   	{
	   	    shared = new ConcurrentHashMap<String, Object>();
	   	    sc.setAttribute("sharedData", shared);
	   	}

	   	shared.put("UID", String.valueOf(UUID.randomUUID()));
	}

    @Override
    public void contextDestroyed(ServletContextEvent sce){
    	ServletContext servletContext = sce.getServletContext();
    	// get our timer from the Context
    	Timer timer = (Timer)servletContext.getAttribute ("timer");
    	// cancel all pending tasks in the timers queue
    	if (timer != null)
    	timer.cancel();
    	// remove the timer from the servlet context
    	servletContext.removeAttribute ("timer");
    	System.out.println("ServletContextListener destroyed");
    }
}