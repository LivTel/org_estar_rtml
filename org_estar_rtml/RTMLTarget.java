// RTMLTarget.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLTarget.java,v 1.1 2003-02-24 13:19:56 cjm Exp $
package org.estar.rtml;

import org.estar.astrometry.*;

/**
 * This class is a data container for information contained in the target nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 */
public class RTMLTarget
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLTarget.java,v 1.1 2003-02-24 13:19:56 cjm Exp $";
	/**
	 * The type of the Target, the type attribute in the Target tag. Should be either "normal" or
	 * "toop".
	 */
	private String type = null;
	/**
	 * The name of the target, as specified in the #PCDATA of the TargetName sub node.
	 */
	private String name = null;
	/**
	 * The right ascension of the target.
	 */
	private RA ra = null;
	/**
	 * The declination of the target.
	 */
	private Dec dec = null;
	/**
	 * The equinox of the coordinates.
	 */
	private String equinox = null;

	/**
	 * Default constructor.
	 */
	public RTMLTarget()
	{
		super();
		type = new String("normal");
	}

	/**
	 * Set the target type.
	 * @param s The type of the target. Should be either "normal", or "toop". See DTD, %targetType;.
	 * @exception IllegalArgumentException Thrown if the type is nor legal.
	 * @see #type
	 */
	public void setType(String s) throws IllegalArgumentException
	{
		if((s.equals("normal") == false)&&(s.equals("toop") == false))
			throw new IllegalArgumentException(this.getClass().getName()+":setType:Type "+s+" not legal.");
		type = s;
	}

	/**
	 * Set the target type to a target of opportunity. The "type" string is set to "toop".
	 * @see #type
	 */
	public void setTypeTOOP()
	{
		type = new String("toop");
	}

	/**
	 * Get the target type.
	 * @return A string representing the target type, either "normal", or "toop".
	 * @see #type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * Return whether the target is normal, as opposed to a target of opportunity.
	 * @return Returns true if the target type is normal, otherwise false.
	 * @see #type
	 */
	public boolean isTypeNormal()
	{
		return type.equals("normal");
	}

	/**
	 * Return whether the target is a target of opportunity.
	 * @return Returns true if the target type is toop, otherwise false.
	 * @see #type
	 */
	public boolean isTypeTOOP()
	{
		return type.equals("toop");
	}

	/**
	 * Set the target name.
	 * @param s The name.
	 * @see #name
	 */
	public void setName(String s)
	{
		name = s;
	}

	/**
	 * Get the target name.
	 * @return The name.
	 * @see #name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Set the target right ascension.
	 * @param r The right ascension.
	 * @see #ra
	 */
	public void setRA(RA r)
	{
		ra = r;
	}

	/**
	 * Set the target right ascension.
	 * @param s A string representing the right ascension, in colon separated format.
	 * @see #ra
	 */
	public void setRA(String s)
	{
		ra = new RA();
		ra.parseColon(s);
	}

	/**
	 * Get the target right ascension.
	 * @return The name.
	 * @see #ra
	 */
	public RA getRA()
	{
		return ra;
	}

	/**
	 * Set the target declination.
	 * @param d The declination.
	 * @see #dec
	 */
	public void setDec(Dec d)
	{
		dec = d;
	}

	/**
	 * Set the target declination.
	 * @param s A string representing the declination, in colon separated format.
	 * @see #dec
	 */
	public void setDec(String s)
	{
		dec = new Dec();
		dec.parseColon(s);
	}

	/**
	 * Get the target declination.
	 * @return The declination.
	 * @see #dec
	 */
	public Dec getDec()
	{
		return dec;
	}

	/**
	 * Set the equinox.
	 * @param s The equinox.
	 * @see #equinox
	 */
	public void setEquinox(String s)
	{
		equinox = s;
	}

	/**
	 * Get the equinox.
	 * @return The equinox.
	 * @see #equinox
	 */
	public String getEquinox()
	{
		return equinox;
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
	 * @see #type
	 * @see #name
	 * @see #ra
	 * @see #dec
	 * @see #equninox
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"Target: type = "+type+"\n");
		if(name != null)
			sb.append(prefix+"\tName:"+name+"\n");
		if(ra != null)
			sb.append(prefix+"\tRA:"+ra+"\n");
		if(dec != null)
			sb.append(prefix+"\tDec:"+dec+"\n");
		if(equinox != null)
			sb.append(prefix+"\tEquinox:"+equinox);
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
*/
