package com.group14.findeyourfriend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.group14.common_interface.MessageSimulationPayload;
import com.group14.common_interface.PersonDto;

public class Notifier {

	private MessageProducer messageProducer;
	private DtoConverter converter;
	private Session session;

	public Notifier() {
		converter = new DtoConverter();
	}

	public Notifier(String clientId, String queueName) {
		this();
		try {
			createQueue(clientId, queueName);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createQueue(String clientId, String queueName) throws JMSException {
		// create a Connection Factory
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_BROKER_URL);
		connectionFactory.setTrustAllPackages(true);

		// create a Connection
		Connection connection = connectionFactory.createConnection();
		connection.setClientID(clientId);

		// create a Session
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// create the Queue to which messages will be sent
		javax.jms.Queue queue = session.createQueue(queueName);

		// create a MessageProducer for sending messages
		messageProducer = session.createProducer(queue);
	}

	public void notify(Collection<Person> values) {
		if (values == null) {
			return;
		}

		List<PersonDto> dtos = new ArrayList<>();
		values.forEach(v -> {
			dtos.add(converter.convert(v));
		});

		MessageSimulationPayload payload = new MessageSimulationPayload();
		payload.setPeople(dtos);

		try {
			messageProducer.send(session.createObjectMessage(payload));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Notified UI with list of people");
	}
}
