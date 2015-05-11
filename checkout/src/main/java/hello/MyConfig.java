package hello;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.social.oauth2.OAuth2Template;
import sun.net.www.http.HttpClient;

/**
 * Created by agutheil on 11.05.15.
 */
@Configuration
public class MyConfig {
    @Bean
    public MightyCore restTemplate(){
        MightyCore mightyCore = new MightyCore("mightymerceapp","mySecretOAuthSecret","http://localhost:8080/oauth/authorize", "http://localhost:8080/oauth/authenticate", "http://localhost:8080/oauth/token");
        mightyCore.setUseParametersForClientAuthentication(true);
        mightyCore.setRequestFactory(createSecureTransport("admin","admin"));
        return mightyCore;
    }

    public ClientHttpRequestFactory createSecureTransport(final String username, final String password) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        BasicCredentialsProvider credentialsProvider =  new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        httpClient.setCredentialsProvider(credentialsProvider);
        ClientHttpRequestFactory rf = new HttpComponentsClientHttpRequestFactory(httpClient);
        return rf;
    }
}
