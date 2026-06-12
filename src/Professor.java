import java.io.Serializable;

public class Professor extends Pessoa implements Serializable {
    private static final long serialVersionUID = 3L;
    private String siape;
    private String departamento;
    public Professor(String nome, String cpf, String email, String siape, String departamento) {
        super(nome, cpf, email);
        this.siape        = siape;
        this.departamento = departamento;
    }
    @Override
    public void exibirDados() {
        System.out.println("=== PROFESSOR ===");
        System.out.println("Nome:         " + nome);
        System.out.println("CPF:          " + cpf);
        System.out.println("E-mail:       " + email);
        System.out.println("SIAPE:        " + siape);
        System.out.println("Departamento: " + departamento);
    }
    @Override
    public String toString() {
        return "[Professor] " + nome + " | SIAPE: " + siape + " | Depto: " + departamento;
    }
    public String getSiape()        { return siape; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
}
