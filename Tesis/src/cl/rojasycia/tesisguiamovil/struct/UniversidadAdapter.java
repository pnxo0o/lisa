package cl.rojasycia.tesisguiamovil.struct;

import java.util.ArrayList;

import cl.rojasycia.tesisguiamovil.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UniversidadAdapter extends ArrayAdapter<UniversidadItem> {
	private Context context;
	private ArrayList<UniversidadItem> datos;

	/**
	 * Constructor del Adapter.
	 * 
	 * @param context
	 *            context de la Activity que hace uso del Adapter.
	 * @param datos
	 *            Datos que se desean visualizar en el ListView.
	 */
	public UniversidadAdapter(Context context, ArrayList<UniversidadItem> datos) {
		super(context, R.layout.item_listuniversidad, datos);
		// Guardamos los parámetros en variables de clase.
		this.context = context;
		this.datos = datos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// En primer lugar "inflamos" una nueva vista, que será la que se
		// mostrará en la celda del ListView.
		View item = LayoutInflater.from(context).inflate(
				R.layout.item_listuniversidad, null);

		// A partir de la vista, recogeremos los controles que contiene para
		// poder manipularlos.
		// Recogemos el ImageView y le asignamos una foto.
		ImageView imagen = (ImageView) item.findViewById(R.id.logoUniv);
		imagen.setImageResource(datos.get(position).getDrawableImageID());

		// Recogemos el TextView para mostrar el nombre y establecemos el
		// nombre.
		TextView nombre = (TextView) item.findViewById(R.id.txUniv);
		nombre.setText(datos.get(position).getNombre());
		

		// Devolvemos la vista para que se muestre en el ListView.
		return item;
	}

}
