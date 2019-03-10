package com.content_load_sb.utils.filetransfer.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Created by asd on 19.11.2017.
 */
public class JavaUDPClient {


    public static byte[] readFileByte(String filePath) {
        Path path = Paths.get(filePath);
        try {
            byte[] data = Files.readAllBytes(path);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String args[]) throws Exception {
        byte b[] = readFileByte("D:\\staff.json");
        DatagramSocket dsoc = new DatagramSocket(2000);
        dsoc.send(new DatagramPacket(b, b.length, InetAddress.getLocalHost(), 1000));
    }

}
