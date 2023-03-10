/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.vertx.websocket;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class VertxWebsocketHelper {

    private VertxWebsocketHelper() {
        // Utility class
    }

    /**
     * Creates a VertxWebsocketHostKey from a given VertxWebsocketConfiguration
     */
    public static VertxWebsocketHostKey createHostKey(URI websockerURI) {
        return new VertxWebsocketHostKey(websockerURI.getHost(), websockerURI.getPort());
    }

    /**
     * Appends a header value to exchange headers, using a List if there are multiple items for the same key
     */
    @SuppressWarnings("unchecked")
    public static void appendHeader(Map<String, Object> headers, String key, Object value) {
        if (headers.containsKey(key)) {
            Object existing = headers.get(key);
            List<Object> list;
            if (existing instanceof List) {
                list = (List<Object>) existing;
            } else {
                list = new ArrayList<>();
                list.add(existing);
            }
            list.add(value);
            value = list;
        }
        headers.put(key, value);
    }
}
