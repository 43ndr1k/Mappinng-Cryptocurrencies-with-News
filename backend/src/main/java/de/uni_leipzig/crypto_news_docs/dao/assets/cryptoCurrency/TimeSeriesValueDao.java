package de.uni_leipzig.crypto_news_docs.dao.assets.cryptoCurrency;

import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrency;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.TimeSeriesValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSeriesValueDao extends JpaRepository<TimeSeriesValue, Long> {



}
