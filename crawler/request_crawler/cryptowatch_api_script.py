# __filename__: CryptowatchCurrencyScraper.py
#
# __description__:
#
# __remark__:
#
# __todos__:
#
# Created by Tobias Wenzel in November 2017

import requests
import pickle
from pathlib import Path
from datetime import datetime
import sys

"""------------------------------------------------------
    import own functions/modules
------------------------------------------------------"""
sys.path.append("/home/tobias/mygits/crypto-news-docs/crawler/helper")
from json_helper import *
from date_helper import *

CRYPTO_WATCH_INPUT_FOLDER = "/home/tobias/mygits/crypto-news-docs/crawler/input/"
CRYPTO_WATCH_OUTPUT_FOLDER = "/home/tobias/mygits/crypto-news-docs/crawler/output/timeSeriesUsd/"

class CryptowatchCurrencyScraper:
    head = {
        'Accept':'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
        'Host':'api.cryptowat.ch',
        'User-Agent':'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:56.0) Gecko/20100101 Firefox/56.0',
        'Accept-Encoding':'gzip, deflate, br',
        'Accept-Language':'de,en-US;q=0.7,en;q=0.3',
        'Referer':'https://cryptowatch.de/docs/api',
        'Upgrade-Insecure-Requests':'1',
        'Connection':'keep-alive',
        'DNT':"1"
    }
    INDEX_REQUEST_URL ="https://api.cryptowat.ch/markets"
    name = 'currencyscraper'
    price_objs = []

    def __init__(self):
        self.price_objs_filename = CRYPTO_WATCH_INPUT_FOLDER + "reduced_price_objs"
        self.pairs = {}
        with open(CRYPTO_WATCH_INPUT_FOLDER + "mypairs_cryptowatch.json", 'r') as fin:
            self.pairs = json.load(fin)
        # specify in this file!
        with open(CRYPTO_WATCH_INPUT_FOLDER + "desired_currencies.json", 'r') as fin:
            self.desired_currencies = json.load(fin)['shortNames']

        my_file = Path(self.price_objs_filename)
        if my_file.is_file():
            with open(self.price_objs_filename, 'rb') as inputFile:
                self.price_objs = pickle.load(inputFile)
        else:
            # only has to be done once!
            print("fetch price objects with price")
            try:
                response = requests.get(self.INDEX_REQUEST_URL, self.head)
            except requests.exceptions.ConnectionError as e:
                print(e)
            self.markets = response.json()['result']
            self.get_price_objects()

    def get_price_objects(self):
        """
        called in __init___
        here we get get the url for the price!
        only done once
        :return:
        """

        for market, i in zip(self.markets, range(len(self.markets))):
            try:
                response = requests.get(market['route'], self.head)
            except requests.exceptions.ConnectionError as e:
                print(e)
            market = response.json()
            temp = {}
            temp['price_url'] = market['result']['routes']['price']
            temp['exchange'] = market['result']['exchange']
            temp['pair'] = market['result']['pair']
            self.price_objs.append(temp)
            print(i, float(100*i/len(self.markets)))

        with open(self.price_objs_filename, 'wb') as output:
            pickle.dump(self.price_objs, output)

    def update_special(self, base='', quote=''):
        """

        :param base:
        :param quote:
        :return:
        """
        exchange_dict = {}
        for price_object in self.price_objs:
            pair = self.pairs[price_object['pair']]
            key_name = price_object['exchange'] + "_" + pair['base'] + "_" + pair['quote']
            exchange_dict[key_name] = {
                'exchange': price_object['exchange'],
                'shortName': pair['base'],
                'timeSeriesValues': [],
                'unit': pair['quote']
            }

            if pair['base'] == 'dash':
                pair['base'] = 'dsh'
                key_name = price_object['exchange'] + "_" + pair['base'] + "_" + pair['quote']

                exchange_dict[key_name] = {
                    'exchange': price_object['exchange'],
                    'shortName': pair['base'],
                    'timeSeriesValues': [],
                    'unit': pair['quote']
                }

        base = base.replace("dash", "dsh")
        for i, price_object in zip(range(len(self.price_objs)), self.price_objs):
            crawl_time = get_date_time_tz(str(datetime.now()))
            pair = self.pairs[price_object['pair']]
            if pair['base'] == base and pair['quote'] == quote:
                try:
                    response = requests.get(price_object['price_url'], self.head)

                except requests.exceptions.ConnectionError as e:
                    print(e)

                key_name = price_object['exchange']+"_"+pair['base']+"_"+pair['quote']

                exchange_dict[key_name]['timeSeriesValues'].append({
                    'value': response.json()['result']['price'],
                    'date': crawl_time
                })
                key_name = key_name.replace("dash", "dsh")

                output_filename = CRYPTO_WATCH_OUTPUT_FOLDER + key_name + ".json"
                #key_name = key_name.replace("dash", "dsh")

                print(output_filename)
                if Path(output_filename).is_file():
                    with open(output_filename, 'r') as fin:
                        old_exchange_dict = json.load(fin)

                        exchange_dict[key_name]['timeSeriesValues'].extend(old_exchange_dict['timeSeriesValues'])
                new_save_as_json(as_array=False,
                             output=exchange_dict[key_name],
                             filename=output_filename)

    def get_price_update(self):
        """
        - create dicct for disired output
        - collect new data
        - write to specific files (if theres already data append)
        :return:
        """

        exchange_dicct = {}
        for price_object in self.price_objs:
            pair = self.pairs[price_object['pair']]

            if pair['base'] in self.desired_currencies or pair['quote'] in self.desired_currencies:
                key_name = price_object['exchange'] + "_" + pair['base'] + "_" + pair['quote']

                exchange_dicct[key_name] = {
                    'exchange': price_object['exchange'],
                    'shortName': pair['base'],
                    'timeSeriesValues': [],
                    'unit': pair['quote']
                }

                if pair['base'] == 'dash':
                    pair['base'] = 'dsh'
                    key_name = price_object['exchange'] + "_" + pair['base'] + "_" + pair['quote']

                    exchange_dicct[key_name] = {
                        'exchange': price_object['exchange'],
                        'shortName': pair['base'],
                        'timeSeriesValues': [],
                        'unit': pair['quote']
                    }
        count_btc, count_ltc = False, False
        for i, price_object in zip(range(len(self.price_objs)), self.price_objs):
            crawl_time = get_date_time_tz(str(datetime.now()))
            pair = self.pairs[price_object['pair']]

            if price_object['exchange'] == 'okcoin' \
                    and not count_btc and pair['base'] =='btc':
                count_btc = True
            elif price_object['exchange'] == 'okcoin' \
                    and not count_ltc and pair['base'] == 'ltc':
                count_ltc = True
            elif price_object['exchange'] == 'okcoin' and \
                    ((count_ltc and pair['base'] == 'ltc') or (count_btc and pair['base'] == 'btc')):
                continue

            try:
                if pair['base'] == 'dash':
                    continue
                response = requests.get(price_object['price_url'], self.head)
                if pair['base'] == 'dsh':
                    pair['base'] = 'dash'

                response = requests.get(price_object['price_url'], self.head)
            except requests.exceptions.ConnectionError as e:
                print(e)
            key_name = price_object['exchange']+"_"+pair['base']+"_"+pair['quote']
            exchange_dicct[key_name]['timeSeriesValues'].append({
                'value': response.json()['result']['price'],
                'date': crawl_time
            })
            key_name = key_name.replace("dash", "dsh")

            output_filename = CRYPTO_WATCH_OUTPUT_FOLDER + key_name + ".json"
            print(output_filename)
            if Path(output_filename).is_file():
                with open(output_filename, 'r') as fin:
                    old_exchange_dict = json.load(fin)
                    exchange_dicct[key_name]['timeSeriesValues'].extend(old_exchange_dict['timeSeriesValues'])

            new_save_as_json(as_array=False,
                         output=exchange_dicct[key_name],
                         filename=output_filename)
            print(i, float(100*i/len(self.price_objs)),key_name, crawl_time)


if __name__ == '__main__':

    cs = CryptowatchCurrencyScraper()

    # reduced_price_objs = []
    # for price_object in cs.price_objs:
    #     pair = cs.pairs[price_object['pair']]
    #
    #     if (pair['base'] in cs.desired_currencies or pair['quote'] in cs.desired_currencies) \
    #             and (pair['base'] == 'usd' or pair['quote'] == 'usd')\
    #             and price_object['pair'].endswith("usd"):
    #         #print(price_object)
    #         reduced_price_objs.append(price_object)
    # with open("/home/tobias/Dokumente/Crypto-News-Projekt/input/reduced_price_objs", 'wb') as output:
    #     pickle.dump(reduced_price_objs, output)

    cs.get_price_update()
    cs.update_special(base='dash', quote='usd')
