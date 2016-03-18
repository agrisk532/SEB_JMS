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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for UnifiedServiceHeader complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UnifiedServiceHeader"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="requestId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="serviceName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="officerId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="xsdBasedRequest" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="responseIsCompressed" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="requestRemoteHost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="requestServerHost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="requestServerId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="responseServerHost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="responseServerId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="targetQueue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="backEndTransformerEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="additionalProPath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="authenticationMethod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="avatar" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="brand" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="channel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="clientApplication" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="clientApplicationVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="clientId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="countryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="debugBack" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="debugFront" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="pvkActive" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="department" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="fromP2pAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="homeBackend" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="info" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="numberOfRModules" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="oriSessionName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="remoteHost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="replayTo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="representativeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="representativeCustomerId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="separateProgressLog" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="sessionKey" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="timeStamp" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="toP2pAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="originalXmlFormat" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="currentUserSignatureId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="vmUniqueId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UnifiedServiceHeader", namespace = "http://www.seb.ee/integration", propOrder = {
    "requestId",
    "serviceName",
    "userId",
    "officerId",
    "xsdBasedRequest",
    "responseIsCompressed",
    "requestRemoteHost",
    "requestServerHost",
    "requestServerId",
    "responseServerHost",
    "responseServerId",
    "targetQueue",
    "backEndTransformerEnabled",
    "additionalProPath",
    "authenticationMethod",
    "avatar",
    "brand",
    "channel",
    "clientApplication",
    "clientApplicationVersion",
    "clientId",
    "countryCode",
    "debugBack",
    "debugFront",
    "pvkActive",
    "department",
    "fromP2PAddress",
    "homeBackend",
    "info",
    "language",
    "numberOfRModules",
    "oriSessionName",
    "remoteHost",
    "replayTo",
    "representativeId",
    "representativeCustomerId",
    "separateProgressLog",
    "sessionKey",
    "timeStamp",
    "toP2PAddress",
    "originalXmlFormat",
    "currentUserSignatureId",
    "vmUniqueId"
})
public class UnifiedServiceHeader {

    @XmlElement(required = true)
    protected String requestId;
    @XmlElement(required = true)
    protected String serviceName;
    protected String userId;
    protected String officerId;
    protected boolean xsdBasedRequest;
    protected boolean responseIsCompressed;
    protected String requestRemoteHost;
    protected String requestServerHost;
    protected String requestServerId;
    protected String responseServerHost;
    protected String responseServerId;
    protected String targetQueue;
    protected Boolean backEndTransformerEnabled;
    protected String additionalProPath;
    protected String authenticationMethod;
    protected String avatar;
    protected String brand;
    protected String channel;
    protected String clientApplication;
    protected String clientApplicationVersion;
    protected String clientId;
    protected String countryCode;
    protected Boolean debugBack;
    protected Boolean debugFront;
    protected Boolean pvkActive;
    protected String department;
    @XmlElement(name = "fromP2pAddress")
    protected String fromP2PAddress;
    protected String homeBackend;
    protected String info;
    @XmlElement(required = true)
    protected String language;
    protected Long numberOfRModules;
    protected String oriSessionName;
    protected String remoteHost;
    protected String replayTo;
    protected String representativeId;
    protected String representativeCustomerId;
    protected Boolean separateProgressLog;
    @XmlElement(required = true)
    protected String sessionKey;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timeStamp;
    @XmlElement(name = "toP2pAddress")
    protected String toP2PAddress;
    protected Boolean originalXmlFormat;
    protected String currentUserSignatureId;
    protected String vmUniqueId;

    /**
     * Gets the value of the requestId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the value of the requestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestId(String value) {
        this.requestId = value;
    }

    /**
     * Gets the value of the serviceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Sets the value of the serviceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceName(String value) {
        this.serviceName = value;
    }

    /**
     * Gets the value of the userId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserId(String value) {
        this.userId = value;
    }

    /**
     * Gets the value of the officerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOfficerId() {
        return officerId;
    }

    /**
     * Sets the value of the officerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOfficerId(String value) {
        this.officerId = value;
    }

    /**
     * Gets the value of the xsdBasedRequest property.
     * 
     */
    public boolean isXsdBasedRequest() {
        return xsdBasedRequest;
    }

