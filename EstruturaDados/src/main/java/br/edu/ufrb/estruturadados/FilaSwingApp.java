package br.edu.ufrb.estruturadados;

import javax.swing.*;
import java.awt.*;

/**
 * Implementação da Fila (Queue) FIFO
 * @author MSCF
 */
public class FilaSwingApp extends JPanel {

    // Nó da fila
    private static class Node {
        String data;
        Node next;

        Node(String data) {
            this.data = data;
            this.next = null;
        }
    }

    // Ponteiros para o INÍCIO (frente) e FIM (traseira) da fila
    private Node front; // INÍCIO da fila (quem será removido)
    private Node rear;  // FIM da fila (onde insere)
    private int size = 0;

    // Componentes Swing
    private JTextField campoValor;
    private JButton botaoEnfileirar, botaoDesenfileirar;
    private VisualizacaoPanel painelDesenho;

    public FilaSwingApp() {
        front = null;
        rear = null;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel painelControles = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        campoValor = new JTextField(8);

        botaoEnfileirar = new JButton("ENQUEUE (Inserir)");
        botaoDesenfileirar = new JButton("DEQUEUE (Remover)");

        painelControles.add(new JLabel("Valor:"));
        painelControles.add(campoValor);
        painelControles.add(botaoEnfileirar);
        painelControles.add(botaoDesenfileirar);

        add(painelControles, BorderLayout.NORTH);

        painelDesenho = new VisualizacaoPanel();
        add(painelDesenho, BorderLayout.CENTER);

        JTextArea textoDefinicao = new JTextArea(getDefinicao());
        textoDefinicao.setEditable(false);
        textoDefinicao.setFont(new Font("Serif", Font.ITALIC, 14));
        textoDefinicao.setLineWrap(true);
        textoDefinicao.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textoDefinicao);
        scrollPane.setPreferredSize(new Dimension(100, 80));
        add(scrollPane, BorderLayout.SOUTH);

        botaoEnfileirar.addActionListener(e -> enfileirar());
        botaoDesenfileirar.addActionListener(e -> desenfileirar());
    }

    private String getDefinicao() {
        return "FILA (FIFO - First-In, First-Out)\n\n" +
               "\u2713 Estrutura de dados que organiza elementos seguindo o princípio do 'Primeiro a entrar, Primeiro a sair'\n" +
               "\u2713 Inserção (ENQUEUE) ocorre no FIM (REAR) e remoção (DEQUEUE) ocorre no INÍCIO (FRONT).\n" +
               "\u2713 Exemplos práticos: filas de banco, impressão de documentos, atendimento em serviços.";
    }

    // --- LÓGICA DA FILA ---

    private void enfileirar() {
        String valor = campoValor.getText().trim();
        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Insira um valor.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Node novoNo = new Node(valor);
        if (rear == null) {
            front = novoNo;
            rear = novoNo;
        } else {
            rear.next = novoNo;
            rear = novoNo;
        }
        size++;
        campoValor.setText("");
        painelDesenho.repaint();
    }

    private void desenfileirar() {
        if (front == null) {
            JOptionPane.showMessageDialog(this, "A fila está vazia!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dadoRemovido = front.data;
        front = front.next;
        if (front == null) rear = null;
        size--;
        painelDesenho.repaint();

        JOptionPane.showMessageDialog(this, "Elemento removido (DEQUEUE): " + dadoRemovido, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    // --- VISUALIZAÇÃO DA FILA ---
    private class VisualizacaoPanel extends JPanel {
        private static final int NODE_WIDTH = 100;
        private static final int NODE_HEIGHT = 40;
        private static final int H_GAP = 10; // Espaçamento horizontal

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));

            if (front == null) {
                g2d.drawString("A fila está vazia.", getWidth() / 2 - 70, getHeight() / 2);
                return;
            }

            int nodeY = (getHeight() - NODE_HEIGHT) / 2;
            int x = 20;

            Node atual = front;
            while (atual != null) {
                // Destaca o INÍCIO (FRONT)
                if (atual == front) {
                    g2d.setColor(new Color(255, 230, 153)); // Amarelo claro
                    g2d.fillRect(x, nodeY, NODE_WIDTH, NODE_HEIGHT);
                    g2d.setColor(Color.RED);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawRect(x, nodeY, NODE_WIDTH, NODE_HEIGHT);
                    g2d.setStroke(new BasicStroke(1));
                } else {
                    g2d.setColor(new Color(204, 255, 204)); // Verde claro
                    g2d.fillRect(x, nodeY, NODE_WIDTH, NODE_HEIGHT);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, nodeY, NODE_WIDTH, NODE_HEIGHT);
                }

                // Valor
                FontMetrics fm = g2d.getFontMetrics();
                g2d.setColor(Color.BLACK);
                String data = atual.data;
                int textX = x + (NODE_WIDTH - fm.stringWidth(data)) / 2;
                int textY = nodeY + NODE_HEIGHT / 2 + fm.getAscent() / 2;
                g2d.drawString(data, textX, textY);

                // Seta para o próximo
                if (atual.next != null) {
                    g2d.setColor(new Color(0, 102, 204));
                    g2d.setStroke(new BasicStroke(2));
                    int x_start = x + NODE_WIDTH;
                    int x_end = x + NODE_WIDTH + H_GAP;
                    int y_center = nodeY + NODE_HEIGHT / 2;
                    g2d.drawLine(x_start, y_center, x_end, y_center);
                    g2d.fillPolygon(new int[]{x_end, x_end - 6, x_end - 6},
                                    new int[]{y_center, y_center - 6, y_center + 6}, 3);
                    g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                    g2d.drawString("next", x_start + 2, y_center - 8);
                    g2d.setFont(new Font("Arial", Font.BOLD, 14));
                }

                // Indicação de INÍCIO e FIM
                if (atual == front) {
                    g2d.setColor(Color.RED);
                    g2d.drawString("INÍCIO (FRONT)", x, nodeY - 8);
                }
                if (atual == rear) {
                    g2d.setColor(Color.BLUE);
                    g2d.drawString("FIM (REAR)", x, nodeY + NODE_HEIGHT + 18);
                }

                x += NODE_WIDTH + H_GAP;
                atual = atual.next;
            }
        }
    }

    // --- MÉTODO MAIN ---
    public static void main(String[] args) {
        JFrame frame = new JFrame("Fila");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new FilaSwingApp());
        frame.setSize(600, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}