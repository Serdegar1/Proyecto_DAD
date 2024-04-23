package es.us.lsi.dad;

import java.util.Objects;

public class SensorTemperaturaEntity {
	//El sensor tiene temperatura y humedada, pero de momento solo usaré la temperatura
	private Integer idth;
	private Double temperatura;
	private Long timestampt;
	
	public SensorTemperaturaEntity(Integer idth, Double temperatura, Long timestampt) {
		super();
		this.idth = idth;
		this.temperatura = temperatura;
		this.timestampt = timestampt;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idth);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SensorTemperaturaEntity other = (SensorTemperaturaEntity) obj;
		return Objects.equals(idth, other.idth);
	}

	public Integer getIdth() {
		return idth;
	}

	public void setIdth(Integer idth) {
		this.idth = idth;
	}

	public Double getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(Double temperatura) {
		this.temperatura = temperatura;
	}

	public Long getTimestampt() {
		return timestampt;
	}

	public void setTimestampt(Long timestampt) {
		this.timestampt = timestampt;
	}
	
}
