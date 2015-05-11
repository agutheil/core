package hello;

import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.social.oauth2.TokenStrategy;

/**
 * Created by agutheil on 11.05.15.
 */
public class MightyCore extends AbstractOAuth2ApiBinding {
    protected MightyCore() {
        super();
    }

    protected MightyCore(String accessToken) {
        super(accessToken);
    }

    protected MightyCore(String accessToken, TokenStrategy tokenStrategy) {
        super(accessToken, tokenStrategy);
    }

    public Article getArticle(String articleId) {
        Article article = getRestTemplate().getForObject("http://localhost:8080/api/articles/"+articleId, Article.class);
        return article;
    }

    public void createOrder(Article article, String id) {
        Order order = new Order();
        order.setTest(article.getArticleId());
        order.setTest2(id);
        getRestTemplate().postForObject("http://localhost:8080/api/socialOrders/",order,Order.class);
    }
}
