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
package org.apache.camel.spring.processor.onexception;

import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Headers;

/**
 * Order service as a plain POJO class
 */
public class OrderService {

    /**
     * This method handle our order input and return the order
     *
     * @param  headers              the in headers
     * @param  payload              the in payload
     * @return                      the out payload
     * @throws OrderFailedException is thrown if the order cannot be processed
     */
    public Object handleOrder(@Headers Map<String, Object> headers, @Body String payload)
            throws OrderFailedException {
        if ("Order: kaboom".equals(payload)) {
            throw new OrderFailedException("Cannot order: kaboom");
        } else {
            headers.put("orderid", "123");
            return "Order OK";
        }
    }

    /**
     * This method creates the response to the caller if the order could not be processed
     *
     * @param  headers the in headers
     * @param  payload the in payload
     * @return         the out payload
     */
    public Object orderFailed(@Headers Map<String, Object> headers, @Body String payload) {
        headers.put("orderid", "failed");
        return "Order ERROR";
    }
}
