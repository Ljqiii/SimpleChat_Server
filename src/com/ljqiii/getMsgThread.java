package com.ljqiii;


import java.io.DataInputStream;
import java.io.IOException;


/*
 * 监听消息
 * */
class getMsgThread extends Thread {
    SocketClient ins;
    DataInputStream in = null;
    String receivetemp = "";

    getMsgThread(SocketClient ins) {
        this.ins = ins;
        in = ins.getIn();
    }

    public void run() {
        while (true) {
            try {
                char a = in.readChar();

                if (a != '\n') {
                    receivetemp = receivetemp + a;
                } else if (a == '\n') {

                    //System.out.println("receivetemp:"+receivetemp);
                    //System.out.print(receivetemp.contains(":"));
                    if (receivetemp.contains(":")) {

                        ins.parseMsg(receivetemp);
                    }

                    receivetemp = "";
                }
            } catch (IOException e) {
                //e.printStackTrace();
                Main.removeClient(ins);
                break;
            }
        }
    }
}