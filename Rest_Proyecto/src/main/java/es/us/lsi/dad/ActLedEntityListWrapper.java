package es.us.lsi.dad;

import java.util.List;
import java.util.Objects;

public class ActLedEntityListWrapper {
	private List<ActLedEntity> ledList;

	public ActLedEntityListWrapper() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ActLedEntityListWrapper(List<ActLedEntity> ledList) {
		super();
		this.ledList = ledList;
	}

	public List<ActLedEntity> getLedList() {
		return ledList;
	}

	public void setLedList(List<ActLedEntity> ledList) {
		this.ledList = ledList;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ledList);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActLedEntityListWrapper other = (ActLedEntityListWrapper) obj;
		return Objects.equals(ledList, other.ledList);
	}
}
