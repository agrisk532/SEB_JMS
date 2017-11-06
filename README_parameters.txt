Hello,

Would it be acceptable for you if all the parameters are defined in the global context file ./conf/context.xml with following lines:

    <Parameter name="broker" value="sonic1.llv.baltic.seb.net:2526"
         description="JMS broker:port" override="false"/>
    <Parameter name="usernameSonic" value="genesys"
         description="JMS username" override="false"/>
    <Parameter name="passwordSonic" value="********"
         description="JMS password" override="false"/>
    <Parameter name="queue" value="SEB_SERVICES"
         description="JMS queue" override="false"/>
    <Parameter name="connectionTimeout" value="60000"
         description="time in ms to wait for a reply on temporary queue. If 0, blocks indefinitely until a message arrives" override="false"/>
    <Parameter name="pingPongInterval" value="30000"
         description="interval between PingPong messages, in ms" override="false"/>
    <Parameter name="TTL" value="30000"
         description="JMS Message attribute TTL, in ms" override="false"/>
    <Parameter name="responseMsgTTL" value="30000"
         description="JMS Message attribute responseMsgTTL, in ms" override="false"/>
    <Parameter name="pingPongStatusFileName" value="PingPongStatusFile.txt"
         description="file with PingPong status (for NMS), contains 0 - no services available, 1 - ok" override="false"/>
    <Parameter name="log4j-config-location" value="../../conf/SEB_JMS_ORCHESTRA_log4j.properties"
         description="location of the logging properties file" override="false"/>

Insert these lines in the Context section of the ./conf/context.xml file (replace the passwordSonic with the valid one).

Additionally we need logging properties file ./conf/SEB_JMS_ORCHESTRA_log4j.properties with following content (you can fine tune it according to your needs):

# LOG4J configuration
log4j.rootLogger=INFO, Appender
log4j.appender.Appender=org.apache.log4j.DailyRollingFileAppender
log4j.appender.Appender.File=<folder for application logs>/SonicLog.log
log4j.appender.Appender.layout=org.apache.log4j.PatternLayout
log4j.appender.Appender.layout.ConversionPattern=%-7p %d [%t] %c %x - %m%n
log4j.appender.loggerId.DatePattern='-'yyyyMMdd'.log'

Janis