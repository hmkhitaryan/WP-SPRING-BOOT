package com.egs.account.config;

import com.egs.account.utils.domainUtils.DomainUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.AbstractLocaleContextResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class GeneralConfig {
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver getMultipartResolver() {
        final CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxUploadSize(10240000);

        return resolver;
    }

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

    @Bean(name = "domainUtils")
    public DomainUtils getDomainUtils() {
        return new DomainUtils();
    }

    @Bean
    public LocaleChangeInterceptor localeInterceptor() {
        final LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("language");

        return interceptor;
    }
}

