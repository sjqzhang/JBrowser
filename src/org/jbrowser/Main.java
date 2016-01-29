/**
 * <b>项目名：</b>JBrowser<br/>
 * <b>包名：</b>org.jbrowser<br/>
 * <b>文件名：</b>Main.java<br/>
 * <b>版本信息：</b> @version 1.0.0<br/>
 * <b>日期：</b>2016-1-28-上午11:58:52<br/>
 * <b>Copyright (c)</b> 2016魅族公司-版权所有<br/>
 *
 */

package org.jbrowser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.servlet.Context;

public class Main {

	public static class RedirectHandler extends AbstractHandler {
		
		private static String getFile(InputStream input) throws IOException {

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			int len = 0;
			byte[] b = new byte[1024];
			while ((len = input.read(b)) > 0) {
				os.write(b, 0, len);
			}
			return os.toString("utf-8");

		}
		
		private static String getResources(String name){
			
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


		public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {

			System.out.println(target);

			response.setContentType("text/html;charset=UTF-8");

			String content = "";

			if (target.startsWith("/api/")) {

				String type = request.getParameter("type");
				String posturl = request.getParameter("posturl");
				Integer timeout = 1000;
				try {
					timeout = Integer.valueOf(request.getParameter("timeout"));
				} catch (Exception e) {

				}
				if (type == null || type.equalsIgnoreCase("0")) {
					content = Util.loadPage(request.getParameter("url"), request.getParameter("jscode"), timeout); 
				} else {
					content = Util.loadPage2(request.getParameter("url"), request.getParameter("jscode"), timeout);
				}
				
				
				if(!Util.isEmpty(posturl)) {
					
//					System.out.println( Util.doPoast2(posturl, content));
				}
				
			} else if (target.endsWith(".html")) {
//				content = Util.getResources("index.html");
				content = getResources("index.html");
			} else if (target.endsWith(".js")) {
				response.setContentType("text/javascript;charset=UTF-8");
//				content = Util.getResources("jquery.js");
				content = getResources("jquery.js");
			} else {
//				content = Util.getResources("index.html");
				content = getResources("index.html");
			}
			response.getWriter().write(content);

			response.getWriter().flush();
			response.setStatus(HttpServletResponse.SC_OK);
			((Request) request).setHandled(true);

		}
	}
	
	public static void help(){
				
		StringBuilder builder = new StringBuilder();
		builder.append(" -port 6666 \r\n");
		System.out.println(builder.toString());
	}

	public static void main(String[] args) throws Exception {
		
		
	
		int port = 6666;



		int len = args.length;
		int pos = 0;
		while (pos < len) {
			if (args[pos].equals("-port")) {
				port = Integer.valueOf(args[++pos]);
			} else if (args[pos].equals("-h") || args[pos].equals("-help")) {
				help();
				System.exit(0);
			} else {
				++pos;
			}
		}


				
		Server server = new Server(port);

		ResourceHandler fileHandler = new ResourceHandler();

		Context context = new Context(server, "/", Context.SESSIONS);

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { new RedirectHandler(), context, new DefaultHandler() });
		server.setHandler(handlers);

		server.start();
		server.join();

	}
}
