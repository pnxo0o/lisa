package cl.rojasycia.tesisguiamovil.ui;


//hiy
//sdgfds

import android.os.Bundle;
import cl.rojasycia.tesisguiamovil.R;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.manuelpeinado.fadingactionbar.extras.actionbarsherlock.FadingActionBarHelper;

public class InfUniversidadActivity extends SherlockActivity {
	
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

    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
       MenuInflater inflater = getSupportMenuInflater();
       return super.onCreateOptionsMenu(menu);
    }

}
