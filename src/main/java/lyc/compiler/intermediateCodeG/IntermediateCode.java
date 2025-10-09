package lyc.compiler.intermediateCodeG;

import java.util.ArrayList;

public class IntermediateCode {
    ArrayList<Terceto> tercetos;
    private static IntermediateCode intermediateCode;

    // El constructor es privado, no permite que se genere un constructor por defecto.
    private IntermediateCode() {
      this.tercetos = new ArrayList<>(); 
      System.out.println("Inicializando Codigo Intermedio");
    }

    public static IntermediateCode getSingletonInstance() {
      if (intermediateCode == null){
          intermediateCode = new IntermediateCode();
      }
      
      return intermediateCode;
    }

    public void crear_terceto(String t1, String t2, String t3){
        this.tercetos.add(
            new Terceto(
                        (" ".equals(t1))? "_": t1,
                        (" ".equals(t2))? "_": t2,
                        (" ".equals(t3))? "_": t3
                        )
            );

    }

    public Integer cantidad(){
        return tercetos.size();
    }

    public void modificar_terceto(Integer indice, String t2){
        Terceto terceto = tercetos.get(indice);
        terceto.setT2(t2);
        tercetos.set(indice, terceto);
    }

    @Override
    public String toString(){
        Integer count = 1;
        String result = String.format("     %-10s%-10s%-10s\n",  "T1", "T2", "T3");
        for (Terceto row : tercetos){
            result += String.format("[%-2d] (%-10s,%-10s,%-10s)\n", count++, row.getT1(), row.getT2(), row.getT3());
        }
        return result;
    }
}
