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
package org.alfresco.web.ui.repo.component.shelf;

import org.alfresco.web.ui.common.component.SelfRenderingComponent;

/**
 * @author Kevin Roast
 */
public class UIShelfItem extends SelfRenderingComponent
{
   protected final static String SHELF_START = "<table border=0 cellspacing=3 cellpadding=0 width=100% valign=top>";
   protected final static String SHELF_END   = "</table>";
   
   
   // ------------------------------------------------------------------------------
   // Component Impl 

   /**
    * @see javax.faces.component.UIComponent#getFamily()
    */
   public String getFamily()
   {
      return "org.alfresco.faces.Shelf";
   }
}
