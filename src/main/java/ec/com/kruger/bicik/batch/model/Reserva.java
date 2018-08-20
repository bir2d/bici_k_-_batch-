package ec.com.kruger.bicik.batch.model;

import java.util.Date;


public class Reserva {	
	
	
	private Long id;
	
	private Date createAt;
	
	private String empleado;

	
	private Long empleadoId;
	
	
	private String bicicleta;

	
	private Long bicicletaId;
	
	private int tiempoUso;
	
	private String estado;
	
	public Date getFinUso() {
		return finUso;
	}

	public void setFinUso(Date finUso) {
		this.finUso = finUso;
	}

	private String claveCandado;	
	
	
	private Date inicioUso;
	
	
	private Date finUso;
	
	private int numeroAplazado;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	

	public Long getEmpleadoId() {
		return empleadoId;
	}

	public void setEmpleadoId(Long empleadoId) {
		this.empleadoId = empleadoId;
	}

	

	public String getEmpleado() {
		return empleado;
	}

	public void setEmpleado(String empleado) {
		this.empleado = empleado;
	}

	public String getBicicleta() {
		return bicicleta;
	}

	public void setBicicleta(String bicicleta) {
		this.bicicleta = bicicleta;
	}

	public Long getBicicletaId() {
		return bicicletaId;
	}

	public void setBicicletaId(Long bicicletaId) {
		this.bicicletaId = bicicletaId;
	}

	public int getTiempoUso() {
		return tiempoUso;
	}

	public void setTiempoUso(int tiempoUso) {
		this.tiempoUso = tiempoUso;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getClaveCandado() {
		return claveCandado;
	}

	public void setClaveCandado(String claveCandado) {
		this.claveCandado = claveCandado;
	}

	public Date getInicioUso() {
		return inicioUso;
	}

	public void setInicioUso(Date inicioUso) {
		this.inicioUso = inicioUso;
	}

	public int getNumeroAplazado() {
		return numeroAplazado;
	}

	public void setNumeroAplazado(int numeroAplazado) {
		this.numeroAplazado = numeroAplazado;
	}
	
	@Override
	public String toString() {
		return "Reserva [id=" + id + ", createAt=" + createAt + ", estado=" + estado + "]";
	}


}
