package hut34.wallet.config;

import hut34.wallet.filter.AlwaysAllowPreauthFilter;
import hut34.wallet.filter.PreauthFilter;
import hut34.wallet.filter.ProtectedEnvironmentFilter;
import hut34.wallet.framework.security.SessionExpiryHeaderWriter;
import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.framework.usermanagement.model.UserAdapterGae;
import hut34.wallet.framework.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.contrib.gae.security.rest.RestLogoutSuccessHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.Filter;
import java.util.Optional;

import static hut34.wallet.controller.ProtectedEnvironmentController.PROTECTED_AUTH_PATH;
import static hut34.wallet.filter.ProtectedEnvironmentFilter.PROTECTED_ENVIRONMENT_PATH;
import static org.springframework.http.HttpMethod.GET;

@Configuration
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@EnableOAuth2Client
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private OAuth2ClientContext oauth2ClientContext;

    @Autowired
    private PrincipalExtractor googlePrincipalExtractor;

    @Autowired
    private PreauthFilter preauthFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // @formatter:off
        http
            .csrf()
                .ignoringAntMatchers("/protected-auth", "/system/**", "/task/**", "/cron/**", "/_ah/**")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .and()
            .exceptionHandling()
            .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new AntPathRequestMatcher("/api/**"))
        .and()
            .authorizeRequests()
            .antMatchers("/_ah/**", "/system/**", "/task/**", "/cron/**").permitAll()  // protected by security-constraint in web.xml which delegates to GCP's IAM
            .antMatchers("/login**", PROTECTED_ENVIRONMENT_PATH, PROTECTED_AUTH_PATH).permitAll()
            .antMatchers("/api/users/me").permitAll()
            .antMatchers(GET, "/api/reference-data").permitAll()
            .antMatchers("/api/error/**").permitAll()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/**").permitAll()
        .and()
            .logout()
            .logoutUrl("/api/logout")
            .logoutSuccessHandler(new RestLogoutSuccessHandler())
            .permitAll()
        .and()
            .addFilterBefore(oauthAuthenticationFilter(), BasicAuthenticationFilter.class)
            .addFilterBefore(preauthFilter, OAuth2ClientAuthenticationProcessingFilter.class)
            .headers()
            .addHeaderWriter(new SessionExpiryHeaderWriter())
            .contentSecurityPolicy("default-src 'self'; connect-src 'self' https://mainnet.infura.io https://api.etherscan.io; script-src 'self' https://cdn.polyfill.io; style-src 'self' 'unsafe-inline' https://fonts.googleapis.com blob:; img-src 'self' data:; font-src 'self' https://fonts.gstatic.com");
        // @formatter:on
    }

    private Filter oauthAuthenticationFilter() {
        OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/google");
        OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(googleClient(), oauth2ClientContext);
        googleFilter.setRestTemplate(googleTemplate);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(googleResource().getUserInfoUri(), googleClient().getClientId());
        tokenServices.setRestTemplate(googleTemplate);
        tokenServices.setPrincipalExtractor(googlePrincipalExtractor);
        googleFilter.setTokenServices(tokenServices);
        return googleFilter;
    }

    @Bean
    @ConfigurationProperties("oauth.google.client")
    public AuthorizationCodeResourceDetails googleClient() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("oauth.google.resource")
    public ResourceServerProperties googleResource() {
        return new ResourceServerProperties();
    }

    @Bean
    public Class<User> gaeUserClass() {
        return User.class;
    }

    @Bean
    public UserAdapterGae gaeUserAdapter(UserService userService) {
        return UserAdapterGae.byEmail(userService);
    }


    @ConditionalOnProperty("app.protectedEnvironmentPassword")
    @Bean
    public static ProtectedEnvironmentFilter protectedEnvironmentFilter(@Value("${app.protectedEnvironmentPassword}") String secretPassword) {
        return new ProtectedEnvironmentFilter(secretPassword, PROTECTED_AUTH_PATH, PROTECTED_ENVIRONMENT_PATH);
    }
    @ConditionalOnMissingBean(ProtectedEnvironmentFilter.class)
    @Bean
    public static PreauthFilter alwaysAllowPreauthFilter() {
        return new AlwaysAllowPreauthFilter();
    }

}
