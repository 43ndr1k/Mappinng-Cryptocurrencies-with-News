package de.uni_leipzig.crypto_news_docs.controller.assets;

import de.uni_leipzig.crypto_news_docs.dto.website.MetaInfoDto;
import de.uni_leipzig.crypto_news_docs.dto.website.WebsiteDto;
import de.uni_leipzig.crypto_news_docs.dto.website.WebsiteResponseDto;
import de.uni_leipzig.crypto_news_docs.model.assets.website.WebsiteData;
import de.uni_leipzig.crypto_news_docs.service.assets.website.WebsiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Website Controller
 */
@Api(description = "Speichern und Auslesen von Cypto Nachrichten.")
@RestController
public class WebsiteController {


	@Autowired
	WebsiteService websiteService;

	@Autowired
	private ModelMapper modelMapper;
	
	
  @ApiOperation(value = "Website-Objekte", notes = "Rückgabewert ist ein Liste der Values und oder ein Http Status Code.", response = WebsiteDto.class)
  @RequestMapping(method= RequestMethod.GET, path="/assets/websites-filtered-by-date")//), produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getWebsitesByDateBetween(
          @RequestParam("startDate") @DateTimeFormat (pattern= "yyyy-MM-dd HH:mm:ss") Date startDate,
          @RequestParam("endDate") @DateTimeFormat (pattern= "yyyy-MM-dd HH:mm:ss") Date endDate,
          Pageable pageable){//, Pageable pageable){ //public ResponseEntity<Object> getAllWebsites(@RequestParam("countryName") String countryName)

          //WebsiteData websiteData = websiteService.findByDateAndCurrency(currency, startDate, endDate);// findByDateAndCurrency(currency, date, pageable);
          Page<WebsiteData> websiteList = websiteService.findByDateBetween(startDate, endDate, pageable);
          
          if (websiteList == null) {
              new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
          }
          
          Page<WebsiteResponseDto> websiteDtoPage = websiteList.map(new Converter<WebsiteData, WebsiteResponseDto>() {
            @Override
            public WebsiteResponseDto convert(WebsiteData websiteData) {
                return modelMapper.map(websiteData, WebsiteResponseDto.class);
            }
          });
          

//          Page<WebsiteDto> websiteDtoPage = websiteList.map(new Converter<WebsiteData, WebsiteDto>() {
//            @Override
//            public WebsiteDto convert(WebsiteData websiteData) {
//                return modelMapper.map(websiteData, WebsiteDto.class);
//            }
//        });
          //List<WebsiteDto> list = new ArrayList<>();
          //list.add(websiteDto);
          return new ResponseEntity<Object>(websiteDtoPage, HttpStatus.OK);//new ResponseEntity(list, HttpStatus.OK);//new ResponseEntity<Object>(websiteDtoPage, HttpStatus.OK);
  }

	@ApiOperation(value = "Website-Objekte", notes = "Auslesen von allen gespeicherten Nachrichten.", response = Page.class)
	@RequestMapping(method= RequestMethod.GET, path="/assets/website", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getAllWebsites(Pageable pageable)
	{
		Page<WebsiteData> websiteList = websiteService.listAllByPage(pageable);

		if (websiteList.getSize() == 0) {
			return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Page<WebsiteResponseDto> websiteDtoPage = websiteList.map(new Converter<WebsiteData, WebsiteResponseDto>() {
			@Override
			public WebsiteResponseDto convert(WebsiteData websiteData) {
				return modelMapper.map(websiteData, WebsiteResponseDto.class);
			}
		});


		return new ResponseEntity<Object>(websiteDtoPage, HttpStatus.OK);
	}
	

	@ApiOperation(value = "Post einer Website", notes = "Speichern von eines Nachrichtenartikels.", response = ResponseEntity.class)
	@RequestMapping(method = RequestMethod.POST, path = "/assets/website")
	@ResponseBody
	public ResponseEntity<Object> createWebsite(@RequestBody WebsiteDto websiteDto) {

		if (websiteDto == null) {
			return new ResponseEntity<Object>("object not valide", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		cutWebsiteDtoFields(websiteDto);

		WebsiteData websiteData = convertToEntity(websiteDto);
		WebsiteData postCreated = websiteService.create(websiteData);

		if (postCreated == null) {
			return new ResponseEntity<Object>("Not saved object", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Object>("object saved", HttpStatus.CREATED);
	}
	
	   @ApiOperation(value = "PostList von Website-Daten", notes = "Speichern von Nachrichten zu Cryptowährungen.", response = ResponseEntity.class)
	    @RequestMapping(method = RequestMethod.POST, path = "/assets/website-data-list")
	    @ResponseBody
	    public ResponseEntity<Object> createWebsiteDataFromList(@RequestBody
	            List<WebsiteDto> websiteListDto) {

		   if (websiteListDto == null) {
			   return new ResponseEntity<Object>("object not valide", HttpStatus.INTERNAL_SERVER_ERROR);
		   }
		   
		   
		   String tmp;
		   MetaInfoDto metaTest = null;
		   for (WebsiteDto dto: websiteListDto) {
		     cutWebsiteDtoFields(dto);
		   }

	        List<WebsiteData> list = websiteListDto.stream()
	                .map(websiteDto -> convertToEntity(websiteDto))
	                .collect(Collectors.toList());


	        List<WebsiteDto> dtoList = new LinkedList<>();
	        for (WebsiteData website: list) {
	            WebsiteData postCreated = websiteService.create(website);
				if (postCreated == null) {
					return new ResponseEntity<Object>("Not saved object", HttpStatus.INTERNAL_SERVER_ERROR);
				}
	            dtoList.add(convertToDto(postCreated));
	        }

		   return new ResponseEntity<Object>("object saved", HttpStatus.CREATED);
	    }

    private void cutWebsiteDtoFields(WebsiteDto dto) {
      String tmp;
      MetaInfoDto metaTest;
      if (dto.getBody().length() > 10000)
         dto.setBody(dto.getBody().substring(0, 10000));
       if (dto.getSource().length() > 10000)
         dto.setSource(dto.getSource().substring(0, 10000));
       if (dto.getTitle().length() > 10000)
         dto.setTitle(dto.getTitle().substring(0, 10000));
       metaTest = dto.getMetaInfo();
       if (dto.getMetaInfo() != null) {
           if (dto.getMetaInfo().getAuthor() != null) {
        		     if (dto.getMetaInfo().getAuthor().length() > 10000) {
        		       tmp = dto.getMetaInfo().getAuthor().substring(0, 10000);
        		       dto.getMetaInfo().setAuthor(tmp);
        		     }  
      	     }
           if (dto.getMetaInfo().getKeywords() != null) {
        	         if (dto.getMetaInfo().getKeywords().length() > 10000 && dto.getMetaInfo() != null) {
        	               tmp = dto.getMetaInfo().getKeywords().substring(0, 10000);
        	               dto.getMetaInfo().setKeywords(tmp);
        	         }
           }
           if (dto.getMetaInfo().getLanguage() != null) {
        	         if (dto.getMetaInfo().getLanguage().length() > 10000 && dto.getMetaInfo() != null) {
                         tmp = dto.getMetaInfo().getLanguage().substring(0, 10000);
                         dto.getMetaInfo().setLanguage(tmp);
        	         }
           }
           if (dto.getMetaInfo().getPageTopic() != null) {
        	         if (dto.getMetaInfo().getPageTopic().length() > 10000 && dto.getMetaInfo() != null) {
        	                 tmp = dto.getMetaInfo().getPageTopic().substring(0, 10000);
        	                 dto.getMetaInfo().setPageTopic(tmp);
        	         } 
           }
       }
    }

/*	@ApiOperation(value = "PostList von Webseiten Values", notes = "Rückgabewert ist ein Currency value und oder ein Http Status Code.", response = CurrencyListDto.class)
	@RequestMapping(method = RequestMethod.POST, path = "/assets/country-currency-list")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public List<CountryCurrencyDto> createCryptoCurrencyFromList(@RequestBody
			CurrencyListDto currencyListDto) throws NoSuchAlgorithmException, ParseException {

		List<CountryCurrency> list = currencyListDto.getCurrencyDtoList().stream()
				.map(currencyDto -> convertToEntity(currencyDto))
				.collect(Collectors.toList());

		//CryptoCurrency cryptoCurrency = convertToEntity(currencyDto);

		List<CountryCurrencyDto> dtoList = new LinkedList<>();
		for (CountryCurrency countryCurrency:list) {
			CountryCurrency postCreated = websiteService.create(countryCurrency);
			dtoList.add(convertToDto(postCreated));
		}

		return dtoList;
	}*/

	private WebsiteDto convertToDto(WebsiteData websiteData) {
		return modelMapper.map(websiteData, WebsiteDto.class);
	}

	private WebsiteData convertToEntity(WebsiteDto websiteDto) {
		return modelMapper.map(websiteDto, WebsiteData.class);
	}

}
