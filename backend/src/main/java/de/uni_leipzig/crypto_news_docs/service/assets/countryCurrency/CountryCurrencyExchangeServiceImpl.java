package de.uni_leipzig.crypto_news_docs.service.assets.countryCurrency;

import de.uni_leipzig.crypto_news_docs.dao.assets.countryCurrency.CountryCurrencyExchangeDao;
import de.uni_leipzig.crypto_news_docs.dao.assets.countryCurrency.CustomCountryCurrencyExchangeDao;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.country.CountryCurrency;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.country.CountryCurrencyExchange;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryCurrencyExchangeServiceImpl implements CountryCurrencyExchangeService {


	@Autowired
	private CountryCurrencyExchangeDao countryCurrencyExchangeDao;

	@Autowired
	private CustomCountryCurrencyExchangeDao customCountryCurrencyExchangeDao;

	private void checkEmptyDate(Date date) {
		if (date== null) {
			throw new RuntimeException();
		}
	}

	private void checkEmptyName(String name) {
		if (name.equals("")) {
			throw new RuntimeException();
		}
	}

	@Override
	public List<CountryCurrencyExchange> findByShortName(String name) {
		checkEmptyName(name);
		return countryCurrencyExchangeDao.findByShortName(name);
	}

	@Override
	public CountryCurrencyExchange findByExchangeAndShortNameAndUnit(String exchange, String shortName, String unit) {
		checkEmptyName(exchange);
		return countryCurrencyExchangeDao.findByExchangeAndShortNameAndUnit(exchange, shortName, unit);
	}

	@Override
	public CountryCurrencyExchange create(List<CountryCurrency> list, String exchange, String shortName,
			String unit) {
		if (list.size() == 0 || exchange.equals("") || shortName.equals("") || unit.equals("")) {
			throw new RuntimeException();
		}

		CountryCurrencyExchange countryCurrencyExchange = findByExchangeAndShortNameAndUnit(exchange, shortName,
						unit);

		if (countryCurrencyExchange == null) {
			countryCurrencyExchange = new CountryCurrencyExchange();
			countryCurrencyExchange.setExchange(exchange);
			countryCurrencyExchange.setShortName(shortName);
			countryCurrencyExchange.setUnit(unit);
			countryCurrencyExchange.setTimeSeriesValues(list);

		} else {
			countryCurrencyExchange.getTimeSeriesValues().addAll(list);
		}

			return countryCurrencyExchangeDao.save(countryCurrencyExchange);

	}

	@Override
	public List<CountryCurrencyExchange> findByShortNameAndTimeSpan(String shortName, Date startTime, Date endTime) {
		checkEmptyName(shortName);
		checkEmptyDate(startTime);
		checkEmptyDate(endTime);
		return customCountryCurrencyExchangeDao.findByShortNameAndTimeSpan(shortName, startTime, endTime);
	}

}
