package cl.rojasycia.tesisguiamovil.interfacesusuario;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;
import cl.rojasycia.tesisguiamovil.R;

import cl.rojasycia.tesisguiamovil.universidades.*;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Fragment2 extends SherlockFragment {
	
	private ArrayList<UniversidadItem> arrayUniversidades;
	private ListView lvlUniversidades;
	private UniversidadAdapter adapter;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment2, container, false);
		lvlUniversidades = (ListView) rootView.findViewById(R.id.lvUniv);
		
		arrayUniversidades = new ArrayList<UniversidadItem>();
		rellenarArrayList();
		adapter = new UniversidadAdapter(getActivity(), arrayUniversidades);
		lvlUniversidades.setAdapter(adapter);


		return rootView;
	}
	

	private void rellenarArrayList() {
		arrayUniversidades.add(new UniversidadItem("Universidad de Playa Ancha de Ciencias de la Educación", R.drawable.u_upla));
		arrayUniversidades.add(new UniversidadItem("Pontificia Universidad Católica de Valparaiso", R.drawable.u_pucv));
		arrayUniversidades.add(new UniversidadItem("Universidad Técnica Federico Santa María", R.drawable.u_usm));
		arrayUniversidades.add(new UniversidadItem("Universidad de Valparaiso", R.drawable.u_uv));
	}

//	 private class AsyncLoadUniv extends AsyncTask<Void, Integer, Boolean>{
//
//
//		 @Override
//		    protected Boolean doInBackground(Void... params) {
//		        
//				
//				return true;
//		    }
//			
//		 @Override
//		    protected void onPostExecute(Boolean result) {
//		        
//		    }
//		}

}
