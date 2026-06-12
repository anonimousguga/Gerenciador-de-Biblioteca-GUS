import java.io.Serializable;

public class Aluno extends Pessoa implements Serializable {
    private static final long serialVersionUID = 2L;
    private String matricula;
    private String curso;
    public Aluno(String nome, String cpf, String email, String matricula, String curso) {
        super(nome, cpf, email);
        this.matricula = matricula;
        this.curso     = curso;
    }
    @Override
    public void exibirDados() {
        System.out.println("=== ALUNO ===");
        System.out.println("Nome:      " + nome);
        System.out.println("CPF:       " + cpf);
        System.out.println("E-mail:    " + email);
        System.out.println("Matrícula: " + matricula);
        System.out.println("Curso:     " + curso);
    }
    @Override
    public String toString() {
        return "[Aluno] " + nome + " | Matrícula: " + matricula + " | Curso: " + curso;
    }
    public String getMatricula() { return matricula; }
    public String getCurso()     { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
}
