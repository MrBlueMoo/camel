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
package org.apache.camel.spring;

import java.lang.management.ManagementFactory;

import javax.management.ObjectName;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test that verifies JMX can be disabled via Spring.
 *
 *
 */
public class DisableJmxAgentTest extends DefaultJMXAgentTest {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("org/apache/camel/spring/disableJmxConfig.xml");
    }

    @Override
    @Test
    public void testQueryMbeans() throws Exception {
        assertThrows(NullPointerException.class, () -> getMBeanConnection());

        // whats the numbers before, because the JVM can have left overs when unit testing
        int before = ManagementFactory.getPlatformMBeanServer()
                .queryNames(new ObjectName("org.apache.camel" + ":type=consumers,*"), null).size();

        // start route should enlist the consumer to JMX if JMX was enabled
        context.getRouteController().startRoute("foo");

        assertThrows(NullPointerException.class, () -> getMBeanConnection());

        int after = ManagementFactory.getPlatformMBeanServer()
                .queryNames(new ObjectName("org.apache.camel" + ":type=consumers,*"), null).size();
        assertEquals(before, after, "Should not have added consumer to JMX");
    }

}
