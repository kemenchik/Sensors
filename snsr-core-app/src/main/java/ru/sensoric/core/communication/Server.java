package ru.sensoric.core.communication;

import java.util.List;

/**
 * Интерфейс сервера
 */
public interface Server {
    int getConnectionsCount();

    void setPort(Integer port);

    void start();

    void stop();

    List<Connection> getConnections();

    void addListener(Connection.Listener listener);
}
