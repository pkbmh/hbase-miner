package miner;

import com.ngdata.sep.EventListener;
import com.ngdata.sep.PayloadExtractor;
import com.ngdata.sep.SepModel;
import com.ngdata.sep.impl.BasePayloadExtractor;
import com.ngdata.sep.impl.SepConsumer;
import com.ngdata.sep.impl.SepModelImpl;
import com.ngdata.sep.util.zookeeper.ZkConnectException;
import com.ngdata.sep.util.zookeeper.ZkUtil;
import com.ngdata.sep.util.zookeeper.ZooKeeperItf;
import config.Config;
import config.MinerConfig;
import execptions.SepMinerException;
import listener.SepEventListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;


@Getter
public class SepMiner {
    private final MinerConfig config;
    private final EventListener eventListener;

    private SepConsumer sepConsumer;
    public SepMiner(MinerConfig config, EventListener eventListener) {
        this.config = config;
        this.eventListener = eventListener;
    }
    public void start() throws SepMinerException {
        try {
            Configuration hbaseConfig = HBaseConfiguration.create();
            hbaseConfig.setBoolean("hbase.replication", true);
            ZooKeeperItf zk = ZkUtil.connect(config.getZookeeperConnectString(), 20000);
            SepModel sepModel = new SepModelImpl(zk, hbaseConfig);

            final String subscriptionName = config.getSubscriptionName();

            if (!sepModel.hasSubscription(subscriptionName)) {
                sepModel.addSubscriptionSilent(subscriptionName);
            }
            sepConsumer = new SepConsumer(subscriptionName, 0, eventListener , 1, "localhost", zk, hbaseConfig, null);
            sepConsumer.start();
            System.out.println("Started");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ZkConnectException e) {
            e.printStackTrace();
        }
    }

    public void stop() throws SepMinerException {
        sepConsumer.stop();
    }

}
