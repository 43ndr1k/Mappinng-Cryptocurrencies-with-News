package de.uni_leipzig.crypto_news_docs.controller.assets;


import de.uni_leipzig.crypto_news_docs.dto.crypto.create.TimeSeriesValuesCreateAsListDto;
import de.uni_leipzig.crypto_news_docs.dto.crypto.response.CryptoCurrencyDto;
import de.uni_leipzig.crypto_news_docs.dto.crypto.response.TimeSeriesValueDto;
import de.uni_leipzig.crypto_news_docs.dto.crypto.create.TimeSeriesValueCreateDto;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrency;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrencyExchange;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrencyPrice;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.TimeSeriesValue;
import de.uni_leipzig.crypto_news_docs.service.assets.cryptoCurrency.CryptoCurrencyExchangeService;
import de.uni_leipzig.crypto_news_docs.service.assets.cryptoCurrency.TimeSeriesValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Speichern von TimeSeriesValues zu einer Cryptowährung.")
@RestController
public class CryptoCurrencyTimeSeriesValueController {

	@Autowired
	TimeSeriesValueService timeSeriesValueService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CryptoCurrencyExchangeService cryptoCurrencyExchangeService;

	@ApiOperation(value = "Post einer TimeSeriesValue Liste eines Exchange", notes = "Speichern von TimeSeriesValues, welche zu einem Exchane gehören.", response = ResponseEntity.class)
	@RequestMapping(method = RequestMethod.POST, path = "/assets/time-series-value")
	public ResponseEntity<Object> createTimeSeriesValue(@RequestBody
			@Valid TimeSeriesValuesCreateAsListDto timeSeriesValues) {

		if (timeSeriesValues == null) {
			return new ResponseEntity<Object>("object not valide", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		CryptoCurrencyExchange cryptoCurrencyExchange = cryptoCurrencyExchangeService.findByExchangeName(timeSeriesValues.getExchange());

		if (cryptoCurrencyExchange == null) {
			return new ResponseEntity<Object>("Not saved object because exchange not found", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		cryptoCurrencyExchange = saveTimeServiesValues(timeSeriesValues, cryptoCurrencyExchange);
		CryptoCurrencyExchange postCreated = cryptoCurrencyExchangeService.update(cryptoCurrencyExchange);

		if (postCreated == null) {
			return new ResponseEntity<Object>("Not saved object", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Object>("Saved objects", HttpStatus.CREATED);

	}

	private CryptoCurrencyExchange saveTimeServiesValues(
					TimeSeriesValuesCreateAsListDto timeSeriesValuesCreateAsListDto,
			CryptoCurrencyExchange cryptoCurrencyExchange) {
		List<TimeSeriesValue> list = timeSeriesValuesCreateAsListDto.getTimeSeriesValues().stream()
				.map(this::convertToEntity)
				.collect(Collectors.toList());

		for (CryptoCurrency cryptoCurrency : cryptoCurrencyExchange.getCryptoCurrencies()) {
			if (cryptoCurrency.getShortName().equals(timeSeriesValuesCreateAsListDto.getShortName())) {
				for (CryptoCurrencyPrice cryptoCurrencyPrice : cryptoCurrency.getCryptoCurrencyPrices()) {
					if (cryptoCurrencyPrice.getUnit().equals(timeSeriesValuesCreateAsListDto.getUnit())) {
						cryptoCurrencyPrice.getTimeSeriesValues().addAll(list);
					}
				}
			}
		}
		return cryptoCurrencyExchange;
	}

	private TimeSeriesValueDto convertToDto(TimeSeriesValue timeSeriesValue) {
		return modelMapper.map(timeSeriesValue, TimeSeriesValueDto.class);
	}

	private TimeSeriesValue convertToEntity(TimeSeriesValueCreateDto timeSeriesValueCreateDao)  {
		return modelMapper.map(timeSeriesValueCreateDao, TimeSeriesValue.class);
	}

}
