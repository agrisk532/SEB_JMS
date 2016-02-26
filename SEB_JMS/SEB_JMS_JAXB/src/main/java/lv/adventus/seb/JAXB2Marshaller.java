package lv.adventus.seb;

import javax.xml.bind.*;
import java.io.File;
import java.io.FileOutputStream;
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
      marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "");
      marshaller.marshal(request, new FileOutputStream(xmlDocument));

    } catch (IOException e) {
      System.err.println(e.toString());

    } catch (JAXBException e) {

      System.err.println(e.toString());

    }

  }

  public static void main(String[] argv) {
    String xmlDocument = "catalog.xml";
    JAXB2Marshaller jaxbMarshaller = new JAXB2Marshaller();
    jaxbMarshaller.generateXMLDocument(new File(xmlDocument));
  }
}