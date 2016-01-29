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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.htmlunit.corejs.javascript.Function;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
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

	static WebClient gwebClient = new WebClient(BrowserVersion.FIREFOX_38);

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
	public static String getResources2(String name) {
		try {


		
			InputStream ins = new FileInputStream(name);
			byte[] b = new byte[ins.available()];
			ins.read(b);
			return new String(b);
		} catch (Exception e) {
			System.out.println(e);
			return "";
		}

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
			webClient.getOptions().setCssEnabled(false);

			webClient.getOptions().setJavaScriptEnabled(true);

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

			return e.toString();

		}

	}

	
		 
		 /**
		 * <b>创建人：</b>张军强<br/>
		 * Function<br/>
		 *<b>方法描述：</b> <br/>
		 * @return 
		 * Function
		 * @exception 
		 * @since  1.0.0
		 */
		
	private static Function Function() {
		// TODO Auto-generated method stub
		return null;
	}

	public static String loadPage2(String url, String jscode, int timeout) {
		String result = load(gwebClient, url, jscode, timeout);
		return result;
	}

	public static String loadPage(String url, String jscode, int timeout) {
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
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
		HttpClient httpClient = null;
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
			HttpResponse response = httpClient.execute(httpPost);
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
