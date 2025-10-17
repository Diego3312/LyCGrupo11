package lyc.compiler.files;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class IntermediateCodeGenerator implements FileGenerator {

   private static Indice indice = new Indice();
    private static final ArrayList<Terceto> listaTercetos = new ArrayList<Terceto>();
    private static Stack<Integer> pila = new Stack<Integer>();
   private static IntermediateCodeGenerator intermediateCodeGenerator = null;
    

   private IntermediateCodeGenerator(){}

   public static IntermediateCodeGenerator getSingletonInstance(){
    if(IntermediateCodeGenerator.intermediateCodeGenerator == null)
        intermediateCodeGenerator = new IntermediateCodeGenerator();
    return intermediateCodeGenerator;
   }

   public static void completarSaltos(ArrayList<Integer> lista, int destino){
        for (Integer i : lista) {
            listaTercetos.get(i).setOperandoDer(String.valueOf(destino));
        }
   }
   
    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        for (Terceto t:listaTercetos) {
            fileWriter.write(t.toString() + "\n");
        }
    }
    public ArrayList<Terceto> getListaTercetos() {
        return listaTercetos;
    }

    public static Stack<Integer> getPila() {
        return pila;
    }

    public static Indice getIndice() {
        return indice;
    }
    public static void setIndice(Indice indice) {
        IntermediateCodeGenerator.indice = indice;
    }
}
