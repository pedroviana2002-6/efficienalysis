package br.sistema.dados;

import java.text.NumberFormat;
import java.util.Locale;


public final class Formato {
	private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

	private Formato() {}

	public static String numero(double valor) {
		NumberFormat nf = NumberFormat.getNumberInstance(PT_BR);
		nf.setMaximumFractionDigits(2);
		return nf.format(valor);
	}

	public static String numeroSimples(double valor) {
		NumberFormat nf = NumberFormat.getNumberInstance(PT_BR);
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(2);
		return nf.format(valor);
	}

	public static String percentual(double valor) {
		return String.format(PT_BR, "%+.2f%%", valor);
	}
}
