package QualidadeDoTexto.model;

import hultig.sumo.Sentence;
import hultig.sumo.Word;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Vocabulario {

    private String nomeDosFicheiros, conteudo;
    private int numeroTokens, numeroPalavras, numeroFrases, numeroType;
    private double mtld, taxaDeRepeticoes, vocabularioDeConteudo, maturidade, densidadeLexical;
 private List<String> listaPalavrasdeConteudo=new ArrayList<>();
//    private HashSet<String> vocabulario = new HashSet<>();
    private String[] vocabulario;

    private List<String> tokens = new ArrayList<>();
    private List<String> frases;
    private List<String> palavras;
     private List<String> listaStamming = new ArrayList<>();

  

    public Vocabulario() {
    }

    public List<String> getListaPalavrasdeConteudo() {
        return listaPalavrasdeConteudo;
    }

    public void setListaPalavrasdeConteudo(List<String> listaPalavrasdeConteudo) {
        this.listaPalavrasdeConteudo = listaPalavrasdeConteudo;
    }

    public List<String> getListaStamming() {
        return listaStamming;
    }

    public void setListaStamming(List<String> listaStamming) {
        this.listaStamming = listaStamming;
    }

    /**
     * @return the nomeDosFicheiros
     */
    public String getNomeDosFicheiros() {
        return nomeDosFicheiros;
    }

    /**
     * @param nomeDosFicheiros the nomeDosFicheiros to set
     */
    public void setNomeDosFicheiros(String nomeDosFicheiros) {
        this.nomeDosFicheiros = nomeDosFicheiros;
    }

    /**
     * @return the conteudo
     */
    public String getconteudo() {
        return getConteudo();
    }

    /**
     * @param conteudo the conteudo to set
     */
    public void setconteudo(String conteudo) {
        this.setConteudo(conteudo);
    }

    /**
     * @return the numeroPalavras
     */
    public int getNumeroTokens() {
        return numeroTokens;
    }

    /**
     * @param numeroPalavras the numeroPalavras to set
     */
    public void setNumeroTokens(int numeroTokens) {
        this.numeroTokens = numeroTokens;
    }

    /**
     * @return the numeroPalavras
     */
    public int getNumeroPalavras() {
        return numeroPalavras;
    }

    /**
     * @param numeroPalavras the numeroPalavras to set
     */
    public void setNumeroPalavras(int numeroPalavras) {
        this.numeroPalavras = numeroPalavras;
    }

    /**
     * @return the numeroFrases
     */
    public int getNumeroFrases() {
        return numeroFrases;
    }

    /**
     * @param numeroFrases the numeroFrases to set
     */
    public void setNumeroFrases(int numeroFrases) {
        this.numeroFrases = numeroFrases;
    }

    /**
     * @return the numeroType
     */
    public int getNumeroType() {
        return numeroType;
    }

    /**
     * @param numeroType the numeroType to set
     */
    public void setNumeroType(int numeroType) {
        this.numeroType = numeroType;
    }

    /**
     * @return the Vocd_D
     */
    public double getMtld() {
        return mtld;
    }

    /**
     * @param Vocd_D the Vocd_D to set
     */
    public void setMtld(double mtld) {
        this.mtld = mtld;
    }

    /**
     * @return the taxaDeRepeticoes
     */
    public double getTaxaDeRepeticoes() {
        return taxaDeRepeticoes;
    }

    /**
     * @param taxaDeRepeticoes the taxaDeRepeticoes to set
     */
    public void setTaxaDeRepeticoes(double taxaDeRepeticoes) {
        this.taxaDeRepeticoes = taxaDeRepeticoes;
    }

    /**
     * @return the vocabularioDeConteudo
     */
    public double getVocabularioDeConteudo() {
        return vocabularioDeConteudo;
    }

    /**
     * @param vocabularioDeConteudo the vocabularioDeConteudo to set
     */
    public void setVocabularioDeConteudo(double vocabularioDeConteudo) {
        this.vocabularioDeConteudo = vocabularioDeConteudo;
    }

    /**
     * @return the maturidade
     */
    public double getMaturidade() {
        return maturidade;
    }

    /**
     * @param maturidade the maturidade to set
     */
    public void setMaturidade(double maturidade) {
        this.maturidade = maturidade;
    }

    /**
     * @return the tokens
     */
    public List<String> getTokens() {
        return tokens;
    }

    /**
     * @param tokens the tokens to set
     */
    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    /**
     * @return the vocab
     */
    public String[] getVocabulario() {
        return vocabulario;
    }

    /**
     * @param vocab the vocab to set
     */
    public void setVocabulario(String[] vocabulario) {
        this.vocabulario=vocabulario;
    }

    /**
     * @return the conteudo
     */
    public String getConteudo() {
        return conteudo;
    }

    /**
     * @param conteudo the conteudo to set
     */
    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    /**
     * @return the densidadeLexical
     */
    public double getDensidadeLexical() {
        return densidadeLexical;
    }

    /**
     * @param densidadeLexical the densidadeLexical to set
     */
    public void setDensidadeLexical(double densidadeLexical) {
        this.densidadeLexical = densidadeLexical;
    }

    /**
     * @return the frases
     */
    public List<String> getFrases() {
        return frases;
    }

    /**
     * @param frases the frases to set
     */
    public void setFrases(List<String> frases) {
        this.frases=frases;
    }

   

    /**
     * @return the palavras
     */
    public List<String> getPalavras() {
        return palavras;
    }

    /**
     * @param palavras the palavras to set
     */
    public void setPalavras(List<String> palavras) {
        this.palavras = palavras;
    }

}
