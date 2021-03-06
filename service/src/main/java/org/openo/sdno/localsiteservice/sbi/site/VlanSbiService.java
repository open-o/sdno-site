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

package org.openo.sdno.localsiteservice.sbi.site;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.localsiteservice.util.RestfulParameterUtil;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.model.v2.vlan.SbiIfVlan;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Sbi Service of Vlan.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-24
 */
@Service
public class VlanSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VlanSbiService.class);

    private static final String VLAN_BASE_URL = "/openoapi/sbi-localsite/v1/device/{0}/vlan";

    private static final String VLAN_QUERY_URL = "/openoapi/sbi-localsite/v1/device/{0}/vlan/{1}";

    /**
     * Query Interface Vlan.<br>
     * 
     * @param ctrlUuid Controller Uuid
     * @param deviceId Device Id
     * @param portId Port Id
     * @return IfVlan List queried
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public ResultRsp<List<SbiIfVlan>> query(String ctrlUuid, String deviceId, String portId) throws ServiceException {
        if(StringUtils.isBlank(ctrlUuid) || StringUtils.isBlank(deviceId)) {
            LOGGER.error("Controller or device parameter is invalid");
            return new ResultRsp<>(ErrorCode.OVERLAYVPN_FAILED);
        }

        if(StringUtils.isBlank(portId)) {
            LOGGER.error("portId is invalid");
            return new ResultRsp<>(ErrorCode.OVERLAYVPN_FAILED);
        }

        String queryUrl = MessageFormat.format(VLAN_QUERY_URL, deviceId, portId);

        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        RestfulParameterUtil.setControllerUuid(restfulParameters, ctrlUuid);
        RestfulResponse response = RestfulProxy.get(queryUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Query IfVlan from driver failed");
            return new ResultRsp<>(ErrorCode.OVERLAYVPN_FAILED);
        }

        return JsonUtil.fromJson(response.getResponseContent(), new TypeReference<ResultRsp<List<SbiIfVlan>>>() {});
    }

    /**
     * Create Interface Vlan.<br>
     * 
     * @param ctrlUuid Controller Uuid
     * @param deviceId Device Id
     * @param ifVlanList List of IfVlan need to create
     * @return List of IfVlan created
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    public ResultRsp<List<SbiIfVlan>> create(String ctrlUuid, String deviceId, List<SbiIfVlan> ifVlanList)
            throws ServiceException {

        if(StringUtils.isBlank(ctrlUuid) || StringUtils.isBlank(deviceId)) {
            LOGGER.error("Controller Uuid or Device Uuid is invalid");
            return new ResultRsp<>(ErrorCode.OVERLAYVPN_FAILED);
        }

        String createUrl = MessageFormat.format(VLAN_BASE_URL, deviceId);

        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        RestfulParameterUtil.setControllerUuid(restfulParameters, ctrlUuid);
        restfulParameters.setRawData(JsonUtil.toJson(ifVlanList));
        RestfulResponse response = RestfulProxy.post(createUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Create IfVlan in driver failed");
            return new ResultRsp<>(ErrorCode.OVERLAYVPN_FAILED);
        }

        return JsonUtil.fromJson(response.getResponseContent(), new TypeReference<ResultRsp<List<SbiIfVlan>>>() {});
    }

    /**
     * Update Interface Vlan.<br>
     * 
     * @param ctrlUuid Controller Uuid
     * @param deviceId Device Id
     * @param ifVlanList List of IfVlan need to update
     * @return List of IfVlan created
     * @throws ServiceException when update failed
     * @since SDNO 0.5
     */
    public ResultRsp<List<SbiIfVlan>> update(String ctrlUuid, String deviceId, List<SbiIfVlan> ifVlanList)
            throws ServiceException {

        if(StringUtils.isBlank(ctrlUuid) || StringUtils.isBlank(deviceId)) {
            LOGGER.error("Controller Uuid or Device Uuid is invalid");
            return new ResultRsp<>(ErrorCode.OVERLAYVPN_FAILED);
        }

        String updateUrl = MessageFormat.format(VLAN_BASE_URL, deviceId);

        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        RestfulParameterUtil.setControllerUuid(restfulParameters, ctrlUuid);
        restfulParameters.setRawData(JsonUtil.toJson(ifVlanList));
        RestfulResponse response = RestfulProxy.put(updateUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Update IfVlan in driver failed");
            return new ResultRsp<>(ErrorCode.OVERLAYVPN_FAILED);
        }

        return JsonUtil.fromJson(response.getResponseContent(), new TypeReference<ResultRsp<List<SbiIfVlan>>>() {});
    }

}
