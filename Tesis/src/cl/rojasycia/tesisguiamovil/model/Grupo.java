package cl.rojasycia.tesisguiamovil.model;

public class Grupo {
	
	public static final int UNIVERSIDAD = 0;
	public static final int ALOJAMIENTO = 2;
	
	String[] grupo;
	
	public Grupo(int grupoSeleccionado, int tipoSeleccionado){
		if(grupoSeleccionado == UNIVERSIDAD && tipoSeleccionado == 0 ){
			this.grupo = new String[]{"UNIV"};
		}
		else if(grupoSeleccionado == ALOJAMIENTO && tipoSeleccionado == 0 ){
			this.grupo = new String[]{"RHSE", "HTL", "GHSE"};  //residencial, hotel, hostal
		}
		
	}

	public String[] getGrupo() {
		return grupo;
	}

	public void setGrupo(String[] grupo) {
		this.grupo = grupo;
	}

}
