package cl.rojasycia.tesisguiamovil.ui.struct;


public class LUniversidadItem {
	
	private String nombre;
	private int drawableImageID;

	public LUniversidadItem(String nombre, int drawableImageID) {
		this.nombre = nombre;
		this.drawableImageID = drawableImageID;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getDrawableImageID() {
		return drawableImageID;
	}

	public void setDrawableImageID(int drawableImageID) {
		this.drawableImageID = drawableImageID;
	}

}
