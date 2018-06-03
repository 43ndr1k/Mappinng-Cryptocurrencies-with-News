from contextlib import contextmanager
from time import time
import sys

@contextmanager
def my_main(filename):
    """
    call like with my_main(__file__): do_stuff()
    :param filename:
    :return:
    """
    print("start %s"%filename)
    before = time()
    try:
        yield
    finally:
        after = time()
        print("running %s \n\t took me\t %2.2f sec" % (filename, after - before))



@contextmanager
def json_context_mgnr(filename, overwrite):
    """
    needed for cointelegraph to encapsulate in '[,]'
    :param filename:
    :param overwrite:
    :return:
    """
    if overwrite:
        with open(filename, "wb") as fout:
            fout.write('[\n'.encode('utf-8'))
    else:
        raise NotImplementedError
    try:
        yield
    finally:
        if overwrite:
            with open(filename, "ab") as fout:
                fout.write('\n]'.encode('utf-8'))
        else:
            raise NotImplementedError

