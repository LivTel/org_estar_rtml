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
// TestRTML31Parser.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/test/TestRTML31Parser.java,v 1.1 2008-05-13 16:02:19 cjm Exp $
package org.estar.rtml.test;

import java.io.*;
import java.util.*;

import org.estar.astrometry.*;
import org.estar.rtml.*;

/**
 * This class tests RTML31Parser.
 * @author Chris Mottram
 * @version $Revision$
 */
public class TestRTML31Parser
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id$";
	/**
	 * Name of file to parse.
	 */
	private String filename = null;
	/**
	 * Parser to use for parsing.
	 */
	RTML31Parser parser = null;
	/**
	 * The document structure returned by the parser.
	 */
	RTMLDocument document = null;

	/**
	 * Default constructor.
	 */
	public TestRTML31Parser()
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
			System.err.println("java -Dhttp.proxyHost=wwwcache.livjm.ac.uk -Dhttp.proxyPort=8080 org.estar.rtml.test.TestRTML31Parser <filename>");
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
		parser = new RTML31Parser();
		document = parser.parse(new File(filename));
		System.out.println(document);
	}

	/**
	 * main method of test program.
	 */
	public static void main(String args[])
	{
		TestRTML31Parser testParser = null;

		try
		{
			testParser = new TestRTML31Parser();
			testParser.parseArguments(args);
			testParser.run();
		}
		catch(Exception e)
		{
			System.err.println("TestRTML31Parser:main:"+e);
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}
}
/*
** $Log: not supported by cvs2svn $
*/
