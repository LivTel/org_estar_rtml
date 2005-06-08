// RTMLContact.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLContact.java,v 1.4 2005-06-08 11:37:52 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.net.URL;

/**
 * This class is a data container for information contained in the Contact
 * nodes/tags of an RTML document.
 * @author Jason Etherton, CHris Mottram
 * @version $Revision: 1.4 $
 */
public class RTMLContact implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLContact.java,v 1.4 2005-06-08 11:37:52 cjm Exp $";
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
		return(toString( "" ));
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
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = new StringBuffer();

		sb.append( prefix+"Contact :\n" );
		sb.append( prefix+"\tName : "+name+"\n" );
		sb.append( prefix+"\tUser : "+user+"\n" );
		sb.append( prefix+"\tInstitution : "+institution+"\n" );
		sb.append( prefix+"\tAddress : "+address+"\n" );
		sb.append( prefix+"\tTelephone : "+telephone+"\n" );
		sb.append( prefix+"\tFax : "+fax+"\n" );
		sb.append( prefix+"\teMail : "+email+"\n" );
		sb.append( prefix+"\tURL : "+url+"\n" );

		return(sb.toString());
	}
}
/*
 *    $Date: 2005-06-08 11:37:52 $
 * $RCSfile: RTMLContact.java,v $
 *  $Source: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLContact.java,v $
 *      $Id: RTMLContact.java,v 1.4 2005-06-08 11:37:52 cjm Exp $
 *     $Log: not supported by cvs2svn $
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

