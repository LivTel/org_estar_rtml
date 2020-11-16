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
// RTMLContact.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLContact.java,v 1.7 2008-05-27 14:02:05 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.net.URL;

/**
 * This class is a data container for information contained in the Contact
 * nodes/tags of an RTML document.
 * @author Jason Etherton, CHris Mottram
 * @version $Revision$
 * @see org.estar.rtml.RTMLAttributes
 */
public class RTMLContact extends RTMLAttributes implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id$";
	/**
	 * Serial version ID. Fixed as these documents can be used as parameters in RMI calls across JVMs.
	 */
	static final long serialVersionUID = 3377447263027816499L;
	/**
	 * The User element for this Contact.
	 */
	private String user = null;
	/**
	 * The Name element for this Contact.
	 */
	private String name = null;
	/**
	 * The Institution element for this Contact.
	 */
	private String institution = null;
	/**
	 * The Address element for this Contact.
	 */
	private String address = null;
	/**
	 * The Telephone element for this Contact.
	 */
	private String telephone = null;
	/**
	 * The Fax element for this Contact.
	 */
	private String fax = null;
	/**
	 * The Email element for this Contact.
	 */
	private String email = null;
	/**
	 * The Url element for this Contact.
	 */
	private URL url = null;


	/**
	 * Default constructor.
	 */
	public RTMLContact()
	{
		super();
	}

	/**
	 * Method to set the User.
	 * @param newUser the new User to set
	 */
	public void setUser(String newUser)
	{
		user = newUser;
	}

	/**
	 * Method to get the current User.
	 * @return User
	 * @see #user
	 */
	public String getUser()
	{
		return(user);
	}

	/**
	 * Method to set the Name.
	 * @param newName the new Name to set
	 */
	public void setName(String newName)
	{
		name = newName;
	}

	/**
	 * Method to get the current Name.
	 * @return Name
	 * @see #name
	 */
	public String getName()
	{
		return(name);
	}

	/**
	 * Method to set the Institution.
	 * @param newInstitution the new Institution to set
	 */
	public void setInstitution(String newInstitution)
	{
		institution = newInstitution;
	}

	/**
	 * Method to get the current Institution.
	 * @return Institution
	 * @see #institution
	 */
	public String getInstitution()
	{
		return(institution);
	}

	/**
	 * Method to set the Address.
	 * @param newAddress the new Address to set
	 */
	public void setAddress(String newAddress)
	{
		address = newAddress;
	}

	/**
	 * Method to get the current Address.
	 * @return Address
	 * @see #address
	 */
	public String getAddress()
	{
		return(address);
	}

	/**
	 * Method to set the Telephone.
	 * @param newTelephone the new Telephone to set
	 */
	public void setTelephone(String newTelephone)
	{
		telephone = newTelephone;
	}

	/**
	 * Method to get the current Telephone.
	 * @return Telephone
	 * @see #telephone
	 */
	public String getTelephone()
	{
		return(telephone);
	}

	/**
	 * Method to set the Fax.
	 * @param newFax the new Fax to set
	 */
	public void setFax(String newFax)
	{
		fax = newFax;
	}

	/**
	 * Method to get the current Fax.
	 * @return Fax
	 * @see #fax
	 */
	public String getFax()
	{
		return(fax);
	}


	/**
	 * Method to set the Email.
	 * @param newEmail the new Email to set
	 */
	public void setEmail(String newEmail)
	{
		email = newEmail;
	}

	/**
	 * Method to get the current Email.
	 * @return Email
	 * @see #email
	 */
	public String getEmail()
	{
		return(email);
	}

	/**
	 * Method to set the Url.
	 * @param newUrl the new Url to set
	 */
	public void setUrl(URL newUrl)
	{
		url = newUrl;
	}

	/**
	 * Method to get the current Url.
	 * @return Url
	 * @see #url
	 */
	public URL getUrl()
	{
		return(url);
	}

	/**
	 * String representation of this Conatct.
	 * 'see #toString(java.lang.String)
	 */
	public String toString()
	{
		return(toString(""));
	}


	/**
	 * String representation of this Conatct, with specified prefix.
	 * @param prefix A string to prefix to each line of data we print out.
	 * @see #getName
	 * @see #getUser
	 * @see #getInstitution
	 * @see #getAddress
	 * @see #getTelephone
	 * @see #getFax
	 * @see #getEmail
	 * @see #getUrl
	 * @see org.estar.rtml.RTMLAttributes#toString
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = new StringBuffer();

		sb.append(prefix+"Contact :\n");
		sb.append(super.toString(prefix+"\t"));
		sb.append(prefix+"\tName : "+name+"\n");
		sb.append(prefix+"\tUser : "+user+"\n");
		sb.append(prefix+"\tInstitution : "+institution+"\n");
		sb.append(prefix+"\tAddress : "+address+"\n");
		sb.append(prefix+"\tTelephone : "+telephone+"\n");
		sb.append(prefix+"\tFax : "+fax+"\n");
		sb.append(prefix+"\teMail : "+email+"\n");
		sb.append(prefix+"\tURL : "+url+"\n");

		return(sb.toString());
	}
}
/*
 *    $Date$
 * $RCSfile: RTMLContact.java,v $
 *  $Source$
 *      $Id$
 *     $Log: not supported by cvs2svn $
 *     Revision 1.6  2008/05/23 14:12:11  cjm
 *     Now extends RTMLAttributes.
 *
 *     Revision 1.5  2007/01/30 18:31:08  cjm
 *     gnuify: Added GNU General Public License.
 *
 *     Revision 1.4  2005/06/08 11:37:52  cjm
 *     Fixed comments.
 *     Reformatted.
 *
 *     Revision 1.3  2005/01/19 15:30:38  cjm
 *     Added Serializable.
 *
 *     Revision 1.2  2005/01/18 15:18:19  cjm
 *     Added ID.
 *
 *     Revision 1.1  2004/03/12 10:55:18  je
 *     Initial revision
 *
 */

