package br.edu.ufrb.estruturadados;

import javax.swing.*;
import java.awt.*;

public class ArvoreBinariaNaoOrdenadaPanel extends JPanel {

    /**
     *
     * @author Alex do Nascimento Ambrosio
     */

    // Classe interna para representar um nó da árvore
    private static class No {
        String valor;
        No esquerda;
        No direita;

        No(String valor) {
            this.valor = valor;
            this.esquerda = null;
            this.direita = null;
        }
    }
    
    // Classe auxiliar para passar múltiplas informações da busca recursiva de remoção
    private static class InfoUltimoNo {
        No no = null;
        No pai = null;
        int nivel = 0;
    }

    private No raiz;
    private JTextField campoDeEntrada;
    private JButton botaoAdicionar, botaoRemover;
    private VisualizacaoPanel painelDesenho;

    // Constantes para o desenho da árvore
    private static final int DIAMETRO_NO = 40;
    private static final int ESPACO_VERTICAL = 60;

    public ArvoreBinariaNaoOrdenadaPanel() {
        raiz = null;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Painel de Controles ---
        JPanel painelControles = new JPanel();
        campoDeEntrada = new JTextField(10);
        botaoAdicionar = new JButton("Adicionar");
        botaoRemover = new JButton("Remover (por valor)");

        painelControles.add(new JLabel("Valor:"));
        painelControles.add(campoDeEntrada);
        painelControles.add(botaoAdicionar);
        painelControles.add(botaoRemover);

        add(painelControles, BorderLayout.NORTH);

        // --- Painel de Visualização ---
        painelDesenho = new VisualizacaoPanel();
        add(painelDesenho, BorderLayout.CENTER);

        // --- Painel de Texto de Definição ---
        JTextArea textoDefinicao = new JTextArea();
        textoDefinicao.setEditable(false);
        textoDefinicao.setFont(new Font("Serif", Font.ITALIC, 14));
        textoDefinicao.setLineWrap(true);
        textoDefinicao.setWrapStyleWord(true);
        textoDefinicao.setText(getDefinicao());
        JScrollPane scrollPane = new JScrollPane(textoDefinicao);
        scrollPane.setPreferredSize(new Dimension(100, 120)); // Altura do painel
        add(scrollPane, BorderLayout.SOUTH);

        // --- Lógica dos Botões (Listeners) ---
        botaoAdicionar.addActionListener(e -> adicionarElemento());
        botaoRemover.addActionListener(e -> removerElemento());
    }

    private String getDefinicao() {
        return "ÁRVORE BINÁRIA NÃO ORDENADA:\n\n" +
                "É uma estrutura de dados hierárquica em que cada nó tem no máximo dois filhos, " +
                "geralmente referidos como filho da esquerda e filho da direita. O nó no topo da hierarquia é chamado de raiz.\n"
                +
                "Nesta visualização (não ordenada), os nós são adicionados no primeiro espaço disponível da esquerda para a direita, nível por nível.\n"
                +
                "Prós: Representação eficiente de hierarquias. A busca pode ser eficiente (O(log n)) se a árvore for balanceada (como em uma Árvore de Busca Binária).\n"
                +
                "Contras: Em uma árvore não ordenada como esta, a busca é O(n), pois pode ser necessário percorrer todos os nós.";
    }

    // --- MÉTODOS DE MANIPULAÇÃO DA ÁRVORE ---

    private void adicionarElemento() {
        String valor = campoDeEntrada.getText().trim();
        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        No novoNo = new No(valor);
        if (raiz == null) {
            raiz = novoNo;
        } else {
            // Itera através dos níveis da árvore para encontrar um lugar para inserir
            int altura = calcularAltura(raiz);
            for (int nivel = 1; nivel <= altura + 1; nivel++) {
                if (tentarInserirNoNivel(raiz, novoNo, nivel, 1)) {
                    break; // Sai do loop assim que a inserção for bem-sucedida
                }
            }
        }

        campoDeEntrada.setText("");
        painelDesenho.repaint();
    }

