
package QualidadeDoTexto.model;

public class Fractais {
     Vocabulario vocabulario= new Vocabulario();
     CoesaoECoerencia coesaoECoerencia=new CoesaoECoerencia();

    public Vocabulario getVocabulario() {
        return vocabulario;
    }

    public void setVocabulario(Vocabulario vocabulario) {
        this.vocabulario = vocabulario;
    }

    public CoesaoECoerencia getCoesaoECoerencia() {
        return coesaoECoerencia;
    }

    public void setCoesaoECoerencia(CoesaoECoerencia coesaoECoerencia) {
        this.coesaoECoerencia = coesaoECoerencia;
    }
     
}
