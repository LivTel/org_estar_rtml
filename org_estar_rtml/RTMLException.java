// RTMLException.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLException.java,v 1.1 2003-02-24 13:19:56 cjm Exp $
package org.estar.rtml;

/**
 * This class extends Exception. 
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 */
public class RTMLException extends Exception
{
	/**
	 * Revision Control System id string, showing the version of the Class
	 */
	public final static String RCSID = new String("$Id: RTMLException.java,v 1.1 2003-02-24 13:19:56 cjm Exp $");
	/**
	 * The error string supplied to the exception.
	 */
	private String errorString = null;
	/**
	 * An exception that caused this exception to be generated.
	 */
	private Exception exception = null;

	/**
	 * Constructor for the exception.
	 * @param errorString The error string.
	 */
	public RTMLException(String errorString)
	{
		super(errorString);
		this.errorString = new String(errorString);
	}

	/**
	 * Constructor for the exception.
	 * @param errorString The error string.
	 * @param exception An exception that caused this exception to be generated.
	 */
	public RTMLException(String errorString,Exception e)
	{
		super(errorString);
		this.errorString = new String(errorString);
		exception = e;
	}

	/**
	 * Retrieve routine for the error string supplied to the exception.
	 * @return Returns a copy of the errorString for this exception.
	 * @see #errorString
	 */
	public String getErrorString()
	{
		return new String(errorString);
	}

	/**
	 * Retrieve method to return exception that generated this exception.
	 * @return An exception, or null.
	 * @see #exception
	 */
	public Exception getException()
	{
		return exception;
	}

	/**
	 * Overridden toString method, that calls super toString and adds errorString to it.
	 * @see #errorString
	 */
	public String toString()
	{
		StringBuffer sb = null;

		sb = new StringBuffer();
		sb.append(errorString);
		if(exception != null)
			sb.append(exception.toString());
		return sb.toString();
	}

	/**
	 * Overridden printStackTrace, that prints the creating exceptions stack if it is non-null.
	 */
	public void printStackTrace()
	{
		super.printStackTrace();
		if(exception != null)
			exception.printStackTrace();
	}
}

//
// $Log: not supported by cvs2svn $
//
