package jogador;

import excecoes.ExcecaoDinheiroInsuficiente;
import jogo.Carta;
import java.util.Random;

public class Jogador {

    private short id;
    private String nome;
    private int dinheiro;
    private int ultimaAposta;
    private Carta cartas[] = null;
    private boolean inAllin;
    private boolean estaNaRodada;
    private boolean estaNoJogo;


    public Jogador(short id, String nome, int dinheiro){
        /* Nao existe um 'set dinheiro'. Assumo que cada jogador vai entrar com a
           mesma quantia na partida, que nao pode ser alterada posteriormente */
        this.id = id;
        this.nome = nome;
        this.dinheiro = dinheiro;
        this.inAllin = false;
        this.cartas = new Carta[2];
        this.estaNoJogo = true;
        this.estaNaRodada = true;
    }

    public int getDinheiro(){
        return this.dinheiro;
    }

    public int getId(){
        return this.id;
    }

    public String getNome(){
        return this.nome;
    }

    public int getUltimaAposta(){
        return this.ultimaAposta;
    }

    public String mostraCartas(){
        return this.cartas[0] + ", " + this.cartas[1];
    }

    public boolean isInAllin(){
        return this.inAllin;
    }

    public void entraNaRodada(){
        this.estaNaRodada = true;
    }

    public void saiDaRodada(){
        System.out.println(this.nome + " saiu da rodada.");
        this.estaNaRodada = false;
    }

    public boolean isEstaNaRodada() {
        // Caso o jogador ja tenha saido do jogo ele nao pode estar na rodada
        if(! this.estaNoJogo){
            return this.estaNoJogo;
        }

        return estaNaRodada;
    }

    public boolean isEstaNoJogo(){
        return this.estaNoJogo;
    }

    // Gera um valor aleatorio para aposta
    public int geraValorAposta(){
        Random r = new Random();
        int valorCartas = this.cartas[0].getValor().ordinal() + this.cartas[1].getValor().ordinal();
        return valorCartas + (r.nextInt() % 20);
    }

    public void saiDoJogo(){
        this.estaNoJogo = false;
        System.out.println(this.nome + " saiu do jogo");
    }

    public void aumentaQuantidadeDinheiro(int quantidade){
        /* Utilizo uma assercao porque assumo que este metodo sera chamado apenas
         *  quando um jogador ganhar o pote ou ter aposta devolvida, o que sempre
         *  e um valor maior que 0 */
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

    public int aposta(int valor){
        int retornado;

        if(this.dinheiro < valor){
            System.out.println("Tentou apostar mais do que tinha. Ira apostar seu total, $" +
                this.dinheiro + " e ficar em all-in");
            this.inAllin = true;
            retornado = this.dinheiro;
            this.dinheiro = 0;
        }else{
            retornado = valor;
            this.dinheiro -= retornado;
            if(this.dinheiro == 0){

                this.inAllin = true;
                System.out.println(this.nome + " esta com $0 agora, em All-in.");
            }
        }

        this.ultimaAposta = retornado;
        return retornado;
    }

    public Carta[] getCartas() {
        return cartas;
    }
}
