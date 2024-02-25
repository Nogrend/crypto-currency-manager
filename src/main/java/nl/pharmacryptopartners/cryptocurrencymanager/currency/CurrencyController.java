package nl.pharmacryptopartners.cryptocurrencymanager.currency;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public Page<Currency> getCurrencies(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return currencyService.getCurrenciesAsPageable(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public CurrencyDTO getCurrencyById(@PathVariable UUID id) {
        var foundCurrency = currencyService.getCurrencyById(id);
        return new CurrencyDTO(
                foundCurrency.getId(),
                foundCurrency.getTicker(),
                foundCurrency.getName(),
                foundCurrency.getNumberOfCoins(),
                foundCurrency.getMarketCapitalization()
        );
    }

    @PostMapping
    public void createCurrency(@Valid @RequestBody CreateCurrencyCommand command) {
        this.currencyService.createCurrency(command);
    }

    @PutMapping("/{id}")
    public void updateCurrency(@PathVariable UUID id,
                               @Valid @RequestBody UpdateCurrencyCommand command) {
        this.currencyService.updateCurrencyById(id, command);
    }

    @DeleteMapping("/{id}")
    public void deleteCurrency(@PathVariable UUID id) {
        this.currencyService.deleteCurrencyById(id);
    }
}
