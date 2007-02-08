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
 */
package org.alfresco.repo.version.common;

import java.util.Collection;

import org.alfresco.repo.version.VersionModel;
import org.alfresco.service.cmr.version.ReservedVersionNameException;

/**
 * Helper class containing helper methods for the versioning services.
 * 
 * @author Roy Wetherall
 */
public class VersionUtil
{
    /**
     * Reserved property names
     */
    public static final String[] RESERVED_PROPERTY_NAMES = new String[]{
        VersionModel.PROP_CREATED_DATE, 
        VersionModel.PROP_FROZEN_NODE_ID, 
        VersionModel.PROP_FROZEN_NODE_STORE_ID, 
        VersionModel.PROP_FROZEN_NODE_STORE_PROTOCOL,
        VersionModel.PROP_FROZEN_NODE_TYPE,
        VersionModel.PROP_FROZEN_ASPECTS,
        VersionModel.PROP_VERSION_LABEL,
        VersionModel.PROP_VERSION_NUMBER};
    
    /**
     * Checks that the names of the additional version properties are valid and that they do not clash
     * with the reserved properties.
     * 
     * @param versionProperties  the property names 
     * @return                   true is the names are considered valid, false otherwise
     * @throws                   ReservedVersionNameException
     */
    public static void checkVersionPropertyNames(Collection<String> names)
        throws ReservedVersionNameException
    {
        for (String name : RESERVED_PROPERTY_NAMES)
        {
            if (names.contains(name) == true)
            {
                throw new ReservedVersionNameException(name);
            }
        }
    }
}
