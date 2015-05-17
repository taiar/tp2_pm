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

    public void setValor(Valor v) { this.valor = v; }

    public Naipe getNaipe(){
        return this.naipe;
    }

    public void setNaipe(Naipe n) { this.naipe = n; }

    public String toString(){
        return this.valor + " de " + this.naipe;
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

    @Override
    public boolean equals(Object o) {
        Carta c = (Carta) o;
        return this.getNaipe() == c.getNaipe() && this.getValor() == c.getValor();
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(
            Integer.toString(this.getValor().ordinal()) +
            Integer.toString(this.getNaipe().ordinal())
        );
    }
}
