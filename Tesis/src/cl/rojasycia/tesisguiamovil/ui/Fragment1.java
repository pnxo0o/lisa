package cl.rojasycia.tesisguiamovil.ui;

import com.actionbarsherlock.app.SherlockFragment;

import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.struct.ListViewExpanableAdaptador;
import cl.rojasycia.tesisguiamovil.struct.ListViewExpanableItems;
import cl.rojasycia.tesisguiamovil.utils.NetworkUtil;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class Fragment1 extends SherlockFragment implements TaskFragment.TaskCallbacks {
	
	private SparseArray<ListViewExpanableItems> grupos = new SparseArray<ListViewExpanableItems>();
	private Button btnBuscarAqui;
	
	
	private ProgressDialog mProgressDialog;
	
	//nuevas variables
	private TaskFragment mTaskFragment;
	private static final String TAG_TASK_FRAGMENT = "task_fragment";
	
	
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
	
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      
      if(savedInstanceState != null){
    	  mProgressDialog = new ProgressDialog(getActivity());
    	  mProgressDialog.setMessage("Buscando...");
    	  mProgressDialog.setCancelable(false);
    	  mProgressDialog.show();
      }

      FragmentManager fm = getActivity().getSupportFragmentManager();
      mTaskFragment = (TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
      
      if (mTaskFragment == null) {
        mTaskFragment = new TaskFragment();
        mTaskFragment.setTargetFragment(this, 0);
        fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
      }

      
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
      mProgressDialog.dismiss();
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
			mProgressDialog = new ProgressDialog(getActivity());
			mProgressDialog.setMessage("Buscando...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			mTaskFragment.start();
		}
        
    }

//	public void lanzarMapa(double latitud, double longitud){
//		Intent intent;
//		intent = new Intent(getActivity(), MapPOIActivity.class);
//		intent.putExtra("latitud", latitud);
//		intent.putExtra("longitud", longitud);
//		startActivity(intent);
//	}
//
//	private class AsyncLatLong extends AsyncTask<String, Void, Integer>{
//
//		private Thread a5000, a2500;
//		private OutputStreamWriter fout;
//		private Grupo gp;
//		private StringBuilder sb;
//		private double latitude;
//		private double longitude;
//		private GPSTracker ubicacion;
//		
//		@Override
//        protected void onPreExecute() {
//			ubicacion = new GPSTracker(getActivity(), NetworkUtil.getConnectivityStatus(getActivity()));
//			gp = new Grupo(0, 0);
//			try {
//				fout = new OutputStreamWriter(getActivity().openFileOutput("poi_descargados.xml", Context.MODE_PRIVATE));
//			} catch (FileNotFoundException e1) {
//				e1.printStackTrace();
//				Log.e("yo","cagamos escribiendo el xml culiao");
//			}
//			sb = new StringBuilder();
//			a5000 = new Thread(new Runnable() {
//			    public void run() {
//					try {
//						Thread.sleep(5100);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					} 
//				}
//
//			 });
//			a2500 = new Thread(new Runnable() {
//			    public void run() {
//					try {
//						Thread.sleep(2500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					} 
//				}
//
//			 });
//        }
// 
//        @Override
//        protected Integer doInBackground(String... params) {
//            // Aquí hacemos las tareas 
//        	
//	        if(ubicacion.canGetLocation()){
//	        	if(NetworkUtil.getConnectivityStatus(getActivity())==NetworkUtil.TYPE_WIFI){
//	        		a2500.run();
//	        		latitude = ubicacion.getLatitude();
//		        	longitude = ubicacion.getLongitude();
//		        	if(latitude == 0.0 && longitude == 0.0) return 2;
//	        	}
//	        	else{
//	        		if(!ubicacion.isGPSEnabled()) return 1;
//	        		a5000.run();
//	        		latitude = ubicacion.getLatitude();
//		        	longitude = ubicacion.getLongitude();
//		        	ubicacion.stopUsingGPS();
//		        	if(latitude == 0.0 && longitude == 0.0) return 2;
//	        	}
//	        	 Log.e("yo","ya tengo el place");
//	        	
//	        	try {
//	        		WebService.setUserName("pnxo0o");
//					List<Toponym> searchResult = WebService.findNearby(latitude, longitude, 250.0 ,FeatureClass.S ,gp.getGrupo(), "es", 12);
//					if(searchResult.size()>0){
//						   Log.e("yo","ya baje las weas");
//						   Iterator<Toponym> iterador = searchResult.listIterator(); 
//						   while( iterador.hasNext() ) {
//							   Toponym b = (Toponym) iterador.next();
//							   sb.append("<poi>");
//							   sb.append("<nombre>" + b.getName() + "</nombre>");
//							   sb.append("<tipo>" + b.getFeatureCode() + "</tipo>");
//							   sb.append("<latitud>" + b.getLatitude() + "</latitud>");
//							   sb.append("<longitud>" + b.getLongitude() + "</longitud>");
//							   sb.append("</poi>");
////				                           "VALUES ( '" + b.getName() + "', '" + b.getFeatureCode() +"', "+ b.getLatitude()+", "+b.getLongitude()+")");				  
//						   }
//						   fout.write(sb.toString());
//						   fout.close();
//					}
//				} catch (IOException e) {
//					Log.e("yo","cometí un error ups");
//					e.printStackTrace();
//				} catch (Exception e) {
//					Log.e("yo","no use condon");
//					e.printStackTrace();
//				}
//	        	Log.e("yo","toy redy");
//	        	return 0;
//	        }
//	        else{
//	        	return 1;
//	        }
//        }
// 
//        @Override
//        protected void onPostExecute(Integer result) {
//            // Aquí actualizamos la UI con el resultado
//        	if(result==0){
//        		Toast.makeText(getActivity(), "La ubicación es - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//        		lanzarMapa(latitude, longitude);
//        	}
//        	else if(result==1){
//        		ubicacion.showSettingsAlert(getActivity());
//        	}
//        	else{
//        		Toast.makeText(getActivity(), "Hubo un error al buscar la ubicación", Toast.LENGTH_LONG).show();
//        	}
//        		
//        }
//	}

	@Override
	public void onPreExecute() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onCancelled() {
		// TODO Auto-generated method stub
		mProgressDialog.dismiss();
	}


	@Override
	public void onPostExecute() {
		// TODO Auto-generated method stub
		mProgressDialog.dismiss();
	}
	
}
