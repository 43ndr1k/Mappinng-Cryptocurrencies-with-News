package de.uni_leipzig.crypto_news_docs.dao.assets.countryCurrency;

import de.uni_leipzig.crypto_news_docs.model.assets.currency.country.CountryCurrency;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.country.CountryCurrencyExchange;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryCurrencyExchangeDao extends JpaRepository<CountryCurrencyExchange, Long> {

	List<CountryCurrencyExchange> findByShortName(String name);
	CountryCurrencyExchange findByExchangeAndShortNameAndUnit(String exchange, String shortName, String unit);
}
