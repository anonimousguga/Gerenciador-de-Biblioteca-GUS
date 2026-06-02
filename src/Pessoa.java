import java.io.Serializable;

public abstract class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String nome;
    protected String cpf;
    protected String email;

    public Pessoa(String nome, String cpf, String email) {
        this.nome  = nome;
        this.cpf   = cpf;
        this.email = email;
    }

    public abstract void exibirDados();
    public String getNome()  { return nome; }
    public String getCpf()   { return cpf; }
    public String getEmail() { return email; }
    public void setNome(String nome)   { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return nome + " (CPF: " + cpf + ")";
    }
}
