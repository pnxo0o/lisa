package cl.rojasycia.tesisguiamovil.struct;

import java.util.ArrayList;
import java.util.List;

public class ListViewExpanableItems {
	public String string;
	public final List<String> children = new ArrayList<String>();

	public ListViewExpanableItems(String string) {
		this.string = string;
	}
}
