package de.uni_leipzig.crypto_news_docs.dao.assets.cryptoCurrency;

import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrencyDescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoCurrencyDescriptionDao extends JpaRepository<CryptoCurrencyDescription, Long> {

	CryptoCurrencyDescription findByShortName(String shortName);

}
