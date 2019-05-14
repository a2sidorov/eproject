/*
 * MIT License
 *
 * Copyright (c) 2019 Andrei Sidorov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.a2.estore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.Properties;


/**
 * This class provides configuration for MVC.
 *
 * @author Andrei Sidorov
 */
@Configuration
@EnableWebMvc
@EnableScheduling
@EnableAsync
@ComponentScan({"dev.a2.estore"})
@PropertySource(value = {"classpath:application.properties"})
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Injects the maximum image size specified in the application properties.
     */
    @Value("${max.image.size}")
    private long maxImageSize;

    /**
     * Injects the email specified in the application properties.
     *
     */
    @Value("${app.email}")
    private String email;

    /**
     * Injects the email password specified in the application properties.
     *
     */
    @Value("${app.email.password}")
    private String emailPassword;

    /**
     * Injects the the path to the folder for product images.
     * Path can be modified in file 'application.properties'.
     */
    @Value("${files.upload.folder}")
    private String imageFolder;

    /**
     * This bean initialzies view resolver for .jsp files.
     *
     * @return the view resolver.
     */
    @Bean
    public InternalResourceViewResolver resolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    /**
     * This method adds resource handlers for serving static context in resource folder
     * and also product images in 'imageFolder'.
     *
     * @param registry - a resource handler registry
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/resources/**")
            registry.addResourceHandler("/resources/**")
                .addResourceLocations("classpath:/static/", "file:" + imageFolder);
    }

    /**
     * This bean provides messages stored in file 'messsage.properties'.
     *
     * @return  message source
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages");
        return source;
    }

    /**
     * This bean rpovides property source configurer.
     *
     * @return the property source place holder configurer.
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySource() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * Provides validator.
     *
     * @return the validator.
     */
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource());
        return validator;
    }

    /**
     * This bean provides resolver to handle multipart files uploading.
     *
     * @return the multipart resolver;
     */
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(maxImageSize);
        multipartResolver.setResolveLazily(true);
        return multipartResolver;
    }

    /**
     * This bean provides method-level validation.
     *
     * @return the method validation post processor.
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    /**
     * This bean provides the mail sender.
     *
     * @return the mail sender.
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(email);
        mailSender.setPassword(emailPassword);
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
    }

    /**
     * This class provides mapping for the favicon of the application.
     */
    @Controller
    static class FaviconController {

        /**
         * Serves the favicon.
         *
         * @return forwards the requests to the favicon location.
         */
        @GetMapping("favicon.ico")
        public String favicon() {
            return "forward:/resources/img/favicon.ico";
        }
    }

}

