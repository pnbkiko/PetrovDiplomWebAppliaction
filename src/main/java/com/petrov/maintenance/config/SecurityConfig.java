// src/main/java/com/petrov/maintenance/config/SecurityConfig.java
package com.petrov.maintenance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @SuppressWarnings("deprecation")
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .usersByUsernameQuery(
                        "SELECT username, password, enabled FROM users WHERE username = ?")
                .authoritiesByUsernameQuery(
                        "SELECT u.username, r.name FROM users u " +
                                "JOIN roles r ON u.role_id = r.id WHERE u.username = ?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .antMatchers("/login").permitAll()

                // ADMIN
                .antMatchers("/machines/delete/**").hasRole("ADMIN")
                .antMatchers("/maintenance-types/delete/**").hasRole("ADMIN")
                .antMatchers("/schedule/delete/**").hasRole("ADMIN")
                .antMatchers("/maintenance-acts/delete/**").hasRole("ADMIN")
                .antMatchers("/machines/add").hasRole("ADMIN")
                .antMatchers("/maintenance-types/add").hasRole("ADMIN")

                // ADMIN + ENGINEER
                .antMatchers("/schedule/add").hasAnyRole("ADMIN", "ENGINEER")
                .antMatchers("/maintenance-acts/add").hasAnyRole("ADMIN", "ENGINEER")

                // Все
                .antMatchers("/", "/machines", "/maintenance-types", "/schedule",
                        "/maintenance-acts", "/calendar", "/help", "/profile").authenticated()

                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll();
    }
}