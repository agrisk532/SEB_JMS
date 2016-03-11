package lv.adventus.seb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class ServicesBase {

    protected JAXBContext jaxbContext;
    protected Marshaller marshaller;
    protected ObjectFactory factory;
    protected UnifiedServiceRequest request;
    protected UnifiedServiceHeader header;
    protected UnifiedServiceBody body;
 
	public ServicesBase() throws JAXBException
	{
		this.jaxbContext = JAXBContext.newInstance("lv.adventus.seb");
		this.marshaller = jaxbContext.createMarshaller();
		this.marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
		this.factory  = new ObjectFactory();
		this.request = factory.createUnifiedServiceRequest();
		this.header  = factory.createUnifiedServiceHeader();
		this.body = factory.createUnifiedServiceBody();
	}
	
	public UnifiedServiceHeader GetHeader()
	{
		return this.header;
	}
	
	public void SetHeader(
							String channel,
							String clientApplication,
							String clientApplicationVersion,
							String countryCode,
							String language,
							String officerId,
							String requestId,
							String requestRemoteHost,
							String requestServerId,
							boolean responseIsCompressed,
							String userId,
							boolean xsdBasedRequest
						)
	{
	    header.setChannel(channel);
	    header.setClientApplication(clientApplication);
	    header.setClientApplicationVersion(clientApplicationVersion);
	    header.setCountryCode(countryCode);
	    header.setLanguage(language);
	    header.setOfficerId(officerId);
	    header.setRequestId(requestId);
	    header.setRequestRemoteHost(requestRemoteHost);
	    header.setRequestServerId(requestServerId);
	    header.setResponseIsCompressed(responseIsCompressed);
	    header.setUserId(userId);
	    header.setXsdBasedRequest(xsdBasedRequest);
	}
	
	public void SetHeader()
	{
		SetHeader(	"ContactCenter",
					"IVR",
					"IVR 1.0",
					"LV",
					"LV",
					"admin",
					"cc02ed3ebc-38ec-319c-9328-063c6fc93b55",
					"test remote host",
					"test server id",
					Boolean.FALSE,
					"35503186196",
					Boolean.FALSE
				);	
	}
	
	public void SetHeaderUserId(String userId)
	{
		header.setUserId(userId);
	}
	
	public void SetHeaderRequestId(String rid)
	{
		header.setRequestId(rid);
	}
	
	public void Marshal(String xmlDocument) throws IOException, JAXBException
	{
	      request.setUnifiedServiceHeader(header);
	      request.setUnifiedServiceBody(body);
	      ByteArrayOutputStream ba = new ByteArrayOutputStream();
	      marshaller.marshal(request, ba);

	// remove unnecessary xmlns info in the case <xsdBasedRequest>false</ns2:xsdBasedRequest>,  JMS attribute "xsdBasedMessage" = false
	      String s = RemoveXmlns(ba.toString());

	// write to file

		  FileWriter fileWriter = new FileWriter(new File(xmlDocument));
		  fileWriter.write(s);
		  fileWriter.flush();
		  fileWriter.close();
	   }
	
		public String Marshal() throws JAXBException
		{
		      request.setUnifiedServiceHeader(header);
		      request.setUnifiedServiceBody(body);
		      ByteArrayOutputStream ba = new ByteArrayOutputStream();
		      marshaller.marshal(request, ba);

		// remove unnecessary xmlns info in the case <xsdBasedRequest>false</ns2:xsdBasedRequest>,  JMS attribute "xsdBasedMessage" = false
		      String s = RemoveXmlns(ba.toString());
		      return s;
		}

//removes all xmlns related characters from a string  
	private String RemoveXmlns(String s)
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
}

	