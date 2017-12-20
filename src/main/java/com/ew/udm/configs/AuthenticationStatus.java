package com.ew.udm.configs;

public enum AuthenticationStatus {
    NONE,
    SUCCESS,
    TOKEN_EXPIRE,
    SESSION_EXPIRE,
    USER_EXPIRE,
    TOKEN_ERROR,
    TOKEN_SIGNATURE_ERROR
}
