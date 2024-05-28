package es.us.lsi.dad;

import java.util.Objects;

public class ActLedEntity {
	int idled;
	float nivel_luz;
	public int getId() {
		return idled;
	}
	public void setId(int id) {
		this.idled = id;
	}
	public float getNivel_luz() {
		return nivel_luz;
	}
	public void setNivel_luz(float nivel_luz) {
		this.nivel_luz = nivel_luz;
	}
	@Override
	public int hashCode() {
		return Objects.hash(idled);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActLedEntity other = (ActLedEntity) obj;
		return idled == other.idled;
	}
	public ActLedEntity(int id, float nivel_luz) {
		super();
		this.idled = id;
		this.nivel_luz = nivel_luz;
	}

}
