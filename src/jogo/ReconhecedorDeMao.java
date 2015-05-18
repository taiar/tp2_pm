package jogo;

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

    private Jogador jogadorVencedor;

    private String jogadaVencedora;

    private Carta[] maoVencedora;

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

    private String[] nomeJogada = {
        "Um implacável Royal Straight Flush",
        "Um maravilhoso Straight Flush",
        "Um inexorável Four of A Kind",
        "Um estupendo Full House",
        "Um queridíssimo Flush",
        "Um magnânimo Straight",
        "Uma Trinca Sensacional",
        "Um magnânimo Dois Pares",
        "Um Parzinho gente boa"
    };

    public ReconhecedorDeMao() {
        this.mesa = Mesa.getInstance(0, "");
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
                    if(((Boolean) result).booleanValue()) {
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

    public Jogador resultado() {
        int winnerIndex = -1;
        int melhorAvaliacao = 100;

        for (int i = 0; i < this.mao.length; i += 1)
            if(this.mao[i] < melhorAvaliacao) {
                winnerIndex = i;
                melhorAvaliacao = this.mao[i];
            }

        this.jogadorVencedor = this.jogadores.elementAt(winnerIndex);
        this.jogadaVencedora = this.nomeJogada[melhorAvaliacao];
        this.maoVencedora = this.mergeMao(this.jogadorVencedor);

        return this.jogadores.elementAt(winnerIndex);
    }

    public Jogador getJogadorVencedor() {
        return this.jogadorVencedor;
    }

    public String getJogadaVencedora() {
        return this.jogadaVencedora;
    }

    public Carta[] getMaoVencedora() {
        return this.maoVencedora;
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
        return this.isStraight(cartas) && this.isFlush(cartas);
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
        int[] valores = new int[13];

        for(Carta c : cartas)
            valores[c.getValor().ordinal()] += 1;

        boolean flag_temPar = false;
        boolean flag_temTrio = false;

        for (Integer valore : valores) {
            if (valore == 2) {
                flag_temPar = true;
                continue;
            }
            if (valore == 3)
                flag_temTrio = true;
        }

        return flag_temPar && flag_temTrio;
    }

    private boolean isFlush(Carta[] cartas) {
        ArrayList<Integer> naipes = new ArrayList<Integer>();
        for(Carta c : cartas)
            naipes.add(c.getNaipe().ordinal());

        int[] contaNaipes = new int[4];

        for(int i = 0; i < naipes.size(); i += 1) {
            contaNaipes[naipes.get(i)] += 1;
            if(contaNaipes[naipes.get(i)] == 5)
                return true;
        }
        return false;
    }

    private boolean isStraight(Carta[] cartas) {
        ArrayList<Integer> valores = new ArrayList<Integer>();
        for(Carta c : cartas)
            valores.add(c.getValor().ordinal());
        Collections.sort(valores);

        int seqCount = 0;
        for(int i = 0; i < valores.size(); i += 1) {
            if(seqCount == 0 && i == 0) {
                seqCount += 1;
                continue;
            }
            if(valores.get(i) - valores.get(i - 1) == 1)
                seqCount += 1;
            else
                seqCount = 1;
            if(seqCount == 5)
                return true;
        }
        return false;
    }

    private boolean isThreeOfAKind(Carta[] cartas) {
        Set<Carta> conjunto = new HashSet<Carta>();
        Collections.addAll(conjunto, cartas);
        ArrayList<Carta.Naipe> naipes = new ArrayList<Carta.Naipe>(Arrays.asList(Carta.Naipe.values()));
        int somaContains;

        for(int i = 0; i < cartas.length; i += 1) {
            somaContains = 0;
            ArrayList<Carta.Naipe> naipeRestante = (ArrayList<Carta.Naipe>) naipes.clone();
            naipeRestante.remove(cartas[i].getNaipe());
            for(int j = 0; j < naipeRestante.size(); j += 1){
                if(conjunto.contains(new Carta(naipeRestante.get(j), cartas[i].getValor())))
                    somaContains += 1;
            }
            if(somaContains >= 2)
                return true;
        }

        return false;
    }

    private boolean isTwoPairs(Carta[] cartas) {
        Set<Carta> conjunto = new HashSet<Carta>();
        Collections.addAll(conjunto, cartas);
        ArrayList<Carta.Naipe> naipes = new ArrayList<Carta.Naipe>(Arrays.asList(Carta.Naipe.values()));
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
        ArrayList<Carta.Naipe> naipes = new ArrayList<Carta.Naipe>(Arrays.asList(Carta.Naipe.values()));

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
