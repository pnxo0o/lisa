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
	private static final long MIN_TIME_BW_UPDATES = 1000 * 50 * 1; // 5sg

	// Location Manager
	protected LocationManager locationManager;

	public GPSTracker(Context context, int typeLocation) {
		this.context = context;
		this.typeLocation = typeLocation;
		getLocation();
	}

	public Location getLocation() {
//		try {
			
			locationManager = (LocationManager) context
					.getSystemService(LOCATION_SERVICE);


			if(this.typeLocation==NetworkUtil.TYPE_WIFI){
				this.canGetLocation = true;
//				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
//				}
			}

			if(this.typeLocation==NetworkUtil.TYPE_MOBILE){
//				if (isGPSEnabled) {
					this.canGetLocation = true;
//					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
//						}
					}
//				}
			}
//		 } 
//		catch (Exception e) {
//	            e.printStackTrace();
//	     }
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
	public void showSettingsAlert(Context cont){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(cont);
   	 
        // Titulo
        alertDialog.setTitle("GPS no activado");
 
        // Mensaje
        alertDialog.setMessage("GPS no está activado. Desea activarlo?");
 
        // Boton si
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

	@Override
	public void onLocationChanged(Location location) {
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
