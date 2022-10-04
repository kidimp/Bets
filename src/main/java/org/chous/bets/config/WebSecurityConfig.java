package org.chous.bets.config;

import org.chous.bets.services.UsrDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UsrDetailsService usrDetailsService;

    @Autowired
    public WebSecurityConfig(UsrDetailsService usrDetailsService) {
        this.usrDetailsService = usrDetailsService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                  .antMatchers("/users/all", "/teams/all").hasRole("ADMIN")
                    .antMatchers("/", "/registration", "/login", "/fixtures", "/tables", "/error").permitAll()
                    .anyRequest().hasAnyRole("USER", "ADMIN")
                .and()
                    .formLogin().loginPage("/login")
                    .loginProcessingUrl("/process_login")
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/login?error")
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usrDetailsService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}