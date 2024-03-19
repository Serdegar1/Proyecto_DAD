package es.us.lsi.dad;

import java.util.Objects;

public class SensorTemperatura {
	//El sensor tiene temperatura y humedada, pero de momonto solo usaré la temperatura de momento
	private Integer idth;
	private Double temperatura;
	private Double humedad;
	private Long timestamp;
	//SensorTemperatura(1, 1.1, 1.2, 1.1)
	public Integer getIdth() {
		return idth;
	}
	
	public SensorTemperatura(Integer idth, Double temperatura, Double humedad, Long timestamp) {
		super();
		this.idth = idth;
		this.temperatura = temperatura;
		this.humedad = humedad;
		this.timestamp = timestamp;
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
		SensorTemperatura other = (SensorTemperatura) obj;
		return Objects.equals(idth, other.idth);
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
	public Double getHumedad() {
		return humedad;
	}
	public void setHumedad(Double humedad) {
		this.humedad = humedad;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	
}


