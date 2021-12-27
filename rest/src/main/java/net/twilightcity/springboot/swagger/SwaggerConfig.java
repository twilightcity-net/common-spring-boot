package net.twilightcity.springboot.swagger;

//@Configuration
//@EnableWebMvc
//@EnableAsync
//@EnableSwagger2
public class SwaggerConfig {//extends WebMvcConfigurerAdapter {

//    @Value("${swagger.resource.package}")
//    private String resourcePackage;
//
//    @Bean
//    Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage(resourcePackage))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    @Override
//    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }
//
//    @Override
//    public void addViewControllers(final ViewControllerRegistry registry) {
//        super.addViewControllers(registry);
//        registry.addViewController("/csrfAttacker.html");
//    }

}
