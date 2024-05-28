package es.us.lsi.dad;

import java.util.Objects;

public class SensorTemperaturaImpl {
	//El sensor tiene temperatura y humedada, pero de momento solo usaré la temperatura
	private Integer idtemp;
	private Double temperatura;
	private Long timestampt;
	private Integer idP; // id de la placa
	private Integer idG;	//id del group
	
	//elem.getInteger("idPlaca"), elem.getInteger("idGroup")
	public SensorTemperaturaImpl(Integer idtemp, Double temperatura, Long timestampt, Integer idP, Integer idG)  {
		super();
		this.idtemp = idtemp;
		this.temperatura = temperatura;
		this.timestampt = timestampt;
		this.idP = idP;
		this.idG = idG;
	}
	
	public Integer getIdP() {
		return idP;
	}

	public void setIdP(Integer idP) {
		this.idP = idP;
	}

	public Integer getIdG() {
		return idG;
	}

	public void setIdG(Integer idG) {
		this.idG = idG;
	}

	public SensorTemperaturaImpl() {
		super();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(idtemp);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SensorTemperaturaImpl other = (SensorTemperaturaImpl) obj;
		return Objects.equals(idtemp, other.idtemp);
	}

	public Integer getIdtemp() {
		return idtemp;
	}

	public void setIdtemp(Integer idtemp) {
		this.idtemp = idtemp;
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
