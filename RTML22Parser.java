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
// RTML22Parser.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTML22Parser.java,v 1.32 2012-05-24 14:06:57 cjm Exp $
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
 * This class provides the capability of parsing an RTML document into a DOM tree, using JAXP.
 * The resultant DOM tree is traversed, and relevant eSTAR data extracted.
 * Extends RTMLParser to make use of methods common to this and RTML31Parser (parseIntegerNode etc), even though
 * this subclass is created as part of the RTMLParser's parsing.
 * @author Chris Mottram
 * @version $Revision: 1.32 $
 */
public class RTML22Parser extends RTMLParser
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTML22Parser.java,v 1.32 2012-05-24 14:06:57 cjm Exp $";

	/**
	 * Default constructor.
	 */
	public RTML22Parser()
	{
		super();
	}

	/**
	 * Method to parse the RTML node.
	 * @param rtmlNode The XML DOM node for the RTML tag node.
	 * @return An instance of RTMLDocument, containing the data in the document.
	 * @exception RTMLException Thrown if a strange child is in the node, 
	 *            or the score/completion time fails to parse.
	 * @see #parseIntelligentAgentNode
	 * @see #parseDeviceNode
	 * @see #parseObservationNode
	 * @see #parseScoreNode
	 * @see #parseScoresNode
	 * @see #parseCompletionTimeNode
	 * @see #parseTelescopeNode
	 * @see #parseContactNode
	 * @see #parseProjectNode
	 */
	protected void parseRTMLNode(Node rtmlNode,RTMLDocument rtmlDocument) throws RTMLException
	{
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String type = null;
		String version = null;

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
		if(version.equals(RTMLDocument.RTML_VERSION_22) == false)
		{
			throw new RTMLException(this.getClass().getName()+":parseRTMLNode:Unsupported Version:"+
						version);
		}
		// type (mode in RTML 3.1)
		attributeNode = attributeList.getNamedItem("type");
		type = attributeNode.getNodeValue();
		rtmlDocument.setType(type);
		// go through child nodes
		childList = rtmlNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Contact")
					parseContactNode(rtmlDocument,childNode);
				else if(childNode.getNodeName() == "Project")
					parseProjectNode(rtmlDocument,childNode);
				else if(childNode.getNodeName() == "IntelligentAgent")
					parseIntelligentAgentNode(rtmlDocument,childNode);
				else if(childNode.getNodeName() == "Device")
					parseDeviceNode(rtmlDocument,childNode);
				else if(childNode.getNodeName() == "Observation")
					parseObservationNode(rtmlDocument,childNode);
				else if(childNode.getNodeName() == "Score")
					parseScoreNode(rtmlDocument,childNode);
				else if(childNode.getNodeName() == "Scores")
					parseScoresNode(rtmlDocument,childNode);
				else if(childNode.getNodeName() == "Telescope")
					parseTelescopeNode(rtmlDocument,childNode);
				else if(childNode.getNodeName() == "CompletionTime")
					parseCompletionTimeNode(rtmlDocument,childNode);
			}
			else if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				if((type.equals("reject"))||(type.equals("fail"))||(type.equals("abort")))
					rtmlDocument.setErrorString(childNode.getNodeValue());
			}
		}
	}

	// private methods
	/**
	 * Internal method to parse an Contact node.
	 * @param rtmlDocument The document to add the Contact to.
	 * @param contactNode The XML DOM node for the Contact tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 */
	private void parseContactNode(RTMLDocument rtmlDocument,Node contactNode) throws RTMLException
	{
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

		// go through child nodes
		childList = contactNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "User")
					parseUserNode(contact, childNode);
				else if(childNode.getNodeName() == "Name")
					parseNameNode(contact, childNode);
				else if(childNode.getNodeName() == "Institution")
					parseInstitutionNode(contact, childNode);
				else if(childNode.getNodeName() == "Address")
					parseAddressNode(contact, childNode);
				else if(childNode.getNodeName() == "Telephone")
					parseTelephoneNode(contact, childNode);
				else if(childNode.getNodeName() == "Fax")
					parseFaxNode(contact, childNode);
				else if(childNode.getNodeName() == "Email")
					parseEmailNode(contact, childNode);
				else if(childNode.getNodeName() == "Url")
					parseUrlNode(contact, childNode);
				else
					System.err.println("parseContactNode:ELEMENT:"+childNode);
			}
		}
		// Set contact in RTML document.
		rtmlDocument.setContact(contact);
	}

	/**
	 * Internal method to parse an Project node.
	 * @param rtmlDocument The document to add the Project to.
	 * @param projectNode The XML DOM node for the Project tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 */
	private void parseProjectNode(RTMLDocument rtmlDocument,Node projectNode) throws RTMLException
	{
		Node childNode;
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
		// go through child nodes
		childList = projectNode.getChildNodes();
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
						project.setProject(childNode.getNodeValue());
					}
				}
			}
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				//if(childNode.getNodeName() == "User")
				//	parseUserNode(contact, childNode);
				//else
					System.err.println("parseProjectNode:ELEMENT:"+childNode);
			}
		}
		// Set project in RTML document.
		rtmlDocument.setProject(project);
	}

	/**
	 * Internal method to parse a Contact.User node
	 * @param contact the instance of Contact to set the User for.
	 * @param userNode The XML DOM node for the User tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	private void parseUserNode(RTMLContact contact, Node userNode)
		throws RTMLException
	{
		Node childNode;
		NodeList childList;

		// check current XML node is correct
		if(userNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException
				(this.getClass().getName()+":parseNameNode:Illegal Node:"+userNode);
		}
		if(userNode.getNodeName() != "User")
		{
			throw new RTMLException
				(this.getClass().getName()+":parseNameNode:Illegal Node Name:"+
				  userNode.getNodeName());
		}

		// go through child nodes
		childList = userNode.getChildNodes();
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
						contact.setUser(childNode.getNodeValue());
					}
				}
			}
		}
	}


	/**
	 * Internal method to parse a Contact.Name node
	 * @param contact the instance of Contact to set the Name for.
	 * @param nameNode The XML DOM node for the Name tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	private void parseNameNode(RTMLContact contact, Node nameNode)
		throws RTMLException
	{
		Node childNode;
		NodeList childList;

		// check current XML node is correct
		if(nameNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException
				(this.getClass().getName()+":parseNameNode:Illegal Node:"+nameNode);
		}
		if(nameNode.getNodeName() != "Name")
		{
			throw new RTMLException
				(this.getClass().getName()+":parseNameNode:Illegal Node Name:"+
				  nameNode.getNodeName());
		}

		// go through child nodes
		childList = nameNode.getChildNodes();
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
						contact.setName(childNode.getNodeValue());
					}
				}
			}
		}
	}


	/**
	 * Internal method to parse a Contact.Institution node
	 * @param contact the instance of Contact to set the Institution for.
	 * @param institutionNode The XML DOM node for the Institution tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	private void parseInstitutionNode(RTMLContact contact, Node institutionNode)
		throws RTMLException
	{
		Node childNode;
		NodeList childList;

		// check current XML node is correct
		if(institutionNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException
				(this.getClass().getName()+":parseInstitutionNode:Illegal Node:"+institutionNode);
		}
		if(institutionNode.getNodeName() != "Institution")
		{
			throw new RTMLException
				(this.getClass().getName()+":parseInstitutionNode:Illegal Node Institution:"+
				  institutionNode.getNodeName());
		}

		// go through child nodes
		childList = institutionNode.getChildNodes();
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
						contact.setInstitution(childNode.getNodeValue());
					}
				}
			}
		}
	}


	/**
	 * Internal method to parse a Contact.Address node
	 * @param contact the instance of Contact to set the Address for.
	 * @param addressNode The XML DOM node for the Address tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	private void parseAddressNode(RTMLContact contact, Node addressNode)
		throws RTMLException
	{
		Node childNode;
		NodeList childList;

		// check current XML node is correct
		if(addressNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException
				(this.getClass().getName()+":parseAddressNode:Illegal Node:"+addressNode);
		}
		if(addressNode.getNodeName() != "Address")
		{
			throw new RTMLException
				(this.getClass().getName()+":parseAddressNode:Illegal Node Address:"+
				  addressNode.getNodeName());
		}

		// go through child nodes
		childList = addressNode.getChildNodes();
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
						contact.setAddress(childNode.getNodeValue());
					}
				}
			}
		}
	}


	/**
	 * Internal method to parse a Contact.Telephone node
	 * @param contact the instance of Contact to set the Telephone for.
	 * @param telephoneNode The XML DOM node for the Telephone tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	private void parseTelephoneNode(RTMLContact contact, Node telephoneNode)
		throws RTMLException
	{
		Node childNode;
		NodeList childList;

		// check current XML node is correct
		if(telephoneNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException
				(this.getClass().getName()+":parseTelephoneNode:Illegal Node:"+telephoneNode);
		}
		if(telephoneNode.getNodeName() != "Telephone")
		{
			throw new RTMLException
				(this.getClass().getName()+":parseTelephoneNode:Illegal Node Telephone:"+
				  telephoneNode.getNodeName());
		}

		// go through child nodes
		childList = telephoneNode.getChildNodes();
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
						contact.setTelephone(childNode.getNodeValue());
					}
				}
			}
		}
	}


	/**
	 * Internal method to parse a Contact.Fax node
	 * @param contact the instance of Contact to set the Fax for.
	 * @param faxNode The XML DOM node for the Fax tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	private void parseFaxNode(RTMLContact contact, Node faxNode)
		throws RTMLException
	{
		Node childNode;
		NodeList childList;

		// check current XML node is correct
		if(faxNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException
				(this.getClass().getName()+":parseFaxNode:Illegal Node:"+faxNode);
		}
		if(faxNode.getNodeName() != "Fax")
		{
			throw new RTMLException
				(this.getClass().getName()+":parseFaxNode:Illegal Node Fax:"+
				  faxNode.getNodeName());
		}

		// go through child nodes
		childList = faxNode.getChildNodes();
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
						contact.setFax(childNode.getNodeValue());
					}
				}
			}
		}
	}

	/**
	 * Internal method to parse a Contact.Email node
	 * @param contact the instance of Contact to set the Email for.
	 * @param emailNode The XML DOM node for the Email tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	private void parseEmailNode(RTMLContact contact, Node emailNode)
		throws RTMLException
	{
		Node childNode;
		NodeList childList;

		// check current XML node is correct
		if(emailNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException
				(this.getClass().getName()+":parseEmailNode:Illegal Node:"+emailNode);
		}
		if(emailNode.getNodeName() != "Email")
		{
			throw new RTMLException
				(this.getClass().getName()+":parseEmailNode:Illegal Node Email:"+
				  emailNode.getNodeName());
		}

		// go through child nodes
		childList = emailNode.getChildNodes();
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
						contact.setEmail(childNode.getNodeValue());
					}
				}
			}
		}
	}


	/**
	 * Internal method to parse a Contact.Url node
	 * @param contact the instance of Contact to set the Url for.
	 * @param urlNode The XML DOM node for the Url tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	private void parseUrlNode(RTMLContact contact, Node urlNode)
		throws RTMLException
	{
		Node childNode;
		NodeList childList;

		// check current XML node is correct
		if(urlNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException
				(this.getClass().getName()+":parseUrlNode:Illegal Node:"+urlNode);
		}
		if(urlNode.getNodeName() != "Url")
		{
			throw new RTMLException
				(this.getClass().getName()+":parseUrlNode:Illegal Node Url:"+
				  urlNode.getNodeName());
		}

		// go through child nodes
		childList = urlNode.getChildNodes();
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
						URL url = null;
						String urlString = childNode.getNodeValue();

						try
						{
							url = new URL(urlString);
						}
						catch(MalformedURLException e)
						{
							throw new RTMLException
							(this.getClass().getName()+":parseUrlNode:Illegal URL ["+
							  urlString+"]", e);
						}
						contact.setUrl(url);
					}
				}
			}
		}
	}

	/**
	 * Internal method to parse an Telescope node.
	 * @param rtmlDocument The document to add the Telescope to.
	 * @param telescopeNode The XML DOM node for the Telescope tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @see #parseStringNode
	 * @see #parseApertureNode
	 * @see #parseFocalLengthNode
	 * @see #parseStringNode
	 * @see #parseTelescopeLocationNode
	 * @see org.estar.rtml.RTMLTelescope
	 * @see org.estar.rtml.RTMLTelescope#setName
	 * @see org.estar.rtml.RTMLTelescope#setFocalRatio
	 * @see org.estar.rtml.RTMLDocument#setTelescope
	 */
	private void parseTelescopeNode(RTMLDocument rtmlDocument,Node telescopeNode) throws RTMLException
	{
		Node childNode;
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
		// add telescope node
		RTMLTelescope telescope = new RTMLTelescope();
		// go through child nodes
		childList = telescopeNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Name")
					telescope.setName(parseStringNode("Name",childNode));
				else if(childNode.getNodeName() == "Aperture")
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
		Node childNode;
		NodeList childList;
		String s = null;

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
		// go through child nodes
		childList = locationNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Name")
					location.setName(parseStringNode("Name",childNode));
				else if(childNode.getNodeName() == "Longitude")
				{
					// assume value is a double in degrees, east of Greenwich
					// In the format "d.ddd E"
					// we could check format/units attribute here
					// This will work with default created document with this library
					s = parseStringNode("Longitude",childNode);
					// if string ends with " E" strip this to get parsable double
					if(s.endsWith(" E"))
						s = s.substring(0,s.length()-2);
					location.setLongitude(s);
				}
				else if(childNode.getNodeName() == "Latitude")
				{
					// assume value is a double in degrees
					// we could check format/units attribute here
					// This will work with default created document with this library
					location.setLatitude(parseStringNode("Latitude",childNode));
				}
				else if(childNode.getNodeName() == "Altitude")
				{
					// assume value is a double in meters
					// we could check units attribute here
					// This will work with default created document with this library
					location.setAltitude(parseStringNode("Altitude",childNode));
				}
				else
					System.err.println("parseTelescopeLocationNode:ELEMENT:"+childNode);
			}
		}
		// Set location in telescope.
		telescope.setLocation(location);
	}

	/**
	 * Internal method to parse an IntelligentAgent node.
	 * @param rtmlDocument The document to add the intelligent agent to.
	 * @param intelligentAgentNode The XML DOM node for the IntelligentAgent tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 */
	private void parseIntelligentAgentNode(RTMLDocument rtmlDocument,Node intelligentAgentNode) 
		throws RTMLException
	{
		RTMLIntelligentAgent intelligentAgent = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;

		// check current XML node is correct
		if(intelligentAgentNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseIntelligentAgentNode:Illegal Node:"+
						intelligentAgentNode);
		}
		if(intelligentAgentNode.getNodeName() != "IntelligentAgent")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseIntelligentAgentNode:Illegal Node Name:"+
						intelligentAgentNode.getNodeName());
		}
		// add intelligentAgent node
		intelligentAgent = new RTMLIntelligentAgent();
		// go through attribute list
		attributeList = intelligentAgentNode.getAttributes();
		// host
		attributeNode = attributeList.getNamedItem("host");
		if(attributeNode != null)
			intelligentAgent.setHostname(attributeNode.getNodeValue());
		else
			intelligentAgent.setHostname(null);
		// port
		attributeNode = attributeList.getNamedItem("port");
		if(attributeNode != null)
			intelligentAgent.setPort(attributeNode.getNodeValue());
		else
			intelligentAgent.setPort(0);
		// go through child nodes
		childList = intelligentAgentNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				if(childNode.getNodeValue() != null)
				{
					if(childNode.getNodeValue().trim().length() > 0)
					{
						intelligentAgent.setId(childNode.getNodeValue());
					}
				}
			}
		}
		// set intelligentAgent in RTML document.
		rtmlDocument.setIntelligentAgent(intelligentAgent);
	}

	/**
	 * Internal method to parse a Device node.
	 * @param rtmlDeviceHolder The object to add the device to - either an instance of RTMLDocument or 
	 *                         RTMLObservation.
	 * @param deviceNode The XML DOM node for the IntelligentAgent tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @see #parseFilterNode
	 * @see #parseDetectorNode
	 * @see #parseGratingNode
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
		// type
		attributeNode = attributeList.getNamedItem("type");
		if(attributeNode != null)
			device.setType(attributeNode.getNodeValue());
		// spectral region
		attributeNode = attributeList.getNamedItem("region");
		if(attributeNode != null)
			device.setSpectralRegion(attributeNode.getNodeValue());
		// go through child nodes
		childList = deviceNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Filter")
					parseFilterNode(device,childNode);
				if(childNode.getNodeName() == "Detector")
					parseDetectorNode(device,childNode);
				if(childNode.getNodeName() == "Grating")
					parseGratingNode(device,childNode);
			}
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				if(childNode.getNodeValue() != null)
				{
					if(childNode.getNodeValue().trim().length() > 0)
					{
						device.setName(childNode.getNodeValue());
					}
				}
			}
		}
		// set device in device holder (RTML document/RTML Observation).
		rtmlDeviceHolder.setDevice(device);
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
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode,filterTypeNode;
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
		// go through child nodes
		childList = filterNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "FilterType")
					parseFilterTypeNode(device,childNode);
			}
		}
	}

	/**
	 * Internal method to parse a FilterType node.
	 * @param device The device to add the filter type to.
	 * @param filterTypeNode The XML DOM node for the FilterType tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 */
	private void parseFilterTypeNode(RTMLDevice device,Node filterTypeNode) 
		throws RTMLException
	{
		Node childNode,attributeNode;
		NodeList childList;

		// check current XML node is correct
		if(filterTypeNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseFilterTypeNode:Illegal Node:"+
						filterTypeNode);
		}
		if(filterTypeNode.getNodeName() != "FilterType")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseFilterTypeNode:Illegal Node Name:"+
						filterTypeNode.getNodeName());
		}
		// go through child nodes
		childList = filterTypeNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				if(childNode.getNodeValue() != null)
				{
					if(childNode.getNodeValue().trim().length() > 0)
					{
						device.setFilterType(childNode.getNodeValue());
					}
				}
			}
		}
	}

	/**
	 * Internal method to parse a Detector node.
	 * @param device The device to add the detector to.
	 * @param detectorNode The XML DOM node for the Detector tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 */
	private void parseDetectorNode(RTMLDevice device,Node detectorNode) 
		throws RTMLException
	{
		RTMLDetector detector = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode,binningNode;
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
				{
					binningNode = childNode;
					// go through attribute list
					attributeList = binningNode.getAttributes();
					// row binning
					attributeNode = attributeList.getNamedItem("rows");
					if(attributeNode != null)
						detector.setRowBinning(attributeNode.getNodeValue());
					// column binning
					attributeNode = attributeList.getNamedItem("columns");
					if(attributeNode != null)
						detector.setColumnBinning(attributeNode.getNodeValue());
				}
			}
		}
		// set detector in device.
		device.setDetector(detector);
	}

	/**
	 * Internal method to parse a Grating node.
	 * @param device The device to add the grating to.
	 * @param gratingNode The XML DOM node for the Grating tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @see org.estar.rtml.RTMLGrating
	 * @see org.estar.rtml.RTMLGrating#setName
	 * @see org.estar.rtml.RTMLGrating#setWavelength
	 * @see org.estar.rtml.RTMLGrating#setWavelengthUnits
	 * @see org.estar.rtml.RTMLGrating#setResolution
	 * @see org.estar.rtml.RTMLGrating#setAngle
	 */
	private void parseGratingNode(RTMLDevice device,Node gratingNode) 
		throws RTMLException
	{
		RTMLGrating grating = null;
		NamedNodeMap attributeList = null;
		Node attributeNode;

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
		// add Grating node
		grating = new RTMLGrating();
		// go through attribute list
		attributeList = gratingNode.getAttributes();
		// name
		attributeNode = attributeList.getNamedItem("name");
		if(attributeNode != null)
			grating.setName(attributeNode.getNodeValue());
		// wavelength
		attributeNode = attributeList.getNamedItem("wavelength");
		if(attributeNode != null)
			grating.setWavelength(attributeNode.getNodeValue());
		// wavelengthUnits
		attributeNode = attributeList.getNamedItem("units");
		if(attributeNode != null)
			grating.setWavelengthUnits(attributeNode.getNodeValue());
		// resolution
		attributeNode = attributeList.getNamedItem("resolution");
		if(attributeNode != null)
			grating.setResolution(attributeNode.getNodeValue());
		// angle
		attributeNode = attributeList.getNamedItem("angle");
		if(attributeNode != null)
			grating.setAngle(attributeNode.getNodeValue());
		// set grating in device.
		device.setGrating(grating);
	}

	/**
	 * Internal method to parse an Observation node.
	 * @param rtmlDocument The document to add the observation to.
	 * @param observationNode The XML DOM node for the Observation tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @see #parseTargetNode
	 * @see #parseScheduleNode
	 * @see #parseImageDataNode
	 */
	private void parseObservationNode(RTMLDocument rtmlDocument,Node observationNode) throws RTMLException
	{
		RTMLObservation observation = null;
		Node childNode;
		NodeList childList;

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
		// add observation node
		observation = new RTMLObservation();		
		// go through child nodes
		childList = observationNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Device")
					parseDeviceNode(observation,childNode);
				else if(childNode.getNodeName() == "Target")
					parseTargetNode(observation,childNode);
				else if(childNode.getNodeName() == "Schedule")
					parseScheduleNode(observation,childNode);
				else if(childNode.getNodeName() == "ImageData")
					parseImageDataNode(observation,childNode);
				else
					System.err.println("parseObservationNode:ELEMENT:"+childNode);
			}
		}
		// add observation to RTML document.
		rtmlDocument.addObservation(observation);
	}

	/**
	 * Internal method to parse an Target node.
	 * @param observation The instance of RTMLObservation to set the target for.
	 * @param targetNode The XML DOM node for the Target tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @see #parseTargetNameNode
	 * @see #parseCoordinatesNode
	 */
	private void parseTargetNode(RTMLObservation observation,Node targetNode) throws RTMLException
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
		// type
		attributeNode = attributeList.getNamedItem("type");
		target.setType(attributeNode.getNodeValue());
		// ident
		attributeNode = attributeList.getNamedItem("ident");
		if(attributeNode != null)
			target.setIdent(attributeNode.getNodeValue());
		else
			target.setIdent(null);
		// go through child nodes
		childList = targetNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "TargetName")
					parseTargetNameNode(target,childNode);
				else if(childNode.getNodeName() == "Coordinates")
					parseCoordinatesNode(target,childNode);
				else
					System.err.println("parseTargetNode:ELEMENT:"+childNode);
			}
		}
		// add target to observation.
		observation.setTarget(target);
	}

	/**
	 * Internal method to parse an TargetName node.
	 * @param target The instance of RTMLTarget to set the name for.
	 * @param targetNameNode The XML DOM node for the TargetName tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 */
	private void parseTargetNameNode(RTMLTarget target,Node targetNameNode) throws RTMLException
	{
		Node childNode;
		NodeList childList;

		// check current XML node is correct
		if(targetNameNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseTargetNameNode:Illegal Node:"+
						targetNameNode);
		}
		if(targetNameNode.getNodeName() != "TargetName")
		{
			throw new RTMLException(this.getClass().getName()+":parseTargetNameNode:Illegal Node Name:"+
						targetNameNode.getNodeName());
		}
		// go through child nodes
		childList = targetNameNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				if(childNode.getNodeValue() != null)
				{
					if(childNode.getNodeValue().trim().length() > 0)
					{
						target.setName(childNode.getNodeValue());
					}
				}
			}
		}
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
	 * @see #parseAngleOffsetNode
	 */
	private void parseRightAscensionNode(RTMLTarget target,Node rightAscensionNode) throws RTMLException
	{
		RA ra = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String units = null;
		String valueString = null;
		double radians;

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
		// go through attribute list
		attributeList = rightAscensionNode.getAttributes();
		// type
		attributeNode = attributeList.getNamedItem("units");
		units = attributeNode.getNodeValue();
		// go through child nodes
		childList = rightAscensionNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "AngleOffset")
				{
					target.setRAOffset(parseAngleOffsetNode(childNode));
				}
			}
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				if(childNode.getNodeValue() != null)
				{
					if(childNode.getNodeValue().trim().length() > 0)
					{
						valueString = childNode.getNodeValue();
						if(units.equals("radians"))
						{
							radians = Double.parseDouble(valueString);
							ra = new RA();
							ra.fromRadians(radians);
							target.setRA(ra);
						}
						else if(units.equals("hms"))
						{
							ra = new RA();
							ra.parseSpace(valueString);
							target.setRA(ra);
						}
						else
						{
							throw new RTMLException(this.getClass().getName()+
								   ":parseRightAscensionNode:Illegal Units:"+units+
										":value:"+valueString);
						}
					}
				}
			}
		}
	}

	/**
	 * Internal method to parse an Declination node.
	 * @param target The instance of RTMLTarget to set the declination for.
	 * @param declinationNode The XML DOM node for the Declination tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @see #parseAngleOffsetNode
	 */
	private void parseDeclinationNode(RTMLTarget target,Node declinationNode) throws RTMLException
	{
		Dec dec = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String units = null;
		String valueString = null;
		double radians;

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
		// go through attribute list
		attributeList = declinationNode.getAttributes();
		// type
		attributeNode = attributeList.getNamedItem("units");
		units = attributeNode.getNodeValue();
		// go through child nodes
		childList = declinationNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "AngleOffset")
				{
					target.setDecOffset(parseAngleOffsetNode(childNode));
				}
			}
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				if(childNode.getNodeValue() != null)
				{
					if(childNode.getNodeValue().trim().length() > 0)
					{
						valueString = childNode.getNodeValue();
						if(units.equals("radians"))
						{
							radians = Double.parseDouble(valueString);
							dec = new Dec();
							dec.fromRadians(radians);
							target.setDec(dec);
						}
						else if(units.equals("dms"))
						{
							dec = new Dec();
							dec.parseSpace(valueString);
							target.setDec(dec);
						}
						else
						{
							throw new RTMLException(this.getClass().getName()+
								       	":parseDeclinationNode:Illegal Units:"+units+
										":value:"+valueString);
						}
					}
				}
			}
		}
	}

	/**
	 * Internal method to parse an Angle offset inside a Right Ascension or a Declination node.
	 * @param angleOffsetNode The XML DOM node for the AngleOffset tag node.
	 * @return A double, the value of the Angle offset in arcseconds.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @see #parseAngleOffsetNode
	 */
	private double parseAngleOffsetNode(Node angleOffsetNode) throws RTMLException
	{
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String units = null;
		String valueString = null;
		double value = 0.0;

		// check current XML node is correct
		if(angleOffsetNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseAngleOffsetNode:Illegal Node:"+
						angleOffsetNode);
		}
		if(angleOffsetNode.getNodeName() != "AngleOffset")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseAngleOffsetNode:Illegal Node Name:"+
						angleOffsetNode.getNodeName());
		}
		// go through attribute list
		attributeList = angleOffsetNode.getAttributes();
		// units
		attributeNode = attributeList.getNamedItem("units");
		units = attributeNode.getNodeValue();
		if(units.equals("arcsec")||units.equals("arcsecs")||units.equals("arcseconds") == false)
		{
			throw new RTMLException(this.getClass().getName()+
						":parseAngleOffsetNode:Units are not arcseconds:"+
						units);
		}
		// go through child nodes
		childList = angleOffsetNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				valueString = childNode.getNodeValue();
				value =  Double.parseDouble(valueString);
			}
		}
		return value;
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
						target.setEquinox(childNode.getNodeValue());
					}
				}
			}
		}
	}

	/**
	 * Internal method to parse a Schedule node.
	 * @param observation The instance of RTMLObservation to set the schedule for.
	 * @param scheduleNode The XML DOM node for the Schedule tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @exception NumberFormatException Thrown if the priority number parsing fails.
	 * @see #parseExposureNode
	 * @see #parseCalibrationNode
	 * @see #parseTimeConstraintNode
	 * @see #parseSeriesConstraintNode
	 * @see #parseSeeingConstraintNode
	 * @see #parseMoonConstraintNode
	 * @see #parseSkyConstraintNode
	 */
	private void parseScheduleNode(RTMLObservation observation,Node scheduleNode) throws RTMLException, 
											     NumberFormatException
	{
		RTMLSchedule schedule = null;
		Node childNode;
		NodeList childList;
		NamedNodeMap attributeList = null;
		Node attributeNode;
		String priorityString = null;

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
		// add schedule node
		schedule = new RTMLSchedule();
		// get schedule attributes
		attributeList = scheduleNode.getAttributes();
		// parse priority attribute
		attributeNode = attributeList.getNamedItem("priority");
		if(attributeNode != null)
		{
			priorityString = attributeNode.getNodeValue();
			if(priorityString != null)
				schedule.setPriority(priorityString);
		}
		// go through child nodes
		childList = scheduleNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Exposure")
					parseExposureNode(schedule,childNode);
				else if(childNode.getNodeName() == "Calibaration")
					parseCalibrationNode(schedule,childNode);
				else if(childNode.getNodeName() == "TimeConstraint")
					parseTimeConstraintNode(schedule,childNode);
				else if(childNode.getNodeName() == "SeriesConstraint")
					parseSeriesConstraintNode(schedule,childNode);
				else if(childNode.getNodeName() == "SeeingConstraint")
					parseSeeingConstraintNode(schedule,childNode);
				else if(childNode.getNodeName() == "MoonConstraint")
					parseMoonConstraintNode(schedule,childNode);
				else if(childNode.getNodeName() == "SkyConstraint")
					parseSkyConstraintNode(schedule,childNode);
			}
		}
		// add scheule to observation
		observation.setSchedule(schedule);
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
		// exposure type
		attributeNode = attributeList.getNamedItem("type");
		type = attributeNode.getNodeValue();
		schedule.setExposureType(type);
		// exposure units
		attributeNode = attributeList.getNamedItem("units");
		units = attributeNode.getNodeValue();
		schedule.setExposureUnits(units);
		// go through child nodes
		childList = exposureNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);

			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Count")
				{
					count = parseIntegerNode(childNode);
					schedule.setExposureCount(count);
				}
			}
			else if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				if(childNode.getNodeValue() != null)
				{
					if(childNode.getNodeValue().trim().length() > 0)
					{
						schedule.setExposureLength(childNode.getNodeValue());
					}
				}
			}
		}
	}

	private void parseCalibrationNode(RTMLSchedule schedule,Node calibrationNode) throws RTMLException
	{
	}

	/**
	 * Internal method to parse a TimeConstraint node.
	 * @param schedule The instance of RTMLSchedule to set the time constrints for.
	 * @param timeConstraintNode The XML DOM node for the TimeConstrint tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @see RTMLSchedule
	 */
	private void parseTimeConstraintNode(RTMLSchedule schedule,Node timeConstraintNode) throws RTMLException
	{
		Node childNode;
		NodeList childList;
		Date date = null;

		// check current XML node is correct
		if(timeConstraintNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseTimeConstraintNode:Illegal Node:"+
						timeConstraintNode);
		}
		if(timeConstraintNode.getNodeName() != "TimeConstraint")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseTimeConstraintNode:Illegal Node Name:"+
						timeConstraintNode.getNodeName());
		}
		// go through child nodes
		childList = timeConstraintNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "StartDateTime")
				{
					date = parseDateTimeNode(childNode);
					schedule.setStartDate(date);
				}
				else if(childNode.getNodeName() == "EndDateTime")
				{
					date = parseDateTimeNode(childNode);
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
		// go through attribute list
		attributeList = moonConstraintNode.getAttributes();
		// distance
		attributeNode = attributeList.getNamedItem("distance");
		if(attributeNode != null)
		{
			s = attributeNode.getNodeValue();
			if(s != null)
				moonConstraint.setDistance(s);
		}
		// units
		attributeNode = attributeList.getNamedItem("units");
		if(attributeNode != null)
		{
			s = attributeNode.getNodeValue();
			if(s != null)
				moonConstraint.setUnits(s);
		}
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
		// go through attribute list
		attributeList = skyConstraintNode.getAttributes();
		// sky 
		attributeNode = attributeList.getNamedItem("sky");
		if(attributeNode != null)
		{
			s = attributeNode.getNodeValue();
			if(s != null)
				skyConstraint.setSky(s);
		}
		// value flux
		attributeNode = attributeList.getNamedItem("flux");
		if(attributeNode != null)
		{
			s = attributeNode.getNodeValue();
			if(s != null)
				skyConstraint.setValue(s);
		}
		// value units
		attributeNode = attributeList.getNamedItem("units");
		if(attributeNode != null)
		{
			s = attributeNode.getNodeValue();
			if(s != null)
				skyConstraint.setUnits(s);
		}
		// add sky constraint to schedule
		schedule.setSkyConstraint(skyConstraint);
	}

	/**
	 * Internal method to parse an ImageData node.
	 * @param observation The instance of RTMLObservation to set the image data for.
	 * @param imageDataNode The XML DOM node for the ImageData tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @see #parseFITSHeaderNode
	 * @see #parseObjectListNode
	 */
	private void parseImageDataNode(RTMLObservation observation,Node imageDataNode) throws RTMLException
	{
		RTMLImageData imageData = null;
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String type = null;

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
		// create new image data
		imageData = new RTMLImageData();
		// go through attribute list
		attributeList = imageDataNode.getAttributes();
		// type
		attributeNode = attributeList.getNamedItem("type");
		type = attributeNode.getNodeValue();
		imageData.setImageDataType(type);
		// go through child nodes
		childList = imageDataNode.getChildNodes();
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
						imageData.setImageDataURL(childNode.getNodeValue());
					}
				}
			}
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "ObjectList")
					parseObjectListNode(imageData,childNode);
				else if(childNode.getNodeName() == "FITSHeader")
					parseFITSHeaderNode(imageData,childNode);
			}
		}
		// add image data to observation
		observation.addImageData(imageData);
	}

	/**
	 * Internal method to parse an FITSHeader node.
	 * @param imageData The instance of RTMLImageData to set the FITS header for.
	 * @param fitsHeaderNode The XML DOM node for the FITSHeader tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	private void parseFITSHeaderNode(RTMLImageData imageData,Node fitsHeaderNode) throws RTMLException
	{
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String type = null;

		// check current XML node is correct
		if(fitsHeaderNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseFITSHeaderNode:Illegal Node:"+
						fitsHeaderNode);
		}
		if(fitsHeaderNode.getNodeName() != "FITSHeader")
		{
			throw new RTMLException(this.getClass().getName()+":parseFITSHeaderNode:Illegal Node Name:"+
						fitsHeaderNode.getNodeName());
		}
		// go through attribute list
		attributeList = fitsHeaderNode.getAttributes();
		// type
		attributeNode = attributeList.getNamedItem("type");
		if(attributeNode == null)
		{
			throw new RTMLException(this.getClass().getName()+
						":parseFITSHeaderNode:type attribute does not exist.");
		}
		type = attributeNode.getNodeValue();
		// go through child nodes
		childList = fitsHeaderNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				imageData.setFITSHeader(childNode.getNodeValue());
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
	 * Internal method to parse an Score node.
	 * @param rtmlDocument The document to set the score in.
	 * @param scoreNode The XML DOM node for the Score tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	private void parseScoreNode(RTMLDocument rtmlDocument,Node scoreNode) throws RTMLException
	{
		Node childNode;
		NodeList childList;

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
		// go through child nodes
		childList = scoreNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				rtmlDocument.setScore(childNode.getNodeValue());
			}
			else
				System.err.println("parseScoreNode:"+childNode);
		}
	}

	/**
	 * Internal method to parse an Scores node.
	 * @param rtmlDocument The document to add the scores to.
	 * @param scoresNode The XML DOM node for the Scores tag node.
	 * @exception RTMLException Thrown if a strange child is in the node.
	 * @see #parseScoresScoreNode
	 */
	private void parseScoresNode(RTMLDocument rtmlDocument,Node scoresNode) throws RTMLException
	{
		Node childNode;
		NodeList childList;

		// check current XML node is correct
		if(scoresNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseScoresNode:Illegal Node:"+
						scoresNode);
		}
		if(scoresNode.getNodeName() != "Scores")
		{
			throw new RTMLException(this.getClass().getName()+":parseScoresNode:Illegal Node Name:"+
						scoresNode.getNodeName());
		}
		// clear scores list
		rtmlDocument.clearScoresList();
		// go through child nodes
		childList = scoresNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				if(childNode.getNodeName() == "Score")
					parseScoresScoreNode(rtmlDocument,childNode);
				else
					System.err.println("parseScoresNode:ELEMENT:"+childNode);
			}
		}
	}

	/**
	 * Internal method to parse an Score node inside a Scores tag.
	 * @param rtmlDocument The document with the list to add the score to.
	 * @param scoreNode The XML DOM node for the Score tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 * @see RTMLDocument#addScore(java.lang.String,java.lang.String,java.lang.String)
	 */
	private void parseScoresScoreNode(RTMLDocument rtmlDocument,Node scoreNode) throws RTMLException
	{
		NamedNodeMap attributeList = null;
		Node childNode,attributeNode;
		NodeList childList;
		String delayString,probabilityString,cumulativeString;

		// check current XML node is correct
		if(scoreNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseScoresScoreNode:Illegal Node:"+
						scoreNode);
		}
		if(scoreNode.getNodeName() != "Score")
		{
			throw new RTMLException(this.getClass().getName()+":parseScoresScoreNode:Illegal Node Name:"+
						scoreNode.getNodeName());
		}
		// go through attribute list
		attributeList = scoreNode.getAttributes();
		// delay
		attributeNode = attributeList.getNamedItem("delay");
		if(attributeNode == null)
		{
			throw new RTMLException(this.getClass().getName()+
						":parseScoresScoreNode:delay attribute does not exist.");
		}
		delayString = attributeNode.getNodeValue();
		// probability
		attributeNode = attributeList.getNamedItem("probability");
		if(attributeNode == null)
		{
			throw new RTMLException(this.getClass().getName()+
						":parseScoresScoreNode:probability attribute does not exist.");
		}
		probabilityString = attributeNode.getNodeValue();
		// cumulative
		attributeNode = attributeList.getNamedItem("cumulative");
		if(attributeNode == null)
		{
			throw new RTMLException(this.getClass().getName()+
						":parseScoresScoreNode:cumulative attribute does not exist.");
		}
		cumulativeString = attributeNode.getNodeValue();
		// parse and add score data to list
		rtmlDocument.addScore(delayString,probabilityString,cumulativeString);
	}

	/**
	 * Internal method to parse an Completion Time node.
	 * @param rtmlDocument The document to set the completion time in.
	 * @param completionTimeNode The XML DOM node for the CompletionTime tag node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	private void parseCompletionTimeNode(RTMLDocument rtmlDocument,Node completionTimeNode) throws RTMLException
	{
		Node childNode;
		NodeList childList;
		String s;

		// check current XML node is correct
		if(completionTimeNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseCompletionTimeNode:Illegal Node:"+
						completionTimeNode);
		}
		if(completionTimeNode.getNodeName() != "CompletionTime")
		{
			throw new RTMLException(this.getClass().getName()+
						":parseCompletionTimeNode:Illegal Node Name:"+
						completionTimeNode.getNodeName());
		}
		// go through child nodes
		childList = completionTimeNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				s = childNode.getNodeValue();
				if(s.equals("never"))
					rtmlDocument.setCompletionTime((Date)null);
				else
					rtmlDocument.setCompletionTime(s);
			}
			else
				System.err.println("parseCompletionTimeNode:"+childNode);
		}
	}

}
/*
** $Log: not supported by cvs2svn $
** Revision 1.31  2008/08/11 13:54:54  cjm
** Added Target RA/Dec offset parsing: parseAngleOffsetNode.
**
** Revision 1.30  2008/06/05 14:26:08  cjm
** Added Telescope and Telescope Location support.
**
** Revision 1.29  2008/05/23 14:08:30  cjm
** New version after RTML 3.1a integration.
** Removed common methods and put into RTMLParser.
**
** Revision 1.28  2008/05/13 10:30:48  cjm
** Initial version of RTML22Parser copied from RTMLParser, based on version 1.27.
**
** Revision 1.27  2008/03/27 17:13:54  cjm
** Added parseGratingNode and call in parseDeviceNode to handle spectrographs.
**
** Revision 1.26  2007/07/09 12:04:23  cjm
** Added parseMoonConstraintNode , parseSkyConstraintNode.
**
** Revision 1.25  2007/07/06 15:17:19  cjm
** IntelligentAgent parsing checks for null attributes.
**
** Revision 1.24  2007/03/27 19:15:39  cjm
** Added Scores handling, added parseScoresNode/parseScoresScoreNode.
**
** Revision 1.23  2007/01/30 18:31:18  cjm
** gnuify: Added GNU General Public License.
**
** Revision 1.22  2006/03/20 16:21:28  cjm
** Updated date time parsing to use RTMLDateFormat.
**
** Revision 1.21  2005/08/19 17:01:09  cjm
** Added VOTable URL support to ImageData.
**
** Revision 1.20  2005/06/20 10:55:04  cjm
** Error string now parsed for documents of type fail and abort, as well as reject.
**
** Revision 1.19  2005/06/08 13:58:00  cjm
** Added parseSeeingConstraintNode.
**
** Revision 1.18  2005/06/08 11:38:09  cjm
** Fixed comments.
**
** Revision 1.17  2005/06/06 10:33:41  cjm
** Added Schedule Priority.
**
** Revision 1.16  2005/06/01 16:30:13  cjm
** Reformatting.
**
** Revision 1.15  2005/05/04 18:58:25  cjm
** Changed parsing of ObjectList, FITSHeader and ImageData nodes,
** to reflect new design of RTMLObservation.
**
** Revision 1.14  2005/04/29 17:18:41  cjm
** Added exposureCount.
**
** Revision 1.13  2005/04/27 15:44:27  cjm
** Added parseSeriesConstraintNode, parseIntegerNode and parsePeriodNode.
**
** Revision 1.12  2005/04/26 11:26:44  cjm
** Added Schedule TimeConstraint parsing.
**
** Revision 1.11  2005/04/25 10:32:36  cjm
** Added parsing of Target ident attribute.
**
** Revision 1.10  2005/01/19 11:52:45  cjm
** Now uses RTMLDeviceHolder in parseDeviceNode, as Device can occur
** in RTMLDocuments and RTMLObservations.
**
** Revision 1.9  2005/01/18 19:37:56  cjm
** FilterType now has type as PCDATA rather than an attribute.
**
** Revision 1.8  2005/01/18 15:17:54  cjm
** Added project node parsing.
**
** Revision 1.7  2004/03/19 16:59:40  cjm
** Fixed bug in device and contact nodes, if attributes do not exist.
** Test attribute is non-null.
**
** Revision 1.6  2004/03/18 17:33:10  cjm
** Added parsing of ImageData "type" attribute.
**
** Revision 1.5  2004/03/12 17:28:35  je
** Added RTMLContact stuff
**
** Revision 1.4  2004/03/12 10:36:35  je
** *** empty log message ***
**
** Revision 1.3  2004/03/11 13:28:18  cjm
** Added parsing of device node.
**
** Revision 1.2  2003/05/19 15:31:53  cjm
** Added completion time "never" parsing.
**
** Revision 1.1  2003/02/24 13:19:56  cjm
** Initial revision
**
*/
