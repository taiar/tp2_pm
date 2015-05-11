package jogador;

import excecoes.ExcecaoNumeroInvalidoDeCartasNaMao;
import jogo.Carta;

import java.util.Collections;

public class MaoDePoquer extends Mao {
    public static final short NUMERO_CARTAS_MAO_POQUER = 5;

    public MaoDePoquer(){
        super(NUMERO_CARTAS_MAO_POQUER);
    }

    public void insereCarta(Carta c) throws ExcecaoNumeroInvalidoDeCartasNaMao{
        int cartasNaMao = this.Cartas.size();

        if(cartasNaMao == NUMERO_CARTAS_MAO_POQUER){
            throw new ExcecaoNumeroInvalidoDeCartasNaMao();
        }

        this.Cartas.add(cartasNaMao, c);
    }

    public void ordenaMao(){
        Collections.sort(this.Cartas);
    }

}
