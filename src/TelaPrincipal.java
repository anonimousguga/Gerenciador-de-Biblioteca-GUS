import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

// tela principal do sistema, aqui fica tudo da interface grafica
public class TelaPrincipal extends JFrame {

    private Biblioteca biblioteca;

    // campos da aba de livros
    private JTextField campoBusca;
    private JTable tabelaLivros;
    private DefaultTableModel modeloLivros;

    // campos do cadastro de livro
    private JTextField campoTitulo;
    private JTextField campoAutor;
    private JTextField campoIsbn;
    private JTextField campoAno;

    // campos do emprestimo
    private JTextField campoTituloEmp;
    private JTextField campoNomeEmp;
    private JTextField campoTituloDev;

    // campos de pessoas
    private JTextField campoNome;
    private JTextField campoCpf;
    private JTextField campoEmail;
    private JTextField campoMatricula;
    private JTextField campoCurso;
    private JTextField campoSiape;
    private JTextField campoDpto;

    // paineis que aparecem/somem dependendo do tipo
    private JPanel painelAluno;
    private JPanel painelProfessor;
    private JButton btnCadastrarPessoa;

    private JTable tabelaPessoas;
    private DefaultTableModel modeloPessoas;

    public TelaPrincipal() {
        biblioteca = Biblioteca.carregar();
        montarTela();
    }

    private void montarTela() {
        setTitle("Gerenciador de Biblioteca - GUS");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Acervo", abaAcervo());
        abas.addTab("Novo Livro", abaCadastroLivro());
        abas.addTab("Emprestimo", abaEmprestimo());
        abas.addTab("Pessoas", abaPessoas());

        setLayout(new BorderLayout());
        add(abas, BorderLayout.CENTER);

        setVisible(true);
        recarregarTabelaLivros();
    }

