package br.edu.ufrb.estruturadados;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Implementação da Pilha (Stack) LIFO
  * * @author MSCF
 */
public class PilhaSwingApp extends JPanel {

    
    // Pilha só precisa de um ponteiro para o próximo elemento.
    private static class Node {
        String data;
        // Referência para o próximo na pilha
        Node next; 

        Node(String data) {
            this.data = data;
            this.next = null;
        }
    }

    // --- 2. VARIÁVEIS DA PILHA E DO SWING ---
    
    // Na Pilha, 'head' será nosso 'TOPO'.
    private Node head; // Ponteiro para o TOPO da pilha
    private int size = 0;

    // Componentes Swing 
    private JTextField campoValor;
    // Push/Pop
    private JButton botaoAddInicio, botaoRemover; 
    private VisualizacaoPanel painelDesenho;

    public PilhaSwingApp() {
        head = null;
        
        // --- Configuração do Layout e Controles ---
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel painelControles = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        campoValor = new JTextField(8);
        
        // Renomeamos os botões para as operações de Pilha: PUSH e POP
        botaoAddInicio = new JButton("PUSH (Inserir)");
        botaoRemover = new JButton("POP (Remover do Topo)");
        
        painelControles.add(new JLabel("Valor:"));
        painelControles.add(campoValor);
        painelControles.add(botaoAddInicio);
        painelControles.add(botaoRemover);
        
        add(painelControles, BorderLayout.NORTH);

        painelDesenho = new VisualizacaoPanel();
        add(painelDesenho, BorderLayout.CENTER);

        JTextArea textoDefinicao = new JTextArea(getDefinicao());
        textoDefinicao.setEditable(false);
        textoDefinicao.setFont(new Font("Serif", Font.ITALIC, 14));
        textoDefinicao.setLineWrap(true);
        textoDefinicao.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textoDefinicao);
        scrollPane.setPreferredSize(new Dimension(100, 80)); // Altura menor
        add(scrollPane, BorderLayout.SOUTH);

