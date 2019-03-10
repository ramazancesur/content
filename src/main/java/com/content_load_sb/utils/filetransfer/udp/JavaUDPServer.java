package com.content_load_sb.utils.filetransfer.udp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by asd on 19.11.2017.
 */
public class JavaUDPServer {
    public static void main(String args[]) throws IOException {
        byte b[] = new byte[3072];
        DatagramSocket dsoc = new DatagramSocket(1000);
        FileOutputStream f = new FileOutputStream("D:/nandha.txt");
        while (true) {
            DatagramPacket dp = new DatagramPacket(b, b.length);
            dsoc.receive(dp);

            System.out.println(dp.getData());
            System.out.println("------------------");
            System.out.println(dp.getLength());

        }
    }
}