    // -------------------------------------------------------
    // ABA ACERVO
    // -------------------------------------------------------
    private JPanel abaAcervo() {
        JPanel tela = new JPanel(new BorderLayout(8, 8));
        tela.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        campoBusca = new JTextField(20);
        JButton btnBuscar    = criarBotao("Buscar");
        JButton btnAtualizar = criarBotao("Atualizar");
        JButton btnImportar  = criarBotao("Importar TXT");
        JButton btnExcluir   = criarBotao("Excluir Livro");

        barra.add(new JLabel("Buscar:"));
        barra.add(campoBusca);
        barra.add(btnBuscar);
        barra.add(btnAtualizar);
        barra.add(btnImportar);
        barra.add(btnExcluir);

        String[] colunas = { "Titulo", "Autor", "ISBN", "Ano", "Situacao" };
        modeloLivros = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tabelaLivros = new JTable(modeloLivros);
        tabelaLivros.setRowHeight(24);
        tabelaLivros.getTableHeader().setReorderingAllowed(false);

        tela.add(barra, BorderLayout.NORTH);
        tela.add(new JScrollPane(tabelaLivros), BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> buscarNaTabela());
        btnAtualizar.addActionListener(e -> recarregarTabelaLivros());
        btnImportar.addActionListener(e -> importarDoTxt());

        btnExcluir.addActionListener(e -> {
            int linhaSelecionada = tabelaLivros.getSelectedRow();

            if (linhaSelecionada < 0) {
                JOptionPane.showMessageDialog(this,
                    "Selecione um livro na tabela primeiro.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String titulo = modeloLivros.getValueAt(linhaSelecionada, 0).toString();
            int confirmacao = JOptionPane.showConfirmDialog(this,
                "Excluir o livro \"" + titulo + "\"?",
                "Confirmar exclusao", JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                biblioteca.removerLivro(titulo);
                biblioteca.salvar();
                recarregarTabelaLivros();
            }
        });

        return tela;
    }

    // -------------------------------------------------------
    // ABA CADASTRO DE LIVRO
    // -------------------------------------------------------
    private JPanel abaCadastroLivro() {
        JPanel tela = new JPanel(new GridBagLayout());
        tela.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        campoTitulo = new JTextField(22);
        campoAutor  = new JTextField(22);
        campoIsbn   = new JTextField(22);
        campoAno    = new JTextField(22);

        inserirCampo(tela, g, "Titulo:", campoTitulo, 0);
        inserirCampo(tela, g, "Autor:", campoAutor, 1);
        inserirCampo(tela, g, "ISBN:", campoIsbn, 2);
        inserirCampo(tela, g, "Ano de publicacao:", campoAno, 3);

        JButton btn = criarBotao("Cadastrar Livro");
        btn.setPreferredSize(new Dimension(180, 32));
        g.gridx = 0; g.gridy = 4;
        g.gridwidth = 2;
        g.fill = GridBagConstraints.NONE;
        g.anchor = GridBagConstraints.CENTER;
        tela.add(btn, g);

        btn.addActionListener(e -> salvarLivro());
        return tela;
    }

    // -------------------------------------------------------
    // ABA EMPRESTIMO
    // -------------------------------------------------------
    private JPanel abaEmprestimo() {
        JPanel tela = new JPanel(new GridLayout(2, 1, 10, 10));
        tela.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel parteCima = new JPanel(new GridBagLayout());
        parteCima.setBorder(BorderFactory.createTitledBorder("Realizar Emprestimo"));
        GridBagConstraints g1 = new GridBagConstraints();
        g1.insets = new Insets(6, 6, 6, 6);
        g1.fill = GridBagConstraints.HORIZONTAL;

        campoTituloEmp = new JTextField(18);
        campoNomeEmp   = new JTextField(18);

        inserirCampo(parteCima, g1, "Titulo do livro:", campoTituloEmp, 0);
        inserirCampo(parteCima, g1, "Nome da pessoa:", campoNomeEmp, 1);

        JButton btnEmp = criarBotao("Emprestar");
        g1.gridx = 0; g1.gridy = 2; g1.gridwidth = 2;
        g1.fill = GridBagConstraints.NONE;
        g1.anchor = GridBagConstraints.CENTER;
        parteCima.add(btnEmp, g1);
        btnEmp.addActionListener(e -> fazerEmprestimo());

        JPanel parteBaixo = new JPanel(new GridBagLayout());
        parteBaixo.setBorder(BorderFactory.createTitledBorder("Devolver Livro"));
        GridBagConstraints g2 = new GridBagConstraints();
        g2.insets = new Insets(6, 6, 6, 6);
        g2.fill = GridBagConstraints.HORIZONTAL;

        campoTituloDev = new JTextField(18);
        inserirCampo(parteBaixo, g2, "Titulo do livro:", campoTituloDev, 0);

        JButton btnDev = criarBotao("Devolver");
        g2.gridx = 0; g2.gridy = 1; g2.gridwidth = 2;
        g2.fill = GridBagConstraints.NONE;
        g2.anchor = GridBagConstraints.CENTER;
        parteBaixo.add(btnDev, g2);
        btnDev.addActionListener(e -> fazerDevolucao());

        tela.add(parteCima);
        tela.add(parteBaixo);
        return tela;
    }

    // -------------------------------------------------------
    // ABA PESSOAS
    // -------------------------------------------------------
    private JPanel abaPessoas() {
        JPanel tela = new JPanel(new BorderLayout(8, 8));
        tela.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Cadastrar Pessoa"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.fill = GridBagConstraints.HORIZONTAL;

        campoNome  = new JTextField(18);
        campoCpf   = new JTextField(18);
        campoEmail = new JTextField(18);

        inserirCampo(form, g, "Nome:", campoNome, 0);
        inserirCampo(form, g, "CPF:", campoCpf, 1);
        inserirCampo(form, g, "Email:", campoEmail, 2);

        JRadioButton rbAluno     = new JRadioButton("Aluno", true);
        JRadioButton rbProfessor = new JRadioButton("Professor");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbAluno);
        grupo.add(rbProfessor);

        JPanel painelRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        painelRadio.add(rbAluno);
        painelRadio.add(rbProfessor);

        g.gridx = 0; g.gridy = 3; g.gridwidth = 1; g.weightx = 0;
        form.add(new JLabel("Tipo:"), g);
        g.gridx = 1; g.weightx = 1;
        form.add(painelRadio, g);

        campoMatricula = new JTextField(18);
        campoCurso     = new JTextField(18);
        painelAluno = new JPanel(new GridBagLayout());
        GridBagConstraints gA = new GridBagConstraints();
        gA.insets = new Insets(4, 4, 4, 4);
        gA.fill = GridBagConstraints.HORIZONTAL;
        inserirCampo(painelAluno, gA, "Matricula:", campoMatricula, 0);
        inserirCampo(painelAluno, gA, "Curso:", campoCurso, 1);

        campoSiape = new JTextField(18);
        campoDpto  = new JTextField(18);
        painelProfessor = new JPanel(new GridBagLayout());
        GridBagConstraints gP = new GridBagConstraints();
        gP.insets = new Insets(4, 4, 4, 4);
        gP.fill = GridBagConstraints.HORIZONTAL;
        inserirCampo(painelProfessor, gP, "SIAPE:", campoSiape, 0);
        inserirCampo(painelProfessor, gP, "Departamento:", campoDpto, 1);
        painelProfessor.setVisible(false);

        g.gridx = 0; g.gridy = 4; g.gridwidth = 2;
        form.add(painelAluno, g);
        g.gridy = 5;
        form.add(painelProfessor, g);

        btnCadastrarPessoa = criarBotao("Cadastrar Aluno");
        g.gridy = 6;
        g.fill = GridBagConstraints.NONE;
        g.anchor = GridBagConstraints.CENTER;
        form.add(btnCadastrarPessoa, g);

        rbAluno.addActionListener(e -> {
            painelAluno.setVisible(true);
            painelProfessor.setVisible(false);
            btnCadastrarPessoa.setText("Cadastrar Aluno");
            form.revalidate();
            form.repaint();
        });

        rbProfessor.addActionListener(e -> {
            painelAluno.setVisible(false);
            painelProfessor.setVisible(true);
            btnCadastrarPessoa.setText("Cadastrar Professor");
            form.revalidate();
            form.repaint();
        });

        btnCadastrarPessoa.addActionListener(e -> {
            if (rbAluno.isSelected()) cadastrarAluno();
            else cadastrarProfessor();
        });

        String[] cols = { "Nome", "CPF", "Tipo", "Info" };
        modeloPessoas = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaPessoas = new JTable(modeloPessoas);
        tabelaPessoas.setRowHeight(24);

        JPanel painelBaixo = new JPanel(new BorderLayout(4, 4));
        painelBaixo.add(new JScrollPane(tabelaPessoas), BorderLayout.CENTER);

        JButton btnExcluir = criarBotao("Excluir Pessoa");
        painelBaixo.add(btnExcluir, BorderLayout.SOUTH);

        btnExcluir.addActionListener(e -> {
            int linha = tabelaPessoas.getSelectedRow();

            if (linha < 0) {
                JOptionPane.showMessageDialog(this,
                    "Selecione uma pessoa na tabela.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nome = modeloPessoas.getValueAt(linha, 0).toString();
            String cpf  = modeloPessoas.getValueAt(linha, 1).toString();
            String tipo = modeloPessoas.getValueAt(linha, 2).toString();

            int ok = JOptionPane.showConfirmDialog(this,
                "Excluir " + tipo + " \"" + nome + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

            if (ok == JOptionPane.YES_OPTION) {
                biblioteca.removerPessoa(cpf);
                biblioteca.salvar();
                recarregarTabelaPessoas();
            }
        });

        tela.add(form, BorderLayout.NORTH);
        tela.add(painelBaixo, BorderLayout.CENTER);
        return tela;
    }

    // -------------------------------------------------------
    // METODO AUXILIAR - adiciona label + campo no GridBag
    // -------------------------------------------------------
    private void inserirCampo(JPanel painel, GridBagConstraints g,
                               String label, JTextField campo, int linha) {
        g.gridx = 0; g.gridy = linha;
        g.gridwidth = 1; g.weightx = 0;
        painel.add(new JLabel(label), g);
        g.gridx = 1; g.weightx = 1;
        painel.add(campo, g);
    }

    // -------------------------------------------------------
    // ACOES DOS BOTOES
    // -------------------------------------------------------

    private void salvarLivro() {
        String titulo = campoTitulo.getText().trim();
        String autor  = campoAutor.getText().trim();
        String isbn   = campoIsbn.getText().trim();
        String anoStr = campoAno.getText().trim();

        if (titulo.isEmpty() || autor.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Titulo e autor sao obrigatorios!",
                "Campos vazios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int ano = 0;
        if (!anoStr.isEmpty()) {
            try {
                ano = Integer.parseInt(anoStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Ano invalido, coloque so numeros.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        biblioteca.adicionarLivro(new Livro(titulo, autor, isbn.isEmpty() ? "N/A" : isbn, ano));
        biblioteca.salvar();

        campoTitulo.setText("");
        campoAutor.setText("");
        campoIsbn.setText("");
        campoAno.setText("");

        recarregarTabelaLivros();
    }

    private void buscarNaTabela() {
        String termo = campoBusca.getText().trim().toLowerCase();
        modeloLivros.setRowCount(0);

        for (Livro l : biblioteca.getLivros()) {
            if (l.getTitulo().toLowerCase().contains(termo)) {
                String sit = l.isDisponivel()
                    ? "Disponivel"
                    : "Com: " + l.getPessoaComLivro().getNome();
                modeloLivros.addRow(new Object[]{
                    l.getTitulo(), l.getAutor(), l.getIsbn(), l.getAnoPublicacao(), sit
                });
            }
        }
    }

    private void importarDoTxt() {
        biblioteca.importarLivrosTxt();
        recarregarTabelaLivros();
    }

    private void fazerEmprestimo() {
        String titulo = campoTituloEmp.getText().trim();
        String nome   = campoNomeEmp.getText().trim();

        if (titulo.isEmpty() || nome.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Preencha o titulo e o nome da pessoa.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            biblioteca.emprestar(titulo, nome);
            campoTituloEmp.setText("");
            campoNomeEmp.setText("");
            recarregarTabelaLivros();
        } catch (LivroIndisponivelException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Livro indisponivel", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fazerDevolucao() {
        String titulo = campoTituloDev.getText().trim();

        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Digite o titulo do livro para devolver.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        biblioteca.devolver(titulo);
        campoTituloDev.setText("");
        recarregarTabelaLivros();
    }

    private void cadastrarAluno() {
        String nome      = campoNome.getText().trim();
        String cpf       = campoCpf.getText().trim();
        String email     = campoEmail.getText().trim();
        String matricula = campoMatricula.getText().trim();
        String curso     = campoCurso.getText().trim();

        if (nome.isEmpty() || cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nome e CPF sao obrigatorios.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        biblioteca.adicionarPessoa(new Aluno(nome, cpf, email, matricula, curso));
        biblioteca.salvar();
        limparCamposPessoa();
        recarregarTabelaPessoas();
    }

    private void cadastrarProfessor() {
        String nome  = campoNome.getText().trim();
        String cpf   = campoCpf.getText().trim();
        String email = campoEmail.getText().trim();
        String siape = campoSiape.getText().trim();
        String dpto  = campoDpto.getText().trim();

        if (nome.isEmpty() || cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nome e CPF sao obrigatorios.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        biblioteca.adicionarPessoa(new Professor(nome, cpf, email, siape, dpto));
        biblioteca.salvar();
        limparCamposPessoa();
        recarregarTabelaPessoas();
    }

    private void limparCamposPessoa() {
        campoNome.setText("");
        campoCpf.setText("");
        campoEmail.setText("");
        campoMatricula.setText("");
        campoCurso.setText("");
        campoSiape.setText("");
        campoDpto.setText("");
    }

    // -------------------------------------------------------
    // ATUALIZAR TABELAS
    // -------------------------------------------------------

    private void recarregarTabelaLivros() {
        modeloLivros.setRowCount(0);
        for (Livro l : biblioteca.getLivros()) {
            String sit = l.isDisponivel()
                ? "Disponivel"
                : "Com: " + l.getPessoaComLivro().getNome();
            modeloLivros.addRow(new Object[]{
                l.getTitulo(), l.getAutor(), l.getIsbn(), l.getAnoPublicacao(), sit
            });
        }
    }

    private void recarregarTabelaPessoas() {
        modeloPessoas.setRowCount(0);
        for (Pessoa p : biblioteca.getPessoas()) {
            if (p instanceof Aluno) {
                Aluno a = (Aluno) p;
                modeloPessoas.addRow(new Object[]{
                    p.getNome(), p.getCpf(), "Aluno", "Curso: " + a.getCurso()
                });
            } else if (p instanceof Professor) {
                Professor pr = (Professor) p;
                modeloPessoas.addRow(new Object[]{
                    p.getNome(), p.getCpf(), "Professor", "Dpto: " + pr.getDepartamento()
                });
            }
        }
    }

    // deixa o botao com visual mais simples e flat
    private JButton criarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(new Color(60, 90, 160));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }
}