package cl.rojasycia.tesisguiamovil.ui;



import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.ImageView;
import android.widget.TextView;
import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.model.Universidad;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.manuelpeinado.fadingactionbar.extras.actionbarsherlock.FadingActionBarHelper;

public class InfUniversidadActivity extends SherlockActivity {
	
	private int universidad_elegida;
	private Universidad u;
	private TextView tituloUniversidad;
	private TextView descripcionUniversidad;
	private TextView urlUniversidad;
	private ImageView imgUniversidad;

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
        
        getSupportActionBar().setTitle(u.getNombreUniversidad());
        tituloUniversidad.setText(u.getNombreUniversidad());
        urlUniversidad.setText(u.getUrl());
        descripcionUniversidad.setText(u.getDescripcion());
        imgUniversidad.setImageResource(u.getImagenUniversidad());

        urlUniversidad.setText(u.getUrl());
        Linkify.addLinks(urlUniversidad, Linkify.ALL);
        
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
       return super.onCreateOptionsMenu(menu);
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
