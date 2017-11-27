package com.group14.findeyourfriend;

import java.util.List;

public class ArrivalEvent extends Event {

	private List<Person> peopleComing;

	@Override
	void process() {
		sim.newGuestsArrived(peopleComing);
	}

	public void setPeopleComing(List<Person> peopleComing) {
		this.peopleComing = peopleComing;
	}

	public List<Person> getPeopleComing() {
		return peopleComing;
	}
}
