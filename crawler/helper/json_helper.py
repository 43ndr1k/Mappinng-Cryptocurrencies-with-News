# -*- coding: latin-1 -*-
# __filename__: json_helper.py
#
# __description__:
#
# __remark__:
#
# __todos__:
#
# Created by Tobias Wenzel in November 2017

import json
import codecs
import glob


def new_save_as_json(output, filename="", as_array=True):
    """

    :param output:
    :param filename:
    :return:
    """

    with open(filename, "wb") as fout:
        if as_array:
            fout.write(json.dumps([k for k in output],
                                  separators=(',', ':'),
                                  sort_keys=True, indent=2,ensure_ascii=False).encode('utf8'))
        else:
            fout.write(json.dumps(output,
                                  separators=(',', ':'),
                                  sort_keys=True, indent=2, ensure_ascii=False).encode('utf-8'))


def save_as_json(output, filename="", as_array=True):
    """

    :param output:
    :param filename:
    :return:
    """

    print("please use new_save_as_json-function")

    with open(filename, "wb") as fout:
        if as_array:
            fout.write(json.dumps([k for k in output],
                                  separators=(',', ':'),
                                  sort_keys=True, indent=2, ensure_ascii=True))
        else:

            fout.write(json.dumps(output,
                                  separators=(',', ':'),
                                  sort_keys=True, indent=2, ensure_ascii=True))


def get_json_as_dict(file_name=None):
    """

    :param file_name:
    :return:
    """
    if file_name is None:
        raise ValueError('Please submit filename!')
    with codecs.open(file_name, "r", "utf-8") as fin:
        try:
            return json.loads(fin.read().encode('utf-8').decode())
        except json.decoder.JSONDecodeError:
            print(file_name)


def get_lines(lines=[], file_name=""):
    """
    ... of json file
    :param lines:
    :param file_name:
    :return:
    """
    lines = []
    with open(file_name, 'r') as fin:
        for line in fin:
            try:
                obj = json.loads(line)
                lines.append(obj)
            except json.decoder.JSONDecodeError as e:
                pass

    if len(lines) == 0 or lines is None:
        with open(file_name, 'r') as fin:
            return json.load(fin)
    else:
        return lines


def create_cryptowatch_base_quote_json():
    """
    -> cryptowat.ch
    helper function to create simple json file with dicct of pairs
    :return:
    """
    pairs = {}
    with open("../input/pairs_cryptowatch.json", 'r') as fin:
        pairs = json.load(fin)['result']

    my_pairs = {}
    for pair in pairs:
        my_pairs[pair['symbol']] = {
            'base': pair['base']['symbol'],
            'quote': pair['quote']['symbol']
        }
    save_as_json(as_array=False, output=my_pairs, filename="../input/mypairs_cryptowatch.json")


def get_cryptowatch_exchanges():
    """
    -> cryptowat.ch
    for information purposes
    :return:
    """
    json_files = glob.glob("../input/currency_info/" + "*.json")
    currency_at_exchange = {}
    for file in json_files:
        with open(file, 'r') as fin:
            data = json.load(fin)

        my_set = set()
        for market in data['result']['markets']['base']:
            my_set.add(market['exchange'])
        currency_symbol = data['result']['symbol']
        currency_at_exchange[currency_symbol] = [el for el in my_set]
    save_as_json(output=currency_at_exchange, as_array=False, filename="../input/currency_at_exchange.json")


def merge_news_json_files(input_folder="", output_folder=""):
    """

    :param input_folder:
    :return:
    """
    json_files = glob.glob(input_folder + "*.json")

    for i, file in enumerate(json_files,0):
        if i == 0:
            data = get_json_as_dict(file_name=file)
        else:
            data.extend(get_json_as_dict(file_name=file))
    new_save_as_json(output=data, filename=output_folder, as_array=True)



def merge_simple_json_files(scr_file, fname2, output_file_name):
    """
    merge two files (two different methods are used to load json)
    :param scr_file:
    :param fname2: 
    :param output_file_name: 
    :return: 
    """
    if scr_file and fname2 and output_file_name:
        data = get_lines(file_name=scr_file)
        try:
            data.extend(get_json_as_dict(fname2))
        except TypeError:
            data = get_json_as_dict(fname2)

        new_save_as_json(output=data, filename=output_file_name, as_array=True)



def chunk_news_json(fin_nam, output_folder, file_number=20):
    """
    helper function to chunk large json-file into smaller portions.
    :param fin_nam:
    :param output_folder:
    :param file_number:
    :return:
    """
    site_data = get_json_as_dict(file_name=fin_nam)

    num_sites = len(site_data)
    #print(num_sites)
    chunk_size = num_sites/file_number
    chunk_nr= 1
    page_id = 0

    while page_id < num_sites:
        temp_chunk = []

        chunk_i = 0

        while chunk_i < chunk_size and page_id < num_sites:
            temp_chunk.append(site_data[page_id])
            chunk_i += 1
            page_id +=1

        new_save_as_json(temp_chunk, filename=output_folder+"news_"+str(chunk_nr)+".json", as_array=True)
        chunk_nr += 1


if __name__ == '__main__':
    pass
    #merge_news_json_files(foldername="/home/tobias/Dokumente/Crypto-News-Projekt/output/news/v_2/")
    chunk_news_json(fin_nam="/home/tobias/Dokumente/Crypto-News-Projekt/output/news/merged.json",
                    output_folder="/home/tobias/Dokumente/Crypto-News-Projekt/output/news/splitted/",
                    file_number=20)
