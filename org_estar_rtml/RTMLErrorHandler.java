// RTMLErrorHandler.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLErrorHandler.java,v 1.1 2003-02-24 13:19:56 cjm Exp $
package org.estar.rtml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;  
import org.xml.sax.SAXParseException;  

/**
 * This class is a JAXP compliant ErrorHandler, i.e. it implements org.xml.sax.ErrorHandler.
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 */
public class RTMLErrorHandler implements org.xml.sax.ErrorHandler
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLErrorHandler.java,v 1.1 2003-02-24 13:19:56 cjm Exp $";
	/**
	 * The parser that is using this error handler.
	 */
	private RTMLParser parser = null;
	/**
	 * The instance of RTMLCreate that is using this error handler.
	 */
	private RTMLCreate creator = null;
	/**
	 * String buffer containing text of errors handled.
	 */
	private StringBuffer stringBuffer = null;

	/**
	 * Default constructor.
	 * @param p The instance of RTMLParser that is using this error handler.
	 * @see #parser
	 * @see #stringBuffer
	 */
	public RTMLErrorHandler(RTMLParser p)
	{
		super();
		parser = p;
		stringBuffer = new StringBuffer();
	}

	/**
	 * Default constructor.
	 * @param c The instance of RTMLCreate that is using this error handler.
	 * @see #creator
	 * @see #stringBuffer
	 */
	public RTMLErrorHandler(RTMLCreate c)
	{
		super();
		creator = c;
		stringBuffer = new StringBuffer();
	}

	/**
	 * Method to get text of all strings returned by error handler.
	 * @see #stringBuffer
	 */
	public String getErrorString()
	{
		return stringBuffer.toString();
	}

	/**
	 * Warning.
	 * @see #stringBuffer
	 */
	public void warning(SAXParseException exception) throws SAXException
	{
		System.err.println(exception);
		//diddly exception.printStackTrace();
		stringBuffer.append(exception.toString()+"\n");
	}

	/**
	 * Warning.
	 * @see #stringBuffer
	 */
	public void error(SAXParseException exception) throws SAXException
	{
		System.err.println(exception);
		// diddly exception.printStackTrace();
		stringBuffer.append(exception.toString()+"\n");
	}

	/**
	 * Fatal error.
	 * @see #stringBuffer
	 */
	public void fatalError(SAXParseException exception) throws SAXException
	{
		System.err.println(exception);
		//diddly exception.printStackTrace();
		stringBuffer.append(exception.toString()+"\n");
	}
}
/*
** $Log: not supported by cvs2svn $
*/
