/*
* Copyright 2016, Julius Krah
* by the @authors tag. See the LICENCE in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.juliuskrah.multipart;

import javax.inject.Inject;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
public class RootConfig implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	@Inject
	private Environment env;

	private ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(applicationContext);
		resolver.setCacheable(env.getRequiredProperty("template.cacheable", Boolean.class));
		resolver.setPrefix(env.getRequiredProperty("template.prefix"));
		resolver.setSuffix(env.getRequiredProperty("template.suffix"));
		resolver.setTemplateMode(TemplateMode.HTML);
		return resolver;
	}

	@Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		resolver.setCharacterEncoding(env.getRequiredProperty("template.encoding"));
		return resolver;
	}

	@Bean
	public TemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.addDialect(new SpringSecurityDialect());
		engine.addDialect(new LayoutDialect());
		engine.setEnableSpringELCompiler(env.getRequiredProperty("template.el-compiler", Boolean.class));
		engine.setTemplateResolver(templateResolver());
		return engine;
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(10);
	}

	@Bean
	public MultipartResolver filterMultipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding(env.getRequiredProperty("multipart.default-encoding"));
		resolver.setMaxUploadSize(env.getRequiredProperty("multipart.max-upload-size", Long.class));

		return resolver;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
