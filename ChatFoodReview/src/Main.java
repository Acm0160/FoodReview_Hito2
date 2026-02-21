import java.io.*;
import java.net.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(5000);
        System.out.println("Servidor escuchando en 5000...");

        Socket client = server.accept();
        System.out.println("Cliente conectado: " + client.getInetAddress());

        DataInputStream in = new DataInputStream(new BufferedInputStream(client.getInputStream()));
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));

        new Thread(() -> {
            try {
                while (true) {
                    String msg = in.readUTF();
                    System.out.println("Cliente: " + msg);
                }
            } catch (Exception e) {
                System.out.println("Cliente desconectado");
            }
        }).start();

        Scanner sc = new Scanner(System.in);
        while (true) {
            String mensajeAEnviar = sc.nextLine();
            out.writeUTF("Soporte: " +mensajeAEnviar);
            out.flush();
        }
    }
}