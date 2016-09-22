[![Build Status](https://travis-ci.org/juliuskrah/spring-security-multipart.svg?branch=master)](https://travis-ci.org/juliuskrah/spring-security-multipart)

# Spring-security with Multipart uploads
This is a simple project that demonstrates how to add Multipart uploads to a Spring-security project.  
The headache with Multipart uploads in a Spring-security project using Java only configuration is what motivated me to create this template project.  
**Note**: When you are using spring-boot, this problem does not exist.  
In most stacks however, switching to spring-boot is not an option and this template is designed for them.

## Configuration
In configuring multipart into a Spring-security application, you need to consider three things:  
- Your multipart configuration bean needs to be in the *ROOT* configuration class:
```java
@Configuration
@ComponentScan
public class RootConfig {

  	...

	@Bean
	public MultipartResolver filterMultipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("UTF-8");
		resolver.setMaxUploadSize(100000L); //Max size in bytes

		return resolver;
	}

}
```
You register this configuration class with the WebApplicationContext:
```java
public class ServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { RootConfig.class };
	}

	...

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}
```

- Spring-security will only load the multipart configuration if the bean name is `filterMultipartResolver`. Any other bean name will be ignored:
```java
@Bean(name = "filterMultipartResolver")
public MultipartResolver filterMultipartResolver() {
  // multipart implementation
}
```

- From point two above, it is indicative that a `Filter` must be registered for multipart. In Spring-security this filter must be registered before the *Security Filter Chain*:
```java
public class SecurityIntializer extends AbstractSecurityWebApplicationInitializer {

	@Override
	protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
		insertFilters(servletContext, new MultipartFilter());
	}
}
```

## Run the application
`mvn exec:java` visit <http://localhost:8080>  

With this three things in mind, you can save hours on debuging.



Happy coding :smile:
