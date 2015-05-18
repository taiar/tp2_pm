package jogo;
import java.lang.Comparable;

public class Carta implements Comparable{
    public static enum Naipe{
        Copas,
        Ouros,
        Paus,
        Espadas
    }

    public static enum Valor{
        Dois,
        Tres,
        Quatro,
        Cinco,
        Seis,
        Sete,
        Oito,
        Nove,
        Dez,
        Valete,
        Rainha,
        Rei,
        As
    }

    private Naipe naipe;
    private Valor valor;

    public Carta(Naipe n, Valor v){
        this.naipe = n;
        this.valor = v;
    }

    public Valor getValor(){
        return this.valor;
    }

    public Naipe getNaipe(){
        return this.naipe;
    }

    public String toString(){
        //return this.valor + " de " + this.naipe;
        String carta = "";

        switch(this.valor){
            case Dois:
                carta = "2";
                break;
            case Tres:
                carta = "3";
                break;
            case Quatro:
                carta = "4";
                break;
            case Cinco:
                carta = "5";
                break;
            case Seis:
                carta = "6";
                break;
            case Sete:
                carta = "7";
                break;
            case Oito:
                carta = "8";
                break;
            case Nove:
                carta = "9";
                break;
            case Dez:
                carta = "10";
                break;
            case Valete:
                carta = "J";
                break;
            case Rainha:
                carta = "Q";
                break;
            case Rei:
                carta = "K";
                break;
            case As:
                carta = "A";
                break;
        }

        String naipe = "";
        switch (this.naipe){
            case Espadas:
                naipe = new String(new int[] { 0x2660 }, 0, 1);
                break;
            case Copas:
                naipe = new String(new int[] { 0x2665 }, 0, 1);
                break;
            case Ouros:
                naipe = new String(new int[] { 0x2666 }, 0, 1);
                break;
            case Paus:
                naipe = new String(new int[] { 0x2663 }, 0, 1);
                break;

        }

        return carta + naipe;
    }

    @Override
    public int compareTo(Object o) {
        final int HIGHER_THAN = 1;
        final int EQUAL_TO = 0;
        final int LESSER_THAN = -1;

        Carta c = (Carta) o;
        if (this.getValor().ordinal() > c.getValor().ordinal()) return HIGHER_THAN;
        if (this.getValor().ordinal() < c.getValor().ordinal()) return LESSER_THAN;

        assert (this.getValor().ordinal() == c.getValor().ordinal());

        return EQUAL_TO;
    }
}
