package cl.rojasycia.tesisguiamovil.model;

/*
 * Clasificacion
 * 
 * Universidades:
 * 0-0 Todas las ues x
 * 0-1 UPLA
 * 0-2 UV
 * 0-3 PUCV
 * 0-4 UTFSM
 * 
 * Alimentación:
 * 1-0 Todo alimento
 * 1-1 Cafeterías
 * 1-2 Comida Rápida
 * 1-3 Restaurant
 * 1-4 Supermercado
 * 
 * Alojamiento:
 * 2-0 Todo alojamineto
 * 2-1 residencial
 * 2-2 hostal
 * 2-3 hotel
 * 
 * Entretencion
 * 3-0 Todo entretencion
 * 3-1 museo
 * 3-2 cine
 * 3-3 bar
 * 3-4 disco
 * 3-5 demas
 * 
 * Servicios
 * 4-0 todo serv
 * 4-1 Carabineros
 * 4-2 Servicio Medico
 * 4-3 Bomberos
 * 4-4 Metro
 */

public class Categoria {
	
	public static final int UNIVERSIDAD = 0;
	public static final int ALIMENTACION = 1;
	public static final int ALOJAMIENTO = 2;
	public static final int SERVICIOS = 4;
	public static final int CERCA = 5;
	
	private String[] grupo;
	private boolean encontrado = false;
	
	public Categoria(int grupoSeleccionado, int tipoSeleccionado){
		if (grupoSeleccionado == UNIVERSIDAD){
			if(tipoSeleccionado == 0){
				this.grupo = new String[]{"UNIV"};//todo univ
				this.encontrado = true;
			}
		}
		else if(grupoSeleccionado == ALOJAMIENTO){
			if(tipoSeleccionado == 0){
				this.grupo = new String[]{"RHSE", "GHSE", "HTL"};//todo
				this.encontrado = true;
			}
			else if(tipoSeleccionado == 1){
				this.grupo = new String[]{"RHSE"};//residenciales
				this.encontrado = true;
			}
			else if(tipoSeleccionado == 2){
				this.grupo = new String[]{"GHSE"};//hostales
				this.encontrado = true;
			}
			else if(tipoSeleccionado == 3){
				this.grupo = new String[]{"HTL"};//hoteles 
				this.encontrado = true;
			}
		}
		else if(grupoSeleccionado == SERVICIOS){
			if(tipoSeleccionado == 0){
				this.grupo = new String[]{"PP", "HSP"};//todos
				this.encontrado = true;
			}
			else if(tipoSeleccionado == 1){
				this.grupo = new String[]{"PP"};//policia
				this.encontrado = true;
			}
			else if(tipoSeleccionado == 2){
				this.grupo = new String[]{"HSP"};//MEDICO
				this.encontrado = true;
			}
		}
		else if(grupoSeleccionado == CERCA){
			this.grupo = new String[]{"UNIV", "RHSE", "GHSE", "HTL", "PP", "HSP", "RESTO"};
		}
	}
	
	

	public String[] getGrupo() {
		return grupo;
	}

	public void setGrupo(String[] grupo) {
		this.grupo = grupo;
	}



	public boolean isEncontrado() {
		return encontrado;
	}


	public void setEncontrado(boolean encontrado) {
		this.encontrado = encontrado;
	}

}
