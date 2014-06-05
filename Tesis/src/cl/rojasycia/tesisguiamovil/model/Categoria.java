package cl.rojasycia.tesisguiamovil.model;

public class Categoria {
	
	public static final int UNIVERSIDAD = 0;
	public static final int ALOJAMIENTO = 2;
	public static final int SERVICIOS = 4;
	public static final int CERCA = 5;
	
	String[] grupo;
	
	public Categoria(int grupoSeleccionado, int tipoSeleccionado){
		if (grupoSeleccionado == UNIVERSIDAD){
			this.grupo = new String[]{"UNIV"};
		}
		else if(grupoSeleccionado == 1){
			this.grupo = new String[]{"UNIV"};
		}
		else if(grupoSeleccionado == ALOJAMIENTO){
			if(tipoSeleccionado == 0){
				this.grupo = new String[]{"RHSE", "GHSE", "HTL"};//todos los alojamientos
			}
			else if(tipoSeleccionado == 1){
				this.grupo = new String[]{"RHSE"};//residenciales
			}
			else if(tipoSeleccionado == 2){
				this.grupo = new String[]{"GHSE"};//hostales
			}
			else if(tipoSeleccionado == 3){
				this.grupo = new String[]{"HTL"};//hoteles HTL
			}
		}
		else if(grupoSeleccionado == SERVICIOS){
			if(tipoSeleccionado == 0){
				this.grupo = new String[]{"RHSE", "GHSE", "HTL"};//todos los alojamientos
			}
		}
		else if(grupoSeleccionado == 5){
			this.grupo = new String[]{"UNIV", "RHSE", "GHSE"};
		}
	}
	
	

	public String[] getGrupo() {
		return grupo;
	}

	public void setGrupo(String[] grupo) {
		this.grupo = grupo;
	}

}
