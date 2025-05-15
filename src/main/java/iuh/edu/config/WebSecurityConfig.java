
package iuh.edu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import iuh.edu.service.implement.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("api/products",
                        "api/products/bestseller",
                        "api/products/latest",
                        "api/products/rated",
                        "api/products/suggest/**",
                        "api/products/category/**",
                        "api/products/{id}",
                        "api/categories", "api/categories/{id}",
                        "api/rates/**",
                        "api/send-mail/**",
                        "api/cart/user/**",
                        "api/orders/user/**",
                        "api/favorites/email/**",
                        "api/cartDetail/**",
                        "api/auth/email/**",
                        "api/auth/signin/**",
                        "api/auth/signin/**",
                        "api/auth/send-mail-forgot-password-token",
                        "forgot-password",
                        "/v3/api-docs/**", // API docs của Swagger
                        "/swagger-ui/**",   // Swagger UI
                        "/swagger-ui.html",
                        "api/recommendations/nopage/**",
                        "api/notification/**")
                .permitAll();

        http.authorizeRequests().antMatchers("api/orderDetail/**", "api/cart/**").access("hasRole('ROLE_USER')");

        http.authorizeRequests().antMatchers("api/orderDetail/**", "api/cart/**", "api/statistical/**", "api/auth/**").access("hasRole('ROLE_ADMIN')");

        http.addFilterBefore(authenticationJwTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
