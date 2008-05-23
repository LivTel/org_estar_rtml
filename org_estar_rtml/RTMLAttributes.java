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
// RTMLAttributes.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLAttributes.java,v 1.1 2008-05-23 14:10:41 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * This class is a data container for information contained RTMLAttrubutes attribute group.
 * Most RTML 3.1a element tags have these attributes.
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 */
public class RTMLAttributes implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLAttributes.java,v 1.1 2008-05-23 14:10:41 cjm Exp $";
	/**
	 * The ID (local key) of this element.
	 */
	protected String id = null;
	/**
	 * The ref (reference to local ID) of this element.
	 */
	protected String ref = null;
	/**
	 * The uref (reference to non-local ID) of this element.
	 */
	protected String uref = null;

	/**
	 * Default constructor.
	 */
	public RTMLAttributes()
	{
		super();
	}

	/**
	 * Set the Id of this element (RTML 3.1a).
	 * @param s The id.
	 * @see #id
	 */
	public void setId(String s)
	{
		id = s;
	}

	/**
	 * Get the Id of this element (RTML 3.1a).
	 * @return The id.
	 * @see #id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * Set the ref of this element (RTML 3.1a).
	 * @param s The ref.
	 * @see #ref
	 */
	public void setRef(String s)
	{
		ref = s;
	}

	/**
	 * Get the Ref of this element (RTML 3.1a).
	 * @return The ref.
	 * @see #ref
	 */
	public String getRef()
	{
		return ref;
	}

	/**
	 * Set the uref of this element (RTML 3.1a).
	 * @param s The uref.
	 * @see #uref
	 */
	public void setURef(String s)
	{
		uref = s;
	}

	/**
	 * Get the uref of this element (RTML 3.1a).
	 * @return The uref.
	 * @see #uref
	 */
	public String getURef()
	{
		return uref;
	}

	/**
	 * Method to print out a string representation of the attributes.
	 * @see #toString(java.lang.String)
	 */
	public String toString()
	{
		return toString("");
	}

	/**
	 * Method to print out a string representation of this node, with a prefix.
	 * @param prefix A string to prefix to each line of data we print out.
	 * @see #id
	 * @see #ref
	 * @see #uref
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"Attributes:\n");
		sb.append(prefix+"\tId:"+id+"\n");
		sb.append(prefix+"\tRef:"+ref+"\n");
		sb.append(prefix+"\tURef:"+uref+"\n");
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
*/
