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
package org.apache.camel.component.web3j;

import org.apache.camel.RuntimeCamelException;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.Transaction;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;

import static org.apache.camel.component.web3j.Web3jConstants.CATCH_UP_TO_LATEST_TRANSACTION_OBSERVABLE;
import static org.apache.camel.component.web3j.Web3jConstants.OPERATION;
import static org.mockito.ArgumentMatchers.any;

public class Web3jConsumerCatchUpToLatestTransactionsObservableMockTest extends Web3jMockTestSupport {

    @Mock
    private Observable<Transaction> observable;

    @Test
    public void successTest() throws Exception {
        mockError.expectedMinimumMessageCount(0);
        mockResult.expectedMinimumMessageCount(1);

        Mockito.when(mockWeb3j.catchUpToLatestTransactionObservable(any(DefaultBlockParameter.class))).thenReturn(observable);
        Mockito.when(observable.subscribe(any(), any(), any())).thenAnswer(new Answer() {
            public Subscription answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                ((Action1<Transaction>) args[0]).call(new Transaction());
                return subscription;
            }
        });

        context.start();
        mockResult.assertIsSatisfied();
        mockError.assertIsSatisfied();
    }

    @Test
    public void errorTest() throws Exception {
        mockResult.expectedMessageCount(0);
        mockError.expectedMinimumMessageCount(1);

        Mockito.when(mockWeb3j.catchUpToLatestTransactionObservable(any(DefaultBlockParameter.class))).thenReturn(observable);
        Mockito.when(observable.subscribe(any(), any(), any())).thenAnswer(new Answer() {
            public Subscription answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                ((Action1<Throwable>) args[1]).call(new RuntimeCamelException("Error"));
                return subscription;
            }
        });

        context.start();
        mockError.assertIsSatisfied();
        mockResult.assertIsSatisfied();
    }

    @Test
    public void doneTest() throws Exception {
        mockResult.expectedMessageCount(1);
        mockResult.expectedHeaderReceived("status", "done");
        mockError.expectedMinimumMessageCount(0);

        Mockito.when(mockWeb3j.catchUpToLatestTransactionObservable(any(DefaultBlockParameter.class))).thenReturn(observable);
        Mockito.when(observable.subscribe(any(), any(), any())).thenAnswer(new Answer() {
            public Subscription answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                ((Action0) args[2]).call();
                return subscription;
            }
        });

        context.start();
        mockError.assertIsSatisfied();
        mockResult.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                errorHandler(deadLetterChannel("mock:error"));
                from(getUrl() + OPERATION.toLowerCase() + "=" + CATCH_UP_TO_LATEST_TRANSACTION_OBSERVABLE
                     + "&fromBlock=5499965")
                        .to("mock:result");
            }
        };
    }
}
