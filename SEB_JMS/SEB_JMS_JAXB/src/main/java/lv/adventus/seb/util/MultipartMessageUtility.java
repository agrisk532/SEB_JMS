package lv.adventus.seb.util;

import progress.message.jclient.BytesMessage;
import progress.message.jclient.Constants;
import progress.message.jclient.MultipartMessage;
import progress.message.jclient.Part;
import progress.message.jclient.TextMessage;

public class MultipartMessageUtility {
	
	private Connector connector;
	private String xmlResponse;
	private String partMessage;
	
	static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(MultipartMessageUtility.class);

	public MultipartMessageUtility()
	{
	}
	
	public MultipartMessageUtility(Connector c)
	{
		this.connector = c;
		xmlResponse = "unassignedXMLResponse";
		partMessage = "unassignedPartMessage";
	}
	
	public String getXMLMessage()
	{
		if(xmlResponse.length() > 0) return xmlResponse;
		else
		{
			LOGGER.error("XML response is null length.");
			return null;
		}
			
	}
    /**
     * Handle the message
     * (as specified in the javax.jms.MessageListener interface).
     */
    public void onMessage( javax.jms.Message aMessage) throws javax.jms.JMSException
    {

        if (aMessage instanceof MultipartMessage)
        {
            LOGGER.debug( "received MutipartMessage.... "  );
            unpackMM(aMessage, 0);
        }
        else
        {
            LOGGER.debug("Received a JMS message.");
            LOGGER.error("Received not a MultipartMessage....Cannot continue.");
        }
    }

    private void unpackMM(javax.jms.Message aMessage, int depth) throws javax.jms.JMSException
    {
        int n = depth;
        indent(n); LOGGER.debug("******* Beginning of MultipartMessage ******");

        indent(n); LOGGER.debug("Extend_type property = " + aMessage.getStringProperty(Constants.EXTENDED_TYPE));
        MultipartMessage mm = (MultipartMessage)aMessage;
        int partCount = mm.getPartCount();

        indent(n); LOGGER.debug("partCount of this MultipartMessage = " + partCount);
        for (int i = 0; i < partCount; i++)
        {
            indent(n); LOGGER.debug("--------Beginning of part " + (i+1));
            Part part = mm.getPart(i);
            indent(n); LOGGER.debug("Part.contentType = " + part.getHeader().getContentType());
            indent(n); LOGGER.debug("Part.contentId = " + part.getHeader().getContentId());
 
            if (mm.isMessagePart(i))
            {
                javax.jms.Message msg = mm.getMessageFromPart(i);
                if (msg instanceof MultipartMessage)
                {
                	LOGGER.debug("MM: in MM part");
                    unpackMM(msg, ++depth);
                }
                else
                {
                    if(part.getHeader().getContentId().equals("XMLResponse"))
                    {
                    	//LOGGER.info("MM: in XML part");
                    	unpackJMSMessage(msg, n);
                    	xmlResponse = partMessage;
                    }
                    else
                    {
                    	LOGGER.debug("MM: in not XML part");
                    	unpackJMSMessage(msg, n);
                    }
                }
            }
            else
            {
            	LOGGER.debug("MM: in other part");
                unpackPart(part, n);
            }
            indent(n); LOGGER.debug("--------end of part " + (i+1));
        }
        indent(n); LOGGER.debug("******* End of MultipartMessage ******");
    }


    private void indent(int num)
    {
        for (int i = 0; i < num; i++)
        {
            System.out.print("\t");
        }
    }

    private void unpackJMSMessage(javax.jms.Message aMessage, int depth) throws javax.jms.JMSException
    {
// Be careful. progress.message.jclient.XMLMessage is derived from progress.message.jclient.TextMessage.
// Order of following operators is important.    	
        if (aMessage instanceof progress.message.jclient.XMLMessage)
        {
            progress.message.jclient.XMLMessage msg = (progress.message.jclient.XMLMessage)aMessage;
            indent(depth); LOGGER.debug( "content in XMLmessage... " + msg.getText());
            partMessage = msg.getText();
        }
        else if (aMessage instanceof TextMessage)   
        {
            javax.jms.TextMessage tmsg = (javax.jms.TextMessage) aMessage;
            indent(depth); LOGGER.debug( "content in TextMessage... " + tmsg.getText() );
        }
        else if (aMessage instanceof BytesMessage)
        {
            javax.jms.BytesMessage bmsg = (javax.jms.BytesMessage) aMessage;
            indent(depth); LOGGER.debug( "content in Bytesmessage... " + bmsg.readUTF());
        }
        else
        {
            indent(depth); LOGGER.debug( "a JMS message... ");
        }
    }

    private void unpackPart(Part part, int depth)
    {
        byte[] content = part.getContentBytes();
        int size = content.length;
        String s = new String(content);
        indent(depth); LOGGER.debug( "...size :  " + size);
        indent(depth); LOGGER.debug( "...content :  ");
        indent(depth); LOGGER.debug(s);
    }
}
