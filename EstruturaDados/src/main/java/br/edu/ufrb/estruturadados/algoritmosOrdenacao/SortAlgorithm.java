package br.edu.ufrb.estruturadados.algoritmosOrdenacao;

import java.util.List;
import java.util.function.BiConsumer;

/**
 *
 * @author kelly
 */
public interface SortAlgorithm {

    void sort(List<Integer> numeros, BiConsumer<Integer, Integer> highlight) throws InterruptedException;

    String getDescricao();
}
