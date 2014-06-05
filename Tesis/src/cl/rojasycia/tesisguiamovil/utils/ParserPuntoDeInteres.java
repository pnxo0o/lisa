package cl.rojasycia.tesisguiamovil.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.model.PuntoDeInteres;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Xml;

public class ParserPuntoDeInteres {

    private Context context;
	private ArrayList<PuntoDeInteres> puntoDeInteres;

	public ParserPuntoDeInteres(Context context)
    {
        this.context = context;
    }
    
    public List<PuntoDeInteres> getPOI(){
    	
        XmlPullParser parser = Xml.newPullParser();
        
        try
        {    
        	parser.setInput(context.openFileInput("poi_descargados.xml"), null);      	
        	int evento = parser.getEventType();
        	PuntoDeInteres poiActual = null;
            
            while (evento != XmlPullParser.END_DOCUMENT)
            {
                String etiqueta = null;
                
                switch (evento)
                {
                    case XmlPullParser.START_DOCUMENT:
                    	
                    	puntoDeInteres = new ArrayList<PuntoDeInteres>();
                        break;
                        
                    case XmlPullParser.START_TAG:
                    	
                    	etiqueta = parser.getName();
                        
                        if (etiqueta.equals("poi"))
                        {
                        	poiActual = new PuntoDeInteres();
                        } 
                        else if (poiActual != null)
                        {
                            if (etiqueta.equals("nombre"))
                            {
                            	poiActual.setNombrePOI(parser.nextText());
                            } 
                            else if (etiqueta.equals("tipo"))
                            {
                            	poiActual.setTipoPOI(parser.nextText());
                            } 
                            else if (etiqueta.equals("latitud"))
                            {
                            	poiActual.setLatitudPOI(Double.parseDouble(parser.nextText()));
                            } 
                            else if (etiqueta.equals("longitud"))
                            {
                            	poiActual.setLongitudPOI(Double.parseDouble(parser.nextText()));
                            } 
                        }
                        break;
                        
                    case XmlPullParser.END_TAG:
                    	
                    	etiqueta = parser.getName();
                    	
                        if (etiqueta.equals("poi") && poiActual != null)
                        {
                        	puntoDeInteres.add(poiActual);
                        } 
                        break;
                }
                
                evento = parser.next();
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }

        return puntoDeInteres;
    }
    
    
    public ArrayList<PuntoDeInteres> convertirListaAArreglo(List<PuntoDeInteres> ls, Context context) {	
    	POISQLiteHelper usdbh = new POISQLiteHelper(context, "DBPoi", null, 1);
        SQLiteDatabase db = usdbh.getWritableDatabase();
    	ArrayList<PuntoDeInteres> x = new ArrayList<PuntoDeInteres>();
    	if(ls.size() > 0){
			Iterator<PuntoDeInteres> iterador = ls.listIterator();
			while( iterador.hasNext() ) {
				
				PuntoDeInteres b = (PuntoDeInteres) iterador.next();
				//pongo imagen
				if(b.getTipoPOI().equals("UNIV")){
					b.setImagenPOI(R.drawable.icon_universidad);
				}
				else if(b.getTipoPOI().equals("RHSE") || b.getTipoPOI().equals("GHSE") || b.getTipoPOI().equals("HTL")){
					b.setImagenPOI(R.drawable.icon_alojamiento);
				}
				else if(b.getTipoPOI().equals("RESTO")){
					b.setImagenPOI(R.drawable.icon_alimentacion);
				}
				
				else if(b.getTipoPOI().equals("PP") || b.getTipoPOI().equals("HSP")){
					b.setImagenPOI(R.drawable.icon_servicios);
				}
				else{
					b.setImagenPOI(R.drawable.icon_universidad);
				}
				
				
				//consulto por checkbox
				Cursor c = db.rawQuery("SELECT latitudPOI, longitudPOI FROM PuntoDeInteres WHERE latitudPOI="+b.getLatitudPOI()+" AND longitudPOI="+b.getLongitudPOI()+"", null);
				if (c.moveToFirst()) {
					if(c.getDouble(0) == b.getLatitudPOI() && c.getDouble(1) == b.getLongitudPOI()){
						b.setFavorito(true);
					}
					else{
						b.setFavorito(false);
					}
				}
				c.close();
				x.add(b);
			}
		}
    	return x;
    
    }

    public ArrayList<PuntoDeInteres> getPOIDesdeBD(Context context){
    	ArrayList<PuntoDeInteres> puntosSQL = new ArrayList<PuntoDeInteres>();
		final POISQLiteHelper usdbh = new POISQLiteHelper(context, "DBPoi", null, 1);
        final SQLiteDatabase db = usdbh.getWritableDatabase();
    	Cursor c = db.rawQuery("SELECT nombrePOI, tipoPOI, latitudPOI, longitudPOI FROM PuntoDeInteres ", null);

		if (c.moveToFirst()) {
		     do {
		    	 
		    	 PuntoDeInteres b = new PuntoDeInteres(c.getString(0), c.getString(1), c.getDouble(2), c.getDouble(3));
		    	 if(b.getTipoPOI().equals("UNIV")){
						b.setImagenPOI(R.drawable.icon_universidad);
					}
					else if(b.getTipoPOI().equals("RHSE") || b.getTipoPOI().equals("GHSE") || b.getTipoPOI().equals("HTL")){
						b.setImagenPOI(R.drawable.icon_alojamiento);
					}
					else if(b.getTipoPOI().equals("RESTO")){
						b.setImagenPOI(R.drawable.icon_alimentacion);
					}
					
					else if(b.getTipoPOI().equals("PP") || b.getTipoPOI().equals("HSP")){
						b.setImagenPOI(R.drawable.icon_servicios);
					}
					else{
						b.setImagenPOI(R.drawable.icon_universidad);
					}
		    	 puntosSQL.add(b);
		     
		     } while(c.moveToNext());
		     return puntosSQL;
		}
		else{
			return null;
		}
		
	}
    
    
}
