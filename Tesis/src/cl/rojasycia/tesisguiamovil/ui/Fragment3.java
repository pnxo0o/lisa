package cl.rojasycia.tesisguiamovil.ui;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;

import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.model.PuntoDeInteres;
import cl.rojasycia.tesisguiamovil.struct.PuntoDeInteresAdapter;
import cl.rojasycia.tesisguiamovil.utils.POISQLiteHelper;
import cl.rojasycia.tesisguiamovil.utils.ParserPuntoDeInteres;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class Fragment3 extends SherlockFragment {
	
	private ParserPuntoDeInteres listaGuardadaPtos;
	private ArrayList<PuntoDeInteres> puntos;
	private ListView listaVisualizada;
	private PuntoDeInteresAdapter adaptador;
	private TextView textoNoPoi;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment3, container, false);
		
		listaVisualizada = (ListView)rootView.findViewById(R.id.listViewFavPoi);
		textoNoPoi = (TextView)rootView.findViewById(R.id.textView2);
		
		listaGuardadaPtos = new ParserPuntoDeInteres(getActivity());
		puntos = listaGuardadaPtos.getPOIDesdeBD(getActivity());
		
		
		if(puntos !=null){
			adaptador = new PuntoDeInteresAdapter (getActivity(), R.layout.item_poi, puntos );
			listaVisualizada.setAdapter(adaptador);
			listaVisualizada.setVisibility(View.VISIBLE);
			textoNoPoi.setVisibility(View.GONE);
		}
		else{
			listaVisualizada.setVisibility(View.GONE);
			textoNoPoi.setVisibility(View.VISIBLE);
		}
		
		listaVisualizada.setOnItemClickListener(new OnItemClickListener() {
			
			int posicion;

			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				
				posicion = position;
				
				AlertDialog.Builder alertDialog;
				LayoutInflater inflater;
				View convertView;
				ListView lv;
				
				final POISQLiteHelper usdbh = new POISQLiteHelper(getActivity(), "DBPoi", null, 1);
		        final SQLiteDatabase db = usdbh.getWritableDatabase();
				final AlertDialog dlg;
				
				String names[] ={"Eliminar de Favoritos","Ver Ruta"};
    		    alertDialog = new AlertDialog.Builder(getActivity());
    		    inflater = getActivity().getLayoutInflater();
    		    convertView = (View) inflater.inflate(R.layout.list_dialog, null);
    		    alertDialog.setView(convertView);
    		    alertDialog.setTitle("Opciones de lugares");
    		    lv = (ListView) convertView.findViewById(R.id.lv);
    		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, names);
    		    lv.setAdapter(adapter);
    		    dlg = alertDialog.create();
    		    dlg.show();
    		        
    		    lv.setOnItemClickListener(new OnItemClickListener() {
    		    	@Override
    				public void onItemClick(AdapterView<?> arg0, View arg1, int positionl, long id) {		
    		    		if(positionl == 0){
    		    			String sql = "DELETE FROM PuntoDeInteres WHERE latitudPOI="
    		    				+adaptador.getItem(posicion).getLatitudPOI()+" AND longitudPOI="
    							+adaptador.getItem(posicion).getLongitudPOI();
    							
    						db.execSQL(sql);
    						db.close();
    						adaptador.getItem(posicion).setFavorito(false);
    						puntos.remove(posicion);
    						adaptador.notifyDataSetChanged();
    	                    adaptador.notifyDataSetInvalidated();

    					}
    					else if(positionl == 1){
    						String uriBegin = "geo:" + adaptador.getItem(posicion).getLatitudPOI() + "," + adaptador.getItem(posicion).getLongitudPOI(); 
    						String query = adaptador.getItem(posicion).getLatitudPOI() + "," + adaptador.getItem(posicion).getLongitudPOI() + "(" + adaptador.getItem(posicion).getNombrePOI() + ")"; 
    						String encodedQuery = Uri.encode(query); 
    						String uriString = uriBegin + "?q=" + encodedQuery + "&z=16"; 
    						Uri uri = Uri.parse(uriString); 
    						Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri); 
    						startActivity(mapIntent);
    					}
    					dlg.dismiss();
    				}
    			});
				
                
            }
		}); 
	
		return rootView;
		
		
	}

}
