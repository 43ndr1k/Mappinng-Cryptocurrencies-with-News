# __filename__: cryptocoins_spider.py
#
# __description__: Spider for cryptocoinsnews.com. Possible for Bitcoin and Ethereum
#
# __remark__: To run spider activate venv with scrapy, cd to folder scrapy_crawler and
# scrapy crawl coindesk (-a t=option)

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
from language_processor import get_text_of_n_words
from date_helper import get_formated_date
from date_helper import timestamp_to_date
from my_logging import *
from my_imports import *



class CryptocoinSpider(scrapy.Spider):
    name = "cryptocoinsnewsspider"
    index = 1
    crawl_until = 0


    href_bag = []

    allowed_domains = ["ccn.com"]

    def __init__(self, start_date=dt(2018, 1, 1),
                 stop_date=dt(2018, 1, 21), currency=BITCOIN, *args, **kwargs):
        super(CryptocoinSpider, self).__init__(*args, **kwargs)

        self.currency = currency
        if self.currency == BITCOIN:
            self.url = 'https://www.ccn.com/ethereum-news/page/'
            self.start_urls = [ 'https://www.ccn.com/news/']
        elif self.currency == ETHEREUM:
            self.url = 'https://www.ccn.com/ethereum-news/page/'
            self.start_urls = ['https://www.ccn.com/ethereum-news/']
        else:
            raise NotImplementedError

        self.start_date = start_date
        self.stop_date = stop_date

        self.my_logger = get_my_logger()
        self.my_logger.info("start %s"% __class__)
        self.my_logger.info("crawl {} today until {}".format(self.currency, self.start_date))

    def start_requests(self):
        """

        :return:
        """
        self.crawl_until = 1000000000  # until i find another solution
        for url in self.start_urls:
            yield scrapy.Request(url=url, callback=self.collect_page_links)

    def parse_article_page(self, response):
        """
        walks through articles that are already downloaded and
        scrape description
        :param response:
        :return:
        """
        description = ""
        i = 1
        while len(description) < CHARACTER_NUMBER:
            # first is advertising
            description = " ".join(response.xpath('//div[contains(@class, "entry-content")]/p/text()')[:i].extract())
            if i == 10:
                # at this point check if there are other elements
                new_text = response.xpath('//div[contains(@class, "entry-content")]/p/span/text()').extract_first()
                if new_text is not None:
                    description += new_text
                break
            i += 1
        if description:
            description = get_text_of_n_words(description, n=WORD_NUMBER)

        title = response.xpath('//h1[@class="entry-title"]/text()').extract_first()

        author = response.xpath('''//div[@class="col-md-6 d-flex post-author align-self-center"]
        /div[@class="d-inline-flex flex-column align-self-center"]
        /span[@class="font-weight-normal text-capitalize"]/text()''').extract_first()

        keywords = response.xpath('''//div[@class="post-tags col-md-6 text-uppercase"]/a/text()''')
        keyword_string = ""
        for i, keyword in enumerate(keywords, 0):
            keyword_string += keyword.extract()
            if i != len(keywords)-1:
                keyword_string += ", "
        if len(keywords) > 3:
            keyword_string = ",".join(keyword_string.split(",")[:3])

        meta = response.xpath('''//div[@class="entry-meta"]''')
        topic = meta.xpath('''span[@class="featured-category"]/a/text()''').extract_first()

        date_string = meta.xpath('''time[@class="updated"]/@datetime''').extract_first()
        date = get_formated_date(timestamp_to_date(date_string))

        item = ScrapyCrawlerItem()
        item['title'] = title
        item['metaInfo'] = {
            'author': author,
            'language': 'english',
            'pageTopic': topic,
            'keywords': keyword_string,
        }
        item['date'] = date
        item['source'] = response.url
        item['body'] = description
        item['cryptoCurrency'] = {'currency': self.currency}

        yield item

    def collect_page_links(self, response):
        """
        collects article links and
        calls parse_article_page
        :param response:
        :return:
        """

        if self.index < self.crawl_until:
            stop = False
            for post in response.xpath('//article[starts-with(@class, "post-")]'):
                link = post.xpath('header/h4[starts-with(@class, "entry-title")]/a/@href').extract_first()

                date_string = post.xpath('header/div[starts-with(@class, "entry-meta")]/time/@datetime').extract_first()

                date = dt.strptime(date_string.split("+")[0], '%Y-%m-%dT%H:%M:%S')

                if self.stop_date > date > self.start_date:
                    if link not in self.href_bag:
                        self.href_bag.append(link)

                else:
                    stop = True

            if not stop:

                self.index += 1
                if self.index == 1:
                    self.index = 2

                next_page = self.url + str(self.index)+"/"
                yield response.follow(next_page, callback=self.collect_page_links)
            else:

                for url in self.href_bag:
                    yield scrapy.Request(url=url, callback=self.parse_article_page)
