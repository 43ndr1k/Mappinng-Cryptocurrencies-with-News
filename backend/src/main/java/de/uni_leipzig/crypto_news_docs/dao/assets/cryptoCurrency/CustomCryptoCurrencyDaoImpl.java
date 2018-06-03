package de.uni_leipzig.crypto_news_docs.dao.assets.cryptoCurrency;

import de.uni_leipzig.crypto_news_docs.dto.crypto.response.CryptoCurrencyCountry;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrency;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.TimeSeriesValue;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CustomCryptoCurrencyDaoImpl implements CustomCryptoCurrencyDao {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Find a CryptoCurrency with a Name and a timespan.
	 * @param shortName String
	 * @param unit String
	 * @param startDate Date
	 * @param endDate Date
	 * @return CryptoCurrency
	 */
	@Override
	public CryptoCurrency findByShortNameAndTimeSpan(String shortName, String unit, Date startDate, Date endDate) {
		final Query query = this.entityManager.createQuery(

				"SELECT cr, tsv "
						+ "from  "
						+ "CryptoCurrency cr "
						+ "join cr.cryptoCurrencyPrices ccp "
						+ "join ccp.timeSeriesValues tsv "
						+ "WHERE "
						+ "cr.shortName = ?1 "
						+ "and ccp.unit = ?4 "
						+ "and tsv.date BETWEEN ?2 and ?3"
				);
		query.setParameter(1, shortName);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);
		query.setParameter(4, unit);

		List<Object[]> l = query.getResultList();
		CryptoCurrency cryptoCurrency = null;
		if (l.size() != 0) {
			Object[] object = l.get(0);
			cryptoCurrency = (CryptoCurrency) object[0];

			List<TimeSeriesValue> timeSeriesValueList = new ArrayList<>();
			for (Object[] obj : l) {
				timeSeriesValueList.add((TimeSeriesValue) obj[1]);
			}
			assert cryptoCurrency != null;
			cryptoCurrency.getCryptoCurrencyPrices().get(0).setTimeSeriesValues(timeSeriesValueList);
		}
		return cryptoCurrency;
	}

	/**
	 * Find a CryptoCurrencyCountry AVG by a Name.
	 * @param cryptoName String
	 * @return
	 */
	@Override
	public CryptoCurrencyCountry getCryptoCurrencyAVGByCountry(String cryptoName) {

		final Query query = this.entityManager.createQuery(
		"SELECT cce.position, ccp.unit, avg(coalesce(tsv.value,0)) "
				+ "from  "
				+ "CryptoCurrencyExchange cce "
				+ "join cce.cryptoCurrencies cr "
				+ "join cr.cryptoCurrencyPrices ccp "
				+ "join ccp.timeSeriesValues tsv "
				+ "WHERE "
				+ "cr.shortName = ?1 "
				+ "and tsv.date >= CURRENT_DATE - 30 "
				+ "group by cce.position, ccp.unit "

				);

		query.setParameter(1, cryptoName);

		List<Object[]> l = query.getResultList();

		CryptoCurrencyCountry cryptoCurrencyCountry = new CryptoCurrencyCountry();
		if (l.size() != 0) {
			cryptoCurrencyCountry.setUnit(l.get(0)[1].toString());
			Map<String, Double> map = new HashMap<>();
			for (Object[] o : l) {
				map.put(o[0].toString(), Double.valueOf(o[2].toString()));
				cryptoCurrencyCountry.setData(map);
			}

		} else {
			return null;
		}
		return cryptoCurrencyCountry;
	}
}

/*
select cr.short_name, ti.date, ti.value
from crypto_currencies as cr, time_series_values as ti, crypto_currencies_price as pi
WHERE  cr.short_name = 'dash' and cr.id =pi.crypto_currency_id
and cr.crypto_currency_exchange_id = pi.crypto_currency_exchange_id
and pi.id = ti.crypto_currency_price_id
and ti.date BETWEEN '2017-11-19 17:30:20' and '2017-11-19 17:36:20';
*/
