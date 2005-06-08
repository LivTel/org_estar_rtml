// RTMLPeriodFormat.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLPeriodFormat.java,v 1.3 2005-06-08 11:39:01 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * This class represents a time period, as specified in RTML 3.0g. THis is represented in string form as:
 * <code>
 * P{(yyyy)Y{(mm)M}{(dd)D}{T{(hh)H}{(mm}M}{(ss.s..)S}
 * </code>
 * For the purposes of this implementation, we have assumed 30 days in 1 month, and 365 days in 1 year.
 * @author Chris Mottram
 * @version $Revision: 1.3 $
 */
public class RTMLPeriodFormat implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLPeriodFormat.java,v 1.3 2005-06-08 11:39:01 cjm Exp $";
	/**
	 * Number of milliseconds in a second. 
	 */
	private final static long SECOND_MS = 1000L;
	/**
	 * Number of milliseconds in a minute. 
	 */
	private final static long MINUTE_MS = (60L*SECOND_MS);
	/**
	 * Number of milliseconds in an hour. 
	 */
	private final static long HOUR_MS = (60L*MINUTE_MS);
	/**
	 * Number of milliseconds in an day. 
	 */
	private final static long DAY_MS = (24L*HOUR_MS);
	/**
	 * Number of milliseconds in a month. We have assumed a month has 30 days for this implementation. 
	 */
	private final static long MONTH_MS = (30L*DAY_MS);
	/**
	 * Number of milliseconds in a year. We have assumed a year has 365 days for this implementation. 
	 */
	private final static long YEAR_MS = (365L*DAY_MS);
	/**
	 * Part of the time period.
	 */
	private int years = 0;
	/**
	 * Part of the time period.
	 */
	private int months = 0;
	/**
	 * Part of the time period.
	 */
	private int days = 0;
	/**
	 * Part of the time period.
	 */
	private int hours = 0;
	/**
	 * Part of the time period.
	 */
	private int minutes = 0;
	/**
	 * Part of the time period.
	 */
	private double seconds = 0;

	/**
	 * Default constructor.
	 */
	public RTMLPeriodFormat()
	{
		super();
	}

	/**
	 * Parse a string of the form:
	 * <code>
	 * P{(yyyy)Y{(mm)M}{(dd)D}{T{(hh)H}{(mm}M}{(ss.s..)S}
	 * </code>
	 * into a time period.
	 * @param s The string to parse.
	 * @exception RTMLException Thrown if the input string is not valid.
	 * @exception NumberFormatException Thrown if parsing a numeric value within the period fails.
	 * @see #removeWhitespace
	 * @see #setYears
	 * @see #setMonths
	 * @see #setDays
	 * @see #setHours
	 * @see #setMinutes
	 * @see #setSeconds
	 */
	public void parse(String s) throws RTMLException, NumberFormatException
	{
		StringTokenizer st = null;
		String tokenString = null;
		String delimiterString = new String("PYMDTHS");
		String valueString = null;
		boolean inP = false;
		boolean inT = false;
		int i;
		double d;

		// remove whitespace from ends of string
		s = removeWhitespace(s);
		// return delimiters
		st = new StringTokenizer(s,delimiterString,true);
		while(st.hasMoreTokens())
		{
			tokenString = st.nextToken();
			// ensure P token is first token received
			if(tokenString.equals("P") == true)
				inP = true;
			if(inP == false)
			{
				throw new RTMLException(this.getClass().getName()+
							 ":parse("+s+"):P not parsed when received token "+
							 tokenString);
			}
			// Is it a delimiter or a value?
			if(delimiterString.indexOf(tokenString) > -1) // tokenString is in delimiterString
			{
				if(tokenString.equals("P"))
				{
					inP = true;// no nothing, effectively - see above
				}
				else if(tokenString.equals("Y"))
				{
					if(inT)
					{
						throw new RTMLException(this.getClass().getName()+
									 ":parse("+s+"):Y detected after T.");
					}
					i = Integer.parseInt(valueString);
					setYears(i);
					valueString = null;
				}
				else if(tokenString.equals("M"))
				{
					i = Integer.parseInt(valueString);
					if(inT)
						setMinutes(i);
					else
						setMonths(i);
					valueString = null;
				}
				else if(tokenString.equals("D"))
				{
					if(inT)
					{
						throw new RTMLException(this.getClass().getName()+
									 ":parse("+s+"):D detected after T.");
					}
					i = Integer.parseInt(valueString);
					setDays(i);
					valueString = null;
				}
				else if(tokenString.equals("T"))
				{
					inT = true;
					if(valueString != null)
					{
						throw new RTMLException(this.getClass().getName()+
									 ":parse("+s+"):T detected when value string "+
									 valueString+" not used yet.");
					}
				}
				else if(tokenString.equals("H"))
				{
					if(inT == false)
					{
						throw new RTMLException(this.getClass().getName()+
									 ":parse("+s+"):H detected before T.");
					}
					i = Integer.parseInt(valueString);
					setHours(i);
					valueString = null;
				}
				else if(tokenString.equals("S"))
				{
					if(inT == false)
					{
						throw new RTMLException(this.getClass().getName()+
									 ":parse("+s+"):S detected before T.");
					}
					d = Double.parseDouble(valueString);
					setSeconds(d);
					valueString = null;
				}
				else
				{
					throw new RTMLException(this.getClass().getName()+
							 ":parse("+s+"):Illegal delimiter "+tokenString+".");
				}
			}
			else
			{
				if(valueString != null)
				{
					throw new RTMLException(this.getClass().getName()+
							 ":parse("+s+"):setting new value string to "+tokenString+
							 " when old value string "+valueString+" not used yet.");
				}
				// string is value  - to be associated with next parsed delimiter
				valueString = tokenString;
			}// end else if not delimiter
		}// end while tokens
		if(valueString != null)
		{
			throw new RTMLException(this.getClass().getName()+
						 ":parse("+s+"):value string "+valueString+
						 " detected without trailing delimiter.");
		}
	}

	/**
	 * Get the number of milliseconds represented by this period.
	 * @return The number of milliseconds as a long.
	 * @see #years
	 * @see #months
	 * @see #days
	 * @see #hours
	 * @see #minutes
	 * @see #seconds
	 * @see #SECOND_MS
	 * @see #MINUTE_MS
	 * @see #HOUR_MS
	 * @see #DAY_MS
	 * @see #MONTH_MS
	 * @see #YEAR_MS
	 */
	public long getMilliseconds()
	{
		return (long)(years*YEAR_MS)+(months*MONTH_MS)+(days*DAY_MS)+
			(hours*HOUR_MS)+(minutes*MINUTE_MS)+(long)(seconds*((double)SECOND_MS));
	}

	/**
	 * Set method.
	 * @param i An integer.
	 * @see #years
	 * @exception IllegalArgumentException Thrown if the input number is out of range.
	 */
	public void setYears(int i) throws IllegalArgumentException
	{
		if(i<0)
		{
			throw new IllegalArgumentException(this.getClass().getName()+":setYears:"+i+
							   " is not a legal number of years:It is negative.");
		}
		years = i;
	}

	/**
	 * Get method.
	 * @return An integer.
	 * @see #years
	 */
	public int getYears()
	{
		return years;
	}

	/**
	 * Set method.
	 * @param i An integer.
	 * @see #months
	 * @exception IllegalArgumentException Thrown if the input number is out of range.
	 */
	public void setMonths(int i) throws IllegalArgumentException
	{
		if(i<0)
		{
			throw new IllegalArgumentException(this.getClass().getName()+":setMonths:"+i+
							   " is not a legal number of months:It is negative.");
		}
		months = i;
	}

	/**
	 * Get method.
	 * @return An integer.
	 * @see #months
	 */
	public int getMonths()
	{
		return months;
	}

	/**
	 * Set method.
	 * @param i An integer.
	 * @see #days
	 * @exception IllegalArgumentException Thrown if the input number is out of range.
	 */
	public void setDays(int i) throws IllegalArgumentException
	{
		if(i<0)
		{
			throw new IllegalArgumentException(this.getClass().getName()+":setDays:"+i+
							   " is not a legal number of days:It is negative.");
		}
		days = i;
	}

	/**
	 * Get method.
	 * @return An integer.
	 * @see #days
	 */
	public int getDays()
	{
		return days;
	}

	/**
	 * Set method.
	 * @param i An integer.
	 * @see #hours
	 * @exception IllegalArgumentException Thrown if the input number is out of range.
	 */
	public void setHours(int i) throws IllegalArgumentException
	{
		if(i<0)
		{
			throw new IllegalArgumentException(this.getClass().getName()+":setHours:"+i+
							   " is not a legal number of hours:It is negative.");
		}
		hours = i;
	}

	/**
	 * Get method.
	 * @return An integer.
	 * @see #hours
	 */
	public int getHours()
	{
		return hours;
	}

	/**
	 * Set method.
	 * @param i An integer.
	 * @see #minutes
	 * @exception IllegalArgumentException Thrown if the input number is out of range.
	 */
	public void setMinutes(int i) throws IllegalArgumentException
	{
		if(i<0)
		{
			throw new IllegalArgumentException(this.getClass().getName()+":setMinutes:"+i+
							   " is not a legal number of minutes:It is negative.");
		}
		minutes = i;
	}

	/**
	 * Get method.
	 * @return An integer.
	 * @see #minutes
	 */
	public int getMinutes()
	{
		return minutes;
	}

	/**
	 * Set method.
	 * @param d A double.
	 * @see #seconds
	 * @exception IllegalArgumentException Thrown if the input number is out of range.
	 */
	public void setSeconds(double d) throws IllegalArgumentException
	{
		if(d<0.0)
		{
			throw new IllegalArgumentException(this.getClass().getName()+":setSeconds:"+d+
							   " is not a legal number of seconds:It is negative.");
		}
		seconds = d;
	}

	/**
	 * Get method.
	 * @return A double.
	 * @see #seconds
	 */
	public double getSeconds()
	{
		return seconds;
	}

	/**
	 * Method to print out a string representation of this period.
	 * @return A string.
	 * @see #toString(java.lang.String)
	 */
	public String toString()
	{
		return toString("");
	}

	/**
	 * Method to print out a string representation of this period, with a prefix.
	 * String is of the form:
	 * <code>
	 * P{(yyyy)Y{(mm)M}{(dd)D}{T{(hh)H}{(mm}M}{(ss.s..)S}
	 * </code>
	 * @param prefix A string to prefix the period.
	 * @return A string.
	 * @see #years
	 * @see #months
	 * @see #days
	 * @see #hours
	 * @see #minutes
	 * @see #seconds
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"P");
		if(years != 0)
			sb.append(years+"Y");
		if(months != 0)
			sb.append(months+"M");
		if(days != 0)
			sb.append(days+"D");
		if((hours != 0) || (minutes != 0) || (seconds != 0))
			sb.append("T");
		if(hours != 0)
			sb.append(hours+"H");
		if(minutes != 0)
			sb.append(minutes+"M");
		if(seconds != 0)
			sb.append(seconds+"S");
		return sb.toString();
	}

	/**
	 * Method to remove whitespace before the first non-whitespace character, and after the last non-whitespace
	 * character. Whitespace in the middle of the non-whitespace string are <b>not</b> removed.
	 * Whitespace is "\n\t\r\f ".
	 * @param s The string.
	 * @return A string with start and end whitespace removed.
	 */
	protected String removeWhitespace(String s)
	{
		String whitespace = new String("\n\t\r\f ");
		int sindex,eindex;
		boolean done;

		// set sindex to index of first character in s not a whitespace
		sindex = 0;
		done = false;
		while((sindex < s.length()) && (done == false))
		{
			done = (whitespace.indexOf(s.charAt(sindex)) < 0);
			if(done == false)
				sindex++;
		}
		// set eindex to index of last character in s not a whitespace
		eindex = s.length()-1;
		done = false;
		while((eindex >= 0) && (done == false))
		{
			done = (whitespace.indexOf(s.charAt(eindex)) < 0);
			if(done == false)
				eindex--;
		}
		// check if string all whitespace
		if(eindex <= sindex)
			return new String("");
		return s.substring(sindex,eindex+1);
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.2  2005/04/27 15:22:04  cjm
** Added parse checks, ensure Y/D apppear before T, ensure H/S appear after T.
**
** Revision 1.1  2005/04/27 13:45:48  cjm
** Initial revision
**
*/
