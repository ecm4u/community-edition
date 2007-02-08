/*
 * Copyright (C) 2005 Alfresco, Inc.
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
 *
 * Script: onAddAspect_rmaCutoff.js
 * Author: Roy Wetherall
 * 
 * Behaviour script executed when the cutoff aspect is added.
 */ 
 
var record = behaviour.args[0];

if (record.hasAspect(rm.ASPECT_RECORD) == true)
{
    var filePlan = rm.getFilePlan(record);    
    
    if (filePlan.properties[rm.PROP_PROCESS_HOLD]) 
    {
        /// Hold the record
        record.addAspect(rm.ASPECT_HELD);
    }
    else
    {
        // Process any dispositions that should occure immediatly
        rm.processImmediateDispositions(record);
    }
    
    // Ensure that once a record is cutoff only the record managers can see it
    rm.setCutoffPermissions(record);
}