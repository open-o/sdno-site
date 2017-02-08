/*
 * Copyright 2017 Huawei Technologies Co., Ltd.
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

package org.openo.sdno.localsiteservice.inf.testcase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.drivermanager.DriverRegisterManager;
import org.openo.sdno.localsiteservice.inf.mocoserver.InterfaceEmptyMocoServer;
import org.openo.sdno.localsiteservice.responsechecker.StatusAndBodyChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.testmanager.TestManager;
import org.openo.sdno.testframework.topology.Topology;

public class InterfaceEmptyTest extends TestManager {

    private static final String EMPTY_QUERY_TESTCASE =
            "src/integration-test/resources/interface/testcase/emptyquery.json";

    private static final String TOPO_DATA_PATH = "src/integration-test/resources/interface/topodata";

    private InterfaceEmptyMocoServer emptyMocoServer = new InterfaceEmptyMocoServer();

    private Topology topo = new Topology(TOPO_DATA_PATH);

    @Before
    public void setup() throws ServiceException {
        topo.createInvTopology();
        DriverRegisterManager.registerDriver();
        emptyMocoServer.start();
    }

    @After
    public void tearDown() throws ServiceException {
        emptyMocoServer.stop();
        DriverRegisterManager.unRegisterDriver();
        topo.clearInvTopology();
    }

    @Test
    public void queryEmptyInterfaceTest() throws ServiceException {
        HttpRquestResponse httpQueryObject = HttpModelUtils.praseHttpRquestResponseFromFile(EMPTY_QUERY_TESTCASE);
        execTestCase(httpQueryObject.getRequest(), new StatusAndBodyChecker(httpQueryObject.getResponse()));
    }

}
