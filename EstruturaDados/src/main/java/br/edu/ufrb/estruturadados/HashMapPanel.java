package br.edu.ufrb.estruturadados;

import java.awt.*;
import java.util.Random;
import javax.swing.*;

/**
 *
 * @author kelly
 */
public class HashMapPanel extends JPanel {

    private static final int CAPACIDADE = 15;
    private Integer[] tabelaHash = new Integer[CAPACIDADE];
    private int tamanhoAtual = 0;

    private PainelDesenhoHashMap painelDesenho;
    private JTextField campoValor, campoQtdAleatorio;
    private JButton botaoInserir, botaoBuscar, botaoRemover, botaoLimpar, botaoGerar;
    private JLabel labelInfo;
    private JTextArea textoDefinicao;

    private final Color COR_SUCESSO = new Color(144, 238, 144);
    private final Color COR_COLISAO = new Color(255, 100, 100);
    private final Color COR_BUSCA = new Color(173, 216, 230);

    public HashMapPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Painel de Controles (Superior) ===
        JPanel painelControles = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridy = 0;
        gbc.gridx = 0;
        painelControles.add(new JLabel("Valor (int):"), gbc);
        gbc.gridx = 1;
        campoValor = new JTextField(5);
        painelControles.add(campoValor, gbc);
        gbc.gridx = 2;
        botaoInserir = new JButton("Inserir");
        painelControles.add(botaoInserir, gbc);
        gbc.gridx = 3;
        botaoBuscar = new JButton("Buscar");
        painelControles.add(botaoBuscar, gbc);
        gbc.gridx = 4;
        botaoRemover = new JButton("Remover");
        painelControles.add(botaoRemover, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        painelControles.add(new JLabel("Qtd. Aleatória:"), gbc);
        gbc.gridx = 1;
        campoQtdAleatorio = new JTextField(5);
        painelControles.add(campoQtdAleatorio, gbc);
        gbc.gridx = 2;
        botaoGerar = new JButton("Gerar");
        painelControles.add(botaoGerar, gbc);
        gbc.gridx = 3;
        botaoLimpar = new JButton("Limpar Tabela");
        painelControles.add(botaoLimpar, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 5;
        labelInfo = new JLabel("Info: Insira um valor para calcular o índice.");
        painelControles.add(labelInfo, gbc);

        add(painelControles, BorderLayout.NORTH);

        // === Painel de Desenho (Centro) ===
        painelDesenho = new PainelDesenhoHashMap();
        add(new JScrollPane(painelDesenho, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

        // === Painel de Definição (Inferior) ===
        textoDefinicao = new JTextArea(getDefinicao());
        textoDefinicao.setEditable(false);
        textoDefinicao.setFont(new Font("Serif", Font.ITALIC, 14));
        textoDefinicao.setLineWrap(true);
        textoDefinicao.setWrapStyleWord(true);

        JScrollPane scrollDescricao = new JScrollPane(textoDefinicao);
        scrollDescricao.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollDescricao.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        int alturaMaxima = 115;
        scrollDescricao.setPreferredSize(new Dimension(Integer.MAX_VALUE, alturaMaxima));

        add(scrollDescricao, BorderLayout.SOUTH);

        // === Eventos ===
        botaoInserir.addActionListener(e -> executarAcao("inserir"));
        botaoBuscar.addActionListener(e -> executarAcao("buscar"));
        botaoRemover.addActionListener(e -> executarAcao("remover"));
        botaoGerar.addActionListener(e -> gerarAleatorios());
        botaoLimpar.addActionListener(e -> limpar());

        atualizarVisualizacao();
    }

    private void limpar() {
        tabelaHash = new Integer[CAPACIDADE];
        tamanhoAtual = 0;
        labelInfo.setText("Info: Tabela foi limpa.");
        atualizarVisualizacao();
    }

    private void executarAcao(String acao) {

        try {
            int valor = Integer.parseInt(campoValor.getText().trim());

            bloquearControles(true);
            SwingWorker<Void, String> worker = new SwingWorker<>() {
                private int index;

                @Override
                protected Void doInBackground() throws Exception {
                    index = valor % CAPACIDADE;

                    publish("Calculando índice para o valor " + valor + "...");
                    Thread.sleep(500);

                    publish(valor + " % " + CAPACIDADE + " = " + index);
                    painelDesenho.destacar(index, COR_BUSCA);
                    Thread.sleep(700);

                    switch (acao) {
                        case "inserir":
                            if (tabelaHash[index] != null) {
                                painelDesenho.destacar(index, COR_COLISAO);
                                aviso("COLISÃO! Índice " + index + " já ocupado por " + tabelaHash[index] + ".");
                            } else {
                                painelDesenho.destacar(index, COR_SUCESSO);
                                aviso("Índice " + index + " livre. Inserindo valor " + valor + ".");
                                tabelaHash[index] = valor;
                                tamanhoAtual++;
                            }
                            break;
                        case "buscar":
                            if (tabelaHash[index] != null && tabelaHash[index].equals(valor)) {
                                painelDesenho.destacar(index, COR_SUCESSO);
                                aviso("Valor " + valor + " encontrado no índice " + index + ".");
                            } else {
                                painelDesenho.destacar(index, COR_COLISAO);
                                aviso("Valor " + valor + " NÃO encontrado no índice " + index + ".");
                            }
                            break;
                        case "remover":

                            if (tabelaHash[index] != null && tabelaHash[index].equals(valor)) {
                                painelDesenho.destacar(index, COR_SUCESSO);
                                aviso("Valor " + valor + " removido do índice " + index + ".");
                                tabelaHash[index] = null;
                                tamanhoAtual--;
                            } else {
                                painelDesenho.destacar(index, COR_COLISAO);
                                aviso("Valor " + valor + " NÃO encontrado. Índice " + index + " está vazio.");
                            }
                            break;
                    }

                    Thread.sleep(1000);
                    return null;
                }

                @Override
                protected void process(java.util.List<String> chunks) {
                    labelInfo.setText("Info: " + chunks.get(chunks.size() - 1));
                }

                @Override
                protected void done() {
                    atualizarVisualizacao();
                    bloquearControles(false);
                    campoValor.setText("");
                }
            };
            worker.execute();

        } catch (NumberFormatException ex) {
            erro("O valor deve ser um número inteiro válido.");
        }
    }

    private void gerarAleatorios() {

        try {
            int qtd = Integer.parseInt(campoQtdAleatorio.getText().trim());
            if (qtd <= 0) {
                erro("A quantidade deve ser maior que zero.");
                return;
            }

            Random rand = new Random();
            int inseridos = 0;
            int tentados = 0;
            int limiteTentativas = Math.max(100, qtd * 5);
            while (inseridos < qtd && tamanhoAtual < CAPACIDADE && tentados < limiteTentativas) {
                int valorAleatorio = rand.nextInt(1000) + 1;
                int index = valorAleatorio % CAPACIDADE;
                if (tabelaHash[index] == null) {
                    tabelaHash[index] = valorAleatorio;
                    inseridos++;
                    tamanhoAtual++;
                }
                tentados++;
            }

            if (inseridos == 0) {
                labelInfo.setText("Info: Não foi possível inserir novos valores (colisões ou tabela cheia).");
            } else if (inseridos < qtd) {
                labelInfo.setText("Info: Inseridos " + inseridos + " de " + qtd + " valores (outros colidiram).");
            } else {
                labelInfo.setText("Info: " + inseridos + " valores aleatórios inseridos com sucesso!");
            }

            atualizarVisualizacao();

        } catch (NumberFormatException ex) {
            erro("A quantidade deve ser um número inteiro válido.");
        }
    }

    private void atualizarVisualizacao() {
        painelDesenho.atualizar(tabelaHash, CAPACIDADE);
    }

    private void bloquearControles(boolean bloquear) {
        botaoInserir.setEnabled(!bloquear);
        botaoBuscar.setEnabled(!bloquear);
        botaoRemover.setEnabled(!bloquear);
        botaoLimpar.setEnabled(!bloquear);
        botaoGerar.setEnabled(!bloquear);
        campoValor.setEnabled(!bloquear);
        campoQtdAleatorio.setEnabled(!bloquear);
    }

    private String getDefinicao() {
        return """
        Hash Map Simples (Tabela Hash):

        Estrutura de dados que associa chaves a valores, armazenando os elementos em posições determinadas por uma função hash. 
        Nesta implementação, a função hash é calculada como valor % capacidade, mapeando a chave para um índice no array e permitindo acesso direto aos valores armazenados. 
        Quando duas chaves diferentes produzem o mesmo índice, ocorre uma colisão. Nesta versão simplificada, as colisões são apenas detectadas, sem estratégias de resolução, pois o foco é compreender o mapeamento e como as colisões podem acontecer.

        Vantagens:
        - Inserção, busca e remoção geralmente muito rápidas, graças ao acesso direto via índice.
        - Estrutura eficiente para armazenar e recuperar pares de chave-valor.

        Desvantagens:
        - Pode sofrer colisões, o que reduz o desempenho se não forem tratadas.
        - Requer uma função hash eficiente para distribuir os elementos de forma uniforme.

        Complexidade (assumindo uma boa função hash):
        - Melhor caso: O(1)
        - Caso médio: O(1)
        - Pior caso (muitas colisões): O(n)""";
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    class PainelDesenhoHashMap extends JPanel {

        private Integer[] tabelaHash;
        private int capacidade;
        private int indexDestaque = -1;
        private Color corDestaque = Color.GRAY;

        public PainelDesenhoHashMap() {
            setBackground(Color.WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 14));
        }

        public void atualizar(Integer[] tabelaHash, int capacidade) {
            this.tabelaHash = tabelaHash;
            this.capacidade = capacidade;
            this.indexDestaque = -1;
            repaint();
        }

        public void destacar(int index, Color cor) {
            this.indexDestaque = index;
            this.corDestaque = cor;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (tabelaHash == null) {
                return;
            }

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            FontMetrics fm = g2d.getFontMetrics();

            int colunas = 5;
            int linhas = (int) Math.ceil((double) capacidade / colunas);
            int bucketWidth = 120;
            int bucketHeight = 60;
            int spacingX = 20;
            int spacingY = 30;

            int totalWidth = colunas * (bucketWidth + spacingX) - spacingX;
            int totalHeight = linhas * (bucketHeight + spacingY) - spacingY;

            int startX = (getWidth() - totalWidth) / 2;
            int startY = (getHeight() - totalHeight) / 2;

            for (int i = 0; i < capacidade; i++) {
                int row = i / colunas;
                int col = i % colunas;
                int x = startX + col * (bucketWidth + spacingX);
                int y = startY + row * (bucketHeight + spacingY);

                g2d.setColor(i == indexDestaque ? corDestaque : new Color(240, 240, 240));
                g2d.fillRoundRect(x, y, bucketWidth, bucketHeight, 15, 15);
                g2d.setColor(Color.BLACK);
                g2d.drawRoundRect(x, y, bucketWidth, bucketHeight, 15, 15);

                String textoIndice = "Índice [" + i + "]";
                int strWidthIndice = fm.stringWidth(textoIndice);
                g2d.drawString(textoIndice, x + (bucketWidth - strWidthIndice) / 2, y + 20);

                if (tabelaHash[i] != null) {
                    String textoValor = "Valor: " + tabelaHash[i];
                    g2d.setFont(new Font("SansSerif", Font.PLAIN, 16));
                    int strWidthValor = g2d.getFontMetrics().stringWidth(textoValor);
                    g2d.drawString(textoValor, x + (bucketWidth - strWidthValor) / 2, y + 45);
                    g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
                }
            }

            g2d.dispose();
        }

    }
}
