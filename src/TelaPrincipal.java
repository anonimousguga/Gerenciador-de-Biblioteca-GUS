import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Interface gráfica principal do BibliotecaManager.
 * Utiliza Java Swing com JFrame, JPanel, JButton, JTextField, JTable e eventos.
 */
public class TelaPrincipal extends JFrame {

    private Biblioteca biblioteca;

    // Componentes da aba Livros
    private JTextField campoBusca;
    private JTable tabelaLivros;
    private DefaultTableModel modeloTabelaLivros;

    // Componentes da aba Cadastro de Livro
    private JTextField campoTitulo;
    private JTextField campoAutor;
    private JTextField campoIsbn;
    private JTextField campoAno;

    // Componentes da aba Empréstimo
    private JTextField campoTituloEmprestimo;
    private JTextField campoNomePessoa;
    private JTextField campoTituloDevolucao;

    // Componentes da aba Pessoas
    private JTextField campoNomeCadastro;
    private JTextField campoCpf;
    private JTextField campoEmail;
    private JTextField campoMatricula;
    private JTextField campoCurso;
    private JTable tabelaPessoas;
    private DefaultTableModel modeloTabelaPessoas;

    // Área de log
    private JTextArea areaLog;

    public TelaPrincipal() {
        biblioteca = Biblioteca.carregar();
        iniciarUI();
    }

    private void iniciarUI() {
        setTitle("BibliotecaManager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal com abas
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("📚 Acervo",     painelAcervo());
        abas.addTab("➕ Cadastrar Livro", painelCadastrarLivro());
        abas.addTab("🔄 Empréstimo",  painelEmprestimo());
        abas.addTab("👤 Pessoas",     painelPessoas());

        // Área de log na parte inferior
        areaLog = new JTextArea(4, 0);
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaLog.setBackground(new Color(30, 30, 30));
        areaLog.setForeground(new Color(0, 255, 100));
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Log"));

        // Layout principal
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.add(abas, BorderLayout.CENTER);
        painelPrincipal.add(scrollLog, BorderLayout.SOUTH);

        add(painelPrincipal);
        setVisible(true);

        log("Sistema iniciado. Bem-vindo ao BibliotecaManager!");
        atualizarTabelaLivros();
    }

    // ── Aba Acervo ───────────────────────────────────────────

    private JPanel painelAcervo() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Barra de busca
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBusca.add(new JLabel("Buscar:"));
        campoBusca = new JTextField(25);
        JButton botaoBuscar   = new JButton("Buscar");
        JButton botaoImportar = new JButton("Importar TXT");
        JButton botaoAtualizar = new JButton("Atualizar");
        painelBusca.add(campoBusca);
        painelBusca.add(botaoBuscar);
        painelBusca.add(botaoAtualizar);
        painelBusca.add(botaoImportar);

        // Tabela de livros
        String[] colunas = {"Título", "Autor", "ISBN", "Ano", "Status"};
        modeloTabelaLivros = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaLivros = new JTable(modeloTabelaLivros);
        tabelaLivros.setRowHeight(22);
        JScrollPane scroll = new JScrollPane(tabelaLivros);

        painel.add(painelBusca, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);

        // Eventos
        botaoBuscar.addActionListener(e -> buscarLivro());
        botaoAtualizar.addActionListener(e -> atualizarTabelaLivros());
        botaoImportar.addActionListener(e -> importarTxt());

        return painel;
    }

    // ── Aba Cadastrar Livro ──────────────────────────────────

