package jogo;

import com.sun.deploy.util.ArrayUtil;
import jogador.Jogador;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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
        "isTwoPairs",
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
                    Object result = m.invoke(this, new Object[]{ hand });
                    if((boolean) result) {
                        this.mao[i] = j;
                        break;
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

    public void resultado() {
        int winnerIndex = -1;
        int melhorAvaliacao = 100;

        for (int i = 0; i < this.mao.length; i += 1)
            if(this.mao[i] < melhorAvaliacao) {
                winnerIndex = i;
                melhorAvaliacao = this.mao[i];
            }

        System.out.println("Jogo ganho por: " + this.jogadores.elementAt(winnerIndex).getNome());
        System.out.println("Jogo ganho com: " + this.ordemAvaliacao[melhorAvaliacao]);
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
        Set<Carta> conjunto = new HashSet<Carta>();
        Collections.addAll(conjunto, cartas);

        for (int j = 0; j < cartas.length; j += 1) {
            Carta.Valor v = cartas[j].getValor();
            ArrayList<Carta> four = new ArrayList<Carta>();
            four.add(new Carta(Carta.Naipe.Espadas, v));
            four.add(new Carta(Carta.Naipe.Paus, v));
            four.add(new Carta(Carta.Naipe.Ouros, v));
            four.add(new Carta(Carta.Naipe.Copas, v));
            if(conjunto.containsAll(four)) {
                return true;
            }
        }
        return false;
    }

    private boolean isFullHouse(Carta[] cartas) {
        return false;
    }

    private boolean isFlush(Carta[] cartas) {
        return false;
    }

    private boolean isStraight(Carta[] cartas) {
        return false;
    }

    private boolean isThreeOfAKind(Carta[] cartas) {
        return false;
    }

    private boolean isTwoPairs(Carta[] cartas) {
        Set<Carta> conjunto = new HashSet<Carta>();
        Collections.addAll(conjunto, cartas);
        ArrayList<Carta.Naipe> naipes = new ArrayList<>(Arrays.asList(Carta.Naipe.values()));
        boolean flag_temUmPar = false;

        for(int i = 0; i < cartas.length; i += 1) {
            ArrayList<Carta.Naipe> naipeRestante = (ArrayList<Carta.Naipe>) naipes.clone();
            naipeRestante.remove(cartas[i].getNaipe());
            for(int j = 0; j < naipeRestante.size(); j += 1){
                Carta checkContains = new Carta(naipeRestante.get(j), cartas[i].getValor());
                if(conjunto.contains(checkContains)) {
                    conjunto.remove(cartas[i]);
                    conjunto.remove(checkContains);
                    flag_temUmPar = true;
                    break;
                }
            }
            if(flag_temUmPar) break;
        }

        if(!flag_temUmPar)
            return false;

        return this.isOnePair(conjunto.toArray(new Carta[conjunto.size()]));
    }

    private boolean isOnePair(Carta[] cartas) {
        Set<Carta> conjunto = new HashSet<Carta>();
        Collections.addAll(conjunto, cartas);
        ArrayList<Carta.Naipe> naipes = new ArrayList<>(Arrays.asList(Carta.Naipe.values()));

        for(int i = 0; i < cartas.length; i += 1) {
            ArrayList<Carta.Naipe> naipeRestante = (ArrayList<Carta.Naipe>) naipes.clone();
            naipeRestante.remove(cartas[i].getNaipe());
            for(int j = 0; j < naipeRestante.size(); j += 1){
                if(conjunto.contains(new Carta(naipeRestante.get(j), cartas[i].getValor())))
                    return true;
            }
        }

        return false;
    }
}
