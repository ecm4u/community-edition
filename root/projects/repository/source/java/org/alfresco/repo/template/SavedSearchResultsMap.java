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
package org.alfresco.repo.template;

import java.io.StringReader;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.model.ContentModel;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.TemplateNode;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Provides functionality to load a saved search and execute it to return TemplateNode objects.
 * 
 * @author Kevin Roast
 */
public class SavedSearchResultsMap extends BaseSearchResultsMap
{
    private static final String ELEMENT_QUERY = "query";
    
    /**
     * Constructor
     * 
     * @param parent         The parent TemplateNode to execute searches from 
     * @param services       The ServiceRegistry to use
     */
    public SavedSearchResultsMap(TemplateNode parent, ServiceRegistry services)
    {
        super(parent, services);
    }

    /**
     * @see org.alfresco.repo.template.BaseTemplateMap#get(java.lang.Object)
     */
    public Object get(Object key)
    {
        String search = null;
        
        if (key != null && key.toString().length() != 0)
        {
            // read the Saved Search XML on the specified node - and get the Lucene search from it
            try
            {
                NodeRef ref = new NodeRef(key.toString());
                
                ContentReader content = services.getContentService().getReader(ref, ContentModel.PROP_CONTENT);
                if (content != null && content.exists())
                {
                    // get the root element
                    SAXReader reader = new SAXReader();
                    Document document = reader.read(new StringReader(content.getContentString()));
                    Element rootElement = document.getRootElement();
                    
                    Element queryElement = rootElement.element(ELEMENT_QUERY);
                    if (queryElement != null)
                    {
                        search = queryElement.getText();
                    }
                }
            }
            catch (Throwable err)
            {
                throw new AlfrescoRuntimeException("Failed to find or load saved Search: " + key, err);
            }
        }
        
        // execute the search
        return query(search);
    }
}
