// RTMLSeeingConstraint.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLSeeingConstraint.java,v 1.1 2005-06-08 11:39:35 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;

/**
 * This class is a data container for information contained in the SeeingConstraint nodes/tags of an RTML document.
 * There are two main attributes in a SeeingConstraint:
 * <ul>
 * <li><b>minimum</b> The minimum (best) required seeing in arcseconds.
 * <li><b>maximum</b> The maximum (worst) required seeing in arcseconds.
 * </ul>
 * The SeeingConstraint constrains it's associated observation in the following manner:
 * <i>Do the observation if the seeing in arcseconds is worse (greater than) the minimum and 
 * better (less than) the maximum.</i>
 * Note the RTML2.2 DTD also supports "nominal" seeing and an "ExposureFactor" element to adjust the exposure length
 * by the (nominal-actual seeing) * ExposureFactor in some manner. This is not implemented, as it does not exist
 * in RTML 3. RTML 3 has the minimum and maximum attributes as sub-elements, and a fixed Units element.
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 */
public class RTMLSeeingConstraint implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLSeeingConstraint.java,v 1.1 2005-06-08 11:39:35 cjm Exp $";
	/**
	 * The minimum (best) required seeing in arcseconds.
	 */
	private double minimum = 0.0;
	/**
	 * The maximum (worst) required seeing in arcseconds.
	 */
	private double maximum = 10.0;

	/**
	 * Default constructor.
	 */
	public RTMLSeeingConstraint()
	{
		super();
	}

	/**
	 * Set the minimum (best) required seeing in arcseconds.
	 * @param s The minimum seeing, specified as a string representation of a double.
	 * @exception NumberFormatException Thrown if the string parsing fails.
	 * @see #minimum
	 */
	public void setMinimum(String s) throws NumberFormatException
	{
		minimum = Double.parseDouble(s);
	}

	/**
	 * Set the minimum (best) required seeing in arcseconds.
	 * @param d The minimum seeing, specified as a double.
	 * @see #minimum
	 */
	public void setMinimum(double d)
	{
		minimum = d;
	}

	/**
	 * Get the minimum (best) required seeing in arcseconds..
	 * @return The minimum seeing in arc-seconds.
	 * @see #minimum
	 */
	public double getMinimum()
	{
		return minimum;
	}

	/**
	 * Set the maximum (best) required seeing in arcseconds.
	 * @param s The maximum seeing, specified as a string representation of a double.
	 * @exception NumberFormatException Thrown if the string parsing fails.
	 * @see #maximum
	 */
	public void setMaximum(String s) throws NumberFormatException
	{
		maximum = Double.parseDouble(s);
	}

	/**
	 * Set the maximum (best) required seeing in arcseconds.
	 * @param d The maximum seeing, specified as a double.
	 * @see #maximum
	 */
	public void setMaximum(double d)
	{
		maximum = d;
	}

	/**
	 * Get the maximum (best) required seeing in arcseconds.
	 * @return The maximum seeing in arc-seconds.
	 * @see #maximum
	 */
	public double getMaximum()
	{
		return maximum;
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
	 * @see #minimum
	 * @see #maximum
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"Seeing Constraint:\n");
		sb.append(prefix+"\tMinimum:"+minimum+" arcseconds\n");
		sb.append(prefix+"\tMaximum:"+maximum+" arcseconds\n");
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
*/
