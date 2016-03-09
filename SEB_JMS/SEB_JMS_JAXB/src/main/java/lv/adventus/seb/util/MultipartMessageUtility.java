package lv.adventus.seb.util;

import progress.message.jclient.BytesMessage;
import progress.message.jclient.Constants;
import progress.message.jclient.MultipartMessage;
import progress.message.jclient.Part;
import progress.message.jclient.TextMessage;

public class MultipartMessageUtility {
	
	private Connector connector;
	private int xmlResponsePart = -1; // MultipartMessage part containing XMLResponse 
	private String xmlResponse;
	private String partMessage;

	public MultipartMessageUtility()
	{
	}
	
	public MultipartMessageUtility(Connector c)
	{
		this.connector = c;
	}
	
	public String getXMLMessage()
	{
		if(xmlResponse.length() > 0) return xmlResponse;
		else
			return null;
	}
    /**
     * Handle the message
     * (as specified in the javax.jms.MessageListener interface).
     */
    public void onMessage( javax.jms.Message aMessage)
    {
        System.out.println();

        if (aMessage instanceof MultipartMessage)
        {
            System.out.println( "received MutipartMessage.... "  );
            unpackMM(aMessage, 0);
        }
        else
        {
            System.out.println( "received a JMS message ");
        }
        System.out.println("Received not a MultipartMessage....Cannot continue.");

    }

    private void unpackMM(javax.jms.Message aMessage, int depth)
    {
        int n = depth;
        System.out.println();
        indent(n); System.out.println("******* Beginning of MultipartMessage ******");
        System.out.println();

        try
        {
            indent(n); System.out.println("Extend_type property = " + aMessage.getStringProperty(Constants.EXTENDED_TYPE));
            MultipartMessage mm = (MultipartMessage)aMessage;
            int partCount = mm.getPartCount();

            indent(n); System.out.println("partCount of this MultipartMessage = " + partCount);
            for (int i = 0; i < partCount; i++)
            {
                System.out.println();
                indent(n); System.out.println("--------Beginning of part " + (i+1));
                Part part = mm.getPart(i);
                indent(n); System.out.println("Part.contentType = " + part.getHeader().getContentType());
                indent(n); System.out.println("Part.contentId = " + part.getHeader().getContentId());

                if (mm.isMessagePart(i))
                {
                    javax.jms.Message msg = mm.getMessageFromPart(i);
                    if (msg instanceof MultipartMessage)
                        unpackMM(msg, ++depth);
                    else
                    {
                        if(part.getHeader().getContentId()=="XMLResponse")
                        {
                        	unpackJMSMessage(msg, n);
                        	xmlResponse = partMessage;
                        	return; // no need to scan after XMLResponse is found
                        }
                        else
                        {
                        	unpackJMSMessage(msg, n);
                        }
                    }
                }
                else
                {
                    unpackPart(part, n);
                }
                indent(n); System.out.println("--------end of part " + (i+1));
                System.out.println();
            }
            indent(n); System.out.println("******* End of MultipartMessage ******");
            System.out.println();
        }
         catch (javax.jms.JMSException jmse)
        {
            System.err.println(jmse);
        }
    }


    private void indent(int num)
    {
        for (int i = 0; i < num; i++)
        {
            System.out.print("\t");
        }
    }

    private void unpackJMSMessage(javax.jms.Message aMessage, int depth)
    {
        try
        {
            if (aMessage instanceof TextMessage)
            {
                javax.jms.TextMessage tmsg = (javax.jms.TextMessage) aMessage;
                indent(depth); System.out.println( "content in TextMessage... " + tmsg.getText() );
            }
            else if (aMessage instanceof BytesMessage)
            {
                javax.jms.BytesMessage bmsg = (javax.jms.BytesMessage) aMessage;
                indent(depth); System.out.println( "content in Bytesmessage... " + bmsg.readUTF());
            }
            else if (aMessage instanceof progress.message.jclient.XMLMessage)
            {
                progress.message.jclient.XMLMessage msg = ((progress.message.jclient.Session)connector.getSession()).createXMLMessage();
                indent(depth); System.out.println( "content in XMLmessage... " + msg.getText());
                partMessage = msg.getText();
            }
            else
            {
                indent(depth); System.out.println( "a JMS message... ");
            }
        }
         catch (javax.jms.JMSException jmse)
        {
            System.err.println(jmse);
        }
    }

    private void unpackPart(Part part, int depth)
    {
        byte[] content = part.getContentBytes();
        int size = content.length;
        String s = new String(content);
        indent(depth); System.out.println( "...size :  " + size);
        indent(depth); System.out.println( "...content :  ");
        System.out.println();
        indent(depth); System.out.println(s);
    }
}
