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

import dev.a2.estore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * This class provides security configuration.
 *
 * @author Andrei Sidorov
 */
@Configuration
@EnableWebSecurity
@ComponentScan({"dev.a2.estore"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Injects bean UserService.
     *
     */
    @Autowired
    private UserService userService;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers(
                            "/",
                            "/signup",
                            "/forgot",
                            "/order/**",
                            "/error",
                            "/product",
                            "/products/**",
                            "/attributes/**",
                            "/favicon.ico",
                            "/api/**")
                .permitAll()
                    .antMatchers("/users/**")
                .hasAuthority("ROLE_ADMIN")
                    .antMatchers(
                            "/product/**",
                            "/orders/**",
                            "/reports",
                            "/category")
                .hasAuthority("ROLE_MANAGER")
                    .anyRequest().authenticated()
                    .and().csrf().disable()
                .formLogin()
                    .loginPage("/signin")
                    .defaultSuccessUrl("/")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/signout")
                    .logoutSuccessUrl("/")
                    .permitAll();
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

    }

    /**
     * This bean provides bcrypt password encoder.
     *
     * @return the bcrypt password encoder.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * This bean provides authentication for users.
     *
     * @return the authentication provider.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }


    @Override
    public void configure(final WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/resources/css/**")
                .antMatchers("/resources/js/**")
                .antMatchers("/resources/img/**");
    }




}

