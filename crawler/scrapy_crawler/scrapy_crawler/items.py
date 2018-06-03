# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class ScrapyCrawlerItem(scrapy.Item):
    # define the fields for your item here like:
    title = scrapy.Field()
    #timestamp = scrapy.Field()
    date = scrapy.Field()
    body = scrapy.Field()
    source = scrapy.Field()

    pageTopic = scrapy.Field()
    cryptoCurrency = scrapy.Field()

    metaInfo = scrapy.Field()

# class LinkItem(scrapy.Item):
#     url = scrapy.Field()