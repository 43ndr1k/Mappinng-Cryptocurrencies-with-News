# __filename__: spider_controller.py
#
# __description__: starts all all spiders and saves the output into a json-file
#
# __remark__:
#
# __todos__:
#
# Created by Tobias Wenzel in October/November 2017

import json
import sys
import requests
import os
from datetime import datetime as dt, timedelta

CRAWLER_BASE_FOLDER = "/home/tobias/mygits/crypto-news-docs/crawler/"

sys.path.append(CRAWLER_BASE_FOLDER+ "scrapy_crawler")
sys.path.append(CRAWLER_BASE_FOLDER+ "request_crawler")
sys.path.append(CRAWLER_BASE_FOLDER+ "helper")

# Helper-Scripts
from my_constants import *
from json_helper import merge_simple_json_files
from scrapy.crawler import CrawlerProcess
from script_structure import my_main, json_context_mgnr

# 'Request-Spiders'
from cointelegraph import CoinTelegraphSpider
# Scrapy-Spiders
from spiders.newsbitcoin_spider import NewsBitcoinSpider
from spiders.bitcoinmagazine_spider import BitcoinmagazineSpider
from spiders.cryptocoins_spider import CryptocoinSpider
from spiders.coindesk_spider import CoindeskSpider
from spiders.ethnews_spider import EthnewsSpider


class SpiderController:
    EXPORT_FOLDER = CRAWLER_BASE_FOLDER +  "output/news/update/"
    EXPORT_FILE = ''
    process = None

    def __init__(self):
        self.post_url = "http://localhost:8080"

    def get_news(self, start_date=None, stop_date=None, day_delta= 7):
        """

        :return:
        """
        if start_date is None:
            delta = timedelta(days=day_delta)
            start_date = stop_date - delta

        if start_date == stop_date:
            time_span = start_date.strftime(DATE_FORMAT) +"_"
        elif start_date < stop_date:
            time_span = "{}_{}_".format(start_date.strftime(DATE_FORMAT), stop_date.strftime(DATE_FORMAT))
        else:
            raise ValueError('choose start_data < stop_date')

        if start_date and stop_date:
            self.EXPORT_FILE = self.EXPORT_FOLDER + time_span + "scr.jl"
            ct_output_file = self.EXPORT_FOLDER + time_span + 'ct.json'

            if os.path.isfile(ct_output_file):
                os.remove(ct_output_file)
                os.remove(self.EXPORT_FILE)
            else:
                pass

            self.process = CrawlerProcess({
                'FEED_URI': self.EXPORT_FILE,
                'FEED_FORMAT': 'utf-8',
                'FEED_EXPORT_INDENT':2,
                'FEED_FORMAT':'jl', # geht leider nicht anders (würde [[spider output][noch was]] erzeugen)
            })

            self.process.crawl(BitcoinmagazineSpider, start_date=start_date, stop_date=stop_date)
            self.process.crawl(EthnewsSpider, start_date=start_date, stop_date=stop_date)
            self.process.crawl(NewsBitcoinSpider, start_date=start_date, stop_date=stop_date)
            self.process.crawl(CoindeskSpider, start_date=start_date, stop_date=stop_date)
            self.process.crawl(CryptocoinSpider, start_date=start_date, stop_date=stop_date,
                               currency=BITCOIN)
            self.process.crawl(CryptocoinSpider, start_date=start_date, stop_date=stop_date,
                               currency=ETHEREUM)
            self.process.start()

            with json_context_mgnr(filename=ct_output_file,
                                   overwrite=True):
                """
                """
                for i, currency in enumerate(['litecoin', 'ethereum', 'bitcoin', 'dash'], 0):

                    coin_telegraph_spider = CoinTelegraphSpider(currency=currency,
                                                                start_date=start_date,
                                                                stop_date=stop_date)
                    if i != 0:
                        # ensure valid formatting.
                        coin_telegraph_spider.file_dirty = True
                    coin_telegraph_spider.output_file = ct_output_file

                    coin_telegraph_spider.request_crawl()

            merge_simple_json_files(scr_file=self.EXPORT_FILE,
                                    fname2=ct_output_file,
                                    output_file_name=self.EXPORT_FOLDER + time_span[:-1] + '.json')

            if os.path.isfile(ct_output_file):
                os.remove(ct_output_file)
                os.remove(self.EXPORT_FILE)
            else:
                print("One of {} \n {} files not found".format(ct_output_file, self.EXPORT_FILE))
            self.EXPORT_FILE = self.EXPORT_FOLDER + time_span[:-1] + '.json'
        else:
            print('not crawling at all.')


if __name__ == '__main__':
    sc = SpiderController()
    with my_main(__file__):
        stop_date = dt.today()
        sc.get_news(stop_date=stop_date, day_delta=37)
