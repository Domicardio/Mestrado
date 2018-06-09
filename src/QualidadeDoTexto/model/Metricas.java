package QualidadeDoTexto.model;

import static QualidadeDoTexto.model.Ficheiros.getRemocaoDeStopWords;
import hultig.sumo.Sentence;
import hultig.sumo.Text;
import hultig.sumo.Word;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class Metricas {

    Sentence sentencas;
    private static int totalFrase = 0, total;
    private static List<List<String>> ListaPapeisEntidadesPorFrase = new ArrayList<>();
    private static List<List<String>> ListaEntidadesPorFrase = new ArrayList<>();
    private static double val, somatorio = 0, contador = 0, retorno = 0, totalPronomes, totalPalavrasRepetidas = 0.0,
            totalRepeticoes = 0.0, mtldInversoReverso = 0.0, ttr = 0.0, fatorPadrao = 0.720, fatores = 0.0,
            fatorParcial = 0.0, totalPalavras = 0.0, mtld = 0, mtldInverso = 0, mtldReverso = 0,
            densidade = 0, totalPalavrasDeConteudo = 0.0, valorDaCelula, bonus = 0, penalidade = 0, normal;
    private static List<String> tokensTtr = new ArrayList<String>(), tokensMtld = new ArrayList<String>();
    private static String texto, palavra;
    private static Text txt = null;
    private static Set<String> types = new HashSet<String>();
    private static List<String> listaSubstantivos1 = new ArrayList<>();
    private static List<String> lista2 = new ArrayList<>(), lista3 = new ArrayList<>(), lista1 = new ArrayList<>();
    private static LinkedHashSet<String> vocabulario = new LinkedHashSet<>();
 private static double[][] projecaoPAcc=new double[2][2];
    public static int gerarNumeroAleatorio(int limiteinferior, int limitesuperior) {
        int retorno = 0;
        Random gerador = new Random();
        retorno = limiteinferior + gerador.nextInt(limitesuperior);
        return retorno;
    }

    public static double arredondarDecimal(double numero, int precisao) {
        BigDecimal bd;
        retorno = 0;
        bd = new BigDecimal(numero).setScale(precisao, RoundingMode.HALF_EVEN);
        retorno = bd.doubleValue();
        return retorno;
    }

//    public static int contarSentencas(String conteudo, String lingua) throws Exception {
//        InputStream inputStream = null;
//        String sentences[] = null;
//       
//            if (lingua.endsWith("en")) {
//                inputStream = new FileInputStream("/home/hp_probook/NetBeansProjects/Dissertacao/src/QualidadeDoTexto/resource/english/en-sent.bin");
//            } else if (lingua.endsWith("pt")) {
//                inputStream = new FileInputStream("/home/hp_probook/NetBeansProjects/Dissertacao/src/QualidadeDoTexto/resource/portuguese/pt-sent.bin");
//            }
//
//            SentenceModel model = new SentenceModel(inputStream);
//            SentenceDetectorME detector = new SentenceDetectorME(model);
//            sentences = detector.sentDetect(conteudo);
//        
//        return sentences.length;
//    }

    public static int contarPalavras(List<String> tokens) throws Exception {
        total = 0;
        for (String s : tokens) {
            if (new Word(s).isWord()) //vocab.add(s);
            {
                total++;
            }
        }
        return total;
    }

    public static double getMtldInversoEReverso(List<String> listtokens) throws Exception {

        mtldInversoReverso = 0.0;
        ttr = 0.0;
        fatorPadrao = 0.720;
        fatores = 0.0;
        fatorParcial = 0.0;
        totalPalavras = 0.0;
        tokensTtr = new ArrayList<>();
        tokensMtld = new ArrayList<>();
        types = new HashSet<String>();
        total = 0;
        for (String s : listtokens) {
            total++;
            if (new Word(s).isWord()) {
                tokensMtld.add(s.toLowerCase());
                types.add(s.toLowerCase());
                tokensTtr.add(s.toLowerCase());

                ttr = new Integer(types.size()).doubleValue() / new Integer(tokensTtr.size()).doubleValue();

                if (ttr <= fatorPadrao) {
                    fatores++;
                    ttr = 0.0;
                    types.clear();
                    tokensTtr.clear();
                }
            }
            if (ttr == 1) {
                ttr = 0.999;
            }
            if (total == listtokens.size()) {
                fatorParcial = (1.0 - ttr) / (1.0 - fatorPadrao);
                fatores += fatorParcial;
            }
        }

        if (fatores == 0) {
            mtldInversoReverso = 0.0;
        } else {
            mtldInversoReverso = tokensMtld.size() / fatores;
        }
        tokensTtr.clear();
        tokensMtld.clear();
        types.clear();
        return mtldInversoReverso;
    }

    public static double getTaxaDeRepeticao(Vocabulario vocab, String lingua) throws Exception {
        retorno = 0.0;
        val = 0;
        lista2 = new ArrayList<>();
        if (lingua.equalsIgnoreCase("en")) {
            lista2 = Ficheiros.getListaSteming(getRemocaoDeStopWords(
                    vocab.getPalavras(),
                    Inicializacoes.stopoWordsIngles), Inicializacoes.stemmerIngles);
            total = lista2.size();
            txt = new Text(lista2.toArray(new String[total]));
        } else if (lingua.equalsIgnoreCase("pt")) {
            lista2 = Ficheiros.getListaSteming(getRemocaoDeStopWords(
                    vocab.getPalavras(),
                    Inicializacoes.stopoWordsPortugues), Inicializacoes.stemmerPortugues);
            total = lista2.size();
            txt = new Text(lista2.toArray(new String[total]));
        }
        totalPalavrasRepetidas = 0;
        totalRepeticoes = 0;
        for (String s : txt.getVocab()) {
            contador = txt.freq(s);
            if (contador >= 2) {
                totalPalavrasRepetidas++;
                totalRepeticoes += contador;
            }
        }
        //if(totalPalavrasRepetidas>0)
        //val=(totalRepeticoes/totalPalavrasRepetidas);
        if (totalPalavrasRepetidas == 0.0) {
            retorno = 0.0;
        } else {
            retorno = totalRepeticoes / total;
        }
        return arredondarDecimal(retorno, 4);
    }

    public static double getMTLD(List<String> palavras) throws Exception {
        mtld = 0;
        mtldInverso = 0;
        mtldReverso = 0;
        mtldInverso = getMtldInversoEReverso(palavras);
        Collections.reverse(palavras);
        mtldReverso = getMtldInversoEReverso(palavras);
        mtld = (mtldInverso + mtldReverso) / 2;
        return arredondarDecimal(mtld, 4);
    }

    public static double getMaturidade(Vocabulario voc, HashMap<String, String> getChaveValor, List<String> dicionario) {

        bonus = 0;
        penalidade = 0;
        normal = 0;
        retorno = 0.0;
        txt = new Text(voc.getPalavras().toArray(new String[voc.getPalavras().size()]));
        totalPalavras = txt.getVocab().length;
        for (String s : txt.getVocab()) {
//            palavra = txt.getVocab()[i];
            if (getChaveValor.containsKey(s.toLowerCase())) {
                normal += txt.freq(s);
            } else if (dicionario.contains(s)) {
                bonus += txt.freq(s) * 2;
            } else {
                penalidade += txt.freq(s);
            }
        }
        retorno = (normal+bonus -penalidade )/ totalPalavras;
        return arredondarDecimal(retorno, 4);
    }

    /**
     *
     * @param palavrasDeConteudo
     * @param numeroDePalavras
     * @return A densidade lexical é o termo mais usado para descrever a
     * proporção de palavras de conteúdo (substantivos, verbos, adjetivos e
     * muitas vezes também advérbios) ao número total de palavras no texto.
     * @throws Exception
     */
    public static double getDensidade(List<String> palavrasDeConteudo, int numeroDePalavras) throws Exception {
        /* A densidade lexical é o termo mais usado para descrever a proporção de palavras 
        de conteúdo (substantivos, verbos, adjetivos e muitas vezes também 
        advérbios) ao número total de palavras no texto. */
        densidade = 0;
        totalPalavrasDeConteudo = 0.0;

        totalPalavrasDeConteudo = palavrasDeConteudo.size();
        if (totalPalavrasDeConteudo == 0.0) {
            densidade = 0.0;
        } else {
            densidade = totalPalavrasDeConteudo / numeroDePalavras;
        }
        return arredondarDecimal(densidade, 4);
    }

    //Medidas de coesão e coerência
    public static int getPesoDaEntidadeNaFrase(List<String> listaDeEntidadesComPapeisNaFrase,
            List<String> entidadesNaFrase, String entidade) {
        int pesoDaEntidade = 0;
        if (entidadesNaFrase.contains(entidade)) {
            if (listaDeEntidadesComPapeisNaFrase.contains(entidade.concat("---s"))) {
                pesoDaEntidade = 3;
            } else if (listaDeEntidadesComPapeisNaFrase.contains(entidade.concat("---s"))) {
                pesoDaEntidade += 2;
            } else {
                pesoDaEntidade += 1;
            }
        }
        return pesoDaEntidade;
    }

//    public static double[][] getMatrizDeInsidencia(CoesaoECoerencia cocoe, List<List<String>> listaEntidadesPorFrase,
//            List<List<String>> listaPapeisEntidadesPorFrase) {
//        cocoe.setNumFrases(listaEntidadesPorFrase.size());
//        cocoe.setNumEntidades(cocoe.getVocabularioEntidades().size());
//        double[][] matrizInsidencia = new double[cocoe.getNumFrases()][cocoe.getNumEntidades()];
//        int ind = 0;
//        for (int i = 0; i < cocoe.getNumFrases(); i++) {
//            ind = 0;
//            for (int j = 0; j < cocoe.getNumEntidades(); j++) {
//                ind++;
//                String entidade = cocoe.getVocabularioEntidades().get(j);
//                if (listaEntidadesPorFrase.get(i).contains(entidade)) {
//                    if (listaPapeisEntidadesPorFrase.get(i).contains(entidade.concat("---S").trim())) {
//                        //System.out.print(3 + "|");
//                        matrizInsidencia[i][j] = 3;
//                        //matrizInsidencia.set(i, j, 3);
//
//                    } else if (listaPapeisEntidadesPorFrase.get(i).contains(entidade.concat("---O").trim())) {
//                        // System.out.print(2 + "|");
//                        matrizInsidencia[i][j] = 2;
//                        // matrizInsidencia.set(i, j, 2);
//                    } else {
//                        // System.out.print(1 + "|");
//                        matrizInsidencia[i][j] = 1;
//                        //matrizInsidencia.set(i, j, 1);
//                    }
//
//                } else {
//
//                    // System.out.print(" " + "|");
//                }
//            }
//            //System.out.println("");
//        }
//        return matrizInsidencia;
//    }
//    public static double[][] getPrijecaoPU(List<List<String>> listaEntidadesPorFrase) {
//        int numFrases = listaEntidadesPorFrase.size();
//        int valorDaCelula = 0;
//        double[][] projecaoPU = new double[numFrases][numFrases];
//        for (int i = 0; i < numFrases; i++) {
//            valorDaCelula = 0;
//            for (int j = 0; j < numFrases; j++) {
//                if (i != j) {
//                    for (String s : listaEntidadesPorFrase.get(i)) {
//                        if (listaEntidadesPorFrase.get(j).contains(s)) {
//                            valorDaCelula = 1;
//                            break;
//                        }
//                    }
//                }
//                projecaoPU[i][j] = valorDaCelula;
//                //projecaoPU.set(i, j, valorDaCelula);
//                //System.out.print(valorDaCelula + "|");
//                valorDaCelula = 0;
//            }
//            //System.out.println("");
//        }
//
//        return projecaoPU;
//    }
//    public static double[][] getPrijecaoPW(List<List<String>> listaEntidadesPorFrase) {
//        int numFrases = listaEntidadesPorFrase.size();
//        valorDaCelula = 0;
//        double[][] projecaoPW = new double[numFrases][numFrases];
//        for (int i = 0; i < numFrases; i++) {
//            valorDaCelula = 0;
//            for (int j = 0; j < numFrases; j++) {
//                if (i != j) {
//                    for (String s : listaEntidadesPorFrase.get(i)) {
//                        if (listaEntidadesPorFrase.get(j).contains(s)) {
//                            valorDaCelula += 1;
//                        }
//                    }
//                }
//                projecaoPW[i][j] = valorDaCelula;
//                // projecaoPW.set(i, j, valorDaCelula);
//                //System.out.print(valorDaCelula + "|");
//                valorDaCelula = 0;
//            }
//            //System.out.println("");
//        }
//
//        return projecaoPW;
//    }

    public static double[][] getPrijecaoPAcc(List<List<String>> listaEntidadesPorFrase,
            List<List<String>> listaPapeisEntidadesPorFrase) {
        total = listaEntidadesPorFrase.size();
        //int valorDaCelula = 0;
      
        for (int i = 0; i < total; i++) {
            for (int j = 0; j < total; j++) {
                valorDaCelula = 0;

//                if (i != j) {
//                    for (String s : listaEntidadesPorFrase.get(i)) {
//                        valorDaCelula += (getPesoDaEntidadeNaFrase(listaPapeisEntidadesPorFrase.get(i),
//                                listaEntidadesPorFrase.get(j), s) * getPesoDaEntidadeNaFrase(listaPapeisEntidadesPorFrase.get(j),
//                                listaEntidadesPorFrase.get(j), s));
//                    }
//                }
                if (i == 0 && j == 1) {
                    for (String s : listaEntidadesPorFrase.get(i)) {
                        if (listaEntidadesPorFrase.get(j).contains(s)) {
                            valorDaCelula += (getPesoDaEntidadeNaFrase(listaPapeisEntidadesPorFrase.get(i),
                                    listaEntidadesPorFrase.get(i), s) * getPesoDaEntidadeNaFrase(listaPapeisEntidadesPorFrase.get(j),
                                    listaEntidadesPorFrase.get(j), s));
                        }
                    }
                }
//                if (j - i > 0) {
//                    projecaoPAcc[i][j] = valorDaCelula / (j - i);
//                } else {
                projecaoPAcc[i][j] = valorDaCelula;
//                }

                //projecaoPAcc.set(i, j, valorDaCelula);
                // System.out.print(valorDaCelula + "|");
                valorDaCelula = 0;
            }
            // System.out.println("");
        }
        return projecaoPAcc;
    }

    public static double getCoerenciaDoTexto(CoesaoECoerencia cocoe) {
        try {
            ListaEntidadesPorFrase = new ArrayList<>();
        ListaPapeisEntidadesPorFrase = new ArrayList<>();
        retorno = 0;
//        cocoe.setProjecaoPAcc(Metricas.getPrijecaoPAcc(cocoe.getListaEntidadesPorFrase(),
//                cocoe.getListaPapeisEntidadesPorFrase()));
        totalFrase = cocoe.getListaEntidadesPorFrase().size();
        for (int i = 0; i < totalFrase; i++) {
            if (i > 0) {
                 ListaEntidadesPorFrase.clear();
        ListaPapeisEntidadesPorFrase.clear();
               /// System.out.println("Frase: " + i);
                ListaEntidadesPorFrase.add(cocoe.getListaEntidadesPorFrase().get(i - 1));
                ListaEntidadesPorFrase.add(cocoe.getListaEntidadesPorFrase().get(i));
                ListaPapeisEntidadesPorFrase.add(cocoe.getListaPapeisEntidadesPorFrase().get(i - 1));
                ListaPapeisEntidadesPorFrase.add(cocoe.getListaPapeisEntidadesPorFrase().get(i));
                retorno += getSomatorio(getPrijecaoPAcc(ListaEntidadesPorFrase, ListaPapeisEntidadesPorFrase));
                //System.out.println("Passou!");
            }
        }
        retorno = (retorno / totalFrase);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return arredondarDecimal(retorno, 4);
    }

    public static double getSomatorio(double[][] matrizDeInsidencia) {
        somatorio = 0;
        contador = 0;
        for (int i = 0; i < matrizDeInsidencia.length; i++) {
            for (int j = 0; j < matrizDeInsidencia[0].length; j++) {
                val = matrizDeInsidencia[i][j];
                if (val > 0) {
                    somatorio += val;
                    contador++;
                }
            }
        }
        // coerencia = (somatorio / (double) matrizDeInsidencia.length);
        return somatorio;
    }

    public static double getSujeitoObjecto(CoesaoECoerencia cocoe, String tipo) {
        try {
             retorno = 0;
        totalFrase = cocoe.getListaEntidadesPorFrase().size();
        palavra = "";
        somatorio = 0;
        lista1 = new LinkedList<>();
        lista2 = new LinkedList<>();
        lista3 = new LinkedList<>();
        listaSubstantivos1 = new LinkedList<>();
        for (int i = 0; i < totalFrase; i++) {

            if (i < totalFrase - 2) {
                listaSubstantivos1 = cocoe.getListaEntidadesPorFrase().get(i);
                lista1 = cocoe.getListaEntidadesPorFrase().get(i + 1);
                lista2 = cocoe.getListaPapeisEntidadesPorFrase().get(i);
                lista3 = cocoe.getListaPapeisEntidadesPorFrase().get(i + 1);
                for (int j = 0; j < listaSubstantivos1.size(); j++) {
                    palavra = listaSubstantivos1.get(j);

                    if (lista1.contains(palavra)) {
                        switch (tipo) {
                            case "sujeitoSujeito":
                                if (lista2.contains(palavra.concat("---s")) && lista3.contains(palavra.concat("---s"))) {
                                    // if (cocoe.getListaPapeisEntidadesPorFrase().get(i + 1).contains(palavra.concat("---s"))) {
                                    somatorio++;
                                    //}
                                }
                                break;
                            case "sujeitoObjeto":
                                if (lista2.contains(palavra.concat("---s")) && lista3.contains(palavra.concat("---o"))) {
                                    //if (cocoe.getListaPapeisEntidadesPorFrase().get(i + 1).contains(palavra.concat("---o"))) {
                                    somatorio++;
                                    //}
                                }
                                break;
                            case "objetoObjeto":
                                if (lista2.contains(palavra.concat("---o")) && lista3.contains(palavra.concat("---o"))) {
                                    // if (cocoe.getListaPapeisEntidadesPorFrase().get(i + 1).contains(palavra.concat("---o"))) {
                                    somatorio++;
                                    //  }
                                }
                                break;
                            case "objetoSujeito":
                                if (lista2.contains(palavra.concat("---o")) && lista3.contains(palavra.concat("---s"))) {
                                    //if (cocoe.getListaPapeisEntidadesPorFrase().get(i + 1).contains(palavra.concat("---s"))) {
                                    somatorio++;
                                    //}
                                }
                                break;
                        }
                    }
                }
            }
        }
        retorno = somatorio / totalFrase;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return arredondarDecimal(retorno, 4);
    }

    public static double getMediaDeOperadoresLogicosPorFrase(List<String> listaDeFrases, String lingua) {
        retorno = 0.0;
        total = 0;
        somatorio = 0.0;
        for (String s : listaDeFrases) {
            total = getTota(Ficheiros.getOperadoresLogicos(s, lingua));
            totalPalavras = getTota(Ficheiros.getListaDePalavras(s));

            if (totalPalavras > 0) {
                somatorio += total / (double) totalPalavras;
            }
            total = 0;
        }
        retorno = somatorio / (double) listaDeFrases.size();
        return arredondarDecimal(retorno, 4);
    }

    public static double getComprimentoMedio(CoesaoECoerencia cocoe, String tipo) throws Exception {
        retorno = 0;
        somatorio = 0;
        if (tipo.equalsIgnoreCase("palavra")) {
            total = cocoe.getTokens().size();
            for (int i = 0; i < total; i++) {
                somatorio += cocoe.getTokens().get(i).length();
            }
        } else if (tipo.equalsIgnoreCase("frase")) {
            total = cocoe.getFrases().length;
            for (int i = 0; i < total; i++) {
                somatorio += cocoe.getTokensPorFrase().get(i).size();
            }
        }
        if (total > 1) {
            return arredondarDecimal((somatorio / total), 4);
        } else {
            return 0;
        }
    }

    private static int getTota(List<String> lista) {
        if (lista.isEmpty()) {
            return 0;
        } else {
            return lista.size();
        }
    }
//
//    public static double getSemelhancaSemanticaEntreParagrafosInicEMedios(String[] frases) {
//        int totalFrases = frases.length;
//        int totalPorPorcao = totalFrases / 3;
//        double retorno = 0.0;
//
//        if (totalFrases >= 3) {
//            String primeirasFrases = "", frasesMedias = "";
//
//            for (int i = 0; i < totalPorPorcao; i++) {
//                primeirasFrases += frases[i] + " ";
//            }
//
//            for (int i = totalPorPorcao; i < (totalPorPorcao * 2); i++) {
//                frasesMedias += frases[i] + " ";
//            }
//
////        int numCentral = (int) totalFrases / 2;
////        int limiteinferior, numDeFrases = 0;
////        int inicioDoCiclo = 0, acrescimoDecrescimo = 0;
////        if (numCentral >= 20) {
////            limiteinferior = 5;
////            numDeFrases = gerarNumeroAleatorio(limiteinferior, 4);
////            acrescimoDecrescimo = (numDeFrases / 2);
////            inicioDoCiclo = numCentral - acrescimoDecrescimo;
////
////        } else if (numCentral >= 10 && numCentral < 20) {
////            limiteinferior = 3;
////            numDeFrases = gerarNumeroAleatorio(limiteinferior, 3);
////            acrescimoDecrescimo = (numDeFrases / 2);
////            inicioDoCiclo = numCentral - acrescimoDecrescimo;
////
////        } else if (numCentral >= 5 && numCentral < 10) {
////            limiteinferior = 2;
////            numDeFrases = gerarNumeroAleatorio(limiteinferior, 1);
////            acrescimoDecrescimo = (numDeFrases / 2);
////            inicioDoCiclo = numCentral - acrescimoDecrescimo;
////        } else if (numCentral >= 2 && numCentral < 5) {
////            inicioDoCiclo = 0;
////            numDeFrases = 2;
////            acrescimoDecrescimo = 1;
////
////            inicioDoCiclo = numCentral - acrescimoDecrescimo;
////
////        } else {
////            inicioDoCiclo = 1;
////            numDeFrases = 1;
////            acrescimoDecrescimo = 0;
////
////            inicioDoCiclo = numCentral;
////
////        }
////        for (int i = 0; i < numDeFrases; i++) {
////            primeirasFrases += frases[i] + " ";
////        }
////        for (int i = inicioDoCiclo; i < numCentral + acrescimoDecrescimo; i++) {
////            frasesMedias += frases[i] + " ";
////        }
//            Text texto1 = new Text(primeirasFrases);
//            Text texto2 = new Text(frasesMedias);
//            retorno = arredondarDecimal(texto1.similarity(texto2), 4);
//        } else {
//            retorno = 0;
//        }
//        return retorno;
//    }

    public static double getSemelhancaSemanticaEntreParagrafosInicEFinais(CoesaoECoerencia cocoe) {
        totalFrase = cocoe.getFrases().length;
        total = totalFrase / 3;
        retorno = 0.0;
        if (totalFrase >= 3) {
            String primeirasFrases = "", frasesFinais = "";

            for (int i = 0; i < total; i++) {
                primeirasFrases += cocoe.getFrases()[i] + " ";
            }

            for (int i = (total * 2); i < totalFrase; i++) {
                frasesFinais += cocoe.getFrases()[i] + " ";
            }
            retorno = arredondarDecimal(new Text(primeirasFrases).similarity(new Text(frasesFinais)), 4);
        } else {
            retorno = 0;
        }
        return retorno;
    }
    //Índices de coesão local
    //Média da semelhança semática entre frases adjacente

    public static double getMediaDeSemelhancaSemanticaEntreFrases(CoesaoECoerencia cocoe) {
        retorno = 0.0;
        somatorio = 0;
        totalFrase = cocoe.getFrases().length;
        if (totalFrase > 1) {
            for (int i = 0; i < totalFrase; i++) {
                if ((i > 0) && i < (totalFrase - 2)) {
                    somatorio += new Text(cocoe.getFrases()[i]).similarity(new Text(cocoe.getFrases()[i + 1]));
                }
            }
            retorno = arredondarDecimal((somatorio / (totalFrase - 1)), 4);
        }
        return retorno;
    }

    //Sobreposicao de substantivos
    public static double getSobreposicaoDeSUbstantivos(CoesaoECoerencia cocoe, SnowballStemmer stemmer) throws Exception {
        retorno = 0.0;
        somatorio = 0;
        total = cocoe.getListaDeSubstantivosPorFrase().size();
        for (int i = 0; i < total; i++) {
            listaSubstantivos1.clear();
            lista2 = new LinkedList<>();
            vocabulario = new LinkedHashSet<>();
            if (i < (total - 2)) {

                listaSubstantivos1 = Ficheiros.getListaSteming(cocoe.getListaDeSubstantivosPorFrase().get(i), stemmer);
                lista2 = Ficheiros.getListaSteming(cocoe.getListaDeSubstantivosPorFrase().get(i + 1), stemmer);

                for (String s : listaSubstantivos1) {
                    vocabulario.add(s);
                }
                for (String s : lista2) {
                    vocabulario.add(s);
                }
                for (String s : vocabulario) {
                    if (listaSubstantivos1.contains(s) && lista2.contains(s)) {
                        somatorio++;
                    }
                }
            }

        }
        if (total > 2) {
            retorno = arredondarDecimal((somatorio / (double) (total - 1)), 4);
        } else {
            retorno = 0;
        }
        return retorno;
    }

    public static double getInsidenciaDePalavrasDeConteudo(List<String> palavrasConteudo) {
        double retorno = 0.0;
        double totalPalavrasConteudo = palavrasConteudo.size();
        retorno = arredondarDecimal((totalPalavrasConteudo / 1000), 4);
        return retorno;
    }

    public static double getDensidadeDePronomes(CoesaoECoerencia cocoe) {
        retorno = 0.0;
        totalPronomes = cocoe.getPronomes().size();
        retorno = arredondarDecimal((totalPronomes / cocoe.getTotalPalavras()), 4);
        return retorno;
    }

    public static double getMediaDePronomesPorFrase(CoesaoECoerencia cocoe) {
        totalPronomes = cocoe.getPronomes().size();
        totalFrase = cocoe.getFrases().length;
        retorno = arredondarDecimal((totalPronomes / totalFrase), 4);
        return retorno;
    }

    public static double getMediaDeSobreposicaoDePalavrasEmCadaParDeFrases(CoesaoECoerencia cocoe) {
        retorno = 0.0;
        somatorio = 0.0;
        totalFrase = cocoe.getFrases().length;

        for (int i = 0; i < totalFrase; i++) {
            if (i < totalFrase - 2) {
                for (String s : cocoe.getTokensPorFrase().get(i)) {
                    if (cocoe.getTokensPorFrase().get(i + 1).contains(s)) {
                        somatorio += 1;
                    }
                }
            }
        }
        if (totalFrase > 1) {
            retorno = arredondarDecimal((somatorio / (totalFrase - 1)), 4);
        } else {
            retorno = 0;
        }
        return retorno;
    }

    public static double getMediaDaSobreposicaoDeLemas(List<List<String>> listaDeLemasPorFrases) {
        double retorno = 0.0, somatorio = 0.0;
        double totalFrases = listaDeLemasPorFrases.size();
//        LinkedHashSet<String> vocabulariodeLemas = new LinkedHashSet<>();
//        for (List<String> lista : listaDeLemasPorFrases) {
//            for (String lema : lista) {
//                vocabulariodeLemas.add(lema);
//            }
//        }
//        double 
        for (int i = 0; i < totalFrases; i++) {
            if (i < totalFrases - 2) {
                for (String s : listaDeLemasPorFrases.get(i)) {
                    if (listaDeLemasPorFrases.get(i + 1).contains(s)) {
                        somatorio += 1;
                    }
                }
            }
        }
        retorno = arredondarDecimal((somatorio / totalFrases), 4);
        return retorno;
    }

    public static double getMTLDLemas(List<String> lemas) throws Exception {
        double mtld = 0;
        mtld = arredondarDecimal(getMTLD(lemas), 4);
        return mtld;
    }

    public static double getMTLDPalavrasDeConteudo(CoesaoECoerencia cocoe) throws Exception {
        double mtld = 0;
        mtld = arredondarDecimal(getMTLD(cocoe.getListaPalavrasdeConteudo()), 4);
        return mtld;
    }

    public static double getRazoEntrePronomesESubstativos(CoesaoECoerencia cocoe) {
        retorno = 0;
        retorno = arredondarDecimal(((double) cocoe.getPronomes().size() / (double) cocoe.getListaDeSubstantivos().size()), 4);
        return retorno;
    }
}
