package es.us.lsi.dad;

import java.util.Objects;

public class SensorLuz {

	private Integer idLuz;
	private Integer valorluz;
	private Long timestamp;
	//()
	public Integer getIdLuz() {
		return idLuz;
	}
	public void setIdLuz(Integer idLuz) {
		idLuz = idLuz;
	}
	public Integer getValorluz() {
		return valorluz;
	}
	public void setValorluz(Integer valorluz) {
		valorluz = valorluz;
	}
	public Long getTimestampp() {
		return timestamp;
	}
	public void setTimestampp(Long timestampp) {
		this.timestamp = timestampp;
	}
	@Override
	public int hashCode() {
		return Objects.hash(idLuz);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SensorLuz other = (SensorLuz) obj;
		return Objects.equals(idLuz, other.idLuz);
	}
	
	public SensorLuz(Integer idLuz, Integer valorluz, Long timestampp) {
		super();
		idLuz = idLuz;
		valorluz = valorluz;
		this.timestamp = timestamp;
	}
	
}
	
