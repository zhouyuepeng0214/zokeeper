package com.atguigu.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class ZKclient1 {

    private ZooKeeper zkClient;

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZKclient1 cli = new ZKclient1();

        cli.zkClient = new ZooKeeper("hadoop110:2181,hadoop111:2181,hadoop112:2181",
                2000, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("Type:" + event.getType());
                System.out.println("Path:" + event.getPath());
            }
        });

        cli.getC();

        Thread.sleep(Long.MAX_VALUE);
    }

    @Before
    public void before() throws IOException {
        zkClient = new ZooKeeper("hadoop110:2181,hadoop111:2181,hadoop112:2181", 2000,
                new Watcher() {
                    public void process(WatchedEvent event) {
                        System.out.println("Tpye: " + event.getType());
                        System.out.println("Path: " + event.getPath());
                    }
                });
    }

    @Test
    public void ls() throws KeeperException, InterruptedException {
//        List<String> children = zkClient.getChildren("/", true);

        List<String> children = zkClient.getChildren("/", new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("自定义");
            }
        });

        for (String child : children) {
            System.out.println(child);
        }

        Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void get() throws KeeperException, InterruptedException {
        Stat stat = new Stat();
        byte[] data = zkClient.getData("/atguigu", true, stat);

        System.out.println(new String(data));
        System.out.println(stat.getDataLength());

        Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void set() throws KeeperException, InterruptedException {
        Stat exists = zkClient.exists("/ttt", true);
        if (exists != null) {
            Stat stat = zkClient.setData("/ttt", "999".getBytes(), exists.getVersion());
        }

    }

    @Test
    public void delete () throws KeeperException, InterruptedException {
        Stat exists = zkClient.exists("/ttt", true);
        if (exists != null) {
            zkClient.delete("/ttt", exists.getVersion());
        }
    }

    public void getC() {
        try {
            List<String> children = zkClient.getChildren("/", new Watcher() {
                public void process(WatchedEvent event) {
                    getC();
                }
            });

            System.out.println("=========================================");
            for (String child : children) {
                System.out.println(child);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
