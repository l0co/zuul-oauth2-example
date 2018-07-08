package com.lifeinide.oauth2.service.as;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Oauth2 authorization server with internal client.
 * <p>
 * Internal client is the one we trust to get username and password from user and usually it's out own web or mobile ui. For this client
 * we'd like the application to work in the same way as the default application authorized by session and cookie. I.e. we want the client
 * to be automatically logged out in 30 mins after the last click in the app.
 * </p>
 *
 * @author Lukasz Frankowski (lifeinide.com)
 */
@SpringBootApplication
@EnableAuthorizationServer
@EnableResourceServer
public class OAuthASApplication implements AuthorizationServerConfigurer, ResourceServerConfigurer {

	/**********************************************************************************************************
	 * Some additional beans we need
	 **********************************************************************************************************/

	@Autowired protected AuthenticationManager authenticationManager;

	/**
	 * Example user details service, in real life connected to some db.
	 */
	@SuppressWarnings("deprecation")
	@Bean
	public UserDetailsService userDetailsService() {
		return new InMemoryUserDetailsManager(

			User.withDefaultPasswordEncoder()
				.username("user")
				.password("user")
				.authorities("ROLE_USER")
				.build()

		);
	}


	/**********************************************************************************************************
	 * {@link AuthorizationServerConfigurer} = configuration for oauth2 authorization server
	 **********************************************************************************************************/

	/**
	 * Sets up two oauth2 clients.
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		clients.inMemory()

			// internal client config

			.withClient("internal")
			.secret("internal_secret")
			.scopes("account", "contacts", "internal")
			.resourceIds()
			.authorizedGrantTypes("refresh_token", "password") // to get the access token uses directly username+password
			.autoApprove(true) // all scopes are auto approved for internal client (no scopes approval view is displayed)
			.accessTokenValiditySeconds(10*60) // by default for the internal client access token will expiry after 10 min
			.refreshTokenValiditySeconds(30*60); // by default for the internal client refresh token will expiry after 30 min


	}

	/**
	 * This fixes the problem that authorization server expects passwords in default {@link PasswordEncoder} format, which is
	 * {@code "{encoder}encodedPass"} for {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}. We don't want our internal
	 * client to encode password before sends it, because it makes no sense. This default encoder for
	 * {@link AuthorizationServerSecurityConfigurer} looks like a spring security oauth2 glitch.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.passwordEncoder(NoOpPasswordEncoder.getInstance());
	}

	/**
	 * This method configures another security details which is an obscurity after having {@link AuthorizationServerSecurityConfigurer}
	 * method configuration above. I see no reason to split this configuration for two separate methods. What do we do here:
	 * <ol>
	 *     <li>If we want to use {@code "password"} grant type in internal client, we need to supply it with
	 *         {@link AuthenticationManager} and {@link UserDetailsService}.</li>
	 *     <li>By default refresh token expires after some amount of time and we set this up to 30 min for internal client. This means
	 *     	   that the client will be unconditionally logged out after this time, even if he is already clicking in our app. To avoid
	 *     	   this we set {@code reuseRefreshTokens(false)} what results in refreshing and returning new refresh token each time it's
	 *     	   used to get new access token. This operation then becomes our key feature to ensure normal look&feel for the user and
	 *     	   neccessary security. Please note that this also entices the refresh token timeout longer than access token timeout.</li>
	 * </ol>
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.authenticationManager(authenticationManager) 
			.userDetailsService(userDetailsService());
//			.reuseRefreshTokens(false); // TODOLF step 3

	}

	/**********************************************************************************************************
	 * {@link ResourceServerConfigurer} = security configuration.
	 **********************************************************************************************************/

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.anyRequest()
			.authenticated();	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
	}


	/**********************************************************************************************************
	 * {@link WebSecurityConfigurerAdapter} = another security configuration?
	 *
	 * This is the most obscure thing I've met in this whole config. This class is not intended to be used in
	 * oauth2 config, however there's now no other way to access authentication manager bean.
	 * See https://github.com/spring-projects/spring-boot/issues/11136
	 **********************************************************************************************************/

	@Configuration
	public static class AuthenticationMananagerProvider extends WebSecurityConfigurerAdapter {

		@Bean
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

	}

	public static void main(String[] args) {
		SpringApplication.run(OAuthASApplication.class, args);
	}

}
