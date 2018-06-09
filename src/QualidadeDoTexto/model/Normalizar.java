package QualidadeDoTexto.model;

import static QualidadeDoTexto.model.Ficheiros.escreverNoArquivoCsv;
import java.awt.List;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class Normalizar {

    public static void lista() throws Exception {
        Map<String, String[][]> dados = new HashMap<>();
        // String[][] vocabularioTextosMaus = new String[12][12];
        // ArrayList<String> vocabularioTextosBons = new ArrayList<>();
        // String[][] coesaoTextosMaus = new String[2][];
        //String[][] coesaoTextosBons = new String[9][];
        ArrayList<String> vTextosBons = new ArrayList<>();
        ArrayList<String> vTextosMaus = new ArrayList<>();
        ArrayList<String> cTextosBons = new ArrayList<>();
        ArrayList<String> cTextosMaus = new ArrayList<>();
        String tipo = "";
        //Map<String, List<String>> dados= new LinkedHashMap<String, List<String>>();
        String[] list = Ficheiros.lerFicheiro(new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/saida/qualidade_do_texto.csv");

        for (String s : list) {
            //System.out.println(s);
            if (s.equalsIgnoreCase("Vocabulário de textos sem qualidade")
                    || s.equalsIgnoreCase("Vocabulário de textos com qualidade")
                    || s.equalsIgnoreCase("Coesão e coerência de textos sem qualidade")
                    || s.equalsIgnoreCase("Coesão e coerência de textos com qualidade")) {
                tipo = s;

            }
            switch (tipo) {
                case "Vocabulário de textos sem qualidade":
                    if (s.startsWith("-->")) {
                        String linha = s.replace("--> ", "");
                        vTextosMaus.add(linha);

                    }
                    break;
                case "Vocabulário de textos com qualidade":
                    if (s.startsWith("-->")) {
                        String linha = s.replace("--> ", "");
                        vTextosBons.add(linha);
                    }
                    break;
                case "Coesão e coerência de textos sem qualidade":
                    if (s.startsWith("-->")) {
                        String linha = s.replace("--> ", "");
                        cTextosMaus.add(linha);
                    }
                    break;
                case "Coesão e coerência de textos com qualidade":
                    if (s.startsWith("-->")) {
                        String linha = s.replace("--> ", "");
                        cTextosBons.add(linha);
                    }
                    break;
            }

        }
//               Ficheiros.escreverNoArquivoCsv(new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/saida/qualidade_do_texto(Normalizado).csv", getListaComValoresNormalizados( getMatriz(vTextosMaus, 5)), "dl", "Maus");
//                escreverNoArquivoCsv(new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/saida/qualidade_do_texto(Normalizado).csv", getListaComValoresNormalizados( getMatriz(vTextosBons, 5)), "dl", "Bons");
//                escreverNoArquivoCsv(new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/saida/qualidade_do_texto(Normalizado).csv", getListaComValoresNormalizados( getMatriz(cTextosMaus, 17)), "cc", "Maus");
//                escreverNoArquivoCsv(new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/saida/qualidade_do_texto(Normalizado).csv", getListaComValoresNormalizados( getMatriz(cTextosBons, 17)), "cc", "Bons");
//
//        dados.put("Vocabulário de textos sem qualidade", getMatriz(vTextosMaus, 5));
//        dados.put("Vocabulário de textos com qualidade", getMatriz(vTextosBons, 5));
//        dados.put("Coesão e coerência de textos sem qualidade", getMatriz(cTextosMaus, 17));
//        dados.put("Coesão e coerência de textos com qualidade", getMatriz(cTextosBons, 17));
        //getListaComValoresNormalizados( getMatriz(vTextosMaus, 5));
    }

    public static ArrayList<String> getListaComValoresNormalizados(String[][] matriz) {
       // imprimirMatriz(matriz);
        ArrayList<String> retorno = new ArrayList<>();
        double maior = 0;
        int linhas = matriz.length;
        int colunas = matriz[0].length;
        double[] vetorColuna = new double[linhas];
        for (int i = 0; i < colunas; i++) {
            maior = 0;
            if (i != 0) {
                for (int j = 0; j < linhas; j++) {
                    String a = matriz[j][i].replace(",", ".");
                    double num = Double.parseDouble(a);
                    if (num > maior) {
                        maior = num;
                    }
                    vetorColuna[j] = num;

                }
                int n = 0;
               // System.out.println("Maior: " + maior);
                for (Double d : vetorColuna) {
                    if (maior > 1) {
                        matriz[n][i] = String.valueOf(Metricas.arredondarDecimal((d / maior),4));
                    } else {
                        matriz[n][i] = String.valueOf((d));
                    }
                    n++;
                }
            }

        }
        String s = "";
        for (int i = 0; i < matriz.length; i++) {
            s = "";
            for (int j = 0; j < matriz[0].length; j++) {
                s = s + matriz[i][j] + ";";
            }
            retorno.add(s);
        }
        return retorno;
    }

    public static String[][] getMatriz(ArrayList<String> lista, int colunas) {

        String[][] matrizRetorno = new String[lista.size()][colunas];
        int a = 0, b = 0;
        for (String s : lista) {
            String[] separarLinha = s.split(";");
            for (String divisao : separarLinha) {
                matrizRetorno[a][b] = divisao;
                b++;
            }
            b = 0;
            a++;
        }
        return matrizRetorno;
    }

//    public static void imprimirMatriz(String[][] matriz) {
//        System.out.println("Matriz");
//        for (int i = 0; i < matriz.length; i++) {
//            for (int j = 0; j < matriz[0].length; j++) {
//                System.out.printf("%10s", matriz[i][j] + "  |");
//            }
//            System.out.println("");
//        }
//    }
}
