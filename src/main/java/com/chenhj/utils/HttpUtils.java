package com.chenhj.utils;




import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;


public class HttpUtils {

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 * @throws Exception 
	 */
	public static String sendGet(String url, String param) throws Exception {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;
			if (StringUtils.isNotEmpty(param)) {
				url = url + "?" + param;
			}
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			// connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("Content-type", "application/json;charset=UTF-8");

			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			// for (String key : map.keySet()) {
			// logger.info(key + "--->" + map.get(key));
			// System.out.println(key + "--->" + map.get(key));
			// }
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			throw e;
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				throw e2;
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 * @throws Exception 
	 */
	public static String sendPost(String url, String param, boolean gzip) throws Exception {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			// conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("accept",
					"text/html,application/xhtml+xml,application/xml,application/json;q=0.9,image/webp,*/*;q=0.8");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			// 发送POST请求必须设置如下两行
			conn.setRequestProperty("Content-type", "application/json;charset=UTF-8");
			conn.setRequestProperty("Accept- Encoding", "gzip");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			OutputStream output = conn.getOutputStream();
			
			if (gzip) {
				byte[] reqData = GZipUtils.compress(param.getBytes("utf-8"));
				conn.setRequestProperty("Content-Encoding", "gzip");
				output.write(reqData);
			} else {
				out = new PrintWriter(output);
				// 发送请求参数
				if (StringUtils.isNotEmpty(param)) {
					out.print(param);
				}
			}
			// flush输出流的缓冲
			out.flush();

			InputStream respIn = conn.getInputStream();
			String resqEncoding = conn.getHeaderField("Content-Encoding");
			if (resqEncoding != null && resqEncoding.equalsIgnoreCase("gzip")) {
				ByteArrayOutputStream outer = new ByteArrayOutputStream();
				GZipUtils.decompress(respIn, outer);
				if (outer != null) {
					result = outer.toString();
					outer.close();
				}
			} else {
				// 定义BufferedReader输入流来读取URL的响应
				in = new BufferedReader(new InputStreamReader(respIn,"utf-8"));

				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
			}

		} catch (Exception e) {
			throw e;
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static InputStream sendPostByte(String url, byte[] PostData) {
		URL u = null;
		HttpURLConnection con = null;
		InputStream inputStream = null;
		// 尝试发送请求
		try {
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type", "application/octet-stream");
			OutputStream outStream = con.getOutputStream();
			outStream.write(PostData);
			outStream.flush();
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}

		// 读取返回内容
		try {
			// 读取返回内容
			inputStream = con.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputStream;
	}

	// 替换URL中的值
	public static String replaceUrlParamReg(String url, String key, String val) {
		if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(val)) {
			url = url.replaceAll("(" + key + "=[^&]*)", key + "=" + val);
		}
		return url;
	}

	// 从URL中获取某个键值
	public static String getParamsFromUrl(String url, String key) {
		String regex = "(\\b" + key + "\\b)\\s*=\\s*(\\w+)";
		String val = getUrlValue(url, regex, 2);
		return val;
	}

	// 判断URL中某个key是否存在
	public static boolean urlContains(String url, String key) {
		String regex = "(\\b" + key + "\\b)\\s*=\\s*(\\w+)";
		String val = getUrlValue(url, regex, 1);
		return StringUtils.isNotBlank(val);
	}

	// 向URL中新增一个key=value，如果存在则更新value
	public static String addOrUpdUrlKeyVal(String url, String key, String val) {
		if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(key)) {
			if (urlContains(url, key)) {
				url = HttpUtils.replaceUrlParamReg(url, key, val);
			} else {
				url = url.indexOf("?") > 0 ? url + "&" : url + "?";
				url = url + key + "=" + val;
			}
		}
		return url;
	}

	public static String getUrlValue(String info, String reg, int gid) {
		String value = null;
		try {
			Pattern pat = Pattern.compile(reg);
			Matcher mc = pat.matcher(info);
			if (!mc.find())
				return null;
			int cn = mc.groupCount();
			if (cn >= gid) {
				value = mc.group(gid);
			}
		} catch (Exception e) {
		}

		return value;
	}

	public static String getRequestInputString(HttpServletRequest request) {
		try {
			String inData = null;
			int contentLength = request.getContentLength();
			if (contentLength > 0) {

				InputStream is = request.getInputStream();
				// inData = new byte[contentLength];
				// is.read(inData);
				// is.close();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] bys = new byte[4096];
				for (int p = -1; (p = is.read(bys)) != -1;) {
					out.write(bys, 0, p);
				}

				inData = out.toString();
				is.close();
				out.close();
				return inData;
			}
		} catch (IOException e) {
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		String url = "http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&lang=zh_cn";
		url = "http://api.openweathermap.org/data/2.5/forecast/daily?id=524901&lang=zh_cn";
		// String result = HttpUtils.sendGet(url, "");
		// System.out.println(result);
		 url = "http://127.0.0.1:8021/geoip/city?appId=abc&sign=8B1B5BAF9DC0B64F640E07EF4B50616D";
		//url = "http://192.168.1.2:809/geoip/city?appId=abc&sign=8B1B5BAF9DC0B64F640E07EF4B50616D";
		String json = "{\"ip\":\"64.23.53.11\"}";
		String ret = HttpUtils.sendPost(url, json,false);
		System.out.println(ret);
	}
}
