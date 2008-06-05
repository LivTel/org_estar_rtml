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
// RTMLTelescopeLocation.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLTelescopeLocation.java,v 1.1 2008-06-05 14:18:03 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;

/**
 * This class is a data container for information contained in the Telescope Location nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 * @see org.estar.rtml.RTMLAttributes
 */
public class RTMLTelescopeLocation extends RTMLAttributes implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLTelescopeLocation.java,v 1.1 2008-06-05 14:18:03 cjm Exp $";
	/**
	 * Serial version ID. Fixed as these documents can be used as parameters in RMI calls across JVMs.
	 */
	static final long serialVersionUID = 6174424500302437004L;
	/**
	 * The name of the telescope.
	 */
	protected String name = null;
	/**
	 * The Longitude (E/W position) of the telescopes location. 
	 * Longitude is given degrees east of Greenwich (IAU convention).
	 * As also mandated by RTML 3.1a.
	 */
	protected double longitude = 0.0;
	/**
	 * The latitude (N/S position) in degrees of the telescopes location. 
	 */
	protected double latitude = 0.0;
	/**
	 * The altitude of the telescopes location, in meters above sea level. 
	 */
	protected double altitude = 0.0;

	/**
	 * Default constructor.
	 */
	public RTMLTelescopeLocation()
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
	 * Method to set the longitude (E/W position).
	 * @param s The longitude, in degrees east of Greenwich as a string.
	 * @exception RTMLException Thrown if the string cannot be parsed.
	 * @see #longitude
	 */
	public void setLongitude(String s) throws RTMLException
	{
		try
		{
			longitude = Double.parseDouble(s);
		}
		catch(Exception e)
		{
			throw new RTMLException(this.getClass().getName()+
						":setLongitude:longitude was not parseable:"+s,e);
		}
	}

	/**
	 * Method to set the longitude (E/W position).
	 * @param d The longitude, in degrees east of Greenwich.
	 * @see #longitude
	 */
	public void setLongitude(double d)
	{
		longitude = d;
	}

	/**
	 * Method to get the longitude (E/W position).
	 * @return The longitude, in degrees east of Greenwich.
	 * @see #longitude
	 */
	public double getLongitude()
	{
		return longitude;
	}

	/**
	 * Method to set the latitude (N/S position).
	 * @param s The latitude, in degrees.
	 * @exception RTMLException Thrown if the string cannot be parsed.
	 * @see #latitude
	 */
	public void setLatitude(String s) throws RTMLException
	{
		try
		{
			latitude = Double.parseDouble(s);
		}
		catch(Exception e)
		{
			throw new RTMLException(this.getClass().getName()+
						":setLatitude:latitude was not parseable:"+s,e);
		}
	}

	/**
	 * Method to set the latitude (N/S position).
	 * @param d The latitude, in degrees.
	 * @see #latitude
	 */
	public void setLatitude(double d)
	{
		latitude = d;
	}

	/**
	 * Method to get the latitude (N/S position).
	 * @return The latitude, in degrees.
	 * @see #latitude
	 */
	public double getLatitude()
	{
		return latitude;
	}

	/**
	 * Method to set the altitude.
	 * @param s The altitude, as a string in meters above sea level.
	 * @exception RTMLException Thrown if the string cannot be parsed.
	 * @see #altitude
	 */
	public void setAltitude(String s) throws RTMLException
	{
		try
		{
			altitude = Double.parseDouble(s);
		}
		catch(Exception e)
		{
			throw new RTMLException(this.getClass().getName()+
						":setAltitude:altitude was not parseable:"+s,e);
		}
	}

	/**
	 * Method to set the altitude.
	 * @param d The altitude, in meters above sea level.
	 * @see #altitude
	 */
	public void setAltitude(double d)
	{
		altitude = d;
	}

	/**
	 * Method to get the altitude.
	 * @return The altitude, in meters above sea level.
	 * @see #altitude
	 */
	public double getAltitude()
	{
		return altitude;
	}

	/**
	 * String representation of this Telescope Location.
	 * @see #toString(java.lang.String)
	 */
	public String toString()
	{
		return(toString(""));
	}

	/**
	 * String representation of this Telescope Location, with specified prefix.
	 * @param prefix A string to prefix to each line of data we print out.
	 * @see #name
	 * @see #longitude
	 * @see #latitude
	 * @see #altitude
	 * @see org.estar.rtml.RTMLAttributes#toString(java.lang.String)
	 */
	public String toString(String prefix)
	{
		DecimalFormat nf = null;
		StringBuffer sb = new StringBuffer();

		nf = new DecimalFormat("###0.0#");
		sb.append(prefix+"Location :\n");
		sb.append(super.toString(prefix+"\t"));
		sb.append(prefix+"\tName : "+name+"\n");
		sb.append(prefix+"\tLongitude : "+nf.format(longitude)+" degrees east of Greenwich.\n");
		sb.append(prefix+"\tLatitude : "+nf.format(latitude)+" degrees\n");
		sb.append(prefix+"\tAltitude : "+nf.format(altitude)+" metres.\n");
		return(sb.toString());
	}
}
//
// $Log: not supported by cvs2svn $
//
