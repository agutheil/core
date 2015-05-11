package hello;

import org.springframework.social.oauth2.OAuth2Template;

/**
 * Created by agutheil on 11.05.15.
 */
public class MightyCore extends OAuth2Template {
    public MightyCore(String clientId, String clientSecret, String authorizeUrl, String authenticateUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, authenticateUrl, accessTokenUrl);
    }

    public MightyCore(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
    }
}
