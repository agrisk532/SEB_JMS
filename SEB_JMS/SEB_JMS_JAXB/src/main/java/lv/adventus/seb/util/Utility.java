package lv.adventus.seb.util;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import lv.adventus.seb.UnifiedServiceResponse;
import lv.adventus.seb.UnifiedServiceErrors;

public class Utility
{
	public static void ServletResponse(HttpServletResponse response, String content) throws java.io.IOException
	{
		PrintWriter out = response.getWriter();
		out.print(content); // without CRLF for Genesys
		out.flush();
	}
	
	public static boolean CheckPingPongStatus(HttpServletRequest request, HttpServletResponse response, String callerFunction) throws java.io.IOException
	{
	    ServletContext sc = request.getSession().getServletContext();
		ConcurrentHashMap<String, Object> shared = (ConcurrentHashMap<String, Object>)sc.getAttribute("sharedData");
		Boolean pp = (Boolean)shared.get("PingPong");

 		if(pp == null || pp.booleanValue() == false)
 		{
 			System.out.println(callerFunction + ": PingPong returns 0. Processing stopped.");
 			Utility.ServletResponse(response,"error:TECHNICALERROR");
 			return false;
 		}
 		else
 		{
 			System.out.println(callerFunction + ": PingPong returns 1. Processing continues.");
 			return true;
 		}
	}
	
	public static void stringToFile( String text, String fileName ) throws java.io.IOException
	{
	    File file = new File( fileName );

	    // if file doesnt exists, then create it 
	    if ( ! file.exists( ) )
	    {
	        file.createNewFile( );
	    }

	    FileWriter fw = new FileWriter( file.getAbsoluteFile( ) );
	    BufferedWriter bw = new BufferedWriter( fw );
	    bw.write( text );
	    bw.close( );
	}
}

