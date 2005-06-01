// RTMLDocument.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLDocument.java,v 1.9 2005-06-01 16:30:22 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * This class is a data container for information contained in the base nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.9 $
 */
public class RTMLDocument implements Serializable, RTMLDeviceHolder
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLDocument.java,v 1.9 2005-06-01 16:30:22 cjm Exp $";
	/**
	 * The type of the document, as specified in the RTML node's "type" attribute.
	 */
	public String type = null;
	/**
	 * The details contained in the IntelligentAgent tag/node.
	 */
	public RTMLIntelligentAgent intelligentAgent = null;
	/**
	 * Contact info for this document.
	 */
	protected RTMLContact contact = null;
	/**
	 * Project info for this document.
	 */
	protected RTMLProject project = null;
	/**
	 * The instrument device this document wants to use.
	 */
	public RTMLDevice device = null;
	/**
	 * List containing observations.
	 */
	public List observationList = null;
	/**
	 * The score of the document. Note this should be a per observation score, according to the DTD.
	 */
	public Double score = null;
	/**
	 * The completion time. Note this should be a per observation score, according to the DTD.
	 */
	public Date completionTime = null;
	/**
	 * Error String contained in TEXT node of RTML document, if document has type reject.
	 */
	public String errorString = null;

	/**
	 * Default constructor.
	 * @see #observationList
	 */
	public RTMLDocument()
	{
		super();
		observationList = new Vector();
	}

	public void setType(String s)
	{
		type = s;
	}

	public String getType()
	{
		return type;
	}

	public void setContact(RTMLContact newContact)
	{
		contact = newContact;
	}

	public RTMLContact getContact()
	{
		return(contact);
	}

	public void setProject(RTMLProject p)
	{
		project = p;
	}

	public RTMLProject getProject()
	{
		return(project);
	}

	public void setIntelligentAgent(RTMLIntelligentAgent ia)
	{
		intelligentAgent = ia;
	}

	public RTMLIntelligentAgent getIntelligentAgent()
	{
		return intelligentAgent;
	}

	/**
	 * Set the device.
	 * @param device The device to set.
	 */
	public void setDevice(RTMLDevice d)
	{
		device = d;
	}

	/**
	 * Get the device.
	 * @return The device.
	 */
	public RTMLDevice getDevice()
	{
		return device;
	}

	/**
	 * Add an observation to the document.
	 * @param ob The observation to add.
	 * @see #observationList
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
	 * Set the documents score.
	 * @param s The score.
	 * @see #score
	 */
	public void setScore(double s)
	{
		score = new Double(s);
	}

	/**
	 * Set the documents score.
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
	 * Get the documents score.
	 * @return The score.
	 * @see #score
	 */
	public Double getScore()
	{
		return score;
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
	 * Set the documents completion time, based on a string of the form "yyyy-MM-dd'T'HH:mm:ss".
	 * @param s The completion time string.
	 * @exception RTMLException Thrown if the string is not a valid completion time, 
	 *            using the format specified above.
	 * @see #completionTime
	 */
	public void setCompletionTime(String s) throws RTMLException
	{
		DateFormat dateFormat = null;

		dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
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
	 * Set the error string. The error String is the TEXT node in the RTML element, if 
	 * the document has type "reject".
	 * @param s The error string.
	 * @exception RTMLException Thrown if the document type was not "reject".
	 */
	public void setErrorString(String s) throws RTMLException
	{
		if(type.equals("reject") == false)
		{
			throw new RTMLException(this.getClass().getName()+
				    ":setErrorString:Trying to set error string in document of wrong type:"+type+".");
		}
		errorString = s;
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
	 * @see #getContact
	 * @see #getType
	 * @see #getIntelligentAgent
	 * @see #getDevice
	 * @see #getObservationListCount
	 * @see #getObservation
	 * @see #getScore
	 * @see #getCompletionTime
	 * @see #getErrorString
	 */
	public String toString(String prefix)
	{
		RTMLObservation ob = null;
		StringBuffer sb = null;
		
		sb = new StringBuffer();
		sb.append(prefix+"RTML : type = "+getType()+"\n");
		if(getContact() != null)
			sb.append( prefix+getContact().toString("\t")+"\n");
		if(getProject() != null)
			sb.append( prefix+getProject().toString("\t")+"\n");
		if(getIntelligentAgent() != null)
			sb.append(prefix+getIntelligentAgent().toString("\t")+"\n");
		if(getDevice() != null)
			sb.append(prefix+getDevice().toString("\t")+"\n");
		for(int i = 0; i < getObservationListCount();i++)
		{
			ob = getObservation(i);
			if(ob != null)
				sb.append(prefix+ob.toString("\t")+"\n");
		}
		if(getScore() != null)
			sb.append(prefix+"\tScore:"+getScore()+"\n");
		if(getCompletionTime() != null)
			sb.append(prefix+"\tCompletion Time:"+getCompletionTime()+"\n");
		if(type.equals("reject") && (errorString != null))
			sb.append(prefix+"\tError:"+getErrorString()+"\n");
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
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
