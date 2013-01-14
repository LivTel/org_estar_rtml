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
// RTML31Parser.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTML31Parser.java,v 1.8 2013-01-14 11:04:37 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;
import java.util.*;

import java.net.URL;
import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.FactoryConfigurationError;  
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;  
import org.xml.sax.SAXParseException;  

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.DOMException;

import org.estar.astrometry.*;

/**
 * This class provides the capability of parsing an RTML3.1 document into a DOM tree, using JAXP.
 * The resultant DOM tree is traversed, and relevant eSTAR data extracted.
 * Extends RTMLParser to make use of methods common to this and RTML22Parser (parseIntegerNode etc), even though
 * this subclass is created as part of the RTMLParser's parsing.
 * @author Chris Mottram
 * @version $Revision: 1.8 $
 */
public class RTML31Parser extends RTMLParser
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTML31Parser.java,v 1.8 2013-01-14 11:04:37 cjm Exp $";

	/**
	 * Default constructor.
	 */
	public RTML31Parser()
	{
		super();
	}

	/**
	 * Method to parse the RTML node.
	 * @param rtmlNode The XML DOM node for the RTML tag node.
	 * @param rtmlDocument The RTMLDocument instance to fill with the parsed data.
	 * @return An instance of RTMLDocument, containing the data in the document.
	 * @exception RTMLException Thrown if a strange child is in the node, 
	 *            or the score/completion time fails to parse.
	 * @exception ParseException Thrown if parsing the time stamp fails.
	 * @see RTMLDocument#setVersion
	 * @see RTMLDocument#setMode
	 * @see RTMLIntelligentAgent
	 * @see #parseDeviceNode
	 * @see #parseScheduleNode
	 * @see #parseScoringNode
	 * @see #parseProjectNode
	 * @see #parseHistoryNode
	 * @see #parseTelescopeNode
	 */
	protected void parseRTMLNode(Node rtmlNode,RTMLDocument rtmlDocument) throws RTMLException, 
											   ParseException
	{
		RTMLObservation observation = null;
		RTMLIntelligentAgent intelligenAgent = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String uid = null;
		String version = null;
		String mode = null;
		String uriString = null;

		// check current XML node is correct
		if(rtmlNode.getNodeType() != Node.ELEMENT_NODE)
			throw new RTMLException(this.getClass().getName()+":parseRTMLNode:Illegal Node:"+rtmlNode);
		if(rtmlNode.getNodeName() != "RTML")
		{
			throw new RTMLException(this.getClass().getName()+":parseRTMLNode:Illegal Node Name:"+
						rtmlNode.getNodeName());
		}
		// go through attribute list
		attributeList = rtmlNode.getAttributes();
		// check version
		attributeNode = attributeList.getNamedItem("version");
		version = attributeNode.getNodeValue();
		if(version == null)
			throw new RTMLException(this.getClass().getName()+":parseRTMLNode:Version was null.");
		rtmlDocument.setVersion(version);
		if(version.equals(RTMLDocument.RTML_VERSION_31) == false)
		{
			throw new RTMLException(this.getClass().getName()+":parseRTMLNode:Unsupported Version:"+
						version);
		}
		// uid
		attributeNode = attributeList.getNamedItem("uid");
		uid = attributeNode.getNodeValue();
		rtmlDocument.setUId(uid);
		// mode = type in RTML 2.2
		attributeNode = attributeList.getNamedItem("mode");
		mode = attributeNode.getNodeValue();
		rtmlDocument.setMode(mode);
		// RTML 3.1 has Observation inside Schedule!
		//observation = new RTMLObservation();
		//rtmlDocument.addObservation(observation);
		// go through child nodes
		childList = rtmlNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{

				if(childNode.getNodeName() == "Device")
					parseDeviceNode(rtmlDocument,childNode);
				else if(childNode.getNodeName() == "History")
					parseHistoryNode(rtmlDocument,childNode);
				else if(childNode.getNodeName() == "Project")
					parseProjectNode(rtmlDocument,childNode);
				else if(childNode.getNodeName() == "RespondTo")
				{
					uriString = parseStringNode("RespondTo",childNode);
					intelligenAgent = new RTMLIntelligentAgent();
					intelligenAgent.setUri(uriString);
					rtmlDocument.setIntelligentAgent(intelligenAgent);
				}
				else if(childNode.getNodeName() == "Schedule")
					parseScheduleNode(rtmlDocument,childNode);
				else if(childNode.getNodeName() == "Scoring")
					parseScoringNode(rtmlDocument,childNode);
				else if(childNode.getNodeName() == "Target")
					parseTargetNode(rtmlDocument,childNode);
				else if(childNode.getNodeName() == "Telescope")
					parseTelescopeNode(rtmlDocument,childNode);
				else
					System.err.println("parseRTMLNode:ELEMENT:"+childNode);
			}
			else if(childNode.getNodeType() == Node.TEXT_NODE)
			{
			}
		}
	}

	// private methods
	/**
	 * Internal method to parse a History node.
	 * @param rtmlDocument The document to add the History to.
	 * @param historyNode The XML DOM node for the History tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @exception ParseException Thrown if parsing the time stamp fails.
	 */
	private void parseHistoryNode(RTMLDocument rtmlDocument,Node historyNode) throws RTMLException, ParseException
	{
		Node childNode;
		NodeList childList;

		// check current XML node is correct
		if(historyNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseHistoryNode:Illegal Node:"+
					        historyNode);
		}
		if(historyNode.getNodeName() != "History")
		{
			throw new RTMLException(this.getClass().getName()+":parseHistoryNode:Illegal Node Name:"+
						historyNode.getNodeName());
		}
		// add history node
		RTMLHistory history = new RTMLHistory();
		// go through child nodes
		childList = historyNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Entry")
					parseHistoryEntryNode(history, childNode);
				else
					System.err.println("parseHistoryNode:ELEMENT:"+childNode);
			}
		}
		// Set history in RTML document.
		rtmlDocument.setHistory(history);
	}

	/**
	 * Internal method to parse a History Entry node.
	 * @param history The History to add the Entry to.
	 * @param entryNode The XML DOM node for the Entry tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @exception ParseException Thrown if parsing the time stamp fails.
	 * @see #parseStringNode
	 * @see #parseIntegerNode
	 * @see #parseRejectionNode
	 */
	private void parseHistoryEntryNode(RTMLHistory history,Node entryNode) throws RTMLException, ParseException
	{
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		RTMLHistoryEntry entry = null;
		String timeStamp = null;

		// check current XML node is correct
		if(entryNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseHistoryEntryNode:Illegal Node:"+
					        entryNode);
		}
		if(entryNode.getNodeName() != "Entry")
		{
			throw new RTMLException(this.getClass().getName()+":parseHistoryEntryNode:Illegal Node Name:"+
						entryNode.getNodeName());
		}
		// create entry node
		entry = new RTMLHistoryEntry();
		// go through attribute list
		attributeList = entryNode.getAttributes();
		// timeStamp
		attributeNode = attributeList.getNamedItem("timeStamp");
		timeStamp = attributeNode.getNodeValue();
		entry.setTimeStamp(timeStamp);
		// go through child nodes
		childList = entryNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Agent")
				{
					parseAgentNode(entry,childNode);
				}
				else if(childNode.getNodeName() == "Description")
					entry.setDescription(parseStringNode("Description",childNode));
				else if(childNode.getNodeName() == "Error")
					entry.setError(parseStringNode("Error",childNode));
				else if(childNode.getNodeName() == "Rejection")
					parseRejectionNode(entry,childNode);
				else if(childNode.getNodeName() == "Version")
					entry.setVersion(parseIntegerNode(childNode));
				else
					System.err.println("parseHistoryEntryNode:ELEMENT:"+childNode);
			}
		}
		// add entry to history
		history.addEntry(entry);
	}

	/**
	 * Internal method to parse an Agent node.
	 * @param entry The history entry to add the agent to.
	 * @param agentNode The XML DOM node for the Agent tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 */
	private void parseAgentNode(RTMLHistoryEntry entry,Node agentNode) 
		throws RTMLException
	{
		RTMLIntelligentAgent intelligentAgent = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;

		// check current XML node is correct
		if(agentNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseAgentNode:Illegal Node:"+
						agentNode);
		}
		if(agentNode.getNodeName() != "Agent")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseAgentNode:Illegal Node Name:"+
						agentNode.getNodeName());
		}
		// add intelligentAgent node
		intelligentAgent = new RTMLIntelligentAgent();
		// go through attribute list
		attributeList = agentNode.getAttributes();
		// name
		attributeNode = attributeList.getNamedItem("name");
		if(attributeNode != null)
			intelligentAgent.setId(attributeNode.getNodeValue());
		else
			intelligentAgent.setId(null);
		// uri
		attributeNode = attributeList.getNamedItem("uri");
		if(attributeNode != null)
			intelligentAgent.setUri(attributeNode.getNodeValue());
		else
			intelligentAgent.setUri(null);
		// hostname/port not used in RTML3.1
		intelligentAgent.setHostname(null);
		intelligentAgent.setPort(0);
		// set intelligentAgent in history entry.
		entry.setAgent(intelligentAgent);
	}

	/**
	 * Internal method to parse an Rejection node.
	 * @param entry The history entry to add the rejection information to.
	 * @param rejectionNode The XML DOM node for the Rejection tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @see #parseStringNode
	 * @see org.estar.rtml.RTMLHistoryEntry#setRejectionReason
	 * @see org.estar.rtml.RTMLHistoryEntry#setRejectionDescription
	 */
	private void parseRejectionNode(RTMLHistoryEntry entry,Node rejectionNode) throws RTMLException
	{
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;

		// check current XML node is correct
		if(rejectionNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseRejectionNode:Illegal Node:"+
						rejectionNode);
		}
		if(rejectionNode.getNodeName() != "Rejection")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseRejectionNode:Illegal Node Name:"+
						rejectionNode.getNodeName());
		}
		// go through attribute list
		attributeList = rejectionNode.getAttributes();
		// name
		attributeNode = attributeList.getNamedItem("reason");
		if(attributeNode != null)
			entry.setRejectionReason(attributeNode.getNodeValue());
		// go through child nodes
		childList = rejectionNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Description")
					entry.setRejectionDescription(parseStringNode("Description",childNode));
				else
					System.err.println("parseRejectionNode:ELEMENT:"+childNode);
			}
		}
	}

	/**
	 * Internal method to parse an Contact node. In RTML 3.1a, these are contained within a Project node,
	 * but as the document object model reflects RTML 2.2 the RTMLContact is in the top level RTMLDocument.
	 * @param rtmlDocument The document to add the Contact to.
	 * @param contactNode The XML DOM node for the Contact tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @see #parseRTMLAttributes
	 * @see #parseStringNode
	 * @see #parseNodeAttribute
	 * @see #parseCommunicationNode
	 */
	private void parseContactNode(RTMLDocument rtmlDocument,Node contactNode) throws RTMLException
	{
		NamedNodeMap attributeList = null;
		Node childNode;
		NodeList childList;

		// check current XML node is correct
		if(contactNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseContactNode:Illegal Node:"+
						 contactNode);
		}
		if(contactNode.getNodeName() != "Contact")
		{
			throw new RTMLException(this.getClass().getName()+":parseContactNode:Illegal Node Name:"+
						 contactNode.getNodeName());
		}
		// add contact node
		RTMLContact contact = new RTMLContact();
		// go through attribute list
		attributeList = contactNode.getAttributes();
		// get standard RTML attributes id,ref,uref
		parseRTMLAttributes(contact,attributeList);
		// go through child nodes
		childList = contactNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Username")
					contact.setUser(parseStringNode("Username",childNode));
				else if(childNode.getNodeName() == "Name")
					contact.setName(parseStringNode("Name",childNode));
				else if(childNode.getNodeName() == "Institution")
					contact.setInstitution(parseNodeAttribute("Institution",childNode,"name"));
				else if(childNode.getNodeName() == "Communication")
					parseCommunicationNode(contact, childNode);
				else
					System.err.println("parseContactNode:ELEMENT:"+childNode);
			}
		}
		// Set contact in RTML document.
		rtmlDocument.setContact(contact);
	}

	/**
	 * Internal method to parse a Communication node. 
	 * @param contact The contact to add the communication data to.
	 * @param communicationNode The XML DOM node for the Communication tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @see #parseStringNode
	 */
	private void parseCommunicationNode(RTMLContact contact,Node communicationNode) throws RTMLException
	{
		URL url = null;
		NamedNodeMap attributeList = null;
		Node childNode;
		NodeList childList;
		String urlString = null;

		// check current XML node is correct
		if(communicationNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseCommunicationNode:Illegal Node:"+
						 communicationNode);
		}
		if(communicationNode.getNodeName() != "Communication")
		{
			throw new RTMLException(this.getClass().getName()+":parseCommunicationNode:Illegal Node Name:"+
						 communicationNode.getNodeName());
		}
		// go through child nodes
		childList = communicationNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "AddressLine")
					contact.setAddress(parseStringNode("AddressLine",childNode));
				else if(childNode.getNodeName() == "Telephone")
					contact.setTelephone(parseStringNode("Telephone",childNode));
				else if(childNode.getNodeName() == "Fax")
					contact.setFax(parseStringNode("Fax",childNode));
				else if(childNode.getNodeName() == "Email")
					contact.setEmail(parseStringNode("Email",childNode));
				else if(childNode.getNodeName() == "Uri")
				{
					urlString = parseStringNode("Uri",childNode);
					try
					{
						url = new URL(urlString);
					}
					catch(MalformedURLException e)
					{
						throw new RTMLException(this.getClass().getName()+
									":parseCommunicationNode:Illegal URL ["+
									urlString+"]", e);
					}
					contact.setUrl(url);
				}
				else
					System.err.println("parseCommunicationNode:ELEMENT:"+childNode);
			}
		}
	}

	/**
	 * Internal method to parse an Project node.
	 * @param rtmlDocument The document to add the Project to.
	 * @param projectNode The XML DOM node for the Project tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @see #parseContactNode
	 * @see #parseRTMLAttributes
	 */
	private void parseProjectNode(RTMLDocument rtmlDocument,Node projectNode) throws RTMLException
	{
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;

		// check current XML node is correct
		if(projectNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseProjectNode:Illegal Node:"+
						projectNode);
		}
		if(projectNode.getNodeName() != "Project")
		{
			throw new RTMLException(this.getClass().getName()+":parseProjectNode:Illegal Node Name:"+
						projectNode.getNodeName());
		}
		// add project node
		RTMLProject project = new RTMLProject();
		// go through attribute list
		attributeList = projectNode.getAttributes();
		// get standard RTML attributes id,ref,uref
		parseRTMLAttributes(project,attributeList);
		// name
		attributeNode = attributeList.getNamedItem("ProjectID");
		if(attributeNode != null)
			project.setProject(attributeNode.getNodeValue());
		else
			project.setProject(null);
		// go through child nodes
		childList = projectNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Contact")
					parseContactNode(rtmlDocument, childNode);
				else
					System.err.println("parseProjectNode:ELEMENT:"+childNode);
			}
		}
		// Set project in RTML document.
		rtmlDocument.setProject(project);
	}

	/**
	 * Internal method to parse a Telescope node.
	 * @param rtmlDocument The document to add the Telescope to.
	 * @param telescopeNode The XML DOM node for the Telescope tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @see #parseRTMLAttributes
	 * @see #parseApertureNode
	 * @see #parseStringNode
	 * @see #parseTelescopeLocationNode
	 * @see org.estar.rtml.RTMLTelescope
	 * @see org.estar.rtml.RTMLTelescope#setName
	 * @see org.estar.rtml.RTMLTelescope#setFocalRatio
	 * @see org.estar.rtml.RTMLDocument#setTelescope
	 */
	private void parseTelescopeNode(RTMLDocument rtmlDocument,Node telescopeNode) throws RTMLException
	{
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;

		// check current XML node is correct
		if(telescopeNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseTelescopeNode:Illegal Node:"+
						telescopeNode);
		}
		if(telescopeNode.getNodeName() != "Telescope")
		{
			throw new RTMLException(this.getClass().getName()+":parseTelescopeNode:Illegal Node Name:"+
						telescopeNode.getNodeName());
		}
		// add project node
		RTMLTelescope telescope = new RTMLTelescope();
		// go through attribute list
		attributeList = telescopeNode.getAttributes();
		// get standard RTML attributes id,ref,uref
		parseRTMLAttributes(telescope,attributeList);
		// name
		attributeNode = attributeList.getNamedItem("name");
		if(attributeNode != null)
			telescope.setName(attributeNode.getNodeValue());
		else
			telescope.setName(null);
		// go through child nodes
		childList = telescopeNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Aperture")
					parseApertureNode(telescope,childNode);
				else if(childNode.getNodeName() == "FocalLength")
					parseFocalLengthNode(telescope,childNode);
				else if(childNode.getNodeName() == "FocalRatio")
					telescope.setFocalRatio(parseStringNode("FocalRatio",childNode));
				else if(childNode.getNodeName() == "Location")
					parseTelescopeLocationNode(telescope,childNode);
				else
					System.err.println("parseTelescopeNode:ELEMENT:"+childNode);
			}
		}
		// Set telescope in RTML document.
		rtmlDocument.setTelescope(telescope);
	}

	/**
	 * Internal method to parse an Aperture node.
	 * @param telescope The instance of Telescope to set the aperture data for.
	 * @param apertureNode The XML DOM node for the Aperture tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @exception NumberFormatException Thrown if parsing the aperture value fails.
	 * @see org.estar.rtml.RTMLTelescope#setApertureUnits
	 * @see org.estar.rtml.RTMLTelescope#setApertureType
	 * @see org.estar.rtml.RTMLTelescope#setAperture(java.lang.String)
	 */
	private void parseApertureNode(RTMLTelescope telescope, Node apertureNode)
		throws RTMLException, NumberFormatException
	{
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;

		// check current XML node is correct
		if(apertureNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException
				(this.getClass().getName()+":parseApertureNode:Illegal Node:"+apertureNode);
		}
		if(apertureNode.getNodeName() != "Aperture")
		{
			throw new RTMLException
				(this.getClass().getName()+":parseApertureNode:Illegal Node Aperture:"+
				  apertureNode.getNodeName());
		}
		// go through attribute list
		attributeList = apertureNode.getAttributes();
		// units
		attributeNode = attributeList.getNamedItem("units");
		if(attributeNode != null)
			telescope.setApertureUnits(attributeNode.getNodeValue());
		else
			telescope.setApertureUnits(null);
		// type
		attributeNode = attributeList.getNamedItem("type");
		if(attributeNode != null)
			telescope.setApertureType(attributeNode.getNodeValue());
		else
			telescope.setApertureType("geometric"); // default is geometric
		// go through child nodes
		childList = apertureNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				if(childNode.getNodeValue() != null)
				{
					// ensure it is not all whitespace
					if(childNode.getNodeValue().trim().length() > 0)
					{
						telescope.setAperture(childNode.getNodeValue());
					}
				}
			}
		}
	}

	/**
	 * Internal method to parse an FocalLength node.
	 * @param telescope The instance of Telescope to set the focal length data for.
	 * @param focalLengthNode The XML DOM node for the FocalLength tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @exception NumberFormatException Thrown if parsing the focal length value fails.
	 * @see org.estar.rtml.RTMLTelescope#setFocalLengthUnits
	 * @see org.estar.rtml.RTMLTelescope#setFocalLength(java.lang.String)
	 */
	private void parseFocalLengthNode(RTMLTelescope telescope, Node focalLengthNode)
		throws RTMLException, NumberFormatException
	{
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;

		// check current XML node is correct
		if(focalLengthNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException
				(this.getClass().getName()+":parseFocalLengthNode:Illegal Node:"+focalLengthNode);
		}
		if(focalLengthNode.getNodeName() != "FocalLength")
		{
			throw new RTMLException
				(this.getClass().getName()+":parseFocalLengthNode:Illegal Node FocalLength:"+
				  focalLengthNode.getNodeName());
		}
		// go through attribute list
		attributeList = focalLengthNode.getAttributes();
		// units
		attributeNode = attributeList.getNamedItem("units");
		if(attributeNode != null)
			telescope.setFocalLengthUnits(attributeNode.getNodeValue());
		else
			telescope.setFocalLengthUnits(null);
		// go through child nodes
		childList = focalLengthNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				if(childNode.getNodeValue() != null)
				{
					// ensure it is not all whitespace
					if(childNode.getNodeValue().trim().length() > 0)
					{
						telescope.setFocalLength(childNode.getNodeValue());
					}
				}
			}
		}
	}

	/**
	 * Internal method to parse an Telescope Location node.
	 * @param telescope The telescope to add the Location data to.
	 * @param locationNode The XML DOM node for the Location tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @see #parseStringNode
	 * @see org.estar.rtml.RTMLTelescopeLocation
	 * @see org.estar.rtml.RTMLTelescopeLocation#setName
	 * @see org.estar.rtml.RTMLTelescopeLocation#setLongitude
	 * @see org.estar.rtml.RTMLTelescopeLocation#setLatitude
	 * @see org.estar.rtml.RTMLTelescopeLocation#setAltitude
	 * @see org.estar.rtml.RTMLTelescope#setLocation
	 */
	private void parseTelescopeLocationNode(RTMLTelescope telescope,Node locationNode) throws RTMLException
	{
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;

		// check current XML node is correct
		if(locationNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseTelescopeLocationNode:Illegal Node:"+
						locationNode);
		}
		if(locationNode.getNodeName() != "Location")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseTelescopeLocationNode:Illegal Node Name:"+
						locationNode.getNodeName());
		}
		// add location data to telescope
		RTMLTelescopeLocation location = new RTMLTelescopeLocation();
		// go through attribute list
		attributeList = locationNode.getAttributes();
		// get standard RTML attributes id,ref,uref
		parseRTMLAttributes(location,attributeList);
		// name
		attributeNode = attributeList.getNamedItem("name");
		if(attributeNode != null)
			location.setName(attributeNode.getNodeValue());
		else
			location.setName(null);
		// go through child nodes
		childList = locationNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "EastLongitude")
					location.setLongitude(parseStringNode("EastLongitude",childNode));
				else if(childNode.getNodeName() == "Latitude")
					location.setLatitude(parseStringNode("Latitude",childNode));
				else if(childNode.getNodeName() == "Height")
					location.setAltitude(parseStringNode("Height",childNode));
				else
					System.err.println("parseTelescopeLocationNode:ELEMENT:"+childNode);
			}
		}
		// Set location in telescope.
		telescope.setLocation(location);
	}

	/**
	 * Internal method to parse a Device node.
	 * @param rtmlDeviceHolder The object to add the device to - either an instance of RTMLDocument or 
	 *                         RTMLObservation.
	 * @param deviceNode The XML DOM node for the Device tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @see #parseSetupNode
	 * @see #parseRTMLAttributes
	 * @see #parseStringNode
	 */
	private void parseDeviceNode(RTMLDeviceHolder rtmlDeviceHolder,Node deviceNode) 
		throws RTMLException
	{
		RTMLDevice device = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;

		// check current XML node is correct
		if(deviceNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseDeviceNode:Illegal Node:"+
						deviceNode);
		}
		if(deviceNode.getNodeName() != "Device")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseDeviceNode:Illegal Node Name:"+
						deviceNode.getNodeName());
		}
		// add Device node
		device = new RTMLDevice();
		// go through attribute list
		attributeList = deviceNode.getAttributes();
		// get standard RTML attributes id,ref,uref
		parseRTMLAttributes(device,attributeList);
		// type
		attributeNode = attributeList.getNamedItem("type");
		if(attributeNode != null)
			device.setType(attributeNode.getNodeValue());
		// name
		attributeNode = attributeList.getNamedItem("name");
		if(attributeNode != null)
			device.setName(attributeNode.getNodeValue());
		// go through child nodes
		childList = deviceNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Setup")
					parseSetupNode(device,childNode);
				else if(childNode.getNodeName() == "SpectralRegion")
					device.setSpectralRegion(parseStringNode("SpectralRegion",childNode));
				else
					System.err.println("parseDeviceNode:ELEMENT:"+childNode);
			}
		}
		// set device in device holder (RTML document/RTML Observation).
		rtmlDeviceHolder.setDevice(device);
	}

	/**
	 * Internal method to parse a Setup node (inside a Device node).
	 * @param device The RTMLDevice instance to add the setup data to.
	 * @param setupNode The XML DOM node for the Setup tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @see #parseFilterNode
	 * @see #parseDetectorNode
	 * @see #parseGratingNode
	 */
	private void parseSetupNode(RTMLDevice device,Node setupNode) 
		throws RTMLException
	{
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;

		// check current XML node is correct
		if(setupNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseSetupNode:Illegal Node:"+
						setupNode);
		}
		if(setupNode.getNodeName() != "Setup")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseSetupNode:Illegal Node Name:"+
						setupNode.getNodeName());
		}
		// go through child nodes
		childList = setupNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Detector")
					parseDetectorNode(device,childNode);
				else if(childNode.getNodeName() == "Filter")
					parseFilterNode(device,childNode);
				else if(childNode.getNodeName() == "Grating")
					parseGratingNode(device,childNode);
				else
					System.err.println("parseSetupNode:ELEMENT:"+childNode);
			}
		}
	}

	/**
	 * Internal method to parse a Filter node.
	 * @param device The device to add the filter to.
	 * @param filterNode The XML DOM node for the Filter tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 */
	private void parseFilterNode(RTMLDevice device,Node filterNode) 
		throws RTMLException
	{
		RTMLGrating grating = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode,centerNode;
		NodeList childList;

		// check current XML node is correct
		if(filterNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseFilterNode:Illegal Node:"+
						filterNode);
		}
		if(filterNode.getNodeName() != "Filter")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseFilterNode:Illegal Node Name:"+
						filterNode.getNodeName());
		}
		// go through attribute list
		attributeList = filterNode.getAttributes();
		// type
		attributeNode = attributeList.getNamedItem("type");
		if(attributeNode != null)
			device.setFilterType(attributeNode.getNodeValue());
		// go through child nodes
		childList = filterNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Center")
				{
					centerNode = childNode;
					// currently used to respresent central wavelength of spectrograph.
					// This is wrong!
					if(device.getGrating() != null)
						grating = device.getGrating();
					else
						grating = new RTMLGrating();
					// go through attribute list
					attributeList = centerNode.getAttributes();
					// wavelengthUnits
					attributeNode = attributeList.getNamedItem("units");
					if(attributeNode != null)
						grating.setWavelengthUnits(attributeNode.getNodeValue());
					// wavelength is double value of node
					grating.setWavelength(parseDoubleNode(centerNode));
					// set grating in device.
					device.setGrating(grating);
				}
				else
					System.err.println("parseFilterNode:ELEMENT:"+childNode);
			}
		}
	}

	/**
	 * Internal method to parse a Detector node.
	 * @param device The device to add the detector to.
	 * @param detectorNode The XML DOM node for the Detector tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @see #parseBinningNode
	 * @see #parseGainNode
	 */
	private void parseDetectorNode(RTMLDevice device,Node detectorNode) 
		throws RTMLException
	{
		RTMLDetector detector = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;

		// check current XML node is correct
		if(detectorNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseDetectorNode:Illegal Node:"+
						detectorNode);
		}
		if(detectorNode.getNodeName() != "Detector")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseDetectorNode:Illegal Node Name:"+
						detectorNode.getNodeName());
		}
		// add Detector node
		detector = new RTMLDetector();
		// go through child nodes
		childList = detectorNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Binning")
					parseBinningNode(detector,childNode);
				else if(childNode.getNodeName() == "Gain")
					parseGainNode(detector,childNode);
				else
					System.err.println("parseDetectorNode:ELEMENT:"+childNode);
			}
		}
		// set detector in device.
		device.setDetector(detector);
	}

	/**
	 * Internal method to parse a Binning node.
	 * @param detector The detector to set the binning values in.
	 * @param binningNode The XML DOM node for the Binning tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @see org.estar.rtml.RTMLDetector
	 * @see org.estar.rtml.RTMLDetector#setColumnBinning
	 * @see org.estar.rtml.RTMLDetector#setRowBinning
	 */
	private void parseBinningNode(RTMLDetector detector,Node binningNode) 
		throws RTMLException
	{
		Node childNode,attributeNode;
		NamedNodeMap attributeList = null;
		NodeList childList;

		// check current XML node is correct
		if(binningNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseBinningNode:Illegal Node:"+
						binningNode);
		}
		if(binningNode.getNodeName() != "Binning")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseBinningNode:Illegal Node Name:"+
						binningNode.getNodeName());
		}
		// go through child nodes
		childList = binningNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "X")
				{
					binningNode = childNode;
					// go through attribute list
					attributeList = binningNode.getAttributes();
					// row binning
					attributeNode = attributeList.getNamedItem("units");
					if(attributeNode != null)
					{
						if(attributeNode.getNodeValue().equals("pixels") == false)
						{
							throw new RTMLException(this.getClass().getName()+
										":parseBinningNode:Illegal X units:"+
										attributeNode.getNodeValue());
						}
					}
					detector.setColumnBinning(parseIntegerNode(binningNode));
				}
				else if(childNode.getNodeName() == "Y")
				{
					binningNode = childNode;
					// go through attribute list
					attributeList = binningNode.getAttributes();
					// row binning
					attributeNode = attributeList.getNamedItem("units");
					if(attributeNode != null)
					{
						if(attributeNode.getNodeValue().equals("pixels") == false)
						{
							throw new RTMLException(this.getClass().getName()+
										":parseBinningNode:Illegal Y units:"+
										attributeNode.getNodeValue());
						}
					}
					detector.setRowBinning(parseIntegerNode(binningNode));
				}
				else
					System.err.println("parseBinningNode:ELEMENT:"+childNode);
			}
		}
	}

	/**
	 * Internal method to parse a Gain node.
	 * @param detector The detector to set the gain value in.
	 * @param gainNode The XML DOM node for the Gain tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @see org.estar.rtml.RTMLDetector
	 * @see org.estar.rtml.RTMLDetector#setGain
	 */
	private void parseGainNode(RTMLDetector detector,Node gainNode) throws RTMLException
	{
		Node childNode,attributeNode;
		NamedNodeMap attributeList = null;
		NodeList childList;

		// check current XML node is correct
		if(gainNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseGainNode:Illegal Node:"+
						gainNode);
		}
		if(gainNode.getNodeName() != "Gain")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseGainNode:Illegal Node Name:"+
						gainNode.getNodeName());
		}
		// go through child nodes
		childList = gainNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Description")
				{
					// We should really put the gain in the 'Value' element.
					// But the 'Value' element has a fixed 'units' attribute of 'electrons_per_adu'
					// We want to specify the EMCCD Gain, which as far as I know, 
					// does not use these units.
					// Therefore retrieve the value into the 'Description' element instead,
					// where the creator put it (RTML31Create.java:createDetector).
					detector.setGain(parseDoubleNode(childNode));
				}
				else
					System.err.println("parseBinningNode:ELEMENT:"+childNode);
			}
		}
	}

	/**
	 * Internal method to parse a Grating node.
	 * @param device The device to add the grating data to.
	 * @param gratingNode The XML DOM node for the Grating tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 */
	private void parseGratingNode(RTMLDevice device,Node gratingNode) 
		throws RTMLException
	{
		RTMLGrating grating = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode,centerNode;
		NodeList childList;

		// check current XML node is correct
		if(gratingNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseGratingNode:Illegal Node:"+
						gratingNode);
		}
		if(gratingNode.getNodeName() != "Grating")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseGratingNode:Illegal Node Name:"+
						gratingNode.getNodeName());
		}
		if(device.getGrating() != null)
			grating = device.getGrating();
		else
			grating = new RTMLGrating();
		// go through attribute list
		attributeList = gratingNode.getAttributes();

		// name
		attributeNode = attributeList.getNamedItem("name");
		if(attributeNode != null)
			grating.setName(attributeNode.getNodeValue());
		// go through child nodes
		childList = gratingNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				System.err.println("parseGratingNode:ELEMENT:"+childNode);
			}
		}
		// set grating in device.
		device.setGrating(grating);
	}

	/**
	 * Internal method to parse an Observation node.
	 * @param observation The observation data to add ImageData sub-elements to. The observation
	 *        itself is created in parseSchedule, as the document object model has schedule in observation,
	 *        whereas in RTML 3.1a Observation is contained within Schedule (as opposed to RTML2.2 where it is
	 *        the other way around).
	 * @param observationNode The XML DOM node for the Observation tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @see #parseImageDataNode
	 * @see #parseSourceCatalogueNode
	 */
	private void parseObservationNode(RTMLObservation observation,Node observationNode) throws RTMLException
	{
		RTMLImageData imageData = null;
		Node childNode;
		NodeList childList;
		boolean parsedImageData = false;
		boolean parsedSourceCatalogue = false;

		// check current XML node is correct
		if(observationNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseObservationNode:Illegal Node:"+
						observationNode);
		}
		if(observationNode.getNodeName() != "Observation")
		{
			throw new RTMLException(this.getClass().getName()+":parseObservationNode:Illegal Node Name:"+
						observationNode.getNodeName());
		}
		// go through child nodes
		childList = observationNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "ImageData")
				{
					// if we havn't started parsing image data create object
					if(imageData == null)
					{
						// create new image data
						imageData = new RTMLImageData();
						parsedImageData = false;
						parsedSourceCatalogue = false;
					}
					// parse image data
					parseImageDataNode(imageData,childNode);
					// set flag
					parsedImageData = true;
					// if we have parsed image data and source catalogue, this
					// imageData entry is finished. Add and reset
					if(parsedImageData && parsedSourceCatalogue)
					{
						// add image data to observation
						observation.addImageData(imageData);
						// reset imageData
						imageData = null;
					}
				}
				else if(childNode.getNodeName() == "SourceCatalogue")
				{
					// if we havn't started parsing image data create object
					if(imageData == null)
					{
						// create new image data
						imageData = new RTMLImageData();
						parsedImageData = false;
						parsedSourceCatalogue = false;
					}
					parseSourceCatalogueNode(imageData,childNode);
					// set flag
					parsedSourceCatalogue = true;
					// if we have parsed image data and source catalogue, this
					// imageData entry is finished. Add and reset
					if(parsedImageData && parsedSourceCatalogue)
					{
						// add image data to observation
						observation.addImageData(imageData);
						// reset imageData
						imageData = null;
					}
				}
				else
					System.err.println("parseObservationNode:ELEMENT:"+childNode);
			}
		}// end for
		// If theres some image data we havn't added to the observation yet, lets do that now
		if((imageData != null) && (parsedImageData || parsedSourceCatalogue))
		{
			// add image data to observation
			observation.addImageData(imageData);
		}
	}

	/**
	 * Internal method to parse an Target node.
	 * @param parent The instance of RTMLObservation/RTMLDocument (RTMLTargetHolder) to set the target for.
	 * @param targetNode The XML DOM node for the Target tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @see org.estar.rtml.RTMLTargetHolder
	 * @see #parseCoordinatesNode
	 * @see #parseTargetBrightnessNode
	 */
	private void parseTargetNode(RTMLTargetHolder parent,Node targetNode) throws RTMLException
	{
		RTMLTarget target = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;

		// check current XML node is correct
		if(targetNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseTargetNode:Illegal Node:"+
						targetNode);
		}
		if(targetNode.getNodeName() != "Target")
		{
			throw new RTMLException(this.getClass().getName()+":parseTargetNode:Illegal Node Name:"+
						targetNode.getNodeName());
		}
		// add target node
		target = new RTMLTarget();
		// go through attribute list
		attributeList = targetNode.getAttributes();
		// RTMLTarget has a type attribute that should be set to "normal" or "toop"
		// This is related to RTML2.2's type attribute - this is not present in RTML 3.1a
		// Will will set this from the schedule priority later...
		// ident
		attributeNode = attributeList.getNamedItem("id");
		if(attributeNode != null)
			target.setIdent(attributeNode.getNodeValue());
		else
			target.setIdent(null);
		// name
		attributeNode = attributeList.getNamedItem("name");
		if(attributeNode != null)
			target.setName(attributeNode.getNodeValue());
		else
			target.setName(null);
		// go through child nodes
		childList = targetNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Coordinates")
					parseCoordinatesNode(target,childNode);
				else if(childNode.getNodeName() == "TargetBrightness")
					parseTargetBrightnessNode(target,childNode);
				else
					System.err.println("parseTargetNode:ELEMENT:"+childNode);
			}
		}
		// add target to parent document/observation.
		parent.setTarget(target);
	}

	/**
	 * Internal method to parse an Coordinates node.
	 * @param target The instance of RTMLTarget to set the coordinates for.
	 * @param coordinatesNode The XML DOM node for the Coordinates tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @see #parseRightAscensionNode
	 * @see #parseDeclinationNode
	 * @see #parseEquinoxNode
	 */
	private void parseCoordinatesNode(RTMLTarget target,Node coordinatesNode) throws RTMLException
	{
		Node childNode;
		NodeList childList;

		// check current XML node is correct
		if(coordinatesNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseCoordinatesNode:Illegal Node:"+
						coordinatesNode);
		}
		if(coordinatesNode.getNodeName() != "Coordinates")
		{
			throw new RTMLException(this.getClass().getName()+":parseCoordinatesNode:Illegal Node Name:"+
						coordinatesNode.getNodeName());
		}
		// go through child nodes
		childList = coordinatesNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "RightAscension")
					parseRightAscensionNode(target,childNode);
				else if(childNode.getNodeName() == "Declination")
					parseDeclinationNode(target,childNode);
				else if(childNode.getNodeName() == "Equinox")
					parseEquinoxNode(target,childNode);
			}
		}
	}

	/**
	 * Internal method to parse an RightAscension node.
	 * @param target The instance of RTMLTarget to set the right ascension for.
	 * @param rightAscensionNode The XML DOM node for the RightAscension tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @see #parseIntegerNode
	 * @see #parseDoubleNode
	 */
	private void parseRightAscensionNode(RTMLTarget target,Node rightAscensionNode) throws RTMLException
	{
		RA ra = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		double dvalue;

		// check current XML node is correct
		if(rightAscensionNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseRightAscensionNode:Illegal Node:"+
						rightAscensionNode);
		}
		if(rightAscensionNode.getNodeName() != "RightAscension")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseRightAscensionNode:Illegal Node Name:"+
						rightAscensionNode.getNodeName());
		}
		// initialise RA object
		ra = new RA();
		// go through attribute list
		attributeList = rightAscensionNode.getAttributes();
		// go through child nodes
		childList = rightAscensionNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Hours")
				{
					ra.setHours(parseIntegerNode(childNode));
				}
				else if(childNode.getNodeName() == "Minutes")
				{
					ra.setMinutes(parseIntegerNode(childNode));
				}
				else if(childNode.getNodeName() == "Seconds")
				{
					ra.setSeconds(parseDoubleNode(childNode));
					target.setRA(ra);
				}
				else if(childNode.getNodeName() == "Offset")
				{
					target.setRAOffset(parseDoubleNode(childNode));
				}
				else if(childNode.getNodeName() == "Value")
				{
					// value has fixed units hours
					dvalue = parseDoubleNode(childNode);
					ra.setHours((int)(Math.floor(dvalue)));
					// calculate remaining minutes
					dvalue -= Math.floor(dvalue);
					dvalue *= 60.0;
					ra.setMinutes((int)(Math.floor(dvalue)));
					// calculate remaining seconds
					dvalue -= Math.floor(dvalue);
					dvalue *= 60.0;
					ra.setSeconds(dvalue);
					target.setRA(ra);
				}
			}
			else if(childNode.getNodeType() == Node.TEXT_NODE)
			{
			}
		}
	}

	/**
	 * Internal method to parse an Declination node.
	 * @param target The instance of RTMLTarget to set the declination for.
	 * @param declinationNode The XML DOM node for the Declination tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @see #parseStringNode
	 * @see #parseIntegerNode
	 * @see #parseDoubleNode
	 */
	private void parseDeclinationNode(RTMLTarget target,Node declinationNode) throws RTMLException
	{
		Dec dec = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String valueString = null;
		double dvalue;

		// check current XML node is correct
		if(declinationNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseDeclinationNode:Illegal Node:"+
						declinationNode);
		}
		if(declinationNode.getNodeName() != "Declination")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseDeclinationNode:Illegal Node Name:"+
						declinationNode.getNodeName());
		}
		// initialise Dec object
		dec = new Dec();
		// go through attribute list
		attributeList = declinationNode.getAttributes();
		// go through child nodes
		childList = declinationNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Degrees")
				{
					// complicated, this could be "-00".
					// setDegrees and setNegative must be called as appropriate
					valueString = parseStringNode("Degrees",childNode);
					if(valueString.startsWith("+"))
						dec.setNegative(false);
					else if(valueString.startsWith("-"))
						dec.setNegative(true);
					else
					{
						throw new RTMLException(this.getClass().getName()+
									":parseDeclinationNode:Illegal Degrees value:"+
								     valueString+" does not start with a '+' or '-'.");
					}
					// parse (positive) rest of value after +/-
					dec.setDegrees(Integer.parseInt(valueString.substring(1)));
				}
				else if(childNode.getNodeName() == "Arcminutes")
				{
					dec.setMinutes(parseIntegerNode(childNode));
				}
				else if(childNode.getNodeName() == "Arcseconds")
				{
					dec.setSeconds(parseDoubleNode(childNode));
					target.setDec(dec);
				}
				else if(childNode.getNodeName() == "Offset")
				{
					target.setDecOffset(parseDoubleNode(childNode));
				}
				else if(childNode.getNodeName() == "Value")
				{
					// value has fixed units degrees
					dvalue = parseDoubleNode(childNode);
					// set negative
					if(dvalue < 0.0)
						dec.setNegative(true);
					else 
						dec.setNegative(false);
					dvalue = Math.abs(dvalue);
					// degrees
					dec.setDegrees((int)(Math.floor(dvalue)));
					// calculate remaining minutes
					dvalue -= Math.floor(dvalue);
					dvalue *= 60.0;
					dec.setMinutes((int)(Math.floor(dvalue)));
					// calculate remaining seconds
					dvalue -= Math.floor(dvalue);
					dvalue *= 60.0;
					dec.setSeconds(dvalue);
					target.setDec(dec);
				}
			}
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
			}
		}
	}

	/**
	 * Internal method to parse an Equinox node.
	 * @param target The instance of RTMLTarget to set the equinox for.
	 * @param equinoxNode The XML DOM node for the Equinox tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 */
	private void parseEquinoxNode(RTMLTarget target,Node equinoxNode) throws RTMLException
	{
		Node childNode;
		NodeList childList;

		// check current XML node is correct
		if(equinoxNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseEquinoxNode:Illegal Node:"+
						equinoxNode);
		}
		if(equinoxNode.getNodeName() != "Equinox")
		{
			throw new RTMLException(this.getClass().getName()+":parseEquinoxNode:Illegal Node Name:"+
						equinoxNode.getNodeName());
		}
		// go through child nodes
		childList = equinoxNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				if(childNode.getNodeValue() != null)
				{
					if(childNode.getNodeValue().trim().length() > 0)
					{
						target.setEquinox(childNode.getNodeValue().trim());
					}
				}
			}
		}
	}

	/**
	 * Internal method to parse an TargetBrightness node.
	 * @param target The instance of RTMLTarget to set the target brightness.
	 * @param targetBrightnessNode The XML DOM node for the TargetBrightness tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @see #parseDoubleNode
	 */
	private void parseTargetBrightnessNode(RTMLTarget target,Node targetBrightnessNode) throws RTMLException
	{
		RA ra = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		double dvalue;

		// check current XML node is correct
		if(targetBrightnessNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseTargetBrightnessNode:Illegal Node:"+
						targetBrightnessNode);
		}
		if(targetBrightnessNode.getNodeName() != "TargetBrightness")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseTargetBrightnessNode:Illegal Node Name:"+
						targetBrightnessNode.getNodeName());
		}
		// go through attribute list
		attributeList = targetBrightnessNode.getAttributes();
		// go through child nodes
		childList = targetBrightnessNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Magnitude")
				{
					target.setMagnitude(parseDoubleNode(childNode));
				}
				else if(childNode.getNodeName() == "Type")
				{
					target.setMagnitudeFilterType(parseStringNode("Type",childNode));
				}
				else if(childNode.getNodeName() == "Error")
				{
					target.setMagnitudeError(parseDoubleNode(childNode));
				}
			}
			else if(childNode.getNodeType() == Node.TEXT_NODE)
			{
			}
		}
	}

	/**
	 * Internal method to parse a Schedule node.
	 * @param document The instance of RTMLDocument to set the schedule for.
	 * @param scheduleNode The XML DOM node for the Schedule tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @exception NumberFormatException Thrown if the priority number parsing fails.
	 * @exception ParseException Thrown if date parsing fails.
	 * @see #parseExposureNode
	 * @see #parseCalibrationNode
	 * @see #parseDateTimeConstraintNode
	 * @see #parseDeviceNode
	 * @see #parseSeriesConstraintNode
	 * @see #parseSeeingConstraintNode
	 * @see #parseMoonConstraintNode
	 * @see #parseSkyConstraintNode
	 * @see #parseExtinctionConstraintNode
	 * @see #parseAirmassConstraintNode
	 * @see #parseTargetNode
	 * @see #parseObservationNode
	 * @see #parseIntegerNode
	 */
	private void parseScheduleNode(RTMLDocument document,Node scheduleNode) throws RTMLException, 
								     NumberFormatException, ParseException
	{
		RTMLObservation observation = null;
		RTMLSchedule schedule = null;
		Node childNode;
		NodeList childList;
		NamedNodeMap attributeList = null;
		Node attributeNode;
		String priorityString = null;
		int priority = RTMLSchedule.SCHEDULE_PRIORITY_NORMAL;

		// check current XML node is correct
		if(scheduleNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseScheduleNode:Illegal Node:"+
						scheduleNode);
		}
		if(scheduleNode.getNodeName() != "Schedule")
		{
			throw new RTMLException(this.getClass().getName()+":parseScheduleNode:Illegal Node Name:"+
						scheduleNode.getNodeName());
		}
		// In RTML 2.2 Observation contains Schedule
		// In org.estar.rtml document mode, Observation contains Schedule.
		// In RTML 3.1 Schedule contains Observation
		// Therefore, add an Observation for the Schedule to be in.
		// This assumes one observation per schedule at the moment!
		observation = new RTMLObservation();
		document.addObservation(observation);
		// add schedule node
		schedule = new RTMLSchedule();
		// add scheule to observation
		observation.setSchedule(schedule);
		// get schedule attributes
		attributeList = scheduleNode.getAttributes();
		// status (StatusTypes) inactive|active|done|error
		// type requested|triggered
		// go through child nodes
		childList = scheduleNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "AirmassConstraint")
					parseAirmassConstraintNode(schedule,childNode);
				else if(childNode.getNodeName() == "Calibaration")
					parseCalibrationNode(schedule,childNode);
				else if(childNode.getNodeName() == "DateTimeConstraint")
					parseDateTimeConstraintNode(schedule,childNode);
				else if(childNode.getNodeName() == "Device")
					parseDeviceNode(observation,childNode);
				else if(childNode.getNodeName() == "Exposure")
					parseExposureNode(schedule,childNode);
				else if(childNode.getNodeName() == "ExtinctionConstraint")
					parseExtinctionConstraintNode(schedule,childNode);
				else if(childNode.getNodeName() == "MoonConstraint")
					parseMoonConstraintNode(schedule,childNode);
				else if(childNode.getNodeName() == "Observation")
					parseObservationNode(observation,childNode);
				else if(childNode.getNodeName() == "Priority")
				{
					priority = parseIntegerNode(childNode);
					schedule.setPriority(priority);
				}
				else if(childNode.getNodeName() == "SeriesConstraint")
					parseSeriesConstraintNode(schedule,childNode);
				else if(childNode.getNodeName() == "SeeingConstraint")
					parseSeeingConstraintNode(schedule,childNode);
				else if(childNode.getNodeName() == "SkyConstraint")
					parseSkyConstraintNode(schedule,childNode);
				else if(childNode.getNodeName() == "Target")
					parseTargetNode(observation,childNode);
			}
		}
		// based on the schedule priority, we have to set the target type to "toop" or "normal"
		if(observation.getTarget() != null)
		{
			if(priority == RTMLSchedule.SCHEDULE_PRIORITY_TOOP)
				observation.getTarget().setType("toop");
			else
				observation.getTarget().setType("normal");
		}
	}

	/**
	 * Internal method to parse an Exposure node.
	 * @param schedule The instance of RTMLSchedule to set the exposure for.
	 * @param exposureNode The XML DOM node for the Exposure tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @see #parseIntegerNode
	 */
	private void parseExposureNode(RTMLSchedule schedule,Node exposureNode) throws RTMLException
	{
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String units = null,type = null;
		double exposureLength;
		int count;

		// check current XML node is correct
		if(exposureNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseExposureNode:Illegal Node:"+
						exposureNode);
		}
		if(exposureNode.getNodeName() != "Exposure")
		{
			throw new RTMLException(this.getClass().getName()+":parseExposureNode:Illegal Node Name:"+
						exposureNode.getNodeName());
		}
		// go through attribute list
		attributeList = exposureNode.getAttributes();
		// exposure count
		attributeNode = attributeList.getNamedItem("count");
		schedule.setExposureCount(attributeNode.getNodeValue());
		// go through child nodes
		childList = exposureNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Value")
				{
					exposureLength = parseDoubleNode(childNode);
					schedule.setExposureLength(exposureLength);
					// Get value attribute list
					attributeList = childNode.getAttributes();
					// get units attribute
					attributeNode = attributeList.getNamedItem("units");
					units = attributeNode.getNodeValue();
					schedule.setExposureUnits(units);
					// type is time rather than snr
					schedule.setExposureType("time");
				}
			}
			else if(childNode.getNodeType() == Node.TEXT_NODE)
			{
			}
		}
	}

	private void parseCalibrationNode(RTMLSchedule schedule,Node calibrationNode) throws RTMLException
	{
	}

	/**
	 * Internal method to parse a DateTimeConstraint node.
	 * @param schedule The instance of RTMLSchedule to set the time constrints for.
	 * @param dateTimeConstraintNode The XML DOM node for the DateTimeConstrint tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs, or the type
	 *                        is 'exclude' (we don't support them).
	 * @exception ParseException Thrown if the date was not parseable.
	 * @see RTMLSchedule
	 */
	private void parseDateTimeConstraintNode(RTMLSchedule schedule,
						 Node dateTimeConstraintNode) throws RTMLException, ParseException
	{
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		RTMLDateFormat dateFormat = null;
		String type = null;
		String value = null;
		String system = null;
		String error = null;
		Date date = null;

		dateFormat = new RTMLDateFormat();
		// check current XML node is correct
		if(dateTimeConstraintNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseDateTimeConstraintNode:Illegal Node:"+
						dateTimeConstraintNode);
		}
		if(dateTimeConstraintNode.getNodeName() != "DateTimeConstraint")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseDateTimeConstraintNode:Illegal Node Name:"+
						dateTimeConstraintNode.getNodeName());
		}
		// go through attribute list
		attributeList = dateTimeConstraintNode.getAttributes();
		// type, include or exclude
		attributeNode = attributeList.getNamedItem("type");
		if(attributeNode != null)
		{
			type = attributeNode.getNodeValue();
			if(type.equals("exclude"))
			{
				throw new RTMLException(this.getClass().getName()+
				 ":parseDateTimeConstraintNode:Time constraint is an exclude: We only allow include.");
			}
		}
		// go through child nodes
		childList = dateTimeConstraintNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "DateTimeStart")
				{
					// go through child attribute list
					attributeList = childNode.getAttributes();
					// value
					attributeNode = attributeList.getNamedItem("value");
					value = attributeNode.getNodeValue();
					// system must be UT
					attributeNode = attributeList.getNamedItem("system");
					if(attributeNode != null)
					{
						system = attributeNode.getNodeValue();
						if(system.equals("UT") == false)
						{
							throw new RTMLException(this.getClass().getName()+
							  ":parseDateTimeConstraintNode:DateTimeStart "+
										"system was not UT.");
						}
					}
					// parse value into date
					date = dateFormat.parse(value);
					schedule.setStartDate(date);
				}
				else if(childNode.getNodeName() == "DateTimeEnd")
				{
					// go through child attribute list
					attributeList = childNode.getAttributes();
					// value
					attributeNode = attributeList.getNamedItem("value");
					value = attributeNode.getNodeValue();
					// system must be UT
					attributeNode = attributeList.getNamedItem("system");
					if(attributeNode != null)
					{
						system = attributeNode.getNodeValue();
						if(system.equals("UT") == false)
						{
							throw new RTMLException(this.getClass().getName()+
							  ":parseDateTimeConstraintNode:DateTimeEnd "+
										"system was not UT.");
						}
					}
					// parse value into date
					date = dateFormat.parse(value);
					schedule.setEndDate(date);
				}
			}
		}
	}

	/**
	 * Parse a date time node. This is a node (such as StartDateTime, EndDateTime) containing a date
	 * formatted in the form: 2001-05-29T12:00:00-1100. The keyword 'never' returns a null date.
	 * @param dateTimeNode The XML node containing a date to parse as it's CDATA.
	 * @return The parsed date is returned, or null if 'never' was specified.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @see RTMLDateFormat
	 */
	private Date parseDateTimeNode(Node dateTimeNode) throws RTMLException
	{
		Node childNode;
		NodeList childList;
		Date date = null;
		RTMLDateFormat dateFormat = null;
		String s = null;

		dateFormat = new RTMLDateFormat();
		childList = dateTimeNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				s = childNode.getNodeValue();
				if(s.equals("never"))
					date = null;
				else
				{
					try
					{
						date = dateFormat.parse(s);
					}
					catch(ParseException e)
					{
						throw new RTMLException(this.getClass().getName()+
								    ":parseDateTimeNode:Illegal Date/Time:"+s+":",e);
					}
				}
			}
		}
		return date;
	}

	/**
	 * Internal method to parse a AirmassConstraint node.
	 * @param schedule The instance of RTMLSchedule to set the airmass constrints for.
	 * @param seeingConstraintNode The XML DOM node for the AirmassConstraint tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @exception NumberFormatException Thrown if a parse error occurs when parsing the numeric attributes.
	 * @see RTMLSchedule
	 * @see RTMLAirmassConstraint
	 */
	private void parseAirmassConstraintNode(RTMLSchedule schedule,Node airmassConstraintNode) throws 
		RTMLException, NumberFormatException
	{
		RTMLAirmassConstraint airmassConstraint = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String s = null;

		// check current XML node is correct
		if(airmassConstraintNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseAirmassConstraintNode:Illegal Node:"+
						airmassConstraintNode);
		}
		if(airmassConstraintNode.getNodeName() != "AirmassConstraint")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseAirmassConstraintNode:Illegal Node Name:"+
						airmassConstraintNode.getNodeName());
		}
		// add airmass constraint object
		airmassConstraint = new RTMLAirmassConstraint();
		// go through attribute list
		attributeList = airmassConstraintNode.getAttributes();
		// minimum
		attributeNode = attributeList.getNamedItem("minimum");
		if(attributeNode != null)
		{
			s = attributeNode.getNodeValue();
			if(s != null)
				airmassConstraint.setMinimum(s);
		}
		// maximum
		attributeNode = attributeList.getNamedItem("maximum");
		if(attributeNode != null)
		{
			s = attributeNode.getNodeValue();
			if(s != null)
				airmassConstraint.setMaximum(s);
		}
		// add airmass constraint to schedule
		schedule.setAirmassConstraint(airmassConstraint);
	}

	/**
	 * Internal method to parse a SeriesConstraint node.
	 * @param schedule The instance of RTMLSchedule to set the series constrints for.
	 * @param seriesConstraintNode The XML DOM node for the SeriesConstrint tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @see #parseIntegerNode
	 * @see #parsePeriodNode
	 * @see RTMLSchedule
	 * @see RTMLSeriesConstraint
	 */
	private void parseSeriesConstraintNode(RTMLSchedule schedule,Node seriesConstraintNode) throws RTMLException
	{
		RTMLSeriesConstraint seriesConstraint = null;
		RTMLPeriodFormat period = null;
		Node childNode;
		NodeList childList;
		int count;

		// check current XML node is correct
		if(seriesConstraintNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseSeriesConstraintNode:Illegal Node:"+
						seriesConstraintNode);
		}
		if(seriesConstraintNode.getNodeName() != "SeriesConstraint")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseSeriesConstraintNode:Illegal Node Name:"+
						seriesConstraintNode.getNodeName());
		}
		// add series constraint object
		seriesConstraint = new RTMLSeriesConstraint();
		// go through child nodes
		childList = seriesConstraintNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Count")
				{
					count = parseIntegerNode(childNode);
					seriesConstraint.setCount(count);
				}
				else if(childNode.getNodeName() == "Interval")
				{
					period = parsePeriodNode(childNode);
					seriesConstraint.setInterval(period);
				}
				else if(childNode.getNodeName() == "Tolerance")
				{
					period = parsePeriodNode(childNode);
					seriesConstraint.setTolerance(period);
				}
			}
		}
		// add series constraint to schedule
		schedule.setSeriesConstraint(seriesConstraint);
	}

	/**
	 * Internal method to parse a SeeingConstraint node.
	 * @param schedule The instance of RTMLSchedule to set the seeing constrints for.
	 * @param seeingConstraintNode The XML DOM node for the SeeingConstrint tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @exception NumberFormatException Thrown if a parse error occurs when parsing the numeric attributes.
	 * @see RTMLSchedule
	 * @see RTMLSeeingConstraint
	 */
	private void parseSeeingConstraintNode(RTMLSchedule schedule,Node seeingConstraintNode) throws RTMLException, 
											   NumberFormatException
	{
		RTMLSeeingConstraint seeingConstraint = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String s = null;

		// check current XML node is correct
		if(seeingConstraintNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseSeeingConstraintNode:Illegal Node:"+
						seeingConstraintNode);
		}
		if(seeingConstraintNode.getNodeName() != "SeeingConstraint")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseSeeingConstraintNode:Illegal Node Name:"+
						seeingConstraintNode.getNodeName());
		}
		// add seeing constraint object
		seeingConstraint = new RTMLSeeingConstraint();
		// go through attribute list
		attributeList = seeingConstraintNode.getAttributes();
		// minimum
		attributeNode = attributeList.getNamedItem("minimum");
		if(attributeNode != null)
		{
			s = attributeNode.getNodeValue();
			if(s != null)
				seeingConstraint.setMinimum(s);
		}
		// maximum
		attributeNode = attributeList.getNamedItem("maximum");
		if(attributeNode != null)
		{
			s = attributeNode.getNodeValue();
			if(s != null)
				seeingConstraint.setMaximum(s);
		}
		// go through child nodes
		childList = seeingConstraintNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "ExposureFactor")
				{
					// not used atm
				}
			}
		}
		// add seeing constraint to schedule
		schedule.setSeeingConstraint(seeingConstraint);
	}

	/**
	 * Internal method to parse a MoonConstraint node.
	 * @param schedule The instance of RTMLSchedule to set the moon constrints for.
	 * @param moonConstraintNode The XML DOM node for the MoonConstrint tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @exception NumberFormatException Thrown if a parse error occurs when parsing the numeric attributes.
	 * @see RTMLSchedule
	 * @see RTMLMoonConstraint
	 */
	private void parseMoonConstraintNode(RTMLSchedule schedule,Node moonConstraintNode) throws RTMLException, 
											   NumberFormatException
	{
		RTMLMoonConstraint moonConstraint = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String s = null;

		// check current XML node is correct
		if(moonConstraintNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseMoonConstraintNode:Illegal Node:"+
						moonConstraintNode);
		}
		if(moonConstraintNode.getNodeName() != "MoonConstraint")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseMoonConstraintNode:Illegal Node Name:"+
						moonConstraintNode.getNodeName());
		}
		// add moon constraint object
		moonConstraint = new RTMLMoonConstraint();
		// go through child nodes
		childList = moonConstraintNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Distance")
				{
					// value
					moonConstraint.setDistance(parseDoubleNode(childNode));
					// go through Distance attribute list
					attributeList = childNode.getAttributes();
					// units
					attributeNode = attributeList.getNamedItem("units");
					if(attributeNode != null)
					{
						s = attributeNode.getNodeValue();
						if(s != null)
						{
							if(s.equals("degrees"))
								moonConstraint.setUnits(s);
							else
							{
								throw new RTMLException(this.getClass().getName()+
								   ":parseMoonConstraintNode:Illegal Distance units:"+
								   s+":Should be 'degrees'.");
							}
						}
					}
				}
			}
		}// end for
		// add moon constraint to schedule
		schedule.setMoonConstraint(moonConstraint);
	}

	/**
	 * Internal method to parse a SkyConstraint node.
	 * @param schedule The instance of RTMLSchedule to set the sky constrints for.
	 * @param skyConstraintNode The XML DOM node for the SkyConstrint tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @exception NumberFormatException Thrown if a parse error occurs when parsing the numeric attributes.
	 * @see RTMLSchedule
	 * @see RTMLSkyConstraint
	 */
	private void parseSkyConstraintNode(RTMLSchedule schedule,Node skyConstraintNode) throws RTMLException, 
											   NumberFormatException
	{
		RTMLSkyConstraint skyConstraint = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String s = null;

		// check current XML node is correct
		if(skyConstraintNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseSkyConstraintNode:Illegal Node:"+
						skyConstraintNode);
		}
		if(skyConstraintNode.getNodeName() != "SkyConstraint")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseSkyConstraintNode:Illegal Node Name:"+
						skyConstraintNode.getNodeName());
		}
		// add sky constraint object
		skyConstraint = new RTMLSkyConstraint();
		// go through child nodes
		childList = skyConstraintNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Brightness")
				{
					skyConstraint.setSky(parseStringNode("Brightness",childNode));
				}
				if(childNode.getNodeName() == "Flux")
				{
					skyConstraint.setValue(parseDoubleNode(childNode));
				}
				if(childNode.getNodeName() == "Units")
				{
					skyConstraint.setUnits(parseStringNode("Units",childNode));
				}
			}
		}// end for
		// add sky constraint to schedule
		schedule.setSkyConstraint(skyConstraint);
	}

	/**
	 * Internal method to parse a ExtinctionConstraint node.
	 * @param schedule The instance of RTMLSchedule to set the sky constrints for.
	 * @param extinctionConstraintNode The XML DOM node for the ExtinctionConstrint tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @exception NumberFormatException Thrown if a parse error occurs when parsing the numeric attributes.
	 * @see RTMLSchedule
	 * @see RTMLExtinctionConstraint
	 */
	private void parseExtinctionConstraintNode(RTMLSchedule schedule,Node extinctionConstraintNode) throws 
		RTMLException, NumberFormatException
	{
		RTMLExtinctionConstraint extinctionConstraint = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String s = null;

		// check current XML node is correct
		if(extinctionConstraintNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+
						":parseExtinctionConstraintNode:Illegal Node:"+
						extinctionConstraintNode);
		}
		if(extinctionConstraintNode.getNodeName() != "ExtinctionConstraint")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseExtinctionConstraintNode:Illegal Node Name:"+
						extinctionConstraintNode.getNodeName());
		}
		// add sky constraint object
		extinctionConstraint = new RTMLExtinctionConstraint();
		// go through child nodes
		childList = extinctionConstraintNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Clouds")
				{
					extinctionConstraint.setClouds(parseStringNode("Clouds",childNode));
				}
				if(childNode.getNodeName() == "Magnitudes")
				{
					extinctionConstraint.setValue(parseDoubleNode(childNode));
				}
			}
		}// end for
		// add extinction constraint to schedule
		schedule.setExtinctionConstraint(extinctionConstraint);
	}

	/**
	 * Internal method to parse an ImageData node.
	 * @param imageData The instance of RTMLImageData to set the image data for.
	 * @param imageDataNode The XML DOM node for the ImageData tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	private void parseImageDataNode(RTMLImageData imageData,Node imageDataNode) throws RTMLException
	{
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String s = null;

		// check current XML node is correct
		if(imageDataNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseImageDataNode:Illegal Node:"+
						imageDataNode);
		}
		if(imageDataNode.getNodeName() != "ImageData")
		{
			throw new RTMLException(this.getClass().getName()+":parseImageDataNode:Illegal Node Name:"+
						imageDataNode.getNodeName());
		}
		// go through attribute list
		attributeList = imageDataNode.getAttributes();
		// type
		// diddly attributeNode = attributeList.getNamedItem("type");
		//type = attributeNode.getNodeValue();
		//imageData.setImageDataType(type);
		// go through child nodes
		childList = imageDataNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "FITSHeader")
				{
					s = parseStringNode("FITSHeader",childNode);
					imageData.setFITSHeader(s);
				}
				else if(childNode.getNodeName() == "Uri")
				{
					 s = parseStringNode("Uri",childNode);
					 imageData.setImageDataURL(s);
					 imageData.setImageDataType("FITS16");
				}
			}
		}
	}

	/**
	 * Internal method to parse a SourceCatalogue node.
	 * @param imageData The instance of RTMLImageData to set the image data for.
	 * @param sourceCatalogueNode The XML DOM node for the SourceCatalogue tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	private void parseSourceCatalogueNode(RTMLImageData imageData,Node sourceCatalogueNode) throws 
		RTMLException
	{
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String type = null;

		// check current XML node is correct
		if(sourceCatalogueNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseSourceCatalogueNode:Illegal Node:"+
						sourceCatalogueNode);
		}
		if(sourceCatalogueNode.getNodeName() != "SourceCatalogue")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseSourceCatalogueNode:Illegal Node Name:"+
						sourceCatalogueNode.getNodeName());
		}
		// go through attribute list
		attributeList = sourceCatalogueNode.getAttributes();
		// type
		attributeNode = attributeList.getNamedItem("type");
		if(attributeNode ==null)
		{
			throw new RTMLException(this.getClass().getName()+
						":parseSourceCatalogueNode:SourceCatalogue has no type attribute.");
		}			
		type = attributeNode.getNodeValue();
		// According to RTML 3.1a schema, SourceCatalogue type can be "text" (cluster) or "other" (votable-url)
		// imageData.setObjectListType accepts "cluster" (=text) or "votable-url" (=other)
		// Do the translation here
		if(type ==null)
		{
			throw new RTMLException(this.getClass().getName()+
						":parseSourceCatalogueNode:Unknown SourceCatalogue type attribute.");
		}			
		else if(type.equals("text"))
		{
			imageData.setObjectListType("cluster");
		}
		else if(type.equals("other"))
		{
			imageData.setObjectListType("votable-url");
		}
		else
		{
			throw new RTMLException(this.getClass().getName()+
						":parseSourceCatalogueNode:Unknown SourceCatalogue type attribute:"+
						type);
		}
		//imageData.setImageDataType(type);
		// go through child nodes
		childList = sourceCatalogueNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				if(childNode.getNodeValue() != null)
				{
					// ensure it is not all whitespace
					if(childNode.getNodeValue().trim().length() > 0)
					{
						if(type.equals("text"))// cluster
						{
							imageData.setObjectListCluster(childNode.getNodeValue());
						}
						else if(type.equals("other"))// votable-url
						{
							imageData.setObjectListVOTableURL(childNode.getNodeValue());
						}
					}
				}
			}
		}
	}

	/**
	 * Internal method to parse an ObjectList node.
	 * @param imageData The instance of RTMLImageData to set the object list for.
	 * @param objectListNode The XML DOM node for the ObjectList tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	private void parseObjectListNode(RTMLImageData imageData,Node objectListNode) throws RTMLException
	{
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String units = null,type = null;

		// check current XML node is correct
		if(objectListNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseObjectListNode:Illegal Node:"+
						objectListNode);
		}
		if(objectListNode.getNodeName() != "ObjectList")
		{
			throw new RTMLException(this.getClass().getName()+":parseObjectListNode:Illegal Node Name:"+
						objectListNode.getNodeName());
		}
		// go through attribute list
		attributeList = objectListNode.getAttributes();
		// type
		attributeNode = attributeList.getNamedItem("type");
		if(attributeNode == null)
		{
			throw new RTMLException(this.getClass().getName()+
						":parseObjectListNode:type attribute does not exist.");
		}
		type = attributeNode.getNodeValue();
		imageData.setObjectListType(type);
		// go through child nodes
		childList = objectListNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				if(type.equals("cluster"))
					imageData.setObjectListCluster(childNode.getNodeValue());
				if(type.equals("votable-url"))
				{
					// ensure it is not all whitespace
					if(childNode.getNodeValue().trim().length() > 0)
						imageData.setObjectListVOTableURL(childNode.getNodeValue());
				}
			}
		}
	}

	/**
	 * Internal method to parse an Scoring node.
	 * @param rtmlDocument The document to add the scores to.
	 * @param scoringNode The XML DOM node for the Scoring tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @see #parseScoreNode
	 */
	private void parseScoringNode(RTMLDocument rtmlDocument,Node scoringNode) throws RTMLException
	{
		Node childNode;
		NodeList childList;

		// check current XML node is correct
		if(scoringNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseScoringNode:Illegal Node:"+
						scoringNode);
		}
		if(scoringNode.getNodeName() != "Scoring")
		{
			throw new RTMLException(this.getClass().getName()+":parseScoringNode:Illegal Node Name:"+
						scoringNode.getNodeName());
		}
		// clear scores list
		rtmlDocument.clearScoresList();
		// go through child nodes
		childList = scoringNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Score")
					parseScoreNode(rtmlDocument,childNode);
				else
					System.err.println("parseScoringNode:ELEMENT:"+childNode);
			}
		}
	}

	/**
	 * Internal method to parse an Score node inside a Scoring tag.
	 * @param rtmlDocument The document with the list to add the score to.
	 * @param scoreNode The XML DOM node for the Score tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @see RTMLDocument#addScore(java.lang.String,java.lang.String,java.lang.String)
	 */
	private void parseScoreNode(RTMLDocument rtmlDocument,Node scoreNode) throws RTMLException
	{
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String delayString,probabilityString,cumulativeString;

		// check current XML node is correct
		if(scoreNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseScoreNode:Illegal Node:"+
						scoreNode);
		}
		if(scoreNode.getNodeName() != "Score")
		{
			throw new RTMLException(this.getClass().getName()+":parseScoreNode:Illegal Node Name:"+
						scoreNode.getNodeName());
		}
		// go through attribute list
		attributeList = scoreNode.getAttributes();
		// delay
		attributeNode = attributeList.getNamedItem("delay");
		if(attributeNode == null)
		{
			throw new RTMLException(this.getClass().getName()+
						":parseScoreNode:delay attribute does not exist.");
		}
		delayString = attributeNode.getNodeValue();
		// probability
		attributeNode = attributeList.getNamedItem("probability");
		if(attributeNode == null)
		{
			throw new RTMLException(this.getClass().getName()+
						":parseScoreNode:probability attribute does not exist.");
		}
		probabilityString = attributeNode.getNodeValue();
		// cumulative
		attributeNode = attributeList.getNamedItem("cumulative");
		if(attributeNode == null)
		{
			throw new RTMLException(this.getClass().getName()+
						":parseScoreNode:cumulative attribute does not exist.");
		}
		cumulativeString = attributeNode.getNodeValue();
		// parse and add score data to list
		rtmlDocument.addScore(delayString,probabilityString,cumulativeString);
	}

	/**
	 * Parse the id, ref, and uref RTMLAttributes out of the specified node.
	 * @param attributes An instance of RTMLAttributes (or subclass thereof) to store the parsed attributes.
	 * @param attributeList The XML attribute list to query.
	 * @exception RTMLException Thrown if the attributes or attributeList are null.
	 */
	private void parseRTMLAttributes(RTMLAttributes attributes,NamedNodeMap attributeList) throws RTMLException
	{
		Node attributeNode;
		String s = null;

		if(attributeList == null)
		{
			throw new RTMLException(this.getClass().getName()+":parseRTMLAttributes:List was null.");
		}
		if(attributes == null)
		{
			throw new RTMLException(this.getClass().getName()+":parseRTMLAttributes:Attributes was null.");
		}
		// id
		attributeNode = attributeList.getNamedItem("id");
		if(attributeNode != null)
		{
			s = attributeNode.getNodeValue();
			if(s != null)
				attributes.setId(s);
		}
		// ref
		attributeNode = attributeList.getNamedItem("ref");
		if(attributeNode != null)
		{
			s = attributeNode.getNodeValue();
			if(s != null)
				attributes.setRef(s);
		}
		// uref
		attributeNode = attributeList.getNamedItem("uref");
		if(attributeNode != null)
		{
			s = attributeNode.getNodeValue();
			if(s != null)
				attributes.setURef(s);
		}		
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.7  2012/05/24 14:09:01  cjm
** Changed parseSkyConstraintNode so Flux and Units sub elements are parsed correctly, as part
** os RCS sky brightness changes.
**
** Revision 1.6  2011/02/09 18:42:38  cjm
** Added airmass and extinction constraint parsing.
**
** Revision 1.5  2009/08/12 17:52:57  cjm
** Now parses default document-wide target.
** Now parses Grating in Setup, for FrodoSpec grating names.
** Now parses TargetBrightness in Target, for giving a hint as to what acquisition mode to use.
**
** Revision 1.4  2009/03/27 11:38:37  cjm
** Added extra check to parseSourceCatalogueNode if type attribute does not exist (is null).
**
** Revision 1.3  2008/08/11 13:54:54  cjm
** Added Target RA/Dec offset parsing.
**
** Revision 1.2  2008/06/05 14:25:45  cjm
** Added Telescope and Telescope Location support.
**
** Revision 1.1  2008/05/23 14:09:04  cjm
** Initial revision
**
*/
