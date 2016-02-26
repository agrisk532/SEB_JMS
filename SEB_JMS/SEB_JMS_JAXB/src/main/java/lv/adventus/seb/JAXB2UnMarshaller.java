package lv.adventus.seb;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import org.xml.sax.SAXException;
import java.io.*;

public class JAXB2UnMarshaller {

  public void unMarshall(File xmlDocument) {
    try {

      JAXBContext jaxbContext = JAXBContext.newInstance(UnifiedServiceResponse.class);
      Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
//      final InputStream isMain = this.getClass().getResourceAsStream( "contactcenter.xsd" );
//      final InputStream isImport = this.getClass().getResourceAsStream( "integration.xsd" );
//      final Source imp = new StreamSource( isImport );
//      final Source main = new StreamSource( isMain  );
//
//      final Source[] schemaFiles = new Source[] { imp, main };
//
//      final SchemaFactory sf = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
//      final Schema schema = sf.newSchema( schemaFiles );
      CustomValidationEventHandler validationEventHandler = new CustomValidationEventHandler();
      unMarshaller.setEventHandler(validationEventHandler);
//      unMarshaller.setSchema(schema);

//      JAXBElement<UnifiedServiceResponse> catalogElement = 
//    		  ((JAXBElement<UnifiedServiceResponse>) unMarshaller.unmarshal(xmlDocument));

      UnifiedServiceResponse usr = (UnifiedServiceResponse) unMarshaller.unmarshal(xmlDocument);
      System.out.println(usr.getUnifiedServiceHeader().getRequestId());
      System.out.println(usr.getUnifiedServiceHeader().getLanguage());
      System.out.println(usr.getUnifiedServiceHeader().getServiceName());
      System.out.println(usr.getUnifiedServiceBody().getAny().get(0));
      if(usr.getUnifiedServiceBody().getAny().get(0).getClass() == ContactcenterFindCustomerByPhoneOrPersonalCode2Output.class)
      {
    	  ContactcenterFindCustomerByPhoneOrPersonalCode2Output o =
    			  (ContactcenterFindCustomerByPhoneOrPersonalCode2Output) usr.getUnifiedServiceBody().getAny().get(0);
    	  System.out.println(o.getFindCustomerResponse().getFirstName());
    	  
      }

    }
    catch (JAXBException e) {
      System.err.println(e.getMessage());
    }
  }

  public static void main(String[] argv) {
    File xmlDocument = new File("catalog1.xml");
    System.out.println(xmlDocument.exists()); // prints true if a file exists at that location
    System.out.println(xmlDocument.getAbsoluteFile());// prints "c:\\eclipse\\eclipse.ini"
    JAXB2UnMarshaller jaxbUnmarshaller = new JAXB2UnMarshaller();

    jaxbUnmarshaller.unMarshall(xmlDocument);

  }

  class CustomValidationEventHandler implements ValidationEventHandler {
    public boolean handleEvent(ValidationEvent event) {
      if (event.getSeverity() == ValidationEvent.WARNING) {
        return true;
      }
      if ((event.getSeverity() == ValidationEvent.ERROR)
          || (event.getSeverity() == ValidationEvent.FATAL_ERROR)) {

        System.err.println("Validation Error:" + event.getMessage());

        ValidationEventLocator locator = event.getLocator();
        System.err.println("at line number:" + locator.getLineNumber());
        System.err.println("Unmarshalling Terminated");
        return false;
      }
      return true;
    }

  } 
}  