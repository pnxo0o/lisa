package cl.rojasycia.tesisguiamovil.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.geonames.FeatureClass;
import org.geonames.Toponym;
import org.geonames.WebService;
import org.xmlpull.v1.XmlSerializer;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.model.Categoria;
import cl.rojasycia.tesisguiamovil.ui.struct.ExpandableListAdapter;
import cl.rojasycia.tesisguiamovil.utils.GPSTracker;
import cl.rojasycia.tesisguiamovil.utils.NetworkUtil;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

public class Fragment1 extends SherlockFragment  implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener  {
	
	public static final int ERROR_GPS = 1;
	public static final int FALLO_TIME_OUT = 2;
	public static final int ERROR_WIFI = 3;
	public static final int OK = 0;
	public static final int FALLO = 4;
	

	private List<String> listDataHeader;
	private HashMap<String, List<String>> listDataChild;
	private Button btnBuscarAqui;
	private ExpandableListView listaExpandible;
	
	private ProgressDialog mProgressDialog;
	private AsyncLatLong latLong;
	private ExpandableListAdapter listAdapter;
	private Categoria gp;
	private LocationClient mLocationClient;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment1, container, false);
		
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setMessage("Buscando...");
		mProgressDialog.setCancelable(false);
		listaExpandible = (ExpandableListView) rootView.findViewById(R.id.listViewexp);
		prepararDatos();

		listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
		listaExpandible.setAdapter(listAdapter);

		listaExpandible.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				buscarGrupo(groupPosition, childPosition);
				return false;
			}
		});
		
		btnBuscarAqui = (Button) rootView.findViewById(R.id.btnBuscarAqui);
		btnBuscarAqui.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
				{
			    	buscarAqui();
			    } 
			}); 
		mLocationClient = new LocationClient(getActivity(), this, this);
		
		return rootView;
	}
	
	private void prepararDatos() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Universidades");
		listDataHeader.add("Alimentaci�n");
		listDataHeader.add("Alojamiento");
		listDataHeader.add("Entretenci�n");
		listDataHeader.add("Servicios");

		// Adding child data
		List<String> a = new ArrayList<String>();
		a.add("Todas las Universidades");
		a.add("Universidad de Playa Ancha de Ciencias de la Educaci�n");
		a.add("Universidad de Valpara�so");
		a.add("Pontificia Universidad Cat�lica de Valpara�so");
		a.add("Universidad T�cnica Federico Santa Mar�a");


		List<String> b = new ArrayList<String>();
		b.add("Todos los lugares de Alimentaci�n");
		b.add("Cafeter�as");
		b.add("Comida R�pida");
		b.add("Restaurantes");
		b.add("Supermercados");
		
		List<String> c = new ArrayList<String>();
		c.add("Todos los Alojamientos");
		c.add("Residenciales");
		c.add("Hostales");
		c.add("Hoteles");
		
		List<String> d = new ArrayList<String>();
		d.add("Todos los lugares de Entretenci�n");
		d.add("Museos");
		d.add("Cines");
		d.add("Bares");
		d.add("Discos");
		d.add("Espacios Publicos");
		
		List<String> e = new ArrayList<String>();
		e.add("Todos los Servicios");
		e.add("Carabineros");
		e.add("Servicio Medico");
		e.add("Bomberos");
		e.add("Metro");

		listDataChild.put(listDataHeader.get(0), a); // Header, Child data
		listDataChild.put(listDataHeader.get(1), b);
		listDataChild.put(listDataHeader.get(2), c);
		listDataChild.put(listDataHeader.get(3), d);
		listDataChild.put(listDataHeader.get(4), e);
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
			gp = new Categoria(5, 0);
			latLong  = new AsyncLatLong();
			latLong.execute();
		}
    }
	
	public void buscarGrupo(int grupo, int subGrupo){
		if(NetworkUtil.getConnectivityStatus(getActivity()) == 0){
			Toast.makeText(getActivity(), "Revise su conexi�n a internet", Toast.LENGTH_SHORT).show();
		}
		else{
			gp = new Categoria(grupo, subGrupo);
			if(gp.isEncontrado() == true){
				latLong  = new AsyncLatLong();
				latLong.execute();
			}
			else{
				Toast.makeText(getActivity(), "Implementaci�n de funcionalidad en prototipo 2", Toast.LENGTH_LONG).show();
			}

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

//		private Thread aGPS, aWIFI;
		private OutputStreamWriter fout;
		private double latitude;
		private double longitude;
		private GPSTracker ubicacion;
		private XmlSerializer ser;
		
		@Override
        protected void onPreExecute() {
			ubicacion = new GPSTracker(getActivity(), NetworkUtil.getConnectivityStatus(getActivity()));
			Location ubicacionActual = mLocationClient.getLastLocation();
			latitude = ubicacionActual.getLatitude();
	        longitude = ubicacionActual.getLongitude();

			ser = Xml.newSerializer();
			 
			try {
				fout = new OutputStreamWriter(
						getActivity().openFileOutput("poi_descargados.xml",
				        Context.MODE_PRIVATE));
			} catch (FileNotFoundException e1) {
				Log.e("yo","fallo xml");
				e1.printStackTrace();
			} catch (IllegalArgumentException e) {
				Log.e("yo","fallo xml");
				e.printStackTrace();
			} catch (IllegalStateException e) {
				Log.e("yo","fallo xml");
				e.printStackTrace();
			}

			
//			aGPS = new Thread(new Runnable() {
//			    public void run() {
//					try {
//						Thread.sleep(GPSTracker.TIEMPO_GPS+500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					} 
//				}
//
//			 });
//			aWIFI = new Thread(new Runnable() {
//			    public void run() {
//					try {
//						Thread.sleep(GPSTracker.TIEMPO_WIFI+200);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					} 
//				}
//
//			 });
			mProgressDialog.show();
        }
 
        @Override
        protected Integer doInBackground(String... params) {
            // Aqu� hacemos las tareas 
//        	
//	        if(NetworkUtil.getConnectivityStatus(getActivity()) == NetworkUtil.TYPE_WIFI){
//	        	if(!ubicacion.isWifiEnabled()){
//	        		return ERROR_WIFI;
//	        	}
//	        	else if(ubicacion.canGetLocation()){
//	        		aWIFI.run();
//	        		latitude = ubicacion.getLatitude();
//		        	longitude = ubicacion.getLongitude();
//		        	if(latitude == 0.0 && longitude == 0.0) return FALLO_TIME_OUT;
//	        	}
//	        }
//	        else {
//	        	if(!ubicacion.isGPSEnabled()){
//	        		return ERROR_GPS;
//	        	}
//	        	else if(ubicacion.canGetLocation()) {
//	        		aGPS.run();
//		        	latitude = ubicacion.getLatitude();
//			        longitude = ubicacion.getLongitude();
//			        ubicacion.stopUsingGPS();
//			        Log.e("yo",latitude+" - "+longitude);
//			        if(latitude == 0.0 && longitude == 0.0) return FALLO_TIME_OUT;
//	        	}
//	        		
//	        }
//	        
	        
	        Log.e("yo","ubicacion lista");
       	
	        try {
	        	WebService.setUserName("pnxo0o");
				List<Toponym> searchResult = WebService.findNearby(latitude, longitude, 250.0 ,FeatureClass.S ,gp.getGrupo(), "es", 12);
		        
				if(searchResult.size() > 0){
					Log.e("yo","descarga poi lista");
					Iterator<Toponym> iterador = searchResult.listIterator(); 
					
					ser.setOutput(fout);
					ser.startTag("", "pois");

					while( iterador.hasNext() ) {
						
						Toponym b = (Toponym) iterador.next();
						
						ser.startTag("", "poi");					 
						ser.startTag("", "nombre");
						ser.text(b.getName());
						ser.endTag("", "nombre");						 
						ser.startTag("", "tipo");
						ser.text(b.getFeatureCode());
						ser.endTag("", "tipo");						
						ser.startTag("", "latitud");
						ser.text(b.getLatitude()+"");
						ser.endTag("", "latitud");						
						ser.startTag("", "longitud");
						ser.text(b.getLongitude()+"");
						ser.endTag("", "longitud");						 
						ser.endTag("", "poi");		
					}
					
					ser.endTag("", "pois");
					ser.endDocument();
					fout.close();
					
				}
				
			} catch (IOException e) {
				Log.e("yo","IOException");
				return FALLO;
			} catch (Exception e) {
				Log.e("yo","Exception");
				return FALLO;
			}
	        
	        
	        return OK;
    }
        
 
        @Override
        protected void onPostExecute(Integer result) {
        	mProgressDialog.dismiss();
        	if(result == OK){
        		//Toast.makeText(getActivity(), "La ubicaci�n es - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
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
        	else if(result == FALLO){
        		Toast.makeText(getActivity(), "Ocurri� un error obteniendo los lugares", Toast.LENGTH_LONG).show();
        	}
        		
        }
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
	}

	@Override
	public void onConnected(Bundle arg0) {
	}

	@Override
	public void onDisconnected() {
	}
	
    @Override
	public void onStart() {
       super.onStart();
       mLocationClient.connect();
    }
    @Override
	public void onStop() {
       mLocationClient.disconnect();
       super.onStop();
    }
}
