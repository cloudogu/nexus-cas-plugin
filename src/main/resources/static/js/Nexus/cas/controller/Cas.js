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
NX.define('Nexus.cas.controller.Cas', {
  extend: 'Nexus.controller.Controller',
  
  require: [
    'extjs', 'sonatype', 'Sonatype/all'
  ],

  init: function(){
    // override logout
    Sonatype.repoServer.RepoServer.logout = this.logout;
    // override login handler
    Sonatype.repoServer.RepoServer.loginHandler = this.loginHandler;
  },
  
  loginHandler: function(){
    if (Sonatype.user.curr.isLoggedIn)
    {
      // user is already authenticated, call logout
      this.logout();
    } 
    else 
    {
      // redirect to cas login filter
      window.location = Sonatype.config.contextPath + '/cas/login';
    }
  },
  
  logout: function(){
    // stop refresh task and redirect to cas logout filter
    Sonatype.utils.refreshTask.stop();
    window.location = Sonatype.config.contextPath + '/cas/logout';
  }
  
});