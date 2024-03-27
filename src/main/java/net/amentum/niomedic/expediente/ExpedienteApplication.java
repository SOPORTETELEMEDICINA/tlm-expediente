package net.amentum.niomedic.expediente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableDiscoveryClient
public class ExpedienteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpedienteApplication.class, args);
	}

		@Bean
	public Docket docket(){
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("net.amentum.niomedic.expediente"))
				.paths(PathSelectors.any())
				.build();
	}

	public ApiInfo apiInfo(){
	return new ApiInfoBuilder()
			.title("Microservicio de expedientes")
			.description("Alta, Baja, Edicion de expedientes")
			.contact("Amentum IT Services")
			.licenseUrl("http://www.amentum.net")
			.build();
}
}
