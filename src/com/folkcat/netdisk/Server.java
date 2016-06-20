package com.folkcat.netdisk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {
	private boolean isClientDead = false;

	public static void main(String args[]) {
		InetAddress address;
		DatagramSocket socket = null;
		byte[] message = new byte[1024];
		DatagramPacket receivedPacket = new DatagramPacket(
				message, message.length);
		try {
			socket = new DatagramSocket(2222);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		while (true) {
			try {
				socket.setSoTimeout(0);
				socket.receive(receivedPacket);
				System.out.println(new String(message));
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			int length = message.length;
		}

	}

}