    /**
     * Sets the value of the xsdBasedRequest property.
     * 
     */
    public void setXsdBasedRequest(boolean value) {
        this.xsdBasedRequest = value;
    }

    /**
     * Gets the value of the responseIsCompressed property.
     * 
     */
    public boolean isResponseIsCompressed() {
        return responseIsCompressed;
    }

    /**
     * Sets the value of the responseIsCompressed property.
     * 
     */
    public void setResponseIsCompressed(boolean value) {
        this.responseIsCompressed = value;
    }

    /**
     * Gets the value of the requestRemoteHost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestRemoteHost() {
        return requestRemoteHost;
    }

    /**
     * Sets the value of the requestRemoteHost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestRemoteHost(String value) {
        this.requestRemoteHost = value;
    }

    /**
     * Gets the value of the requestServerHost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestServerHost() {
        return requestServerHost;
    }

    /**
     * Sets the value of the requestServerHost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestServerHost(String value) {
        this.requestServerHost = value;
    }

    /**
     * Gets the value of the requestServerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestServerId() {
        return requestServerId;
    }

    /**
     * Sets the value of the requestServerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestServerId(String value) {
        this.requestServerId = value;
    }

    /**
     * Gets the value of the responseServerHost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseServerHost() {
        return responseServerHost;
    }

    /**
     * Sets the value of the responseServerHost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseServerHost(String value) {
        this.responseServerHost = value;
    }

    /**
     * Gets the value of the responseServerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseServerId() {
        return responseServerId;
    }

    /**
     * Sets the value of the responseServerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseServerId(String value) {
        this.responseServerId = value;
    }

    /**
     * Gets the value of the targetQueue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetQueue() {
        return targetQueue;
    }

    /**
     * Sets the value of the targetQueue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetQueue(String value) {
        this.targetQueue = value;
    }

    /**
     * Gets the value of the backEndTransformerEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBackEndTransformerEnabled() {
        return backEndTransformerEnabled;
    }

    /**
     * Sets the value of the backEndTransformerEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBackEndTransformerEnabled(Boolean value) {
        this.backEndTransformerEnabled = value;
    }

    /**
     * Gets the value of the additionalProPath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalProPath() {
        return additionalProPath;
    }

    /**
     * Sets the value of the additionalProPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalProPath(String value) {
        this.additionalProPath = value;
    }

    /**
     * Gets the value of the authenticationMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticationMethod() {
        return authenticationMethod;
    }

    /**
     * Sets the value of the authenticationMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticationMethod(String value) {
        this.authenticationMethod = value;
    }

    /**
     * Gets the value of the avatar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Sets the value of the avatar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvatar(String value) {
        this.avatar = value;
    }

    /**
     * Gets the value of the brand property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the value of the brand property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBrand(String value) {
        this.brand = value;
    }

    /**
     * Gets the value of the channel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Sets the value of the channel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannel(String value) {
        this.channel = value;
    }

    /**
     * Gets the value of the clientApplication property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientApplication() {
        return clientApplication;
    }

    /**
     * Sets the value of the clientApplication property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientApplication(String value) {
        this.clientApplication = value;
    }

    /**
     * Gets the value of the clientApplicationVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientApplicationVersion() {
        return clientApplicationVersion;
    }

    /**
     * Sets the value of the clientApplicationVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientApplicationVersion(String value) {
        this.clientApplicationVersion = value;
    }

    /**
     * Gets the value of the clientId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Sets the value of the clientId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientId(String value) {
        this.clientId = value;
    }

    /**
     * Gets the value of the countryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the value of the countryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryCode(String value) {
        this.countryCode = value;
    }

    /**
     * Gets the value of the debugBack property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDebugBack() {
        return debugBack;
    }

    /**
     * Sets the value of the debugBack property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDebugBack(Boolean value) {
        this.debugBack = value;
    }

    /**
     * Gets the value of the debugFront property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDebugFront() {
        return debugFront;
    }

    /**
     * Sets the value of the debugFront property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDebugFront(Boolean value) {
        this.debugFront = value;
    }

    /**
     * Gets the value of the pvkActive property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPvkActive() {
        return pvkActive;
    }

    /**
     * Sets the value of the pvkActive property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPvkActive(Boolean value) {
        this.pvkActive = value;
    }

    /**
     * Gets the value of the department property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the value of the department property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartment(String value) {
        this.department = value;
    }

    /**
     * Gets the value of the fromP2PAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromP2PAddress() {
        return fromP2PAddress;
    }

    /**
     * Sets the value of the fromP2PAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromP2PAddress(String value) {
        this.fromP2PAddress = value;
    }

    /**
     * Gets the value of the homeBackend property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomeBackend() {
        return homeBackend;
    }

    /**
     * Sets the value of the homeBackend property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomeBackend(String value) {
        this.homeBackend = value;
    }

    /**
     * Gets the value of the info property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo() {
        return info;
    }

    /**
     * Sets the value of the info property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo(String value) {
        this.info = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguage(String value) {
        this.language = value;
    }

    /**
     * Gets the value of the numberOfRModules property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNumberOfRModules() {
        return numberOfRModules;
    }

    /**
     * Sets the value of the numberOfRModules property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNumberOfRModules(Long value) {
        this.numberOfRModules = value;
    }

    /**
     * Gets the value of the oriSessionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriSessionName() {
        return oriSessionName;
    }

    /**
     * Sets the value of the oriSessionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriSessionName(String value) {
        this.oriSessionName = value;
    }

    /**
     * Gets the value of the remoteHost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemoteHost() {
        return remoteHost;
    }

    /**
     * Sets the value of the remoteHost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemoteHost(String value) {
        this.remoteHost = value;
    }

    /**
     * Gets the value of the replayTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReplayTo() {
        return replayTo;
    }

    /**
     * Sets the value of the replayTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReplayTo(String value) {
        this.replayTo = value;
    }

    /**
     * Gets the value of the representativeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepresentativeId() {
        return representativeId;
    }

    /**
     * Sets the value of the representativeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepresentativeId(String value) {
        this.representativeId = value;
    }

    /**
     * Gets the value of the representativeCustomerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepresentativeCustomerId() {
        return representativeCustomerId;
    }

    /**
     * Sets the value of the representativeCustomerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepresentativeCustomerId(String value) {
        this.representativeCustomerId = value;
    }

    /**
     * Gets the value of the separateProgressLog property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSeparateProgressLog() {
        return separateProgressLog;
    }

    /**
     * Sets the value of the separateProgressLog property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSeparateProgressLog(Boolean value) {
        this.separateProgressLog = value;
    }

    /**
     * Gets the value of the sessionKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionKey() {
        return sessionKey;
    }

    /**
     * Sets the value of the sessionKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionKey(String value) {
        this.sessionKey = value;
    }

    /**
     * Gets the value of the timeStamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the value of the timeStamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTimeStamp(XMLGregorianCalendar value) {
        this.timeStamp = value;
    }

    /**
     * Gets the value of the toP2PAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToP2PAddress() {
        return toP2PAddress;
    }

    /**
     * Sets the value of the toP2PAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToP2PAddress(String value) {
        this.toP2PAddress = value;
    }

    /**
     * Gets the value of the originalXmlFormat property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOriginalXmlFormat() {
        return originalXmlFormat;
    }

    /**
     * Sets the value of the originalXmlFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOriginalXmlFormat(Boolean value) {
        this.originalXmlFormat = value;
    }

    /**
     * Gets the value of the currentUserSignatureId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentUserSignatureId() {
        return currentUserSignatureId;
    }

    /**
     * Sets the value of the currentUserSignatureId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentUserSignatureId(String value) {
        this.currentUserSignatureId = value;
    }

    /**
     * Gets the value of the vmUniqueId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVmUniqueId() {
        return vmUniqueId;
    }

    /**
     * Sets the value of the vmUniqueId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVmUniqueId(String value) {
        this.vmUniqueId = value;
    }

}
