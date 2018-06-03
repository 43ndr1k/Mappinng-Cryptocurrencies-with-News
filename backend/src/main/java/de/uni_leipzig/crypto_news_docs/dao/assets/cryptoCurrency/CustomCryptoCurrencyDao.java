package de.uni_leipzig.crypto_news_docs.dao.assets.cryptoCurrency;

import de.uni_leipzig.crypto_news_docs.dto.crypto.response.CryptoCurrencyCountry;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrency;
import java.util.Date;
import java.util.List;
import org.springframework.data.repository.query.Param;

public interface CustomCryptoCurrencyDao {

	CryptoCurrency findByShortNameAndTimeSpan(String shortName, String unit, Date startDate, Date endDate);

	CryptoCurrencyCountry getCryptoCurrencyAVGByCountry(String cryptoName);
}
