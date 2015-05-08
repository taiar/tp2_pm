import jogo.Mesa;

public class main {
    public static void main(String[] args){

        Mesa m = Mesa.getInstance();
        m.distribuiCartas();
        m.flop();

    }
}
