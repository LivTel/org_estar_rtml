// RTMLProject.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLProject.java,v 1.2 2005-01-19 15:30:38 cjm Exp $
package org.estar.rtml;

import java.io.*;

/**
 * This class is a data container for information contained in the Project nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.2 $
 */
public class RTMLProject implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLProject.java,v 1.2 2005-01-19 15:30:38 cjm Exp $";
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
// Revision 1.1  2005/01/18 15:08:28  cjm
// Initial revision
//
//
