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
// RTMLDevice.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLDevice.java,v 1.7 2008-05-27 14:09:46 cjm Exp $
package org.estar.rtml;

import java.io.*;

/**
 * This class is a data container for information contained in the device nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.7 $
 * @see org.estar.rtml.RTMLAttributes
 */
public class RTMLDevice extends RTMLAttributes implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLDevice.java,v 1.7 2008-05-27 14:09:46 cjm Exp $";
	/**
	 * Serial version ID. Fixed as these documents can be used as parameters in RMI calls across JVMs.
	 */
	static final long serialVersionUID = 1527480918462068854L;
	/**
	 * The type of device this is. See the dtd: %deviceTypes;.
	 */
	private String type = null;
	/**
	 * The wavelength region this device is good for. See the RTML 2.2 DTD: %spectralRegions;
	 * See RTML 3.1a schema SpectralRegionTypes. e.g. infrared, optical.
	 */
	private String spectralRegion = null;
	/**
	 * The name of the instrument.
	 */
	private String name = null;
	/**
	 * What type of filter the device should be using. See the dtd: %filterTypes;.
	 * This is actually part of the FilterType tag in the Filter tag.
	 */
	private String filterType = null;
	/**
	 * Detector information associated with this device.
	 */
	private RTMLDetector detector = null;
	/**
	 * Grating information associated with this device.
	 * Only normally used for spectrographs.
	 */
	private RTMLGrating grating = null;

	/**
	 * Default constructor.
	 */
	public RTMLDevice()
	{
		super();
	}

	/**
	 * Set the type of instrument. This should be one of the types defined in the RTML v 2.2 DTD,
	 * entity %deviceTypes;. 
	 * This corresponds to the <b>type</b> attribute in the RTML &lt;Device&gt; tag. e.g:
	 * camera, spectrograph, polarimeter, and a variety of others.
	 * <p>The RTML 3.1a equivalent is the DeviceTypes type, which also contains the above.
	 * @param s A valid type.
	 * @exception IllegalArgumentException Thrown if s is not valid.
	 * @see #type
	 */
	public void setType(String s) throws IllegalArgumentException
	{
		if(!(s.equals("camera")||s.equals("spectrograph")||s.equals("photometer")||s.equals("polarimeter")||
		     s.equals("spectropolarimeter")||s.equals("weatherStation")||s.equals("autoguider")||
		     s.equals("webCamera")||s.equals("skyMonitor")||s.equals("other")))
		{
			throw new IllegalArgumentException(this.getClass().getName()+":setType:Illegal type "+s);
		}
		type = s;
	}

	/**
	 * Get the type of device.
	 * This corresponds to the <b>type</b> attribute in the RTML &lt;Device&gt; tag.
	 * @return A String from the list of valid device types (RTML DTD entity %deviceTypes;).
	 * @see #type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * Set the spectral region of the instrument. This should be one of the types defined in the RTML DTD,
	 * entity %spectralRegions;.
	 * This corresponds to the <b>region</b> attribute in the RTML &lt;Device&gt; tag.
	 * @param s A valid spectral region.
	 * @exception IllegalArgumentException Thrown if s is not valid.
	 * @see #spectralRegion
	 */
	public void setSpectralRegion(String s) throws IllegalArgumentException
	{
		if(!(s.equals("radio")||s.equals("millimeter")||s.equals("infrared")||s.equals("optical")||
		     s.equals("ultraviolet")||s.equals("x-ray")||s.equals("gamma-ray")||s.equals("other")))
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setSpectralRegion:Illegal region "+s);
		}
		spectralRegion = s;
	}

	/**
	 * Get the spectral region of the device.
	 * This corresponds to the <b>region</b> attribute in the RTML &lt;Device&gt; tag.
	 * @return A String from the list of valid spectral regions (RTML DTD, entity %spectralRegions;).
	 * @see #spectralRegion
	 */
	public String getSpectralRegion()
	{
		return spectralRegion;
	}

	public void setName(String s)
	{
		name = s;
	}

	public String getName()
	{
		return name;
	}

	public void setFilterType(String s)
	{
		filterType = s;
	}

	public String getFilterType()
	{
		return filterType;
	}

	public void setDetector(RTMLDetector d)
	{
		detector = d;
	}

	public RTMLDetector getDetector()
	{
		return detector;
	}

	public void setGrating(RTMLGrating g)
	{
		grating = g;
	}

	public RTMLGrating getGrating()
	{
		return grating;
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
	 * @see #type
	 * @see #spectralRegion
	 * @see #name
	 * @see #filterType
	 * @see #detector
	 * @see #grating
	 * @see org.estar.rtml.RTMLAttributes#toString(java.lang.String)
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"Device\n");
		sb.append(super.toString(prefix+"\t"));
		if(type != null)
			sb.append(prefix+"\tType: "+type+"\n");
		if(spectralRegion != null)
			sb.append(prefix+"\tSpectralRegion: "+spectralRegion+"\n");
		if(name != null)
			sb.append(prefix+"\tName: "+name+"\n");
		if(filterType != null)
			sb.append(prefix+"\tFilter: type = "+filterType+"\n");
		if(detector != null)
			sb.append(prefix+detector.toString("\t")+"\n");
		if(grating != null)
			sb.append(prefix+grating.toString("\t")+"\n");
		return sb.toString();
	}

}
/*
** $Log: not supported by cvs2svn $
** Revision 1.6  2008/05/23 14:19:47  cjm
** Now extends RTMLAttributes.
**
** Revision 1.5  2008/03/27 17:15:02  cjm
** Added RTMLGrating sub-element for spectrograph support.
**
** Revision 1.4  2007/04/26 16:27:38  cjm
** Added more comments.
**
** Revision 1.3  2007/01/30 18:31:13  cjm
** gnuify: Added GNU General Public License.
**
** Revision 1.2  2005/01/19 11:54:09  cjm
** Added detector to device.
**
** Revision 1.1  2004/03/11 13:27:33  cjm
** Initial revision
**
*/
