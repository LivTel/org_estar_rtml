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
// RTMLSeeingConstraint.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLSeeingConstraint.java,v 1.4 2008-08-11 13:54:54 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;

/**
 * This class is a data container for information contained in the SeeingConstraint nodes/tags of an RTML document.
 * There are two main attributes in a SeeingConstraint:
 * <ul>
 * <li><b>minimum</b> The minimum (best) required seeing in arcseconds.
 * <li><b>maximum</b> The maximum (worst) required seeing in arcseconds.
 * </ul>
 * The SeeingConstraint constrains it's associated observation in the following manner:
 * <i>Do the observation if the seeing in arcseconds is worse (greater than) the minimum and 
 * better (less than) the maximum.</i>
 * Note the RTML2.2 DTD also supports "nominal" seeing and an "ExposureFactor" element to adjust the exposure length
 * by the (nominal-actual seeing) * ExposureFactor in some manner. This is not implemented, as it does not exist
 * in RTML 3. RTML 3 has the minimum and maximum attributes as sub-elements, and a fixed Units element.
 * @author Chris Mottram
 * @version $Revision$
 */
public class RTMLSeeingConstraint implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id$";
	/**
	 * Serial version ID. Fixed as these documents can be used as parameters in RMI calls across JVMs.
	 */
	static final long serialVersionUID = -6093777153073022079L;
	/**
	 * The minimum (best) required seeing in arcseconds.
	 */
	private double minimum = 0.0;
	/**
	 * The maximum (worst) required seeing in arcseconds.
	 */
	private double maximum = 10.0;

	/**
	 * Default constructor.
	 */
	public RTMLSeeingConstraint()
	{
		super();
	}

	/**
	 * Set the minimum (best) required seeing in arcseconds.
	 * @param s The minimum seeing, specified as a string representation of a double.
	 * @exception NumberFormatException Thrown if the string parsing fails.
	 * @see #minimum
	 */
	public void setMinimum(String s) throws NumberFormatException
	{
		minimum = Double.parseDouble(s);
	}

	/**
	 * Set the minimum (best) required seeing in arcseconds.
	 * @param d The minimum seeing, specified as a double.
	 * @see #minimum
	 */
	public void setMinimum(double d)
	{
		minimum = d;
	}

	/**
	 * Get the minimum (best) required seeing in arcseconds..
	 * @return The minimum seeing in arc-seconds.
	 * @see #minimum
	 */
	public double getMinimum()
	{
		return minimum;
	}

	/**
	 * Set the maximum (best) required seeing in arcseconds.
	 * @param s The maximum seeing, specified as a string representation of a double.
	 * @exception NumberFormatException Thrown if the string parsing fails.
	 * @see #maximum
	 */
	public void setMaximum(String s) throws NumberFormatException
	{
		maximum = Double.parseDouble(s);
	}

	/**
	 * Set the maximum (best) required seeing in arcseconds.
	 * @param d The maximum seeing, specified as a double.
	 * @see #maximum
	 */
	public void setMaximum(double d)
	{
		maximum = d;
	}

	/**
	 * Get the maximum (best) required seeing in arcseconds.
	 * @return The maximum seeing in arc-seconds.
	 * @see #maximum
	 */
	public double getMaximum()
	{
		return maximum;
	}

	/**
	 * Test the equality of the seeing constraint.
	 * @param obj The other instance we are testing against.
	 * @return Returns true if the contents are the same, and false if they are not.
	 */
	public boolean equals(Object obj)
	{
		RTMLSeeingConstraint other = null;

		if(obj == null)
			return false;
		if((obj instanceof RTMLSeeingConstraint) == false)
			return false;
		other = (RTMLSeeingConstraint)obj;
		if(Math.abs(other.getMinimum() - minimum) > 0.0001)
			return false;
		if(Math.abs(other.getMaximum() - maximum) > 0.0001)
			return false;
		return true;
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
	 * @see #minimum
	 * @see #maximum
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"Seeing Constraint:\n");
		sb.append(prefix+"\tMinimum:"+minimum+" arcseconds\n");
		sb.append(prefix+"\tMaximum:"+maximum+" arcseconds\n");
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.3  2008/05/27 14:59:26  cjm
** Added serialVersionUID.
**
** Revision 1.2  2007/01/30 18:31:22  cjm
** gnuify: Added GNU General Public License.
**
** Revision 1.1  2005/06/08 11:39:35  cjm
** Initial revision
**
*/
