package jogador;

import jogo.Carta;
import java.util.Vector;

public abstract class Mao {
    protected short numeroDeCartasMao;

    protected Vector<Carta> Cartas;

    public Mao(short numeroDeCartasMao){
        this.numeroDeCartasMao = numeroDeCartasMao;
        this.Cartas = new Vector<Carta>();
    }

    public int getNumeroDeCartasMao(){
        return this.numeroDeCartasMao;
    }

    public void printMao(){
        for(Carta c: this.Cartas){
            System.out.println(c);
        }
    }
}
