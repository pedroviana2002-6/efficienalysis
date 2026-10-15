package br.sistema.userinterface;

import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

/** Controla a troca de telas e avisa cada painel quando ele vai ser exibido. */
public class Navegador {
	private final CardLayout layout = new CardLayout();
	private final JPanel container = new JPanel(layout);
	private final Map<String, JPanel> telas = new HashMap<>();

	public JPanel getContainer() {
		return container;
	}

	public void registrar(String nome, JPanel painel) {
		telas.put(nome, painel);
		container.add(painel, nome);
	}

	public void ir(String nome) {
		JPanel tela = telas.get(nome);
		if (tela instanceof AoExibir pronta) pronta.aoExibir();
		layout.show(container, nome);
	}
}
