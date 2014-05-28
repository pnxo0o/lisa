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

import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.model.Categoria;
import cl.rojasycia.tesisguiamovil.struct.ExpandableListAdapter;
import cl.rojasycia.tesisguiamovil.utils.GPSTracker;
import cl.rojasycia.tesisguiamovil.utils.NetworkUtil;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class Fragment1 extends SherlockFragment   {
	
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
		
		return rootView;
		
	}
	
	private void prepararDatos() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Universidades");
		listDataHeader.add("Alimentación");
		listDataHeader.add("Alojamiento");
		listDataHeader.add("Entretención");
		listDataHeader.add("Servicios");

		// Adding child data
		List<String> a = new ArrayList<String>();
		a.add("Todas las Universidades");
		a.add("Universidad de Playa Ancha de Ciencias de la Educación");
		a.add("Universidad de Valparaíso");
		a.add("Universidad Técnica Federico Santa María");
		a.add("Pontificia Universidad Católica de Valparaíso");

		List<String> b = new ArrayList<String>();
		b.add("Todos los lugares de Alimentación");
		b.add("Supermercados");
		b.add("Cafeterías");
		b.add("Comida Rápida");

		List<String> c = new ArrayList<String>();
		c.add("Todos los Alojamientos");
		c.add("Residenciales");
		c.add("Hostales");
		c.add("Hoteles");
		
		List<String> d = new ArrayList<String>();
		d.add("Todos los lugares de Entretención");
		d.add("Espacios Publicos");
		d.add("Bares");
		d.add("Discos");
		
		List<String> e = new ArrayList<String>();
		e.add("Todos los Servicios");
		e.add("Servicio Medico");
		e.add("Carabineros");
		e.add("Bomberos");

		listDataChild.put(listDataHeader.get(0), a); // Header, Child data
		listDataChild.put(listDataHeader.get(1), b);
		listDataChild.put(listDataHeader.get(2), c);
		listDataChild.put(listDataHeader.get(3), d);
		listDataChild.put(listDataHeader.get(4), e);
	}

	/**
	 * Método que valida la conexión y ubicación
	 * y busca puntos de interes
	 */
	public void buscarAqui() {
		if(NetworkUtil.getConnectivityStatus(getActivity()) == 0){
			Toast.makeText(getActivity(), "Revise su conexión a internet", Toast.LENGTH_SHORT).show();
		}
		else{
			gp = new Categoria(5, 0);
			latLong  = new AsyncLatLong();
			latLong.execute();
		}
    }
	
	public void buscarGrupo(int grupo, int subGrupo){
		if(NetworkUtil.getConnectivityStatus(getActivity()) == 0){
			Toast.makeText(getActivity(), "Revise su conexión a internet", Toast.LENGTH_SHORT).show();
		}
		else{
			gp = new Categoria(grupo, subGrupo);
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
//		private StringBuilder sb;
		private double latitude;
		private double longitude;
		private GPSTracker ubicacion;
		private XmlSerializer ser;
		
		@Override
        protected void onPreExecute() {
			ubicacion = new GPSTracker(getActivity(), NetworkUtil.getConnectivityStatus(getActivity()));
			
			ser = Xml.newSerializer();
			 
			try {
				fout = new OutputStreamWriter(
						getActivity().openFileOutput("poi_descargados.xml",
				        Context.MODE_PRIVATE));
				ser.setOutput(fout);
			} catch (FileNotFoundException e1) {
				Log.e("yo","cagamos escribiendo el xml culiao xd");
				e1.printStackTrace();
			} catch (IllegalArgumentException e) {
				Log.e("yo","cagamos escribiendo el xml culiao xd");
				e.printStackTrace();
			} catch (IllegalStateException e) {
				Log.e("yo","cagamos escribiendo el xml culiao xd");
				e.printStackTrace();
			} catch (IOException e) {
				Log.e("yo","cagamos escribiendo el xml culiao xd");
				e.printStackTrace();
			}
			
//			try {
//				fout = new OutputStreamWriter(getActivity().openFileOutput("poi_descargados.xml", Context.MODE_PRIVATE));
//			} catch (FileNotFoundException e1) {
//				e1.printStackTrace();
//				Log.e("yo","cagamos escribiendo el xml culiao xd");
//			}
//			sb = new StringBuilder();
			
			aGPS = new Thread(new Runnable() {
			    public void run() {
					try {
						Thread.sleep(GPSTracker.TIEMPO_GPS+200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}

			 });
			aWIFI = new Thread(new Runnable() {
			    public void run() {
					try {
						Thread.sleep(GPSTracker.TIEMPO_WIFI+200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}

			 });
			mProgressDialog.show();
        }
 
        @Override
        protected Integer doInBackground(String... params) {
            // Aquí hacemos las tareas 
        	
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
//					sb.append("<pois>");
					

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
						 
//						ser.endTag("", "poi");
//						sb.append("<poi>");
//						sb.append("<nombre>" + b.getName() + "</nombre>");
//						sb.append("<tipo>" + b.getFeatureCode() + "</tipo>");
//						sb.append("<latitud>" + b.getLatitude() + "</latitud>");
//						sb.append("<longitud>" + b.getLongitude() + "</longitud>");
//						sb.append("</poi>");
						
					}
					ser.endTag("", "pois");

					ser.endDocument();
					fout.close();
					
//					sb.append("</pois>");
//					fout.write(sb.toString());
//					fout.close();
				}
			} catch (IOException e) {
				return FALLO;
			} catch (Exception e) {
				return FALLO;
			}
	        
	        
	        return OK;
    }
        
 
        @Override
        protected void onPostExecute(Integer result) {
        	mProgressDialog.dismiss();
        	if(result == OK){
        		Toast.makeText(getActivity(), "La ubicación es - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        		lanzarMapa(latitude, longitude);
        	}
        	else if(result == ERROR_GPS){
        		ubicacion.showSettingsAlert(getActivity(), "GPS no activado", "GPS no está activado. Desea activarlo?");
        	}
        	else if(result == FALLO_TIME_OUT){
        		Toast.makeText(getActivity(), "Ups, tuvimos un error detectando su ubicación, intente nuevamente", Toast.LENGTH_LONG).show();
        	}
        	else if(result == ERROR_WIFI){
        		ubicacion.showSettingsAlert(getActivity(), "Localización por red no activada", "La localización por red no está activada. Desea activarla?");
        	}
        	else if(result == FALLO){
        		Toast.makeText(getActivity(), "Ocurrió un error obteniendo los lugares", Toast.LENGTH_LONG).show();
        	}
        		
        }
        
        
	}

}
