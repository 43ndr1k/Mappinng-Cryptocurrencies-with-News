from nltk.tokenize import sent_tokenize
from nltk.tokenize import word_tokenize


def get_text_of_n_words(text="",n=200):
    """
    :param text:
    :param n:
    :return:
    """
    sentences = sent_tokenize(text=text)
    res = ""
    words_already = 0
    for sent in sentences:
        res += str(sent) + " "
        words_already += len(word_tokenize(sent))
        if words_already > n:
            break

    return res


def clean_string(text=""):
    """

    :param text:
    :return:
    """
    text = text.strip().replace('\n', '')


    return text

if __name__ == '__main__':
    print(get_text_of_n_words("""368
down vote.
If it's just a substring search you can use string.find("substring").
You do have to be a little careful with find, index, and in though, as they are substring searches. In other words, this:"""))