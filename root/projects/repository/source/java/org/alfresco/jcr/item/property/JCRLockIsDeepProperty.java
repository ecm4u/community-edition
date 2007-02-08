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
package org.alfresco.jcr.item.property;

import javax.jcr.RepositoryException;

import org.alfresco.jcr.dictionary.JCRNamespace;
import org.alfresco.jcr.item.NodeImpl;
import org.alfresco.jcr.item.PropertyImpl;
import org.alfresco.service.namespace.QName;

/**
 * Implementation for mix:lockable lockIsDeep property
 * 
 * @author David Caruana
 */
public class JCRLockIsDeepProperty extends PropertyImpl
{
    public static QName PROPERTY_NAME = QName.createQName(JCRNamespace.JCR_URI, "lockIsDeep");
    

    /**
     * Construct
     * 
     * @param node
     */
    public JCRLockIsDeepProperty(NodeImpl node)
    {
        super(node, PROPERTY_NAME);
    }

    @Override
    protected Object getPropertyValue() throws RepositoryException
    {
        return false;
    }
    
}
