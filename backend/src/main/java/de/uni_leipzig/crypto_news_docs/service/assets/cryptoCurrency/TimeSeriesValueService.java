package de.uni_leipzig.crypto_news_docs.service.assets.cryptoCurrency;

import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.TimeSeriesValue;

public interface TimeSeriesValueService {

	TimeSeriesValue create(TimeSeriesValue timeSeriesValue);
	Iterable<TimeSeriesValue> createFromList(Iterable<TimeSeriesValue> timeSeriesValues);
}
