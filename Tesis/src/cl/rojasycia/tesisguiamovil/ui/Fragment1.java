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
	 * Método que crea los datos de la lista expandible
	 */
	public void crearDatos() {
		ListViewExpanableItems grupo0 = new ListViewExpanableItems("Universidades");
		grupo0.children.add("Universidades");
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
	 * Método que valida la conexión y ubicación
	 * y busca puntos de interes
	 */
	public void buscarAqui() {
		if(NetworkUtil.getConnectivityStatus(getActivity())==0){
			Toast.makeText(getActivity(), "Revise su conexión a internet", Toast.LENGTH_SHORT).show();
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
            // Aquí hacemos las tareas 
        	
	        if(NetworkUtil.getConnectivityStatus(getActivity()) == NetworkUtil.TYPE_WIFI){
	        	if(!ubicacion.isWifiEnabled()){
	        		return 3;
	        	}
	        	else if(ubicacion.canGetLocation()){
	        		aWIFI.run();
	        		latitude = ubicacion.getLatitude();
		        	longitude = ubicacion.getLongitude();
		        	if(latitude == 0.0 && longitude == 0.0) return 2;
	        	}
	        else if(NetworkUtil.getConnectivityStatus(getActivity()) == NetworkUtil.TYPE_MOBILE){
	        	if(!ubicacion.isGPSEnabled()){
	        		return 1;
	        	}
	        	else if(ubicacion.canGetLocation()) {
	        		aGPS.run();
		        	latitude = ubicacion.getLatitude();
			        longitude = ubicacion.getLongitude();
			        ubicacion.stopUsingGPS();
			        if(latitude == 0.0 && longitude == 0.0) return 2;
	        	}
	        		
	        }
	        
	        Log.e("yo","tenemos la ubicacion");
	        	
	        try {
	        	WebService.setUserName("pnxo0o");
				List<Toponym> searchResult = WebService.findNearby(latitude, longitude, 250.0 ,FeatureClass.S ,gp.getGrupo(), "es", 12);
				if(searchResult.size()>0){
					Log.e("yo","ya baje las weas");
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
	        	return 0;
	        }
			return 2;
	        
        }
        
 
        @Override
        protected void onPostExecute(Integer result) {
            // Aquí actualizamos la UI con el resultado
        	mProgressDialog.dismiss();
        	if(result == 0){
        		Toast.makeText(getActivity(), "La ubicación es - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        		lanzarMapa(latitude, longitude);
        	}
        	else if(result == 1){
        		ubicacion.showSettingsAlert(getActivity(), "GPS no activado", "GPS no está activado. Desea activarlo?");
        	}
        	else if(result == 2){
        		Toast.makeText(getActivity(), "Hubo un error desconocido al buscar la ubicación", Toast.LENGTH_LONG).show();
        	}else{
        		ubicacion.showSettingsAlert(getActivity(), "Localización por red no activada", "La localización por red no está activada. Desea activarla?");
        	}
        		
        }
	}

}
