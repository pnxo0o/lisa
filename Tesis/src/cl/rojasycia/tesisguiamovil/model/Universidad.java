package cl.rojasycia.tesisguiamovil.model;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cl.rojasycia.tesisguiamovil.R;
import android.content.Context;

public class Universidad {
	
	public static final int UPLA = 1;
	public static final int UV = 2;
	public static final int USM = 3;
	public static final int PUCV = 4;
	
	private String nombreUniversidad;
	private String descripcionUniversidad;
	private String urlUniversidad;
	private String feedUniversidad;
	private int imagenUniversidad;
	private int logoUniversidad;
	
	public Universidad(int universidad, Context context) {
		super();
		
		try{
        	InputStream is = null;
        	NodeList titulo, descripcion, url, feed;
        	Node item_titulo, item_descripcion, item_url, item_feed;
        	
        	switch (universidad){
            case UPLA:
            	is = context.getResources().openRawResource(R.raw.datos_upla);
            	setImagenUniversidad(R.drawable.wall_upla);
            	setLogoUniversidad(R.drawable.u_upla);
            	break;
            case UV:
            	is = context.getResources().openRawResource(R.raw.datos_uv);
            	setImagenUniversidad(R.drawable.wall_uv);
            	setLogoUniversidad(R.drawable.u_uv);
            	break;
            case USM:
            	is = context.getResources().openRawResource(R.raw.datos_usm);
            	setImagenUniversidad(R.drawable.wall_usm);
            	setLogoUniversidad(R.drawable.u_usm);
            	break;
            case PUCV:
            	is = context.getResources().openRawResource(R.raw.datos_pucv);
            	setImagenUniversidad(R.drawable.wall_pucv);
            	setLogoUniversidad(R.drawable.u_pucv);
            	break;
            }

        	//DOM 
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(is);

            //A partir de aquí se trataría el árbol DOM como siempre.
            Element root = dom.getDocumentElement();
            
            titulo = root.getElementsByTagName("titulo");
            descripcion = root.getElementsByTagName("descripcion");
            url = root.getElementsByTagName("url");
            feed = root.getElementsByTagName("feed");
            
            item_titulo = titulo.item(0);
            item_descripcion=descripcion.item(0);
            item_url=url.item(0);
            item_feed=feed.item(0);
            
            setNombreUniversidad(obtenerTextoDeNodo(item_titulo));
            setDescripcion(obtenerTextoDeNodo(item_descripcion));
            setUrl(obtenerTextoDeNodo(item_url));
            setFeedUniversidad(obtenerTextoDeNodo(item_feed));
            
        }
        catch (Exception ex)
        {

        }
			
	}
	
	public String getNombreUniversidad() {
		return nombreUniversidad;
	}
	
	public void setNombreUniversidad(String nombreUniversidad) {
		this.nombreUniversidad = nombreUniversidad;
	}
	
	public String getDescripcion() {
		return descripcionUniversidad;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcionUniversidad = descripcion;
	}
	
	public String getUrl() {
		return urlUniversidad;
	}
	
	public void setUrl(String url) {
		this.urlUniversidad = url;
	}
	
	public int getImagenUniversidad() {
		return imagenUniversidad;
	}
	
	public void setImagenUniversidad(int imagenUniversidad) {
		this.imagenUniversidad = imagenUniversidad;
	}
	
	public int getLogoUniversidad() {
		return logoUniversidad;
	}

	public void setLogoUniversidad(int logoUniversidad) {
		this.logoUniversidad = logoUniversidad;
	}

	public String getFeedUniversidad() {
		return feedUniversidad;
	}

	public void setFeedUniversidad(String feedUniversidad) {
		this.feedUniversidad = feedUniversidad;
	}
	
	public static void descargarNoticiasDeUniversidad(String feed){
		
	}

	private String obtenerTextoDeNodo(Node dato)
    {
            StringBuilder texto = new StringBuilder();
            NodeList fragmentos = dato.getChildNodes();
            
            for (int k=0;k<fragmentos.getLength();k++)
            {
                texto.append(fragmentos.item(k).getNodeValue());
            }
            
            return texto.toString();
    }
	

}
