/*
 * Copyright (c) 2014 Sonatype, Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sonatype, Inc. - initial API and implementation and/or initial documentation
 */



package org.sonatype.nexus.plugins.cas;

import com.google.common.base.Strings;

import org.apache.shiro.cas.CasToken;
import org.apache.shiro.subject.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.sonatype.nexus.plugins.cas.config.CasPluginConfiguration;
import org.sonatype.nexus.plugins.cas.config.model.v1_0_0.Configuration;
import org.sonatype.security.SecuritySystem;
import org.sonatype.security.authentication.AuthenticationException;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cas authentication filter. The cas authentication filter redirects the user
 * to the cas login page and authenticates the user if the request contains a 
 * cas ticket id. This filter redirects only web browsers.
 *
 * @author Sebastian Sdorra <sebastian.sdorra@triology.de>
 */
@Singleton
public class CasAuthenticationFilter extends HttpFilter {

    private static final String HEADER_CAS_REDIRECT = "X-CAS-Redirect";
  
    private static final String TICKET_PARAMETER = "ticket";
    private static final Logger logger = LoggerFactory.getLogger(CasAuthenticationFilter.class);

    private final CasPluginConfiguration casPluginConfiguration;
    private final SecuritySystem securitySystem;

    @Inject
    public CasAuthenticationFilter(CasPluginConfiguration casPluginConfiguration,
      SecuritySystem securitySystem) {
        this.casPluginConfiguration = checkNotNull(casPluginConfiguration, "casPluginConfiguration");
        this.securitySystem = checkNotNull(securitySystem, "securitySystem");
    }

    @Override
    protected void doFilter(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        if (CasUtil.isCasEnabled(securitySystem) && isCasUrlOrRestricted(request)) {
            doCasFilter(request, response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void doCasFilter(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        Subject subject = securitySystem.getSubject();

        if (!subject.isAuthenticated() && CasUtil.isBrowser(request)) {
            String ticket = request.getParameter(TICKET_PARAMETER);

            if (Strings.isNullOrEmpty(ticket)) {
                sendRedirect(request, response, CasUtil.createCasLoginUrl(casPluginConfiguration));
            } else {
                logger.info("create cas authentication token for ticket {}", ticket);

                try {
                    Configuration config = casPluginConfiguration.getConfiguration();

                    securitySystem.login(new CasToken(ticket));

                    // redirect again to remove token from url
                    sendRedirect(request, response, config.getCasService());
              } catch (AuthenticationException ex) {
                  throw new ServletException("authentication failed", ex);
              }
            }
        }
        else
        {
            chain.doFilter(request, response);
        }
    }
    
    private void sendRedirect( HttpServletRequest request, HttpServletResponse response, String location ) throws IOException {
        if ( CasUtil.isWebInterface(request) ){
            response.setHeader(HEADER_CAS_REDIRECT, location);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            response.sendRedirect(location);
        }
    }

    private boolean isCasUrlOrRestricted(HttpServletRequest request) {
        return !securitySystem.isAnonymousAccessEnabled()
          || request.getRequestURI().endsWith("/cas/login");
    }
}
