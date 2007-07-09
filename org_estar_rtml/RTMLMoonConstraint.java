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
// RTMLMoonConstraint.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLMoonConstraint.java,v 1.1 2007-07-09 11:44:46 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;

/**
 * This class is a data container for information contained in the MoonConstraint nodes/tags of an RTML document.
 * There are four main attributes in a MoonConstraint:
 * <ul>
 * <li><b>maxPhase</b> The maximum acceptable phase of the moon. Full moon is phase=1.0.
 * <li><b>distance</b> The minimum acceptable distance from the moon in units.
 * <li><b>units</b> The distance units, of type %angleUnits. 
 *     We implement a subset: (deg|degs|degrees|rad|rads|radians).
 * <li><b>width</b> The "width" attribute is a Lorentzian weight parameter describing how "inacceptable" is
 *     inacceptable (i.e. if the weight is 0.01, then this is 10x
 *     better than having a weight of 0.1 and 100x better than being at the moon).
 * </ul>
 * Only the <i>distance</i> and <i>units</i> attributes are currently used by the TEA.
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 */
public class RTMLMoonConstraint implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLMoonConstraint.java,v 1.1 2007-07-09 11:44:46 cjm Exp $";
	/**
	 * The maximum acceptable phase of the moon. Full moon is phase=1.0.
	 */
	private double maxPhase = 0.0;
	/**
	 * The minimum distance from the moon, in <i>units</i> angular units.
	 */
	private double distance = 0.0;
	/**
	 * The units of the distance measure.
	 */
	private String units = null;
	/**
	 * The "width" attribute is a
	 * Lorentzian weight parameter describing how "inacceptable" is
	 * inacceptable (i.e. if the weight is 0.01, then this is 10x
	 * better than having a weight of 0.1 and 100x better than being
	 * at the moon).
	 */
	private double width = 0.0;

	/**
	 * Default constructor.
	 */
	public RTMLMoonConstraint()
	{
		super();
	}

	/**
	 * Set the minimum distance from the moon, in <i>units</i> angular units.. 
	 * @param s The string, which should parse as a double.
	 * @exception NumberFormatException Thrown if the string parsing fails.
	 * @see #distance
	 */
	public void setDistance(String s) throws NumberFormatException
	{
		distance = Double.parseDouble(s);
	}

	/**
	 * Set the minimum distance from the moon, in <i>units</i> angular units.
	 * @param d The minimum distance, specified as a double.
	 * @see #distance
	 */
	public void setDistance(double d)
	{
		distance = d;
	}

	/**
	 * Get the minimum distance from the moon, in <i>units</i> angular units.
	 * @return The minimum distance in <i>units</i> angular units.
	 * @see #distance
	 */
	public double getDistance()
	{
		return distance;
	}

	/**
	 * Get the minimum distance from the moon, in degrees.
	 * @return The minimum distance in degrees.
	 * @exception IllegalArgumentException Thrown if the units are not supported/are null.
	 * @see #distance
	 * @see #units
	 * @see #isUnitsDegrees
	 * @see #isUnitsRadians
	 */
	public double getDistanceDegrees() throws IllegalArgumentException
	{
		if(units == null)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":getDistanceDegrees:units were null.");
		}
		if(isUnitsDegrees(units))
			return distance;
		else if(isUnitsRadians(units))
			return distance*(180.0/Math.PI);
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":getDistanceDegrees:Illegal units:"+units+".");
		}
	}

	/**
	 * Get the minimum distance from the moon, in radians.
	 * @return The minimum distance in radians.
	 * @exception IllegalArgumentException Thrown if the units are not supported/are null.
	 * @see #distance
	 * @see #units
	 * @see #isUnitsDegrees
	 * @see #isUnitsRadians
	 */
	public double getDistanceRadians() throws IllegalArgumentException
	{
		if(units == null)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":getDistanceRadians:units were null.");
		}
		if(isUnitsDegrees(units))
			return distance*(Math.PI/180.0);
		else if(isUnitsRadians(units))
			return distance;
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":getDistanceRadians:Illegal units:"+units+".");
		}
	}

	/**
	 * Set the units.
	 * @param s The units, must be one of (deg|degs|degrees|rad|rads|radians).
	 * @exception IllegalArgumentException Thrown if the units are not supported.
	 * @see #units
	 * @see #isUnitsDegrees
	 * @see #isUnitsRadians
	 */
	public void setUnits(String s) throws IllegalArgumentException
	{
		if((isUnitsDegrees(s)||isUnitsRadians(s))== false)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setUnits:Illegal units:"+units+".");
		}
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
	 * Is the passed ion string a representation of degrees units.
	 * @param s The string to test.
	 * @return True if the string is on of (deg|degs|degrees), else false.
	 */
	public boolean isUnitsDegrees(String s)
	{
		return ((s.equals("deg"))||(s.equals("degs"))||(s.equals("degrees")));
	}

	/**
	 * Is the passed ion string a representation of radians units.
	 * @param s The string to test.
	 * @return True if the string is on of (rad|rads|radians), else false.
	 */
	public boolean isUnitsRadians(String s)
	{
		return ((s.equals("rad"))||(s.equals("rads"))||(s.equals("radians")));
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
	 * @see #distance
	 * @see #units
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"Moon Constraint:\n");
		sb.append(prefix+"\tDistance:"+distance+" "+units+"\n");
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.2  2007/01/30 18:31:22  cjm
** gnuify: Added GNU General Public License.
**
** Revision 1.1  2005/06/08 11:39:35  cjm
** Initial revision
**
*/
