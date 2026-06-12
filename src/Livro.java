import java.io.Serializable;

public class Livro implements Emprestavel, Serializable {

    private static final long serialVersionUID = 4L;

    private String titulo;
    private String autor;
    private String isbn;
    private int    anoPublicacao;
    private boolean disponivel;
    private Pessoa pessoaComLivro;

    public Livro(String titulo, String autor, String isbn, int anoPublicacao) {
        this.titulo        = titulo;
        this.autor         = autor;
        this.isbn          = isbn;
        this.anoPublicacao = anoPublicacao;
        this.disponivel    = true;
        this.pessoaComLivro = null;
    }

    public Livro(String titulo, String autor) {
        this(titulo, autor, "N/A", 0);
    }

    @Override
    public void emprestar(Pessoa pessoa) throws LivroIndisponivelException {
        if (!disponivel) {
            throw new LivroIndisponivelException(titulo);
        }
        this.disponivel     = false;
        this.pessoaComLivro = pessoa;
    }

    @Override
    public void devolver() {
        this.disponivel     = true;
        this.pessoaComLivro = null;
    }

    @Override
    public boolean isDisponivel() {
        return disponivel;
    }

    public String getTitulo()        { return titulo; }
    public String getAutor()         { return autor; }
    public String getIsbn()          { return isbn; }
    public int    getAnoPublicacao() { return anoPublicacao; }
    public Pessoa getPessoaComLivro() { return pessoaComLivro; }
    public void setAutor(String autor) { this.autor = autor; }

    @Override
    public String toString() {
        String status = disponivel ? "Disponível" : "Emprestado para: " + pessoaComLivro.getNome();
        return "\"" + titulo + "\" — " + autor + " | " + status;
    }
}
