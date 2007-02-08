/*
 * Copyright (C) 2005-2006 Alfresco, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.alfresco.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A class that keeps track of the VM shutdown status.  It can be
 * used by threads either as a singleton to check if the
 * VM shutdown status has been activated.
 * <p>
 * <b>NOTE: </b> In order to prevent a proliferation of shutdown hooks,
 *      it is advisable to use instances as singletons only. 
 * <p>
 * This component should be used by long-running, but interruptable processes.
 * 
 * @author Derek Hulley
 */
public class VmShutdownListener
{
    private Log logger;
    private boolean vmShuttingDown;
    
    /**
     * Constructs this instance to listen to the VM shutdown call.
     *
     */
    public VmShutdownListener(final String name)
    {
        logger = LogFactory.getLog(VmShutdownListener.class);
        
        vmShuttingDown = false;
        Runnable shutdownRunnable = new Runnable()
        {
            public void run()
            {
                vmShuttingDown = true;
                if (logger.isDebugEnabled())
                {
                    logger.debug("VM shutdown detected by listener " + name);
                }
            };  
        };
        Thread shutdownThread = new Thread(shutdownRunnable);
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }

    /**
     * @return Returns true if the VM shutdown signal was detected.
     */
    public boolean isVmShuttingDown()
    {
        return vmShuttingDown;
    }
}
