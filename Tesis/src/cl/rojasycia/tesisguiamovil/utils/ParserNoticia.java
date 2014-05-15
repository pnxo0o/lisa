package cl.rojasycia.tesisguiamovil.utils;

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

import cl.rojasycia.tesisguiamovil.model.Noticia;
import android.content.Context;

public class ParserNoticia {
	
	private Context context;
	
	public ParserNoticia(Context context)
    {
        this.context = context;
    }
    
    public List<Noticia> getNoticiasGuardadas(){
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Noticia> listaNoticias = new ArrayList<Noticia>();
 
        try
        {
            //Creamos un nuevo parser DOM
            DocumentBuilder builder = factory.newDocumentBuilder();
            FileInputStream fil = context.openFileInput("noticias_descargadas.xml");
 
            //Realizamos lalectura completa del XML
            Document dom = builder.parse(fil);
 
            //Nos posicionamos en el nodo principal del árbol 
            Element root = dom.getDocumentElement();
 
            //Localizamos todos los elementos <item>
            NodeList items = root.getElementsByTagName("noticia");
 
            //Recorremos 
            for (int i=0; i<items.getLength(); i++)
            {
                Noticia noticia = new Noticia();
 
                //Obtenemos poi
                Node item = items.item(i);
 
                //Obtenemos la lista de datos de poi actual
                NodeList datosPoi = item.getChildNodes();
 
                //Procesamos cada dato 
                for (int j=0; j<datosPoi.getLength(); j++)
                {
                    Node dato = datosPoi.item(j);
                    String etiqueta = dato.getNodeName();
 
                    if (etiqueta.equals("titular"))
                    {
                        String texto = obtenerTexto(dato);
                        noticia.setTitularNoticia(texto);
                    }
                    else if (etiqueta.equals("fecha"))
                    {
                    	String texto = obtenerTexto(dato);
                    	noticia.setFechaNoticia(texto);
                    }
                    else if (etiqueta.equals("link"))
                    {
                    	String texto = obtenerTexto(dato);
                    	noticia.setLinkNoticia(texto);
                    }
                }
 
                listaNoticias.add(noticia);
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
 
        return listaNoticias;
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
    
    public ArrayList<Noticia> convertirListaAArreglo(List<Noticia> ls) {	
    	ArrayList<Noticia> x = new ArrayList<Noticia>();
    	if(ls.size() > 0){
			Iterator<Noticia> iterador = ls.listIterator();
			while( iterador.hasNext() ) {
				Noticia b = (Noticia) iterador.next();
				x.add(b);
			}
		}
    	return x;
    }

}
