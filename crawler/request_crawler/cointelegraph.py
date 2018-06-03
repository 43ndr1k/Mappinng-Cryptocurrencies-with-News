# __filename__: cointelegraph.py
#
# __description__: Spider for cointelegraph.com. v2
#
# __remark__:
#
# __todos__:
#
# Created by Tobias Wenzel in October/November 2017


import requests
import time
import sys
from lxml import etree
from datetime import datetime as dt, timedelta
import urllib.request

import numpy as np

"""------------------------------------------------------
    import own functions/modules
------------------------------------------------------"""
sys.path.append("/home/tobias/mygits/crypto-news-docs/crawler/helper")
from my_constants import *
from script_structure import my_main, json_context_mgnr
from language_processor import get_text_of_n_words
from my_logging import *

"""------------------------------------------------------
   module settings
------------------------------------------------------"""

output_folder = "/home/tobias/mygits/crypto-news-docs/crawler/output/news/update/" # for testing (-> main)
disclaimer = """The views and opinions expressed here are solely those of authors/contributors and do not necessarily reflect the views of Cointelegraph.com. Every investment and trading move involves risk, you should conduct your own research when making a decision."""


class CoinTelegraphSpider:

    page = 1
    href_bag = []
    REQUEST_URL = "https://cointelegraph.com/api/v1/ajax/tags/next"

    allowed_domains = ['cointelegraph.com']

    def __init__(self, currency='bitcoin', start_date=None, stop_date=None):
        """
                possible: bitcoin, ethereum, ripple, dash, litecoin
        :param currency:
        :param start_date:
        :param stop_date:
        """

        self.my_logger = get_my_logger()
        self.random_pause = [0.2, 5.5]

        self.currency = currency
        self.currency_abbr= ""
        if self.currency == "bitcoin":
            self.currency_abbr = BITCOIN
        elif self.currency == "ripple":
            self.currency_abbr = RIPPLE
        elif self.currency == "dash":
            self.currency_abbr = DASH
        elif self.currency == "ethereum":
            self.currency_abbr = ETHEREUM
        elif self.currency == 'litecoin':
            self.currency_abbr = LITECOIN
        else:
            raise NotImplementedError

        self.output_file = self.currency_abbr + "_cointelegraph.json"
        self.start_date = start_date
        self.stop_date = stop_date

        self.my_logger.info("start %s"% __class__)
        self.my_logger.info("crawl {} {} until {}".format(self.currency, self.stop_date,self.start_date))
        self.file_dirty = False

    def set_dirty_flag(self):
        if os.path.isfile(self.output_file):
            self.file_dirty = True

    def parse_description(self, url):
        """

        :param url:
        :return:
        """

        try:
            response = urllib.request.urlopen(url)
            tree = etree.HTML(response.read())
        except UnicodeEncodeError:
            # @todo
            return "None","None"
        description = ""
        i = 0
        while len(description) < CHARACTER_NUMBER:
            news_text = " ".join(tree.xpath("""//div[@class="post-full-text contents"]/descendant::*/text()""")[:i])

            description = news_text.replace(disclaimer, '')
            if i == 10:
                #print(response.url)
                break
            i += 1
        if description:
            description = get_text_of_n_words(description, n=WORD_NUMBER)  # see helper.language_processor

        keyword_string = ""
        keywords = tree.xpath("""//div[contains(@class,"tags")]/ul/li/a/text()""")
        for i, keyword in enumerate(keywords,0):
            keyword_string += keyword.strip()
            if i != len(keyword_string) - 1 and keyword.strip():
                keyword_string += ", "

        if not keyword_string:
            keyword_string = 'None'
        elif len(keywords) > 3:
            keyword_string = ",".join(keyword_string.split(",")[:3])

        return description, keyword_string.strip().rstrip(",")

    def request_crawl(self):
        """

        :return:
        """

        try:
            response = requests.post(self.REQUEST_URL, {'page': str(self.page),
                                                        'tag': self.currency})
        except requests.exceptions.ConnectionError:
            return

        if response is not None:

            try:
                articles = response.json()['recent']

                if articles:
                    for post in articles:

                        link = post['url']
                        date_string = post['created']

                        if date_string.endswith('AGO'):
                            date = dt.today()
                        else:
                            date = dt.strptime(date_string, '%b %d, %Y')

                        if date >= self.start_date:

                            if date <= self.stop_date or self.stop_date == self.start_date:

                                if link not in self.href_bag:
                                    self.my_logger.debug(str(date) + "\t" + post['title'])
                                    description, keywords = self.parse_description(link)
                                    date = date.strftime(DATE_FORMAT)
                                    article = {
                                        'body': description,
                                        'title': post['title'],
                                        'source': link,
                                        'metaInfo': {
                                            'author': post['author'],
                                            'language': 'english',
                                            'pageTopic': keywords.split(",")[0], # first is marked as 'super-tag'~topic
                                            'keywords': keywords.lower()
                                        },
                                        'date':  date,
                                        'cryptoCurrency': {'currency': self.currency_abbr}
                                    }

                                    with open(self.output_file, "ab") as fout:
                                        if self.file_dirty:
                                            fout.write(",\n".encode('utf-8'))
                                        fout.write(json.dumps(article,
                                                              separators=(',', ':'),
                                                              sort_keys=True, indent=2, ensure_ascii=False).encode('utf8'))
                                        self.file_dirty = True

                        else:
                            return

                    t = np.random.uniform(self.random_pause[0], self.random_pause[1])
                    time.sleep(t)
                    self.page += 1
                    self.request_crawl()
                elif not articles and self.page > 150:
                    return

            except json.decoder.JSONDecodeError as e:
                print(e.with_traceback())
            except KeyboardInterrupt:
                return


if __name__ == '__main__':
    # only used for testing.
    # start from controller!

    with my_main(__file__):


        overwrite = True

        stop_date = dt(2018, 1, 21)
        delta = timedelta(days=7)
        start_date = stop_date - delta

        if start_date == stop_date:
            time_span = start_date.strftime(DATE_FORMAT) +"_"
        elif start_date < stop_date:
            time_span = "{}_{}_".format(start_date.strftime(DATE_FORMAT), stop_date.strftime(DATE_FORMAT))
        else:
            raise ValueError('choose start_data < stop_date')

        for currency in ['litecoin', 'ethereum','bitcoin','dash']:

            msg = "<b>" + str(__file__)+"\t" + str(__name__) + "</b>\n"
            msg += "start crawler {} from {} until {} done. ".format(currency, start_date,stop_date)

            coin_telegraph_spider = CoinTelegraphSpider(currency=currency,
                                                        start_date=start_date,
                                                        stop_date =stop_date)
            coin_telegraph_spider.output_file = output_folder + time_span + coin_telegraph_spider.output_file

            with json_context_mgnr(filename=coin_telegraph_spider.output_file,
                                   overwrite=overwrite):
                coin_telegraph_spider.request_crawl()

            msg = "<b>" +str(__file__)+"\t"+ str(__name__) + "</b>\n"
            msg += "downloaded {} from {} until {} done. ".format(currency,start_date,stop_date)
            coin_telegraph_spider.my_logger.info(msg)








