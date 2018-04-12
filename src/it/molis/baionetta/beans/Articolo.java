package it.molis.baionetta.beans;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Articolo implements Comparable<Articolo> {

	private String titolo;
	private Mostrina mostrina;
	private Penna penna;
	private String link;
	private LocalDate data;
	private Set<ParolaChiave> paroleChiave;

	public Articolo(String titolo, Mostrina mostrina, Penna penna, String link, LocalDate data) {
		super();
		this.titolo = titolo;
		this.mostrina = mostrina;
		this.penna = penna;
		this.link = link;
		this.data = data;
		this.paroleChiave = new HashSet<>();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Articolo other = (Articolo) obj;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		return result;
	}

	public LocalDate getData() {
		return this.data;
	}

	public String getLink() {
		return link;
	}

	public Mostrina getMostrina() {
		return mostrina;
	}

	public Penna getPenna() {
		return penna;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setMostrina(Mostrina mostrina) {
		this.mostrina = mostrina;
	}

	public void setPenna(Penna penna) {
		this.penna = penna;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	@Override
	public int compareTo(Articolo o) {
		return -getData().compareTo(o.getData());
	}

	@Override
	public String toString() {
		return "" + titolo + "\n" + penna.getNome() + "\n"
				+ data.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ITALY) + " " + data.getDayOfMonth() + " - "
				+ data.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALY) + " - " + data.getYear();
	}

	public Set<ParolaChiave> getParoleChiave() {
		return paroleChiave;
	}

	public void setParoleChiave(Set<ParolaChiave> paroleChiave) {
		this.paroleChiave = paroleChiave;
	}

	public void addParolaChiave(ParolaChiave pc) {
		this.paroleChiave.add(pc);
	}
}
