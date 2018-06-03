package de.uni_leipzig.crypto_news_docs.dao.assets.cryptoCurrency;

import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrency;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CryptoCurrencyDao extends JpaRepository<CryptoCurrency, Long> {

	List<CryptoCurrency> findByShortName(String shortName);

}
