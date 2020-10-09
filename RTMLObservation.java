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
// RTMLObservation.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLObservation.java,v 1.10 2009-08-12 17:49:19 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This class is a data container for information contained in the observation nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision$
 * @see org.estar.rtml.RTMLDeviceHolder
 * @see org.estar.rtml.RTMLTargetHolder
 */
public class RTMLObservation implements Serializable, RTMLDeviceHolder, RTMLTargetHolder
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id$";
	/**
	 * Serial version ID. Fixed as these documents can be used as parameters in RMI calls across JVMs.
	 */
	static final long serialVersionUID = 3570970220652251564L;
	/**
	 * The device (instrument) of this observation.
	 * This can be null, in which case pick up the generic document device information.
	 */
	private RTMLDevice device = null;
	/**
	 * The target of this observation.
	 */
	private RTMLTarget target = null;
	/**
	 * The schedule of this observation.
	 */
	private RTMLSchedule schedule = null;
	/**
	 * A list of data generated by this observation.
	 * @see RTMLImageData
	 */
	private List imageDataList = null;

	/**
	 * Default constructor. Constructs an empty list for the returned image data.
	 * @see #imageDataList
	 */
	public RTMLObservation()
	{
		super();
		imageDataList = new Vector();
	}

	/**
	 * Set the device.
	 * @param d The device to set.
	 * @see #device
	 */
	public void setDevice(RTMLDevice d)
	{
		device = d;
	}

	/**
	 * Get the device.
	 * @return The device.
	 * @see #device
	 * @see RTMLDevice
	 */
	public RTMLDevice getDevice()
	{
		return device;
	}

	/**
	 * Set the observation target.
	 * @param t The target.
	 * @see #target
	 * @see RTMLTarget
	 */
	public void setTarget(RTMLTarget t)
	{
		target = t;
	}

	/**
	 * Get the observation target.
	 * @return The target.
	 * @see #target
	 * @see RTMLTarget
	 */
	public RTMLTarget getTarget()
	{
		return target;
	}

	/**
	 * Set the observation schedule.
	 * @param s The schedule.
	 * @see #schedule
	 * @see RTMLSchedule
	 */
	public void setSchedule(RTMLSchedule s)
	{
		schedule = s;
	}

	/**
	 * Get the observation schedule.
	 * @return The schedule.
	 * @see #schedule
	 * @see RTMLSchedule
	 */
	public RTMLSchedule getSchedule()
	{
		return schedule;
	}

	/**
	 * Add some image data to the list of image data.
	 * @param o The image data to add.
	 * @see #imageDataList
	 * @see RTMLImageData
	 */
	public void addImageData(RTMLImageData o)
	{
		imageDataList.add(o);
	}

	/**
	 * Clear all the image data from the list of image data.
	 * Re-initialises imageDataList to a blank vector.
	 * @see #imageDataList
	 */
	public void clearImageDataList()
	{
		imageDataList = null;
		imageDataList = new Vector();
	}

	/**
	 * Set the image data at a particular index in the list
	 * @param i The index in the list.
	 * @param o The image data to set.
	 * @see #imageDataList
	 * @see RTMLImageData
	 */
	public void setImageData(int i,RTMLImageData o) throws Exception
	{
		imageDataList.set(i,o);
	}

	/**
	 * Get the image data at an index.
	 * @param i The index.
	 * @return The image data.
	 * @exception IndexOutOfBoundsException Thrown if the index is out of range.
	 */
	public RTMLImageData getImageData(int i) throws IndexOutOfBoundsException
	{
		return (RTMLImageData)(imageDataList.get(i));
	}

	/**
	 * Get the image data list.
	 * @return A list, full of instances of RTMLImageData.
	 */
	public List getImageDataList()
	{
		return imageDataList;
	}

	/**
	 * Get the number of elements in the image data list.
	 * @return A number of items in the list.
	 */
	public int getImageDataCount()
	{
		if(imageDataList != null)
			return imageDataList.size();
		else
			return 0;
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
	 * @see #device
	 * @see #target
	 * @see #schedule
	 * @see #getImageData
	 * @see #getImageDataCount
	 */
	public String toString(String prefix)
	{
		RTMLImageData imageData = null;
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"Observation\n");
		if(target != null)
			sb.append(prefix+target.toString("\t"));
		if(device != null)
			sb.append(prefix+device.toString("\t"));
		if(schedule != null)
			sb.append(prefix+schedule.toString("\t"));
		for(int i = 0; i < getImageDataCount(); i++)
		{
			imageData = getImageData(i);
			if(imageData != null)
				sb.append(prefix+imageData.toString("\t"));
		}
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.9  2008/05/27 14:18:38  cjm
** Added serialVersionUID.
**
** Revision 1.8  2007/01/30 18:31:17  cjm
** gnuify: Added GNU General Public License.
**
** Revision 1.7  2005/06/08 11:38:51  cjm
** Fixed comments.
**
** Revision 1.6  2005/05/12 12:08:59  cjm
** Fixed clearImageDataList.
**
** Revision 1.5  2005/05/04 18:56:52  cjm
** Removed objectListType, objectListClusterString, imageDataType, imageDataURL, fitsHeader.
** Added imageDataList, a list of RTMLImageData.
**
** Revision 1.4  2005/01/19 12:05:29  cjm
** Reorder toString prints.
**
** Revision 1.3  2005/01/19 11:53:28  cjm
** Added capability of having per-observation devices (instrument configs).
**
** Revision 1.2  2004/03/18 17:32:34  cjm
** Added imageDataType.
**
** Revision 1.1  2003/02/24 13:19:56  cjm
** Initial revision
**
*/
