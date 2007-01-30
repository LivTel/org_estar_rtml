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
// RTMLProject.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLProject.java,v 1.3 2007-01-30 18:31:20 cjm Exp $
package org.estar.rtml;

import java.io.*;

/**
 * This class is a data container for information contained in the Project nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.3 $
 */
public class RTMLProject implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLProject.java,v 1.3 2007-01-30 18:31:20 cjm Exp $";
	/**
	 * The project text node, currently used to store the proposal ID for LT/FTN/FTS.
	 * In RTML3.0, this will change to ProjectID.
	 */
	private String project = null;

	/**
	 * Default constructor.
	 */
	public RTMLProject()
	{
		super();
	}

	/**
	 * Method to set the Project.
	 * @param p The project - currently the LT/FTN/FTS proposal ID.
	 */
	public void setProject(String p)
	{
		project = p;
	}

	/**
	 * Method to get the current project.
	 * @return The project.
	 * @see #project
	 */
	public String getProject()
	{
		return project;
	}

	/**
	 * String representation of this Project.
	 * @see #toString(java.lang.String)
	 */
	public String toString()
	{
		return(toString(""));
	}

	/**
	 * String representation of this Project, with specified prefix.
	 * @param prefix A string to prefix to each line of data we print out.
	 * @see #getProject
	 */
	public String toString( String prefix )
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append( prefix+"Project :\n" );
		sb.append( prefix+"\t       Project : "+project+"\n" );
		return( sb.toString() );
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 1.2  2005/01/19 15:30:38  cjm
// Added Serializable.
//
// Revision 1.1  2005/01/18 15:08:28  cjm
// Initial revision
//
//
