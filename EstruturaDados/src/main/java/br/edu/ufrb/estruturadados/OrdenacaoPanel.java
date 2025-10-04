package br.edu.ufrb.estruturadados;

import br.edu.ufrb.estruturadados.algoritmosOrdenacao.BubbleSort;
import br.edu.ufrb.estruturadados.algoritmosOrdenacao.InsertionSort;
import br.edu.ufrb.estruturadados.algoritmosOrdenacao.PainelVisualizacao;
import br.edu.ufrb.estruturadados.algoritmosOrdenacao.SelectionSort;
import br.edu.ufrb.estruturadados.algoritmosOrdenacao.SortAlgorithm;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.*;

/**
 *
 * @author kelly
 */
public class OrdenacaoPanel extends JPanel {

    private static final int MAX_TAMANHO = 30;
    private static final int DELAY_MIN = 50, DELAY_MAX = 1000;

    private final Map<String, SortAlgorithm> algoritmos;
    private final List<Integer> numeros = new ArrayList<>();

    private JTextField campoEntrada, campoTamanho;
    private JButton botaoAdicionar, botaoLimpar, botaoOrdenar, botaoAleatorio, botaoPausar;
    private JComboBox<String> seletorMetodo;
    private JSlider sliderVelocidade;
    private JTextArea textoDefinicao;
    private PainelVisualizacao painelDesenho;

    private SwingWorker<Void, List<Integer>> worker;
    private final AtomicBoolean pausado = new AtomicBoolean(false);

    public OrdenacaoPanel() {
        algoritmos = new LinkedHashMap<>();
        algoritmos.put("Bubble Sort", new BubbleSort());
        algoritmos.put("Selection Sort", new SelectionSort());
        algoritmos.put("Insertion Sort", new InsertionSort());
        configurarInterface();
    }

