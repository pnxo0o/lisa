package cl.rojasycia.tesisguiamovil.ui;

import com.actionbarsherlock.app.SherlockFragment;

import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.ui.struct.LicensesFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Fragment4 extends SherlockFragment {
	private Button btnLicencias;
	private Button btnYop;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment4, container, false);
		
		btnLicencias = (Button) rootView.findViewById(R.id.btnLicencias);
		btnLicencias.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
				{
				LicensesFragment.displayLicensesFragment(getFragmentManager());
			    } 
			}); 
		
		btnYop = (Button) rootView.findViewById(R.id.btnyop);
		btnYop.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
				{
				Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
                myWebLink.setData(Uri.parse("http://about.me/pnxo0o"));
                    startActivity(myWebLink);
			    } 
			}); 
		return rootView;
	}
	


}
