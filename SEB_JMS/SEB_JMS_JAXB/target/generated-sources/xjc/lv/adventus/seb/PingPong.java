//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.18 at 12:40:11 PM EET 
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
 *         &lt;element name="pingMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pongMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pingMessage",
    "pongMessage"
})
@XmlRootElement(name = "PingPong", namespace = "http://www.seb.ee/integration")
public class PingPong {

    @XmlElement(namespace = "http://www.seb.ee/integration")
    protected String pingMessage;
    @XmlElement(namespace = "http://www.seb.ee/integration")
    protected String pongMessage;

    /**
     * Gets the value of the pingMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPingMessage() {
        return pingMessage;
    }

    /**
     * Sets the value of the pingMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPingMessage(String value) {
        this.pingMessage = value;
    }

    /**
     * Gets the value of the pongMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPongMessage() {
        return pongMessage;
    }

    /**
     * Sets the value of the pongMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPongMessage(String value) {
        this.pongMessage = value;
    }

}
