package net.newglobe.config;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.newglobe.filter.AuthorizationInterceptor;

public class WebMvcConfig implements WebMvcConfigurer {

	/**
	 * 静态资源路径配置
	 * 
	 * @author Ray
	 * @date 2018/11/2 14:17
	 **/
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AuthorizationInterceptor())
								.addPathPatterns("/api/")
								.excludePathPatterns("/login")
								.excludePathPatterns("/api/login");
	}
	


}
