----------------------------------------------------
If there is the following error in Genesys URS logs:
----------------------------------------------------

09/01/16@16:21:04: [HTTP Handler 1c7cc20] Ref 22324. Attempting to contact Web Server
    HTTP Method:    GET
    URL: http://wsp5518a.baltic.seb.net:8050/SEB_JMS_ORCHESTRA/GetChallengeCodeByPersonalCode?id=24098710007&connid=00a8027af2d3e9740
    HTTP Auth:    Anonymous
09/01/16@16:21:04: [HTTP Client 1c7cd70] Connecting to host wsp5518a.baltic.seb.net, port 8050
09/01/16@16:21:04: [HTTP Client 1c7cd70] Connected on socket 9
09/01/16@16:21:04: [HTTP Client 1c7cd70] Request sent:
GET /SEB_JMS_ORCHESTRA/GetChallengeCodeByPersonalCode?id=24098710007&connid=00a8027af2d3e9740 HTTP/1.1
Host: wsp5518a.baltic.seb.net:8050
User-Agent: gSOAP/2.7
Connection: keep-alive


09/01/16@16:21:14: [HTTP Client 1c7cd70] Received 2189 bytes from server on socket 9:
HTTP/1.1 500
Server: Apache-Coyote/1.1
Set-Cookie: JSESSIONID=03EEC6EA2E12097E2DE10CFD700C56F9;path=/SEB_JMS_ORCHESTRA/;HttpOnly
Content-Type: text/html;charset=UTF-8
Content-Language: en
Content-Length: 1915
Date: Thu, 01 Sep 2016 13:21:14 GMT
Connection: close

<!DOCTYPE html><html><head><title>Apache Tomcat/9.0.0.M3 - Error report</title><style type="text/css">H1 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:22px;} H2 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:16px;} H3 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:14px;} BODY {font-family:Tahoma,Arial,sans-serif;color:black;background-color:white;} B {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;} P {font-family:Tahoma,Arial,sans-serif;background:white;color:black;font-size:12px;}A {color : black;}A.name {color : black;}.line {height: 1px; background-color: #525D76; border: none;}</style> </head><body><h1>HTTP Status 500 - com.sun.org.apache.xerces.internal.dom.ElementNSImpl cannot be cast to lv.adventus.seb.ContactcenterGiveDigipassChallenge2Output</h1><div class="line"></div><p><b>type</b> Exception report</p><p><b>message</b> <u>com.sun.org.apache.xerces.internal.dom.ElementNSImpl cannot be cast to lv.adventus.seb.ContactcenterGiveDigipassChallenge2Output</u></p><p><b>description</b> <u>The server encountered an internal error that prevented it from fulfilling this request.</u></p><p><b>exception</b></p><pre>java.lang.ClassCastException: com.sun.org.apache.xerces.internal.dom.ElementNSImpl cannot be cast to lv.adventus.seb.ContactcenterGiveDigipassChallenge2Output
lv.adventus.seb.servlets.FindCustomerByPhoneOrPersonalCode.doGet(FindCustomerByPhoneOrPersonalCode.java:192)
    javax.servlet.http.HttpServlet.service(HttpServlet.java:622)
    javax.servlet.http.HttpServlet.service(HttpServlet.java:729)
org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)
</pre><p><b>note</b> <u>The full stack trace of the root cause is available in the Apache Tomcat/9.0.0.M3 logs.</u></p><hr class="line"><h3>Apache Tomcat/9.0.0.M3</h3></body></html> 

--------------------------------------------------------------------------
Then look for errors in the received from JMS <unifiedServiceBody> format:
--------------------------------------------------------------------------

Message with errors:

<?xml version="1.0" encoding="UTF-8" ?>
<UnifiedServiceResponse xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <unifiedServiceHeader>
    <requestId>cc00a8027af2d3e97401</requestId>
    <serviceName>contactcenter.GiveDigipassChallenge_2</serviceName>
    <userId>M00013</userId>
    <officerId>admin</officerId>
    <xsdBasedRequest>false</xsdBasedRequest>
    <responseIsCompressed>false</responseIsCompressed>
    <authenticationMethod/>
    <avatar/>
    <brand/>
    <channel>ContactCenter</channel>
    <clientId/>
    <countryCode>LV</countryCode>
    <department>0</department>
    <homeBackend/>
    <info/>
    <language>LV</language>
    <numberOfRModules>0</numberOfRModules>
    <representativeId/>
    <representativeCustomerId/>
    <sessionKey/>
    <timeStamp>2016-09-01T16:21:13.936</timeStamp>
  </unifiedServiceHeader>
<unifiedServiceBody>
  <contactcenter:GiveChallengeResponse xmlns:contactcenter="http://www.seb.ee/contactcenter">
    <contactcenter:customerId>M00013</contactcenter:customerId>
    <contactcenter:challengeCode>5654</contactcenter:challengeCode>
    <contactcenter:username>Raa1234</contactcenter:username>
    <contactcenter:idCode>240987-10007</contactcenter:idCode>
  </contactcenter:GiveChallengeResponse>
</unifiedServiceBody></UnifiedServiceResponse>

Correct message:

<?xml version="1.0" encoding="UTF-8" ?>
<UnifiedServiceResponse xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <unifiedServiceHeader>
    <requestId>cc00a8027af2d3e97401</requestId>
    <serviceName>contactcenter.GiveDigipassChallenge_2</serviceName>
    <userId>M00013</userId>
    <officerId>admin</officerId>
    <xsdBasedRequest>false</xsdBasedRequest>
    <responseIsCompressed>false</responseIsCompressed>
    <authenticationMethod/>
    <avatar/>
    <brand/>
    <channel>ContactCenter</channel>
    <clientId/>
    <countryCode>LV</countryCode>
    <department>0</department>
    <homeBackend/>
    <info/>
    <language>LV</language>
    <numberOfRModules>0</numberOfRModules>
    <representativeId/>
    <representativeCustomerId/>
    <sessionKey/>
    <timeStamp>2016-09-01T16:21:13.936</timeStamp>
  </unifiedServiceHeader>
<unifiedServiceBody>
<contactcenter:contactcenter.GiveDigipassChallenge_2_Output xmlns:cmn="http://www.seb.ee/common" xmlns:contactcenter="http://www.seb.ee/contactcenter">
<contactcenter:GiveChallengeResponse>
    <contactcenter:customerId>M00013</contactcenter:customerId>
    <contactcenter:challengeCode>5654</contactcenter:challengeCode>
    <contactcenter:username>Raa1234</contactcenter:username>
    <contactcenter:idCode>240987-10007</contactcenter:idCode>
    </contactcenter:GiveChallengeResponse>
</contactcenter:contactcenter.GiveDigipassChallenge_2_Output>
</unifiedServiceBody>
</UnifiedServiceResponse>