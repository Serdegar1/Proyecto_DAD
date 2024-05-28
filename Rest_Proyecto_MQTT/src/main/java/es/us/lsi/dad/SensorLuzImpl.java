package es.us.lsi.dad;

import java.util.Objects;

public class SensorLuzImpl {
	protected Integer idl;
	protected Double nivel_luz;
	protected Long timestampl;
	protected Integer idP; // id de la placa
	protected Integer idG;	//id del group
	
	public SensorLuzImpl(Integer idl, Double nivel_luz, Long timestampl, Integer idP, Integer idG) {
		super();
		this.idl = idl;
		this.nivel_luz = nivel_luz;
		this.timestampl = timestampl;
		this.idP = idP;
		this.idG = idG;
		
	}
	public SensorLuzImpl() {
		super();
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(idl);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SensorLuzImpl other = (SensorLuzImpl) obj;
		return Objects.equals(idl, other.idl);
	}
	public Integer getIdl() {
		return idl;
	}
	public void setIdl(Integer idl) {
		this.idl = idl;
	}
	public Double getNivel_luz() {
		return nivel_luz;
	}
	public void setNivel_luz(Double nivel_luz) {
		this.nivel_luz = nivel_luz;
	}
	public Long getTimestampl() {
		return timestampl;
	}
	public void setTimestampl(Long timestampl) {
		this.timestampl = timestampl;
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
}
