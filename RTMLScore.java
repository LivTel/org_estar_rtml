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
// RTMLScore.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLScore.java,v 1.2 2008-05-27 14:58:25 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * This class is a data container for information contained in the Score tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision$
 */
public class RTMLScore implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id$";
	/**
	 * Serial version ID. Fixed as these documents can be used as parameters in RMI calls across JVMs.
	 */
	static final long serialVersionUID = 6600424686842633408L;
	/**
	 * The delay attribute specified in the score, can be null.
	 * @see RTMLPeriodFormat
	 */
	protected RTMLPeriodFormat delay = null;
	/**
	 * The probability attribute specified in the score, only used if delay is non-null.
	 * Can be NaN.
	 * @see #delay
	 */
	protected double probability = 0.0;
	/**
	 * The cumulative probability attribute specified in the score, only used if delay is non-null.
	 * Can be NaN.
	 * @see #delay
	 */
	protected double cumulative = 0.0;
	/**
	 * Score 'value', used as tag CDATA if delay _is_ null.
	 * @see #delay
	 */
	protected double score = 0.0;

	/**
	 * Default constructor. 
	 */
	public RTMLScore()
	{
		super();
	}

	/**
	 * Set the score to be a simple number. The delay is set to null by this method.
	 * @param s The score, a double normally between 0 and 1.
	 * @see #score
	 * @see #delay
	 */
	public void setScore(double s)
	{
		score = s;
		delay = null;
	}

	/**
	 * Get the score parameter. Only really use if isSimple returns true.
	 * @return The score, a double normally between 0 and 1.
	 * @see #score
	 */
	public double getScore()
	{
		return score;
	}

	/**
	 * Get whether the RTML Score is "simple" (a single double CDATA between 0 and 1),
	 * or not (contains delay, probability and cumulative attributes and no CDATA).
	 * @return Returns a boolean, true if the score is "simple".
	 */
	public boolean isSimple()
	{
		return (delay == null);
	}

	/**
	 * Set the delay. If null, this score will be "simple".
	 * @param pf The delay, an instance of RTMLPeriodFormat. This can be null, 
	 *      if we wish this score to be "simple".
	 * @see #delay
	 * @see #isSimple
	 * @see RTMLPeriodFormat
	 */
	public void setDelay(RTMLPeriodFormat pf)
	{
		delay = pf;
	}

	/**
	 * Get the delay.
	 * @return The delay. This can be null if the Score is "simple"
	 * @see #delay
	 * @see #isSimple
	 */
	public RTMLPeriodFormat getDelay()
	{
		return delay;
	}

	/**
	 * Set the probability. This will be used only if delay is _not_ null, 
	 * @param p The probability, a number between 0 and 1 or NaN.
	 * @see #probability
	 * @see #isSimple
	 * @see java.lang.Double#NaN
	 */
	public void setProbability(double p)
	{
		probability = p;
	}

	/**
	 * Get the probability. This will be used only if delay is _not_ null, 
	 * @return The probability, a number between 0 and 1 or NaN.
	 * @see #probability
	 * @see #isSimple
	 * @see java.lang.Double#NaN
	 */
	public double getProbability()
	{
		return probability;
	}

	/**
	 * Set the cumulative probability. This will be used only if delay is _not_ null, 
	 * @param p The cumulative probability, a number between 0 and 1 or NaN.
	 * @see #cumulative
	 * @see #isSimple
	 * @see java.lang.Double#NaN
	 */
	public void setCumulative(double p)
	{
		cumulative = p;
	}

	/**
	 * Get the cumulative probability. This will be used only if delay is _not_ null, 
	 * @return The cumulative probability, a number between 0 and 1 or NaN.
	 * @see #probability
	 * @see #isSimple
	 * @see java.lang.Double#NaN
	 */
	public double getCumulative()
	{
		return cumulative;
	}

	/**
	 * Method to print out a string representation of this node.
	 * @return A string representation of this object.
	 * @see #toString(java.lang.String)
	 */
	public String toString()
	{
		return toString("");
	}

	/**
	 * Method to print out a string representation of this node, with a prefix.
	 * @param prefix A string to prefix to each line of data we print out.
	 * @return A string representation of this object.
	 * @see #score
	 * @see #delay
	 * @see #probability
	 * @see #cumulative
	 * @see #isSimple
	 */
	public String toString(String prefix)
	{
		DecimalFormat df = null;
		StringBuffer sb = null;
		
		df = new DecimalFormat("0.0#");
		sb = new StringBuffer();
		sb.append(prefix+"Score:");
		if(isSimple())
			sb.append(" "+df.format(score)+"\n");
		else
		{
			sb.append(" Delay: "+delay.toString());
			if(Double.isNaN(probability))
				sb.append(" Probability: NaN");
			else
				sb.append(" Probability: "+df.format(probability));
			if(Double.isNaN(cumulative))
				sb.append(" Cumulative: NaN");
			else
				sb.append(" Cumulative: "+df.format(cumulative));
			sb.append("\n");
		}
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.1  2007/03/27 19:15:01  cjm
** Initial revision
**
*/
