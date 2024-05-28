package es.us.lsi.dad;

import java.util.Objects;

public class ActVenEntity {
	int idv;
	int onoff;
	public int getIdv() {
		return idv;
	}
	public void setIdv(int idv) {
		this.idv = idv;
	}
	public int getOnoff() {
		return onoff;
	}
	public void setOnoff(int onoff) {
		this.onoff = onoff;
	}
	@Override
	public int hashCode() {
		return Objects.hash(idv);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActVenEntity other = (ActVenEntity) obj;
		return idv == other.idv;
	}
	public ActVenEntity(int idv, int onoff) {
		super();
		this.idv = idv;
		this.onoff = onoff;
	}
	
	
	
}
