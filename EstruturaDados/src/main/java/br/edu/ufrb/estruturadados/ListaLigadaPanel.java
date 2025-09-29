/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ufrb.estruturadados;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 *
 * @author 2401210
 */
public class ListaLigadaPanel extends JPanel {
    private LinkedList<String> lista;
    private JTextField campoValor, campoIndice;
    private JButton botaoAddInicio, botaoAddFim, botaoRemover;
    private VisualizacaoPanel painelDesenho;

    public ListaLigadaPanel() {
        // Inicializa a estrutura de dados
        lista = new LinkedList<>();

        // Layout principal
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Painel de Controles ---
        JPanel painelControles = new JPanel();
        // Usando GridBagLayout para melhor alinhamento
        painelControles.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5); // Espaçamento

        campoValor = new JTextField(8);
        campoIndice = new JTextField(4);
        botaoAddInicio = new JButton("Adicionar no Início");
        botaoAddFim = new JButton("Adicionar no Fim");
        botaoRemover = new JButton("Remover (por índice)");

        // Linha 1 de controles
        gbc.gridx = 0; gbc.gridy = 0; painelControles.add(new JLabel("Valor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelControles.add(campoValor, gbc);
        gbc.gridx = 2; gbc.gridy = 0; painelControles.add(botaoAddInicio, gbc);
        gbc.gridx = 3; gbc.gridy = 0; painelControles.add(botaoAddFim, gbc);
        
        // Linha 2 de controles
        gbc.gridx = 0; gbc.gridy = 1; painelControles.add(new JLabel("Índice:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; painelControles.add(campoIndice, gbc);
        gbc.gridx = 2; gbc.gridy = 1; painelControles.add(botaoRemover, gbc);

        add(painelControles, BorderLayout.NORTH);

        // --- Painel de Visualização ---
        painelDesenho = new VisualizacaoPanel();
        add(painelDesenho, BorderLayout.CENTER);

        // --- Painel de Texto de Definição ---
        JTextArea textoDefinicao = new JTextArea(getDefinicao());
        textoDefinicao.setEditable(false);
        textoDefinicao.setFont(new Font("Serif", Font.ITALIC, 14));
        textoDefinicao.setLineWrap(true);
        textoDefinicao.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textoDefinicao);
        scrollPane.setPreferredSize(new Dimension(100, 120));
        add(scrollPane, BorderLayout.SOUTH);

        // --- Lógica dos Botões (Listeners) ---
        botaoAddFim.addActionListener(e -> adicionarNoFim());
        botaoAddInicio.addActionListener(e -> adicionarNoInicio());
        botaoRemover.addActionListener(e -> removerElemento());
    }

    private String getDefinicao() {
        return "LISTA LIGADA (ou Encadeada):\n\n" +
               "É uma coleção linear de elementos chamados 'nós'. Cada nó armazena um valor e uma referência (ou 'ponteiro') para o próximo nó da sequência. " +
               "Diferente do array, os nós não precisam estar em posições contíguas de memória.\n" +
               "Prós: Inserção e remoção de elementos são muito eficientes (O(1)) se a posição for conhecida (início/fim).\n" +
               "Contras: O acesso a um elemento no meio da lista é lento (O(n)), pois é preciso percorrer a lista desde o início.";
    }

    private void adicionarNoFim() {
        String valor = campoValor.getText().trim();
        if (!valor.isEmpty()) {
            lista.addLast(valor);
            campoValor.setText("");
            painelDesenho.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void adicionarNoInicio() {
        String valor = campoValor.getText().trim();
        if (!valor.isEmpty()) {
            lista.addFirst(valor);
            campoValor.setText("");
            painelDesenho.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerElemento() {
        String indiceStr = campoIndice.getText().trim();
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "A lista está vazia!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int indice = Integer.parseInt(indiceStr);
            if (indice >= 0 && indice < lista.size()) {
                lista.remove(indice);
                campoIndice.setText("");
                painelDesenho.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Índice fora dos limites da lista.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um índice numérico válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Classe interna responsável por todo o desenho da lista
    private class VisualizacaoPanel extends JPanel {
        private static final int NODE_WIDTH = 70;
        private static final int POINTER_WIDTH = 30;
        private static final int NODE_HEIGHT = 40;
        private static final int H_GAP = 50; // Espaço horizontal entre nós

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            
            if (lista.isEmpty()) {
                g2d.drawString("A lista está vazia. Adicione um elemento!", getWidth() / 2 - 150, getHeight() / 2);
                g2d.drawString("HEAD -> null", 20, 50);
                return;
            }
            
            int startX = 20;
            int y = getHeight() / 2 - NODE_HEIGHT / 2;

            // Desenha o ponteiro "HEAD"
            g2d.drawString("HEAD", startX, y - 20);
            drawArrow(g2d, startX + 20, y - 10, startX + 20, y);


            for (int i = 0; i < lista.size(); i++) {
                int nodeX = startX + i * (NODE_WIDTH + POINTER_WIDTH + H_GAP);

                // Desenha o nó (caixa de dados)
                g2d.setColor(new Color(144, 238, 144)); // Verde claro
                g2d.fillRect(nodeX, y, NODE_WIDTH, NODE_HEIGHT);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(nodeX, y, NODE_WIDTH, NODE_HEIGHT);
                
                // Desenha o valor dentro do nó
                String valor = lista.get(i);
                FontMetrics fm = g2d.getFontMetrics();
                int stringWidth = fm.stringWidth(valor);
                g2d.drawString(valor, nodeX + (NODE_WIDTH - stringWidth) / 2, y + NODE_HEIGHT / 2 + fm.getAscent() / 2);

                // Desenha a caixa do ponteiro
                int pointerX = nodeX + NODE_WIDTH;
                g2d.setColor(new Color(173, 216, 230)); // Azul claro
                g2d.fillRect(pointerX, y, POINTER_WIDTH, NODE_HEIGHT);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(pointerX, y, POINTER_WIDTH, NODE_HEIGHT);
                // Desenha um ponto no meio da caixa de ponteiro
                g2d.fillOval(pointerX + POINTER_WIDTH / 2 - 3, y + NODE_HEIGHT / 2 - 3, 6, 6);

                // Se não for o último nó, desenha a seta para o próximo
                if (i < lista.size() - 1) {
                    int startArrowX = pointerX + POINTER_WIDTH;
                    int endArrowX = nodeX + NODE_WIDTH + POINTER_WIDTH + H_GAP;
                    drawArrow(g2d, startArrowX, y + NODE_HEIGHT / 2, endArrowX, y + NODE_HEIGHT / 2);
                } else { // Se for o último nó, aponta para "null"
                    int startArrowX = pointerX + POINTER_WIDTH;
                    g2d.drawLine(startArrowX, y + NODE_HEIGHT / 2, startArrowX + H_GAP / 2, y + NODE_HEIGHT / 2);
                    g2d.drawLine(startArrowX + H_GAP / 2, y + NODE_HEIGHT / 2, startArrowX + H_GAP / 2, y + NODE_HEIGHT + 20);
                    g2d.drawLine(startArrowX + H_GAP / 2 - 10, y + NODE_HEIGHT + 20, startArrowX + H_GAP / 2 + 10, y + NODE_HEIGHT + 20);
                    g2d.drawString("null", startArrowX + H_GAP/2 - 15, y + NODE_HEIGHT + 40);
                }
            }
        }

        // Método auxiliar para desenhar setas
        private void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
            Graphics2D g2d = (Graphics2D) g.create();
            double dx = x2 - x1, dy = y2 - y1;
            double angle = Math.atan2(dy, dx);
            int len = (int) Math.sqrt(dx * dx + dy * dy);
            
            // Desenha a linha principal
            g2d.drawLine(x1, y1, x2, y2);

            // Desenha a ponta da seta
            Polygon arrowHead = new Polygon();
            arrowHead.addPoint(x2, y2);
            arrowHead.addPoint(x2 - 10, y2 - 5);
            arrowHead.addPoint(x2 - 10, y2 + 5);
            
            // Rotaciona a ponta da seta para a direção correta
            java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();
            tx.translate(x1, y1);
            tx.rotate(angle);
            tx.translate(len - 10, 0); // posiciona a seta um pouco antes do fim da linha
            
            g2d.setTransform(tx);
            g2d.fill(arrowHead);
        }
    }
}
