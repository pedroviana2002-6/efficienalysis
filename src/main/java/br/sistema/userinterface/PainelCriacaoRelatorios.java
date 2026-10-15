package br.sistema.userinterface;

import java.io.UncheckedIOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import br.sistema.dados.GerenciaDados;
import br.sistema.dados.Relatorio;

public class PainelCriacaoRelatorios extends Formulario implements AoExibir {
	private static final long serialVersionUID = 1L;

	private final JComboBox<String> comboMes = Ui.comboMes();
	private final JSpinner spinnerAno = Ui.spinnerAno();
	private final JTextField campoEnergia = new JTextField(15);
	private final JTextField campoEstufa = new JTextField(15);
	private final JTextField campoLixo = new JTextField(15);

	public PainelCriacaoRelatorios(GerenciaDados gd, Navegador nav) {
		JButton botaoSalvar = new JButton("Salvar relatório");
		JButton botaoVoltar = new JButton("Voltar");

		adicionar("Mês:", comboMes);
		adicionar("Ano:", spinnerAno);
		adicionar("Gasto de energia (kWh):", campoEnergia);
		adicionar("Emissões de efeito estufa (tCO2):", campoEstufa);
		adicionar("Materiais não biodegradáveis (kg):", campoLixo);
		adicionarLargo(botaoSalvar);
		adicionarLargo(botaoVoltar);

		botaoSalvar.addActionListener(e -> {
			int mes = comboMes.getSelectedIndex() + 1;
			int ano = Ui.ano(spinnerAno);

			if (gd.buscarRelatorio(mes, ano).isPresent()) {
				Ui.erro(this, String.format("Já existe um relatório de %02d/%04d. Use \"Modificar relatório\" para alterá-lo.", mes, ano));
				return;
			}

			boolean temCampoVazio = campoEnergia.getText().isBlank()
					|| campoEstufa.getText().isBlank() || campoLixo.getText().isBlank();
			if (temCampoVazio && !Ui.confirmar(this, "Há campos vazios. Eles serão registrados como 0. Deseja continuar?")) {
				return;
			}

			double energia, estufa, lixo;
			try {
				energia = Ui.lerNumero(campoEnergia.getText(), "Gasto de energia");
				estufa = Ui.lerNumero(campoEstufa.getText(), "Emissões de efeito estufa");
				lixo = Ui.lerNumero(campoLixo.getText(), "Materiais não biodegradáveis");
			} catch (NumberFormatException ex) {
				Ui.erro(this, ex.getMessage());
				return; // mantém o que o usuário digitou
			}

			try {
				gd.adicionarRelatorio(new Relatorio(mes, ano, energia, estufa, lixo));
				Ui.info(this, "Relatório criado com sucesso!");
				nav.ir("opcoes");
			} catch (UncheckedIOException ex) {
				Ui.erroDeArquivo(this, ex);
			}
		});

		botaoVoltar.addActionListener(e -> nav.ir("opcoes"));
	}

	@Override
	public void aoExibir() {
		campoEnergia.setText("");
		campoEstufa.setText("");
		campoLixo.setText("");
	}
}
