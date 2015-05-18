import jogo.Mesa;
import java.util.Scanner;
import jogo.ReconhecedorDeMao;

public class main {

    public static boolean executaJogo(String nomeJogador){
        boolean fimDeJogo = false;

        Mesa m = Mesa.getInstance(8, nomeJogador);
        fimDeJogo = m.preFlop();
        if(fimDeJogo){
            return fimDeJogo;
        }
        fimDeJogo = m.flop();
        if(fimDeJogo){
            return fimDeJogo;
        }
        fimDeJogo = m.turn();
        if(fimDeJogo){
            return fimDeJogo;
        }
        fimDeJogo = m.river();
        if(fimDeJogo){
            return fimDeJogo;
        }

        m.showdown();

        return true;
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
