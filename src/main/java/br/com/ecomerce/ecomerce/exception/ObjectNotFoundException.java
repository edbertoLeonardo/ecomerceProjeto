package br.com.ecomerce.ecomerce.exception;

public class ObjectNotFoundException extends RuntimeException{

    public ObjectNotFoundException(String mensagem){
        super(mensagem);
    }

    public ObjectNotFoundException(String mensagem, Throwable causa){
        super(mensagem, causa);
    }
}
