package com.ew.udm.configs;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfigure extends WebMvcConfigurerAdapter {

    private final CorsConfigs corsConfigs;

    @Autowired
    public WebMvcConfigure(CorsConfigs corsConfigs) {
        this.corsConfigs = corsConfigs;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setCharset(Charset.forName("utf-8"));
        config.setSerializerFeatures(
                // SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteNullListAsEmpty);
        converter.setFastJsonConfig(config);
        SerializeConfig.getGlobalInstance().put(Long.class, new LongValueSerializer());
        SerializeConfig.getGlobalInstance().put(long.class, new LongValueSerializer());
        converters.add(responseBodyConverter());
        converters.add(converter);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = new String[corsConfigs.getAllowOrigins().size()];
        corsConfigs.getAllowOrigins().toArray(origins);
        registry.addMapping("/**")
                .allowedOrigins(origins)
                .allowedMethods("GET", "POST", "OPTIONS", "DELETE")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }
}
