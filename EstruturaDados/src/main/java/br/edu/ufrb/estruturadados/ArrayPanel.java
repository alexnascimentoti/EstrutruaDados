/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ufrb.estruturadados;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 2401210
 */
public class ArrayPanel extends JPanel {
     private List<String> array;
    private JTextField campoDeEntrada;
    private JButton botaoAdicionar, botaoRemover;
    private VisualizacaoPanel painelDesenho;

    public ArrayPanel() {
        // Inicializa a estrutura de dados interna
        array = new ArrayList<>();

        // Layout principal deste painel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Painel de Controles ---
        JPanel painelControles = new JPanel();
        campoDeEntrada = new JTextField(10);
        botaoAdicionar = new JButton("Adicionar");
        botaoRemover = new JButton("Remover (por índice)");

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
        scrollPane.setPreferredSize(new Dimension(100, 100)); // Altura do painel
        add(scrollPane, BorderLayout.SOUTH);

        // --- Lógica dos Botões (Listeners) ---
        botaoAdicionar.addActionListener(e -> adicionarElemento());
        botaoRemover.addActionListener(e -> removerElemento());
    }
    
    private String getDefinicao() {
        return "ARRAY (ou Lista Dinâmica):\n\n" +
               "É uma coleção de itens armazenados em locais de memória contíguos. " +
               "Os elementos podem ser acessados diretamente usando um índice numérico.\n" +
               "Prós: Acesso rápido aos elementos (O(1)).\n" +
               "Contras: A inserção ou remoção de elementos no meio da lista pode ser lenta (O(n)), pois exige o deslocamento de outros elementos.";
    }

    private void adicionarElemento() {
        String valor = campoDeEntrada.getText().trim();
        if (!valor.isEmpty()) {
            array.add(valor);
            campoDeEntrada.setText("");
            painelDesenho.repaint(); // Manda o painel se redesenhar
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerElemento() {
        String indiceStr = campoDeEntrada.getText().trim();
        try {
            int indice = Integer.parseInt(indiceStr);
            if (indice >= 0 && indice < array.size()) {
                array.remove(indice);
                campoDeEntrada.setText("");
                painelDesenho.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Índice inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um índice numérico válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Classe interna para cuidar do desenho
    private class VisualizacaoPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int boxWidth = 60;
            int boxHeight = 40;
            int startX = 20;
            int y = 50;

            for (int i = 0; i < array.size(); i++) {
                int x = startX + i * (boxWidth + 10);

                // Desenha a caixa
                g2d.setColor(Color.CYAN);
                g2d.fillRect(x, y, boxWidth, boxHeight);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, boxWidth, boxHeight);

                // Desenha o valor dentro da caixa
                String valor = array.get(i);
                FontMetrics fm = g2d.getFontMetrics();
                int stringWidth = fm.stringWidth(valor);
                g2d.drawString(valor, x + (boxWidth - stringWidth) / 2, y + boxHeight / 2 + fm.getAscent() / 2);

                // Desenha o índice abaixo da caixa
                String indiceStr = String.valueOf(i);
                int indexStringWidth = fm.stringWidth(indiceStr);
                g2d.drawString(indiceStr, x + (boxWidth - indexStringWidth) / 2, y + boxHeight + 20);
            }
        }
    }
}
