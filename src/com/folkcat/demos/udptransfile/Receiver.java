package com.folkcat.demos.udptransfile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Receiver {

	public static void main(String args[]) throws Exception {
		System.out.println("Ready to receive the file!");

		final int port=2000;
		final String fileName="transfered.png";

		receiveAndCreate(port, fileName);
	}

	public static void receiveAndCreate(int port, String fileName)
			throws IOException {
		// 创建Socket和文件
		DatagramSocket socket = new DatagramSocket(port);
		InetAddress address;
		File file = new File(fileName);
		if (file.exists()){
			file.delete();
		}
		FileOutputStream outToFile = new FileOutputStream(file);

		// 标记是否为最后一条数据报
		boolean lastMessageFlag = false;

		// 序列号
		int sequenceNumber = 0;
		int lastSequenceNumber = 0;

		// For each message we will receive
		while (!lastMessageFlag) {
			
			byte[] message = new byte[1024];
			byte[] fileByteArray = new byte[1021];
			// 接收数据报
			DatagramPacket receivedPacket = new DatagramPacket(
					message, message.length);
			socket.setSoTimeout(0);
			socket.receive(receivedPacket);
			
			//message = receivedPacket.getData();
			
			int lastIndex=message.length-1;//1023;
			System.out.println("message[0]:"+message[0]);
			System.out.println("message[1]:"+message[1]);
			System.out.println("message[2]:"+message[2]);
			int length=message.length;
//			for (int i=0;i<length;i++){
//				System.out.println(message[i]);
//			}
			
			
			for(;lastIndex>0;){
				//System.out.println(message[size]);
				if(message[lastIndex]!=0)
					break;
				lastIndex--;
			}
			System.out.println("最后字节下标："+lastIndex);
			// 提取IP地址以确认
			address = receivedPacket.getAddress();
			port = receivedPacket.getPort();

			// Retrieve sequence number
			sequenceNumber = ((message[0] & 0xff) << 8)
					+ (message[1] & 0xff);

			// Retrieve the last message flag
			if ((message[2] & 0xff) == 1) {
				lastMessageFlag = true;
			} else {
				lastMessageFlag = false;
			}

			if (sequenceNumber == (lastSequenceNumber + 1)) {

				// Update latest sequence number
				lastSequenceNumber = sequenceNumber;

				// Retrieve data from message
				for (int i = 3; i < 1024; i++) {
					fileByteArray[i - 3] = message[i];
				}

				// Write the message to the file
				
				outToFile.write(message,3,lastIndex-2);
				System.out.println(
						"Received: Sequence number = "
								+ sequenceNumber
								+ ", Flag = "
								+ lastMessageFlag);

				// Send acknowledgement
				sendAck(lastSequenceNumber, socket, address,
						port);

				// Check for last message
				if (lastMessageFlag) {
					outToFile.close();
				}
			} else {
				// If packet has been received, send ack for
				// that packet again
				if (sequenceNumber < (lastSequenceNumber + 1)) {
					// Send acknowledgement for received
					// packet
					sendAck(sequenceNumber, socket, address,
							port);
				} else {
					// Resend acknowledgement for last
					// packet received
					sendAck(lastSequenceNumber, socket,
							address, port);
				}
			}
		}

		socket.close();
		System.out.println("File " + fileName + " has been received.");
	}

	public static void sendAck(int lastSequenceNumber,
			DatagramSocket socket, InetAddress address, int port)
			throws IOException {
		// Resend acknowledgement
		byte[] ackPacket = new byte[2];
		ackPacket[0] = (byte) (lastSequenceNumber >> 8);
		ackPacket[1] = (byte) (lastSequenceNumber);
		DatagramPacket acknowledgement = new DatagramPacket(ackPacket,
				ackPacket.length, address, port);
		socket.send(acknowledgement);
		System.out.println("Sent ack: Sequence Number = "
				+ lastSequenceNumber);
	}
}
