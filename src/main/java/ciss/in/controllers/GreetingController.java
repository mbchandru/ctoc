package ciss.in.controllers;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@Controller
public class GreetingController  extends WebMvcConfigurerAdapter {
       
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/index");
        registry.addViewController("/user/contact").setViewName("/user/contact");
    }
    
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/myicon.ico").addResourceLocations("/myicon.ico");
        registry.addResourceHandler("/robots.txt").addResourceLocations("/robots.txt");
    }    
}