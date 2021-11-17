package ru.sensoric.core.communication.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.sensoric.core.communication.Connection;
import ru.sensoric.core.communication.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс TCP сервера
 */
@Component
@Slf4j
public class TcpServer implements Server, Connection.Listener {

    private ServerSocket serverSocket;
    private volatile boolean isStop;
    private List<Connection> connections = new ArrayList<>();
    private List<Connection.Listener> listeners = new ArrayList<>();

    /**
     * Задает порт для нашего соединения, по умолчанию "1234"
     * @param port Значение для порта
     */
    public void setPort(Integer port) {
        try {
            if (port == null) {
                log.info("Property core.server.port not found. Use default port 1234");
                port = 1234;
            }
            serverSocket = new ServerSocket(6666);
            log.info("Server start at port " + port);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("May be port " + port + " busy.");
        }
    }

    /**
     * Возвращает количество соединений
     * @return Количество соединений
     */
    @Override
    public int getConnectionsCount() {
        return connections.size();
    }

    @Override
    public void start() {
        new Thread(() -> {
            while (!isStop) {
                try {
                    Socket socket = serverSocket.accept();
                    if (socket.isConnected()) {
                        TcpConnection tcpConnection = new TcpConnection(socket);
                        tcpConnection.start();
                        tcpConnection.addListener(this);
                        connected(tcpConnection);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Устанавливает флаг
     */
    @Override
    public void stop() {
        isStop = true;
    }

    /**
     * Возвращает лист соединений
     * @return Лист соединений
     */
    @Override
    public List<Connection> getConnections() {
        return connections;
    }

    /**
     * Добавляет слушателя
     * @param listener
     */
    @Override
    public void addListener(Connection.Listener listener) {
        listeners.add(listener);
    }

    /**
     * Принимает сообщение
     * @param connection
     * @param message Сообщение
     */
    @Override
    public void messageReceived(Connection connection, Object message) {
        log.trace("Received new message from " + connection.getAddress().getCanonicalHostName());
        log.trace("Class name: " + message.getClass().getCanonicalName() + ", toString: " + message.toString());
        for (Connection.Listener listener : listeners) {
            listener.messageReceived(connection, message);
        }
    }

    /**
     * Сообщает о новом соединении
     * @param connection
     */
    @Override
    public void connected(Connection connection) {
        log.info("New connection! Ip: " + connection.getAddress().getCanonicalHostName() + ".");
        connections.add(connection);
        log.info("Current connections count: " + connections.size());
        for (Connection.Listener listener : listeners) {
            listener.connected(connection);
        }
    }

    /**
     * Разорвать конкретное соединение
     * @param connection Соединение
     */
    @Override
    public void disconnected(Connection connection) {
        log.info("Disconnect! Ip: " + connection.getAddress().getCanonicalHostName() + ".");
        connections.remove(connection);
        log.info("Current connections count: " + connections.size());
        for (Connection.Listener listener : listeners) {
            listener.disconnected(connection);
        }
    }
}