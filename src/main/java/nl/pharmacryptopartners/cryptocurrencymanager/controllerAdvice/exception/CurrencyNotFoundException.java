package nl.pharmacryptopartners.cryptocurrencymanager.controllerAdvice.exception;

import java.util.UUID;

public class CurrencyNotFoundException extends RuntimeException {

    public CurrencyNotFoundException(UUID id) {
        super("Currency not found with id " + id);
    }
}
