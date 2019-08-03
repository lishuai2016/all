目录


# 1、快速检查集群的健康状况

 curl -X GET 'localhost:9200/_cat/health?v'

```
curl -X GET 'localhost:9200/_cat/health?v'
epoch      timestamp cluster       status node.total node.data shards pri relo init unassign pending_tasks max_task_wait_time active_shards_percent
1558318678 02:17:58  elasticsearch yellow          1         1     30  30    0    0       30             0                  -                 50.0%
```
# 2、快速查看集群中有哪些索引

curl -X GET 'localhost:9200/_cat/indices?v'

```
curl -X GET 'localhost:9200/_cat/indices?v'
health status index      uuid                   pri rep docs.count docs.deleted store.size pri.store.size
yellow open   weather    6JMBqdCRT1Orr6Ck2_HTOA   5   1          0            0      1.2kb          1.2kb
yellow open   book_index 7JClFnMKRYusadTM_RjXSA   5   1          1            0      6.4kb          6.4kb
yellow open   ymq_index  bWVbvIcRTUGIQ5ILJttVGg   5   1          0            0      1.2kb          1.2kb
yellow open   index_log  J6Ul_BngQbmQLPgMQW7bHA  10   1      10010            0     19.6mb         19.6mb
yellow open   index      DwoFR72FRPSVXrfrzGiNwA   5   1          0            0      1.2kb          1.2kb
```

# 3、查询操作

## 3.1、query string search

GET /ecommerce/product/_search?q=name:yagao&sort=price:desc

定制返回结果，也就是指定返回的字段

GET /test_index/test_type/1?_source=test_field1,test_field2

> 1、query string基础语法

GET /test_index/test_type/_search?q=test_field:test

GET /test_index/test_type/_search?q=+test_field:test

GET /test_index/test_type/_search?q=-test_field:test

一个是掌握q=field:search content的语法，还有一个是掌握+和-的含义

+号好像有没有不影响

-号是排除的意思？

> 2、_all metadata的原理和作用

GET /test_index/test_type/_search?q=test

直接可以搜索所有的field

es中的_all元数据，在建立索引的时候，我们插入一条document，它里面包含了多个field，此时，es会自动将多个field的值，全部用字符串的方式串联起来，变成一个长的字符串，作为_all field的值，同时建立索引

## 3.2、query DSL 和 query filter

```
GET /ecommerce/product/_search
{
  "query": {
    
    "match": {
      "producer": "gaolujie producer"
    }
  }
}


GET /ecommerce/product/_search
{
  "query": {
    
    "bool": {
      "must": [
        {"match": {
          "name": "yagao"
        }}
      ],
      "filter": {
        "range": {
          "price": {
            "gte": 10,
            "lte": 25
          }
        }
      }
    }
    
    
  }
  , "from": 0
  , "size": 10
  , "sort": [
    {
      "price": {
        "order": "asc"
      }
    }
  ]
  , "_source": ["name","price","desc"]
  
}

```



## 3.3、full-text search（全文检索）

针对text字段查询，会走全文检索

```
GET /ecommerce/product/_search
{
    "query" : {
        "match" : {
            "producer" : "yagao producer"
        }
    }
}
```


## 3.4、phrase search（短语搜索）
```
GET /ecommerce/product/_search
{
  "query": {
    
    "match_phrase": {
      "producer": "jiajieshi producer"
    }
  }
}

```


备注：这个搜索和match 匹配字段producer.keyword都可以达到效果，有什么具体的区别吗？


## 3.5、highlight search（高亮搜索结果）

```
GET /ecommerce/product/_search
{
  "query": {
    
    "match": {
      "producer": "jiajieshi producer"
    }
  }
  , "highlight": {
    "fields": {
      "producer": {}
    }
  }
}
```

会把字段producer包含jiajieshi producer两个词中的任何一个高亮显示

