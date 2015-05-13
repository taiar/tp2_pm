/*import jogador.MaoDePoquer;
import jogo.Mesa;
import jogo.Carta;*/
import java.util.Scanner;
import java.util.Random;

class TESTE_jogador{
    public boolean noJogo;
    public boolean naRodada;
    public double dinheiro;
    public String nome;
    public double ultimaAposta;

    public TESTE_jogador(double dinheiro, String nome){
        this.noJogo = true;
        this.naRodada = true;
        this.dinheiro = dinheiro;
        this.nome = nome;
        this.ultimaAposta = 0;
    }

    public double aposta(double quantia){
        if(quantia > dinheiro){
            // Dinheiro = 0: AllIn
            double dinheiroTotal = this.dinheiro;
            this.dinheiro = 0;
            this.ultimaAposta = dinheiroTotal;
            System.out.println("Jogador " + this.nome + " esta em All-in");
            return dinheiroTotal;
        }

        this.ultimaAposta = quantia;
        this.dinheiro -= quantia;
        return quantia;
    }
}

public class main {

    private static int smallBlind(int dealer, int nJogadores){
        return (dealer + 1) % nJogadores;
    }

    private static int bigBlind(int dealer, int nJogadores){
        return (dealer + 2) % nJogadores;
    }

    private static void pt(String s){
        System.out.println(s);
    }

    public static void main(String[] args){

        // Teste de distribuicao e entrega de cartas
        /*Mesa m = Mesa.getInstance();
        m.distribuiCartas();
        m.flop();*/

        /* Este e um esqueleto do fluxo do programa, para que se possa
         * desenvolver com um guia. Ele e bem simples, nao chega a ter
          * checagem de erro*/

        Random r = new Random();
        Scanner console = new Scanner(System.in);
        TESTE_jogador[] jogadores;
        final Double DINHEIRO_INICIAL = 100.0;
        final Double SMALL_BLIND = 10.0;
        final Double BIG_BLIND = 2 * SMALL_BLIND;
        final String[] nomes = {"Albert", "Isaac", "Johannes", "Stephen",
                                "Galileo", "Werner", "Max", "Enrico"};
        final int fatorDesistencia = 10;

        double pote = 0;


        System.out.println("Inicio do programa! Quantos jogadores deseja?");
        // Esse numero tem que ser entre 2 e 8
        int numero_jogadores = console.nextInt();
        jogadores = new TESTE_jogador[numero_jogadores];

        // Pre-flop
        for(int i = 0; i < numero_jogadores; i++){
            jogadores[i] = new TESTE_jogador(DINHEIRO_INICIAL, nomes[i]);
        }

        System.out.println("Dinheiro distribuido");

        // Flop

        int dealer = 0;

        System.out.println("Dealer e o jogador " + dealer);
        pt("Small blind e o jogador " + smallBlind(dealer, numero_jogadores));
        pote += jogadores[smallBlind(dealer, numero_jogadores)].aposta(SMALL_BLIND);
        pt("Big blind e o jogador " + bigBlind(dealer, numero_jogadores));
        pote += jogadores[bigBlind(dealer, numero_jogadores)].aposta(BIG_BLIND);

        for(int i = dealer + 1; i <= numero_jogadores; i++){
            pt("Distribuindo cartas para jogador " + i % numero_jogadores);
        }

        pt("Big blind e small blind ja fizeram apostas. Todo o resto vai apostar ou sair agora.");

        int i;
        for(int j = dealer + 1; j <= numero_jogadores; j++){
            i = j % numero_jogadores;

            if(jogadores[i].noJogo){
                int desistencia = r.nextInt() % fatorDesistencia;

                // Condicao de desistencia
                if(desistencia == 0){
                    pt(jogadores[i].nome + " desistiu da rodada");
                    jogadores[i].naRodada = false;
                }else{
                    if(jogadores[i].ultimaAposta < BIG_BLIND){
                        pote += jogadores[i].aposta(BIG_BLIND - jogadores[i].ultimaAposta);
                    }
                }
            }
        }

        pt("Valor do pote: " + pote);
    }
}
