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
// RTMLIntelligentAgent.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLIntelligentAgent.java,v 1.5 2008-05-27 14:16:49 cjm Exp $
package org.estar.rtml;

import java.io.*;

/**
 * This class is a data container for information contained in the intelligent agent nodes/tags of an RTML document.
 * These are IntelligentAgent nodes in RTML2.2, and Agent nodes in RTML3.1.
 * @author Chris Mottram
 * @version $Revision$
 */
public class RTMLIntelligentAgent implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id$";
	/**
	 * Serial version ID. Fixed as these documents can be used as parameters in RMI calls across JVMs.
	 */
	static final long serialVersionUID = 3463829464314530120L;
	/**
	 * The unique identifier for the RTML request, the #PCDATA in the IntelligentAgent tag.
	 */
	private String id = null;
	/**
	 * The hostname string, the "host" attribute of the IntelligentAgent tag.
	 */
	private String hostname = null;
	/**
	 * The port number of the intelligent agent reply port, the "port" attribute of the IntelligentAgent tag.
	 */
	private int port = 0;
	/**
	 * The URI of an RTML3.1 Agent tag.
	 */
	private String uri = null;

	/**
	 * Default constructor.
	 */
	public RTMLIntelligentAgent()
	{
		super();
	}

	public void setId(String s)
	{
		id = s;
	}

	public String getId()
	{
		return id;
	}

	public void setHostname(String s)
	{
		hostname = s;
	}

	public String getHostname()
	{
		return hostname;
	}

	public void setPort(int i)
	{
		port = i;
	}

	/**
	 * Set the intelligent agent's port attribute.
	 * @param s The port, as a string representing a integer.
	 * @exception RTMLException Thrown if the string is not a valid integer.
	 * @see #port
	 */
	public void setPort(String s) throws RTMLException
	{
		try
		{
			port = Integer.parseInt(s);
		}
		catch(NumberFormatException e)
		{
			throw new RTMLException(this.getClass().getName()+":setPort:Illegal Port:"+s+":",e);
		}
	}

	public int getPort()
	{
		return port;
	}

	/**
	 * Set the Uniform Resource Identifier (RTML 3.1 only).
	 * @param s The Uri.
	 * @see #uri
	 */
	public void setUri(String s)
	{
		uri = s;
	}

	/**
	 * Get the Uniform Resource Identifier (RTML 3.1 only).
	 * @return The Uri.
	 * @see #uri
	 */
	public String getUri()
	{
		return uri;
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
	 * @see #hostname
	 * @see #port
	 * @see #id
	 * @see #uri
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"Intelligent Agent: host = "+hostname+": port = "+port+"\n");
		sb.append(prefix+"\tID:"+id);
		sb.append(prefix+"\tURI:"+uri);
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.4  2008/05/23 14:30:56  cjm
** Added URI for RTML 3.1a integration.
**
** Revision 1.3  2007/01/30 18:31:16  cjm
** gnuify: Added GNU General Public License.
**
** Revision 1.2  2005/01/19 15:30:38  cjm
** Added Serializable.
**
** Revision 1.1  2003/02/24 13:19:56  cjm
** Initial revision
**
*/
