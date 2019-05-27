package br.com.rafaelportela.demo

import org.slf4j.LoggerFactory.getLogger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/articles")
class ArticlesController {

    companion object {
        private val logger = getLogger(ArticlesController::class.java)
        private val ALL_ARTICLES: List<Article> = listOf(
                Article("1", "First Post"),
                Article("2", "A Second Nice Post"))
    }

    @GetMapping
    fun allArticles(): List<Article> {
        logger.info("GET /articles " + ALL_ARTICLES.count())
        return ALL_ARTICLES
    }
}