package lv.adventus.seb.util;

import javax.jms.*;
/**
* The <CODE>QueueRequestor</CODE> helper class simplifies making service
* requests using the request/reply pattern.
*
* <P>
* The <CODE>QueueRequestor</CODE> constructor is given a non-transacted
* <CODE>QueueSession</CODE> and a destination <CODE>Queue</CODE>. It
* creates a <CODE>TemporaryQueue</CODE> for the responses and provides a
* <CODE>request</CODE> method that sends the request message and waits for
* its reply.
*
*/
public class QueueRequestor {
protected Session session; // The queue session to which the queue belongs.
protected Destination queue; // The queue on which to performthe request/reply.
protected TemporaryQueue tempQueue;
protected MessageProducer sender;
protected MessageConsumer receiver;
/**
* Constructor for the <CODE>QueueRequestor</CODE> class.
*
* <P>
* This implementation assumes the session parameter to be non-transacted,
* with a delivery mode of either <CODE>AUTO_ACKNOWLEDGE</CODE> or
* <CODE>DUPS_OK_ACKNOWLEDGE</CODE>.
*
* @paramsession
* the <CODE>QueueSession</CODE> the queue belongs to
* @paramqueue
* the queue to performthe request/reply call on
*
* @exception JMSException
* if the JMS provider fails to create the
* <CODE>QueueRequestor</CODE> due to some internal error.
* @exception InvalidDestinationException
* if an invalid queue is specified.
*/
public QueueRequestor(Session session, Queue queue) throws JMSException {
this.session = session;
this.queue = queue;
tempQueue = session.createTemporaryQueue();
sender = session.createProducer(queue);
receiver = session.createConsumer(tempQueue);
}
/**
* Sends a request and waits for a reply. The temporary queue is used for
* the <CODE>JMSReplyTo</CODE> destination, and only one reply per request
* is expected. The method blocks indefinitely until a message arrives!
*
* @parammessage
* the message to send
*
* @return the reply message
*
* @exception JMSException
* if the JMS provider fails to complete the request due to
* some internal error.
*/
public Message request(Message message) throws JMSException {
return request(message, 0);
}
/**
* Sends a request and waits for a reply. The temporary queue is used for
* the <CODE>JMSReplyTo</CODE> destination, and only one reply per request
* is expected. The client waits/blocks for the reply until the timeout is
* reached.
*
* @parammessage
* the message to send
* @paramtimeout
* time to wait for a reply on temporary queue. If you specify no
* arguments or an argument of 0, the method blocks indefinitely
* until a message arrives
*
* @return the reply message
*
* @exception JMSException
* if the JMS provider fails to complete the request due to
* some internal error.
*/
public Message request(Message message, long timeout) throws JMSException {
message.setJMSReplyTo(tempQueue);
sender.send(message);
return (receiver.receive(timeout));
}
/**
* Closes the <CODE>QueueRequestor</CODE> and its session.
*
* <P>
* Since a provider may allocate some resources on behalf of a
* <CODE>QueueRequestor</CODE> outside the Java virtual machine, clients
* should close themwhen they are not needed. Relying on garbage collection
* to eventually reclaimthese resources may not be timely enough.
*
* <P>
* Note that this method closes the <CODE>QueueSession</CODE> object
* passed to the <CODE>QueueRequestor</CODE> constructor.
*
* @exception JMSException
* if the JMS provider fails to close the
* <CODE>QueueRequestor</CODE> due to some internal error.
*/
public void close() throws JMSException {
// publisher and consumer created by constructor are implicitly closed.
session.close();
tempQueue.delete();
}
}