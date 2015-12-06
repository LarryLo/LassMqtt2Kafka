#!/usr/bin/env python

import re
import json
import sys
import os.path
import argparse
#import request
from datetime import datetime
from elasticsearch import Elasticsearch, helpers
from elasticsearch_dsl import DocType, String, Boolean
from kafka import SimpleProducer, KafkaClient
from elasticsearch_dsl.connections import connections
connections.create_connection(hosts=['52.74.47.196', ])

INDEX_PREFIX = "analysis-"
ES_CLUSTER = 'http://localhost:9200/'
ES_INDEX = 'test'
ES_TYPE = 'log'


class ESconnection:
    def __init__(self):
        # self.json_datas=[]
        self.es = Elasticsearch(ES_CLUSTER)

        # es.index(index="my-index", doc_type="test-type", id=42, body={"any": "data", "timestamp": datetime.now()})
        # {u'_id': u'42', u'_index': u'my-index', u'_type': u'test-type', u'_version': 1, u'ok': True}

        # es.index(index='posts', doc_type='blog', id=3, body={
        # 'author': 'Benjamin Pollack',
        # 'blog': 'bitquabit',
        # 'title': 'How to Write Clickbait Titles About Git Being Awful Compared to Mercurial',
        # 'topics': ['mercurial', 'git', 'flamewars', 'hidden messages'],
        # 'awesomeness': 0.95

    def send_to_es(self, index_name, json_docs):
        #es = Elasticsearch(index=index_name, body={'refresh_interval': '5s'})
        # self.es.indices.create()
        # self.es.bulk(json_docs, index_name, ES_TYPE, raise_on_error=True)

        """
        entry_mapping = {
            'entry-type': {
                'properties': {
                    'id': {'type': 'integer'},
                    'published': {'type': 'date'},
                    'title': {'type': 'string'},
                    'tags': {'type': 'string', 'analyzer': 'keyword'},
                    'content': {'type': 'string'}
                }
            }
        }
        """
        """
        es.indices.create(
        index='blog-index',
        body={
            'mappings': {
                'blog-entry-type': {
                    'properties': {
                        'id': {'type': 'integer'},
                        'title': {'type': 'string'},
                        'content': {'type': 'string'},
                        'tags': {'type': 'string'},
                        'created': {'type': 'date'}
                    }
                }
            }
        }
        """
        if self.es.indices.exists(index_name):
            print "deleting '%s' index..." % (index_name)
            print(self.es.indices.delete(index= index_name, ignore=[400, 404]))
            #self.es.indices.delete(index_name)


        self.es.indices.create(
            index=index_name,
            body={
                'mappings': {
                    'sensor': {
                        'properties': {
                            'date': {'type': 'date', "format": "strict_date_optional_time||epoch_millis"},
                            'datetime': {'type': 'date', "format": "strict_date_optional_time||epoch_millis"},
                            'location': {'type': 'geo_point'}
                        }
                    }
                }
            }
        )

        #self.es.indices.create(index_name)

        #self.es.health(wait_for_status='yellow')
        #self.es.create(index=index_name, settings={'mappings': entry_mapping})

        print "TO_ES %s %d" % (index_name, len(json_docs))
        """
        aa=self.es.index(index='posts', doc_type='blog', id=1, body={
            'author': 'Santa Clause',
            'blog': 'Slave Based Shippers of the North',
            'title': 'Using Celery for distributing gift dispatch',
            'topics': ['slave labor', 'elves', 'python',
                       'celery', 'antigravity reindeer'],
            'awesomeness': 0.2
        })
        """

        actions = []
        '''
        for doc in json_docs:
            action = {
                "_index": index_name,
                "_type": "test",
                "_id": doc_index + 1,
                "_source": json_docs[doc]
            }
            doc_index = doc_index + 1
        '''
        for idx, doc in enumerate(json_docs):
            action = dict(_index=index_name, _type="test", _id=idx + 1, _source=json_docs[idx])
            actions.append(action)
            #actions.append(action)


        if len(actions) > 0:
            #self.es.bulk(index=index_name, body=actions)
            helpers.bulk(self.es, actions)

        #self.es.bulk(index,index_name,"test",actions)
        #def bulk(self, body, index=None, doc_type=None, params=None):

        # self.es.bulk()
        # self.es.index(index=index_name, doc_type='test', id=1, body=json_docs[1])
        #print aa
        # self.es.delete()

class KFconnection:
    def __init__(self):
        pass

    def send_to_kafka():
        pass


class MGconnection:
    def __init__(self):
        pass

    def send_to_mango():
        pass


