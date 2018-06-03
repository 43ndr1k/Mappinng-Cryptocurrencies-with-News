package de.uni_leipzig.crypto_news_docs.service.assets.cryptoCurrency;

import de.uni_leipzig.crypto_news_docs.dto.crypto.response.CryptoCurrencyCountry;
import de.uni_leipzig.crypto_news_docs.dto.crypto.response.CryptoCurrencyCountryDto;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrency;
import java.util.Date;
import java.util.List;

public interface CryptoCurrencyService {

	List<CryptoCurrency> findByShortName(String shortName);
	CryptoCurrency create(CryptoCurrency cryptoCurrency);
	CryptoCurrency findByShortNameAndTimeSpan(String shortName, String unit, Date startDate, Date endDate);

	CryptoCurrencyCountry getCryptoCurrencyAVGByCountry(String cryptoName);
}
