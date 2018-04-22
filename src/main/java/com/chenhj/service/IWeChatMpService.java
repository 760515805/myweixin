package com.chenhj.service;

import java.util.Map;

/**
 * 微信API的Service
 */
public interface IWeChatMpService {
 

  /**
   * <pre>
   * 验证消息的确来自微信服务器
   * 详情请见: http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421135319&token=&lang=zh_CN
   * </pre>
   */
  boolean checkSignature(String timestamp, String nonce, String signature);
  
  public String processRequest(Map<String,String> requestMap);

}

