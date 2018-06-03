# __filename__: date_helper.py
#
# __description__: some help functions to generate time series etc
#
# __remark__:
#
# __todos__:
#
# Created by Tobias Wenzel in November 2017



from datetime import datetime as dt, timedelta
from my_constants import *

def utc_to_str(utc_date):
    """

    :param utc_date:
    :return:
    """
    return dt.fromtimestamp(int(utc_date)/1000).strftime(DATE_FORMAT)


def datespan(start_date, end_date, delta=timedelta(days=1)):
    """

    :param start_date:
    :param end_date:
    :param delta:
    :return:
    """
    current_date = start_date
    while current_date < end_date:
        yield current_date
        current_date += delta


def get_date_time_tz(date_string=""):
    """

    :param date_string:
    :return:
    """
    parts = date_string.split(" ")
    try:
        res = parts[0]+"T"+parts[1]+"Z"
    except IndexError:
        res = date_string
    return res


def timestamp_to_date(my_timestamp):
    """

    :param my_timestamp:
    :return:
    """

    return dt.strptime(my_timestamp.strip().split("T")[0], DATE_FORMAT)


def get_formated_date(date):
    """

    :param date:
    :return:
    """
    return date.strftime(DATE_FORMAT)


def reverse_format(date):
    """

    :param date:
    :return:
    """
    return dt.strptime(date, DATE_FORMAT)