package nl.pharmacryptopartners.cryptocurrencymanager.currency;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Currency {
    @Id
    private UUID id;
    private String ticker;
    private String name;
    private Double numberOfCoins;
    private Double marketCapitalization;

    public Currency() {
    }

    public Currency(UUID id, String ticker, String name, Double numberOfCoins, Double marketCapitalization) {
        this.id = id;
        this.ticker = ticker;
        this.name = name;
        this.numberOfCoins = numberOfCoins;
        this.marketCapitalization = marketCapitalization;
    }

    public UUID getId() {
        return id;
    }

    public String getTicker() {
        return ticker;
    }

    public String getName() {
        return name;
    }

    public Double getNumberOfCoins() {
        return numberOfCoins;
    }

    public Double getMarketCapitalization() {
        return marketCapitalization;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfCoins(Double numberOfCoins) {
        this.numberOfCoins = numberOfCoins;
    }

    public void setMarketCapitalization(Double marketCapitalization) {
        this.marketCapitalization = marketCapitalization;
    }
}
