# __filename__: json_builder.py
#
# __description__: this collects all newsdata and combines it to a json file/ data
#
# __remark__:
#
# __todos__:
#
# Created by Tobias Wenzel in November 2017

import pickle
import re
from datetime import datetime as dt


from script_structure import my_main
from my_constants import *
from json_helper import *
from date_helper import get_date_time_tz
from date_helper import utc_to_str


def correct_date(date_string):
    """

    :param date_string:
    :return:
    """
    r = re.compile('[a-zA-Z]{3} \d{2}, \d{4}')

    if r.match(date_string):
        return dt.strptime(date_string.strip(), '%b %d, %Y').strftime(DATE_FORMAT)
    if "/" in date_string:
        new_date_string = dt.strptime(date_string.strip(), '%d/%m/%Y').strftime(DATE_FORMAT)
        if new_date_string:
            return new_date_string
    else:
        return date_string

"""----------------------------------------------------
                        timeSeries
----------------------------------------------------"""

def bitcoinde(filename="", output_file = ""):
    """
    timeseries aren't included in bitfinex or crypto.wat.ch,
    have to be downloaded and adjusted to fit.
    https://www.bitcoin.de/json/chart/stats_hourly_btcusd_statistics_60month.json
    :return:
    """
    output_dicct = {}
    output_dicct['exchange'] = "bitcoin.de"
    output_dicct['shortName'] = "btc"
    output_dicct['unit'] = "usd"
    output_dicct['timeSeriesValues'] =[]
    with open(filename, "r") as fin:
        lines = fin.read().split("],")

    last_date = "'2017-01-01"
    last_course = ""
    for line in lines[1:]:
        line= line[1:].split(",")
        date = utc_to_str(line[0])
        if date.startswith("2017") or date.startswith("2018"):
            if date != last_date:
                output_dicct['timeSeriesValues'].append({
                    'date': last_date+"T23:59:59.00000Z",
                    'value': last_course
                })
            last_date = date
            last_course = line[4]

    new_save_as_json(output=output_dicct,filename=output_file,as_array=False )


def space_to_tz(foldername=""):
    """

    :param foldername:
    :return:
    """

    json_files = glob.glob(foldername + "*.json")
    for file in json_files:
        with open(file, 'r') as fin:
            data = json.load(fin)

        for i in range(len(data['timeSeriesValues'])):
            entry = data['timeSeriesValues'][i]

            if len(entry['date'].split(" ")) > 1:
                entry['date'] = get_date_time_tz(entry['date'])
                data['timeSeriesValues'][i] = entry
                #print(entry)
        new_save_as_json(data, file, as_array=False)


"""----------------------------------------------------
                        news
----------------------------------------------------"""


def remove_duplicates(file_name=""):
    """

    :param file_name:
    :return:
    """
    with open(file_name, 'r') as fin:
        mydicct_array= json.load(fin)
    sources = []
    result_ar = []
    for i, article in enumerate(mydicct_array, 0):
        link = mydicct_array[i]['source']
        if link not in sources:
            sources.append(link)
            result_ar.append(article)
    output_name = "/home/tobias/Dokumente/Crypto-News-Projekt/output/news/news_data_v7.json"
    new_save_as_json(output=mydicct_array, filename= output_name
                    , as_array=True)

"""----------------------------------------------------
                        more
----------------------------------------------------"""

