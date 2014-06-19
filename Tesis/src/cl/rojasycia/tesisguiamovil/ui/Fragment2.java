package cl.rojasycia.tesisguiamovil.ui;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;

import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.model.Universidad;
import cl.rojasycia.tesisguiamovil.ui.struct.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Fragment2 extends SherlockFragment  {
	
	private ArrayList<LUniversidadItem> arrayUniversidades;
	private ListView lvlUniversidades;
	private LUniversidadAdapter adapter;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment2, container, false);
		lvlUniversidades = (ListView) rootView.findViewById(R.id.lvUniv);
		
		arrayUniversidades = new ArrayList<LUniversidadItem>();
		rellenarArrayList();
		adapter = new LUniversidadAdapter(getActivity(), arrayUniversidades);
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
            		lanzarUniversidad(Universidad.PUCV);
            		break;
            	case 3:
            		lanzarUniversidad(Universidad.USM);
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
		arrayUniversidades.add(new LUniversidadItem("Universidad de Playa Ancha de Ciencias de la Educación", R.drawable.u_upla));
		arrayUniversidades.add(new LUniversidadItem("Universidad de Valparaíso", R.drawable.u_uv));
		arrayUniversidades.add(new LUniversidadItem("Pontificia Universidad Católica de Valparaiso", R.drawable.u_pucv));
		arrayUniversidades.add(new LUniversidadItem("Universidad Técnica Federico Santa María", R.drawable.u_usm));

	}


}
