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
package org.alfresco.repo.security.authentication;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.alfresco.repo.cache.SimpleCache;
import org.alfresco.service.cmr.repository.datatype.Duration;
import org.alfresco.util.GUID;
public class InMemoryTicketComponentImpl implements TicketComponent
{
    public static final String GRANTED_AUTHORITY_TICKET_PREFIX = "TICKET_";

    private boolean ticketsExpire;

    private Duration validDuration;
    
    private boolean oneOff;

    private SimpleCache<String, Ticket> ticketsCache;     // Can't use Ticket as it's private

    public InMemoryTicketComponentImpl()
    {
        super();
    }

    public void setTicketsCache(SimpleCache<String, Ticket> ticketsCache)
    {
        this.ticketsCache = ticketsCache; 
    }

    public String getTicket(String userName) throws AuthenticationException
    {
            Date expiryDate = null;
            if (ticketsExpire)
            {
                expiryDate = Duration.add(new Date(), validDuration);
            }
            Ticket ticket = new Ticket(ticketsExpire, expiryDate, userName);
            ticketsCache.put(ticket.getTicketId(), ticket);
       
            return GRANTED_AUTHORITY_TICKET_PREFIX + ticket.getTicketId();
    }

    public String validateTicket(String ticketString) throws AuthenticationException
    {
        if (ticketString.length() < GRANTED_AUTHORITY_TICKET_PREFIX.length())
        {
           throw new AuthenticationException(ticketString  + " is an invalid ticket format");
        }
        
        String key = ticketString.substring(GRANTED_AUTHORITY_TICKET_PREFIX.length());
        Ticket ticket = ticketsCache.get(key);
        if (ticket == null)
        {
            throw new AuthenticationException("Missing ticket for " + ticketString);
        }
        if (ticket.hasExpired())
        {
            throw new TicketExpiredException("Ticket expired for " + ticketString);
        }
        // TODO: Recheck the user details here
        // TODO: Strengthen ticket as GUID is predicatble
        if(oneOff)
        {
            ticketsCache.remove(key);
        }
        return ticket.getUserName();
    }
    
    public void invalidateTicketById(String ticketString)
    {
        String key = ticketString.substring(GRANTED_AUTHORITY_TICKET_PREFIX.length());
        ticketsCache.remove(key);
    }
    
    public void invalidateTicketByUser(String userName)
    {
        Set<String> toRemove = new HashSet<String>();
        
        for(String key: ticketsCache.getKeys())
        {
            Ticket ticket = ticketsCache.get(key);
            if(ticket.getUserName().equals(userName))
            {
                toRemove.add(ticket.getTicketId());
            }
        }
        
        for(String id: toRemove)
        {
            ticketsCache.remove(id);
        }
    }
    
    
    
    public static class Ticket implements Serializable
    {
        private static final long serialVersionUID = -5904510560161261049L;

        private boolean expires;

        private Date expiryDate;

        private String userName;

        private String ticketId;

        Ticket(boolean expires, Date expiryDate, String userName)
        {
            this.expires = expires;
            this.expiryDate = expiryDate;
            this.userName = userName;
            this.ticketId = GUID.generate();
        }

        /**
         * Has the tick expired
         * 
         * @return
         */
        boolean hasExpired()
        {
            if (expires && (expiryDate != null) && (expiryDate.compareTo(new Date()) < 0))
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public boolean equals(Object o)
        {
            if (o == this)
            {
                return true;
            }
            if (!(o instanceof Ticket))
            {
                return false;
            }
            Ticket t = (Ticket) o;
            return (this.expires == t.expires) && this.expiryDate.equals(t.expiryDate) && this.userName.equals(t.userName) && this.ticketId.equals(t.ticketId);
        }

        public int hashCode()
        {
            return ticketId.hashCode();
        }

        protected boolean getExpires()
        {
            return expires;
        }

        protected Date getExpiryDate()
        {
            return expiryDate;
        }

        protected String getTicketId()
        {
            return ticketId;
        }

        protected String getUserName()
        {
            return userName;
        }

    }



    public void setOneOff(boolean oneOff)
    {
        this.oneOff = oneOff;
    }
    

    public void setTicketsExpire(boolean ticketsExpire)
    {
        this.ticketsExpire = ticketsExpire;
    }
    

    public void setValidDuration(String validDuration)
    {
        this.validDuration = new Duration(validDuration);
    }
    
}
