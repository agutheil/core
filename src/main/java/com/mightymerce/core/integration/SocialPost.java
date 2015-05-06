package com.mightymerce.core.integration;

import com.mightymerce.core.domain.Article;

/**
 * Created by agutheil on 04.05.15.
 */

public interface SocialPost {
    public String post(Article article, String accessToken);

}
