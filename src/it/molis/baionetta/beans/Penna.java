package it.molis.baionetta.beans;

import java.util.HashSet;
import java.util.Set;

public class Penna {

	private String nome;
	private Set<Articolo> articoli;

	public Penna(String nome) {
		super();
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Set<Articolo> getAllArticoli() {
		return articoli;
	}

	public void setArticoli(Set<Articolo> art) {
		articoli = new HashSet<>(art);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Penna other = (Penna) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.nome;
	}

}