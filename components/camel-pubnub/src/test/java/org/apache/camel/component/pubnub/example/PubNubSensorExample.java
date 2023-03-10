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
package org.apache.camel.component.pubnub.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

import static org.apache.camel.component.pubnub.example.PubNubExampleConstants.PUBNUB_SUBSCRIBE_KEY;

public final class PubNubSensorExample {

    private PubNubSensorExample() {
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new SensorRoute());
        main.run();
    }

    static class SensorRoute extends RouteBuilder {
        @Override
        public void configure() {
            from("pubnub:pubnub-sensor-network?uuid=camel&subscribeKey=" + PUBNUB_SUBSCRIBE_KEY).log("${body}")
                    .to("mock:result");
        }
    }

}
