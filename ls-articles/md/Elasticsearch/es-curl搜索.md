curl常用搜索

- [[转]23个最有用的Elasticsearch检索技巧](https://juejin.im/post/5b7fe4a46fb9a019d92469a9)
- [命令的java实现](https://github.com/whirlys/Elastic-In-Practice/tree/master/UsefullESSearchSkill)






- [注意：本文实验使用的ES版本是 ES 6.5.3 + kibana6.0.1，并且命令的操作在kibana的dev tools中执行]


# 0、数据准备

为了讲解不同类型 ES 检索，我们将要对包含以下类型的文档集合进行检索：

```
title               标题
authors             作者
summary             摘要
publish_date        发布日期
num_reviews         评论数
publisher           出版社
```

首先，我们借助 bulk API 批量创建新的索引并提交数据

## 设置索引 settings
```
PUT /bookdb_index
{ "settings": { "number_of_shards": 1 }}
```

## bulk 提交数据
```
POST /bookdb_index/book/_bulk
{"index":{"_id":1}}
{"title":"Elasticsearch: The Definitive Guide","authors":["clinton gormley","zachary tong"],"summary":"A distibuted real-time search and analytics engine","publish_date":"2015-02-07","num_reviews":20,"publisher":"oreilly"}
{"index":{"_id":2}}
{"title":"Taming Text: How to Find, Organize, and Manipulate It","authors":["grant ingersoll","thomas morton","drew farris"],"summary":"organize text using approaches such as full-text search, proper name recognition, clustering, tagging, information extraction, and summarization","publish_date":"2013-01-24","num_reviews":12,"publisher":"manning"}
{"index":{"_id":3}}
{"title":"Elasticsearch in Action","authors":["radu gheorge","matthew lee hinman","roy russo"],"summary":"build scalable search applications using Elasticsearch without having to do complex low-level programming or understand advanced data science algorithms","publish_date":"2015-12-03","num_reviews":18,"publisher":"manning"}
{"index":{"_id":4}}
{"title":"Solr in Action","authors":["trey grainger","timothy potter"],"summary":"Comprehensive guide to implementing a scalable search engine using Apache Solr","publish_date":"2014-04-05","num_reviews":23,"publisher":"manning"}
```

## 基于es建立的默认mapping

```
GET /bookdb_index/_mapping/book/

{
  "bookdb_index": {
    "mappings": {
      "book": {
        "properties": {
          "authors": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          },
          "num_reviews": {
            "type": "long"
          },
          "publish_date": {
            "type": "date"
          },
          "publisher": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          },
          "summary": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          },
          "title": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          }
        }
      }
    }
  }
}


```




# 01、基本匹配检索( Basic Match Query)

## 1.1 全文检索

有两种方式可以执行全文检索：

### 1）使用包含参数的检索API，参数作为URL的一部分

举例：以下对 "guide" 执行全文检索

```
GET bookdb_index/book/_search?q=guide

{
  "took": 93,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 2,
    "max_score": 1.3278645,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "4",
        "_score": 1.3278645,
        "_source": {
          "title": "Solr in Action",
          "authors": [
            "trey grainger",
            "timothy potter"
          ],
          "summary": "Comprehensive guide to implementing a scalable search engine using Apache Solr",
          "publish_date": "2014-04-05",
          "num_reviews": 23,
          "publisher": "manning"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "1",
        "_score": 1.2871116,
        "_source": {
          "title": "Elasticsearch: The Definitive Guide",
          "authors": [
            "clinton gormley",
            "zachary tong"
          ],
          "summary": "A distibuted real-time search and analytics engine",
          "publish_date": "2015-02-07",
          "num_reviews": 20,
          "publisher": "oreilly"
        }
      }
    ]
  }
}


```

问题：为什么文档4的得分比文档1高，都是只包含一个关键字？得分是如何计算的？



### 2）使用完整的ES DSL，其中Json body作为请求体 其执行结果如方式 1）结果一致.

multi_match的特例使用，检索所有的字段

```
GET /bookdb_index/book/_search
{
  "query": {
    "multi_match": {
      "query": "guide",
      "fields": []
    }
  }
}

{
  "took": 2,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 2,
    "max_score": 1.3278645,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "4",
        "_score": 1.3278645,
        "_source": {
          "title": "Solr in Action",
          "authors": [
            "trey grainger",
            "timothy potter"
          ],
          "summary": "Comprehensive guide to implementing a scalable search engine using Apache Solr",
          "publish_date": "2014-04-05",
          "num_reviews": 23,
          "publisher": "manning"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "1",
        "_score": 1.2871116,
        "_source": {
          "title": "Elasticsearch: The Definitive Guide",
          "authors": [
            "clinton gormley",
            "zachary tong"
          ],
          "summary": "A distibuted real-time search and analytics engine",
          "publish_date": "2015-02-07",
          "num_reviews": 20,
          "publisher": "oreilly"
        }
      }
    ]
  }
}

```

解读： 使用multi_match关键字代替match关键字，作为对多个字段运行相同查询的方便的简写方式。 
fields属性指定要查询的字段，在这种情况下，我们要对文档中的所有字段进行查询

注意：ES 6.x 默认不启用 _all 字段, 不指定 fields 默认搜索为所有字段，如果指定_all则查询不出数据，如下所示


```
GET /bookdb_index/book/_search
{
  "query": {
    "multi_match": {
      "query": "guide",
      "fields": ["_all"]
    }
  }
}

{
  "took": 1,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 0,
    "max_score": null,
    "hits": []
  }
}

```


## 1.2 指定特定字段检索

这两个API也允许您指定要搜索的字段。
例如，要在标题字段(title)中搜索带有 "in action" 字样的图书

### 1）URL检索方式

```
GET /bookdb_index/book/_search?q=title:in action


{
  "took": 48,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 2,
    "max_score": 1.6323128,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "3",
        "_score": 1.6323128,
        "_source": {
          "title": "Elasticsearch in Action",
          "authors": [
            "radu gheorge",
            "matthew lee hinman",
            "roy russo"
          ],
          "summary": "build scalable search applications using Elasticsearch without having to do complex low-level programming or understand advanced data science algorithms",
          "publish_date": "2015-12-03",
          "num_reviews": 18,
          "publisher": "manning"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "4",
        "_score": 1.6323128,
        "_source": {
          "title": "Solr in Action",
          "authors": [
            "trey grainger",
            "timothy potter"
          ],
          "summary": "Comprehensive guide to implementing a scalable search engine using Apache Solr",
          "publish_date": "2014-04-05",
          "num_reviews": 23,
          "publisher": "manning"
        }
      }
    ]
  }
}


```

### 2）DSL检索方式

然而，full body的DSL为您提供了创建更复杂查询的更多灵活性（我们将在后面看到）以及指定您希望的返回结果。
在下面的示例中，我们指定要返回的结果数、偏移量（对分页有用）、我们要返回的文档字段以及属性的高亮显示。

- 结果数的表示方式：size
- 偏移值的表示方式：from
- 指定返回字段 的表示方式 ：_source
- 高亮显示 的表示方式 ：highliaght


```
GET /bookdb_index/book/_search
{
  "query": {
    "match": {
      "title": "in action"
    }
  },
  "size": 20,
  "from": 0,
  "_source": ["title","summary","publish_date"],
  "highlight": {
    "fields": {
      "title": {}
    }
  }
}


{
  "took": 305,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 2,
    "max_score": 1.6323128,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "3",
        "_score": 1.6323128,
        "_source": {
          "summary": "build scalable search applications using Elasticsearch without having to do complex low-level programming or understand advanced data science algorithms",
          "title": "Elasticsearch in Action",
          "publish_date": "2015-12-03"
        },
        "highlight": {
          "title": [
            "Elasticsearch <em>in</em> <em>Action</em>"
          ]
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "4",
        "_score": 1.6323128,
        "_source": {
          "summary": "Comprehensive guide to implementing a scalable search engine using Apache Solr",
          "title": "Solr in Action",
          "publish_date": "2014-04-05"
        },
        "highlight": {
          "title": [
            "Solr <em>in</em> <em>Action</em>"
          ]
        }
      }
    ]
  }
}

```


注意:
- 对于 multi-word 检索，匹配查询允许您指定是否使用  and 运算符，而不是使用默认 or 运算符  --->   "operator" : "and"

- 您还可以指定 minimum_should_match 选项来调整返回结果的相关性，详细信息可以在Elasticsearch指南中查询Elasticsearch guide获取



# 02、多字段检索 (Multi-field Search)

如我们已经看到的，要在搜索中查询多个文档字段（例如在标题和摘要中搜索相同的查询字符串），请使用multi_match查询

```
GET /bookdb_index/book/_search
{
  "query": {
    "multi_match": {
      "query": "guide",
      "fields": ["title","summary"]
    }
  }
}

{
  "took": 4,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 2,
    "max_score": 1.3278645,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "4",
        "_score": 1.3278645,
        "_source": {
          "title": "Solr in Action",
          "authors": [
            "trey grainger",
            "timothy potter"
          ],
          "summary": "Comprehensive guide to implementing a scalable search engine using Apache Solr",
          "publish_date": "2014-04-05",
          "num_reviews": 23,
          "publisher": "manning"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "1",
        "_score": 1.2871116,
        "_source": {
          "title": "Elasticsearch: The Definitive Guide",
          "authors": [
            "clinton gormley",
            "zachary tong"
          ],
          "summary": "A distibuted real-time search and analytics engine",
          "publish_date": "2015-02-07",
          "num_reviews": 20,
          "publisher": "oreilly"
        }
      }
    ]
  }
}

```

注意：以上结果中文档4（_id=4）匹配的原因是guide在summary存在。




# 03、 Boosting提升某字段得分的检索( Boosting)

由于我们正在多个字段进行搜索，我们可能希望提高某一字段的得分。 在下面的例子中，我们将“标题”字段的得分提高了3倍，以增加“摘要”字段的重要性，从而提高文档 1 的相关性。

```
GET /bookdb_index/book/_search
{
  "query": {
    "multi_match": {
      "query": "guide",
      "fields": ["title^3","summary"]
    }
  }
}


{
  "took": 3,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 2,
    "max_score": 3.861335,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "1",
        "_score": 3.861335,
        "_source": {
          "title": "Elasticsearch: The Definitive Guide",
          "authors": [
            "clinton gormley",
            "zachary tong"
          ],
          "summary": "A distibuted real-time search and analytics engine",
          "publish_date": "2015-02-07",
          "num_reviews": 20,
          "publisher": "oreilly"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "4",
        "_score": 1.3278645,
        "_source": {
          "title": "Solr in Action",
          "authors": [
            "trey grainger",
            "timothy potter"
          ],
          "summary": "Comprehensive guide to implementing a scalable search engine using Apache Solr",
          "publish_date": "2014-04-05",
          "num_reviews": 23,
          "publisher": "manning"
        }
      }
    ]
  }
}

```

注意：Boosting不仅意味着计算得分乘法以增加因子。 实际的提升得分值是通过归一化和一些内部优化。参考 Elasticsearch guide查看更多


# 04、Bool检索( Bool Query)

可以使用 AND / OR / NOT 运算符来微调我们的搜索查询，以提供更相关或指定的搜索结果。
在搜索API中是通过bool查询来实现的。bool查询接受 must 参数（等效于AND），一个 must_not 参数（相当于NOT）或者一个 should 参数（等同于OR）。
例如，如果我想在标题中搜索一本名为 "Elasticsearch" 或 "Solr" 的书，AND由 "clinton gormley" 创作，但NOT由 "radu gheorge" 创作

```
GET /bookdb_index/book/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "bool": {
            "should": [
              {"match": {
                "title": "Elasticsearch"
              }},
              {"match": {
                "title": "Solr"
              }}
            ]
          }
        },
        {
          "match": {
            "authors": "clinton gormely"
          }  
        }
      ],
      "must_not": [
        {
          "match": {
            "authors": "radu gheorge"
          }
        }
      ]
    }
  }
}



{
  "took": 166,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 1,
    "max_score": 2.0749094,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "1",
        "_score": 2.0749094,
        "_source": {
          "title": "Elasticsearch: The Definitive Guide",
          "authors": [
            "clinton gormley",
            "zachary tong"
          ],
          "summary": "A distibuted real-time search and analytics engine",
          "publish_date": "2015-02-07",
          "num_reviews": 20,
          "publisher": "oreilly"
        }
      }
    ]
  }
}

```

关于bool查询中的should， 有两种情况：

- 当should的同级存在must的时候，should中的条件可以满足也可以不满足，满足的越多得分越高

- 当没有must的时候，默认should中的条件至少要满足一个


注意：您可以看到，bool查询可以包含任何其他查询类型，包括其他布尔查询，以创建任意复杂或深度嵌套的查询




# 05、 Fuzzy 模糊检索( Fuzzy Queries)

在 Match检索 和多匹配检索中可以启用模糊匹配来捕捉拼写错误。 基于与原始词的 Levenshtein 距离来指定模糊度

```
GET /bookdb_index/book/_search
{
  "query": {
    "multi_match": {
      "query": "comprihensiv guide",
      "fields": [],
      "fuzziness": "AUTO"
    }
  }
}

{
  "took": 72,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 2,
    "max_score": 2.4344182,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "4",
        "_score": 2.4344182,
        "_source": {
          "title": "Solr in Action",
          "authors": [
            "trey grainger",
            "timothy potter"
          ],
          "summary": "Comprehensive guide to implementing a scalable search engine using Apache Solr",
          "publish_date": "2014-04-05",
          "num_reviews": 23,
          "publisher": "manning"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "1",
        "_score": 1.2871116,
        "_source": {
          "title": "Elasticsearch: The Definitive Guide",
          "authors": [
            "clinton gormley",
            "zachary tong"
          ],
          "summary": "A distibuted real-time search and analytics engine",
          "publish_date": "2015-02-07",
          "num_reviews": 20,
          "publisher": "oreilly"
        }
      }
    ]
  }
}
```
"AUTO" 的模糊值相当于当字段长度大于5时指定值2。但是，设置80％的拼写错误的编辑距离为1，将模糊度设置为1可能会提高整体搜索性能。



```
GET /bookdb_index/book/_search
{
  "query": {
    "multi_match": {
      "query": "hensive",
      "fields": [],
      "fuzziness": "AUTO"
    }
  }
}


{
  "took": 31,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 0,
    "max_score": null,
    "hits": []
  }
}
```

问题：这里的模糊匹配其实是根据单词的前缀来进行的？



# 06、 Wildcard Query 通配符检索

通配符查询允许您指定匹配的模式，而不是整个词组（term）检索

- ？ 匹配任何字符
- 。匹配零个或多个字符

举例，要查找具有以 "t" 字母开头的作者的所有记录，如下所示



```
GET /bookdb_index/book/_search
{
  "query": {
    "wildcard": {
      "authors": {
        "value": "t*"
      }
    }
  }
}

{
  "took": 6,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 3,
    "max_score": 1,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "1",
        "_score": 1,
        "_source": {
          "title": "Elasticsearch: The Definitive Guide",
          "authors": [
            "clinton gormley",
            "zachary tong"
          ],
          "summary": "A distibuted real-time search and analytics engine",
          "publish_date": "2015-02-07",
          "num_reviews": 20,
          "publisher": "oreilly"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "2",
        "_score": 1,
        "_source": {
          "title": "Taming Text: How to Find, Organize, and Manipulate It",
          "authors": [
            "grant ingersoll",
            "thomas morton",
            "drew farris"
          ],
          "summary": "organize text using approaches such as full-text search, proper name recognition, clustering, tagging, information extraction, and summarization",
          "publish_date": "2013-01-24",
          "num_reviews": 12,
          "publisher": "manning"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "4",
        "_score": 1,
        "_source": {
          "title": "Solr in Action",
          "authors": [
            "trey grainger",
            "timothy potter"
          ],
          "summary": "Comprehensive guide to implementing a scalable search engine using Apache Solr",
          "publish_date": "2014-04-05",
          "num_reviews": 23,
          "publisher": "manning"
        }
      }
    ]
  }
}


```



前后匹配，类似于模糊查询

```
GET /bookdb_index/book/_search
{
  "query": {
    "wildcard": {
      "authors": {
        "value": "*om*"
      }
    }
  }
}

{
  "took": 2,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 1,
    "max_score": 1,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "2",
        "_score": 1,
        "_source": {
          "title": "Taming Text: How to Find, Organize, and Manipulate It",
          "authors": [
            "grant ingersoll",
            "thomas morton",
            "drew farris"
          ],
          "summary": "organize text using approaches such as full-text search, proper name recognition, clustering, tagging, information extraction, and summarization",
          "publish_date": "2013-01-24",
          "num_reviews": 12,
          "publisher": "manning"
        }
      }
    ]
  }
}

```




# 07、正则表达式检索( Regexp Query)
正则表达式能指定比通配符检索更复杂的检索模式，举例如下：


```
GET /bookdb_index/book/_search
{
  "query": {
    "regexp":{
       "authors": "t[a-z]*y"
    }
  }
}

{
  "took": 7,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 1,
    "max_score": 1,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "4",
        "_score": 1,
        "_source": {
          "title": "Solr in Action",
          "authors": [
            "trey grainger",
            "timothy potter"
          ],
          "summary": "Comprehensive guide to implementing a scalable search engine using Apache Solr",
          "publish_date": "2014-04-05",
          "num_reviews": 23,
          "publisher": "manning"
        }
      }
    ]
  }
}


```

# 08、匹配短语检索( Match Phrase Query)
匹配短语查询要求查询字符串中的所有词都存在于文档中，按照查询字符串中指定的顺序并且彼此靠近。
默认情况下，这些词必须完全相邻，但您可以指定偏离值（slop value)，该值指示在仍然考虑文档匹配的情况下词与词之间的偏离值。

```
GET /bookdb_index/book/_search
{
  "query": {
    "multi_match": {
      "query": "search engine",
      "fields": [],
      "type": "phrase",
      "slop": 2
    }
  }
}

{
  "took": 4,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 2,
    "max_score": 0.88067603,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "4",
        "_score": 0.88067603,
        "_source": {
          "title": "Solr in Action",
          "authors": [
            "trey grainger",
            "timothy potter"
          ],
          "summary": "Comprehensive guide to implementing a scalable search engine using Apache Solr",
          "publish_date": "2014-04-05",
          "num_reviews": 23,
          "publisher": "manning"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "1",
        "_score": 0.51429313,
        "_source": {
          "title": "Elasticsearch: The Definitive Guide",
          "authors": [
            "clinton gormley",
            "zachary tong"
          ],
          "summary": "A distibuted real-time search and analytics engine",
          "publish_date": "2015-02-07",
          "num_reviews": 20,
          "publisher": "oreilly"
        }
      }
    ]
  }
}

```

可以看到文档4中两个词的间距为2，如果slope小于2，则搜索不到文档4

```
GET /bookdb_index/book/_search
{
  "query": {
    "multi_match": {
      "query": "search engine",
      "fields": [],
      "type": "phrase",
      "slop": 1
    }
  }
}

{
  "took": 2,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 1,
    "max_score": 0.88067603,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "4",
        "_score": 0.88067603,
        "_source": {
          "title": "Solr in Action",
          "authors": [
            "trey grainger",
            "timothy potter"
          ],
          "summary": "Comprehensive guide to implementing a scalable search engine using Apache Solr",
          "publish_date": "2014-04-05",
          "num_reviews": 23,
          "publisher": "manning"
        }
      }
    ]
  }
}

```

注意：在上面的示例中，对于非短语类型查询，文档_id 1通常具有较高的分数，并且显示在文档_id 4之前，因为其字段长度较短。

然而，作为一个短语查询，词与词之间的接近度被考虑在内，所以文档_id 4分数更好


# 09、匹配词组前缀检索

匹配词组前缀查询在查询时提供搜索即时类型或 "相对简单" "的自动完成版本，而无需以任何方式准备数据。
像match_phrase查询一样，它接受一个斜率参数，使得单词的顺序和相对位置没有那么 "严格"。 它还接受max_expansions参数来限制匹配的条件数以减少资源强度


```
GET /bookdb_index/book/_search
{
  "query": {
    "match_phrase_prefix": {
      "summary": {
        "query": "search en",
        "slop":2,
        "max_expansions": 10
      }
    }
  }
}


{
  "took": 11,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 2,
    "max_score": 1.7613521,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "4",
        "_score": 1.7613521,
        "_source": {
          "title": "Solr in Action",
          "authors": [
            "trey grainger",
            "timothy potter"
          ],
          "summary": "Comprehensive guide to implementing a scalable search engine using Apache Solr",
          "publish_date": "2014-04-05",
          "num_reviews": 23,
          "publisher": "manning"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "1",
        "_score": 1.0285863,
        "_source": {
          "title": "Elasticsearch: The Definitive Guide",
          "authors": [
            "clinton gormley",
            "zachary tong"
          ],
          "summary": "A distibuted real-time search and analytics engine",
          "publish_date": "2015-02-07",
          "num_reviews": 20,
          "publisher": "oreilly"
        }
      }
    ]
  }
}

```

注意：查询时间搜索类型具有性能成本。 一个更好的解决方案是将时间作为索引类型。 更多相关API查询 Completion Suggester API 或者 Edge-Ngram filters 。


# 10、字符串检索（ Query String）

query_string查询提供了以简明的简写语法执行多匹配查询 multi_match queries ，布尔查询 bool queries ，提升得分 boosting ，
模糊匹配 fuzzy matching ，通配符 wildcards ，正则表达式 regexp 和范围查询 range queries 的方式。

在下面的例子中，我们对 "search algorithm" 一词执行模糊搜索，其中一本作者是 "grant ingersoll" 或 "tom morton"。
 我们搜索所有字段，但将提升应用于文档2的摘要字段


```
GET /bookdb_index/book/_search
{
  "query": {
    "query_string": {
      "query": "(saerch~1 algorithm~1) AND (grant ingersoll)  OR (tom morton)",
      "fields": ["summary^2","title","authors","publisher"]
    }
  },
  "highlight": {
    "fields": {
      "summary": {}
    }
  }
}


{
  "took": 42,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 1,
    "max_score": 3.571021,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "2",
        "_score": 3.571021,
        "_source": {
          "title": "Taming Text: How to Find, Organize, and Manipulate It",
          "authors": [
            "grant ingersoll",
            "thomas morton",
            "drew farris"
          ],
          "summary": "organize text using approaches such as full-text search, proper name recognition, clustering, tagging, information extraction, and summarization",
          "publish_date": "2013-01-24",
          "num_reviews": 12,
          "publisher": "manning"
        },
        "highlight": {
          "summary": [
            "organize text using approaches such as full-text <em>search</em>, proper name recognition, clustering, tagging"
          ]
        }
      }
    ]
  }
}

```

# 11、简化的字符串检索 （Simple Query String）
simple_query_string 查询是 query_string 查询的一个版本，更适合用于暴露给用户的单个搜索框，
因为它分别用 + / | / -  替换了 AND / OR / NOT 的使用，并放弃查询的无效部分，而不是在用户出错时抛出异常。

```
GET /bookdb_index/book/_search
{
"query": {
    "simple_query_string": {
      "query": "(saerch~1 algorithm~1) + (grant ingersoll)  | (tom morton)",
      "fields": ["summary^2","title","authors","publisher"]
    }
  },
  "_source": ["title","summary","authors"],
  "highlight": {
    "fields": {
      "summary": {}
    }
  }

}


{
  "took": 124,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 1,
    "max_score": 3.571021,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "2",
        "_score": 3.571021,
        "_source": {
          "summary": "organize text using approaches such as full-text search, proper name recognition, clustering, tagging, information extraction, and summarization",
          "title": "Taming Text: How to Find, Organize, and Manipulate It",
          "authors": [
            "grant ingersoll",
            "thomas morton",
            "drew farris"
          ]
        },
        "highlight": {
          "summary": [
            "organize text using approaches such as full-text <em>search</em>, proper name recognition, clustering, tagging"
          ]
        }
      }
    ]
  }
}

```


# 12、Term/Terms检索（指定字段检索）
上面1-11小节的例子是全文搜索的例子。 有时我们对结构化搜索更感兴趣，我们希望在其中找到完全匹配并返回结果
在下面的例子中，我们搜索 Manning Publications 发布的索引中的所有图书（借助 term和terms查询 ）

```
GET /bookdb_index/book/_search
{
"query": {
  "term": {
    "publisher": {
      "value": "manning"
    }
  }
}

}

{
  "took": 1,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 3,
    "max_score": 0.35667494,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "2",
        "_score": 0.35667494,
        "_source": {
          "title": "Taming Text: How to Find, Organize, and Manipulate It",
          "authors": [
            "grant ingersoll",
            "thomas morton",
            "drew farris"
          ],
          "summary": "organize text using approaches such as full-text search, proper name recognition, clustering, tagging, information extraction, and summarization",
          "publish_date": "2013-01-24",
          "num_reviews": 12,
          "publisher": "manning"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "3",
        "_score": 0.35667494,
        "_source": {
          "title": "Elasticsearch in Action",
          "authors": [
            "radu gheorge",
            "matthew lee hinman",
            "roy russo"
          ],
          "summary": "build scalable search applications using Elasticsearch without having to do complex low-level programming or understand advanced data science algorithms",
          "publish_date": "2015-12-03",
          "num_reviews": 18,
          "publisher": "manning"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "4",
        "_score": 0.35667494,
        "_source": {
          "title": "Solr in Action",
          "authors": [
            "trey grainger",
            "timothy potter"
          ],
          "summary": "Comprehensive guide to implementing a scalable search engine using Apache Solr",
          "publish_date": "2014-04-05",
          "num_reviews": 23,
          "publisher": "manning"
        }
      }
    ]
  }
}

```

Multiple terms可指定多个关键词进行检索
```
GET /bookdb_index/book/_search
{
"query": {
  "terms": {
    "publisher": [
      "oreilly",
      "manning"
    ]
  }
}

}

{
  "took": 12,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 4,
    "max_score": 1,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "1",
        "_score": 1,
        "_source": {
          "title": "Elasticsearch: The Definitive Guide",
          "authors": [
            "clinton gormley",
            "zachary tong"
          ],
          "summary": "A distibuted real-time search and analytics engine",
          "publish_date": "2015-02-07",
          "num_reviews": 20,
          "publisher": "oreilly"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "2",
        "_score": 1,
        "_source": {
          "title": "Taming Text: How to Find, Organize, and Manipulate It",
          "authors": [
            "grant ingersoll",
            "thomas morton",
            "drew farris"
          ],
          "summary": "organize text using approaches such as full-text search, proper name recognition, clustering, tagging, information extraction, and summarization",
          "publish_date": "2013-01-24",
          "num_reviews": 12,
          "publisher": "manning"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "3",
        "_score": 1,
        "_source": {
          "title": "Elasticsearch in Action",
          "authors": [
            "radu gheorge",
            "matthew lee hinman",
            "roy russo"
          ],
          "summary": "build scalable search applications using Elasticsearch without having to do complex low-level programming or understand advanced data science algorithms",
          "publish_date": "2015-12-03",
          "num_reviews": 18,
          "publisher": "manning"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "4",
        "_score": 1,
        "_source": {
          "title": "Solr in Action",
          "authors": [
            "trey grainger",
            "timothy potter"
          ],
          "summary": "Comprehensive guide to implementing a scalable search engine using Apache Solr",
          "publish_date": "2014-04-05",
          "num_reviews": 23,
          "publisher": "manning"
        }
      }
    ]
  }
}

```


# 13、Term排序检索-（Term Query - Sorted）
Term查询和其他查询一样，轻松的实现排序。多级排序也是允许的

```
GET /bookdb_index/book/_search
{
"query": {
  "term": {
    "publisher": {
      "value": "manning"
    }
  }
 },
 "sort": [
   {
     "publisher.keyword": {
       "order": "desc"
     },
     "title.keyword": {
        "order": "desc"
     }
   }
 ]

}

{
  "took": 2,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 3,
    "max_score": null,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "2",
        "_score": null,
        "_source": {
          "title": "Taming Text: How to Find, Organize, and Manipulate It",
          "authors": [
            "grant ingersoll",
            "thomas morton",
            "drew farris"
          ],
          "summary": "organize text using approaches such as full-text search, proper name recognition, clustering, tagging, information extraction, and summarization",
          "publish_date": "2013-01-24",
          "num_reviews": 12,
          "publisher": "manning"
        },
        "sort": [
          "manning",
          "Taming Text: How to Find, Organize, and Manipulate It"
        ]
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "4",
        "_score": null,
        "_source": {
          "title": "Solr in Action",
          "authors": [
            "trey grainger",
            "timothy potter"
          ],
          "summary": "Comprehensive guide to implementing a scalable search engine using Apache Solr",
          "publish_date": "2014-04-05",
          "num_reviews": 23,
          "publisher": "manning"
        },
        "sort": [
          "manning",
          "Solr in Action"
        ]
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "3",
        "_score": null,
        "_source": {
          "title": "Elasticsearch in Action",
          "authors": [
            "radu gheorge",
            "matthew lee hinman",
            "roy russo"
          ],
          "summary": "build scalable search applications using Elasticsearch without having to do complex low-level programming or understand advanced data science algorithms",
          "publish_date": "2015-12-03",
          "num_reviews": 18,
          "publisher": "manning"
        },
        "sort": [
          "manning",
          "Elasticsearch in Action"
        ]
      }
    ]
  }
}



注意：排序字段必须是xxx.keyword，而不能是text文本字段本身

```


# 14、范围检索（Range query）
另一个结构化检索的例子是范围检索。下面的举例中，我们检索了2015年发布的书籍。

```
GET /bookdb_index/book/_search
{
  "query": {
    "range": {
      "publish_date": {
        "gte": "2015-01-01",
        "lte": "2015-12-31"
      }
    }
  }

}

{
  "took": 25,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 2,
    "max_score": 1,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "1",
        "_score": 1,
        "_source": {
          "title": "Elasticsearch: The Definitive Guide",
          "authors": [
            "clinton gormley",
            "zachary tong"
          ],
          "summary": "A distibuted real-time search and analytics engine",
          "publish_date": "2015-02-07",
          "num_reviews": 20,
          "publisher": "oreilly"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "3",
        "_score": 1,
        "_source": {
          "title": "Elasticsearch in Action",
          "authors": [
            "radu gheorge",
            "matthew lee hinman",
            "roy russo"
          ],
          "summary": "build scalable search applications using Elasticsearch without having to do complex low-level programming or understand advanced data science algorithms",
          "publish_date": "2015-12-03",
          "num_reviews": 18,
          "publisher": "manning"
        }
      }
    ]
  }
}

```
注意：范围查询适用于日期，数字和字符串类型字段

# 15、过滤检索（Filtered query）
（5.0版本起已不再存在，不必关注）

过滤的查询允许您过滤查询的结果。 如下的例子，我们在标题或摘要中查询名为“Elasticsearch”的图书，但是我们希望将结果过滤到只有20个或更多评论的结果。


```

```


# 16、多个过滤器检索（Multiple Filters）
（5.x不再支持，无需关注） 多个过滤器可以通过使用布尔过滤器进行组合。

在下一个示例中，过滤器确定返回的结果必须至少包含20个评论，不得在2015年之前发布，并且应该由oreilly发布


```

```


# 17、 Function 得分：Field值因子（ Function Score: Field Value Factor）
可能有一种情况，您想要将文档中特定字段的值纳入相关性分数的计算。 这在您希望基于其受欢迎程度提升文档的相关性的情况下是有代表性的场景
在我们的例子中，我们希望增加更受欢迎的书籍（按评论数量判断）。 这可以使用field_value_factor函数得分

```
GET /bookdb_index/book/_search
{
"query": {
    "function_score": {
      "query": {
        "multi_match": {
          "query": "search engine",
          "fields": ["title","summary"]
        }
      },
      "field_value_factor": {
        "field": "num_reviews",
        "modifier": "log1p",
        "factor": 2
      }
    }
  },
  "_source": ["title", "summary", "publish_date", "num_reviews"]
}


{
  "took": 31,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 4,
    "max_score": 1.5694137,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "1",
        "_score": 1.5694137,
        "_source": {
          "summary": "A distibuted real-time search and analytics engine",
          "num_reviews": 20,
          "title": "Elasticsearch: The Definitive Guide",
          "publish_date": "2015-02-07"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "4",
        "_score": 1.4725765,
        "_source": {
          "summary": "Comprehensive guide to implementing a scalable search engine using Apache Solr",
          "num_reviews": 23,
          "title": "Solr in Action",
          "publish_date": "2014-04-05"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "3",
        "_score": 0.14181662,
        "_source": {
          "summary": "build scalable search applications using Elasticsearch without having to do complex low-level programming or understand advanced data science algorithms",
          "num_reviews": 18,
          "title": "Elasticsearch in Action",
          "publish_date": "2015-12-03"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "2",
        "_score": 0.13297246,
        "_source": {
          "summary": "organize text using approaches such as full-text search, proper name recognition, clustering, tagging, information extraction, and summarization",
          "num_reviews": 12,
          "title": "Taming Text: How to Find, Organize, and Manipulate It",
          "publish_date": "2013-01-24"
        }
      }
    ]
  }
}

```

- 注1：我们可以运行一个常规的multi_match查询，并按num_reviews字段排序，但是我们失去了相关性得分的好处。

- 注2：有许多附加参数可以调整对原始相关性分数（如“ modifier ”，“ factor ”，“boost_mode”等）的增强效果的程度。详见 Elasticsearch guide.


# 18、 Function 得分：衰减函数( Function Score: Decay Functions )
假设，我们不是想通过一个字段的值逐渐增加得分，以获取理想的结果。    举例：价格范围、数字字段范围、日期范围。 在我们的例子中，我们正在搜索2014年6月左右出版的“ search engines ”的书籍。


```
GET /bookdb_index/book/_search
{
  "query": {
    "function_score": {
      "query": {
        "multi_match": {
          "query": "search engine",
          "fields": ["title", "summary"]
        }
      },
      "functions": [
        {
          "exp": {
            "publish_date": {
              "origin": "2014-06-15",
              "scale": "30d",
              "offset": "7d"
            }
          }
        }
      ],
      "boost_mode": "replace"
    }
  },
  "_source": ["title", "summary", "publish_date", "num_reviews"]
}



{
  "took": 8,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 4,
    "max_score": 0.22793062,
    "hits": [
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "4",
        "_score": 0.22793062,
        "_source": {
          "summary": "Comprehensive guide to implementing a scalable search engine using Apache Solr",
          "num_reviews": 23,
          "title": "Solr in Action",
          "publish_date": "2014-04-05"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "1",
        "_score": 0.0049215667,
        "_source": {
          "summary": "A distibuted real-time search and analytics engine",
          "num_reviews": 20,
          "title": "Elasticsearch: The Definitive Guide",
          "publish_date": "2015-02-07"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "2",
        "_score": 0.000009612435,
        "_source": {
          "summary": "organize text using approaches such as full-text search, proper name recognition, clustering, tagging, information extraction, and summarization",
          "num_reviews": 12,
          "title": "Taming Text: How to Find, Organize, and Manipulate It",
          "publish_date": "2013-01-24"
        }
      },
      {
        "_index": "bookdb_index",
        "_type": "book",
        "_id": "3",
        "_score": 0.0000049185574,
        "_source": {
          "summary": "build scalable search applications using Elasticsearch without having to do complex low-level programming or understand advanced data science algorithms",
          "num_reviews": 18,
          "title": "Elasticsearch in Action",
          "publish_date": "2015-12-03"
        }
      }
    ]
  }
}


```


问题：没太理解？


# 19、Function得分：脚本得分（ Function Score: Script Scoring ）

在内置计分功能不符合您需求的情况下，可以选择指定用于评分的Groovy脚本
在我们的示例中，我们要指定一个考虑到publish_date的脚本，然后再决定考虑多少评论。 较新的书籍可能没有这么多的评论，所以他们不应该为此付出“代价”
得分脚本如下所示:



```

```
