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
// RTMLDateFormat.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLDateFormat.java,v 1.3 2007-01-30 18:31:10 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * This class is a wrapper for SimpleDateFormat to allow the parsing and formating of ISO8601 dates,
 * as used by the RTML standard for DateTimeConstraint s etc.
 * This is needed as SimpleDateFormat does not allow optional time zone parsing, so we need two instances
 * of SimpleDateFormat to cover both cases.
 * <b>NB This class does not follow the RTML standard or ISO8601 at the present time.</b> It differs in that
 * ISO8601 dates without a timezone should be parsed as being UTC dates, but we currently parse them in the parser's
 * locale timezone , which makes specifying local-relative dates easy.
 * @author Chris Mottram
 * @version $Revision: 1.3 $
 */
public class RTMLDateFormat implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLDateFormat.java,v 1.3 2007-01-30 18:31:10 cjm Exp $";
	/**
	 * Instance of SimpleDateFormat setup to parse ISO8601 dates with a timezone field e.g.:
	 * 2006-02-28T18:00:00+1100
	 */
	protected SimpleDateFormat timezoneDateFormat = null;
	/**
	 * Instance of SimpleDateFormat setup to parse ISO8601 dates <b>without</b> a timezone field e.g.:
	 * 2006-02-28T18:00:00. The fields are parsed as if they are specified in the parser's local timezone.
	 */
	protected SimpleDateFormat localDateFormat = null;

	/**
	 * Default constructor.
	 */
	public RTMLDateFormat()
	{
		super();
		timezoneDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		localDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	}

	/**
	 * Parse a string of the form:"yyyy-MM-dd'T'HH:mm:ssZ" or "yyyy-MM-dd'T'HH:mm:ss" into a date.
	 * @param s The string to parse.
	 * @return An instance of Date containing the parsed date.
	 * @see #timezoneDateFormat
	 * @see #localDateFormat
	 */
	public Date parse(String s) throws ParseException
	{
		Date date = null;

		// First, try parsing as if there is a timezone present.
		// If there is not this will throw a ParseException, see the catch to see what happens next.
		try
		{
			date = timezoneDateFormat.parse(s);
		}
		catch(ParseException e)
		{
			// retry parsing without a timezone
			// The other fields are interpreted using the locale timezone
			// If this fails the method throws an exception.
			date = localDateFormat.parse(s);
		}
		return date;
	}

	/**
	 * Method to format a Date into an acceptable ISO8601 String.
	 * Currently uses timezoneDateFormat to get timezone information.
	 * @see #timezoneDateFormat
	 */
	public String format(Date d)
	{
		return timezoneDateFormat.format(d);
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 1.2  2006/03/20 16:10:20  cjm
// Fixed comment.
//
// Revision 1.1  2006/03/20 15:23:11  cjm
// Initial revision
//
//
