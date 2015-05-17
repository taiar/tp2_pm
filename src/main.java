import jogo.Mesa;
import java.util.Scanner;
import jogo.ReconhecedorDeMao;

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

    public static void main(String[] args) {
       boolean fimDeJogo = false;
        Scanner sc = new Scanner(System.in);

        System.out.println("Olá! Bem vindo ao Pôquer Texas Hold 'em PM 2015/1!");
        System.out.println("Qual é o seu nome? ");
        String nomeJogador = sc.next();

        Mesa m = Mesa.getInstance(8, nomeJogador);
        fimDeJogo = m.preFlop();
        if(fimDeJogo){
            return;
        }
        fimDeJogo = m.flop();
        if(fimDeJogo){
            return;
        }
        fimDeJogo = m.turn();
        if(fimDeJogo){
            return;
        }
        fimDeJogo = m.river();
        if(fimDeJogo){
            return;
        }

        m.showdown();

        /*
        Mesa m = Mesa.getInstance(5);
        m.preFlop();
        m.flop();

        ReconhecedorDeMao r = new ReconhecedorDeMao(m);

        r.mostraCartasJogadores();
        r.mostraCartasMesa();
        r.iteraSobreAvaliacoes();
        */
    }
}
