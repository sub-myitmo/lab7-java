package client;

import client.helpers.UserHandler;
import client.helpers.UserLogin;
import common.actions.Console;
import common.exceptions.NotWithinEstablishedLimitsException;
import common.exceptions.WrongCommandArgsException;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;
import java.util.logging.Logger;

public class StartClient {
    //private static String host = "localhost";

    private static String host = "pg";
    public static void main(String[] args) {

        try {
            if (args.length != 1) throw new WrongCommandArgsException();
            if (Integer.parseInt(args[0]) < 0) throw new NotWithinEstablishedLimitsException();

            Scanner userScanner = new Scanner(System.in);
            UserHandler userHandler = new UserHandler(userScanner);
            UserLogin userLogin = new UserLogin(userScanner);
            Client client = new Client(host, Integer.parseInt(args[0]), userHandler, userLogin);

            DatagramChannel datagramChannel = DatagramChannel.open();
            InetSocketAddress address = new InetSocketAddress(host, Integer.parseInt(args[0]));
            datagramChannel.connect(address);

            Console.println("Соединение выполнено, хост: " + host);
            client.run();
            userScanner.close();

        } catch (NotWithinEstablishedLimitsException e) {
            Console.printerror("Порт < 0" + e);
        } catch (WrongCommandArgsException exception) {
            String jarFileName = new File(StartClient.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            Console.println("Запускать надо так: 'java -jar " + jarFileName + " <port>'");
        } catch (NumberFormatException exception) {
            Console.printerror("Порт должен быть представлен числом!");
        } catch (Exception e) {
            e.printStackTrace();
            Console.printerror("Возникла неожиданная ошибка!");
        }
    }
}