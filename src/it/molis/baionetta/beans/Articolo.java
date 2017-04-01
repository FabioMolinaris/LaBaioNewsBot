package it.molis.baionetta.beans;

import java.time.LocalDateTime;

public class Articolo implements Comparable<Articolo>{

	private String titolo;
	private String mostrina;
	private String penna;
	private String link;
	private LocalDateTime data;

	public Articolo(String titolo, String mostrina, String penna, String link, LocalDateTime data) {
		super();
		this.titolo = titolo;
		this.mostrina = mostrina;
		this.penna = penna;
		this.link = link;
		this.data = data;
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

	public LocalDateTime getData() {
		return this.data;
	}

	public String getLink() {
		return link;
	}

	public String getMostrina() {
		return mostrina;
	}

	public String getPenna() {
		return penna;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setMostrina(String mostrina) {
		this.mostrina = mostrina;
	}

	public void setPenna(String penna) {
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
		return "" + titolo + "\n" + penna + "\n" + data;
	}


}
