package jogo;

import excecoes.ExcecaoNumeroMaximoJogadores;

import jogador.Jogador;
import java.util.Vector;

public class Mesa {

    public static final int QUANTIDADE_PADRAO_DINHEIRO = 100;
    public static final int NUMERO_MAXIMO_JOGADORES = 8;
    public static final int NUMERO_MINIMO_JOGADORES = 2;
    public static final int NUMERO_TOTAL_CARTAS_PUBLICAS = 5;
    public static final int VALOR_PADRAO_SMALL_BLIND = 5;
    public static final int VALOR_PADRAO_BIG_BLIND = 2 * VALOR_PADRAO_SMALL_BLIND;

    private static Mesa instanciaMesa = null;
    private Carta[] cartas = null;
    private int pote;
    private int dealer;
    private Baralho baralho = null;
    private Vector<Jogador> jogadores = null;
    private static String[] nomesJogadores = {"James Bond", "Matt Damon", "Clint Eastwood",
                                              "Lady Gaga", "Frank Underwood", "Lemmy",
                                              "Littlefinger", "Batman"};

    private Mesa(int numeroDeJogadores){
        this.baralho = new Baralho();
        this.baralho.embaralha();
        this.jogadores = new Vector<Jogador>(numeroDeJogadores);
        this.cartas = new Carta[NUMERO_TOTAL_CARTAS_PUBLICAS];

        short i = 0;
        for(i = 0; i < numeroDeJogadores; i++){
            try{
                this.adicionaJogador(i);
            }catch(ExcecaoNumeroMaximoJogadores e){
                System.out.println("ERRO: Numero maximo de jogadores atingido.");
            }
        }
    }

    public void adicionaJogador(short id) throws ExcecaoNumeroMaximoJogadores{
        if(this.jogadores.capacity() == NUMERO_MAXIMO_JOGADORES){
            throw new ExcecaoNumeroMaximoJogadores();
        }

        this.jogadores.add(new Jogador(id, nomesJogadores[id], QUANTIDADE_PADRAO_DINHEIRO));
    }

    public static Mesa getInstance(int numeroDeJogadores){
        if(instanciaMesa == null){
            instanciaMesa = new Mesa(numeroDeJogadores);
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

    public void preFlop(){
        int numeroDeJogadores = this.jogadores.size();

        int smallBlind = (this.dealer + 1) % numeroDeJogadores;
        int bigBlind = (smallBlind + 1) % numeroDeJogadores;

        int aposta;

        // Small Blind aposta
        Jogador j = this.jogadores.get(smallBlind);
        aposta = j.aposta(VALOR_PADRAO_SMALL_BLIND);
        pote += aposta;

        // Big Blind aposta
        j = this.jogadores.get(bigBlind);
        aposta = j.aposta(VALOR_PADRAO_BIG_BLIND);
        pote += aposta;

        this.distribuiCartas();

        // Retira apostas de todos os outros jogadores
        int index, valorAposta;
        for(int i = 0; i < numeroDeJogadores; i++){
            index = (i + bigBlind + 1) % numeroDeJogadores;

            j = jogadores.get(index);

            if(! j.isInAllin()){
                if(j.getUltimaAposta() < VALOR_PADRAO_BIG_BLIND){
                    valorAposta = VALOR_PADRAO_BIG_BLIND - j.getUltimaAposta();
                    aposta = j.aposta(valorAposta);
                    pote += aposta;
                    System.out.println("Pote: " + this.pote);
                }
            }

        }

    }

    public void flop(){
        for(int i = 0; i < 3; i++){
            this.cartas[i] = this.baralho.getCartaTopo();
        }

        for(int i = 0; i < 3; i++) System.out.println(this.cartas[i]);
    }

}
