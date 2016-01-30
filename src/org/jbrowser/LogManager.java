/**
 * <b>项目名：</b>JBrowser<br/>
 * <b>包名：</b>org.jbrowser<br/>
 * <b>文件名：</b>LogManager.java<br/>
 * <b>版本信息：</b> @version 1.0.0<br/>
 * <b>日期：</b>2016-1-29-下午2:57:15<br/>

 *
 */

package org.jbrowser;

/**
 * <b>类名称：</b>LogManager<br/>
 * <b>类描述：</b><pre>

 </pre><br/>

 * <b>邮箱：</b>s_jqzhang@163.com<br/>

 */

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class LogManager {

	static {

		InputStream inputStream = Util.getResources2("log.properties");

		java.util.logging.LogManager logManager = java.util.logging.LogManager.getLogManager();
		try {

			logManager.readConfiguration(inputStream);

		} catch (SecurityException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}


	public static Logger getLogger(Class clazz) {
		Logger logger = Logger.getLogger(clazz.getName());
		return logger;
	}

}