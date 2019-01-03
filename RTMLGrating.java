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
// RTMLGrating.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLGrating.java,v 1.5 2009-08-12 17:50:25 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;

/**
 * This class is a data container for information contained in the Grating nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.5 $
 */
public class RTMLGrating implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLGrating.java,v 1.5 2009-08-12 17:50:25 cjm Exp $";
	/**
	 * Serial version ID. Fixed as these documents can be used as parameters in RMI calls across JVMs.
	 */
	static final long serialVersionUID = -3776514019215482505L;
	/**
	 * The name of the grating.
	 */
	private String name = null;
	/**
	 * The central wavelength of the grating. Units are in the units attribute.
	 */
	private double wavelength = 0.0;
	/**
	 * The central wavelength units of the grating. Shoule be a valid %lengthUnits;.
	 */
	private String wavelengthUnits = null;
	/**
	 * The resolution of the grating. 
	 */
	private double resolution = 0.0;
	/**
	 * The angle of the grating. 
	 */
	private double angle = 0.0;

	/**
	 * Default constructor.
	 */
	public RTMLGrating()
	{
		super();
	}

	/**
	 * Set the name of the grating.
	 * @param s The name.
	 * @see #name
	 */
	public void setName(String s)
	{
		name = s;
	}

	/**
	 * Get the name of the grating.
	 * @return The name.
	 * @see #name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Set the wavelength of the grating.
	 * @param d The wavelength.
	 * @see #wavelength
	 */
	public void setWavelength(double d)
	{
		wavelength = d;
	}

	/**
	 * Set the wavelength of the grating.
	 * @param s The wavelength as a string. This should be parsable as a valid double.
	 * @exception NumberFormatException Thrown if the string is not a valid double.
	 * @see #wavelength
	 */
	public void setWavelength(String s) throws NumberFormatException
	{
		wavelength = Double.parseDouble(s);
	}

	/**
	 * Get the wavelength of the grating.
	 * @return The wavelength. Returned as parsed, with units wavelengthUnits.
	 * @see #wavelength
	 */
	public double getWavelength()
	{
		return wavelength;
	}

	/**
	 * Get the wavelength of the grating, as a string, in wavelengthUnits.
	 * @return The wavelength as a string. Returned as parsed, with units wavelengthUnits.
	 * @see #wavelength
	 */
	public String getWavelengthString()
	{
		DecimalFormat df = null;

		df = new DecimalFormat("#0.0#");
		return df.format(wavelength);
	}

	/**
	 * Set the wavelength units of the grating.
	 * @param s The units of wavelength. This should be a valid %lengthUnits; according to the RTML DTD.
	 *       We currently only recognise a small subset:
	 *       m|meter|meters|metres|cm|centimeter|centimeters|mm|millimeter|millimeters|
	 *       micron|microns|nm|nanometer|nanometers|Angstrom|Angstroms.
	 *       And now some from the RTML 3.1a Schema:micrometers.
	 * @exception IllegalArgumentException Thrown if the units are not recognised.
	 * @see #wavelengthUnits
	 */
	public void setWavelengthUnits(String s) throws IllegalArgumentException
	{
		if((s.equals("m")||s.equals("meter")||s.equals("meters")||s.equals("metres")||
		   s.equals("cm")||s.equals("centimeter")||s.equals("centimeters")||
		   s.equals("mm")||s.equals("millimeter")||s.equals("millimeters")||
		   s.equals("micron")||s.equals("microns")||s.equals("micrometers")||
		   s.equals("nm")||s.equals("nanometer")||s.equals("nanometers")||
		    s.equals("Angstrom")||s.equals("Angstroms")) == false)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   "setWavelengthUnits: Illegal units:"+s);
		}
		wavelengthUnits = s;

	}

	/**
	 * Get the wavelength units of the grating.
	 * @return The wavelength units.
	 * @see #wavelengthUnits
	 */
	public String getWavelengthUnits()
	{
		return wavelengthUnits;
	}

	/**
	 * Get the wavelength of the grating, in Angstroms.
	 * @return The wavelength.
	 * @exception IllegalArgumentException Thrown if wavelengthUnits are not a recognised unit value.
	 * @see #wavelength
	 * @see #wavelengthUnits
	 */
	public double getWavelengthAngstroms() throws IllegalArgumentException
	{
		double multiplicationFactor;

		multiplicationFactor = 1.0;
		if(wavelengthUnits.equals("m"))
			multiplicationFactor = 10000000000.0;
		else if(wavelengthUnits.equals("meter"))
			multiplicationFactor = 10000000000.0;
		else if(wavelengthUnits.equals("meters"))
			multiplicationFactor = 10000000000.0;
		else if(wavelengthUnits.equals("metres"))
			multiplicationFactor = 10000000000.0;
		else if(wavelengthUnits.equals("cm"))
			multiplicationFactor = 100000000.0;
		else if(wavelengthUnits.equals("centimeter"))
			multiplicationFactor = 100000000.0;
		else if(wavelengthUnits.equals("centimeters"))
			multiplicationFactor = 100000000.0;
		else if(wavelengthUnits.equals("mm"))
			multiplicationFactor = 10000000.0;
		else if(wavelengthUnits.equals("millimeter"))
			multiplicationFactor = 10000000.0;
		else if(wavelengthUnits.equals("millimeters"))
			multiplicationFactor = 10000000.0;
		else if(wavelengthUnits.equals("micron"))
			multiplicationFactor = 10000.0;
		else if(wavelengthUnits.equals("microns"))
			multiplicationFactor = 10000.0;
		else if(wavelengthUnits.equals("nm"))
			multiplicationFactor = 10.0;
		else if(wavelengthUnits.equals("nanometer"))
			multiplicationFactor = 10.0;
		else if(wavelengthUnits.equals("nanometers"))
			multiplicationFactor = 10.0;
		else if(wavelengthUnits.equals("Angstrom"))
			multiplicationFactor = 1.0;
		else if(wavelengthUnits.equals("Angstroms"))
			multiplicationFactor = 1.0;
		else
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":getWavelengthAngstroms: Illegal units:"+wavelengthUnits);
		return wavelength*multiplicationFactor;
	}

	/**
	 * Get the wavelength of the grating, in Angstroms.
	 * @return The wavelength, as a string, to 1.d.p (as needed for Meaburn).
	 * @exception IllegalArgumentException Thrown if wavelengthUnits are not a recognised unit value.
	 * @see #wavelength
	 * @see #wavelengthUnits
	 * @see #getWavelengthAngstroms
	 */
	public String getWavelengthAngstromString() throws IllegalArgumentException
	{
		DecimalFormat df = null;
		double wavelengthAngstroms;

		// set to 1d.p for Meaburn
		df = new DecimalFormat("#0.0");
		wavelengthAngstroms = getWavelengthAngstroms();
		return df.format(wavelengthAngstroms);
	}

	/**
	 * Set the resolution of the grating.
	 * @param d The resolution.
	 * @see #resolution
	 */
	public void setResolution(double d)
	{
		resolution = d;
	}

	/**
	 * Set the resolution of the grating.
	 * @param s The resolution as a string. This should be parsable as a valid double.
	 * @exception NumberFormatException Thrown if the string is not a valid double.
	 * @see #resolution
	 */
	public void setResolution(String s) throws NumberFormatException
	{
		resolution = Double.parseDouble(s);
	}

	/**
	 * Get the resolution of the grating.
	 * @return The resolution.
	 * @see #resolution
	 */
	public double getResolution()
	{
		return resolution;
	}

	/**
	 * Get the resolution of the grating, as a string.
	 * @return Theresolution as a string. 
	 * @see #resolution
	 */
	public String getResolutionString()
	{
		DecimalFormat df = null;

		df = new DecimalFormat("#0.0#");
		return df.format(resolution);
	}

	/**
	 * Set the angle of the grating.
	 * @param d The angle.
	 * @see #angle
	 */
	public void setAngle(double d)
	{
		angle = d;
	}

	/**
	 * Set the angle of the grating.
	 * @param s The angle as a string. This should be parsable as a valid double.
	 * @exception NumberFormatException Thrown if the string is not a valid double.
	 * @see #angle
	 */
	public void setAngle(String s) throws NumberFormatException
	{
		angle = Double.parseDouble(s);
	}

	/**
	 * Get the angle of the grating.
	 * @return The angle.
	 * @see #angle
	 */
	public double getAngle()
	{
		return angle;
	}

	/**
	 * Get the angle of the grating, as a string.
	 * @return The angle as a string. 
	 * @see #angle
	 */
	public String getAngleString()
	{
		DecimalFormat df = null;

		df = new DecimalFormat("#0.0#");
		return df.format(angle);
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
	 * @see #name
	 * @see #wavelength
	 * @see #wavelengthUnits
	 * @see #resolution
	 * @see #angle
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;
		DecimalFormat df = null;

		df = new DecimalFormat("#0.0#####");
		sb = new StringBuffer();
		sb.append(prefix+"Grating\n");
		if(name != null)
			sb.append(prefix+"\tName: "+name+"\n");
		if(wavelength != 0.0)
			sb.append(prefix+"\tWavelength: "+df.format(wavelength)+" "+wavelengthUnits+"\n");
		if(resolution != 0.0)
			sb.append(prefix+"\tResolution: "+df.format(resolution)+"\n");
		if(angle != 0.0)
			sb.append(prefix+"\tAngle: "+df.format(angle)+"\n");
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.4  2008/08/11 13:54:54  cjm
** Added micrometers as possible units from RTML 3.1a schema.
**
** Revision 1.3  2008/05/27 14:12:47  cjm
** Added serialVersionUID.
**
** Revision 1.2  2008/05/23 14:24:41  cjm
** Comment fixes.
**
** Revision 1.1  2008/03/27 17:13:18  cjm
** Initial revision
**
*/
