package com.ljqiii;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/*
* 客户
* */

public class SocketClient extends Thread {
    Socket s = null;
    DataOutputStream out = null;
    DataInputStream in = null;
    String nickname = "";

    public SocketClient() {

    }

    public SocketClient(Socket s, DataOutputStream out, DataInputStream in, String nickname) {
        this.s = s;
        this.out = out;
        this.in = in;
        this.nickname = nickname;
    }

    //发送消息
    public void send(String msg) {

        for (int i = 0; i < msg.length(); i++) {
            try {
                this.out.writeChar(msg.charAt(i));
            } catch (IOException e) {
                //e.printStackTrace();
                Main.removeClient(this);
            }

        }
    }
    //发送消息
    public void sendsto(String nickname,String msg){
        System.out.println("in func sendto:");

        for(int i=0;i<Main.clientlist.size();i++){

            String getednickname=Main.clientlist.get(i).getNickname();
            System.out.println("i:"+i+" "+"getednickname:"+getednickname);
            if(nickname.equals(getednickname)){
                //receice:from|msg
                System.out.println("receive:"+this.getNickname()+"|"+msg);
                Main.clientlist.get(i).send("receive:"+this.getNickname()+"|"+msg+"\n");
                break;
            }
        }

    }


    //解析消息
    public void parseMsg(String msg) {

        String command = msg.split(":")[0];
        String content = msg.split(":")[1];
        //设置客户端昵称
        if(command.equals("setnickname")){
            this.setNickname(content);

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
                        e.printStackTrace();

                    }

                }

            }
            

            System.out.println("set nickname ok");

        }
        //发送消息
        //sendmsg:to|msg
        else  if(command.equals("sendmsg")){
            if(content.contains("|")){
                String to=content.split("\\|")[0];
                String tomsg=content.split("\\|")[1];
                sendsto(to,tomsg);
            }
        }
        //获得所有昵称列表
        //未使用
        else if(command.equals("getallname")){
            String allnickname="";
            for(int i=0;i<Main.clientlist.size();i++){
                allnickname=allnickname+ Main.clientlist.get(i).getNickname()+",";
            }
            allnickname="allnickname:"+allnickname.substring(0,allnickname.length()-1);
            this.send(allnickname+"\n");
        }
        else{
            System.out.print("else");
        }


    }


    public void run() {
        getMsgThread readchar = new getMsgThread(this);
        readchar.start();
    }


    public void setS(Socket s) {
        this.s = s;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public Socket getS() {
        return s;
    }

    public String getNickname() {
        return nickname;
    }

    public DataInputStream getIn() {
        return in;
    }
}


