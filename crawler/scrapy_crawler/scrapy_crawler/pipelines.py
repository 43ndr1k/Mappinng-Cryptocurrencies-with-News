# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html


class CoindeskPipeline(object):
    def process_item(self, item, spider):
        return item


from scrapy.exporters import JsonItemExporter

class Utf8JsonItemExporter(JsonItemExporter):

    def __init__(self, file, **kwargs):
        super(Utf8JsonItemExporter, self).__init__(
            file, ensure_ascii=False, **kwargs)


# class JsonWithEncodingPipeline(object):
#
#     def __init__(self):
#         self.file = open(spider.name + '.json', 'wb')
#         self.exporter = JsonItemExporter(self.file, encoding='utf-8', ensure_ascii=False)
#         self.exporter.start_exporting()
#
#     def spider_closed(self, spider):
#         self.exporter.finish_exporting()
#         self.file.close()
#
#     def process_item(self, item, spider):
#         self.exporter.export_item(item)
#         return item