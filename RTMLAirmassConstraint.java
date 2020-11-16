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
// RTMLAirmassConstraint.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLAirmassConstraint.java,v 1.3 2011-02-10 14:49:45 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;

/**
 * This class is a data container for information contained in the AirmassConstraint nodes/tags of an RTML document.
 * The attributes in a AirmassConstraint are:
 * <ul>
 * <li><b>maximum</b> The maximum airmass as a double.
 * <li><b>minimum</b> The minimum airmass as a double.
 * </ul>
 * Only the <i>maximum</i> value is used by the TEA at the moment. 
 * @author Chris Mottram
 * @version $Revision$
 */
public class RTMLAirmassConstraint implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id$";
	/**
	 * Serial version ID. Fixed as these documents can be used as parameters in RMI calls across JVMs.
	 */
	static final long serialVersionUID = -5689766256888594078L;
	/**
	 * The minimum airmass to observe at.
	 */
	private double minimum = 0.0;
	/**
	 * The maximum airmass to observe at.
	 */
	private double maximum = 10.0;


	/**
	 * Default constructor.
	 */
	public RTMLAirmassConstraint()
	{
		super();
	}

	/**
	 * Set the minimum airmass.
	 * @param s The minimum value specified as a string representation of a double.
	 * @exception NumberFormatException Thrown if the string parsing fails.
	 * @see #minimum
	 */
	public void setMinimum(String s) throws NumberFormatException
	{
		minimum = Double.parseDouble(s);
	}

	/**
	 * Set the minimum airmass.
	 * @param d The minimum value, specified as a double.
	 * @see #minimum
	 */
	public void setMinimum(double d)
	{
		minimum = d;
	}

	/**
	 * Get the minimum airmass.
	 * @return The value of the minimum airmass.
	 * @see #minimum
	 */
	public double getMinimum()
	{
		return minimum;
	}

	/**
	 * Set the maximum airmass.
	 * @param s The maximum value specified as a string representation of a double.
	 * @exception NumberFormatException Thrown if the string parsing fails.
	 * @see #maximum
	 */
	public void setMaximum(String s) throws NumberFormatException
	{
		maximum = Double.parseDouble(s);
	}

	/**
	 * Set the maximum airmass.
	 * @param d The maximum value, specified as a double.
	 * @see #maximum
	 */
	public void setMaximum(double d)
	{
		maximum = d;
	}

	/**
	 * Get the maximum airmass.
	 * @return The value of the maximum airmass.
	 * @see #maximum
	 */
	public double getMaximum()
	{
		return maximum;
	}

	/**
	 * Test the equality of the airmass constraint.
	 * @param obj The other instance we are testing against.
	 * @return Returns true if the contents are the same, and false if they are not.
	 */
	public boolean equals(Object obj)
	{
		RTMLAirmassConstraint other = null;

		if(obj == null)
			return false;
		if((obj instanceof RTMLAirmassConstraint) == false)
			return false;
		other = (RTMLAirmassConstraint)obj;
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
		sb.append(prefix+"Airmass Constraint:\n");
		sb.append(prefix+"\tMinimum:"+minimum+".\n");
		sb.append(prefix+"\tMaximum:"+maximum+".\n");
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.2  2011/02/10 14:09:43  cjm
** Added serialVersionUID.
**
** Revision 1.1  2011/02/09 18:40:24  cjm
** Initial revision
**
*/
	
