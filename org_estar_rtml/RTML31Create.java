/*   
    Copyright 2006, Astrophysics Research Institute, Liverpool John Moores University.

    This file is part of org.estar.rtml.

    org.estar.rtml is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    org.estar.rtml is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with org.estar.rtml; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
// RTML31Create.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTML31Create.java,v 1.12 2014-05-08 09:19:47 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.FactoryConfigurationError;  
import javax.xml.parsers.ParserConfigurationException;
 
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource; 
import javax.xml.transform.stream.StreamResult; 
import javax.xml.transform.OutputKeys;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;  
import org.xml.sax.SAXParseException;  

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.DOMException;

import org.estar.astrometry.*;

/**
 * This class provides the capability of creating an RTML 3.1a document from Java data,
 * from an instance of RTMLDocument into a DOM tree, using JAXP.
 * The resultant DOM tree is traversed,and created into a valid XML document to send to the server.
 * @author Chris Mottram
 * @version $Revision: 1.12 $
 */
public class RTML31Create
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTML31Create.java,v 1.12 2014-05-08 09:19:47 cjm Exp $";
	/**
	 * Default Schema location (URL).
	 */
	//public final static String DEFAULT_SCHEMA_URL_STRING = "http://monet.uni-sw.gwdg.de/XMLSchema/RTML/schemas/RTML-nightly.xsd";
	public final static String DEFAULT_SCHEMA_URL_STRING = "http://telescope.livjm.ac.uk/rtml/RTML-nightly.xsd";
	/**
	 * RTML Schema location. This is the URL of the RTML Schema.
	 * Defaults to DEFAULT_SCHEMA_URL_STRING. It is a class-wide (not per instance) variable.
	 * @see #DEFAULT_SCHEMA_URL_STRING
	 */
	private static String schemaURLString = new String(DEFAULT_SCHEMA_URL_STRING); 
	/**
	 * Private reference to org.w3c.dom.Document, the head of the DOM tree.
	 */
	private Document document = null;

	/**
	 * Method to set the Schema URL String. This is the URL of the RTML Schema. e.g.:
	 * http://monet.uni-sw.gwdg.de/XMLSchema/RTML/schemas/RTML-nightly.xsd
	 * @param s The string to use. This is the URL of the RTML Schema.
	 * @see #DEFAULT_SCHEMA_URL_STRING
	 * @see #schemaURLString
	 */
	public static void setSchemaURLString(String s)
	{
		schemaURLString = s;
	}

	/**
	 * Default constructor.
	 * @exception Exception Thrown if init fails.
	 */
	public RTML31Create()
	{
		super();
	}

	/**
	 * Create an XML representation (DOM tree) from the RTMLDocument (Java object tree).
	 * @param rtmlDocument The Java representation of an RTML document.
	 * @param doc The XML DOM document being created from the Java representation.
	 * @see #document
	 * @see #createRTML
	 */
	public void create(RTMLDocument rtmlDocument,Document doc) throws RTMLException
	{
		document = doc;
		createRTML(rtmlDocument);
	}

	// private methods
	/**
	 * Create RTML element, and add to document.
	 * @param d The Java representation of an RTML document.
	 * @see #document
	 * @see #schemaURLString
	 * @see #createProject
	 * @see #createTelescope
	 * @see #createIntelligentAgent
	 * @see #createDevice
	 * @see #createTarget
	 * @see #createObservation
	 * @see #createScoring
	 */
	private void createRTML(RTMLDocument d)
	{
		RTMLObservation obs = null;
		RTMLIntelligentAgent ia = null;
		Element rtmlElement = null;
		Element respondToElement = null;

		rtmlElement = (Element)document.createElement("RTML"); 
		document.appendChild(rtmlElement);
		rtmlElement.setAttribute("version",d.getVersion());
		if(d.getMode() != null)
			rtmlElement.setAttribute("mode",d.getMode());
		// add various scheme namespaces
		rtmlElement.setAttribute("xmlns","http://www.rtml.org/v3.1a");
		rtmlElement.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
		rtmlElement.setAttribute("xsi:schemaLocation", schemaURLString);
		// This is the mapping from the namespace to it's schema
		// The www.rtml.org domain is now owned by someone else, but here it functions
		// only as a namespace identifier and it not resolved/contacted during document parsing
		// so we think it's still OK to use it here
		rtmlElement.setAttribute("xsi:schemaLocation","http://www.rtml.org/v3.1a "+schemaURLString);
		// uid - must exist in RTML
		if(d.getUId() != null)
			rtmlElement.setAttribute("uid",d.getUId());
		else
			rtmlElement.setAttribute("uid","");
		createHistory(rtmlElement,d.getHistory());
		if(d.getDevice() != null)
			createDevice(rtmlElement,d.getDevice());
		if(d.getTarget() != null)
			createTarget(rtmlElement,d.getTarget());
		if(d.getTelescope() != null)
			createTelescope(rtmlElement,d.getTelescope());
		// Project (which contains Contact in RTML 3.1a
		if(d.getProject() != null)
			createProject(rtmlElement,d);
		// RespondTo (from Intelligent Agent URI)
		if(d.getIntelligentAgent() != null)
		{
			ia = d.getIntelligentAgent();
			if(ia.getUri() != null)
			{
				respondToElement = (Element)document.createElement("RespondTo");
			        respondToElement.appendChild(document.createTextNode(ia.getUri()));
				rtmlElement.appendChild(respondToElement);
			}
			else if(ia.getHostname() != null)
			{
				respondToElement = (Element)document.createElement("RespondTo");
				respondToElement.appendChild(document.createTextNode("http://"+ia.getHostname()+":"+
										     ia.getPort()+"/"));
				rtmlElement.appendChild(respondToElement);
			}
		}
		for(int i = 0; i < d.getObservationListCount(); i++)
		{
			obs = d.getObservation(i);
			createObservation(rtmlElement,obs);
		}
		// Scoring
		if(d.getScoresListCount() > 0)
			createScoring(rtmlElement,d);
	}

	/**
	 * Create a history node and associated sub-elements.
	 * @param rtmlElement The RTML DOM element to add the History tag to.
	 * @param history The Java object containing the history data to add.
	 * @see org.estar.rtml.RTMLHistory
	 */
	private void createHistory(Element rtmlElement,RTMLHistory history)
	{
		Element historyElement = null;
		Element entryElement = null;
		Element subElement = null;
		Element subSubElement = null;
		RTMLHistoryEntry entry = null;
		RTMLDateFormat dateFormat = null;

		dateFormat = new RTMLDateFormat();
		// create History
		historyElement = (Element)document.createElement("History");
		if(history != null)
		{
			for(int i = 0; i< history.getEntryListCount(); i++)
			{

				entry = history.getEntry(i);
				// entry element
				entryElement = (Element)document.createElement("Entry");
				// time stamp
				if(entry.getTimeStamp() != null)
				{
					entryElement.setAttribute("timeStamp",
							dateFormat.formatWithColonTimezone(entry.getTimeStamp()));
				}
				// agent
				if(entry.getAgent() != null)
				{
					subElement = (Element)document.createElement("Agent");
					if(entry.getAgent().getId() != null)
						subElement.setAttribute("name",entry.getAgent().getId());
					if(entry.getAgent().getUri() != null)
						subElement.setAttribute("uri",entry.getAgent().getUri());
					entryElement.appendChild(subElement);
				}
				// descrption
				if(entry.getDescription() != null)
				{
					subElement = (Element)document.createElement("Description");
					subElement.appendChild(document.createTextNode(entry.getDescription()));
					entryElement.appendChild(subElement);
				}
				// error
				if(entry.getError() != null)
				{
					subElement = (Element)document.createElement("Error");
					subElement.appendChild(document.createTextNode(entry.getError()));
					entryElement.appendChild(subElement);
				}
				// rejection
				if(entry.getRejectionReason() != null)
				{
					subElement = (Element)document.createElement("Rejection");
					subElement.setAttribute("reason",entry.getRejectionReason());
					if(entry.getRejectionDescription() != null)
					{
						subSubElement = (Element)document.createElement("Description");
						subSubElement.appendChild(document.createTextNode(
									  entry.getRejectionDescription()));
						subElement.appendChild(subSubElement);
					}
					entryElement.appendChild(subElement);
				}
				// version
				if(entry.getVersion() != 0)
				{
					subElement = (Element)document.createElement("Version");
					subElement.appendChild(document.createTextNode(""+entry.getVersion()));
					entryElement.appendChild(subElement);
				}
				// Add Entry to History
				historyElement.appendChild(entryElement);
			}
		}
		// add history to RTML
		rtmlElement.appendChild(historyElement);
	}

	/**
	 * Create RTML Project node.
	 * @param rtmlElement The RTML DOM element to add the Project tag to.
	 * @param rtmlDocument The Java document object containing the project data to add.
	 * @see org.estar.rtml.RTMLProject
	 * @see #createContact
	 */
	private void createProject(Element rtmlElement,RTMLDocument rtmlDocument)
	{
		RTMLContact contact = null;
		RTMLProject project = null;
		Element projectElement = null;

		// get project from document
		project = rtmlDocument.getProject();
		// Project
		projectElement = (Element)document.createElement("Project");
		if((project != null) && (project.getProject() != null))
			projectElement.setAttribute("ProjectID",project.getProject());
		// if contact exists, add to project
		if(rtmlDocument.getContact() != null)
			createContact(projectElement,rtmlDocument.getContact());
		// add project to document
		rtmlElement.appendChild(projectElement);
	}

	/**
	 * Create a contact node and associated sub-elements.
	 * @param rtmlElement The RTML Project DOM element to add the Contact tag to.
	 * @param contact The Java object containing the contact data to add.
	 * @see org.estar.rtml.RTMLContact
	 */
	private void createContact(Element rtmlElement, RTMLContact contact)
	{
		Element contactElement = null;
		Element communicationElement = null;
		Element userNameElement = null;
		Element subElement = null;
		int index;

		contactElement = (Element)document.createElement("Contact");
		// We can't use contact.getName() here, as RTML3.1a requires a first and last name
		// Contact->Username must be reserved for the Contact User.
		// Contact User
		if(contact.getUser() != null)
		{
			userNameElement = (Element)document.createElement("Username");
			userNameElement.appendChild(document.createTextNode(contact.getUser()));
			contactElement.appendChild(userNameElement);
		}
		// Contact Name
		if(contact.getName() != null)
		{
			// Name
			subElement = (Element)document.createElement("Name");
			subElement.appendChild(document.createTextNode(contact.getName()));
			// add Name to Contact
			contactElement.appendChild(subElement);
		}
		// Contact Institution
		if(contact.getInstitution() != null)
		{
			subElement = (Element)document.createElement("Institution");
			subElement.setAttribute("name",contact.getInstitution());
			contactElement.appendChild(subElement);
		}
		// communication
		communicationElement = (Element)document.createElement("Communication");
		if(contact.getAddress() != null)
		{
			subElement = (Element)document.createElement("AddressLine");
			subElement.appendChild(document.createTextNode(contact.getAddress()));
			communicationElement.appendChild(subElement);
		}
		if(contact.getTelephone() != null)
		{
			subElement = (Element)document.createElement("Telephone");
			subElement.appendChild(document.createTextNode(contact.getTelephone()));
			communicationElement.appendChild(subElement);
		}
		if(contact.getFax() != null)
		{
			subElement = (Element)document.createElement("Fax");
			subElement.appendChild(document.createTextNode(contact.getFax()));
			communicationElement.appendChild(subElement);
		}
		if(contact.getEmail() != null)
		{
			subElement = (Element)document.createElement("Email");
			subElement.appendChild(document.createTextNode(contact.getEmail()));
			communicationElement.appendChild(subElement);
		}
		if(contact.getUrl() != null)
		{
			subElement = (Element)document.createElement("Uri");
			subElement.appendChild(document.createTextNode(contact.getUrl().toString()));
			communicationElement.appendChild(subElement);
		}
		// Add communication to contact
		contactElement.appendChild(communicationElement);
		// Add contact to project (rtmlElement)
		rtmlElement.appendChild(contactElement);
	}

	/**
	 * Create RTML Telescope node.
	 * @param rtmlElement The RTML DOM element to add the Telescope tag to.
	 * @param telescope The Java object containing the telescope data to add.
	 * @see #createTelescopeLocation
	 * @see org.estar.rtml.RTMLTelescope
	 * @see org.estar.rtml.RTMLTelescope#getName
	 * @see org.estar.rtml.RTMLTelescope#getAperture
	 * @see org.estar.rtml.RTMLTelescope#getApertureMeters
	 * @see org.estar.rtml.RTMLTelescope#getApertureUnits
	 * @see org.estar.rtml.RTMLTelescope#getApertureType
	 * @see org.estar.rtml.RTMLTelescope#getFocalRatio
	 * @see org.estar.rtml.RTMLTelescope#getFocalLength
	 * @see org.estar.rtml.RTMLTelescope#getFocalLengthMeters
	 * @see org.estar.rtml.RTMLTelescope#getFocalLengthUnits
	 */
	private void createTelescope(Element rtmlElement,RTMLTelescope telescope)
	{
		Element telescopeElement = null;
		Element subElement = null;
		DecimalFormat nf = null;

		nf = new DecimalFormat("#####0.0#");
		telescopeElement = (Element)document.createElement("Telescope");
		if(telescope.getName() != null)
		{
			telescopeElement.setAttribute("name",telescope.getName());
		}
		if((telescope.getAperture() != 0.0)&&(telescope.getApertureUnits() != null))
		{
			// NB Aperture units are fixed to "meters" in RTML 3.1a.
			subElement = (Element)document.createElement("Aperture");
			subElement.appendChild(document.createTextNode(nf.format(telescope.getApertureMeters())));
			subElement.setAttribute("units","meters"); 
			if(telescope.getApertureType() != null)
				subElement.setAttribute("type",telescope.getApertureType());
			telescopeElement.appendChild(subElement);
		}
		if(telescope.getFocalRatio() != null)
		{
			subElement = (Element)document.createElement("FocalRatio");
			subElement.appendChild(document.createTextNode(telescope.getFocalRatio()));
			telescopeElement.appendChild(subElement);
		}
		if((telescope.getFocalLength() != 0.0)&&(telescope.getFocalLengthUnits() != null))
		{
			// NB FocalLength units are fixed to "meters" in RTML 3.1a.
			subElement = (Element)document.createElement("FocalLength");
			subElement.appendChild(document.createTextNode(nf.format(telescope.getFocalLengthMeters())));
			subElement.setAttribute("units","meters"); 
			telescopeElement.appendChild(subElement);
		}
		if(telescope.getLocation() != null)
			createTelescopeLocation(telescopeElement,telescope.getLocation());
		rtmlElement.appendChild(telescopeElement);
	}

	/**
	 * Create RTML Telescope Location node.
	 * @param rtmlElement The RTML DOM element to add the Telescope Location tag to.
	 * @param telescopeLocation The Java object containing the telescope location data to add.
	 * @see org.estar.rtml.RTMLTelescopeLocation
	 * @see org.estar.rtml.RTMLTelescopeLocation#getName
	 * @see org.estar.rtml.RTMLTelescopeLocation#getLongitude
	 * @see org.estar.rtml.RTMLTelescopeLocation#getLatitude
	 * @see org.estar.rtml.RTMLTelescopeLocation#getAltitude
	 */
	private void createTelescopeLocation(Element rtmlElement,RTMLTelescopeLocation telescopeLocation)
	{
		Element telescopeLocationElement = null;
		Element subElement = null;
		DecimalFormat nf = null;

		nf = new DecimalFormat("#####0.0#");
		telescopeLocationElement = (Element)document.createElement("Location");
		if(telescopeLocation.getName() != null)
		{
			telescopeLocationElement.setAttribute("name",telescopeLocation.getName());
		}
		if(telescopeLocation.getLongitude() != 0.0)
		{
			subElement = (Element)document.createElement("EastLongitude");
			subElement.setAttribute("units","degrees");
			subElement.appendChild(document.createTextNode(nf.format(telescopeLocation.getLongitude())));
			telescopeLocationElement.appendChild(subElement);
		}
		if(telescopeLocation.getLatitude() != 0.0)
		{
			subElement = (Element)document.createElement("Latitude");
			subElement.setAttribute("units","degrees");
			subElement.appendChild(document.createTextNode(nf.format(telescopeLocation.getLatitude())));
			telescopeLocationElement.appendChild(subElement);
		}
		if(telescopeLocation.getAltitude() != 0.0)
		{
			subElement = (Element)document.createElement("Height");
			subElement.setAttribute("units","meters");
			subElement.appendChild(document.createTextNode(nf.format(telescopeLocation.getAltitude())));
			telescopeLocationElement.appendChild(subElement);
		}
		rtmlElement.appendChild(telescopeLocationElement);
	}

	/**
	 * Create the Intelligent Agent node.
	 */
	private void createIntelligentAgent(Element rtmlElement,RTMLIntelligentAgent rtmlIA)
	{
		Element iaElement = null;

		iaElement = (Element)document.createElement("IntelligentAgent");
		if(rtmlIA.getHostname() != null)
			iaElement.setAttribute("host",rtmlIA.getHostname());
		if(rtmlIA.getPort() != 0)
			iaElement.setAttribute("port",""+rtmlIA.getPort());
		if(rtmlIA.getId() != null)
			iaElement.appendChild(document.createTextNode(rtmlIA.getId()));
		rtmlElement.appendChild(iaElement);
	}

	/**
	 * Method to create the Device tags.
	 * Create a contact node and associated sub-elements.
	 * @param rtmlElement The RTML DOM element to add the Contact tag to.
	 * @param device The Java object containing the device (instrument) data to add.
	 * @see org.estar.rtml.RTMLDevice
	 * @see #createDetector
	 * @see #createGrating
	 */
	private void createDevice(Element rtmlElement,RTMLDevice device)
	{
		Element deviceElement = null;
		Element spectralRegionElement = null;
		Element setupElement = null;
		Element filterElement = null;
		Element filterTypeElement = null;
		Element centerElement = null;

		deviceElement = (Element)document.createElement("Device");
		// RTML 3.1a name and type (DeviceTypes)
		if(device.getType() != null)
			deviceElement.setAttribute("type",device.getType());
		if(device.getName() != null)
			deviceElement.setAttribute("name",device.getName());
		// spectral region
		if(device.getSpectralRegion() != null)
		{
			spectralRegionElement = (Element)document.createElement("SpectralRegion");
			spectralRegionElement.appendChild(document.createTextNode(device.getSpectralRegion()));
			deviceElement.appendChild(spectralRegionElement);
		}
		// create setup containing filter/detector/grating etc
		setupElement = (Element)document.createElement("Setup");
		// filter
		if(device.getFilterType() != null)
		{
			// filter node/tag
			filterElement = (Element)document.createElement("Filter");
			filterElement.setAttribute("type",device.getFilterType());
			setupElement.appendChild(filterElement);
		}
		// detector
		if(device.getDetector() != null)
			createDetector(setupElement,device.getDetector());
		// grating
		if(device.getGrating() != null)
		{
			// For FrodoSpec we can use the Grating name.
			if(device.getGrating().getName() != null)
			{
				createGrating(setupElement,device.getGrating());
			}
			// for Meaburn we want to encapsulate the central wavelength data here
			// RTML 3.1a Filter has a Central wavelength mode.
			if(device.getGrating().getWavelength() != 0.0f)
			{
				// filter node/tag
				filterElement = (Element)document.createElement("Filter");
				centerElement = (Element)document.createElement("Center");
				centerElement.setAttribute("units",device.getGrating().getWavelengthUnits());
				centerElement.appendChild(document.createTextNode(""+
							  device.getGrating().getWavelengthString()));
				filterElement.appendChild(centerElement);
				setupElement.appendChild(filterElement);
			}
		}
		// add Setup to Device
		deviceElement.appendChild(setupElement);
		// Add Device to RTML/Schedule
		rtmlElement.appendChild(deviceElement);
	}

	/**
	 * Method to create the Detector tags.
	 * Create a detector node and associated sub-elements.
	 * @param rtmlElement The RTML DOM element (Setup) to add the Detector tag to.
	 * @param detector The Java object containing the detector (instrument) data to add.
	 * @see org.estar.rtml.RTMLDetector
	 * @see #createDevice
	 */
	private void createDetector(Element rtmlElement,RTMLDetector detector)
	{
		Element detectorElement = null;
		Element binningElement = null;
		Element xyElement = null;
		Element gainElement = null;
		Element descriptionElement = null;
		DecimalFormat df = null;

		df = new DecimalFormat("###0.0#");
		detectorElement = (Element)document.createElement("Detector");
		// binning node/tag
		binningElement = (Element)document.createElement("Binning");
		detectorElement.appendChild(binningElement);
		// X
		xyElement = (Element)document.createElement("X");
		xyElement.appendChild(document.createTextNode(""+detector.getColumnBinning()));
		xyElement.setAttribute("units","pixels");
		binningElement.appendChild(xyElement);
		// Y
		xyElement = (Element)document.createElement("Y");
		xyElement.appendChild(document.createTextNode(""+detector.getRowBinning()));
		xyElement.setAttribute("units","pixels");
		binningElement.appendChild(xyElement);
		// gain
		if(detector.getUseGain())
		{
			gainElement = (Element)document.createElement("Gain");
			// We should really put the gain in the 'Value' element.
			// But the 'Value' element has a fixed 'units' attribute of 'electrons_per_adu'
			// We want to specify the EMCCD Gain, which as far as I know, does not use these units.
			// Therefore stuff the value into the 'Description' element instead.
			// This is reflected in the corresponding parser code which expects the value to be hidden here
			// (RTML31Parser.java:parseGainNode)
			descriptionElement = (Element)document.createElement("Description");
			descriptionElement.appendChild(document.createTextNode(""+df.format(detector.getGain())));
			gainElement.appendChild(descriptionElement);
			detectorElement.appendChild(gainElement);
		}
		// append Detector to parent Device element
		rtmlElement.appendChild(detectorElement);
	}

	/**
	 * Method to create the Grating tags.
	 * Create a grating node and associated attributes.
	 * @param rtmlElement The RTML DOM element or Setup to add the Grating tag to.
	 * @param grating The Java object containing the grating (instrument) data to add.
	 * @see #createDevice
	 * @see org.estar.rtml.RTMLGrating
	 * @see org.estar.rtml.RTMLGrating#getName
	 * @see org.estar.rtml.RTMLGrating#getWavelengthString
	 * @see org.estar.rtml.RTMLGrating#getWavelengthUnits
	 * @see org.estar.rtml.RTMLGrating#getResolutionString
	 * @see org.estar.rtml.RTMLGrating#getAngleString
	 */
	private void createGrating(Element rtmlElement,RTMLGrating grating)
	{
		Element gratingElement = null;

		gratingElement = (Element)document.createElement("Grating");
		if(grating.getName() != null)
			gratingElement.setAttribute("name",grating.getName());
		if(grating.getWavelength() != 0.0)
			gratingElement.setAttribute("wavelength",grating.getWavelengthString());
		if(grating.getWavelengthUnits() != null)
			gratingElement.setAttribute("units",grating.getWavelengthUnits());
		if(grating.getResolution() != 0.0)
			gratingElement.setAttribute("resolution",grating.getResolutionString());
		if(grating.getAngle() != 0.0)
			gratingElement.setAttribute("angle",grating.getAngleString());
		rtmlElement.appendChild(gratingElement);
	}

	/**
	 * Method to create XML in the Observation tag.
	 * Our document model has Observation's, each Containing one Schedule.
	 * However, RTML3.1a has the main RTML element containing Schedule elements, each containing one or
	 * more Observation elements.
	 * In our document mode, each Observation can have multiple ImageData's for each FITS image
	 * generated by one observation (which may have SeriesConstraint data).
	 * However, in RTML3.1a the data in ImageData has to be represented in a ImageData tag <b>and</b>
	 * a SourceCatalogue tag.
	 * Therefore, this method creates a Schedule element. It puts an Observation element in for each Image data.
	 * @param rtmlElement The RTML element to add the Observation tag to.
	 * @param observation The Java object containing the observation data to add.
	 * @see #document
	 * @see #createTarget
	 * @see #createDevice
	 * @see #createSchedule
	 * @see #createImageData
	 * @see #createSourceCatalogue
	 */
	private void createObservation(Element rtmlElement,RTMLObservation observation)
	{
		Element scheduleElement = null;
		Element observationElement = null;
		RTMLImageData imageData = null;

		// RTML 3.1a requires Observation to be inside Schedule
		scheduleElement = (Element)document.createElement("Schedule");
		if(observation.getTarget() != null)
			createTarget(scheduleElement,observation.getTarget());
		if(observation.getDevice() != null)
			createDevice(scheduleElement,observation.getDevice());
		if(observation.getSchedule() != null)
			createSchedule(scheduleElement,observation.getSchedule());
		// for each ImageData
		for(int i = 0; i < observation.getImageDataCount(); i++)
		{
			imageData = observation.getImageData(i);
			// Create an Observation
			observationElement = (Element)document.createElement("Observation");
			// Create ImageData within Observation
			createImageData(observationElement,imageData);
			// Create SourceCatalogue within Observation
			createSourceCatalogue(observationElement,imageData);
			// Add Observation to Schedule
			scheduleElement.appendChild(observationElement);
		}
		// Add Schedule to parent
		rtmlElement.appendChild(scheduleElement);
	}

	/**
	 * Create a Target element and append it to the specified Observation Schedule/RTML element.
	 * @param parentElement The parent element to append the new Target element to, probably a Schedule
	 *        or could be an RTML element.
	 * @param target The RTMLTarget object containing target information.
	 * @see RTMLTarget
	 */
	private void createTarget(Element parentElement,RTMLTarget target)
	{
		Element targetElement = null;
		Element coordElement = null;
		Element raElement = null;
		Element decElement = null;
		Element equinoxElement = null;
		Element brightnessElement = null;
		Element subElement = null;
		String s = null;
		DecimalFormat df = null;

		df = new DecimalFormat("##0.0#");
		// target element
		targetElement = (Element)document.createElement("Target");
		// type attribute does not exist in RTML3.1a - perhaps try Priority sub-element?
		//targetElement.setAttribute("type",target.getType());
		// ident attribute - map to RTML3.1a RTMLAttribute 'id'
		if(target.getIdent() != null)
			targetElement.setAttribute("id",target.getIdent());
		// target name attribute
		if(target.getName() != null)
			targetElement.setAttribute("name",target.getName());
		// coordinate sub-element
		coordElement = (Element)document.createElement("Coordinates");
		// add coordinate sub-element to target element
		targetElement.appendChild(coordElement);
		// ra sub-sub element
		if(target.getRA() != null)
		{
			raElement = (Element)document.createElement("RightAscension");
			// hours
			subElement = (Element)document.createElement("Hours");
			subElement.appendChild(document.createTextNode(""+target.getRA().getHours()));
			raElement.appendChild(subElement);
			// minutes
			subElement = (Element)document.createElement("Minutes");
			subElement.appendChild(document.createTextNode(""+target.getRA().getMinutes()));
			raElement.appendChild(subElement);
			// seconds
			subElement = (Element)document.createElement("Seconds");
			subElement.appendChild(document.createTextNode(df.format(target.getRA().getSeconds())));
			raElement.appendChild(subElement);
			// offset
			subElement = (Element)document.createElement("Offset");
			subElement.setAttribute("units","arcseconds");
			subElement.appendChild(document.createTextNode(df.format(target.getRAOffset())));
			raElement.appendChild(subElement);
			// add ra element to coordElement.
			coordElement.appendChild(raElement);
		}
		// dec sub-sub element
		if(target.getDec() != null)
		{
			decElement = (Element)document.createElement("Declination");
			// degrees
			subElement = (Element)document.createElement("Degrees");
			if(target.getDec().getNegative())
				s = new String("-");
			else
				s = new String("+");
			// getDegrees always returns +'ve, we use getNegative to determine the sign
			s = new String(s+target.getDec().getDegrees());
			subElement.appendChild(document.createTextNode(s));
			decElement.appendChild(subElement);
			// minutes
			subElement = (Element)document.createElement("Arcminutes");
			subElement.appendChild(document.createTextNode(""+target.getDec().getMinutes()));
			decElement.appendChild(subElement);
			// seconds
			subElement = (Element)document.createElement("Arcseconds");
			subElement.appendChild(document.createTextNode(df.format(target.getDec().getSeconds())));
			decElement.appendChild(subElement);
			// offset
			subElement = (Element)document.createElement("Offset");
			subElement.setAttribute("units","arcseconds");
			subElement.appendChild(document.createTextNode(df.format(target.getDecOffset())));
			decElement.appendChild(subElement);
			// add dec element to coordElement.
			coordElement.appendChild(decElement);
		}
		if(target.getEquinox() != null)
		{
			equinoxElement = (Element)document.createElement("Equinox");
			equinoxElement.appendChild(document.createTextNode(target.getEquinox()));
			// add equinox element to coordElement.
			coordElement.appendChild(equinoxElement);
		}
		// System  = FK5 or ICRS
		subElement = (Element)document.createElement("System");
		subElement.appendChild(document.createTextNode("FK5"));
		// diddly subElement never added here!
		// target magnitude
		if(target.getMagnitudeFilterType() != null)
		{
			// TargetBrightness
			brightnessElement = (Element)document.createElement("TargetBrightness");
			// Magnitude
			subElement = (Element)document.createElement("Magnitude");
			subElement.appendChild(document.createTextNode(df.format(target.getMagnitude())));
			brightnessElement.appendChild(subElement);
			// Type (filter)
			subElement = (Element)document.createElement("Type");
			subElement.appendChild(document.createTextNode(target.getMagnitudeFilterType()));
			brightnessElement.appendChild(subElement);
			// Error
			if(target.getMagnitudeError() != 0.0)
			{
				subElement = (Element)document.createElement("Error");
				subElement.appendChild(document.createTextNode(df.format(target.getMagnitudeError())));
				brightnessElement.appendChild(subElement);
			}
			// append TargetBrightness to Target
			targetElement.appendChild(brightnessElement);
		}
		// add target to the parent
		parentElement.appendChild(targetElement);
		
	}

	/**
	 * Create a Schedule tag details. The Schedule element itself was created in createObservation.
	 * This is because the document object model has Observation containing Schedule, whilst in RTML 3.1a
	 * Schedule contains Observation.
	 * @param scheduleElement The schedule XML node to add the schedule details to.
	 * @param schedule The RTML schedule data.
	 * @see #createTimeConstraint
	 * @see #createSeriesConstraint
	 * @see #createSeeingConstraint
	 * @see #createMoonConstraint
	 * @see #createSkyConstraint
	 * @see #createExtinctionConstraint
	 * @see #createAirmassConstraint
	 * @see RTMLSchedule
	 */
	private void createSchedule(Element scheduleElement,RTMLSchedule schedule)
	{
		Element priorityElement = null;
		Element exposureElement = null;
		Element exposureConstraintElement = null;
		Element valueElement = null;
		Element exposureCountElement = null;
		Element snrElement = null;
		DecimalFormat df = null;

		df = new DecimalFormat("#####0.0##");
		// schedule priority element
		priorityElement = (Element)document.createElement("Priority");
		priorityElement.appendChild(document.createTextNode(""+schedule.getPriority()));
		scheduleElement.appendChild(priorityElement);
		// if type of exposure
		if(schedule.isExposureTypeSNR())
		{
			// exposure constraint (type == snr)
			exposureConstraintElement  = (Element)document.createElement("ExposureConstraint");
			// count is a sub-element in constraint!
			exposureCountElement = (Element)document.createElement("Count");
			exposureCountElement.appendChild(document.createTextNode(""+schedule.getExposureCount()));
			exposureConstraintElement.appendChild(exposureCountElement);
			// snr - minimum
			snrElement  = (Element)document.createElement("MinimumSignalToNoise");
			exposureConstraintElement.appendChild(document.createTextNode(
							     df.format(schedule.getExposureLength())));
			exposureConstraintElement.appendChild(snrElement);
			// snr - maximum
			snrElement  = (Element)document.createElement("MaximumSignalToNoise");
			exposureConstraintElement.appendChild(document.createTextNode(
							     df.format(schedule.getExposureLength())));
			exposureConstraintElement.appendChild(snrElement);
			// add exposure to a schedule
			scheduleElement.appendChild(exposureConstraintElement);
		}
		else // getExposureType == "time"
		{
			// exposure element
			exposureElement = (Element)document.createElement("Exposure");
			exposureElement.setAttribute("count",""+schedule.getExposureCount());
			// exposure value sub-element
			valueElement = (Element)document.createElement("Value");
			// exposure units are fixed as "seconds" in RTML 3.1
			valueElement.setAttribute("units","seconds");
			valueElement.appendChild(document.createTextNode(df.format(
						 schedule.getExposureLengthSeconds())));
			exposureElement.appendChild(valueElement);
			// add exposure to a schedule
			scheduleElement.appendChild(exposureElement);
		}
		// TimeConstraint
		if((schedule.getStartDate() != null) || (schedule.getEndDate() != null))
			createTimeConstraint(scheduleElement,schedule);
		// SeriesConstraint
		if(schedule.getSeriesConstraint() != null)
			createSeriesConstraint(scheduleElement,schedule.getSeriesConstraint());
		// SeeingConstraint
		if(schedule.getSeeingConstraint() != null)
			createSeeingConstraint(scheduleElement,schedule.getSeeingConstraint());
		// MoonConstraint
		if(schedule.getMoonConstraint() != null)
			createMoonConstraint(scheduleElement,schedule.getMoonConstraint());
		// SkyConstraint
		if(schedule.getSkyConstraint() != null)
			createSkyConstraint(scheduleElement,schedule.getSkyConstraint());
		// ExtinctionConstraint
		if(schedule.getExtinctionConstraint() != null)
			createExtinctionConstraint(scheduleElement,schedule.getExtinctionConstraint());
		// AirmassConstraint
		if(schedule.getAirmassConstraint() != null)
			createAirmassConstraint(scheduleElement,schedule.getAirmassConstraint());
	}

	/**
	 * Create a TimeConstraint tag.
	 * @param scheduleElement The schedule XML node to add the schedule to.
	 * @param schedule The RTML schedule data (which contains time constrint data).
	 * @see RTMLSchedule
	 * @see RTMLDateFormat
	 */
	private void createTimeConstraint(Element scheduleElement,RTMLSchedule schedule)
	{
		Element timeConstraintElement = null;
		Element dateTimeElement = null;
		RTMLDateFormat dateFormat = null;

		dateFormat = new RTMLDateFormat();
		// schedule element
		timeConstraintElement = (Element)document.createElement("DateTimeConstraint");
		timeConstraintElement.setAttribute("type","include");
		// start date time element
		if(schedule.getStartDate() != null)
		{
			dateTimeElement = (Element)document.createElement("DateTimeStart");
			dateTimeElement.setAttribute("value",
						     dateFormat.formatWithColonTimezone(schedule.getStartDate()));
			dateTimeElement.setAttribute("system","UT");
			// add dateTimeElement to a timeConstraintElement
			timeConstraintElement.appendChild(dateTimeElement);
		}
		// end date time element
		if(schedule.getEndDate() != null)
		{
			dateTimeElement = (Element)document.createElement("DateTimeEnd");
			dateTimeElement.setAttribute("value",
						     dateFormat.formatWithColonTimezone(schedule.getEndDate()));
			dateTimeElement.setAttribute("system","UT");
			// add dateTimeElement to a timeConstraintElement
			timeConstraintElement.appendChild(dateTimeElement);
		}
		// add timeConstraintElement to a scheduleElement
		scheduleElement.appendChild(timeConstraintElement);		
	}

	/**
	 * Create a SeriesConstraint tag.
	 * @param scheduleElement The schedule XML node to add the series constraint to.
	 * @param seriesConstraint The RTML series constraint data.
	 * @see RTMLSeriesConstraint
	 */
	private void createSeriesConstraint(Element scheduleElement,RTMLSeriesConstraint seriesConstraint)
	{
		Element seriesConstraintElement = null;
		Element countElement = null;
		Element intervalElement = null;
		Element toleranceElement = null;

		// series constraint element
		seriesConstraintElement = (Element)document.createElement("SeriesConstraint");
		// count element
		if(seriesConstraint.getCount() != 0)
		{
			countElement = (Element)document.createElement("Count");
			countElement.appendChild(document.createTextNode(""+seriesConstraint.getCount()));
			// add countElement to a seriesConstraintElement
			seriesConstraintElement.appendChild(countElement);
		}
		// interval element
		if(seriesConstraint.getInterval() != null)
		{
			intervalElement = (Element)document.createElement("Interval");
			intervalElement.appendChild(document.createTextNode(seriesConstraint.getInterval().
									    toString()));
			// add intervalElement to a seriesConstraintElement
			seriesConstraintElement.appendChild(intervalElement);
		}
		// tolerance element
		if(seriesConstraint.getTolerance() != null)
		{
			toleranceElement = (Element)document.createElement("Tolerance");
			toleranceElement.appendChild(document.createTextNode(seriesConstraint.getTolerance().
									    toString()));
			// add toleranceElement to a seriesConstraintElement
			seriesConstraintElement.appendChild(toleranceElement);
		}
		// add seriesConstraintElement to a scheduleElement
		scheduleElement.appendChild(seriesConstraintElement);		
	}

	/**
	 * Create a AirmassConstraint tag.
	 * @param scheduleElement The schedule XML node to add the airmass constraint to.
	 * @param airmassConstraint The RTML seeing constraint data.
	 * @see RTMLAirmassConstraint
	 */
	private void createAirmassConstraint(Element scheduleElement,RTMLAirmassConstraint airmassConstraint)
	{
		Element airmassConstraintElement = null;
		DecimalFormat nf = null;

		nf = new DecimalFormat("#####0.0#");
		// airmass constraint element
		airmassConstraintElement = (Element)document.createElement("AirmassConstraint");
		airmassConstraintElement.setAttribute("minimum",nf.format(airmassConstraint.getMinimum()));
		airmassConstraintElement.setAttribute("maximum",nf.format(airmassConstraint.getMaximum()));
		// add airmassConstraintElement to a scheduleElement
		scheduleElement.appendChild(airmassConstraintElement);		
	}

	/**
	 * Create a SeeingConstraint tag.
	 * @param scheduleElement The schedule XML node to add the seeing constraint to.
	 * @param seeingConstraint The RTML seeing constraint data.
	 * @see RTMLSeeingConstraint
	 */
	private void createSeeingConstraint(Element scheduleElement,RTMLSeeingConstraint seeingConstraint)
	{
		Element seeingConstraintElement = null;
		DecimalFormat nf = null;

		nf = new DecimalFormat("#####0.0#");
		// seeing constraint element
		seeingConstraintElement = (Element)document.createElement("SeeingConstraint");
		seeingConstraintElement.setAttribute("minimum",nf.format(seeingConstraint.getMinimum()));
		seeingConstraintElement.setAttribute("maximum",nf.format(seeingConstraint.getMaximum()));
		seeingConstraintElement.setAttribute("units","arcseconds");
		// add seeingConstraintElement to a scheduleElement
		scheduleElement.appendChild(seeingConstraintElement);		
	}

	/**
	 * Create a MoonConstraint tag.
	 * @param scheduleElement The schedule XML node to add the moon constraint to.
	 * @param moonConstraint The RTML moon constraint data.
	 * @see RTMLMoonConstraint
	 */
	private void createMoonConstraint(Element scheduleElement,RTMLMoonConstraint moonConstraint)
	{
		Element moonConstraintElement = null;
		Element distanceElement = null;
		DecimalFormat nf = null;

		nf = new DecimalFormat("#####0.0#");
		// moon constraint element
		moonConstraintElement = (Element)document.createElement("MoonConstraint");
		// distance sub-element
		distanceElement = (Element)document.createElement("Distance");
		distanceElement.appendChild(document.createTextNode(nf.format(moonConstraint.getDistance())));
		distanceElement.setAttribute("units","degrees");
		// add Distance to MoonConstraint
		moonConstraintElement.appendChild(distanceElement);
		// add MoonConstraint to Schedule
		scheduleElement.appendChild(moonConstraintElement);		
	}

	/**
	 * Create a SkyConstraint tag.
	 * @param scheduleElement The schedule XML node to add the sky constraint to.
	 * @param skyConstraint The RTML sky constraint data.
	 * @see RTMLSkyConstraint
	 * @see RTMLSkyConstraint#getSky
	 * @see RTMLSkyConstraint#getUseValue
	 * @see RTMLSkyConstraint#getValue
	 * @see RTMLSkyConstraint#getUnits
	 */
	private void createSkyConstraint(Element scheduleElement,RTMLSkyConstraint skyConstraint)
	{
		Element skyConstraintElement = null;
		Element subElement = null;
		DecimalFormat df = null;

		// sky constraint element
		skyConstraintElement = (Element)document.createElement("SkyConstraint");
		if(skyConstraint.getSky() != null)
		{
			subElement = (Element)document.createElement("Brightness");
			subElement.appendChild(document.createTextNode(skyConstraint.getSky()));
			skyConstraintElement.appendChild(subElement);
		}
		if(skyConstraint.getUseValue())
		{
			// flux
			df = new DecimalFormat("#0.0##");
			subElement = (Element)document.createElement("Flux");
			subElement.appendChild(document.createTextNode(df.format(skyConstraint.getValue())));
			skyConstraintElement.appendChild(subElement);
			// units
			subElement = (Element)document.createElement("Units");
			subElement.appendChild(document.createTextNode(skyConstraint.getUnits()));
			skyConstraintElement.appendChild(subElement);
		}
		// add skyConstraintElement to a scheduleElement
		scheduleElement.appendChild(skyConstraintElement);		
	}

	/**
	 * Create a ExtinctionConstraint tag.
	 * @param scheduleElement The schedule XML node to add the sky constraint to.
	 * @param extinctionConstraint The RTML extinction constraint data.
	 * @see RTMLExtinctionConstraint
	 */
	private void createExtinctionConstraint(Element scheduleElement,RTMLExtinctionConstraint extinctionConstraint)
	{
		Element extinctionConstraintElement = null;
		Element cloudsElement = null;
		String cloudsString = null;
		Element magnitudesElement = null;

		// extinction constraint element
		extinctionConstraintElement = (Element)document.createElement("ExtinctionConstraint");
		// clouds
		cloudsString = extinctionConstraint.getClouds();
		if(cloudsString != null)
		{
			cloudsElement = (Element)document.createElement("Clouds");
			cloudsElement.appendChild(document.createTextNode(cloudsString));
			extinctionConstraintElement.appendChild(cloudsElement);
		}
		// magnitudes
		if(extinctionConstraint.getValue() != 0.0)
		{
			magnitudesElement = (Element)document.createElement("Magnitudes");
			magnitudesElement.appendChild(document.createTextNode(""+extinctionConstraint.getValue()));
			extinctionConstraintElement.appendChild(magnitudesElement);
		}
		// add extinctionConstraintElement to a scheduleElement
		scheduleElement.appendChild(extinctionConstraintElement);		
	}

	/**
	 * Mehtod to create image data sub-node.
	 * @param rtmlElement The Element to add the <ImageData> node to.
	 * @param imageData The object to construct the <ImageData> node from.
	 * @see RTMLImageData
	 */
	private void createImageData(Element rtmlElement,RTMLImageData imageData)
	{
		Element imageDataElement = null;
		Element fitsHeaderElement = null;
		Element subElement = null;
		String s = null;

		imageDataElement = (Element)document.createElement("ImageData");
		// FITS header
		if(imageData.getFITSHeader() != null)
		{
			fitsHeaderElement = (Element)document.createElement("FITSHeader");
			fitsHeaderElement.appendChild(document.createTextNode(imageData.getFITSHeader()));
			imageDataElement.appendChild(fitsHeaderElement);
		}
		// if URL is present, add as text node
		if(imageData.getImageDataURL() != null)
		{
			s = imageData.getImageDataURL().toString();
			subElement = (Element)document.createElement("Uri");
			subElement.appendChild(document.createTextNode(s));
			imageDataElement.appendChild(subElement);
		}
		// Delivery type
		subElement = (Element)document.createElement("Delivery");
		subElement.appendChild(document.createTextNode("uri"));
		imageDataElement.appendChild(subElement);
		// CompressionTypes
		subElement = (Element)document.createElement("CompressionTypes");
		subElement.appendChild(document.createTextNode("none"));
		imageDataElement.appendChild(subElement);
		// add ImageData to parent
		rtmlElement.appendChild(imageDataElement);
	}

	/**
	 * Mehtod to create source catalogue sub-node.
	 * @param rtmlElement The Element to add the <SourceCatalogue> node to.
	 * @param imageData The object to construct the <SourceCatalogue> node from.
	 * @see RTMLImageData
	 * @see RTMLImageData#getObjectListType
	 * @see RTMLImageData#isObjectListTypeCluster
	 * @see RTMLImageData#getObjectListCluster
	 * @see RTMLImageData#isObjectListTypeVOTableURL
	 * @see RTMLImageData#getObjectListVOTableURL
	 */
	private void createSourceCatalogue(Element rtmlElement,RTMLImageData imageData)
	{
		Element sourceCatalogueElement = null;
		Element subElement = null;
		String s = null;

		// Only create SourceCatalogue if the object list type is set,
		// otherwise you get a blank SourceCatalogue which fails to parse.
		if(imageData.getObjectListType() != null)
		{
			// SourceCatalogue
			sourceCatalogueElement = (Element)document.createElement("SourceCatalogue");
			// Cluster
			if(imageData.isObjectListTypeCluster())
			{
				sourceCatalogueElement.appendChild(document.createTextNode(imageData.
											   getObjectListCluster()));
				sourceCatalogueElement.setAttribute("type","text");// imageData.getObjectListType()
				// Cluster format: " fn sn rah ram ras decd decm decs xpos ypos mag magerror magflag"
			}
			// VOTableURL
			if(imageData.isObjectListTypeVOTableURL())
			{
				sourceCatalogueElement.appendChild(document.createTextNode(imageData.
								   getObjectListVOTableURL().toString()));
				sourceCatalogueElement.setAttribute("type","other");
			}
			// add SourceCatalogue to parent (Observation?)
			rtmlElement.appendChild(sourceCatalogueElement);
		}// end if getObjectListType != null
	}

	/**
	 * Create a Scoring element, and Score sub-elements.
	 * @param rtmlElement The RTML document element to put the score element in.
	 * @param rtmlDocument The RTML document
	 * @see RTMLDocument#getScoresListCount
	 * @see RTMLDocument#getScore
	 * @see RTMLScore#getDelay
	 * @see RTMLScore#getProbability
	 * @see RTMLScore#getCumulative
	 */
	private void createScoring(Element rtmlElement,RTMLDocument rtmlDocument)
	{
		RTMLScore score = null;
		Element scoringElement = null;
		Element scoreElement = null;
		DecimalFormat df = null;

		df = new DecimalFormat("#####0.0#####");
		// Create Scores element
		scoringElement = (Element)document.createElement("Scoring");
		// loop on Score list
		for(int i = 0; i < rtmlDocument.getScoresListCount(); i++)
		{
			// get score
			score = rtmlDocument.getScore(i);
			// create Score element
			scoreElement = (Element)document.createElement("Score");
			scoreElement.setAttribute("delay",score.getDelay().toString());
			if(Double.isNaN(score.getProbability()))
				scoreElement.setAttribute("probability","NaN");
			else
				scoreElement.setAttribute("probability",df.format(score.getProbability()));
			if(Double.isNaN(score.getCumulative()))
				scoreElement.setAttribute("cumulative","NaN");
			else
				scoreElement.setAttribute("cumulative",df.format(score.getCumulative()));
			// Add score to Scoring list
			scoringElement.appendChild(scoreElement);

		}
		// Add Scoring to RTML
		rtmlElement.appendChild(scoringElement);
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.11  2013/08/30 12:44:46  eng
** removed rtml.org from schemaLocation attribute
**
** Revision 1.10  2013/01/14 11:04:40  cjm
** Added <Detector> <Gain> paramater. Currently the value is stuffed in a <Gain><Description>
** tag, as the <Gain><Value> tag has fixed units which are not compatible with Andor EMCCD
** units.
**
** Revision 1.9  2012/05/24 14:08:22  cjm
** Changed createSkyConstraint so it creates Flux and Unuts subElements as appropriate, as part of RCS
** sky brightness changes.
**
** Revision 1.8  2011/02/09 18:42:04  cjm
** Added airmass and extinction constraint creation.
**
** Revision 1.7  2009/08/12 17:54:19  cjm
** Now creates document-wide target if present.
** Now creates Grating, for Frodospec.
** Now creates TargetBrightness if present.
**
** Revision 1.6  2009/03/27 11:26:25  cjm
** Changed createSourceCatalogue so SourceCatalogue is not created if the objectListType is not set.
** This stops the parser failing.
**
** Revision 1.5  2009/03/16 12:12:30  cjm
** Changed default schema to telescope.livjm.ac.uk hosted. ltproxy
** hosted does not work as ltproxy cannot see itself via external ip/8080.
**
** Revision 1.4  2009/03/16 12:00:15  cjm
** Changed default URL schema from monet hosted to ltproxy hosted copy.
**
** Revision 1.3  2008/08/11 13:54:54  cjm
** Added creation of target offset "Offset" elements.
**
** Revision 1.2  2008/06/05 14:19:51  cjm
** Added Telescope and Telescope Location support.
**
** Revision 1.1  2008/05/23 14:08:50  cjm
** Initial revision
**
*/
