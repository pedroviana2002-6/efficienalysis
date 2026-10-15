package br.sistema.userinterface;

import java.io.UncheckedIOException;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import br.sistema.dados.GerenciaDados;

public class PainelCadastro extends Formulario implements AoExibir {
	private static final long serialVersionUID = 1L;
	private static final int TAMANHO_MINIMO_SENHA = 6;

	private final JTextField campoUsuario = new JTextField(15);
	private final JPasswordField campoSenha = new JPasswordField(15);
	private final JPasswordField campoConfirmacao = new JPasswordField(15);
	private final JButton botaoCadastrar = new JButton("Cadastrar");

	public PainelCadastro(GerenciaDados gd, Navegador nav) {
		JButton botaoIrLogin = new JButton("Já tenho conta");

		adicionar("Novo usuário:", campoUsuario);
		adicionar("Nova senha:", campoSenha);
		adicionar("Confirmar senha:", campoConfirmacao);
		adicionarLargo(botaoCadastrar);
		adicionarLargo(botaoIrLogin);

		botaoCadastrar.addActionListener(e -> {
			String nome = campoUsuario.getText().trim();
			String senha = new String(campoSenha.getPassword());
			String confirmacao = new String(campoConfirmacao.getPassword());

			if (nome.isEmpty() || senha.isEmpty()) {
				Ui.erro(this, "Por favor, preencha todos os campos.");
				return;
			}
			if (senha.length() < TAMANHO_MINIMO_SENHA) {
				Ui.erro(this, "A senha deve ter pelo menos " + TAMANHO_MINIMO_SENHA + " caracteres.");
				return;
			}
			if (!senha.equals(confirmacao)) {
				Ui.erro(this, "A confirmação não é igual à senha.");
				return;
			}

			try {
				if (gd.cadastrarUsuario(nome, senha)) {
					Ui.info(this, "Usuário cadastrado com sucesso!");
					nav.ir("login");
				} else {
					Ui.erro(this, "Já existe um usuário com esse nome.");
				}
			} catch (UncheckedIOException ex) {
				Ui.erroDeArquivo(this, ex);
			}
		});

		botaoIrLogin.addActionListener(e -> nav.ir("login"));
	}

	@Override
	public void aoExibir() {
		campoUsuario.setText("");
		campoSenha.setText("");
		campoConfirmacao.setText("");
		SwingUtilities.invokeLater(() -> {
			if (getRootPane() != null) getRootPane().setDefaultButton(botaoCadastrar);
			campoUsuario.requestFocusInWindow();
		});
	}
}
