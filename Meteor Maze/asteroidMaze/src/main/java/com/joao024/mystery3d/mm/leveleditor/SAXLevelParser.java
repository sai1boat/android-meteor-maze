package com.joao024.mystery3d.mm.leveleditor;

import android.util.Log;

import com.joao024.mystery3d.mm.game.Globals;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
 
public class SAXLevelParser {
	
	private InputStream is;
	static StringBuilder sb = new StringBuilder();
	static StringBuilder fileName = new StringBuilder();

	public SAXLevelParser(InputStream is){
		this.is = is;
	}
	
	
	public void onLevelParsed(){
		
	}
 
    public void parse() {
    	
    	sb.setLength(0);
    	fileName.setLength(0);
 
	    try {
	 
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
		 
			DefaultHandler handler = new DefaultHandler() {
		 
				boolean bfname = false;
				boolean blname = false;
				boolean bnname = false;
				boolean bsalary = false;
				
				boolean bmissleOrigin = false;
				boolean bearthOrigin  = false;
				boolean bisCameraFollowsMissle = false;
				boolean bfileName = false;
				boolean bcreatorId = false;
				boolean buuid = false;
				boolean bisBuiltIn = false;
				boolean bsatellite  = false; //x,y,uuid
				boolean bwall = false; //x,y,rotation,width,height,uuid
				boolean btrigger = false; //x,y,rotation,width,height,uuid,text,zoom,isCameraAttachedToMissle
				boolean basteroid = false; //x,y,releaseTime,uuid
				
				
				
			 
				public void startElement(String uri, String localName,String qName, 
			                Attributes attributes) throws SAXException {
			 
					
					/*
					 * Append every tag except mazes.
					 */
					if(!qName.equals("mazes")){
						sb.append("<");
						sb.append(qName);
						sb.append(">");
					}
					
					if(qName.equalsIgnoreCase("fileName")){
						bfileName = true;
					}
			 
				}
			 
				public void endElement(String uri, String localName,
					String qName) throws SAXException {
			 
					if(!qName.equals("mazes")){
						sb.append("</");
						sb.append(qName);
						sb.append(">");
					}
					
					if(qName.equalsIgnoreCase("root")){
						

						if(Globals.instance().isDebug()){
							Log.w("SB!!!",sb.toString());
							Log.w("SB!!!","--END OF FILE "+fileName.toString()+" -----------");
						}

						onLevelParsed();

						sb.setLength(0);
						fileName.setLength(0);
					}

			 
				}
			 
				public void characters(char ch[], int start, int length) throws SAXException {
					
					sb.append(ch, start, length);
					
					if(bfileName){
						fileName.append(ch,start,length);
						bfileName = false;
					}

				}
				   
		 
		    };
	 
	
	       
	       saxParser.parse(is, handler);
	 
	     } catch (Exception e) {
	       e.printStackTrace();
	     }
 
    }
 
}
