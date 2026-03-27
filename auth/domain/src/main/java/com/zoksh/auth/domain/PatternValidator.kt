package com.zoksh.auth.domain

interface PatternValidator {
    fun matches(email: String): Boolean
}