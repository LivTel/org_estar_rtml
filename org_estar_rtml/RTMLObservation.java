// RTMLObservation.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLObservation.java,v 1.2 2004-03-18 17:32:34 cjm Exp $
package org.estar.rtml;

import java.net.*;

/**
 * This class is a data container for information contained in the observation nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.2 $
 */
public class RTMLObservation
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLObservation.java,v 1.2 2004-03-18 17:32:34 cjm Exp $";
	/**
	 * The target of this observation.
	 */
	private RTMLTarget target = null;
	/**
	 * The schedule of this observation.
	 */
	private RTMLSchedule schedule = null;
	/**
	 * The type of the object list belonging to this observation. Either "xml" or "cluster".
	 */
	private String objectListType = null;
	/**
	 * A string, containing the contents of a cluster format object list file.
	 */
	private String objectListClusterString = null;
	/**
	 * The ImageData type string containing the format of the image data.
	 * Should be a valid %imageFormats; ENTITY, see DTD.
	 */
	private String imageDataType = null;
	/**
	 * The URL containing the location of the image data.
	 */
	private URL imageDataURL = null;
	/**
	 * A string, containing the FITS header from the image data/observation.
	 */
	private String fitsHeader = null;

	/**
	 * Default constructor.
	 */
	public RTMLObservation()
	{
		super();
	}

	public void setTarget(RTMLTarget t)
	{
		target = t;
	}

	public RTMLTarget getTarget()
	{
		return target;
	}

	public void setSchedule(RTMLSchedule s)
	{
		schedule = s;
	}

	public RTMLSchedule getSchedule()
	{
		return schedule;
	}

	/**
	 * Set the object list type.
	 * @param s The type of the object list. Should be either "cluster", or "xml". See DTD, %objectListTypes;.
	 * @exception IllegalArgumentException Thrown if the type is nor legal.
	 * @see #objectListType
	 */
	public void setObjectListType(String s)
	{
		if((s.equals("xml") == false)&&(s.equals("cluster") == false))
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setObjectListType:Object List Type "+
							   s+" not legal.");
		}
		objectListType = s;
	}

	public String getObjectListType()
	{
		return objectListType;
	}

	/**
	 * Return whether the object list type is "cluster".
	 * @return True if the object list type is cluster, false if it is not, or has not been set yet.
	 */
	public boolean isObjectListTypeCluster()
	{
		if(objectListType == null)
			return false;
		return objectListType.equals("cluster");
	}

	public void setObjectListCluster(String s)
	{
		objectListClusterString = s;
	}

	public String getObjectListCluster()
	{
		return objectListClusterString;
	}


	/**
	 * Routine to set the image data Type.
	 * @param s The string containing a valid type. See %imageFormats; ENTITY in DTD.
	 * @exception RTMLException Thrown if the image data type string is not a valid type.
	 * @see #imageDataType
	 */
	public void setImageDataType(String s) throws RTMLException
	{
		if(!(s.equals("FITS")||s.equals("FITS16")||s.equals("gif")||s.equals("jpg")||s.equals("JPEG")))
			throw new RTMLException(this.getClass().getName()+":setImageDataType:Illegal Type:"+s);
		imageDataType = s;
	}

	/**
	 * Get the image data type.
	 * @see #imageDataType
	 */
	public String getImageDataType()
	{
		return imageDataType;
	}

	/**
	 * Routine to set the image data URL.
	 * @param s The string to create a URL from.
	 * @exception RTMLException Thrown if the image data string is not a valid URL.
	 */
	public void setImageDataURL(String s) throws RTMLException
	{
		try
		{
			imageDataURL = new URL(s);
		}
		catch(MalformedURLException e)
		{
			throw new RTMLException(this.getClass().getName()+"setImageDataURL:Malformed URL:"+s,e);
		}
	}

	public URL getImageDataURL()
	{
		return imageDataURL;
	}

	public void setFITSHeader(String s)
	{
		fitsHeader = s;
	}

	public String getFITSHeader()
	{
		return fitsHeader;
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
	 * @see #target
	 * @see #schedule
	 * @see #objectListClusterString
	 * @see #objectListType
	 * @see #imageDataType
	 * @see #imageDataURL
	 * @see #fitsHeader
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"Observation\n");
		if(target != null)
			sb.append(prefix+target.toString("\t")+"\n");
		if(schedule != null)
			sb.append(prefix+schedule.toString("\t")+"\n");
		if(isObjectListTypeCluster()&&(objectListClusterString != null))
		{
			sb.append(prefix+"\tObjectList: type = cluster\n");
			sb.append(prefix+"\t"+objectListClusterString+"\n");
		}
		if(imageDataType != null)
			sb.append(prefix+"\tImageDataType: "+imageDataType+"\n");
		if(imageDataURL != null)
			sb.append(prefix+"\tImageData: "+imageDataURL+"\n");
		if(fitsHeader != null)
			sb.append(prefix+"\tFITSHeader: "+fitsHeader+"\n");
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.1  2003/02/24 13:19:56  cjm
** Initial revision
**
*/
