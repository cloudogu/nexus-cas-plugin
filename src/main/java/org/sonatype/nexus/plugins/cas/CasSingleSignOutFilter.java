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

//~--- non-JDK imports --------------------------------------------------------

import org.jasig.cas.client.session.SingleSignOutHandler;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import javax.inject.Singleton;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra <sebastian.sdorra@triology.de>
 */
@Singleton
public class CasSingleSignOutFilter extends HttpFilter
{
  
  /**
   * Method description
   *
   *
   * @param filterConfig
   *
   * @throws ServletException
   */
  @Override
  public void init(FilterConfig filterConfig) throws ServletException
  {
    super.init(filterConfig);
    WeakSessionMappingStorage sessionStorage = new WeakSessionMappingStorage();
    handler.setSessionMappingStorage(sessionStorage);
    handler.init();
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param chain
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void doFilter(HttpServletRequest request,
    HttpServletResponse response, FilterChain chain)
    throws IOException, ServletException
  {
    if (handler.isTokenRequest(request))
    {
      handler.recordSession(request);
      chain.doFilter(request, response);
    }
    else if (handler.isLogoutRequest(request))
    {
      handler.destroySession(request);
    }
    else
    {
      chain.doFilter(request, response);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final SingleSignOutHandler handler = new SingleSignOutHandler();
}