```
{
  "took": 193,
  "timed_out": false,
  "_shards": {
    "total": 5,
    "successful": 5,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 4,
    "max_score": 0.95348084,
    "hits": [
      {
        "_index": "ecommerce",
        "_type": "product",
        "_id": "2",
        "_score": 0.95348084,
        "_source": {
          "name": "jiajieshi yagao",
          "desc": "youxiao fangzhu",
          "price": 25,
          "producer": "jiajieshi producer",
          "tags": [
            "fangzhu"
          ]
        },
        "highlight": {
          "producer": [
            "<em>jiajieshi</em> <em>producer</em>"
          ]
        }
      },
      {
        "_index": "ecommerce",
        "_type": "product",
        "_id": "1",
        "_score": 0.2876821,
        "_source": {
          "name": "gaolujie yagao",
          "desc": "gaoxiao meibai",
          "price": 30,
          "producer": "gaolujie producer",
          "tags": [
            "meibai",
            "fangzhu"
          ]
        },
        "highlight": {
          "producer": [
            "gaolujie <em>producer</em>"
          ]
        }
      },
      {
        "_index": "ecommerce",
        "_type": "product",
        "_id": "3",
        "_score": 0.2876821,
        "_source": {
          "name": "zhonghua yagao",
          "desc": "caoben zhiwu",
          "price": 40,
          "producer": "zhonghua producer",
          "tags": [
            "qingxin"
          ]
        },
        "highlight": {
          "producer": [
            "zhonghua <em>producer</em>"
          ]
        }
      },
      {
        "_index": "ecommerce",
        "_type": "product",
        "_id": "4",
        "_score": 0.16853254,
        "_source": {
          "name": "special yagao",
          "desc": "special meibai",
          "price": 50,
          "producer": "special yagao producer",
          "tags": [
            "meibai"
          ]
        },
        "highlight": {
          "producer": [
            "special yagao <em>producer</em>"
          ]
        }
      }
    ]
  }
}

```

## 3.7、filter与query深入对比解密：相关度，性能

```
GET /company/employee/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "join_date": "2016-01-01"
          }
        }
      ],
      "filter": {
        "range": {
          "age": {
            "gte": 30
          }
        }
      }
    }
  }
}
```

> filter与query对比大解密

filter，仅仅只是按照搜索条件过滤出需要的数据而已，不计算任何相关度分数，对相关度没有任何影响

query，会去计算每个document相对于搜索条件的相关度，并按照相关度进行排序

一般来说，如果你是在进行搜索，需要将最匹配搜索条件的数据先返回，那么用query；如果你只是要根据一些条件筛选出一部分数据，不关注其排序，那么用filter

除非是你的这些搜索条件，你希望越符合这些搜索条件的document越排在前面返回，那么这些搜索条件要放在query中；如果你不希望一些搜索条件来影响你的document排序，那么就放在filter中即可

> filter与query性能

filter，不需要计算相关度分数，不需要按照相关度分数进行排序，同时还有内置的自动cache最常使用filter的数据

query，相反，要计算相关度分数，按照分数进行排序，而且无法cache结果


## 3.6、如何组合多个搜索条件

搜索需求：title必须包含elasticsearch，content可以包含elasticsearch也可以不包含，author_id必须不为111

```
PUT /website/article/4
{
 "title": "my hadoop article",
          "content": "hadoop is very bad",
          "author_id": 111
}

PUT /website/article/5
{
 "title": "my elasticsearch article",
          "content": "es is very bad",
          "author_id": 110
}



PUT /website/article/6
{
 "title": "my elasticsearch article",
          "content": "es is very goods",
          "author_id": 111
}


```

```
GET /website/article/_search
{
  "query": {
    "bool": {
      "must": [
        {"term": {
          "title": {
            "value": "elasticsearch"
          }
        }}
      ]
      , "must_not": [
        {"term": {
          "author_id": {
            "value": "111"
          }
        }}
      ]
      , "should": [
        {"term": {
          "content": {
            "value": "elasticsearch"
          }
        }}
      ]
    }
  }
}

```


## 3.7、query查询汇总

```
1、match all

GET /company/employee/_search
{
  "query": {
    "match_all": {}
  }
}

获取该index和type下的所有数据，相当于没有过滤和筛选条件

2、match

GET /company/employee/_search
{
  "query": {
    "match": {
      "name": "tom"
    }
  }
}

一个搜索词匹配一个字段


3、multi match

GET /company/employee/_search
{
  "query": {
   "multi_match": {
     "query": "jiangsu",  //搜索词
     "fields": ["address.province","name"]
   }
  }
}

一个搜索词匹配多个字段


4、range query
GET /company/employee/_search
{
  "query": {
    "range": {
      "age": {
        "gte": 10,
        "lte": 30
      }
    }
  }
}

范围查询，类似于between and

5、term query







6、terms query






```


# 4、嵌套聚合，下钻分析，聚合分析

## 4.1、group by 分析

### 需求1 计算每个tag下的商品数量

备注：在使用前 将文本field的fielddata属性设置为true，否则报错

```
PUT /ecommerce/_mapping/product
{
  "properties": {
    "tags": {
      "type": "text",
      "fielddata": true
    }
  }
}

```




```
GET /ecommerce/product/_search
{
  "aggs": {
    "group_by_tags": {
      "terms": { "field": "tags" }
    }
  }
}

```

