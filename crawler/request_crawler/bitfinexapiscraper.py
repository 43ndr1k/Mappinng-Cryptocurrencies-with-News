# __filename__: BitfinexApiScraper.py
#
# __description__: "Spider" for BitfinexApi
#
# __remark__: it might seem double (cryptowatch) but we can scrape historic data from this
# api. thus, I don't run this as a cronjob on my pi.
#  start vis function from console!
#
# __todos__:
#
# Created by Tobias Wenzel in November 2017

import requests
from datetime import datetime, timedelta
import time
import json
import sys
sys.path.append("../helper")
from date_helper import datespan
from date_helper import utc_to_str
from json_helper import save_as_json

class BitfinexApiScraper:
    url = 'https://api.bitfinex.com/v2/candles/trade:1D:t'
    stock_data = {}
    output_folder = "../output/"
    input_folder = "../input/"

    def __init__(self, output_filename=""):
        self.output_filename=output_filename

    def crawl_time_span(self, start_time, end_time, pair=""):
        """

        :param start_time:
        :param end_time:
        :param stock_data:
        :return:
        """
        url = self.url + pair + '/hist'
        params = {'start': int(start_time * 1000), 'end': int(end_time * 1000), 'sort': '1'}
        r = requests.get(url, params=params)
        data = r.json()


        output_dicct = {}
        output_dicct['exchange'] = "bitfinex"
        output_dicct['shortName'] = pair.split("USD")[0].lower()
        output_dicct['unit'] = "usd"
        output_dicct['timeSeriesValues'] = []

        for line in data:
            try:
                #print(datetime.fromtimestamp(int(line[0])/1000))
                output_dicct['timeSeriesValues'].append({
                    'date': utc_to_str(line[0])+"T02:00:00Z",
                    'value': line[4] ## check!
                })
                print(output_dicct['timeSeriesValues'][-1])
            except TypeError as te:
                print(te)
            except ValueError as ve:
                print(ve)
        output_name = self.output_folder + 'bitfinex_'+ output_dicct['shortName']+ "_usd.json"
        save_as_json(filename=output_name, output=output_dicct, as_array=False)


    def save_candle(self):
        """

        :return:
        """
        day_dicct = {}
        for candle_key in self.stock_data.keys():
            try:
                date_string = str(utc_to_str(candle_key))
            except ValueError:
                # skip errors
                continue
            day_dicct[date_string] = self.stock_data[candle_key]

        save_as_json(filename=self.output_filename, output=day_dicct, as_array=False)

    def crawl(self, start_date, my_timedelta, pair = "BTCUSD"):
        """

        :param start_date:
        :param my_timedelta:
        :param pair:
        :return:
        """
        end_time = datetime(2017, 12, 14) #datetime.fromtimestamp(int(time.time()))
        for day in datespan(start_date=start_date,
                            end_date=end_time, delta=timedelta(days=my_timedelta)):
            start_date = int(day.timestamp())
            print(datetime.fromtimestamp(start_date), datetime.fromtimestamp(start_date) + timedelta(days=my_timedelta))
            next_end_time = int((datetime.fromtimestamp(start_date) + timedelta(days=my_timedelta)).timestamp())
            self.crawl_time_span(start_time=start_date,
                                 end_time=next_end_time,
                                 pair=pair)


            time.sleep(3)


def vis_course(filename="../output/bitfinex.json", show_course="btcusd", step_size = 5):
    """

    :param filename:
    :param show_course:
    :return:
    """
    import sys
    sys.path.append("/home/tobias/Dropbox/Programme/python_vis/src")
    from my_visualisation import Scatter_Visualizer

    lines = []
    with open(filename, 'r') as fin:
        lines = json.load(fin)[0]
    prices = []
    for i, key in zip(range(len(lines.keys())),lines.keys()):
        # could also be done with dicct and every pair is a key.
        for pair_index in range(len(lines[key])):
            if lines[key][pair_index]['pair'] == show_course.lower() and i%step_size == 0:
                prices.append(lines[key][pair_index]['data'][4])

    course_visualiser = Scatter_Visualizer(interactive=False, offset=1000,
                                           xlim=len(prices), log_scale=False,
                                           sexify=False)
    course_visualiser.plot_my_data(range(0, len(prices)), prices)
    course_visualiser.save_me("/home/tobias/Bilder/"+show_course+"course.pdf")


if __name__ == '__main__':
    pairs = ['BTCUSD', 'LTCUSD', 'ETHUSD',
             'ZECUSD', 'XMRUSD', 'DSHUSD',
             'XRPUSD', 'IOTAUSD', 'EOSUSD',
             'SANUSD', 'OMGUSD', 'BCHUSD',
             'NEOUSD', 'ETPUSD', 'EDOUSD',
             'DATAUSD']

    cs = BitfinexApiScraper(output_filename="../output/bitfinex_dec.json")
    my_timedelta = 19
    for i, pair in zip(range(len(pairs)), pairs):
        print(100*i/len(pairs))
        cs.crawl(start_date=datetime(2017, 11, 26), my_timedelta=my_timedelta, pair=pair)

    # with open("../output/stock_stuff_complete", 'wb') as output:
    #     pickle.dump(cs.stock_data, output)

    """
    you can of course skip this step.
    """
    # with open("../output/stock_stuff_complete", 'rb') as inputFile:
    #     cs.stock_data = pickle.load(inputFile)
    #cs.save_candle()
    #vis_course(show_course="zecusd", filename="../output/bitfinex.json")
