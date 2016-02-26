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

      UnifiedServiceHeader header = factory.createUnifiedServiceHeader();
      header.setChannel("ContactCenter");
      header.setClientApplication("IVR");
      header.setClientApplicationVersion("IVR 1.0");
      header.setCountryCode("EE");
      header.setOfficerId("admin");
      header.setRequestId("cc02ed3ebc-38ec-319c-9328-063c6fc93b55");
      header.setRemoteHost("test remote host");
      header.setRequestServerId("test server id");
      header.setResponseIsCompressed(false);
      header.setServiceName("contactcenter.FindCustomerByPhoneOrPersonalCode_2");
      header.setUserId("35503186196");
      header.setXsdBasedRequest(false);
      UnifiedServiceBody body = factory.createUnifiedServiceBody();
      UnifiedServiceRequest request = factory.createUnifiedServiceRequest();
      ContactcenterFindCustomerByPhoneOrPersonalCode2Input cci = 
    		  factory.createContactcenterFindCustomerByPhoneOrPersonalCode2Input();
      FindCustomerQuery fcq = factory.createFindCustomerQuery();
      fcq.setIdCode("35503186196");
      fcq.setUserPhoneNumber("");
      cci.setFindCustomerQuery(fcq);
      body.getAny().add(cci);
      request.setUnifiedServiceHeader(header);
      request.setUnifiedServiceBody(body);
      ByteArrayOutputStream ba = new ByteArrayOutputStream();
      marshaller.marshal(request, ba);

// remove unneeded xmlns info in the case <xsdBasedRequest>false</ns2:xsdBasedRequest>,  JMS attribute "xsdBasedMessage" = false
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
    String xmlDocument = "catalog.xml";
    JAXB2Marshaller jaxbMarshaller = new JAXB2Marshaller();
    jaxbMarshaller.generateXMLDocument(new File(xmlDocument));
  }
}