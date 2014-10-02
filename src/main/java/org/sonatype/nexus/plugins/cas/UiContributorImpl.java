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

import org.sonatype.nexus.plugins.ui.contribution.UiContributorSupport;

//~--- JDK imports ------------------------------------------------------------

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Register CAS plugins as ui contributor.
 *
 * @author Sebastian Sdorra <sebastian.sdorra@triology.de>
 */
@Named
@Singleton
public class UiContributorImpl extends UiContributorSupport {
  
    @Inject
    public UiContributorImpl(final CasPlugin owner) {
      super(owner);
    }
  
}
