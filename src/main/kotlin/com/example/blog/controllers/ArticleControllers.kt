package com.example.blog.controllers

import com.example.blog.Article
import com.example.blog.ArticleRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/api")
class ArticleController(private val repository: ArticleRepository){
    @GetMapping("/articles")
    fun index() = repository.findAllByOrderByAddedAtDesc()

    @GetMapping("/articles/{id}")
    fun getById(@PathVariable(value = "id") articleId: Long) =
            repository.findById(articleId) ?: ResponseStatusException(HttpStatus.NOT_FOUND, "This article does not exist")

    @GetMapping("/articles/search")
    fun getBy(@RequestParam(required = false) slug: String?,
              @RequestParam(required = false) headline: String?) :
            ResponseEntity<Iterable<Article?>>{

        val articles = if (slug != null){
                        repository.findBySlug(slug)
                      } else {
                        if (headline != null) {
                            repository.findByHeadline(headline)
                        } else {
                            null
                        }
                      }

        return if (articles != null) ResponseEntity.ok(articles) else ResponseEntity.notFound().build()
    }

    @PostMapping("/articles")
    fun createNew(@RequestBody article: Article): Article =
            repository.save(article)

    @DeleteMapping("/articles/{id}")
    fun deleteById(@PathVariable(value = "id") articleId: Long): ResponseEntity<Void> {
        return repository.findById(articleId).map { article  ->
            repository.delete(article)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())
    }
}