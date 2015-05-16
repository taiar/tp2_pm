package jogo;

import jogador.Jogador;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

public class ReconhecedorDeMao {

    private Mesa mesa;
    private Vector<Jogador> jogadores;
    private String[] ordemAvaliacao = {
        "isRoyalStraightFlush",
        "isStraightFlush",
        "isFourOfAKind",
        "isFullHouse",
        "isFlush",
        "isStraight",
        "isThreeOfAKind",
        "isOnePair"
    };

    public ReconhecedorDeMao(Mesa mesa) {
        this.mesa = mesa.getInstance(550);
        this.jogadores = this.mesa.getJogadores();
    }

    public void mostraCartasJogadores() {
        for (int i = 0; i < this.jogadores.size(); i++) {
            Carta[] c = this.jogadores.elementAt(i).getCartas();
            System.out.println(c[1] + " " + c[0]);
        }
    }

    public void mostraCartasMesa() {
        Carta[] cartasMesa = this.mesa.getCartas();
        for(int i = 0; i < cartasMesa.length; i += 1) {
            System.out.println(cartasMesa[i]);
        }
    }

    public void iteraSobreAvaliacoes() {
        for(String s : this.ordemAvaliacao) {
            try {
                Method m = this.getClass().getDeclaredMethod(s);
                m.setAccessible(true);
                m.invoke(this);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean isRoyalStraightFlush() {
        System.out.println("isRoyalStraightFlush");
        return true;
    }

    private boolean isStraightFlush() {
        System.out.println("isStraightFlush");
        return true;
    }

    private boolean isFourOfAKind() {
        System.out.println("isFourOfAKind");
        return true;
    }

    private boolean isFullHouse() {
        return true;
    }

    private boolean isFlush() {
        return true;
    }

    private boolean isStraight() {
        return true;
    }

    private boolean isThreeOfAKind() {
        return true;
    }

    private boolean isTwoPairs() {
        return true;
    }

    private boolean isOnePair() {
        return true;
    }
}
