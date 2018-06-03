package de.uni_leipzig.crypto_news_docs.service.assets.cryptoCurrency;

import de.uni_leipzig.crypto_news_docs.dao.assets.cryptoCurrency.CryptoCurrencyDescriptionDao;
import de.uni_leipzig.crypto_news_docs.dao.assets.cryptoCurrency.CryptoCurrencyExchangeDao;
import de.uni_leipzig.crypto_news_docs.dto.crypto.create.CryptoCurrencyExchangeInitCreateDto;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrencyDescription;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrencyExchange;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrencyPrice;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CryptoCurrencyExchangeServiceImpl implements CryptoCurrencyExchangeService {


	@Autowired
	private CryptoCurrencyExchangeDao cryptoCurrencyExchangeDao;

	@Autowired
	private CryptoCurrencyDescriptionDao cryptoCurrencyDescriptionDao;

	private void checkEmptyO(CryptoCurrencyExchange o) {
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
	public CryptoCurrencyExchange findByExchangeName(String name) {
		checkEmptyName(name);
		return cryptoCurrencyExchangeDao.findByExchangeName(name);
	}

	@Override
	public CryptoCurrencyExchange create(CryptoCurrencyExchange cryptoCurrencyExchange) {
		checkEmptyO(cryptoCurrencyExchange);

		return cryptoCurrencyExchangeDao.save(cryptoCurrencyExchange);
	}



	@Override
	public CryptoCurrencyExchange update(CryptoCurrencyExchange cryptoCurrencyExchange) {
		checkEmptyO(cryptoCurrencyExchange);

		cryptoCurrencyExchange = addCryptoCurrencyDiscription(cryptoCurrencyExchange);

		return cryptoCurrencyExchangeDao.save(cryptoCurrencyExchange);
	}

	@Override
	public CryptoCurrencyExchange createWithCryptoCurrenciesAndPrices(CryptoCurrencyExchange cryptoCurrencyExchange,
			CryptoCurrencyExchangeInitCreateDto cryptoCurrencyExchangeInitCreateDto) {
		checkEmptyO(cryptoCurrencyExchange);

		cryptoCurrencyExchange = addCryptoCurrencyDiscription(cryptoCurrencyExchange);

		CryptoCurrencyExchange postCreated = cryptoCurrencyExchangeDao.save(cryptoCurrencyExchange);
		if (saveCurrencies(cryptoCurrencyExchangeInitCreateDto, cryptoCurrencyExchange, postCreated)) {
			return null;
		}
		return postCreated;
	}

	private boolean saveCurrencies(
			CryptoCurrencyExchangeInitCreateDto cryptoCurrencyExchangeInitCreateDto,
			CryptoCurrencyExchange cryptoCurrencyExchange, CryptoCurrencyExchange postCreated) {

		for (int i=0; i < cryptoCurrencyExchange.getCryptoCurrencies().size(); i++) {
			CryptoCurrencyPrice cryptoCurrencyPrice = new CryptoCurrencyPrice(cryptoCurrencyExchangeInitCreateDto.getCryptoCurrencies().get(i).getUnit(), null);
			List<CryptoCurrencyPrice> list = new ArrayList<>();
			list.add(cryptoCurrencyPrice);
			postCreated.getCryptoCurrencyPrices().addAll(list);
			postCreated.getCryptoCurrencies().get(i).setCryptoCurrencyPrices(list);
			postCreated = update(postCreated);

			if (postCreated == null) {
				return true;
			}
		}
		return false;
	}

	private CryptoCurrencyExchange addCryptoCurrencyDiscription(CryptoCurrencyExchange cryptoCurrencyExchange) {
		for (int i = 0; i < cryptoCurrencyExchange.getCryptoCurrencies().size(); i++) {
			CryptoCurrencyDescription cryptoCurrencyDescription = cryptoCurrencyDescriptionDao
					.findByShortName(cryptoCurrencyExchange.getCryptoCurrencies().get(i).getShortName());
			if (cryptoCurrencyDescription != null) {
				cryptoCurrencyExchange.getCryptoCurrencies().get(i).setDescription(cryptoCurrencyDescription);
			}
		}
		return cryptoCurrencyExchange;
	}
}
