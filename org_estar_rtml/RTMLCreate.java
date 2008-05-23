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
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLCreate.java,v 1.41 2008-05-23 14:14:33 cjm Exp $
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
 * @version $Revision: 1.41 $
 */
public class RTMLCreate
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLCreate.java,v 1.41 2008-05-23 14:14:33 cjm Exp $";
	/**
	 * System ID put into DOCTYPE statement. This is the URL of the RTML DTD.
	 */
        //public final static String DEFAULT_DOCTYPE_SYSTEM_ID = "http://150.204.240.111/~dev/robonet/rtml2.1.dtd";
        //public final static String DEFAULT_DOCTYPE_SYSTEM_ID = "http://www.astro.livjm.ac.uk/~je/rtml2.1.dtd";
        //public final static String DEFAULT_DOCTYPE_SYSTEM_ID = "http://www.estar.org.uk/documents/rtml2.1.dtd";
        public final static String DEFAULT_DOCTYPE_SYSTEM_ID = "http://www.estar.org.uk/documents/rtml2.2.dtd";
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
	 * A copy of the RTMLDocuments version. Used for setting various output transformer properties
	 * (DTD declaration for RTML v2.2).
	 */
	private String rtmlVersion = null;

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
	 * @param rtmlDocument The Java representation of an RTML document.
	 * @see #builder
	 * @see #document
	 * @see #rtmlVersion
	 */
	public void create(RTMLDocument rtmlDocument) throws RTMLException
	{
		RTML22Create create22 = null;
		RTML31Create create31 = null;

		document = builder.newDocument();
		if(rtmlDocument.getVersion() == null)
		{
			throw new RTMLException(this.getClass().getName()+":create:"+"document version was null.");
		}
		else if(rtmlDocument.getVersion().equals(RTMLDocument.RTML_VERSION_22))
		{
			create22 = new RTML22Create();
			create22.create(rtmlDocument,document);
			rtmlVersion = rtmlDocument.getVersion();
		}
		else if(rtmlDocument.getVersion().equals(RTMLDocument.RTML_VERSION_31))
		{
			create31 = new RTML31Create();
			create31.create(rtmlDocument,document);
			rtmlVersion = rtmlDocument.getVersion();
		}
		else
		{
			throw new RTMLException(this.getClass().getName()+":create:"+"Unsupported document version:"+
						rtmlDocument.getVersion());
		}
	}

	/**
	 * Method to send the created XML to a stream.
	 * @see #doctypeSystemID
	 * @see #rtmlVersion
	 * @see org.estar.rtml.RTMLDocument#RTML_VERSION_22
	 */
	public void toStream(OutputStream os) throws RTMLException
	{
		try
		{
			// Use a Transformer for output
			TransformerFactory factory = TransformerFactory.newInstance();
			// Java 1.5
			try
			{
				factory.setAttribute("indent-number",new Integer(2));
			}
			catch(Exception e)
			{
				//Throw Java 1.4 errors away
			}
			Transformer transformer = factory.newTransformer();
			
			// setup DOCTYPE if RTML v2.2.
			if(rtmlVersion.equals(RTMLDocument.RTML_VERSION_22))
				transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,doctypeSystemID);
			transformer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT,"yes");
			// This is the Java 1.4 equivalent of the factory setting above
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(document);
			//StreamResult result = new StreamResult(os);
			// Java 1.5 requires OutputStreamWriter wrapping for indentation to work!
			StreamResult result = new StreamResult(new OutputStreamWriter(os));
			transformer.transform(source, result);
			os.flush();
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
	 * @see #rtmlVersion
	 * @see org.estar.rtml.RTMLDocument#RTML_VERSION_22
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
			if(rtmlVersion.equals(RTMLDocument.RTML_VERSION_22))
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
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.40  2008/03/27 17:14:31  cjm
** Added createGrating for spectrograph support.
**
** Revision 1.39  2007/07/09 11:47:48  cjm
** Added creation of MoonConstraint and SkyConstraint nodes.
**
** Revision 1.38  2007/07/06 15:18:09  cjm
** IntelligentAgent port only added to IntelligentAgent if non-zero.
**
** Revision 1.37  2007/05/04 09:30:17  cjm
** Added stream flush to OutputStream. (toStream method).
**
** Revision 1.36  2007/05/02 09:17:20  snf
** added decimal format for createScore
**
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
