package cl.rojasycia.tesisguiamovil.model;

public class PuntoDeInteres {
	
	private String nombrePOI;
    private String tipoPOI;
	private double latitudPOI;
	private double longitudPOI;

	public PuntoDeInteres(String nombrePOI, String tipoPOI, double latitudPOI, double longitudPOI) {
		super();
		this.nombrePOI = nombrePOI;
		this.tipoPOI = tipoPOI;
		this.latitudPOI = latitudPOI;
		this.longitudPOI = longitudPOI;
	}

	public String getNombrePOI() {
		return nombrePOI;
	}

	public void setNombrePOI(String nombrePOI) {
		this.nombrePOI = nombrePOI;
	}

	public String getTipoPOI() {
		return tipoPOI;
	}

	public void setTipoPOI(String tipoPOI) {
		this.tipoPOI = tipoPOI;
	}

	public double getLatitudPOI() {
		return latitudPOI;
	}

	public void setLatitudPOI(double latitudPOI) {
		this.latitudPOI = latitudPOI;
	}

	public double getLongitudPOI() {
		return longitudPOI;
	}

	public void setLongitudPOI(double longitudPOI) {
		this.longitudPOI = longitudPOI;
	}

}
