package de.uni_leipzig.crypto_news_docs.service.assets.cryptoCurrency;

import de.uni_leipzig.crypto_news_docs.dao.assets.cryptoCurrency.CryptoCurrencyPriceDao;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrencyPrice;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CryptoCurrencyPriceServiceImpl implements CryptoCurrencyPriceService
{
	@Autowired
	CryptoCurrencyPriceDao cryptoCurrencyPriceDao;

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
	public CryptoCurrencyPrice findByUnit(String unit) {
		checkEmptyName(unit);
		return cryptoCurrencyPriceDao.findByUnit(unit);
	}

	@Override
	public CryptoCurrencyPrice create(CryptoCurrencyPrice cryptoCurrencyPrice) {
		checkEmptyO(cryptoCurrencyPrice);
		return cryptoCurrencyPriceDao.save(cryptoCurrencyPrice);
	}

	@Override
	public List<CryptoCurrencyPrice> findAllPrices() {
		return cryptoCurrencyPriceDao.findAll();
	}
}
