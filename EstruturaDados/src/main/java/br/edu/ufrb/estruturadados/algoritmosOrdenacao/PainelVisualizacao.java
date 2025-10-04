package br.edu.ufrb.estruturadados.algoritmosOrdenacao;

import java.awt.*;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author kelly
 */
public class PainelVisualizacao extends JPanel {

    private List<Integer> numeros;
    private int indiceA = -1, indiceB = -1;

    public void atualizarLista(List<Integer> numeros) {
        this.numeros = numeros;
        repaint();
    }

    public void destacarIndices(int a, int b) {
        this.indiceA = a;
        this.indiceB = b;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (numeros == null || numeros.isEmpty()) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.GRAY);
            g2.setFont(new Font("SansSerif", Font.BOLD, 18));

            String msg = "A lista está vazia! Adicione ou gere valores para iniciar a visualização.";
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(msg)) / 2;
            int y = getHeight() / 2;
            g2.drawString(msg, x, y);
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int baseY = height - 40;
        int n = numeros.size();

        // Configurações de layout
        int margem = 30;
        int espacoEntreBarras = 5;

        // Calcula a largura das barras considerando os espaços entre elas
        int larguraDisponivel = width - 2 * margem - (n - 1) * espacoEntreBarras;
        int barWidth = Math.max(15, larguraDisponivel / n);

        // Centraliza o gráfico horizontalmente
        int totalWidth = n * barWidth + (n - 1) * espacoEntreBarras;
        int inicioX = (width - totalWidth) / 2;

        // Calcula escala de altura
        int maxValor = numeros.stream().max(Integer::compareTo).orElse(1);
        double escala = (double) (height - 100) / maxValor;

        for (int i = 0; i < n; i++) {
            int valor = numeros.get(i);
            int altura = (int) (valor * escala);
            int x = inicioX + i * (barWidth + espacoEntreBarras);
            int y = baseY - altura;

            // Cores de destaque
            if (i == indiceA) {
                g2d.setColor(Color.RED);
            } else if (i == indiceB) {
                g2d.setColor(Color.GREEN);
            } else {
                g2d.setColor(new Color(255, 140, 0));
            }

            // Desenha a barra
            g2d.fillRect(x, y, barWidth, altura);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, barWidth, altura);

            // Desenha o valor abaixo
            String texto = String.valueOf(valor);
            int strWidth = g2d.getFontMetrics().stringWidth(texto);
            g2d.drawString(texto, x + (barWidth - strWidth) / 2, baseY + 15);
        }

        g2d.dispose();
    }
}
