package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.Article;
import com.mightymerce.core.repository.ArticleRepository;
import com.mightymerce.core.web.rest.util.HeaderUtil;
import com.mightymerce.core.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Article.
 */
@RestController
@RequestMapping("/api")
public class ArticleResource {

    private final Logger log = LoggerFactory.getLogger(ArticleResource.class);

    @Inject
    private ArticleRepository articleRepository;

    /**
     * POST  /articles -> Create a new article.
     */
    @RequestMapping(value = "/articles",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Article> create(@Valid @RequestBody Article article) throws URISyntaxException {
        log.debug("REST request to save Article : {}", article);
        if (article.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new article cannot already have an ID").body(null);
        }
        Article result = articleRepository.save(article);
        return ResponseEntity.created(new URI("/api/articles/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("article", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /articles -> Updates an existing article.
     */
    @RequestMapping(value = "/articles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Article> update(@Valid @RequestBody Article article) throws URISyntaxException {
        log.debug("REST request to update Article : {}", article);
        if (article.getId() == null) {
            return create(article);
        }
        Article result = articleRepository.save(article);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("article", article.getId().toString()))
                .body(result);
    }

    /**
     * GET  /articles -> get all the articles.
     */
    @RequestMapping(value = "/articles",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Article>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Article> page = articleRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/articles", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /articles/:id -> get the "id" article.
     */
    @RequestMapping(value = "/articles/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Article> get(@PathVariable Long id) {
        log.debug("REST request to get Article : {}", id);
        return Optional.ofNullable(articleRepository.findOne(id))
            .map(article -> new ResponseEntity<>(
                article,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /articles/:id -> delete the "id" article.
     */
    @RequestMapping(value = "/articles/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Article : {}", id);
        articleRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("article", id.toString())).build();
    }
}
