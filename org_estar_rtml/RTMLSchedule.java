// RTMLSchedule.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLSchedule.java,v 1.2 2005-01-19 15:30:38 cjm Exp $
package org.estar.rtml;

import java.io.*;
import org.estar.astrometry.*;

/**
 * This class is a data container for information contained in the Schedule nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.2 $
 */
public class RTMLSchedule implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLSchedule.java,v 1.2 2005-01-19 15:30:38 cjm Exp $";
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
	public boolean isTypeSNR()
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
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"Schedule:\n");
		sb.append(prefix+"\tExposure: type = "+exposureType+": units = "+exposureUnits+"\n");
		sb.append(prefix+"\t\tLength:"+exposureLength+"\n");
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.1  2003/02/24 13:19:56  cjm
** Initial revision
**
*/
