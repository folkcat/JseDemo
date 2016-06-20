package com.folkcat.demos.udpholepunch;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {

	public static void main(String[] args) {
		try {
			DatagramSocket server = new DatagramSocket(2008);
			byte[] buf = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buf,
					buf.length);

			String sendMessageA = "";
			String sendMessageB = "";
			int portA = 0;
			int portB = 0;
			InetAddress addressA = null;
			InetAddress addressB = null;
			for (;;) {
				server.receive(packet);

				String receiveMessage = new String(
						packet.getData(), 0,
						packet.getLength());
				System.out.println(receiveMessage);
				// 接收到clientA
				if (receiveMessage.contains("A")) {
					portA = packet.getPort();
					addressA = packet.getAddress();
					sendMessageA = "host:"
							+ addressA.getHostAddress()
							+ ",port:" + portA;
				}
				// 接收到clientB
				if (receiveMessage.contains("B")) {
					portB = packet.getPort();
					addressB = packet.getAddress();
					sendMessageB = "host:"
							+ addressB.getHostAddress()
							+ ",port:" + portB;
				}
				// 两个都接收到后分别A、B址地交换互发
				if (!sendMessageA.equals("")
						&& !sendMessageB.equals("")) {
					sendA(sendMessageB, portA,
							addressA, server);
					sendB(sendMessageA, portB,
							addressB, server);
					sendMessageA = "";
					sendMessageB = "";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void sendB(String sendMessageA, int portA,
			InetAddress addressA, DatagramSocket server) {
		try {
			byte[] sendBuf = sendMessageA.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendBuf,
					sendBuf.length, addressA, portA);
			server.send(sendPacket);
			System.out.println("消息发送成功!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void sendA(String sendMessageB, int portB,
			InetAddress addressB, DatagramSocket server) {
		try {
			byte[] sendBuf = sendMessageB.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendBuf,
					sendBuf.length, addressB, portB);
			server.send(sendPacket);
			System.out.println("消息发送成功!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}