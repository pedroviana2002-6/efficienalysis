package br.sistema.dados;

import java.util.Objects;

public class Usuario {
	private String nome;
	private String salt;
	private String senhaHash;

	public Usuario() {} 

	public static Usuario novo(String nome, String senha) {
		Usuario u = new Usuario();
		u.nome = nome;
		u.salt = Senha.gerarSalt();
		u.senhaHash = Senha.hash(senha, u.salt);
		return u;
	}

	public boolean senhaConfere(String senha) {
		return Senha.confere(senha, salt, senhaHash);
	}

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getSalt() { return salt; }
	public void setSalt(String salt) { this.salt = salt; }

	public String getSenhaHash() { return senhaHash; }
	public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return Objects.equals(nome, ((Usuario) o).nome);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome);
	}

	@Override
	public String toString() {
		return "Usuario{nome='" + nome + "'}"; 
	}
}
