// RTMLDeviceHolder.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLDeviceHolder.java,v 1.1 2005-01-19 11:55:42 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * This interface is implemented by classes holding device (instrument) data.
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 */
public interface RTMLDeviceHolder
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLDeviceHolder.java,v 1.1 2005-01-19 11:55:42 cjm Exp $";
	/**
	 * Set the device.
	 * @param device The device to set.
	 */
	public void setDevice(RTMLDevice device);
	/**
	 * Get the device.
	 * @return The device.
	 */
	public RTMLDevice getDevice();
}
//
// $Log: not supported by cvs2svn $
//
