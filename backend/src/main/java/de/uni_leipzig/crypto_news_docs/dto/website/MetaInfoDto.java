package de.uni_leipzig.crypto_news_docs.dto.website;

import java.sql.Date;


public class MetaInfoDto {
  
  String author;
  Date creationDate;
  String language;
  String pageTopic;
  String keywords;

  
  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getPageTopic() {
    return pageTopic;
  }

  public void setPageTopic(String pageTopic) {
    this.pageTopic = pageTopic;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  
}
