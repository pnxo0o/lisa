package cl.rojasycia.tesisguiamovil.ui.struct;

import java.util.ArrayList;

import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.model.Noticia;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NoticiasAdapter extends ArrayAdapter<Noticia> {
	ArrayList<Noticia> noticias;
	Context context;

	public NoticiasAdapter(Context context, int textViewResourceId,
			ArrayList<Noticia> noticias) {
		super(context, textViewResourceId, noticias);
		this.noticias = noticias;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_noticias, null);
		}
		Noticia noticia = noticias.get(position);
		if (noticia != null) {
			TextView tv1 = (TextView) convertView.findViewById(R.id.titular_noticia);
			TextView tv2 = (TextView) convertView.findViewById(R.id.fecha_noticia);

			if (tv1 != null) {
				tv1.setText(noticia.getTitularNoticia());
			}
			if (tv2 != null) {
				tv2.setText(noticia.getFechaNoticia());
			}

		}
		return convertView;
	}
}
