package org.geektimes.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 什么都不配置(即接口/hello需要登录)
 */
@Deprecated
//@Order(7777)
//@Configuration
public class WebSecurityConfig { //extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        super.configure(web);
//        logger.info("WebSecurityConfig - WebSecurity: " + web);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
//        logger.info("WebSecurityConfig - HttpSecurity: " + http);
//    }

}
