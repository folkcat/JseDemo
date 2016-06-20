package com.folkcat.netdisk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Client {
	public static void main(String args[]) throws UnknownHostException {
		DatagramPacket request;
		DatagramSocket socket;

		String requestStr = "Heelo F88 you ";
		byte requestByte[] = requestStr
				.getBytes(Charset.forName("UTF-8"));
		request = new DatagramPacket(requestByte, requestByte.length,
				InetAddress.getByName("127.0.0.1"), 2222);
		//Scanner input = new Scanner(System.in);
		try {
			socket = new DatagramSocket(0);
			socket.send(request);

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		while (true) {
//			String str = input.next();
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println(str);
//		}
	}
}
