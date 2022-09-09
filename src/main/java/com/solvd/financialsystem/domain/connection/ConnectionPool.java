package com.solvd.financialsystem.domain.connection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {

    private static volatile ConnectionPool INSTANCE;

    private final BlockingQueue<Connection> connectionPool;
    private final int size;

    private ConnectionPool(int size) {
        this.size = size;
        connectionPool = new ArrayBlockingQueue<>(size);
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

    public synchronized Connection getConnection() {
        Connection result;
        if (connectionPool.size() < size) {
            result = new Connection();
            connectionPool.add(result);
        } else {
            result = connectionPool.peek();
        }
        return result;
    }

    public synchronized void releaseConnection(Connection connection) {
        connectionPool.offer(connection);
        //notifyAll();
    }
}
