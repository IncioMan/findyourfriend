package com.group14.frontend;

import java.io.Serializable;
import java.util.Random;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.servlet.annotation.WebServlet;

import com.group14.common_interface.IConstants;
import com.group14.common_interface.MessageSimulationPayload;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
@Push
public class MyUI extends UI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int GRID_WIDTH = 120;
	private static final int GRID_HEIGHT = 60;
	private GridLayout gridLayout;
	private PersonDetailLayout detailLayout;

	@Override
	protected void init(VaadinRequest request) {
		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		setContent(content);

		content.addComponent(buildFirstLayer());
		content.setSizeFull();

		UINotifier.connect("consumer", IConstants.QUEUE_NAME, this::update);
	}

	private Component buildFirstLayer() {
		HorizontalLayout layout = new HorizontalLayout();
		// Instantiate the component and add it to your UI
		layout.addComponent(gridLayout = new GridLayout(GRID_WIDTH, GRID_HEIGHT));
		gridLayout.addStyleName(ICustomStyles.BORDER);
		gridLayout.addStyleName(ICustomStyles.FIELD);
		gridLayout.setWidth(GRID_WIDTH * 10 + "px");
		gridLayout.setHeight(GRID_HEIGHT * 10 + "px");
		// layout.addStyleName(ValoTheme.BORDE);

		layout.addComponent(detailLayout = new PersonDetailLayout());

		layout.setExpandRatio(gridLayout, 3.0f);
		layout.setExpandRatio(detailLayout, 1.0f);

		layout.setSizeFull();
		return layout;
	}

	private void personClicked(LayoutClickEvent event) {
		if (event.getComponent() instanceof Person) {
			detailLayout.updateDetails((Person) event.getComponent());
		}
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	}

	public void update(Message message) {
		try {
			if (!(message instanceof ObjectMessage)) {
				return;
			}
			Serializable payload = ((ObjectMessage) message).getObject();
			if (payload instanceof MessageSimulationPayload) {
				updateMap((MessageSimulationPayload) payload);
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateMap(MessageSimulationPayload payload) {
		gridLayout.removeAllComponents();
		UI.getCurrent().access(() -> {
			payload.getPeople().forEach(p -> {
				Person person = new Person();
				// person.setPersonId(i + "");
				person.setSearching(new Random().nextBoolean());
				gridLayout.addComponent(person, new Float(p.getPosition().getCoordinates().x + "").intValue(),
						new Float(p.getPosition().getCoordinates().y).intValue());
				person.addLayoutClickListener(this::personClicked);
			});
		});
	}
}
