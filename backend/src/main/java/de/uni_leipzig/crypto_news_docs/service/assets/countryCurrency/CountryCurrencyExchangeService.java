package de.uni_leipzig.crypto_news_docs.service.assets.countryCurrency;

import de.uni_leipzig.crypto_news_docs.model.assets.currency.country.CountryCurrency;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.country.CountryCurrencyExchange;
import java.util.Date;
import java.util.List;

public interface CountryCurrencyExchangeService {

	List<CountryCurrencyExchange> findByShortName(String name);
	CountryCurrencyExchange findByExchangeAndShortNameAndUnit(String exchange, String shortName, String unit);
	CountryCurrencyExchange create(List<CountryCurrency> countryCurrencyExchange, String exchange,
			String shortName, String unit);

	List<CountryCurrencyExchange> findByShortNameAndTimeSpan(String shortName, Date startTime, Date endTime);
}
