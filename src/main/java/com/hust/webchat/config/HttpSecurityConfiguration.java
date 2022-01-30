package com.hust.webchat.config;

import com.hust.webchat.security.jwt.JWTConfigurer;
import com.hust.webchat.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

/**
 * @EnableGlobalMethodSecurity(prePostEnabled = true) cái này sẽ giúp mình có thể kiểm soát security đến từng phương thức.
 * Ở đây prePostEnabled = true => sử dụng 2 annotation @PreAuthorize và @PostAuthorize để phân quyền.
 *
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class HttpSecurityConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    @Import(SecurityProblemSupport.class)
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        private final TokenProvider tokenProvider;

        private final SecurityProblemSupport problemSupport;

        private final CorsFilter corsFilter;

        public ApiWebSecurityConfigurationAdapter(TokenProvider tokenProvider,
                                                  SecurityProblemSupport problemSupport, CorsFilter corsFilter) {
            this.tokenProvider = tokenProvider;
            this.problemSupport = problemSupport;
            this.corsFilter = corsFilter;
        }

        @Override
        public void configure(WebSecurity web) {
            web.ignoring()
                    .antMatchers(HttpMethod.OPTIONS, "/**")
                    .antMatchers("/js/*.{js,html}")
                    .antMatchers("/i18n/**")
                    .antMatchers("/content/**")
                    .antMatchers("/swagger-ui/index_mess.html")
                    .antMatchers(HttpMethod.GET, "/api/public/**")
                    .antMatchers(HttpMethod.POST, "/api/public/**");
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    .antMatcher("/api/**")
                    .csrf()
                    .disable()
                    .exceptionHandling()
                    .authenticationEntryPoint(problemSupport)
                    .accessDeniedHandler(problemSupport)
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/public/**").permitAll()
                    .antMatchers("/api/authenticate").permitAll()
                    .antMatchers("/api/register").permitAll()
                    .antMatchers("/api/account/activate").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic()
                    .and()
                    .apply(securityConfigurerAdapter());
        }

        private JWTConfigurer securityConfigurerAdapter() {
            return new JWTConfigurer(tokenProvider);
        }

    }

    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/api/public/**").permitAll()
                    .antMatchers("/api/authenticate").permitAll()
                    .antMatchers("/api/register").permitAll()
                    .antMatchers("/resources/**").permitAll()
                    .anyRequest().permitAll()
                    .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/?logoutSuccess=true")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .and()
                    .csrf()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        }
    }
}

// contentSecurityPolicy(CPS) giúp ngăn chặn những request xấu được gọi từ website của chúng ta
// CSP là tập hợp một danh sách an toàn (whitelist) những domain hay kiểu script, style, image, frame mà trình duyệt được
// load trên website của chúng ta. Điều này đảm bảo chỉ có những script, style, image, frame được chúng ta chỉ định có thể được tải,
// những request không nằm trong whitelist sẽ bị chặn ngay tức khắc


//Referrer-Policy là một HTTP Header điều khiển việc mình gửi thông tin referrer đi. Tuỳ thuộc vào nhu cầu mình muốn gửi đi
// bằng nào thông tin về referrer, hay bảo vệ thông tin đến mức nào mà có thể lựa chọn rule cho phù hợp
