package com.example.as.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置文件
 * 通过http://localhost:你的端口号/swagger-ui.html 即可访问查看效果
 * 没有该类Swagger默认也是可以访问的
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    public static final String VERSION = "1.0.1";
    public static final String AUTHOR = "移动端架构师讲师团";

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //Controller的位置
                .apis(RequestHandlerSelectors.basePackage("com.example.as.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(ApiIgnore.class)
                .enableUrlTemplating(false)
                .tags(new Tag("Account", "账号模块"))
                .tags(new Tag("City", "城市模块"))
                .tags(new Tag("HiConfig", "配置中心模块"))
                .tags(new Tag("Category", "商品类别"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //设置文档标题
                .title("API文档")
                .description("移动端架构师成长体系课-API文档")
                .version(VERSION)
                .contact(new Contact(AUTHOR, "https://class.imooc.com/sale/mobilearchitect", "xx@gmail.com"))
                .build();
    }
}
