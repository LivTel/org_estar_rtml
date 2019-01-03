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
// ExtractScore.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/test/ExtractScore.java,v 1.1 2019-01-02 11:36:37 cjm Exp $
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
public class ExtractScore
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: ExtractScore.java,v 1.1 2019-01-02 11:36:37 cjm Exp $";
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
	 * Do we want the parser to use Schema validation? This only works on RTML v3.1a documents,
	 * set to use false if you <i>may</i> be parsing RTML v2.2.
	 */
	protected boolean parseSchema = false;
	/**
	 * Get the best score from the document.
	 */
	protected double bestScore = 0.0;

	/**
	 * Default constructor.
	 */
	public ExtractScore()
	{
		super();
	}

	/**
	 * Parse arguments.
	 * @see #filename
	 * @see #parseSchema
	 */
	public void parseArguments(String args[])
	{
		if(args.length == 0)
		{
			help();
			System.exit(2);
		}
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equals("-parse_schema"))
				parseSchema = true;
			else if(args[i].equals("-filename")||args[i].equals("-f"))
			{
				if(filename != null)
				{
					System.out.println(this.getClass().getName()+
							   ":parseArguments: filename already specified:"+filename);
					help();
					System.exit(2);
				}
				if((i+1) < args.length)
				{
					System.out.println(this.getClass().getName()+
							   ":parseArguments: setting filename to:"+args[i+1]);
					filename = args[i+1];
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:No filename specified.");
					System.exit(3);
				}
			}
			else if(args[i].equals("-help"))
			{
				help();
				System.exit(2);
			}
			else if(args[i].equals("-schema_source"))
			{
				if((i+1) < args.length)
				{
					System.out.println(this.getClass().getName()+
							   ":parseArguments: setting schema source to:"+args[i+1]);
					RTMLParser.setSchemaSource(args[i+1]);
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:No schema source specified.");
					System.exit(3);
				}
			}
			else
			{
				System.err.println(this.getClass().getName()+
						   ":parseArguments:Unknown argument "+args[i]+".");
				System.exit(3);
			}
		}
	}

	/**
	 * run method.
	 * @see #parser
	 * @see #parseSchema
	 * @see #document
	 * @see #filename
	 * @see org.estar.rtml.RTMLParser#init(boolean)
	 * @see org.estar.rtml.RTMLParser#parse
	 */
	public void run() throws Exception
	{
		if(filename == null)
		{
			System.err.println("No filename specified to parse.");
			help();
			return;
		}
		parser = new RTMLParser();
		parser.init(parseSchema);
		document = parser.parse(new File(filename));
	}

	/**
	 * Extract the best score from the document.
	 * @see #document
	 * @see #bestScore
	 */
	protected void getBestScore()
	{
		bestScore = 0.0;
		// check simple score
		if(document.getScore() != null)
			bestScore = document.getScore().doubleValue();
		// check scores list
		for(int i = 0; i < document.getScoresListCount(); i++)
		{
			RTMLScore score  = null;
			
			score = document.getScore(i);
			if(score.isSimple())
			{
				if(score.getScore() > bestScore)
					bestScore = score.getScore();
			}
			else
			{
				if(score.getProbability() > bestScore)
					bestScore = score.getProbability();
			}
		}
	}

	/**
	 * Print help message.
	 */
	public void help()
	{
			System.err.println("java -Dhttp.proxyHost=wwwcache.livjm.ac.uk -Dhttp.proxyPort=8080 org.estar.rtml.test.ExtractScore [-parse_schema] [-help] [-schema_source <filename>] -f[ilename] <filename>");
	}

	/**
	 * main method of test program.
	 * @see #parseArguments
	 * @see #run
	 * @see #getBestScore
	 */
	public static void main(String args[])
	{
		ExtractScore extractScore = null;

		extractScore = new ExtractScore();
		extractScore.bestScore = 0.0;
		try
		{
			extractScore.parseArguments(args);
			extractScore.run();
			extractScore.getBestScore();
		}
		catch(Exception e)
		{
			System.err.println("ExtractScore:main:"+e);
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("The best score is:"+extractScore.bestScore);
		// return '0' OK if document scored positive in some way
		// return '1'/ failure if the document is not a score document, had no score, or has a score of zero
		if(extractScore.bestScore > 0.0)
			System.exit(0);
		else
			System.exit(1);
	}
}

/*
** $Log: not supported by cvs2svn $
** Revision 1.1  2010/06/07 16:32:05  cjm
** Initial revision
**
*/
