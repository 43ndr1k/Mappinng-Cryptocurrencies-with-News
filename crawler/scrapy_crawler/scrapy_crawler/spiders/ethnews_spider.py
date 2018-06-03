# __filename__: ethnews_spider.py
#
# __description__: Spider for ethnews.com
#
# __remark__: To run spider activate venv with scrapy, cd to folder scrapy_crawler and
# scrapy crawl ethnews (-a t=option)
#
# __todos__:
#
# Created by Tobias Wenzel in November 2017


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
from date_helper import timestamp_to_date, get_formated_date
from my_logging import *
from my_imports import *


class EthnewsSpider(scrapy.Spider):
    name = "ethnewsspider"
    index = 1
    crawl_until = 0
    start_urls = ['https://www.ethnews.com/news']
    base_url = "https://www.ethnews.com"
    href_bag = []
    allowed_domains = ["ethnews.com"]
    """
    https://www.ethnews.com/news?page=1
    """

    def __init__(self, start_date=dt(2018, 1, 1),
                 stop_date=dt(2018, 1, 21), *args, **kwargs):
        super(EthnewsSpider, self).__init__(*args, **kwargs)
        self.currency = ETHEREUM
        self.start_date = start_date
        self.stop_date = stop_date

        self.my_logger = get_my_logger()
        self.my_logger.info("start %s"% __class__)
        self.my_logger.info("crawl {} from {} until {}".format(self.currency, self.stop_date,self.start_date))

    def start_requests(self):
        """
        :return:
        """
        #
        for url in self.start_urls:
            yield scrapy.Request(url=url, callback=self.collect_page_links)

    def parse_article_page(self, response):
        """

        :param response:
        :return:
        """
        description = response.xpath('//p[@class="article__summary"]/text()').extract_first()
        date_string = response.xpath('//div[@class="article__published"]/@data-created').extract_first()
        date = get_formated_date(timestamp_to_date(date_string))
        title = response.xpath('//div[@class="container"]/h1/text()').extract_first()

        first_name = response.xpath('//div[@class="article-gutter__author"]/a/h4/text()').extract_first()
        last_name = response.xpath('//div[@class="article-gutter__author"]/a/h3/text()').extract_first()
        author = first_name + " " + last_name

        keyword_string = ""
        keywords = response.xpath('//div[@class="article__tags"]/div[@class="article__tags__item"]/text()')
        for i, keyword in enumerate(keywords, 0):
            keyword_string += keyword.extract()
            if i != len(keywords)-1:
                keyword_string += ", "
            elif i >= 3:
                break

        topic_string = ""
        topics = response.xpath('//div[@class="article__category"]/a/text()')
        for i, topic in enumerate(topics, 0):
            topic_string += topic.extract()
            if i != len(topics)-1:
                topic_string += ", "
            elif i >= 3:
                break

        for sign in APOSTROPHES:
            # only on this side.
            description = description.replace(sign, "'")
            title = title.replace(sign, "'")
            author = author.replace(sign, "'")

        item = ScrapyCrawlerItem()
        item['title'] = title
        item['metaInfo'] = {
            'author': author,
            'language': 'english',
            'pageTopic': topic_string,
            'keywords': keyword_string,
        }
        item['date'] = date
        item['source'] = response.url
        item['body'] = description
        item['cryptoCurrency'] = {'currency': self.currency}
        yield item

    def collect_page_links(self, response):
        """

        :param response:
        :return:
        """
        if self.index == 1:
            last_page = response.xpath('//div[contains(@class, "pagination")]/div[contains(@class, "pagination__text")]/text()')\
                .extract_first().split('of')[-1].strip()
            self.crawl_until = int(last_page)
            self.index += 1
            link = response.xpath('//div[contains(@class, "news__top")]/a/@href').extract_first()
            article_link = self.base_url + link
            """nicht dringend, aber hier fehlt eine abfrage nach dem datum (erste seite wird meistens mitgescrollt)"""
            # date_string = response.xpath('//div[contains(@class, "news__top__primary__info")]/i/text()').extract_first()
            # date_string = re.sub(r'(\d)(st|nd|rd|th)', r'\1', date_string)
            # date = dt.strptime(date_string.strip(), '%b %d, %Y').strftime(DATE_FORMAT)
            if article_link not in self.href_bag:
                self.href_bag.append(article_link)

        if self.index < self.crawl_until:
            stop = False
            for post in response.xpath('//div[starts-with(@class,"article-thumbnail__info")]'):
                link = post.xpath('h2[contains(@class,"article-thumbnail__info__title")]/a/@href').extract_first()
                if link is None:
                    continue
                date_string = post.xpath('''div[contains(@class,"article-thumbnail__info__etc")]
                /div[contains(@class, "article-thumbnail__info__etc__date")]/h6/@data-created-short''')\
                    .extract_first()
                article_link = self.base_url + link
                date = dt.strptime(date_string.strip().split("T")[0], '%Y-%m-%d')

                if date >= self.start_date:
                    if date <= self.stop_date or self.stop_date == self.start_date:
                        if article_link not in self.href_bag:
                            self.href_bag.append(article_link)
                else:
                    stop = True
            if not stop:
                self.index += 1
                next_page = self.start_urls[0]+'?page='+str(self.index)
                yield response.follow(next_page, callback=self.collect_page_links)
            else:

                for site in self.href_bag:
                    yield scrapy.Request(url=site, callback=self.parse_article_page)
