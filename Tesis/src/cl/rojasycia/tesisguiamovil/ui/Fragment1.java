package cl.rojasycia.tesisguiamovil.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import org.geonames.FeatureClass;
import org.geonames.Toponym;
import org.geonames.WebService;

import com.actionbarsherlock.app.SherlockFragment;

import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.model.Grupo;
import cl.rojasycia.tesisguiamovil.struct.ListViewExpanableAdaptador;
import cl.rojasycia.tesisguiamovil.struct.ListViewExpanableItems;
import cl.rojasycia.tesisguiamovil.utils.GPSTracker;
import cl.rojasycia.tesisguiamovil.utils.NetworkUtil;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class Fragment1 extends SherlockFragment   {
	
	private final int ERROR_GPS = 1;
	private final int FALLO_TIME_OUT = 2;
	private final int ERROR_WIFI = 3;
	private final int OK = 0;
	
	private SparseArray<ListViewExpanableItems> grupos = new SparseArray<ListViewExpanableItems>();
	private Button btnBuscarAqui;
	
	ProgressDialog mProgressDialog;
	private AsyncLatLong latLong;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment1, container, false);
		
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setMessage("Buscando...");
		mProgressDialog.setCancelable(false);
		
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
		grupo0.children.add("Todas las Universidades");
		grupo0.children.add("Universidad de Playa Ancha de Ciencias de la Educaci�n");
		grupo0.children.add("Universidad de Valpara�so");
		grupo0.children.add("Universidad T�cnica Federico Santa Mar�a");
		grupo0.children.add("Pontificia Universidad Cat�lica de Valpara�so");
		grupos.append(0, grupo0);

		ListViewExpanableItems grupo1 = new ListViewExpanableItems("Alimentaci�n");
		grupo1.children.add("Todos los lugares de Alimentaci�n");
		grupo1.children.add("Supermercados");
		grupo1.children.add("Cafeter�as");
		grupo1.children.add("Comida R�pida");
		grupos.append(1, grupo1);

		ListViewExpanableItems grupo2 = new ListViewExpanableItems("Alojamiento");
		grupo2.children.add("Todos los Alojamientos");
		grupo2.children.add("Residenciales");
		grupo2.children.add("Casas para alojar");
		grupo2.children.add("Habitaciones");
		grupo2.children.add("Hoteles");
		grupos.append(2, grupo2);
		
		ListViewExpanableItems grupo3 = new ListViewExpanableItems("Entretenci�n");
		grupo3.children.add("Todos los lugares de Entretenci�n");
		grupo3.children.add("Espacios Publicos");
		grupo3.children.add("Bares");
		grupo3.children.add("Discos");
		grupos.append(3, grupo3);
		
		ListViewExpanableItems grupo4 = new ListViewExpanableItems("Servicios");
		grupo4.children.add("Todos los Servicios");
		grupo4.children.add("Servicio Medico");
		grupo4.children.add("Carabineros");
		grupo4.children.add("Bomberos");
		grupos.append(4, grupo4);
	}

	/**
	 * M�todo que valida la conexi�n y ubicaci�n
	 * y busca puntos de interes
	 */
	public void buscarAqui() {
		if(NetworkUtil.getConnectivityStatus(getActivity()) == 0){
			Toast.makeText(getActivity(), "Revise su conexi�n a internet", Toast.LENGTH_SHORT).show();
		}
		else{
			latLong  = new AsyncLatLong();
			latLong.execute();
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

		private Thread aGPS, aWIFI;
		private OutputStreamWriter fout;
		private Grupo gp;
		private StringBuilder sb;
		private double latitude;
		private double longitude;
		private GPSTracker ubicacion;
		
		@Override
        protected void onPreExecute() {
			mProgressDialog.show();
			ubicacion = new GPSTracker(getActivity(), NetworkUtil.getConnectivityStatus(getActivity()));
			gp = new Grupo(0, 0);
			try {
				fout = new OutputStreamWriter(getActivity().openFileOutput("poi_descargados.xml", Context.MODE_PRIVATE));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				Log.e("yo","cagamos escribiendo el xml culiao");
			}
			sb = new StringBuilder();
			aGPS = new Thread(new Runnable() {
			    public void run() {
					try {
						Thread.sleep(GPSTracker.TIEMPO_GPS+100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}

			 });
			aWIFI = new Thread(new Runnable() {
			    public void run() {
					try {
						Thread.sleep(GPSTracker.TIEMPO_WIFI+100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}

			 });
        }
 
        @Override
        protected Integer doInBackground(String... params) {
            // Aqu� hacemos las tareas 
        	
	        if(NetworkUtil.getConnectivityStatus(getActivity()) == NetworkUtil.TYPE_WIFI){
	        	if(!ubicacion.isWifiEnabled()){
	        		return ERROR_WIFI;
	        	}
	        	else if(ubicacion.canGetLocation()){
	        		aWIFI.run();
	        		latitude = ubicacion.getLatitude();
		        	longitude = ubicacion.getLongitude();
		        	if(latitude == 0.0 && longitude == 0.0) return FALLO_TIME_OUT;
	        	}
	        }
	        else {
	        	if(!ubicacion.isGPSEnabled()){
	        		return ERROR_GPS;
	        	}
	        	else if(ubicacion.canGetLocation()) {
	        		aGPS.run();
		        	latitude = ubicacion.getLatitude();
			        longitude = ubicacion.getLongitude();
			        ubicacion.stopUsingGPS();
			        if(latitude == 0.0 && longitude == 0.0) return FALLO_TIME_OUT;
	        	}
	        		
	        }
	        
	        Log.e("yo","ubicacion lista");
	        	
	        try {
	        	WebService.setUserName("pnxo0o");
				List<Toponym> searchResult = WebService.findNearby(latitude, longitude, 250.0 ,FeatureClass.S ,gp.getGrupo(), "es", 12);
				if(searchResult.size() > 0){
					Log.e("yo","descarga poi lista");
					Iterator<Toponym> iterador = searchResult.listIterator(); 
					sb.append("<pois>");
					while( iterador.hasNext() ) {
						Toponym b = (Toponym) iterador.next();
						sb.append("<poi>");
						sb.append("<nombre>" + b.getName() + "</nombre>");
						sb.append("<tipo>" + b.getFeatureCode() + "</tipo>");
						sb.append("<latitud>" + b.getLatitude() + "</latitud>");
						sb.append("<longitud>" + b.getLongitude() + "</longitud>");
						sb.append("</poi>");			  
					}
					sb.append("</pois>");
					fout.write(sb.toString());
					fout.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
	        return OK;
	        
    }
        
 
        @Override
        protected void onPostExecute(Integer result) {
        	mProgressDialog.dismiss();
        	if(result == OK){
        		Toast.makeText(getActivity(), "La ubicaci�n es - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        		lanzarMapa(latitude, longitude);
        	}
        	else if(result == ERROR_GPS){
        		ubicacion.showSettingsAlert(getActivity(), "GPS no activado", "GPS no est� activado. Desea activarlo?");
        	}
        	else if(result == FALLO_TIME_OUT){
        		Toast.makeText(getActivity(), "Ups, tuvimos un error detectando su ubicaci�n, intente nuevamente", Toast.LENGTH_LONG).show();
        	}
        	else if(result == ERROR_WIFI){
        		ubicacion.showSettingsAlert(getActivity(), "Localizaci�n por red no activada", "La localizaci�n por red no est� activada. Desea activarla?");
        	}
        		
        }
        
        
	}

}
