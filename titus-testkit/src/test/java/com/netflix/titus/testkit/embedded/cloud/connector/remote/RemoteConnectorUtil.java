/*
 * Copyright 2018 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.titus.testkit.embedded.cloud.connector.remote;

import com.google.inject.AbstractModule;
import com.netflix.governator.InjectorBuilder;
import com.netflix.governator.LifecycleInjector;
import com.netflix.titus.common.util.tuple.Pair;
import com.netflix.titus.testkit.embedded.cloud.SimulatedCloud;
import com.netflix.titus.testkit.embedded.cloud.SimulatedCloudConfiguration;
import com.netflix.titus.testkit.embedded.cloud.endpoint.SimulatedCloudEndpointModule;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

class RemoteConnectorUtil {

    static LifecycleInjector createSimulatedCloudGrpcServer(SimulatedCloud cloud, int grpcPort) {
        return InjectorBuilder.fromModules(
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(SimulatedCloud.class).toInstance(cloud);
                        bind(SimulatedCloudConfiguration.class).toInstance(newConfiguration(grpcPort));
                    }
                },
                new SimulatedCloudEndpointModule()
        ).createInjector();
    }

    static SimulatedCloudConfiguration newConfiguration(int grpcPort) {
        SimulatedCloudConfiguration mock = Mockito.mock(SimulatedCloudConfiguration.class);
        when(mock.getGrpcPort()).thenReturn(grpcPort);
        return mock;
    }

    static CloudSimulatorResolver newConnectorConfiguration(int grpcPort) {
        CloudSimulatorResolver mock = Mockito.mock(CloudSimulatorResolver.class);
        when(mock.resolveGrpcEndpoint()).thenReturn(Pair.of("localhost", grpcPort));
        return mock;
    }
}