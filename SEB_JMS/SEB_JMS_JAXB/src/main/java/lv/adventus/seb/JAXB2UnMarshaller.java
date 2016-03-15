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

	//private UnifiedServiceResponse usr = null;
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

// add missing lines to the XML response, if Sonic is not sending them
//      
// for GiveDigipassChallenge()
//      
//      StringBuilder sb = new StringBuilder(s);
//      String s1 = "<unifiedServiceBody>";
//      String s2 = "<contactcenter:contactcenter.GiveDigipassChallenge_2_Output xmlns:cmn=\"http://www.seb.ee/common\" xmlns:contactcenter=\"http://www.seb.ee/contactcenter\">"; 
//      int pos3 = sb.indexOf(s1);
//	    pos3 += s1.length();
//	    sb.insert(pos3, s2);
//	   
//	    s1 = "</contactcenter:GiveChallengeResponse>";
//	    s2 = "</contactcenter:contactcenter.GiveDigipassChallenge_2_Output>";
//	    pos3 = sb.indexOf(s1);
//	    pos3 += s1.length();
//	    sb.insert(pos3, s2);
//	    
//	     s = sb.toString(); 
//	    
//  for CheckAuthenticationCode()
//    
//	    String s1 = "<unifiedServiceBody>";
//	    StringBuilder sb = new StringBuilder(s);
//	    String s2 = "<contactcenter:contactcenter.CheckAuthenticationCode_2_Output xmlns:cmn=\"http://www.seb.ee/common\" xmlns:contactcenter=\"http://www.seb.ee/contactcenter\">"; 
//	    int pos3 = sb.indexOf(s1);
//	    pos3 += s1.length();
//	    sb.insert(pos3, s2);
//	    s1 = "</contactcenter:AuthenticationResponse>";
//	    s2 = "</contactcenter:contactcenter.CheckAuthenticationCode_2_Output>";
//	    pos3 = sb.indexOf(s1);
//	    pos3 += s1.length();
//	    sb.insert(pos3, s2);
//	    s = sb.toString(); 

      	InputStream stream = new ByteArrayInputStream(s.getBytes("UTF-8"));
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
    File xmlDocument = new File("CheckAuthenticationCode_2Output.xml");
    //File xmlDocument = new File("FindCustomerByPhoneOrPersonalCode_2Output2.xml");
    //File xmlDocument = new File("GiveDigipassChallenge_2Output.xml");
//	  File xmlDocument = new File("PingPong_2Output.xml");
    System.out.println(xmlDocument.exists()); // prints true if a file exists at that location
    System.out.println(xmlDocument.getAbsoluteFile());// prints "c:\\eclipse\\eclipse.ini"
    JAXB2UnMarshaller jaxbUnmarshaller = new JAXB2UnMarshaller();

    UnifiedServiceResponse usr = jaxbUnmarshaller.unMarshall(xmlDocument);
    System.out.println(usr.getUnifiedServiceHeader().getRequestId());
    
//	ContactcenterGiveDigipassChallenge2Output gco = (ContactcenterGiveDigipassChallenge2Output) usr.getUnifiedServiceBody().getAny().get(0);
//    String customerId = gco.getGiveChallengeResponse().getCustomerId();
//	String challengeCode = gco.getGiveChallengeResponse().getChallengeCode(); 
//	String userName = gco.getGiveChallengeResponse().getUsername();
//	String idCode = gco.getGiveChallengeResponse().getIdCode();
//	System.out.println("Answer from JMS Broker:");
//	System.out.println("GiveDigipassChallenge: customerId = " + customerId);
//	System.out.println("GiveDigipassChallenge: challengeCode = " + challengeCode);
//	System.out.println("GiveDigipassChallenge: userName = " + userName);
//	System.out.println("GiveDigipassChallenge: idCode = " + idCode);

		ContactcenterCheckAuthenticationCode2Output cac = (ContactcenterCheckAuthenticationCode2Output) usr.getUnifiedServiceBody().getAny().get(0);

		String authenticationCode = cac.getAuthenticationResponse().getAuthenticationCode();
		String userName = cac.getAuthenticationResponse().getUsername();
		String challengeCode = cac.getAuthenticationResponse().getChallengeCode();
		System.out.println("Answer from JMS Broker:");
	    System.out.println("CheckAuthenticationCode: digipasscode = " + authenticationCode);
	    System.out.println("CheckAuthenticationCode: challengecode = " + challengeCode);
	    System.out.println("CheckAuthenticationCode: username = " + userName);
    
		UnifiedServiceErrors errors = usr.getUnifiedServiceErrors();
		if(errors != null)
		{
		  String errClass = null;
		  String errCode = null;
		  String errObject = null;
		  for (int i = 0; i < errors.getError().size(); i++)
		  {
			  errClass = errors.getError().get(i).getErrorClass();
			  errCode = errors.getError().get(i).getErrorCode();
			  errObject = errors.getError().get(i).getErrorObject().getValue();
			  System.out.println(errClass);
			  System.out.println(errCode);
			  System.out.println(errObject);
		  }
		}
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