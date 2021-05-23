package com.studing.cashRegister.exceptions;

/**
 * Class wrapper for exceptions.
 * @author tHolubets
 */
public class MyException extends Exception{
    String message;

    public MyException(String message, Throwable cause){
        super(message, cause);
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
