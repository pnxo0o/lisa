package cl.rojasycia.tesisguiamovil.ui;


import android.os.Bundle;
import cl.rojasycia.tesisguiamovil.R;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_mappoi);
		
		Bundle datos = this.getIntent().getExtras();
		latitud = datos.getDouble("latitud");
		longitud = datos.getDouble("longitud");
		
		mapa = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mapa.moveCamera(CameraUpdateFactory.zoomTo(13));
		
		
		latLongUsuario = new LatLng (latitud, longitud);
		mapa.addMarker(new MarkerOptions().position(latLongUsuario));
		mapa.animateCamera(CameraUpdateFactory.newLatLng(latLongUsuario), 200, null);
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
