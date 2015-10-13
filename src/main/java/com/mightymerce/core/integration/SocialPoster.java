package com.mightymerce.core.integration;

import com.mightymerce.core.domain.Product;
import com.mightymerce.core.domain.enumeration.Channel;

/**
 * Created by agutheil on 06.05.15.
 */
public interface SocialPoster {
    public String post(Product product, String accessToken, Channel channel);
}
