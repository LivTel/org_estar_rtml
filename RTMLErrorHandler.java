/*   
    Copyright 2006, Astrophysics Research Institute, Liverpool John Moores University.

    This file is part of org.estar.rtml.

    org.estar.rtml is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    org.estar.rtml is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with org.estar.rtml; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
// RTMLErrorHandler.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLErrorHandler.java,v 1.3 2008-05-23 14:24:03 cjm Exp $
package org.estar.rtml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;  
import org.xml.sax.SAXParseException;  

/**
 * This class is a JAXP compliant ErrorHandler, i.e. it implements org.xml.sax.ErrorHandler.
 * @author Chris Mottram
 * @version $Revision$
 */
public class RTMLErrorHandler implements org.xml.sax.ErrorHandler
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id$";
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
	 * @see #stringBuffer
	 */
	public RTMLErrorHandler()
	{
		super();
		stringBuffer = new StringBuffer();
	}

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
		stringBuffer.append(exception.toString()+"\n");
	}

	/**
	 * Warning.
	 * @see #stringBuffer
	 */
	public void error(SAXParseException exception) throws SAXException
	{
		System.err.println(exception);
		stringBuffer.append(exception.toString()+"\n");
	}

	/**
	 * Fatal error.
	 * @see #stringBuffer
	 */
	public void fatalError(SAXParseException exception) throws SAXException
	{
		System.err.println(exception);
		stringBuffer.append(exception.toString()+"\n");
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.2  2007/01/30 18:31:14  cjm
** gnuify: Added GNU General Public License.
**
** Revision 1.1  2003/02/24 13:19:56  cjm
** Initial revision
**
*/