    private void removerElemento() {
        String valor = campoDeEntrada.getText().trim();
        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor a ser removido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (raiz == null) {
            JOptionPane.showMessageDialog(this, "A árvore está vazia.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 1. Encontrar o nó a ser removido
        No noParaRemover = buscarNo(raiz, valor);

        if (noParaRemover == null) {
            JOptionPane.showMessageDialog(this, "Valor não encontrado na árvore.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 2. Encontrar o último nó (o mais profundo e à direita)
        InfoUltimoNo info = new InfoUltimoNo();
        encontrarUltimoNo(raiz, null, 1, info);
        No ultimoNo = info.no;
        No paiDoUltimo = info.pai;

        // Caso especial: se o nó a ser removido é o último nó
        if (noParaRemover == ultimoNo) {
            // Se for a raiz
            if (paiDoUltimo == null) {
                raiz = null;
            } else { // Se não for a raiz
                if (paiDoUltimo.direita == ultimoNo) {
                    paiDoUltimo.direita = null;
                } else {
                    paiDoUltimo.esquerda = null;
                }
            }
        } else {
            // 3. Troca o valor do nó a ser removido pelo valor do último nó
            noParaRemover.valor = ultimoNo.valor;

            // 4. Remove a referência ao último nó do seu pai
            if (paiDoUltimo.direita == ultimoNo) {
                paiDoUltimo.direita = null;
            } else {
                paiDoUltimo.esquerda = null;
            }
        }

        campoDeEntrada.setText("");
        painelDesenho.repaint();
    }
    
    // --- MÉTODOS AUXILIARES RECURSIVOS ---

    /**
     * Calcula a altura de uma árvore/sub-árvore.
     */
    private int calcularAltura(No no) {
        if (no == null) {
            return 0;
        }
        return 1 + Math.max(calcularAltura(no.esquerda), calcularAltura(no.direita));
    }

    /**
     * Tenta inserir um novo nó em um nível específico. Retorna true se conseguiu.
     */
    private boolean tentarInserirNoNivel(No noAtual, No novoNo, int nivelAlvo, int nivelAtual) {
        if (noAtual == null) {
            return false;
        }

        // Se estamos no nível PAI do nível alvo, tentamos inserir nos filhos
        if (nivelAtual == nivelAlvo - 1) {
            if (noAtual.esquerda == null) {
                noAtual.esquerda = novoNo;
                return true;
            }
            if (noAtual.direita == null) {
                noAtual.direita = novoNo;
                return true;
            }
        }

        // Continua a busca recursivamente nos níveis inferiores
        if (tentarInserirNoNivel(noAtual.esquerda, novoNo, nivelAlvo, nivelAtual + 1)) {
            return true;
        }
        return tentarInserirNoNivel(noAtual.direita, novoNo, nivelAlvo, nivelAtual + 1);
    }
    
    /**
     * Busca um nó com um valor específico na árvore.
     */
    private No buscarNo(No noAtual, String valor) {
        if (noAtual == null) {
            return null;
        }
        if (noAtual.valor.equals(valor)) {
            return noAtual;
        }
        No encontradoNaEsquerda = buscarNo(noAtual.esquerda, valor);
        if (encontradoNaEsquerda != null) {
            return encontradoNaEsquerda;
        }
        return buscarNo(noAtual.direita, valor);
    }

    /**
     * Encontra o último nó da árvore (mais profundo, mais à direita) e seu pai.
     */
    private void encontrarUltimoNo(No noAtual, No pai, int nivel, InfoUltimoNo info) {
        if (noAtual == null) {
            return;
        }
        
        // Se encontrarmos um nó em um nível mais profundo, ele se torna o novo "último"
        // O ">=" garante que, no mesmo nível, o nó mais à direita será o escolhido
        if (nivel >= info.nivel) {
            info.no = noAtual;
            info.pai = pai;
            info.nivel = nivel;
        }
        
        encontrarUltimoNo(noAtual.esquerda, noAtual, nivel + 1, info);
        encontrarUltimoNo(noAtual.direita, noAtual, nivel + 1, info);
    }


    // --- CLASSE INTERNA DE DESENHO ---
    private class VisualizacaoPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (raiz != null) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                desenharArvore(g2d, raiz, getWidth() / 2, 30, getWidth() / 4);
            }
        }

        private void desenharArvore(Graphics2D g2d, No no, int x, int y, int espacoHorizontal) {
            if (no == null) {
                return;
            }
            // Desenha o nó
            g2d.setColor(Color.ORANGE);
            g2d.fillOval(x - DIAMETRO_NO / 2, y - DIAMETRO_NO / 2, DIAMETRO_NO, DIAMETRO_NO);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(x - DIAMETRO_NO / 2, y - DIAMETRO_NO / 2, DIAMETRO_NO, DIAMETRO_NO);
            // Desenha o valor
            FontMetrics fm = g2d.getFontMetrics();
            int stringWidth = fm.stringWidth(no.valor);
            g2d.drawString(no.valor, x - stringWidth / 2, y + fm.getAscent() / 2);
            // Desenha a conexão e o filho da esquerda
            if (no.esquerda != null) {
                int xEsquerda = x - espacoHorizontal;
                int yEsquerda = y + ESPACO_VERTICAL;
                g2d.drawLine(x, y + DIAMETRO_NO / 2, xEsquerda, yEsquerda - DIAMETRO_NO / 2);
                desenharArvore(g2d, no.esquerda, xEsquerda, yEsquerda, espacoHorizontal / 2);
            }
            // Desenha a conexão e o filho da direita
            if (no.direita != null) {
                int xDireita = x + espacoHorizontal;
                int yDireita = y + ESPACO_VERTICAL;
                g2d.drawLine(x, y + DIAMETRO_NO / 2, xDireita, yDireita - DIAMETRO_NO / 2);
                desenharArvore(g2d, no.direita, xDireita, yDireita, espacoHorizontal / 2);
            }
        }
    }
}