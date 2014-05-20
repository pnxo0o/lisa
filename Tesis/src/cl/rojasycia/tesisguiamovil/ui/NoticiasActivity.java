package cl.rojasycia.tesisguiamovil.ui;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.view.View;
import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.model.Noticia;
import cl.rojasycia.tesisguiamovil.struct.NoticiasAdapter;
import cl.rojasycia.tesisguiamovil.utils.ParserNoticia;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class NoticiasActivity extends SherlockActivity {
	
	private String universidad_elegida;
	private NoticiasAdapter adaptador = null;
	private ListView lSelected;
	private ParserNoticia listaGuardadaNoticias;
	private List<Noticia> noticias;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_noticias);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Noticias");
		
		Bundle datos = this.getIntent().getExtras();
        universidad_elegida = datos.getString("universidad_seleccionada");
		getSupportActionBar().setSubtitle("de la "+universidad_elegida);
		
		lSelected = (ListView)findViewById(R.id.listView1);
		
		listaGuardadaNoticias = new ParserNoticia(getApplicationContext());
		noticias = listaGuardadaNoticias.getNoticiasGuardadas();
		
		adaptador = new NoticiasAdapter(getApplicationContext(), R.layout.item_noticias, listaGuardadaNoticias.convertirListaAArreglo(noticias));
		lSelected.setAdapter(adaptador);
		
		lSelected.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				lanzarWeb(adaptador.getItem(position).getLinkNoticia());
			}
		});
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
	
	public void lanzarWeb(String Web){
		Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(Web));
		startActivity(viewIntent);
	}

}

