package br.sistema.userinterface;

import javax.swing.JFrame;

import br.sistema.dados.GerenciaDados;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public MainFrame(GerenciaDados gd) {
		setTitle("Efficienalysis");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Navegador nav = new Navegador();
		nav.registrar("login", new PainelLogin(gd, nav));
		nav.registrar("cadastro", new PainelCadastro(gd, nav));
		nav.registrar("opcoes", new PainelOpcoes(nav));
		nav.registrar("visualizacao", new PainelVisualizacao(gd, nav));
		nav.registrar("criar", new PainelCriacaoRelatorios(gd, nav));
		nav.registrar("modificar", new PainelModificacaoRelatorios(gd, nav));
		nav.registrar("comparar", new PainelComparacao(gd, nav));
		nav.registrar("remover", new PainelRemocao(gd, nav));

		setContentPane(nav.getContainer());
		pack();
		setLocationRelativeTo(null);
		nav.ir("login");
	}
}
