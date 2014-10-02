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

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Abstract {@link Filter} which casts {@link ServletRequest} to 
 * {@link HttpServletRequest} and {@link ServletResponse} to 
 * {@link HttpServletResponse}.
 *
 * @author Sebastian Sdorra <sebastian.sdorra@triology.de>
 */
public abstract class HttpFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
      if ( request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
          doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
      } else {
          throw new ServletException("not a http request");
      }
    }

    @Override
    public void destroy() { }

    protected abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException;
}
