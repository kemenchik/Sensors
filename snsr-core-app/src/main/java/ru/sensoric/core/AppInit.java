package ru.sensoric.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.sensoric.core.communication.impl.TcpSensoricListener;
import ru.sensoric.core.communication.impl.TcpServer;

/**
 * Класс запуска приложения
 */
@Component
public class AppInit implements ApplicationRunner {

    @Autowired
    private TcpSensoricListener tcpSensoricListener;

    @Autowired
    private TcpServer tcpServer;

    @Value("${core.server.port}")
    private int port;
    @Override
    public void run(ApplicationArguments args) {
            tcpServer.setPort(port);
            tcpServer.addListener(tcpSensoricListener);
            tcpServer.start();
    }

}
