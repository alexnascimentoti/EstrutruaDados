package br.edu.ufrb.estruturadados.algoritmosOrdenacao;

import java.util.List;
import java.util.function.BiConsumer;

/**
 *
 * @author kelly
 */
public class SelectionSort implements SortAlgorithm {

    @Override
    public void sort(List<Integer> numeros, BiConsumer<Integer, Integer> highlight) throws InterruptedException {
        for (int i = 0; i < numeros.size() - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < numeros.size(); j++) {
                highlight.accept(j, minIndex);
                if (numeros.get(j) < numeros.get(minIndex)) {
                    minIndex = j;
                }
                Thread.sleep(10);
            }
            int temp = numeros.get(i);
            numeros.set(i, numeros.get(minIndex));
            numeros.set(minIndex, temp);
        }
    }

    @Override
    public String getDescricao() {
        return """
            Selection Sort:
            
            Percorre a lista repetidamente, selecionando o menor (ou maior) elemento da parte não ordenada e trocando-o com o elemento na posição correta. Funciona bem para listas pequenas, mas não é eficiente para listas grandes.

            Complexidade: 
            Melhor caso, caso médio e pior caso: O(n²)""";
    }
}
