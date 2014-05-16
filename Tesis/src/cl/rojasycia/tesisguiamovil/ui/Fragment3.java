package cl.rojasycia.tesisguiamovil.ui;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;

import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.helpers.ParserPuntoDeInteres;
import cl.rojasycia.tesisguiamovil.model.PuntoDeInteres;
import cl.rojasycia.tesisguiamovil.struct.PuntoDeInteresAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Fragment3 extends SherlockFragment {
	
	private ParserPuntoDeInteres listaGuardadaPtos;
	private ArrayList<PuntoDeInteres> puntos;
	private ListView listaVisualizada;
	private PuntoDeInteresAdapter adaptador;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment3, container, false);
		
		listaVisualizada = (ListView)rootView.findViewById(R.id.listViewFavPoi);
		
		listaGuardadaPtos = new ParserPuntoDeInteres(getActivity());
		puntos = listaGuardadaPtos.getPOIDesdeBD(getActivity());
		
		
		if(puntos !=null){
			adaptador = new PuntoDeInteresAdapter (getActivity(), R.layout.item_poi, puntos );
			listaVisualizada.setAdapter(adaptador);
		}
	
		return rootView;
	}

}
