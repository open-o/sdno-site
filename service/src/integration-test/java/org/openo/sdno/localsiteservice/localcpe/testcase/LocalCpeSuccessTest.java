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

package org.openo.sdno.localsiteservice.localcpe.testcase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.localsiteservice.cpe.mocoserver.CpeDeviceMocoServer;
import org.openo.sdno.localsiteservice.drivermanager.DriverRegisterManager;
import org.openo.sdno.localsiteservice.esrmanager.EsrRegisterManager;
import org.openo.sdno.localsiteservice.msbmanager.MsbRegisterManager;
import org.openo.sdno.localsiteservice.responsechecker.StatusAndBodyChecker;
import org.openo.sdno.localsiteservice.responsechecker.StatusChecker;
import org.openo.sdno.localsiteservice.vpnservice.mocoserver.VpnServiceSuccessMocoServer;
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiLocalCpeModel;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.testmanager.TestManager;

public class LocalCpeSuccessTest extends TestManager {

    private static final String CREATE_SITE_TESTCASE =
            "src/integration-test/resources/localcpe/testcase/createsite.json";

    private static final String DELETE_SITE_TESTCASE =
            "src/integration-test/resources/localcpe/testcase/deletesite.json";

    private static final String CREATE_LOCALCPE_TESTCASE =
            "src/integration-test/resources/localcpe/testcase/createlocalcpe.json";

    private static final String DELETE_LOCALCPE_TESTCASE =
            "src/integration-test/resources/localcpe/testcase/deletelocalcpe.json";

    private static final String QUERY_LOCALCPE_TESTCASE =
            "src/integration-test/resources/localcpe/testcase/querylocalcpe.json";

    private static final String BATCH_QUERY_LOCALCPE_TESTCASE =
            "src/integration-test/resources/localcpe/testcase/batchquerylocalcpe.json";

    private static final String BATCH_EMPTY_QUERY_LOCALCPE_TESTCASE =
            "src/integration-test/resources/localcpe/testcase/batchemptyquerylocalcpe.json";

    private static CpeDeviceMocoServer successMocoServer = new CpeDeviceMocoServer();

    private static VpnServiceSuccessMocoServer vpnGatewayMocoServer = new VpnServiceSuccessMocoServer();

    @BeforeClass
    public static void setup() throws ServiceException {
        MsbRegisterManager.registerOverlayMsb();
        DriverRegisterManager.registerDriver();
        EsrRegisterManager.registerEsr();
        successMocoServer.start();
        vpnGatewayMocoServer.start();
    }

    @AfterClass
    public static void tearDown() throws ServiceException {
        MsbRegisterManager.unRegisterOverlayMsb();
        DriverRegisterManager.unRegisterDriver();
        EsrRegisterManager.unRegisterEsr();
        successMocoServer.stop();
        vpnGatewayMocoServer.stop();
    }

    @Test
    public void successTest() throws ServiceException {

        HttpRquestResponse httpCreateSiteObject = HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_SITE_TESTCASE);
        execTestCase(httpCreateSiteObject.getRequest(), new StatusChecker(httpCreateSiteObject.getResponse()));

        HttpRquestResponse httpCreateLocalCpeObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_LOCALCPE_TESTCASE);
        HttpRequest httpCreateLocalCpeRequest = httpCreateLocalCpeObject.getRequest();
        NbiLocalCpeModel localCpeModel = JsonUtil.fromJson(httpCreateLocalCpeRequest.getData(), NbiLocalCpeModel.class);
        localCpeModel.setControllerId(EsrRegisterManager.getControllerId());
        httpCreateLocalCpeRequest.setData(JsonUtil.toJson(localCpeModel));
        execTestCase(httpCreateLocalCpeRequest, new StatusAndBodyChecker(httpCreateLocalCpeObject.getResponse()));

        HttpRquestResponse httpQueryLocalCpeObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(QUERY_LOCALCPE_TESTCASE);
        execTestCase(httpQueryLocalCpeObject.getRequest(), new StatusChecker(httpQueryLocalCpeObject.getResponse()));

        HttpRquestResponse httpBatchQueryLocalCpeObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(BATCH_QUERY_LOCALCPE_TESTCASE);
        execTestCase(httpBatchQueryLocalCpeObject.getRequest(),
                new StatusChecker(httpBatchQueryLocalCpeObject.getResponse()));

        HttpRquestResponse httpDeleteLocalCpeObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_LOCALCPE_TESTCASE);
        execTestCase(httpDeleteLocalCpeObject.getRequest(), new StatusChecker(httpDeleteLocalCpeObject.getResponse()));

        HttpRquestResponse httpDeleteSiteObject = HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_SITE_TESTCASE);
        execTestCase(httpDeleteSiteObject.getRequest(), new StatusChecker(httpDeleteSiteObject.getResponse()));
    }

    @Test
    public void batchEmptyQueryTest() throws ServiceException {
        HttpRquestResponse httpBatchEmptyQueryObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(BATCH_EMPTY_QUERY_LOCALCPE_TESTCASE);
        execTestCase(httpBatchEmptyQueryObject.getRequest(),
                new StatusAndBodyChecker(httpBatchEmptyQueryObject.getResponse()));
    }

}
