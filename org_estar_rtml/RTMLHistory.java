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
// RTMLHistory.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLHistory.java,v 1.1 2008-05-23 14:25:08 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * This class is a data container for information contained in the History nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 */
public class RTMLHistory implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLHistory.java,v 1.1 2008-05-23 14:25:08 cjm Exp $";
	/**
	 * List containing History entries.
	 */
	public List entryList = null;

	/**
	 * Default constructor. Initialise entryList.
	 * @see #entryList
	 */
	public RTMLHistory()
	{
		super();
		entryList = new Vector();
	}

	/**
	 * Add an entry to the document.
	 * @param e The entry to add.
	 * @see #entryList
	 */
	public void addEntry(RTMLHistoryEntry e)
	{
		entryList.add(e);
	}

	/**
	 * Clear the entries from the document.
	 * @see #entryList
	 */
	public void clearEntryList()
	{
		entryList = null;
	}

	/**
	 * Get an entry from the History Node.
	 * @param index The index in the entry list.
	 * @return The entry to the specicifed index is returned.
	 * @see #entryList
	 */
	public RTMLHistoryEntry getEntry(int index)
	{
		return (RTMLHistoryEntry)(entryList.get(index));
	}

	/**
	 * Get the number of entries in the list.
	 * @return The number of entries in the list.
	 * @see #entryList
	 */
	public int getEntryListCount()
	{
		return entryList.size();
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
	 * @see #getEntryListCount
	 * @see #getEntry
	 */
	public String toString(String prefix)
	{
		RTMLHistoryEntry entry = null;
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"History:\n");
		for(int i = 0; i < getEntryListCount();i++)
		{
			entry = getEntry(i);
			if(entry != null)
				sb.append(prefix+entry.toString("\t")+"\n");
		}
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
*/
