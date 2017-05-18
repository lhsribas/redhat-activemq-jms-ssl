package com.redhat.pilot.jms.consumer;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQSslConnectionFactory;

public class SSLJMSConsumer {

	public static void main(final String[] args) {

		Connection connection = null;
		MessageConsumer consumer = null;
		Session session = null;

		try {

			String url = "ssl://broker-amq-tcp-ssl-redhat-test.151f.gsat-corp.openshiftapps.com:443";

			ActiveMQSslConnectionFactory connectionFactory = new ActiveMQSslConnectionFactory(url);

			connectionFactory.setUserName("userOlF");
			connectionFactory.setPassword("abDC5KlR");

			connectionFactory.setTrustStore("keys/client.ts");
			connectionFactory.setTrustStorePassword("redhat@123");

			connectionFactory.setKeyStore("keys/client.ks");
			connectionFactory.setKeyStorePassword("redhat@123");

			connection = connectionFactory.createConnection();
			connection.start();

			Queue queue = new Queue() {
				public String getQueueName() throws JMSException {
					return "test";
				}
			};

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			consumer = session.createConsumer(queue);

			Message message = consumer.receive(1000);

			if (message instanceof TextMessage) {

				TextMessage textMessage = (TextMessage) message;
				String text = textMessage.getText();
				System.out.println("Received: " + text);

			} else {

				System.out.println("Received: " + message);
			}

		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		} finally {
			try {

				consumer.close();
				session.close();
				connection.close();

			} catch (JMSException e) {
				e.printStackTrace();
			}

		}

	}
}
