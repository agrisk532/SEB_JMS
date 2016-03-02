package lv.adventus.seb;

import java.io.IOException;
import javax.xml.bind.JAXBException;

public class FindCustomerByPhoneOrPersonalCode extends ServicesBase
{
	private FindCustomerQuery fcq;

	public FindCustomerByPhoneOrPersonalCode() throws JAXBException
	{
	      header.setServiceName("contactcenter.FindCustomerByPhoneOrPersonalCode_2");
	      ContactcenterFindCustomerByPhoneOrPersonalCode2Input cci1 = 
	    		  factory.createContactcenterFindCustomerByPhoneOrPersonalCode2Input();
	      fcq = factory.createFindCustomerQuery();	      
	      cci1.setFindCustomerQuery(fcq);
	      body.getAny().add(cci1);
	}
	
	public void SetBody	(
		String idCode,
		String userPhoneNumber
						)
	{
	      fcq.setIdCode(idCode);
	      fcq.setUserPhoneNumber(userPhoneNumber);
	}
	
	public void SetBody	()
	{
		fcq.setIdCode("35503186196");
		fcq.setUserPhoneNumber("");
	}
	
	  public static void main(String[] argv)
	  {
		  try
		  {
			  String xmlDocument = "FindCustomerByPhoneOrPersonalCode_2Input2.xml";
			  //String xmlDocument = "GiveDigipassChallenge_2Input2.xml";
			  //String xmlDocument = "CheckAuthenticationCode_2Input2.xml";
			  FindCustomerByPhoneOrPersonalCode fc = new FindCustomerByPhoneOrPersonalCode();
			  fc.SetHeader();
			  fc.SetBody();
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
