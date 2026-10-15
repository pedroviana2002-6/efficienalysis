package br.sistema.dados;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class GerenciaDados {
	private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
	private final Path arquivoUsuarios;
	private final Path arquivoRelatorios;

	private final List<Usuario> contas;
	private final List<Relatorio> relatorios;

	public GerenciaDados() {
		this(pastaPadrao());
	}

	public GerenciaDados(Path pasta) {
		Path base = pasta.toAbsolutePath();
		this.arquivoUsuarios = base.resolve("usuarios.json");
		this.arquivoRelatorios = base.resolve("relatorios.json");
		this.contas = carregar(arquivoUsuarios, new TypeReference<List<Usuario>>() {});
		this.relatorios = carregar(arquivoRelatorios, new TypeReference<List<Relatorio>>() {});
		Collections.sort(relatorios);
	}

	private static Path pastaPadrao() {
		String configurada = System.getProperty("efficienalysis.dados");
		if (configurada != null && !configurada.isBlank()) return Path.of(configurada);
		return Path.of(System.getProperty("user.home"), ".efficienalysis");
	}

	// ---------- usuários ----------

	/** @return false se usuario ja existe */
	public boolean cadastrarUsuario(String nome, String senha) {
		if (buscarUsuario(nome).isPresent()) return false;
		contas.add(Usuario.novo(nome, senha));
		try {
			salvar(arquivoUsuarios, contas);
		} catch (UncheckedIOException e) {
			contas.remove(contas.size() - 1); 
			throw e;
		}
		return true;
	}

	public boolean autenticar(String nome, String senha) {
		return buscarUsuario(nome).map(u -> u.senhaConfere(senha)).orElse(false);
	}

	private Optional<Usuario> buscarUsuario(String nome) {
		return contas.stream().filter(u -> u.getNome().equalsIgnoreCase(nome)).findFirst();
	}

	// ---------- relatórios ----------

	public List<Relatorio> getRelatorios() {
		return Collections.unmodifiableList(relatorios);
	}

	public Optional<Relatorio> buscarRelatorio(int mes, int ano) {
		return relatorios.stream().filter(r -> r.getMes() == mes && r.getAno() == ano).findFirst();
	}

	/** @return false se relatorio ja existe */
	public boolean adicionarRelatorio(Relatorio relatorio) {
		if (relatorios.contains(relatorio)) return false;
		relatorios.add(relatorio);
		Collections.sort(relatorios);
		try {
			salvarRelatorios();
		} catch (UncheckedIOException e) {
			relatorios.remove(relatorio);
			throw e;
		}
		return true;
	}

	/** @return false se a busca de relatorio nao retornar nada */
	public boolean removerRelatorio(int mes, int ano) {
		Optional<Relatorio> alvo = buscarRelatorio(mes, ano);
		if (alvo.isEmpty()) return false;
		relatorios.remove(alvo.get());
		try {
			salvarRelatorios();
		} catch (UncheckedIOException e) {
			relatorios.add(alvo.get());
			Collections.sort(relatorios);
			throw e;
		}
		return true;
	}

	public void salvarRelatorios() {
		salvar(arquivoRelatorios, relatorios);
	}

	// ---------- persistência ----------

	private <T> List<T> carregar(Path arquivo, TypeReference<List<T>> tipo) {
		if (!Files.exists(arquivo)) return new ArrayList<>();
		try {
			return new ArrayList<>(mapper.readValue(arquivo.toFile(), tipo));
		} catch (IOException e) {
			Path backup = arquivo.resolveSibling(arquivo.getFileName() + ".corrompido");
			try {
				Files.move(arquivo, backup, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException ignorada) {
			}
			System.err.println("Não foi possível ler " + arquivo + " (" + e.getMessage()
					+ "). Uma cópia foi guardada em " + backup);
			return new ArrayList<>();
		}
	}

	private void salvar(Path arquivo, Object conteudo) {
		try {
			Files.createDirectories(arquivo.getParent());
			Path temporario = arquivo.resolveSibling(arquivo.getFileName() + ".tmp");
			mapper.writeValue(temporario.toFile(), conteudo);
			Files.move(temporario, arquivo, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new UncheckedIOException("Não foi possível salvar " + arquivo, e);
		}
	}
}
