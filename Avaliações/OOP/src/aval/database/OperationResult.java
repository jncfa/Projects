
package aval.database;


public class OperationResult{

    private boolean resultado;
    private String mensagem;

    public OperationResult(boolean resultado, String mensagem){
        this.resultado = resultado;
        this.mensagem = mensagem;
    }

    public OperationResult(){
        this.resultado = true;
        this.mensagem = "Operacao efetuada com sucesso!";
    }

    public boolean getResultado(){
        return resultado;
    }
    public String getMensagem(){
        return mensagem;
    }

    @Override
    public String toString(){
        return this.mensagem;
    }
}