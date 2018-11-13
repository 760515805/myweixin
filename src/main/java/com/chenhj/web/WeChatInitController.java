package com.chenhj.web;



import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chenhj.service.IWeChatMpService;
import com.chenhj.utils.WeChatUtil;

@RestController
@RequestMapping("/wechat/portal")
public class WeChatInitController {
		@Autowired
	    private IWeChatMpService weChatService;
		 private final Logger logger = LoggerFactory.getLogger(this.getClass());

	    /**
	     * 处理微信服务器发来的get请求，进行签名的验证
	     * 
	     * signature 微信端发来的签名
	     * timestamp 微信端发来的时间戳
	     * nonce     微信端发来的随机字符串
	     * echostr   微信端发来的验证字符串
	     */
	    @GetMapping(produces = "text/plain;charset=utf-8")
	    public String validate(
	    	      @RequestParam(name = "signature",
	              required = false) String signature,
	          @RequestParam(name = "timestamp",
	              required = false) String timestamp,
	          @RequestParam(name = "nonce", required = false) String nonce,
	          @RequestParam(name = "echostr", required = false) String echostr) {

	        logger.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature,
	            timestamp, nonce, echostr);

	        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
	          throw new IllegalArgumentException("请求参数非法，请核实!");
	        }

	        if (weChatService.checkSignature(timestamp, nonce, signature)) {
	          return echostr;
	        }

	        return "非法请求";

	    }
	    /**
	     * 此处是处理微信服务器的消息转发的
	     * @throws Exception 
	     */
	    @PostMapping(produces = "application/xml; charset=UTF-8")
	    public String processMsg(HttpServletRequest request) throws Exception {
            // 调用parseXml方法解析请求消息
            Map<String,String> requestMap = WeChatUtil.parseXml(request);
	        // 调用核心服务类接收处理请求
	        return weChatService.processRequest(requestMap);
	    }
	}
