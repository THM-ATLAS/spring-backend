package com.example.atlasbackend.exception

object TaskNotFoundException : RuntimeException() {
    private const val serialVersionUID = 1L
}

object InternalServerErrorException : RuntimeException() {
    private const val serialVersionUID = 1L
}