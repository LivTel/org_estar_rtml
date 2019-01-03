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
// RTMLConverter.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/test/RTMLConverter.java,v 1.1 2008-05-23 17:10:20 cjm Exp $
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
public class RTMLConverter
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLConverter.java,v 1.1 2008-05-23 17:10:20 cjm Exp $";
	/**
	 * Name of file to parse.
	 */
	private String filename = null;
	/**
	 * Parser to use for parsing.
	 */
	RTMLParser parser = null;
	/**
	 * Create to use for generating the converted XML.
	 */
	protected RTMLCreate create = null;
	/**
	 * The document structure returned by the parser.
	 */
	RTMLDocument document = null;
	/**
	 * Do we want the parser to use Schema validation? This only works on RTML v3.1a documents,
	 * set to use false if you <i>may</i> be parsing RTML v2.2.
	 */
	protected boolean parseSchema = false;

	/**
	 * Default constructor.
	 */
	public RTMLConverter()
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
			System.err.println("java -Dhttp.proxyHost=wwwcache.livjm.ac.uk -Dhttp.proxyPort=8080 org.estar.rtml.test.RTMLConverter <filename>");
			System.exit(2);
		}
		filename = args[0];
	}

	/**
	 * run method.
	 * @see #parser
	 * @see #create
	 * @see #parseSchema
	 * @see #document
	 */
	public void run() throws Exception
	{
		parser = new RTMLParser();
		parser.init(parseSchema);
		document = parser.parse(new File(filename));
		System.err.println("Input Document:");
		System.err.println(document);
		if(document.getVersion().equals(RTMLDocument.RTML_VERSION_22))
		{
			RTMLIntelligentAgent ia = null;
			String type = null;

			document.setVersion(RTMLDocument.RTML_VERSION_31);
			// uid convertion
			ia = document.getIntelligentAgent();
			if(ia != null)
				document.setUId(ia.getId());
			// type <-> mode convertion?
			type = document.getType();
			if(type != null)
			{
				if(type.equals("score"))
					document.setMode("inquiry"); // or is it an "offer" (score reply)!
				else if(type.equals("reject"))
					document.setMode("reject");
				else if(type.equals("request"))
					document.setMode("request");
				else if(type.equals("confirmation"))
					document.setMode("confirm");
				else if(type.equals("update"))
					document.setMode("update");
				else if(type.equals("fail"))
					document.setMode("fail");
				else if(type.equals("incomplete"))
					document.setMode("incomplete");
				else if(type.equals("observation"))
					document.setMode("complete");
				else
					System.err.println("Detected unsupported type:"+type);
			}
		}
		else if(document.getVersion().equals(RTMLDocument.RTML_VERSION_31))
		{
			RTMLIntelligentAgent ia = null;
			String mode = null;

			document.setVersion(RTMLDocument.RTML_VERSION_22);
			// uid convertion
			ia = document.getIntelligentAgent();
			if(ia != null)
				ia.setId(document.getUId());
			// type <-> mode convertion?
			mode = document.getMode();
			if(mode != null)
			{
				if(mode.equals("inquiry"))
					document.setType("score");
				else if(mode.equals("reject"))
					document.setType("reject");
				else if(mode.equals("offer"))
					document.setType("score");
				else if(mode.equals("request"))
					document.setType("request");
				else if(mode.equals("confirm"))
					document.setType("confirmation");
				else if(mode.equals("update"))
					document.setType("update");
				else if(mode.equals("fail"))
					document.setType("fail");
				else if(mode.equals("incomplete"))
					document.setType("incomplete");
				else if(mode.equals("complete"))
					document.setType("observation");
				else
					System.err.println("Detected unsupported mode:"+mode);
			}
		}
		else
		{
			throw new Exception(this.getClass().getName()+":run:Unsupported RTML version:"+
					     document.getVersion());
		}
		// uid convertion?
		// type <-> mode convertion?
		System.err.println("Output Document:");
		System.err.println(document);
		// create new RTML XML
		create = new RTMLCreate();
		create.create(document);
		create.toStream(System.out);		
	}

	/**
	 * main method of test program.
	 */
	public static void main(String args[])
	{
		RTMLConverter rtmlConverter = null;

		try
		{
			rtmlConverter = new RTMLConverter();
			rtmlConverter.parseArguments(args);
			rtmlConverter.run();
		}
		catch(Exception e)
		{
			System.err.println("RTMLConverter:main:"+e);
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.2  2007/01/30 18:31:40  cjm
** gnuify: Added GNU General Public License.
**
** Revision 1.1  2003/02/24 13:23:25  cjm
** Initial revision
**
*/