class Datas:
    def __init__(self):
        self.value_dict={}
        self.json_datas=[]
        self.datatime=0
        self.gps_lon=0.0
        self.gps_lat=0.0
        self.gps_alt=0.0

    def reset(self):
        self.datas=[]

    def gps_to_map(self,x):
        x_m = (x -int(x))/60*100*100
        x_s = (x_m -int(x_m))*100
        gps_x = int(x) + float(int(x_m))/100 + float(x_s)/10000
        return gps_x

    def parse_one(self, line):
        cols = line.split("|")
        self.value_dict = {}
        for element in cols:
            col_pair = element.split("=")
            if len(col_pair) >= 2:
                self.value_dict[col_pair[0]] = col_pair[1]

        # [value for key, value in programs.items() if 'new york' in key.lower()]:
        # [float(self.value_dict(k)) for (k, v) in self.value_dict.iteritems() if 's_' in k]
        # for (k, v) in self.value_dict.iteritems() if 's_' in k:
        for (k, v) in self.value_dict.iteritems():
            if k.startswith('s_'):
                self.value_dict[k] = float(self.value_dict[k])

        if all (key in self.value_dict for key in ("gps_lat","gps_lon","gps_alt","date","time")):
            # print self.value_dict.keys()
            gps_lat = self.value_dict["gps_lat"]
            gps_lon = self.value_dict["gps_lon"]
            gps_alt = self.value_dict["gps_alt"]
            if not bool(re.search(r'\d', gps_alt)): gps_alt=0
            #print "%s : %s : %s" % (gps_lat,gps_lon,gps_alt)
            self.value_dict["gps_lat"] = self.gps_to_map(float(gps_lat))
            self.value_dict["gps_lon"] = self.gps_to_map(float(gps_lon))
            self.value_dict["gps_alt"] = self.gps_to_map(float(gps_alt))
            self.value_dict["gps_fix"] = int(self.value_dict["gps_fix"])
            # self.value_dict["datetime"] = str( datetime.now() )

            self.value_dict["datetime"] = datetime.strptime(self.value_dict["date"] + " " +
                                                            self.value_dict["time"], '%Y-%m-%d %H:%M:%S').isoformat()
            # "yyyy-MM-dd'T'HH:mm:ssZ"
            # self.value_dict["datetime"] = datetime.fromtimestamp(record_time).strftime('%Y-%m-%d %H:%M:%S')
            # self.value_dict["datetime"] = record_time.isoformat()
            return self.value_dict
        else:
            return None

    def add(self, line):
        record_obj = json.dumps(self.parse_one(line))
        self.json_datas.append(record_obj)
        return record_obj


def load_raw(filename):
    raw_file = open(filename, 'r')
    esData.json_datas=[]
    cnt_record = 0
    for line in raw_file:
        if esData.add(str(line)): cnt_record = cnt_record + 1
    print cnt_record
    raw_file.close()
    return cnt_record


def main():
    parser = argparse.ArgumentParser()
    global esData

    esData = Datas()
    esSender = ESconnection()

    parser.add_argument('-l','--logfile', metavar=("logfile"),help="input lass logfile with directory")
    parser.add_argument('-i','--index', metavar=("logfile"),default=INDEX_PREFIX,help="ElasticSearch index prefix")
    #parser.add_argument('-i','--index', metavar=("index"),required=False,default=None,help="index name")
    parser.add_argument('-t','--testconnect',action='store_true', default=None, help="Run offline unit test.")
    parser.add_argument('-td','--testdev', action='store_true',required=False,default=None,help="Run test for one record.")
    args = parser.parse_args()

    if args.logfile is not None:
        if os.path.isfile(args.logfile):
            index_date = args.logfile.split('-')
            #print "qq  %s" % datetime[1]
            load_raw(args.logfile)
            esSender.send_to_es(INDEX_PREFIX + index_date[1], esData.json_datas)
            #print esData.json_datas
        else:
            print "Please specify a correct filename."

    if args.testdev is not None:
        test_log = "LASS/Test/PM25 |ver_format=3|fmt_opt=0|app=PM25|ver_app=0.7.10|device_id=FT1_035|tick=141284566|" \
                   "date=2015-11-22|time=06:26:30|device=LinkItONE|s_0=14131.00|s_1=100.00|s_2=1.00|s_3=0.00|" \
                   "s_d0=18.00|s_t0=32.30|s_h0=57.70|s_d1=25.00|gps_lat=24.353619|gps_lon=120.523148|gps_fix=1|gps_num=16|gps_alt=7"
        '''
        test_log = "LASS/Test/MAPS |ver_format=3|fmt_opt=0|app=MAPS|ver_app=0.7.10|device_id=MAPS_001|tick=86434792|" \
                   "date=2015-11-22|time=06:26:32|device=LinkItONE|s_0=8656.00|s_1=100.00|s_2=1.00|s_3=0.00|" \
                   "s_b0=1011.93|s_t0=30.30|s_h0=51.80|s_l0=0.00|gps_lat=21.543361|gps_lon=137.076127|gps_fix=0|gps_num=0|gps_alt=11"
        '''
        if test_log:
            esData.add(test_log)

if __name__ == "__main__":
    main()