package lv.adventus.seb;

import java.io.IOException;
import javax.xml.bind.JAXBException;

public class PingPongService extends ServicesBase
{
	private PingPong pp;
	
	public PingPongService() throws JAXBException
	{
	      header.setServiceName("system.PingPong_1");
	      pp = factory.createPingPong();
	      body.getAny().add(pp);
	}
	
	public void SetBody	(
		String pingMessage,
		String pongMessage
						)
	{
	      pp.setPingMessage(pingMessage);
	      pp.setPongMessage(pongMessage);
	}
	
	public void SetBody	()
	{
		pp.setPingMessage("Test");
		pp.setPongMessage(null);
	}
	
	  public static void main(String[] argv)
	  {
		  try
		  {
			  String xmlDocument = "PingPong_2Input.xml";
			  PingPongService fc = new PingPongService();
			  fc.SetHeader();
			  fc.SetBody("Test", null);
			  fc.Marshal(xmlDocument);
		  }
		  catch (JAXBException e)
		  {
			  System.out.println(e.getMessage());
		  }
		  catch (IOException e)
		  {
			  System.out.println(e.getMessage());
		  }
	  }
}
