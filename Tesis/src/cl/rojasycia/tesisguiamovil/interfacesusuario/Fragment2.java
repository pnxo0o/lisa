package cl.rojasycia.tesisguiamovil.interfacesusuario;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidbegin.sidemenututorial.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment2 extends SherlockFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment2, container, false);
		return rootView;
	}

}
