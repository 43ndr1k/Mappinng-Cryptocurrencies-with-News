package de.uni_leipzig.crypto_news_docs.controller.assets;

import de.uni_leipzig.crypto_news_docs.dto.country.CountryCurrencyDto;
import de.uni_leipzig.crypto_news_docs.dto.country.CountryCurrencyMapDto;
import de.uni_leipzig.crypto_news_docs.dto.country.CountryCurrencyMapOutDto;
import de.uni_leipzig.crypto_news_docs.dto.crypto.response.CryptoCurrencyDto;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.country.CountryCurrency;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.country.CountryCurrencyExchange;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrency;
import de.uni_leipzig.crypto_news_docs.service.assets.countryCurrency.CountryCurrencyExchangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * CountryCurrency Controller
 */
@Api(description = "Eine Länderwährung erstellen oder deren Daten auslesen.")
@RestController
public class CountryCurrencyController {


	@Autowired
	CountryCurrencyExchangeService countryCurrencyExchangeService;

	@Autowired
	private ModelMapper modelMapper;

	@ApiOperation(value = "Alle Werte einer Währung", notes = "Abfrage einer Länderwährung mit deren ShortName.", response = ResponseEntity.class)
	@RequestMapping(method= RequestMethod.GET, path="/assets/country-currency", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getAllCountryCurrencyByName(@RequestParam("shortName") String shortName)
	{
		if (!(shortName.equals("usd") || shortName.equals("eur"))) {
			return new ResponseEntity<Object>("Currency not acceptable", HttpStatus.NOT_ACCEPTABLE);
		}

		List<CountryCurrencyExchange> countryCurrencyExchange = countryCurrencyExchangeService.findByShortName(shortName);

		if (countryCurrencyExchange.size() == 0) {
			return new ResponseEntity("not founded", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<CountryCurrencyMapOutDto> list = countryCurrencyExchange.stream()
				.map(countryCurrencyExchange1 -> convertToOutDto(countryCurrencyExchange1))
				.collect(Collectors.toList());

/*		Map<String, List<CountryCurrencyOutDto>> map = new HashMap<>();
		map.put(shortName, list);*/

		return new ResponseEntity(list, HttpStatus.OK);
	}


	@ApiOperation(value = "Post einer Liste von Länderwährung Values", notes = "Speichern von TimeSeriesValues einer Länderwährung.", response = HttpStatus.class)
	@RequestMapping(method = RequestMethod.POST, path = "/assets/country-currency-list") //}, consumes = "application/json")
	public ResponseEntity createCryptoCurrencyFromList(@RequestBody
			@Valid CountryCurrencyMapDto countryCurrencyMapDto) {

		if (countryCurrencyMapDto == null) {
			return new ResponseEntity("object not valide", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<CountryCurrency> list = countryCurrencyMapDto.getTimeSeriesValues().stream()
				.map(this::convertToEntity)
				.collect(Collectors.toList());

		CountryCurrencyExchange postCreated = countryCurrencyExchangeService.create(list, countryCurrencyMapDto.getExchange(), countryCurrencyMapDto.getShortName(),
				countryCurrencyMapDto.getUnit());

		if (postCreated == null) {
			return new ResponseEntity("Not saved object", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body("saved");

	}

	@ApiOperation(value = "Alle Werte einer Währung in einer Zeitspanne", notes = "Abfrage einer Länderwährung mit deren ShortName und einer s Start und end Datum.", response = ResponseEntity.class)
	@RequestMapping(method= RequestMethod.GET, path="/assets/country-currency-list")
	public ResponseEntity<Object> getAllCountryCurrencyByNameAndTimeSpan(
			@RequestParam("shortName") String shortName,
			@RequestParam @DateTimeFormat(pattern= "yyyy-MM-dd HH:mm:ss")
					Date startTime,
			@RequestParam @DateTimeFormat (pattern= "yyyy-MM-dd HH:mm:ss") Date endTime
	)
	{
		List<CountryCurrencyExchange> countryCurrencyExchange = countryCurrencyExchangeService
				.findByShortNameAndTimeSpan(shortName, startTime, endTime);

		if (countryCurrencyExchange.size() == 0) {
			return new ResponseEntity("not founded", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<CountryCurrencyMapOutDto> list = countryCurrencyExchange.stream()
				.map(countryCurrencyExchange1 -> convertToOutDto(countryCurrencyExchange1))
				.collect(Collectors.toList());

		return new ResponseEntity(list, HttpStatus.OK);
	}

	private CountryCurrencyMapOutDto convertToOutDto(CountryCurrencyExchange countryCurrencyExchange) {
		return modelMapper.map(countryCurrencyExchange, CountryCurrencyMapOutDto.class);
	}

	private CountryCurrency convertToEntity(CountryCurrencyDto countryCurrencyDto) {
		return modelMapper.map(countryCurrencyDto, CountryCurrency.class);
	}

}
