//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.22 at 11:39:58 AM EET 
//


package lv.adventus.seb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="unifiedServiceHeader" type="{http://www.seb.ee/integration}UnifiedServiceHeader"/&gt;
 *         &lt;element name="unifiedServiceBody" type="{http://www.seb.ee/integration}UnifiedServiceBody" minOccurs="0"/&gt;
 *         &lt;element name="unifiedServiceErrors" type="{http://www.seb.ee/integration}UnifiedServiceErrors" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "unifiedServiceHeader",
    "unifiedServiceBody",
    "unifiedServiceErrors"
})
@XmlRootElement(name = "UnifiedServiceResponse", namespace = "http://www.seb.ee/integration")
public class UnifiedServiceResponse {

    @XmlElement(namespace = "http://www.seb.ee/integration", required = true)
    protected UnifiedServiceHeader unifiedServiceHeader;
    @XmlElement(namespace = "http://www.seb.ee/integration")
    protected UnifiedServiceBody unifiedServiceBody;
    @XmlElement(namespace = "http://www.seb.ee/integration")
    protected UnifiedServiceErrors unifiedServiceErrors;

    /**
     * Gets the value of the unifiedServiceHeader property.
     * 
     * @return
     *     possible object is
     *     {@link UnifiedServiceHeader }
     *     
     */
    public UnifiedServiceHeader getUnifiedServiceHeader() {
        return unifiedServiceHeader;
    }

    /**
     * Sets the value of the unifiedServiceHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnifiedServiceHeader }
     *     
     */
    public void setUnifiedServiceHeader(UnifiedServiceHeader value) {
        this.unifiedServiceHeader = value;
    }

    /**
     * Gets the value of the unifiedServiceBody property.
     * 
     * @return
     *     possible object is
     *     {@link UnifiedServiceBody }
     *     
     */
    public UnifiedServiceBody getUnifiedServiceBody() {
        return unifiedServiceBody;
    }

    /**
     * Sets the value of the unifiedServiceBody property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnifiedServiceBody }
     *     
     */
    public void setUnifiedServiceBody(UnifiedServiceBody value) {
        this.unifiedServiceBody = value;
    }

    /**
     * Gets the value of the unifiedServiceErrors property.
     * 
     * @return
     *     possible object is
     *     {@link UnifiedServiceErrors }
     *     
     */
    public UnifiedServiceErrors getUnifiedServiceErrors() {
        return unifiedServiceErrors;
    }

    /**
     * Sets the value of the unifiedServiceErrors property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnifiedServiceErrors }
     *     
     */
    public void setUnifiedServiceErrors(UnifiedServiceErrors value) {
        this.unifiedServiceErrors = value;
    }

}
