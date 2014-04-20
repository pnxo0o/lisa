package cl.rojasycia.tesisguiamovil.ui;

import com.actionbarsherlock.app.SherlockFragment;

import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.struct.ListViewExpanableAdaptador;
import cl.rojasycia.tesisguiamovil.struct.ListViewExpanableItems;
import cl.rojasycia.tesisguiamovil.utils.GPSTracker;
import cl.rojasycia.tesisguiamovil.utils.NetworkUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class Fragment1 extends SherlockFragment {
	
	SparseArray<ListViewExpanableItems> grupos = new SparseArray<ListViewExpanableItems>();
	Button btnBuscarAqui;
	GPSTracker ubicacion;
	double latitude;
	double longitude;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment1, container, false);
		
		crearDatos();
		ExpandableListView listaExpandible = (ExpandableListView) rootView.findViewById(R.id.listViewexp);
		ListViewExpanableAdaptador adapter = new ListViewExpanableAdaptador(getActivity(), grupos);
		listaExpandible.setAdapter(adapter);
		
		btnBuscarAqui = (Button) rootView.findViewById(R.id.btnBuscarAqui);
		btnBuscarAqui.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
				{
			    	buscarAqui();
			    } 
			}); 
		
		return rootView;
	}
	
	
	/**
	 * M�todo que crea los datos de la lista expandible
	 */
	public void crearDatos() {
		ListViewExpanableItems grupo0 = new ListViewExpanableItems("Universidades");
		grupo0.children.add("Universidad de Playa Ancha de Ciencias de la Educaci�n");
		grupo0.children.add("Universidad de Valpara�so");
		grupo0.children.add("Universidad T�cnica Federico Santa Mar�a");
		grupo0.children.add("Pontificia Universidad Cat�lica de Valpara�so");
		grupos.append(0, grupo0);

		ListViewExpanableItems grupo1 = new ListViewExpanableItems("Alimentaci�n");
		grupo1.children.add("Supermercado");
		grupos.append(1, grupo1);

		ListViewExpanableItems grupo2 = new ListViewExpanableItems("Alojamiento");
		grupo2.children.add("Hostal");
		grupos.append(2, grupo2);
		
		ListViewExpanableItems grupo3 = new ListViewExpanableItems("Entretenci�n");
		grupo3.children.add("Bar");
		grupos.append(3, grupo3);
		
		ListViewExpanableItems grupo4 = new ListViewExpanableItems("Servicios");
		grupo4.children.add("Servicio Medico");
		grupos.append(4, grupo4);
	}

	/**
	 * @param view
	 * M�todo que valida la conexi�n y ubicaci�n
	 * y busca puntos de interes
	 */
	public void buscarAqui() {
		if(NetworkUtil.getConnectivityStatus(getActivity())==0){
			Toast.makeText(getActivity(), "Revise su conexi�n a internet", Toast.LENGTH_SHORT).show();
		}
		else{
			AsyncLatLong task = new AsyncLatLong();
	        task.execute();
        	
		}
        
    }
	
	private class AsyncLatLong extends AsyncTask<String, Void, Integer>{
		
		double latitude;
		double longitude;
		
		@Override
        protected void onPreExecute() {
            // Av�sele al usuario que estamos trabajando
			ubicacion = new GPSTracker(getActivity().getApplication());
        }
 
        @Override
        protected Integer doInBackground(String... params) {
            // Aqu� hacemos una tarea laaarga
	
	        if(ubicacion.canGetLocation()){
	        	latitude = ubicacion.getLatitude();
	        	longitude = ubicacion.getLongitude();
	        	return 0;
	        }
	        else{
	        	return 1;
	        }
        }
 
        @Override
        protected void onPostExecute(Integer result) {
            // Aqu� actualizamos la UI con el resultado
        	if(result==0){
        		Toast.makeText(getActivity(), "La ubicaci�n es - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        	}
        	else{
        		//error
        		ubicacion.showSettingsAlert();
        	}
        }
	}
}
