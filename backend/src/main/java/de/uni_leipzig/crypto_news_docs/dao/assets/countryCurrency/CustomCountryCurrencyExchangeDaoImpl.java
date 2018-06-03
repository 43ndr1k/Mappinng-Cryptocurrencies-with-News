package de.uni_leipzig.crypto_news_docs.dao.assets.countryCurrency;

import de.uni_leipzig.crypto_news_docs.dao.assets.cryptoCurrency.CustomCryptoCurrencyDao;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.country.CountryCurrency;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.country.CountryCurrencyExchange;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrency;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.TimeSeriesValue;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CustomCountryCurrencyExchangeDaoImpl implements CustomCountryCurrencyExchangeDao {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Find CountryCurrencyExchanges by a name and a time span with a start and a enddate.
	 * @param shortName String
	 * @param startDate Date
	 * @param endDate Date
	 * @return List<CountryCurrencyExchange>
	 */
	@Override
	public List<CountryCurrencyExchange> findByShortNameAndTimeSpan(String shortName, Date startDate, Date endDate) {
		final Query query = this.entityManager.createQuery(

				"SELECT cce, tsv "
						+ "from  "
						+ "CountryCurrencyExchange cce "
						+ "join cce.timeSeriesValues tsv "
						+ "WHERE "
						+ "cce.shortName = ?1 "
						+ "and tsv.date BETWEEN ?2 and ?3"
				);
		query.setParameter(1, shortName);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);


		List<Object[]> l = query.getResultList();
		List<CountryCurrencyExchange> list = new ArrayList<>();

		CountryCurrencyExchange countryCurrencyExchange = null;
		if (l.size() != 0) {
			Object[] object = l.get(0);
			countryCurrencyExchange = (CountryCurrencyExchange) object[0];

			List<CountryCurrency> timeSeriesValueList = new ArrayList<>();
			for (Object[] obj : l) {
				timeSeriesValueList.add((CountryCurrency) obj[1]);
			}
			assert countryCurrencyExchange != null;
			countryCurrencyExchange.setTimeSeriesValues(timeSeriesValueList);
		}
		list.add(countryCurrencyExchange);

		return list;
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
