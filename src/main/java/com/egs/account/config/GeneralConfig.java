package com.egs.account.config;

import com.egs.account.config.security.SessionExpiredStrategy;
import com.egs.account.utils.domainUtils.DomainUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.AbstractLocaleContextResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class GeneralConfig extends WebMvcConfigurerAdapter {

    @Bean(name = "messageSource")
    public MessageSource getMessageSource() {
        final ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
        resource.setBasenames("classpath:validation", "classpath:messages");
        resource.setDefaultEncoding("UTF-8");

        return resource;
    }

    @Bean(name = "localeResolver")
    public AbstractLocaleContextResolver getLocaleResolver() {
        final AbstractLocaleContextResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);

        return resolver;
    }

    @Bean(name = "utilsService")
    public DomainUtils getDomainUtils() {
        return new DomainUtils();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        final LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("language");

        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String externalFilePath = "file:///C:/Users/haykmk/hmkhitaryan/dev/proj/temp/";  //C:\Users\haykmk\hmkhitaryan\dev\proj\temp
        registry.addResourceHandler("/temp/**").addResourceLocations(externalFilePath);

        super.addResourceHandlers(registry);
    }

    @Bean(name = "sessionExpiredStrategy")
    public SessionExpiredStrategy getSessionExpiredStrategy() {
        return new SessionExpiredStrategy();
    }

}

