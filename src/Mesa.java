import excecoes.ExcecaoNumeroMaximoJogadores;

import java.util.ArrayList;

public class Mesa {

    public static final int QUANTIDADE_PADRAO_DINHEIRO = 100;
    public static final int NUMERO_MAXIMO_JOGADORES = 8;

    private static Mesa instanciaMesa = null;
    private Carta[] cartas = null;
    private int pote;
    private Baralho baralho = null;
    private ArrayList<Jogador> jogadores = null;

    private Mesa(){
        this.baralho = new Baralho();
        this.baralho.embaralha();
        this.jogadores = new ArrayList<Jogador>();

        int i = 0;
        for(i = 0; i < NUMERO_MAXIMO_JOGADORES; i++){
            try{
                this.adicionaJogador();
            }catch(ExcecaoNumeroMaximoJogadores e){
                System.out.println("ERRO: Numero maximo de jogadores atingido.");
            }
        }
    }

    public void adicionaJogador() throws ExcecaoNumeroMaximoJogadores{
        if(this.jogadores.size() == NUMERO_MAXIMO_JOGADORES){
            throw new ExcecaoNumeroMaximoJogadores();
        }

        this.jogadores.add(new Jogador(QUANTIDADE_PADRAO_DINHEIRO));
    }

    public static Mesa getInstance(){
        if(instanciaMesa == null){
            instanciaMesa = new Mesa();
        }

        return instanciaMesa;
    }

    public int getPote(){
        return this.pote;
    }

    public void aumentaPote(int quantia){
        assert quantia > 0;

        this.pote += quantia;
    }

    public void flop(Baralho b){

    }

}
