package de.uni_leipzig.crypto_news_docs.controller.assets;

import de.uni_leipzig.crypto_news_docs.dto.crypto.response.CryptoCurrencyCountry;
import de.uni_leipzig.crypto_news_docs.dto.crypto.response.CryptoCurrencyCountryDto;
import de.uni_leipzig.crypto_news_docs.dto.crypto.response.CryptoCurrencyDto;
import de.uni_leipzig.crypto_news_docs.dto.crypto.create.CryptoCurrencyCreateDto;
import de.uni_leipzig.crypto_news_docs.dto.crypto.response.CryptoCurrencyExchangeDto;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrency;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrencyExchange;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrencyPrice;
import de.uni_leipzig.crypto_news_docs.service.assets.cryptoCurrency.CryptoCurrencyExchangeService;
import de.uni_leipzig.crypto_news_docs.service.assets.cryptoCurrency.CryptoCurrencyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Cryptowährung erstellen oder Cryptowährungsdaten auslesen.")
@RestController
public class CryptoCurrencyController {

	@Autowired
	CryptoCurrencyService cryptoCurrencyService;

	@Autowired
	CryptoCurrencyExchangeService cryptoCurrencyExchangeService;

	@Autowired
	private ModelMapper modelMapper;

	@ApiOperation(value = "Alle Werte einer Cryptowährung", notes = "Suche nach einer Cryptowähung", response = ResponseEntity.class)
	@RequestMapping(method= RequestMethod.GET, path="/assets/crypto-currency", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getAllCryptoCurrencyByName(@RequestParam("cryptoName") String cryptoName)
	{
		List<CryptoCurrency> cryptoCurrency = cryptoCurrencyService.findByShortName(cryptoName);

		if (cryptoCurrency.size() == 0) {
			return new ResponseEntity("not founded", HttpStatus.NOT_FOUND);
		}

		List<CryptoCurrencyDto> list = cryptoCurrency.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
		return new ResponseEntity<Object>(list, HttpStatus.OK);
	}

	@ApiOperation(value = "Alle Werte einer Cryptowährung", notes = "Rückgabewert ist ein Liste der Values und oder ein Http Status Code.", response = ResponseEntity.class)
	@RequestMapping(method= RequestMethod.GET, path="/assets/crypto-currency-list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getAllCryptoCurrencyByNameAndTimeSpan(
			@RequestParam("cryptoName") String cryptoName,
			@RequestParam(value = "unit", defaultValue = "usd") String unit,
			@RequestParam @DateTimeFormat (pattern= "yyyy-MM-dd HH:mm:ss") Date startTime,
			@RequestParam @DateTimeFormat (pattern= "yyyy-MM-dd HH:mm:ss") Date endTime
	)
	{
		CryptoCurrency cryptoCurrency = cryptoCurrencyService.findByShortNameAndTimeSpan(cryptoName, unit, startTime, endTime);

		if (cryptoCurrency == null) {
			new ResponseEntity<Object>("not founded", HttpStatus.NOT_FOUND);
		}

		CryptoCurrencyDto cryptoCurrencyDto = convertToDto(cryptoCurrency);

		List<CryptoCurrencyDto> list = new ArrayList<>();
		list.add(cryptoCurrencyDto);

		return new ResponseEntity(list, HttpStatus.OK);
	}


	@ApiOperation(value = "CryptoCurrencies sotiert nach Länder.", notes = "Länder bezifischen Daten.", response = CryptoCurrencyCountryDto.class)
	@RequestMapping(method= RequestMethod.GET, path="/assets/crypto-currency-sort-by-country", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getCryptoCurrencyAVGByCountry(@RequestParam("cryptoName") String cryptoName)
	{

		CryptoCurrencyCountry cryptoCurrencyCountry = cryptoCurrencyService.getCryptoCurrencyAVGByCountry(cryptoName);

		if (cryptoCurrencyCountry == null) {
			return new ResponseEntity<Object>(null, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Object>(convertToDto2(cryptoCurrencyCountry), HttpStatus.OK);
	}

	@ApiOperation(value = "Post einer Cryptowährung", notes = "Anlegen einer einzelden Währung.", response = ResponseEntity.class)
	@RequestMapping(method = RequestMethod.POST, path = "/assets/crypto-currency")
	public ResponseEntity createCryptoCurrency(@RequestBody
			@Valid CryptoCurrencyCreateDto cryptoCurrencyCreateDto) {

		if (cryptoCurrencyCreateDto == null) {
			return new ResponseEntity("object not valide", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (cryptoCurrencyCreateDto.getDescription().getWikiEnglish().length() > 10000 ||
				cryptoCurrencyCreateDto.getDescription().getWikiGerman().length() > 1000) {
			return new ResponseEntity<Object>("object not valide; Wiki to long", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		CryptoCurrencyPrice cryptoCurrencyPrice = new CryptoCurrencyPrice(cryptoCurrencyCreateDto.getUnit(), null);
		CryptoCurrency cryptoCurrency = convertToEntity(cryptoCurrencyCreateDto);
		List<CryptoCurrencyPrice> list = new ArrayList<>();
		list.add(cryptoCurrencyPrice);
		cryptoCurrency.setCryptoCurrencyPrices(list);

		CryptoCurrencyExchange cryptoCurrencyExchange = cryptoCurrencyExchangeService.findByExchangeName(cryptoCurrencyCreateDto.getExchange());

		if (cryptoCurrencyExchange == null) {
			return new ResponseEntity("not founded exchange", HttpStatus.NOT_FOUND);
		}

		for (CryptoCurrency cryptoCurrency1: cryptoCurrencyExchange.getCryptoCurrencies()) {
			if (cryptoCurrency1.getShortName().equals(cryptoCurrency.getShortName())) {
				return new ResponseEntity("Cryptocurrency "+ cryptoCurrency1.getShortName() + " already exists", HttpStatus.FOUND);
			}
		}
		cryptoCurrencyExchange.getCryptoCurrencyPrices().add(cryptoCurrencyPrice);
		cryptoCurrencyExchange.getCryptoCurrencies().add(cryptoCurrency);
		CryptoCurrencyExchange postCreated = cryptoCurrencyExchangeService.update(cryptoCurrencyExchange);

		if (postCreated == null) {
			return new ResponseEntity("Not saved object", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity("Saved object", HttpStatus.CREATED);

	}

	private CryptoCurrencyDto convertToDto(CryptoCurrency cryptoCurrency) {
		return modelMapper.map(cryptoCurrency, CryptoCurrencyDto.class);
	}

	private CryptoCurrency convertToEntity(CryptoCurrencyCreateDto cryptoCurrencyCreateDto)  {
		return modelMapper.map(cryptoCurrencyCreateDto, CryptoCurrency.class);
	}

	private CryptoCurrencyCountryDto convertToDto2(CryptoCurrencyCountry cryptoCurrencyCountry) {
		return modelMapper.map(cryptoCurrencyCountry, CryptoCurrencyCountryDto.class);
	}

}
