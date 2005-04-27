// RTMLSeriesConstraint.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLSeriesConstraint.java,v 1.3 2005-04-27 15:41:56 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;

/**
 * This class is a data container for information contained in the SeriesConstraint nodes/tags of an RTML document.
 * There are three main elements in a SeriesConstrint:
 * <ul>
 * <li><b>count</b>
 * <li><b>interval</b>
 * <li><b>tolerance</b>
 * </ul>
 * The SeriesConstraint constrains it's associated observation in the following manner:
 * <i>Do the observation <b>count</b> times with a time period of <b>interval</b> 
 * +/- <b>tolerance</b> between them.</i>
 * @author Chris Mottram
 * @version $Revision: 1.3 $
 */
public class RTMLSeriesConstraint implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLSeriesConstraint.java,v 1.3 2005-04-27 15:41:56 cjm Exp $";
	/**
	 * The number of times the observation associated wit this SeriesConstraint should be executed.
	 */
	private int count = 0;
	/**
	 * The interval between doing each observation.
	 */
	private RTMLPeriodFormat interval = null;
	/**
	 * The slack in the interval between doing each observation.e.g.
	 * Do the observation count times with a time period of interval +/- tolerance between them. 
	 */
	private RTMLPeriodFormat tolerance = null;

	/**
	 * Default constructor.
	 */
	public RTMLSeriesConstraint()
	{
		super();
	}


	/**
	 * Set the count
	 * @param s The count, specified as a string representation of an integer.
	 * @see #count
	 */
	public void setCount(String s) throws NumberFormatException
	{
		count = Integer.parseInt(s);
	}

	/**
	 * Set the count.
	 * @param i An integer.
	 * @see #count
	 */
	public void setCount(int i)
	{
		count = i;
	}

	/**
	 * Get the count.
	 * @return The count.
	 * @see #count
	 */
	public int getCount()
	{
		return count;
	}

	/**
	 * Set the interval.
	 * @param p An interval.
	 * @see #interval
	 */
	public void setInterval(RTMLPeriodFormat p)
	{
		interval = p;
	}

	/**
	 * Set the interval
	 * @param s A string, in the format P{(yyyy)Y{(mm)M}{(dd)D}{T{(hh)H}{(mm}M}{(ss.s..)S}.
	 * @see #interval
	 * @exception RTMLException Thrown if the string is not a valid period, 
	 *            using the format specified above.
	 * @see #interval
	 */
	public void setInterval(String s) throws RTMLException
	{
		interval = new RTMLPeriodFormat();
		try
		{
		        interval.parse(s);
		}
		catch(RTMLException e)
		{
			throw new RTMLException(this.getClass().getName()+":setInterval:Illegal Period:"+s+":",e);
		}
	}

	/**
	 * Get the interval
	 * @return An interval.
	 * @see #interval
	 */
	public RTMLPeriodFormat getInterval()
	{
		return interval;
	}

	/**
	 * Set the tolerance.
	 * @param p An tolerance.
	 * @see #tolerance
	 */
	public void setTolerance(RTMLPeriodFormat p)
	{
		tolerance = p;
	}

	/**
	 * Set the tolerance.
	 * @param s A string, in the format P{(yyyy)Y{(mm)M}{(dd)D}{T{(hh)H}{(mm}M}{(ss.s..)S}.
	 * @see #tolerance
	 * @exception RTMLException Thrown if the string is not a valid period, 
	 *            using the format specified above.
	 * @see #tolerance
	 */
	public void setTolerance(String s) throws RTMLException
	{
		tolerance = new RTMLPeriodFormat();
		try
		{
		        tolerance.parse(s);
		}
		catch(RTMLException e)
		{
			throw new RTMLException(this.getClass().getName()+":setTolerance:Illegal Period:"+s+":",e);
		}
	}

	/**
	 * Get the tolerance
	 * @return An tolerance.
	 * @see #tolerance
	 */
	public RTMLPeriodFormat getTolerance()
	{
		return tolerance;
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
	 * @see #count
	 * @see #interval
	 * @see #tolerance
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"Series Constrint:\n");
		sb.append(prefix+"\tCount:"+count+"\n");
		if(interval != null)
			sb.append(prefix+"\tInterval:"+interval+" : "+interval.getMilliseconds()+" ms.\n");
		if(tolerance != null)
			sb.append(prefix+"\tTolerance:"+tolerance+" : "+tolerance.getMilliseconds()+" ms.\n");
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.2  2005/04/27 15:40:05  cjm
** Fixed toString tabbing.
**
** Revision 1.1  2005/04/27 13:46:24  cjm
** Initial revision
**
*/
