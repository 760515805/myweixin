package com.montnets.my_weixin;


import java.util.UUID;

/**  
 * @Title:  Demo.java   
 * @Description:  TODO(用一句话描述该文件做什么)   
 * @author: chenhongjie     
 * @date:   2018年4月19日 上午11:59:07   
 * @version V1.0 
 */
public class Demo {
	public static void main(String[] args) {
		  String uuid = UUID.randomUUID().toString(); //获取UUID并转化为String对象  
	        uuid = uuid.replace("-", "");               //因为UUID本身为32位只是生成时多了“-”，所以将它们去点就可  
	        System.out.println(uuid);  
	}
	
}
