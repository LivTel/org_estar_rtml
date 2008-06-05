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
// RTMLTelescope.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLTelescope.java,v 1.1 2008-06-05 14:18:18 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;

/**
 * This class is a data container for information contained in the Telescope nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 * @see org.estar.rtml.RTMLAttributes
 */
public class RTMLTelescope extends RTMLAttributes implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLTelescope.java,v 1.1 2008-06-05 14:18:18 cjm Exp $";
	/**
	 * Serial version ID. Fixed as these documents can be used as parameters in RMI calls across JVMs.
	 */
	static final long serialVersionUID = 4696210110556867480L;
	/**
	 * The name of the telescope.
	 */
	protected String name = null;
	/**
	 * The aperture of the telescope.
	 * @see #apertureUnits
	 */
	protected double aperture = 0.0;
	/**
	 * The units of the aperture of the telescope.
	 * Should be "meters" in RTML3.1a, and one of %lengthUnits; entity in RTML 2.2.
	 * @see #aperture
	 */
	protected String apertureUnits = "meters";
	/**
	 * The type of the aperture of the telescope: one of:geometric|effective.
	 * Only used in RTML3.1a, defaults to "geometric".
	 * @see #aperture
	 */
	protected String apertureType = "geometric";
	/**
	 * The focal ratio of the telescope. Specified as f/\d+\.?\d* in RTML 3.1a i.e. "f/10".
	 */
	protected String focalRatio = null;
	/**
	 * The Focal Length of the telescope.
	 * @see #focalLengthUnits
	 */
	protected double focalLength = 0.0;
	/**
	 * The units of the focal length of the telescope.
	 * Should be "meters" in RTML3.1a, and one of %lengthUnits; entity in RTML 2.2.
	 * @see #focalLength
	 */
	protected String focalLengthUnits = "meters";
	/**
	 * Where the telescope is sited.
	 * @see org.estar.rtml.RTMLTelescopeLocation
	 */
	protected RTMLTelescopeLocation location = null;

	/**
	 * Default constructor.
	 */
	public RTMLTelescope()
	{
		super();
	}

	/**
	 * Method to set the name.
	 * @param s The name.
	 * @see #name
	 */
	public void setName(String s)
	{
		name = s;
	}

	/**
	 * Method to get the name.
	 * @return The name.
	 * @see #name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Method to set the aperture. The units of the value are stored in apertureUnits.
	 * @param d The aperture value. 
	 * @see #aperture
	 * @see #apertureUnits
	 */
	public void setAperture(double d)
	{
		aperture = d;
	}

	/**
	 * Method to set the aperture. The units of the value are stored in apertureUnits.
	 * @param s The aperture's double value as a string. 
	 * @exception NumberFormatException Thrown if s is not a valid double.
	 * @see #aperture
	 * @see #apertureUnits
	 */
	public void setAperture(String s) throws NumberFormatException
	{
		aperture = Double.parseDouble(s);
	}

	/**
	 * Method to get the aperture.
	 * @return The aperture value.  The units of the value are stored in apertureUnits.
	 * @see #aperture
	 * @see #apertureUnits
	 */
	public double getAperture()
	{
		return aperture;
	}

	/**
	 * Method to get the aperture. It will convert the following units ( a subset of %lengthUnits in RTML 2.2):
	 * "m"|"meter"|"meters"|"metres"|"cm"|"centimeter"|"centimeters"|"mm"|"millimeter"|"millimeters"|
	 * "ft"|"foot"|"feet"|"inch"|"inches".
	 * @return The aperture value, in meters.
	 * @exception IllegalArgumentException Thrown if apertureUnits are not convertable.
	 * @see #aperture
	 * @see #apertureUnits
	 * @see #getLengthUnitsMetersMultiplicationFactor
	 */
	public double getApertureMeters() throws IllegalArgumentException
	{
		double multiplicationFactor;

		multiplicationFactor = getLengthUnitsMetersMultiplicationFactor(apertureUnits);
		return aperture*multiplicationFactor;
	}

	/**
	 * Method to set the aperture units.
	 * @param s The aperture units. Should be "meters" in RTML3.1a, and one of %lengthUnits; entity in RTML 2.2.
	 * @see #apertureUnits
	 */
	public void setApertureUnits(String s)
	{
		apertureUnits = s;
	}

	/**
	 * Method to get the aperture units.
	 * @return The aperture units.
	 * @see #apertureUnits
	 */
	public String getApertureUnits()
	{
		return apertureUnits;
	}

	/**
	 * Method to set the aperture type.
	 * @param s The aperture type. Only used in RTML 3.1a, one of:geometric|effective .
	 * @see #apertureType
	 */
	public void setApertureType(String s)
	{
		apertureType = s;
	}

	/**
	 * Method to get the aperture type.
	 * @return The aperture type.
	 * @see #apertureType
	 */
	public String getApertureType()
	{
		return apertureType;
	}

	/**
	 * Method to set the focal ratio.
	 * @param s The focal ratio.
	 * @see #focalRatio
	 */
	public void setFocalRatio(String s)
	{
		focalRatio = s;
	}

	/**
	 * Method to get the focal ratio.
	 * @return The focal ratio.
	 * @see #focalRatio
	 */
	public String getFocalRatio()
	{
		return focalRatio;
	}

	/**
	 * Method to set the focal length. The units of the value are stored in focalLengthUnits.
	 * @param d The focal length value. 
	 * @see #focalLength
	 * @see #focalLengthUnits
	 */
	public void setFocalLength(double d)
	{
		focalLength = d;
	}

	/**
	 * Method to set the focal length. The units of the value are stored in focalLengthUnits.
	 * @param s The focal length's double value as a string. 
	 * @exception NumberFormatException Thrown if s is not a valid double.
	 * @see #focalLength
	 * @see #focalLengthUnits
	 */
	public void setFocalLength(String s) throws NumberFormatException
	{
		focalLength = Double.parseDouble(s);
	}

	/**
	 * Method to get the focal length.
	 * @return The focal length value.  The units of the value are stored in focalLengthUnits.
	 * @see #focalLength
	 * @see #focalLengthUnits
	 */
	public double getFocalLength()
	{
		return focalLength;
	}

	/**
	 * Method to get the focal length. It will convert the following units ( a subset of %lengthUnits in RTML 2.2):
	 * "m"|"meter"|"meters"|"metres"|"cm"|"centimeter"|"centimeters"|"mm"|"millimeter"|"millimeters"|
	 * "ft"|"foot"|"feet"|"inch"|"inches".
	 * @return The focal length value, in meters.
	 * @exception IllegalArgumentException Thrown if focalLengthUnits are not convertable.
	 * @see #focalLength
	 * @see #focalLengthUnits
	 * @see #getLengthUnitsMetersMultiplicationFactor
	 */
	public double getFocalLengthMeters() throws IllegalArgumentException
	{
		double multiplicationFactor;

		multiplicationFactor = getLengthUnitsMetersMultiplicationFactor(focalLengthUnits);
		return focalLength*multiplicationFactor;
	}

	/**
	 * Method to set the focal length units.
	 * @param s The focal length units. Should be "meters" in RTML3.1a, 
	 *        and one of %lengthUnits; entity in RTML 2.2.
	 * @see #focalLengthUnits
	 */
	public void setFocalLengthUnits(String s)
	{
		focalLengthUnits = s;
	}

	/**
	 * Method to get the focal length units.
	 * @return The focal length units.
	 * @see #focalLengthUnits
	 */
	public String getFocalLengthUnits()
	{
		return focalLengthUnits;
	}

	/**
	 * Method to set the telescope location.
	 * @param o The telescope location.
	 * @see #location
	 */
	public void setLocation(RTMLTelescopeLocation o)
	{
		location = o;
	}

	/**
	 * Method to get the telescope location.
	 * @return The telescope location.
	 * @see #location
	 */
	public RTMLTelescopeLocation getLocation()
	{
		return location;
	}

	/**
	 * String representation of this Telescope.
	 * @see #toString(java.lang.String)
	 */
	public String toString()
	{
		return(toString(""));
	}

	/**
	 * String representation of this Telescope, with specified prefix.
	 * @param prefix A string to prefix to each line of data we print out.
	 * @see #name
	 * @see #aperture
	 * @see #apertureUnits
	 * @see #apertureType
	 * @see #focalRatio
	 * @see #focalLength
	 * @see #focalLengthUnits
	 * @see #location
	 * @see org.estar.rtml.RTMLAttributes#toString(java.lang.String)
	 */
	public String toString(String prefix)
	{
		DecimalFormat nf = null;
		StringBuffer sb = new StringBuffer();

		nf = new DecimalFormat("###0.0#");
		sb.append(prefix+"Telescope :\n");
		sb.append(super.toString(prefix+"\t"));
		sb.append(prefix+"\tName : "+name+"\n");
		if((aperture != 0.0)&&(apertureUnits != null))	
			sb.append(prefix+"\tAperture : "+nf.format(aperture)+" "+apertureUnits+"("+apertureType+")\n");
		if(focalRatio != null)	
			sb.append(prefix+"\tFocal Ratio : "+focalRatio+"\n");
		if((focalLength != 0.0)&&(focalLengthUnits != null))	
			sb.append(prefix+"\tFocal Length : "+nf.format(focalLength)+" "+focalLengthUnits+"\n");
		if(location != null)	
			sb.append(location.toString(prefix+"\t")+"\n");
		return(sb.toString());
	}

	/**
	 * Get the multiplication factor to apply to the units specified to return a length in meters.
	 * It will convert the following units ( a subset of %lengthUnits in RTML 2.2):
	 * "m"|"meter"|"meters"|"metres"|"cm"|"centimeter"|"centimeters"|"mm"|"millimeter"|"millimeters"|
	 * "ft"|"foot"|"feet"|"inch"|"inches".
	 * @param s The units string.
	 * @return The multiplication value.
	 * @exception IllegalArgumentException Thrown if the units are not convertable.
	 */
	protected double getLengthUnitsMetersMultiplicationFactor(String s)
	{
		double multiplicationFactor;

		multiplicationFactor = 1.0;
		if(s.equals("m"))
			multiplicationFactor = 1.0;
		else if(s.equals("meter"))
			multiplicationFactor = 1.0;
		else if(s.equals("meters"))
			multiplicationFactor = 1.0;
		else if(s.equals("metres"))
			multiplicationFactor = 1.0;
		else if(s.equals("cm"))
			multiplicationFactor = 0.01;
		else if(s.equals("centimeter"))
			multiplicationFactor = 0.01;
		else if(s.equals("centimeters"))
			multiplicationFactor = 0.01;
		else if(s.equals("mm"))
			multiplicationFactor = 0.001;
		else if(s.equals("millimeter"))
			multiplicationFactor = 0.001;
		else if(s.equals("millimeters"))
			multiplicationFactor = 0.001;
		else if(s.equals("ft"))
			multiplicationFactor = 0.3048;
		else if(s.equals("foot"))
			multiplicationFactor = 0.3048;
		else if(s.equals("feet"))
			multiplicationFactor = 0.3048;
		else if(s.equals("inch"))
			multiplicationFactor = 0.0254;
		else if(s.equals("inches"))
			multiplicationFactor = 0.0254;
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
						":getLengthUnitsMetersMultiplicationFactor: Illegal units:"+s);
		}
		return multiplicationFactor;
	}
}
//
// $Log: not supported by cvs2svn $
//
