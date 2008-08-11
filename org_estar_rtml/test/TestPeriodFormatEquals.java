/*   
    Copyright 2006, Astrophysics Research Institute, Liverpool John Moores University.

    This file is part of org.estar.rtml.test.

    org.estar.rtml.test is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    org.estar.rtml.test is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with org.estar.rtml.test; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
// TestPeriodFormatEquals.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/test/TestPeriodFormatEquals.java,v 1.1 2008-08-11 14:55:49 cjm Exp $
package org.estar.rtml.test;

import java.io.*;
import java.util.*;

import org.estar.rtml.*;

/**
 * This class tests the parse method in TestPeriodFormatEquals.
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 */
public class TestPeriodFormatEquals
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: TestPeriodFormatEquals.java,v 1.1 2008-08-11 14:55:49 cjm Exp $";
	/**
	 * The input period to parse.
	 */
	private String inputString[] = new String[2];
	/**
	 * Parser to use for parsing.
	 */
	private RTMLPeriodFormat periodFormat[] = new RTMLPeriodFormat[2];

	/**
	 * Default constructor.
	 */
	public TestPeriodFormatEquals()
	{
		super();
	}

	/**
	 * Parse arguments.
	 * @see #inputString
	 */
	public void parseArguments(String args[])
	{
		if(args.length != 2)
		{
			System.err.println("java org.estar.rtml.test.TestPeriodFormatEquals <input period> <input period>");
			System.err.println("Period string is of the form: P{(yyyy)Y{(mm)M}{(dd)D}{T{(hh)H}{(mm}M}{(ss.s..)S}");
			System.exit(2);
		}
		inputString[0] = args[0];
		inputString[1] = args[1];
	}

	/**
	 * run method.
	 * @see #inputString
	 * @see #periodFormat
	 */
	public void run() throws Exception
	{
		for(int i = 0; i < 2; i++)
		{
			periodFormat[i] = new RTMLPeriodFormat();
			periodFormat[i].parse(inputString[i]);
			System.out.println(periodFormat[i]);
			System.out.println("Years:"+periodFormat[i].getYears());
			System.out.println("Months:"+periodFormat[i].getMonths());
			System.out.println("Days:"+periodFormat[i].getDays());
			System.out.println("Hours:"+periodFormat[i].getHours());
			System.out.println("Minutes:"+periodFormat[i].getMinutes());
			System.out.println("Seconds:"+periodFormat[i].getSeconds());
			System.out.println("Period in milliseconds:"+periodFormat[i].getMilliseconds());
		}
		if(periodFormat[0].equals(periodFormat[1]))
			System.out.println("Periods are equal.");
		else
			System.out.println("Periods are NOT equal.");
	}

	/**
	 * main method of test program.
	 */
	public static void main(String args[])
	{
		TestPeriodFormatEquals testParser = null;

		try
		{
			testParser = new TestPeriodFormatEquals();
			testParser.parseArguments(args);
			testParser.run();
		}
		catch(Exception e)
		{
			System.err.println("TestPeriodFormatEquals:main:"+e);
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.3  2007/01/30 18:31:41  cjm
** gnuify: Added GNU General Public License.
**
** Revision 1.2  2005/04/27 13:45:42  cjm
** Initial working version.
**
** Revision 1.1  2005/04/27 11:06:42  cjm
** Initial revision
**
** Revision 1.1  2003/02/24 13:23:25  cjm
** Initial revision
**
*/
