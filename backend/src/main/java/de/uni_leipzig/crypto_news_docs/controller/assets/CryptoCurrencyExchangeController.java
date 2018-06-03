package de.uni_leipzig.crypto_news_docs.controller.assets;

import de.uni_leipzig.crypto_news_docs.dto.crypto.create.CryptoCurrencyCreateDto;
import de.uni_leipzig.crypto_news_docs.dto.crypto.create.CryptoCurrencyExchangeInitCreateDto;
import de.uni_leipzig.crypto_news_docs.dto.crypto.response.CryptoCurrencyDto;
import de.uni_leipzig.crypto_news_docs.dto.crypto.response.CryptoCurrencyExchangeDto;
import de.uni_leipzig.crypto_news_docs.dto.crypto.create.CryptoCurrencyExchangeCreateDto;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrency;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrencyExchange;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrencyPrice;
import de.uni_leipzig.crypto_news_docs.service.assets.cryptoCurrency.CryptoCurrencyExchangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * CryptoCurrencyExchange Controller
 */
@Api(description = "Exchange erstellen oder Exchange Daten auslesen.")
@RestController
public class CryptoCurrencyExchangeController {

	@Autowired
	CryptoCurrencyExchangeService cryptoCurrencyExchangeService;

	@Autowired
	private ModelMapper modelMapper;

	@ApiOperation(value = "CryptoCurrencyExchange", notes = "Alle Rückgabe Relevante Daten eines Exchange.", response = CryptoCurrencyExchangeDto.class)
	@RequestMapping(method= RequestMethod.GET, path="/assets/crypto-currency-exchange", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getCryptoCurrencyExchangeByName(@RequestParam("exchange")
			String exchange)
	{
		CryptoCurrencyExchange cryptoCurrencyExchange = cryptoCurrencyExchangeService.findByExchangeName(exchange);

		if (cryptoCurrencyExchange == null) {
			return new ResponseEntity<Object>(null, HttpStatus.NOT_FOUND);
		}

		CryptoCurrencyExchangeDto cryptoCurrencyExchangeDto = convertToDto(cryptoCurrencyExchange);

		return new ResponseEntity<Object>(cryptoCurrencyExchangeDto, HttpStatus.OK);
	}



	@ApiOperation(value = "Post Exchange", notes = "Ersten nur eines Exchange ohne Angaben von Cryptowährungen.", response = ResponseEntity.class)
	@RequestMapping(method = RequestMethod.POST, path = "/assets/crypto-currency-exchange")
	public ResponseEntity<Object> createCryptoCurrencyExchange(@RequestBody
	//CryptoCurrencyExchangeDto cryptoCurrencyExchangeDto
	@Valid CryptoCurrencyExchangeCreateDto cryptoCurrencyExchangeCreateDto
	) {

		if (cryptoCurrencyExchangeCreateDto == null) {
			return new ResponseEntity<Object>("object not valide", HttpStatus.INTERNAL_SERVER_ERROR);
		}


		CryptoCurrencyExchange cryptoCurrencyExchange = convertToEntity2(cryptoCurrencyExchangeCreateDto);
		CryptoCurrencyExchange postCreated = cryptoCurrencyExchangeService.create(cryptoCurrencyExchange);

		if (postCreated == null) {
			return new ResponseEntity<Object>("Not saved object", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Object>("Saved object", HttpStatus.CREATED);

	}

	@ApiOperation(value = "Post Exchange-Init", notes = "Anlegen des ersten Exchange mit Cryptowährung und Länderwährungstyp.", response = ResponseEntity.class)
	@RequestMapping(method = RequestMethod.POST, path = "/assets/crypto-currency-exchange-complete")
	public ResponseEntity<Object> createCryptoCurrencyExchangeComplete(@RequestBody
	//CryptoCurrencyExchangeDto cryptoCurrencyExchangeDto
	@Valid CryptoCurrencyExchangeInitCreateDto exchangeList
	) {

		if (exchangeList == null) {
			return new ResponseEntity<Object>("object not valide", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		for (CryptoCurrencyCreateDto cryptoCurrencyCreateDtoDto : exchangeList.getCryptoCurrencies()) {
			if (cryptoCurrencyCreateDtoDto.getDescription().getWikiEnglish().length() > 10000 ||
					cryptoCurrencyCreateDtoDto.getDescription().getWikiGerman().length() > 1000) {
				return new ResponseEntity<Object>("object not valide; Wiki to long", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		CryptoCurrencyExchange cryptoCurrencyExchange = convertToEntity(exchangeList);

		CryptoCurrencyExchange postCreated = cryptoCurrencyExchangeService.createWithCryptoCurrenciesAndPrices(cryptoCurrencyExchange, exchangeList);

		if (postCreated == null) {
			return new ResponseEntity<Object>("Not saved object", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Object>("Saved init-object", HttpStatus.CREATED);

	}



	private CryptoCurrencyExchangeDto convertToDto(CryptoCurrencyExchange cryptoCurrencyExchange) {
		return modelMapper.map(cryptoCurrencyExchange, CryptoCurrencyExchangeDto.class);
	}

	private CryptoCurrencyExchange convertToEntity(CryptoCurrencyExchangeInitCreateDto cryptoCurrencyExchangeInitCreateDto)  {
		return modelMapper.map(cryptoCurrencyExchangeInitCreateDto, CryptoCurrencyExchange.class);
	}

	private CryptoCurrencyExchange convertToEntity2(CryptoCurrencyExchangeCreateDto cryptoCurrencyExchangeCreateDto)  {
		return modelMapper.map(cryptoCurrencyExchangeCreateDto, CryptoCurrencyExchange.class);
	}

}
