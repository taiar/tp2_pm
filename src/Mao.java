/**
 * Created by ghapereira on 4/24/15.
 */
public abstract class Mao {
    private short numeroDeCartasMao;

    public Mao(short numeroDeCartasMao){
        this.numeroDeCartasMao = numeroDeCartasMao;
    }

    public int getNumeroDeCartasMao(){
        return this.numeroDeCartasMao;
    }
}