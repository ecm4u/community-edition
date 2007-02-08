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
package org.alfresco.repo.security.person;

import org.alfresco.service.cmr.repository.NodeRef;

/**
 * Provider to use in the boostrap process - does nothing
 * 
 * Probably not required as behaviour/policies are disabled during normal import.
 * 
 * @author Andy Hind
 */
public class BootstrapHomeFolderProvider extends AbstractHomeFolderProvider
{

    @Override
    protected HomeSpaceNodeRef getHomeFolder(NodeRef person)
    {
        return new HomeSpaceNodeRef(null, HomeSpaceNodeRef.Status.VALID);
    }

}
