package edu.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
                .authorizeRequests()
                    .antMatchers("/rest/**", "/h2-console/**", "/manager.html").hasAuthority("ADMIN")
                    .antMatchers(HttpMethod.GET, "/api/clients", "/api/accounts").hasAuthority("ADMIN")
                    .antMatchers("/web/accounts.html", "/web/account.html", "/web/cards.html", "/web/transfers.html", "/web/loan-application.html").hasAuthority("CLIENT")
                    .antMatchers(HttpMethod.GET, "/api/clients/current", "/api/clients/account/send/{number}", "/api/loans", "/api/accounts/pdf/generate").hasAuthority("CLIENT")
                    .antMatchers(HttpMethod.POST, "/api/clients/current/cards", "/api/transactions", "/api/clients/current/accounts", "/api/loans", "/api/payloan").hasAuthority("CLIENT")
                    .antMatchers(HttpMethod.DELETE, "/api/clients/current/card", "/api/clients/current/account").hasAuthority("CLIENT")
                    .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                    .antMatchers("/index.html", "/login.html", "/js/**", "/css/**", "/images/**", "/web/css/**", "/web/js/**", "/web/images/**").permitAll()
                    .anyRequest().denyAll()
                .and()
                .formLogin()
                    .loginPage("/api/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                .and()
                .logout()
                    .logoutUrl("/api/logout")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                .and()
                .csrf().disable();
            http
                    .cors()
                    .and()
                    .headers()
                        .frameOptions().disable()
                    .and()
                    .exceptionHandling()
                        .authenticationEntryPoint((request, response, exception) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                        .authenticationEntryPoint(((request, response, authException) -> {
                            response.sendRedirect("/login.html?error=true");
                        }))
                    .and()
                    .formLogin()
                        .successHandler((request, response, authentication) -> {
                            clearAuthenticationAttributes(request);
                            if (authentication.getAuthorities().contains("ADMIN")){
                                response.sendRedirect("/manager.html");
                            }
                            response.sendRedirect("/web/accounts.html");
                        })
                    .and()
                    .formLogin()
                        .failureHandler((request, response, exception) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                    .and()
                    .logout()
                        .logoutSuccessHandler(((request, response, authentication) -> {
                            new HttpStatusReturningLogoutSuccessHandler();
                            response.sendRedirect("/login.html?logout=true");
                        }));
        //@formatter:on
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
