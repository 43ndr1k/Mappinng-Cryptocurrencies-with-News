package de.uni_leipzig.crypto_news_docs.service.assets.cryptoCurrency;

import de.uni_leipzig.crypto_news_docs.dao.assets.cryptoCurrency.TimeSeriesValueDao;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.TimeSeriesValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeSeriesValueServiceImpl implements TimeSeriesValueService {

	@Autowired
	TimeSeriesValueDao timeSeriesValueDao;

	private void checkEmptyO(Object o) {
		if (o== null) {
			throw new RuntimeException();
		}
	}

	private void checkEmptyName(String name) {
		if (name.equals("")) {
			throw new RuntimeException();
		}
	}

	@Override
	public TimeSeriesValue create(TimeSeriesValue timeSeriesValue) {
		checkEmptyO(timeSeriesValue);
		return timeSeriesValueDao.save(timeSeriesValue);
	}

	@Override
	public Iterable<TimeSeriesValue> createFromList(Iterable<TimeSeriesValue> timeSeriesValues) {
		checkEmptyO(timeSeriesValues);
		return timeSeriesValueDao.save(timeSeriesValues);
	}
}
