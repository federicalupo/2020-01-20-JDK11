package it.polito.tdp.artsmia.model;

public class Arco implements Comparable<Arco> {
	private Artista a1;
	private Artista a2;
	private int peso;
	
	public Arco(Artista a1, Artista a2, int peso) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.peso = peso;
	}

	public Artista getA1() {
		return a1;
	}

	public Artista getA2() {
		return a2;
	}

	public int getPeso() {
		return peso;
	}

	@Override
	public String toString() {
		return a1 + "  " + a2 + " " + peso;
	}

	@Override
	public int compareTo(Arco o) {
		return -(this.peso-o.getPeso());
	}
	
	
}
