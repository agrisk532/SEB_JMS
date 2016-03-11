//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.11 at 04:22:36 PM EET 
//


package lv.adventus.seb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the lv.adventus.seb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ErrorErrorMessage_QNAME = new QName("http://www.seb.ee/integration", "errorMessage");
    private final static QName _ErrorErrorObject_QNAME = new QName("http://www.seb.ee/integration", "errorObject");
    private final static QName _ErrorErrorParameter_QNAME = new QName("http://www.seb.ee/integration", "errorParameter");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: lv.adventus.seb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ContactcenterFindCustomerByPhoneOrPersonalCode2Input }
     * 
     */
    public ContactcenterFindCustomerByPhoneOrPersonalCode2Input createContactcenterFindCustomerByPhoneOrPersonalCode2Input() {
        return new ContactcenterFindCustomerByPhoneOrPersonalCode2Input();
    }

    /**
     * Create an instance of {@link FindCustomerQuery }
     * 
     */
    public FindCustomerQuery createFindCustomerQuery() {
        return new FindCustomerQuery();
    }

    /**
     * Create an instance of {@link ContactcenterFindCustomerByPhoneOrPersonalCode2Output }
     * 
     */
    public ContactcenterFindCustomerByPhoneOrPersonalCode2Output createContactcenterFindCustomerByPhoneOrPersonalCode2Output() {
        return new ContactcenterFindCustomerByPhoneOrPersonalCode2Output();
    }

    /**
     * Create an instance of {@link FindCustomerResponse }
     * 
     */
    public FindCustomerResponse createFindCustomerResponse() {
        return new FindCustomerResponse();
    }

    /**
     * Create an instance of {@link ContactcenterGiveDigipassChallenge2Input }
     * 
     */
    public ContactcenterGiveDigipassChallenge2Input createContactcenterGiveDigipassChallenge2Input() {
        return new ContactcenterGiveDigipassChallenge2Input();
    }

    /**
     * Create an instance of {@link GiveDigipassQuery }
     * 
     */
    public GiveDigipassQuery createGiveDigipassQuery() {
        return new GiveDigipassQuery();
    }

    /**
     * Create an instance of {@link ContactcenterGiveDigipassChallenge2Output }
     * 
     */
    public ContactcenterGiveDigipassChallenge2Output createContactcenterGiveDigipassChallenge2Output() {
        return new ContactcenterGiveDigipassChallenge2Output();
    }

    /**
     * Create an instance of {@link GiveDigipassResponse }
     * 
     */
    public GiveDigipassResponse createGiveDigipassResponse() {
        return new GiveDigipassResponse();
    }

    /**
     * Create an instance of {@link ContactcenterCheckAuthenticationCode2Input }
     * 
     */
    public ContactcenterCheckAuthenticationCode2Input createContactcenterCheckAuthenticationCode2Input() {
        return new ContactcenterCheckAuthenticationCode2Input();
    }

    /**
     * Create an instance of {@link AuthenticationQuery }
     * 
     */
    public AuthenticationQuery createAuthenticationQuery() {
        return new AuthenticationQuery();
    }

    /**
     * Create an instance of {@link ContactcenterCheckAuthenticationCode2Output }
     * 
     */
    public ContactcenterCheckAuthenticationCode2Output createContactcenterCheckAuthenticationCode2Output() {
        return new ContactcenterCheckAuthenticationCode2Output();
    }

    /**
     * Create an instance of {@link UnifiedServiceRequest }
     * 
     */
    public UnifiedServiceRequest createUnifiedServiceRequest() {
        return new UnifiedServiceRequest();
    }

    /**
     * Create an instance of {@link UnifiedServiceHeader }
     * 
     */
    public UnifiedServiceHeader createUnifiedServiceHeader() {
        return new UnifiedServiceHeader();
    }

    /**
     * Create an instance of {@link UnifiedServiceBody }
     * 
     */
    public UnifiedServiceBody createUnifiedServiceBody() {
        return new UnifiedServiceBody();
    }

    /**
     * Create an instance of {@link UnifiedServiceResponse }
     * 
     */
    public UnifiedServiceResponse createUnifiedServiceResponse() {
        return new UnifiedServiceResponse();
    }

    /**
     * Create an instance of {@link UnifiedServiceErrors }
     * 
     */
    public UnifiedServiceErrors createUnifiedServiceErrors() {
        return new UnifiedServiceErrors();
    }

    /**
     * Create an instance of {@link PingPong }
     * 
     */
    public PingPong createPingPong() {
        return new PingPong();
    }

    /**
     * Create an instance of {@link Error }
     * 
     */
    public Error createError() {
        return new Error();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.seb.ee/integration", name = "errorMessage", scope = Error.class)
    public JAXBElement<String> createErrorErrorMessage(String value) {
        return new JAXBElement<String>(_ErrorErrorMessage_QNAME, String.class, Error.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.seb.ee/integration", name = "errorObject", scope = Error.class)
    public JAXBElement<String> createErrorErrorObject(String value) {
        return new JAXBElement<String>(_ErrorErrorObject_QNAME, String.class, Error.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.seb.ee/integration", name = "errorParameter", scope = Error.class)
    public JAXBElement<String> createErrorErrorParameter(String value) {
        return new JAXBElement<String>(_ErrorErrorParameter_QNAME, String.class, Error.class, value);
    }

}
