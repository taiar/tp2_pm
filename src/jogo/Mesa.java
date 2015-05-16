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
    private static String[] nomesJogadores = {"William", "Othelo", "Hamlet",
                                                "Petruchio", "Horacio", "Ofelia",
                                              "Catarina", "Romeu"};

    private Mesa(int numeroDeJogadores, String nomeJogadorUsuario){
        this.baralho = new Baralho();
        this.baralho.embaralha();
        this.jogadores = new Vector<Jogador>(numeroDeJogadores);
        this.cartas = new Carta[NUMERO_TOTAL_CARTAS_PUBLICAS];

        short i = 0;
        for(i = 0; i < numeroDeJogadores; i++){

            if(i != 0){
                this.adicionaJogador(i, nomesJogadores[i]);
            }else{ // Jogador 0 e o jogador selecionado pelo usuario
                this.adicionaJogador(i, nomeJogadorUsuario);
            }

        }
    }

    private void adicionaJogador(short id){
        /*if(this.jogadores.size() == NUMERO_MAXIMO_JOGADORES){
            throw new ExcecaoNumeroMaximoJogadores();
        }*/

        this.jogadores.add(new Jogador(id, nomesJogadores[id], QUANTIDADE_PADRAO_DINHEIRO));
    }

    private void adicionaJogador(short id, String nomeJogador){
        /*if(this.jogadores.capacity() == NUMERO_MAXIMO_JOGADORES){
            throw new ExcecaoNumeroMaximoJogadores();
        }*/

        this.jogadores.add(new Jogador(id, nomeJogador, QUANTIDADE_PADRAO_DINHEIRO));
    }

    public static Mesa getInstance(int numeroDeJogadores, String nomeJogador){
        if(instanciaMesa == null){
            instanciaMesa = new Mesa(numeroDeJogadores, nomeJogador);
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

    private void distribuiCartas(){
        for(Jogador j : this.jogadores){
            j.ganhaCartas(this.baralho.getCartaTopo(), this.baralho.getCartaTopo());
        }

        for(Jogador j : this.jogadores){
            System.out.println(j.mostraCartas());
        }
    }

    private void mostraCartasNaMesa(){
        System.out.print("Cartas na mesa:\n| ");
        for (Carta c : this.cartas) {
            if (c != null) {
                System.out.print(c + " | ");
            }
        }
        System.out.println();
    }

    public boolean preFlop(){
        int numeroDeJogadores = this.jogadores.capacity();

        int smallBlind = (this.dealer + 1) % numeroDeJogadores;
        int bigBlind = (smallBlind + 1) % numeroDeJogadores;

        int aposta;

        // Small Blind aposta

        Jogador j = this.jogadores.remove(smallBlind);
        aposta = j.aposta(VALOR_PADRAO_SMALL_BLIND);
        pote += aposta;
        this.jogadores.add(smallBlind, j);

        // Big Blind aposta
        j = this.jogadores.remove(bigBlind);
        aposta = j.aposta(VALOR_PADRAO_BIG_BLIND);
        pote += aposta;
        this.jogadores.add(bigBlind, j);

        this.distribuiCartas();

        // Retira apostas de todos os outros jogadores
        int index, valorAposta;
        for(int i = 0; i < numeroDeJogadores; i++){
            index = (i + bigBlind + 1) % numeroDeJogadores;

            j = this.jogadores.remove(index);

            if(! j.isInAllin()){
                if(j.getUltimaAposta() < VALOR_PADRAO_BIG_BLIND){
                    valorAposta = VALOR_PADRAO_BIG_BLIND - j.getUltimaAposta();
                    aposta = j.aposta(valorAposta);
                    this.pote += aposta;
                    System.out.println("Pote: " + this.pote);
                    // Se alguem desistir nessa fase, remova-o
                    // Se todos desistirem, retorna true para encerrar
                }
            }

            this.jogadores.add(index, j);
        }

        // Se o jogo continuou ate aqui, retorna falso
        return false;
    }

    private boolean rodadaDeApostas(){
        int numeroDeJogadores = this.jogadores.capacity();
        int aposta;
        // Valor da aposta corrente da rodada (maior aposta)
        int apostaCorrente = 0;



        // Procede com a rodada de apostas para cada jogador
        int index;
        Jogador j;
        for (int i = 0; i < numeroDeJogadores; i++) {
            index = (i + dealer + 1) % numeroDeJogadores;

            j = this.jogadores.remove(index);

            if (j.isEstaNaRodada()) {
                // TODO: apostamos com valor padrao para testes, antes
                aposta = j.aposta(10);
                pote += aposta;

                // Se o jogador apostou mais do que os outros, valor da aposta sobe
                if (apostaCorrente <= aposta) {
                    apostaCorrente = aposta;
                } else { // Caso em que o jogador tenta apostar menos do que o anterior
                    System.out.println("Apostou menos. Tratar isso.");

                    // Se esta em all-in, pode continuar. Senao, foi erro
                    if (!j.isInAllin()) {
                        System.out.println("Por enquanto vamos simplesmente remover o jogador.");
                        j.saiDaRodada();
                    }
                }
            }

            // Substitui o jogador alterado que usamos aqui no vetor, efetivamente processando a acao
            this.jogadores.add(index, j);
        }// Fim rodada de apostas

        // Rodada de ajuste de apostas
        for(int i = 0; i < numeroDeJogadores; i++){
            index = (i + dealer + 1) % numeroDeJogadores;

            j = this.jogadores.remove(index);

            if (j.isEstaNaRodada()) {
                // Tem que completar a aposta
                if(apostaCorrente > j.getUltimaAposta()){
                    int diferenca = apostaCorrente - j.getUltimaAposta();
                    // TODO: decisao do jogador de apostar ou nao
                    /* TODO: Se nao decidir apostar, sai. Quando alguem sair verificar se sobrou
                       TODO: apenas um jogador */
                    // Caso decida completar
                    aposta = j.aposta(diferenca);
                    pote += aposta;
                }
            }

            // Substitui o jogador alterado que usamos aqui no vetor, efetivamente processando a acao
            this.jogadores.add(index, j);
        }// Fim da rodada de ajuste de apostas

        System.out.println("Pote: " + this.pote);

        // Mostra estado dos jogadores
        for(Jogador temp : this.jogadores){
            System.out.println(temp.getNome() + ": " + temp.getDinheiro());
        }

        // Se o jogo continuou ate aqui, e falso que ele terminou
        return false;
    }

    public boolean flop() {
        System.out.println("Flop");

        // Revela cartas na mesa
        for (int i = 0; i < 3; i++) {
            this.cartas[i] = this.baralho.getCartaTopo();
        }

        this.mostraCartasNaMesa();

        System.out.println("");

        return rodadaDeApostas();

    }// Fim Flop

    /**
     * @brief Estado do jogo que envolve a revelacao da quarta carta e subsequentes apostas
     * @return O estado de fim do jogo: acabou ou nao (nao = continua)
     */
    public boolean turn(){
        System.out.println("Turn");

        // Revela quarta carta
        this.cartas[3] = this.baralho.getCartaTopo();

        this.mostraCartasNaMesa();

        // Se o jogo continuou ate aqui, e falso que ele terminou
        return rodadaDeApostas();
    }

<<<<<<< HEAD
    /**
     * @brief Estado do jogo que envolve a revelacao da quinta carta e subsequentes apostas
     * @return O estado de fim do jogo: acabou ou nao (nao = continua)
     */
    public boolean river(){
        System.out.println("River");

        // Revela quarta carta
        this.cartas[4] = this.baralho.getCartaTopo();

        this.mostraCartasNaMesa();

        // Se o jogo continuou ate aqui, e falso que ele terminou
        return rodadaDeApostas();
    }

    /**
     * @brief Ultima fase do jogo. Jogadores tem suas maos comparadaspara decidir o vencedor
     * @return O estado de fim do jogo: acabou ou nao (nao = continua)
     */
    public void showdown(){
        System.out.println("Showdown");

        this.mostraCartasNaMesa();
=======
    public Vector<Jogador> getJogadores() {
        return jogadores;
    }

    public Carta[] getCartas() {
        return cartas;
>>>>>>> a7a268d1bc97775d4dc34dcbfc367ff406f3a042
    }
}
