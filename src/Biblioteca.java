import java.io.*;
import java.util.ArrayList;

/**
 * Classe principal que gerencia o acervo da biblioteca.
 * Contém coleções de livros e pessoas (ArrayList).
 * Responsável pela persistência e leitura de arquivos.
 */
public class Biblioteca implements Serializable {

    private static final long serialVersionUID = 5L;

    private String nome;
    private ArrayList<Livro> livros;
    private ArrayList<Pessoa> pessoas;

    private static final String ARQUIVO_DAT = "dados/biblioteca.dat";
    private static final String ARQUIVO_TXT = "dados/livros.txt";

    public Biblioteca(String nome) {
        this.nome    = nome;
        this.livros  = new ArrayList<>();
        this.pessoas = new ArrayList<>();
    }

    // ── Livros ──────────────────────────────────────────────

    public void adicionarLivro(Livro livro) {
        livros.add(livro);
    }

    public void removerLivro(String titulo) {
        livros.removeIf(l -> l.getTitulo().equalsIgnoreCase(titulo));
    }

    public Livro buscarLivro(String titulo) {
        for (Livro l : livros) {
            if (l.getTitulo().equalsIgnoreCase(titulo)) {
                return l;
            }
        }
        return null;
    }

    public ArrayList<Livro> getLivros() {
        return livros;
    }

    public ArrayList<Livro> getLivrosDisponiveis() {
        ArrayList<Livro> disponiveis = new ArrayList<>();
        for (Livro l : livros) {
            if (l.isDisponivel()) disponiveis.add(l);
        }
        return disponiveis;
    }

    // ── Pessoas ─────────────────────────────────────────────

    public void adicionarPessoa(Pessoa pessoa) {
        pessoas.add(pessoa);
    }

    public void removerPessoa(String cpf) {
    pessoas.removeIf(p -> p.getCpf().equals(cpf));
    }
    public Pessoa buscarPessoa(String nome) {
        for (Pessoa p : pessoas) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Pessoa> getPessoas() {
        return pessoas;
    }

    // ── Empréstimo / Devolução ───────────────────────────────

    public void emprestar(String tituloLivro, String nomePessoa)
            throws LivroIndisponivelException {

        Livro livro = buscarLivro(tituloLivro);
        if (livro == null) {
            throw new LivroIndisponivelException(tituloLivro);
        }

        // Chamada polimórfica — pessoa pode ser Aluno ou Professor
        Pessoa pessoa = buscarPessoa(nomePessoa);
        if (pessoa == null) {
            pessoa = new Aluno(nomePessoa, "000.000.000-00", "", "S/N", "Visitante");
            pessoas.add(pessoa);
        }

        livro.emprestar(pessoa);
        salvar();
    }

    public void devolver(String tituloLivro) {
        Livro livro = buscarLivro(tituloLivro);
        if (livro != null) {
            livro.devolver();
            salvar();
        }
    }

    // ── Persistência ─────────────────────────────────────────

    public void salvar() {
        try {
            new File("dados").mkdirs();
            ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(ARQUIVO_DAT));
            out.writeObject(this);
            out.close();
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public static Biblioteca carregar() {
        try {
            ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(ARQUIVO_DAT));
            Biblioteca b = (Biblioteca) in.readObject();
            in.close();
            return b;
        } catch (Exception e) {
            return new Biblioteca("BibliotecaManager");
        }
    }

    // ── Leitura de TXT ───────────────────────────────────────

    public int importarLivrosTxt() {
        int count = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_TXT));
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (!linha.isEmpty() && buscarLivro(linha) == null) {
                    livros.add(new Livro(linha, "Autor Desconhecido"));
                    count++;
                }
            }
            br.close();
            salvar();
        } catch (IOException e) {
            System.err.println("Erro ao ler TXT: " + e.getMessage());
        }
        return count;
    }

    // ── Getters ──────────────────────────────────────────────

    public String getNome() { return nome; }
}