```
{
  "took": 14,
  "timed_out": false,
  "_shards": {
    "total": 5,
    "successful": 5,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 4,
    "max_score": 1,
    "hits": [
      {
        "_index": "ecommerce",
        "_type": "product",
        "_id": "2",
        "_score": 1,
        "_source": {
          "name": "jiajieshi yagao",
          "desc": "youxiao fangzhu",
          "price": 25,
          "producer": "jiajieshi producer",
          "tags": [
            "fangzhu"
          ]
        }
      },
      {
        "_index": "ecommerce",
        "_type": "product",
        "_id": "4",
        "_score": 1,
        "_source": {
          "name": "special yagao",
          "desc": "special meibai",
          "price": 50,
          "producer": "special yagao producer",
          "tags": [
            "meibai"
          ]
        }
      },
      {
        "_index": "ecommerce",
        "_type": "product",
        "_id": "1",
        "_score": 1,
        "_source": {
          "name": "gaolujie yagao",
          "desc": "gaoxiao meibai",
          "price": 30,
          "producer": "gaolujie producer",
          "tags": [
            "meibai",
            "fangzhu"
          ]
        }
      },
      {
        "_index": "ecommerce",
        "_type": "product",
        "_id": "3",
        "_score": 1,
        "_source": {
          "name": "zhonghua yagao",
          "desc": "caoben zhiwu",
          "price": 40,
          "producer": "zhonghua producer",
          "tags": [
            "qingxin"
          ]
        }
      }
    ]
  },
  "aggregations": {
    "group_by_tags": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 0,
      "buckets": [
        {
          "key": "fangzhu",
          "doc_count": 2
        },
        {
          "key": "meibai",
          "doc_count": 2
        },
        {
          "key": "qingxin",
          "doc_count": 1
        }
      ]
    }
  }
}
```

aggregations部分返回的数据即使聚合结果




### 需求2 对名称中包含yagao的商品，计算每个tag下的商品数量

```
GET /ecommerce/product/_search
{
  "size": 0,
  "query": {
    
    "match": {
      "name": "yagao"
    }
    
  }, 
  
  "aggs": {
    "group_by_tags": {
      "terms": { "field": "tags" }
    }
  }
}

```




备注:"size": 0设置可以只显示aggregations结果

### 需求3 第三个聚合分析的需求：先分组，再算每组的平均值，计算每个tag下的商品的平均价格

```
GET /ecommerce/product/_search
{
  "size": 0,
  "aggs": {
    "group_by_tags": {
      "terms": { "field": "tags" },
      "aggs": {
        "avg_price": {
          "avg": {
            "field": "price"
          }
        }
      }
    }
  }
}


```
比如下面的，不会影响返回的数据值，类似于别名的作用

```
GET /ecommerce/product/_search
{
  "size": 0,
  "aggs": {
    "group_by_tagsssssss": {
      "terms": { "field": "tags" },
      "aggs": {
        "avg_pricesssssss": {
          "avg": {
            "field": "price"
          }
        }
      }
    }
  }
}

```

```
{
  "took": 11,
  "timed_out": false,
  "_shards": {
    "total": 5,
    "successful": 5,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 4,
    "max_score": 0,
    "hits": []
  },
  "aggregations": {
    "group_by_tagsssssss": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 0,
      "buckets": [
        {
          "key": "fangzhu",
          "doc_count": 2,
          "avg_pricesssssss": {
            "value": 27.5
          }
        },
        {
          "key": "meibai",
          "doc_count": 2,
          "avg_pricesssssss": {
            "value": 40
          }
        },
        {
          "key": "qingxin",
          "doc_count": 1,
          "avg_pricesssssss": {
            "value": 40
          }
        }
      ]
    }
  }
}

```


聚合的嵌套使用，一般第一层为嵌套起的名称，第二层为聚合函数


### 需求4 第四个数据分析需求：计算每个tag下的商品的平均价格，并且按照平均价格降序排序

```
GET /ecommerce/product/_search
{
  "size": 0,
  "aggs": {
    "group_by_tag": {
      "terms": { 
        "field": "tags" , 
        "order": {
          "avg_price": "desc"  //指定排序字段
        }
      },
      "aggs": {
        "avg_price": {
          "avg": {
            "field": "price"
          }
        }
      }
    }
  }
}

```


### 需求5 第五个数据分析需求：按照指定的价格范围区间进行分组，然后在每组内再按照tag进行分组，最后再计算每组的平均价格

```
GET /ecommerce/product/_search
{
  "size": 0,
  "aggs": {
    "group_by_price": {
      "range": {
        "field": "price",
        "ranges": [
          {
            "from": 0,
            "to": 20
          },
          {
            "from": 20,
            "to": 40
          },
          {
            "from": 40,
            "to": 50
          }
        ]
      },
      "aggs": {
        "group_by_tags": {
          "terms": {
            "field": "tags"
          },
          "aggs": {
            "average_price": {
              "avg": {
                "field": "price"
              }
            }
          }
        }
      }
    }
  }
}


```


