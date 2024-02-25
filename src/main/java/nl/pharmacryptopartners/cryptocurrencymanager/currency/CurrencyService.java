package nl.pharmacryptopartners.cryptocurrencymanager.currency;

import nl.pharmacryptopartners.cryptocurrencymanager.controllerAdvice.exception.CurrencyNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CurrencyService {

    public final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public Page<Currency> getCurrenciesAsPageable(Pageable pageable) {
        return this.currencyRepository.findAll(pageable);
    }

    public Currency getCurrencyById(UUID id) {
        return this.findByIdOrThrow(id);
    }

    public void createCurrency(CreateCurrencyCommand command) {
        var currency = new Currency(
                command.id(),
                command.ticker(),
                command.name(),
                command.numberOfCoins(),
                command.marketCapitalization());
        this.currencyRepository.save(currency);
    }

    public void updateCurrencyById(UUID id, UpdateCurrencyCommand command) {

        var currency = this.findByIdOrThrow(id);

        currency.setId(id);
        currency.setName(command.name());
        currency.setTicker(command.ticker());
        currency.setNumberOfCoins(command.numberOfCoins());
        currency.setMarketCapitalization(command.marketCapitalization());

        this.currencyRepository.save(currency);
    }

    public void deleteCurrencyById(UUID id) {
        this.currencyRepository.deleteById(id);
    }

    private Currency findByIdOrThrow(UUID id) {
        return this.currencyRepository.findById(id)
                .orElseThrow(() -> new CurrencyNotFoundException(id));
    }
}
