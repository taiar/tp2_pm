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
        this.mesa = mesa.getInstance(0);
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
        int[] valorMaoDeUsuario = new int[this.jogadores.size()];
        for(Jogador j : this.jogadores) {
            Carta[] hand = this.mergeMao(j);
            for (String s : this.ordemAvaliacao) {
                try {
                    Method m = this.getClass().getDeclaredMethod(s, Carta[].class);
                    m.setAccessible(true);
                    if((boolean) m.invoke(this, new Object[]{ hand })) {

                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Carta[] mergeMao(Jogador j) {
        Carta[] mao = new Carta[7];
        int iCarta = 0;
        for(int i = 0; i < j.getCartas().length; i += 1) {
            mao[i] = j.getCartas()[i];
            iCarta += 1;
        }
        for(int i = 0; i < mesa.getCartas().length; i += 1)
            mao[i + iCarta] = this.mesa.getCartas()[i];

        return mao;
    }

    private boolean isRoyalStraightFlush(Carta[] cartas) {
        System.out.println("isRoyalStraightFlush");
        return true;
    }

    private boolean isStraightFlush(Carta[] cartas) {
        System.out.println("isStraightFlush");
        return false;
    }

    private boolean isFourOfAKind(Carta[] cartas) {
        System.out.println("isFourOfAKind");
        return true;
    }

    private boolean isFullHouse(Carta[] cartas) {
        return true;
    }

    private boolean isFlush(Carta[] cartas) {
        return true;
    }

    private boolean isStraight(Carta[] cartas) {
        return true;
    }

    private boolean isThreeOfAKind(Carta[] cartas) {
        return true;
    }

    private boolean isTwoPairs(Carta[] cartas) {
        return true;
    }

    private boolean isOnePair(Carta[] cartas) {
        return true;
    }
}
