package cl.rojasycia.tesisguiamovil.utils;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener {

	private Context context;

	// bandera para estado GPS
	boolean isGPSEnabled = false;

	// bandera para estad de red
	boolean isNetworkEnabled = false;

	// bandera para locacion 
	boolean canGetLocation = false;
	
	//determinar tipo de conexion
	int typeLocation = NetworkUtil.TYPE_NOT_CONNECTED;

	Location location; // localizacion
	double latitude; // latitud
	double longitude; // longitud

	// Minima distancia para cambiar actualizacion 
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 3; // 3 metros

	// Minimo tiempo para actualizar
	public static final long TIEMPO_GPS = 1000 * 51 * 1; // 51sg
	public static final long TIEMPO_WIFI = 1000 * 28 * 1; // 28sg

	// Location Manager
	protected LocationManager locationManager;

	public GPSTracker(Context context, int typeLocation) {
		this.context = context;
		this.typeLocation = typeLocation;
		if(this.typeLocation == NetworkUtil.TYPE_WIFI){
			getLocationWifi();
		}
		else{
			getLocationGPS();
		}
		
	}

	public Location getLocationGPS() {
		try {
			
			locationManager = (LocationManager) context
					.getSystemService(LOCATION_SERVICE);

			if(isGPSEnabled()){
				this.canGetLocation = true;
					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER,
							TIEMPO_GPS,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
			}
		 } 
		catch (Exception e) {
	            e.printStackTrace();
	     }
		return location;
	}
	
	
	public Location getLocationWifi() {
		try {
			
			locationManager = (LocationManager) context
					.getSystemService(LOCATION_SERVICE);
			this.canGetLocation = true;
			locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER,
						TIEMPO_WIFI,
						MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				if (locationManager != null) {
					location = locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (location != null) {
						latitude = location.getLatitude();
						longitude = location.getLongitude();
					}
				}
		 } 
		catch (Exception e) {
	            e.printStackTrace();
	     }
		return location;
	}
	
	
	/**
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 * */
	public void stopUsingGPS(){
		if(locationManager != null){
			locationManager.removeUpdates(GPSTracker.this);
		}		
	}
	
	/**
	 * Obtener latitud
	 * */
	public double getLatitude(){
		if(location != null){
			latitude = location.getLatitude();
		}
		return latitude;
	}
	
	/**
	 * Obtener longitud
	 * */
	public double getLongitude(){
		if(location != null){
			longitude = location.getLongitude();
		}
		return longitude;
	}
	
	/**
	 * Function to check GPS/wifi enabled
	 * @return boolean
	 * */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}
	
	/**
	 * Function to show settings alert dialog
	 * On pressing Settings button will launch Settings Options
	 * */
	public void showSettingsAlert(Context cont, String titulo, String texto){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(cont);

        alertDialog.setTitle(titulo);

        alertDialog.setMessage(texto);

        alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            	context.startActivity(intent);
            	
            }
        });
 
        // Boton no
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
 
        // Mostrar
        alertDialog.show();
	}
	
	public boolean isGPSEnabled(){
		locationManager = (LocationManager) context
				.getSystemService(LOCATION_SERVICE);

		// Obteniendo estado GPS
		isGPSEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		return isGPSEnabled;
	}
	
	public boolean isWifiEnabled(){
		locationManager = (LocationManager) context
				.getSystemService(LOCATION_SERVICE);

		// Obteniendo estado GPS
		isNetworkEnabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		return isNetworkEnabled;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.e("yo",location.getLatitude()+" - "+location.getLongitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
