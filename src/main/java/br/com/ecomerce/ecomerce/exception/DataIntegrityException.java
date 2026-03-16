package br.com.ecomerce.ecomerce.exception;

public class DataIntegrityException extends RuntimeException{

    public DataIntegrityException(String mensagem){
        super(mensagem);
    }

    public DataIntegrityException(String mensagem, Throwable causa){
        super(mensagem, causa);
    }
}
