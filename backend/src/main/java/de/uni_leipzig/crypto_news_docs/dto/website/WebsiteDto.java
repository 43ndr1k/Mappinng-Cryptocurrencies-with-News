package de.uni_leipzig.crypto_news_docs.dto.website;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class WebsiteDto {
  
  String source;
  CryptoCurrencyNewsDto cryptoCurrency;
  String title;
  String body;
  MetaInfoDto metaInfo; // Dto, damit nicht die ID zurueckgegeben wird

  //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  Date date;
  

  public WebsiteDto() {
    
  }


  public WebsiteDto(String source, CryptoCurrencyNewsDto cryptoCurrency, String title, String body,
          MetaInfoDto metaInfo, String currency, Date date) {
    this.source = source;
    this.cryptoCurrency = cryptoCurrency;
    this.title = title;
    this.body = body;
    this.metaInfo = metaInfo;
    this.cryptoCurrency = cryptoCurrency;
    this.date = date;
  }
  
  
  public CryptoCurrencyNewsDto getCryptoCurrency() {
    return cryptoCurrency;
  }


  public void setCryptoCurrency(CryptoCurrencyNewsDto cryptoCurrency) {
    this.cryptoCurrency = cryptoCurrency;
  }
  
  public Date getDate() {
    return date;
  }


  public void setDate(Date date) {
    this.date = date;
  }
  

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public MetaInfoDto getMetaInfo() {
    return metaInfo;
  }

  public void setMetaInfo(MetaInfoDto metaInfo) {
    this.metaInfo = metaInfo;
  }
}
