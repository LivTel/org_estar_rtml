// RTMLDetector.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLDetector.java,v 1.1 2005-01-19 11:53:42 cjm Exp $
package org.estar.rtml;

import java.io.*;

/**
 * This class is a data container for information contained in the Detector nodes/tags of an RTML document.
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 */
public class RTMLDetector implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLDetector.java,v 1.1 2005-01-19 11:53:42 cjm Exp $";
	/**
	 * The Detector Binning row number.
	 */
	private int rowBinning = 2;
	/**
	 * The Detector Binning column number.
	 */
	private int columnBinning = 2;

	/**
	 * Default constructor.
	 */
	public RTMLDetector()
	{
		super();
	}

	/**
	 * Method to set the row binning.
	 * @param i The row binning.
	 * @see #rowBinning
	 */
	public void setRowBinning(int i)
	{
		rowBinning = i;
	}

	/**
	 * Method to set the row binning.
	 * @param s The row binning as a string.
	 * @exception RTMLException Thrown if the string is not a valid integer.
	 * @see #rowBinning
	 */
	public void setRowBinning(String s) throws RTMLException
	{
		try
		{
			rowBinning = Integer.parseInt(s);
		}
		catch(NumberFormatException e)
		{
			throw new RTMLException(this.getClass().getName()+":setRowBinning:Number Format Exception:"+
						s+":",e);
		}
	}

	/**
	 * Method to get the row binning.
	 * @return The row binning.
	 * @see #rowBinning
	 */
	public int getRowBinning()
	{
		return rowBinning;
	}

	/**
	 * Method to set the column binning.
	 * @param i The column binning.
	 */
	public void setColumnBinning(int i)
	{
		columnBinning = i;
	}

	/**
	 * Method to set the column binning.
	 * @param s The column binning as a string.
	 * @exception RTMLException Thrown if the string is not a valid integer.
	 * @see #columnBinning
	 */
	public void setColumnBinning(String s) throws RTMLException
	{
		try
		{
			columnBinning = Integer.parseInt(s);
		}
		catch(NumberFormatException e)
		{
			throw new RTMLException(this.getClass().getName()+":setColumnBinning:Number Format Exception:"+
						s+":",e);
		}
	}

	/**
	 * Method to get the column binning.
	 * @return The column binning.
	 * @see #columnBinning
	 */
	public int getColumnBinning()
	{
		return columnBinning;
	}

	/**
	 * String representation of this Detector.
	 * @see #toString(java.lang.String)
	 */
	public String toString()
	{
		return(toString(""));
	}

	/**
	 * String representation of this Detector, with specified prefix.
	 * @param prefix A string to prefix to each line of data we print out.
	 * @see #getDetector
	 */
	public String toString( String prefix )
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append( prefix+"Detector :\n" );
		sb.append( prefix+"\t       Row Binning : "+rowBinning+"\n" );
		sb.append( prefix+"\t       Column Binning : "+columnBinning+"\n" );
		return( sb.toString() );
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 1.1  2005/01/18 15:08:28  cjm
// Initial revision
//
//
