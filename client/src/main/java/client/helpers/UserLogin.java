package client.helpers;

import common.actions.Console;
import common.actions.User;
import common.actions.Request;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class UserLogin {

    private Scanner userScanner;

    public UserLogin(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * Handle user authentication.
     *
     * @return Request of user.
     */
    public Request authentication() {
        LoginAsker asker = new LoginAsker(userScanner);
        String command = asker.askQuestion("У вас уже есть учетная запись?") ? "login" : "register";
        User user = new User(asker.askLogin(), hash(asker.askPassword()));
        return new Request(command, "", user);
    }

    public String hash(String password) {
        try {
            // getInstance() method is called with algorithm MD2
            MessageDigest md = MessageDigest.getInstance("MD2");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(password.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            Console.printerror("Не найден алгоритм хэширования пароля!");
        }
        return null;
    }
}
