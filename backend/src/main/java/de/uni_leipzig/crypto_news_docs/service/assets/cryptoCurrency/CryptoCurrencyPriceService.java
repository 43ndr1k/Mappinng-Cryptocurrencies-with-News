package de.uni_leipzig.crypto_news_docs.service.assets.cryptoCurrency;

import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrencyPrice;
import java.util.List;

public interface CryptoCurrencyPriceService {

	CryptoCurrencyPrice findByUnit(String unit);
	CryptoCurrencyPrice create(CryptoCurrencyPrice cryptoCurrencyPrice);
	List<CryptoCurrencyPrice> findAllPrices();

}
