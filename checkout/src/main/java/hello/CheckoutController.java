package hello;

import javax.inject.Inject;
import javax.websocket.server.PathParam;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/")
@SessionAttributes("articleId")
public class CheckoutController {

    private Facebook facebook;

    @Inject
    public CheckoutController(Facebook facebook) {
        this.facebook = facebook;
    }

    @RequestMapping(value = "/checkout/{articleId}", method=RequestMethod.GET)
    public String checkout(@PathVariable String articleId ,Model model) {
        model.addAttribute("articleId", articleId);
        if (!facebook.isAuthorized()) {
            return "redirect:/connect/facebook";
        }
        model.addAttribute(facebook.userOperations().getUserProfile());
        return "checkout";
    }

    @RequestMapping(method=RequestMethod.GET)
    public String show(Model model) {
        if (!facebook.isAuthorized()) {
            return "redirect:/connect/facebook";
        }
        model.addAttribute(facebook.userOperations().getUserProfile());
        return "checkout";
    }
}
