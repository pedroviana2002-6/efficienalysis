package br.sistema.userinterface;

import java.awt.Component;
import java.io.UncheckedIOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import br.sistema.dados.Relatorio;


final class Ui {
	static final String[] MESES = {
		"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
		"Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
	};

	private Ui() {}

	static void info(Component pai, String mensagem) {
		JOptionPane.showMessageDialog(pai, mensagem, "Efficienalysis", JOptionPane.INFORMATION_MESSAGE);
	}

	static void erro(Component pai, String mensagem) {
		JOptionPane.showMessageDialog(pai, mensagem, "Efficienalysis", JOptionPane.ERROR_MESSAGE);
	}

	static void erroDeArquivo(Component pai, UncheckedIOException e) {
		erro(pai, "Não foi possível gravar os dados em disco:\n" + e.getCause().getMessage());
	}

	static boolean confirmar(Component pai, String mensagem) {
		return JOptionPane.showConfirmDialog(pai, mensagem, "Efficienalysis",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
	}

	static JComboBox<String> comboMes() {
		JComboBox<String> combo = new JComboBox<>(MESES);
		combo.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
		return combo;
	}

	static JSpinner spinnerAno() {
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(), 2000, 2100, 1));
		spinner.setEditor(new JSpinner.NumberEditor(spinner, "#")); // sem separador de milhar
		return spinner;
	}

	static int ano(JSpinner spinner) {
		try {
			spinner.commitEdit();
		} catch (ParseException e) {
		}
		return (Integer) spinner.getValue();
	}

	static JComboBox<Relatorio> comboRelatorios() {
		JComboBox<Relatorio> combo = new JComboBox<>();
		combo.setPrototypeDisplayValue(new Relatorio(12, 2100, 0, 0, 0));
		return combo;
	}

	static void preencher(JComboBox<Relatorio> combo, List<Relatorio> relatorios) {
		combo.removeAllItems();
		relatorios.forEach(combo::addItem);
	}

	static double lerNumero(String texto, String campo) {
		String t = texto.trim();
		if (t.isEmpty()) return 0;
		if (t.contains(",")) t = t.replace(".", "").replace(',', '.');                 
		else if (t.matches("\\d{1,3}(\\.\\d{3})+")) t = t.replace(".", "");              
		try {
			double valor = Double.parseDouble(t);
			if (Double.isNaN(valor) || Double.isInfinite(valor) || valor < 0) throw new NumberFormatException();
			return valor;
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Valor inválido em \"" + campo + "\": informe um número maior ou igual a zero.");
		}
	}
}
