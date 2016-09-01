package com.juliuskrah.multipart;

import java.util.Optional;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static final Optional<String> port = Optional.ofNullable(System.getenv("PORT"));

	public static void main(String[] args) throws LifecycleException, ServletException {
		String contextPath = "";
		String appBase = ".";
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(Integer.valueOf(port.orElse("8080")));
		tomcat.getHost().setAppBase(appBase);
		tomcat.addWebapp(contextPath, appBase);
		log.info("Starting embedded tomcat..");
		tomcat.start();
		
		tomcat.getServer().await();
	}

}
