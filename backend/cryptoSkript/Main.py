import os
import requests
from pathlib import Path
import argparse
import codecs

exchanges = "exchanges"
timeSeriesValues = "timeSeriesValues"
websites = "websites"
countryCurrencies = "countryCurrencies"

exchangeUrl = "http://127.0.0.1:8080/assets/crypto-currency-exchange-complete"
timeSeriesValuesUrl = "http://127.0.0.1:8080/assets/time-series-value"
websitesUrl = "http://127.0.0.1:8080/assets/website-data-list"
countryCurrencyUrl = "http://127.0.0.1:8080/assets/country-currency-list"

def readFilesFromDir(dir):

    return os.listdir(dir)

def readFile(path):

    file = codecs.open(path, "r", "utf-8")# open(path, 'r')
    data = file.read().encode('utf-8')
    file.close()
    return data

def sendPost(url, data):
    headers = {'Content-Type': 'application/json', 'Accept': 'text/plain', 'User-Agent': 'python-requests/2.4.3 Python/3.5.0'}
    return requests.post(url, data=data, headers=headers)

def init():

    dirs = [exchanges, timeSeriesValues, websites, countryCurrencies]
    urls = [exchangeUrl, timeSeriesValuesUrl, websitesUrl, countryCurrencyUrl]
    code = 0
    if (len(dirs) == len(urls)):
        for idx, dirr in enumerate(dirs):
            files = readFilesFromDir(dirr)
            url = urls[idx]
            for file in files:
                data = readFile(dirr + '/' + file)
                print("Post: ", file, "to url: ", url)
                response = sendPost(url, data)
                print("Response code:", response.status_code)
                print("Response content: ", response.text)
                print("===================================")
                print("\n")
                code = response.status_code
                if (response.status_code != 201):
                    break
            else:
                continue
            break
        if(code == 201):
            print("Init Saved complete")
        else:
            print("Files not saved")

def saveFiles(path, url):

    path = Path(path)
    if (path.is_file()):
        data = readFile(path.__str__())
        print("Post: ", path.name, "to url: ", url)
        response = sendPost(url, data)
        print("Response code:", response.status_code)
        print("Response content: ", response.text)
        if (response.status_code != 201):
            print("File not saved: ", path.name)
        else:
            print("File saved: ", path.name)
        print("===================================")

    else:
        if (path.is_dir()):
            files = readFilesFromDir(path.__str__())
            for file in files:
                data = readFile(path.name + '/' + file)
                print("Post: ", file, "to url: ", url)
                response = sendPost(url, data)
                print("Response code:", response.status_code)
                print("Response content: ", response.text)
                if (response.status_code != 201):
                    print("File not saved: ", file)
                else:
                    print("File saved: ", file)
                print("===================================\n")
        else:
            print("Parameter data not found: ", path.name, "\n")

def main():

    parser = argparse.ArgumentParser()
    parser.add_argument("--e", help="Create one or more exchanges")
    parser.add_argument("--t", help="Create one or more timeSeriesValues")
    parser.add_argument("--w", help="Create one or more websites")
    parser.add_argument("--c", help="Create one or more country courrencies")
    args = parser.parse_args()

    if (args.e):
        saveFiles(args.e, exchangeUrl)
    elif (args.t):
        saveFiles(args.t, timeSeriesValuesUrl)
    elif (args.w):
        saveFiles(args.w, websitesUrl)
    elif (args.c):
        saveFiles(args.c, countryCurrencyUrl)
    else:
        init()

if __name__ == "__main__":
   main()
