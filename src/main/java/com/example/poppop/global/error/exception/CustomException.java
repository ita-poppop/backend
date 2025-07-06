package com.example.poppop.global.error.exception;

import com.example.poppop.global.error.BaseError;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final BaseError baseError;

    public CustomException(BaseError baseError) {
        super(baseError.getMessage());
        this.baseError = baseError;
    }

    public CustomException(BaseError baseError,String message) {
        super(message);
        this.baseError = baseError;
    }
}
