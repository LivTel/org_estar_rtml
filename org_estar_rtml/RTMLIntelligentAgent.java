// RTMLIntelligentAgent.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLIntelligentAgent.java,v 1.2 2005-01-19 15:30:38 cjm Exp $
package org.estar.rtml;

import java.io.*;

/**
 * This class is a data container for information contained in the intelligent agent nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.2 $
 */
public class RTMLIntelligentAgent implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLIntelligentAgent.java,v 1.2 2005-01-19 15:30:38 cjm Exp $";
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
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"Intelligent Agent: host = "+hostname+": port = "+port+"\n");
		sb.append(prefix+"\tID:"+id);
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.1  2003/02/24 13:19:56  cjm
** Initial revision
**
*/
