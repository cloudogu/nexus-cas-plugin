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
import eu.bitwalker.useragentutils.BrowserType;
import eu.bitwalker.useragentutils.UserAgent;
import javax.servlet.http.HttpServletRequest;
import org.sonatype.nexus.plugins.cas.config.CasPluginConfiguration;
import org.sonatype.nexus.plugins.cas.config.model.v1_0_0.Configuration;
import org.sonatype.security.SecuritySystem;

/**
 * CAS util methods.
 *
 * @author Sebastian Sdorra <sebastian.sdorra@triology.de>
 */
public final class CasUtil {

    private CasUtil(){}


    /**
     * Returns {@code true} if the request was issued by a web browser.
     *
     *
     * @param request http request
     *
     * @return {@code true} if the request was issued by a web browser.
     */
    public static boolean isBrowser(HttpServletRequest request)
    {
      boolean browser = false;

      String ua = request.getHeader("User-Agent");

      if (!Strings.isNullOrEmpty(ua))
      {
        browser = isBrowser(ua);
      }

      return browser;
    }

    private static boolean isBrowser(String ua) {
        return BrowserType.WEB_BROWSER == UserAgent.parseUserAgentString(ua).getBrowser().getBrowserType();
    }


    /**
     * Returns the url to the cas login page.
     * 
     * @param casPluginConfiguration cas plugin configuration
     * 
     * @return cas login url
     */
    public static String createCasLoginUrl(CasPluginConfiguration casPluginConfiguration) {
        Configuration config = casPluginConfiguration.getConfiguration();

        String serverUrl = config.getCasServerUrl();
        StringBuilder buffer = new StringBuilder(serverUrl);

        if (!serverUrl.endsWith("/")) {
            buffer.append("/");
        }

        buffer.append("login?service=");
        String serviceUrl = config.getCasService();
        buffer.append(serviceUrl);
        if ( !serviceUrl.endsWith("/") ) {
            buffer.append("/");
        }
        buffer.append("cas/login");

        return buffer.toString();
    }

    /**
     * Returns {@code true} is the cas realm is enabled.
     * 
     * @param securitySystem nexus security system
     * 
     * @return {@code true} if cas is enabled
     */
    public static boolean isCasEnabled(SecuritySystem securitySystem) {
        return securitySystem.getRealms().contains(CasAuthenticatingRealm.ROLE);
    }

    /**
     * Returns the url to the cas logout page.
     * 
     * @param casPluginConfiguration cas plugin configuration
     * 
     * @return cas logout url
     */
    public static String createCasLogoutUrl(CasPluginConfiguration casPluginConfiguration) {
        Configuration config = casPluginConfiguration.getConfiguration();
        String serverUrl = config.getCasServerUrl();
        StringBuilder buffer = new StringBuilder(serverUrl);

        if (!serverUrl.endsWith("/"))
        {
            buffer.append("/");
        }

        buffer.append("logout");

        return buffer.toString();
    }

}
