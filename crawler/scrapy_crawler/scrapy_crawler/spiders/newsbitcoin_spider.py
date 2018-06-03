# __filename__: newsbitcoin.py
#
# __description__: Spider for news.bitcoin.com. Version 2.0: first collect links, then crawl one layer deeper.
#
# __remark__: To run spider activate venv with scrapy, cd to folder scrapy_crawler and
# scrapy crawl newsbitcoin (-a t=option)
#
# __todos__:
#
# Created by Tobias Wenzel in October/November 2017
import sys
import datetime
from datetime import datetime as dt

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
from my_imports import *
from my_logging import *


class NewsBitcoinSpider(scrapy.Spider):
    name = "newsbitcoinspider"
    index = 0
    crawl_until = 0
    allowed_domains = ["news.bitcoin.com"]
    start_urls = ['https://news.bitcoin.com/']

    complete_cycle = False

    def __init__(self, start_date=dt(2018, 1, 1),
                 stop_date=dt(2018, 1, 21), *args, **kwargs):
        super(NewsBitcoinSpider, self).__init__(*args, **kwargs)
        self.currency = BITCOIN
        self.start_date = start_date
        self.stop_date = stop_date

        self.href_bag = []

        self.my_logger = get_my_logger()
        self.my_logger.info("start %s"% __class__)
        self.my_logger.info("crawl {} today until {}".format(self.currency, self.start_date))

    def start_requests(self):
        """

        :return:
        """
        for url in self.start_urls:
            yield scrapy.Request(url=url, callback=self.collect_page_links)

    def parse_article_page(self, response):
        """
        also parse author
        is called after links have been collected
        :param response:
        :return:
        """

        description = " ".join(response.xpath('//div[@class="td-post-content"]/p/strong/text()').extract())
        i = 3  # while there the description is not long enougth, add text.
        while len(description) < CHARACTER_NUMBER and i < 13:
            description = " ".join(response.xpath('//div[@class="td-post-content"]/p/text()').extract()[:i]).strip()
            i += 1
        if description:
            description = get_text_of_n_words(description, n=WORD_NUMBER)

        title = response.xpath('//h1[@class= "entry-title"]/text()').extract_first()

        author = response.xpath('//div[@class="td-post-author-name"]/a/text()').extract_first()
        try:
            date_string = response.xpath('//div[@class="btc-post-meta td-post-title td-post-author-name"]/text()').extract_first().strip()
        except AttributeError:
            date_string = ''
        if date_string:
            if "days ago" in date_string:
                days_ago = int(date_string.strip().split("days")[0].rstrip())
                date_string = (datetime.date.today() - datetime.timedelta(days=days_ago)).strftime(DATE_FORMAT)
            elif "day ago" in date_string:
                date_string = (datetime.date.today() - datetime.timedelta(days=1)).strftime(DATE_FORMAT)
            elif "hours ago" in date_string:
                hours_ago = int(date_string.strip().split("hours")[0].rstrip())
                date_string = (datetime.date.today() - datetime.timedelta(hours=hours_ago)).strftime(DATE_FORMAT)
            elif "hour ago" in date_string:
                date_string = (datetime.date.today() - datetime.timedelta(hours=1)).strftime(DATE_FORMAT)
        else:
            date = response.xpath('//span[@class="td-post-date"]/time[@class="entry-date updated td-module-date"]/@datetime').extract_first()
            date_string = get_formated_date(timestamp_to_date(date))

        topic = response.xpath('//div[@class="td-post-category top-left-label btc-post-meta td-post-title"]/text()')\
            .extract_first().strip()

        keywords = response.xpath('//ul[@class="td-tags td-post-small-box clearfix"]')  # first is 'TAG'
        keywords = keywords.xpath('li/a/text()')
        keyword_string = ""
        for i, keyword in enumerate(keywords, 1):
            keyword_string += keyword.extract()
            if i != len(keywords):
                keyword_string += ", "

        if len(keywords) > 3:
            keyword_string = ",".join(keyword_string.split(",")[:3])

        item = ScrapyCrawlerItem()
        item['title'] = title
        item['metaInfo'] = {
            'author': author,
            'language': 'english',
            'pageTopic': topic,
            'keywords': keyword_string,
        }
        item['date'] = date_string
        item['source'] = response.url
        item['body'] = description
        item['cryptoCurrency'] = {'currency': self.currency}
        yield item

    def collect_page_links(self, response):
        """

        :param response:
        :return:
        """
        if self.index == 0:
            last_page = response.xpath('//div[starts-with(@class, "page-nav")]/a[contains(@class, "last")]/@title')\
                .extract_first()
            self.crawl_until = int(last_page)

            for post in response.xpath('//div[starts-with(@class,"td_module_mx16")]'):
                link = post.xpath('div/div/h3/a/@href').extract_first()

                if link not in self.href_bag:
                    self.href_bag.append(link)

        if self.index < self.crawl_until:
            stop = False
            for post in response.xpath('//div[contains(@class, "item-details")]'):
                link = post.xpath('h3/a/@href').extract_first()
                date_string = post.xpath('div/span/time/@datetime').extract_first()
                date = dt.strptime(date_string.strip().split("T")[0], '%Y-%m-%d')

                if self.stop_date > date > self.start_date:
                    if link not in self.href_bag:
                        self.href_bag.append(link)
                else:
                    stop= True

            if not stop:
                self.index += 1
                next_page = self.start_urls[0]+"page/"+str(self.index)
                yield response.follow(next_page, callback=self.collect_page_links)
            else:

                for url in self.href_bag:
                    yield scrapy.Request(url=url, callback=self.parse_article_page)