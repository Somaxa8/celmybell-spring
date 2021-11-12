package com.somacode.celmybell.security;

import com.somacode.celmybell.entity.Authority;
import com.somacode.celmybell.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.sql.DataSource;

@Configuration
public class OAuth2Config {

    @Autowired DataSource dataSource;

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource) {
            private final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            private static final String DEFAULT_ACCESS_TOKEN_SELECT_STATEMENT = "select token_id, token from oauth_access_token where token_id = ?";
            private String selectAccessTokenSql = DEFAULT_ACCESS_TOKEN_SELECT_STATEMENT;

            @Override
            public OAuth2AccessToken readAccessToken(String tokenValue) {
                OAuth2AccessToken accessToken = null;
                try {
                    accessToken = jdbcTemplate.queryForObject(
                        selectAccessTokenSql,
                        (rs, rowNum) -> deserializeAccessToken(rs.getBytes(2)),
                        extractTokenKey(tokenValue)
                    );
                } catch (EmptyResultDataAccessException e) {
//                    if (LOG.isInfoEnabled()) {
//                        LOG.info("Failed to find access token for token " + tokenValue);
//                    }
                } catch (IllegalArgumentException e) {
                    LogService.out.warn("Failed to deserialize access token for " + tokenValue);
                    removeAccessToken(tokenValue);
                }
                return accessToken;
            }
        };
    }

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Autowired TokenStore tokenStore;
        @Autowired LogoutSuccess logoutSuccess;
        @Value("${spring.application.name}") String appName;

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                .exceptionHandling().authenticationEntryPoint(new BasicAuthenticationEntryPoint())
                .and().logout().logoutUrl("/api/logout").logoutSuccessHandler(logoutSuccess)
                .and().csrf().disable().headers().frameOptions().disable()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/*api/**").authenticated();
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId("res_" + appName).tokenStore(tokenStore);
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired DataSource dataSource;
        @Autowired TokenStore tokenStore;
        @Autowired AuthenticationManager authenticationManager;

        @Bean
        protected AuthorizationCodeServices authorizationCodeServices() {return new JdbcAuthorizationCodeServices(dataSource);}

        @Bean
        protected ApprovalStore approvalStore() {return new JdbcApprovalStore(dataSource);}

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) {
            security.allowFormAuthenticationForClients();
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.jdbc(dataSource);
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
            endpoints
                    .authorizationCodeServices(authorizationCodeServices())
                    .approvalStore(approvalStore())
                    .tokenStore(tokenStore)
                    .authenticationManager(authenticationManager);
        }
    }

    @Order(1)
    @Configuration
    public static class FormLoginWebSecurityConfigureAdapter extends WebSecurityConfigurerAdapter {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                .requestMatchers().antMatchers(
                        "/swagger-resources/**",
                        "/swagger-ui.html**",
                        "/swagger",
                        "/oauth/authorize",
                        "/admin/**",
                        "/login", "/logout",
                        "/h2-console/**"
                )
                .and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/**", "/h2-console/**")
                .hasAnyAuthority(Authority.Role.ADMIN.toString())
                .antMatchers("/swagger-resources/**", "/swagger-ui.html**", "/swagger")
                .hasAnyAuthority(Authority.Role.SWAGGER.toString(), Authority.Role.ADMIN.toString())
                .and().formLogin().loginPage("/login").failureUrl("/login?error=1").permitAll()
                .and().logout().logoutUrl("/logout").permitAll();
        }
    }

}
