// RTMLCreate.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLCreate.java,v 1.7 2004-03-11 13:52:17 cjm Exp $
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
 * @author Chris Mottram
 * @version $Revision: 1.7 $
 */
public class RTMLCreate
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLCreate.java,v 1.7 2004-03-11 13:52:17 cjm Exp $";
	/**
	 * RTML version attribute constant string (2.1) for eSTAR documents.
	 */
	public final static String RTML_VERSION_STRING = "2.1";
	/**
	 * System ID put into DOCTYPE statement. This is the URL of the RTML DTD.
	 */
	public final static String DOCTYPE_SYSTEM_ID = "http://150.204.240.111/~dev/robonet/rtml2.1.dtd";
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
	 * @see #DOCTYPE_SYSTEM_ID
	 */
	public void toStream(OutputStream os) throws RTMLException
	{
		try
		{
			// Use a Transformer for output
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			
			// setup DOCTYPE
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,DOCTYPE_SYSTEM_ID);
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
	 * @see #DOCTYPE_SYSTEM_ID
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
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,DOCTYPE_SYSTEM_ID);
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
	 * @param RTMLErrorHandler The error handler to use. Must be a sub-class of RTMLErrorHandler, this
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
	 * @see #RTML_VERSION_STRING
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
		rtmlElement.setAttribute("version",RTML_VERSION_STRING);
		rtmlElement.setAttribute("type",d.getType());
		createContact(rtmlElement);
		createProject(rtmlElement);
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
		createScore(rtmlElement,d.getScore());
		if(d.getCompletionTime() != null)
		{
			createCompletionTime(rtmlElement,d.getCompletionTime());
		}
		if((d.getType().equals("reject")) && (d.getErrorString() != null))
			rtmlElement.appendChild(document.createTextNode(d.getErrorString()));
	}

	private void createContact(Element rtmlElement)
	{
		Element e = null;

		e = (Element)document.createElement("Contact");
		rtmlElement.appendChild(e);
	}

	private void createProject(Element rtmlElement)
	{
		Element e = null;

		e = (Element)document.createElement("Project");
		rtmlElement.appendChild(e);
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
		deviceElement.appendChild(document.createTextNode(device.getName()));
		// filter type sub-element(s)
		if(device.getFilterType() != null)
		{
			// filter node/tag
			filterElement = (Element)document.createElement("Filter");
			deviceElement.appendChild(filterElement);
			// filter type node/tag
			filterTypeElement = (Element)document.createElement("FilterType");
			filterTypeElement.setAttribute("type",device.getFilterType());
			filterElement.appendChild(filterTypeElement);
		}
		rtmlElement.appendChild(deviceElement);
	}

	/**
	 * Method to create XML in the Observation tag.
	 * @param rtmlElement The RTML element to add the Observation tag to.
	 * @param observation The Java object containing the observation data to add.
	 * @see #document
	 * @see #createTarget
	 * @see #createSchedule
	 * @see #createImageData
	 */
	private void createObservation(Element rtmlElement,RTMLObservation observation)
	{
		Element observationElement = null;

		observationElement = (Element)document.createElement("Observation");
		if(observation.getTarget() != null)
			createTarget(observationElement,observation.getTarget());
		if(observation.getSchedule() != null)
			createSchedule(observationElement,observation.getSchedule());
		if(observation.getImageDataURL() != null)
			createImageData(observationElement,observation.getImageDataURL());
		rtmlElement.appendChild(observationElement);
	}

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

	private void createSchedule(Element observationElement,RTMLSchedule schedule)
	{
		Element scheduleElement = null;
		Element exposureElement = null;

		// schedule element
		scheduleElement = (Element)document.createElement("Schedule");
		// exposure element
		exposureElement = (Element)document.createElement("Exposure");
		if(schedule.getExposureType() != null)
			exposureElement.setAttribute("type",schedule.getExposureType());
		if(schedule.getExposureUnits() != null)
			exposureElement.setAttribute("units",schedule.getExposureUnits());
		exposureElement.appendChild(document.createTextNode(""+schedule.getExposureLength()));
		// add exposure to a schedule
		scheduleElement.appendChild(exposureElement);
		// add schedule to a observation
		observationElement.appendChild(scheduleElement);		
	}

	private void createImageData(Element rtmlElement,URL imageDataURL)
	{
		Element imageDataElement = null;
		String s = null;

		imageDataElement = (Element)document.createElement("ImageData");
		s = imageDataURL.toString();
		imageDataElement.appendChild(document.createTextNode(s));
		if(s.endsWith(".fits"))
			imageDataElement.setAttribute("type","FITS16");
		else if(s.endsWith(".jpg"))
			imageDataElement.setAttribute("type","jpg");
		imageDataElement.setAttribute("delivery","url");
		imageDataElement.setAttribute("reduced","true");
		rtmlElement.appendChild(imageDataElement);
	}

	private void createScore(Element rtmlElement,double score)
	{
		Element e = null;

		e = (Element)document.createElement("Score");
		e.appendChild(document.createTextNode(""+score));
		rtmlElement.appendChild(e);
	}

	private void createCompletionTime(Element rtmlElement,Date completionTime)
	{
		Element e = null;
		String dateString = null;
		DateFormat dateFormat = null;

		dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		e = (Element)document.createElement("CompletionTime");
		dateString = dateFormat.format(completionTime);
		e.appendChild(document.createTextNode(dateString));
		rtmlElement.appendChild(e);
	}

}
/*
** $Log: not supported by cvs2svn $
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
