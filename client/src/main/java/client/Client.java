package client;

import client.helpers.UserHandler;
import client.helpers.UserLogin;
import common.actions.*;
import common.actions.Console;
import common.exceptions.LoginException;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Client {

    private String host;
    private int port;

    private UserHandler userHandler;
    private UserLogin userLogin;
    private User user;
    private DatagramChannel datagramChannel = DatagramChannel.open();

    public Client(String host, int port, UserHandler userHandler, UserLogin userLogin) throws IOException {
        this.host = host;
        this.port = port;
        this.userHandler = userHandler;
        this.userLogin= userLogin;
        datagramChannel.configureBlocking(false);
    }

    public void run() {
        try {
            while (true) {
                try {
                    authentication();
                    requestToServer();
                    Console.println("Работа клиента завершена.");
                } catch (Exception exception) {
                    Console.println("Ошибка при работе клиента");
                }
                if (datagramChannel != null) datagramChannel.close();
                Console.println("Работа клиента завершена.");
                System.exit(0);
            }
        } catch (IOException e) {
            Console.println("Возникла ошибка!");
        }

    }

    private boolean requestToServer() {
        Request requestToServer = null;
        Response serverResponse = null;
        do {
            try {
                requestToServer = serverResponse != null ? userHandler.handle(serverResponse.getIsGoodResponse(), user) :
                        userHandler.handle(null, user);
                if (requestToServer.isEmpty()) continue;

                ByteArrayOutputStream serverWriter = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(serverWriter);
                objectOutputStream.writeObject(requestToServer);
                byte[] bytes;
                bytes = serverWriter.toByteArray();
                ByteBuffer buffer = ByteBuffer.allocate(4096);
                buffer.put(bytes);
                buffer.flip();
                InetSocketAddress address = new InetSocketAddress(host, port);
                datagramChannel.send(buffer, address);

                ByteBuffer receiveBuffer = ByteBuffer.allocate(4096);

                long timeout = 5000;
                long start = System.currentTimeMillis();
                while (datagramChannel.receive(receiveBuffer) == null && System.currentTimeMillis() - start < timeout) {
                    Thread.sleep(100);
                }

                if (System.currentTimeMillis() - start >= timeout) {
                    System.out.println("Превышено время ожидания ответа от сервера. Повторите попытку позже");
                    continue;
                }

                receiveBuffer.flip();
                byte[] data = new byte[receiveBuffer.limit()];
                receiveBuffer.get(data);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);

                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Object deserializedObject = objectInputStream.readObject();
                serverResponse = (Response) deserializedObject;
                Console.println(serverResponse.getResponse());

                Thread.sleep(500);

                if (Objects.equals(serverResponse.getResponse(), new LoginException().toString() + "\n")) authentication();

            } catch (NullPointerException e) {
                Console.printerror("Недопустимый ввод");
                assert serverResponse != null;
                requestToServer = userHandler.handle(serverResponse.getIsGoodResponse(), user);
            } catch (ClassNotFoundException e) {
                Console.printerror("Ошибка при чтении пакета");
            } catch (IOException e) {
                Console.printerror("Непредвиденная ошибка при отправке данных");
            } catch (InterruptedException e) {
                Console.printerror("Прервано ожидание ответа от сервера");
            }
        } while (!requestToServer.getName().equals("exit"));

        return false;
    }


    private void authentication() {
        Request requestToServer = null;
        Response serverResponse = null;

        ByteBuffer buffer = ByteBuffer.allocate(4096);
        //DatagramChannel datagramChannel;
        InetSocketAddress serverAddress = new InetSocketAddress(host, port);

//        try {
//            datagramChannel = DatagramChannel.open();
//            datagramChannel.configureBlocking(true);
//        } catch (IOException e) {
//            throw new RuntimeException("Ошибка при создании DatagramChannel");
//        }

        do {
            try {
                requestToServer = userLogin.authentication();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(requestToServer);
                byte[] dataToSend = baos.toByteArray();
                buffer.clear();
                buffer.put(dataToSend);
                buffer.flip();
                datagramChannel.send(buffer, serverAddress);

                buffer.clear();

                long timeout = 5000;
                long start = System.currentTimeMillis();
                while (datagramChannel.receive(buffer) == null && System.currentTimeMillis() - start < timeout) {
                    Thread.sleep(100);
                }

                if (System.currentTimeMillis() - start >= timeout) {
                    System.out.println("Превышено время ожидания ответа от сервера. Повторите попытку позже");
                    continue;
                }
                datagramChannel.receive(buffer);
                buffer.flip();

                byte[] responseData = new byte[buffer.limit()];
                buffer.get(responseData);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(responseData);

                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Object deserializedObject = objectInputStream.readObject();
                serverResponse = (Response) deserializedObject;
                Console.println(serverResponse.getResponse());

            } catch (InvalidClassException | NotSerializableException exception) {
                Console.printerror("Ошибка при отправке данных на сервер!");
            } catch (ClassNotFoundException exception) {
                Console.printerror("Ошибка при чтении полученных данных!");
            } catch (IOException exception) {
                Console.printerror("Соединение с сервером разорвано!");
            } catch (InterruptedException e) {
                Console.printerror("Прервано ожидание ответа от сервера");
            }
        } while (serverResponse == null || !serverResponse.getIsGoodResponse().equals(ResponseCode.OK));
        user = requestToServer.getUser();
    }

}
