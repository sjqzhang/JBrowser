/**
 * <b>项目名：</b>JBrowser<br/>
 * <b>包名：</b>org.jbrowser<br/>
 * <b>文件名：</b>Util.java<br/>
 * <b>版本信息：</b> @version 1.0.0<br/>
 * <b>日期：</b>2016-1-28-下午1:22:59<br/>
 * <b>Copyright (c)</b> 2016魅族公司-版权所有<br/>
 *
 */

package org.jbrowser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * <b>类名称：</b>Util<br/>
 * <b>类描述：</b>
 * 
 * <pre>
 </pre>
 * 
 * <br/>
 * <b>创建人：</b>张军强<br/>
 * <b>邮箱：</b>s_jqzhang@163.com<br/>
 * <b>修改时间：</b>2016-1-28 下午1:22:59<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 */

public class Util {

	static WebClient gwebClient = getClient();
	
//	static DefaultHttpClient  httpClient = new DefaultHttpClient();
	
	static  CookieStore cookieStore =new BasicCookieStore();;


	static {
		gwebClient.getOptions().setCssEnabled(false);

		gwebClient.getOptions().setJavaScriptEnabled(true);

		gwebClient.setAjaxController(new NicelyResynchronizingAjaxController());

		gwebClient.getCookieManager().setCookiesEnabled(true);
		
		
	}
	

	
	public  static String getFile(InputStream input) throws IOException {

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int len = 0;
		byte[] b = new byte[1024];
		while ((len = input.read(b)) > 0) {
			os.write(b, 0, len);
		}
		return os.toString("utf-8");

	}
	public static String getResources(String name){
		
		String basepath="/org/jbrowser/resources/";
		
	  try {
		return getFile(Main.class.getResourceAsStream(basepath + name));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println(e);
		e.printStackTrace();
		return "";
	}
	  
	}
	public static InputStream getResources2(String name) {
		String basepath="/org/jbrowser/resources/";
		  return (Main.class.getResourceAsStream(basepath + name));

	}
	

	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		} else if (obj.toString().trim().equalsIgnoreCase("")) {
			return true;
		} else {
			return false;
		}
	}

	private static String load(WebClient webClient, String url, String jscode, int timeout) {

		try {


			webClient.setAjaxController(new NicelyResynchronizingAjaxController());

			HtmlPage page = webClient.getPage(url);
			
			webClient.waitForBackgroundJavaScript(timeout);
			
			
			

			page.executeJavaScript(getResources("jquery2.2.js"));

			page.executeJavaScript(getResources("util.js"));

			// webClient.waitForBackgroundJavaScript(10000);
			Object obj = null;

			if (!isEmpty(jscode)) {
				

				obj = page.executeJavaScript(jscode).getJavaScriptResult();
				

			} else {

				obj = page.asXml();
			}

			return obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "error:"+e.toString();

		}

	}
	
	
	public static String getHtml(String url){
		
		String result="";
		
		
		try {
		
		WebClient client= new WebClient(BrowserVersion.FIREFOX_38);
		
		client.getOptions().setJavaScriptEnabled(false);
		client.getOptions().setCssEnabled(false);
		
		HtmlPage html= client.getPage(url);
		
		result=html.asXml();

		}catch (Exception e) {
			e.printStackTrace();
			return "error"+ e.toString();
			
		}
		return result;
	}
	
	
	public static String trimScript(String html) {
		
		
		html= html.replaceAll("<script[\\s\\S]+?>[\\s\\S]+?<\\/script>", "");

		html= html.replaceAll("<style[\\s\\S]+?>[\\s\\S]+?<\\/style>", "");
		
		return html;
		
		
	}
	
	
	public static String loadPageSimple(String url,String jscode,int timeout){
		
		try {
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
		
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		

		File file=File.createTempFile("JBrowser", ".html");
		
		FileOutputStream writeFile = new FileOutputStream(file); 

		String html =""+getHtml(url);
		
		html=trimScript(html);
		
		html= html+ "<script> var __www_url='"+url+"'; </script>";

		writeFile.write(html.getBytes());
		
		writeFile.flush();
		
		URL fileurl=new URL("file:///"+file.getPath());
		
		HtmlPage page= webClient.getPage(fileurl);
		
		page.executeJavaScript(getResources("jquery2.2.js"));

		page.executeJavaScript(getResources("util.js"));
		
		Object obj = null;

		if (!isEmpty(jscode)) {
			

			obj = page.executeJavaScript(jscode).getJavaScriptResult();
			

		} else {

			obj = page.asXml();
		}

		return obj.toString();
		
		}catch (Exception e) {
			// TODO: handle exception
			
			return "error:"+e.toString();

		}
		
		

		
	}

	


	public static String loadPageWithGlobalClient(String url, String jscode, int timeout) {
		String result = load(gwebClient, url, jscode, timeout);
		return result;
	}
	
	public static WebClient getClient(){
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		return webClient;

	}

	public static String loadPage(String url, String jscode, int timeout) {
		
		WebClient webClient= getClient();
		webClient.setCookieManager(gwebClient.getCookieManager());
		String result = load(webClient, url, jscode, timeout);
		webClient.close();
		return result;
	}
	
	
	public static String doPoast2(String url,String data){
		
		Map<String, String> map=new HashMap<String, String>();
		map.put("data", data);
		return doPost(url, map, "utf-8");
	}

	public static String doPost(String url, Map<String, String> map,String charset) {
		DefaultHttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
//		String charset="utf-8";
		try {
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost(url);
			
			
			
			// 设置参数
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			Iterator iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> elem = (Entry<String, String>) iterator.next();
				list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
			}
			if (list.size() > 0) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
				httpPost.setEntity(entity);
			}
			
			httpClient.setCookieStore(cookieStore);
			
			HttpResponse response = httpClient.execute(httpPost);
			
			cookieStore=httpClient.getCookieStore();
			
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	
	public static String doGet(String url,Map<String, String> map,String charset) {
		DefaultHttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
//		String charset="utf-8";
		try {
			httpClient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);	
			httpget.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
			
			
			
			
			List params=new ArrayList<NameValuePair>();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			Iterator iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> elem = (Entry<String, String>) iterator.next();
				params.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
			}
			String str = EntityUtils.toString(new UrlEncodedFormEntity(params,"UTF-8"));
			if (params.size()==0) {
				str = "";
			}
			if (httpget.getURI().toString().indexOf("?") != -1) {
				httpget.setURI(new URI(httpget.getURI().toString() + "&" + str));
			} else {
				httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));
			}
			

			
			
			httpClient.setCookieStore(cookieStore);
			
			HttpResponse response = httpClient.execute(httpget);
			
			cookieStore=httpClient.getCookieStore();
			
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
			
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

}
