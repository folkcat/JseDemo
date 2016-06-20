package com.folkcat.demos.udptransfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Vector;

public class Sender {

	public static void main(String args[]) throws Exception {
		// 获取 目标主机IP、端口、要上传的文件路径
		final String hostName = args[0];
		final int port = Integer.parseInt(args[1]);
		final String fileName = args[2];

		createAndSend(hostName, port, fileName);
	}

	public static void createAndSend(String hostName, int port,
			String fileName) throws IOException {
		System.out.println("Sending the file");

		// 创建Socket
		DatagramSocket socket = new DatagramSocket();
		InetAddress address = InetAddress.getByName(hostName);
		File file = new File(fileName);

		// 存储文件的字节数组
		InputStream inFromFile = new FileInputStream(file);
		byte[] fileByteArray = new byte[(int) file.length()];
		inFromFile.read(fileByteArray);

		StartTime timer = new StartTime(0);

		// 标记报文的序列号以及是否最后一条报文
		int sequenceNumber = 0;
		boolean lastMessageFlag = false;

		
		int ackSequenceNumber = 0;
		int lastAckedSequenceNumber = 0;
		boolean lastAcknowledgedFlag = false;

		int retransmissionCounter = 0;
		int windowSize = 128;

		// Vector to store the sent messages
		Vector<byte[]> sentMessageList = new Vector<byte[]>();

		// For as each message we will create
		for (int i = 0; i < fileByteArray.length; i = i + 1021) {

			sequenceNumber += 1;

			byte[] message = new byte[1024];

			// 提取序列号
			message[0] = (byte) (sequenceNumber >> 8);
			message[1] = (byte) (sequenceNumber);

			// Set flag to 1 if packet is last packet and store it
			// in third byte of header
			if ((i + 1021) >= fileByteArray.length) {
				lastMessageFlag = true;
				message[2] = (byte) (1);
			} else { // If not last message store flag as 0
				lastMessageFlag = false;
				message[2] = (byte) (0);
			}

			// Copy the bytes for the message to the message array
			if (!lastMessageFlag) {
				for (int j = 0; j != 1021; j++) {
					message[j + 3] = fileByteArray[i + j];
				}
			} else if (lastMessageFlag) { // If it is the last
							// message
				for (int j = 0; j < (fileByteArray.length
						- i); j++) {
					message[j + 3] = fileByteArray[i + j];
				}
			}

			// Package the message
			DatagramPacket sendPacket = new DatagramPacket(message,
					message.length, address, port);

			// Add the message to the sent message list
			sentMessageList.add(message);

			while (true) {
				// If next sequence number is outside the window
				if ((sequenceNumber
						- windowSize) > lastAckedSequenceNumber) {

					boolean ackRecievedCorrect = false;
					boolean ackPacketReceived = false;

					while (!ackRecievedCorrect) {
						// Check for an ack
						byte[] ack = new byte[2];
						DatagramPacket ackpack = new DatagramPacket(
								ack,
								ack.length);

						try {
							socket.setSoTimeout(50);
							socket.receive(ackpack);
							ackSequenceNumber = ((ack[0]
									& 0xff) << 8)
									+ (ack[1] & 0xff);
							ackPacketReceived = true;
						} catch (SocketTimeoutException e) {
							ackPacketReceived = false;
							// System.out.println("Socket
							// timed out while
							// waiting for an
							// acknowledgement");
							// e.printStackTrace();
						}

						if (ackPacketReceived) {
							if (ackSequenceNumber >= (lastAckedSequenceNumber
									+ 1)) {
								lastAckedSequenceNumber = ackSequenceNumber;
							}
							ackRecievedCorrect = true;
							System.out.println(
									"Ack recieved: Sequence Number = "
											+ ackSequenceNumber);
							break; // Break if there
								// is an ack so
								// the next
								// packet can be
								// sent
						} else { // Resend the packet
							System.out.println(
									"Resending: Sequence Number = "
											+ sequenceNumber);
							// Resend the packet
							// following the last
							// acknowledged packet
							// and all following
							// that (cumulative
							// acknowledgement)
							for (int y = 0; y != (sequenceNumber
									- lastAckedSequenceNumber); y++) {
								byte[] resendMessage = new byte[1024];
								resendMessage = sentMessageList
										.get(y + lastAckedSequenceNumber);

								DatagramPacket resendPacket = new DatagramPacket(
										resendMessage,
										resendMessage.length,
										address,
										port);
								socket.send(resendPacket);
								retransmissionCounter += 1;
							}
						}
					}
				} else { // Else pipeline is not full, break so
						// we can send the message
					break;
				}
			}

			// Send the message
			socket.send(sendPacket);
			System.out.println("Sent: Sequence number = "
					+ sequenceNumber + ", Flag = "
					+ lastMessageFlag);

			// Check for acknowledgements
			while (true) {
				boolean ackPacketReceived = false;
				byte[] ack = new byte[2];
				DatagramPacket ackpack = new DatagramPacket(ack,
						ack.length);

				try {
					socket.setSoTimeout(10);
					socket.receive(ackpack);
					ackSequenceNumber = ((ack[0]
							& 0xff) << 8)
							+ (ack[1] & 0xff);
					ackPacketReceived = true;
				} catch (SocketTimeoutException e) {
					// System.out.println("Socket timed out
					// waiting for an ack");
					ackPacketReceived = false;
					// e.printStackTrace();
					break;
				}

				// Note any acknowledgements and move window
				// forward
				if (ackPacketReceived) {
					if (ackSequenceNumber >= (lastAckedSequenceNumber
							+ 1)) {
						lastAckedSequenceNumber = ackSequenceNumber;
						System.out.println(
								"Ack recieved: Sequence number = "
										+ ackSequenceNumber);
					}
				}
			}
		}

		// Continue to check and resend until we receive final ack
		while (!lastAcknowledgedFlag) {

			boolean ackRecievedCorrect = false;
			boolean ackPacketReceived = false;

			while (!ackRecievedCorrect) {
				// Check for an ack
				byte[] ack = new byte[2];
				DatagramPacket ackpack = new DatagramPacket(ack,
						ack.length);

				try {
					socket.setSoTimeout(50);
					socket.receive(ackpack);
					ackSequenceNumber = ((ack[0]
							& 0xff) << 8)
							+ (ack[1] & 0xff);
					ackPacketReceived = true;
				} catch (SocketTimeoutException e) {
					// System.out.println("Socket timed out
					// waiting for an ack1");
					ackPacketReceived = false;
					// e.printStackTrace();
				}

				// If its the last packet
				if (lastMessageFlag) {
					lastAcknowledgedFlag = true;
					break;
				}
				// Break if we receive acknowledgement so that
				// we can send next packet
				if (ackPacketReceived) {
					System.out.println(
							"Ack recieved: Sequence number = "
									+ ackSequenceNumber);
					if (ackSequenceNumber >= (lastAckedSequenceNumber
							+ 1)) {
						lastAckedSequenceNumber = ackSequenceNumber;
					}
					ackRecievedCorrect = true;
					break; // Break if there is an ack so
						// the next packet can be sent
				} else { // Resend the packet
						// Resend the packet following
						// the last acknowledged packet
						// and all following that
						// (cumulative acknowledgement)
					for (int j = 0; j != (sequenceNumber
							- lastAckedSequenceNumber); j++) {
						byte[] resendMessage = new byte[1024];
						resendMessage = sentMessageList
								.get(j + lastAckedSequenceNumber);
						DatagramPacket resendPacket = new DatagramPacket(
								resendMessage,
								resendMessage.length,
								address, port);
						socket.send(resendPacket);
						System.out.println(
								"Resending: Sequence Number = "
										+ lastAckedSequenceNumber);

						// Increment retransmission
						// counter
						retransmissionCounter += 1;
					}
				}
			}
		}

		socket.close();
		System.out.println("File " + fileName + " has been sent");

		// Calculate the average throughput
		int fileSizeKB = (fileByteArray.length) / 1024;
		int transferTime = timer.getTimeElapsed() / 1000;
		double throughput = (double) fileSizeKB / transferTime;
		System.out.println("File size: " + fileSizeKB
				+ "KB, Transfer time: " + transferTime
				+ " seconds. Throughput: " + throughput
				+ "KBps");
		System.out.println("Number of retransmissions: "
				+ retransmissionCounter);
	}
}
