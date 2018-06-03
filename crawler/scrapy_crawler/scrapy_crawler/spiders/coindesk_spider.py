# __filename__: coindesk_spider.py
#
# __description__: Spider for coindesk.com. Version v.1 description available (no tags + topics)
#
# __remark__: To run spider activate venv with scrapy, cd to folder scrapy_crawler and
# scrapy crawl coindesk (-a t=option)
#
# __todos__:
#
# Created by Tobias Wenzel in October/November 2017
# Copyright (c) 2017 Tobias Wenzel


from datetime import datetime as dt
import sys

"""------------------------------------------------------
    import third party modules
------------------------------------------------------"""
import scrapy

"""------------------------------------------------------
    import own functions/modules
------------------------------------------------------"""
sys.path.append("/home/tobias/mygits/crypto-news-docs/crawler/helper")
from my_constants import *
from my_imports import *
from language_processor import get_text_of_n_words
from my_logging import *


class CoindeskSpider(scrapy.Spider):
    name = "coindeskspider"
    index = 0
    crawl_until = 0
    start_urls = ['https://www.coindesk.com/category/bitcoin/news/']
    href_bag = []
    allowed_domains = ["coindesk.com"]

    def __init__(self, start_date=dt(2018, 1, 1),
                 stop_date=dt(2018, 1, 21), *args, **kwargs):
        super(CoindeskSpider, self).__init__(*args, **kwargs)
        self.currency = BITCOIN

        self.start_date = start_date
        self.stop_date = stop_date

        self.my_logger = get_my_logger()
        self.my_logger.info("start %s"% __class__)
        self.my_logger.info("crawl {} {} until {}".format(self.currency, self.stop_date,self.start_date))

    def start_requests(self):
        """
        :return:
        """

        for url in self.start_urls:
            yield scrapy.Request(url=url, callback=self.collect_page_links)

    def collect_page_links(self, response):
        """

        :param response:
        :return:
        """
        if self.index == 0:
            last_page = response.css('div.pagination a::attr(href)').extract()[-1]
            self.crawl_until = int(last_page.split("/")[-2])
            print(self.crawl_until)

        if self.index < self.crawl_until:
            stop = False
            for post in response.xpath('//*[starts-with(@class, "article medium post-")]'):
                link = post.css('div.post-info h3 a.fade::attr(href)').extract_first()
                date_string = post.css('time::attr(datetime)').extract_first()
                date = dt.strptime(date_string.strip().split("T")[0], '%Y-%m-%d')

                if date > self.start_date:
                    if date > self.stop_date:
                        continue

                    if link not in self.href_bag:
                        self.href_bag.append(link)
                else:
                    stop = True
                    break

            if not stop:
                self.index += 1
                if self.index == 1:
                    self.index = 2
                next_page = self.start_urls[0]+'page/'+str(self.index)
                yield response.follow(next_page, callback=self.collect_page_links)
            else:
                self.my_logger.info("start crawling"+str(len(self.href_bag))+ " pages.")
                for url in self.href_bag:
                    yield scrapy.Request(url=url, callback=self.parse_article_page)

    def parse_article_page(self, response):
        """

        :param response:
        :return:
        """
        title =response.xpath('''//h3[@class="article-top-title"]/text()''').extract_first()
        author = response.xpath('''//a[@class="article-container-lab-name"]/text()''').extract_first()
        date_string = response.xpath('''//span[@class="article-container-left-timestamp"]/text()''').extract()[1].split("at")[0].strip()

        date = dt.strptime(date_string.split("at")[0].strip(), '%b %d, %Y').strftime(DATE_FORMAT)

        topic = response.xpath('''//span[@class="article-top-tag"]/text()''').extract_first()
        keywords = response.xpath('''//p[@class="single-tags"]/a/text()''')
        keyword_string = ""
        for i, keyword in enumerate(keywords, 0):
            keyword_string += keyword.extract()
            if i != len(keywords)-1:
                keyword_string += ", "

        description = ""
        i = 1  # while there the description is not long enougth, add text.
        while len(description) < CHARACTER_NUMBER and i < 10:
            description = " ".join(response.xpath('''//div[@class="article-content-container noskimwords"]/p/text()''')
                                   .extract()[:i]).strip()
            i += 1
        if description:
            description = get_text_of_n_words(description, n=WORD_NUMBER).strip()  # see helper.language_processor
            if not description.endswith("."):
                description += '...'
        else:
            description = 'None'

        item = ScrapyCrawlerItem()
        item['title'] = title
        item['metaInfo'] = {
            'author': author,
            'language': 'english',
            'pageTopic': topic.lower(),
            'keywords': keyword_string.lower(),
        }
        item['date'] = date
        item['source'] = response.url
        item['body'] = description
        item['cryptoCurrency'] = {'currency': self.currency}
        yield item
