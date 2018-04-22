package com.chenhj.bean;

/**  
 * @Title:  ArticleItem.java   
 * @Description:  TODO(用一句话描述该文件做什么)   
 * @author: chenhongjie     
 * @date:   2018年4月19日 下午7:59:20   
 * @version V1.0 
 */
public class ArticleItem {
	 private String Title;
	    private String Description;
	    private String PicUrl;
	    private String Url;
	    public String getTitle() {
	        return Title;
	    }
	    public void setTitle(String title) {
	        Title = title;
	    }
	    public String getDescription() {
	        return Description;
	    }
	    public void setDescription(String description) {
	        Description = description;
	    }
	    public String getPicUrl() {
	        return PicUrl;
	    }
	    public void setPicUrl(String picUrl) {
	        PicUrl = picUrl;
	    }
	    public String getUrl() {
	        return Url;
	    }
	    public void setUrl(String url) {
	        Url = url;
	    }
	    
}
