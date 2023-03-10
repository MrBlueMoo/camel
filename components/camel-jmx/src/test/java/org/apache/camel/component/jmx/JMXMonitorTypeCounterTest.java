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
package org.apache.camel.component.jmx;

import java.io.File;

import org.apache.camel.component.jmx.beans.ISimpleMXBean;
import org.junit.jupiter.api.Test;

public class JMXMonitorTypeCounterTest extends SimpleBeanFixture {

    @Test
    public void counter() throws Exception {

        ISimpleMXBean simpleBean = getSimpleMXBean();

        // we should get an event after the monitor number reaches 3
        simpleBean.setMonitorNumber(1);
        // this should trigger a notification
        simpleBean.setMonitorNumber(3);

        // we should get 1 change from the counter bean
        getMockFixture().waitForMessages();
        getMockFixture().assertMessageReceived(new File("src/test/resources/monitor-consumer/monitorNotification.xml"));

        // the offset value was two, so another change won't trigger an event.
        // it'll take 2 more changes to trigger the event
        getMockFixture().getMockEndpoint().setExpectedMessageCount(1);
        simpleBean.setMonitorNumber(4);
        simpleBean.setMonitorNumber(5);
        getMockFixture().waitForMessages();
        getMockFixture().assertMessageReceived(new File("src/test/resources/monitor-consumer/monitorNotification-2.xml"));
    }

    @Override
    protected JMXUriBuilder buildFromURI() {
        return super.buildFromURI().withMonitorType("counter")
                .withGranularityPeriod(500)
                .withObservedAttribute("MonitorNumber")
                .withInitThreshold(2)
                .withOffset(2)
                .withModulus(100)
                .withDifferenceMode(false);
    }
}
