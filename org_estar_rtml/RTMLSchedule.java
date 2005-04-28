// RTMLSchedule.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLSchedule.java,v 1.5 2005-04-28 09:40:01 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;
import java.util.Date;

import org.estar.astrometry.*;

/**
 * This class is a data container for information contained in the Schedule nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.5 $
 */
public class RTMLSchedule implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLSchedule.java,v 1.5 2005-04-28 09:40:01 cjm Exp $";
	/**
	 * The type of the Exposure, the "type" attribute in the Exposure tag. Should be either "time" or
	 * "snr".
	 */
	private String exposureType = null;
	/**
	 * The units of the Exposure, the "units" attribute in the Exposure tag.
	 */
	private String exposureUnits = null;
	/**
	 * The length of the Exposure, the Exposure tag's text.
	 */
	private double exposureLength = 0.0;
	/**
	 * The date this observation can be scheduled from. i.e.
	 * the observation should be started AFTER this time.
	 */
	private Date startDate = null;
	/**
	 * The date this observation can be scheduled until. i.e.
	 * the observation should be started BEFORE this time.
	 */
	private Date endDate = null;
	/**
	 * Object containing details of any specified series constraint.
	 * This reference can be null, if no series constraint was specified.
	 */
	private RTMLSeriesConstraint seriesConstraint = null;
	/**
	 * The calibration data that may be contained a a sub-tag.
	 */
	/*diddly	private RTMLCalibration calibration = null;*/

	/**
	 * Default constructor.
	 */
	public RTMLSchedule()
	{
		super();
	}

	/**
	 * Set the schedule exposure type.
	 * @param s The type of the exposure. Should be either "time", or "snr". See DTD, %exposureType;.
	 * @exception IllegalArgumentException Thrown if the type is nor legal.
	 * @see #exposureType
	 */
	public void setExposureType(String s) throws IllegalArgumentException
	{
		if((s.equals("time") == false)&&(s.equals("snr") == false))
		{
			throw new IllegalArgumentException(this.getClass().getName()+":setExposureType:Exposure Type "+
							   s+" not legal.");
		}
		exposureType = s;
	}

	/**
	 * Get the exposure type.
	 * @return A string representing the exposure type, either "time", or "snr".
	 * @see #exposureType
	 */
	public String getExposureType()
	{
		return exposureType;
	}

	/**
	 * Return whether the exposure type is time.
	 * @return Returns true if the exposure type is normal, otherwise false.
	 * @see #exposureType
	 */
	public boolean isExposureTypeTime()
	{
		return exposureType.equals("time");
	}

	/**
	 * Return whether the exposure type is a signal to noise ration.
	 * @return Returns true if the exposure type is snr, otherwise false.
	 * @see #exposureType
	 */
	public boolean isExposureTypeSNR()
	{
		return exposureType.equals("snr");
	}

	/**
	 * Set the exposure units.
	 * @param s The units.
	 * @see #exposureUnits
	 */
	public void setExposureUnits(String s)
	{
		exposureUnits = s;
	}

	/**
	 * Get the exposure units.
	 * @return The units.
	 * @see #exposureUnits
	 */
	public String getExposureUnits()
	{
		return exposureUnits;
	}

	/**
	 * Set the exposure length. What this value actually means is based on the units and type fields.
	 * @param s The length, either as a length or a signal to noise ratio.
	 * @see #exposureLength
	 */
	public void setExposureLength(String s) throws NumberFormatException
	{
		exposureLength = Double.parseDouble(s);
	}

	/**
	 * Set the exposure length. What this value actually means is based on the units and type fields.
	 * @param s The length, either as a length or a signal to noise ratio.
	 * @see #exposureLength
	 */
	public void setExposureLength(double d)
	{
		exposureLength = d;
	}

	/**
	 * Get the exposure length.
	 * @return The length.
	 * @see #exposureLength
	 */
	public double getExposureLength()
	{
		return exposureLength;
	}

	/**
	 * Set the date the observation can be scheduled AFTER.
	 * @param d A date.
	 * @see #startDate
	 */
	public void setStartDate(Date d)
	{
		startDate = d;
	}

	/**
	 * Set the date the observation can be scheduled AFTER.
	 * @param s A string, in the format yyyy-MM-dd'T'HH:mm:ss.
	 * @see #startDate
	 * @exception RTMLException Thrown if the string is not a valid date/time, 
	 *            using the format specified above.
	 */
	public void setStartDate(String s) throws RTMLException
	{
		DateFormat dateFormat = null;

		dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try
		{
			startDate = dateFormat.parse(s);
		}
		catch(ParseException e)
		{
			throw new RTMLException(this.getClass().getName()+":setStartDate:Illegal Date/Time:"+s+":",e);
		}
	}

	/**
	 * Get the date the observation can be scheduled AFTER.
	 * @return A date.
	 * @see #startDate
	 */
	public Date getStartDate()
	{
		return startDate;
	}

	/**
	 * Set the date the observation should be scheduled BEFORE.
	 * @param d A date.
	 * @see #endDate
	 */
	public void setEndDate(Date d)
	{
		endDate = d;
	}

	/**
	 * Set the date the observation should be scheduled BEFORE.
	 * @param s A string, in the format yyyy-MM-dd'T'HH:mm:ss.
	 * @exception RTMLException Thrown if the string is not a valid date/time, 
	 *            using the format specified above.
	 * @see #startDate
	 */
	public void setEndDate(String s) throws RTMLException
	{
		DateFormat dateFormat = null;

		dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try
		{
			endDate = dateFormat.parse(s);
		}
		catch(ParseException e)
		{
			throw new RTMLException(this.getClass().getName()+":setEndDate:Illegal Date/Time:"+s+":",e);
		}
	}

	/**
	 * Get the date the observation should be scheduled BEFORE.
	 * @return A date.
	 * @see #endDate
	 */
	public Date getEndDate()
	{
		return endDate;
	}


	/**
	 * Set the series constraint data. This data contains information on how to repeat
	 * the observation over time (e.g. a MonitorGroup, repeat the observation <b>count</b> time 
	 * with an elapsed time between repeats of <b>interval</b> +/- <b>tolerance</b>.
	 * @param sc A series constrint. This can be null.
	 * @see #seriesConstraint
	 */
	public void setSeriesConstraint(RTMLSeriesConstraint sc)
	{
		seriesConstraint = sc;
	}

	/**
	 * Get the series constraint data.
	 * @return A series constrint. This can be null.
	 * @see #seriesConstraint
	 */
	public RTMLSeriesConstraint getSeriesConstraint()
	{
		return seriesConstraint;
	}

	/**
	 * Determine whether this schedule specifies a flexibly scheduled one off observation,
	 * or a regularily spaced monitor group observation.
	 * Currently a schedule containing a series constraint with a count > 1 is counted as a monitor group.
	 * Note the interval and/or tolerance can still be null in this case (suitable defaults should be used?).
	 * @return Returns true if this is a monitor group, false otherwise.
	 */
	public boolean isMonitorGroup()
	{
		if(seriesConstraint != null)
		{
			if(seriesConstraint.getCount() > 1)
				return true;
		}
		return false;
	}

	/**
	 * Method to print out a string representation of this node.
	 */
	public String toString()
	{
		return toString("");
	}

	/**
	 * Method to print out a string representation of this node, with a prefix.
	 * @param prefix A string to prefix to each line of data we print out.
	 * @see #exposureType
	 * @see #exposureUnits
	 * @see #exposureLength
	 * @see #startDate
	 * @see #endDate
	 * @see #seriesConstraint
	 * @see #isMonitorGroup
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"Schedule: (Monitor Group:"+isMonitorGroup()+")\n");
		sb.append(prefix+"\tExposure: type = "+exposureType+": units = "+exposureUnits+"\n");
		sb.append(prefix+"\t\tLength:"+exposureLength+"\n");
		if(getSeriesConstraint() != null)
			sb.append(getSeriesConstraint().toString(prefix+"\t"));
		sb.append(prefix+"\tBetween:"+startDate+" and "+endDate+"\n");
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.4  2005/04/27 15:36:40  cjm
** Added SeriesConstraint handling.
**
** Revision 1.3  2005/04/26 11:25:53  cjm
** Added time constraint : startDate and endDate.
**
** Revision 1.2  2005/01/19 15:30:38  cjm
** Added Serializable.
**
** Revision 1.1  2003/02/24 13:19:56  cjm
** Initial revision
**
*/
