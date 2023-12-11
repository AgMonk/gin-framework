package com.gin.aliyun.service;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gin.aliyun.config.AliyunProfile;
import com.gin.aliyun.dto.AliyunSmsResponse;
import com.gin.jackson.utils.JacksonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 阿里云短信服务
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/12/11 12:15
 **/
@Service
@RequiredArgsConstructor
public class AliyunSmsService {
    private final AliyunProfile aliyunProfile;

    /**
     * 发送短信
     *
     * @param phoneNumber   手机号
     * @param signName      签名
     * @param templateCode  模板CODE
     * @param templateParam 模板参数对象
     * @return 响应
     * @throws JsonProcessingException
     * @throws ClientException
     */
    public AliyunSmsResponse send(String phoneNumber, String signName, String templateCode, Object templateParam) throws JsonProcessingException, ClientException {
        return send(phoneNumber, signName, templateCode, JacksonUtils.MAPPER.writeValueAsString(templateParam));
    }

    /**
     * 发送短信
     *
     * @param phoneNumber   手机号
     * @param signName      签名
     * @param templateCode  模板CODE
     * @param templateParam 模板参数(json格式字符串)
     * @return 响应
     * @throws JsonProcessingException
     * @throws ClientException
     */
    public AliyunSmsResponse send(String phoneNumber, String signName, String templateCode, String templateParam) throws JsonProcessingException, ClientException {
        CommonRequest request = getRequest();
        request.putQueryParameter("PhoneNumbers", phoneNumber);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        if (!ObjectUtils.isEmpty(templateParam)) {
            request.putQueryParameter("TemplateParam", templateParam);
        }
        final String response = aliyunProfile.obtainClient().getCommonResponse(request).getData();
        return JacksonUtils.MAPPER.readValue(response, AliyunSmsResponse.class);
    }


    /**
     * 生成请求
     *
     * @return CommonRequest
     */
    private CommonRequest getRequest() {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", aliyunProfile.getRegionId());
        return request;
    }
}
