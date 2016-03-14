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

	private UnifiedServiceResponse usr = null;
	
  public UnifiedServiceResponse unMarshall(File xmlDocument) {
	  
    try {

      JAXBContext jaxbContext = JAXBContext.newInstance(UnifiedServiceResponse.class);
      Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
      InputStream isMain = this.getClass().getResourceAsStream( "src/main/resources/contactcenter.xsd" );
      InputStream isImport = this.getClass().getResourceAsStream( "src/main/resources/integration.xsd" );
      Source imp = new StreamSource( isImport );
      Source main = new StreamSource( isMain  );
//
      Source[] schemaFiles = new Source[] { imp, main };
//
      //SchemaFactory sf = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
      //Schema schema = sf.newSchema( schemaFiles );
      //CustomValidationEventHandler validationEventHandler = new CustomValidationEventHandler();
      //unMarshaller.setEventHandler(validationEventHandler);
      //unMarshaller.setSchema(schema);

//      JAXBElement<UnifiedServiceResponse> catalogElement = 
//    		  ((JAXBElement<UnifiedServiceResponse>) unMarshaller.unmarshal(xmlDocument));

      
// mangle namespaces in the xmlDocument
      String s = JAXB2UnMarshaller.readFile(xmlDocument,"utf8");
      int pos = s.indexOf("UnifiedServiceResponse") + (new String("UnifiedServiceResponse")).length();
      int pos2 = s.indexOf(">", pos);
      s = s.replace(s.substring(pos, pos2), " xmlns=\"http://www.seb.ee/integration\"");
      
      InputStream stream = new ByteArrayInputStream(s.getBytes());
      
      //UnifiedServiceResponse usr = (UnifiedServiceResponse) unMarshaller.unmarshal(stream);
     
      usr = (UnifiedServiceResponse) unMarshaller.unmarshal(stream); 

    }
    catch (JAXBException e)
    {
    	System.err.println(e.getMessage());
    }
    catch (IOException e)
    {
    	System.err.println(e.getMessage());
    }
    
    return usr;
  }
  
  static String readFile(File file, String charset)
	        throws IOException
	{
	    FileInputStream fileInputStream = new FileInputStream(file);
	    byte[] buffer = new byte[fileInputStream.available()];
	    int length = fileInputStream.read(buffer);
	    fileInputStream.close();
	    return new String(buffer, 0, length, charset);
	}
  
  public static void main(String[] argv) {
    //File xmlDocument = new File("CheckAuthenticationCode_2Output.xml");
    File xmlDocument = new File("FindCustomerByPhoneOrPersonalCode_2Output2.xml");
    //File xmlDocument = new File("GiveDigipassChallenge_2Output.xml");
//	  File xmlDocument = new File("PingPong_2Output.xml");
    System.out.println(xmlDocument.exists()); // prints true if a file exists at that location
    System.out.println(xmlDocument.getAbsoluteFile());// prints "c:\\eclipse\\eclipse.ini"
    JAXB2UnMarshaller jaxbUnmarshaller = new JAXB2UnMarshaller();

    UnifiedServiceResponse usr = jaxbUnmarshaller.unMarshall(xmlDocument);
    System.out.println(usr.getUnifiedServiceHeader().getRequestId());
    if(usr.getUnifiedServiceErrors() != null)
    	System.out.println("There are errors.");
    System.exit(0);

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