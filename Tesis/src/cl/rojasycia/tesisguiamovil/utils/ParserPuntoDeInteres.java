package cl.rojasycia.tesisguiamovil.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
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
//    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        List<PuntoDeInteres> listaPuntos = new ArrayList<PuntoDeInteres>();
// 
        
        XmlPullParser parser = Xml.newPullParser();
        
        try
        {
//            //Creamos un nuevo parser DOM
//            DocumentBuilder builder = factory.newDocumentBuilder();         
//            FileInputStream fil = context.openFileInput("poi_descargados.xml");
//            InputStream is = fil;  
//            Reader reader = new InputStreamReader(is, "UTF8"); // look up which encoding your file should have  
//            InputSource source = new InputSource(reader);  
//            Document dom = builder.parse(source);
//
//            Element root = dom.getDocumentElement();
//            NodeList items = root.getElementsByTagName("poi");
            
        	parser.setInput(context.openFileInput("poi_descargados.xml"), "utf-8");
        	
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
// 
//            //Recorremos 
//            for (int i=0; i<items.getLength(); i++)
//            {
//                PuntoDeInteres poi = new PuntoDeInteres();
// 
//                //Obtenemos poi
//                Node item = items.item(i);
// 
//                //Obtenemos la lista de datos de poi actual
//                NodeList datosPoi = item.getChildNodes();
// 
//                //Procesamos cada dato 
//                for (int j=0; j<datosPoi.getLength(); j++)
//                {
//                    Node dato = datosPoi.item(j);
//                    String etiqueta = dato.getNodeName();
// 
//                    if (etiqueta.equals("nombre"))
//                    {
//                        String texto = obtenerTexto(dato);
//                        poi.setNombrePOI(texto);;
//                    }
//                    else if (etiqueta.equals("tipo"))
//                    {
//                    	String texto = obtenerTexto(dato);
//                    	poi.setTipoPOI(texto);
//                    }
//                    else if (etiqueta.equals("latitud"))
//                    {
//                    	String texto = obtenerTexto(dato);
//                    	poi.setLatitudPOI(Double.parseDouble(texto));;
//                    }
//                    else if (etiqueta.equals("longitud"))
//                    {
//                    	String texto = obtenerTexto(dato);
//                    	poi.setLongitudPOI(Double.parseDouble(texto));;
//                    }
//                }
// 
//                listaPuntos.add(poi);
//            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
 
//        return listaPuntos;
        return puntoDeInteres;
    }
    
    private String obtenerTexto(Node dato)
    {
        StringBuilder texto = new StringBuilder();
        NodeList fragmentos = dato.getChildNodes();
 
        for (int k=0;k<fragmentos.getLength();k++)
        {
            texto.append(fragmentos.item(k).getNodeValue());
        }
 
        return texto.toString();
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
				else{
					b.setImagenPOI(R.drawable.icon_alojamiento);
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
		    	 
		    	 PuntoDeInteres p = new PuntoDeInteres(c.getString(0), c.getString(1), c.getDouble(2), c.getDouble(3));
		    	 if(p.getTipoPOI().equals("UNIV")){
						p.setImagenPOI(R.drawable.icon_universidad);
					}
					else{
						p.setImagenPOI(R.drawable.icon_alojamiento);
					}
		    	 puntosSQL.add(p);
		     
		     } while(c.moveToNext());
		     return puntosSQL;
		}
		else{
			return null;
		}
		
	}
    
    
}
