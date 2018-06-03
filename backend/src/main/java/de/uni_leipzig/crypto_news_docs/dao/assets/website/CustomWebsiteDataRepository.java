package de.uni_leipzig.crypto_news_docs.dao.assets.website;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.uni_leipzig.crypto_news_docs.model.assets.website.WebsiteData;

public interface CustomWebsiteDataRepository extends JpaRepository<WebsiteData, Long>{
	
	@Query(value = "SELECT wd FROM WebsiteData wd WHERE wd.date BETWEEN ?1 AND ?2")
	Page<WebsiteData> findByDateBetween(Date startDate, Date endDate, Pageable pageable);
}
