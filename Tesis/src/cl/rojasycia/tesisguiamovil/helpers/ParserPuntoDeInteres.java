package cl.rojasycia.tesisguiamovil.helpers;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cl.rojasycia.tesisguiamovil.R;
import cl.rojasycia.tesisguiamovil.model.PuntoDeInteres;
import cl.rojasycia.tesisguiamovil.utils.POISQLiteHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ParserPuntoDeInteres {

    private Context context;

	public ParserPuntoDeInteres(Context context)
    {
        this.context = context;
    }
    
    public List<PuntoDeInteres> getPOI(){
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<PuntoDeInteres> listaPuntos = new ArrayList<PuntoDeInteres>();
 
        try
        {
            //Creamos un nuevo parser DOM
            DocumentBuilder builder = factory.newDocumentBuilder();
            FileInputStream fil = context.openFileInput("poi_descargados.xml");
 
            //Realizamos la lectura completa del XML
            Document dom = builder.parse(fil);
 
            //Nos posicionamos en el nodo principal del árbol 
            Element root = dom.getDocumentElement();
 
            //Localizamos todos los elementos <item>
            NodeList items = root.getElementsByTagName("poi");
 
            //Recorremos 
            for (int i=0; i<items.getLength(); i++)
            {
                PuntoDeInteres poi = new PuntoDeInteres();
 
                //Obtenemos poi
                Node item = items.item(i);
 
                //Obtenemos la lista de datos de poi actual
                NodeList datosPoi = item.getChildNodes();
 
                //Procesamos cada dato 
                for (int j=0; j<datosPoi.getLength(); j++)
                {
                    Node dato = datosPoi.item(j);
                    String etiqueta = dato.getNodeName();
 
                    if (etiqueta.equals("nombre"))
                    {
                        String texto = obtenerTexto(dato);
                        poi.setNombrePOI(texto);;
                    }
                    else if (etiqueta.equals("tipo"))
                    {
                    	String texto = obtenerTexto(dato);
                    	poi.setTipoPOI(texto);
                    }
                    else if (etiqueta.equals("latitud"))
                    {
                    	String texto = obtenerTexto(dato);
                    	poi.setLatitudPOI(Double.parseDouble(texto));;
                    }
                    else if (etiqueta.equals("longitud"))
                    {
                    	String texto = obtenerTexto(dato);
                    	poi.setLongitudPOI(Double.parseDouble(texto));;
                    }
                }
 
                listaPuntos.add(poi);
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
 
        return listaPuntos;
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
				Cursor c = db.rawQuery("SELECT latitudPOI, longitudPOI FROM PuntoDeInteres", null);
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
}
