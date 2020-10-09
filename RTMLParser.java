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
// RTMLParser.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLParser.java,v 1.29 2008-06-02 11:21:48 cjm Exp $
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
 * The <b>init</b> method must be called before <b>parse</b>.
 * @see #init
 * @see #parse
 * @author Chris Mottram
 * @version $Revision$
 */
public class RTMLParser
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id$";
	/**
	 * Constant used to configure schema parsing.
	 */
	static final String JAXP_SCHEMA_LANGUAGE ="http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	/**
	 * Constant used to configure schema parsing.
	 */
	static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema"; 
	/**
	 * Constant used to set file RTML schema load.
	 */
	static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
	/**
	 * String specifying a schema file .xsd to parse against, rather than rading the default web-based
	 * xsi:schemaLocation. A class-wide (static) variable.
	 */
	protected static String schemaSource = null;
	/**
	 * Private reference to org.w3c.dom.Document, the head of the DOM tree.
	 */
	private Document document = null;
	/**
	 * The instance of DocumentBuilder, used to build the document tree.
	 */
	private DocumentBuilder builder = null;
	/**
	 * The instance of RTMLErrorHandler, attached to the DocumentBuilder to handle DOM errors.
	 */
	RTMLErrorHandler errorHandler = null;

	/**
	 * Default constructor.
	 */
	public RTMLParser()
	{
		super();
	}

	/**
	 * Initialisation method. Must be called after the constructor, but before <b>parse</b>, to create the
	 * <i>builder</i> used by <b>parse</b>.
	 * @param parseSchema Boolean. If true, set the parser to use Schema. Only works for RTML3.1a.
	 * @exception ParserConfigurationException Thrown if DocumentBuilderFactory.newDocumentBuilder fails.
	 * @exception IllegalArgumentException Thrown by factory.setAttribute if the parser does not support JAXP1.2.
	 * @see #builder
	 * @see #errorHandler
	 * @see #JAXP_SCHEMA_LANGUAGE
	 * @see #W3C_XML_SCHEMA
	 * @see #JAXP_SCHEMA_SOURCE
	 * @see #schemaSource
	 */
	public void init(boolean parseSchema) throws ParserConfigurationException
	{
		DocumentBuilderFactory factory = null;

		// create factory
		factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);   
		factory.setNamespaceAware(true);
		// set factory Schema if configured to do so.
		if(parseSchema)
		{
			factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
			if(schemaSource != null)
				factory.setAttribute(JAXP_SCHEMA_SOURCE,new File(schemaSource));
		}
		// create builder
		builder = factory.newDocumentBuilder();
		errorHandler = new RTMLErrorHandler();
		builder.setErrorHandler(errorHandler);
	}

	/**
	 * Method to a file schema to parse against. Must be set before the RTMLParser instance is created.
	 * Only useful for parsing RTML 3.1a documents.
	 * @param filename A string representing a valid xsd file.
	 * @see #schemaSource
	 */
	public static void setSchemaSource(String filename)
	{
		schemaSource = filename;
	}

	/**
	 * Method to parse an RTML document. Must be called after <b>init</b> so the builder exists.
	 * @param f The file to parse from.
	 * @return An instance of RTMLDocument, containing the data in the document.
	 * @exception RTMLException Thrown if the parsing fails.
	 * @exception ParseException Thrown if the parsing fails.
	 * @see #document
	 * @see #builder
	 * @see #parseDocument
	 */
	public RTMLDocument parse(File f) throws RTMLException, ParseException
	{
		RTMLDocument rtmlDocument = null;

		if(builder == null)
		{
			throw new RTMLException(this.getClass().getName()+
						":parse:RTMLParser.init not called:builder was null.");
		}
		try
		{
			document = builder.parse(f);
		}
		catch(Exception e)
		{
			//SAXException,ParserConfigurationException,IOException
			throw new RTMLException(this.getClass().getName()+":parse:",e);
		}
		rtmlDocument = parseDocument();
		return rtmlDocument;
	}

	/**
	 * Method to parse an RTML document. Must be called after <b>init</b> so the builder exists.
	 * @param i The input stream to parse from.
	 * @return An instance of RTMLDocument, containing the data in the document.
	 * @exception RTMLException Thrown if the parsing fails.
	 * @exception ParseException Thrown if the parsing fails.
	 * @see #document
	 * @see #builder
	 * @see #parseDocument
	 */
	public RTMLDocument parse(InputStream i) throws RTMLException, ParseException
	{
		RTMLDocument rtmlDocument = null;

		if(builder == null)
		{
			throw new RTMLException(this.getClass().getName()+
						":parse:RTMLParser.init not called:builder was null.");
		}
		try
		{
			document = builder.parse(i);
		}
		catch(Exception e)
		{
			//SAXException,ParserConfigurationException,IOException
			throw new RTMLException(this.getClass().getName()+":parse:",e);
		}
		rtmlDocument = parseDocument();
		return rtmlDocument;
	}

	/**
	 * Method to parse an RTML document. Must be called after <b>init</b> so the builder exists.
	 * @param s The string to parse from.
	 * @return An instance of RTMLDocument, containing the data in the document.
	 * @exception RTMLException Thrown if the parsing fails.
	 * @exception ParseException Thrown if the parsing fails.
	 * @see #document
	 * @see #builder
	 * @see #parseDocument
	 */
	public RTMLDocument parse(String s) throws RTMLException, ParseException
	{
		RTMLDocument rtmlDocument = null;
		InputSource is = null;
		StringReader sr = null;

		if(builder == null)
		{
			throw new RTMLException(this.getClass().getName()+
						":parse:RTMLParser.init not called:builder was null.");
		}
		try
		{
			sr = new StringReader(s);
			is = new InputSource(sr);
			document = builder.parse(is);
		}
		catch(Exception e)
		{
			//SAXException,ParserConfigurationException,IOException
			throw new RTMLException(this.getClass().getName()+":parse:",e);
		}
		rtmlDocument = parseDocument();
		return rtmlDocument;
	}

	/**
	 * Method to set a custom error handler. Should be called after init (constructor), before parse is called.
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
	 */
	public RTMLErrorHandler getErrorHandler()
	{
		return errorHandler;
	}

	// protected methods (used by sub-parsers etc)
	/**
	 * Internal method to parse a node containing a String.
	 * @param nodeName The name of the Node.
	 * @param node The XML DOM node for the  node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	protected String parseStringNode(String nodeName, Node node) throws RTMLException
	{
		Node childNode;
		NodeList childList;
		String valueString = null;

		// check current XML node is correct
		if(node.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseStringNode:Illegal Node:"+node);
		}
		if(node.getNodeName() != nodeName)
		{
			throw new RTMLException(this.getClass().getName()+":parseStringNode:Illegal Node Name:"+
				  node.getNodeName()+" did not match expected name "+nodeName);
		}
		// go through child nodes
		childList = node.getChildNodes();
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
						valueString = childNode.getNodeValue().trim();
					}
					else
						valueString = new String("");
				}
			}
		}
		return valueString;
	}

	/**
	 * Parse a child node, containing a text node with an integer number. 
	 * This is a node (such as Count) containing an integer number.
	 * @param integerNode The XML node containing an integer to parse as it's CDATA.
	 * @return The parsed integer is returned. Note if no text is found in the node, 0 is returned.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	protected int parseIntegerNode(Node integerNode) throws RTMLException
	{
		Node childNode;
		NodeList childList;
		String s = null;
		int number = 0;

		// check current XML node is correct
		if(integerNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseIntegerNode:Illegal Node:"+
						integerNode);
		}
		childList = integerNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				s = childNode.getNodeValue();
				try
				{
					number = Integer.parseInt(s);
				}
				catch(NumberFormatException e)
				{
					throw new RTMLException(this.getClass().getName()+
								":parseInteger:Illegal integer:"+s+":",e);
				}
			}
		}
		return number;
	}

	/**
	 * Parse a child node, containing a text node with an double number. 
	 * This is a node (such as Value) containing a double number.
	 * @param doubleNode The XML node containing an double to parse as it's CDATA.
	 * @return The parsed double is returned. Note if no text is found in the node, 0 is returned.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	protected double parseDoubleNode(Node doubleNode) throws RTMLException
	{
		Node childNode;
		NodeList childList;
		String s = null;
		double number = 0;

		// check current XML node is correct
		if(doubleNode.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseDoubleNode:Illegal Node:"+
						doubleNode);
		}
		childList = doubleNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				s = childNode.getNodeValue();
				try
				{
					number = Double.parseDouble(s);
				}
				catch(NumberFormatException e)
				{
					throw new RTMLException(this.getClass().getName()+
								":parseDoubleNode:Illegal double:"+s+":",e);
				}
			}
		}
		return number;
	}

	/**
	 * Parse a child node, containing a text node with a period specification of the form:
	 * <code>
	 * P{(yyyy)Y{(mm)M}{(dd)D}{T{(hh)H}{(mm}M}{(ss.s..)S}
	 * </code>
	 * @param periodNode The XML node containing a period to parse as it's CDATA.
	 * @return The parsed period is returned. Note if no text is found in the node, null is returned.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	protected RTMLPeriodFormat parsePeriodNode(Node periodNode) throws RTMLException
	{
		RTMLPeriodFormat period = null;
		Node childNode;
		NodeList childList;
		String s = null;

		childList = periodNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			if(childNode.getNodeType() == Node.TEXT_NODE)
			{
				s = childNode.getNodeValue();
				period = new RTMLPeriodFormat();
				try
				{
					period.parse(s);
				}
				catch(Exception e)
				{
					throw new RTMLException(this.getClass().getName()+
								":parsePeriodNode:Illegal period:"+s+":",e);
				}
			}
		}
		return period;
	}

	/**
	 * Internal method to parse and return an attribute in a node.
	 * @param nodeName The name of the Node.
	 * @param node The XML DOM node for the  node.
	 * @param attributeName The name of an attribute set in that node.
	 * @return The value as a string of the specified attributte in the specified node.
	 * @exception RTMLException Thrown if a strange child is in the node, or a parse error occurs.
	 */
	protected String parseNodeAttribute(String nodeName, Node node,String attributeName) throws RTMLException
	{
		NamedNodeMap attributeList = null;
		Node attributeNode;
		String valueString = null;

		// check current XML node is correct
		if(node.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new RTMLException(this.getClass().getName()+":parseNodeAttribute:Illegal Node:"+node);
		}
		if(node.getNodeName() != nodeName)
		{
			throw new RTMLException(this.getClass().getName()+":parseNodeAttribute:Illegal Node Name:"+
				  node.getNodeName()+" did not match expected name "+nodeName);
		}
		// go through attribute list
		attributeList = node.getAttributes();
		// get specified attribute
		attributeNode = attributeList.getNamedItem(attributeName);
		if(attributeNode != null)
			valueString = attributeNode.getNodeValue();
		else
			valueString = null;
		return valueString;
	}

	// private methods
	/**
	 * Internal method to parse the root document node.
	 * @return An instance of RTMLDocument, containing the data in the document.
	 * @exception RTMLException Thrown if a strange child is in the node, or the document is not a document.
	 * @exception ParseException Thrown if the parsing fails.
	 * @see #document
	 * @see #parseRTMLNode
	 */
	private RTMLDocument parseDocument() throws RTMLException, ParseException
	{
		RTMLDocument rtmlDocument = null;
		Node domNode,childNode;
		NodeList childList;

		domNode = (Node)document;
		if(domNode.getNodeType() != Node.DOCUMENT_NODE)
			throw new RTMLException(this.getClass().getName()+":parseDocument:Illegal Node"+domNode);
		childList = domNode.getChildNodes();
		for(int i = 0; i < childList.getLength(); i++)
		{
			childNode = childList.item(i);
			switch(childNode.getNodeType())
			{
				case Node.ELEMENT_NODE:
					rtmlDocument = parseRTMLNode(childNode);
					break;
				case Node.DOCUMENT_TYPE_NODE:
					//System.err.println(childNode);
					break;
				case Node.COMMENT_NODE:
					//System.err.println(childNode);
					break;
				default:
					throw new RTMLException(this.getClass().getName()+
								":parseDocument:Illegal Child:"+childNode);
			}
		}
		return rtmlDocument;
	}

	/**
	 * Internal method to parse the RTML node. This checks the XML Node is correct, extracts the version attribute,
	 * and creates and calls the RTML22Parser or RTML31Parser parseRTMLNode method as appropriate.
	 * @param rtmlNode The XML DOM node for the RTML tag node.
	 * @return An instance of RTMLDocument, containing the data in the document.
	 * @exception RTMLException Thrown if a strange child is in the node, 
	 *            or the score/completion time fails to parse.
	 * @exception ParseException Thrown if the parsing fails.
	 * @see org.estar.rtml.RTML22Parser#parseRTMLNode
	 * @see org.estar.rtml.RTML31Parser#parseRTMLNode
	 */
	private RTMLDocument parseRTMLNode(Node rtmlNode) throws RTMLException, ParseException
	{
		RTML22Parser parser22 = null;
		RTML31Parser parser31 = null;
		RTMLDocument rtmlDocument = null;
		NamedNodeMap attributeList = null;
		Node attributeNode;
		String version = null;

		// create document to return
		rtmlDocument = new RTMLDocument();
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
		// version
		attributeNode = attributeList.getNamedItem("version");
		version = attributeNode.getNodeValue();
		if(version == null)
			throw new RTMLException(this.getClass().getName()+":parseRTMLNode:Version was null.");
		rtmlDocument.setVersion(version);
		// depending on version, call required parser.
		if(version.equals(RTMLDocument.RTML_VERSION_22))
		{
			parser22 = new RTML22Parser();
			parser22.parseRTMLNode(rtmlNode,rtmlDocument);
		}
		else if (version.equals(RTMLDocument.RTML_VERSION_31))
		{
			parser31 = new RTML31Parser();
			parser31.parseRTMLNode(rtmlNode,rtmlDocument);
		}
		else
		{
			throw new RTMLException(this.getClass().getName()+":parseRTMLNode:Unsupported version:"+
						version);
		}
		// return created document
		return rtmlDocument;
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.28  2008/05/23 17:05:14  cjm
** New version after RTML 3.1a integration.
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
