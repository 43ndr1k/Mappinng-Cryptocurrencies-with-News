package de.uni_leipzig.crypto_news_docs.service.assets.cryptoCurrency;

import de.uni_leipzig.crypto_news_docs.dao.assets.cryptoCurrency.CryptoCurrencyDao;
import de.uni_leipzig.crypto_news_docs.dao.assets.cryptoCurrency.CryptoCurrencyDescriptionDao;
import de.uni_leipzig.crypto_news_docs.dao.assets.cryptoCurrency.CustomCryptoCurrencyDao;
import de.uni_leipzig.crypto_news_docs.dto.crypto.response.CryptoCurrencyCountry;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrency;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrencyDescription;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.TimeSeriesValue;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {


	@Autowired
	private CryptoCurrencyDao cryptoCurrencyDao;

	@Autowired
	private CustomCryptoCurrencyDao customCryptoCurrencyDao;

	@Autowired
	private CryptoCurrencyDescriptionDao cryptoCurrencyDescriptionDao;


	private void checkEmptyO(CryptoCurrency cryptoCurrency) {
		if (cryptoCurrency== null) {
			throw new RuntimeException();
		}
	}

	private void checkEmptyName(String name) {
		if (name.equals("")) {
			throw new RuntimeException();
		}
	}

	@Override
	public List<CryptoCurrency> findByShortName(String shortName) {
		checkEmptyName(shortName);
		List<CryptoCurrency> list = cryptoCurrencyDao.findByShortName(shortName);

		if (list.size() != 0) {
			CryptoCurrency cryptoCurrency = list.get(0);
			List<TimeSeriesValue> timeSeriesValues = cryptoCurrency.getCryptoCurrencyPrices().get(0)
					.getTimeSeriesValues();
			//if (timeSeriesValues.size() != 0) {
				for (int i = 1; i < list.size(); i++) {
					timeSeriesValues.addAll(list.get(i).getCryptoCurrencyPrices().get(0).getTimeSeriesValues());
				}
				cryptoCurrency.getCryptoCurrencyPrices().get(0).setTimeSeriesValues(timeSeriesValues);

				list.clear();
				list.add(calculateAverageTimeSeriesValue(cryptoCurrency));
			/*} else {
				list.clear();
			}*/
		}
		return list;
	}

	@Override
	public CryptoCurrency create(CryptoCurrency cryptoCurrency) {
		checkEmptyO(cryptoCurrency);
		CryptoCurrencyDescription cryptoCurrencyDescription = cryptoCurrencyDescriptionDao.findByShortName(cryptoCurrency.getShortName());
		if (cryptoCurrencyDescription != null) {
			cryptoCurrency.setDescription(cryptoCurrencyDescription);
		}
		return cryptoCurrencyDao.save(cryptoCurrency);
	}

	@Override
	public CryptoCurrency findByShortNameAndTimeSpan(String shortName, String unit, Date startDate, Date endDate) {
		CryptoCurrency cryptoCurrency = customCryptoCurrencyDao.findByShortNameAndTimeSpan(shortName, unit, startDate, endDate);

		if (cryptoCurrency != null && cryptoCurrency.getCryptoCurrencyPrices().get(0).getTimeSeriesValues().size() != 0) {
			calculateAverageTimeSeriesValue(cryptoCurrency);
		}

		return cryptoCurrency;
	}

	@Override
	public CryptoCurrencyCountry getCryptoCurrencyAVGByCountry(String cryptoName) {
		checkEmptyName(cryptoName);
		return customCryptoCurrencyDao.getCryptoCurrencyAVGByCountry(cryptoName);
	}

	/**
	 * Join together several timesSeriesValues to a NavigableMap.
	 * @param cryptoCurrency CryptoCurrency
	 * @return CryptoCurrency
	 */
	private CryptoCurrency calculateAverageTimeSeriesValue(CryptoCurrency cryptoCurrency) {

		NavigableMap<Date, List<Double>> navigableMap = new TreeMap<>();

		for (TimeSeriesValue timeSeriesValue : cryptoCurrency.getCryptoCurrencyPrices().get(0).getTimeSeriesValues()) {
			List<Double> list = new ArrayList<>();
			if (navigableMap.get(timeSeriesValue.getDate()) !=  null) {
				navigableMap.get(timeSeriesValue.getDate()).add(timeSeriesValue.getValue());
			} else {
				list.add(timeSeriesValue.getValue());
				navigableMap.put(timeSeriesValue.getDate(), list);
			}
		}

		cryptoCurrency.getCryptoCurrencyPrices().get(0).getTimeSeriesValues().clear();

		final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
		final int TIME_INTERVAL = 24*60;

		Date from = navigableMap.firstKey();

		for (int i = 0; i < navigableMap.size(); i++) {
			long curTimeInMs = from.getTime();
			Date to = new Date(curTimeInMs + (TIME_INTERVAL * ONE_MINUTE_IN_MILLIS));
			SortedMap<Date, List<Double>> sortedMap = navigableMap.subMap(from, to);
			from = to;

			if (sortedMap.size() != 0) {
				BigInteger total = BigInteger.ZERO;
				for (Date date1 : sortedMap.keySet()) {
					total = total.add(BigInteger.valueOf(date1.getTime()));
				}
				BigInteger averageMillis = total.divide(BigInteger.valueOf(sortedMap.size()));
				Date date = new Date(averageMillis.longValue());

				Double value = 0.0;
				for (List<Double> list : sortedMap.values()) {
					for (Double d : list) {
						value += d;
					}
					value = value / list.size();
				}
				value = value / sortedMap.size();

				cryptoCurrency.getCryptoCurrencyPrices().get(0).getTimeSeriesValues()
						.add(new TimeSeriesValue(date, value));
			}
		}

		return cryptoCurrency;
	}

}
