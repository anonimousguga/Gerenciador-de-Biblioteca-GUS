public interface Emprestavel {
    void emprestar(Pessoa pessoa) throws LivroIndisponivelException;
    void devolver();
    boolean isDisponivel();
}