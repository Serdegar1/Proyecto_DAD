package es.us.lsi.dad;

import java.util.Objects;

public class SensorLuz {

	private Integer idLuz;
	private Integer valorluz;
	private Long timestampl;
	//()
	
	@Override
	public int hashCode() {
		return Objects.hash(idLuz);
	}
	public Integer getIdLuz() {
		return idLuz;
	}
	public void setIdLuz(Integer idLuz) {
		this.idLuz = idLuz;
	}
	public Integer getValorluz() {
		return valorluz;
	}
	public void setValorluz(Integer valorluz) {
		this.valorluz = valorluz;
	}
	public Long getTimestampl() {
		return timestampl;
	}
	public void setTimestampl(Long timestampl) {
		this.timestampl = timestampl;
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
	
	public SensorLuz(Integer idLuz, Integer valorluz, Long timestampl) {
		super();
		this.idLuz = idLuz;
		this.valorluz = valorluz;
		this.timestampl = timestampl;
	}
	
}
	
