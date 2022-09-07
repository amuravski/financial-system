package com.solvd.financialsystem.domain.connections;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConnectionPool {

    private static volatile ConnectionPool INSTANCE;

    private final List<Connection> connectionPool;
    private final int size;

    private ConnectionPool(int size) {
        this.size = size;
        connectionPool = new ArrayList<>(size);
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

    public synchronized Optional<Connection> getConnection() {
        Connection connection;
        Optional<Connection> result = Optional.empty();
        if (connectionPool.size() < size) {
            connection = new Connection();
            result = Optional.of(connection);
            connectionPool.add(connection);
        }
        return result;
    }

    public void releaseConnection(Connection connection) {
        connectionPool.remove(connection);
    }

    public void releaseAll() {
        connectionPool.clear();
    }
}
