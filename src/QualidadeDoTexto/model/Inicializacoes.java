/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QualidadeDoTexto.model;

import static QualidadeDoTexto.model.Ficheiros.getChaveValor;
import static QualidadeDoTexto.model.Ficheiros.getStopWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.PropertiesUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.stemmer.snowball.SnowballStemmer;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 *
 * @author hp_probook
 */
public class Inicializacoes {

    public static Properties props;
    public static AnnotationPipeline pipeline;
    public static InputStream isSentenceIngles, isSentencePortugues;
    public static SentenceModel sentenceModelIngles, sentenceModelPortugues;
    public static SentenceDetectorME sdetectorIngles, sdetectorPortugues;
    public static InputStream istokenModelIgles = null, istokenModelPortugues = null;
    public static InputStream iSPOSModelIngles = null, iSPOSModelPortugues = null;
    public static POSModel posModelIngles, posModelPortugues;
    public static String caminhoFicheirosBons, caminhoFicheirosMaus, caminhoFicheirosBonsPalavras, caminhoFicheirosMausPalavras;
    public static TokenizerModel tokenModelIngles, tokenModelPortugues;
    public static Tokenizer tokenizerIngles, tokenizerPortugues,tokenizerSiples;
    public static POSTaggerME posTaggerIngles, posTaggerPortugues;
    public static SnowballStemmer stemmerPortugues;
    public static SnowballStemmer stemmerIngles;
    public static HashMap<String, String> hashMapChaveValorIngles = null;
    public static HashMap<String, String> hashMapChaveValorPortugues = null;
    public static List<String> dicionarioPortugues = new ArrayList<>(), dicionarioIngles = new ArrayList<>();
    public static List<String> stopoWordsIngles = new ArrayList<>(), stopoWordsPortugues = new ArrayList<>();

    public Inicializacoes() throws Exception {

        props = PropertiesUtils.asProperties(
                "annotators", "tokenize,ssplit,pos,depparse",
                "depparse.model", DependencyParser.DEFAULT_MODEL
        );
        pipeline = new StanfordCoreNLP(props);
        istokenModelIgles = new FileInputStream("/home/hp_probook/Dissertacao/resource/english/en-token.bin");
        tokenModelIngles = new TokenizerModel(Inicializacoes.istokenModelIgles);
        tokenizerIngles = new TokenizerME(tokenModelIngles);
        istokenModelPortugues = new FileInputStream("/home/hp_probook/Dissertacao/resource/portuguese/pt-token.bin");
        tokenModelPortugues = new TokenizerModel(Inicializacoes.istokenModelPortugues);
        tokenizerPortugues = new TokenizerME(tokenModelPortugues);
tokenizerSiples = SimpleTokenizer.INSTANCE;
        isSentenceIngles = new FileInputStream("/home/hp_probook/Dissertacao/resource/english/en-sent.bin");
        sentenceModelIngles = new SentenceModel(isSentenceIngles);
        sdetectorIngles = new SentenceDetectorME(sentenceModelIngles);
        iSPOSModelIngles = new FileInputStream("/home/hp_probook/Dissertacao/resource/english/en-pos-maxent.bin");
        posModelIngles = new POSModel(iSPOSModelIngles);
        posTaggerIngles = new POSTaggerME(posModelIngles);

        isSentencePortugues = new FileInputStream("/home/hp_probook/Dissertacao/resource/portuguese/pt-sent.bin");
        sentenceModelPortugues = new SentenceModel(isSentencePortugues);
        sdetectorPortugues = new SentenceDetectorME(sentenceModelPortugues);
        iSPOSModelPortugues = new FileInputStream("/home/hp_probook/Dissertacao/resource/portuguese/pt-pos-maxent.bin");
        posModelPortugues = new POSModel(iSPOSModelPortugues);
        posTaggerPortugues = new POSTaggerME(posModelPortugues);
        caminhoFicheirosBons = ("/home/hp_probook/Dissertacao/Corpora/Bons");
        caminhoFicheirosMaus = ("/home/hp_probook/Dissertacao/Corpora/Maus");
        caminhoFicheirosBonsPalavras = ("/home/hp_probook/Dissertacao/Corpora/BonsPalavras");
        caminhoFicheirosMausPalavras = ("/home/hp_probook/Dissertacao/Corpora/MausPalavras");
        stemmerIngles = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
        stemmerPortugues = new SnowballStemmer(SnowballStemmer.ALGORITHM.PORTUGUESE);
        hashMapChaveValorIngles = new HashMap<>();
        hashMapChaveValorIngles = getChaveValor("/home/hp_probook/Dissertacao/Apoio/NGSL.txt ");
        hashMapChaveValorPortugues = new HashMap<>();
        hashMapChaveValorPortugues = getChaveValor("/home/hp_probook/Dissertacao/Apoio/common_words.txt");
        dicionarioIngles = Ficheiros.getDicionario("en");
        dicionarioPortugues = Ficheiros.getDicionario("pt");
        stopoWordsIngles = getStopWord(Ficheiros.getConteudo("/home/hp_probook/Dissertacao/Apoio/stopwordsEn.txt"));
        stopoWordsPortugues = getStopWord(Ficheiros.getConteudo("/home/hp_probook/Dissertacao/Apoio/stopwordsPt.txt"));

    }

}
