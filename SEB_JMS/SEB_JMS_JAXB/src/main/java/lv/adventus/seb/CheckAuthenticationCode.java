package lv.adventus.seb;

import java.io.IOException;
import javax.xml.bind.JAXBException;

public class CheckAuthenticationCode extends ServicesBase
{
	AuthenticationQuery aq;

	public CheckAuthenticationCode() throws JAXBException
	{
	      header.setServiceName("contactcenter.CheckAuthenticationCode_2_Input");
	      ContactcenterCheckAuthenticationCode2Input cac = factory.createContactcenterCheckAuthenticationCode2Input();
	      aq = factory.createAuthenticationQuery();
	      cac.setAuthenticationQuery(aq);
	      body.getAny().add(cac);
	}
	
	public void SetBody	(
		String authenticationCode,
		String challengeCode,
		String userName
						)
	{
	      aq.setAuthenticationCode(authenticationCode);
	      aq.setChallengeCode(challengeCode);
	      aq.setUsername(userName);
	}
	
	public void SetBody	()
	{
	      aq.setAuthenticationCode("651904");
	      aq.setChallengeCode("");
	      aq.setUsername("florida");
	}
	
	  public static void main(String[] argv)
	  {
		  try
		  {
			  //String xmlDocument = "FindCustomerByPhoneOrPersonalCode_2Input2.xml";
			  //String xmlDocument = "GiveDigipassChallenge_2Input2.xml";
			  String xmlDocument = "CheckAuthenticationCode_2Input2.xml";
			  CheckAuthenticationCode ac = new CheckAuthenticationCode();
			  ac.SetHeader();
			  ac.SetBody();
			  ac.Marshal(xmlDocument);
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
