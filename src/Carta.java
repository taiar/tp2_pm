public class Carta {
    public static enum Naipe{
        Copas,
        Ouros,
        Paus,
        Espadas
    }

    public static enum Valor{
        As,
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
        Rei
    }

    private Naipe naipe;
    private Valor valor;

    public Carta(Naipe n, Valor v){
        this.naipe = n;
        this.valor = v;
    }

    public String toString(){
        return this.valor + " de " + this.naipe;
    }
}
