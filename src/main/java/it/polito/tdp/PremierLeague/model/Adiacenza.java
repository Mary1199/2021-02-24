package it.polito.tdp.PremierLeague.model;

public class Adiacenza {
	private Player v1;
	private Player v2;
	private double peso;
	
	public Adiacenza(Player v1, Player v2, double peso) {
		super();
		this.v1 = v1;
		this.v2 = v2;
		this.peso = peso;
	}
	public Player getV1() {
		return v1;
	}
	public void setV1(Player v1) {
		this.v1 = v1;
	}
	public Player getV2() {
		return v2;
	}
	public void setV2(Player v2) {
		this.v2 = v2;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "Adiacenza [v1=" + v1 + ", v2=" + v2 + ", peso=" + peso + "]";
	}


}
