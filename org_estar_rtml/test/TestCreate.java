// TestCreate.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/test/TestCreate.java,v 1.3 2004-03-12 18:30:01 cjm Exp $
package org.estar.rtml.test;

import java.io.*;
import java.net.*;
import java.util.*;

import org.estar.astrometry.*;
import org.estar.rtml.*;

/**
 * This class tests RTMLCreate.
 * @author Chris Mottram
 * @version $Revision: 1.3 $
 */
public class TestCreate
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: TestCreate.java,v 1.3 2004-03-12 18:30:01 cjm Exp $";
	/**
	 * Create to use for parsing.
	 */
	RTMLCreate create = null;
	/**
	 * The document structure used by the create.
	 */
	RTMLDocument document = null;
	/**
	 * RTML contact data.
	 */
	RTMLContact contact = null;
	/**
	 * RTML intelligent agent data.
	 */
	RTMLIntelligentAgent ia = null;
	/**
	 * RTML device data.
	 */
	RTMLDevice device = null;
	/**
	 * RTML observation data.
	 */
	RTMLObservation observation = null;
	RTMLTarget target = null;
	RTMLSchedule schedule = null;

	/**
	 * Default constructor. Initialise document.
	 * @see #document
	 * @see #ia
	 * @see #observation
	 */
	public TestCreate()
	{
		super();
		document = new RTMLDocument();
		ia = new RTMLIntelligentAgent();
		observation = null;
		document.setIntelligentAgent(ia);
	}

	/**
	 * Parse arguments.
	 * @param args An array of arguments to parse.
	 * @exception RTMLException Thrown if an error occurs during parsing.
	 */
	public void parseArguments(String args[]) throws RTMLException,Exception
	{
		if(args.length < 1)
		{
			help();
			System.exit(0);
		}
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equals("-contact"))
			{
				contact = new RTMLContact();
				document.setContact(contact);
			}
			else if(args[i].equals("-contact_address"))
			{
				if((i+1) < args.length)
				{
					if(contact != null)
					{
						contact.setAddress(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
	       						   ":parseArguments:Contact Address:Contact was null.");
						System.exit(4);
					}
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Contact Address needs a value.");
					System.exit(3);
				}
			}
			else if(args[i].equals("-contact_email"))
			{
				if((i+1) < args.length)
				{
					if(contact != null)
					{
						contact.setEmail(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
	       						   ":parseArguments:Contact Email:Contact was null.");
						System.exit(4);
					}
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Contact Email needs a value.");
					System.exit(3);
				}
			}
			else if(args[i].equals("-contact_fax"))
			{
				if((i+1) < args.length)
				{
					if(contact != null)
					{
						contact.setFax(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
	       						   ":parseArguments:Contact Fax:Contact was null.");
						System.exit(4);
					}
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Contact Fax needs a value.");
					System.exit(3);
				}
			}
			else if(args[i].equals("-contact_institution"))
			{
				if((i+1) < args.length)
				{
					if(contact != null)
					{
						contact.setInstitution(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
	       						   ":parseArguments:Contact Institution:Contact was null.");
						System.exit(4);
					}
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Contact Institution needs a value.");
					System.exit(3);
				}
			}
			else if(args[i].equals("-contact_name"))
			{
				if((i+1) < args.length)
				{
					if(contact != null)
					{
						contact.setName(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
	       						   ":parseArguments:Contact Name:Contact was null.");
						System.exit(4);
					}
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Contact Name needs a value.");
					System.exit(3);
				}
			}
			else if(args[i].equals("-contact_telephone"))
			{
				if((i+1) < args.length)
				{
					if(contact != null)
					{
						contact.setTelephone(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
	       						   ":parseArguments:Contact Telephone:Contact was null.");
						System.exit(4);
					}
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Contact Telephone needs a value.");
					System.exit(3);
				}
			}
			else if(args[i].equals("-contact_url"))
			{
				if((i+1) < args.length)
				{
					if(contact != null)
					{
						contact.setUrl(new URL(args[i+1]));
					}
					else
					{
						System.err.println(this.getClass().getName()+
	       						   ":parseArguments:Contact Url:Contact was null.");
						System.exit(4);
					}
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Contact Url needs a value.");
					System.exit(3);
				}
			}
			else if(args[i].equals("-contact_user"))
			{
				if((i+1) < args.length)
				{
					if(contact != null)
					{
						contact.setUser(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
	       						   ":parseArguments:Contact User:Contact was null.");
						System.exit(4);
					}
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Contact User needs a value.");
					System.exit(3);
				}
			}
			else if(args[i].equals("-dec"))
			{
				if((i+1) < args.length)
				{
					if(target != null)
					{
						target.setDec(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:Dec:Target was null.");
						System.exit(4);
					}
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Dec needs value.");
					System.exit(3);
				}
			}
			else if(args[i].equals("-device"))
			{
				if((i+4) < args.length)
				{
					device = new RTMLDevice();
					device.setName(args[i+1]);
					device.setType(args[i+2]);
					device.setSpectralRegion(args[i+3]);
					device.setFilterType(args[i+4]);
					document.setDevice(device);
					i+= 4;
				}
				else
				{
					System.err.println(this.getClass().getName()+
					":parseArguments:device needs name,type,spectral region and filter type.");
					System.exit(5);
				}
			}
			else if(args[i].equals("-exposure"))
			{
				if((i+2) < args.length)
				{
					if(schedule != null)
					{
						schedule.setExposureLength(args[i+1]);
						schedule.setExposureUnits(args[i+2]);
						schedule.setExposureType("time");
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:exposure:schedule was null.");
						System.exit(2);
					}
					i+= 2;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:exposure needs a length and units.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-help"))
			{
				help();
				System.exit(0);
			}
			else if(args[i].equals("-iahost"))
			{
				if((i+1) < args.length)
				{
					ia.setHostname(args[i+1]);
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:IA hostname needs a hostname.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-iaid"))
			{
				if((i+1) < args.length)
				{
					ia.setId(args[i+1]);
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:IA ID needs an ID string.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-iaport"))
			{
				if((i+1) < args.length)
				{
					ia.setPort(args[i+1]);
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:IA port needs a port number.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-name"))
			{
				if((i+1) < args.length)
				{
					if(target != null)
					{
						target.setName(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:Name:Target was null.");
						System.exit(4);
					}
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Target name needs a name.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-observation"))
			{
				if(observation != null)
				{
					observation.setTarget(target);
					observation.setSchedule(schedule);
					document.addObservation(observation);
				}
				observation = new RTMLObservation();
				target = new RTMLTarget();
				target.setEquinox("J2000");
				schedule = new RTMLSchedule();
			}
			else if(args[i].equals("-ra"))
			{
				if((i+1) < args.length)
				{
					if(target != null)
					{
						target.setRA(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:RA:Target was null.");
						System.exit(4);
					}
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:RA needs value.");
					System.exit(3);
				}
			}
			else if(args[i].equals("-request"))
			{
				document.setType("request");
			}
			else if(args[i].equals("-score"))
			{
				document.setType("score");
			}
			else if(args[i].equals("-toop"))
			{
				if(target != null)
				{
					target.setTypeTOOP();
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:TOOP:Target was null.");
					System.exit(4);
				}
			}
			else
			{
				System.err.println(this.getClass().getName()+":parseArguments:Unknown Argument"+
						   args[i]);
				System.exit(1);
			}
		}
		// add any observation not already added yet.
		if(observation != null)
		{
			observation.setTarget(target);
			observation.setSchedule(schedule);
			document.addObservation(observation);
		}

	}

	/**
	 * run method.
	 * @see #create
	 * @see #document
	 */
	public void run() throws Exception
	{
		create = new RTMLCreate();
		//System.out.println(document);
		create.create(document);
		create.toStream(System.out);
	}

	/**
	 * help method.
	 */
	public void help()
	{
		System.err.println("java -Dhttp.proxyHost=wwwcache.livjm.ac.uk -Dhttp.proxyPort=8080 org.estar.rtml.test.TestCreate");
		System.err.println("\t<-request|-score><-iahost <hostname><-iaid <id>><-iaport <number>>[-help]");
		System.err.println("\t[-contact [-contact_address <address>][-contact_email <email>]");
		System.err.println("\t\t[-contact_fax <fax>][-contact_institution <institute>][-contact_name <name>]");
		System.err.println("\t\t[-contact_telephone <telno>][-contact_url <URL>][-contact_user <user>]]");
		System.err.println("\t[-device <name> <device type> <spectral region> <filter type>]");
		System.err.println("\t<-observation <-name <string>> <-ra <HH:MM:SS>> <-dec <[+|-]DD:MM:SS>> [-toop]");
		System.err.println("\t\t<-exposure <length> <units>>>");
	}

	/**
	 * main method of test program.
	 */
	public static void main(String args[])
	{
		TestCreate testCreate = null;

		try
		{
			testCreate = new TestCreate();
			testCreate.parseArguments(args);
			testCreate.run();
		}
		catch(Exception e)
		{
			System.err.println("TestCreate:main:"+e);
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.2  2004/03/11 13:01:14  cjm
** Added device node creation.
**
** Revision 1.1  2003/02/24 13:23:25  cjm
** Initial revision
**
*/
