package br.sistema.userinterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Painel com linhas "rótulo + campo" e botões em largura total, centralizado na tela. */
public class Formulario extends JPanel {
	private static final long serialVersionUID = 1L;

	private final GridBagConstraints gbc = new GridBagConstraints();
	private int linha = 0;

	public Formulario() {
		super(new GridBagLayout());
		gbc.insets = new Insets(5, 5, 5, 5);
	}

	public void adicionar(String rotulo, JComponent campo) {
		gbc.gridy = linha++;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		add(new JLabel(rotulo), gbc);
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		add(campo, gbc);
	}

	/** Componente ocupando as duas colunas (botões, avisos, textos). */
	public void adicionarLargo(JComponent componente) {
		gbc.gridy = linha++;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		add(componente, gbc);
	}
}
