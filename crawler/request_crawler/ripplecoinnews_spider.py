# __filename__: cointelegraph.py
#
# __description__: Spider for ripplecoinnews.com. Not used.
#
# __remark__: hat mit scrapy noch nicht so ganz funktioniert, das geht aber.
# attributes: title, datetime, href __NOT on main page: author and description
#
# __todos__:  load-more button ist tricky!
#
# Created by Tobias Wenzel in October/November 2017
# Copyright (c) 2017 Tobias Wenzel


import requests
import time
import numpy as np
from lxml import etree
import sys
sys.path.append("../helper")

from helper.json_helper import save_as_json

DATE_FORMAT = "%Y-%m-%d"

class RippleCoinNewsSpider():

    page = 1
    href_bag = []
    REQUEST_URL = "https://www.ripplecoinnews.com/wp-admin/admin-ajax.php?_wpnonce=2ed8421096"
    allowed_domains = ['ripplecoinnews.com']
    article_dicct_array = []

    def __init__(self, year=2017):
        """
        :param year:
        :param crawl_recent:
        :param currency:
        """
        self.random_pause = [0.2, 2.5]
        self.crawl_recent = True
        self.year = year
        self.currency = 'XRP' #currency


        self.output_file = self.currency + "ripplecoinnews.json"

    def request_crawl(self):
        """

        :return:
        """
        try:
            response = requests.post(self.REQUEST_URL, {'action': 'load_post',
                                                        'cat':'ripple-news',
                                                        'col':'col-xs-6+col-sm-6+col-md-4',
                                                        'layout':'grid',
                                                        'page':'1'})

        except requests.exceptions.ConnectionError:
            return

        if response is not None:

            tree = etree.HTML(response.text)

            for post in tree.xpath("""//div[contains(@class,"article-content")]"""):
                """
                ich kann nicht wie bei scrapy einfach das resulat weiterverarbeiten
                -> vllt finde ich da ja noch ne andere lösung
                """
                article_href = post.xpath("""div[starts-with(@class,"entry-header")]/h3/a/@href""")[0]
                if article_href not in self.href_bag:

                    self.article_dicct_array.append({
                       # 'href': article_href,
                        'title': post.xpath("""div[starts-with(@class,"entry-header")]
                    /h3/a/text()""")[0],
                        'currency': self.currency,
                        'metaInfo': {
                            'author': post.xpath("""div[starts-with(@class,"entry-meta")]
                                        /span/span/a/text()""")[0],
                            'pagetopic': 'None',
                            'keywords': 'None',
                            'language': 'english'
                        },
                        # 'timestamp': {
                        #     'timestamp': post.xpath("""div[starts-with(@class,"entry-meta")]
                        #                /span[contains(@class,"posted-on")]
                        #                /a/time[contains(@class,"entry-date published")]/@datetime""")[0]
                        # },
                        'date' : post.xpath("""div[starts-with(@class,"entry-meta")]
                                       /span[contains(@class,"posted-on")]
                                       /a/time[contains(@class,"entry-date published")]/@datetime""")[0], # >v.6
                        'source': article_href,
                        'body': 'None'



                    })
                else:
                    return
            print(self.page)
            t = np.random.uniform(self.random_pause[0], self.random_pause[1])
            time.sleep(t)
            self.page += 1
            #self.request_crawl()




def main(argv):
    # try:
    #     opts, args = getopt.getopt(argv, "ht:c:", ["time=", "currency="])
    # except getopt.GetoptError:
    #     print('cointelegraph.py -t <year/recent> -c <currency>')
    #     sys.exit(2)
    #
    # time_span = ''
    # currency = ''
    # for opt, arg in opts:
    #     if opt == '-h':
    #         print('ripplecoinnews_spider.py -t <year/recent> -c <currency>')
    #         sys.exit()
    #     elif opt in ("-t", "--time"):
    #         time_span = arg
    #     elif opt in ("-c", "--currency"):
    #         currency = arg
    #     else:
    #         time_span = 'recent'
    #         currency = 'Ripple'
    #
    # if time_span == 'recent':
    #     year = 2017
    #     crawl_recent = True
    # else:
    #     year = time_span
    #     crawl_recent = False

    print("ripplecoinnews-scraper")
   # print("Start to crawl for news about {} in timespan {}".format(currency,time_span))

    ripplecoinnews_spider = RippleCoinNewsSpider(year='2017')

    start = time.time()
    ripplecoinnews_spider.request_crawl()
    save_as_json(filename="../output/" + ripplecoinnews_spider.output_file, as_array=True, output=ripplecoinnews_spider.article_dicct_array)
    # with open("../output/" + ripplecoinnews_spider.output_file, 'w') as f:
    #     json.dump(ripplecoinnews_spider.article_dict_array, f)
    # end = time.time() - start
    #print('finished crawl after %f sec' % end)

if __name__ == '__main__':
    main(sys.argv[1:])
