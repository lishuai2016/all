package demo.hivemetastore;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.IMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @program: all
 * @author: lishuai
 * @create: 2019-08-06 11:19
 *
 *MetaStoreServer提供hive的元数据信息
 *
 * 启动hive后
 *
 * 通过命令启动服务：hive --service metastore
 *
 * 问题：
 * 暂时只能获取数据库名称，没法获得库下的表以及表的详细信息
 */
public class HiveMetaStoreProxy {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(HiveMetaStoreProxy.class);

    //@Value("${hive.metastore.uris}")
    private String uris= "thrift://192.168.254.101:9083";

    /**
     * Set attempts and interval for HiveMetastoreClient to retry.
     *
     * @hive.hmshandler.retry.attempts: The number of times to retry a
     * HMSHandler call if there were a connection error
     * .
     * @hive.hmshandler.retry.interval: The time between HMSHandler retry
     * attempts on failure.
     */
    //@Value("${hive.hmshandler.retry.attempts}")
    private int attempts=15;

    //@Value("${hive.hmshandler.retry.interval}")
    private String interval="2000ms";

    private IMetaStoreClient client = null;


    public IMetaStoreClient initHiveMetastoreClient() {
        System.setProperty("hadoop.home.dir", "D:\\hadoop\\hadoop-2.7.3");
        HiveConf hiveConf = new HiveConf();
        hiveConf.set("hive.metastore.local", "false");
        hiveConf.setIntVar(HiveConf.ConfVars.METASTORETHRIFTCONNECTIONRETRIES,
                3);
        hiveConf.setVar(HiveConf.ConfVars.METASTOREURIS, uris);
        hiveConf.setIntVar(HiveConf.ConfVars.HMSHANDLERATTEMPTS, attempts);
        hiveConf.setVar(HiveConf.ConfVars.HMSHANDLERINTERVAL, interval);
        try {
            client = HiveMetaStoreClient.newSynchronizedClient(new HiveMetaStoreClient(hiveConf));
        } catch (Exception e) {
            LOGGER.error("Failed to connect hive metastore. {}", e);
        }
        return client;
    }

    public void destroy() {
        if (null != client) {
            client.close();
        }
    }


    public static void main(String[] args) {
        HiveMetaStoreProxy hiveMetaStoreProxy = new HiveMetaStoreProxy();
        IMetaStoreClient iMetaStoreClient = hiveMetaStoreProxy.initHiveMetastoreClient();
        HiveMetaStoreUtil.init(iMetaStoreClient);
        Iterable<String> allDatabases = HiveMetaStoreUtil.getAllDatabases();
        System.out.println(allDatabases);
        List<Table> allTable = HiveMetaStoreUtil.getAllTable("default");
        System.out.println(allTable);

        Table table = HiveMetaStoreUtil.getTable("default", "testtable");
        System.out.println(table);
    }
}

