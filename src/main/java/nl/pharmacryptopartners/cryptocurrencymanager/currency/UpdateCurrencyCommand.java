package nl.pharmacryptopartners.cryptocurrencymanager.currency;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateCurrencyCommand(
        @NotBlank
        String ticker,
        @NotBlank
        String name,
        @NotNull
        @PositiveOrZero
        Double numberOfCoins,
        @NotNull
        @PositiveOrZero
        Double marketCapitalization
) {
}
