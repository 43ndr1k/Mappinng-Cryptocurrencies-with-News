# __filename__: ecbscraper.py
#
# __description__: scrapes courses from ecb. only needed if system runs as real-time application.
#
# __remark__:
#
# __todos__:        # was heißt A

#
# Created by Tobias Wenzel in November 2017
# Copyright (c) 2017 Tobias Wenzel


import requests
from lxml import etree
from datetime import datetime, timedelta
import time
import sys
import json

sys.path.append("/home/tobias/mygits/crypto-news-docs/crawler/helper")
from date_helper import get_formated_date
from date_helper import reverse_format

from date_helper import datespan
from date_helper import get_date_time_tz
from json_helper import new_save_as_json

ECB_OUTPUT_FILE = "/home/tobias/mygits/crypto-news-docs/crawler/output/countryCurrencies/ecb_usdeur.json"
PAIR = "USD.EUR"

class ECBScraper:
    base_url = "https://sdw-wsrest.ecb.europa.eu/service/data/EXR/D."
    currency_dict = {}
    name = "ecbscraper"

    def __init__(self, pair="USD.EUR", my_timedelta=30):
        """

        :param pair:
        :param my_timedelta:
        """
        self.url = self.base_url + pair + ".SP00.A"
        self.my_timedelta = my_timedelta
        self.pair = "".join(pair.split("."))

    def crawl_time_span(self, start_date, end_date):
        """

        :param start_date:
        :param end_date:
        :return:
        """
        params = {
            'startPeriod': get_formated_date(start_date),
            'endPeriod': get_formated_date(end_date)
        }
        try:
            r = requests.get(self.url, params=params)
            data = etree.XML(r.content)
        except requests.exceptions.ConnectionError as e:
            print(e)

        observations = data.xpath('//*[name()="generic:Obs"]')
        for obs in observations:
            val = obs.xpath('*[name()="generic:ObsValue"]/@value')
            date = obs.xpath('*[name()="generic:ObsDimension"]/@value')
            formated_date = get_date_time_tz(str(reverse_format(date[0])))
            print(date)
            temp_dicct = {
                'value': val[0],
                'date': formated_date
            }
            self.currency_dict['timeSeriesValues'].append(temp_dicct)

    def crawl_pair(self, start_date, end_date, pair="USD.EUR"):
        """

        :param start_date:
        :param end_date:
        :param pair:
        :return:
        """
        self.pair = pair
        if not self.currency_dict:
            self.currency_dict = {}
            self.currency_dict['exchange'] = "ecb"
            self.currency_dict['shortName'] = "eur"
            self.currency_dict['unit'] = "usd"
            self.currency_dict['timeSeriesValues'] = []

        self.url = self.base_url + pair + ".SP00.A"

        ## slice into nice little pieces so i don't get banned
        for day in datespan(start_date=start_date,
                            end_date=end_date, delta=timedelta(days=self.my_timedelta)):
            next_end_time = day + timedelta(days=self.my_timedelta)
            self.crawl_time_span(start_date=day, end_date=next_end_time)
            time.sleep(3)

    def daily_routine(self):
        """
        first load old data from json, then append new
        crawls last day!
        :return:
        """
        td = datetime.today() - timedelta(days=1)
        today = datetime(td.year, td.month, td.day)

        with open(ECB_OUTPUT_FILE, 'r') as fin:
            self.currency_dict = json.load(fin)
        # pair is passed as default in constructor
        self.crawl_time_span(start_date=today, end_date=today)
        new_save_as_json(output=self.currency_dict, filename=ECB_OUTPUT_FILE,
                         as_array=False)
        # self.__str__()

    def __str__(self):
        print("exchange:\t", self.currency_dict['exchange'],
              '\nshortName\t', self.currency_dict['shortName'],
              '\nunit', self.currency_dict['unit'])
        for entry in self.currency_dict['timeSeriesValues']:
            print(entry)


if __name__ == '__main__':

    es = ECBScraper(my_timedelta=30)
    """
        called by anachron-script
    """
    es.daily_routine()
    """
        use this if you want to crawl a timespan
    """
    # td = datetime.today() - timedelta(days=1)
    # today = datetime(td.year, td.month, td.day)
    # es.crawl_pair(start_date=datetime(2017, 1, 1),
    #               end_date=today,
    #               pair=PAIR)
    #
    # new_save_as_json(output=es.currency_dict,
    #                  filename=ECB_OUTPUT_FILE,
    #                  as_array=False)
