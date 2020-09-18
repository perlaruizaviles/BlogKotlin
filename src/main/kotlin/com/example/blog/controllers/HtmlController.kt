package com.example.blog.controllers

import com.example.blog.Article
import com.example.blog.ArticleRepository
import com.example.blog.User
import com.example.blog.format
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@Controller
class HtmlController(private val repository : ArticleRepository) {

    @GetMapping
    fun blog(model: Model) : String {
        model["title"] = "Blog"
        model["banner"] = "The blog will be down tomorrow"
        model["articles"] = repository.findAllByOrderByAddedAtDesc().map { it.render() }
        return "blog"
    }

    @GetMapping("/article/{slug}")
    fun article(@PathVariable slug : String, model : Model) : String {
        val article = repository
                .findBySlug(slug).first()
                ?.render()
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "This article does not exist")

        model["title"] = article.title
        model["test"] = "testing"
        model["article"] = article
        return "article"
    }

    fun Article.render() = RenderedArticle(
            slug,
            title,
            headline,
            content,
            author,
            addedAt.format()
    )

    data class RenderedArticle(
            val slug: String,
            val title: String,
            val headline: String,
            val content: String,
            val author: User,
            val addedAt: String)
}