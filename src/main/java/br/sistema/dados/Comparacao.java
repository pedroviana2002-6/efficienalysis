package br.sistema.dados;


public class Comparacao {
	private final Relatorio inicial;
	private final Relatorio fim;

	public Comparacao(Relatorio inicial, Relatorio fim) {
		this.inicial = inicial;
		this.fim = fim;
	}

	static double variacao(double antes, double depois) {
		if (antes == 0) return depois == 0 ? 0 : Double.NaN;
		return (depois - antes) / antes * 100.0;
	}

	public double variacaoEnergia() { return variacao(inicial.getEnergia(), fim.getEnergia()); }
	public double variacaoEmissoes() { return variacao(inicial.getEmissoes(), fim.getEmissoes()); }
	public double variacaoResiduos() { return variacao(inicial.getResiduos(), fim.getResiduos()); }

	private static String veredito(double variacao) {
		if (Double.isNaN(variacao)) return "sem base de comparação (valor inicial 0)";
		if (variacao < 0) return "eficiência ganha";
		if (variacao > 0) return "eficiência perdida";
		return "sem alteração";
	}

	private static String linha(String nome, double antes, double depois, String unidade, double variacao) {
		String pct = Double.isNaN(variacao) ? "--" : Formato.percentual(variacao);
		return nome + ": " + Formato.numero(antes) + " " + unidade + " -> "
				+ Formato.numero(depois) + " " + unidade + "  (" + pct + ")  " + veredito(variacao);
	}

	public String comoTexto() {
		return "Comparação de relatórios: " + inicial + " -> " + fim + "\n\n"
				+ linha("Energia", inicial.getEnergia(), fim.getEnergia(), "kWh", variacaoEnergia()) + "\n"
				+ linha("Emissões", inicial.getEmissoes(), fim.getEmissoes(), "tCO2", variacaoEmissoes()) + "\n"
				+ linha("Resíduos", inicial.getResiduos(), fim.getResiduos(), "kg", variacaoResiduos()) + "\n";
	}

	public Relatorio getInicial() { return inicial; }
	public Relatorio getFim() { return fim; }
}
