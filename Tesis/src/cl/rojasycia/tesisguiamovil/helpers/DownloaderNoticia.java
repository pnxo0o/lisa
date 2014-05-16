package cl.rojasycia.tesisguiamovil.helpers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import cl.rojasycia.tesisguiamovil.model.Noticia;

public class DownloaderNoticia {
	
	private ArrayList<Noticia> noticias;
	private URL url;
	
	public void setUrl(URL url) {
		this.url = url;
	}

	public DownloaderNoticia (String urlFuente) {
		try {
			this.setUrl(new URL(urlFuente));
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Noticia> descargarNoticias(){
		try {
			noticias = new ArrayList<Noticia>();
			
			XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
			XmlPullParser parser = parserCreator.newPullParser();
			parser.setInput(url.openStream(), null);
			int parserEvent = parser.getEventType();
			
			while (parserEvent != XmlPullParser.END_DOCUMENT) {
				switch (parserEvent) {
				
				case XmlPullParser.START_TAG:
					String tag = parser.getName();
					
					if (tag.equalsIgnoreCase("item")) {
						Noticia noticia = new Noticia();
						parserEvent = parser.next();
						boolean itemClosed = false;
						
						while (parserEvent != XmlPullParser.END_DOCUMENT && !itemClosed) {
							switch (parserEvent) {
							
							case XmlPullParser.START_TAG:
								tag = parser.getName();
								if (tag.equalsIgnoreCase("title")) {
									noticia.setTitularNoticia(parser.nextText());;
								}
								if (tag.equalsIgnoreCase("pubdate")) {
									noticia.setFechaNoticia(parser.nextText());;
								}
								if (tag.equalsIgnoreCase("link")) {
									noticia.setLinkNoticia(parser.nextText());;
								}
								break;
								
							case XmlPullParser.END_TAG:
								tag = parser.getName();
								if(tag.equalsIgnoreCase("item")){
									itemClosed = true;
									noticias.add(noticia);
								}
								break;
							}
							parserEvent = parser.next();
						}
					}
					break;
				}
				parserEvent = parser.next();
			}
		} catch (Exception e) {
			//
		}
		return noticias;
	}

	

}
