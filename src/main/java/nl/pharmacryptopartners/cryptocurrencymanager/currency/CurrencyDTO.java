package nl.pharmacryptopartners.cryptocurrencymanager.currency;

import java.util.UUID;

public record CurrencyDTO(UUID id,
                          String ticker,
                          String name,
                          Double numberOfCoins,
                          Double marketCapitalization
) {
}