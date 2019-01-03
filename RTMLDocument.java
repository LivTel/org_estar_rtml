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
// RTMLDocument.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLDocument.java,v 1.21 2016-06-08 13:57:33 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * This class is a data container for information contained in the base nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.21 $
 * @see org.estar.rtml.RTMLDeviceHolder
 * @see org.estar.rtml.RTMLTargetHolder
 */
public class RTMLDocument implements Serializable, RTMLDeviceHolder, RTMLTargetHolder
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLDocument.java,v 1.21 2016-06-08 13:57:33 cjm Exp $";
	/**
	 * Serial version ID. Fixed as these documents can be used as parameters in RMI calls across JVMs.
	 */
	static final long serialVersionUID = -8805723117207611978L;
	/**
	 * Constant representing the value of the RTML version attribute specifying RTML version 2.2.
	 * @see #version
	 */
	public final static String RTML_VERSION_22 = "2.2";
	/**
	 * Constant representing the value of the RTML version attribute specifying RTML version 3.1a.
	 * @see #version
	 */
	public final static String RTML_VERSION_31 = "3.1a";
	/**
	 * The RTML version of the document.
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public String version = null;
	/**
	 * The type of the document, as specified in the RTML node's "type" attribute, of type %rtmlDocumentModes;.
	 * Only used in RTML 2.2 documents, the equivalent is "mode" in RTML 3.1.
	 * See the DTD for possible values, such as: score, request, confirmation, information, update, observation, 
	 * reject, fail, abort, incomplete.
	 */
	public String type = null;
	/**
	 * The mode of the document, as specified in the RTML node's "mode" attribute.
	 * Only used in RTML 3.1 documents, the equivalent is "type" in RTML 2.2.
	 * See the schema for possible values, such as: abort, acknowledged, complete, confirm, fail, incomplete, 
	 * inquiry, offer, reject, report, request, resource, update .
	 */
	public String mode = null;
	/**
	 * The unique ID of the document, as specified in the RTML node's "uid" attribute.
	 * Only used in RTML 3.1 documents, the equivalent is some element of IntelligentAgent in RTML 2.2.
	 */
	public String uid = null;
	/**
	 * The details contained in the IntelligentAgent tag/node.
	 */
	public RTMLIntelligentAgent intelligentAgent = null;
	/**
	 * History info for this document.
	 */
	protected RTMLHistory history = null;
	/**
	 * Contact info for this document.
	 */
	protected RTMLContact contact = null;
	/**
	 * Project info for this document.
	 */
	protected RTMLProject project = null;
	/**
	 * Telescope info for this document.
	 */
	protected RTMLTelescope telescope = null;
	/**
	 * The instrument device this document wants to use.
	 * This can be null, with each Observation in the document having it's own Device.
	 */
	public RTMLDevice device = null;
	/**
	 * The target of this document.
	 * This can be null, with each Observation in the document having it's own Target.
	 */
	private RTMLTarget target = null;
	/**
	 * List containing observations.
	 */
	public List observationList = null;
	/**
	 * The score of the document. Note this should be a per observation score, according to the DTD.
	 */
	public Double score = null;
	/**
	 * A list of Score tags in a Scores tag, with probability/cumulative probability and delays. 
	 */
	public List scoresList = null;
	/**
	 * The completion time. Note this should be a per observation score, according to the DTD.
	 */
	public Date completionTime = null;
	/**
	 * Error String contained in TEXT node of RTML document, if document has type reject.
	 */
	public String errorString = null;

	/**
	 * Default constructor. Initialise scoresList and observationList.
	 * @see #observationList
	 * @see #scoresList
	 */
	public RTMLDocument()
	{
		super();
		observationList = new Vector();
		scoresList = new Vector();
	}

	/**
	 * Set the RTML version of the document.
	 * @param s The version.
	 * @see #version
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public void setVersion(String s)
	{
		version = s;
	}

	/**
	 * Get the RTML version of the document.
	 * @return The version.
	 * @see #version
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public String getVersion()
	{
		return version;
	}

	/**
	 * Set the type of the document (RTML 2.2).
	 * @param s The type.
	 * @see #type
	 */
	public void setType(String s)
	{
		type = s;
	}

	/**
	 * Get the type of the document.
	 * @return The type.
	 * @see #type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * Set the mode of the document (RTML 3.1).
	 * @param s The mode.
	 * @see #mode
	 */
	public void setMode(String s)
	{
		mode = s;
	}

	/**
	 * Get the mode of the document.
	 * @return The mode.
	 * @see #mode
	 */
	public String getMode()
	{
		return mode;
	}

	/**
	 * Set the Unique ID of the document (RTML 3.1).
	 * @param s The uid.
	 * @see #uid
	 */
	public void setUId(String s)
	{
		uid = s;
	}

	/**
	 * Get the Unique ID of the document. This is retrieved from the intelligent agent ID if that has been set 
	 * (usually from RTML 2.2 documents),
	 * or from the document uid (RTML 3.1a documents). The uid takes preference if it is non-null.
	 * @return The uid.
	 * @see #uid
	 * @see #intelligentAgent
	 */
	public String getUId()
	{
		String s = null;

		// if the intelligent agent data exists
		if(intelligentAgent != null)
		{
			// we can use the id (extracted from the PCDATA) as the uid in RTML2.2 documents
			if(intelligentAgent.getId() != null)
				s = intelligentAgent.getId();
		}
		// RTML 3.1a documents should have the uid attribute of the document itself set
		// This should override the intelligent agent data if it exists
		if(uid != null)
			s = uid;
		return s;
	}

	/**
	 * Is this document a score request document?
	 * @return true if the document is a score request, false otherwise.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public boolean isScoreRequest()
	{
		// if we don't know what version of RTML this doc is, return false.
		if(version == null)
			return false;
		if(version.equals(RTML_VERSION_22) && (type.equals("score")))
			return true;
		if(version.equals(RTML_VERSION_31) && (mode.equals("inquiry")))
			return true;
		return false;
	}

	/**
	 * Is this document a reject document?
	 * @return true if the document is a reject, false otherwise.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public boolean isReject()
	{
		// if we don't know what version of RTML this doc is, return false.
		if(version == null)
			return false;
		if(version.equals(RTML_VERSION_22) && (type.equals("reject")))
			return true;
		if(version.equals(RTML_VERSION_31) && (mode.equals("reject")))
			return true;
		return false;
	}

	/**
	 * Is this document a score reply document?
	 * @return true if the document is a score reply, false otherwise.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public boolean isScoreReply()
	{
		// if we don't know what version of RTML this doc is, return false.
		if(version == null)
			return false;
		if(version.equals(RTML_VERSION_22) && (type.equals("score")))
			return true;
		if(version.equals(RTML_VERSION_31) && (mode.equals("offer")))
			return true;
		return false;
	}

	/**
	 * Is this document an observation request document?
	 * @return true if the document is an observation request, false otherwise.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public boolean isRequest()
	{
		// if we don't know what version of RTML this doc is, return false.
		if(version == null)
			return false;
		if(version.equals(RTML_VERSION_22) && (type.equals("request")))
			return true;
		if(version.equals(RTML_VERSION_31) && (mode.equals("request")))
			return true;
		return false;
	}

	/**
	 * Is this document an observation request reply (confirmation) document?
	 * @return true if the document is an observation request reply (confirmation), false otherwise.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public boolean isRequestConfirm()
	{
		// if we don't know what version of RTML this doc is, return false.
		if(version == null)
			return false;
		if(version.equals(RTML_VERSION_22) && (type.equals("confirmation")))
			return true;
		if(version.equals(RTML_VERSION_31) && (mode.equals("confirm")))
			return true;
		return false;
	}

	/**
	 * Is this document an observation update document?
	 * @return true if the document is an observation update, false otherwise.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public boolean isUpdate()
	{
		// if we don't know what version of RTML this doc is, return false.
		if(version == null)
			return false;
		if(version.equals(RTML_VERSION_22) && (type.equals("update")))
			return true;
		if(version.equals(RTML_VERSION_31) && (mode.equals("update")))
			return true;
		return false;
	}

	/**
	 * Is this document a fail document?
	 * @return true if the document is a fail, false otherwise.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public boolean isFail()
	{
		// if we don't know what version of RTML this doc is, return false.
		if(version == null)
			return false;
		if(version.equals(RTML_VERSION_22) && (type.equals("fail")))
			return true;
		if(version.equals(RTML_VERSION_31) && (mode.equals("fail")))
			return true;
		return false;
	}

	/**
	 * Is this document an abort document?
	 * @return true if the document is a abort, false otherwise.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public boolean isAbort()
	{
		// if we don't know what version of RTML this doc is, return false.
		if(version == null)
			return false;
		if(version.equals(RTML_VERSION_22) && (type.equals("abort")))
			return true;
		if(version.equals(RTML_VERSION_31) && (mode.equals("abort")))
			return true;
		return false;
	}

	/**
	 * Is this document an incomplete document?
	 * @return true if the document is an incomplete, false otherwise.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public boolean isIncomplete()
	{
		// if we don't know what version of RTML this doc is, return false.
		if(version == null)
			return false;
		if(version.equals(RTML_VERSION_22) && (type.equals("incomplete")))
			return true;
		if(version.equals(RTML_VERSION_31) && (mode.equals("incomplete")))
			return true;
		return false;
	}

	/**
	 * Is this document a complete/observation document?
	 * @return true if the document is a complete/observation, false otherwise.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public boolean isComplete()
	{
		// if we don't know what version of RTML this doc is, return false.
		if(version == null)
			return false;
		if(version.equals(RTML_VERSION_22) && (type.equals("observation")))
			return true;
		if(version.equals(RTML_VERSION_31) && (mode.equals("complete")))
			return true;
		return false;
	}

	/**
	 * Set this document to be a score request document. The underlying type or mode is set based on
	 * the document's version (which must have been set before this method is invoked using setVersion).
	 * @exception NullPointerException Thrown if the version has not been set.
	 * @exception IllegalArgumentException Thrown if the version is not supported.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #setType
	 * @see #setMode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public void setScoreRequest() throws NullPointerException, IllegalArgumentException
	{
		if(version == null)
			throw new NullPointerException(this.getClass().getName()+":setScoreRequest:version was null.");
		if(version.equals(RTML_VERSION_22))
			setType("score");
		else if(version.equals(RTML_VERSION_31))
			setMode("inquiry");
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setScoreRequest:Unsupported version:"+version);
		}
	}

	/**
	 * Set this document to be a score reject document. The underlying type or mode is set based on
	 * the document's version (which must have been set before this method is invoked using setVersion).
	 * @exception NullPointerException Thrown if the version has not been set.
	 * @exception IllegalArgumentException Thrown if the version is not supported.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #setType
	 * @see #setMode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public void setScoreReject() throws NullPointerException, IllegalArgumentException
	{
		if(version == null)
			throw new NullPointerException(this.getClass().getName()+":setScoreReject:version was null.");
		if(version.equals(RTML_VERSION_22))
			setType("reject");
		else if(version.equals(RTML_VERSION_31))
			setMode("reject");
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setScoreReject:Unsupported version:"+version);
		}
	}

	/**
	 * Set this document to be a score reply document. The underlying type or mode is set based on
	 * the document's version (which must have been set before this method is invoked using setVersion).
	 * @exception NullPointerException Thrown if the version has not been set.
	 * @exception IllegalArgumentException Thrown if the version is not supported.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #setType
	 * @see #setMode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public void setScoreReply() throws NullPointerException, IllegalArgumentException
	{
		if(version == null)
			throw new NullPointerException(this.getClass().getName()+":setScoreReply:version was null.");
		if(version.equals(RTML_VERSION_22))
			setType("score");
		else if(version.equals(RTML_VERSION_31))
			setMode("offer");
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setScoreReply:Unsupported version:"+version);
		}
	}

	/**
	 * Set this document to be a request document. The underlying type or mode is set based on
	 * the document's version (which must have been set before this method is invoked using setVersion).
	 * @exception NullPointerException Thrown if the version has not been set.
	 * @exception IllegalArgumentException Thrown if the version is not supported.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #setType
	 * @see #setMode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public void setRequest() throws NullPointerException, IllegalArgumentException
	{
		if(version == null)
			throw new NullPointerException(this.getClass().getName()+":setRequest:version was null.");
		if(version.equals(RTML_VERSION_22))
			setType("request");
		else if(version.equals(RTML_VERSION_31))
			setMode("request");
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setRequest:Unsupported version:"+version);
		}
	}

	/**
	 * Set this document to be a request reply (confirmation) document. The underlying type or mode is set based on
	 * the document's version (which must have been set before this method is invoked using setVersion).
	 * @exception NullPointerException Thrown if the version has not been set.
	 * @exception IllegalArgumentException Thrown if the version is not supported.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #setType
	 * @see #setMode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public void setRequestReply() throws NullPointerException, IllegalArgumentException
	{
		if(version == null)
			throw new NullPointerException(this.getClass().getName()+":setRequestReply:version was null.");
		if(version.equals(RTML_VERSION_22))
			setType("confirmation");
		else if(version.equals(RTML_VERSION_31))
			setMode("confirm");
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setRequestReply:Unsupported version:"+version);
		}
	}

	/**
	 * Set this document to be an update document. The underlying type or mode is set based on
	 * the document's version (which must have been set before this method is invoked using setVersion).
	 * @exception NullPointerException Thrown if the version has not been set.
	 * @exception IllegalArgumentException Thrown if the version is not supported.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #setType
	 * @see #setMode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public void setUpdate() throws NullPointerException, IllegalArgumentException
	{
		if(version == null)
			throw new NullPointerException(this.getClass().getName()+":setUpdate:version was null.");
		if(version.equals(RTML_VERSION_22))
			setType("update");
		else if(version.equals(RTML_VERSION_31))
			setMode("update");
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setUpdate:Unsupported version:"+version);
		}
	}

	/**
	 * Set this document to be a fail document. The underlying type or mode is set based on
	 * the document's version (which must have been set before this method is invoked using setVersion).
	 * @exception NullPointerException Thrown if the version has not been set.
	 * @exception IllegalArgumentException Thrown if the version is not supported.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #setType
	 * @see #setMode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public void setFail() throws NullPointerException, IllegalArgumentException
	{
		if(version == null)
			throw new NullPointerException(this.getClass().getName()+":setFail:version was null.");
		if(version.equals(RTML_VERSION_22))
			setType("fail");
		else if(version.equals(RTML_VERSION_31))
			setMode("fail");
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setFail:Unsupported version:"+version);
		}
	}

	/**
	 * Set this document to be a fail document. The underlying type or mode is set based on
	 * the document's version (which must have been set before this method is invoked using setVersion).
	 * @exception NullPointerException Thrown if the version has not been set.
	 * @exception IllegalArgumentException Thrown if the version is not supported.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #setType
	 * @see #setMode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public void setReject() throws NullPointerException, IllegalArgumentException
	{
		if(version == null)
			throw new NullPointerException(this.getClass().getName()+":setReject:version was null.");
		if(version.equals(RTML_VERSION_22))
			setType("reject");
		else if(version.equals(RTML_VERSION_31))
			setMode("reject");
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setReject:Unsupported version:"+version);
		}
	}

	/**
	 * Set this document to be an abort document. The underlying type or mode is set based on
	 * the document's version (which must have been set before this method is invoked using setVersion).
	 * Note RTML 3.1a suggests an "abort reply" has the mode "confirm".
	 * @exception NullPointerException Thrown if the version has not been set.
	 * @exception IllegalArgumentException Thrown if the version is not supported.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #setType
	 * @see #setMode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public void setAbort() throws NullPointerException, IllegalArgumentException
	{
		if(version == null)
			throw new NullPointerException(this.getClass().getName()+":setAbort:version was null.");
		if(version.equals(RTML_VERSION_22))
			setType("abort");
		else if(version.equals(RTML_VERSION_31))
			setMode("abort");
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setAbort:Unsupported version:"+version);
		}
	}

	/**
	 * Set this document to be a incomplete document. The underlying type or mode is set based on
	 * the document's version (which must have been set before this method is invoked using setVersion).
	 * @exception NullPointerException Thrown if the version has not been set.
	 * @exception IllegalArgumentException Thrown if the version is not supported.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #setType
	 * @see #setMode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public void setIncomplete() throws NullPointerException, IllegalArgumentException
	{
		if(version == null)
			throw new NullPointerException(this.getClass().getName()+":setIncomplete:version was null.");
		if(version.equals(RTML_VERSION_22))
			setType("incomplete");
		else if(version.equals(RTML_VERSION_31))
			setMode("incomplete");
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setIncomplete:Unsupported version:"+version);
		}
	}

	/**
	 * Set this document to be a complete document. The underlying type or mode is set based on
	 * the document's version (which must have been set before this method is invoked using setVersion).
	 * @exception NullPointerException Thrown if the version has not been set.
	 * @exception IllegalArgumentException Thrown if the version is not supported.
	 * @see #version
	 * @see #type
	 * @see #mode
	 * @see #setType
	 * @see #setMode
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public void setComplete() throws NullPointerException, IllegalArgumentException
	{
		if(version == null)
			throw new NullPointerException(this.getClass().getName()+":setComplete:version was null.");
		if(version.equals(RTML_VERSION_22))
			setType("observation");
		else if(version.equals(RTML_VERSION_31))
			setMode("complete");
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setComplete:Unsupported version:"+version);
		}
	}

	/**
	 * Method to return whether this documents represents a target of opportunity request (TOOP) which
	 * must be serviced immediately, or a regular phase II scheduled observation.
	 * @return true if the document is a TOOP, false otherwise.
	 * @exception NullPointerException Thrown if the version has not been set, or a document's observation #
	 *           target/schedule are null.
	 * @exception IllegalArgumentException Thrown if the version is not supported, or the document
	 *           contains multiple observations which don't agree as to whether they are a toop.
	 * @see #version
	 * @see #getObservationListCount
	 * @see #getObservation
	 * @see org.estar.rtml.RTMLObservation#getTarget
	 * @see org.estar.rtml.RTMLObservation#getSchedule
	 * @see org.estar.rtml.RTMTarget#isTypeTOOP
	 * @see org.estar.rtml.RTMLSchedule#getPriority
	 * @see org.estar.rtml.RTMLSchedule#SCHEDULE_PRIORITY_TOOP
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public boolean isTOOP() throws NullPointerException, IllegalArgumentException
	{
		RTMLObservation observation = null;
		RTMLSchedule schedule = null;
		RTMLTarget target = null;
		boolean isToop = false;

		if(version == null)
			throw new NullPointerException(this.getClass().getName()+":isTOOP:version was null.");
		for(int i = 0; i < getObservationListCount(); i++)
		{
			observation = getObservation(i);
			target = observation.getTarget();
			// if the observation target is null, use the document's overall target if it exists
			if(target == null)
			{
				target = this.target;
			}
			schedule = observation.getSchedule();
			if(target == null)
			{
				throw new NullPointerException(this.getClass().getName()+":isTOOP:observation "+i+
							       " has null target.");
			}
			if(schedule == null)
			{
				throw new NullPointerException(this.getClass().getName()+":isTOOP:observation "+i+
							       " has null schedule.");
			}
			if(version.equals(RTML_VERSION_22)) // check target type == toop
			{
				// if first observation, set isToop
				if(i == 0)
				{
					if(target.isTypeTOOP())
						isToop = true;
					else
						isToop = false;
				}
				else // check this observation matches others in the list else error
				{
					if(target.isTypeTOOP())
					{
						if(isToop == false)
						{
							throw new IllegalArgumentException(this.getClass().getName()+
							   ":isTOOP:Observations don't match toop-wise(1): "+i);
						}
					}
					else
					{
						if(isToop)
						{
							throw new IllegalArgumentException(this.getClass().getName()+
							   ":isTOOP:Observations don't match toop-wise(2): "+i);
						}
					}
				}
			}
			else if(version.equals(RTML_VERSION_31))// check schedule priority == 0
			{
				// if first observation, set isToop
				if(i == 0)
				{
					if(schedule.getPriority() ==  RTMLSchedule.SCHEDULE_PRIORITY_TOOP)
						isToop = true;
					else
						isToop = false;
				}
				else // check this observation matches others in the list else error
				{
					if(schedule.getPriority() ==  RTMLSchedule.SCHEDULE_PRIORITY_TOOP)
					{
						if(isToop == false)
						{
							throw new IllegalArgumentException(this.getClass().getName()+
							   ":isTOOP:Observations don't match toop-wise(3): "+i);
						}
					}
					else
					{
						if(isToop)
						{
							throw new IllegalArgumentException(this.getClass().getName()+
							   ":isTOOP:Observations don't match toop-wise(4): "+i);
						}
					}
				}
			}
			else
			{
				throw new IllegalArgumentException(this.getClass().getName()+
							   ":isTOOP:Unsupported version:"+version);
			}
		}// end for on observations
		return isToop;
	}

	/**
	 * Method to set that this documents represents a target of opportunity request (TOOP) which
	 * must be serviced immediately.
	 * @exception NullPointerException Thrown if the version has not been set, or a document's observation #
	 *           target/schedule are null.
	 * @exception IllegalArgumentException Thrown if the version is not supported.
	 * @see #version
	 * @see #getObservationListCount
	 * @see #getObservation
	 * @see org.estar.rtml.RTMLObservation#getTarget
	 * @see org.estar.rtml.RTMLObservation#getSchedule
	 * @see org.estar.rtml.RTMTarget#setTypeTOOP
	 * @see org.estar.rtml.RTMLSchedule#setPriority
	 * @see org.estar.rtml.RTMLSchedule#SCHEDULE_PRIORITY_TOOP
	 * @see #RTML_VERSION_22
	 * @see #RTML_VERSION_31
	 */
	public void setTOOP() throws NullPointerException, IllegalArgumentException
	{
		RTMLObservation observation = null;
		RTMLSchedule schedule = null;
		RTMLTarget target = null;
		boolean isToop = false;

		if(version == null)
			throw new NullPointerException(this.getClass().getName()+":setTOOP:version was null.");
		for(int i = 0; i < getObservationListCount(); i++)
		{
			observation = getObservation(i);
			target = observation.getTarget();
			// if the observation target is null, use the document's overall target if it exists
			if(target == null)
			{
				target = this.target;
			}
			schedule = observation.getSchedule();
			if(target == null)
			{
				throw new NullPointerException(this.getClass().getName()+":setTOOP:observation "+i+
							       " has null target.");
			}
			if(schedule == null)
			{
				throw new NullPointerException(this.getClass().getName()+":setTOOP:observation "+i+
							       " has null schedule.");
			}
			if(version.equals(RTML_VERSION_22)) // target type == toop
			{
				target.setTypeTOOP();
			}
			else if(version.equals(RTML_VERSION_31))// schedule priority == 0
			{
				schedule.setPriority(RTMLSchedule.SCHEDULE_PRIORITY_TOOP);
			}
			else
			{
				throw new IllegalArgumentException(this.getClass().getName()+
							   ":setTOOP:Unsupported version:"+version);
			}
		}// end for on observations
	}

	/**
	 * Set the document's history.
	 * @param h The history.
	 * @see #history
	 * @see org.estar.rtml.RTMLHistory
	 */
	public void setHistory(RTMLHistory h)
	{
		history = h;
	}

	/**
	 * Get the document's history.
	 * @return The history.
	 * @see #history
	 * @see org.estar.rtml.RTMLHistory
	 */
	public RTMLHistory getHistory()
	{
		return history;
	}

	/**
	 * Get the number of Entrys in the History list.
	 * @return The number of Entrys in the list.
	 * @see #history
	 * @see org.estar.rtml.RTMLHistory#getEntryListCount
	 */
	public int getHistoryEntryCount()
	{
		if(history == null)
			return 0;
		return history.getEntryListCount();
	}

	/**
	 * Convenience method to add a normal history entry. The history container is initialised if
	 * necessary. The entry timestamp is set to now.
	 * @param agentName The name of the agent adding the history.
	 * @param agentUri The agent URI.
	 * @param description A description of the changes made to the document.
	 * @see #history
	 */
	public void addHistoryEntry(String agentName,String agentUri,String description)
	{
		RTMLHistoryEntry entry = null;
		RTMLIntelligentAgent ia = null;

		// if no history in the document, add the list container
		if(history == null)
			history = new RTMLHistory();
		// setup agent for this entry
		ia = new RTMLIntelligentAgent();
		ia.setId(agentName);
		ia.setUri(agentUri);
		// entry to add
		entry = new RTMLHistoryEntry();
		entry.setTimeStamp(new Date());
		entry.setAgent(ia);
		entry.setDescription(description);
		// version ?
		// add entry to history.
		history.addEntry(entry);
	}

	/**
	 * Convenience method to add a rejection history entry. The history container is initialised if
	 * necessary. The entry timestamp is set to now.
	 * @param agentName The name of the agent adding the history.
	 * @param agentUri The agent URI.
	 * @param rejectionReason The reason for rejection, one of: REJECTION_REASON_INSUFFICIENT_PRIORITY,
	 *        REJECTION_REASON_NOT_AUTHORISED, REJECTION_REASON_NOT_AVAILABLE, REJECTION_REASON_OTHER,
	 *        REJECTION_REASON_SYNTAX.
	 * @param rejectionDescription A description of why the document was rejected.
	 * @see #history
	 * @see org.estar.rtml.RTMLHistoryEntry#REJECTION_REASON_INSUFFICIENT_PRIORITY
	 * @see org.estar.rtml.RTMLHistoryEntry#REJECTION_REASON_NOT_AUTHORISED
	 * @see org.estar.rtml.RTMLHistoryEntry#REJECTION_REASON_NOT_AVAILABLE 
	 * @see org.estar.rtml.RTMLHistoryEntry#REJECTION_REASON_OTHER  
	 * @see org.estar.rtml.RTMLHistoryEntry#REJECTION_REASON_SYNTAX
	 */
	public void addHistoryRejection(String agentName,String agentUri,String rejectionReason,
					String rejectionDescription)
	{
		RTMLHistoryEntry entry = null;
		RTMLIntelligentAgent ia = null;

		// if no history in the document, add the list container
		if(history == null)
			history = new RTMLHistory();
		// setup agent for this entry
		ia = new RTMLIntelligentAgent();
		ia.setId(agentName);
		ia.setUri(agentUri);
		// entry to add
		entry = new RTMLHistoryEntry();
		entry.setTimeStamp(new Date());
		entry.setAgent(ia);
		entry.setRejectionReason(rejectionReason);
		entry.setRejectionDescription(rejectionDescription);
		// version ?
		// add entry to history.
		history.addEntry(entry);
	}

	/**
	 * Convenience method to add an error history entry. The history container is initialised if
	 * necessary. The entry timestamp is set to now.
	 * @param agentName The name of the agent adding the history.
	 * @param agentUri The agent URI.
	 * @param errorString The error.
	 * @param description A description of this entry.
	 * @see #history
	 */
	public void addHistoryError(String agentName,String agentUri,String errorString,String description)
	{
		RTMLHistoryEntry entry = null;
		RTMLIntelligentAgent ia = null;

		// if no history in the document, add the list container
		if(history == null)
			history = new RTMLHistory();
		// setup agent for this entry
		ia = new RTMLIntelligentAgent();
		ia.setId(agentName);
		ia.setUri(agentUri);
		// entry to add
		entry = new RTMLHistoryEntry();
		entry.setTimeStamp(new Date());
		entry.setAgent(ia);
		entry.setError(errorString);
		entry.setDescription(description);
		// version ?
		// add entry to history.
		history.addEntry(entry);
	}

	/**
	 * Set the document's contact.
	 * @param c The contact.
	 * @see #contact
	 * @see org.estar.rtml.RTMLContact
	 */
	public void setContact(RTMLContact c)
	{
		contact = c;
	}

	/**
	 * Get the document's contact.
	 * @return The contact.
	 * @see #contact
	 * @see org.estar.rtml.RTMLContact
	 */
	public RTMLContact getContact()
	{
		return contact;
	}

	/**
	 * Set the document's project.
	 * @param p The project.
	 * @see #project
	 * @see org.estar.rtml.RTMLProject
	 */
	public void setProject(RTMLProject p)
	{
		project = p;
	}

	/**
	 * Get the document's project.
	 * @return The project.
	 * @see #project
	 * @see org.estar.rtml.RTMLProject
	 */
	public RTMLProject getProject()
	{
		return(project);
	}

	/**
	 * Set the document's telescope.
	 * @param o The telescope.
	 * @see #telescope
	 * @see org.estar.rtml.RTMLTelescope
	 */
	public void setTelescope(RTMLTelescope o)
	{
		telescope = o;
	}

	/**
	 * Get the document's telescope.
	 * @return The telescope.
	 * @see #telescope
	 * @see org.estar.rtml.RTMLTelescope
	 */
	public RTMLTelescope getTelescope()
	{
		return(telescope);
	}

	/**
	 * Set the document's intelligent agent information.
	 * @param ia The intelligent agent information.
	 * @see #intelligentAgent
	 * @see org.estar.rtml.RTMLIntelligentAgent
	 */
	public void setIntelligentAgent(RTMLIntelligentAgent ia)
	{
		intelligentAgent = ia;
	}

	/**
	 * Get the document's intelligent agent information.
	 * @return The intelligent agent information.
	 * @see #intelligentAgent
	 * @see org.estar.rtml.RTMLIntelligentAgent
	 */
	public RTMLIntelligentAgent getIntelligentAgent()
	{
		return intelligentAgent;
	}

	/**
	 * Set the default device for the document.
	 * @param d The device to set.
	 * @see #device
	 * @see org.estar.rtml.RTMLDevice
	 */
	public void setDevice(RTMLDevice d)
	{
		device = d;
	}

	/**
	 * Get the default device for the document.
	 * @return The device. This can be null if each observation in the document has it's own device.
	 * @see #device
	 * @see org.estar.rtml.RTMLDevice
	 */
	public RTMLDevice getDevice()
	{
		return device;
	}

	/**
	 * Set the default document target.
	 * @param t The target to set.
	 * @see #target
	 * @see org.estar.rtml.RTMLTarget
	 */
	public void setTarget(RTMLTarget t)
	{
		target = t;
	}

	/**
	 * Get the default document target.
	 * @return The target. This can be null if each observation in the document has it's own target.
	 * @see #target
	 * @see org.estar.rtml.RTMLTarget
	 */
	public RTMLTarget getTarget()
	{
		return target;
	}

	/**
	 * Add an observation to the document.
	 * @param ob The observation to add.
	 * @see #observationList
	 * @see org.estar.rtml.RTMLObservation
	 */
	public void addObservation(RTMLObservation ob)
	{
		observationList.add(ob);
	}

	/**
	 * Clear the observations from the document.
	 * @see #observationList
	 */
	public void clearObservationList()
	{
		observationList = null;
	}

	/**
	 * Get an observation from the RTML Node.
	 * @param index The index in the observation list.
	 * @return The observation to the specicifed index is returned.
	 * @see #observationList
	 * @see org.estar.rtml.RTMLObservation
	 */
	public RTMLObservation getObservation(int index)
	{
		return (RTMLObservation)(observationList.get(index));
	}

	/**
	 * Get the number of observations in the list.
	 * @return The number of observations in the list.
	 * @see #observationList
	 */
	public int getObservationListCount()
	{
		return observationList.size();
	}

	/**
	 * Set the documents "simple" score.
	 * @param s The score.
	 * @see #score
	 */
	public void setScore(double s)
	{
		score = new Double(s);
	}

	/**
	 * Set the documents "simple" score.
	 * @param s The score, as a string representing a double.
	 * @exception RTMLException Thrown if the string is not a valid double.
	 * @see #score
	 */
	public void setScore(String s) throws RTMLException
	{
		double d;

		try
		{
			d = Double.parseDouble(s);
			score = new Double(d);
		}
		catch(NumberFormatException e)
		{
			throw new RTMLException(this.getClass().getName()+":setScore:Illegal Score:"+s+":",e);
		}
		catch(Exception e)
		{
			throw new RTMLException(this.getClass().getName()+":setScore:Illegal Score:"+s+":",e);
		}
	}

	/**
	 * Get the documents "simple" score.
	 * @return The score. This can be null.
	 * @see #score
	 */
	public Double getScore()
	{
		return score;
	}

	/**
	 * Add an score to the Scores list.
	 * @param sc The score to add.
	 * @see #scoresList
	 * @see RTMLScore
	 */
	public void addScore(RTMLScore sc)
	{
		scoresList.add(sc);
	}

	/**
	 * Add an score to the Scores list.
	 * @param delayString The delay, as a valid period format: P{(yyyy)Y{(mm)M}{(dd)D}{T{(hh)H}{(mm}M}{(ss.s..)S}.
	 * @param probabilityString The differential probability, a valid double or "NaN".
	 * @param cumulativeString The cumulative probability as a string, a valid double or "NaN".
	 * @exception RTMLException Thrown if the string is not a valid double.
	 * @see #score
	 */
	public void addScore(String delayString,String probabilityString,String cumulativeString) throws RTMLException
	{
		RTMLScore newScore = null;
		RTMLPeriodFormat delay = null;
		double d;

		newScore = new RTMLScore();
		// delay
		delay = new RTMLPeriodFormat();
		delay.parse(delayString); // throws RTMLException/NumberFormatException
		newScore.setDelay(delay);
		// probability
		try
		{
			if(probabilityString.equals("NaN"))
				d = Double.NaN;
			else
				d = Double.parseDouble(probabilityString);
			newScore.setProbability(d);
		}
		catch(NumberFormatException e)
		{
			throw new RTMLException(this.getClass().getName()+":addScore:Illegal probabilityString:"+
						probabilityString+":",e);
		}
		catch(Exception e)
		{
			throw new RTMLException(this.getClass().getName()+":addScore:Illegal probabilityString:"+
						probabilityString+":",e);
		}
		// cumulative
		try
		{
			if(cumulativeString.equals("NaN"))
				d = Double.NaN;
			else
				d = Double.parseDouble(cumulativeString);
			newScore.setCumulative(d);
		}
		catch(NumberFormatException e)
		{
			throw new RTMLException(this.getClass().getName()+":addScore:Illegal cumulativeString:"+
						cumulativeString+":",e);
		}
		catch(Exception e)
		{
			throw new RTMLException(this.getClass().getName()+":addScore:Illegal cumulativeString:"+
						cumulativeString+":",e);
		}
		scoresList.add(newScore);
	}

	/**
	 * Clear the Scores list from the document.
	 * @see #scoresList
	 */
	public void clearScoresList()
	{
		scoresList = null;
		scoresList = new Vector();
	}

	/**
	 * Get a Score from the Scores list.
	 * @param index The index in the scores list.
	 * @return The score in the specicifed index is returned.
	 * @see #scoresList
	 */
	public RTMLScore getScore(int index)
	{
		return (RTMLScore)(scoresList.get(index));
	}

	/**
	 * Get the number of Score's in the Scores list.
	 * @return The number of scores in the list.
	 * @see #scoresList
	 */
	public int getScoresListCount()
	{
		return scoresList.size();
	}

	/**
	 * Set the documents completion time.
	 * @param t The completion time.
	 * @see #completionTime
	 */
	public void setCompletionTime(Date t)
	{
		completionTime = t;
	}

	/**
	 * Set the documents completion time, based on a string of the form "yyyy-MM-dd'T'HH:mm:ssZ".
	 * @param s The completion time string.
	 * @exception RTMLException Thrown if the string is not a valid completion time, 
	 *            using the format specified above.
	 * @see #completionTime
	 * @see RTMLDateFormat
	 */
	public void setCompletionTime(String s) throws RTMLException
	{
		RTMLDateFormat dateFormat = null;

		dateFormat = new RTMLDateFormat();
		try
		{
			completionTime = dateFormat.parse(s);
		}
		catch(ParseException e)
		{
			throw new RTMLException(this.getClass().getName()+":setCompletionTime:Illegal Time:"+s+":",e);
		}
	}

	/**
	 * Get the documents completion time.
	 * @return The completion time.
	 * @see #completionTime
	 */
	public Date getCompletionTime()
	{
		return completionTime;
	}

	/**
	 * Set the error string. In RTML2.2, the error String is the TEXT node in the RTML element, if 
	 * the document has type "reject","fail" or "abort". In RTML 3.1a, we call addHistoryError with the agent 
	 *Id and URI set to unknown.
	 * @param s The error string.
	 * @exception RTMLException Thrown if the document type was not "reject","fail" or "abort" in RTML 2.2.
	 *            Thrown if the version is not supported.
	 * @see #version
	 * @see #type
	 * @see #addHistoryError
	 */
	public void setErrorString(String s) throws RTMLException
	{
		if(version.equals(RTML_VERSION_22))
		{
			if((type.equals("reject") == false)&&(type.equals("fail") == false)&&
			   (type.equals("abort") == false))
			{
				throw new RTMLException(this.getClass().getName()+
				    ":setErrorString:Trying to set error string in document of wrong type:"+type+".");
			}
			errorString = s;
		}
		else if(version.equals(RTML_VERSION_31))
		{
			addHistoryError("unknown","unknown",s,s);
		}
		else
		{
			throw new RTMLException(this.getClass().getName()+
						":setErrorString:Unsupported document version:"+version+
						" when trying to set error string:"+s);
		}
	}

	public String getErrorString()
	{
		return errorString;
	}

	/**
	 * Clone the document and all it's sub elements.
	 * @return a deep clone of the document.
	 */
	public Object deepClone() throws Exception
	{
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(b);
		out.writeObject(this);
		out.close();
		ByteArrayInputStream bIn = new ByteArrayInputStream(b.toByteArray());
		ObjectInputStream oi = new ObjectInputStream(bIn);
		oi.close();
		return oi.readObject();
	}
 

	/**
	 * Method to print out a string representation of this node.
	 * @see #toString(java.lang.String)
	 */
	public String toString()
	{
		return toString("");
	}

	/**
	 * Method to print out a string representation of this node, with a prefix.
	 * @param prefix A string to prefix to each line of data we print out.
	 * @see #getHistory
	 * @see #getContact
	 * @see #getMode
	 * @see #getType
	 * @see #getIntelligentAgent
	 * @see #getDevice
	 * @see #getTarget
	 * @see #getObservationListCount
	 * @see #getObservation
	 * @see #getScore
	 * @see #getCompletionTime
	 * @see #getErrorString
	 * @see #getProject
	 * @see #getTelescope
	 */
	public String toString(String prefix)
	{
		RTMLObservation ob = null;
		RTMLScore sc = null;
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"RTML :");
		if(version != null)
			sb.append(" version = "+getVersion());
		if(uid != null)
			sb.append(" uid = "+getUId());
		if(type != null)
			sb.append(" type = "+getType());
		if(mode != null)
			sb.append(" mode = "+getMode());
		sb.append("\n");
		if(getHistory() != null)
			sb.append( prefix+getHistory().toString("\t")+"\n");
		if(getContact() != null)
			sb.append( prefix+getContact().toString("\t")+"\n");
		if(getProject() != null)
			sb.append( prefix+getProject().toString("\t")+"\n");
		if(getTelescope() != null)
			sb.append( prefix+getTelescope().toString("\t")+"\n");
		if(getIntelligentAgent() != null)
			sb.append(prefix+getIntelligentAgent().toString("\t")+"\n");
		if(getDevice() != null)
			sb.append(prefix+getDevice().toString("\t")+"\n");
		if(getTarget() != null)
			sb.append(prefix+getTarget().toString("\t")+"\n");
		for(int i = 0; i < getObservationListCount();i++)
		{
			ob = getObservation(i);
			if(ob != null)
				sb.append(prefix+ob.toString("\t")+"\n");
		}
		if(getScore() != null)
			sb.append(prefix+"\tScore:"+getScore()+"\n");
		for(int i = 0; i < getScoresListCount();i++)
		{
			sc = getScore(i);
			if(sc != null)
				sb.append(prefix+sc.toString("\t")+"\n");
		}
		if(getCompletionTime() != null)
			sb.append(prefix+"\tCompletion Time:"+getCompletionTime()+"\n");
		if((type != null) && type.equals("reject") && (errorString != null))
			sb.append(prefix+"\tError:"+getErrorString()+"\n");
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
** Revision 1.20  2009/08/12 17:49:50  cjm
** Now has default document-wide target.
** Now implements RTMLTargetHolder.
**
** Revision 1.19  2008/07/16 13:32:38  cjm
** Attempt to bodge setErrorString for RTML 3.1a, rather than getting a NullPointerException on type.
**
** Revision 1.18  2008/06/06 12:00:31  cjm
** Added setTOOP method.
**
** Revision 1.17  2008/06/03 15:32:40  cjm
** Added RTMLTelescope sub-element.
**
** Revision 1.16  2008/05/27 14:00:06  cjm
** Added serialVersionUID.
**
** Revision 1.15  2008/05/23 14:23:30  cjm
** New version after RTML 3.1a integration.
** Now supports version/mode/uid/history.
** Has various helper methods to determine/set what type/mode document is based on version.
** Has helper method to determine whether the document is a toop.
** Has helper methods for adding history.
**
** Revision 1.14  2007/03/27 19:16:58  cjm
** Added Scores list handling.
** Added scoresList.
** Added methods to add/retrieve scores from the list and reset it.
**
** Revision 1.13  2007/01/30 18:31:13  cjm
** gnuify: Added GNU General Public License.
**
** Revision 1.12  2006/03/20 16:22:33  cjm
** Now uses RTMLDateFormat for date/time parsing/formatting.
**
** Revision 1.11  2005/06/20 10:48:49  cjm
** setErrorString now works for fail and abort type documents as well as reject.
**
** Revision 1.10  2005/06/08 11:37:11  cjm
** Fixed comments.
**
** Revision 1.9  2005/06/01 16:30:22  cjm
** Added clearObservationList method.
**
** Revision 1.8  2005/05/05 15:02:39  cjm
** Added deepClone.
**
** Revision 1.7  2005/04/28 09:39:41  cjm
** Changed score from primitive to object type.
** This allows us to specify exactley when the score element is present, and when it isn't.
**
** Revision 1.6  2005/01/19 11:51:27  cjm
** Now implments RTMLDeviceHolder.
**
** Revision 1.5  2005/01/18 15:08:21  cjm
** Added project.
**
** Revision 1.4  2004/03/12 17:15:52  cjm
** reformatted.
**
** Revision 1.3  2004/03/12 10:24:54  je
** Added Contact field, accessor, mutator and included in toString
**
** Revision 1.2  2004/03/11 13:27:28  cjm
** Added Device node.
**
** Revision 1.1  2003/02/24 13:19:56  cjm
** Initial revision
**
*/
