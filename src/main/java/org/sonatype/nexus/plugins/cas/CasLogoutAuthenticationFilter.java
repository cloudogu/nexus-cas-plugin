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

import org.apache.shiro.subject.Subject;

import org.sonatype.nexus.plugins.cas.config.CasPluginConfiguration;
import org.sonatype.security.SecuritySystem;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * CAS Logout Filter. The cas logout filter destroys the local session and 
 * redirects the user to the cas logout page.
 *
 * @author Sebastian Sdorra <sebastian.sdorra@triology.de>
 */
@Singleton
public class CasLogoutAuthenticationFilter extends HttpFilter {

    private final CasPluginConfiguration casPluginConfiguration;
    private final SecuritySystem securitySystem;
  
    @Inject
    public CasLogoutAuthenticationFilter(CasPluginConfiguration casPluginConfiguration, SecuritySystem securitySystem) {
        this.casPluginConfiguration = checkNotNull(casPluginConfiguration, "casPluginConfiguration");
        this.securitySystem = checkNotNull(securitySystem, "securitySystem");
    }

    @Override
    protected void doFilter(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        Subject subject = securitySystem.getSubject();

        if ((subject != null) && (subject.isAuthenticated() || subject.isRemembered())) {
            subject.logout();
        }

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect(createLogoutUrl(request));
    }

    private String createLogoutUrl(HttpServletRequest request) {
        String url;
        if ( CasUtil.isCasEnabled(securitySystem) ) {
            url = CasUtil.createCasLogoutUrl(casPluginConfiguration);
        } else {
            url = request.getContextPath();
        }
        return url;
    }
}
