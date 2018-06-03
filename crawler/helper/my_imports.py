import sys

sys.path.append("/home/tobias/mygits/crypto-news-docs/crawler/scrapy_crawler")
try:
    from scrapy_crawler.items import ScrapyCrawlerItem
except ImportError as e:
    print(e)

