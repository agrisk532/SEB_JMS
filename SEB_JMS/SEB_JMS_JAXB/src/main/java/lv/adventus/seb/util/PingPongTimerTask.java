package lv.adventus.seb.util;

import java.util.Date;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import lv.adventus.seb.PingPong;

class PingPongTimerTask extends TimerTask
{
	private lv.adventus.seb.PingPongService pps;
	private String pingMessage;
	private String pongMessage;
	
	private String broker;
	private String usernameSonic;
	private String passwordSonic;
	private String queue;
	private long connectionTimeout;
	private ServletContext sc;
	
	public PingPongTimerTask(ServletContext sc, String broker, String username, String password,
			String queue, long timeout) throws javax.xml.bind.JAXBException
	{
		this.sc = sc;
		this.broker = broker;
		this.usernameSonic = username;
		this.passwordSonic = password;
		this.queue = queue;
		this.connectionTimeout = timeout;
		pps = new lv.adventus.seb.PingPongService();
	}
	
	@Override
	public void run()
	{
		try
		{
			String xmlrequest;
			pps.SetHeader();
			pps.SetBody("Test", "");

			xmlrequest = pps.Marshal();
			// print XML
			System.out.println("PingPongService sent this XML:");
			System.out.println(XMLUtility.prettyFormat(xmlrequest));

			Connector c = new Connector(broker,usernameSonic,passwordSonic,queue,null,connectionTimeout);
			c.SetHeader(pps.GetHeader());
			c.start();
			c.createMessage();
			lv.adventus.seb.UnifiedServiceResponse usr = c.query(xmlrequest);

			if(usr.getUnifiedServiceErrors() != null)
			{
				System.out.println("PingPongTimerTask: PingPong service returns 0.");
				sc.setAttribute("PingPong", "0");
				return;
			}

			PingPong pp = (PingPong) usr.getUnifiedServiceBody().getAny().get(0);
			this.pongMessage = pp.getPongMessage();

			System.out.println("Answer from JMS Broker:");
			System.out.println("system.PingPong_1: pongMessage = " + this.pongMessage);
			c.exit();
			sc.setAttribute("PingPong", "1");
		}
		catch (javax.jms.JMSException e)
		{
			System.out.println("JMS exception in PingPongTimerTask()");
			e.printStackTrace();
			return;
		}
		catch (javax.xml.bind.JAXBException e)
		{
			System.out.println("JAXB exception in PingPongTimerTask()");
			e.printStackTrace();
			return;
		}
	}
}
