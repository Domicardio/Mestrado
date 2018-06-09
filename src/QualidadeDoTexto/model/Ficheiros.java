package QualidadeDoTexto.model;

import static QualidadeDoTexto.model.Inicializacoes.tokenizerSiples;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import hultig.io.FileIN;
import hultig.sumo.Sentence;
import hultig.sumo.Text;
import hultig.sumo.Word;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class Ficheiros {

    private static Text txt;
    
    Metricas metricas = new Metricas();
    //private static HashMap<String, String> hashMapChaveValorPortugues = new HashMap<>();
    //private static HashMap<String, String> hashMapChaveValorIngles = new HashMap<>();
    private static int num = 0, totalFicheiros = 0;
    private static List<String> conteudo = new LinkedList<>();
    private static List<String> operadoresLogicos = new LinkedList<>();
    private static boolean controlo = true;
    private static String caminhoDoFicheiro = "", palavra = "", caminhoDoFicheiro2 = "", termoAnotado = "", ultimapalavra = "";
    private static double mtld = 0.0;
    private static double maturidade = 0.0;
    private static double taxarepeticao = 0.0;
    private static double densidade = 0.0;
    private static String pasta = "", lingua;
    private static int totalPalavras = 0;
    private static Corpora corpora = null;
    private static File file;
    private static LinkedHashSet<String> vocabEntidades = new LinkedHashSet<>();
    private static List<String> lista = new LinkedList<>(), lista2 = new LinkedList<>(), lista3 = new LinkedList<>();
    private static Fractais fractais;
    private static int indexLast, indexFirst;
    private static String tokens[] = null, types[] = null, tags[] = null, palavras[] = null;
    public static boolean gravarCabecalho;

    public static boolean criarFicheiro(String local, String nome) throws Exception {
        File ficheiro = new File(local + "/" + nome);
        if (!ficheiro.exists()) {
            ficheiro.createNewFile();
            return true;
        }
        return false;
    }

    public static boolean gerarTextos() {
        try {
            for (int i = 0; i <= 3; i++) {
                if (i == 0) {
                    gravarDados(new File("..").getAbsolutePath() + "/Dissertacao/src/QualidadeDoTexto/Corpora/Bons", "Bons", "(pt)");
                } else if (i == 1) {
                    gravarDados(new File("..").getAbsolutePath() + "/Dissertacao/src/QualidadeDoTexto/Corpora/Bons", "Bons", "(en)");
                } else if (i == 2) {
                    gravarDados(new File("..").getAbsolutePath() + "/Dissertacao/src/QualidadeDoTexto/Corpora/Maus", "Maus", "(pt)");
                } else if (i == 3) {
                    gravarDados(new File("..").getAbsolutePath() + "/Dissertacao/src/QualidadeDoTexto/Corpora/Maus", "Maus", "(en)");
                }

            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static List<String> getStopWord(String ficheiro) throws Exception {
        List<String> stopWords = new LinkedList<>();
        //String texto = "";
        //String[] tokens = getConteudo(ficheiro).split("[ ,.;“”:()\"!?]+");
        // System.out.println(texto);
        for (Word s : new Text(ficheiro).getOnlyWords()) {
            stopWords.add(s.toString().toLowerCase());
        }
        return stopWords;
    }

    public static List<String> getRemocaoDeStopWords(List<String> palavras, List<String> stopWords) throws Exception {
        lista3 = new LinkedList<>();
        lista3 = palavras;
        controlo = true;
        //getStopWord(new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Apoio/stopwordsPt.txt")
        //String[] palavras = texto.split("[ “”,.;:()\"!?]+");
//        for (Word string : new Text(texto).getOnlyWords()) {
//            lista3.add(string.toLowerCase().trim());
//        }
        for (String s : stopWords) {
            while (controlo) {
                lista3.remove(s.toLowerCase());
                if (!lista3.contains(s.toLowerCase())) {
                    controlo = false;
                }
            }
            controlo = true;
        }

        return palavras;
    }

    public static List<String> getDicionario(String lingua) throws Exception {
        List<String> conteudo = new LinkedList<>();
        if (lingua.equalsIgnoreCase("en")) {
            caminhoDoFicheiro = (new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Apoio/Dic_words_inglesh.txt");
        } else {
            caminhoDoFicheiro = (new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Apoio/Dic_words_Portuguese.txt");
        }
        String[] dic = lerFicheiro(caminhoDoFicheiro);
        Arrays.sort(dic);
        //conteudo = Arrays.asList(dic);

        for (String string : dic) {
            palavra = string.toLowerCase();
            indexLast = palavra.indexOf(" ") - 1;

            if (palavra.contains("/")) {
                indexLast = palavra.indexOf("/");
                palavra = palavra.substring(0, indexLast);
            } else if (palavra.contains("[")) {
                indexLast = palavra.indexOf("[");
                palavra = palavra.substring(0, indexLast);
            } else {
                if (indexLast > 0) {
                    palavra = palavra.substring(0, indexLast);
                }
            }
            conteudo.add(palavra.trim());
        }
        return conteudo;

    }

    public boolean palavraExiste(List<String> dic, String palavra) {
        if (dic.contains(palavra)) {
            return true;
        } else {
            return false;
        }

    }

    public static void gravarDados(String pasta, String tipoFicheiro, String lingua) {
        File file = new File(pasta);
        String textoIngles = "", textoPortugues = "";
        boolean controlo = false;
        for (File ficheiro : file.listFiles()) {
            //FileIN fi = new FileIN(ficheiro.getAbsolutePath());
            String nome = ficheiro.getName();

            if (ficheiro.isFile()) {
                if (nome.contains("(pt)") && lingua.equalsIgnoreCase("(pt)")) {
                    for (String string : lerFicheiro(ficheiro.getAbsolutePath())) {
                        textoPortugues = textoPortugues + string.toLowerCase() + " \n";

                    }
                } else if (nome.contains("(en)") && lingua.equalsIgnoreCase("(en)")) {
                    for (String string : lerFicheiro(ficheiro.getAbsolutePath())) {
                        // textoPortugues = textoPortugues + string + " ";
                        textoIngles = textoIngles + string.toLowerCase() + " \n";

                    }
                }

                controlo = true;
            }
        }
        if (controlo) {
            if (lingua.equalsIgnoreCase("(pt)")) {
                escreverNoArquivo(textoPortugues, tipoFicheiro, "(pt)");
            } else if (lingua.equalsIgnoreCase("(en)")) {
                escreverNoArquivo(textoIngles, tipoFicheiro, "(en)");
            }
        }

        //System.out.println(s);
    }

    public static void escreverNoArquivo(String texto, String tipoFicheiro, String lingua) {
        try {
            totalPalavras = 0;
            File ficheiro = null;

            FileWriter filewriter;
            BufferedWriter escrever;
            //String[] palavras = texto.split("[ ,.;:()\"!?]+");
            String[] palavras = texto.split("[ \n]+");

            String gravar = "";
            for (int i = 0; i < palavras.length; i++) {
                palavra = palavras[i];
                if (new Word(palavra).isWord()) {
                    totalPalavras++;
                }
                if (i == palavras.length - 1) {
                    gravar = gravar.concat(palavras[i] + ". ");

                } else if (palavra.endsWith(".")) {
                    gravar = gravar.concat(palavras[i] + "\n ");

                } else {
                    gravar = gravar.concat(palavras[i] + " ");
                }
                if (totalPalavras == 200) {
                    if (lingua.equalsIgnoreCase("(pt)") && tipoFicheiro.equalsIgnoreCase("Bons")) {
                        ficheiro = new File(new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Corpora/Fractais/Bons/pt/Fractal" + num + "(pt).txt");
                    } else if (lingua.equalsIgnoreCase("(pt)") && tipoFicheiro.equalsIgnoreCase("Maus")) {
                        ficheiro = new File(new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Corpora/Fractais/Maus/pt/Fractal" + num + "(pt).txt");
                    }
                    if (lingua.equalsIgnoreCase("(en)") && tipoFicheiro.equalsIgnoreCase("Bons")) {
                        ficheiro = new File(new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Corpora/Fractais/Bons/en/Fractal" + num + "(en).txt");
                    } else if (lingua.equalsIgnoreCase("(en)") && tipoFicheiro.equalsIgnoreCase("Maus")) {
                        ficheiro = new File(new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Corpora/Fractais/Maus/en/Fractal" + num + "(en).txt");
                    }
                    if (!ficheiro.exists()) {
                        ficheiro.createNewFile();
                    }
                    filewriter = new FileWriter(ficheiro);
                    escrever = new BufferedWriter(filewriter);
                    if (!gravar.endsWith(".")) {
                        gravar = gravar.concat(".");

                    }
                    escrever.write(gravar + "\n");
                    gravar = "";
                    escrever.close();
                    filewriter.close();
                    num++;
                    totalPalavras = 0;
                }

            }

        } catch (Exception e) {

        }
    }

    static List<String> getOperadoresLogicos(String texto, String lingua) {
        operadoresLogicos = new LinkedList<>();
        for (String s : getListaDePalavras(texto)) {
            switch (lingua) {
                case "pt":
                    if (s.equalsIgnoreCase("ou") || s.equalsIgnoreCase("e") || s.equalsIgnoreCase("não") || s.equalsIgnoreCase("nao") || s.equalsIgnoreCase("se") || s.equalsIgnoreCase("enquanto")) {
                        operadoresLogicos.add(s);
                    }
                    break;
                case "en":
                    if (s.equalsIgnoreCase("or") || s.equalsIgnoreCase("and") || s.equalsIgnoreCase("not") || s.equalsIgnoreCase("if") || s.equalsIgnoreCase("while")) {
                        operadoresLogicos.add(s);
                    }
                    break;
            }
        }

        return operadoresLogicos;
    }

    public static List<String> getPalavrasDeConteudo(String[] tokens, String lingua) {
        lista = new LinkedList<>();
        conteudo = new LinkedList<>();
        switch (lingua) {
            case "en":
                tags = Inicializacoes.posTaggerIngles.tag(tokens);
                for (int i = 0; i < tags.length; i++) {
                    palavra = tags[i];
                    if ((palavra.equalsIgnoreCase("NN") || palavra.equalsIgnoreCase("NNS")
                            || palavra.equalsIgnoreCase("NNP") || palavra.equalsIgnoreCase("NNPS")
                            || palavra.equalsIgnoreCase("VB") || palavra.equalsIgnoreCase("VBD")
                            || palavra.equalsIgnoreCase("VBG") || palavra.equalsIgnoreCase("VBN")
                            || palavra.equalsIgnoreCase("VBP") || palavra.equalsIgnoreCase("VBZ")
                            || palavra.equalsIgnoreCase("JJ") || palavra.equalsIgnoreCase("JJR")
                            || palavra.equalsIgnoreCase("JJS") || palavra.equalsIgnoreCase("RB")
                            || palavra.equalsIgnoreCase("RBR") || palavra.equalsIgnoreCase("RBS"))) {
                        lista.add(tokens[i].toLowerCase());
                    }
                }
                conteudo = getListaSteming(lista, Inicializacoes.stemmerIngles);
                break;
            case "pt":
                tags = Inicializacoes.posTaggerPortugues.tag(tokens);
                for (int i = 0; i < tags.length; i++) {
                    palavra = tags[i];
                    if ((palavra.equalsIgnoreCase("n") || palavra.equalsIgnoreCase("N")
                            || palavra.equalsIgnoreCase("prop") || palavra.equalsIgnoreCase("v-fim ")
                            || palavra.equalsIgnoreCase("v-inf") || palavra.equalsIgnoreCase("v-pcp")
                            || palavra.equalsIgnoreCase("v-ger") || palavra.equalsIgnoreCase("adj")
                            || palavra.equalsIgnoreCase("adv"))) {
                        lista.add(tokens[i].toLowerCase());
                    }
                }
                conteudo = getListaSteming(lista, Inicializacoes.stemmerPortugues);
                break;
        }
        return conteudo;
    }

    public static List<String> getListaSteming(List<String> palavras, SnowballStemmer stemmer) {
        lista3 = new LinkedList<>();
        for (String s : palavras) {
            lista3.add(stemmer.stem(s.toLowerCase()).toString());
        }
        return lista3;
    }

    public static List<List<String>> getListaDeStemmingPorFrase(List<String> frases, SnowballStemmer stemmer) {
        List<List<String>> listaDeStemingPorFrase = new LinkedList<>();
        LinkedHashSet<String> listaDeStemming;
        for (String frase : frases) {
            listaDeStemming = new LinkedHashSet<>(getListaSteming(getListaDePalavras(frase.toLowerCase()), stemmer));
            listaDeStemingPorFrase.add(new LinkedList<>(listaDeStemming));
        }
        return listaDeStemingPorFrase;
    }

    public static String[] getSemPalavrasDesconhecidas(String[] palavras, String lingua) {
        lista2 = new LinkedList<>();
        for (String s : palavras) {
            if (lingua.equalsIgnoreCase("en")) {
                if (Inicializacoes.dicionarioIngles.contains(s)) {
                    lista2.add(s);
                }
            } else if (lingua.equalsIgnoreCase("pt")) {
                if (Inicializacoes.dicionarioPortugues.contains(s)) {
                    lista2.add(s);
                }
            }
        }
        return lista2.toArray(new String[lista2.size()]);
    }

    public static List<List<String>> getPronomesPorFrase(CoesaoECoerencia cocoe, String lingua) {
        lista2 = new LinkedList<>();
        List<List<String>> listaDePronomesPorFrase = new LinkedList<>();
        tags = null;
        for (List<String> listaDePronomesNaFrase : cocoe.getTokensPorFrase()) {
            lista = new LinkedList<>();

            switch (lingua) {
                case "en":
                    tokens = listaDePronomesNaFrase.toArray(new String[listaDePronomesNaFrase.size()]);
                    tags = Inicializacoes.posTaggerIngles.tag(tokens);
                    for (int i = 0; i < tags.length; i++) {
                        palavra = tags[i];
                        if (palavra.equalsIgnoreCase("PRP") || palavra.equalsIgnoreCase("PRP$")) {
                            palavra = tokens[i].toLowerCase();
                            lista.add(palavra);
                            lista2.add(palavra);
                        }
                    }
                    break;
                case "pt":
                    tokens = listaDePronomesNaFrase.toArray(new String[listaDePronomesNaFrase.size()]);
                    tags = Inicializacoes.posTaggerPortugues.tag(tokens);
                    for (int i = 0; i < tags.length; i++) {
                        palavra = tags[i];
                        if (palavra.equalsIgnoreCase("pron-det") || palavra.equalsIgnoreCase("pron-pers")
                                || palavra.equalsIgnoreCase("pron-indp")) {
                            palavra = tokens[i].toLowerCase();
                            lista.add(palavra);
                            lista2.add(palavra);
                        }
                    }
                    break;
            }
            listaDePronomesPorFrase.add(lista);
        }
        cocoe.setPronomes(lista2);
        return listaDePronomesPorFrase;
    }

//    public static List<List<String>> getListaDePronomesPorFrase(String[] frases, String lingua) {
//        List<List<String>> listaDePronomesPorFrase = new LinkedList<>();
//
//        for (String frase : frases) {
//            if (lingua.equalsIgnoreCase("en")) {
//                tokens = Inicializacoes.tokenizerIngles.tokenize(frase.toLowerCase());
//            } else if (lingua.equalsIgnoreCase("pt")) {
//                tokens = Inicializacoes.tokenizerPortugues.tokenize(frase.toLowerCase());
//            }
//            listaDePronomesPorFrase.add(getPronomes(tokens, lingua));
//        }
//        return listaDePronomesPorFrase;
//    }
    public static List<String> getConjuncoes(String[] frases, String lingua) {
        List<String> retorno = new LinkedList<>();
        for (String frase : frases) {
            String tokens[] = null;
            String tags[] = null;
            if (lingua.equalsIgnoreCase("en")) {
                tokens = Inicializacoes.tokenizerIngles.tokenize(frase.toLowerCase());
                tags = Inicializacoes.posTaggerIngles.tag(tokens);
            } else if (lingua.equalsIgnoreCase("pt")) {
                tokens = Inicializacoes.tokenizerPortugues.tokenize(frase.toLowerCase());
                tags = Inicializacoes.posTaggerPortugues.tag(tokens);
            }
            for (int i = 0; i < tokens.length; i++) {
                if (lingua.equalsIgnoreCase("en")) {
                    if (tags[i].equalsIgnoreCase("CC")) {
                        retorno.add(tokens[i].toLowerCase());
                    }

                } else if (lingua.equalsIgnoreCase("pt")) {
                    if (tags[i].equalsIgnoreCase("conj-c") || tags[i].equalsIgnoreCase("conj-s")
                            || tags[i].equalsIgnoreCase("conj")) {
                        retorno.add(tokens[i].toLowerCase());
                    }
                }
            }
        }
        return retorno;
    }

    public static String[] lerArquivoCsv(File f, String separador) throws Exception {
        String[] retorno = null;
        String linha = new String();
        BufferedReader leitor = null;
        leitor = new BufferedReader(new FileReader(f));
        while ((linha = leitor.readLine()) != null) {
            retorno = linha.split(separador);
        }
        return retorno;
    }

    public static boolean escreverNoArquivoCsv(String f, String linha, String tipo, String tipoDeTexto, boolean cabecalho) {
        List<String> conteudoDoFicheiro = null;
        try {
            File ficheiro = new File(f);
            if (!ficheiro.exists()) {
                ficheiro.createNewFile();
            } else {
                conteudoDoFicheiro = Arrays.asList(lerFicheiro(f));
            }
            FileWriter filewriter = new FileWriter(f, true);
            BufferedWriter escrever = new BufferedWriter(filewriter);
            if (cabecalho) {
                switch (tipo) {
                    case "dl":
                        if ((tipoDeTexto.equalsIgnoreCase("Maus") || tipoDeTexto.equalsIgnoreCase("MausPalavras")) && !conteudoDoFicheiro.contains("Vocabulário de textos sem qualidade")) {

                            escrever.write("Vocabulário de textos sem qualidade\n\n");
                            escrever.write("1- Ficheiro 2- MTLD 3- Taxa de Repetição 4- Maturidade 5- Densidade " + "\n\n");
                        } else if ((tipoDeTexto.equalsIgnoreCase("Bons") || tipoDeTexto.equalsIgnoreCase("BonsPalavras")) && !conteudoDoFicheiro.contains("Vocabulário de textos com qualidade")) {
                            escrever.write("Vocabulário de textos com qualidade\n\n");
                            escrever.write("1- Ficheiro 2- MTLD 3- Taxa de repetição  4- Maturidade 5- Densidade " + "\n\n");
                            //    escrever.write("1- Ficheiro 2- Frases 3- Tokens 4- Palavras 5- Types 6- MTLD 7- Repetição 8- Maturidade 9- Densidade " + "\n\n");
                        }
                        break;
                    case "cc":
                        if ((tipoDeTexto.equalsIgnoreCase("Maus") || tipoDeTexto.equalsIgnoreCase("MausPalavras")) && !conteudoDoFicheiro.contains("Coesão e coerência de textos sem qualidade")) {
                            escrever.write("Coesão e coerência de textos sem qualidade\n\n");
                            escrever.write("1- Ficheiro 2- Coerência local 3- Transição sujeito --> Sujeito  4- Transição sujeito --> objeto"
                                    + " 5- Transição Objeto --> objeto 6- Transição objeto --> sujeito \n");
                            escrever.write("7- Média de operadores lógicos entre frases 8- Densidade de pronomes 9- Sobreposição média de substantivos (lemas) entre Frase \n");
                            escrever.write("10- Proporção média de pronomes/por frases 11- Semelhança semântica entre frases (iniciais e finais) " + "\n");
                            escrever.write("12- Média da semelhança entre frases  13- Sobreposição de palavras entre frases adjacentes " + "\n");
                            escrever.write("14- Proporção de pronomes por substantivos  15- Diversidade de palavras de conteúdo " + "\n");
                            escrever.write("16- Compimento médio da palavra 17- Comprimento médio da frase " + "\n\n");

                        } else if ((tipoDeTexto.equalsIgnoreCase("Bons") || tipoDeTexto.equalsIgnoreCase("BonsPalavras")) && !conteudoDoFicheiro.contains("Coesão e coerência de textos com qualidade")) {
                            escrever.write("Coesão e coerência de textos com qualidade\n\n");
                            escrever.write("1- Ficheiro 2- Coerência local 3- Transição sujeito --> Sujeito  4- Transição sujeito --> objeto"
                                    + " 5- Transição Objeto --> objeto 6- Transição objeto --> sujeito \n");
                            escrever.write("7- Média de operadores lógicos entre frases 8- Densidade de pronomes 9- Sobreposição média de substantivos (lemas) entre Frase \n");
                            escrever.write("10- Proporção média de pronomes/por frases 11- Semelhança semântica entre frases (iniciais e finais) " + "\n");
                            escrever.write("12- Média da semelhança entre frases  13- Sobreposição de palavras entre frases adjacentes " + "\n");
                            escrever.write("14- Proporção de pronomes por substantivos  15- Diversidade de palavras de conteúdo " + "\n");
                            escrever.write("16- Compimento médio da palavra 17- Comprimento médio da frase " + "\n\n");
                        }
                        break;
                }
            }

            if (!conteudoDoFicheiro.contains(linha)) {
                escrever.write("--> " + linha + "\n");
                gravarCabecalho = false;
            }

            escrever.close();
            filewriter.close();

//        for (int i = 0; i < linhas.size(); i++) {
//            if (i == 0 && tipo.equalsIgnoreCase("dl")) {
//                if ((tipoDeTexto.equalsIgnoreCase("Maus") || tipoDeTexto.equalsIgnoreCase("MausPalavras")) && !conteudoDoFicheiro.contains("Vocabulário de textos sem qualidade")) {
//
//                    escrever.write("Vocabulário de textos sem qualidade\n\n");
//                    escrever.write("1- Ficheiro 2- MTLD 3- Taxa de Repetição 4- Maturidade 5- Densidade " + "\n\n");
//                } else if ((tipoDeTexto.equalsIgnoreCase("Bons") || tipoDeTexto.equalsIgnoreCase("BonsPalavras")) && !conteudoDoFicheiro.contains("Vocabulário de textos com qualidade")) {
//                    escrever.write("Vocabulário de textos com qualidade\n\n");
//                    escrever.write("1- Ficheiro 2- MTLD 3- Taxa de repetição  4- Maturidade 5- Densidade " + "\n\n");
//                    //    escrever.write("1- Ficheiro 2- Frases 3- Tokens 4- Palavras 5- Types 6- MTLD 7- Repetição 8- Maturidade 9- Densidade " + "\n\n");
//                }
//            } else if (i == 0 && tipo.equalsIgnoreCase("cc")) {
//                if ((tipoDeTexto.equalsIgnoreCase("Maus") || tipoDeTexto.equalsIgnoreCase("MausPalavras")) && !conteudoDoFicheiro.contains("Coesão e coerência de textos sem qualidade")) {
//                    escrever.write("Coesão e coerência de textos sem qualidade\n\n");
//                    escrever.write("1- Ficheiro 2- Coerência local 3- Transição sujeito --> Sujeito  4- Transição sujeito --> objeto"
//                            + " 5- Transição Objeto --> objeto 6- Transição objeto --> sujeito \n");
//                    escrever.write("7- Média de operadores lógicos entre frases 8- Densidade de pronomes 9- Sobreposição média de substantivos (lemas) entre Frase \n");
//                    escrever.write("10- Proporção média de pronomes/por frases 11- Semelhança semântica entre frases (iniciais e finais) " + "\n");
//                    escrever.write("12- Média da semelhança entre frases  13- Sobreposição de palavras entre frases adjacentes " + "\n");
//                    escrever.write("14- Proporção de pronomes por substantivos  15- Diversidade de palavras de conteúdo " + "\n");
//                    escrever.write("16- Compimento médio da palavra 17- Comprimento médio da frase " + "\n\n");
//
//                } else if ((tipoDeTexto.equalsIgnoreCase("Bons") || tipoDeTexto.equalsIgnoreCase("BonsPalavras")) && !conteudoDoFicheiro.contains("Coesão e coerência de textos com qualidade")) {
//                    escrever.write("Coesão e coerência de textos com qualidade\n\n");
//                    escrever.write("1- Ficheiro 2- Coerência local 3- Transição sujeito --> Sujeito  4- Transição sujeito --> objeto"
//                            + " 5- Transição Objeto --> objeto 6- Transição objeto --> sujeito \n");
//                    escrever.write("7- Média de operadores lógicos entre frases 8- Densidade de pronomes 9- Sobreposição média de substantivos (lemas) entre Frase \n");
//                    escrever.write("10- Proporção média de pronomes/por frases 11- Semelhança semântica entre frases (iniciais e finais) " + "\n");
//                    escrever.write("12- Média da semelhança entre frases  13- Sobreposição de palavras entre frases adjacentes " + "\n");
//                    escrever.write("14- Proporção de pronomes por substantivos  15- Diversidade de palavras de conteúdo " + "\n");
//                    escrever.write("16- Compimento médio da palavra 17- Comprimento médio da frase " + "\n\n");
//                }
//
//            }
//            String linha = "--> " + linhas.get(i);
//            if (!conteudoDoFicheiro.contains(linha)) {
//                escrever.write(linha + "\n");
//            }
//            if (i == linhas.size() - 1) {
//                escrever.write("\n\n");
//            }
//        }
//        escrever.close();
//        filewriter.close();
//        linhas.clear();
            return true;
        } catch (Exception e) {
            return false;

        }
    }

    public static void escreverNoArquivoFractais(String textoOriginal, String nomeDoFicheiro) {
        try {
            File ficheiro = ficheiro = new File(nomeDoFicheiro);
            if (!ficheiro.exists()) {
                ficheiro.createNewFile();
            }
            FileWriter filewriter = new FileWriter(ficheiro, true);
            BufferedWriter escrever = new BufferedWriter(filewriter);
            escrever.write(textoOriginal + " ");
            escrever.close();
            filewriter.close();

        } catch (Exception e) {

        }
    }

    public static String getConteudo(String url) throws Exception {
        String retorno = "";
        for (String frase : lerFicheiro(url)) {
            retorno = retorno.concat(frase.toLowerCase());
        }
        return retorno;
    }

    public static String[] lerFicheiro(String nome) {
        lista = new LinkedList<>();
        // String[] retorno = null;
        FileIN fin = new FileIN(nome);
        for (String s : fin.readAll()) {
            if (!s.isEmpty() || !s.equalsIgnoreCase("")) {
                lista.add(s);
            }
        }
        //retorno = fin.readAll();
        return lista.toArray(new String[lista.size()]);
    }

    public static List<String> getTokens(String conteudo, String lingua) throws Exception {
        // List<String> retorno = new LinkedList<>();
        // Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
        if (lingua.equalsIgnoreCase("en")) {
            tokens = getSemPalavrasDesconhecidas(Inicializacoes.tokenizerIngles.tokenize(conteudo), lingua);
        } else {
            tokens = getSemPalavrasDesconhecidas(Inicializacoes.tokenizerPortugues.tokenize(conteudo), lingua);
        }
        return Arrays.asList(tokens);
    }

    public static List<List<String>> getTokensPorFrase(CoesaoECoerencia cocoe, String lingua) throws Exception {
        List<List<String>> retorno = new LinkedList<>();
        lista = new LinkedList<>();

        for (String frase : cocoe.getFrases()) {
            lista3 = new LinkedList<>();
            switch (lingua) {
                case "en":
                    //tokens = getSemPalavrasDesconhecidas(Inicializacoes.tokenizerIngles.tokenize(frase), lingua);
                    tokens = Inicializacoes.tokenizerSiples.tokenize(frase);
                    break;
                case "pt":
                    //tokens = getSemPalavrasDesconhecidas(Inicializacoes.tokenizerPortugues.tokenize(frase), lingua);
                    tokens = Inicializacoes.tokenizerSiples.tokenize(frase);
                    break;
            }
            for (String s : tokens) {
                lista.add(s);
                lista3.add(s);
            }
            retorno.add(lista3);
        }
        cocoe.setTokens(lista);
        return retorno;
    }

    public static List<String> getWords(String[] frases) throws Exception {
        List<String> retorno = new LinkedList<>();
        for (String s : frases) {
            for (Word palavra : new Sentence(s.toLowerCase())) {
                if (palavra.isWord()) {
                    retorno.add(palavra.toString().toLowerCase());
                }
            }
        }

        return retorno;
    }

    public static String[] getFrases(String texto, String lingua) throws Exception {

        String sentences[] = null;
        if (lingua.equalsIgnoreCase("en")) {
            sentences = Inicializacoes.sdetectorIngles.sentDetect(texto);
        } else if (lingua.equalsIgnoreCase("pt")) {
            sentences = Inicializacoes.sdetectorPortugues.sentDetect(texto);
        }
        return sentences;
    }

    public static List<List<String>> getListaDeSubstativosPorFrase(CoesaoECoerencia cocoe,
            String lingua) throws Exception {
        List<String> substantivos, substantivosGerais = new LinkedList<>();
        List<List<String>> listaDeSubstantivosPorFrase = new LinkedList<>();
        //tokens = listaDeTokens.toArray(new String[listaDeTokens.size()]);
        tags = null;
        for (List<String> tokensNaFrase : cocoe.getTokensPorFrase()) {
            substantivos = new LinkedList<>();
            switch (lingua) {
                case "en":
                    //tokens = getSemPalavrasDesconhecidas(Inicializacoes.tokenizerIngles.tokenize(frase), lingua);
                    tags = Inicializacoes.posTaggerIngles.tag(tokensNaFrase.toArray(new String[tokensNaFrase.size()]));
                    for (int i = 0; i < tags.length; i++) {
                        palavra = tags[i];
                        if (palavra.equalsIgnoreCase("NN") || palavra.equalsIgnoreCase("NNS")
                                || palavra.equalsIgnoreCase("NNP") || palavra.equalsIgnoreCase("NNPS")) {
                            palavra = tokensNaFrase.get(i);
                            substantivosGerais.add(palavra);
                            substantivos.add(palavra.toLowerCase());
                        }
                    }
                    break;
                case "pt":
                    //tokens = getSemPalavrasDesconhecidas(Inicializacoes.tokenizerPortugues.tokenize(frase), lingua);
                    tags = Inicializacoes.posTaggerPortugues.tag(tokensNaFrase.toArray(new String[tokensNaFrase.size()]));
                    for (int i = 0; i < tags.length; i++) {
                        palavra = tags[i];
                        if (palavra.equalsIgnoreCase("n-adj") || palavra.equalsIgnoreCase("n")
                                || palavra.equalsIgnoreCase("prop")) {
                            palavra = tokensNaFrase.get(i);
                            substantivosGerais.add(palavra);
                            substantivos.add(palavra.toLowerCase());
                        }
                    }
                    break;
            }
            listaDeSubstantivosPorFrase.add(substantivos);
        }

//        for (int i = 0; i < tokens.length; i++) {
//            palavra = tags[i];
//            if (lingua.equalsIgnoreCase("en")) {
//                if (palavra.equalsIgnoreCase("NN") || palavra.equalsIgnoreCase("NNS")
//                        || palavra.equalsIgnoreCase("NNP") || palavra.equalsIgnoreCase("NNPS")) {
//                    palavra = tokens[i];
//                    substantivos.add(palavra.toLowerCase());
//                    listaDeSubstantivos.add(palavra);
//                }
//            } else if (lingua.equalsIgnoreCase("pt")) {
//                if (palavra.equalsIgnoreCase("n-adj") || palavra.equalsIgnoreCase("n")
//                        || palavra.equalsIgnoreCase("prop")) {
//                    palavra = tokens[i];
//                    substantivos.add(palavra.toLowerCase());
//                    listaDeSubstantivos.add(palavra);
//                }
//            }
//        }
        cocoe.setListaDeSubstantivos(substantivosGerais);
        return listaDeSubstantivosPorFrase;
    }

//    public static List<String> getListaGeralDeSubstativos(List<String> frases,
//            String lingua) throws Exception {
//        List<String> listaDeSubstantivos = new LinkedList<>();
//
//        for (String frase : frases) {
////            for (String substantivo : getListaDeSubstativos(frase.toLowerCase(), lingua)) {
////                listaDeSubstantivos.add(substantivo.toLowerCase());
////            }
//        }
//        return listaDeSubstantivos;
//    }
//    public static List<String> getVocabularioDeEntidades(List<List<String>> listaDeEntidades) {
//        vocabEntidades = new LinkedHashSet<>();
//        for (List<String> lista : listaDeEntidades) {
//            for (String s : lista) {
//                vocabEntidades.add(s);
//            }
//        }
//        return new LinkedList<String>(vocabEntidades);
//    }
//    public static List<List<String>> getListaEntidadesPorFrase(CoesaoECoerencia cocoe, String[] fraseMarcadas, List<List<String>> listaDeTokensPorFrase,
//            String lingua) throws Exception {
//        vocabEntidades = new LinkedHashSet<>();
//        List<List<String>> retorno = new LinkedList<List<String>>();
//        List<String> entidadesNaFrase = new LinkedList<>();
//        String termoAnotado;
////        
////        for (String s : fraseMarcadas) {
////            if (lingua.equalsIgnoreCase("pt")) {
////                indexLast = s.indexOf("[") - 1;
////                if (s.equals(".") || s.equalsIgnoreCase("?") || s.equalsIgnoreCase("!")) {
////                    retorno.add(entidadesNaFrase);
////                    entidadesNaFrase = new LinkedList<>();
////                } else if (indexLast >= 0) {
////                    if (s.contains("N M S") || s.contains("PROMP") || s.contains("N F P")
////                            || s.contains("N F S") || s.endsWith("N M P")) {
////                        termoAnotado = s.substring(0, indexLast);
////                        entidadesNaFrase.add(termoAnotado.toLowerCase());
////                        vocabEntidades.add(termoAnotado.toLowerCase());
////                    }
////                }
////
////            } else if (lingua.equalsIgnoreCase("en")) {
////                String tokens[] = getSemPalavrasDesconhecidas(Inicializacoes.tokenizerIngles.tokenize(s), lingua);
////                String tags[] = Inicializacoes.posTaggerIngles.tag(tokens);
////                for (int i = 0; i < tokens.length; i++) {
////                    if (tags[i].equalsIgnoreCase("NN") || tags[i].equalsIgnoreCase("NNS")
////                            || tags[i].equalsIgnoreCase("NNP") || tags[i].equalsIgnoreCase("NNPS")) {
////                        entidadesNaFrase.add(tokens[i].toLowerCase());
////                        vocabEntidades.add(tokens[i].toLowerCase());
////                    }
////                }
////                retorno.add(entidadesNaFrase);
////                entidadesNaFrase = new LinkedList<>();
////            }
////
////        }
////        return retorno;
//        switch (lingua) {
//            case "en":
//
//                for (int i = 0; i < listaDeTokensPorFrase.size(); i++) {
//                    entidadesNaFrase = new LinkedList<>();
//                    lista3 = listaDeTokensPorFrase.get(i);
//                    tags = Inicializacoes.posTaggerIngles.tag(lista3.toArray(
//                            new String[lista3.size()]));
//                    for (int j = 0; j < lista3.size(); j++) {
//                        termoAnotado = tags[j];
//                        if (termoAnotado.equalsIgnoreCase("NN") || termoAnotado.equalsIgnoreCase("NNS")
//                                || termoAnotado.equalsIgnoreCase("NNP") || termoAnotado.equalsIgnoreCase("NNPS")) {
//                            palavra = lista3.get(j);
//                            entidadesNaFrase.add(palavra.toLowerCase());
//                            vocabEntidades.add(palavra);
//                        }
//                    }
//                    retorno.add(entidadesNaFrase);
//                }
//                break;
//            case "pt":
//                for (String s : fraseMarcadas) {
//                    indexLast = s.indexOf("[") - 1;
//                    if (s.equals(".") || s.equalsIgnoreCase("?") || s.equalsIgnoreCase("!")) {
//                        retorno.add(entidadesNaFrase);
//                        entidadesNaFrase = new LinkedList<>();
//                    } else if (indexLast >= 0) {
//                        if (s.contains("N M S") || s.contains("PROMP") || s.contains("N F P")
//                                || s.contains("N F S") || s.endsWith("N M P")) {
//                            termoAnotado = s.substring(0, indexLast);
//                            entidadesNaFrase.add(termoAnotado.toLowerCase());
//                        }
//                    }
//                }
//                break;
//        }
//        cocoe.setVocabularioEntidades(new LinkedList<String>(vocabEntidades));
//        return retorno;
//    }
    public static List<List<String>> getListaPapeisEntidadesPorFrase(CoesaoECoerencia cocoe, String lingua) {
        List<List<String>> retorno = new LinkedList<List<String>>();
        LinkedHashSet<String> vocabEntidadesMarcadas = new LinkedHashSet<>();
        termoAnotado = "";
        switch (lingua) {
            case "en":
                for (String s : cocoe.getFrases()) {
                    // System.out.println(s);

                    termoAnotado = "";
                    Annotation ann = new Annotation(s);
                    Inicializacoes.pipeline.annotate(ann);

                    for (CoreMap sent : ann.get(CoreAnnotations.SentencesAnnotation.class
                    )) {
                        SemanticGraph sg = sent.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class
                        );
                        String[] ab = sg.toString(SemanticGraph.OutputFormat.LIST).split("\n");
                        for (String b : ab) {
                            if (b.contains("nsubj") || b.contains("dobj") || b.contains("iobj")) {
                                indexFirst = b.indexOf(",") + 1;
                                indexLast = b.lastIndexOf("-");
                                String palavra = b.substring(indexFirst, indexLast).toLowerCase();
                                if (b.contains("nsubj")) {
                                    termoAnotado = palavra.concat("---s").trim();
                                    if (vocabEntidadesMarcadas.contains(palavra.concat("---o").trim())) {
                                        vocabEntidadesMarcadas.remove(palavra.concat("---o").trim());
                                    }
                                    vocabEntidadesMarcadas.add(termoAnotado);
                                } else if (b.contains("dobj")) {
                                    termoAnotado = palavra.concat("---o").trim();
                                    if (!vocabEntidadesMarcadas.contains(palavra.concat("---s").trim())) {
                                        vocabEntidadesMarcadas.add(termoAnotado);
                                    }
                                }
                            }
                        }

                    }
                    List<String> ent = new LinkedList<>(vocabEntidadesMarcadas);
                    retorno.add(ent);
                    vocabEntidadesMarcadas = new LinkedHashSet<>();
                }
                break;
            case "pt":
                num = 0;
                for (String s : cocoe.getConteudoEmLinhas()) {
                    // System.out.println(s);
                    termoAnotado = "";
                    indexLast = s.indexOf("[") - 1;
                    if (s.trim().equals(".") || s.trim().equalsIgnoreCase("?") || s.trim().equalsIgnoreCase("!")) {
                        List<String> ent = new LinkedList<>(vocabEntidadesMarcadas);
                        retorno.add(ent);
                        vocabEntidadesMarcadas = new LinkedHashSet<>();
                    } else {
                        if (!s.trim().equals(",") && !s.trim().equalsIgnoreCase(";") && !s.trim().equalsIgnoreCase(")")
                                && !s.trim().equalsIgnoreCase("(") && !s.trim().equalsIgnoreCase(":") && !s.trim().equalsIgnoreCase("-")
                                && !s.trim().equalsIgnoreCase("--") && !s.trim().equalsIgnoreCase("¶") && !s.trim().equalsIgnoreCase("/")
                                && !s.trim().equalsIgnoreCase(".") && !s.trim().equalsIgnoreCase("?") && !s.trim().equalsIgnoreCase("\\")
                                && !s.trim().equalsIgnoreCase("!") && !s.trim().equalsIgnoreCase("\"") && !s.trim().equalsIgnoreCase("<")
                                && !s.trim().equalsIgnoreCase(">") && !s.trim().equalsIgnoreCase("«") && !s.trim().equalsIgnoreCase("»")
                                && !s.trim().equalsIgnoreCase("$") && !s.trim().equalsIgnoreCase("#") && !s.trim().equalsIgnoreCase("[")
                                && !s.trim().equalsIgnoreCase("%") && !s.trim().equalsIgnoreCase("@") && !s.trim().equalsIgnoreCase("*")
                                && !s.trim().equalsIgnoreCase("]") && !s.trim().equalsIgnoreCase("{") && !s.trim().equalsIgnoreCase("}")
                                && !s.trim().equalsIgnoreCase("]") && !s.trim().equalsIgnoreCase("'") && !s.trim().equalsIgnoreCase("º")
                                && !s.trim().equalsIgnoreCase("ª") && !s.trim().equalsIgnoreCase("+") && !s.trim().equalsIgnoreCase("§")
                                && !s.trim().equalsIgnoreCase("|") && !s.equalsIgnoreCase("") && !s.isEmpty() && !s.equalsIgnoreCase(" ")
                                && !s.equalsIgnoreCase(null)) {
                            if (indexLast >= 0) {
                                palavra = s.substring(0, indexLast).toLowerCase();
                            } else {
                                palavra = "";
                            }
                            if (s.endsWith("@SUBJ>") || s.endsWith("@<SUBJ") || s.endsWith("@SUBJ>>")) {
                                termoAnotado = palavra.concat("---s").trim();
                                if (vocabEntidadesMarcadas.contains(palavra.concat("---o").trim())) {
                                    vocabEntidadesMarcadas.remove(palavra.concat("---o").trim());
                                }
                                vocabEntidadesMarcadas.add(termoAnotado);
                            } else if (s.endsWith("@ACC>") || s.endsWith("@<ACC") || s.endsWith("@ACC>>")
                                    || s.endsWith("@DAT>") || s.endsWith("@<DAT") || s.endsWith("@PIV>")
                                    || s.endsWith("@<PIV")) {
                                termoAnotado = palavra.concat("---o").trim();
                                if (!vocabEntidadesMarcadas.contains(palavra.concat("---s").trim())) {
                                    vocabEntidadesMarcadas.add(termoAnotado);
                                }
                            }
                        }
                    }
                }
                if (retorno.size() < cocoe.getListaEntidadesPorFrase().size()) {
                    for (int i = retorno.size(); i < cocoe.getListaEntidadesPorFrase().size(); i++) {
                        retorno.add(new LinkedList<>(vocabEntidadesMarcadas));
                    }

                }
                break;
        }

//        for (String s : fraseMarcadas) {
//            // System.out.println(s);
//
//            termoAnotado = "";
//            if (lingua.equalsIgnoreCase("pt")) {
//                indexLast = s.indexOf("[") - 1;
//                String palavra;
//                if (s.trim().equals(".") || s.trim().equalsIgnoreCase("?") || s.trim().equalsIgnoreCase("!")) {
//                    List<String> ent = new LinkedList<>(vocabEntidadesMarcadas);
//                    retorno.add(ent);
//                    vocabEntidadesMarcadas = new LinkedHashSet<>();
//                } else {
//                    if (!s.trim().equals(",") && !s.trim().equalsIgnoreCase(";") && !s.trim().equalsIgnoreCase(")")
//                            && !s.trim().equalsIgnoreCase("(") && !s.trim().equalsIgnoreCase(":") && !s.trim().equalsIgnoreCase("-")
//                            && !s.trim().equalsIgnoreCase("--") && !s.trim().equalsIgnoreCase("¶") && !s.trim().equalsIgnoreCase("/")
//                            && !s.trim().equalsIgnoreCase(".") && !s.trim().equalsIgnoreCase("?") && !s.trim().equalsIgnoreCase("\\")
//                            && !s.trim().equalsIgnoreCase("!") && !s.trim().equalsIgnoreCase("\"") && !s.trim().equalsIgnoreCase("<")
//                            && !s.trim().equalsIgnoreCase(">") && !s.trim().equalsIgnoreCase("«") && !s.trim().equalsIgnoreCase("»")
//                            && !s.trim().equalsIgnoreCase("$") && !s.trim().equalsIgnoreCase("#") && !s.trim().equalsIgnoreCase("[")
//                            && !s.trim().equalsIgnoreCase("%") && !s.trim().equalsIgnoreCase("@") && !s.trim().equalsIgnoreCase("*")
//                            && !s.trim().equalsIgnoreCase("]") && !s.trim().equalsIgnoreCase("{") && !s.trim().equalsIgnoreCase("}")
//                            && !s.trim().equalsIgnoreCase("]") && !s.trim().equalsIgnoreCase("'") && !s.trim().equalsIgnoreCase("º")
//                            && !s.trim().equalsIgnoreCase("ª") && !s.trim().equalsIgnoreCase("+") && !s.trim().equalsIgnoreCase("§")
//                            && !s.trim().equalsIgnoreCase("|") && !s.equalsIgnoreCase("") && !s.isEmpty() && !s.equalsIgnoreCase(" ")
//                            && !s.equalsIgnoreCase(null)) {
//                        if (indexLast >= 0) {
//                            palavra = s.substring(0, indexLast).toLowerCase();
//
//                        } else {
//                            palavra = "";
//                        }
//                        if (s.endsWith("@SUBJ>") || s.endsWith("@<SUBJ") || s.endsWith("@SUBJ>>")) {
//                            termoAnotado = palavra.concat("---s").trim();
//                            if (vocabEntidadesMarcadas.contains(palavra.concat("---o").trim())) {
//                                vocabEntidadesMarcadas.remove(palavra.concat("---o").trim());
//                            }
//                            vocabEntidadesMarcadas.add(termoAnotado);
//                        } else if (s.endsWith("@ACC>") || s.endsWith("@<ACC") || s.endsWith("@ACC>>")
//                                || s.endsWith("@DAT>") || s.endsWith("@<DAT") || s.endsWith("@PIV>")
//                                || s.endsWith("@<PIV")) {
//                            termoAnotado = palavra.concat("---o").trim();
//                            if (!vocabEntidadesMarcadas.contains(palavra.concat("---s").trim())) {
//                                vocabEntidadesMarcadas.add(termoAnotado);
//                            }
//                        }
//                    }
//                }
//            } else if (lingua.equalsIgnoreCase("en")) {
//                Annotation ann = new Annotation(s);
//                Inicializacoes.pipeline.annotate(ann);
//
//                for (CoreMap sent : ann.get(CoreAnnotations.SentencesAnnotation.class
//                )) {
//                    SemanticGraph sg = sent.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class
//                    );
//                    String[] ab = sg.toString(SemanticGraph.OutputFormat.LIST).split("\n");
//                    for (String b : ab) {
//                        if (b.contains("nsubj") || b.contains("dobj") || b.contains("iobj")) {
//                            int p1 = b.indexOf(",") + 1;
//                            int p2 = b.lastIndexOf("-");
//                            String palavra = b.substring(p1, p2).toLowerCase();
//                            if (b.contains("nsubj")) {
//                                termoAnotado = palavra.concat("---s").trim();
//                                if (vocabEntidadesMarcadas.contains(palavra.concat("---o").trim())) {
//                                    vocabEntidadesMarcadas.remove(palavra.concat("---o").trim());
//                                }
//                                vocabEntidadesMarcadas.add(termoAnotado);
//                            } else if (b.contains("dobj")) {
//                                termoAnotado = palavra.concat("---o").trim();
//                                if (!vocabEntidadesMarcadas.contains(palavra.concat("---s").trim())) {
//                                    vocabEntidadesMarcadas.add(termoAnotado);
//                                }
//                            }
//                        }
//                    }
//                }
//                List<String> ent = new LinkedList<>(vocabEntidadesMarcadas);
//                retorno.add(ent);
//                vocabEntidadesMarcadas = new LinkedHashSet<>();
//            }
//        }
        return retorno;
    }

    public static String linguaDoTexto(String nome) {
        String retorno = "";
        if (nome.contains("(en)")) {
            retorno = "en";
        } else if (nome.contains("pt")) {
            retorno = "pt";
        }
        return retorno;
    }

    public static LinkedHashSet<String> getVocabulario(List<String> tokens) {
        LinkedHashSet<String> vocab = new LinkedHashSet<>();
        vocab.clear();
        for (String s : tokens) {
            if (new Word(s).isWord()) //vocab.add(s);
            {
                vocab.add(s.toLowerCase());

            }
        }
        return vocab;
    }

    public static HashMap<String, String> getChaveValor(String ficheiro) {
        HashMap<String, String> chaveValor = new HashMap<>();
        BufferedReader br;
        String linha;
        int i = 0;
        try {
            br = new BufferedReader(new FileReader(ficheiro));
            while ((linha = br.readLine()) != null) {
                chaveValor.put(linha.toLowerCase(), String.valueOf(i).toLowerCase());
                i++;
            }
            // System.out.println(chaveValor);
            br.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return chaveValor;
    }

    public static List<String> getListaDePalavras(String frase) {
        lista2 = new LinkedList<>();
        for (Word s : new Text(frase).getOnlyWords()) {
            lista2.add(s.toString().toLowerCase());
        }
        return lista2;
    }

//    public static List<List<String>> getListaDePalavrasPorFrase(String[] frases, String lingua) {
//        List<List<String>> ListaDePalavrasPorFrase = new LinkedList<>();
//        lista = new LinkedList<>();
//        for (String s : frases) {
//            lista = getListaDePalavras(s.toLowerCase());
//            if (lingua.equalsIgnoreCase("en")) {
//                ListaDePalavrasPorFrase.add(getListaSteming(Arrays.asList(getSemPalavrasDesconhecidas(lista.toArray(new String[lista.size()]), lingua)), Inicializacoes.stemmerIngles));
//            } else if (lingua.equalsIgnoreCase("pt")) {
//                ListaDePalavrasPorFrase.add(getListaSteming(Arrays.asList(getSemPalavrasDesconhecidas(lista.toArray(new String[lista.size()]), lingua)), Inicializacoes.stemmerPortugues));
//            }
//        }
//        return ListaDePalavrasPorFrase;
//    }
    public Corpora getCorpora(String tipo) throws Exception {
        corpora = new Corpora();
        corpora.setTotalFicheiros(0);
        corpora.setTotalFrases(0);
        corpora.setTotalTokens(0);
        corpora.setTotalPalavras(0);
        corpora.setTotalTypes(0);

        if (tipo.equalsIgnoreCase("Bons")) {
            file = new File(new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Corpora/Bons");
        } else {
            file = new File(new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Corpora/Maus");
        }
        if (file.isDirectory()) {

            for (File f : file.listFiles()) {
                corpora.setConteudo(getConteudo(f.getAbsolutePath()));
                this.txt = new Text(corpora.getConteudo());
                corpora.setTotalFicheiros(corpora.getTotalFicheiros() + 1);
                if (linguaDoTexto(f.getName()).equalsIgnoreCase("pt")) {
                    corpora.setTotalFrases(corpora.getTotalFrases() + getFrases(corpora.getConteudo(), "pt").length);
                } else if (linguaDoTexto(f.getName()).equalsIgnoreCase("en")) {
                    corpora.setTotalFrases(corpora.getTotalFrases() + getFrases(corpora.getConteudo(), "en").length);
                }
                corpora.setTotalTokens(corpora.getTotalTokens() + getTokens(corpora.getConteudo(), linguaDoTexto(f.getName())).size());
                corpora.setTotalPalavras(corpora.getTotalPalavras() + txt.getNumWords());
                corpora.setTotalTypes(corpora.getTotalTypes() + txt.getVocab().length);

            }
        }
        return corpora;
    }

    public List<Vocabulario> diversidadeLexical(String tipo) throws Exception {
        gravarCabecalho = true;
        List<Vocabulario> retorno = new LinkedList<>();
        String s = "";

        if (tipo.equalsIgnoreCase("Bons")) {
            file = new File(new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Corpora/Bons");
        } else {
            file = new File(new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Corpora/Maus");
        }
        // hashMapChaveValorIngles = getChaveValor(new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Apoio/NGSL.txt ");
        //hashMapChaveValorPortugues = getChaveValor(new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Apoio/common_words.txt");

        if (file.isDirectory()) {
            s = "";

            for (File f : file.listFiles()) {
                densidade = 0;
                maturidade = 0;
                mtld = 0;
                taxarepeticao = 0;
                s = "";
                // System.out.println(f.getName());
                lingua = linguaDoTexto(f.getName());
                Vocabulario vc = new Vocabulario();
                vc.setNomeDosFicheiros(f.getName());
                vc.setconteudo(getConteudo(f.getAbsolutePath()));
                this.txt = new Text(vc.getConteudo());
                vc.setPalavras(getListaDePalavras(vc.getConteudo()));
                vc.setNumeroPalavras(txt.getNumWords());
                vc.setTokens(getTokens(vc.getconteudo(), lingua));
                vc.setVocabulario(txt.getVocab());
                vc.setListaPalavrasdeConteudo(getPalavrasDeConteudo(vc.getTokens().toArray(new String[vc.getTokens().size()]), lingua));
                switch (lingua) {
                    case "en":
                        mtld = Metricas.getMTLD(getListaSteming(vc.getPalavras(), Inicializacoes.stemmerIngles));
                        taxarepeticao = Metricas.getTaxaDeRepeticao(vc, lingua);
                        maturidade = Metricas.getMaturidade(vc, Inicializacoes.hashMapChaveValorIngles, Inicializacoes.dicionarioIngles);
                        densidade = Metricas.getDensidade(vc.getListaPalavrasdeConteudo(), vc.getNumeroPalavras());
                        break;
                    case "pt":
                        mtld = Metricas.getMTLD(getListaSteming(vc.getPalavras(), Inicializacoes.stemmerPortugues));
                        taxarepeticao = Metricas.getTaxaDeRepeticao(vc, lingua);
                        maturidade = Metricas.getMaturidade(vc, Inicializacoes.hashMapChaveValorPortugues, Inicializacoes.dicionarioPortugues);
                        densidade = Metricas.getDensidade(vc.getListaPalavrasdeConteudo(), vc.getNumeroPalavras());
                        break;
                }
                vc.setMtld(mtld);
                vc.setTaxaDeRepeticoes(taxarepeticao);
                vc.setMaturidade(maturidade);
                vc.setDensidadeLexical(densidade);
                s = f.getName() + ";" + String.valueOf(vc.getMtld()).replace(".", ",") + ";" + String.valueOf(vc.getTaxaDeRepeticoes()).replace(".", ",")
                        + ";" + String.valueOf(vc.getMaturidade()).replace(".", ",") + ";" + String.valueOf(vc.getDensidadeLexical()).replace(".", ",");
                escreverNoArquivoCsv(
                        new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/saida/qualidade_do_texto.csv", s, "dl", tipo, gravarCabecalho);

                retorno.add(vc);
            }
        }
        return retorno;
    }

    public List<CoesaoECoerencia> getCoesaoECoerencia(String tipo) throws Exception {
        List<CoesaoECoerencia> retorno = new LinkedList<>();
        // List<String> ficheiroCsv = new LinkedList<>();
        file = null;
        if (tipo.equalsIgnoreCase("Bons")) {
            file = new File(Inicializacoes.caminhoFicheirosBons);
        } else if (tipo.equalsIgnoreCase("Maus")) {
            file = new File(Inicializacoes.caminhoFicheirosMaus);
        }
        if (tipo.equalsIgnoreCase("BonsPalavras")) {
            file = new File(Inicializacoes.caminhoFicheirosBonsPalavras);
        }
        if (tipo.equalsIgnoreCase("MausPalavras")) {
            file = new File(Inicializacoes.caminhoFicheirosMausPalavras);
        }
        String nomeFicheiro = "";
        if (file.isDirectory()) {
            for (File ficheiro : file.listFiles()) {
                CoesaoECoerencia cocoe = new CoesaoECoerencia();
                nomeFicheiro = ficheiro.getName();
                cocoe.setNomeDosFicheiros(nomeFicheiro);
                lingua = linguaDoTexto(nomeFicheiro);
                System.out.println(nomeFicheiro);
//                if (nomeFicheiro.equalsIgnoreCase("As drogas e suas consequências(pt).txt")) {
                //System.out.println(nomeFicheiro);
//                }

                caminhoDoFicheiro = ficheiro.getAbsolutePath();
                if (tipo.equalsIgnoreCase("MausPalavras")) {
                    caminhoDoFicheiro2 = new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Corpora/Maus/" + nomeFicheiro;
                } else if (tipo.equalsIgnoreCase("BonsPalavras")) {
                    caminhoDoFicheiro2 = new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Corpora/Bons/" + nomeFicheiro;
                }
                if (lingua.equalsIgnoreCase("(pt)")) {

                    if (tipo.equalsIgnoreCase("MausPalavras") || tipo.equalsIgnoreCase("BonsPalavras")) {
                        cocoe.setConteudoEmLinhas(lerFicheiro(caminhoDoFicheiro));
                    } else {
                        cocoe.setConteudoEmLinhas(lerFicheiro(caminhoDoFicheiro2));
                    }

                    long dt, t0 = System.currentTimeMillis();
                    cocoe.setConteudo(getConteudo(caminhoDoFicheiro2));
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P1   dt: %f s\n", 1.0 * dt / 1000.0);

                    Ficheiros.txt = new Text(cocoe.getConteudo());
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P1   dt: %f s\n", 1.0 * dt / 1000.0);
                    cocoe.setTotalPalavras(Ficheiros.txt.getNumWords());
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P1   dt: %f s\n", 1.0 * dt / 1000.0);
                    cocoe.setFrases(getFrases(cocoe.getConteudo(), lingua));
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P2   dt: %f s\n", 1.0 * dt / 1000.0);
                    cocoe.setTokensPorFrase(getTokensPorFrase(cocoe, lingua));
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P3   dt: %f s\n", 1.0 * dt / 1000.0);
                    cocoe.setListaDePronomesPorFrase(getPronomesPorFrase(cocoe, lingua));
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P4   dt: %f s\n", 1.0 * dt / 1000.0);
                    cocoe.setListaPalavrasdeConteudo(getPalavrasDeConteudo(cocoe.getTokens().toArray(new String[cocoe.getTokens().size()]), lingua));
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P5   dt: %f s\n", 1.0 * dt / 1000.0);
                    cocoe.setListaDeSubstantivosPorFrase(getListaDeSubstativosPorFrase(cocoe, lingua));
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P6   dt: %f s\n", 1.0 * dt / 1000.0);
                    cocoe.setListaEntidadesPorFrase(cocoe.getListaDeSubstantivosPorFrase());
//                    cocoe.setVocabularioEntidades(getVocabularioDeEntidades(cocoe.getListaEntidadesPorFrase()));
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P7   dt: %f s\n", 1.0 * dt / 1000.0);
                    cocoe.setListaPapeisEntidadesPorFrase(getListaPapeisEntidadesPorFrase(cocoe, lingua));
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P8   dt: %f s\n", 1.0 * dt / 1000.0);

                    cocoe.setCoerenciaPAcc(Metricas.getCoerenciaDoTexto(cocoe));
                    cocoe.setTrasicaoSS(Metricas.getSujeitoObjecto(cocoe, "sujeitoSujeito"));
                    cocoe.setTrasicaoSO(Metricas.getSujeitoObjecto(cocoe, "sujeitoObjeto"));
                    cocoe.setTrasicaoOO(Metricas.getSujeitoObjecto(cocoe, "objetoSujeito"));
                    cocoe.setTrasicaoOS(Metricas.getSujeitoObjecto(cocoe, "objetoSujeito"));
                    cocoe.setMediaDeOperadoresLogicosPorFrases(Metricas.getMediaDeOperadoresLogicosPorFrase(Arrays.asList(cocoe.getFrases()), lingua));
                    cocoe.setDensidadeDePronomes(Metricas.getDensidadeDePronomes(cocoe));
                    cocoe.setMediaDaSobreposicaoDeSubstantivos(Metricas.getSobreposicaoDeSUbstantivos(cocoe, Inicializacoes.stemmerPortugues));
                    cocoe.setMediaDePronomesPorFrase(Metricas.getMediaDePronomesPorFrase(cocoe));
                    cocoe.setSimilaridadeInicioFim(Metricas.getSemelhancaSemanticaEntreParagrafosInicEFinais(cocoe));
                    cocoe.setMediaDaSemelhancaSemanticaEntreFrases(Metricas.getMediaDeSemelhancaSemanticaEntreFrases(cocoe));

                    cocoe.setSobreposicaoDePalavrasEmFrasesAdjcentes(Metricas.getMediaDeSobreposicaoDePalavrasEmCadaParDeFrases(cocoe));
                    cocoe.setRazaoDePronomesPorSubsitantivos(Metricas.getRazoEntrePronomesESubstativos(cocoe));
                    cocoe.setMtldPalavrasDeConteudos(Metricas.getMTLDPalavrasDeConteudo(cocoe));
                    cocoe.setComprimentoMedioDaPalavra(Metricas.getComprimentoMedio(cocoe, "palavra"));
                    cocoe.setComprimentoMedioDaFrase(Metricas.getComprimentoMedio(cocoe, "frase"));

                } else if (lingua.equalsIgnoreCase("en")) {
                    long dt, t0 = System.currentTimeMillis();

                    cocoe.setConteudoEmLinhas(lerFicheiro(caminhoDoFicheiro));
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P1   dt: %f s\n", 1.0 * dt / 1000.0);
                    cocoe.setConteudo(getConteudo(caminhoDoFicheiro));
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P2   dt: %f s\n", 1.0 * dt / 1000.0);
                    Ficheiros.txt = new Text(cocoe.getConteudo());
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P3   dt: %f s\n", 1.0 * dt / 1000.0);
                    cocoe.setTotalPalavras(Ficheiros.txt.getNumWords());
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P4   dt: %f s\n", 1.0 * dt / 1000.0);
                    cocoe.setFrases(getFrases(cocoe.getConteudo(), lingua));
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P5   dt: %f s\n", 1.0 * dt / 1000.0);
                    cocoe.setTokensPorFrase(getTokensPorFrase(cocoe, lingua));
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P6   dt: %f s\n", 1.0 * dt / 1000.0);
                    cocoe.setListaDePronomesPorFrase(getPronomesPorFrase(cocoe, lingua));
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P7   dt: %f s\n", 1.0 * dt / 1000.0);
                    cocoe.setListaPalavrasdeConteudo(getPalavrasDeConteudo(cocoe.getTokens().toArray(new String[cocoe.getTokens().size()]), lingua));
//                    cocoe.setListaEntidadesPorFrase(getListaEntidadesPorFrase(cocoe, null, cocoe.getTokensPorFrase(), lingua));
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P8   dt: %f s\n", 1.0 * dt / 1000.0);
                    cocoe.setListaDeSubstantivosPorFrase(getListaDeSubstativosPorFrase(cocoe, lingua));
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P9   dt: %f s\n", 1.0 * dt / 1000.0);

                    cocoe.setListaEntidadesPorFrase(cocoe.getListaDeSubstantivosPorFrase());
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P5   dt: %f s\n", 1.0 * dt / 1000.0);
//                    cocoe.setVocabularioEntidades(getVocabu1larioDeEntidades(cocoe.getListaEntidadesPorFrase()));

                    cocoe.setListaPapeisEntidadesPorFrase(getListaPapeisEntidadesPorFrase(cocoe, lingua));
                    dt = System.currentTimeMillis() - t0;
                    System.out.printf("P5   dt: %f s\n", 1.0 * dt / 1000.0);

                    cocoe.setCoerenciaPAcc(Metricas.getCoerenciaDoTexto(cocoe));

                    cocoe.setTrasicaoSS(Metricas.getSujeitoObjecto(cocoe, "sujeitoSujeito"));
                    cocoe.setTrasicaoSO(Metricas.getSujeitoObjecto(cocoe, "sujeitoObjeto"));
                    cocoe.setTrasicaoOO(Metricas.getSujeitoObjecto(cocoe, "objetoSujeito"));
                    cocoe.setTrasicaoOS(Metricas.getSujeitoObjecto(cocoe, "objetoSujeito"));
                    cocoe.setMediaDeOperadoresLogicosPorFrases(Metricas.getMediaDeOperadoresLogicosPorFrase(Arrays.asList(cocoe.getFrases()), lingua));
                    cocoe.setDensidadeDePronomes(Metricas.getDensidadeDePronomes(cocoe));
                    cocoe.setMediaDaSobreposicaoDeSubstantivos(Metricas.getSobreposicaoDeSUbstantivos(cocoe, Inicializacoes.stemmerIngles));
                    cocoe.setMediaDePronomesPorFrase(Metricas.getMediaDePronomesPorFrase(cocoe));
                    cocoe.setSimilaridadeInicioFim(Metricas.getSemelhancaSemanticaEntreParagrafosInicEFinais(cocoe));
                    cocoe.setMediaDaSemelhancaSemanticaEntreFrases(Metricas.getMediaDeSemelhancaSemanticaEntreFrases(cocoe));
                    cocoe.setSobreposicaoDePalavrasEmFrasesAdjcentes(Metricas.getMediaDeSobreposicaoDePalavrasEmCadaParDeFrases(cocoe));
                    cocoe.setRazaoDePronomesPorSubsitantivos(Metricas.getRazoEntrePronomesESubstativos(cocoe));
                    cocoe.setMtldPalavrasDeConteudos(Metricas.getMTLDPalavrasDeConteudo(cocoe));
                    cocoe.setComprimentoMedioDaPalavra(Metricas.getComprimentoMedio(cocoe, "palavra"));
                    cocoe.setComprimentoMedioDaFrase(Metricas.getComprimentoMedio(cocoe, "frase"));
                }
                String s = cocoe.getNomeDosFicheiros() + ";" + String.valueOf(cocoe.getCoerenciaPAcc()).replace(".", ",") + ";" + String.valueOf(cocoe.getTrasicaoSS()).replace(".", ",") + ";" + String.valueOf(cocoe.getTrasicaoSO()).replace(".", ",")
                        + ";" + String.valueOf(cocoe.getTrasicaoOO()).replace(".", ",") + ";" + String.valueOf(cocoe.getTrasicaoOS()).replace(".", ",") + ";" + String.valueOf(cocoe.getMediaDeOperadoresLogicosPorFrases()).replace(".", ",")
                        + ";" + String.valueOf(cocoe.getDensidadeDePronomes()) + ";" + String.valueOf(cocoe.getMediaDaSobreposicaoDeSubstantivos()).replace(".", ",")
                        + ";" + String.valueOf(cocoe.getMediaDePronomesPorFrase()).replace(".", ",") + ";" + String.valueOf(cocoe.getSimilaridadeInicioFim()).replace(".", ",")
                        + ";" + String.valueOf(cocoe.getMediaDaSemelhancaSemanticaEntreFrases()).replace(".", ",") + ";" + String.valueOf(cocoe.getSobreposicaoDePalavrasEmFrasesAdjcentes()).replace(".", ",")
                        + ";" + String.valueOf(cocoe.getRazaoDePronomesPorSubsitantivos()).replace(".", ",") + ";" + String.valueOf(cocoe.getMtldPalavrasDeConteudos()).replace(".", ",")
                        + ";" + String.valueOf(cocoe.getComprimentoMedioDaPalavra()).replace(".", ",") + ";" + String.valueOf(cocoe.getComprimentoMedioDaFrase()).replace(".", ",");

//                        + ";" + String.valueOf(cocoe.getMediaDaSobreposicaoDeLemasDeSubstantivos()).replace(".", ",") 
//                        + ";" + String.valueOf(cocoe.getInsidenciaDePalavrasDeConteudo()).replace(".", ",")  + ";" + String.valueOf(cocoe.getMdldLemas()).replace(".", ",")
//                        + ";" + String.valueOf(cocoe.getSobreposicaoMediadDeLemasEmFrasesAdjacentes()) 
//                         ;
                escreverNoArquivoCsv(new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/saida/qualidade_do_texto.csv", s, "cc", tipo, gravarCabecalho);
                retorno.add(cocoe);
            }
        }
        return retorno;
    }

    public static void fractais() throws Exception {
        for (int i = 0; i <= 1; i++) {
            if (i == 0) {
                pasta = new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Corpora/Fractais/MausPalavras";
                processarFractais(pasta, "MausPalavras");
            } else if (i == 1) {
                pasta = new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Corpora/Fractais/BonsPalavras";
                processarFractais(pasta, "BonsPalavras");
            }
        }
    }

    public static boolean processarFractais(String pasta, String tipo) throws Exception {
        file = new File(pasta);

        String ficheiroAGravar = "";
        String conteudo = "";

        for (File ficheiro : file.listFiles()) {
            fractais = new Fractais();

            conteudo = "";
            lingua = linguaDoTexto(ficheiro.getName());
//            if (ficheiro.getName().equalsIgnoreCase("Fractal11036(pt).txt")) {
//                System.out.println(ficheiro.getName());
//            }
            caminhoDoFicheiro = ficheiro.getAbsolutePath();
            if (tipo.equalsIgnoreCase("MausPalavras")) {
                caminhoDoFicheiro2 = new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Corpora/Fractais/Maus/" + ficheiro.getName();
            } else if (tipo.equalsIgnoreCase("BonsPalavras")) {
                caminhoDoFicheiro2 = new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/Corpora/Fractais/Bons/" + ficheiro.getName();
            }
            for (int i = 0; i <= 1; i++) {
                if (i == 0) {
                    fractais.vocabulario.setConteudo(getConteudo(caminhoDoFicheiro2));
                    Ficheiros.txt = new Text(fractais.vocabulario.getconteudo());
                    fractais.vocabulario.setVocabulario(txt.getVocab());
                    fractais.vocabulario.setNumeroPalavras(txt.getNumWords());
                    fractais.vocabulario.setPalavras(getListaDePalavras(fractais.vocabulario.getconteudo()));
                    fractais.vocabulario.setTokens(getTokens(fractais.vocabulario.getConteudo(), lingua));
                    fractais.vocabulario.setListaPalavrasdeConteudo(getPalavrasDeConteudo(fractais.vocabulario.getTokens().toArray(new String[fractais.vocabulario.getTokens().size()]), lingua));
                    for (int z = 0; z < 4; z++) {
                        conteudo = "";
                        if (tipo.equalsIgnoreCase("Bons") || tipo.equalsIgnoreCase("BonsPalavras")) {
                            ficheiroAGravar = (new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/saida/Fractais/Bons/");
                        } else if (tipo.equalsIgnoreCase("Maus") || tipo.equalsIgnoreCase("MausPalavras")) {
                            ficheiroAGravar = (new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/saida/Fractais/Maus/");
                        }
                        if (z == 0) {
                            ficheiroAGravar = ficheiroAGravar.concat("F1.txt");
                            if (lingua.equalsIgnoreCase("pt")) {
                                mtld = Metricas.getMTLD(getListaSteming(fractais.vocabulario.getPalavras(), Inicializacoes.stemmerPortugues));
                            } else if (lingua.equalsIgnoreCase("en")) {
                                mtld = Metricas.getMTLD(getListaSteming(fractais.vocabulario.getPalavras(), Inicializacoes.stemmerIngles));
                            }
                            conteudo = conteudo.concat(String.valueOf(mtld) + " ");
                        } else if (z == 1) {
                            ficheiroAGravar = ficheiroAGravar.concat("F2.txt");
                            taxarepeticao = Metricas.getTaxaDeRepeticao(fractais.vocabulario, lingua);
                            conteudo = conteudo.concat(String.valueOf(taxarepeticao) + " ");
                        } else if (z == 2) {
                            ficheiroAGravar = ficheiroAGravar.concat("F3.txt");
                            if (lingua.equalsIgnoreCase("pt")) {
                                maturidade = Metricas.getMaturidade(fractais.vocabulario, Inicializacoes.hashMapChaveValorPortugues, Inicializacoes.dicionarioPortugues);
                            } else if (lingua.equalsIgnoreCase("en")) {
                                maturidade = Metricas.getMaturidade(fractais.vocabulario, Inicializacoes.hashMapChaveValorIngles, Inicializacoes.dicionarioIngles);
                            }
                            conteudo = conteudo.concat(String.valueOf(maturidade) + " ");
                        } else if (z == 3) {
                            ficheiroAGravar = ficheiroAGravar.concat("F4.txt");
                            densidade = Metricas.getDensidade(fractais.vocabulario.getListaPalavrasdeConteudo(), fractais.vocabulario.getNumeroPalavras());
                            conteudo = conteudo.concat(String.valueOf(densidade) + " ");
                        }
                        escreverNoArquivoFractais(conteudo.replace(".", ","), ficheiroAGravar);
                        ficheiroAGravar = "";
                        conteudo = "";
                    }
                } else if (i == 1) {
                    if (tipo.equalsIgnoreCase("Bons") || tipo.equalsIgnoreCase("BonsPalavras")) {
                        ficheiroAGravar = (new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/saida/Fractais/Bons/");
                    } else if (tipo.equalsIgnoreCase("Maus") || tipo.equalsIgnoreCase("MausPalavras")) {
                        ficheiroAGravar = (new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/saida/Fractais/Maus/");
                    }
                    if (lingua.equalsIgnoreCase("pt")) {

                        if (tipo.equalsIgnoreCase("MausPalavras") || tipo.equalsIgnoreCase("BonsPalavras")) {
                            fractais.coesaoECoerencia.setConteudoEmLinhas(lerFicheiro(caminhoDoFicheiro));
                        } else {
                            fractais.coesaoECoerencia.setConteudoEmLinhas(lerFicheiro(caminhoDoFicheiro2));
                        }
                        fractais.coesaoECoerencia.setConteudo(getConteudo(caminhoDoFicheiro2));
                        Ficheiros.txt = new Text(fractais.coesaoECoerencia.getConteudo());
                        fractais.coesaoECoerencia.setTotalPalavras(Ficheiros.txt.getNumWords());
                        fractais.coesaoECoerencia.setFrases(getFrases(fractais.coesaoECoerencia.getConteudo(), lingua));
                        fractais.coesaoECoerencia.setTokensPorFrase(getTokensPorFrase(fractais.coesaoECoerencia, lingua));
                        fractais.coesaoECoerencia.setListaDePronomesPorFrase(getPronomesPorFrase(fractais.coesaoECoerencia, lingua));
                        fractais.coesaoECoerencia.setListaPalavrasdeConteudo(getPalavrasDeConteudo(fractais.coesaoECoerencia.getTokens().toArray(new String[fractais.coesaoECoerencia.getTokens().size()]), lingua));
                        fractais.coesaoECoerencia.setListaDeSubstantivosPorFrase(getListaDeSubstativosPorFrase(fractais.coesaoECoerencia, lingua));
                        fractais.coesaoECoerencia.setListaEntidadesPorFrase(fractais.coesaoECoerencia.getListaDeSubstantivosPorFrase());
                        // fractais.coesaoECoerencia.setListaEntidadesPorFrase(getListaEntidadesPorFrase(fractais.coesaoECoerencia, fractais.coesaoECoerencia.getConteudoEmLinhas(), null, lingua));
//                        fractais.coesaoECoerencia.setVocabularioEntidades(getVocabularioDeEntidades(fractais.coesaoECoerencia.getListaEntidadesPorFrase()));
                        try {
                            fractais.coesaoECoerencia.setListaPapeisEntidadesPorFrase(getListaPapeisEntidadesPorFrase(fractais.coesaoECoerencia, lingua));

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                        for (int j = 0; j < 17; j++) {
                            conteudo = "";
                            switch (j) {
                                case 0:
                                    ficheiroAGravar = ficheiroAGravar.concat("F5.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getCoerenciaDoTexto(fractais.coesaoECoerencia)) + " ");
                                    break;
                                case 1:
                                    ficheiroAGravar = ficheiroAGravar.concat("F6.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getSujeitoObjecto(fractais.coesaoECoerencia, "sujeitoSujeito")) + " ");
                                    break;
                                case 2:
                                    ficheiroAGravar = ficheiroAGravar.concat("F7.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getSujeitoObjecto(fractais.coesaoECoerencia, "sujeitoObjeto")) + " ");
                                    break;
                                case 3:
                                    ficheiroAGravar = ficheiroAGravar.concat("F8.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getSujeitoObjecto(fractais.coesaoECoerencia, "objetoObjeto")) + " ");
                                    break;
                                case 4:
                                    ficheiroAGravar = ficheiroAGravar.concat("F9.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getSujeitoObjecto(fractais.coesaoECoerencia, "objetoSujeito")) + " ");
                                    break;
                                case 5:
                                    ficheiroAGravar = ficheiroAGravar.concat("F10.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getMediaDeOperadoresLogicosPorFrase(Arrays.asList(fractais.coesaoECoerencia.getFrases()), "pt")) + " ");
                                    break;
                                case 6:
                                    ficheiroAGravar = ficheiroAGravar.concat("F11.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getDensidadeDePronomes(fractais.coesaoECoerencia)) + " ");
                                    break;
                                case 7:
                                    ficheiroAGravar = ficheiroAGravar.concat("F12.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getSobreposicaoDeSUbstantivos(fractais.coesaoECoerencia, Inicializacoes.stemmerPortugues)) + " ");
                                    break;
                                case 8:
                                    ficheiroAGravar = ficheiroAGravar.concat("F13.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getMediaDePronomesPorFrase(fractais.coesaoECoerencia)) + " ");
                                    break;
                                case 9:
                                    ficheiroAGravar = ficheiroAGravar.concat("F14.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getSemelhancaSemanticaEntreParagrafosInicEFinais(fractais.coesaoECoerencia)) + " ");
                                    break;
                                case 10:
                                    ficheiroAGravar = ficheiroAGravar.concat("F15.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getMediaDeSemelhancaSemanticaEntreFrases(fractais.coesaoECoerencia)) + " ");
                                    break;
                                case 11:
                                    ficheiroAGravar = ficheiroAGravar.concat("F16.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getMediaDeSobreposicaoDePalavrasEmCadaParDeFrases(fractais.coesaoECoerencia)) + " ");
                                    break;
                                case 12:
                                    ficheiroAGravar = ficheiroAGravar.concat("F17.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getRazoEntrePronomesESubstativos(fractais.coesaoECoerencia) + " "));
                                    break;
                                case 13:
                                    ficheiroAGravar = ficheiroAGravar.concat("F18.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getMTLDPalavrasDeConteudo(fractais.coesaoECoerencia)) + " ");
                                    break;
                                case 14:
                                    ficheiroAGravar = ficheiroAGravar.concat("F19.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getComprimentoMedio(fractais.coesaoECoerencia, "palavra")) + " ");
                                    break;
                                case 15:
                                    ficheiroAGravar = ficheiroAGravar.concat("F20.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getComprimentoMedio(fractais.coesaoECoerencia, "frase")) + " ");

                                    break;
                            }
                            escreverNoArquivoFractais(conteudo.replace(".", ","), ficheiroAGravar);
                            if (tipo.equalsIgnoreCase("Bons") || tipo.equalsIgnoreCase("BonsPalavras")) {
                                ficheiroAGravar = (new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/saida/Fractais/Bons/");
                            } else if (tipo.equalsIgnoreCase("Maus") || tipo.equalsIgnoreCase("MausPalavras")) {
                                ficheiroAGravar = (new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/saida/Fractais/Maus/");
                            }
                            conteudo = "";
                        }
                    } else if (lingua.equalsIgnoreCase("en")) {
                        fractais.coesaoECoerencia.setConteudoEmLinhas(lerFicheiro(caminhoDoFicheiro));
                        fractais.coesaoECoerencia.setConteudo(getConteudo(caminhoDoFicheiro));
                        Ficheiros.txt = new Text(fractais.coesaoECoerencia.getConteudo());
                        fractais.coesaoECoerencia.setTotalPalavras(Ficheiros.txt.getNumWords());
                        fractais.coesaoECoerencia.setFrases(getFrases(fractais.coesaoECoerencia.getConteudo(), lingua));
                        fractais.coesaoECoerencia.setTokensPorFrase(getTokensPorFrase(fractais.coesaoECoerencia, lingua));
                        fractais.coesaoECoerencia.setListaDePronomesPorFrase(getPronomesPorFrase(fractais.coesaoECoerencia, lingua));
                        fractais.coesaoECoerencia.setListaPalavrasdeConteudo(getPalavrasDeConteudo(fractais.coesaoECoerencia.getTokens().toArray(new String[fractais.coesaoECoerencia.getTokens().size()]), lingua));
                        fractais.coesaoECoerencia.setListaDeSubstantivosPorFrase(getListaDeSubstativosPorFrase(fractais.coesaoECoerencia, lingua));
                        fractais.coesaoECoerencia.setListaEntidadesPorFrase(fractais.coesaoECoerencia.getListaDeSubstantivosPorFrase());

                        //fractais.coesaoECoerencia.setListaEntidadesPorFrase(getListaEntidadesPorFrase(fractais.coesaoECoerencia, null, fractais.coesaoECoerencia.getTokensPorFrase(), lingua));
//                        fractais.coesaoECoerencia.setVocabularioEntidades(getVocabularioDeEntidades(fractais.coesaoECoerencia.getListaEntidadesPorFrase()));
                        fractais.coesaoECoerencia.setListaPapeisEntidadesPorFrase(getListaPapeisEntidadesPorFrase(fractais.coesaoECoerencia, lingua));
                        //  fractais.coesaoECoerencia.setMatrizDeInsidencia(Metricas.getMatrizDeInsidencia(fractais.coesaoECoerencia, fractais.coesaoECoerencia.getListaEntidadesPorFrase(), fractais.coesaoECoerencia.getListaPapeisEntidadesPorFrase()));

                        /*    
                         */
                        for (int j = 0; j < 17; j++) {
                            switch (j) {
                                case 0:
                                    ficheiroAGravar = ficheiroAGravar.concat("F5.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getCoerenciaDoTexto(fractais.coesaoECoerencia)) + " ");
                                    break;
                                case 1:
                                    ficheiroAGravar = ficheiroAGravar.concat("F6.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getSujeitoObjecto(fractais.coesaoECoerencia, "sujeitoSujeito")) + " ");
                                    break;
                                case 2:
                                    ficheiroAGravar = ficheiroAGravar.concat("F7.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getSujeitoObjecto(fractais.coesaoECoerencia, "sujeitoObjeto")) + " ");
                                    break;
                                case 3:
                                    ficheiroAGravar = ficheiroAGravar.concat("F8.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getSujeitoObjecto(fractais.coesaoECoerencia, "objetoObjeto")) + " ");
                                    break;
                                case 4:
                                    ficheiroAGravar = ficheiroAGravar.concat("F9.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getSujeitoObjecto(fractais.coesaoECoerencia, "objetoSujeito")) + " ");
                                    break;
                                case 5:
                                    ficheiroAGravar = ficheiroAGravar.concat("F10.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getMediaDeOperadoresLogicosPorFrase(Arrays.asList(fractais.coesaoECoerencia.getFrases()), "en")) + " ");
                                    break;
                                case 6:
                                    ficheiroAGravar = ficheiroAGravar.concat("F11.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getDensidadeDePronomes(fractais.coesaoECoerencia)) + " ");
                                    break;
                                case 7:
                                    ficheiroAGravar = ficheiroAGravar.concat("F12.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getSobreposicaoDeSUbstantivos(fractais.coesaoECoerencia, Inicializacoes.stemmerIngles)) + " ");
                                    break;
                                case 8:
                                    ficheiroAGravar = ficheiroAGravar.concat("F13.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getMediaDePronomesPorFrase(fractais.coesaoECoerencia)) + " ");
                                    break;
                                case 9:
                                    ficheiroAGravar = ficheiroAGravar.concat("F14.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getSemelhancaSemanticaEntreParagrafosInicEFinais(fractais.coesaoECoerencia)) + " ");
                                    break;
                                case 10:
                                    ficheiroAGravar = ficheiroAGravar.concat("F15.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getMediaDeSemelhancaSemanticaEntreFrases(fractais.coesaoECoerencia)) + " ");
                                    break;
                                case 11:
                                    ficheiroAGravar = ficheiroAGravar.concat("F16.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getMediaDeSobreposicaoDePalavrasEmCadaParDeFrases(fractais.coesaoECoerencia)) + " ");
                                    break;
                                case 12:
                                    ficheiroAGravar = ficheiroAGravar.concat("F17.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getRazoEntrePronomesESubstativos(fractais.coesaoECoerencia) + " "));
                                    break;
                                case 13:
                                    ficheiroAGravar = ficheiroAGravar.concat("F18.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getMTLDPalavrasDeConteudo(fractais.coesaoECoerencia)) + " ");
                                    break;
                                case 14:
                                    ficheiroAGravar = ficheiroAGravar.concat("F19.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getComprimentoMedio(fractais.coesaoECoerencia, "palavra")) + " ");
                                    break;
                                case 15:
                                    ficheiroAGravar = ficheiroAGravar.concat("F20.txt");
                                    conteudo = conteudo.concat(String.valueOf(Metricas.getComprimentoMedio(fractais.coesaoECoerencia, "frase")) + " ");
                                    break;
                            }
                            escreverNoArquivoFractais(conteudo.replace(".", ","), ficheiroAGravar);
                            if (tipo.equalsIgnoreCase("Bons") || tipo.equalsIgnoreCase("BonsPalavras")) {
                                ficheiroAGravar = (new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/saida/Fractais/Bons/");
                            } else if (tipo.equalsIgnoreCase("Maus") || tipo.equalsIgnoreCase("MausPalavras")) {
                                ficheiroAGravar = (new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/saida/Fractais/Maus/");
                            }
                            conteudo = "";
                        }
                    }

                }
            }
            fractais.vocabulario.setConteudo(null);
            fractais.vocabulario.setVocabulario(null);
            fractais.vocabulario.setNumeroPalavras(0);
            fractais.vocabulario.setPalavras(null);
            fractais.vocabulario.setTokens(null);
            fractais.vocabulario.setListaPalavrasdeConteudo(null);

            Ficheiros.txt = null;
            fractais.coesaoECoerencia.setTotalPalavras(0);
            fractais.coesaoECoerencia.setFrases(null);
            fractais.coesaoECoerencia.setTokensPorFrase(null);
            fractais.coesaoECoerencia.setListaDePronomesPorFrase(null);
            fractais.coesaoECoerencia.setListaPalavrasdeConteudo(null);
            fractais.coesaoECoerencia.setListaDeSubstantivosPorFrase(null);
            fractais.coesaoECoerencia.setListaEntidadesPorFrase(null);
        }

        return false;
    }

}
