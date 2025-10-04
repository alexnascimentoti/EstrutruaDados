package br.edu.ufrb.estruturadados.algoritmosOrdenacao;

import java.util.List;
import java.util.function.BiConsumer;

/**
 *
 * @author kelly
 */
public class BubbleSort implements SortAlgorithm {

    @Override
    public void sort(List<Integer> numeros, BiConsumer<Integer, Integer> highlight) throws InterruptedException {
        for (int i = 0; i < numeros.size() - 1; i++) {
            for (int j = 0; j < numeros.size() - 1 - i; j++) {
                highlight.accept(j, j + 1);
                if (numeros.get(j) > numeros.get(j + 1)) {
                    int temp = numeros.get(j);
                    numeros.set(j, numeros.get(j + 1));
                    numeros.set(j + 1, temp);
                }
                Thread.sleep(10);
            }
        }
    }

    @Override
    public String getDescricao() {
        return """
        Bubble Sort:
        
        Compara elementos adjacentes e os troca se estiverem fora de ordem. Repete o processo até que nenhum elemento precise ser trocado. Simples de entender, mas ineficiente para listas grandes.

        Complexidade: 
        Melhor caso (lista já ordenada): O(n)
        Caso médio e pior caso: O(n²)""";
    }
}
