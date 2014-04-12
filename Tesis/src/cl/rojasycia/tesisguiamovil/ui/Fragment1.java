package cl.rojasycia.tesisguiamovil.ui;

import com.actionbarsherlock.app.SherlockFragment;

import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.struct.ListViewExpanableAdaptador;
import cl.rojasycia.tesisguiamovil.struct.ListViewExpanableItems;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class Fragment1 extends SherlockFragment {
	
	SparseArray<ListViewExpanableItems> grupos = new SparseArray<ListViewExpanableItems>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment1, container, false);
		crearDatos();
		ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.listViewexp);
		ListViewExpanableAdaptador adapter = new ListViewExpanableAdaptador(getActivity(), grupos);
		listView.setAdapter(adapter);
		return rootView;
	}
	
	public void crearDatos() {

		ListViewExpanableItems grupo0 = new ListViewExpanableItems("Universidades");
		grupo0.children.add("Universidad de Playa Ancha de Ciencias de la Educaci�n");
		grupo0.children.add("Universidad de Valpara�so");
		grupo0.children.add("Universidad T�cnica Federico Santa Mar�a");
		grupo0.children.add("Pontificia Universidad Cat�lica de Valpara�so");
		grupos.append(0, grupo0);

		ListViewExpanableItems grupo1 = new ListViewExpanableItems("Alimentaci�n");
		grupo1.children.add("Paella");
		grupo1.children.add("A la parrilla");
		grupo1.children.add("Frito");
		grupos.append(1, grupo1);

		ListViewExpanableItems grupo2 = new ListViewExpanableItems("Alojamiento");
		grupo2.children.add("Jam�n, queso y anan�");
		grupo2.children.add("Pollo, morrones y aceitunas");
		grupo2.children.add("Carlitos");
		grupos.append(2, grupo2);
		
		ListViewExpanableItems grupo3 = new ListViewExpanableItems("Entretenci�n");
		grupo2.children.add("Jam�n, queso y anan�");
		grupo2.children.add("Pollo, morrones y aceitunas");
		grupo2.children.add("Carlitos");
		grupos.append(3, grupo3);
		
		ListViewExpanableItems grupo4 = new ListViewExpanableItems("Servicios");
		grupo2.children.add("Jam�n, queso y anan�");
		grupo2.children.add("Pollo, morrones y aceitunas");
		grupo2.children.add("Carlitos");
		grupos.append(4, grupo4);
	}

}
