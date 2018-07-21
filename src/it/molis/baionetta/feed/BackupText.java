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
	    if (titolo.length() > 70) {
	      titolo = titolo.substring(0, 70);
	    }
	    String testo = a.getTitolo() + "\n" + a.getMostrina() + "\n" + a.getPenna() +
	      "\n" + a.getLink() + "\n" + a.getData() + "\n" + feed.getTextBody(a);
	    try
	    {
	      Writer out = new OutputStreamWriter(new FileOutputStream(
	        "/home/fabio/Dropbox/La baionetta - munizioni/BaioBackupAutomatico/" + a.getPenna() + "/" + a.getData() + "-" + titolo), "UTF-8");

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
