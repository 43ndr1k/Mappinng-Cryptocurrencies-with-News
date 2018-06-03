

import logging
import logging.config

import os
import json

LOG_FILE = "/home/tobias/mygits/crypto-news-docs/crawler/scrapy_crawler/scrapy_crawler/spider.log"


def setup_logging(
    default_path='',
    default_level = logging.INFO,
    env_key='LOG_CFG'
):
    """Setup logging configuration

    """
    path = default_path
    value = os.getenv(env_key, None)
    if value:
        path = value
    if os.path.exists(path):
        with open(path, 'rt') as f:
            config = json.load(f)
        logging.config.dictConfig(config)
    else:
        logging.basicConfig(level=default_level)


def get_my_logger(fout_name=LOG_FILE):
    logger = logging.getLogger(__name__)
    logger.setLevel(logging.INFO)

    handler = logging.handlers.RotatingFileHandler(fout_name)
    handler.setLevel(logging.DEBUG)

    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
    handler.setFormatter(formatter)

    logger.addHandler(handler)

    logging.basicConfig(level=logging.INFO)
    return logger


