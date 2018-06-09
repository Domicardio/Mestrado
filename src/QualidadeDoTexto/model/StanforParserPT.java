package QualidadeDoTexto.model;

import hultig.sumo.Sentence;
import hultig.sumo.Text;
import hultig.sumo.Word;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import tagger.TestTagger;

public class StanforParserPT {

    String taggerModelPath;

    PrintStream output;
    PrintStream oldOutput;
    PrintStream oldOutErr;

    InputStream oldInput;

    public StanforParserPT(String taggerModelPath) {
        this.taggerModelPath = taggerModelPath;
        oldOutput = System.out;
        oldOutErr = System.err;
        oldInput = System.in;
    }

    public String[] doit(String[] frases) {
        if (frases == null || frases.length < 1) {
            return frases;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < frases.length; i++) {
            sb.append(frases[i]);
            if (i < frases.length - 1) {
                sb.append('\n');
            }
        }

        InputStream input = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(input);

        /**/
        System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        }));
        /**/

        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        output = new PrintStream(byteOutStream);
        System.setOut(output);

        TestTagger.main(new String[]{taggerModelPath});

        System.out.flush();
        System.setOut(oldOutput);
        System.setIn(oldInput);
        System.setErr(oldOutErr);

        String[] frasesPOS = byteOutStream.toString().split("\n");
        return frasesPOS;
    }

    public Text doit(Text tx) {
        String[] v = new String[tx.size()];
        for (int i = 0; i < v.length; i++) {
            Sentence si = tx.get(i);
            StringBuilder sb = new StringBuilder();
            for (Word w : si) {
                sb.append(' ').append(w.toString());
            }
            v[i] = sb.toString().trim();
        }
        v = doit(v);
        Text ty = new Text();
        for (int i = 0; i < v.length; i++) {
            Sentence si = tx.get(i);
            String[] vi = v[i].split("[ ]+");
            Sentence sy = new Sentence();
            for (int j = 0; j < vi.length; j++) {
                String[] vij = vi[j].split("_");
                Word wij = si.get(j);
                wij.setPOS(vij[1]);
                sy.add(wij);
            }
            ty.add(sy);
        }
        return ty;
    }

    public static void main(String[] args) throws IOException {
        String[] frases = {
            "Hoje está um lindo dia de Inverno .",
            "O gato das botas é guloso !",
            "",
            "O Banco de Portugal foi à falência !"
        };
        

        StanforParserPT tagger = new StanforParserPT(new File("..").getCanonicalPath() + "/Dissertacao/src/QualidadeDoTexto/taggerPT/");
        frases = tagger.doit(frases);
        for (int i = 0; i < frases.length; i++) {
            System.out.printf("F(%d) ==> [%s]\n", i, frases[i]);
        }
        System.out.println("\n");

        // String s= "Hoje está um lindo dia de Inverno. O gato das botas é guloso! O Banco de Portugal foi à falência!";
        String s = "Domingos Carlos Dionísio é muito lindo!";
        Text t = new Text(s);
        t = tagger.doit(t);
        for (Sentence st : t) {
            for (Word w : st) {
                System.out.println(w.getPOS());
                //System.out.printf("   %s_%s", w, w.getPOS(5));
            }
            System.out.println();
        }

    }

}
