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
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLDocument.java,v 1.14 2007-03-27 19:16:58 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * This class is a data container for information contained in the base nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.14 $
 */
public class RTMLDocument implements Serializable, RTMLDeviceHolder
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLDocument.java,v 1.14 2007-03-27 19:16:58 cjm Exp $";
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
	 * @param d The device to set.
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
	 * @param s The score, as a string representing a double.
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
	 * Set the error string. The error String is the TEXT node in the RTML element, if 
	 * the document has type "reject","fail" or "abort".
	 * @param s The error string.
	 * @exception RTMLException Thrown if the document type was not "reject","fail" or "abort".
	 */
	public void setErrorString(String s) throws RTMLException
	{
		if((type.equals("reject") == false)&&(type.equals("fail") == false)&&(type.equals("abort") == false))
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
		RTMLScore sc = null;
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
		for(int i = 0; i < getScoresListCount();i++)
		{
			sc = getScore(i);
			if(sc != null)
				sb.append(prefix+sc.toString("\t")+"\n");
		}
		if(getCompletionTime() != null)
			sb.append(prefix+"\tCompletion Time:"+getCompletionTime()+"\n");
		if(type.equals("reject") && (errorString != null))
			sb.append(prefix+"\tError:"+getErrorString()+"\n");
		return sb.toString();
	}
}
/*
** $Log: not supported by cvs2svn $
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
