package it.molis.baionetta.feed;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import it.molis.baionetta.beans.Articolo;

public class BackupText {

	public BackupText() {
		super();
	}

	public void backupText(Articolo a) {
		ArticoloFeed feed = new ArticoloFeed();

		String titolo = a.getTitolo().replace("/", " ");
		titolo = titolo.replace(" ", "_");
		titolo = titolo.replace("ì", "i");
		titolo = titolo.replace("ò", "o");
		titolo = titolo.replace("à", "a");
		titolo = titolo.replace("á", "a");
		titolo = titolo.replace("ù", "u");
		titolo = titolo.replace("è", "e");
		titolo = titolo.replace("é", "e");
		titolo = titolo.replace("È", "E");
		titolo = titolo.replace("'", "");
		titolo = titolo.replace("\"", "");
		titolo = titolo.replace("«", "");
		titolo = titolo.replace("»", "");
		titolo = titolo.replace("“", "");
		titolo = titolo.replace("”", "");
		titolo = titolo.replace("–", "-");
		titolo = titolo.replace("’", "");
		
	    if (titolo.length() > 50) 
	    	titolo = titolo.substring(0, 50);
	    
	    String testo = a.getTitolo() + "\n" + a.getMostrina() + "\n" + a.getPenna() +
	      "\n" + a.getLink() + "\n" + a.getData() + "\n" + feed.getTextBody(a);
	    try
	    {
	      Writer out = new OutputStreamWriter(new FileOutputStream(
	        //"/home/fabio/Dropbox/La baionetta - munizioni/BaioBackupAutomatico/" + a.getPenna() + "/" + a.getData() + "-" + titolo), "UTF-8");
	          "/volume1/homes/fabio/Drive/Dropbox/La baionetta - munizioni/BaioBackupAutomatico/" 
	        		+ a.getPenna() + "/" + a.getData() + "-" + titolo + ".txt"), "UTF-8");
	      System.out.println(titolo);
	      out.write(testo);
	      out.close();
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	}
}
