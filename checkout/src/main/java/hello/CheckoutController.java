package hello;

import javax.inject.Inject;
import javax.websocket.server.PathParam;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
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
@SessionAttributes("articleId")
public class CheckoutController {

    private Facebook facebook;

    private OAuth2Template oAuth2Template;

    //private MightyCore mightyCore;

    private MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

    @Inject
    public CheckoutController(Facebook facebook, OAuth2Template oAuth2Template) {
        this.facebook = facebook;
        this.oAuth2Template = oAuth2Template;
      //  this.mightyCore = mightyCore;
        params.set("scope", "read");

    }

    @RequestMapping(value = "checkout/{articleId}", method=RequestMethod.GET)
    public String checkout(@PathVariable String articleId ,Model model) {
        model.addAttribute("articleId", articleId);
        return process(model);
    }

    @RequestMapping(method=RequestMethod.GET)
    public String show(Model model) {
        return process(model);
    }

    private String process(Model model) {
        if (!facebook.isAuthorized()) {
            return "redirect:/connect/facebook";
        }
        model.addAttribute(facebook.userOperations().getUserProfile());
        AccessGrant ag = oAuth2Template.exchangeCredentialsForAccess("admin", "admin",params);
        String articleId = (String) model.asMap().get("articleId");
        MightyCore mightyCore = new MightyCore(ag.getAccessToken(), TokenStrategy.AUTHORIZATION_HEADER);
        Article article = mightyCore.getArticle(articleId);
        model.addAttribute("articleName",article.getName());
        return "checkout";
    }

}
