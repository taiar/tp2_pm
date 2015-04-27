import java.util.ArrayList;

public class Baralho {
    private ArrayList<Carta> cartas = new ArrayList<>();

    public Baralho(){
        for (Carta.Naipe n : Carta.Naipe.values()){
            for(Carta.Valor v : Carta.Valor.values()){
                this.cartas.add(new Carta(n, v));
            }
        }

        for (Carta c : this.cartas){
            System.out.println(c);
        }
    }
}