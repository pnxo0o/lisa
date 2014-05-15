package cl.rojasycia.tesisguiamovil.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.model.Noticia;
import cl.rojasycia.tesisguiamovil.model.Universidad;
import cl.rojasycia.tesisguiamovil.utils.NetworkUtil;
import cl.rojasycia.tesisguiamovil.utils.DownloaderNoticia;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.manuelpeinado.fadingactionbar.extras.actionbarsherlock.FadingActionBarHelper;

public class InfUniversidadActivity extends SherlockActivity {
	
	private final int OK = 0;
	private final int FALLO = 1;
	
	private int universidad_elegida;
	private Universidad u;
	private TextView tituloUniversidad;
	private TextView descripcionUniversidad;
	private TextView urlUniversidad;
	private ImageView imgUniversidad;
	private ImageView logoUniversidad;
	private String feedUniversidad;
	
	private ProgressDialog mProgressDialog;
	private AsyncNoticias descarganoticias;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        FadingActionBarHelper helper = new FadingActionBarHelper()
            .actionBarBackground(R.drawable.ab_background)
            .headerLayout(R.layout.header_infuniversidad)
            .contentLayout(R.layout.activity_infuniversidad);
        setContentView(helper.createView(this));
        helper.initActionBar(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        
        Bundle datos = this.getIntent().getExtras();
        universidad_elegida = datos.getInt("universidad_elegida");
        
        u = new Universidad(universidad_elegida, getApplicationContext());
        
        tituloUniversidad = (TextView)findViewById(R.id.txTituloUniversidad);
        urlUniversidad = (TextView)findViewById(R.id.txUrlUniversidad);
        descripcionUniversidad = (TextView)findViewById(R.id.txDescripcionUniversidad);
        imgUniversidad = (ImageView)findViewById(R.id.image_header);
        logoUniversidad = (ImageView)findViewById(R.id.logoUniversidad);
        
        getSupportActionBar().setTitle(u.getNombreUniversidad());
        tituloUniversidad.setText(u.getNombreUniversidad());
        urlUniversidad.setText(u.getUrl());
        descripcionUniversidad.setText(u.getDescripcion());
        imgUniversidad.setImageResource(u.getImagenUniversidad());
        logoUniversidad.setImageResource(u.getLogoUniversidad());

        urlUniversidad.setText(u.getUrl());
        Linkify.addLinks(urlUniversidad, Linkify.ALL);
        
        feedUniversidad=u.getFeedUniversidad();
        
        mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("Buscando...");
		mProgressDialog.setCancelable(false);
        
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
       MenuInflater inflater = getSupportMenuInflater();
       inflater.inflate(R.menu.noticias_menu, menu);
       return super.onCreateOptionsMenu(menu);
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.noticias_universidad:
        	if ( NetworkUtil.getConnectivityStatus(this) == NetworkUtil.TYPE_NOT_CONNECTED ){
            	Toast.makeText(getApplicationContext(), "No hay conexión a internet", Toast.LENGTH_SHORT).show();
            }
            else{   		
            	descarganoticias = new AsyncNoticias();
    			descarganoticias.execute();
            	return true;
            }  
        case android.R.id.home:
        	super.onBackPressed();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	public void lanzarNoticias(String universidad){
		Intent intent;
		intent = new Intent(this, NoticiasActivity.class);
		intent.putExtra("universidad_seleccionada", universidad);
		startActivity(intent);
	}
	
	
	private class AsyncNoticias extends AsyncTask<String, Void, Integer>{

		private StringBuilder sb;
		private OutputStreamWriter fout;
		
		@Override
        protected void onPreExecute() {
			mProgressDialog.show();
			sb = new StringBuilder();
			try {
				fout = new OutputStreamWriter(getApplicationContext().openFileOutput("noticias_descargadas.xml", Context.MODE_PRIVATE));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		
		@Override
		protected Integer doInBackground(String... params) {
			try {	
				DownloaderNoticia pnoticia = new DownloaderNoticia(feedUniversidad);
				List<Noticia> noticias = pnoticia.descargarNoticias();
				if(noticias.size() > 0){
					Iterator<Noticia> iterador = noticias.listIterator(); 
					sb.append("<noticias>");
					while( iterador.hasNext() ) {
						Noticia b = (Noticia) iterador.next();
						sb.append("<noticia>");
						sb.append("<titular>" + b.getTitularNoticia() + "</titular>");
						sb.append("<fecha>" + b.getFechaNoticia() + "</fecha>");
						sb.append("<link>" + b.getLinkNoticia() + "</link>");
						sb.append("</noticia>");			  
					}
					sb.append("</noticias>");
					fout.write(sb.toString());
					fout.close();
				}
			} catch (IOException e) {
					e.printStackTrace();
					return FALLO;
			}
			return OK;
		}
		
		@Override
	    protected void onPostExecute(Integer result) {
			mProgressDialog.dismiss();
			if(result == OK){
				lanzarNoticias(u.getNombreUniversidad());
	        }
	        else 
	        {
	        	Toast.makeText(getApplicationContext(), "Hubo un error desconocido al descargar las noticias", Toast.LENGTH_LONG).show();
	        }
		}
		
	}

}
