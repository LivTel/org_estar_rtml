// RTMLDateFormat.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLDateFormat.java,v 1.1 2006-03-20 15:23:11 cjm Exp $
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
 * @version $Revision: 1.1 $
 */
public class RTMLDateFormat implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLDateFormat.java,v 1.1 2006-03-20 15:23:11 cjm Exp $";
	/**
	 * Instance of SimpleDateFormat setup to parse ISO8601 dates with a timezone field e.g.:
	 * 2006-02-28T18:00:00+11:00
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
//
