package com.ljqiii;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static ArrayList<SocketClient> clientlist;


    //从客户端移除下线的clientlist,并通知其他所有客户端
    public static void removeClient(SocketClient in) {
        clientlist.remove(in);
        System.out.println("Client --,now:"+String.valueOf(clientlist.size()));


        if(clientlist.size()!=0){
            String allnickname="";
            for(int i=0;i<Main.clientlist.size();i++){
                allnickname=allnickname+ Main.clientlist.get(i).getNickname()+",";
            }
            allnickname="allnickname:"+allnickname.substring(0,allnickname.length()-1);
            allnickname=allnickname+"\n";

            DataOutputStream tempout;
            for (int ii=0;ii<Main.clientlist.size();ii++){
                tempout=Main.clientlist.get(ii).getOut();

                for (int i = 0; i < allnickname.length(); i++) {
                    try {
                        tempout.writeChar(allnickname.charAt(i));
                    } catch (IOException e) {
                        //导致出现原因,在短时间内有多核客户端同时离线
                        //发送失败,无需处理异常
                        //e.printStackTrace();
                    }

                }

            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        clientlist = new ArrayList<SocketClient>();
        ServerSocket server = null;
        Socket clientside = null;
        DataInputStream in = null;
        DataOutputStream out = null;
        int port;
        Scanner scanerin=new Scanner(System.in);
        System.out.print("请输入要运行的端口:");
        port=scanerin.nextInt();


        //新的Socket Server
        try {
            server = new ServerSocket(port);
        } catch (java.net.BindException e) {
            System.out.println("端口被占用");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //添加线程,用于添加新的SocketClient对象
        newClient addclientthread = new newClient(server, clientlist);
        addclientthread.start();

    }
}


