package nl.pharmacryptopartners.cryptocurrencymanager.currency;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record CreateCurrencyCommand(
        @NotNull(message = "Uuid is mandatory.")
        UUID id,
        @NotBlank(message = "Ticker is mandatory.")
        String ticker,
        @NotBlank(message = "Name is mandatory.")
        String name,
        @NotNull
        @PositiveOrZero
        Double numberOfCoins,
        @NotNull
        @PositiveOrZero
        Double marketCapitalization
) {
}
