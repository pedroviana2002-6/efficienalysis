package br.sistema.applauncher;

import javax.swing.SwingUtilities;

import br.sistema.dados.GerenciaDados;
import br.sistema.userinterface.MainFrame;

public class AppLauncher {
	public static void main(String[] args) {
		GerenciaDados gd = new GerenciaDados(); // já carrega usuários e relatórios
		SwingUtilities.invokeLater(() -> new MainFrame(gd).setVisible(true));
	}
}
