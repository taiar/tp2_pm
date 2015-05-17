package jogo;

import jogador.Jogador;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class ReconhecedorDeMao {

    /**
     * Referência para a mesa.
     */
    private Mesa mesa;

    /**
     * Referência para os jogadores.
     */
    private Vector<Jogador> jogadores;

    /**
     * Descrição da avaliação da mão da rodada.
     */
    private int[] mao;

    /**
     * Ordem de valor das combinações de cartas possíveis.
     */
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
        this.mesa = Mesa.getInstance(0);
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


    public void calculaMao() {
        this.mao = new int[this.jogadores.size()];
        for(int i = 0; i < this.jogadores.size(); i += 1) {
            Carta[] hand = this.mergeMao(this.jogadores.elementAt(i));
            this.mao[i] = -1;
            for (int j = 0; j < this.ordemAvaliacao.length; j += 1) {
                try {
                    Method m = this.getClass().getDeclaredMethod(this.ordemAvaliacao[j], Carta[].class);
                    m.setAccessible(true);
                    if((boolean) m.invoke(this, new Object[]{ hand })) {
                        this.mao[i] = j;
                        continue;
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
        Set<Carta> conjunto = new HashSet<Carta>();
        Collections.addAll(conjunto, cartas);
        int[] valorNumerico = {12, 11, 10, 9, 8};
        Carta.Valor[] valoreObjeto = Carta.Valor.values();

        boolean break_flag = false;

        for(Carta.Naipe n : Carta.Naipe.values()) {
            break_flag = false;
            for(int i = 0; i < valorNumerico.length; i += 1) {
                if(!conjunto.contains(new Carta(n, valoreObjeto[valorNumerico[i]]))) {
                    break_flag = true;
                    break;
                }
            }
        }

        return !break_flag;
    }

    private boolean isStraightFlush(Carta[] cartas) {
        return false;
    }

    private boolean isFourOfAKind(Carta[] cartas) {
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
