package jogo;

import java.util.Random;
import java.lang.Math;
import java.util.Vector;
import excecoes.ExcecaoCartaRepetida;
import excecoes.ExcecaoCartasInsuficientes;


public class Baralho {
    private Vector<Carta> cartas = new Vector<>();

    public Baralho() {
        for (Carta.Naipe n : Carta.Naipe.values()) {
            for (Carta.Valor v : Carta.Valor.values()) {
                this.cartas.add(new Carta(n, v));
            }
        }
    }

    public void embaralha() {
        Random r = new Random();

        int quantidadeDeCartas = this.cartas.size();
        Carta cartaATrocar, cartaAtual;
        int indiceAdicao;

        for (int i = 0; i < quantidadeDeCartas; i++) {
            indiceAdicao = Math.abs(r.nextInt()) % (quantidadeDeCartas - 1);

            cartaATrocar = this.cartas.get(indiceAdicao);
            cartaAtual = this.cartas.get(i);

            this.cartas.setElementAt(cartaATrocar, i);
            this.cartas.setElementAt(cartaAtual, indiceAdicao);
        }

    }

    private void imprimeOrdemDasCartas(){
        int quantidadeDeCartas = this.cartas.size();

        for(int i = 0; i < quantidadeDeCartas; i++){
            System.out.println((i + 1) + ": " + this.cartas.get(i));
        }
    }

    private void assertCartaRepetida() throws ExcecaoCartaRepetida{
        for (Carta c : this.cartas) {
            int matchCount = 0;
            for (Carta k : this.cartas) {
                if (c == k) {
                    matchCount++;
                }
                if (matchCount > 1) {
                    throw new ExcecaoCartaRepetida(c + " " + k);
                }
            }
        }
    }

    private Carta getCarta(int posicao) throws ExcecaoCartasInsuficientes{
        if(this.cartas.size() == 0){
            throw new ExcecaoCartasInsuficientes();
        }
        return this.cartas.remove(posicao);
    }

    public Carta getCartaTopo(){
        Carta c = null;

        try{
            c = this.getCarta(0);
        }catch (ExcecaoCartasInsuficientes e){
            System.out.println("Nao e possivel retirar mais cartas do baralho.");
            System.exit(1);
        }

        return c;

    }

}
