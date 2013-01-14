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
// RTMLDetector.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLDetector.java,v 1.6 2013-01-14 11:04:42 cjm Exp $
package org.estar.rtml;

import java.io.*;

/**
 * This class is a data container for information contained in the Detector nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.6 $
 * @see org.estar.rtml.RTMLAttributes
 */
public class RTMLDetector extends RTMLAttributes implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLDetector.java,v 1.6 2013-01-14 11:04:42 cjm Exp $";
	/**
	 * Serial version ID. Fixed as these documents can be used as parameters in RMI calls across JVMs.
	 */
	static final long serialVersionUID = -4089518386587922272L;
	/**
	 * The Detector Binning row number.
	 */
	private int rowBinning = 2;
	/**
	 * The Detector Binning column number.
	 */
	private int columnBinning = 2;
	/**
	 * The gain to be used by the detector. Only really used for EMCCDs, where we take it to mean
	 * EMGain, which is not quite the same thing, but close enough.
	 */
	private double gain = 1.0;
	/**
	 * This boolean is used to indicate the gain value has been set.
	 */
	private boolean useGain = false;

	/**
	 * Default constructor.
	 * @see #useGain
	 */
	public RTMLDetector()
	{
		super();
		useGain = false;
	}

	/**
	 * Method to set the row binning.
	 * @param i The row binning.
	 * @see #rowBinning
	 */
	public void setRowBinning(int i)
	{
		rowBinning = i;
	}

	/**
	 * Method to set the row binning.
	 * @param s The row binning as a string.
	 * @exception RTMLException Thrown if the string is not a valid integer.
	 * @see #rowBinning
	 */
	public void setRowBinning(String s) throws RTMLException
	{
		try
		{
			rowBinning = Integer.parseInt(s);
		}
		catch(NumberFormatException e)
		{
			throw new RTMLException(this.getClass().getName()+":setRowBinning:Number Format Exception:"+
						s+":",e);
		}
	}

	/**
	 * Method to get the row binning.
	 * @return The row binning.
	 * @see #rowBinning
	 */
	public int getRowBinning()
	{
		return rowBinning;
	}

	/**
	 * Method to set the column binning.
	 * @param i The column binning.
	 */
	public void setColumnBinning(int i)
	{
		columnBinning = i;
	}

	/**
	 * Method to set the column binning.
	 * @param s The column binning as a string.
	 * @exception RTMLException Thrown if the string is not a valid integer.
	 * @see #columnBinning
	 */
	public void setColumnBinning(String s) throws RTMLException
	{
		try
		{
			columnBinning = Integer.parseInt(s);
		}
		catch(NumberFormatException e)
		{
			throw new RTMLException(this.getClass().getName()+":setColumnBinning:Number Format Exception:"+
						s+":",e);
		}
	}

	/**
	 * Method to get the column binning.
	 * @return The column binning.
	 * @see #columnBinning
	 */
	public int getColumnBinning()
	{
		return columnBinning;
	}

	/**
	 * Method to set the gain.
	 * @param d The gain.
	 * @see #gain
	 * @see #useGain
	 */
	public void setGain(double d)
	{
		gain = d;
		useGain = true;
	}

	/**
	 * Method to set the gain.
	 * @param s The gain as a double string.
	 * @exception RTMLException Thrown if the string is not a valid double.
	 * @see #gain
	 * @see #useGain
	 */
	public void setGain(String s) throws RTMLException
	{
		try
		{
			gain = Double.parseDouble(s);
			useGain = true;
		}
		catch(NumberFormatException e)
		{
			throw new RTMLException(this.getClass().getName()+":setGain:Number Format Exception:"+
						s+":",e);
		}
	}

	/**
	 * Method to get the gain.
	 * @return The gain. This is normally only used for EMCCD, where it represents the 
	 *         multiplying factor.
	 * @see #gain
	 */
	public double getGain()
	{
		return gain;
	}

	/**
	 * Get whether the gain value has been set.
	 * @return A boolean, true if the gain value has been set.
	 * @see #useGain
	 */
	public boolean getUseGain()
	{
		return useGain;
	}

	/**
	 * String representation of this Detector.
	 * @see #toString(java.lang.String)
	 */
	public String toString()
	{
		return(toString(""));
	}

	/**
	 * String representation of this Detector, with specified prefix.
	 * @param prefix A string to prefix to each line of data we print out.
	 * @see #rowBinning
	 * @see #columnBinning
	 * @see org.estar.rtml.RTMLAttributes#toString(java.lang.String)
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append(prefix+"Detector :\n");
		sb.append(super.toString(prefix+"\t"));
		sb.append(prefix+"\tRow Binning : "+rowBinning+"\n");
		sb.append(prefix+"\tColumn Binning : "+columnBinning+"\n");
		if(useGain)
			sb.append(prefix+"\tGain : "+gain+"\n");
		return(sb.toString());
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 1.5  2008/05/27 14:07:55  cjm
// Added serialVersionUID.
//
// Revision 1.4  2008/05/23 14:15:24  cjm
// Now extends RTMLAttributes.
//
// Revision 1.3  2007/01/30 18:31:11  cjm
// gnuify: Added GNU General Public License.
//
// Revision 1.2  2005/06/08 11:38:29  cjm
// Fixed comments.
// Reformatted.
//
// Revision 1.1  2005/01/19 11:53:42  cjm
// Initial revision
//
// Revision 1.1  2005/01/18 15:08:28  cjm
// Initial revision
//
//
