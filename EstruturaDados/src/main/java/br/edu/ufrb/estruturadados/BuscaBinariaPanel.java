package br.edu.ufrb.estruturadados;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Implementação da Busca Binária em uma lista ordenada
 * @author OLS
 */
public class BuscaBinariaPanel extends JPanel {
    private DefaultListModel<Integer> modeloLista;
    private JTextField campoValor, campoBusca;
    private JTextArea areaExplicacao;
    private JLabel resultadoBusca;

    public BuscaBinariaPanel() {
        setLayout(new BorderLayout(10, 10));

        // Painel de controles (igual ao de ordenação)
        JPanel painelControles = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        campoValor = new JTextField(5);
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnLimpar = new JButton("Limpar");
        JButton btnAleatorio = new JButton("Gerar Aleatório");
        campoBusca = new JTextField(5);
        JButton btnBuscar = new JButton("Buscar Binária");

        // Linha 1: Valor
        gbc.gridx = 0; gbc.gridy = 0;
        painelControles.add(new JLabel("Valor:"), gbc);
        gbc.gridx = 1;
        painelControles.add(campoValor, gbc);
        gbc.gridx = 2;
        painelControles.add(btnAdicionar, gbc);
        gbc.gridx = 3;
        painelControles.add(btnLimpar, gbc);

        // Linha 2: Tamanho/Aleatório
        gbc.gridx = 0; gbc.gridy = 1;
        painelControles.add(new JLabel("Tamanho:"), gbc);
        gbc.gridx = 1;
        JTextField campoTamanho = new JTextField(5);
        painelControles.add(campoTamanho, gbc);
        gbc.gridx = 2;
        painelControles.add(btnAleatorio, gbc);

        // Linha 3: Buscar
        gbc.gridx = 0; gbc.gridy = 2;
        painelControles.add(new JLabel("Buscar:"), gbc);
        gbc.gridx = 1;
        painelControles.add(campoBusca, gbc);
        gbc.gridx = 2;
        painelControles.add(btnBuscar, gbc);

        add(painelControles, BorderLayout.NORTH);

        // Painel central: lista e resultado
        JPanel painelCentral = new JPanel(new BorderLayout());
        modeloLista = new DefaultListModel<>();

        // Painel de visualização customizado
        VisualizacaoPanel painelDesenho = new VisualizacaoPanel();
        painelDesenho.setPreferredSize(new Dimension(600, 120));

        // Adiciona o painel de desenho dentro de um JScrollPane
        JScrollPane scrollPanel = new JScrollPane(painelDesenho);
        scrollPanel.setPreferredSize(new Dimension(600, 120));
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        painelCentral.add(scrollPanel, BorderLayout.CENTER);

        resultadoBusca = new JLabel("A lista está vazia! Adicione ou gere valores para iniciar a busca.", SwingConstants.CENTER);
        resultadoBusca.setFont(new Font("Arial", Font.PLAIN, 20));
        resultadoBusca.setForeground(Color.GRAY);
        painelCentral.add(resultadoBusca, BorderLayout.SOUTH);

        add(painelCentral, BorderLayout.CENTER);

        // Explicação
        areaExplicacao = new JTextArea(
            "Busca Binária:\n" +
            "Procura um valor em uma lista ordenada dividindo o intervalo de busca pela metade a cada passo.\n\n" +
            "Complexidade:\n" +
            "Melhor caso: O(1)\n" +
            "Caso médio/pior: O(log n)"
        );
        areaExplicacao.setEditable(false);
        areaExplicacao.setLineWrap(true);
        areaExplicacao.setWrapStyleWord(true);
        areaExplicacao.setFont(new Font("Arial", Font.ITALIC, 14));
        JScrollPane scrollExplicacao = new JScrollPane(areaExplicacao);
        scrollExplicacao.setPreferredSize(new Dimension(600, 80));
        add(scrollExplicacao, BorderLayout.SOUTH);

        // Ações dos botões
        btnAdicionar.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(campoValor.getText());
                modeloLista.addElement(valor);
                campoValor.setText("");
                ordenarLista();
                painelDesenho.revalidate();
                painelDesenho.repaint();
                atualizarMensagem();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite um número válido.");
            }
        });

        btnAleatorio.addActionListener(e -> {
            modeloLista.clear();
            int tamanho = 10;
            try {
                if (!campoTamanho.getText().isEmpty()) {
                    tamanho = Integer.parseInt(campoTamanho.getText());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite um tamanho válido.");
            }
            Random rand = new Random();
            for (int i = 0; i < tamanho; i++) {
                modeloLista.addElement(rand.nextInt(100));
            }
            ordenarLista();
            painelDesenho.revalidate();
            painelDesenho.repaint();
            atualizarMensagem();
        });

        btnBuscar.addActionListener(e -> {
            try {
                int valorBusca = Integer.parseInt(campoBusca.getText());
                java.util.List<Integer> valores = java.util.Collections.list(modeloLista.elements());
                int[] array = valores.stream().mapToInt(Integer::intValue).toArray();
                int idx = buscaBinaria(array, valorBusca);
                if (modeloLista.isEmpty()) {
                    resultadoBusca.setText("A lista está vazia! Adicione ou gere valores para iniciar a busca.");
                    resultadoBusca.setForeground(Color.GRAY);
                } else if (idx >= 0) {
                    resultadoBusca.setText("Valor encontrado no índice: " + idx);
                    resultadoBusca.setForeground(new Color(0, 128, 0));
                } else {
                    resultadoBusca.setText("Valor não encontrado.");
                    resultadoBusca.setForeground(Color.RED);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite um número válido para busca.");
            }
        });

        btnLimpar.addActionListener(e -> {
            modeloLista.clear();
            painelDesenho.revalidate();
            painelDesenho.repaint();
            atualizarMensagem();
        });

        atualizarMensagem();
    }

    private void ordenarLista() {
        java.util.List<Integer> valores = java.util.Collections.list(modeloLista.elements());
        java.util.Collections.sort(valores);
        modeloLista.clear();
        for (int v : valores) modeloLista.addElement(v);
    }

    private int buscaBinaria(int[] arr, int valor) {
        int inicio = 0, fim = arr.length - 1;
        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            if (arr[meio] == valor) return meio;
            else if (arr[meio] < valor) inicio = meio + 1;
            else fim = meio - 1;
        }
        return -1;
    }

    private void atualizarMensagem() {
        if (modeloLista.isEmpty()) {
            resultadoBusca.setText("A lista está vazia! Adicione ou gere valores para iniciar a busca.");
            resultadoBusca.setForeground(Color.GRAY);
        } else {
            resultadoBusca.setText(" ");
        }
    }

    class VisualizacaoPanel extends JPanel {
        @Override
        public Dimension getPreferredSize() {
            int boxWidth = 60;
            int espacamento = 10;
            int altura = 120;
            int largura = 20 + modeloLista.size() * (boxWidth + espacamento);
            return new Dimension(Math.max(largura, 600), altura);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int boxWidth = 60;
            int boxHeight = 40;
            int startX = 20;
            int y = 30;

            java.util.List<Integer> valores = java.util.Collections.list(modeloLista.elements());
            for (int i = 0; i < valores.size(); i++) {
                int x = startX + i * (boxWidth + 10);

                // Caixa
                g2d.setColor(new Color(200, 220, 255));
                g2d.fillRect(x, y, boxWidth, boxHeight);
                g2d.setColor(Color.BLACK);
                // ValorRect(x, y, boxWidth, boxHeight);
                String valorStr = String.valueOf(valores.get(i));
                FontMetrics fm = g2d.getFontMetrics();
                int stringWidth = fm.stringWidth(valorStr);
                g2d.drawString(valorStr, x + (boxWidth - stringWidth) / 2, y + boxHeight / 2 + fm.getAscent() / 2);
                String indiceStr = String.valueOf(i);
                int indexStringWidth = fm.stringWidth(indiceStr);
                g2d.drawString(indiceStr, x + (boxWidth - indexStringWidth) / 2, y + boxHeight + 15);
            }
        }
    }
}