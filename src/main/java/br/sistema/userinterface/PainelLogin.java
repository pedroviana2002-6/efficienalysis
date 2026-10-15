package br.sistema.userinterface;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import br.sistema.dados.GerenciaDados;

public class PainelLogin extends Formulario implements AoExibir {
	private static final long serialVersionUID = 1L;

	private final JTextField campoUsuario = new JTextField(15);
	private final JPasswordField campoSenha = new JPasswordField(15);
	private final JButton botaoEntrar = new JButton("Entrar");

	public PainelLogin(GerenciaDados gd, Navegador nav) {
		JButton botaoIrCadastro = new JButton("Criar conta");

		adicionar("Usuário:", campoUsuario);
		adicionar("Senha:", campoSenha);
		adicionarLargo(botaoEntrar);
		adicionarLargo(botaoIrCadastro);

		botaoEntrar.addActionListener(e -> {
			String usuario = campoUsuario.getText().trim();
			String senha = new String(campoSenha.getPassword());

			if (gd.autenticar(usuario, senha)) {
				nav.ir("opcoes");
			} else {
				Ui.erro(this, "Usuário ou senha incorretos.");
				campoSenha.setText("");
				campoSenha.requestFocusInWindow();
			}
		});

		botaoIrCadastro.addActionListener(e -> nav.ir("cadastro"));
	}

	@Override
	public void aoExibir() {
		campoUsuario.setText("");
		campoSenha.setText("");
		SwingUtilities.invokeLater(() -> {
			if (getRootPane() != null) getRootPane().setDefaultButton(botaoEntrar); 
			campoUsuario.requestFocusInWindow();
		});
	}
}
