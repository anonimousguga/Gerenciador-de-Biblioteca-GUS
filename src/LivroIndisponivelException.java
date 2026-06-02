public class LivroIndisponivelException extends Exception {

    private static final long serialVersionUID = 10L;
    private String tituloLivro;
    public LivroIndisponivelException(String tituloLivro) {
        super("O livro \"" + tituloLivro + "\" não está disponível para empréstimo.");
        this.tituloLivro = tituloLivro;
    }

    public String getTituloLivro() {
        return tituloLivro;
    }
}
