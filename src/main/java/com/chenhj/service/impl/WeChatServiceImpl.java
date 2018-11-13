package com.chenhj.service.impl;


/**  
 * @Title:  WeChatServiceImpl.java   
 * @Description:  TODO(用一句话描述该文件做什么)   
 * @author: chenhongjie     
 * @date:   2018年4月19日 下午8:00:16   
 * @version V1.0 
 */

import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chenhj.config.WeChatConfig;
import com.chenhj.constant.WeChatContant;
import com.chenhj.service.IWeChatMpService;
import com.chenhj.utils.SHA1;
import com.chenhj.utils.WeChatUtil;

/**
 * 核心服务类
 */
@Service
public class WeChatServiceImpl implements IWeChatMpService{
	  private final Logger logger = LoggerFactory.getLogger(this.getClass());

	  @Autowired
	  private WeChatConfig weChatConfig;
	@Override
    public String processRequest(Map<String,String> requestMap) {
        // xml格式的消息数据
        String respXml = null;
        // 默认返回的文本消息内容
        String respContent;
        try {

             // 消息类型
            String msgType = (String) requestMap.get(WeChatContant.MsgType);
            String mes = null;
            // 文本消息
            if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_TEXT)) {
                mes =requestMap.get(WeChatContant.Content).toString();
                if(mes.contains("help")||"help".equalsIgnoreCase(mes)||"帮助".equalsIgnoreCase(mes)||"?".equals(mes)||"？".equals(mes)){
                	respContent="您好，这里是个人订阅号开发测试，请回复数字选择服务：\n\n"
                			   +"1--查看技术前线[机智]\n"
                			  + "2--查看今日幽默[奸笑]\n"
                			  + "3--拿走本套代码\n"
                			  +	"回复\"?\"显示此帮助菜单\n\n";
                	 respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
                }
            }
            // 图片消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_IMAGE)) {
                respContent = "您发送的是图片消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 语音消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_VOICE)) {
                respContent = "您发送的是语音消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 视频消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_VIDEO)) {
                respContent = "您发送的是视频消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 地理位置消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_LOCATION)) {
                respContent = "您发送的是地理位置消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 链接消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_LINK)) {
                respContent = "您发送的是链接消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 事件推送
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = (String) requestMap.get(WeChatContant.Event);
                // 关注
                if (eventType.equals(WeChatContant.EVENT_TYPE_SUBSCRIBE)) {
                    respContent = "谢谢您的关注！";
                    respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
                }
                // 取消关注
                else if (eventType.equals(WeChatContant.EVENT_TYPE_UNSUBSCRIBE)) {
                    // TODO 取消订阅后用户不会再收到公众账号发送的消息，因此不需要回复
                }
                // 扫描带参数二维码
                else if (eventType.equals(WeChatContant.EVENT_TYPE_SCAN)) {
                    // TODO 处理扫描带参数二维码事件
                }
                // 上报地理位置
                else if (eventType.equals(WeChatContant.EVENT_TYPE_LOCATION)) {
                    // TODO 处理上报地理位置事件
                }
                // 自定义菜单
                else if (eventType.equals(WeChatContant.EVENT_TYPE_CLICK)) {
                    // TODO 处理菜单点击事件
                }
            }
            mes = mes == null ? "不知道你在干嘛" : mes;
            if(respXml == null)
                respXml = WeChatUtil.sendTextMsg(requestMap, mes);
            System.out.println(respXml);
            return respXml;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
        
    }

	  @Override
	  public boolean checkSignature(String timestamp, String nonce, String signature) {
	    try {
	      return SHA1.gen(weChatConfig.getToken(), timestamp, nonce)
	        .equals(signature);
	    } catch (Exception e) {
	       logger.error("Checking signature failed, and the reason is :" + e.getMessage());
	      return false;
	    }
	  }
}
