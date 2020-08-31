package uk.co.danielbryant.djshopping.shopfront.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import uk.co.danielbryant.djshopping.shopfront.handler.AuthInterceptor;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        String[] pathPatterns = {
                "/**"};
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns(pathPatterns);
    }


}
