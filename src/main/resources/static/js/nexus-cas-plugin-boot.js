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
/*global define,NX*/
define('nexus-cas-plugin-boot', [
  'Nexus/cas/controller/Cas'
],
function () {
  NX.log.debug('Module loaded: nexus-cas-plugin-boot');
  
  // load cas controller
  NX.create('Nexus.cas.controller.Cas').init();
});