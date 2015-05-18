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
        this.jogadores.add(new Jogador(id, nomeJogador, QUANTIDADE_PADRAO_DINHEIRO));
    }

    public static Mesa getInstance(int numeroDeJogadores, String nomeJogador){
        if(instanciaMesa == null){
            instanciaMesa = new Mesa(numeroDeJogadores, nomeJogador);
        }else{// Caso ja se tenha iniciado o jogo, incrementa o dealer
            instanciaMesa.dealer = (instanciaMesa.dealer + 1) % instanciaMesa.jogadores.size();
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

    // Mostra estado dos jogadores
    public void mostraEstadoJogadores(){
        String estado;

        System.out.println("------- Jogadores -------");

        for(Jogador temp : this.jogadores){
            estado = temp.getNome() + ": $" + temp.getDinheiro();

            if(temp.isEstaNoJogo()){
                if(! temp.isEstaNaRodada()){
                    estado = "X " + estado;
                }else{
                    if(temp.isInAllin()){
                        estado = "# " + estado;
                    }
                }
                System.out.println(estado);
            }
        }

        System.out.println("-------------------------");
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

    // Metodo chamado no fim da rodada, eliminando todos os jogadores
    // em all-in que nao tenham ganho nada
    public void eliminaJogadores(){
        int size = this.jogadores.size();
        Jogador j;
        for(int i = 0; i < size; i++){
            j = jogadores.get(i);
            if(j.isInAllin()) {
                baralho.adicionaCarta(j.getCartas()[0]);
                baralho.adicionaCarta(j.getCartas()[1]);
                jogadores.remove(i);
            }
        }
    }

    public boolean preFlop(){
        int numeroDeJogadores = this.jogadores.size();

        // So um jogador, fim de jogo
        if(numeroDeJogadores == 1){
            return true;
        }

        int smallBlind = (this.dealer + 1) % numeroDeJogadores;
        int bigBlind = (smallBlind + 1) % numeroDeJogadores;

        Scanner sc = new Scanner(System.in);

        System.out.println("* Dealer: " + this.jogadores.get(this.dealer).getNome() +
          ", Small Blind: " +this.jogadores.get(smallBlind).getNome() +
          ", Big Blind: " + this.jogadores.get(bigBlind).getNome() + " *");

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

            if((! j.isInAllin()) && (j.isEstaNaRodada())){
                if(j.getUltimaAposta() < VALOR_PADRAO_BIG_BLIND){
                    valorAposta = VALOR_PADRAO_BIG_BLIND - j.getUltimaAposta();
                    if(! (j.getId() == ID_JOGADOR_USUARIO)){
                        aposta = j.aposta(valorAposta);
                        this.pote += aposta;
                    }else{ // Jogador usuario
                        String hand = new String(new int[] { 0x261E }, 0, 1);
                        System.out.println("\n"+ hand +"Sua vez, " + j.getNome());
                        System.out.println(j.getNome() + ", suas cartas são: " + j.mostraCartas());
                        System.out.println("Para continuar na rodada, voce deve completar $" + valorAposta);
                        System.out.println("Você tem $" + j.getDinheiro() + ". Completa $" + valorAposta + "? <s/n>");

                        String opcao = sc.next();

                        while ((! opcao.equals("s")) && (! opcao.equals("n"))){
                            System.out.println("Opcao '" + opcao + "' invalida.");
                            System.out.println("Para continuar na rodada, voce deve completar $" + valorAposta);
                            System.out.println("Completa $" + valorAposta + "? <s/n>");
                            opcao = sc.next();
                        }

                        if(opcao.equals("n")){ // desistiu da rodada
                            j.saiDaRodada();
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
                                eliminaJogadores();
                                // Retorna, mostrando que o jogo acabou
                                return true;
                            }
                        }else{ // Continua
                            aposta = j.aposta(valorAposta);
                            this.pote += aposta;
                        }
                    }
                }
            } else if(j.isInAllin()){ // Trata all-in
                System.out.println("######## " + j.getNome() + " esta em all-in neste jogo.");
            }

            // Devolve jogador alterado ao seu lugar no vetor
            this.jogadores.set(index, j);
        }

        System.out.println("Pote: $" + this.pote);
        // Se o jogo continuou ate aqui, retorna falso
        return false;
    }

    private boolean rodadaDeApostas(){
        int numeroDeJogadores = this.jogadores.size();
        int aposta = 0;
        // Valor da aposta corrente da rodada (maior aposta)
        int apostaCorrente = 0;
        Scanner sc = new Scanner(System.in);

        // Procede com a rodada de apostas para cada jogador
        int index;
        Jogador j;
        for (int i = 0; i < numeroDeJogadores; i++) {
            index = (i + dealer + 1) % numeroDeJogadores;

            j = this.jogadores.get(index);

            if(j.isEstaNaRodada()) { // so contabilizo jogadores que estao na rodada
                if(j.getId() != ID_JOGADOR_USUARIO){ // Jogador da maquina

                    if(j.isInAllin()){
                        System.out.println(j.getNome() + " esta em All-in");
                    }else{
                        aposta = j.aposta(j.geraValorAposta());
                        // Assumo que neste caso o jogador quer desistir
                        if(aposta < apostaCorrente){
                            j.aumentaQuantidadeDinheiro(aposta);
                            aposta = 0;
                            j.saiDaRodada();

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

                        }else{// Continua na rodada
                            apostaCorrente = aposta;
                            System.out.println(j.getNome() + " apostou $" + aposta);
                        }
                    }

                }else { // Jogador usuario
                    if(! j.isInAllin()){
                        String hand = new String(new int[] { 0x261E }, 0, 1);
                        System.out.println("\n"+ hand +"Sua vez, " + j.getNome());
                        System.out.println(j.getNome() + ", suas cartas sao: ");
                        System.out.println(j.mostraCartas());
                        System.out.println("Para continuar na rodada, voce deve cobrir $" + apostaCorrente +
                        " ou aumentar.");
                        System.out.println("Você tem $" + j.getDinheiro());
                        System.out.println("O que deseja fazer?\nDigite um valor menor do que a aposta corrente" +
                                " para sair da rodada sem gastar mais nada,\nou um valor maior ou igual para continuar.");

                        aposta = sc.nextInt();

                        if(aposta < apostaCorrente){ // Apostou menos do que tem, sai
                            System.out.println("A aposta corrente minima para continuar e $" + apostaCorrente + ". " +
                                    "\nComo voce tem mais dinheiro do que este valor, interpreto que deseja sair da" +
                                    " rodada.\nVoce esta fora desta rodada.");
                            aposta = 0;
                            j.saiDaRodada();
                        }else{// Aposta valida
                            aposta = j.aposta(aposta);
                            System.out.println("Voce esta apostando $" + aposta);
                        }
                    }else{ // Usuario em all-in
                        System.out.println("Voce esta em all-in");
                    }
                }

                // Recalcula aposta corrente
                if(apostaCorrente < aposta){
                    System.out.println(j.getNome() + " aumentou o valor da aposta do turno de $" +
                            + apostaCorrente + " para $" + aposta);
                    apostaCorrente = aposta;
                }

                this.pote += aposta;

                // Substitui o jogador alterado que usamos aqui no vetor, efetivamente processando a acao
                this.jogadores.set(index, j);
            }// END if jogador esta na rodada

        }// Fim rodada de apostas

        System.out.println("Rodada de ajuste de apostas");
        // Rodada de ajuste de apostas
        for(int i = 0; i < numeroDeJogadores; i++){
            index = (i + dealer + 1) % numeroDeJogadores;

            j = this.jogadores.get(index);

            if (j.isEstaNaRodada()) {
                if(apostaCorrente > j.getUltimaAposta()){
                    int diferenca = apostaCorrente - j.getUltimaAposta();
                    if(j.getId() != ID_JOGADOR_USUARIO){ // Jogador da maquina
                        // Tem que completar a aposta
                        // TODO: decisao do jogador de apostar ou nao
                        /* TODO: Se nao decidir apostar, sai. Quando alguem sair verificar se sobrou
                           TODO: apenas um jogador */
                        // Caso decida completar
                        aposta = j.aposta(diferenca);
                        pote += aposta;
                    }else{ // Jogador usuario
                        System.out.println("Sua vez, " + j.getNome());
                        System.out.println(j.getNome() + ", suas cartas sao:");
                        System.out.println(j.mostraCartas());

                        if(! j.isInAllin()){
                            System.out.println("Para continuar na rodada, voce deve completar $" + diferenca);
                            System.out.println("Você tem $" + j.getDinheiro());
                            System.out.println("O que deseja fazer?\nDigite um valor menor do que a diferenca" +
                                    " para sair da rodada sem gastar mais nada,\nou um valor maior ou igual para continuar.");

                            String opcao = sc.next();

                            while ((! opcao.equals("s")) && (! opcao.equals("n"))){
                                System.out.println("Opcao '" + opcao + "' invalida.");
                                System.out.println("Para continuar na rodada, voce deve completar $" + diferenca);
                                System.out.println("Completa " + diferenca + "? <s/n>");
                                opcao = sc.next();
                            }

                            if(opcao.equals("n")){ // desistiu da rodada
                                j.saiDaRodada();
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
                                aposta = j.aposta(diferenca);
                                this.pote += aposta;
                            }
                        }else{ // Usuario em all-in
                            System.out.println("Voce esta em all-in");
                        }
                    }
                }
            }

            // Substitui o jogador alterado que usamos aqui no vetor, efetivamente processando a acao
            this.jogadores.set(index, j);
        }// Fim da rodada de ajuste de apostas

        System.out.println("Pote: $" + this.pote);

        this.mostraEstadoJogadores();

        // Espera interacao com o usuario para partir para a proxima fase
        System.out.println("Fim do turno. Pressione ENTER para prosseguir.");
        String exitCode = sc.nextLine();
        while ((exitCode = sc.nextLine()).length() > 0){
            exitCode = sc.nextLine();
        }

        // Se o jogo continuou ate aqui, e falso que ele terminou
        return false;
    }

    public boolean flop() {
        System.out.println("==========> Primeira Fase: Flop <==========");

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
        System.out.println("==========> Segunda Fase: Turn <==========");

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
        System.out.println("==========> Terceira Fase: River <==========");

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
        System.out.println("==========> Ultima Fase: Showdown <==========");

        this.mostraCartasNaMesa();
    }

    public Vector<Jogador> getJogadores() {
        return jogadores;
    }

    public Carta[] getCartas() {
        return cartas;
    }

    public void devolveCartasAoBaralho(){
        for(Carta c : this.cartas){
            this.baralho.adicionaCarta(c);
        }

        for(Jogador j : this.jogadores){
            this.baralho.adicionaCarta(j.getCartas()[0]);
            this.baralho.adicionaCarta(j.getCartas()[1]);
        }

        this.printStatusBaralho();
    }

    public void printStatusBaralho(){
        System.out.println(this.baralho.quantidadeDeCartas() +" cartas no baralho");
    }
}
