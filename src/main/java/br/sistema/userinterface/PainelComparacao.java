package br.sistema.userinterface;

import java.awt.CardLayout;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import br.sistema.dados.Comparacao;
import br.sistema.dados.GerenciaDados;
import br.sistema.dados.Relatorio;

/** RF7/RF8: compara dois meses (variação % de eficiência) e permite salvar o relatório da comparação. */
public class PainelComparacao extends JPanel implements AoExibir {
	private static final long serialVersionUID = 1L;

	private final GerenciaDados gd;
	private final CardLayout cartas = new CardLayout();
	private final JComboBox<Relatorio> comboInicial = Ui.comboRelatorios();
	private final JComboBox<Relatorio> comboFinal = Ui.comboRelatorios();
	private final JLabel aviso = new JLabel("São necessários pelo menos dois relatórios para comparar.");
	private final JButton botaoComparar = new JButton("Comparar");
	private final JTextArea areaResultado = new JTextArea(6, 55);

	private Comparacao atual;

	public PainelComparacao(GerenciaDados gd, Navegador nav) {
		this.gd = gd;
		setLayout(cartas);

		Formulario busca = new Formulario();
		JButton botaoVoltarBusca = new JButton("Voltar");
		busca.adicionar("Mês inicial:", comboInicial);
		busca.adicionar("Mês final:", comboFinal);
		busca.adicionarLargo(aviso);
		busca.adicionarLargo(botaoComparar);
		busca.adicionarLargo(botaoVoltarBusca);

		areaResultado.setEditable(false);
		areaResultado.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		JButton botaoExportar = new JButton("Salvar relatório (.txt)");
		JButton botaoOutra = new JButton("Nova comparação");
		JButton botaoVoltar = new JButton("Voltar ao menu");

		Formulario resultado = new Formulario();
		resultado.adicionarLargo(new JLabel("Variação de cada indicador do mês inicial para o final:"));
		resultado.adicionarLargo(areaResultado);
		resultado.adicionarLargo(botaoExportar);
		resultado.adicionarLargo(botaoOutra);
		resultado.adicionarLargo(botaoVoltar);

		add(busca, "busca");
		add(resultado, "resultado");

		botaoComparar.addActionListener(e -> {
			Relatorio inicial = (Relatorio) comboInicial.getSelectedItem();
			Relatorio fim = (Relatorio) comboFinal.getSelectedItem();
			if (inicial == null || fim == null) return;
			if (inicial.equals(fim)) {
				Ui.erro(this, "Escolha dois meses diferentes.");
				return;
			}
			atual = new Comparacao(inicial, fim);
			areaResultado.setText(atual.comoTexto());
			cartas.show(this, "resultado");
		});

		botaoExportar.addActionListener(e -> exportar());
		botaoOutra.addActionListener(e -> cartas.show(this, "busca"));
		botaoVoltar.addActionListener(e -> nav.ir("opcoes"));
		botaoVoltarBusca.addActionListener(e -> nav.ir("opcoes"));
	}

	private void exportar() {
		JFileChooser escolha = new JFileChooser();
		escolha.setSelectedFile(new File(String.format("comparacao_%02d-%04d_vs_%02d-%04d.txt",
				atual.getInicial().getMes(), atual.getInicial().getAno(),
				atual.getFim().getMes(), atual.getFim().getAno())));
		if (escolha.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

		File destino = escolha.getSelectedFile();
		if (destino.exists() && !Ui.confirmar(this, "O arquivo já existe. Deseja substituí-lo?")) return;
		try {
			Files.writeString(destino.toPath(), atual.comoTexto(), StandardCharsets.UTF_8);
			Ui.info(this, "Relatório salvo em:\n" + destino.getAbsolutePath());
		} catch (IOException ex) {
			Ui.erro(this, "Não foi possível salvar o arquivo:\n" + ex.getMessage());
		}
	}

	@Override
	public void aoExibir() {
		atual = null;
		Ui.preencher(comboInicial, gd.getRelatorios());
		Ui.preencher(comboFinal, gd.getRelatorios());
		if (comboFinal.getItemCount() > 1) comboFinal.setSelectedIndex(comboFinal.getItemCount() - 1);
		boolean poucos = gd.getRelatorios().size() < 2; // UC05: precisa de dois meses
		aviso.setVisible(poucos);
		botaoComparar.setEnabled(!poucos);
		cartas.show(this, "busca");
	}
}
