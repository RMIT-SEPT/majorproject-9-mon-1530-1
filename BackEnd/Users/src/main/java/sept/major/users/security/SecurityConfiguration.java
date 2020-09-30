package sept.major.users.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import static sept.major.users.security.SecurityConstants.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    UserAuthenticationProvider provider;

    public SecurityConfiguration(final UserAuthenticationProvider userAuthenticationProvider) {
        super();
        this.provider = userAuthenticationProvider;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(provider);
    }

    @Override
    public void configure(final WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers("/users/token/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()

                .and()
                .authenticationProvider(provider)
                .authorizeRequests()
                .requestMatchers(OPEN_ENDPOINTS)
                .permitAll()

                .and()
                .authenticationProvider(provider)
                .authorizeRequests()
                .requestMatchers(USER_ENDPOINTS)
                .hasRole(USER_CODE)

                .and()
                .authenticationProvider(provider)
                .authorizeRequests()
                .requestMatchers(ADMIN_ENDPOINTS)
                .hasAnyRole(ADMIN_CODE)

                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();
    }


}
