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


import org.eclipse.sisu.EagerSingleton;

import org.jetbrains.annotations.NonNls;

import org.sonatype.nexus.plugin.PluginIdentity;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * CAS Plugin
 *
 * @author Sebastian Sdorra <sebastian.sdorra@triology.de>
 */
@Named
@EagerSingleton
public class CasPlugin extends PluginIdentity {

    /**
     * Expected groupId for plugin artifact.
     */
    @NonNls
    public static final String GROUP_ID = "org.sonatype.nexus.plugins";

    /**
     * Prefix for ID-like things.
     */
    @NonNls
    public static final String ID_PREFIX = "cas";

    /**
     * Expected artifactId for plugin artifact.
     */
    @NonNls
    public static final String ARTIFACT_ID = "nexus-" + ID_PREFIX + "-plugin";

    //~--- constructors ---------------------------------------------------------

    @Inject
    public CasPlugin() throws Exception {
        super(GROUP_ID, ARTIFACT_ID);
    }
  
}
