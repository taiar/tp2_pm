import jogo.Mesa;
import java.util.Scanner;

public class main {

    public static void executaJogo(String nomeJogador){
        boolean fimDeJogo = false;

        Mesa m = Mesa.getInstance(Mesa.NUMERO_MAXIMO_JOGADORES, nomeJogador);
        fimDeJogo = m.preFlop();
        if(fimDeJogo){
            m.eliminaJogadores();
            m.devolveCartasAoBaralho();
            return;
        }
        fimDeJogo = m.flop();
        if(fimDeJogo){
            m.eliminaJogadores();
            m.devolveCartasAoBaralho();
            return;
        }
        fimDeJogo = m.turn();
        if(fimDeJogo){
            m.eliminaJogadores();
            m.devolveCartasAoBaralho();
            return;
        }
        fimDeJogo = m.river();
        if(fimDeJogo){
            m.eliminaJogadores();
            m.devolveCartasAoBaralho();
            return;
        }

        m.showdown();

        m.eliminaJogadores();
        m.devolveCartasAoBaralho();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Olá! Bem vindo ao Pôquer Texas Hold 'em PM 2015/1!");
        System.out.println("Qual é o seu nome? ");
        String opcao;
        String nomeJogador = sc.nextLine();

        do{
            executaJogo(nomeJogador);
            System.out.println("Deseja continuar jogando? <s/n>");

            opcao = sc.next();

            while ((! opcao.equals("s")) && (! opcao.equals("n"))){
                System.out.println("Opcao '" + opcao + "' invalida.");
                System.out.println("Deseja continuar jogando? <s/n>");
                opcao = sc.next();
            }

        }while(opcao.equals("s"));
    }
}
