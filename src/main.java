import jogo.Mesa;

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

        Mesa m = Mesa.getInstance(5);
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
    }
}
