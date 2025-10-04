package br.edu.ufrb.estruturadados.algoritmosOrdenacao;

import java.util.List;
import java.util.function.BiConsumer;

/**
 *
 * @author kelly
 */
public class InsertionSort implements SortAlgorithm {

    @Override
    public void sort(List<Integer> numeros, BiConsumer<Integer, Integer> highlight) throws InterruptedException {
        for (int i = 1; i < numeros.size(); i++) {
            int chave = numeros.get(i);
            int j = i - 1;
            while (j >= 0 && numeros.get(j) > chave) {
                highlight.accept(j, j + 1);
                numeros.set(j + 1, numeros.get(j));
                j--;
                Thread.sleep(10);
            }
            numeros.set(j + 1, chave);
        }
    }

    @Override
    public String getDescricao() {
        return """
            Insertion Sort:
            
            Constrói a lista ordenada elemento por elemento. Para cada elemento, encontra a posição correta e o insere, deslocando os elementos maiores para a direita. Muito eficiente para listas quase ordenadas.

            Complexidade:
            Melhor caso (lista já ordenada): O(n)
            Caso médio e pior caso: O(n²)""";
    }
}
