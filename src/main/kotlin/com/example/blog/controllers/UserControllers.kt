package com.example.blog.controllers

import com.example.blog.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/api")
class UserController(private val repository: UserRepository){

    @GetMapping("/users/")
    fun findAll() = repository.findAll()

    @GetMapping("/users/{login}")
    fun findByLogin(@PathVariable login : String) =
        repository.findByLogin(login)
        ?: ResponseStatusException(HttpStatus.NOT_FOUND, "This user doesn't exist")
}