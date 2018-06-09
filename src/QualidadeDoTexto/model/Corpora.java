/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QualidadeDoTexto.model;

/**
 *
 * @author hp_probook
 */
public class Corpora {
    String conteudo;
    private int totalFicheiros, totalFrases,totalTokens,totalTypes,totalPalavras;

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public int getTotalFicheiros() {
        return totalFicheiros;
    }

    public void setTotalFicheiros(int totalFicheiros) {
        this.totalFicheiros = totalFicheiros;
    }

    public int getTotalFrases() {
        return totalFrases;
    }

    public void setTotalFrases(int totalFrases) {
        this.totalFrases = totalFrases;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public int getTotalTypes() {
        return totalTypes;
    }

    public void setTotalTypes(int totalTypes) {
        this.totalTypes = totalTypes;
    }

    public void setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
    }

    public int getTotalPalavras() {
        return totalPalavras;
    }

    public void setTotalPalavras(int totalPalavras) {
        this.totalPalavras = totalPalavras;
    }
    
}