    private JPanel painelCadastrarLivro() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 8, 8, 8);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.anchor  = GridBagConstraints.WEST;

        campoTitulo = new JTextField(25);
        campoAutor  = new JTextField(25);
        campoIsbn   = new JTextField(25);
        campoAno    = new JTextField(25);

        adicionarCampo(painel, gbc, "Título:",  campoTitulo, 0);
        adicionarCampo(painel, gbc, "Autor:",   campoAutor,  1);
        adicionarCampo(painel, gbc, "ISBN:",    campoIsbn,   2);
        adicionarCampo(painel, gbc, "Ano:",     campoAno,    3);

        JButton botaoCadastrar = new JButton("Cadastrar Livro");
        botaoCadastrar.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(botaoCadastrar, gbc);

        botaoCadastrar.addActionListener(e -> cadastrarLivro());

        return painel;
    }

    // ── Aba Empréstimo ───────────────────────────────────────

    private JPanel painelEmprestimo() {
        JPanel painel = new JPanel(new GridLayout(2, 1, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Empréstimo
        JPanel painelEmp = new JPanel(new GridBagLayout());
        painelEmp.setBorder(BorderFactory.createTitledBorder("Realizar Empréstimo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        campoTituloEmprestimo = new JTextField(20);
        campoNomePessoa       = new JTextField(20);

        adicionarCampo(painelEmp, gbc, "Título do Livro:", campoTituloEmprestimo, 0);
        adicionarCampo(painelEmp, gbc, "Nome da Pessoa:",  campoNomePessoa,       1);

        JButton botaoEmprestar = new JButton("Emprestar");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        painelEmp.add(botaoEmprestar, gbc);
        botaoEmprestar.addActionListener(e -> realizarEmprestimo());

        // Devolução
        JPanel painelDev = new JPanel(new GridBagLayout());
        painelDev.setBorder(BorderFactory.createTitledBorder("Realizar Devolução"));
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(6, 6, 6, 6);
        gbc2.fill   = GridBagConstraints.HORIZONTAL;

        campoTituloDevolucao = new JTextField(20);
        adicionarCampo(painelDev, gbc2, "Título do Livro:", campoTituloDevolucao, 0);

        JButton botaoDevolver = new JButton("Devolver");
        gbc2.gridx = 0; gbc2.gridy = 1; gbc2.gridwidth = 2;
        gbc2.fill = GridBagConstraints.NONE;
        gbc2.anchor = GridBagConstraints.CENTER;
        painelDev.add(botaoDevolver, gbc2);
        botaoDevolver.addActionListener(e -> realizarDevolucao());

        painel.add(painelEmp);
        painel.add(painelDev);

        return painel;
    }

    // ── Aba Pessoas ──────────────────────────────────────────

    private JPanel painelPessoas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulário
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Cadastrar Aluno"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        campoNomeCadastro = new JTextField(20);
        campoCpf          = new JTextField(20);
        campoEmail        = new JTextField(20);
        campoMatricula    = new JTextField(20);
        campoCurso        = new JTextField(20);

        adicionarCampo(form, gbc, "Nome:",       campoNomeCadastro, 0);
        adicionarCampo(form, gbc, "CPF:",        campoCpf,          1);
        adicionarCampo(form, gbc, "E-mail:",     campoEmail,        2);
        adicionarCampo(form, gbc, "Matrícula:",  campoMatricula,    3);
        adicionarCampo(form, gbc, "Curso:",      campoCurso,        4);

        JButton botaoCadastrar = new JButton("Cadastrar Aluno");
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(botaoCadastrar, gbc);
        botaoCadastrar.addActionListener(e -> cadastrarAluno());

        // Tabela de pessoas
        String[] colunas = {"Nome", "CPF", "Tipo"};
        modeloTabelaPessoas = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaPessoas = new JTable(modeloTabelaPessoas);
        tabelaPessoas.setRowHeight(22);
        JScrollPane scroll = new JScrollPane(tabelaPessoas);

        painel.add(form,   BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    // ── Helpers de layout ────────────────────────────────────

    private void adicionarCampo(JPanel painel, GridBagConstraints gbc,
                                 String label, JTextField campo, int linha) {
        gbc.gridx = 0; gbc.gridy = linha; gbc.gridwidth = 1;
        gbc.weightx = 0;
        painel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        painel.add(campo, gbc);
    }

    // ── Ações ────────────────────────────────────────────────

    private void cadastrarLivro() {
        String titulo = campoTitulo.getText().trim();
        String autor  = campoAutor.getText().trim();
        String isbn   = campoIsbn.getText().trim();
        String anoStr = campoAno.getText().trim();

        if (titulo.isEmpty() || autor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título e Autor são obrigatórios!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int ano = 0;
        if (!anoStr.isEmpty()) {
            try { ano = Integer.parseInt(anoStr); }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ano inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Livro livro = new Livro(titulo, autor, isbn.isEmpty() ? "N/A" : isbn, ano);
        biblioteca.adicionarLivro(livro);
        biblioteca.salvar();

        log("Livro cadastrado: " + titulo);
        campoTitulo.setText(""); campoAutor.setText("");
        campoIsbn.setText("");   campoAno.setText("");
        atualizarTabelaLivros();
    }

    private void buscarLivro() {
        String termo = campoBusca.getText().trim();
        modeloTabelaLivros.setRowCount(0);

        for (Livro l : biblioteca.getLivros()) {
            if (l.getTitulo().toLowerCase().contains(termo.toLowerCase())) {
                String status = l.isDisponivel() ? "Disponível"
                        : "Emprestado → " + l.getPessoaComLivro().getNome();
                modeloTabelaLivros.addRow(new Object[]{
                    l.getTitulo(), l.getAutor(), l.getIsbn(), l.getAnoPublicacao(), status
                });
            }
        }
        log("Busca por \"" + termo + "\": " + modeloTabelaLivros.getRowCount() + " resultado(s).");
    }

    private void importarTxt() {
        int count = biblioteca.importarLivrosTxt();
        log(count + " livro(s) importado(s) do arquivo TXT.");
        atualizarTabelaLivros();
    }

    private void realizarEmprestimo() {
        String titulo = campoTituloEmprestimo.getText().trim();
        String nome   = campoNomePessoa.getText().trim();

        if (titulo.isEmpty() || nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            biblioteca.emprestar(titulo, nome);
            log("Empréstimo realizado: \"" + titulo + "\" → " + nome);
            campoTituloEmprestimo.setText("");
            campoNomePessoa.setText("");
            atualizarTabelaLivros();
        } catch (LivroIndisponivelException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Livro Indisponível", JOptionPane.ERROR_MESSAGE);
            log("ERRO: " + ex.getMessage());
        }
    }

    private void realizarDevolucao() {
        String titulo = campoTituloDevolucao.getText().trim();
        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o título do livro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        biblioteca.devolver(titulo);
        log("Devolução realizada: \"" + titulo + "\"");
        campoTituloDevolucao.setText("");
        atualizarTabelaLivros();
    }

    private void cadastrarAluno() {
        String nome       = campoNomeCadastro.getText().trim();
        String cpf        = campoCpf.getText().trim();
        String email      = campoEmail.getText().trim();
        String matricula  = campoMatricula.getText().trim();
        String curso      = campoCurso.getText().trim();

        if (nome.isEmpty() || cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e CPF são obrigatórios!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Chamada polimórfica — adicionarPessoa aceita qualquer Pessoa
        Aluno aluno = new Aluno(nome, cpf, email, matricula, curso);
        biblioteca.adicionarPessoa(aluno);
        biblioteca.salvar();

        log("Aluno cadastrado: " + nome);
        campoNomeCadastro.setText(""); campoCpf.setText("");
        campoEmail.setText("");        campoMatricula.setText("");
        campoCurso.setText("");
        atualizarTabelaPessoas();
    }

    // ── Atualizar tabelas ────────────────────────────────────

    private void atualizarTabelaLivros() {
        modeloTabelaLivros.setRowCount(0);
        for (Livro l : biblioteca.getLivros()) {
            String status = l.isDisponivel() ? "Disponível"
                    : "Emprestado → " + l.getPessoaComLivro().getNome();
            modeloTabelaLivros.addRow(new Object[]{
                l.getTitulo(), l.getAutor(), l.getIsbn(), l.getAnoPublicacao(), status
            });
        }
    }

    private void atualizarTabelaPessoas() {
        modeloTabelaPessoas.setRowCount(0);
        for (Pessoa p : biblioteca.getPessoas()) {
            String tipo = (p instanceof Aluno) ? "Aluno" : "Professor";
            modeloTabelaPessoas.addRow(new Object[]{ p.getNome(), p.getCpf(), tipo });
        }
    }

    // ── Log ──────────────────────────────────────────────────

    private void log(String mensagem) {
        areaLog.append("> " + mensagem + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }
}

