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
// RTMLExtinctionConstraint.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLExtinctionConstraint.java,v 1.1 2011-02-09 18:40:42 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;

/**
 * This class is a data container for information contained in the ExtinctionConstraint nodes/tags of an RTML document.
 * The elements in a ExtinctionConstraint are:
 * <ul>
 * <li><b>Clouds</b> A string based description, one of: (clear|light|scattered|heavy).
 * <li><b>Magnitudes</b> The extinction in magnitudes.
 * </ul>
 * Only the <i>Clouds</i> value is used by the TEA at the moment. 
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 */
public class RTMLExtinctionConstraint implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLExtinctionConstraint.java,v 1.1 2011-02-09 18:40:42 cjm Exp $";
	/**
	 * Serial version ID. Fixed as these documents can be used as parameters in RMI calls across JVMs.
	 */
	static final long serialVersionUID = 7432524348364737357L;
	/**
	 * A description of the clouds: (clear|light|scattered|heavy) are acceptable.
	 */
	private String clouds = null;
	/**
	 * The extinction in magnitudes.
	 */
	private double value = 0.0;

	/**
	 * Default constructor.
	 */
	public RTMLExtinctionConstraint()
	{
		super();
	}

	/**
	 * Set the value of extinction in magnitudes.
	 * @param s The value specified as a string representation of a double.
	 * @exception NumberFormatException Thrown if the string parsing fails.
	 * @see #value
	 */
	public void setValue(String s) throws NumberFormatException
	{
		value = Double.parseDouble(s);
	}

	/**
	 * Set the value of extinction in magnitudes.
	 * @param d The value, specified as a double.
	 * @see #value
	 */
	public void setValue(double d)
	{
		value = d;
	}

	/**
	 * Get the value.
	 * @return The value of extinction in magnitudes.
	 * @see #value
	 */
	public double getValue()
	{
		return value;
	}

	/**
	 * Set the extinction in terms of cloud cover.
	 * @param s A string, a valid description of the cloud cover (clear|light|scattered|heavy).
	 * @exception IllegalArgumentException Thrown if the string is not a valid description.
	 * @see #clouds
	 * @see #isClear
	 * @see #isLight
	 * @see #isScattered
	 * @see #isHeavy
	 */
	public void setClouds(String s) throws IllegalArgumentException
	{
		if(!(isClear(s)||isLight(s)||(isScattered(s))||(isHeavy(s))))
		{
			throw new IllegalArgumentException(this.getClass().getName()+":setClouds:Illegal cloud cover:"
							   +s+".");
		}
		clouds = s;
	}

	/**
	 * Get the cloud cover description.
	 * @return The string representing cloud cover.
	 * @see #clouds
	 */
	public String getClouds()
	{
		return clouds;
	}

	/**
	 * Is the cloud cover "clear"? 
	 * Only looks at the <i>clouds</i> description at the present time.
	 * @return A boolean, true if the <i>clouds</i> description is "clear", false otherwise.
	 * @see #clouds
	 * @see #isClear(java.lang.String)
	 */
	public boolean isClear()
	{
		return isClear(clouds);
	}

	/**
	 * Is the specified cloud cover string "clear"? 
	 * @param s The string representing the cloud cover.
	 * @return A boolean, true if the string cloud cover is "clear", false otherwise.
	 */
	protected boolean isClear(String s)
	{
		return s.equals("clear");
	}

	/**
	 * Is the cloud cover "light"? 
	 * Only looks at the <i>clouds</i> description at the present time.
	 * @return A boolean, true if the <i>clouds</i> description is "light", false otherwise.
	 * @see #clouds
	 * @see #isLight(java.lang.String)
	 */
	public boolean isLight()
	{
		return isLight(clouds);
	}

	/**
	 * Is the specified cloud cover string "light"? 
	 * @param s The string representing the cloud cover.
	 * @return A boolean, true if the string cloud cover is "light", false otherwise.
	 */
	protected boolean isLight(String s)
	{
		return s.equals("light");
	}

	/**
	 * Is the cloud cover "scattered"? 
	 * Only looks at the <i>clouds</i> description at the present time.
	 * @return A boolean, true if the <i>clouds</i> description is "scattered", false otherwise.
	 * @see #clouds
	 * @see #isScattered(java.lang.String)
	 */
	public boolean isScattered()
	{
		return isScattered(clouds);
	}

	/**
	 * Is the specified cloud cover string "scattered"? 
	 * @param s The string representing the cloud cover.
	 * @return A boolean, true if the string cloud cover is "scattered", false otherwise.
	 */
	protected boolean isScattered(String s)
	{
		return s.equals("scattered");
	}

	/**
	 * Is the cloud cover "heavy"? 
	 * Only looks at the <i>clouds</i> description at the present time.
	 * @return A boolean, true if the <i>clouds</i> description is "heavy", false otherwise.
	 * @see #clouds
	 * @see #isHeavy(java.lang.String)
	 */
	public boolean isHeavy()
	{
		return isHeavy(clouds);
	}

	/**
	 * Is the specified cloud cover string "heavy"? 
	 * @param s The string representing the cloud cover.
	 * @return A boolean, true if the string cloud cover is "heavy", false otherwise.
	 */
	protected boolean isHeavy(String s)
	{
		return s.equals("heavy");
	}

	/**
	 * Is the cloud cover photometric.
	 * Only looks at the <i>clouds</i> description at the present time.
	 * @return A boolean, true if the <i>clouds</i> description is "clear", false otherwise.
	 * @see #clouds
	 * @see #isClear()
	 */
	public boolean isPhotometric()
	{
		return isClear();
	}

	/**
	 * Is the cloud cover spectroscopic.
	 * Only looks at the <i>clouds</i> description at the present time.
	 * @return A boolean, true if the <i>clouds</i> description is "light|scattered|heavy", false otherwise.
	 * @see #clouds
	 * @see #isLight()
	 * @see #isScattered()
	 * @see #isHeavy()
	 */
	public boolean isSpectroscopic()
	{
		return isLight()||isScattered()||isHeavy();
	}

	/**
	 * Test the equality of the extinction constraint.
	 * @param obj The other instance we are testing against.
	 * @return Returns true if the contents are the same, and false if they are not.
	 */
	public boolean equals(Object obj)
	{
		RTMLExtinctionConstraint other = null;

		if(obj == null)
			return false;
		if((obj instanceof RTMLExtinctionConstraint) == false)
			return false;
		other = (RTMLExtinctionConstraint)obj;
		if(other.getClouds().equals(clouds) == false)
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
	 * @see #clouds
	 * @see #isPhotometric
	 * @see #isSpectroscopic
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"Extinction Constraint:\n");
		sb.append(prefix+"\tCloud Cover:"+clouds+".\n");
		sb.append(prefix+"\tPhotometric:"+isPhotometric()+".\n");
		sb.append(prefix+"\tSpectroscopic:"+isSpectroscopic()+".\n");
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
*/
	
