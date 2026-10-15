package br.sistema.userinterface;

import java.awt.CardLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.sistema.dados.Formato;
import br.sistema.dados.GerenciaDados;
import br.sistema.dados.Relatorio;

public class PainelVisualizacao extends JPanel implements AoExibir {
	private static final long serialVersionUID = 1L;

	private final GerenciaDados gd;
	private final CardLayout cartas = new CardLayout();
	private final JComboBox<Relatorio> comboRelatorios = Ui.comboRelatorios();
	private final JLabel aviso = new JLabel("Nenhum relatório cadastrado.");
	private final JButton botaoVer = new JButton("Ver");

	public PainelVisualizacao(GerenciaDados gd, Navegador nav) {
		this.gd = gd;
		setLayout(cartas);

		Formulario busca = new Formulario();
		JButton botaoVoltarBusca = new JButton("Voltar");
		busca.adicionar("Relatório:", comboRelatorios);
		busca.adicionarLargo(aviso);
		busca.adicionarLargo(botaoVer);
		busca.adicionarLargo(botaoVoltarBusca);

		JLabel periodoLabel = new JLabel();
		JLabel energiaLabel = new JLabel();
		JLabel estufaLabel = new JLabel();
		JLabel lixoLabel = new JLabel();
		JButton botaoOutro = new JButton("Ver outro relatório");
		JButton botaoVoltar = new JButton("Voltar ao menu");

		Formulario resultado = new Formulario();
		resultado.adicionar("Período:", periodoLabel);
		resultado.adicionar("Energia:", energiaLabel);
		resultado.adicionar("Emissões:", estufaLabel);
		resultado.adicionar("Não biodegradáveis:", lixoLabel);
		resultado.adicionarLargo(botaoOutro);
		resultado.adicionarLargo(botaoVoltar);

		add(busca, "busca");
		add(resultado, "resultado");

		botaoVer.addActionListener(e -> {
			Relatorio r = (Relatorio) comboRelatorios.getSelectedItem();
			if (r == null) return;
			periodoLabel.setText(Ui.MESES[r.getMes() - 1] + " de " + r.getAno());
			energiaLabel.setText(Formato.numero(r.getEnergia()) + " kWh");
			estufaLabel.setText(Formato.numero(r.getEmissoes()) + " tCO2");
			lixoLabel.setText(Formato.numero(r.getResiduos()) + " kg");
			cartas.show(this, "resultado");
		});

		botaoOutro.addActionListener(e -> cartas.show(this, "busca"));
		botaoVoltar.addActionListener(e -> nav.ir("opcoes"));
		botaoVoltarBusca.addActionListener(e -> nav.ir("opcoes"));
	}

	@Override
	public void aoExibir() {
		Ui.preencher(comboRelatorios, gd.getRelatorios());
		boolean vazio = comboRelatorios.getItemCount() == 0;
		aviso.setVisible(vazio);
		botaoVer.setEnabled(!vazio);
		cartas.show(this, "busca");
	}
}
