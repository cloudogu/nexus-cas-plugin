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

import com.google.inject.AbstractModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.sonatype.nexus.guice.FilterChainModule;

import static org.sonatype.nexus.security.filter.FilterProviderSupport
  .filterKey;

//~--- JDK imports ------------------------------------------------------------

import javax.inject.Named;

/**
 * Register CAS filters.
 *
 * @author Sebastian Sdorra <sebastian.sdorra@triology.de>
 */
@Named
public class CasAuthenticationModule extends AbstractModule
{

  /** Field description */
  private static final Logger log =
    LoggerFactory.getLogger(CasAuthenticationModule.class);

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  @Override
  protected void configure()
  {
    bind(filterKey("casSingleSignOut")).to(CasSingleSignOutFilter.class);
    bind(filterKey("casLogout")).to(CasLogoutAuthenticationFilter.class);
    bind(filterKey("casAuth")).to(CasAuthenticationFilter.class);

    install(new FilterChainModule()
    {

      @Override
      protected void configure()
      {
        addFilterChain("/cas/logout", "casSingleSignOut,casLogout");

        // addFilterChain("", "casAuth");
        addFilterChain("/", "casSingleSignOut,casAuth");
        addFilterChain("/index.html", "casSingleSignOut,casAuth");
        addFilterChain("/cas/login", "casSingleSignOut,casAuth");
        addFilterChain("/service/**", "casSingleSignOut,casAuth");
        addFilterChain("/**", "casSingleSignOut");
      }

    });
  }
}
