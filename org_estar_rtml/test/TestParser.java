// TestParser.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/test/TestParser.java,v 1.1 2003-02-24 13:23:25 cjm Exp $
package org.estar.rtml.test;

import java.io.*;
import java.util.*;

import org.estar.astrometry.*;
import org.estar.rtml.*;

/**
 * This class tests RTMLParser.
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 */
public class TestParser
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: TestParser.java,v 1.1 2003-02-24 13:23:25 cjm Exp $";
	/**
	 * Name of file to parse.
	 */
	private String filename = null;
	/**
	 * Parser to use for parsing.
	 */
	RTMLParser parser = null;
	/**
	 * The document structure returned by the parser.
	 */
	RTMLDocument document = null;

	/**
	 * Default constructor.
	 */
	public TestParser()
	{
		super();
	}

	/**
	 * Parse arguments.
	 * @see #filename
	 */
	public void parseArguments(String args[])
	{
		if(args.length != 1)
		{
			System.err.println("java -Dhttp.proxyHost=wwwcache.livjm.ac.uk -Dhttp.proxyPort=8080 org.estar.rtml.test.TestParser <filename>");
			System.exit(2);
		}
		filename = args[0];
	}

	/**
	 * run method.
	 * @see #parser
	 * @see #document
	 */
	public void run() throws Exception
	{
		parser = new RTMLParser();
		document = parser.parse(new File(filename));
		System.out.println(document);
	}

	/**
	 * main method of test program.
	 */
	public static void main(String args[])
	{
		TestParser testParser = null;

		try
		{
			testParser = new TestParser();
			testParser.parseArguments(args);
			testParser.run();
		}
		catch(Exception e)
		{
			System.err.println("TestParser:main:"+e);
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}
}
/*
** $Log: not supported by cvs2svn $
*/
