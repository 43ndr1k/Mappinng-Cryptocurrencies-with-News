package de.uni_leipzig.crypto_news_docs.dao.assets.cryptoCurrency;

import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrencyPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoCurrencyPriceDao extends JpaRepository<CryptoCurrencyPrice, Long> {

	CryptoCurrencyPrice findByUnit(String unit);

}
