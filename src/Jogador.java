import excecoes.ExcecaoDinheiroInsuficiente;
import excecoes.ExcecaoValorNegativo;

public class Jogador {

    private int dinheiro;
    private Carta cartas[] = new Carta[2];
    private boolean inAllin;

    public Jogador(int dinheiro){
        /* Nao existe um 'set dinheiro'. Assumo que cada jogador vai entrar com a
           mesma quantia na partida, que nao pode ser alterada posteriormente */
        this.dinheiro = dinheiro;
        this.inAllin = false;
    }

    public int getDinheiro(){
        return this.dinheiro;
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

    // Neste metodo vou montar as maos de acordo com as cartas na mesa
    // Pretendo fazer para verificar de 3 a 5 cartas, para que seja possivel
    // montar maos 'parciais'
    public void montaMao(Mesa m){

    }
}
