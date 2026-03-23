/*   
    Copyright 2026, Astrophysics Research Institute, Liverpool John Moores University.

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
// RTMLAcquisition.java
package org.estar.rtml;

import java.io.*;
import java.text.*;

/**
 * This class is a data container for information contained in the Device sub-nodes of type 
 * in an RTML document. This is used to store the Moptop polarimeter rotor speed.
 * @author Chris Mottram
 * @version $Revision$
 */
public class RTMLAcquisition implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id$";
	/**
	 * String used to represent no acquisition required for the target.
	 */
	public final static String ACQUISITION_STRING_NONE = "none";
	/**
	 * String used to represent WCS (world coordinate system)  acquisition required for the target.
	 */
	public final static String ACQUISITION_STRING_WCS = "wcs";
	/**
	 * String used to represent brightest acquisition required for the target.
	 */
	public final static String ACQUISITION_STRING_BRIGHTEST = "brightest";
	/**
	 * Which acquisition mode to use for this target. One of:
	 * ACQUISITION_STRING_NONE / ACQUISITION_STRING_WCS / ACQUISITION_STRING_BRIGHTEST.
	 * @see #ACQUISITION_STRING_NONE
	 * @see #ACQUISITION_STRING_WCS
	 * @see #ACQUISITION_STRING_BRIGHTEST
	 */
	private String acquisitionMode = ACQUISITION_STRING_WCS;
	
	/**
	 * Default constructor.
	 */
	public RTMLAcquisition()
	{
		super();
	}

	/**
	 * Set the acquisition mode to use for the target..
	 * @param s The acquisition mode, one of "none" or "wcs" or "brightest".
	 * @exception IllegalArgumentException Thrown if the input string is not a legal value.
	 * @see #acquisitionMode
	 * @see #ACQUISITION_STRING_NONE
	 * @see #ACQUISITION_STRING_WCS
	 * @see #ACQUISITION_STRING_BRIGHTEST
	 */
	public void setAcquisitionMode(String s) throws IllegalArgumentException
	{
		if(s.equals(ACQUISITION_STRING_NONE)||s.equals(ACQUISITION_STRING_WCS)||
		   s.equals(ACQUISITION_STRING_BRIGHTEST))
		{
			acquisitionMode = s;
		}
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+":setAcquisitionMode: mpde '"+s+
				  "' is not a legal value ("+ACQUISITION_STRING_NONE+"/"+ACQUISITION_STRING_WCS+"/"+
							   ACQUISITION_STRING_BRIGHTEST+").");
		}
	}

	/**
	 * Return the current acquisition mode.
	 * @return An string representing the acquisition mode, one of:
	 *         ACQUISITION_STRING_NONE , ACQUISITION_STRING_WCS or ACQUISITION_STRING_BRIGHTEST.
	 * @see #acquisitionMode
	 * @see #ACQUISITION_STRING_NONE
	 * @see #ACQUISITION_STRING_WCS
	 * @see #ACQUISITION_STRING_BRIGHTEST
	 */
	public String getAcquisitionMode()
	{
		return acquisitionMode;
	}

	/**
	 * Method to print out a string representation of this node.
	 * @see #toString(java.lang.String)
	 */
	public String toString()
	{
		return toString("");
	}

	/**
	 * Method to print out a string representation of this node, with a prefix.
	 * @param prefix A string to prefix to each line of data we print out.
	 * @see #acquisitionMode
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;

		sb = new StringBuffer();
		sb.append(prefix+"Acquisition:\n");
		sb.append(prefix+"\tMode: "+acquisitionMode+"\n");
		return sb.toString();
	}
}
