package org.cf.broker.config;

import org.cf.broker.config.security.CustomSecurityConfiguration;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.EnumSet;

public class CustomWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    public CustomWebInitializer() {
    }

    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{CustomSecurityConfiguration.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[0];
    }


    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    protected Filter[] getServletFilters() {
        return new Filter[]{new MultipartFilter()};
    }

    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setInitParameter("dispatchOptionsRequest", "true");
        registration.setAsyncSupported(true);
    }

    public void onStartup(ServletContext servletContext) throws ServletException {
        javax.servlet.FilterRegistration.Dynamic corsFilter = servletContext.addFilter("corsFilter", CORSFilter.class);
        corsFilter.addMappingForUrlPatterns((EnumSet)null, false, new String[]{"/service_brokers"});
        super.onStartup(servletContext);
    }
}
