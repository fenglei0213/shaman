package org.shaman.example;

import org.h2.tools.Server;

import java.sql.SQLException;

/**
 * Created by fenglei on 2016/6/10.
 */
public class H2DataBase {

    private static Server server;

    /**
     *  start start
     */
    public static void start() {
        try {
            System.out.println("正在启动h2...");
            server = Server.createTcpServer(
                    new String[]{"-tcp", "-tcpAllowOthers", "-tcpPort",
                            "8043"}).start();
            System.out.println("启动成功：" + server.getStatus());
        } catch (SQLException e) {
            System.out.println("启动h2出错：" + e.toString());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * stop stop
     *
     */
    public static void stop() {
        if (server != null) {
            System.out.println("正在关闭h2...");
            server.stop();
            System.out.println("关闭成功.");
        }
    }
}
