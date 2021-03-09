package org.cf.servicebroker.config;


import org.cf.servicebroker.interceptor.BrokerApiVersionInterceptor;
import org.cf.servicebroker.model.BrokerApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
public class BrokerApiVersionConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private BrokerApiVersion brokerApiVersion;

    public BrokerApiVersionConfig() {
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BrokerApiVersionInterceptor(this.brokerApiVersion)).addPathPatterns(new String[]{"/v2/**"});
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("locale");
        registry.addInterceptor(localeChangeInterceptor);
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(new String[]{"/resources/**"}).addResourceLocations(new String[]{"/resources/"});
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
}
