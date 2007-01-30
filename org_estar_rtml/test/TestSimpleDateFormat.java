// TestSimpleDateFormat.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/test/TestSimpleDateFormat.java,v 1.2 2007-01-30 14:50:26 cjm Exp $
package org.estar.rtml.test;

import java.io.*;
import java.util.*;
import java.text.*;

import org.estar.rtml.*;

/**
 * This class tests the parse and format methods in SimpleDateFormat.
 * @author Chris Mottram
 * @version $Revision: 1.2 $
 */
public class TestSimpleDateFormat
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: TestSimpleDateFormat.java,v 1.2 2007-01-30 14:50:26 cjm Exp $";
	/**
	 * Default SimpleDateFormat pattern.
	 */
	public final static String DEFAULT_SIMPLE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	/**
	 * The input to parse.
	 */
	private String inputString = null;
	/**
	 * Should input parsing be lenient? Set to FALSE.
	 */
	private boolean inputLenient = false;
	/**
	 * The input pattern string to be passed into the input SimpleDatFormat.
	 * @see #inputSimpleDateFormat
	 */
	private String inputPatternString = null;
	/**
	 * The output pattern string to be passed into the output SimpleDatFormat.
	 * @see #outputSimpleDateFormat
	 */
	private String outputPatternString = null;
	/**
	 * Parser to use for date parsing.
	 */
	private SimpleDateFormat inputSimpleDateFormat = null;
	/**
	 * Formatter to use for output date formatting.
	 */
	private SimpleDateFormat outputSimpleDateFormat = null;
	/**
	 * Parser/Formatter based on the current RTML parser standard.
	 */
	private RTMLDateFormat rtmlDateFormat = null;

	/**
	 * Default constructor.
	 * @see #inputPatternString
	 * @see #outputPatternString
	 * @see #DEFAULT_SIMPLE_DATE_FORMAT
	 */
	public TestSimpleDateFormat()
	{
		super();
		inputPatternString = null;
		outputPatternString = DEFAULT_SIMPLE_DATE_FORMAT;
	}

	/**
	 * Parse arguments.
	 * @see #inputPatternString
	 * @see #outputPatternString
	 * @see #inputLenient
	 * @see #inputString
	 */
	public void parseArguments(String args[])
	{
		if(args.length < 1)
		{
			System.err.println("java org.estar.rtml.test.TestSimpleDateFormat -ip <input pattern> -il -op <output pattern> -i <input date>");
			System.err.println("Default input and output pattern:"+DEFAULT_SIMPLE_DATE_FORMAT);
			System.err.println("-il sets the input date formatter lenient.");
			System.err.println("If you don't specify '-ip' the RTMLDateFormat parser is used.");
			System.exit(2);
		}
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equals("-ip"))
			{
				if((i+1) < args.length)
				{
					inputPatternString = args[i+1];
					i++;
				}
				else
				{
					System.err.println("No argument for -ip.");
					System.exit(3);
				}
			}
			else if(args[i].equals("-il"))
			{
				inputLenient = true;
			}
			else if(args[i].equals("-op"))
			{
				if((i+1) < args.length)
				{
					outputPatternString = args[i+1];
					i++;
				}
				else
				{
					System.err.println("No argument for -op.");
					System.exit(3);
				}
			}
			else if(args[i].equals("-i"))
			{
				if((i+1) < args.length)
				{
					inputString = args[i+1];
					i++;
				}
				else
				{
					System.err.println("No argument for -i.");
					System.exit(3);
				}
			}
			else
			{
				System.err.println("Unknown argument:"+args[i]+".");
				System.exit(3);
			}
		
		}
	}

	/**
	 * run method.
	 * @see #inputPatternString
	 * @see #outputPatternString
	 * @see #inputSimpleDateFormat
	 * @see #outputSimpleDateFormat
	 * @see #inputString
	 */
	public void run() throws Exception
	{
		Date date = null;
		SimpleDateFormat gmtSimpleDateFormat;
		TimeZone timeZone = null;

		// sort out simple date formats
		if(inputPatternString != null)
		{
			inputSimpleDateFormat = new SimpleDateFormat(inputPatternString);
			if(inputLenient)
			{
				System.out.println("Setting Input date format lenient.");
				inputSimpleDateFormat.setLenient(true);
			}
		}
		outputSimpleDateFormat = new SimpleDateFormat(outputPatternString);
		// rtml date formatter
		rtmlDateFormat = new RTMLDateFormat();

		// sort out GMT formatter
		timeZone = TimeZone.getTimeZone("UTC");
		gmtSimpleDateFormat = new SimpleDateFormat(DEFAULT_SIMPLE_DATE_FORMAT+" z");
		gmtSimpleDateFormat.setTimeZone(timeZone);

		System.out.println("Input date format:\t\t"+inputPatternString);
		System.out.println("Output date format:\t\t"+outputPatternString);
		System.out.println("Input date:\t\t\t"+inputString);
		if(inputSimpleDateFormat != null)
		{
			System.out.println("Parsing input date using Input date format...");
			date = inputSimpleDateFormat.parse(inputString);
		}
		else
		{
			System.out.println("Parsing input date using RTML date format...");
			date = rtmlDateFormat.parse(inputString);
		}
		System.out.println("Date (internal formatter):\t"+date);
		System.out.println("Date (milliseconds since 1970):\t"+date.getTime());
		System.out.println("Date using Output date format:\t"+outputSimpleDateFormat.format(date));
		System.out.println("Date using RTML date format:\t"+rtmlDateFormat.format(date));
		System.out.println("Date using GMT date format:\t"+gmtSimpleDateFormat.format(date));
	}

	/**
	 * main method of test program.
	 */
	public static void main(String args[])
	{
		TestSimpleDateFormat test = null;

		try
		{
			test = new TestSimpleDateFormat();
			test.parseArguments(args);
			test.run();
		}
		catch(Exception e)
		{
			System.err.println("TestSimpleDateFormat:main:"+e);
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.1  2006/02/28 17:47:20  cjm
** Initial revision
**
*/
