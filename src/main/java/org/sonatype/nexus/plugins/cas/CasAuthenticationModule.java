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
import com.google.inject.servlet.ServletModule;

//~--- JDK imports ------------------------------------------------------------

import javax.inject.Named;

/**
 * Register CAS filters.
 * 
 * @author Sebastian Sdorra <sebastian.sdorra@triology.de>
 */
@Named
public class CasAuthenticationModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ServletModule() {

            @Override
            protected void configureServlets() {
                filter("/*").through(CasSingleSignOutFilter.class);
                filter("/cas/logout").through(CasLogoutAuthenticationFilter.class);
                filter("/*").through(CasAuthenticationFilter.class);
            }

        });
    }
}
