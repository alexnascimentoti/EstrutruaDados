package br.edu.ufrb.estruturadados;
import br.edu.ufrb.estruturadados.arvoreBinariaNaoBalanceada.ArvoreBinaria;
import br.edu.ufrb.estruturadados.arvoreBinariaNaoBalanceada.PainelDesenhoArvore;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.swing.*;

/**
 *
 * @author kelly
 */
public class ArvoreBinariaNaoBalanceadaPanel extends JPanel {

    private static final int MAX_NODES = 20;

    private ArvoreBinaria arvore = new ArvoreBinaria();
    private JTextField campoValor, campoQtdNos;
    private JButton botaoInserir, botaoRemover, botaoBuscar, botaoLimpar, botaoGerar;
    private JButton botaoEmOrdem, botaoPreOrdem, botaoPosOrdem;
    private PainelDesenhoArvore painelDesenho;
    private JLabel labelAltura, labelNos, labelFolhas;
    private JTextArea textoDefinicao;
    private JScrollPane scrollPaneDesenho;

    private SwingWorker<Void, ArvoreBinaria.No> workerAtivo;

    public ArvoreBinariaNaoBalanceadaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Painel Superior: Controles ===
        JPanel painelControles = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // --- Linha 1: Controles Principais ---
        gbc.gridy = 0;
        gbc.gridx = 0; painelControles.add(new JLabel("Valor:"), gbc);
        gbc.gridx = 1; campoValor = new JTextField(5); painelControles.add(campoValor, gbc);
        gbc.gridx = 2; botaoInserir = new JButton("Inserir"); painelControles.add(botaoInserir, gbc);
        gbc.gridx = 3; botaoRemover = new JButton("Remover"); painelControles.add(botaoRemover, gbc);
        gbc.gridx = 4; botaoBuscar = new JButton("Buscar"); painelControles.add(botaoBuscar, gbc);
        gbc.gridx = 5; botaoLimpar = new JButton("Limpar"); painelControles.add(botaoLimpar, gbc);

        // --- Linha 2: Geração e Percursos ---
        gbc.gridy = 1;
        gbc.gridx = 0; painelControles.add(new JLabel("Número de nós:"), gbc);
        gbc.gridx = 1; campoQtdNos = new JTextField(5); painelControles.add(campoQtdNos, gbc);
        gbc.gridx = 2; botaoGerar = new JButton("Gerar Árvore"); painelControles.add(botaoGerar, gbc);
        gbc.gridx = 3; botaoEmOrdem = new JButton("Em Ordem"); painelControles.add(botaoEmOrdem, gbc);
        gbc.gridx = 4; botaoPreOrdem = new JButton("Pré-Ordem"); painelControles.add(botaoPreOrdem, gbc);
        gbc.gridx = 5; botaoPosOrdem = new JButton("Pós-Ordem"); painelControles.add(botaoPosOrdem, gbc);
        
        // --- Linha 3: Métricas ---
        gbc.gridy = 2;
        gbc.gridx = 0; gbc.gridwidth=6; gbc.anchor = GridBagConstraints.CENTER;
        JPanel painelMetricas = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        labelAltura = new JLabel("Altura: -1");
        labelNos = new JLabel("Nós: 0");
        labelFolhas = new JLabel("Folhas: 0");
        painelMetricas.add(labelAltura);
        painelMetricas.add(labelNos);
        painelMetricas.add(labelFolhas);
        painelControles.add(painelMetricas, gbc);
        
        add(painelControles, BorderLayout.NORTH);

        // === Painel Central: Desenho da Árvore (com Scroll) ===
        painelDesenho = new PainelDesenhoArvore(arvore);
        scrollPaneDesenho = new JScrollPane(painelDesenho);
        add(scrollPaneDesenho, BorderLayout.CENTER);

        // === Painel Inferior: Definição ===
        textoDefinicao = new JTextArea(getDefinicao());
        textoDefinicao.setEditable(false);
        textoDefinicao.setFont(new Font("Serif", Font.ITALIC, 14));
        textoDefinicao.setLineWrap(true);
        textoDefinicao.setWrapStyleWord(true);
        JScrollPane scrollDefinicao = new JScrollPane(textoDefinicao);
        scrollDefinicao.setPreferredSize(new Dimension(0, 100));
        add(scrollDefinicao, BorderLayout.SOUTH);

        // === Eventos ===
        botaoInserir.addActionListener(e -> inserirValor());
        botaoRemover.addActionListener(e -> removerValor());
        botaoBuscar.addActionListener(e -> buscarValor());
        botaoLimpar.addActionListener(e -> limparArvore());
        botaoGerar.addActionListener(e -> gerarArvoreAleatoria());
        botaoEmOrdem.addActionListener(e -> animarPercurso(arvore.getPercorsoEmOrdem()));
        botaoPreOrdem.addActionListener(e -> animarPercurso(arvore.getPercorsoPreOrdem()));
        botaoPosOrdem.addActionListener(e -> animarPercurso(arvore.getPercorsoPosOrdem()));

