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
 *         &lt;element name="GiveDigipassQuery" type="{http://www.seb.ee/contactcenter}GiveDigipassQuery"/&gt;
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
    "giveDigipassQuery"
})
@XmlRootElement(name = "contactcenter.GiveDigipassChallenge_2_Input")
public class ContactcenterGiveDigipassChallenge2Input {

    @XmlElement(name = "GiveDigipassQuery", required = true)
    protected GiveDigipassQuery giveDigipassQuery;

    /**
     * Gets the value of the giveDigipassQuery property.
     * 
     * @return
     *     possible object is
     *     {@link GiveDigipassQuery }
     *     
     */
    public GiveDigipassQuery getGiveDigipassQuery() {
        return giveDigipassQuery;
    }

    /**
     * Sets the value of the giveDigipassQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link GiveDigipassQuery }
     *     
     */
    public void setGiveDigipassQuery(GiveDigipassQuery value) {
        this.giveDigipassQuery = value;
    }

}
