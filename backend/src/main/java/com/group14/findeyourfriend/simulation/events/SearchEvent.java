package com.group14.findeyourfriend.simulation.events;

import com.group14.findeyourfriend.bracelet.Person;
import com.group14.findeyourfriend.debug.DebugLog;

public class SearchEvent extends Event {

	int end;
	private String hunterName;
	// private int hunterId;
	private String preyName;
	// private int preyId;

	@Override
	public void process() {
		for (Person h : sim.getGuests()) {
			if (h.getName().equals(hunterName)) {
				for (Person p : sim.getGuests()) {
					if (p.getName().equals(preyName)) {
						h.getBracelet().StartSearch(p);
						DebugLog.log("SearchTimer: " + h.toString() + " searching for " + p.toString());
						break;
					}
				}
			}
		}

	}

	public void setHunterName(String hName) {
		this.hunterName = hName;
	}

	public void setPreyName(String pName) {
		this.preyName = pName;
	}

	@Override
	public int getStart() {
		return super.getStart();
	}

	public String getHunterName() {
		return hunterName;
	}

	public int getEnd() {
		return end;
	}

	public String getPreyName() {
		return preyName;
	}

	@Override
	public String getDescription() {
		StringBuilder builder = new StringBuilder();
		builder.append("Search Event - " + hunterName + " searching " + preyName);
		return builder.toString();
	}
}
