package com.tianque.wechat.sync.controller;

import com.tianque.wechat.sync.util.AesException;
import com.tianque.wechat.sync.util.WXBizMsgCrypt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wuzhiwei
 * @date: 2021/5/19 13:41
 * @description:
 */
@RequestMapping("/weChatSync")
@RestController
public class WeChatSync {

    /***
     * 微信回调函数
     * @param msg_signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     * @throws AesException
     */
    @RequestMapping(value = "/syncPoints")
    public String messageSend(String msg_signature, String timestamp, String nonce, String echostr) throws AesException {
        String sToken = "loNSttvI1aWowl99u5scy4oU";
        String sCorpID = "wwba3b94c5c0bb7ccf";
        String sEncodingAESKey = "mwRD8LRcEE4DcimysrBDMs2d1d3ydnJhzVnRAICdRDO";

        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);

        // 解析出url上的参数值如下：
        // String sVerifyMsgSig = HttpUtils.ParseUrl("msg_signature");
        String sVerifyMsgSig = "5c45ff5e21c57e6ad56bac8758b79b1d9ac89fd3";
        // String sVerifyTimeStamp = HttpUtils.ParseUrl("timestamp");
        String sVerifyTimeStamp = "1409659589";
        // String sVerifyNonce = HttpUtils.ParseUrl("nonce");
        String sVerifyNonce = "263014780";
        // String sVerifyEchoStr = HttpUtils.ParseUrl("echostr");
        String sVerifyEchoStr = "P9nAzCzyDtyTWESHep1vC5X9xho/qYX3Zpb4yKa9SKld1DsH3Iyt3tP3zNdtp+4RPcs8TgAE7OaBO+FZXvnaqQ==";
        String sEchoStr; //需要返回的明文
        try {
            sEchoStr = wxcpt.VerifyURL(msg_signature, timestamp,
                    nonce, echostr);
            System.out.println("verifyurl echostr: " + sEchoStr);
//             验证URL成功，将sEchoStr返回
            return sEchoStr;
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            e.printStackTrace();
        }
        return "";
    }
}
