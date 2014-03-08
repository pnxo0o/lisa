package cl.rojasycia.tesisguiamovil.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.actionbarsherlock.app.SherlockActivity;
import cl.rojasycia.tesisguiamovil.R;

public class SplashScreen extends SherlockActivity {

	// Tiempo de pantalla
	private static int SPLASH_TIME_OUT = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		getSupportActionBar().hide();

		new Handler().postDelayed(new Runnable() {

			/*
			 * Mostrando la pantalla de inicion
			 */

			@Override
			public void run() {
				// Este metodo se ejecutará cuando se acabe el tiempo
				// Se iniciará el Fragment
				Intent i = new Intent(SplashScreen.this, PrincipalActivity.class);
				startActivity(i);

				// Se cierra esta Actividad
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

}