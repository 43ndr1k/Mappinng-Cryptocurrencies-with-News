package de.uni_leipzig.crypto_news_docs.service.assets.cryptoCurrency;

import de.uni_leipzig.crypto_news_docs.dto.crypto.create.CryptoCurrencyExchangeInitCreateDto;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrencyExchange;

public interface CryptoCurrencyExchangeService {

	CryptoCurrencyExchange findByExchangeName(String name);
	CryptoCurrencyExchange create(CryptoCurrencyExchange cryptoCurrencyExchange);
	CryptoCurrencyExchange update(CryptoCurrencyExchange cryptoCurrencyExchange);
	CryptoCurrencyExchange createWithCryptoCurrenciesAndPrices(CryptoCurrencyExchange cryptoCurrencyExchange,
			CryptoCurrencyExchangeInitCreateDto cryptoCurrencyExchangeInitCreateDto);

}
