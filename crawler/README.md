# crypto-news-docs 
## Crawler
The crawler (also: scraper) consists of two main components:
- the news-scraper(s)
- the currency scrapers.

The Code is written in Python3 and run using Linux Mint 18.3 (64 bit) respectively Raspbian (release date: 2017-11-29).
The main dependencies are:
```
pip3 install Scrapy==1.4.0
pip3 install nltk==3.2.2
pip3 install numpy==1.12.1
pip3 install requests==2.14.2
```
The versions should be adjusted or left out to get the newest working. If possible use a Python3 Virtual Environment.
See backend/readme.md for example outputs.

### Components
#### News-Crawler
The main part of the news-crawler were written with scrapy. It's possible to adjust the time span which is being crawled to enable run them via a controller (&rightarrow;  **Controller**) each day/week/month.
For a given time interval the article-links are collected by reading the news-feed page (often main page). In a second
step relevant information is extracted of the article page.
 
- [Bitcoinmagzine](https://bitcoinmagazine.com/) (BTC)
- [Coindesk](https://www.coindesk.com/) (BTC)
- [Cryptocoin News](https://www.ccn.com/) (BTC, ETH)
- [Ethnews](https://www.ethnews.com/) (ETH)
- [Newsbitcoin](https://news.bitcoin.com/) (BTC)

The scrapers can be found in the [scrapy spiders folder](./scrapy_crawler/scrapy_crawler/spiders).

Two more crawler sites were implemented in a similar way, calling APIs.
- [Cointelegraph](https://cointelegraph.com/) (BTC, ETH, LTC, XRP)
- [Ripplecoin News](https://www.ripplecoinnews.com/) (XRP &rightarrow; not used)

The scrapers can be found in the [request crawler](./request_crawler).


#### Currency-Crawler
The currencies are obtained by requesting APIs of 
- [Crypto-Wat.ch](https://api.cryptowat.ch/markets) and 
- [ECB](https://sdw-wsrest.ecb.europa.eu/web/generator/index.html) (historical data available!)
- Bitfinex: same as Crypto-Wat.ch but also historical data available (&rightarrow; only Bitfinex). Not used at this moment.

every day. It's possible to change this interval.
The scripts to call the APIs can also be found in the [request crawler folder](./request_crawler).

To run the scripts daily run the scripts as cron-jobs. For this we used a Raspberry Pi 2.

```
crontab -e 
# python3  ~/crypto-news-docs/crawler/request_crawler/ecb_api_script.py 
# python3  ~/crypto-news-docs/crawler/request_crawler/cryptowatch_api_script.py 
```
Alternatively this can be achieved by creating a file (adjust the paths) in the /etc/cron.daily folder. This would run the scripts on a daily basis shortly after each reboot,
which can be handy if the crawler runs on a laptop.


#### Controller
Although each script is designed to also run standalone, 
it's advised to use the controller ([found here](./scrapy_crawler/scrapy_crawler/spider_controller.py)). 
Adjust the time span and if needed the cryto-currencies which are to be crawled (sites like Cointelegraph offer news for various crypto-currencies).

The controller then runs each scraper and combines the result in a file labeled with the time interval.
The output can be either saved for testing purposes in the [output](./output)-folder in the crawler or directly in the python script folder (&rightarrow; backend).

Call the controller using the command line or create a file like explained in the currency [currency-crawler section](####Currency-Crawler).
```
python3 ~/crypto-news-docs/crypto-crawler/scrapy_crawler/scrapy_crawler/spider_controller.py
```



#### helper


Holds functions to process the output downloaded by the crawlers described above.
Don't have to be called in a working environment.

- date-helper.py - date-formatting.
- my_logging.py - logging adjustments.
- script_structure.py - context-manager for main and opening/saving json-files.
- language_processing.py - nltk-functions to retrieve a descriptive text-passage of the text-body.
- json_builder.py - creating json-files in the desired output-format.
- json_helper.py - loading, saving and processing json-files 



