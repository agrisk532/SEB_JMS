package lv.adventus.seb;

import javax.xml.bind.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class JAXB2Marshaller {

  public void generateXMLDocument(File xmlDocument) {
    try {

      JAXBContext jaxbContext = JAXBContext.newInstance("lv.adventus.seb");
      Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.setProperty("jaxb.formatted.output", new Boolean(true));

      ObjectFactory factory = new ObjectFactory();
      UnifiedServiceRequest request = factory.createUnifiedServiceRequest();
      UnifiedServiceHeader header = factory.createUnifiedServiceHeader();
      UnifiedServiceBody body = factory.createUnifiedServiceBody();

      header.setChannel("ContactCenter");
      header.setClientApplication("IVR");
      header.setClientApplicationVersion("IVR 1.0");
      header.setCountryCode("EE");
      header.setLanguage("EE");
      header.setOfficerId("admin");
      header.setRequestId("cc02ed3ebc-38ec-319c-9328-063c6fc93b55");
      header.setRequestRemoteHost("test remote host");
      header.setRequestServerId("test server id");
      header.setResponseIsCompressed(false);
      header.setUserId("35503186196");
      header.setXsdBasedRequest(false);
      
      // ContactcenterFindCustomerByPhoneOrPersonalCode2Input
/*      
      header.setServiceName("contactcenter.FindCustomerByPhoneOrPersonalCode_2");
      ContactcenterFindCustomerByPhoneOrPersonalCode2Input cci1 = 
    		  factory.createContactcenterFindCustomerByPhoneOrPersonalCode2Input();
      
      FindCustomerQuery fcq = factory.createFindCustomerQuery();
      fcq.setIdCode("35503186196");
      fcq.setUserPhoneNumber("");
      
      cci1.setFindCustomerQuery(fcq);
      body.getAny().add(cci1);
*/            
      // ContactcenterGiveDigipassChallenge2Input
/*      
      header.setServiceName("contactcenter.GiveDigipassChallenge_2_Input");
      ContactcenterGiveDigipassChallenge2Input cci2 = factory.createContactcenterGiveDigipassChallenge2Input();
      GiveDigipassQuery dq = factory.createGiveDigipassQuery();
      dq.setCustomerId("1234");
      dq.setIdCode("4321");
      cci2.setGiveDigipassQuery(dq);
      body.getAny().add(cci2);
*/      
      // ContactcenterCheckAuthenticationCode2Input
      
      header.setServiceName("contactcenter.CheckAuthenticationCode_2_Input");
      ContactcenterCheckAuthenticationCode2Input cci3 = factory.createContactcenterCheckAuthenticationCode2Input();
      AuthenticationQuery aq = factory.createAuthenticationQuery();
      aq.setAuthenticationCode("5678");
      aq.setChallengeCode("8765");
      aq.setUsername("clientname");
      cci3.setAuthenticationQuery(aq);
      body.getAny().add(cci3);
      
      request.setUnifiedServiceHeader(header);
      request.setUnifiedServiceBody(body);
      ByteArrayOutputStream ba = new ByteArrayOutputStream();
      marshaller.marshal(request, ba);

// remove unnecessary xmlns info in the case <xsdBasedRequest>false</ns2:xsdBasedRequest>,  JMS attribute "xsdBasedMessage" = false
      String s = RemoveXmlns(ba.toString());

// write to file      
	  FileWriter fileWriter = new FileWriter(xmlDocument);
	  fileWriter.write(s);
	  fileWriter.flush();
	  fileWriter.close();
   }
    catch (IOException e) {
      System.err.println(e.toString());

    }
    catch (JAXBException e) {

      System.err.println(e.toString());

    }

  }
// removes all xmlns related characters from a string  
  public String RemoveXmlns(String s)
  {
	  StringBuilder b = new StringBuilder(s);
	  int pos = b.indexOf("xmlns");
	  int pos2 = b.indexOf(">", pos);
	  b.delete(pos-1, pos2);
	  pos = b.indexOf(":");
	  pos2 = b.lastIndexOf("<", pos);
	  String remove = b.substring(pos2+1, pos+1);
	  String result = b.toString().replaceAll(remove,"");
	  return result;
  }

  public static void main(String[] argv) {
    //String xmlDocument = "FindCustomerByPhoneOrPersonalCode_2Input.xml";
	//String xmlDocument = "GiveDigipassChallenge_2Input.xml";
	String xmlDocument = "CheckAuthenticationCode_2Input.xml";
    JAXB2Marshaller jaxbMarshaller = new JAXB2Marshaller();
    jaxbMarshaller.generateXMLDocument(new File(xmlDocument));
  }
}