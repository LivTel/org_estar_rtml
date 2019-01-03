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
// RTMLTarget.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLTarget.java,v 1.11 2019-01-02 11:34:48 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;
import java.util.*;
//import java.util.Comparator;
//import java.util.SortedSet;
//import java.util.TreeSet;
import org.estar.astrometry.*;

/**
 * This class is a data container for information contained in the target nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.11 $
 * @see org.estar.rtml.RTMLAttributes
 */
public class RTMLTarget extends RTMLAttributes implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLTarget.java,v 1.11 2019-01-02 11:34:48 cjm Exp $";
	/**
	 * Serial version ID. Fixed as these documents can be used as parameters in RMI calls across JVMs.
	 */
	static final long serialVersionUID = -4226657892010129299L;
	/**
	 * The type of the Target, the type attribute in the Target tag. Should be either "normal" or
	 * "toop".
	 */
	private String type = null;
	/**
	 * The ident of the target, as specified in Target's ident attribute.
	 */
	private String ident = null;
	/**
	 * The name of the target, as specified in the #PCDATA of the TargetName sub node.
	 */
	private String name = null;
	/**
	 * The right ascension of the target. This will be null if the target is an ephemeris target.
	 */
	private RA ra = null;
	/**
	 * The declination of the target. This will be null if the target is an ephemeris target.
	 */
	private Dec dec = null;
	/**
	 * The equinox of the coordinates.
	 */
	private String equinox = null;
	/**
	 * If the target is an ephemeris target rather than an extra solar target, this set will contain a 
	 * sorted (in time) list of RTMLEphemerisTargetTrackNode s.
	 * @see org.estar.rtml.RTMLEphemerisTargetTrackNode
	 */
	private SortedSet ephemerisTargetTrack = null;
	/**
	 * An offset to be applied to the Right Ascension.
	 */
	private double raOffset = 0.0;
	/**
	 * An offset to be applied to the Declination.
	 */
	private double decOffset = 0.0;
	/**
	 * The magnitude of the target.
	 */
	private double magnitude = 0.0;
	/**
	 * The filter type the magnitude of the target is quoted in (i.e. Mag 10 in R).
	 */
	private String magnitudeFilterType = null;
	/**
	 * The error of the magnitude.
	 */
	private double magnitudeError = 0.0;

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
	 * Set the target ident. This can be null, if no ident was specified.
	 * @param s The ident.
	 * @see #ident
	 */
	public void setIdent(String s)
	{
		ident = s;
	}

	/**
	 * Get the target ident. This can be null, if no ident was specified.
	 * @return The ident string.
	 * @see #ident
	 */
	public String getIdent()
	{
		return ident;
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
	 * @exception IllegalArgumentException Thrown if there is a problem with the parsing.
	 */
	public void setRA(String s) throws IllegalArgumentException
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
	 * Set the target declination. Now allows declinations with no sign character, assumes positive.
	 * @param s A string representing the declination, in colon separated format.
	 * @see #dec
	 * @see org.estar.astrometry.Dec#parseColon(java.lang.String,boolean)
	 * @exception IllegalArgumentException Thrown if there is a problem with the parsing.
	 */
	public void setDec(String s) throws IllegalArgumentException
	{
		dec = new Dec();
		dec.parseColon(s,false);
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
	 * Set the ephemeris target track to the specified set of RTMLEphemerisTargetTrackNode s.
	 * @param track A SortedSet containing elements of RTMLEphemerisTargetTrackNode, sorted in timestamp order.
	 * @see #ephemerisTargetTrack
	 * @see org.estar.rtml.RTMLEphemerisTargetTrackNode
	 */
	public void setEphemerisTrack(SortedSet track)
	{
		ephemerisTargetTrack = track;
	}

	/**
	 * Get the ephemeris target track.
	 * @return The ephemeris target track, A SortedSet containing elements of RTMLEphemerisTargetTrackNode, 
	 *         sorted in timestamp order.
	 * @see #ephemerisTargetTrack
	 * @see org.estar.rtml.RTMLEphemerisTargetTrackNode
	 */
	public SortedSet getEphemerisTrack()
	{
		return ephemerisTargetTrack;
	}

	/**
	 * Add the specified ephemeris target track node to the list of ephemeris target track nodes.
	 * @param node An instance of RTMLEphemerisTargetTrackNode to add to the list.
	 * @see #ephemerisTargetTrack
	 * @see org.estar.rtml.RTMLEphemerisTargetTrackNode
	 */
	public void addEphemerisTrackNode(RTMLEphemerisTargetTrackNode node)
	{
		if(ephemerisTargetTrack == null)
			ephemerisTargetTrack = new TreeSet(new NodeComparator());
		if(ephemerisTargetTrack.contains(node) == false)
			ephemerisTargetTrack.add(node);
	}

	/**
	 * Remove the list of ephemeris target track nodes.
	 * ephemerisTargetTrack is set to null, which effectively makes this target an extra solar target
	 * until an ephemeris target track node is added.
	 * @see #ephemerisTargetTrack
	 * @see org.estar.rtml.RTMLEphemerisTargetTrackNode
	 */
	public void clearEphemerisTrack()
	{
		ephemerisTargetTrack.clear();
		ephemerisTargetTrack = null;
	}

	/**
	 * Remove the specified ephemeris target track node from the list of ephemeris target track nodes.
	 * @param node An instance of RTMLEphemerisTargetTrackNode to remove from the list.
	 * @see #ephemerisTargetTrack
	 * @see org.estar.rtml.RTMLEphemerisTargetTrackNode
	 */
	public void removeEphemerisTrackNode(RTMLEphemerisTargetTrackNode node)
	{
		if(ephemerisTargetTrack != null)
		{
			if(ephemerisTargetTrack.contains(node))
				ephemerisTargetTrack.remove(node);
		}
	}

	/**
	 * Set an offset from right ascension.
	 * @param d The offset, in decimal arcseconds.
	 * @see #raOffset
	 */
	public void setRAOffset(double d)
	{
		raOffset = d;
	}

	/**
	 * Return an offset to be applied from the target's position.
	 * @return The offset in RA, in arcseconds. 
	 * @see #raOffset
	 */
	public double getRAOffset()
	{
		return raOffset;
	}

	/**
	 * Set an offset from declination.
	 * @param d The offset, in decimal arcseconds.
	 * @see #decOffset
	 */
	public void setDecOffset(double d)
	{
		decOffset = d;
	}

	/**
	 * Return an declination offset to be applied from the target's position.
	 * @return The offset in declination, in arcseconds. 
	 * @see #decOffset
	 */
	public double getDecOffset()
	{
		return decOffset;
	}

	/**
	 * Set target magnitude.
	 * @param d The magnitude.
	 * @see #magnitude
	 */
	public void setMagnitude(double d)
	{
		magnitude = d;
	}

	/**
	 * Return an target magnitude.
	 * @return The magnitude.
	 * @see #magnitude
	 */
	public double getMagnitude()
	{
		return magnitude;
	}

	/**
	 * Set the magnitude filter type.
	 * @param s The magnitude filter type. See schema for valid values e.g.:
	 *          U|B|V|R|I etc.
	 * @see #magnitudeFilterType
	 * @exception IllegalArgumentException Thrown if the filter type is illegal.
	 */
	public void setMagnitudeFilterType(String s) throws IllegalArgumentException
	{
		if((s != null)&&(s.equals("none")||s.equals("clear")||s.equals("neutral_density")||
				 s.equals("blue")||s.equals("green")||s.equals("red")||s.equals("U")||
				 s.equals("B")||s.equals("V")||s.equals("R")||s.equals("I")||s.equals("H")||
				 s.equals("J")||s.equals("K")||s.equals("L")||s.equals("M")||s.equals("N")||
				 s.equals("Johnson_U")||s.equals("Johnson_B")||s.equals("Johnson_V")||
				 s.equals("Johnson_R")||s.equals("Johnson_I")||s.equals("Johnson_J")||
				 s.equals("Johnson_H")||s.equals("Johnson_K")||s.equals("Johnson_L")||
				 s.equals("Johnson_M")||s.equals("Johnson_N")||
				 s.equals("Bessel_U")||s.equals("Bessel_B")||s.equals("Bessel_V")||
				 s.equals("Bessel_R")||s.equals("Bessel_I")||
				 s.equals("Cousins_R")||s.equals("Cousins_I")||
				 s.equals("Sloan_u")||s.equals("Sloan_g")||s.equals("Sloan_r")||
				 s.equals("Sloan_i")||s.equals("Sloan_z")||
				 s.equals("Stroemgren_u")||s.equals("Stroemgren_b")||s.equals("Stroemgren_v")||
				 s.equals("Stroemgren_beta")||s.equals("Stroemgren_y")||
				 s.equals("Gunn_g")||s.equals("Gunn_r")||s.equals("Gunn_i")||s.equals("Gunn_z")||
				 s.equals("narrowband")||s.equals("Halpha")||s.equals("Hbeta")||
				 s.equals("forbidden_OI")||s.equals("forbidden_OII")||s.equals("forbidden_OIII")||
				 s.equals("forbidden_NII")||s.equals("forbidden_SII")||s.equals("other")))
		{
			magnitudeFilterType = s;
		}
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setMagnitudeFilterType:Illegal filter type:"+s);
		}
	}

	/**
	 * Get the magnitude filter type.
	 * @return The magnitude filter type.
	 * @see #magnitudeFilterType
	 */
	public String getMagnitudeFilterType()
	{
		return magnitudeFilterType;
	}

	/**
	 * Set target magnitude error.
	 * @param d The magnitude error.
	 * @see #magnitudeError
	 */
	public void setMagnitudeError(double d)
	{
		magnitudeError = d;
	}

	/**
	 * Return the target magnitude error.
	 * @return The magnitude error.
	 * @see #magnitudeError
	 */
	public double getMagnitudeError()
	{
		return magnitudeError;
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
	 * @see #ident
	 * @see #ra
	 * @see #dec
	 * @see #equinox
	 * @see #ephemerisTargetTrack
	 * @see #raOffset
	 * @see #decOffset
	 * @see #magnitude
	 * @see #magnitudeFilterType
	 * @see #magnitudeError
	 * @see org.estar.rtml.RTMLAttributes#toString(java.lang.String)
	 * @see org.estar.rtml.RTMLEphemerisTargetTrackNode
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;
		DecimalFormat df = null;

		df = new DecimalFormat("#0.0#");
		sb = new StringBuffer();
		sb.append(prefix+"Target: type = "+type+"\n");
		sb.append(super.toString(prefix+"\t"));
		if(name != null)
			sb.append(prefix+"\tName:"+name+"\n");
		if(ident != null)
			sb.append(prefix+"\tIdent:"+ident+"\n");
		if(ra != null)
			sb.append(prefix+"\tRA:"+ra+"\n");
		if(equinox != null)
			sb.append(prefix+"\tEquinox:"+equinox+"\n");
		if(ephemerisTargetTrack != null)
		{
			sb.append(prefix+"\tEphemeris Target Track:\n");
			List<RTMLEphemerisTargetTrackNode> list = new ArrayList<RTMLEphemerisTargetTrackNode>(ephemerisTargetTrack);
			for (RTMLEphemerisTargetTrackNode node : list) 
			{
				//RTMLEphemerisTargetTrackNode node  = (RTMLEphemerisTargetTrackNode)o;
				sb.append(prefix+"\t\t"+node);
			}		
		}
		sb.append(prefix+"\tRA offset:"+df.format(raOffset)+"\n");
		if(dec != null)
			sb.append(prefix+"\tDec:"+dec+"\n");
		sb.append(prefix+"\tDec offset:"+df.format(decOffset)+"\n");
		if(magnitudeFilterType != null)
		{
			sb.append(prefix+"\tMagtitude:"+df.format(magnitude)+" in "+magnitudeFilterType+
				  " (+/- "+df.format(magnitudeError)+")\n");
		}
		return sb.toString();
	}

	/** 
	 * Internal Comparator class for testing order of track anchor points (track nodes).
	 * @see org.estar.rtml.RTMLEphemerisTargetTrackNode
	 */
	public static class NodeComparator implements Comparator, Serializable 
	{
		/** 
		 * Method to compare two objects in the set. These should both be instances of 
		 * RTMLEphemerisTargetTrackNode. Any list using this comparator are ordered in timestamp order.
		 * @param o1 An object, should be an instance of RTMLEphemerisTargetTrackNode.
		 * @param o2 An object, should be an instance of RTMLEphemerisTargetTrackNode.
		 * @return Returns an integer, -1 if o1's timestamp is before o2, 1 if o1's timestamp is after o2,
		 *         0 if they have the same timestamp.
		 * @exception ClassCastException Thrown if o1 and o2 are not both instances of 
		 *            RTMLEphemerisTargetTrackNode
		 * @see org.estar.rtml.RTMLEphemerisTargetTrackNode
		 * @see org.estar.rtml.RTMLEphemerisTargetTrackNode#timestamp
		 * @see org.estar.rtml.RTMLEphemerisTargetTrackNode#getTimestamp
		 */
		public int compare (Object o1, Object o2) 
		{
			long time1,time2;
			if (! (o1 instanceof RTMLEphemerisTargetTrackNode) ||
			    ! (o2 instanceof RTMLEphemerisTargetTrackNode))
			{
				throw new ClassCastException("Compare:: Not both EphemerisTrackNodes: ("+
						(o1 == null ? "NULL": o1.getClass().getName())+","+
						(o2 == null ? "NULL": o2.getClass().getName())+")");
			}
			RTMLEphemerisTargetTrackNode node1 = (RTMLEphemerisTargetTrackNode)o1;
			RTMLEphemerisTargetTrackNode node2 = (RTMLEphemerisTargetTrackNode)o2;
			time1 = node1.getTimestamp().getTime();
			time2 = node2.getTimestamp().getTime();
			if (time1 < time2)
				return -1;
			else if(time1 > time2)
				return 1;
			return 0;	
		}
	} // comparator
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.10  2009/08/12 17:48:46  cjm
** Added target magnitude data.
**
** Revision 1.9  2008/08/11 13:54:54  cjm
** Added RA and Dec offsets.
**
** Revision 1.8  2008/05/27 15:01:06  cjm
** Added serialVersionUID.
**
** Revision 1.7  2008/05/23 17:09:42  cjm
** Now extends RTMLAttributes.
**
** Revision 1.6  2007/01/30 18:31:23  cjm
** gnuify: Added GNU General Public License.
**
** Revision 1.5  2005/06/08 11:38:41  cjm
** Fixed comments.
**
** Revision 1.4  2005/05/12 10:07:02  cjm
** Changed setDec to allow strings without a sign char, it assumes positive.
**
** Revision 1.3  2005/04/25 10:32:00  cjm
** Added ident attribute.
**
** Revision 1.2  2005/01/19 15:30:38  cjm
** Added Serializable.
**
** Revision 1.1  2003/02/24 13:19:56  cjm
** Initial revision
**
*/
