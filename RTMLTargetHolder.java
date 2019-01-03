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
// RTMLTargetHolder.java
// $Header: /space/home/eng/cjm/cvs/org_estar_rtml/RTMLTargetHolder.java,v 1.1 2009-08-12 14:42:31 cjm Exp $
package org.estar.rtml;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * This interface is implemented by classes holding target data.
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 */
public interface RTMLTargetHolder
{
	/**
	 * Revision control system version id.
	 */
	public final static String RCSID = "$Id: RTMLTargetHolder.java,v 1.1 2009-08-12 14:42:31 cjm Exp $";
	/**
	 * Set the target.
	 * @param target The target to set.
	 */
	public void setTarget(RTMLTarget target);
	/**
	 * Get the target.
	 * @return The target.
	 */
	public RTMLTarget getTarget();
}
//
// $Log: not supported by cvs2svn $
//
