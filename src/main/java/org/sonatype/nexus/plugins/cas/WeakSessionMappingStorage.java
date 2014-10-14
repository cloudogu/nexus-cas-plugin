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

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.jasig.cas.client.session.SessionMappingStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sebastian Sdorra <sebastian.sdorra@triology.de>
 */
public class WeakSessionMappingStorage implements SessionMappingStorage
{
    private static final Logger log = LoggerFactory.getLogger(WeakSessionMappingStorage.class);
  
    /**
     * Maps the ID from the CAS server to the Session.
     */
    private final Map<String,WeakReference<HttpSession>> managedSessions = new HashMap<String,WeakReference<HttpSession>>();

    
    private final BiMap<String,String> mappingTable = HashBiMap.create();

   @Override
	public synchronized void addSessionById(String mappingId, HttpSession session) {
      String sessionId = session.getId();
      mappingTable.put(mappingId, sessionId);
      managedSessions.put(sessionId, new WeakReference<HttpSession>(session));
	}                               

    @Override
	public synchronized void removeBySessionById(String sessionId) {
        if (log.isDebugEnabled()) {
            log.debug("Attempting to remove Session=[" + sessionId + "]");
        }

        final String key = mappingTable.inverse().get(sessionId);

        if (log.isDebugEnabled()) {
            if (key != null) {
                log.debug("Found mapping for session.  Session Removed.");
            } else {
                log.debug("No mapping for session found.  Ignoring.");
            }
        }
        mappingTable.remove(key);
        managedSessions.remove(sessionId);
	}

    @Override
	public synchronized HttpSession removeSessionByMappingId(String mappingId) {
        HttpSession session = null;
        String sessionId = mappingTable.remove(mappingId);
        if (sessionId != null ){
            final WeakReference<HttpSession> sessionRef = managedSessions.remove(sessionId);
            if (sessionRef != null) {
                session = sessionRef.get();
                if ( session != null ){
                  log.debug("remove session with id {}", sessionId);
                } else {
                  log.debug("session {} seems to be gone", sessionId);
                }
            }
        }

        return session;
	}
}
