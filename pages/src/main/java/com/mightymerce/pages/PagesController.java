package com.mightymerce.pages;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.social.oauth2.TokenStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

/**
 * Created by agutheil on 19.05.15.
 */
@Controller
@RequestMapping("/")
public class PagesController {

    private final OAuth2Template oAuth2Template;

    private MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();


    @Inject
    public PagesController(OAuth2Template oAuth2Template) {
        this.oAuth2Template = oAuth2Template;
        params.set("scope", "read write");
    }

    @RequestMapping(value = "facebook/{articleId}", method=RequestMethod.GET)
    public String renderFacebook(@PathVariable String articleId, Model model) {
        model.addAttribute("articleId", articleId);
        Article article = getArticle(articleId);
        model.addAttribute("articleName",article.getName());
        return "facebook";
    }

    @RequestMapping(value = "pinterest/{articleId}", method=RequestMethod.GET)
    public String renderPinterest(@PathVariable String articleId, Model model) {
        model.addAttribute("articleId", articleId);
        Article article = getArticle(articleId);
        model.addAttribute("articleName",article.getName());
        return "pinterest";
    }

    @RequestMapping(value = "twitter/{articleId}", method=RequestMethod.GET)
    public String renderTwitter(@PathVariable String articleId, Model model) {
        model.addAttribute("articleId", articleId);
        Article article = getArticle(articleId);
        model.addAttribute("articleName",article.getName());
        return "twitter";
    }

    private Article getArticle(String articleId) {
        AccessGrant ag = oAuth2Template.exchangeCredentialsForAccess("admin", "admin",params);
        MightyCore mightyCore = new MightyCore(ag.getAccessToken(), TokenStrategy.AUTHORIZATION_HEADER);
        return mightyCore.getArticle(articleId);
    }
}
