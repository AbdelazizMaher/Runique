package com.zoksh.auth.data

import android.util.Patterns
import com.zoksh.auth.domain.PatternValidator

object EmailPatternValidator: PatternValidator {
    override fun matches(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}