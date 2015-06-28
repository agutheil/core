package com.mightymerce.checkout;

import javax.inject.Inject;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
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
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/")
public class CheckoutController {


    private OAuth2Template oAuth2Template;


    private MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

    @Inject
    public CheckoutController(OAuth2Template oAuth2Template) {
        this.oAuth2Template = oAuth2Template;
        params.set("scope", "read write");

    }

    @RequestMapping(value = "checkout/{articleId}", method=RequestMethod.GET)
    public String checkout(@PathVariable String articleId, Model model) {
        model.addAttribute("articleId", articleId);
        AccessGrant ag = oAuth2Template.exchangeCredentialsForAccess("admin", "admin",params);
        MightyCore mightyCore = new MightyCore(ag.getAccessToken(), TokenStrategy.AUTHORIZATION_HEADER);
        Article article = mightyCore.getArticle(articleId);
        //TODO: Darf erst übermittelt werden, wenn Paypal erfolgreich war
        mightyCore.createOrder(article);
        model.addAttribute("articleName",article.getName());
        model.addAttribute("paypal",article.getPaypal());
        return "checkout";
    }

    @RequestMapping(value = "success", method=RequestMethod.GET)
    public String success() {
        return "success";
    }

    @RequestMapping(value = "cancel", method=RequestMethod.GET)
    public String cancel() {
        return "cancel";
    }



}