def wiki_script():
    """
    mini-script um wiki-einträge auf exchanges objecte zu mappen.
    :return:
    """
    # with open(
    #         file="/home/tobias/mygits/crypto-news-docs/crawler/input/currency_info/desired_currencies_wiki.json") as fin:
    #     wiki_data = json.load(fin)
    output_folder = "/home/tobias/Schreibtisch/exchanges/"


    json_files = glob.glob("/home/tobias/Schreibtisch/exchanges/" + "*.json")
    print(len(json_files))
    for file in json_files:

        with open(file, 'r') as fin:
            exchange_data = json.load(fin)
        try:
            ioc_codes = {
                'USA': 'USA',
                'CHN': 'CHN',
                'RUS': 'RUS',
                'HKG': 'HKG',
                'GBR': 'GBR',
                'MEX': 'MEX',
                'CAN': 'CAN',
                'LUX': 'LUX',
                'GER': 'DEU',
                'JPN': 'JPN'
            }

            # exchange_data['position']= ioc_codes[exchange_data['position']]
            # if exchange_data['position'] == 'LUY':
            #     exchange_data['position'] = 'LUX'
            for i, currency in enumerate(exchange_data['cryptoCurrencies'], 0):
                exchange_data['cryptoCurrencies'][i]['description']['wikiEnglish'] = 'dummy'#wiki_data[currency['shortName']]['wikiEnglish']
                exchange_data['cryptoCurrencies'][i]['description']['wikiGerman'] = 'dummy'#wiki_data[currency['shortName']]['wikiGerman']
            out_file = output_folder + file.split("/")[-1]
            new_save_as_json(exchange_data, filename=out_file, as_array=False)
        except KeyError as e:
            print(e)


def convert_jl_json_to_json(filename):
    """

    :param filename:
    :return:
    """
    data = []
    with open(filename, 'r') as fin:
        for line in fin:
            try:
                json_string = line.encode('utf-8').decode().strip()
                json_string = json.loads(json_string.strip().rstrip(","))
                data.append(json_string)
            except json.decoder.JSONDecodeError as e:
                pass
    new_save_as_json(output=data, as_array=True,
                     filename="/home/tobias/Dokumente/Crypto-News-Projekt/output/news/btc_cointelegraph.json")


def stat_articles_per_file(foldername = "/home/tobias/Dokumente/Crypto-News-Projekt/output/news/v_2/"):
    """
    :param foldername:
    :return:
    """
    json_files = glob.glob(foldername + "*.json")
    tag_currency_counter = {'ETH': 0,
                            'BTC': 0,
                            'LTC': 0,
                            'DSH': 0}
    authors = {}
    sources = {}


    for file in json_files:

        data = get_json_as_dict(file_name=file)
        currency = data[0]['cryptoCurrency']['currency']
        tag_currency_counter[currency] += len(data)
        """
        counting articles per author
        """
        # for article in data:
        #     author = article['metaInfo']['author']
        #     author_array = []
        #     if ',' in author:
        #         author_array = author.split(',')
        #     else:
        #         author_array.append(author)
        #
        #     for person in author_array:
        #         if author in authors:
        #             authors[person] += 1
        #         else:
        #             authors[person] = 1
        """
        counting articles per platform
        """
        for article in data:
            source = article['source']
            if 'https' in source:
                source = source.split('https://')[1]

            if 'http' in source:
                source = source.split('http://')[1]
            source = source.split("www.")[-1].split('/')[0]

            combi = source+" "+currency
            if combi in sources:
                sources[combi] += 1
            else:
                sources[combi] = 1

    s = [(k, sources[k]) for k in sorted(sources, key=sources.get, reverse=True)]
    print(s)
    print(tag_currency_counter)
    with open('../output/stats/platform_currency_counter', 'wb') as fout:
        pickle.dump(s, fout)



if __name__ == '__main__':
    bitcoinde()
    with my_main(__file__):
        #stat_articles_per_file()
        #convert_jl_json_to_json("/home/tobias/Dokumente/Crypto-News-Projekt/output/news/v_2/btc_cointelegraph2.json")
        pass

        # pass
        # data = get_json_as_dict(file_name="/home/tobias/Dokumente/Crypto-News-Projekt/output/news/merged.json")
        # #print(len(data))
        # for i, el in enumerate(data,0):
        #     body = el['body']
        #     if body == 'None':
        #         if 'bitcoinmagazine' in el['source']:
        #             print(el['source'])


            # if '/r/' in keywords:
            #     keywords = keywords.replace('/r/', '')
            #     print(keywords)
            # if keywords and keywords != 'none':
            #     keywords = keywords.replace('\r', '').replace('\n','').replace('\t','')
            # else:
            #     keywords = ''


        #     data[i]['metaInfo']['keywords']= keywords
        # new_save_as_json(output=data, filename="/home/tobias/Dokumente/Crypto-News-Projekt/output/news/merged.json",
        #                  as_array=True)






