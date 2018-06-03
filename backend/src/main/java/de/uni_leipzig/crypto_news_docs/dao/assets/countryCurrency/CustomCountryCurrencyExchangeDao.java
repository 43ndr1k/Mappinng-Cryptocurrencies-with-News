package de.uni_leipzig.crypto_news_docs.dao.assets.countryCurrency;

import de.uni_leipzig.crypto_news_docs.model.assets.currency.country.CountryCurrencyExchange;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrency;
import java.util.Date;
import java.util.List;

public interface CustomCountryCurrencyExchangeDao {

	List<CountryCurrencyExchange> findByShortNameAndTimeSpan(String shortName, Date startDate, Date endDate);
}
