/*   
    Copyright 2020, Astrophysics Research Institute, Liverpool John Moores University.

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
// RTMLHalfWavePlate.java
package org.estar.rtml;

import java.io.*;
import java.text.*;

/**
 * This class is a data container for information contained in the Device sub-nodes of type 
 * in an RTML document. This is used to store the Moptop polarimeter rotor speed.
 * @author Chris Mottram
 * @version $Revision$
 */
public class RTMLHalfWavePlate implements Serializable
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id$";
	/**
	 * Number used to represent an unknown speed of rotation of the half-wave plate on Moptop.
	 */
	public final static int ROTOR_SPEED_UNKNOWN = 0;
	/**
	 * Number used to represent a slow speed of rotation of the half-wave plate on Moptop.
	 */
	public final static int ROTOR_SPEED_SLOW = 1;
	/**
	 * Number used to represent a fast speed of rotation of the half-wave plate on Moptop.
	 */
	public final static int ROTOR_SPEED_FAST = 2;
	/**
	 * String used to represent a slow speed of rotation of the half-wave plate on Moptop.
	 */
	public final static String ROTOR_SPEED_SLOW_STRING = "slow";
	/**
	 * String used to represent a fast speed of rotation of the half-wave plate on Moptop.
	 */
	public final static String ROTOR_SPEED_FAST_STRING = "fast";
	/**
	 * Serial version ID. Fixed as these documents can be used as parameters in RMI calls across JVMs.
	 */
	//static final long serialVersionUID = ;
	/**
	 * The speed the half-wave plate rotates on Moptop, one of ROTOR_SPEED_SLOW or ROTOR_SPEED_FAST.
	 * @see #ROTOR_SPEED_UNKNOWN
	 * @see #ROTOR_SPEED_SLOW
	 * @see #ROTOR_SPEED_FAST
	 */
	private int rotorSpeed = ROTOR_SPEED_UNKNOWN;

	/**
	 * Default constructor.
	 */
	public RTMLHalfWavePlate()
	{
		super();
	}

	/**
	 * Set the rotator speed of the half-wave plate from a string..
	 * @param s The rotator speed, one of "slow" or "fast".
	 * @exception IllegalArgumentException Thrown if the input string is lot a legal value.
	 * @see #rotorSpeed
	 * @see #ROTOR_SPEED_SLOW
	 * @see #ROTOR_SPEED_FAST
	 * @see #ROTOR_SPEED_SLOW_STRING
	 * @see #ROTOR_SPEED_FAST_STRING
	 */
	public void setRotorSpeed(String s) throws IllegalArgumentException
	{
		if(s.equals(ROTOR_SPEED_SLOW_STRING))
			rotorSpeed = ROTOR_SPEED_SLOW;
		else if(s.equals(ROTOR_SPEED_FAST_STRING))
			rotorSpeed = ROTOR_SPEED_FAST;
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+":setRotorSpeed: rotor speed '"+s+
				  "' is not a legal value ("+ROTOR_SPEED_SLOW_STRING+"/"+ROTOR_SPEED_FAST_STRING+").");
		}
	}

	/**
	 * Set the rotator speed of the half-wave plate from an integer.
	 * @param rs The rotator speed, one of ROTOR_SPEED_SLOW or ROTOR_SPEED_FAST.
	 * @exception IllegalArgumentException Thrown if the input number is lot a legal value.
	 * @see #rotorSpeed
	 * @see #ROTOR_SPEED_SLOW
	 * @see #ROTOR_SPEED_FAST
	 */
	public void setRotorSpeed(int rs) throws IllegalArgumentException
	{
		if((rs != ROTOR_SPEED_SLOW)&&(rs != ROTOR_SPEED_FAST))
		{
			throw new IllegalArgumentException(this.getClass().getName()+":setRotorSpeed: rotor speed "+rs+
				  " is not a legal value ("+ROTOR_SPEED_SLOW+"/"+ROTOR_SPEED_FAST+").");
		}
		rotorSpeed = rs;
	}

	/**
	 * Return the current rotor speed.
	 * @return An integer representing the rotor speed, one of ROTOR_SPEED_SLOW or ROTOR_SPEED_FAST.
	 * @see #rotorSpeed
	 * @see #ROTOR_SPEED_SLOW
	 * @see #ROTOR_SPEED_FAST
	 */
	public int getRotorSpeed()
	{
		return rotorSpeed;
	}

	/**
	 * Return the current rotor speed as a string.
	 * @return An string representing the rotor speed, one of "slow" or "fast" or "unknown".
	 * @see #rotorSpeed
	 * @see #ROTOR_SPEED_SLOW
	 * @see #ROTOR_SPEED_FAST
	 * @see #ROTOR_SPEED_SLOW_STRING
	 * @see #ROTOR_SPEED_FAST_STRING
	 */
	public String rotorSpeedToString()
	{
		switch(rotorSpeed)
		{
			case ROTOR_SPEED_SLOW:
				return ROTOR_SPEED_SLOW_STRING;
			case ROTOR_SPEED_FAST:
				return ROTOR_SPEED_FAST_STRING;
			default:
				return "unknown";
		}
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
	 * @see #rotorSpeedToString
	 */
	public String toString(String prefix)
	{
		StringBuffer sb = null;

		sb = new StringBuffer();
		sb.append(prefix+"Half-Wave Plate:\n");
		sb.append(prefix+"\tRotator Speed: "+rotorSpeedToString()+"\n");
		return sb.toString();
	}
}
