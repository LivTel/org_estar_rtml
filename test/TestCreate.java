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
// TestCreate.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/test/TestCreate.java,v 1.29 2018-06-11 09:54:21 cjm Exp $
package org.estar.rtml.test;

import java.io.*;
import java.net.*;
import java.util.*;

import org.estar.astrometry.*;
import org.estar.cluster.*;
import org.estar.rtml.*;

/**
 * This class tests RTMLCreate.
 * Use:
 * <code>
 * java org.estar.rtml.test.TestCreate -help
 * </code>
 * to obtain information on the command line arguments.
 * @author Chris Mottram
 * @version $Revision$
 */
public class TestCreate
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id$";
	/**
	 * Create to use for creating the RTML XML from the document object model tree.
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
	 * RTML telescope data.
	 */
	protected RTMLTelescope telescope = null;
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
	 * RTML grating data.
	 */
	protected RTMLGrating grating = null;
	/**
	 * RTML half wave plate device data, used to store Moptop rotor speed.
	 */
	protected RTMLHalfWavePlate halfWavePlate = null;
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
	 * RTML schedule airmass constraint data.
	 */
	protected RTMLAirmassConstraint airmassConstraint = null;
	/**
	 * RTML schedule series constraint data.
	 */
	protected RTMLSeriesConstraint seriesConstraint = null;
	/**
	 * RTML schedule seeing constraint data.
	 */
	protected RTMLSeeingConstraint seeingConstraint = null;
	/**
	 * RTML schedule moon constraint data.
	 */
	protected RTMLMoonConstraint moonConstraint = null;
	/**
	 * RTML schedule sky constraint data.
	 */
	protected RTMLSkyConstraint skyConstraint = null;
	/**
	 * RTML schedule extinction constraint data.
	 */
	protected RTMLExtinctionConstraint extinctionConstraint = null;
	/**
	 * RTML observation image data.
	 */
	protected RTMLImageData imageData = null;
	/**
	 * RTML 2.2 Doctype system ID - This is the URL of the RTML DTD. e.g.
	 * http://www.estar.org.uk/documents/rtml2.2.dtd
	 * Leaving as null causes RTMLCreate to use the default.
	 */
	protected String doctypeSystemID = null;
	/**
	 * RTML 3.1a Schema URL string - This is the URL of the RTML Schema. e.g.:
	 * http://monet.uni-sw.gwdg.de/XMLSchema/RTML/schemas/RTML-nightly.xsd
	 * Leaving as null causes RTMLCreate to use the default.
	 */
	protected String schemaURLString = null;

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
	 * @exception RTMLException Thrown if an error occurs during parsing command line arguments.
	 * @exception Exception Thrown if an error occurs.
	 * @see #doctypeSystemID
	 * @see #document
	 * @see #detector
	 * @see #project
	 * @see #contact
	 * @see #telescope
	 * @see #device
	 * @see #target
	 * @see #observation
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
			if(args[i].equals("-abort"))
			{
				document.setType("abort");// RTML 2.2
				document.setMode("abort");// RTML 3.1a
			}
			else if(args[i].equals("-airmass_constraint"))
			{
				if((i+2) < args.length)
				{
					if(schedule != null)
					{
						airmassConstraint = new RTMLAirmassConstraint();
						schedule.setAirmassConstraint(airmassConstraint);
						airmassConstraint.setMinimum(args[i+1]);
						airmassConstraint.setMaximum(args[i+2]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
						     ":parseArguments:airmass_constraint:schedule was null.");
						System.exit(2);
					}
					i+= 2;
				}
				else
				{
					System.err.println(this.getClass().getName()+
						 ":parseArguments:seeing_constraint needs a <minimum> and <maximum>.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-binning"))
			{
				if (device != null)
				{
					if((i+2) < args.length)
					{
						if(device.getDetector() != null)
							detector = device.getDetector();
						else
						{
							detector = new RTMLDetector();
							device.setDetector(detector);
						}
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
			else if(args[i].equals("-complete"))
			{
				document.setType("observation");// this means the document has been completed.
				document.setMode("complete");// this means the document has been completed.
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
				document.setType("confirmation");// RTML 2.2
				document.setMode("confirm");// RTML 3.1a
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
			else if(args[i].equals("-dec_offset"))
			{
				double dvalue;

				if((i+1) < args.length)
				{
					if(target != null)
					{
						dvalue = Double.parseDouble(args[i+1]);
						target.setDecOffset(dvalue);
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:Dec offset:Target was null.");
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
				device = new RTMLDevice();
				// add device to observation if it exists,
				// otherwise make it the generic document device
				if(observation != null)
					observation.setDevice(device);
				else
					document.setDevice(device);
			}
			else if(args[i].equals("-device_filter"))
			{
				if((i+1) < args.length)
				{
					if(device != null)
					{
						device.setFilterType(args[i+1]);
						i+= 1;
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:device_filter not set:"+
								   "No device constructed (-device).");	
						System.exit(2);
					}
				}
				else
				{
					System.err.println(this.getClass().getName()+
		       				   ":parseArguments:device_filter needs a filter type string.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-device_name"))
			{
				if((i+1) < args.length)
				{
					if(device != null)
					{
						device.setName(args[i+1]);
						i+= 1;
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:device_name not set:"+
								   "No device constructed (-device).");	
						System.exit(2);
					}
				}
				else
				{
					System.err.println(this.getClass().getName()+
		       				   ":parseArguments:device_name needs a string.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-device_type"))
			{
				if((i+1) < args.length)
				{
					if(device != null)
					{
						device.setType(args[i+1]);
						i+= 1;
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:device_type not set:"+
								   "No device constructed (-device).");	
						System.exit(2);
					}
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:device_type needs a string:"+
							   "<camera|spectrograph|polarimeter>.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-device_spectral_region"))
			{
				if((i+1) < args.length)
				{
					if(device != null)
					{
						device.setSpectralRegion(args[i+1]);
						i+= 1;
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:device_spectral_region not set:"+
								   "No device constructed (-device).");	
						System.exit(2);
					}
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:device_spectral_region needs a string:"+
							   "<infrared|optical>.");
					System.exit(2);
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
			else if(args[i].equals("-document_score_list"))
			{
				if((i+3) < args.length)
				{
					document.addScore(args[i+1],args[i+2],args[i+3]);
					i+= 3;
				}
				else
				{
					System.err.println(this.getClass().getName()+
			     ":parseArguments:document_score_list needs a delay/probability.cumulative probability.");
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
			else if(args[i].equals("-extinction_constraint"))
			{
				if((i+1) < args.length)
				{
					if(schedule != null)
					{
						extinctionConstraint = new RTMLExtinctionConstraint();
						schedule.setExtinctionConstraint(extinctionConstraint);
						extinctionConstraint.setClouds(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
						     ":parseArguments:extinction_constraint:clouds was null.");
						System.exit(2);
					}
					i+= 1;
				}
				else
				{
					System.err.println(this.getClass().getName()+
						 ":parseArguments:extinction_constraint needs clouds <clear|light|scattered|heavy>.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-fail"))
			{
				document.setType("fail");// RTML 2.2
				document.setMode("fail");// RTML 3.1a
			}
			else if(args[i].equals("-gain"))
			{
				if (device != null)
				{
					if((i+1) < args.length)
					{
						if(device.getDetector() != null)
							detector = device.getDetector();
						else
						{
							detector = new RTMLDetector();
							device.setDetector(detector);
						}
						detector.setGain(args[i+1]);
						i+= 1;
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:Gain needs a value.");
						System.exit(3);
					}
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Gain:Device was null.");
					System.exit(4);
				}
			}
			else if(args[i].equals("-grating_name"))
			{
				if (device != null)
				{
					if((i+1) < args.length)
					{
						grating = new RTMLGrating();
						device.setGrating(grating);
						grating.setName(args[i+1]);
						i+= 1;
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:Grating needs a name.");
						System.exit(3);
					}
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Grating name:Device was null.");
					System.exit(4);
				}
			}
			else if(args[i].equals("-grating_wavelength"))
			{
				if (device != null)
				{
					if((i+2) < args.length)
					{
						grating = new RTMLGrating();
						device.setGrating(grating);
						grating.setWavelength(args[i+1]);
						grating.setWavelengthUnits(args[i+2]);
						i+= 2;
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:Grating needs wavelength and wavelength_units values.");
						System.exit(3);
					}
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Grating wavelength:Device was null.");
					System.exit(4);
				}
			}
			else if(args[i].equals("-help"))
			{
				help();
				System.exit(0);
			}
			else if(args[i].equals("-history"))
			{
				if((i+3) < args.length)
				{
					document.addHistoryEntry(args[i+1],args[i+2],args[i+3]);
					i+= 3;
				}
				else
				{
					System.err.println(this.getClass().getName()+
					   ":parseArguments:history requires <agent name> <uri> <description>.");
					System.exit(2);
				}
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
			else if(args[i].equals("-iaid"))// really the document uid.
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
			else if(args[i].equals("-iauri"))
			{
				if((i+1) < args.length)
				{
					ia.setUri(args[i+1]);
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:IA URI needs a string.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-image_data_url"))
			{
				if((i+1) < args.length)
				{
					if(observation != null)
					{
						imageData = new RTMLImageData();
						observation.addImageData(imageData);
						imageData.setImageDataURL(args[i+1]);
						imageData.setImageDataType("FITS16");
					}
					else
					{
						System.err.println(this.getClass().getName()+
						     ":parseArguments:image_data_url:observation was null.");
						System.exit(2);
					}
					i+= 1;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:image_data_url needs a URL.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-image_data_fits_header"))
			{
				if((i+1) < args.length)
				{
					if(imageData != null)
					{
						imageData.setFITSHeader(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
						     ":parseArguments:image_data_fits_header:imageData was null.");
						System.exit(2);
					}
					i+= 1;
				}
				else
				{
					System.err.println(this.getClass().getName()+
			       			   ":parseArguments:image_data_fits_header needs a FITS header.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-image_data_object_list"))
			{
				if((i+2) < args.length)
				{
					if(imageData != null)
					{
						if(args[i+1].equals("cluster"))
						{
							Cluster cluster = Cluster.load(args[i+2]);
							imageData.setObjectListType("cluster");
							imageData.setObjectListCluster(cluster.toString());
						}
						else if(args[i+1].equals("votable-url"))
						{
							imageData.setObjectListType("votable-url");
							imageData.setObjectListVOTableURL(args[i+2]);
						}
						else
						{
							System.err.println(this.getClass().getName()+
							      ":parseArguments:image_data_object_list:Unknown type "+
									   args[i+1]+".");
							System.exit(3);
						}
					}
					else
					{
						System.err.println(this.getClass().getName()+
						     ":parseArguments:image_data_object_list:imageData was null.");
						System.exit(2);
					}
					i+= 2;
				}
				else
				{
					System.err.println(this.getClass().getName()+
		 ":parseArguments:image_data_object_list <type(cluster/votable-url)> <cluster filename/votable url>.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-incomplete"))
			{
				document.setType("incomplete");// RTML 2.2
				document.setMode("incomplete");// RTML 3.1a
			}
			else if(args[i].equals("-moon_constraint"))
			{
				if((i+2) < args.length)
				{
					if(schedule != null)
					{
						moonConstraint = new RTMLMoonConstraint();
						schedule.setMoonConstraint(moonConstraint);
						moonConstraint.setDistance(args[i+1]);
						moonConstraint.setUnits(args[i+2]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
						     ":parseArguments:moon_constraint:schedule was null.");
						System.exit(2);
					}
					i+= 2;
				}
				else
				{
					System.err.println(this.getClass().getName()+
						 ":parseArguments:moon_constraint needs a <distance> and <units>.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-rotor_speed"))
			{
				if (device != null)
				{
					if((i+1) < args.length)
					{
						halfWavePlate = new RTMLHalfWavePlate();
						device.setHalfWavePlate(halfWavePlate);
						halfWavePlate.setRotorSpeed(args[i+1]);
						i+= 1;
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:Rotor speed needs a speed (slow|fast).");
						System.exit(3);
					}
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Rotor Speed:Device was null.");
					System.exit(4);
				}
			}
			else if(args[i].equals("-target_name"))
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
				observation = new RTMLObservation();
				document.addObservation(observation);
				schedule = new RTMLSchedule();
				observation.setSchedule(schedule);
				seriesConstraint = null; // reset to blank for next schedule
				imageData = null; // reset to blank for next image data
			}
			else if(args[i].equals("-priority"))
			{
				if((i+1) < args.length)
				{
					if(schedule != null)
					{
						schedule.setPriority(args[i+1]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:priority:schedule was null.");
						System.exit(2);
					}
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Schedule priority needs a number.");
					System.exit(2);
				}
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
			else if(args[i].equals("-ra_offset"))
			{
				double dvalue;

				if((i+1) < args.length)
				{
					if(target != null)
					{
						dvalue = Double.parseDouble(args[i+1]);
						target.setRAOffset(dvalue);
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:RA:Target offset was null.");
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
					document.setType("reject");// RTML 2.2
					document.setMode("reject");// RTML 3.1a
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
				document.setType("request");// RTML 2.2
				document.setMode("request");// RTML 3.1a
			}
			else if(args[i].equals("-rtml_version"))
			{
				if((i+1) < args.length)
				{
					document.setVersion(args[i+1]);
					i+= 1;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:rtml_version needs a string.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-schema_url"))
			{
				if((i+1) < args.length)
				{
					schemaURLString = args[i+1];
					i+= 1;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:schema_url needs a string.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-score"))
			{
				document.setType("score");// RTML 2.2
				document.setMode("inquiry");// RTML 3.1a
			}
			else if(args[i].equals("-score_reply"))
			{
				document.setType("score");// RTML 2.2
				document.setMode("offer");// RTML 3.1a
			}
			else if(args[i].equals("-seeing_constraint"))
			{
				if((i+2) < args.length)
				{
					if(schedule != null)
					{
						seeingConstraint = new RTMLSeeingConstraint();
						schedule.setSeeingConstraint(seeingConstraint);
						seeingConstraint.setMinimum(args[i+1]);
						seeingConstraint.setMaximum(args[i+2]);
					}
					else
					{
						System.err.println(this.getClass().getName()+
						     ":parseArguments:seeing_constraint:schedule was null.");
						System.exit(2);
					}
					i+= 2;
				}
				else
				{
					System.err.println(this.getClass().getName()+
						 ":parseArguments:seeing_constraint needs a <minimum> and <maximum>.");
					System.exit(2);
				}
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
			else if(args[i].equals("-sky_constraint"))
			{
				if((i+1) < args.length)
				{
					if(schedule != null)
					{
						skyConstraint = new RTMLSkyConstraint();
						schedule.setSkyConstraint(skyConstraint);
						// is the constraint a number, or dark|grey|bright
						if((skyConstraint.isDark(args[i+1])||skyConstraint.isGrey(args[i+1])||
						    (skyConstraint.isBright(args[i+1]))))
						{
							skyConstraint.setSky(args[i+1]);
						}
						else
						{
							skyConstraint.setValue(args[i+1]);
							skyConstraint.setUnits(RTMLSkyConstraint.UNITS_MAGS_PER_SQUARE_ARCSEC);
						}
					}
					else
					{
						System.err.println(this.getClass().getName()+
						     ":parseArguments:sky_constraint:sky was null.");
						System.exit(2);
					}
					i+= 1;
				}
				else
				{
					System.err.println(this.getClass().getName()+
						 ":parseArguments:sky_constraint needs a <dark|bright>.");
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
			else if(args[i].equals("-target"))
			{
				target = new RTMLTarget();
				target.setEquinox("2000");
				// add to observation if it exists,
				// otherwise make it the generic document target
				if(observation != null)
					observation.setTarget(target);
				else
					document.setTarget(target);
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
			else if(args[i].equals("-target_magnitude"))
			{
				double dvalue;

				if((i+3) < args.length)
				{
					if(target != null)
					{
						dvalue = Double.parseDouble(args[i+1]);
						target.setMagnitude(dvalue);
						target.setMagnitudeFilterType(args[i+2]);
						dvalue = Double.parseDouble(args[i+3]);
						target.setMagnitudeError(dvalue);
					}
					else
					{
						System.err.println(this.getClass().getName()+
								   ":parseArguments:Target Magnitude:Target was null.");
						System.exit(4);
					}
					i+= 3;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Target magnitude requires <magnitude> <filter> <error>.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-telescope"))
			{
				if((i+1) < args.length)
				{
					telescope = new RTMLTelescope();
					document.setTelescope(telescope);
					telescope.setName(args[i+1]);
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:Telescope needs a name.");
					System.exit(3);
				}
			}
			else if(args[i].equals("-telescope_aperture"))
			{
				if((i+2) < args.length)
				{
					if(telescope != null)
					{
						telescope.setAperture(args[i+1]);
						telescope.setApertureUnits(args[i+2]);
						i += 2;
					}
					else
					{
						System.err.println(this.getClass().getName()+
			       			   ":parseArguments:Telescope Aperture:telescope was null.");
						System.exit(4);
					}
				}
				else
				{
					System.err.println(this.getClass().getName()+
						    ":parseArguments:telescope_aperture needs <aperture> <units>.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-telescope_focal_length"))
			{
				if((i+2) < args.length)
				{
					if(telescope != null)
					{
						telescope.setFocalLength(args[i+1]);
						telescope.setFocalLengthUnits(args[i+2]);
						i += 2;
					}
					else
					{
						System.err.println(this.getClass().getName()+
			       			   ":parseArguments:Telescope FocalLength:telescope was null.");
						System.exit(4);
					}
				}
				else
				{
					System.err.println(this.getClass().getName()+
						    ":parseArguments:telescope_focal_length needs <focal length> <units>.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-telescope_focal_ratio"))
			{
				if((i+1) < args.length)
				{
					if(telescope != null)
					{
						telescope.setFocalRatio(args[i+1]);
						i += 1;
					}
					else
					{
						System.err.println(this.getClass().getName()+
			       			   ":parseArguments:Telescope Focal Ratio:telescope was null.");
						System.exit(4);
					}
				}
				else
				{
					System.err.println(this.getClass().getName()+
						    ":parseArguments:telescope_focal_ratio needs <f/n>.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-telescope_location"))
			{
				if((i+5) < args.length)
				{
					if(telescope != null)
					{
						RTMLTelescopeLocation location = null;

						location = new RTMLTelescopeLocation();
						location.setName(args[i+1]);
						location.setLongitude(args[i+2]);
						location.setLatitude(args[i+3]);
						location.setAltitude(args[i+4]);
						//location.setTimeZone(args[i+5]);
						telescope.setLocation(location);
						i += 5;
					}
					else
					{
						System.err.println(this.getClass().getName()+
			       			   ":parseArguments:Telescope Location:telescope was null.");
						System.exit(4);
					}
				}
				else
				{
					System.err.println(this.getClass().getName()+
						    ":parseArguments:telescope_location <name> <longitude> <latitude> <altitude> <time zone>.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-toop"))
			{
				// document.setTOOP does not work here, because the observation/target/schedule
				// are not set in the document at this point
				//document.setTOOP();
				if(target != null)
				{
					// RTML 2.2
					target.setTypeTOOP();
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:TOOP:Target was null.");
					System.exit(4);
				}
				if(schedule != null)
				{
					// RTML 3.1a - overrides schedule priority
					schedule.setPriority(0);
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:TOOP:Schedule was null.");
					System.exit(4);
				}
			}
			else if(args[i].equals("-uid"))// really the document uid.
			{
				if((i+1) < args.length)
				{
					document.setUId(args[i+1]);
					i++;
				}
				else
				{
					System.err.println(this.getClass().getName()+
							   ":parseArguments:document UID needs an ID string.");
					System.exit(2);
				}
			}
			else if(args[i].equals("-update"))
			{
				document.setType("update");// RTML 2.2
				document.setMode("update");// RTML 3.1a
			}
			else
			{
				System.err.println(this.getClass().getName()+":parseArguments:Unknown Argument"+
						   args[i]);
				System.exit(1);
			}
		}
	}

	/**
	 * run method.
	 * @see #create
	 * @see #document
	 * @see #doctypeSystemID
	 * @see #schemaURLString
	 */
	public void run() throws Exception
	{
		// document must always have a history
		if(document.getHistoryEntryCount() < 1)
			document.addHistoryEntry(this.getClass().getName(),null,"Created.");
		// set schema URL is configured
		if(schemaURLString != null)
			RTML31Create.setSchemaURLString(schemaURLString);
		create = new RTMLCreate();
		//System.out.println(document);
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
		System.err.println("\t[-doctype_system_id <url string>][-schema_url <url string>]");
		System.err.println("\t[-rtml_version <string>]");
		System.err.println("\t<-request|-score|-score_reply|-confirmation|-update|-complete|-incomplete|-reject <error string>|-abort>");
		System.err.println("\t[-uid <string>][-iahost <hostname>][-iaid <id>][-iaport <number>][-iauri <uri>][-help]");
		System.err.println("\t[-history <agent name> <agent uri> <description>]");
		System.err.println("\t[-project <proposal id>]");
		System.err.println("\t[-telescope <name> [-telescope_aperture <aperture> <m|cm|inch|ft>][-telescope_focal_ratio <f/<n>>][-telescope_focal_length <focal length> <m|cm|inch|ft>]]");
		System.err.println("\t[-telescope_location  <name> <long> <lat> <altitude> <timezone>]");
		System.err.println("\t[-contact [-contact_address <address>][-contact_email <email>]");
		System.err.println("\t\t[-contact_fax <fax>][-contact_institution <institute>][-contact_name <name>]");
		System.err.println("\t\t[-contact_telephone <telno>][-contact_url <URL>][-contact_user <user>]]");
		System.err.println("\t[-device -device_name <name> -device_type <device type>]");
		System.err.println("\t\t[-device_spectral_region <spectral region> -device_filter <filter type>]");
		System.err.println("\t\t[-binning <x> <y>][-gain <n>]");
		System.err.println("\t\t[-grating_wavelength <wavelength> <m|cm|mm|micron|nm|nanometer|nanometers|Angstrom|Angstroms>]");
		System.err.println("\t\t[-grating_name <name:low|high>");
		System.err.println("\t-observation -target_name <string>"); 
		System.err.println("\t\t[-target][-target_ident <string>] ");
		System.err.println("\t\t-ra <HH:MM:SS> -ra_offset <arcsec> -dec <[+|-]DD:MM:SS> -dec_offset <arcsec>"); 
		System.err.println("\t\t[-target_magnitude <magnitude> <filter> <error>]");
		System.err.println("\t\t[-toop][-priority <0-3>]");
		System.err.println("\t\t-exposure <length> <units> <count>");
		System.err.println("\t\t[-series_constraint_count <number>]");
		System.err.println("\t\t[-series_constraint_interval <P{(y)Y{(m)M}{(d)D}{T{(h)H}{(m}M}{(s.s..)S}>]");
		System.err.println("\t\t[-series_constraint_tolerance <P{(y)Y{(m)M}{(d)D}{T{(h)H}{(m}M}{(s.s..)S}>]");
		System.err.println("\t\t[-start_date <yyyy-MM-ddTHH:mm:ss>]");
		System.err.println("\t\t[-end_date <yyyy-MM-ddTHH:mm:ss>]");
		System.err.println("\t\t[-airmass_constraint <min> <max>]");
		System.err.println("\t\t[-moon_constraint <distance> <units(degs|rads)>]");
		System.err.println("\t\t[-seeing_constraint <min arcsec> <max arcsec>]");
		System.err.println("\t\t[-sky_constraint <dark|bright|<number (mags/arcsec^2)>>]");
		System.err.println("\t\t[-extinction_constraint <clear|light|scattered|heavy>]");
		System.err.println("\t\t[-image_data_url <url> [-image_data_fits_header <string>]");
		System.err.println("\t\t\t[-image_data_object_list <cluster|votable-url> <filename/url>]]");
		System.err.println("\t[-document_score <double>]");
		System.err.println("\t[-document_score_list <P{(y)Y{(m)M}{(d)D}{T{(h)H}{(m}M}{(s.s..)S}> <double> <double>]");
		System.err.println("\t[-completion_time <yyyy-MM-dd'T'HH:mm:ss>]");
		System.err.println("");
		System.err.println("-device and -target can be specified before -observation to add a document default,");
		System.err.println("or after an -observation to add a per observation device/target");
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
** Revision 1.28  2013/01/14 11:04:27  cjm
** Added <Detector> <Gain> paramater.
**
** Revision 1.27  2012/05/24 16:26:42  cjm
** Changed -sky_constraint parameters to allow flux in mags/arcsec^2.
**
** Revision 1.26  2011/02/09 18:43:19  cjm
** Added airmass and extinction constraint arguments.
**
** Revision 1.25  2009/08/12 17:56:06  cjm
** Changed target handling, per-observation and per-document targets can now be created.
** Observation sub-elements now linked at creation.
** Added -target,-grating_name,-target_magnitude.
** Changed -name to -target_name.
**
** Revision 1.24  2008/08/28 18:23:48  cjm
** Changed device handling: device name/spectral_region no longer has to be specified.
**
** Revision 1.23  2008/08/11 14:55:49  cjm
** Added -ra_offset and -dec_offset for setting RA/Dec offsets.
**
** Revision 1.22  2008/07/22 10:11:23  cjm
** Checking -toop option.
**
** Revision 1.21  2008/06/05 14:18:52  cjm
** Added telescope and telescope_location information.
**
** Revision 1.20  2008/05/23 17:11:17  cjm
** Modified to support new RTML3.1a support.
** History support added.
**
** Revision 1.19  2008/03/27 17:16:28  cjm
** Added -grating_wavelength to specify a wavelength for spectrograph support.
** Device filter elements are not mandatory anymore: removed 4th arg from -device and added optional
** -device_filter to add filter element to device.
**
** Revision 1.18  2007/07/09 12:53:14  cjm
** Added sky and moon constraint.
**
** Revision 1.17  2007/03/27 19:17:38  cjm
** Added -document_score_list option to addScore to a document.
**
** Revision 1.16  2007/01/30 18:31:39  cjm
** gnuify: Added GNU General Public License.
**
** Revision 1.15  2005/08/19 17:01:07  cjm
** Added VOTable URL support to ImageData.
**
** Revision 1.14  2005/06/08 14:39:15  cjm
** Added seeing_constraint support.
**
** Revision 1.13  2005/06/06 10:33:07  cjm
** Added Schedule priority.
**
** Revision 1.12  2005/06/01 16:31:30  cjm
** Added image data options.
**
** Revision 1.11  2005/05/04 18:55:28  cjm
** Removed objectListType, objectListClusterString, imageDataType, imageDataURL, fitsHeader.
** Added imageDataList, a list of RTMLImageData entries.
**
** Revision 1.10  2005/04/29 17:19:31  cjm
** Added confirmation and reject flags.
** Added document_score/completion time flag.
** Added exposure count option.
**
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
