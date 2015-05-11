import jogador.MaoDePoquer;
import jogo.Mesa;
import jogo.Carta;

public class main {
    public static void main(String[] args){

        /*Mesa m = Mesa.getInstance();
        m.distribuiCartas();
        m.flop();*/

        MaoDePoquer m = new MaoDePoquer();
        try{
            Carta c = new Carta(Carta.Naipe.Espadas, Carta.Valor.As);
            m.insereCarta(new Carta(Carta.Naipe.Espadas, Carta.Valor.As));
            m.insereCarta(new Carta(Carta.Naipe.Espadas, Carta.Valor.Oito));
            m.insereCarta(new Carta(Carta.Naipe.Paus, Carta.Valor.Oito));
            m.insereCarta(new Carta(Carta.Naipe.Copas, Carta.Valor.Valete));
            m.insereCarta(new Carta(Carta.Naipe.Ouros, Carta.Valor.Rainha));
            m.insereCarta(new Carta(Carta.Naipe.Copas, Carta.Valor.Rainha));
        }catch(Exception e){
            System.out.println("Limite de cartas inserido.");
        }

        m.printMao();
        m.ordenaMao();
        System.out.println("$$$$$$$$$$$$$$$$$$$");
        m.printMao();

    }
}
