package de.uni_leipzig.crypto_news_docs.service.assets.website;

import de.uni_leipzig.crypto_news_docs.dao.assets.website.CustomWebsiteDataRepository;
import de.uni_leipzig.crypto_news_docs.model.assets.website.WebsiteData;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WebsiteServiceImpl implements WebsiteService {

	@Autowired
	private CustomWebsiteDataRepository customWebsiteRepository;

	private void checkEmptyDate(Date date) {
		if (date == null) {
			throw new RuntimeException();
		}
	}

	private void checkEmptyName(String name) {
		if (name.equals("")) {
			throw new RuntimeException();
		}
	}

	@Override
	public WebsiteData findById(Long id) {
		return customWebsiteRepository.findOne(id);
	}

	@Override
	public Page<WebsiteData> findByDateBetween(Date startDate, Date endDate, Pageable pageable) {

		return customWebsiteRepository.findByDateBetween(startDate, endDate, pageable);
	}

	@Override
	public void deleteById(Long id) {
		customWebsiteRepository.delete(id);
	}

	@Override
	public Long count() {
		return customWebsiteRepository.count();
	}

	@Override
	public WebsiteData create(WebsiteData websiteData) {
		return customWebsiteRepository.save(websiteData);
	}

	@Override
	public Page<WebsiteData> listAllByPage(Pageable pageable) {
		return customWebsiteRepository.findAll(pageable);
	}

}
