package jogador;

import excecoes.ExcecaoDinheiroInsuficiente;
import jogo.Carta;

public class Jogador {

    private short id;
    private String nome;
    private int dinheiro;
    private Carta cartas[] = null;
    private boolean inAllin;


    public Jogador(short id, String nome, int dinheiro){
        /* Nao existe um 'set dinheiro'. Assumo que cada jogador vai entrar com a
           mesma quantia na partida, que nao pode ser alterada posteriormente */
        this.id = id;
        this.nome = nome;
        this.dinheiro = dinheiro;
        this.inAllin = false;
        this.cartas = new Carta[2];
    }

    public int getDinheiro(){
        return this.dinheiro;
    }

    public String getNome(){
        return this.nome;
    }

    // REMOVER!!!! TESTE!!!!
    public String mostraCartas(){
        return this.getNome() + ": " + this.cartas[0] + ", " + this.cartas[1];
    }

    public boolean isInAllin(){
        return this.inAllin;
    }

    public void aumentaQuantidadeDinheiro(int quantidade){
        /* Utilizo uma assercao porque assumo que este metodo sera chamado apenas
         *  quando um jogador ganhar o pote, o que sempre e um valor maior que 0 */
        assert quantidade > 0;

        this.dinheiro += quantidade;
        if(this.inAllin){
            this.inAllin = false;
        }
    }

    public void retiraDinheiro(int retirada) throws ExcecaoDinheiroInsuficiente{
        if (this.dinheiro < retirada){
            throw new ExcecaoDinheiroInsuficiente();
        }

        this.dinheiro -= retirada;

        if(this.dinheiro == 0){
            this.inAllin = true;
        }
    }

    public void ganhaCartas(Carta a, Carta b){
        this.cartas[0] = a;
        this.cartas[1] = b;
    }
}
