package nl.pharmacryptopartners.cryptocurrencymanager.currency;

import nl.pharmacryptopartners.cryptocurrencymanager.controllerAdvice.exception.CurrencyNotFoundException;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepositoryMock;

    @InjectMocks
    CurrencyService subject;

    @Captor
    ArgumentCaptor<Currency> currencyArgumentCaptor;

    @Test
    void itShouldFindAllCurrencies() {
        // given
        var currencies = List.of(
                Instancio.create(Currency.class),
                Instancio.create(Currency.class)
        );
        var expectedPage = new PageImpl<>(currencies);

        // when
        when(currencyRepositoryMock.findAll(Pageable.unpaged())).thenReturn(expectedPage);
        var resultPage = subject.getCurrenciesAsPageable(Pageable.unpaged());

        // then
        assertEquals(expectedPage, resultPage);
    }

    @Test
    void itShouldGetACurrency() {
        // given
        var expectedCurrency = Instancio.create(Currency.class);

        // when
        when(currencyRepositoryMock.findById(expectedCurrency.getId()))
                .thenReturn(Optional.of(expectedCurrency));
        subject.getCurrencyById(expectedCurrency.getId());

        // then
        verify(currencyRepositoryMock).findById(expectedCurrency.getId());
    }

    @Test
    void itShouldThrowExceptionWhenCurrencyNotFound() {
        // given
        var id = Instancio.create(UUID.class);
        var expectedExceptionMessage = "Currency not found with id " + id;

        // when
        when(currencyRepositoryMock.findById(id))
                .thenReturn(Optional.empty());
        var exception = assertThrows(CurrencyNotFoundException.class, () -> subject.getCurrencyById(id));

        // then
        assertEquals(expectedExceptionMessage, exception.getMessage());
        verify(currencyRepositoryMock).findById(id);
    }

    @Test
    void itShouldCreateACurrency() {
        // given
        var command = Instancio.create(CreateCurrencyCommand.class);

        // when
        subject.createCurrency(command);

        // then
        verify(currencyRepositoryMock).save(currencyArgumentCaptor.capture());
        var capturedCurrency = currencyArgumentCaptor.getValue();
        assertEquals(capturedCurrency.getId(), command.id());
        assertEquals(capturedCurrency.getTicker(), command.ticker());
        assertEquals(capturedCurrency.getNumberOfCoins(), command.numberOfCoins());
        assertEquals(capturedCurrency.getMarketCapitalization(), command.marketCapitalization());
    }

    @Test
    void itShouldUpdateACurrency() {
        // given
        var command = Instancio.create(UpdateCurrencyCommand.class);
        var existingCurrency = Instancio.create(Currency.class);

        // when
        when(currencyRepositoryMock.findById(existingCurrency.getId()))
                .thenReturn(Optional.of(existingCurrency));
        subject.updateCurrencyById(existingCurrency.getId(), command);

        // then
        verify(currencyRepositoryMock).save(currencyArgumentCaptor.capture());
        var capturedCurrency = currencyArgumentCaptor.getValue();
        assertEquals(capturedCurrency.getId(), existingCurrency.getId());
        assertEquals(capturedCurrency.getTicker(), existingCurrency.getTicker());
        assertEquals(capturedCurrency.getNumberOfCoins(), existingCurrency.getNumberOfCoins());
        assertEquals(capturedCurrency.getMarketCapitalization(), existingCurrency.getMarketCapitalization());
    }

    @Test
    void itShouldThrowExceptionWhenCurrencyToUpdateNotFound() {
        // given
        var id = Instancio.create(UUID.class);
        var command = Instancio.create(UpdateCurrencyCommand.class);
        var expectedExceptionMessage = "Currency not found with id " + id;

        // when
        when(currencyRepositoryMock.findById(id))
                .thenReturn(Optional.empty());
        var exception = assertThrows(CurrencyNotFoundException.class, () -> subject.updateCurrencyById(id, command));

        // then
        assertEquals(expectedExceptionMessage, exception.getMessage());
        verify(currencyRepositoryMock).findById(id);
    }

    @Test
    void itShouldDeleteACurrency() {
        // given
        var existedCurrency = Instancio.create(Currency.class);

        // when
        when(currencyRepositoryMock.findById(existedCurrency.getId()))
                .thenReturn(Optional.of(existedCurrency));
        subject.deleteCurrencyById(existedCurrency.getId());

        // then
        verify(currencyRepositoryMock).deleteById((existedCurrency.getId()));
    }
}
