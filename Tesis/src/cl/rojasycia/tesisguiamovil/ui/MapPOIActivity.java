package cl.rojasycia.tesisguiamovil.ui;


import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.model.ParserPuntoDeInteres;
import cl.rojasycia.tesisguiamovil.model.PuntoDeInteres;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapPOIActivity extends SherlockFragmentActivity {
	
	private GoogleMap mapa;
	private double latitud;
	private double longitud;
	private LatLng latLongUsuario;
	ParserPuntoDeInteres listaGuardadaPtos;
	List<PuntoDeInteres> puntos;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_mappoi);
		
		Bundle datos = this.getIntent().getExtras();
		latitud = datos.getDouble("latitud");
		longitud = datos.getDouble("longitud");
		
		mapa = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mapa.moveCamera(CameraUpdateFactory.zoomTo(15));
		
		latLongUsuario = new LatLng (latitud, longitud);
		mapa.addMarker(new MarkerOptions().position(latLongUsuario));
		mapa.animateCamera(CameraUpdateFactory.newLatLng(latLongUsuario), 200, null);
		
		listaGuardadaPtos = new ParserPuntoDeInteres(getApplicationContext());
		puntos = listaGuardadaPtos.getPOI();
		
		if(puntos.size()>0){
			Iterator<PuntoDeInteres> i = puntos.listIterator();
			while( i.hasNext() ) {
				   PuntoDeInteres b = (PuntoDeInteres) i.next();
				   LatLng POILatLng = new LatLng(b.getLatitudPOI(), b.getLongitudPOI());
				   mapa.addMarker(new MarkerOptions()
					.position(POILatLng));
			   }
		}
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
