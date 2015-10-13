package com.mightymerce.core.integration;

import com.mightymerce.core.domain.Product;

/**
 * Created by agutheil on 04.05.15.
 */

public interface SocialPost {
    public String post(Product product, String accessToken);

}
