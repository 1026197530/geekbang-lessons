package org.geektimes.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 配置接口/hello不需要登录
 */
//@Order(6666)
//@Configuration
public class HttpSecurityConfig {//extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(HttpSecurityConfig.class);

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        super.configure(web);
//        logger.info("HttpSecurityConfig - WebSecurity: " + web);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().antMatchers("/hello").permitAll()
//                .antMatchers("/user/*").permitAll();
//        super.configure(http);
//        logger.info("HttpSecurityConfig - HttpSecurity: " + http);
//    }

}
