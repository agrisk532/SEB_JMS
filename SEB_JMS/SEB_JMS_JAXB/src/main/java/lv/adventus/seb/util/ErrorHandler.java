package lv.adventus.seb.util;

import java.io.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import lv.adventus.seb.UnifiedServiceResponse;
import lv.adventus.seb.UnifiedServiceErrors;

public class ErrorHandler
{
	static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ErrorHandler.class);
	
	public static void SendErrors(HttpServletResponse response, UnifiedServiceResponse usr) throws java.io.IOException
	{
		String errClass = null;
		String errCode = null;
		String errObject = null;
		String servletOut = null; // for servlet output
		PrintWriter out = response.getWriter(); // servlet response writer
		UnifiedServiceErrors errors = usr.getUnifiedServiceErrors();

		if(errors != null)
		{
			for (int i = 0; i < errors.getError().size(); i++)
			{
				errClass = errors.getError().get(i).getErrorClass();
				errCode = errors.getError().get(i).getErrorCode();
				errObject = errors.getError().get(i).getErrorObject().getValue();
				LOGGER.warn("Received ErrorClass: " + errClass);
				LOGGER.warn("Received ErrorCode: " + errCode);
				LOGGER.warn("Received ErrorObject: " + errObject);

				if(out != null)  // print only if called from servlets
				{
				  LOGGER.debug("Connector: Servlet out stream is not null.");
				  if(errClass.equals("VALIDATIONERROR"))
				  {
					  LOGGER.debug("Connector: inside VALIDATIONERROR");
					  if(errCode.equals("NO_MATCH"))
					  {
						  LOGGER.debug("Connector: inside NO_MATCH");
						  servletOut = new String("error:NO_MATCH");
					  }
					  else
					  if(errCode.equals("MULTIPLE_MATCH"))
					  {
						  LOGGER.debug("Connector: inside MULTIPLE_MATCH");
						  servletOut = new String("error:MULTIPLE_MATCH");
					  }
					  else
					  if(errCode.equals("Check ERROR"))
					  {
						  LOGGER.debug("Connector: inside Check ERROR");
						  servletOut = new String("result:WRONGCODE");
					  }
					  else
					  {
						  LOGGER.debug("Connector: inside general error");
						  servletOut = new String("error:" + errCode);
					  }
				  }
				  else
				  {
					  LOGGER.debug("Connector: inside general error. No VALIDATIONERROR.");
					  servletOut = new String("error:" + errClass);
				  }
				  Utility.ServletResponse(response, servletOut);
				}
				else
				{
					LOGGER.error("ErrorHandler.HandleErrors: Servlet out stream is null");
				}
			}
		}
		else
		{
			LOGGER.info("No errors in the received message. All is ok.");
		}
	}
}