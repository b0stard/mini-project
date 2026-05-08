package com.example.demo.config

import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.beans.BeanProperty

@Bean
fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
}