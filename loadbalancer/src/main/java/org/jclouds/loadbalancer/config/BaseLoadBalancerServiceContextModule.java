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
package org.jclouds.loadbalancer.config;

import static org.jclouds.Constants.PROPERTY_SESSION_INTERVAL;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.collect.Memoized;
import org.jclouds.domain.Location;
import org.jclouds.rest.AuthorizationException;
import org.jclouds.rest.suppliers.MemoizedRetryOnTimeOutButNotOnAuthorizationExceptionSupplier;

import com.google.common.base.Supplier;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * 
 * @author Adrian Cole
 */
public abstract class BaseLoadBalancerServiceContextModule extends AbstractModule {

   protected AtomicReference<AuthorizationException> authException = new AtomicReference<AuthorizationException>();

   @Override
   protected void configure() {

   }

   @Provides
   @Singleton
   @Memoized
   protected Supplier<Set<? extends Location>> supplyLocationCache(@Named(PROPERTY_SESSION_INTERVAL) long seconds,
         final Supplier<Set<? extends Location>> locationSupplier) {
      return new MemoizedRetryOnTimeOutButNotOnAuthorizationExceptionSupplier<Set<? extends Location>>(authException, seconds,
            new Supplier<Set<? extends Location>>() {
               @Override
               public Set<? extends Location> get() {
                  return locationSupplier.get();
               }
            });
   }

}