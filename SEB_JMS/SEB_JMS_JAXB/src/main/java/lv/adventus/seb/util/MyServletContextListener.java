package lv.adventus.seb.util;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
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

	   	timer.scheduleAtFixedRate(this.task, delay, 30000);
	   	sc.setAttribute ("timer", timer);
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