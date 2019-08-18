package demo.hivemetastore;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.hadoop.hive.metastore.IMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: all
 * @author: lishuai
 * @create: 2019-08-06 11:24
 */
public class HiveMetaStoreUtil {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(HiveMetaStoreUtil.class);
    private static IMetaStoreClient client = null;

    //初始化工具类的client对象
    public static void init(IMetaStoreClient iMetaStoreClient) {
        client = iMetaStoreClient;
    }

    /**
     * 获取全部的数据库
     * @return
     */
    public static Iterable<String> getAllDatabases() {
        Iterable<String> results = null;
        try {
            if (client == null) {
                LOGGER.warn("Hive client is null. " +
                        "Please check your hive config.");
                return new ArrayList<>();
            }
            results = client.getAllDatabases();//获取所有的hive上的db名称
        } catch (Exception e) {
            reconnect();
            LOGGER.error("Can not get databases : {}", e);
        }
        return results;
    }

    private static void reconnect() {
        try {
            client.reconnect();
        } catch (Exception e) {
            LOGGER.error("reconnect to hive failed: {}", e);
        }
    }


    /**
     * 获取全部的数据库
     * @param dbName
     * @return
     */
    public static Iterable<String> getAllTableNames(String dbName) {
        Iterable<String> results = null;
        try {
            if (client == null) {
                LOGGER.warn("Hive client is null. " +
                        "Please check your hive config.");
                return new ArrayList<>();
            }
            results = client.getAllTables(getUseDbName(dbName));//获取库下的所有表
        } catch (Exception e) {
            reconnect();
            LOGGER.error("Exception fetching tables info: {}", e);
            return null;
        }
        return results;
    }


    /**
     * 获取指定数据库下的表
     * @param db
     * @return
     */
    public static List<Table> getAllTable(String db) {
        return getTables(db);
    }


    /**
     * 获取全部库下的表
     * @return
     */
    public static Map<String, List<String>> getAllTableNames() {//获取整个hive每个包含的每个表，只包含了name
        Map<String, List<String>> result = new HashMap<>();
        for (String dbName: getAllDatabases()) {
            result.put(dbName, Lists.newArrayList(getAllTableNames(dbName)));
        }
        return result;
    }


    //获取整个hive每个包含的每个表，只包含了表的详细信息
    public static  Map<String, List<Table>> getAllTable() {
        Map<String, List<Table>> results = new HashMap<>();
        Iterable<String> dbs;
        // if hive.metastore.uris in application.properties configs wrong,
        // client will be injected failure and will be null.
        if (client == null) {
            LOGGER.warn("Hive client is null. Please check your hive config.");
            return results;
        }
        dbs = getAllDatabases();
        if (dbs == null) {
            return results;
        }
        for (String db : dbs) {
            // TODO: getAllTable() is not reusing caches of getAllTable(db) and vise versa
            // TODO: getTables() can return empty values on metastore exception
            results.put(db, getTables(db));
        }
        return results;
    }


    //获取单个表信息
    public static Table getTable(String dbName, String tableName) {
        Table result = null;
        try {
            if (client == null) {
                LOGGER.warn("Hive client is null. " +
                        "Please check your hive config.");
                return null;
            }
            result = client.getTable(getUseDbName(dbName), tableName);
        } catch (Exception e) {
            reconnect();
            LOGGER.error("Exception fetching table info : {}. {}", tableName,
                    e);
        }
        return result;
    }

    /**
     * 获取指定库下的表
     * @param db
     * @return
     */
    private static List<Table> getTables(String db) {
        String useDbName = getUseDbName(db);//校验万一db为空，返回配置的默认db
        List<Table> allTables = new ArrayList<>();
        try {
            if (client == null) {
                LOGGER.warn("Hive client is null. " +
                        "Please check your hive config.");
                return allTables;
            }
            Iterable<String> tables = client.getAllTables(useDbName);//获取所有的表信息
            for (String table : tables) {
                Table tmp = client.getTable(db, table);
                allTables.add(tmp);
            }
        } catch (Exception e) {
            reconnect();
            LOGGER.error("Exception fetching tables info: {}", e);
        }
        return allTables;
    }

    /**
     * 如果数据库不合法返回默认的
     * @param dbName
     * @return
     */
    private  static String getUseDbName(String dbName) {
        if (Strings.isNullOrEmpty(dbName)) {
            return "default";
        } else {
            return dbName;
        }
    }
}
