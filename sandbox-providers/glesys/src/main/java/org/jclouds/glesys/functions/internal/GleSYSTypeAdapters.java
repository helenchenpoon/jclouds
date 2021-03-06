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
package org.jclouds.glesys.functions.internal;

import java.io.IOException;

import org.jclouds.glesys.domain.ServerState;
import org.jclouds.glesys.domain.ServerUptime;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * @author Adam Lowe
 */
public class GleSYSTypeAdapters {

   public static class ServerStateAdapter extends TypeAdapter<ServerState> {
      @Override
      public void write(JsonWriter writer, ServerState value) throws IOException {
         writer.value(value.value());
      }

      @Override
      public ServerState read(JsonReader reader) throws IOException {
         return ServerState.fromValue(reader.nextString());
      }
   }

   public static class ServerUptimeAdapter extends TypeAdapter<ServerUptime> {
      @Override
      public void write(JsonWriter writer, ServerUptime value) throws IOException {
         writer.value(value.toString());
      }

      @Override
      public ServerUptime read(JsonReader reader) throws IOException {
         return ServerUptime.fromValue(reader.nextString());
      }
   }
}