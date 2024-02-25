package nl.pharmacryptopartners.cryptocurrencymanager.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.pharmacryptopartners.cryptocurrencymanager.controllerAdvice.exception.CurrencyNotFoundException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CurrencyController subject;

    @MockBean
    CurrencyService currencyServiceMock;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void itShouldSaveACurrency() {
        // given
        var command = Instancio.create(CreateCurrencyCommand.class);

        // when
        subject.createCurrency(command);

        // then
        verify(currencyServiceMock, times(1)).createCurrency(command);
    }

    @Test
    void itShouldNotSaveACurrencyWithAnEmptyName() throws Exception {
        // given
        var command = Instancio.of(CreateCurrencyCommand.class)
                .set(field(CreateCurrencyCommand::name), "")
                .create();

        // when
        var actual = mockMvc.perform(post("/api/currencies")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)));

        // then
        var message = Objects.requireNonNull(actual
                .andExpect(status().is4xxClientError())
                .andReturn().getResolvedException()).getMessage();
        assertTrue(message.contains("Name is mandatory."));

        verifyNoInteractions(currencyServiceMock);
    }

    @Test
    void itShouldGetACurrencyById() throws Exception {
        // given
        var expectedCurrency = Instancio.create(Currency.class);

        // when
        when(currencyServiceMock.getCurrencyById(expectedCurrency.getId())).thenReturn(expectedCurrency);
        var actual = mockMvc.perform(
                get(String.format("/api/currencies/%s", expectedCurrency.getId()))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        actual.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedCurrency.getId().toString()))
                .andExpect(jsonPath("$.ticker").value(expectedCurrency.getTicker()))
                .andExpect(jsonPath("$.name").value(expectedCurrency.getName()))
                .andExpect(jsonPath("$.numberOfCoins").value(expectedCurrency.getNumberOfCoins()))
                .andExpect(jsonPath("$.marketCapitalization").value(expectedCurrency.getMarketCapitalization()));

        verify(currencyServiceMock, times(1)).getCurrencyById(expectedCurrency.getId());
    }

    @Test
    void itShouldReturnNotFoundForUnknownCurrency() throws Exception {
        // given
        var unKnownId = UUID.randomUUID();

        // when
        when(currencyServiceMock.getCurrencyById(unKnownId)).thenThrow(new CurrencyNotFoundException(unKnownId));
        var actual = mockMvc.perform(
                get(String.format("/api/currencies/%s", unKnownId)).
                        contentType(MediaType.APPLICATION_JSON));

        // then
        actual.andExpect(status().isNotFound());

        verify(currencyServiceMock, times(1)).getCurrencyById(unKnownId);
    }

    @Test
    void itShouldGetCurrenciesWithPagination() throws Exception {
        // given
        List<CurrencyDTO> currencies = List.of(
                Instancio.create(CurrencyDTO.class),
                Instancio.create(CurrencyDTO.class),
                Instancio.create(CurrencyDTO.class)
        );

        var pageNumber = 0;
        var pageSize = 10;

        var currencyPage = new PageImpl<>(currencies, PageRequest.of(pageNumber, pageSize), currencies.size());

        // when
        doReturn(currencyPage).when(currencyServiceMock).getCurrenciesAsPageable(any(Pageable.class));
        var actual = mockMvc.perform(
                get(String.format("/api/currencies?page=%s&size=%s", pageNumber, pageSize))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        actual.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(currencies.size()))
                .andExpect(jsonPath("$.totalElements").value(currencies.size()))
                .andExpect(jsonPath("$.content[0].id").value(currencies.get(0).id().toString()))
                .andExpect(jsonPath("$.content[1].id").value(currencies.get(1).id().toString()))
                .andExpect(jsonPath("$.content[2].id").value(currencies.get(2).id().toString()));

        verify(currencyServiceMock, times(1)).getCurrenciesAsPageable(any(Pageable.class));
    }

    @Test
    void itShouldUpdateCurrencyById() throws Exception {
        // given
        var id = UUID.randomUUID();
        var command = Instancio.create(UpdateCurrencyCommand.class);

        // when
        subject.updateCurrency(id, command);

        // then
        verify(currencyServiceMock, times(1)).updateCurrencyById(id, command);
    }

    @Test
    void itShouldNotUpdateWhenRequiredNameFieldMissing() throws Exception {
        // given
        var id = UUID.randomUUID();
        var command = Instancio.of(UpdateCurrencyCommand.class)
                .ignore(field(UpdateCurrencyCommand::name))
                .create();

        // when
        var actual = mockMvc.perform(
                put(String.format("/api/currencies/%s", id))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)));

        // then
        var message = Objects.requireNonNull(
                actual
                        .andExpect(status().is4xxClientError())
                        .andReturn().getResolvedException()).getMessage();
        assertTrue(message.contains(("must not be blank")));
        verifyNoInteractions(currencyServiceMock);
    }
}
