package com.pdv.pdv.exceptions;

import com.pdv.pdv.utils.ErrorMessage;
import lombok.Getter;

@Getter
public class ProdutoSemEstoqueException extends RuntimeException {

    private ErrorMessage errorMessage;

    public ProdutoSemEstoqueException(String message, Object object) {
        this.errorMessage = new ErrorMessage(message, object);
    }
}
