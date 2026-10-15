package br.sistema.userinterface;

import java.awt.CardLayout;
import java.io.UncheckedIOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.sistema.dados.Formato;
import br.sistema.dados.GerenciaDados;
import br.sistema.dados.Relatorio;

public class PainelModificacaoRelatorios extends JPanel implements AoExibir {
	private static final long serialVersionUID = 1L;

	private final GerenciaDados gd;
	private final CardLayout cartas = new CardLayout();
	private final JComboBox<Relatorio> comboRelatorios = Ui.comboRelatorios();
	private final JLabel aviso = new JLabel("Nenhum relatório cadastrado.");
	private final JButton botaoEditar = new JButton("Editar");
	private final JLabel periodoLabel = new JLabel();
	private final JTextField campoEnergia = new JTextField(15);
	private final JTextField campoEstufa = new JTextField(15);
	private final JTextField campoLixo = new JTextField(15);

	private Relatorio emEdicao; 

	public PainelModificacaoRelatorios(GerenciaDados gd, Navegador nav) {
		this.gd = gd;
		setLayout(cartas);

		Formulario busca = new Formulario();
		JButton botaoVoltarBusca = new JButton("Voltar");
		busca.adicionar("Relatório:", comboRelatorios);
		busca.adicionarLargo(aviso);
		busca.adicionarLargo(botaoEditar);
		busca.adicionarLargo(botaoVoltarBusca);

		Formulario edicao = new Formulario();
		JButton botaoSalvar = new JButton("Salvar");
		JButton botaoCancelar = new JButton("Cancelar");
		edicao.adicionar("Período:", periodoLabel);
		edicao.adicionar("Energia (kWh):", campoEnergia);
		edicao.adicionar("Emissões (tCO2):", campoEstufa);
		edicao.adicionar("Não biodegradáveis (kg):", campoLixo);
		edicao.adicionarLargo(botaoSalvar);
		edicao.adicionarLargo(botaoCancelar);

		add(busca, "busca");
		add(edicao, "edicao");

		botaoEditar.addActionListener(e -> {
			emEdicao = (Relatorio) comboRelatorios.getSelectedItem();
			if (emEdicao == null) return;
			periodoLabel.setText(Ui.MESES[emEdicao.getMes() - 1] + " de " + emEdicao.getAno());
			campoEnergia.setText(Formato.numeroSimples(emEdicao.getEnergia()));
			campoEstufa.setText(Formato.numeroSimples(emEdicao.getEmissoes()));
			campoLixo.setText(Formato.numeroSimples(emEdicao.getResiduos()));
			cartas.show(this, "edicao");
		});

		botaoSalvar.addActionListener(e -> {
			double energia, estufa, lixo;
			try {
				energia = Ui.lerNumero(campoEnergia.getText(), "Energia");
				estufa = Ui.lerNumero(campoEstufa.getText(), "Emissões");
				lixo = Ui.lerNumero(campoLixo.getText(), "Não biodegradáveis");
			} catch (NumberFormatException ex) {
				Ui.erro(this, ex.getMessage());
				return;
			}

			double energiaAntiga = emEdicao.getEnergia();
			double estufaAntiga = emEdicao.getEmissoes();
			double lixoAntigo = emEdicao.getResiduos();
			emEdicao.setEnergia(energia);
			emEdicao.setEmissoes(estufa);
			emEdicao.setResiduos(lixo);
			try {
				gd.salvarRelatorios();
				Ui.info(this, "Relatório modificado com sucesso.");
				nav.ir("opcoes");
			} catch (UncheckedIOException ex) {
				emEdicao.setEnergia(energiaAntiga); 
				emEdicao.setEmissoes(estufaAntiga);
				emEdicao.setResiduos(lixoAntigo);
				Ui.erroDeArquivo(this, ex);
			}
		});

		botaoCancelar.addActionListener(e -> cartas.show(this, "busca"));
		botaoVoltarBusca.addActionListener(e -> nav.ir("opcoes"));
	}

	@Override
	public void aoExibir() {
		emEdicao = null;
		Ui.preencher(comboRelatorios, gd.getRelatorios());
		boolean vazio = comboRelatorios.getItemCount() == 0;
		aviso.setVisible(vazio);
		botaoEditar.setEnabled(!vazio);
		cartas.show(this, "busca");
	}
}
