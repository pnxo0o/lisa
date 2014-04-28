package cl.rojasycia.tesisguiamovil.ui;

import com.actionbarsherlock.app.SherlockFragment;

import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.struct.ListViewExpanableAdaptador;
import cl.rojasycia.tesisguiamovil.struct.ListViewExpanableItems;
import cl.rojasycia.tesisguiamovil.utils.GPSTracker;
import cl.rojasycia.tesisguiamovil.utils.NetworkUtil;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
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
	 * Método que crea los datos de la lista expandible
	 */
	public void crearDatos() {
		ListViewExpanableItems grupo0 = new ListViewExpanableItems("Universidades");
		grupo0.children.add("Universidad de Playa Ancha de Ciencias de la Educación");
		grupo0.children.add("Universidad de Valparaíso");
		grupo0.children.add("Universidad Técnica Federico Santa María");
		grupo0.children.add("Pontificia Universidad Católica de Valparaíso");
		grupos.append(0, grupo0);

		ListViewExpanableItems grupo1 = new ListViewExpanableItems("Alimentación");
		grupo1.children.add("Supermercado");
		grupos.append(1, grupo1);

		ListViewExpanableItems grupo2 = new ListViewExpanableItems("Alojamiento");
		grupo2.children.add("Hostal");
		grupos.append(2, grupo2);
		
		ListViewExpanableItems grupo3 = new ListViewExpanableItems("Entretención");
		grupo3.children.add("Bar");
		grupos.append(3, grupo3);
		
		ListViewExpanableItems grupo4 = new ListViewExpanableItems("Servicios");
		grupo4.children.add("Servicio Medico");
		grupos.append(4, grupo4);
	}

	/**
	 * @param view
	 * Método que valida la conexión y ubicación
	 * y busca puntos de interes
	 */
	public void buscarAqui() {
		if(NetworkUtil.getConnectivityStatus(getActivity())==0){
			Toast.makeText(getActivity(), "Revise su conexión a internet", Toast.LENGTH_SHORT).show();
		}
		else{
			AsyncLatLong task = new AsyncLatLong();
	        task.execute();
        	
		}
        
    }
	
	public void lanzarMapa(double latitud, double longitud){
		Intent intent;
		intent = new Intent(getActivity(), MapPOIActivity.class);
		intent.putExtra("latitud", latitud);
		intent.putExtra("longitud", longitud);
		startActivity(intent);
	}
	
	private class AsyncLatLong extends AsyncTask<String, Void, Integer>{
		
		Thread a;
		
		@Override
        protected void onPreExecute() {
            // Avísele al usuario que estamos trabajando
			ubicacion = new GPSTracker(getActivity(), NetworkUtil.getConnectivityStatus(getActivity()));
			
			a = new Thread(new Runnable() {
			    public void run() {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
			    	
			 });
        }
 
        @Override
        protected Integer doInBackground(String... params) {
            // Aquí hacemos las tareas 
        	
	        if(ubicacion.canGetLocation()){
	        	if(NetworkUtil.getConnectivityStatus(getActivity())==NetworkUtil.TYPE_WIFI){
	        		latitude = ubicacion.getLatitude();
		        	longitude = ubicacion.getLongitude();
		        	if(latitude == 0.0 && longitude == 0.0) return 1;
	        	}
	        	else{
	        		a.run();
	        		latitude = ubicacion.getLatitude();
		        	longitude = ubicacion.getLongitude();
		        	ubicacion.stopUsingGPS();
		        	//if(latitude == 0.0 && longitude == 0.0) return 1;
	        	}
	        	return 0;
	        }
	        else{
	        	return 1;
	        }
        }
 
        @Override
        protected void onPostExecute(Integer result) {
            // Aquí actualizamos la UI con el resultado
        	if(result==0){
        		Toast.makeText(getActivity(), "La ubicación es - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        		lanzarMapa(latitude, longitude);
        	}
        	else{
        		ubicacion.showSettingsAlert(getActivity());
        	}
        }
	}
	
	
}
