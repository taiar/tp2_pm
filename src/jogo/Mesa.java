package jogo;

import excecoes.ExcecaoNumeroMaximoJogadores;
import java.util.Scanner;

import jogador.Jogador;
import java.util.Vector;

public class Mesa {

    public static final int QUANTIDADE_PADRAO_DINHEIRO = 100;
    public static final int NUMERO_MAXIMO_JOGADORES = 8;
    public static final int NUMERO_MINIMO_JOGADORES = 2;
    public static final int NUMERO_TOTAL_CARTAS_PUBLICAS = 5;
    public static final int VALOR_PADRAO_SMALL_BLIND = 5;
    public static final int VALOR_PADRAO_BIG_BLIND = 2 * VALOR_PADRAO_SMALL_BLIND;
    public static final int ID_JOGADOR_USUARIO = 0;

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
        this.dealer = 0;

        // Inicializa jogadores
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

    public void mostraEstadoJogadores(){
        // Mostra estado dos jogadores
        String estado;
        for(Jogador temp : this.jogadores){
            estado = temp.getNome() + ": " + temp.getDinheiro();

            if(temp.isEstaNoJogo()){
                if(! temp.isEstaNaRodada()){
                    estado = "X " + estado;
                }

                System.out.println(estado);
            }
        }
    }

    public int jogadoresNaRodada(){
        int jogadoresNaRodada = 0;

        for(Jogador j : this.jogadores){
            if(j.isEstaNaRodada()){
                jogadoresNaRodada++;
            }
        }

        return jogadoresNaRodada;
    }


    public int achaIndiceUltimoJogador(){
        // Este metodo so deve ser chamado quando se tem certeza da existencia de apenas um jogador
        assert this.jogadoresNaRodada() == 1;

        for(int i = 0; i < this.jogadores.size(); i++){
            if(this.jogadores.get(i).isEstaNaRodada()){
                return i;
            }
        }

        // Nunca sera retornado, levando-se em conta o assert
        return -1;
    }

    public boolean preFlop(){
        int numeroDeJogadores = this.jogadores.capacity();

        int smallBlind = (this.dealer + 1) % numeroDeJogadores;
        int bigBlind = (smallBlind + 1) % numeroDeJogadores;

        Scanner sc = new Scanner(System.in);

        System.out.println(this.jogadores.get(this.dealer).getNome() + " e o dealer.");
        System.out.println(this.jogadores.get(smallBlind).getNome() + " e o small blind.");
        System.out.println(this.jogadores.get(bigBlind).getNome() + " e o big blind.");

        int aposta;

        // Small Blind aposta

        Jogador j = this.jogadores.get(smallBlind);
        aposta = j.aposta(VALOR_PADRAO_SMALL_BLIND);
        pote += aposta;
        this.jogadores.set(smallBlind, j);

        // Big Blind aposta
        j = this.jogadores.get(bigBlind);
        aposta = j.aposta(VALOR_PADRAO_BIG_BLIND);
        pote += aposta;
        this.jogadores.set(bigBlind, j);

        this.distribuiCartas();

        // Retira apostas de todos os outros jogadores
        int index, valorAposta;
        for(int i = 0; i < numeroDeJogadores; i++){
            index = (i + bigBlind + 1) % numeroDeJogadores;

            j = this.jogadores.get(index);

            if((! j.isInAllin()) && (! j.isEstaNaRodada())){
                if(j.getUltimaAposta() < VALOR_PADRAO_BIG_BLIND){
                    valorAposta = VALOR_PADRAO_BIG_BLIND - j.getUltimaAposta();
                    if(! (j.getId() == ID_JOGADOR_USUARIO)){
                        aposta = j.aposta(valorAposta);
                        this.pote += aposta;
                    }else{ // Jogador usuario
                        System.out.println("Para continuar na rodada, voce deve completar $" + valorAposta);
                        System.out.println("Você tem $" + j.getDinheiro() + ". Completa " + valorAposta + "? <s/n>");

                        String opcao = sc.next();

                        while ((! opcao.equals("s")) && (! opcao.equals("n"))){
                            System.out.println("Opcao '" + opcao + "' invalida.");
                            System.out.println("Para continuar na rodada, voce deve completar $" + valorAposta);
                            System.out.println("Completa " + valorAposta + "? <s/n>");
                            opcao = sc.next();
                        }

                        if(opcao.equals("n")){ // desistiu da rodada
                            j.saiDaRodada();

                            int naRodada = 0;

                            // Verifica se ha mais de um jogador
                            if(this.jogadoresNaRodada() == 1){
                                int indiceUltimoJogador = this.achaIndiceUltimoJogador();
                                Jogador temp = this.jogadores.get(indiceUltimoJogador);
                                temp.aumentaQuantidadeDinheiro(this.pote);
                                System.out.println("Jogador " + temp.getNome() + " ganhou o jogo.");
                                this.pote = 0;
                                this.jogadores.set(index, j);
                                this.jogadores.set(indiceUltimoJogador, temp);
                                this.mostraEstadoJogadores();

                                // Retorna, mostrando que o jogo acabou
                                return true;
                            }

                        }else{
                            aposta = j.aposta(valorAposta);
                            this.pote += aposta;
                        }
                    }
                }
            } else {
                System.out.println(j.getNome() + " esta em all-in neste jogo.");
            }

            // Devolve jogador alterado ao seu lugar no vetor
            this.jogadores.set(index, j);

            System.out.println("Pote: " + this.pote);
        }

        // Se o jogo continuou ate aqui, retorna falso
        return false;
    }

