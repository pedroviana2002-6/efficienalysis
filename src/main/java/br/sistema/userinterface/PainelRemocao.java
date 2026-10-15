package br.sistema.userinterface;

import java.io.UncheckedIOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import br.sistema.dados.GerenciaDados;
import br.sistema.dados.Relatorio;

public class PainelRemocao extends Formulario implements AoExibir {
	private static final long serialVersionUID = 1L;

	private final GerenciaDados gd;
	private final JComboBox<Relatorio> comboRelatorios = Ui.comboRelatorios();
	private final JLabel aviso = new JLabel("Nenhum relatório cadastrado.");
	private final JButton botaoRemover = new JButton("Remover");

	public PainelRemocao(GerenciaDados gd, Navegador nav) {
		this.gd = gd;
		JButton botaoVoltar = new JButton("Voltar");

		adicionar("Relatório:", comboRelatorios);
		adicionarLargo(aviso);
		adicionarLargo(botaoRemover);
		adicionarLargo(botaoVoltar);

		botaoRemover.addActionListener(e -> {
			Relatorio r = (Relatorio) comboRelatorios.getSelectedItem();
			if (r == null) return;
			if (!Ui.confirmar(this, "Remover o relatório de " + r + "? Esta ação não pode ser desfeita.")) return;

			try {
				gd.removerRelatorio(r.getMes(), r.getAno());
				Ui.info(this, "Relatório removido com sucesso.");
				nav.ir("opcoes");
			} catch (UncheckedIOException ex) {
				Ui.erroDeArquivo(this, ex);
			}
		});

		botaoVoltar.addActionListener(e -> nav.ir("opcoes"));
	}

	@Override
	public void aoExibir() {
		Ui.preencher(comboRelatorios, gd.getRelatorios());
		boolean vazio = comboRelatorios.getItemCount() == 0;
		aviso.setVisible(vazio);
		botaoRemover.setEnabled(!vazio);
	}
}
