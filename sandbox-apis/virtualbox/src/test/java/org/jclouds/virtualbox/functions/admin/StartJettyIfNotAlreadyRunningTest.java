/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jclouds.virtualbox.functions.admin;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.testng.Assert.assertEquals;

import java.net.URI;

import org.eclipse.jetty.server.Server;
import org.testng.annotations.Test;

/**
 * @author Andrea Turli, Adrian Cole
 */
@Test(groups = "unit", singleThreaded = true, testName = "StartJettyIfNotAlreadyRunningTest")
public class StartJettyIfNotAlreadyRunningTest {
   @Test
   public void testLaunchJettyServerWhenAlreadyRunningDoesntLaunchAgain() {
      Server jetty = createMock(Server.class);

      String preconfigurationUrl = "http://foo:8080";
      
      expect(jetty.getState()).andReturn(Server.STARTED);
      replay(jetty);

      StartJettyIfNotAlreadyRunning starter = new StartJettyIfNotAlreadyRunning(jetty, preconfigurationUrl);
      starter.start();

      assertEquals(starter.get(), URI.create(preconfigurationUrl));
      verify(jetty);

   }


   @Test
   public void testLaunchJettyServerWhenNotRunningStartsJettyOnCorrectHostPortAndBasedir() {
      // TODO: all yours!
   }
}