        // --- Listeners dos Botões ---
        // Usamos botaoAddInicio para PUSH
        botaoAddInicio.addActionListener(e -> push()); 
        // Usamos botaoRemover para POP
        botaoRemover.addActionListener(e -> pop());
    }

    private String getDefinicao() {
        return "PILHA (LIFO - Last-In, First-Out)\n\n"+
               "\u2713 Estrutura de dados abastrata que organiza elementos seguindo o princípio do 'Ultimo a entrar, Primeiro a sair'\n"+
               "\u2713 A inserção (PUSH) e remoção (POP) ocorrem apenas no TOPO (HEAD). \n" +
               "\u2713 A referência 'next' (ponteiro) é usada para apontar para o elemento que está abaixo na pilha.\n" +
               "\u2713 Exemplos práticos: histórico de navegação de um navegador e a funcionalidade de \"desfazer/refazer\" em editores de texto.\n ";
        
    }

    

    // --- 3. LÓGICA DE MANIPULAÇÃO DA PILHA ---

    // A operação 'adicionarNoInicio' se torna 'push'
    private void push() {
        String valor = campoValor.getText().trim();
        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Insira um valor.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Node novoNo = new Node(valor);
        
        // Novo nó aponta para o antigo topo
        novoNo.next = head;
        
        // O topo (head) é atualizado para o novo nó
        head = novoNo; 
        
        size++;
        campoValor.setText("");
        painelDesenho.repaint();
    }

    // A operação 'removerElemento' (com índice) se torna 'pop' (sem índice)
    private void pop() {
        if (head == null) {
            JOptionPane.showMessageDialog(this, "A pilha está vazia!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // O dado do topo é salvo para ser exibido
        String dadoRemovido = head.data; 
        
        // O topo (head) avança para o próximo nó (o antigo segundo elemento)
        head = head.next; 
        
        size--;
        painelDesenho.repaint();
        
        JOptionPane.showMessageDialog(this, "Elemento removido (POP): " + dadoRemovido, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // --- 4. CLASSE INTERNA DE DESENHO (Visualização LIFO) ---
    private class VisualizacaoPanel extends JPanel {
        
        // Parâmetros de desenho ajustados para a visualização da pilha
        private static final int NODE_WIDTH = 100;
        private static final int NODE_HEIGHT = 40;
        private static final int V_GAP = 10; // Espaçamento vertical

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Casting para Graphics2D
            Graphics2D g2d = (Graphics2D) g; 
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));

            if (head == null) {
                g2d.drawString("A pilha está vazia.", getWidth() / 2 - 70, getHeight() / 2);
                return;
            }

            int nodeX = (getWidth() - NODE_WIDTH) / 2; // Centraliza horizontalmente
            
            // ALTERAÇÃO 1: Começa a desenhar no TOPO da tela (y=20), descendo.
            int y = 20;
            
            Node atual = head;

            // Percorre a pilha, do topo para a base
            while (atual != null) {
                
                // --- 1. Desenho do Bloco (Nó) - Destaque para o TOPO (HEAD) ---
                
                // Configurações de cor e borda
                if (atual == head) {
                    // Destaque para o TOPO (HEAD) - O elemento novo inserido
                    g2d.setColor(new Color(255, 230, 153)); // Amarelo/Laranja claro
                    g2d.fillRect(nodeX, y, NODE_WIDTH, NODE_HEIGHT);
                    
                    // Borda mais grossa para o TOPO
                    g2d.setColor(Color.RED);
                    g2d.setStroke(new BasicStroke(3)); 
                    g2d.drawRect(nodeX, y, NODE_WIDTH, NODE_HEIGHT);
                    g2d.setStroke(new BasicStroke(1)); // Volta a espessura padrão
                } else {
                    // Nós internos (verde, como o usuário mencionou)
                    g2d.setColor(new Color(204, 255, 204)); 
                    g2d.fillRect(nodeX, y, NODE_WIDTH, NODE_HEIGHT);
                    
                    // Borda padrão
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(nodeX, y, NODE_WIDTH, NODE_HEIGHT);
                }

                // Desenha o valor
                FontMetrics fm = g2d.getFontMetrics();
                g2d.setColor(Color.BLACK);
                String data = atual.data;
                int textX = nodeX + (NODE_WIDTH - fm.stringWidth(data)) / 2;
                int textY = y + NODE_HEIGHT / 2 + fm.getAscent() / 2;
                g2d.drawString(data, textX, textY);
                
                
                // --- 2. Desenho do Ponteiro/Referência (Seta Apontando para Baixo) ---
                
                if (atual.next != null) {
                    // A seta aponta para o próximo nó, que está abaixo na tela
                    g2d.setColor(new Color(0, 102, 204)); // Azul escuro
                    g2d.setStroke(new BasicStroke(2)); 
                    
                    int x_center = nodeX + NODE_WIDTH / 2;
                    int y_start = y + NODE_HEIGHT; // Saída na parte inferior do nó atual
                    int y_end = y + NODE_HEIGHT + V_GAP; // Chegada no espaço antes do próximo nó
                    
                    // Desenha a linha vertical
                    g2d.drawLine(x_center, y_start, x_center, y_end);
                    
                    // Adiciona a seta apontando para baixo
                    g2d.setColor(new Color(0, 102, 204));
                    g2d.fillPolygon(new int[]{x_center - 5, x_center + 5, x_center}, 
                                    new int[]{y_end - 6, y_end - 6, y_end}, 3);

                    // Adiciona um texto "next" para clareza
                    g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                    g2d.drawString("next", x_center + 8, y_start + V_GAP / 2);
                    g2d.setFont(new Font("Arial", Font.BOLD, 14)); 
                }
                
                // --- 3. Indicação de TOPO ---
                if (atual == head) {
                    g2d.setColor(Color.RED);
                    g2d.drawString("TOPO (HEAD)", nodeX + NODE_WIDTH + 10, y + NODE_HEIGHT / 2 + fm.getAscent() / 2);
                }
                
                // ALTERAÇÃO 2: Prepara para desenhar o próximo nó, DESCENDO na tela
                y += (NODE_HEIGHT + V_GAP); 
                
                // Move para o próximo nó (o ponteiro 'next')
                atual = atual.next; 
            }
        }
    }
    
    // --- 5. MÉTODO MAIN ---

    public static void main(String[] args) {
        // Cria o JFrame principal
        JFrame frame = new JFrame("Pilha");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new PilhaSwingApp());
        frame.setSize(450, 600);
        frame.setLocationRelativeTo(null); // Centraliza a janela
        frame.setVisible(true);
    }
}