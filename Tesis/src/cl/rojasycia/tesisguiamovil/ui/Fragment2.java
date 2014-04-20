package cl.rojasycia.tesisguiamovil.ui;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;

import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.model.Universidad;
import cl.rojasycia.tesisguiamovil.struct.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
		
		lvlUniversidades.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
            	switch (posicion){
            	case 0:
            		lanzarUniversidad(Universidad.UPLA);
            		break;
            	case 1:
            		lanzarUniversidad(Universidad.UV);
            		break;
            	case 2:
            		lanzarUniversidad(Universidad.USM);
            		break;
            	case 3:
            		lanzarUniversidad(Universidad.PUCV);
            		break;
            	}
            }
        });


		return rootView;
	}
	
	public void lanzarUniversidad(int a){
		Intent intent;
		intent = new Intent(getActivity(), InfUniversidadActivity.class);
		intent.putExtra("universidad_elegida", a);
		startActivity(intent);
	}

	private void rellenarArrayList() {
		arrayUniversidades.add(new UniversidadItem("Universidad de Playa Ancha de Ciencias de la Educaci�n", R.drawable.u_upla));
		arrayUniversidades.add(new UniversidadItem("Universidad de Valpara�so", R.drawable.u_uv));
		arrayUniversidades.add(new UniversidadItem("Universidad T�cnica Federico Santa Mar�a", R.drawable.u_usm));
		arrayUniversidades.add(new UniversidadItem("Pontificia Universidad Cat�lica de Valparaiso", R.drawable.u_pucv));
	}


}