# 5、更新

## 5.1、全量更新

和插入一样

## 5.2、部分更新
```
post /index/type/id/_update 
{
   "doc": {
      "要修改的少数几个field即可，不需要全量的数据"
   }
}
```


# 6、批量操作

## 6.1、批量获取_mget

原来的一条条查询
GET /test_index/test_type/1


> 不同的index和type

```
GET /_mget
{
   "docs" : [
      {
         "_index" : "test_index",
         "_type" :  "test_type",
         "_id" :    1
      },
      {
         "_index" : "test_index",
         "_type" :  "test_type",
         "_id" :    2
      }
   ]
}
```
> index相同，type不同

```
GET /test_index/_mget
{
   "docs" : [
      {
         "_type" :  "test_type",
         "_id" :    1
      },
      {
         "_type" :  "test_type",
         "_id" :    2
      }
   ]
}
```

> index和type均相同

直接通过id列表获取

```
GET /ecommerce/product/_mget
{
  "ids":[1,2]
}
```


## 6.2、bulk批量增删改

```
POST /_bulk
{ "delete": { "_index": "test_index", "_type": "test_type", "_id": "3" }} 
{ "create": { "_index": "test_index", "_type": "test_type", "_id": "12" }}
{ "test_field":    "test12" }
{ "index":  { "_index": "test_index", "_type": "test_type", "_id": "2" }}
{ "test_field":    "replaced test2" }
{ "update": { "_index": "test_index", "_type": "test_type", "_id": "1", "_retry_on_conflict" : 3} }
{ "doc" : {"test_field2" : "bulk test1"} }


每一个操作要两个json串，语法如下：

{"action": {"metadata"}}
{"data"}

```
bulk操作中，任意一个操作失败，是不会影响其他的操作的，但是在返回结果里，会告诉你异常日志


## 6.3、multi-index和multi-type搜索模式

```
告诉你如何一次性搜索多个index和多个type下的数据

/_search：所有索引，所有type下的所有数据都搜索出来

/index1/_search：指定一个index，搜索其下所有type的数据

/index1,index2/_search：同时搜索两个index下的数据

/*1,*2/_search：按照通配符去匹配多个索引

/index1/type1/_search：搜索一个index下指定的type的数据

/index1/type1,type2/_search：可以搜索一个index下多个type的数据

/index1,index2/type1,type2/_search：搜索多个index下的多个type的数据

/_all/type1,type2/_search：_all，可以代表搜索所有index下的指定type的数据

```


# 7、分页和deep paging

```
size，from

GET /_search?size=10

GET /_search?size=10&from=0

GET /_search?size=10&from=20
```

from是一个类似于游标的，从第几个开始查询，然后获取size个元素，排序是根据score



# 8、es中的数据类型

## 1、核心的数据类型

- string、text
- byte，short，integer，long
- float，double
- boolean
- date



备注：
1、是否long和date类型的数据不做拆分，全匹配才可以？

2、text走的是全文匹配


## 2、dynamic mapping

```
true or false	-->	boolean
123		-->	long
123.45		-->	double
2017-01-01	-->	date
"hello world"	-->	string/text
```

## 3、查看mapping

GET /index/_mapping/type



# 9、测试字段的分词情况

测试mapping


```
GET website/_analyze
{
  "field": "post_date"
  , "text": "2017-01-01"
}


{
  "error": {
    "root_cause": [
      {
        "type": "remote_transport_exception",
        "reason": "[wKBRNth][192.168.254.10:9300][indices:admin/analyze[s]]"
      }
    ],
    "type": "illegal_argument_exception",
    "reason": "Can't process field [post_date], Analysis requests are only supported on tokenized fields"
  },
  "status": 400
}

```

有些不支持分词就没法分析

```
GET website/_analyze
{
  "field": "title"
  , "text": "my name is lishuai"
}

{
  "tokens": [
    {
      "token": "my",
      "start_offset": 0,
      "end_offset": 2,
      "type": "<ALPHANUM>",
      "position": 0
    },
    {
      "token": "name",
      "start_offset": 3,
      "end_offset": 7,
      "type": "<ALPHANUM>",
      "position": 1
    },
    {
      "token": "is",
      "start_offset": 8,
      "end_offset": 10,
      "type": "<ALPHANUM>",
      "position": 2
    },
    {
      "token": "lishuai",
      "start_offset": 11,
      "end_offset": 18,
      "type": "<ALPHANUM>",
      "position": 3
    }
  ]
}



```

默认应该是依据空格切分词





