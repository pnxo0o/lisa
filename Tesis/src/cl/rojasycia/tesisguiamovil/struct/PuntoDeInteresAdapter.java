package cl.rojasycia.tesisguiamovil.struct;

import java.util.ArrayList;

import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.model.PuntoDeInteres;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PuntoDeInteresAdapter extends ArrayAdapter<PuntoDeInteres>{
	ArrayList<PuntoDeInteres> puntoDeInteres;
	Context context;

	public PuntoDeInteresAdapter(Context context, int textViewResourceId,
			ArrayList<PuntoDeInteres> puntoDeInteres) {
		super(context, textViewResourceId, puntoDeInteres);
		this.puntoDeInteres = puntoDeInteres;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_poi, null);
		}
		PuntoDeInteres poi = puntoDeInteres.get(position);
		if (poi != null) {
			TextView tv1 = (TextView) convertView.findViewById(R.id.txtPOI);
			TextView tv2 = (TextView) convertView.findViewById(R.id.txtDes);

			if (tv1 != null) {
				tv1.setText(poi.getNombrePOI());
			}
			if (tv2 != null) {
				tv2.setText(poi.getTipoPOI());
			}

		}
		return convertView;
	}
}
