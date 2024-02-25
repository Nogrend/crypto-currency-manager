package nl.pharmacryptopartners.cryptocurrencymanager.controllerAdvice;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorResponse {

    private final List<Violation> violations = new ArrayList<>();

    public List<Violation> getViolations() {
        return violations;
    }
}
