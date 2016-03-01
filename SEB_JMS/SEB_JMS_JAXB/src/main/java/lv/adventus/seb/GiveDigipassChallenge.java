package lv.adventus.seb;

import java.io.IOException;
import javax.xml.bind.JAXBException;

public class GiveDigipassChallenge extends ServicesBase
{
	GiveDigipassQuery dq;

	public GiveDigipassChallenge() throws JAXBException
	{
	      header.setServiceName("contactcenter.GiveDigipassChallenge_2_Input");
	      ContactcenterGiveDigipassChallenge2Input cci2 = factory.createContactcenterGiveDigipassChallenge2Input();
	      dq = factory.createGiveDigipassQuery();
	      cci2.setGiveDigipassQuery(dq);
	      body.getAny().add(cci2);
	}
	
	public void SetBody	(
		String customerId,
		String IdCode
						)
	{
	      dq.setCustomerId(customerId);
	      dq.setIdCode(IdCode);
	}
	
	public void SetBody	()
	{
	      dq.setCustomerId("F85354");
	      dq.setIdCode("38207031078");
	}
	
	  public static void main(String[] argv)
	  {
		  try
		  {
			  //String xmlDocument = "FindCustomerByPhoneOrPersonalCode_2Input2.xml";
			  String xmlDocument = "GiveDigipassChallenge_2Input2.xml";
			  //String xmlDocument = "CheckAuthenticationCode_2Input2.xml";
			  GiveDigipassChallenge dc = new GiveDigipassChallenge();
			  dc.SetHeader();
			  dc.SetBody();
			  dc.Marshal(xmlDocument);
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
