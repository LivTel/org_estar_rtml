// RTMLDevice.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLDevice.java,v 1.2 2005-01-19 11:54:09 cjm Exp $
package org.estar.rtml;

import java.io.*;

/**
 * This class is a data container for information contained in the device nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.2 $
 */
public class RTMLDevice implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLDevice.java,v 1.2 2005-01-19 11:54:09 cjm Exp $";
	/**
	 * The type of device this is. See the dtd: %deviceTypes;.
	 */
	private String type = null;
	/**
	 * The wavelength region this device is good for. See the dtd: %spectralRegions;
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
	 * Default constructor.
	 */
	public RTMLDevice()
	{
		super();
	}

	/**
	 * Set the type of instrument. This should be one of the types defined in the RTML DTD,
	 * entity %deviceTypes;.
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

	public String getType()
	{
		return type;
	}

	/**
	 * Set the spectral region of the instrument. This should be one of the types defined in the RTML DTD,
	 * entity %spectralRegions;.
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
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"Device\n");
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
		return sb.toString();
	}

}
/*
** $Log: not supported by cvs2svn $
** Revision 1.1  2004/03/11 13:27:33  cjm
** Initial revision
**
*/
