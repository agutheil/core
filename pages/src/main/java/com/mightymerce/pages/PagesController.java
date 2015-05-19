package com.mightymerce.pages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by agutheil on 19.05.15.
 */
@Controller
@RequestMapping("/")
public class PagesController {
    @RequestMapping(value = "facebook/{articleId}", method=RequestMethod.GET)
    public String renderFacebook(@PathVariable String articleId, Model model) {
        model.addAttribute("articleId", articleId);
        return "facebook";
    }
}
