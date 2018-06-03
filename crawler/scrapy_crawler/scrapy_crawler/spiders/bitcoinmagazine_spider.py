# __filename__: bitcoinmagazine_spider.py
#
# __description__: Spider for bitcoinmagazine.com.
#
# __remark__: To run spider activate venv with scrapy, cd to folder scrapy_crawler and
# scrapy crawl coindesk (-a t=option)
#
# __todos__:
#
# Created by Tobias Wenzel in October/November 2017


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
from my_logging import *


class BitcoinmagazineSpider(scrapy.Spider):


    name = "bitcoinmagazinespider"
    index = 0
    crawl_until = 0
    start_urls = ['https://bitcoinmagazine.com/articles/?page=1']
    base_url = 'https://bitcoinmagazine.com'
    allowed_domains = ["bitcoinmagazine.com"]

    def __init__(self, start_date=dt(2018, 1, 1),
                 stop_date=dt(2018, 1, 21), *args, **kwargs):
        super(BitcoinmagazineSpider, self).__init__(*args, **kwargs)
        self.currency = BITCOIN
        self.href_bag = []
        self.start_date = start_date
        self.stop_date = stop_date

        self.my_logger = get_my_logger()
        self.my_logger.info("start %s"% __class__)
        self.my_logger.info("crawl {} from {} until {}".format(self.currency, self.stop_date,self.start_date))

    def start_requests(self):
        """
        :return:
        """

        for url in self.start_urls:
            yield scrapy.Request(url=url, callback=self.parse)

    def article_parse(self, response):
        """

        :param response:
        :return:
        """
        date_string = response.css('time.article--time::text').extract_first()
        try:
            date = dt.strptime(date_string.strip(), '%b %d, %Y')
            date_string = date.strftime(DATE_FORMAT)
        except AttributeError:
            # there are some articles which have another structure-> they don't really have an author
            # (only on main page) and no date.
            return

        if date >= self.start_date:
            if date <= self.stop_date or self.stop_date == self.start_date:
                item = ScrapyCrawlerItem()
                try:
                    page_topic = response.xpath(
                        """//div[starts-with(@class,"acticle--current-swction")]/a/text()""").extract_first().split()[-1].lower()
                except AttributeError:
                    page_topic = 'None'  # not always set

                author = response.xpath("""//div[starts-with(@class, 
                            "article--author")]/span/a/text()""").extract_first()
                if not author:
                    author = 'None'
                item['cryptoCurrency'] = {'currency': self.currency}
                item['date'] = date_string
                item['metaInfo'] = {
                    'author': author,
                    'language': 'english',
                    'pageTopic': page_topic,
                    'keywords': '',
                }
                item['source'] = response.url
                item['title'] = response.xpath("""//h1[starts-with(@class,
                 "article--headline")]/text()""").extract_first()
                # I simply take the first span/paragraph as description.
                # sometimes this works, but usually there are span/a/... so I choose to set 'None'
                desc = response.xpath("""//div[contains(@class, "rich-text")]/span/p/span/text()""").extract_first()
                if desc and len(desc) > 50:
                    item['body'] = desc
                else:
                    item['body'] = 'None'
                yield item
        else:
            raise scrapy.exceptions.CloseSpider('finished crawling')



    def parse(self, response):
        """

        :param response:
        :return:
        """
        for post in response.xpath('//a[contains(@class, "category-list--link")]'):
            link = post.xpath("""descendant-or-self::a[@class and contains(concat(' ', normalize-space(@class), ' '),
             ' category-list--link ')]/@href""").extract_first()
            article_href = self.base_url + link
            if article_href not in self.href_bag:
                yield scrapy.Request(article_href, callback=self.article_parse, priority=1)
                self.href_bag.append(article_href)
        next_url = ""
        try:
            next_url = response.css('li a.page-link::attr(href)')[-1].extract()
        except Exception:
            #...weiss ich doch. @todo
            pass
        self.index += 1
        if next_url:
            next_url = self.base_url+"/articles/"+ next_url
            yield response.follow(next_url, callback=self.parse)
