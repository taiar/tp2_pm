import jogo.Mesa;
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
        Mesa m = Mesa.getInstance(5);
        m.preFlop();
        m.flop();

        ReconhecedorDeMao r = new ReconhecedorDeMao(m);

        r.mostraCartasJogadores();
        r.mostraCartasMesa();
        r.calculaMao();
        r.resultado();
    }
}