    private boolean rodadaDeApostas(){
        int numeroDeJogadores = this.jogadores.capacity();
        int aposta;
        // Valor da aposta corrente da rodada (maior aposta)
        int apostaCorrente = 0;
        Scanner sc = new Scanner(System.in);

        // Procede com a rodada de apostas para cada jogador
        int index;
        Jogador j;
        for (int i = 0; i < numeroDeJogadores; i++) {
            index = (i + dealer + 1) % numeroDeJogadores;

            j = this.jogadores.get(index);

            if(! (j.getId() == ID_JOGADOR_USUARIO)){
                aposta = j.aposta(j.geraValorAposta());

                // Assumo que neste caso o jogador quer desistir
                if(aposta < apostaCorrente){
                    j.aumentaQuantidadeDinheiro(aposta);
                    aposta = 0;
                    j.saiDaRodada();
                }else{// Continua na rodada
                    apostaCorrente = aposta;
                    System.out.println(j.getNome() + " apostou $" + aposta);
                }

                this.pote += aposta;
            }else { // Jogador usuario
                System.out.println("Para continuar na rodada, voce deve completar pelo menos $" + apostaCorrente);
                System.out.println("Você tem $" + j.getDinheiro());
                System.out.println("O que deseja fazer? Digite um valor menor do que a aposta corrente" +
                        " para sair da rodada sem gastar nada ou um valor maior ou igual para continuar.");

                aposta = sc.nextInt();

                /*if(aposta > j.getDinheiro()){
                    System.out.println("Voce resolveu apostar " + aposta)
                }*/
            }

            // Substitui o jogador alterado que usamos aqui no vetor, efetivamente processando a acao
            this.jogadores.set(index, j);
        }// Fim rodada de apostas

        // Rodada de ajuste de apostas
        for(int i = 0; i < numeroDeJogadores; i++){
            index = (i + dealer + 1) % numeroDeJogadores;

            j = this.jogadores.get(index);

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
            this.jogadores.set(index, j);
        }// Fim da rodada de ajuste de apostas

        System.out.println("Pote: " + this.pote);

        this.mostraEstadoJogadores();

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
    public void showdown() {
        System.out.println("Showdown");

        this.mostraCartasNaMesa();
    }

    public Vector<Jogador> getJogadores() {
        return jogadores;
    }

    public Carta[] getCartas() {
        return cartas;
    }
}
