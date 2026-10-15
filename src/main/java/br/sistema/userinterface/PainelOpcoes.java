package br.sistema.userinterface;

import javax.swing.JButton;

public class PainelOpcoes extends Formulario {
	private static final long serialVersionUID = 1L;

	public PainelOpcoes(Navegador nav) {
		adicionarBotao("Visualizar relatório existente", () -> nav.ir("visualizacao"));
		adicionarBotao("Criar relatório", () -> nav.ir("criar"));
		adicionarBotao("Modificar relatório", () -> nav.ir("modificar"));
		adicionarBotao("Comparar relatórios de meses diferentes", () -> nav.ir("comparar"));
		adicionarBotao("Deletar um relatório", () -> nav.ir("remover"));
		adicionarBotao("Sair (voltar ao login)", () -> nav.ir("login"));
	}

	private void adicionarBotao(String texto, Runnable acao) {
		JButton botao = new JButton(texto);
		botao.addActionListener(e -> acao.run());
		adicionarLargo(botao);
	}
}
