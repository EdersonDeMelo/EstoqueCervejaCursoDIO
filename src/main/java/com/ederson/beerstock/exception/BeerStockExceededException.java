package com.ederson.beerstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerStockExceededException extends Exception{
    public BeerStockExceededException (Long id, int quantityToIncrement){
        super(String.format("Cervejas com% s ID para incremento informado excede a capacidade máxima de estoque:% s", id, quantityToIncrement ));
    }
}
