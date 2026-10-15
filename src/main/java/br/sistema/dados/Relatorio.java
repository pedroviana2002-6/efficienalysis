package br.sistema.dados;

import java.util.Objects;


public class Relatorio extends Dados implements Comparable<Relatorio> {
	private int mes; 
	private int ano;

	public Relatorio() {}

	public Relatorio(int mes, int ano, double energia, double emissoes, double residuos) {
		this.mes = mes;
		this.ano = ano;
		setEnergia(energia);
		setEmissoes(emissoes);
		setResiduos(residuos);
	}

	public int getMes() { return mes; }
	public void setMes(int mes) { this.mes = mes; }

	public int getAno() { return ano; }
	public void setAno(int ano) { this.ano = ano; }

	@Override
	public int compareTo(Relatorio outro) {
		int porAno = Integer.compare(ano, outro.ano);
		return porAno != 0 ? porAno : Integer.compare(mes, outro.mes);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Relatorio that = (Relatorio) o;
		return mes == that.mes && ano == that.ano;
	}

	@Override
	public int hashCode() {
		return Objects.hash(mes, ano);
	}

	@Override
	public String toString() {
		return String.format("%02d/%04d", mes, ano);
	}
}
