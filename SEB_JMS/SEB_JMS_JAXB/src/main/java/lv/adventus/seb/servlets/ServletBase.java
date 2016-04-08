package lv.adventus.seb.servlets;

//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Unmarshaller;

//import progress.message.jclient.MultipartMessage;
//import progress.message.jclient.Part;
//import progress.message.jclient.Header;
//import progress.message.jclient.Constants;
//import progress.message.jclient.TextMessage;
//import progress.message.jclient.BytesMessage;

import lv.adventus.seb.util.Connector;
//import lv.adventus.seb.util.MultipartMessageUtility;
//import lv.adventus.seb.util.XMLUtility;
import lv.adventus.seb.UnifiedServiceResponse;
//import lv.adventus.seb.UnifiedServiceErrors;
//import lv.adventus.seb.ContactcenterFindCustomerByPhoneOrPersonalCode2Output;
//import lv.adventus.seb.ContactcenterGiveDigipassChallenge2Output;

/**
 * Servlet implementation class CheckAuthenticationCode
 */
public class ServletBase extends HttpServlet {

    protected static String broker = "awebs:6526";
    protected static String usernameSonic = "tester1";
    protected static String passwordSonic = "tester";
    protected static String queue = "SEB_SERVICES";
    protected static long connectionTimeout = 60000L;
    protected static long ttl = 30000L;
    protected static long responseMsgTTL = 30000L;

    protected ServletContext sc;

//	protected String xmlrequest;
//	protected String xmlresponse;
//
//	protected String customerId;
//	protected String idCode;
//	protected String firstName;
//	protected String lastName;
//	protected String userPhoneNumber;
//	protected String challengeCode;
//	protected String userName;
//	protected String authenticationCode;
//	
//	protected UnifiedServiceResponse usr;
//	protected Connector c;

    /**
     * Default constructor. 
     */
    public ServletBase() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		sc = config.getServletContext();
		this.broker = sc.getInitParameter("broker");
		this.usernameSonic = sc.getInitParameter("usernameSonic");
		this.passwordSonic = sc.getInitParameter("passwordSonic");
		this.queue = sc.getInitParameter("queue");
		this.connectionTimeout = Long.parseLong(sc.getInitParameter("connectionTimeout"));
		this.ttl = Long.parseLong(sc.getInitParameter("TTL")); // JMS parameter
		this.responseMsgTTL = Long.parseLong(sc.getInitParameter("responseMsgTTL")); // JMS parameter
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}
}
