package com.ljqiii;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/*
* 添加新的客户端
* */
public class newClient extends Thread {
    ServerSocket server = null;

    ArrayList<SocketClient> clientlist = null;

    newClient(ServerSocket in, ArrayList<SocketClient> clientlist) {
        this.server = in;
        this.clientlist = clientlist;
        //System.out.println("线程创建成功,id:" + this.getId());
    }

    public void run() {

        SocketClient clienttemp = null;
        //System.out.println("等待客户呼叫");

        while (Thread.currentThread().isInterrupted() == false) {
            while (true) {
                try {
                    Socket temp = server.accept();
                    clienttemp = new SocketClient(temp, new DataOutputStream(temp.getOutputStream()), new DataInputStream(temp.getInputStream()), "defaultNickName");
                    clienttemp.start();
                    clientlist.add(clienttemp);

                    System.out.println("添加成功");
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
