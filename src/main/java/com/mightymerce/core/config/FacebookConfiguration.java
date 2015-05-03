package com.mightymerce.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

/**
 * Created by agutheil on 03.05.15.
 */
@Configuration
public class FacebookConfiguration {
    @Bean
    Facebook facebook() {
        // id 404823203038294
        // secret 43b8157da55486a94ec99514d23304f5
        // namespace mightymercetest
        // at CAACEdEose0cBAMUQJfR9yAxf0wSZCZAhl7yi4pofe3li001xUXjdh1yNDxTZCNZCWZB4dYlLzJhkZB3GEEoojWsG8r9oCLK82J5Mv6CKkFrgxbnFtReMDfRHrBjAk9BH29vMg2jyxwictzCBbYwMPo7coI1UzcXN28YxrEYshGSU5NWHUBlivTDEBYx1EKS8RVz8l4m3WpeUJA9tc2r8CzT2xUonD8qCsZD
        return new FacebookTemplate("CAACEdEose0cBAJxgFZBkKGncVLO1Plddc9sAIl3YNYtuHVe6beVX5JJZB18cgmEqtXXeWpK6fVt5Egr8ZAOCrAeqyPxwW1ZAd6EZABxdsD3XqkIK1oBe1j0RrQEZArZAa7CyZArp4mIoOFdTz09ffsADc2901T9rGvpEHrEsHGCnCUdBLtVXAEZCWrRVVrVFH7f07RG9vBzvT7kTWwoDSLfYBO4xZCosRRYxwZD", "mightymercetest");
    }
}
