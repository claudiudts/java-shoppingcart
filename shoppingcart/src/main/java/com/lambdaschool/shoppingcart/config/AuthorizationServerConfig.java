package com.lambdaschool.shoppingcart.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter
{
    private final String CLIENT_ID = System.getenv("OAUTHCLIENTID");
    private final String CLIENT_SECRET = System.getenv("OAUTHCLIENTSECRET");

    private final String GRANT_TYPE_PASSWORD = "password";
    private final String AUTHORIZATION_CODE = "authorization_code";
    private final String SCOPE_READ = "read";
    private final String SCOPE_WRITE = "write";
    private final String SCOPE_TRUST = "trust";

    private final int ACCESS_TOKEN_VALIDITY_TIME = -1;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void configure(ClientDetailsServiceConfigurer configure) throws Exception
    {
        configure.inMemory()
                .withClient(CLIENT_ID)
                .secret(encoder.encode(CLIENT_SECRET))
                .authorizedGrantTypes(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE)
                .scopes(SCOPE_READ, SCOPE_TRUST, SCOPE_WRITE)
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_TIME);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager);

        endpoints.pathMapping("/oauth/token", "/login");
    }
}
