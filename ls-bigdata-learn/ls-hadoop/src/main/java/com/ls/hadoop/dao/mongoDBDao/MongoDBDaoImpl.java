package com.ls.hadoop.dao.mongoDBDao;

import com.mongodb.*;

import java.util.ArrayList;

/*
 * mongodb数据库链接池
 */
public class MongoDBDaoImpl implements MongoDBDao
{
    private MongoClient mongoClient = null;
    private static final MongoDBDaoImpl mongoDBDaoImpl = new MongoDBDaoImpl();// 饿汉式单例模式

    private MongoDBDaoImpl()
    {
        if (mongoClient == null)
        {
            MongoClientOptions.Builder buide = new MongoClientOptions.Builder();
            buide.connectionsPerHost(100);// 与目标数据库可以建立的最大链接数
            buide.connectTimeout(1000 * 60 * 20);// 与数据库建立链接的超时时间
            buide.maxWaitTime(100 * 60 * 5);// 一个线程成功获取到一个可用数据库之前的最大等待时间
            buide.threadsAllowedToBlockForConnectionMultiplier(100);
            buide.maxConnectionIdleTime(0);
            buide.maxConnectionLifeTime(0);
            buide.socketTimeout(0);
            buide.socketKeepAlive(true);
            MongoClientOptions myOptions = buide.build();
            try
            {
                mongoClient = new MongoClient(new ServerAddress("172.17.201.15", 27017), myOptions);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static MongoDBDaoImpl getMongoDBDaoImpl()
    {
        return mongoDBDaoImpl;
    }

    public DB getDb(String dbName)
    {
        return mongoClient.getDB(dbName);
    }

    public DBCollection getCollection(String dbName, String collectionName)
    {
        DB db = mongoClient.getDB(dbName);
        return db.getCollection(collectionName);
    }

    public boolean inSert(String dbName, String collectionName, String keys, Object values)
    {
        DB db = mongoClient.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        long num = dbCollection.count();
        BasicDBObject doc = new BasicDBObject();
        doc.put(keys, values);
        dbCollection.insert(doc);
        if (dbCollection.count() - num > 0)
        {
            System.out.println("添加数据成功！！！");
            return true;
        }
        return false;
    }

    public boolean delete(String dbName, String collectionName, String keys, Object values)
    {
        WriteResult writeResult = null;
        DB db = mongoClient.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        BasicDBObject doc = new BasicDBObject();
        doc.put(keys, values);
        writeResult = dbCollection.remove(doc);
        if (writeResult.getN() > 0)
        {
            System.out.println("删除数据成功!!!!");
            return true;
        }
        return false;
    }

    public ArrayList<DBObject> find(String dbName, String collectionName, int num)
    {
        int count=num;
        ArrayList<DBObject> list = new ArrayList<DBObject>();
        DB db = mongoClient.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        DBCursor dbCursor = dbCollection.find();
        if (num == -1)
        {
            while (dbCursor.hasNext())
            {
                list.add(dbCursor.next());
            }
        } else
        {
            while(dbCursor.hasNext())
            {
                if(count==0) break;
                list.add(dbCursor.next());
                count--;
            }
        }
        return list;
    }

    public boolean update(String dbName, String collectionName, DBObject oldValue, DBObject newValue)
    {
        WriteResult writeResult = null;
        DB db = mongoClient.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        writeResult = dbCollection.update(oldValue, newValue);
        if (writeResult.getN() > 0)
        {
            System.out.println("数据更新成功");
            return true;
        }
        return false;
    }

    public boolean isExit(String dbName, String collectionName, String key, Object value)
    {
        DB db = mongoClient.getDB(dbName);
        DBCollection dbCollection = db.getCollection(collectionName);
        BasicDBObject doc = new BasicDBObject();
        doc.put(key, value);
        if (dbCollection.count(doc) > 0)
        {
            return true;
        }
        return false;
    }
    public static void main(String args[])
    {
        MongoDBDaoImpl mongoDBDaoImpl=MongoDBDaoImpl.getMongoDBDaoImpl();
        ArrayList<DBObject> list=new ArrayList<DBObject>();
        list=mongoDBDaoImpl.find("jd", "phone",-1);
        System.out.println(list.size());
    }
}