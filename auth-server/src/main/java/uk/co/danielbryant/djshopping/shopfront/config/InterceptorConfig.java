package uk.co.danielbryant.djshopping.shopfront.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import uk.co.danielbryant.djshopping.shopfront.handler.AuthInterceptor;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        String[] pathPatterns = {
                "/**"};
        registry.addInterceptor(new AuthInterceptor(stringRedisTemplate)).addPathPatterns(pathPatterns);
    }

}
