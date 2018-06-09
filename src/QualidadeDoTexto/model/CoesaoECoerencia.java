package QualidadeDoTexto.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CoesaoECoerencia {

    private double[][] projecaoPAcc;
    private int totalPalavras;
    private double coerenciaPAcc;
    private double similaridadeInicioFim;
    private double mediaDaSobreposicaoDeSubstantivos;
    private double mediaDaSemelhancaSemanticaEntreFrases;
    private String nomeDosFicheiros;
    private List<String> vocabularioEntidades = null;
    private int numFrases, numEntidades;
    private double insidenciaDePalavrasDeConteudo;
    private double densidadeDePronomes;
    private double mediaDePronomesPorFrase;
    private double sobreposicaoDePalavrasEmFrasesAdjcentes;
    private double mdldLemas, trasicaoSS, trasicaoSO, trasicaoOO, trasicaoOS;
    private double SobreposicaoMediadDeLemasEmFrasesAdjacentes;
    private double razaoDePronomesPorSubsitantivos;
    private double comprimentoMedioDaFrase, comprimentoMedioDaPalavra;
    private double mediaDaSobreposicaoDeLemasDeSubstantivos;
    private double mtldPalavrasDeConteudos, mediaDeOperadoresLogicosPorFrases;
    private String conteudo;
    private String[] conteudoEmLinhas;
    private String[] Frases;
    private List<List<String>> listaPapeisEntidadesPorFrase;
    private List<String> listaPalavrasdeConteudo, listaPalavrasdeConteudo2;
    private List<String> tokens = new ArrayList<>();
    private List<List<String>> tokensPorFrase = new ArrayList<>();
    private List<String> pronomes = new ArrayList<>(), listaDeSubstantivos = new ArrayList<>();
    private List<List<String>> listaEntidadesPorFrase;
    private List<List<String>> listaDeSubstantivosPorFrase;
    private List<List<String>> listaDePronomesPorFrase;

    public List<List<String>> getListaDePronomesPorFrase() {
        return listaDePronomesPorFrase;
    }

    public void setListaDePronomesPorFrase(List<List<String>> listaDePronomesPorFrase) {
        this.listaDePronomesPorFrase = listaDePronomesPorFrase;
    }

    public List<String> getListaDeSubstantivos() {
        return listaDeSubstantivos;
    }

    public void setListaDeSubstantivos(List<String> listaDeSubstantivos) {
        this.listaDeSubstantivos = listaDeSubstantivos;
    }

    public List<List<String>> getListaDeSubstantivosPorFrase() {
        return listaDeSubstantivosPorFrase;
    }

    public void setListaDeSubstantivosPorFrase(List<List<String>> listaDeSubstantivosPorFrase) {
        this.listaDeSubstantivosPorFrase = listaDeSubstantivosPorFrase;
    }

    public List<List<String>> getTokensPorFrase() {
        return tokensPorFrase;
    }

    public void setTokensPorFrase(List<List<String>> tokensPorFrase) {
        this.tokensPorFrase = tokensPorFrase;
    }

    public List<String> getPronomes() {
        return pronomes;
    }

    public void setPronomes(List<String> pronomes) {
        this.pronomes = pronomes;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public double getTrasicaoSS() {
        return trasicaoSS;
    }

    public void setTrasicaoSS(double trasicaoSS) {
        this.trasicaoSS = trasicaoSS;
    }

    public double getTrasicaoSO() {
        return trasicaoSO;
    }

    public void setTrasicaoSO(double trasicaoSO) {
        this.trasicaoSO = trasicaoSO;
    }

    public double getTrasicaoOO() {
        return trasicaoOO;
    }

    public void setTrasicaoOO(double trasicaoOO) {
        this.trasicaoOO = trasicaoOO;
    }

    public double getTrasicaoOS() {
        return trasicaoOS;
    }

    public void setTrasicaoOS(double trasicaoOS) {
        this.trasicaoOS = trasicaoOS;
    }

    public int getTotalPalavras() {
        return totalPalavras;
    }

    public void setTotalPalavras(int totalPalavras) {
        this.totalPalavras = totalPalavras;
    }

    public double getMediaDeOperadoresLogicosPorFrases() {
        return mediaDeOperadoresLogicosPorFrases;
    }

    public void setMediaDeOperadoresLogicosPorFrases(double mediaDeOperadoresLogicosPorFrases) {
        this.mediaDeOperadoresLogicosPorFrases = mediaDeOperadoresLogicosPorFrases;
    }

    public List<String> getListaPalavrasdeConteudo2() {
        return listaPalavrasdeConteudo2;
    }

    public void setListaPalavrasdeConteudo2(List<String> listaPalavrasdeConteudo2) {
        this.listaPalavrasdeConteudo2 = listaPalavrasdeConteudo2;
    }

    public List<String> getListaPalavrasdeConteudo() {
        return listaPalavrasdeConteudo;
    }

    public void setListaPalavrasdeConteudo(List<String> listaPalavrasdeConteudo) {
        this.listaPalavrasdeConteudo = listaPalavrasdeConteudo;
    }

    public List<List<String>> getListaPapeisEntidadesPorFrase() {
        return listaPapeisEntidadesPorFrase;
    }

    public void setListaPapeisEntidadesPorFrase(List<List<String>> listaPapeisEntidadesPorFrase) {
        this.listaPapeisEntidadesPorFrase = listaPapeisEntidadesPorFrase;
    }

    public String[] getFrases() {
        return Frases;
    }

    public void setFrases(String[] Frases) {
        this.Frases = Frases;
    }

    public List<List<String>> getListaEntidadesPorFrase() {
        return listaEntidadesPorFrase;
    }

    public void setListaEntidadesPorFrase(List<List<String>> listaEntidadesPorFrase) {
        this.listaEntidadesPorFrase = listaEntidadesPorFrase;
    }

    public String[] getConteudoEmLinhas() {
        return conteudoEmLinhas;
    }

    public void setConteudoEmLinhas(String[] conteudoEmLinhas) {
        this.conteudoEmLinhas = conteudoEmLinhas;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public double getMtldPalavrasDeConteudos() {
        return mtldPalavrasDeConteudos;
    }

    public void setMtldPalavrasDeConteudos(double mtldPalavrasDeConteudos) {
        this.mtldPalavrasDeConteudos = mtldPalavrasDeConteudos;
    }

    public double getComprimentoMedioDaFrase() {
        return comprimentoMedioDaFrase;
    }

    public void setComprimentoMedioDaFrase(double comprimentoMedioDaFrase) {
        this.comprimentoMedioDaFrase = comprimentoMedioDaFrase;
    }

    public double getComprimentoMedioDaPalavra() {
        return comprimentoMedioDaPalavra;
    }

    public void setComprimentoMedioDaPalavra(double comprimentoMedioDaPalavra) {
        this.comprimentoMedioDaPalavra = comprimentoMedioDaPalavra;
    }

    public double getMediaDaSobreposicaoDeLemasDeSubstantivos() {
        return mediaDaSobreposicaoDeLemasDeSubstantivos;
    }

    public void setMediaDaSobreposicaoDeLemasDeSubstantivos(double mediaDaSobreposicaoDeLemasDeSubstantivos) {
        this.mediaDaSobreposicaoDeLemasDeSubstantivos = mediaDaSobreposicaoDeLemasDeSubstantivos;
    }

    public double getRazaoDePronomesPorSubsitantivos() {
        return razaoDePronomesPorSubsitantivos;
    }

    public void setRazaoDePronomesPorSubsitantivos(double razaoDePronomesPorSubsitantivos) {
        this.razaoDePronomesPorSubsitantivos = razaoDePronomesPorSubsitantivos;
    }

    public double getSobreposicaoMediadDeLemasEmFrasesAdjacentes() {
        return SobreposicaoMediadDeLemasEmFrasesAdjacentes;
    }

    public void setSobreposicaoMediadDeLemasEmFrasesAdjacentes(double SobreposicaoMediadDeLemasEmFrasesAdjacentes) {
        this.SobreposicaoMediadDeLemasEmFrasesAdjacentes = SobreposicaoMediadDeLemasEmFrasesAdjacentes;
    }

    public double getMdldLemas() {
        return mdldLemas;
    }

    public void setMdldLemas(double mdldLemas) {
        this.mdldLemas = mdldLemas;
    }

    public double getSobreposicaoDePalavrasEmFrasesAdjcentes() {
        return sobreposicaoDePalavrasEmFrasesAdjcentes;
    }

    public void setSobreposicaoDePalavrasEmFrasesAdjcentes(double sobreposicaoDePalavrasEmFrasesAdjcentes) {
        this.sobreposicaoDePalavrasEmFrasesAdjcentes = sobreposicaoDePalavrasEmFrasesAdjcentes;
    }

    public double getMediaDePronomesPorFrase() {
        return mediaDePronomesPorFrase;
    }

    public void setMediaDePronomesPorFrase(double mediaDePronomesPorFrase) {
        this.mediaDePronomesPorFrase = mediaDePronomesPorFrase;
    }

    public double getDensidadeDePronomes() {
        return densidadeDePronomes;
    }

    public void setDensidadeDePronomes(double densidadeDePronomes) {
        this.densidadeDePronomes = densidadeDePronomes;
    }

    public double getInsidenciaDePalavrasDeConteudo() {
        return insidenciaDePalavrasDeConteudo;
    }

    public void setInsidenciaDePalavrasDeConteudo(double insidnciaDePalavrasDeConteudo) {
        this.insidenciaDePalavrasDeConteudo = insidnciaDePalavrasDeConteudo;
    }

    public String getNomeDosFicheiros() {
        return nomeDosFicheiros;
    }

    public void setNomeDosFicheiros(String nomeDosFicheiros) {
        this.nomeDosFicheiros = nomeDosFicheiros;
    }

    public double getCoerenciaPAcc() {
        return coerenciaPAcc;
    }

    public void setCoerenciaPAcc(double coerenciaPAcc) {
        this.coerenciaPAcc = coerenciaPAcc;
    }

    public int getNumFrases() {
        return numFrases;
    }

    public void setNumFrases(int numFrases) {
        this.numFrases = numFrases;
    }

    public int getNumEntidades() {
        return numEntidades;
    }

    public void setNumEntidades(int numEntidades) {
        this.numEntidades = numEntidades;
    }

    public List<String> getVocabularioEntidades() {
        return vocabularioEntidades;
    }

    public void setVocabularioEntidades(List<String> vocabularioEntidades) {
        this.vocabularioEntidades = vocabularioEntidades;
    }

    public double[][] getProjecaoPAcc() {
        return projecaoPAcc;
    }

    public void setProjecaoPAcc(double[][] projecaoPAcc) {
        this.projecaoPAcc = projecaoPAcc;
    }

    public double getSimilaridadeInicioFim() {
        return similaridadeInicioFim;
    }

    public void setSimilaridadeInicioFim(double similaridadeInicioFim) {
        this.similaridadeInicioFim = similaridadeInicioFim;
    }

    public double getMediaDaSemelhancaSemanticaEntreFrases() {
        return mediaDaSemelhancaSemanticaEntreFrases;
    }

    public void setMediaDaSemelhancaSemanticaEntreFrases(double mediaDaSemelhancaSemanticaEntreFrases) {
        this.mediaDaSemelhancaSemanticaEntreFrases = mediaDaSemelhancaSemanticaEntreFrases;
    }

    public double getMediaDaSobreposicaoDeSubstantivos() {
        return mediaDaSobreposicaoDeSubstantivos;
    }

    public void setMediaDaSobreposicaoDeSubstantivos(double mediaDaSobreposicaoDeSubstantivos) {
        this.mediaDaSobreposicaoDeSubstantivos = mediaDaSobreposicaoDeSubstantivos;
    }

}
