package com.tianque.wechat.sync.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.XML;
import com.tianque.wechat.sync.constant.CommonConstant;
import com.tianque.wechat.sync.util.AesException;
import com.tianque.wechat.sync.util.WXBizMsgCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: wuzhiwei
 * @date: 2021/5/19 13:41
 * @description:
 */
@RequestMapping("/weChatSync")
@RestController
public class WeChatSync {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /***
     * 微信接口验证
     * @param msg_signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     * @throws AesException
     */
    @RequestMapping(value = "/syncPoints", method = {RequestMethod.GET})
    public String checkUrl(String msg_signature, String timestamp, String nonce, String echostr) throws AesException {
        String sToken = "loNSttvI1aWowl99u5scy4oU";
        String sCorpID = "wwba3b94c5c0bb7ccf";
        String sEncodingAESKey = "I5rjM4OCyRLYdhkiMolWLSQVmcxj8nskkAHPLbAmoPC";

        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
//        if (StrUtil.isEmpty(reqData)) {
        String sEchoStr; //需要返回的明文
        try {
            sEchoStr = wxcpt.VerifyURL(msg_signature, timestamp,
                    nonce, echostr);
            System.out.println("verifyurl echostr: " + sEchoStr);
//             验证URL成功，将sEchoStr返回
            return sEchoStr;
        } catch (Exception e) {
            logger.error("签名校验失败", e);
        }
//        } else {
//
//        }
        return "";
    }

    /***
     * 微信回调函数
     * @return
     * @throws AesException
     */
    @RequestMapping(value = "/syncPoints", method = RequestMethod.POST)
    public String syncPoints(String msg_signature, String timestamp, String nonce, @RequestBody String reqData) throws AesException {
        String sToken = "loNSttvI1aWowl99u5scy4oU";
        String sCorpID = "wwba3b94c5c0bb7ccf";
        String sEncodingAESKey = "I5rjM4OCyRLYdhkiMolWLSQVmcxj8nskkAHPLbAmoPC";
        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
        String xmlData = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, reqData);
        JSONObject xmlJSONObj = XML.toJSONObject(xmlData).getJSONObject("xml");
        if (CommonConstant.LOCATION_EVENT.equals(xmlJSONObj.get(CommonConstant.EVENT))) {
            Map<String, Object> param = new HashMap<>();
            param.put("userName", xmlJSONObj.get(CommonConstant.FROMUSERNAME));
            param.put("centerLon", xmlJSONObj.get(CommonConstant.LONGITUDE));
            param.put("centerLat", xmlJSONObj.get(CommonConstant.LATITUDE));
            param.put("originTime", xmlJSONObj.get(CommonConstant.CREATETIME));
            HttpUtil.get(CommonConstant.GIS_URL, param);
            logger.info("用户名：" + xmlJSONObj.get(CommonConstant.FROMUSERNAME) +
                    ",上传坐标lon:" + xmlJSONObj.get(CommonConstant.LONGITUDE) +
                    ",lat:" + xmlJSONObj.get(CommonConstant.LATITUDE));
        }
        return "";
    }
}
