package com.solvd.financialsystem.domain.connection;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool {

    private static volatile ConnectionPool INSTANCE;

    private final BlockingQueue<Connection> connectionPool;
    private final int size;

    private ConnectionPool(int size) {
        this.size = size;
        connectionPool = new LinkedBlockingQueue<>(size);
    }

    synchronized public static ConnectionPool getInstance(int size) {
        if (INSTANCE == null) {
            INSTANCE = new ConnectionPool(size);
        }
        return INSTANCE;
    }

    synchronized public static ConnectionPool getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() throws InterruptedException {
        Connection result;
        synchronized (connectionPool) {
            if (connectionPool.size() < size) {
                result = new Connection();
            } else {
                result = connectionPool.take();
            }
        }
        return result;
    }

    public void releaseConnection(Connection connection) {
        synchronized (connectionPool) {
            connectionPool.add(connection);
        }
    }
}
