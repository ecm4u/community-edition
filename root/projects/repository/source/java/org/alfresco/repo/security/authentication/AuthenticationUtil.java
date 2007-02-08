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

import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.GrantedAuthority;
import net.sf.acegisecurity.GrantedAuthorityImpl;
import net.sf.acegisecurity.UserDetails;
import net.sf.acegisecurity.context.Context;
import net.sf.acegisecurity.context.ContextHolder;
import net.sf.acegisecurity.context.security.SecureContext;
import net.sf.acegisecurity.context.security.SecureContextImpl;
import net.sf.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import net.sf.acegisecurity.providers.dao.User;

import org.alfresco.service.cmr.security.PermissionService;

public abstract class AuthenticationUtil
{

    public interface RunAsWork<Result>
    {
        /**
         * Method containing the work to be done in the user transaction.
         * 
         * @return Return the result of the operation
         */
        Result doWork() throws Exception;
    }

    private static final String SYSTEM_USER_NAME = "System";

    private AuthenticationUtil()
    {
        super();
    }

    public static Authentication setCurrentUser(String userName)
    {
        return setCurrentUser(userName, getDefaultUserDetails(userName));
    }

    /**
     * Explicitly set the current user to be authenticated.
     * 
     * @param userName - String user id
     * @param providedDetails - provided details for the user
     * 
     * @return Authentication
     */
    public static Authentication setCurrentUser(String userName, UserDetails providedDetails)
            throws AuthenticationException
    {
        if (userName == null)
        {
            throw new AuthenticationException("Null user name");
        }

        try
        {
            UserDetails ud = null;
            if (userName.equals(SYSTEM_USER_NAME))
            {
                GrantedAuthority[] gas = new GrantedAuthority[1];
                gas[0] = new GrantedAuthorityImpl("ROLE_SYSTEM");
                ud = new User(SYSTEM_USER_NAME, "", true, true, true, true, gas);
            }
            else if (userName.equalsIgnoreCase(PermissionService.GUEST_AUTHORITY))
            {
                GrantedAuthority[] gas = new GrantedAuthority[0];
                ud = new User(PermissionService.GUEST_AUTHORITY.toLowerCase(), "", true, true, true, true, gas);
            }
            else
            {
                if (providedDetails.getUsername().equals(userName))
                {
                    ud = providedDetails;
                }
                else
                {
                    throw new AuthenticationException("Provided user details do not match the user name");
                }
            }

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(ud, "", ud
                    .getAuthorities());
            auth.setDetails(ud);
            auth.setAuthenticated(true);
            return setCurrentAuthentication(auth);
        }
        catch (net.sf.acegisecurity.AuthenticationException ae)
        {
            throw new AuthenticationException(ae.getMessage(), ae);
        }
    }

    /**
     * Default implementation that makes an ACEGI object on the fly
     * 
     * @param userName
     * @return
     */
    private static UserDetails getDefaultUserDetails(String userName)
    {
        GrantedAuthority[] gas = new GrantedAuthority[1];
        gas[0] = new GrantedAuthorityImpl("ROLE_AUTHENTICATED");
        UserDetails ud = new User(userName, "", true, true, true, true, gas);
        return ud;
    }

    /**
     * Explicitly set the current authentication.
     * 
     * @param authentication
     *            Authentication
     */
    public static Authentication setCurrentAuthentication(Authentication authentication)
    {
        Context context = ContextHolder.getContext();
        SecureContext sc = null;
        if ((context == null) || !(context instanceof SecureContext))
        {
            sc = new SecureContextImpl();
            ContextHolder.setContext(sc);
        }
        else
        {
            sc = (SecureContext) context;
        }
        authentication.setAuthenticated(true);
        sc.setAuthentication(authentication);
        return authentication;
    }

    /**
     * Get the current authentication context
     * 
     * @return Authentication
     * @throws AuthenticationException
     */
    public static Authentication getCurrentAuthentication() throws AuthenticationException
    {
        Context context = ContextHolder.getContext();
        if ((context == null) || !(context instanceof SecureContext))
        {
            return null;
        }
        return ((SecureContext) context).getAuthentication();
    }

    /**
     * Get the current user name.
     * 
     * @return String
     * @throws AuthenticationException
     */
    public static String getCurrentUserName() throws AuthenticationException
    {
        Context context = ContextHolder.getContext();
        if ((context == null) || !(context instanceof SecureContext))
        {
            return null;
        }
        return getUserName(((SecureContext) context).getAuthentication());
    }

    /**
     * Get the current user name
     * 
     * @param authentication
     *            Authentication
     * @return String
     */
    private static String getUserName(Authentication authentication)
    {
        String username = authentication.getPrincipal().toString();

        if (authentication.getPrincipal() instanceof UserDetails)
        {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        }

        return username;
    }

    /**
     * Set the system user as the current user.
     * 
     * @return Authentication
     */
    public static Authentication setSystemUserAsCurrentUser()
    {
        return setCurrentUser(SYSTEM_USER_NAME);
    }

    /**
     * Get the name of the system user
     * 
     * @return String
     */
    public static String getSystemUserName()
    {
        return SYSTEM_USER_NAME;
    }

    /**
     * Get the name of the Guest User
     */
    public static String getGuestUserName()
    {
        return PermissionService.GUEST_AUTHORITY.toLowerCase();
    }

    /**
     * Remove the current security information
     */
    public static void clearCurrentSecurityContext()
    {
        ContextHolder.setContext(null);
    }

    public static <R> R runAs(RunAsWork<R> runAsWork, String uid)
    {
        String currentUser = AuthenticationUtil.getCurrentUserName();

        R result = null;
        try
        {
            AuthenticationUtil.setCurrentUser(uid);
            result = runAsWork.doWork();
            return result;
        }
        catch (Throwable exception)
        {

            // Re-throw the exception
            if (exception instanceof RuntimeException)
            {
                throw (RuntimeException) exception;
            }
            else
            {
                throw new RuntimeException("Error during run as.", exception);
            }
        }
        finally
        {
            AuthenticationUtil.clearCurrentSecurityContext();
            if (currentUser != null)
            {
                AuthenticationUtil.setCurrentUser(currentUser);
            }
        }
    }
}
