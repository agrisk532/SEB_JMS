//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.25 at 04:07:51 PM EET 
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
 *         &lt;element name="GiveChallengeResponse" type="{http://www.seb.ee/contactcenter}GiveDigipassResponse"/&gt;
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
    "giveChallengeResponse"
})
@XmlRootElement(name = "contactcenter.GiveDigipassChallenge_2_Output")
public class ContactcenterGiveDigipassChallenge2Output {

    @XmlElement(name = "GiveChallengeResponse", required = true)
    protected GiveDigipassResponse giveChallengeResponse;

    /**
     * Gets the value of the giveChallengeResponse property.
     * 
     * @return
     *     possible object is
     *     {@link GiveDigipassResponse }
     *     
     */
    public GiveDigipassResponse getGiveChallengeResponse() {
        return giveChallengeResponse;
    }

    /**
     * Sets the value of the giveChallengeResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link GiveDigipassResponse }
     *     
     */
    public void setGiveChallengeResponse(GiveDigipassResponse value) {
        this.giveChallengeResponse = value;
    }

}
