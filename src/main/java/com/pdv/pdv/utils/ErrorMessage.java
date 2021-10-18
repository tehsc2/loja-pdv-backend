package com.pdv.pdv.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorMessage {

    private String mensagem;
    private Object objeto;
}
