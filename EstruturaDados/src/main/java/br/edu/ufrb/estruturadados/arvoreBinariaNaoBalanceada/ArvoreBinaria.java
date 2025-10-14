package br.edu.ufrb.estruturadados.arvoreBinariaNaoBalanceada;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kelly
 */

public class ArvoreBinaria {

    public static class No {
        public int valor;
        public No esquerda, direita;

        No(int valor) {
            this.valor = valor;
        }
    }

    private No raiz;

    public No getRaiz() {
        return raiz;
    }

    public void limpar() {
        raiz = null;
    }

    public void inserir(int valor) {
        raiz = inserirRec(raiz, valor);
    }

    private No inserirRec(No atual, int valor) {
        if (atual == null) {
            return new No(valor);
        }
        if (valor < atual.valor) {
            atual.esquerda = inserirRec(atual.esquerda, valor);
        } else if (valor > atual.valor) {
            atual.direita = inserirRec(atual.direita, valor);
        }
        return atual;
    }

    public boolean buscar(int valor) {
        return buscarRec(raiz, valor);
    }

    private boolean buscarRec(No atual, int valor) {
        if (atual == null) return false;
        if (atual.valor == valor) return true;
        return valor < atual.valor
                ? buscarRec(atual.esquerda, valor)
                : buscarRec(atual.direita, valor);
    }

    public boolean remover(int valor) {
        if (!buscar(valor)) {
            return false;
        }
        raiz = removerRec(raiz, valor);
        return true;
    }

    private No removerRec(No atual, int valor) {
        if (atual == null) return null;

        if (valor < atual.valor) {
            atual.esquerda = removerRec(atual.esquerda, valor);
        } else if (valor > atual.valor) {
            atual.direita = removerRec(atual.direita, valor);
        } else {
            if (atual.esquerda == null) return atual.direita;
            else if (atual.direita == null) return atual.esquerda;
            
            atual.valor = menorValor(atual.direita);
            atual.direita = removerRec(atual.direita, atual.valor);
        }
        return atual;
    }

    private int menorValor(No no) {
        int menor = no.valor;
        while (no.esquerda != null) {
            menor = no.esquerda.valor;
            no = no.esquerda;
        }
        return menor;
    }

    public List<No> getPercorsoEmOrdem() {
        List<No> percurso = new ArrayList<>();
        percorrerEmOrdem(raiz, percurso);
        return percurso;
    }

    private void percorrerEmOrdem(No no, List<No> percurso) {
        if (no != null) {
            percorrerEmOrdem(no.esquerda, percurso);
            percurso.add(no);
            percorrerEmOrdem(no.direita, percurso);
        }
    }

    public List<No> getPercorsoPreOrdem() {
        List<No> percurso = new ArrayList<>();
        percorrerPreOrdem(raiz, percurso);
        return percurso;
    }

    private void percorrerPreOrdem(No no, List<No> percurso) {
        if (no != null) {
            percurso.add(no);
            percorrerPreOrdem(no.esquerda, percurso);
            percorrerPreOrdem(no.direita, percurso);
        }
    }

    public List<No> getPercorsoPosOrdem() {
        List<No> percurso = new ArrayList<>();
        percorrerPosOrdem(raiz, percurso);
        return percurso;
    }

    private void percorrerPosOrdem(No no, List<No> percurso) {
        if (no != null) {
            percorrerPosOrdem(no.esquerda, percurso);
            percorrerPosOrdem(no.direita, percurso);
            percurso.add(no);
        }
    }

    public int getAltura() {
        return calcularAltura(raiz);
    }

    private int calcularAltura(No no) {
        if (no == null) return -1;
        return 1 + Math.max(calcularAltura(no.esquerda), calcularAltura(no.direita));
    }

    public int getNumeroNos() {
        return contarNos(raiz);
    }

    private int contarNos(No no) {
        if (no == null) return 0;
        return 1 + contarNos(no.esquerda) + contarNos(no.direita);
    }

    public int getNumeroFolhas() {
        return contarFolhas(raiz);
    }

    private int contarFolhas(No no) {
        if (no == null) return 0;
        if (no.esquerda == null && no.direita == null) return 1;
        return contarFolhas(no.esquerda) + contarFolhas(no.direita);
    }
}