    private void configurarInterface() {
        // Define o layout principal da janela e espaçamento das bordas
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // =====================================================
        // PAINEL SUPERIOR: controles (Valor, Tamanho, Velocidade)
        // =====================================================
        JPanel painelControles = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // margem entre componentes
        gbc.anchor = GridBagConstraints.WEST; // alinhamento à esquerda
        gbc.fill = GridBagConstraints.NONE;

        // --- Linha 1: Valor e método ---
        campoEntrada = new JTextField(5);
        seletorMetodo = new JComboBox<>(algoritmos.keySet().toArray(String[]::new));
        botaoAdicionar = new JButton("Adicionar");
        botaoLimpar = new JButton("Limpar");
        botaoOrdenar = new JButton("Ordenar");
        botaoPausar = new JButton("Pausar");
        botaoLimpar.setEnabled(false);
        botaoOrdenar.setEnabled(false);
        botaoPausar.setEnabled(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        painelControles.add(new JLabel("Valor:"), gbc);

        gbc.gridx = 1;
        painelControles.add(campoEntrada, gbc);

        gbc.gridx = 2;
        painelControles.add(botaoAdicionar, gbc);

        gbc.gridx = 3;
        painelControles.add(botaoLimpar, gbc);

        gbc.gridx = 4;
        painelControles.add(new JLabel("Método:"), gbc);

        gbc.gridx = 5;
        painelControles.add(seletorMetodo, gbc);

        gbc.gridx = 6;
        painelControles.add(botaoOrdenar, gbc);

        gbc.gridx = 7;
        painelControles.add(botaoPausar, gbc);

        // --- Linha 2: Tamanho e botão Aleatório ---
        campoTamanho = new JTextField(5);
        botaoAleatorio = new JButton("Gerar Aleatório");

        gbc.gridx = 0;
        gbc.gridy = 1;
        painelControles.add(new JLabel("Tamanho:"), gbc);

        gbc.gridx = 1;
        painelControles.add(campoTamanho, gbc);

        gbc.gridx = 2;
        painelControles.add(botaoAleatorio, gbc);

        // --- Linha 3: Slider de velocidade ---
        JLabel labelVel = new JLabel("Velocidade (ms):");

        sliderVelocidade = new JSlider(DELAY_MIN, DELAY_MAX, 750);
        sliderVelocidade.setMajorTickSpacing(250);
        sliderVelocidade.setMinorTickSpacing(50);
        sliderVelocidade.setPaintTicks(true);
        sliderVelocidade.setPaintLabels(true);

        java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<>();
        labelTable.put(DELAY_MIN, new JLabel(String.valueOf(DELAY_MIN)));
        labelTable.put(250, new JLabel("250"));
        labelTable.put(500, new JLabel("500"));
        labelTable.put(750, new JLabel("750"));
        labelTable.put(DELAY_MAX, new JLabel(String.valueOf(DELAY_MAX))); // valor final
        sliderVelocidade.setLabelTable(labelTable);

        gbc.gridx = 0;
        gbc.gridy = 2;
        painelControles.add(labelVel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painelControles.add(sliderVelocidade, gbc);

        // Adiciona painel de controles no topo
        add(painelControles, BorderLayout.NORTH);

        // =====================================================
        // PAINEL CENTRAL: área de visualização (gráfico)
        // =====================================================
        painelDesenho = new PainelVisualizacao();
        add(painelDesenho, BorderLayout.CENTER);

        // =====================================================
        // PAINEL INFERIOR: texto explicativo do algoritmo
        // =====================================================
        textoDefinicao = new JTextArea();
        textoDefinicao.setEditable(false);
        textoDefinicao.setFont(new Font("Serif", Font.ITALIC, 14));
        textoDefinicao.setLineWrap(true);
        textoDefinicao.setWrapStyleWord(true);
        textoDefinicao.setText(algoritmos.get("Bubble Sort").getDescricao());

        JScrollPane scrollDescricao = new JScrollPane(textoDefinicao);
        scrollDescricao.setPreferredSize(new Dimension(0, 125));
        add(scrollDescricao, BorderLayout.SOUTH);

        // =====================================================
        // EVENTOS
        // =====================================================
        botaoAdicionar.addActionListener(e -> {
            adicionarNumero();
            atualizarEstadoBotoes();
        });

        botaoLimpar.addActionListener(e -> {
            limparLista();
            atualizarEstadoBotoes();
        });

        botaoAleatorio.addActionListener(e -> {
            gerarAleatorio();
            atualizarEstadoBotoes();
        });
        botaoOrdenar.addActionListener(e -> iniciarOrdenacao());
        botaoPausar.addActionListener(e -> alternarPausa());
        seletorMetodo.addActionListener(e -> {
            var metodo = (String) seletorMetodo.getSelectedItem();
            textoDefinicao.setText(algoritmos.get(metodo).getDescricao());
        });
    }

    private void atualizarEstadoBotoes() {
        botaoLimpar.setEnabled(!numeros.isEmpty());

        botaoOrdenar.setEnabled(numeros.size() > 1 && !estaOrdenado());

        botaoAdicionar.setEnabled(numeros.size() < MAX_TAMANHO);
    }

    private void adicionarNumero() {
        try {
            int valor = Integer.parseInt(campoEntrada.getText().trim());
            if (valor < 0) {
                throw new NumberFormatException();
            }
            if (numeros.size() >= MAX_TAMANHO) {
                aviso("Máximo de " + MAX_TAMANHO + " elementos atingido.");
                return;
            }
            numeros.add(valor);
            campoEntrada.setText("");
            painelDesenho.atualizarLista(numeros);
        } catch (NumberFormatException ex) {
            erro("Digite um número inteiro positivo.");
        }
    }

    private void limparLista() {
        numeros.clear();
        painelDesenho.atualizarLista(numeros);
    }

    private void gerarAleatorio() {
        try {
            int tamanho = Integer.parseInt(campoTamanho.getText().trim());
            if (tamanho <= 0 || tamanho > MAX_TAMANHO) {
                erro("Informe um tamanho entre 1 e " + MAX_TAMANHO);
                return;
            }
            numeros.clear();
            Random r = new Random();
            for (int i = 0; i < tamanho; i++) {
                numeros.add(r.nextInt(90) + 10);
            }
            painelDesenho.atualizarLista(numeros);
        } catch (NumberFormatException ex) {
            erro("Digite um tamanho válido (inteiro).");
        }
    }

    private boolean estaOrdenado() {
        for (int i = 0; i < numeros.size() - 1; i++) {
            if (numeros.get(i) > numeros.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    private void iniciarOrdenacao() {
        if (numeros.isEmpty()) {
            aviso("Nenhum número para ordenar.");
            return;
        }

        if (estaOrdenado()) {
            aviso("A lista já está ordenada.");
            return;
        }

        bloquear(true);
        botaoPausar.setEnabled(true);
        sliderVelocidade.setEnabled(false);
        pausado.set(false);

        var metodo = (String) seletorMetodo.getSelectedItem();
        var algoritmo = algoritmos.get(metodo);
        int valorSlider = sliderVelocidade.getValue();
        int delay = DELAY_MAX - valorSlider + DELAY_MIN;

        worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                algoritmo.sort(numeros, (a, b) -> {
                    painelDesenho.destacarIndices(a, b);
                    painelDesenho.atualizarLista(numeros);
                    try {
                        while (pausado.get()) {
                            Thread.sleep(50);
                        }
                        Thread.sleep(delay);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                });
                return null;
            }

            @Override
            protected void done() {
                painelDesenho.destacarIndices(-1, -1);
                bloquear(false);
                botaoPausar.setEnabled(false);
                sliderVelocidade.setEnabled(true);
                atualizarEstadoBotoes();
                aviso("Ordenação concluída!");
            }
        };
        worker.execute();
    }

    private void alternarPausa() {
        pausado.set(!pausado.get());
        botaoPausar.setText(pausado.get() ? "Continuar" : "Pausar");
    }

    private void bloquear(boolean b) {
        botaoAdicionar.setEnabled(!b);
        botaoLimpar.setEnabled(!b);
        botaoAleatorio.setEnabled(!b);
        botaoOrdenar.setEnabled(!b);
        seletorMetodo.setEnabled(!b);
        campoEntrada.setEnabled(!b);
        campoTamanho.setEnabled(!b);
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
