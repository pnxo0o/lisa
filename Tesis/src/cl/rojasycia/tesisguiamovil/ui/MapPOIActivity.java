package cl.rojasycia.tesisguiamovil.ui;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.model.PuntoDeInteres;
import cl.rojasycia.tesisguiamovil.ui.struct.PuntoDeInteresAdapter;
import cl.rojasycia.tesisguiamovil.utils.POISQLiteHelper;
import cl.rojasycia.tesisguiamovil.utils.ParserPuntoDeInteres;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapPOIActivity extends SherlockFragmentActivity {
	
	private GoogleMap mapa;
	private double latitud;
	private double longitud;
	private LatLng posicionUsuario;
	private ParserPuntoDeInteres listaGuardadaPtos;
	private List<PuntoDeInteres> puntosList;
	private ListView listaVisualizada;
	private PuntoDeInteresAdapter adaptador;
	private ArrayList<PuntoDeInteres> puntosArray;
	private ArrayList<Marker> arrayMarker;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_mappoi);

		listaVisualizada = (ListView)findViewById(R.id.listViewPOI);
		
		arrayMarker = new ArrayList<Marker>();
		
		Bundle datos = this.getIntent().getExtras();
		latitud = datos.getDouble("latitud");
		longitud = datos.getDouble("longitud");
		
		mapa = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mapa.moveCamera(CameraUpdateFactory.zoomTo(11));
		
		posicionUsuario = new LatLng (latitud, longitud);
		//mapa.addMarker(new MarkerOptions().position(posicionUsuario));
		mapa.animateCamera(CameraUpdateFactory.newLatLng(posicionUsuario), 200, null);
		
		listaGuardadaPtos = new ParserPuntoDeInteres(getApplicationContext());
		puntosList = listaGuardadaPtos.getPOI();
		puntosArray = listaGuardadaPtos.convertirListaAArreglo(puntosList, getApplicationContext());
		
		adaptador = new PuntoDeInteresAdapter (getApplicationContext(), R.layout.item_poi, puntosArray );
		listaVisualizada.setAdapter(adaptador);
		
		if(puntosList.size()>0){
			Iterator<PuntoDeInteres> i = puntosList.listIterator();
			while( i.hasNext() ) {
				   PuntoDeInteres b = (PuntoDeInteres) i.next();
				   LatLng POILatLng = new LatLng(b.getLatitudPOI(), b.getLongitudPOI());
				   Marker te = mapa.addMarker(new MarkerOptions().title(b.getNombrePOI())
					.position(POILatLng));
				   arrayMarker.add(te);
			   }
		}
		
		listaVisualizada.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {	
				arrayMarker.get(position).showInfoWindow();
				mapa.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(adaptador.getItem(position).getLatitudPOI(), adaptador.getItem(position).getLongitudPOI())), 200, null);
			}
		});
		
		listaVisualizada.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			int posicion;

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
				
				posicion = position;
				
				AlertDialog.Builder alertDialog;
				LayoutInflater inflater;
				View convertView;
				ListView lv;
				
				final POISQLiteHelper usdbh = new POISQLiteHelper(MapPOIActivity.this, "DBPoi", null, 1);
		        final SQLiteDatabase db = usdbh.getWritableDatabase();
				final AlertDialog dlg;
				
            	if(!adaptador.getItem(position).isFavorito()){
            		
    				String names[] ={"Agregar a Favoritos","Ver Ruta"};
    		        alertDialog = new AlertDialog.Builder(MapPOIActivity.this);
    		        inflater = getLayoutInflater();
    		        convertView = (View) inflater.inflate(R.layout.list_dialog, null);
    		        alertDialog.setView(convertView);
    		        alertDialog.setTitle("Opciones de lugares");
    		        lv = (ListView) convertView.findViewById(R.id.lv);
    		        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapPOIActivity.this, android.R.layout.simple_list_item_1, names);
    		        lv.setAdapter(adapter);
    		        dlg = alertDialog.create();
    		        dlg.show();
    		        
    		        lv.setOnItemClickListener(new OnItemClickListener() {
    					@Override
    					public void onItemClick(AdapterView<?> arg0, View arg1, int positionl, long id) {		
    						if(positionl == 0){
    							String sql = "INSERT INTO PuntoDeInteres (nombrePOI, tipoPOI, latitudPOI, longitudPOI) VALUES ('" 
    												+ adaptador.getItem(posicion).getNombrePOI() + "','" 
    												+ adaptador.getItem(posicion).getTipoPOI() + "','" 
    												+ adaptador.getItem(posicion).getLatitudPOI() + "','" 
    												+ adaptador.getItem(posicion).getLongitudPOI() + "') ";
    							db.execSQL(sql);
    							db.close();
    							adaptador.getItem(posicion).setFavorito(true);
    							Toast.makeText(getApplicationContext(), "Lugar Guardado", Toast.LENGTH_SHORT).show();
    						}
    						else if(positionl == 1){
    							Intent intent = new Intent(
    			            			Intent.ACTION_VIEW,
    			            			Uri.parse("geo:<"
    			            					+latitud+">,<"
    			            					+longitud+">"
    			            					+ "?q=<"
    			            					+adaptador.getItem(posicion).getLatitudPOI()+">,<"
    			            					+adaptador.getItem(posicion).getLongitudPOI()+">"
    			            					+ "("+adaptador.getItem(posicion).getNombrePOI()+")"));
    			            	startActivity(intent);
    						}
    						dlg.dismiss();
    					}
    				});
    		        
            		
            	}
            	else{
    				
    				String names[] ={"Eliminar de Favoritos","Ver Ruta"};
    		        alertDialog = new AlertDialog.Builder(MapPOIActivity.this);
    		        inflater = getLayoutInflater();
    		        convertView = (View) inflater.inflate(R.layout.list_dialog, null);
    		        alertDialog.setView(convertView);
    		        alertDialog.setTitle("Opciones de lugares");
    		        lv = (ListView) convertView.findViewById(R.id.lv);
    		        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapPOIActivity.this, android.R.layout.simple_list_item_1, names);
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
    							Toast.makeText(getApplicationContext(), "Lugar Eliminado", Toast.LENGTH_SHORT).show();
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
				
				return true;
                
            }
		}); 
		mapa.setMyLocationEnabled(true);
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	super.onBackPressed();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	

	
}
