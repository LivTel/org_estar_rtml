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
// RTMLCreate.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLCreate.java,v 1.36 2007-05-02 09:17:20 snf Exp $
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
 * This class provides the capability of creating an RTML document from Java data,
 * from an instance of RTMLDocument into a DOM tree, using JAXP.
 * The resultant DOM tree is traversed,and created into a valid XML document to send to the server.
 * @author Chris Mottram, Jason Etherton
 * @version $Revision: 1.36 $
 */
public class RTMLCreate
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLCreate.java,v 1.36 2007-05-02 09:17:20 snf Exp $";
	/**
	 * RTML version attribute constant string (2.2) for eSTAR documents.
	 */
	public final static String DEFAULT_RTML_VERSION_STRING = "2.2";
	/**
	 * System ID put into DOCTYPE statement. This is the URL of the RTML DTD.
	 */
        //public final static String DEFAULT_DOCTYPE_SYSTEM_ID = "http://150.204.240.111/~dev/robonet/rtml2.1.dtd";
        //public final static String DEFAULT_DOCTYPE_SYSTEM_ID = "http://www.astro.livjm.ac.uk/~je/rtml2.1.dtd";
        //public final static String DEFAULT_DOCTYPE_SYSTEM_ID = "http://www.estar.org.uk/documents/rtml2.1.dtd";
        public final static String DEFAULT_DOCTYPE_SYSTEM_ID = "http://www.estar.org.uk/documents/rtml2.2.dtd";
	/**
	 * RTML version string. Defaults to DEFAULT_RTML_VERSION_STRING.
	 * @see #DEFAULT_RTML_VERSION_STRING
	 */
	private String rtmlVersionString = new String(DEFAULT_RTML_VERSION_STRING);
	/**
	 * RTML doctype system ID. Put into DOCTYPE statement. This is the URL of the RTML DTD.
	 * Defaults to DEFAULT_DOCTYPE_SYSTEM_ID.
	 * @see #DEFAULT_DOCTYPE_SYSTEM_ID
	 */
	private String doctypeSystemID = new String(DEFAULT_DOCTYPE_SYSTEM_ID); 
	/**
	 * The instance of DocumentBuilder, used to build the document tree.
	 */
	private DocumentBuilder builder = null;
	/**
	 * The instance of RTMLErrorHandler, attached to the DocumentBuilder to handle DOM errors.
	 */
	RTMLErrorHandler errorHandler = null;
	/**
	 * Private reference to org.w3c.dom.Document, the head of the DOM tree.
	 */
	private Document document = null;

	/**
	 * Default constructor.
	 * @exception Exception Thrown if init fails.
	 * @see #init
	 */
	public RTMLCreate() throws Exception
	{
		super();
		try
		{
			init();
		}
		catch(Exception e)
		{
			//SAXException,ParserConfigurationException,IOException
			throw new RTMLException(this.getClass().getName()+":init:",e);
		}
	}

	/**
	 * Method to set the RTML version string. This is the value of the RTML elements's 
	 * "version" attribute e.g.: "2.2".
	 * @param s The string to use.
	 * @see #DEFAULT_RTML_VERSION_STRING
	 * @see #rtmlVersionString
	 */
	public void setRTMLVersionString(String s)
	{
		rtmlVersionString = s;
	}

	/**
	 * Method to set the Doctype system ID. This is the URL of the RTML DTD. e.g.:
	 * http://www.estar.org.uk/documents/rtml2.2.dtd
	 * @param s The string to use. This is the URL of the RTML DTD.
	 * @see #DEFAULT_DOCTYPE_SYSTEM_ID
	 * @see #doctypeSystemID
	 */
	public void setDoctypeSystemID(String s)
	{
		doctypeSystemID = s;
	}

	/**
	 * Create an XML representation (DOM tree) from the RTMLDocument (Java object tree).
	 * @param d The Java representation of an RTML document.
	 * @see #createRTML
	 */
	public void create(RTMLDocument d) throws RTMLException
	{
		document = builder.newDocument();
		createRTML(d);
	}

	/**
	 * Method to send the created XML to a stream.
	 * @see #doctypeSystemID
	 */
	public void toStream(OutputStream os) throws RTMLException
	{
		try
		{
			// Use a Transformer for output
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			
			// setup DOCTYPE
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,doctypeSystemID);
			transformer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
			transformer.setOutputProperty(OutputKeys.INDENT,"yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(os);
			transformer.transform(source, result);
		}
		catch (Exception e)
		{
			throw new RTMLException(this.getClass().getName()+":toStream:Failed.",e);
		}
	}

	/**
	 * Method to send the created XML to a string.
	 * @return A string.
	 * @see #doctypeSystemID
	 */
	public String toXMLString() throws RTMLException
	{
		StringWriter stringWriter = null;

		try
		{
			// Use a Transformer for output
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			
			// setup DOCTYPE
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,doctypeSystemID);
			transformer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
			transformer.setOutputProperty(OutputKeys.INDENT,"yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(document);

			stringWriter = new StringWriter();
			StreamResult result = new StreamResult(new BufferedWriter(stringWriter));
			transformer.transform(source, result);
		}
		catch (Exception e)
		{
			throw new RTMLException(this.getClass().getName()+":toStream:Failed.",e);
		}
		return stringWriter.toString();
	}

	/**
	 * Method to set a custom error handler. Should be called after init (constructor), before create is called.
	 * @param e The error handler to use. Must be a sub-class of RTMLErrorHandler, this
	 *        should really be org.xml.sax.ErrorHandler.
	 * @see #errorHandler
	 * @see #builder
	 */
	public void setErrorHandler(RTMLErrorHandler e)
	{
		errorHandler = e;
		builder.setErrorHandler(errorHandler);
	}

	/**
	 * Method to get errorHandler assocated with this parser.
	 * @return The current error handler.
	 * @see #errorHandler
	 */
	public RTMLErrorHandler getErrorHandler()
	{
		return errorHandler;
	}

	// private methods
	/**
	 * Initialisation method.
	 * @exception ParserConfigurationException Thrown if DocumentBuilderFactory.newDocumentBuilder fails.
	 * @see #builder
	 * @see #errorHandler
	 */
	private void init() throws ParserConfigurationException
	{
		DocumentBuilderFactory factory = null;

		factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);   
		factory.setNamespaceAware(true);
		builder = factory.newDocumentBuilder();
		errorHandler = new RTMLErrorHandler(this);
		builder.setErrorHandler(errorHandler);
	}

	/**
	 * Create RTML element, and add to document.
	 * @param d The Java representation of an RTML document.
	 * @see #rtmlVersionString
	 * @see #document
	 * @see #createContact
	 * @see #createProject
	 * @see #createTelescope
	 * @see #createIntelligentAgent
	 * @see #createDevice
	 * @see #createObservation
	 * @see #createScore
	 * @see #createCompletionTime
	 */
	private void createRTML(RTMLDocument d)
	{
		RTMLObservation obs = null;
		Element rtmlElement = null;

		rtmlElement = (Element)document.createElement("RTML"); 
		document.appendChild(rtmlElement);
		rtmlElement.setAttribute("version",rtmlVersionString);
		rtmlElement.setAttribute("type",d.getType());
		if(d.getContact() != null)
			createContact(rtmlElement,d.getContact());
		if(d.getProject() != null)
			createProject(rtmlElement,d.getProject());
		createTelescope(rtmlElement);
		if(d.getIntelligentAgent() != null)
			createIntelligentAgent(rtmlElement,d.getIntelligentAgent());
		if(d.getDevice() != null)
			createDevice(rtmlElement,d.getDevice());
		for(int i = 0; i < d.getObservationListCount(); i++)
		{
			obs = d.getObservation(i);
			createObservation(rtmlElement,obs);
		}
		if(d.getScore() != null)
			createScore(rtmlElement,d.getScore());
		if(d.getScoresListCount() > 0)
			createScores(rtmlElement,d);
		if(d.getCompletionTime() != null)
		{
			createCompletionTime(rtmlElement,d.getCompletionTime());
		}
		if((d.getType().equals("reject") || d.getType().equals("fail") || d.getType().equals("abort")) && 
		   (d.getErrorString() != null))
			rtmlElement.appendChild(document.createTextNode(d.getErrorString()));
	}

	/**
	 * Create a contact node and associated sub-elements.
	 * @param rtmlElement The RTML DOM element to add the Contact tag to.
	 * @param contact The Java object containing the contact data to add.
	 * @see org.estar.rtml.RTMLContact
	 */
	private void createContact(Element rtmlElement, RTMLContact contact)
	{
		Element contactElement = null;
		Element subElement = null;

		contactElement = (Element)document.createElement("Contact");
		if(contact.getName() != null)
		{
			subElement = (Element)document.createElement("Name");
			subElement.appendChild(document.createTextNode(contact.getName()));
			contactElement.appendChild(subElement);
		}
		if(contact.getUser() != null)
		{
			subElement = (Element)document.createElement("User");
			subElement.appendChild(document.createTextNode(contact.getUser()));
			contactElement.appendChild(subElement);
		}
		if(contact.getInstitution() != null)
		{
			subElement = (Element)document.createElement("Institution");
			subElement.appendChild(document.createTextNode(contact.getInstitution()));
			contactElement.appendChild(subElement);
		}
		if(contact.getAddress() != null)
		{
			subElement = (Element)document.createElement("Address");
			subElement.appendChild(document.createTextNode(contact.getAddress()));
			contactElement.appendChild(subElement);
		}
		if(contact.getTelephone() != null)
		{
			subElement = (Element)document.createElement("Telephone");
			subElement.appendChild(document.createTextNode(contact.getTelephone()));
			contactElement.appendChild(subElement);
		}
		if(contact.getFax() != null)
		{
			subElement = (Element)document.createElement("Fax");
			subElement.appendChild(document.createTextNode(contact.getFax()));
			contactElement.appendChild(subElement);
		}
		if(contact.getEmail() != null)
		{
			subElement = (Element)document.createElement("Email");
			subElement.appendChild(document.createTextNode(contact.getEmail()));
			contactElement.appendChild(subElement);
		}
		if(contact.getUrl() != null)
		{
			subElement = (Element)document.createElement("Url");
			subElement.appendChild(document.createTextNode(contact.getUrl().toString()));
			contactElement.appendChild(subElement);
		}
		rtmlElement.appendChild(contactElement);
	}

	/**
	 * Create RTML Project node.
	 * @param rtmlElement The RTML DOM element to add the Project tag to.
	 * @param project The Java object containing the project data to add.
	 * @see org.estar.rtml.RTMLProject
	 */
	private void createProject(Element rtmlElement,RTMLProject project)
	{
		Element projectElement = null;

		projectElement = (Element)document.createElement("Project");
		if(project.getProject() != null)
			projectElement.appendChild(document.createTextNode(project.getProject()));
		rtmlElement.appendChild(projectElement);
	}

	private void createTelescope(Element rtmlElement)
	{
		Element e = null;

		e = (Element)document.createElement("Telescope");
		rtmlElement.appendChild(e);
	}

	private void createIntelligentAgent(Element rtmlElement,RTMLIntelligentAgent rtmlIA)
	{
		Element iaElement = null;

		iaElement = (Element)document.createElement("IntelligentAgent");
		if(rtmlIA.getHostname() != null)
			iaElement.setAttribute("host",rtmlIA.getHostname());
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
	 */
	private void createDevice(Element rtmlElement,RTMLDevice device)
	{
		Element deviceElement = null;
		Element filterElement = null;
		Element filterTypeElement = null;

		deviceElement = (Element)document.createElement("Device");
		if(device.getType() != null)
			deviceElement.setAttribute("type",device.getType());
		if(device.getSpectralRegion() != null)
			deviceElement.setAttribute("region",""+device.getSpectralRegion());
		// filter type sub-element(s)
		if(device.getFilterType() != null)
		{
			// filter node/tag
			filterElement = (Element)document.createElement("Filter");
			deviceElement.appendChild(filterElement);
			// filter type node/tag
			filterTypeElement = (Element)document.createElement("FilterType");
			filterElement.appendChild(filterTypeElement);
			// filter type node/tag
			if(device.getFilterType() != null)
				filterTypeElement.appendChild(document.createTextNode(device.getFilterType()));
		}
		if(device.getDetector() != null)
			createDetector(deviceElement,device.getDetector());
		if(device.getName() != null)
			deviceElement.appendChild(document.createTextNode(device.getName()));
		rtmlElement.appendChild(deviceElement);
	}

	/**
	 * Method to create the Detector tags.
	 * Create a detector node and associated sub-elements.
	 * @param rtmlElement The RTML DOM element to add the Detector tag to.
	 * @param detector The Java object containing the detector (instrument) data to add.
	 * @see org.estar.rtml.RTMLDetector
	 * @see #createDetector
	 */
	private void createDetector(Element rtmlElement,RTMLDetector detector)
	{
		Element detectorElement = null;
		Element binningElement = null;

		detectorElement = (Element)document.createElement("Detector");
		// binning node/tag
		binningElement = (Element)document.createElement("Binning");
		detectorElement.appendChild(binningElement);
		binningElement.setAttribute("rows",""+detector.getRowBinning());
		binningElement.setAttribute("columns",""+detector.getColumnBinning());
		rtmlElement.appendChild(detectorElement);
	}


	/**
	 * Method to create XML in the Observation tag.
	 * @param rtmlElement The RTML element to add the Observation tag to.
	 * @param observation The Java object containing the observation data to add.
	 * @see #document
	 * @see #createTarget
	 * @see #createDevice
	 * @see #createSchedule
	 * @see #createImageData
	 */
	private void createObservation(Element rtmlElement,RTMLObservation observation)
	{
		Element observationElement = null;
		RTMLImageData imageData = null;

		observationElement = (Element)document.createElement("Observation");
		if(observation.getTarget() != null)
			createTarget(observationElement,observation.getTarget());
		if(observation.getDevice() != null)
			createDevice(observationElement,observation.getDevice());
		if(observation.getSchedule() != null)
			createSchedule(observationElement,observation.getSchedule());
		for(int i = 0; i < observation.getImageDataCount(); i++)
		{
			imageData = observation.getImageData(i);
			createImageData(observationElement,imageData);
		}
		rtmlElement.appendChild(observationElement);
	}

	/**
	 * Create a Target element and append it to the specified Observation element.
	 * @param observationElement The observation to append the new Target element to.
	 * @param target The RTMLTarget object containing target information.
	 * @see RTMLTarget
	 */
	private void createTarget(Element observationElement,RTMLTarget target)
	{
		Element targetElement = null;
		Element nameElement = null;
		Element coordElement = null;
		Element raElement = null;
		Element decElement = null;
		Element equinoxElement = null;

		// target element
		targetElement = (Element)document.createElement("Target");
		// type attribute
		targetElement.setAttribute("type",target.getType());
		// ident attribute
		if(target.getIdent() != null)
			targetElement.setAttribute("ident",target.getIdent());
		// target name sub-element
		if(target.getName() != null)
		{
			nameElement = (Element)document.createElement("TargetName");
			nameElement.appendChild(document.createTextNode(target.getName()));
			targetElement.appendChild(nameElement);
		}
		// coordinate sub-element
		coordElement = (Element)document.createElement("Coordinates");
		// add coordinate sub-element to target element
		targetElement.appendChild(coordElement);
		// ra sub-sub element
		if(target.getRA() != null)
		{
			raElement = (Element)document.createElement("RightAscension");
			raElement.appendChild(document.createTextNode(target.getRA().toString(' ')));
			raElement.setAttribute("units","hms");
			raElement.setAttribute("format","hh mm ss.ss");
			// add ra element to coordElement.
			coordElement.appendChild(raElement);
		}
		// dec sub-sub element
		if(target.getDec() != null)
		{
			decElement = (Element)document.createElement("Declination");
			decElement.appendChild(document.createTextNode(target.getDec().toString(' ')));
			decElement.setAttribute("units","dms");
			decElement.setAttribute("format","sdd mm ss.ss");
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
		// add target to observation
		observationElement.appendChild(targetElement);
		
	}

	/**
	 * Create a Schedule tag.
	 * @param observationElement The observation XML node to add the schedule to.
	 * @param schedule The RTML schedule data.
	 * @see #createTimeConstraint
	 * @see #createSeriesConstraint
	 * @see #createSeeingConstraint
	 * @see RTMLSchedule
	 */
	private void createSchedule(Element observationElement,RTMLSchedule schedule)
	{
		Element scheduleElement = null;
		Element exposureElement = null;
		Element exposureCountElement = null;

		// schedule element
		scheduleElement = (Element)document.createElement("Schedule");
		scheduleElement.setAttribute("priority",""+schedule.getPriority());
		// exposure element
		exposureElement = (Element)document.createElement("Exposure");
		if(schedule.getExposureType() != null)
			exposureElement.setAttribute("type",schedule.getExposureType());
		if(schedule.getExposureUnits() != null)
			exposureElement.setAttribute("units",schedule.getExposureUnits());
		// exposure count element
		if(schedule.getExposureCount() != 1)
		{
			exposureCountElement = (Element)document.createElement("Count");
			exposureCountElement.appendChild(document.createTextNode(""+schedule.getExposureCount()));
			exposureElement.appendChild(exposureCountElement);
		}
		exposureElement.appendChild(document.createTextNode(""+schedule.getExposureLength()));
		// add exposure to a schedule
		scheduleElement.appendChild(exposureElement);
		// TimeConstraint
		if((schedule.getStartDate() != null) || (schedule.getEndDate() != null))
			createTimeConstraint(scheduleElement,schedule);
		// SeriesConstraint
		if(schedule.getSeriesConstraint() != null)
			createSeriesConstraint(scheduleElement,schedule.getSeriesConstraint());
		// SeeingConstraint
		if(schedule.getSeeingConstraint() != null)
			createSeeingConstraint(scheduleElement,schedule.getSeeingConstraint());
		// add schedule to a observation
		observationElement.appendChild(scheduleElement);		
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
		timeConstraintElement = (Element)document.createElement("TimeConstraint");
		// start date time element
		if(schedule.getStartDate() != null)
		{
			dateTimeElement = (Element)document.createElement("StartDateTime");
			dateTimeElement.appendChild(document.createTextNode(
					     dateFormat.format(schedule.getStartDate())));
			// add dateTimeElement to a timeConstraintElement
			timeConstraintElement.appendChild(dateTimeElement);
		}
		// end date time element
		if(schedule.getEndDate() != null)
		{
			dateTimeElement = (Element)document.createElement("EndDateTime");
			dateTimeElement.appendChild(document.createTextNode(
					     dateFormat.format(schedule.getEndDate())));
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
		// add seeingConstraintElement to a scheduleElement
		scheduleElement.appendChild(seeingConstraintElement);		
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
		Element objectListElement = null;
		String imageDataType = null;
		String s = null;

		imageDataElement = (Element)document.createElement("ImageData");
		// FITS header
		if(imageData.getFITSHeader() != null)
		{
			fitsHeaderElement = (Element)document.createElement("FITSHeader");
			fitsHeaderElement.setAttribute("type","all");// all fits headers
			fitsHeaderElement.appendChild(document.createTextNode(imageData.getFITSHeader()));
			imageDataElement.appendChild(fitsHeaderElement);
		}
		// ObjectList types
		// Cluster
		if(imageData.isObjectListTypeCluster())
		{
			objectListElement = (Element)document.createElement("ObjectList");
			objectListElement.appendChild(document.createTextNode(imageData.getObjectListCluster()));
			objectListElement.setAttribute("type",imageData.getObjectListType());
			objectListElement.setAttribute("format",
					     " fn sn rah ram ras decd decm decs xpos ypos mag magerror magflag");
			imageDataElement.appendChild(objectListElement);
		}
		// VOTableURL
		if(imageData.isObjectListTypeVOTableURL())
		{
			objectListElement = (Element)document.createElement("ObjectList");
			objectListElement.appendChild(document.createTextNode(imageData.getObjectListVOTableURL().
									      toString()));
			objectListElement.setAttribute("type",imageData.getObjectListType());
			imageDataElement.appendChild(objectListElement);
		}
		// get image data type
		imageDataType = imageData.getImageDataType();
		// if URL is present, add as text node
		if(imageData.getImageDataURL() != null)
		{
			s = imageData.getImageDataURL().toString();
			imageDataElement.appendChild(document.createTextNode(s));
			// if no imageDataType has been set, we can deduce this from the end of the URL
			if(imageDataType == null)
			{
				if(s.endsWith(".fits"))
					imageDataType = new String("FITS16");
				else if(s.endsWith(".jpg"))
					imageDataType = new String("jpg");
			}
		}
		if(imageDataType != null)
			imageDataElement.setAttribute("type",imageDataType);
		imageDataElement.setAttribute("delivery","url");
		imageDataElement.setAttribute("reduced","true");
		rtmlElement.appendChild(imageDataElement);
	}

	/**
	 * Create a score element.
	 * @param rtmlElement The RTML document element to put the score element in.
	 * @param score A Double object. This method assumes it is non-null.
	 */
	private void createScore(Element rtmlElement,Double score)
	{
		Element e = null;
		DecimalFormat df = null;

		df = new DecimalFormat("#0.0#####");
		e = (Element)document.createElement("Score");
		e.appendChild(document.createTextNode(df.format(score.doubleValue())));
		rtmlElement.appendChild(e);
	}

	/**
	 * Create a Scores element, and Score sub-elements.
	 * @param rtmlElement The RTML document element to put the score element in.
	 * @param rtmlDocument The RTML document
	 * @see RTMLDocument#getScoresListCount
	 * @see RTMLDocument#getScore
	 * @see RTMLScore#getDelay
	 * @see RTMLScore#getProbability
	 * @see RTMLScore#getCumulative
	 */
	private void createScores(Element rtmlElement,RTMLDocument rtmlDocument)
	{
		RTMLScore score = null;
		Element scoresElement = null;
		Element scoreElement = null;
		DecimalFormat df = null;

		df = new DecimalFormat("#####0.0#");
		// Create Scores element
		scoresElement = (Element)document.createElement("Scores");
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
			// Add score to Scores list
			scoresElement.appendChild(scoreElement);

		}
		// Add Scores to RTML
		rtmlElement.appendChild(scoresElement);
	}

	/**
	 * Create the completion time node.
	 * @param rtmlElement The RTML document element to put the completion time element in.
	 * @param completionTime The date to use.
	 * @see RTMLDateFormat
	 */
	private void createCompletionTime(Element rtmlElement,Date completionTime)
	{
		Element e = null;
		String dateString = null;
		RTMLDateFormat dateFormat = null;

		dateFormat = new RTMLDateFormat();
		e = (Element)document.createElement("CompletionTime");
		dateString = dateFormat.format(completionTime);
		e.appendChild(document.createTextNode(dateString));
		rtmlElement.appendChild(e);
	}

}
/*
** $Log: not supported by cvs2svn $
** Revision 1.35  2007/03/27 19:14:11  cjm
** Added createScores.
**
** Revision 1.34  2007/01/30 18:31:09  cjm
** gnuify: Added GNU General Public License.
**
** Revision 1.33  2006/03/20 16:21:52  cjm
** Updated creation od date-time nodes to use the RTMLDateFormat formatter.
**
** Revision 1.32  2005/08/19 17:01:06  cjm
** Added VOTable URL support to ImageData.
**
** Revision 1.31  2005/06/20 10:55:05  cjm
** Error string now created for documents of type fail and abort, as well as reject.
**
** Revision 1.30  2005/06/08 13:58:24  cjm
** Added createSeeingConstraint.
**
** Revision 1.29  2005/06/08 11:36:55  cjm
** Fixed comments.
**
** Revision 1.28  2005/06/06 10:33:29  cjm
** Added Schedule priority.
**
** Revision 1.27  2005/06/01 16:30:22  cjm
** Added FITS header type to all or cluster.
**
** Revision 1.26  2005/05/04 18:59:45  cjm
** Changed creation of ImageData, ObjectList and FITSHeader nodes.
**
** Revision 1.25  2005/04/29 17:18:41  cjm
** Added exposureCount.
**
** Revision 1.24  2005/04/28 09:43:14  cjm
** Changed createScore to reflect the fact score is now an object,
** so we can represent the lack of a score node.
**
** Revision 1.23  2005/04/27 15:43:27  cjm
** Added createSeriesConstraint.
**
** Revision 1.22  2005/04/26 15:02:59  cjm
** Added setDoctypeSystemID and setRTMLVersionString, so we can override the defaults.
**
** Revision 1.21  2005/04/26 11:27:38  cjm
** Added createTimeConstraint.
**
** Revision 1.20  2005/04/25 10:32:18  cjm
** Added creation of Target ident attribute.
**
** Revision 1.19  2005/01/19 12:06:21  cjm
** Reordered Observation node creation to match DTD - apparently this is important.
**
** Revision 1.18  2005/01/18 19:37:58  cjm
** FilterType now has type as PCDATA rather than an attribute.
**
** Revision 1.17  2005/01/18 15:31:23  cjm
** Added null protection for project.
**
** Revision 1.16  2005/01/18 15:24:54  cjm
** Added project element details.
**
** Revision 1.15  2004/03/18 17:33:53  cjm
** Changed creation of <ImageData> node. Now created if image data type, or image data url,
** is present. Either can be null.
**
** Revision 1.14  2004/03/15 12:24:08  je
** Changed http://www.estar.org.uk/estar/documents/rtml2.1.dtd for http://www.astro.livjm.ac.uk/~je/rtml2.1.dtd
**
** Revision 1.13  2004/03/12 19:55:42  cjm
** Fixed Device tag. Can have a Device tag with no (null) name.
**
** Revision 1.12  2004/03/12 18:29:48  cjm
** Made createContact create sub-elemetn tags correctly.
**
** Revision 1.11  2004/03/12 17:15:52  cjm
** reformatted.
**
** Revision 1.10  2004/03/12 11:01:28  je
** Added imports and fixed typo
**
** Revision 1.9  2004/03/12 10:56:04  je
** Added RTMLContact node creation
**
** Revision 1.8  2004/03/11 15:54:19  cjm
** Tried to format output RTML.
**
** Revision 1.7  2004/03/11 13:52:17  cjm
** Removed test:
** if(g.getType != "score")
**
** around "createScore" call.
**
** This made sense when RTMLCreate was only used to create IA documents (i.e. IA should not create
** <Score> tag for "score" requests). However, now RTMLCreate is used by LT/FT IA, we need a "score"
** document to contain a <Score> tag for "score" replies. Will this break "score" request documents
** from the MPIA IA to Meade ers DNs?
**
** Revision 1.6  2004/03/11 13:25:21  cjm
** Fixed = bug.
**
** Revision 1.5  2004/03/11 13:10:31  cjm
** Protected IntelligentAgent alements against null.
**
** Revision 1.4  2004/03/11 13:06:34  cjm
** Added Device/Filter creation.
**
** Revision 1.3  2004/03/10 12:00:08  cjm
** Added error string creation.
**
** Revision 1.2  2004/03/10 11:43:44  cjm
** Added createImageData.
**
** Revision 1.1  2003/02/24 13:19:56  cjm
** Initial revision
**
*/
