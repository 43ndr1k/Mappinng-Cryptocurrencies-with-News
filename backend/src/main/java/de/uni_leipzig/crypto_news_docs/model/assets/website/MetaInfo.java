package de.uni_leipzig.crypto_news_docs.model.assets.website;

import java.sql.Date;
import de.uni_leipzig.crypto_news_docs.model.AbstractIdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "meta_info")
public class MetaInfo extends AbstractIdEntity  {
  
  @Column(nullable=true, columnDefinition="varchar(10000)")
  String author;
  @Column(nullable=true, columnDefinition="varchar(10000)")
  String language;
  @Column(nullable=true, columnDefinition="varchar(10000)")
  String pageTopic;
  @Column(nullable=true, columnDefinition="varchar(10000)")
  String keywords;

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
