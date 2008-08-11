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
// RTMLSkyConstraint.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLSkyConstraint.java,v 1.3 2008-08-11 13:54:54 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;

/**
 * This class is a data container for information contained in the SkyConstraint nodes/tags of an RTML document.
 * The attributes in a SkyConstraint are:
 * <ul>
 * <li><b>sky</b> A simple description of what sky we are constrained to, of type %simpleSkyTypes: 
 *     (dark|gray|grey|bright).
 * <li><b>value</b> A measure of how much sky brightness is acceptable, <i>units</i>.
 * <li><b>units</b> The units of <i>value</i>.
 * </ul>
 * Only the <i>sky</i> value is used by the TEA at the moment. 
 * @author Chris Mottram
 * @version $Revision: 1.3 $
 */
public class RTMLSkyConstraint implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLSkyConstraint.java,v 1.3 2008-08-11 13:54:54 cjm Exp $";
	/**
	 * Serial version ID. Fixed as these documents can be used as parameters in RMI calls across JVMs.
	 */
	static final long serialVersionUID = 1685937304465026200L;
	/**
	 * A description of the sky brightness: (dark|gray|grey|bright) are acceptable.
	 */
	private String sky = null;
	/**
	 * The sky brightness expressed as a number with <i>units</i> units.
	 * @see #units
	 */
	private double value = 0.0;
	/**
	 * The units of value.
	 * @see #value
	 */
	private String units = null;

	/**
	 * Default constructor.
	 */
	public RTMLSkyConstraint()
	{
		super();
	}

	/**
	 * Set the value.
	 * @param s The value specified as a string representation of a double.
	 * @exception NumberFormatException Thrown if the string parsing fails.
	 * @see #value
	 */
	public void setValue(String s) throws NumberFormatException
	{
		value = Double.parseDouble(s);
	}

	/**
	 * Set the value.
	 * @param d The value, specified as a double.
	 * @see #value
	 */
	public void setValue(double d)
	{
		value = d;
	}

	/**
	 * Get the value.
	 * @return The value.
	 * @see #value
	 */
	public double getValue()
	{
		return value;
	}

	/**
	 * Set the units of value.
	 * @param s The units.
	 * @exception IllegalArgumentException Thrown if the units are not valid.
	 * @see #units
	 * @see #value
	 */
	public void setUnits(String s) throws IllegalArgumentException
	{
		units = s;
	}

	/**
	 * Get the units.
	 * @return The units.
	 * @see #units
	 */
	public String getUnits()
	{
		return units;
	}

	/**
	 * Set the sky brightness description.
	 * @param s A string, a valid description of the sky brightness (dark|gray|grey|bright).
	 * @exception IllegalArgumentException Thrown if the string is not a valid description.
	 * @see #sky
	 * @see #isDark
	 * @see #isGrey
	 * @see #isBright
	 */
	public void setSky(String s) throws IllegalArgumentException
	{
		if(!(isDark(s)||isGrey(s)||(isBright(s))))
		{
			throw new IllegalArgumentException(this.getClass().getName()+":setSky:Illegal sky description:"
							   +s+".");
		}
		sky = s;
	}

	/**
	 * Get the sky brightness description.
	 * @return The string representing sky brightness.
	 * @see #sky
	 */
	public String getSky()
	{
		return sky;
	}

	/**
	 * Is the Sky constraint "dark"? 
	 * Only looks at the <i>sky</i> description at the present time.
	 * @return A boolean, true if the <i>sky</i> description is "dark", false otherwise.
	 * @see #sky
	 * @see #isDark(java.lang.String)
	 */
	public boolean isDark()
	{
		return isDark(sky);
	}

	/**
	 * Is the specified sky description string "dark"? 
	 * @param s The string representing the sky description.
	 * @return A boolean, true if the string description is "dark", false otherwise.
	 */
	protected boolean isDark(String s)
	{
		return s.equals("dark");
	}

	/**
	 * Is the Sky constraint "grey/gray"? 
	 * Only looks at the <i>sky</i> description at the present time.
	 * @return A boolean, true if the <i>sky</i> description is "grey/gray", false otherwise.
	 * @see #sky
	 * @see #isGrey(java.lang.String)
	 */
	public boolean isGrey()
	{
		return isGrey(sky);
	}

	/**
	 * Is the specified sky description string "grey/gray"? 
	 * @param s The string representing the sky description.
	 * @return A boolean, true if the string description is "grey/gray", false otherwise.
	 */
	protected boolean isGrey(String s)
	{
		return (s.equals("grey")||s.equals("gray"));
	}

	/**
	 * Is the Sky constraint "bright"? 
	 * Only looks at the <i>sky</i> description at the present time.
	 * @return A boolean, true if the <i>sky</i> description is "bright", false otherwise.
	 * @see #sky
	 * @see #isBright(java.lang.String)
	 */
	public boolean isBright()
	{
		return isBright(sky);
	}

	/**
	 * Is the specified sky description string "bright"? 
	 * @param s The string representing the sky description.
	 * @return A boolean, true if the string description is "bright", false otherwise.
	 */
	protected boolean isBright(String s)
	{
		return s.equals("bright");
	}

	/**
	 * Test the equality of the sky constraint.
	 * @param obj The other instance we are testing against.
	 * @return Returns true if the contents are the same, and false if they are not.
	 */
	public boolean equals(Object obj)
	{
		RTMLSkyConstraint other = null;

		if(obj == null)
			return false;
		if((obj instanceof RTMLSkyConstraint) == false)
			return false;
		other = (RTMLSkyConstraint)obj;
		if(other.getSky().equals(sky) == false)
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
	 * @see #sky
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"Sky Constraint:\n");
		sb.append(prefix+"\tSky description:"+sky+".\n");
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.2  2008/05/27 15:00:09  cjm
** Added serialVersionUID.
**
** Revision 1.1  2007/07/09 11:44:55  cjm
** Initial revision
**
*/
