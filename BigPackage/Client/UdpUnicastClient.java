package BigPackage.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import BigPackage.BufferPointer;
import BigPackage.MarshUtil;

/**
 * Created by dan.geabunea on 6/3/2016.
 */
public class UdpUnicastClient implements Runnable {
    private final int port;

    public UdpUnicastClient(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        /**
         * Bind the client socket to the port on which you expect to
         * read incoming messages
         */
        try (DatagramSocket clientSocket = new DatagramSocket(port)) {
            /**
             * Create a byte array buffer to store incoming data. If the message length
             * exceeds the length of your buffer, then the message will be truncated. To avoid this,
             * you can simply instantiate the buffer with the maximum UDP packet size, which
             * is 65506
             */

            byte[] buffer = new byte[65507];
            BufferPointer bufPt = new BufferPointer();

            // Set a timeout of 3000 ms for the client.
            clientSocket.setSoTimeout(3000);
            while (true) {
                DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length);

                /**
                 * The receive method will wait for 3000 ms for data.
                 * After that, the client will throw a timeout exception.
                 */
                clientSocket.receive(datagramPacket);

                byte[] receivedMessage = datagramPacket.getData();
                int unmarshalledInt = MarshUtil.unmarshInt(receivedMessage, bufPt);
                System.out.println(100 + unmarshalledInt);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Timeout. Client is closing.");
        }
    }
}