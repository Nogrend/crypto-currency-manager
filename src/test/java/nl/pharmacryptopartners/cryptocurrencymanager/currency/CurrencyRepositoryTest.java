package nl.pharmacryptopartners.cryptocurrencymanager.currency;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CurrencyRepositoryTest {

    @Autowired
    CurrencyRepository subject;

    @Test
    void itShouldSaveACurrency() {
        // given
        var currency = Instancio.create(Currency.class);

        // when
        subject.save(currency);
        var actual = subject.findById(currency.getId()).get();

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(currency);
    }
}