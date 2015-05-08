package jogo;

import excecoes.ExcecaoNumeroMaximoJogadores;

import jogador.Jogador;
import java.util.ArrayList;

public class Mesa {

    public static final int QUANTIDADE_PADRAO_DINHEIRO = 100;
    public static final int NUMERO_MAXIMO_JOGADORES = 8;
    public static final int NUMERO_TOTAL_CARTAS_PUBLICAS = 5;

    private static Mesa instanciaMesa = null;
    private Carta[] cartas = null;
    private int pote;
    private Baralho baralho = null;
    private ArrayList<Jogador> jogadores = null;
    private static String[] nomesJogadores = {"James Bond", "Matt Damon", "Clint Eastwood",
                                              "Lady Gaga", "Frank Underwood", "Lemmy",
                                              "Littlefinger", "Batman"};

    private Mesa(){
        this.baralho = new Baralho();
        this.baralho.embaralha();
        this.jogadores = new ArrayList<Jogador>();
        this.cartas = new Carta[NUMERO_TOTAL_CARTAS_PUBLICAS];

        short i = 0;
        for(i = 0; i < NUMERO_MAXIMO_JOGADORES; i++){
            try{
                this.adicionaJogador(i);
            }catch(ExcecaoNumeroMaximoJogadores e){
                System.out.println("ERRO: Numero maximo de jogadores atingido.");
            }
        }
    }

    public void adicionaJogador(short id) throws ExcecaoNumeroMaximoJogadores{
        if(this.jogadores.size() == NUMERO_MAXIMO_JOGADORES){
            throw new ExcecaoNumeroMaximoJogadores();
        }

        this.jogadores.add(new Jogador(id, nomesJogadores[id], QUANTIDADE_PADRAO_DINHEIRO));
    }

    public static Mesa getInstance(){
        if(instanciaMesa == null){
            instanciaMesa = new Mesa();
        }

        return instanciaMesa;
    }

    public int getPote(){
        return this.pote;
    }

    public void aumentaPote(int quantia){
        assert quantia > 0;

        this.pote += quantia;
    }

    public void distribuiCartas(){
        for(Jogador j : this.jogadores){
            j.ganhaCartas(this.baralho.getCartaTopo(), this.baralho.getCartaTopo());
        }

        for(Jogador j : this.jogadores){
            System.out.println(j.mostraCartas());
        }
    }

    public void flop(){
        for(int i = 0; i < 3; i++){
            this.cartas[i] = this.baralho.getCartaTopo();
        }

        for(int i = 0; i < 3; i++) System.out.println(this.cartas[i]);
    }

}
