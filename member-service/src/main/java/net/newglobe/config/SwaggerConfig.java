package net.newglobe.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Ordering;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiListingReference;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		ParameterBuilder tokenPar = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<Parameter>();
		Parameter parameter = tokenPar.name("token").description("令牌").defaultValue("test")
				.modelRef(new ModelRef("string")).parameterType("header").required(true).build();

		pars.add(parameter);

		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(io.swagger.annotations.Api.class)).build()
				.globalOperationParameters(pars).apiInfo(apiInfo());
		
		
//                .operationOrdering(new Ordering<Operation>(){
//					@Override
//					public int compare(Operation left, Operation right) {
//						System.err.println(left.getNotes()+"********************"+right.getTags());//it works not
//	                    System.out.println(left.getPosition()+" ----------------------- "+right.getPosition());//it works not
//	                    return left.getPosition()>=right.getPosition()?-1:1;
//					}
//                })
//                .apiListingReferenceOrdering(new Ordering<ApiListingReference>() {//排序
//                    @Override
//                    public int compare(ApiListingReference left, ApiListingReference right) {
//                    	String descriptionLeft = left.getDescription();//这里主要是为了解决排序的bug
//                    	String descriptionRight = right.getDescription();//这里主要是为了解决排序的bug
//                    	if(StringUtils.isEmpty(descriptionLeft) || StringUtils.isEmpty(descriptionRight)){
//                    		return 0;
//                    	}else{
//                        	int leftPos = Integer.parseInt(descriptionLeft);
//                            int rightPos = Integer.parseInt(descriptionRight);
//                            return leftPos>=rightPos?-1:1;
//                    	}
//                    }
//                });
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("对外开放接口API 文档").description("HTTP对外开放接口").version("1.0.0")
				.termsOfServiceUrl("http://xxx.xxx.com").license("").licenseUrl("http://xxx.xxx.com").build();
	}

}
