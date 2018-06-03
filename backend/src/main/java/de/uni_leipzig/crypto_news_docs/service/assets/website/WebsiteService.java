package de.uni_leipzig.crypto_news_docs.service.assets.website;

import de.uni_leipzig.crypto_news_docs.model.assets.website.WebsiteData;

//import java.sql.Date;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WebsiteService {

    WebsiteData create(WebsiteData websiteData);
    Page<WebsiteData> listAllByPage(Pageable pageable);
    WebsiteData findById(Long id);
    void deleteById(Long id);
    Long count();
    
    Page<WebsiteData> findByDateBetween(Date starDate, Date endDate, Pageable pageable);
    
    //Page<WebsiteData> findByTimestampAndCurrency(String currency, Date date, Pageable pageable);
    //Page<WebsiteData> findByTimestampDateAndCurrency(String currency, Date date, Pageable pageable);

}
