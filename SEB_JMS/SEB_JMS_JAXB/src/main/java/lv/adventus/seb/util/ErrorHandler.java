package lv.adventus.seb.util;

import java.io.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import lv.adventus.seb.UnifiedServiceResponse;
import lv.adventus.seb.UnifiedServiceErrors;

public class ErrorHandler
{
	public static void SendErrors(HttpServletResponse response, UnifiedServiceResponse usr) throws java.io.IOException
	{
		String errClass = null;
		String errCode = null;
		String errObject = null;
		PrintWriter out = response.getWriter(); // servlet response writer
		UnifiedServiceErrors errors = usr.getUnifiedServiceErrors();
		String servletOut = null; // for servlet output

		if(errors != null)
		{
			for (int i = 0; i < errors.getError().size(); i++)
			{
				errClass = errors.getError().get(i).getErrorClass();
				errCode = errors.getError().get(i).getErrorCode();
				errObject = errors.getError().get(i).getErrorObject().getValue();
				System.out.println("Received ErrorClass: " + errClass);
				System.out.println("Received ErrorCode: " + errCode);
				System.out.println("Received ErrorObject: " + errObject);

				if(out != null)  // print only if called from servlets
				{
				  System.out.println("Connector: Servlet out stream is not null.");
				  if(errClass.equals("VALIDATIONERROR"))
				  {
					  System.out.println("Connector: inside VALIDATIONERROR");
					  if(errCode.equals("NO_MATCH"))
					  {
						  System.out.println("Connector: inside NO_MATCH");
						  servletOut = new String("error:NO_MATCH");
					  }
					  else
					  if(errCode.equals("MULTIPLE_MATCH"))
					  {
						  System.out.println("Connector: inside MULTIPLE_MATCH");
						  servletOut = new String("error:MULTIPLE_MATCH");
					  }
					  else
					  if(errCode.equals("Check ERROR"))
					  {
						  System.out.println("Connector: inside Check ERROR");
						  servletOut = new String("result:WRONGCODE");
					  }
					  else
					  {
						  System.out.println("Connector: inside general error");
						  servletOut = new String("error:" + errCode);
					  }
				  }
				  else
				  {
					  System.out.println("Connector: inside general error. No VALIDATIONERROR.");
					  servletOut = new String("error:" + errClass);
				  }
				}
				else
				{
					System.out.println("ErrorHandler.HandleErrors: Servlet out stream is null");
					servletOut = new String("error:TECHNICALERROR");
				}
				Utility.ServletResponse(response, servletOut);
			}
		}
	}
}