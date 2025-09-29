/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ufrb.estruturadados;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author 2401210
 */
public class ListaDuplamenteEncadeadaPanel extends JPanel {
    // Classe interna para representar nosso Nó Duplo
    private static class Node {
        String data;
        Node prev;
        Node next;

        Node(String data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
    }

    private Node head; // Ponteiro para o início da lista
    private Node tail; // Ponteiro para o fim da lista
    private int size = 0;

    private JTextField campoValor, campoIndice;
    private JButton botaoAddInicio, botaoAddFim, botaoRemover;
    private VisualizacaoPanel painelDesenho;

    public ListaDuplamenteEncadeadaPanel() {
        head = null;
        tail = null;

        // --- Configuração do Layout e Controles (similar aos outros painéis) ---
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel painelControles = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);

        campoValor = new JTextField(8);
        campoIndice = new JTextField(4);
        botaoAddInicio = new JButton("Adicionar no Início");
        botaoAddFim = new JButton("Adicionar no Fim");
        botaoRemover = new JButton("Remover (por índice)");

        gbc.gridx = 0; gbc.gridy = 0; painelControles.add(new JLabel("Valor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelControles.add(campoValor, gbc);
        gbc.gridx = 2; gbc.gridy = 0; painelControles.add(botaoAddInicio, gbc);
        gbc.gridx = 3; gbc.gridy = 0; painelControles.add(botaoAddFim, gbc);
        gbc.gridx = 0; gbc.gridy = 1; painelControles.add(new JLabel("Índice:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; painelControles.add(campoIndice, gbc);
        gbc.gridx = 2; gbc.gridy = 1; painelControles.add(botaoRemover, gbc);

        add(painelControles, BorderLayout.NORTH);

        painelDesenho = new VisualizacaoPanel();
        add(painelDesenho, BorderLayout.CENTER);

        JTextArea textoDefinicao = new JTextArea(getDefinicao());
        textoDefinicao.setEditable(false);
        textoDefinicao.setFont(new Font("Serif", Font.ITALIC, 14));
        textoDefinicao.setLineWrap(true);
        textoDefinicao.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textoDefinicao);
        scrollPane.setPreferredSize(new Dimension(100, 120));
        add(scrollPane, BorderLayout.SOUTH);

        // --- Listeners dos Botões ---
        botaoAddFim.addActionListener(e -> adicionarNoFim());
        botaoAddInicio.addActionListener(e -> adicionarNoInicio());
        botaoRemover.addActionListener(e -> removerElemento());
    }

    private String getDefinicao() {
        return "LISTA DUPLAMENTE ENCADEADA:\n\n" +
               "Uma versão avançada da lista encadeada. Cada 'nó' armazena um valor, um ponteiro para o próximo nó, e um ponteiro para o nó anterior. " +
               "Isso permite que a lista seja percorrida em ambas as direções (para frente e para trás).\n" +
               "Prós: Remoção de um nó é mais eficiente (se você já tem a referência para ele) e a travessia reversa é trivial.\n" +
               "Contras: Usa mais memória devido ao ponteiro extra para o nó anterior.";
    }

    // --- Lógica de Manipulação da Lista ---

    private void adicionarNoInicio() {
        String valor = campoValor.getText().trim();
        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Insira um valor.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Node novoNo = new Node(valor);
        if (head == null) { // Lista vazia
            head = novoNo;
            tail = novoNo;
        } else {
            head.prev = novoNo;
            novoNo.next = head;
            head = novoNo;
        }
        size++;
        campoValor.setText("");
        painelDesenho.repaint();
    }

    private void adicionarNoFim() {
        String valor = campoValor.getText().trim();
        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Insira um valor.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Node novoNo = new Node(valor);
        if (head == null) { // Lista vazia
            head = novoNo;
            tail = novoNo;
        } else {
            tail.next = novoNo;
            novoNo.prev = tail;
            tail = novoNo;
        }
        size++;
        campoValor.setText("");
        painelDesenho.repaint();
    }

    private void removerElemento() {
        if (head == null) {
            JOptionPane.showMessageDialog(this, "A lista está vazia!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int indice = Integer.parseInt(campoIndice.getText().trim());
            if (indice < 0 || indice >= size) {
                JOptionPane.showMessageDialog(this, "Índice inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Node noParaRemover;
            if (indice == 0) { // Remover o primeiro
                noParaRemover = head;
                head = head.next;
                if (head != null) {
                    head.prev = null;
                } else {
                    tail = null; // A lista ficou vazia
                }
            } else if (indice == size - 1) { // Remover o último
                noParaRemover = tail;
                tail = tail.prev;
                tail.next = null;
            } else { // Remover do meio
                Node atual = head;
                for (int i = 0; i < indice; i++) {
                    atual = atual.next;
                }
                noParaRemover = atual;
                atual.prev.next = atual.next;
                atual.next.prev = atual.prev;
            }
            size--;
            campoIndice.setText("");
            painelDesenho.repaint();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Insira um índice numérico válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Classe de Desenho ---
    private class VisualizacaoPanel extends JPanel {
        private static final int DATA_WIDTH = 70;
        private static final int POINTER_WIDTH = 25;
        private static final int NODE_HEIGHT = 40;
        private static final int H_GAP = 40;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));

            if (head == null) {
                g2d.drawString("A lista está vazia.", getWidth() / 2 - 70, getHeight() / 2);
                return;
            }

            int startX = 20;
            int y = getHeight() / 2 - NODE_HEIGHT / 2;
            Node atual = head;
            int i = 0;

            while (atual != null) {
                int nodeX = startX + i * (POINTER_WIDTH + DATA_WIDTH + POINTER_WIDTH + H_GAP);

                // --- Desenho do Nó ---
                // Caixa do ponteiro ANTERIOR
                g2d.setColor(new Color(255, 204, 204)); // Vermelho claro
                g2d.fillRect(nodeX, y, POINTER_WIDTH, NODE_HEIGHT);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(nodeX, y, POINTER_WIDTH, NODE_HEIGHT);

                // Caixa do DADO
                g2d.setColor(new Color(204, 255, 204)); // Verde claro
                g2d.fillRect(nodeX + POINTER_WIDTH, y, DATA_WIDTH, NODE_HEIGHT);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(nodeX + POINTER_WIDTH, y, DATA_WIDTH, NODE_HEIGHT);

                // Caixa do ponteiro PRÓXIMO
                g2d.setColor(new Color(204, 204, 255)); // Azul claro
                g2d.fillRect(nodeX + POINTER_WIDTH + DATA_WIDTH, y, POINTER_WIDTH, NODE_HEIGHT);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(nodeX + POINTER_WIDTH + DATA_WIDTH, y, POINTER_WIDTH, NODE_HEIGHT);

                // Desenha o valor
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(atual.data, nodeX + POINTER_WIDTH + (DATA_WIDTH - fm.stringWidth(atual.data)) / 2, y + NODE_HEIGHT / 2 + fm.getAscent() / 2);

                // --- Desenho das Setas ---
                // Seta para PRÓXIMO (em cima)
                if (atual.next != null) {
                    g2d.setColor(Color.BLUE);
                    int startArrowX = nodeX + POINTER_WIDTH * 2 + DATA_WIDTH;
                    int endArrowX = startArrowX + H_GAP;
                    drawArrow(g2d, startArrowX, y + NODE_HEIGHT / 3, endArrowX, y + NODE_HEIGHT / 3);
                }

                // Seta para ANTERIOR (em baixo)
                if (atual.prev != null) {
                    g2d.setColor(Color.RED);
                    int startArrowX = nodeX;
                    int endArrowX = nodeX - H_GAP;
                    drawArrow(g2d, startArrowX, y + (NODE_HEIGHT * 2) / 3, endArrowX, y + (NODE_HEIGHT * 2) / 3);
                }
                
                // Ponteiros Head e Tail
                if(atual == head) g2d.drawString("HEAD", nodeX + DATA_WIDTH/2, y - 15);
                if(atual == tail) g2d.drawString("TAIL", nodeX + DATA_WIDTH/2, y + NODE_HEIGHT + 25);


                atual = atual.next;
                i++;
            }
        }
        
        // Método para desenhar setas
        private void drawArrow(Graphics2D g, int x1, int y1, int x2, int y2) {
            g.drawLine(x1, y1, x2, y2);
            int dx = x2 - x1, dy = y2 - y1;
            double angle = Math.atan2(dy, dx);
            int len = (int) Math.sqrt(dx*dx + dy*dy);
            Polygon arrowHead = new Polygon();
            arrowHead.addPoint(0, 5);
            arrowHead.addPoint(0, -5);
            arrowHead.addPoint(10, 0);

            java.awt.geom.AffineTransform tx = java.awt.geom.AffineTransform.getTranslateInstance(x2, y2);
            tx.rotate(angle);

            g.setTransform(tx);
            g.fill(arrowHead);
            g.setTransform(new java.awt.geom.AffineTransform()); // Reseta a transformação
        }
    }
}
