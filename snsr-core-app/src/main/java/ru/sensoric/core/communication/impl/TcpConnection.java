package ru.sensoric.core.communication.impl;

import ru.sensoric.core.communication.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Клаасс TCP соединения
 */
public class TcpConnection implements Connection {
    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket socket;
    private List<Listener> listeners = new ArrayList<>();

    /**
     * Конструктор класса
     * @param socket
     */
    public TcpConnection(Socket socket) {
        this.socket = socket;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Данный метод позволяет узнать адрес к которому подключен сокет
     * @return Возвращает адрес к которому подключаеться сокет
     */
    @Override
    public InetAddress getAddress() {
        return socket.getInetAddress();
    }

    /**
     * Отправляет обект, который мы хотим передать
     * @param objectToSend  обьект типа Object, который мы хотим отправить
     */
    @Override
    public void send(Object objectToSend) {
        if (objectToSend instanceof byte[]) {
            byte[] data = (byte[]) objectToSend;
            try {
                outputStream.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Добавить слушателя
     * @param listener
     */
    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Создает сессию
     */
    @Override
    public void start() {
        new Thread(() -> {
            while (true) {
                byte[] buf = new byte[512];
                try {
                    int count = inputStream.read(buf);
                    if (count > 0) {
                        byte[] bytes = Arrays.copyOf(buf, count);
                        for (Listener listener : listeners) {
                            listener.messageReceived(this, bytes);
                        }
                    } else {
                        socket.close();
                        for (Listener listener : listeners) {
                            listener.disconnected(this);
                        }
                        break;
                    }
                } catch (IOException e) {
                    for (Listener listener : listeners) {
                        listener.disconnected(this);
                    }
                    break;
                }
            }
        }).start();
    }

    /**
     * Закрывает сессию
     */
    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
