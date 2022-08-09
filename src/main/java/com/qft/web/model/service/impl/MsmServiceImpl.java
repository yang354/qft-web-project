package com.qft.web.model.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;


import com.qft.web.model.service.MsmService;
import com.qft.web.util.ShortMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Map;

@Transactional
@Service
public class MsmServiceImpl implements MsmService {

    @Autowired
    private ShortMessage shortMessage;

    /**
     * 发送短信
     */
    @Override
    public boolean send(Map<String, Object> param, String phone) {
        DefaultProfile profile =
                DefaultProfile.getProfile(
                        "default",
                        shortMessage.getAccessKeyId(),
                        shortMessage.getSecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);固定参数
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2022-08-10");
        request.setAction("SendSms");

        request.putQueryParameter("PhoneNumbers", phone);//设置手机号
        request.putQueryParameter("SignName", shortMessage.getSignName());//签名名
        request.putQueryParameter("TemplateCode", shortMessage.getTemplateCode());//模板编号
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));

        try {
            //最终发送
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