        atualizarInterfaceCompleta();
    }
    
    private void bloquearControles(boolean bloquear) {
        // Bloqueia todos os controles durante uma animação
        Component[] componentes = ((JPanel)getComponent(0)).getComponents();
        for (Component comp : componentes) {
             if (comp instanceof JPanel) { // para painel de metricas
                 for(Component compInterno : ((JPanel) comp).getComponents()){
                    compInterno.setEnabled(!bloquear);
                 }
             }
             comp.setEnabled(!bloquear);
        }
        
        if (!bloquear) {
            atualizarEstadoBotoes();
        }
    }

    private void atualizarEstadoBotoes() {
        boolean arvoreCheia = arvore.getNumeroNos() >= MAX_NODES;
        botaoInserir.setEnabled(!arvoreCheia);
    }

    private void atualizarInterfaceCompleta() {
        int alturaArvore = arvore.getAltura();
        int larguraNecessaria = (int) (Math.pow(2, alturaArvore) * 60) + 100;
        int alturaNecessaria = (alturaArvore + 1) * 90;

        painelDesenho.setPreferredSize(new Dimension(larguraNecessaria, alturaNecessaria));
        painelDesenho.revalidate();
        painelDesenho.repaint();
        atualizarMetricas();
        atualizarEstadoBotoes();
    }

    private void atualizarMetricas() {
        labelAltura.setText("Altura: " + arvore.getAltura());
        labelNos.setText("Nós: " + arvore.getNumeroNos());
        labelFolhas.setText("Folhas: " + arvore.getNumeroFolhas());
    }

    private void inserirValor() {
        if (arvore.getNumeroNos() >= MAX_NODES) {
            erro("A árvore atingiu o número máximo de " + MAX_NODES + " nós.");
            return;
        }
        try {
            int valor = Integer.parseInt(campoValor.getText().trim());
            arvore.inserir(valor);
            campoValor.setText("");
            atualizarInterfaceCompleta();
        } catch (NumberFormatException ex) {
            erro("Digite um número inteiro válido.");
        }
    }

    private void removerValor() {
        try {
            int valor = Integer.parseInt(campoValor.getText().trim());
            if (arvore.remover(valor)) {
                aviso("Valor " + valor + " removido com sucesso!");
            } else {
                erro("Valor " + valor + " não encontrado na árvore.");
            }
            campoValor.setText("");
            atualizarInterfaceCompleta();
        } catch (NumberFormatException ex) {
            erro("Digite um número inteiro válido.");
        }
    }

    private void limparArvore() {
        arvore.limpar();
        painelDesenho.setArvore(arvore);
        atualizarInterfaceCompleta();
    }

    private void gerarArvoreAleatoria() {
        try {
            int qtd = Integer.parseInt(campoQtdNos.getText().trim());
            if (qtd <= 0 || qtd > MAX_NODES) {
                erro("Informe uma quantidade entre 1 e " + MAX_NODES + ".");
                return;
            }
            limparArvore();
            Random rand = new Random();
            Set<Integer> valores = new HashSet<>();
            while(valores.size() < qtd) {
                valores.add(rand.nextInt(200));
            }
            for (Integer valor : valores) {
                arvore.inserir(valor);
            }
            atualizarInterfaceCompleta();
        } catch (NumberFormatException ex) {
            erro("Digite uma quantidade inteira válida.");
        }
    }

    private void buscarValor() {
        try {
            int valorBusca = Integer.parseInt(campoValor.getText().trim());
            bloquearControles(true);

            workerAtivo = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    ArvoreBinaria.No atual = arvore.getRaiz();
                    while (atual != null) {
                        publish(atual);
                        Thread.sleep(500);
                        if (valorBusca < atual.valor) atual = atual.esquerda;
                        else if (valorBusca > atual.valor) atual = atual.direita;
                        else break;
                    }
                    return null;
                }

                @Override
                protected void process(List<ArvoreBinaria.No> chunks) {
                    painelDesenho.destacarNo(chunks.get(chunks.size() - 1), true);
                }

                @Override
                protected void done() {
                    boolean encontrado = arvore.buscar(valorBusca);
                    if (encontrado) aviso("Valor " + valorBusca + " encontrado!");
                    else erro("Valor " + valorBusca + " não encontrado.");
                    painelDesenho.destacarNo(null, true);
                    bloquearControles(false);
                }
            };
            workerAtivo.execute();

        } catch (NumberFormatException ex) {
            erro("Digite um número inteiro válido para a busca.");
            bloquearControles(false);
        }
    }
    
    private void animarPercurso(List<ArvoreBinaria.No> percurso) {
        if(percurso.isEmpty()) {
            aviso("A árvore está vazia.");
            return;
        }
        
        bloquearControles(true);
        workerAtivo = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                for(ArvoreBinaria.No no : percurso) {
                    publish(no);
                    Thread.sleep(500);
                }
                return null;
            }

            @Override
            protected void process(List<ArvoreBinaria.No> chunks) {
                painelDesenho.destacarNo(chunks.get(chunks.size() - 1), false);
            }

            @Override
            protected void done() {
                painelDesenho.destacarNo(null, false);
                bloquearControles(false);
                aviso("Percurso finalizado!");
            }
        };
        workerAtivo.execute();
    }

    private String getDefinicao() {
    return "ÁRVORE BINÁRIA DE BUSCA (NÃO BALANCEADA):\n\n" +
            "É uma estrutura de dados hierárquica em que cada nó possui um valor e, no máximo, dois filhos: " +
            "um à esquerda e outro à direita. Todos os valores na subárvore esquerda são menores que o valor do nó, " +
            "e todos os valores na subárvore direita são maiores, mantendo assim uma ordenação natural entre os elementos. Nesta versão não balanceada, não há mecanismos para redistribuir automaticamente os nós, " +
            "o que pode levar a uma estrutura assimétrica dependendo da ordem de inserção.\n\n" +
            "Prós: Estrutura simples de implementar e entender. Mantém os elementos sempre ordenados e permite percursos em ordem (in-order) " +
            "que retornam os valores em sequência crescente.\n\n" +
            "Contras: Pode se tornar desbalanceada e perder eficiência, degradando para O(n) em operações de busca, inserção e remoção " +
            "quando os dados são inseridos de forma sequencial.\n\n" +
            "Complexidade: Busca, inserção e remoção — melhor caso O(log n), caso médio O(log n), pior caso O(n).";
}

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}