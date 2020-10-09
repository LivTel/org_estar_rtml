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
// RTMLHistoryEntry.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLHistoryEntry.java,v 1.2 2008-05-27 14:13:44 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;
import java.util.*;

import org.estar.rtml.RTMLDateFormat;

/**
 * This class is a data container for information contained in the Entry nodes/tags of an RTML History list.
 * @author Chris Mottram
 * @version $Revision$
 */
public class RTMLHistoryEntry implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id$";
	/**
	 * Serial version ID. Fixed as these documents can be used as parameters in RMI calls across JVMs.
	 */
	static final long serialVersionUID = -7348500985710947403L;
	/**
	 * Rejection reason attribute value.
	 */
	public final static String REJECTION_REASON_NOT_AUTHORISED = "not authorized";
	/**
	 * Rejection reason attribute value.
	 */
	public final static String REJECTION_REASON_INSUFFICIENT_PRIORITY = "insufficient priority";
	/**
	 * Rejection reason attribute value.
	 */
	public final static String REJECTION_REASON_OTHER = "other";
	/**
	 * Rejection reason attribute value.
	 */
	public final static String REJECTION_REASON_NOT_AVAILABLE = "not available";
	/**
	 * Rejection reason attribute value.
	 */
	public final static String REJECTION_REASON_SYNTAX = "syntax";
	/**
	 * The time stamp of this entry.
	 */
	private Date timeStamp = null;
	/**
	 * The agent adding this entry.
	 */
	private RTMLIntelligentAgent agent = null;
	/**
	 * Description of this history entry.
	 */
	private String description = null;
	/**
	 * String error.
	 */
	private String error = null;
	/**
	 * Rejection attribute describing reason for rejection.
	 * @see #REJECTION_REASON_NOT_AUTHORISED
	 * @see #REJECTION_REASON_INSUFFICIENT_PRIORITY
	 * @see #REJECTION_REASON_OTHER
	 * @see #REJECTION_REASON_NOT_AVAILABLE
	 * @see #REJECTION_REASON_SYNTAX
	 */
	private String rejectionReason = null;
	/**
	 * Description of the rejection.
	 */
	private String rejectionDescription = null;
	/**
	 * Version number for this entry.
	 */
	private int version = 0;

	/**
	 * Default constructor.
	 */
	public RTMLHistoryEntry()
	{
		super();
	}

	/**
	 * Set the time stamp of the entry.
	 * @param s The time stamp, as a string in the format "2005-07-20T12:34:56".
	 * @see #timeStamp
	 * @see org.estar.rtml.RTMLDateFormat
	 * @see org.estar.rtml.RTMLDateFormat#parse
	 * @exception ParseException Thrown by RTMLDateFormat parse method.
	 */
	public void setTimeStamp(String s) throws ParseException
	{
		RTMLDateFormat dateFormat = null;

		dateFormat = new RTMLDateFormat();
		timeStamp = dateFormat.parse(s);
	}

	/**
	 * Set the time stamp of the entry.
	 * @param d The time stamp as a date.
	 * @see #timeStamp
	 */
	public void setTimeStamp(Date d)
	{
		timeStamp = d;
	}

	/**
	 * Get the time stamp of the entry.
	 * @return The time stamp.
	 * @see #timeStamp
	 */
	public Date getTimeStamp()
	{
		return timeStamp;
	}

	/**
	 * Set the agent of the entry.
	 * @param s The agent.
	 * @see #agent
	 */
	public void setAgent(RTMLIntelligentAgent s)
	{
		agent = s;
	}

	/**
	 * Get the agent of the entry.
	 * @return The agent.
	 * @see #agent
	 */
	public RTMLIntelligentAgent getAgent()
	{
		return agent;
	}

	/**
	 * Set the description of the entry.
	 * @param s The description.
	 * @see #description
	 */
	public void setDescription(String s)
	{
		description = s;
	}

	/**
	 * Get the description of the entry.
	 * @return The description.
	 * @see #description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Set the error string of the entry.
	 * @param s The error.
	 * @see #error
	 */
	public void setError(String s)
	{
		error = s;
	}

	/**
	 * Get the error string of the entry.
	 * @return The error.
	 * @see #error
	 */
	public String getError()
	{
		return error;
	}

	/**
	 * Set the rejection reason of the entry.
	 * @param s The rejection reason.
	 * @see #rejectionReason
	 * @see #REJECTION_REASON_NOT_AUTHORISED
	 * @see #REJECTION_REASON_INSUFFICIENT_PRIORITY
	 * @see #REJECTION_REASON_OTHER
	 * @see #REJECTION_REASON_NOT_AVAILABLE
	 * @see #REJECTION_REASON_SYNTAX
	 */
	public void setRejectionReason(String s)
	{
		rejectionReason = s;
	}

	/**
	 * Get the rejection reason of the entry.
	 * @return The rejection reason.
	 * @see #rejectionReason
	 * @see #REJECTION_REASON_NOT_AUTHORISED
	 * @see #REJECTION_REASON_INSUFFICIENT_PRIORITY
	 * @see #REJECTION_REASON_OTHER
	 * @see #REJECTION_REASON_NOT_AVAILABLE
	 * @see #REJECTION_REASON_SYNTAX
	 */
	public String getRejectionReason()
	{
		return rejectionReason;
	}

	/**
	 * Set the rejection description of the entry.
	 * @param s The rejection description.
	 * @see #rejectionDescription
	 */
	public void setRejectionDescription(String s)
	{
		rejectionDescription = s;
	}

	/**
	 * Get the rejection description of the entry.
	 * @return The rejection description.
	 * @see #rejectionDescription
	 */
	public String getRejectionDescription()
	{
		return rejectionDescription;
	}

	/**
	 * Set the version of the entry.
	 * @param s The version as a string.
	 * @see #version
	 * @exception NumberFormatException Thrown if the string is not an integer.
	 */
	public void setVersion(String s) throws NumberFormatException
	{
		version = Integer.parseInt(s);
	}

	/**
	 * Set the version of the entry.
	 * @param i The version.
	 * @see #version
	 */
	public void setVersion(int i)
	{
		version = i;
	}

	/**
	 * Get the version of the entry.
	 * @return The version.
	 * @see #version
	 */
	public int getVersion()
	{
		return version;
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
	 * @see #timeStamp
	 * @see #agent
	 * @see #description
	 * @see #error
	 * @see #rejectionReason
	 * @see #rejectionDescription
	 * @see #version
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;

		sb = new StringBuffer();
		sb.append(prefix+"Entry\n");
		if(timeStamp != null)
			sb.append(prefix+"\tTimeStamp: "+timeStamp+"\n");
		if(agent != null)
			sb.append(prefix+"\tAgent: "+agent+"\n");
		if(description != null)
			sb.append(prefix+"\tDescription: "+description+"\n");
		if(error != null)
			sb.append(prefix+"\tError: "+error+"\n");
		if(rejectionReason != null)
			sb.append(prefix+"\tRejection Reason: "+rejectionReason+"\n");
		if(rejectionDescription != null)
			sb.append(prefix+"\tRejection Description: "+rejectionDescription+"\n");
		if(version != 0)
			sb.append(prefix+"\tVersion: "+version+"\n");
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.1  2008/05/23 14:24:57  cjm
** Initial revision
**
*/
