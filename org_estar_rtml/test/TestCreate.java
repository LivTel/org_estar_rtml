// TestCreate.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/test/TestCreate.java,v 1.10 2005-04-29 17:19:31 cjm Exp $
package org.estar.rtml.test;

import java.io.*;
import java.net.*;
import java.util.*;

import org.estar.astrometry.*;
import org.estar.rtml.*;

/**
 * This class tests RTMLCreate.
 * Use:
 * <code>
 * java org.estar.rtml.test.TestCreate -help
 * </code>
 * to obtain information on the command line arguments.
 * @author Chris Mottram
 * @version $Revision: 1.10 $
 */
public class TestCreate
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: TestCreate.java,v 1.10 2005-04-29 17:19:31 cjm Exp $";
	/**
	 * Create to use for parsing.
	 */
	protected RTMLCreate create = null;
	/**
	 * The document structure used by the create.
	 */
	protected RTMLDocument document = null;
	/**
	 * RTML contact data.
	 */
	protected RTMLContact contact = null;
	/**
	 * RTML project data.
	 */
	protected RTMLProject project = null;
	/**
	 * RTML intelligent agent data.
	 */
	protected RTMLIntelligentAgent ia = null;
	/**
	 * RTML device data.
	 */
	protected RTMLDevice device = null;
	/**
	 * RTML detector data.
	 */
	protected RTMLDetector detector = null;
	/**
	 * RTML observation data.
	 */
	protected RTMLObservation observation = null;
	/**
	 * RTML target data.
	 */
	protected RTMLTarget target = null;
	/**
	 * RTML schedule data.
	 */
	protected RTMLSchedule schedule = null;
	/**
	 * RTML schedule series constraint data.
	 */
	protected RTMLSeriesConstraint seriesConstraint = null;
	/**
	 * Version string used to set RTML Element's version attribute.
	 * Leaving as null causes RTMLCreate to use the default.
	 */
	protected String rtmlVersionString = null;
	/**
	 * Doctype system ID - This is the URL of the RTML DTD. e.g.
	 * http://www.estar.org.uk/documents/rtml2.2.dtd
	 * Leaving as null causes RTMLCreate to use the default.
	 */
	protected String doctypeSystemID = null;

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
	 * @see #rtmlVersionString
	 * @see #doctypeSystemID
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
			if(args[i].equals("-binning"))
			{
				if (device != null)
				{
					if((i+2) < args.length)
					{
						detector = new RTMLDetector();
						device.setDetector(detector);
						detector.setRowBinning(args[i+1]);
						detector.setColumnBinning(args[i+2]);
						i+= 2;
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:Binning needs row and column values.");
						System.exit(3);
					}
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Binning:Device was null.");
					System.exit(4);
				}
			}
			else if(args[i].equals("-completion_time"))
			{
				if((i+1) < args.length)
				{
					document.setCompletionTime(args[i+1]);
					i+= 1;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:completion_time needs a date.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-confirmation"))
			{
				document.setType("confirmation");
			}
			else if(args[i].equals("-contact"))
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
					// add device to observation if it exists,
					// otherwise make it the generic document device
					if(observation != null)
						observation.setDevice(device);
					else
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
			else if(args[i].equals("-doctype_system_id"))
			{
				if((i+1) < args.length)
				{
					doctypeSystemID = args[i+1];
					i+= 1;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:doctype_system_id needs a URL string.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-document_score"))
			{
				if((i+1) < args.length)
				{
					document.setScore(args[i+1]);
					i+= 1;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:document_score needs a double.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-end_date"))
			{
				if((i+1) < args.length)
				{
					if(schedule != null)
					{
						schedule.setEndDate(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:end_date:schedule was null.");
						System.exit(2);
					}
					i+= 1;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:end_date needs a date.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-exposure"))
			{
				if((i+3) < args.length)
				{
					if(schedule != null)
					{
						schedule.setExposureLength(args[i+1]);
						schedule.setExposureUnits(args[i+2]);
						schedule.setExposureCount(args[i+3]);
						schedule.setExposureType("time");
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:exposure:schedule was null.");
						System.exit(2);
					}
					i+= 3;
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
				seriesConstraint = null; // reset to blank for next schedule
			}
			else if(args[i].equals("-project"))
			{
				if((i+1) < args.length)
				{
					project = new RTMLProject();
					document.setProject(project);
					project.setProject(args[i+1]);
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Project needs a value.");
					System.exit(3);
				}
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
			else if(args[i].equals("-reject"))
			{
				if((i+1) < args.length)
				{
					document.setType("reject");
					document.setErrorString(args[i+1]);
					i+= 1;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:reject needs an error string.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-request"))
			{
				document.setType("request");
			}
			else if(args[i].equals("-rtml_version"))
			{
				if((i+1) < args.length)
				{
					rtmlVersionString = args[i+1];
					i+= 1;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:rtml_version needs a string.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-score"))
			{
				document.setType("score");
			}
			else if(args[i].equals("-series_constraint_count"))
			{
				if((i+1) < args.length)
				{
					if(schedule != null)
					{
						if(seriesConstraint == null)
						{
							seriesConstraint = new RTMLSeriesConstraint();
							schedule.setSeriesConstraint(seriesConstraint);
						}
						seriesConstraint.setCount(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
						     ":parseArguments:series_constraint_count:schedule was null.");
						System.exit(2);
					}
					i+= 1;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:series_constraint_count needs a count.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-series_constraint_interval"))
			{
				if((i+1) < args.length)
				{
					if(schedule != null)
					{
						if(seriesConstraint == null)
						{
							seriesConstraint = new RTMLSeriesConstraint();
							schedule.setSeriesConstraint(seriesConstraint);
						}
						seriesConstraint.setInterval(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
						     ":parseArguments:series_constraint_interval:schedule was null.");
						System.exit(2);
					}
					i+= 1;
				}
				else
				{
					System.err.println(this.getClass().getName()+
						  ":parseArguments:series_constraint_interval needs an interval.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-series_constraint_tolerance"))
			{
				if((i+1) < args.length)
				{
					if(schedule != null)
					{
						if(seriesConstraint == null)
						{
							seriesConstraint = new RTMLSeriesConstraint();
							schedule.setSeriesConstraint(seriesConstraint);
						}
						seriesConstraint.setTolerance(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
						     ":parseArguments:series_constraint_tolerance:schedule was null.");
						System.exit(2);
					}
					i+= 1;
				}
				else
				{
					System.err.println(this.getClass().getName()+
						  ":parseArguments:series_constraint_tolerance needs a tolerance.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-start_date"))
			{
				if((i+1) < args.length)
				{
					if(schedule != null)
					{
						schedule.setStartDate(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:start_date:schedule was null.");
						System.exit(2);
					}
					i+= 1;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:start_date needs a date.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-target_ident"))
			{
				if((i+1) < args.length)
				{
					if(target != null)
					{
						target.setIdent(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:Target Ident:Target was null.");
						System.exit(4);
					}
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Target ident needs an ident.");
					System.exit(2);
				}
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
	 * @see #rtmlVersionString
	 * @see #doctypeSystemID
	 */
	public void run() throws Exception
	{
		create = new RTMLCreate();
		//System.out.println(document);
		if(rtmlVersionString != null)
			create.setRTMLVersionString(rtmlVersionString);
		if(doctypeSystemID != null)
			create.setDoctypeSystemID(doctypeSystemID);
		create.create(document);
		create.toStream(System.out);
	}

	/**
	 * help method.
	 */
	public void help()
	{
		System.err.println("java -Dhttp.proxyHost=wwwcache.livjm.ac.uk -Dhttp.proxyPort=8080 org.estar.rtml.test.TestCreate");
		System.err.println("\t[-doctype_system_id <url string>]");
		System.err.println("\t[-rtml_version <string>]");
		System.err.println("\t<-request|-score|-confirmation|-reject <error string>>");
		System.err.println("\t<-iahost <hostname><-iaid <id>><-iaport <number>>[-help]");
		System.err.println("\t[-project <proposal id>]");
		System.err.println("\t[-contact [-contact_address <address>][-contact_email <email>]");
		System.err.println("\t\t[-contact_fax <fax>][-contact_institution <institute>][-contact_name <name>]");
		System.err.println("\t\t[-contact_telephone <telno>][-contact_url <URL>][-contact_user <user>]]");
		System.err.println("\t[-device <name> <device type> <spectral region> <filter type>");
		System.err.println("\t\t[-binning <x> <y>]]");
		System.err.println("\t<-observation <-name <string>> [-target_ident <string>] ");
		System.err.println("\t\t<-ra <HH:MM:SS>> <-dec <[+|-]DD:MM:SS>> [-toop]");
		System.err.println("\t\t<-exposure <length> <units> <count>>");
		System.err.println("\t\t[-series_constraint_count <number>]");
		System.err.println("\t\t[-series_constraint_interval <P{(y)Y{(m)M}{(d)D}{T{(h)H}{(m}M}{(s.s..)S}>]");
		System.err.println("\t\t[-series_constraint_tolerance <P{(y)Y{(m)M}{(d)D}{T{(h)H}{(m}M}{(s.s..)S}>]");
		System.err.println("\t\t[-start_date <yyyy-MM-ddTHH:mm:ss]>");
		System.err.println("\t\t[-end_date <yyyy-MM-ddTHH:mm:ss>]>");
		System.err.println("\t[-document_score <double>]");
		System.err.println("\t[-completion_time <yyyy-MM-dd'T'HH:mm:ss>]");
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
** Revision 1.9  2005/04/27 15:45:47  cjm
** Added series constraint handling.
**
** Revision 1.8  2005/04/26 15:02:35  cjm
** Added -doctype_system_id and -rtml_version arguments.
**
** Revision 1.7  2005/04/26 11:27:16  cjm
** Added TimeConstraint -start_date and -end_date command line arguments.
**
** Revision 1.6  2005/04/25 10:31:50  cjm
** Added target ident.
**
** Revision 1.5  2005/01/19 12:07:11  cjm
** Added -project print.
** Added detector/binning.
** Allowed device to be added to either document or observation.
**
** Revision 1.4  2005/01/18 15:36:27  cjm
** Added project.
**
** Revision 1.3  2004/03/12 18:30:01  cjm
** Added contact details.
**
** Revision 1.2  2004/03/11 13:01:14  cjm
** Added device node creation.
**
** Revision 1.1  2003/02/24 13:23:25  cjm
** Initial revision
**
*/
