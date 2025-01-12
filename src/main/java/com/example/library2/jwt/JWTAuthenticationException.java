package com.example.library2.jwt;


public class JWTAuthenticationException extends RuntimeException {

    public JWTAuthenticationException(String msg) {
        super(msg);
    }
}