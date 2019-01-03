package org.estar.rtml;

import java.io.*;
import java.text.*;
import java.util.*;
import org.estar.astrometry.*;

/**
 * This class is a data container for information contained in an ephemeris target line 
 * of an ephemeris target in an RTML document. e.g. a line of the form:
 * 2018-Sep-26 00:10 00 43 56.61 +11 08 37.2     -78.155203      65.006315
 * where
 * Columns desciption:
 * <ul>
 * <li>1 = UTC Date (YYYY-MMM-DD)
 * <li>2 = UTC time (HH:MM)
 * <li>3,4,5 = RA  ( HH MM SS.SS) (J2000, topocentric)
 * <li>6,7,8 = DEC (sDD mm ss.s ) (J2000, topocentric)
 * <li>9 = RA rate (arcsec/hour)
 * <li>10 = DEC rate (arcsec/hour)
 * </ul>
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 * @see org.estar.rtml.RTMLAttributes
 */
public class RTMLEphemerisTargetTrackNode extends RTMLAttributes implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLEphemerisTargetTrackNode.java,v 1.1 2018-09-27 14:34:31 cjm Exp $";
	/**
	 * The date format used to parse and print the timestamp in an ephemeris target line.
	 * This is currently "yyyy-MMM-dd HH:mm".
	 */
	public final static String EPHEMERIS_DATE_FORMAT = new String("yyyy-MMM-dd HH:mm");
	/**
	 * Serial version ID. Fixed as these documents can be used as parameters in RMI calls across JVMs.
	 */
	//	static final long serialVersionUID = ;
	/** 
	 * The timestamp for this node.
	 */
	private Date timestamp = null;

	/**
	 * The right ascension of the target.
	 * For ephemeris targets these should be J2000, topocentric, astrometric.
	 */
	private RA ra = null;
	/**
	 * The declination of the target.
	 * For ephemeris targets these should be J2000, topocentric, astrometric.
	 */
	private Dec dec = null;
	/**
	 * The non-sidereal tracking rate in RA of the ephemeris at this point in time,in arcsec/hour.
	 */
	private double trackRateRA = 0.0;
	/**
	 * The non-sidereal tracking rate in Dec of the ephemeris at this point in time,in arcsec/hour.
	 */
	private double trackRateDec = 0.0;

	/**
	 * Default constructor.
	 */
	public RTMLEphemerisTargetTrackNode()
	{
		super();
	}


	/**
	 * Set the timestamp of this track node.
	 * @param d A date.
	 * @see #timestamp
	 */
	public void setTimestamp(Date d)
	{
		timestamp = d;
	}

	/**
	 * Parse a string of the form:"2018-Sep-26 00:10", with a SimpleDateFormat using format "yyyy-MMM-dd HH:mm". 
	 * @param dateString The date string to parse, in the format "yyyy-MMM-dd", e.g. "2018-Sep-26".
	 * @param timeString The time string to parse, in the format "HH:mm", e.g. "00:10".
	 * @see #EPHEMERIS_DATE_FORMAT
	 * @see #timestamp
	 */
	public void parseTimestamp(String dateString,String timeString) throws ParseException
	{
		SimpleDateFormat dateFormat = null;
		String parseString = null;

		parseString = new String(dateString+" "+timeString);
		dateFormat = new SimpleDateFormat(EPHEMERIS_DATE_FORMAT);
		timestamp = dateFormat.parse(parseString);
	}

	/**
	 * Get the timestamp of this track node.
	 * @return A date.
	 * @see #timestamp
	 */
	public Date getTimestamp()
	{
		return timestamp;
	}

	/**
	 * Set the target right ascension. For ephemeris targets these should be J2000, topocentric, astrometric.
	 * @param r The right ascension.
	 * @see #ra
	 */
	public void setRA(RA r)
	{
		ra = r;
	}

	/**
	 * Set the target right ascension. For ephemeris targets these should be J2000, topocentric, astrometric.
	 * @param s A string representing the right ascension, in space separated format.
	 * @see #ra
	 * @exception IllegalArgumentException Thrown if there is a problem with the parsing.
	 */
	public void parseRA(String s) throws IllegalArgumentException
	{
		ra = new RA();
		ra.parseSpace(s);
	}

	/**
	 * Get the target right ascension.
	 * @return The right ascension.
	 * @see #ra
	 */
	public RA getRA()
	{
		return ra;
	}

	/**
	 * Set the target declination. For ephemeris targets these should be J2000, topocentric, astrometric.
	 * @param d The declination.
	 * @see #dec
	 */
	public void setDec(Dec d)
	{
		dec = d;
	}

	/**
	 * Set the target declination. Now allows declinations with no sign character, assumes positive.
	 * For ephemeris targets these should be J2000, topocentric, astrometric.
	 * @param s A string representing the declination, in space separated format.
	 * @see #dec
	 * @see org.estar.astrometry.Dec#parseColon(java.lang.String,boolean)
	 * @exception IllegalArgumentException Thrown if there is a problem with the parsing.
	 */
	public void parseDec(String s) throws IllegalArgumentException
	{
		dec = new Dec();
		dec.parseSpace(s);
	}

	/**
	 * Get the target declination.
	 * @return The declination.
	 * @see #dec
	 */
	public Dec getDec()
	{
		return dec;
	}

	/**
	 * Set the non-sidereal tracking rate in RA of the ephemeris at this point in time,in arcsec/hour.
	 * @param d The non-sidereal tracking rate in RA, in decimal arcsec/hour.
	 * @see #trackRateRA
	 */
	public void setTrackRateRA(double d)
	{
		trackRateRA = d;
	}

	/**
	 * Return the non-sidereal tracking rate in RA of the ephemeris at this point in time.
	 * @return The non-sidereal tracking rate in RA, in arcsec/hour. 
	 * @see #trackRateRA
	 */
	public double getTrackRateRA()
	{
		return trackRateRA;
	}

	/**
	 * Set the non-sidereal tracking rate in Declination of the ephemeris at this point in time,in arcsec/hour.
	 * @param d The non-sidereal tracking rate in Declination, in decimal arcsec/hour.
	 * @see #trackRateDec
	 */
	public void setTrackRateDec(double d)
	{
		trackRateDec = d;
	}

	/**
	 * Return the non-sidereal tracking rate in Declination of the ephemeris at this point in time.
	 * @return The non-sidereal tracking rate in Declination, in arcsec/hour. 
	 * @see #trackRateDec
	 */
	public double getTrackRateDec()
	{
		return trackRateDec;
	}

	/**
	 * Parse an ephemeris target line of the form:
	 * "2018-Sep-26 00:10 00 43 56.61 +11 08 37.2     -78.155203      65.006315"
	 * and fill out the fields of this instance of RTMLEphemerisTargetTrackNode accordingly.
	 * @param lineString One line of an ephemeris.
	 * @exception IllegalArgumentException Throwen if the parsing of the string fails.
	 * @see #timestamp
	 * @see #ra
	 * @see #dec
	 * @see #trackRateRA
	 * @see #trackRateDec
	 * @see #parseTimestamp
	 * @see #parseRA
	 * @see #parseDec
	 */
	public void parse(String lineString) throws IllegalArgumentException, ParseException
	{
		StringTokenizer stringTokenizer = new StringTokenizer(lineString," ");
		double dRA,dDec;

		if(stringTokenizer.countTokens() < 8)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":parse:Illegal Ephemeris target line "+lineString+
							   " only has "+stringTokenizer.countTokens()+" tokens.");
		}
		try 
		{
                        String dateString = (String) stringTokenizer.nextElement();
                        String timeString = (String) stringTokenizer.nextElement();
			try
			{
				parseTimestamp(dateString,timeString);
			}
			catch (Exception e) 
			{
				throw new IllegalArgumentException(this.getClass().getName()+
								   ":parse:Illegal Ephemeris target line "+lineString+
								   ":Failed to parse timestamp:"+dateString+" "+
								   timeString+":",e);
			}
			
                        String raHString = (String) stringTokenizer.nextElement();
                        String raMString = (String) stringTokenizer.nextElement();
                        String raSString = (String) stringTokenizer.nextElement();
			try
			{
				parseRA(raHString+" "+raMString+" "+raSString);
			}
			catch (Exception e) 
			{
				throw new IllegalArgumentException(this.getClass().getName()+
								   ":parse:Illegal Ephemeris target line "+lineString+
						      ":Failed to parse RA:"+raHString+" "+raMString+" "+raSString+
								   ":",e);
			}

                        String decDString = (String) stringTokenizer.nextElement();
                        String decMString = (String) stringTokenizer.nextElement();
                        String decSString = (String) stringTokenizer.nextElement();
			try
			{
				parseDec(decDString+" "+decMString+" "+decSString);
			}
			catch (Exception e) 
			{
				throw new IllegalArgumentException(this.getClass().getName()+
								   ":parse:Illegal Ephemeris target line "+lineString+
						      ":Failed to parse Dec:"+decDString+" "+decMString+" "+decSString+
								   ":",e);
			}
			// track rates are apparently optional
			if(stringTokenizer.countTokens() >= 10)
			{
				String trackRateRAString = (String) stringTokenizer.nextElement();
				try
				{
					dRA = Double.parseDouble(trackRateRAString);
				}
				catch (Exception e) 
				{
					throw new IllegalArgumentException(this.getClass().getName()+
						  ":parse:Illegal Ephemeris target line "+lineString+
						  ":Failed to parse RA track rate:"+trackRateRAString+":",e);
				}
				setTrackRateRA(dRA);
				String trackRateDecString = (String) stringTokenizer.nextElement();
				try
				{
					dDec = Double.parseDouble(trackRateDecString);
				}
				catch (Exception e) 
				{
					throw new IllegalArgumentException(this.getClass().getName()+
						  ":parse:Illegal Ephemeris target line "+lineString+
						  ":Failed to parse Dec track rate:"+trackRateDecString+":",e);
				}
				setTrackRateDec(dDec);
			}
			else
			{
				setTrackRateRA(0.0);
				setTrackRateDec(0.0);
			}
		} 
		catch (Exception e) 
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":parse:Illegal Ephemeris target line "+lineString+":",e);
                }
	}

	/**
	 * Print out the data contained in this instance of RTMLEphemerisTargetTrackNode as a 
	 * formatted ephemeris target line as follows:
	 * "2018-Sep-26 00:10 00 43 56.61 +11 08 37.2     -78.155203      65.006315"
	 * @see #EPHEMERIS_DATE_FORMAT
	 * @see #timestamp
	 * @see #ra
	 * @see #dec
	 * @see #trackRateRA
	 * @see #trackRateDec
	 */
	public String toString()
	{
		StringBuffer sb = null;
		SimpleDateFormat sdf = null;
		DecimalFormat df = null;

		// initialise string buffer and decimal format
		sb = new StringBuffer();
		df = new DecimalFormat("0.000000");
		// timestamp
		sdf = new SimpleDateFormat(EPHEMERIS_DATE_FORMAT);
		sb.append(sdf.format(timestamp));
		sb.append(" ");
		// ra
		sb.append(ra.toString(' '));
		sb.append(" ");
		// dec
		sb.append(dec.toString(' '));
		sb.append(" ");
		// trackRateRA
		sb.append(df.format(trackRateRA));
		sb.append(" ");
		// trackRateDec
		sb.append(df.format(trackRateDec));
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
*/

