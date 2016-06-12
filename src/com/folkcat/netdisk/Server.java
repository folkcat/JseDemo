package com.folkcat.netdisk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {
	private boolean isClientDead = false;

	public static void main(String args[]) {
		InetAddress address;
		DatagramSocket socket=null;
		try {
			socket = new DatagramSocket(2222);
		} catch (SocketException e) {
			e.printStackTrace();
		}finally{
			if(socket!=null)
				socket.close();
		}
		

		while (true) {
			// Create byte array for full message and another for
			// file data without header
			byte[] message = new byte[1024];
			DatagramPacket receivedPacket = new DatagramPacket(
					message, message.length);
			try {
				socket.setSoTimeout(0);
				socket.receive(receivedPacket);
			}catch(SocketException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				if(socket!=null){
					socket.close();
				}
			}
			

			int length = message.length;
		}
	}

}
