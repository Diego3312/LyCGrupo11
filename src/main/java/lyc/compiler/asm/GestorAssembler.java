package lyc.compiler.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import lyc.compiler.intermediateCodeG.Terceto;
import lyc.compiler.simbolsTable.DataType;
import lyc.compiler.simbolsTable.SimbolRow;

public class GestorAssembler {
        ArrayList<String> listInst = new ArrayList<String>();

    public void GestorAssembler(){
    }

    public void generarAssembler(List<SimbolRow> TablaDeSimbolos , ArrayList<Terceto> listaTercetos){

        Stack<String> pilaOperandos  = new Stack<String>();
//        Queue<String> colaEtiquetas  = new LinkedList<String>();
        Stack<String> pilaEtiquetas  = new Stack<String>();

        Stack<Integer> pilaNroCelda = new Stack<Integer>();

        ArrayList<String> codigo = new ArrayList<String>(); // sentencias de programacion del programador
        listInst.add("include macros2.asm\r\n" + //
                        "");
        listInst.add("include number.asm");
        listInst.add(".MODEL LARGE");
        listInst.add(".386");
        listInst.add(".STACK 200h\n");

        listInst.add(".DATA\n");
        ///agrego la tabla de simbolos
        for(SimbolRow simbolo : TablaDeSimbolos){
            if(simbolo.getNombre() == DataType.CTE_STRING.toString()){
                listInst.add(String.format("%-20s %-5s %-30s", simbolo.getId(),"db", "\"" + simbolo.getValor()+ "\"" + ",'$'," + simbolo.getLongitud() + " dup(?)"));
            }
            else if(simbolo.getNombre() == DataType.STRING.toString()){
                listInst.add(String.format("%-20s %-5s %-30s", simbolo.getId(),"db", " 100 dup(?)"));
            }
            else if(simbolo.getNombre() == DataType.DATECONVERTED.toString()){
                listInst.add(String.format("%-20s %-5s %-30s", simbolo.getId(),"db", "\"" + simbolo.getValor()+ "\"" + ",'$', 100 dup(?)"));
            }
            else{
                String simbolo_valor = simbolo.getValor();
                if(simbolo_valor == "-")
                    simbolo_valor = "?";
                else if(!simbolo_valor.contains("."))
                    simbolo_valor = simbolo_valor + ".";
                listInst.add(String.format("%-20s %-5s %-30s", simbolo.getId(),"dd", simbolo_valor));
            }
                
        }
        listInst.add(String.format("%-20s %-5s %-30s","_buffer_len","db", "49"));
        listInst.add(String.format("%-20s %-5s %-30s","_buffer_cont","db", "?"));
        listInst.add(String.format("%-20s %-5s %-30s","_buffer_data","db", "49 dup(?)"));
        

        //cabecera de instrucciones
        codigo.add("\n.CODE");
        codigo.add("START:");
        codigo.add("\nMOV AX, @DATA");
        codigo.add("MOV DS, AX");
        codigo.add("MOV ES, AX\n");

        System.out.print("Lista de Tercetos\n");
        boolean esNum = false;
        int cantVariablesAuxiliares = 0;
        String op2;
        String op1;
        String varAux;
        String etiqueta;
        String tipo;
        String etiquetaActual = "";
        for (Terceto terceto : listaTercetos) {
            System.out.println(terceto.toString());
            tipo = terceto.getT1();
            if(tipo.matches("ET_\\d+")){
                etiquetaActual = terceto.getT1();
                 tipo = "ET";
            }
               
            switch (tipo) {
                case ":=":
                    int es_string = 0;
                    op2 = pilaOperandos.pop();
                    try {
                        Float.valueOf(op2);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op2 = "_" +  op2.replace(".","_");
                        esNum = false;
                    }
                    else if(op2.contains("\"")){
                        es_string = 1;
                        op2 = op2.replace("\"","");
                        op2 = "_" + op2.replace(" ","_");
                    }
                    else if( op2 == "@retorno")
                        es_string = 1;
                    op1 = terceto.getT2();
                    try {
                        Float.valueOf(op1);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op1 = "_"+op1;
                        esNum = false;
                    }
                    if(es_string == 1){
                        codigo.add("copy_str " + op2 +", " + op1 );
                    }
                    else{
                    codigo.add("FLD " + op2);
                    codigo.add("FSTP " + op1);
                    codigo.add("");}
                    break;
                case "+":
                    op2 = pilaOperandos.pop();
                    try {
                        Float.valueOf(op2);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op2 = "_" +  op2.replace(".","_");
                        esNum = false;
                    }
                    op1 = pilaOperandos.pop();
                    try {
                        Float.valueOf(op1);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op1 = "_" +  op1.replace(".","_");
                        esNum = false;
                    }
                    varAux = "@aux" + (cantVariablesAuxiliares+1);
                    cantVariablesAuxiliares++;
                    codigo.add("FLD " + op1);
                    codigo.add("FLD " + op2);
                    codigo.add("FADD");
                    codigo.add("FSTP " + varAux);
                    codigo.add("");
                    pilaOperandos.add(varAux);
                    listInst.add(String.format("%-20s %-5s %-30s", varAux,"dd", "?"));//AGREGADO POR MI PARA AGREGAR varAux a .DATA
                    break;
                case "-":
                    op2 = pilaOperandos.pop();
                    try {
                        Float.valueOf(op2);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op2 = "_" +  op2.replace(".","_");
                        esNum = false;
                    }
                    op1 = pilaOperandos.pop();
                    try {
                        Float.valueOf(op1);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op1 = "_" +  op1.replace(".","_");
                        esNum = false;
                    }
                    varAux = "@aux" + (cantVariablesAuxiliares+1);
                    cantVariablesAuxiliares++;
                    codigo.add("FLD " + op1);
                    codigo.add("FLD " + op2);
                    codigo.add("FSUB");
                    codigo.add("FSTP " + varAux);
                    codigo.add("");
                    pilaOperandos.add(varAux);
                    listInst.add(String.format("%-20s %-5s %-30s", varAux,"dd", "?"));//AGREGADO POR MI PARA AGREGAR varAux a .DATA
                    break;
                case "/":
                    op2 = pilaOperandos.pop();
                    try {
                        Float.valueOf(op2);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op2 = "_" +  op2.replace(".","_");
                        esNum = false;
                    }
                    op1 = pilaOperandos.pop();
                    try {
                        Float.valueOf(op1);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op1 = "_" +  op1.replace(".","_");
                        esNum = false;
                    }
                    varAux = "@aux" + (cantVariablesAuxiliares+1);
                    cantVariablesAuxiliares++;
                    codigo.add("FLD " + op1);
                    codigo.add("FLD " + op2);
                    codigo.add("FDIV");
                    codigo.add("FSTP " + varAux);
                    codigo.add("");
                    pilaOperandos.add(varAux);
                    listInst.add(String.format("%-20s %-5s %-30s", varAux,"dd", "?"));//AGREGADO POR MI PARA AGREGAR varAux a .DATA
                    break;
                case "*":
                    op2 = pilaOperandos.pop();
                    try {
                        Float.valueOf(op2);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op2 = "_" +  op2.replace(".","_");
                        esNum = false;
                    }
                    op1 = pilaOperandos.pop();
                    try {
                        Float.valueOf(op1);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op1 = "_" +  op1.replace(".","_");
                        esNum = false;
                    }
                    varAux = "@aux" + (cantVariablesAuxiliares+1);
                    cantVariablesAuxiliares++;
                    codigo.add("FLD " + op1);
                    codigo.add("FLD " + op2);
                    codigo.add("FMUL");
                    codigo.add("FSTP " + varAux);
                    codigo.add("");
                    pilaOperandos.add(varAux);
                    listInst.add(String.format("%-20s %-5s %-30s", varAux,"dd", "?"));//AGREGADO POR MI PARA AGREGAR varAux a .DATA
                    break;
                case "CMP":
                    op2 = pilaOperandos.pop();
                    try {
                        Float.valueOf(op2);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op2 = "_" +  op2.replace(".","_");
                        esNum = false;
                    }
                    op1 = pilaOperandos.pop();
                    try {
                        Float.valueOf(op1);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op1 = "_" +  op1.replace(".","_");
                        esNum = false;
                    }
                    codigo.add("FLD " + op1);
                    codigo.add("FLD " + op2);
                    codigo.add("FXCH");
                    codigo.add("FCOM");
                    codigo.add("FSTSW AX");
                    codigo.add("SAHF");
                    break;
                case "BLE":
                case "BGE":
                case "BLT":
                case "BGT":
                case "BE":
                case "BNE":
                    int tercetoDestino1 = Integer.parseInt(terceto.getT2().substring(1, terceto.getT2().length() - 1));
                    Terceto destino1 = listaTercetos.get(tercetoDestino1 - 1);
                    etiqueta = destino1.getT1();
                    String salto_valido = formato_correcto_salto(terceto.getT1());
                    codigo.add(salto_valido + " " + etiqueta);
                    codigo.add("");
                    break;
                case "JMP":
                    int tercetoDestino = Integer.parseInt(terceto.getT2().substring(1, terceto.getT2().length() - 1));
                    Terceto destino = listaTercetos.get(tercetoDestino - 1);
                    etiqueta = destino.getT1();
                    codigo.add("JMP " + etiqueta);
                    codigo.add("");
                    break;
                case "ET":
                    codigo.add(etiquetaActual + ":");
                    codigo.add("");
                    break;
                case "%READ":
                    //leer línea con funcion 0Ah de DOS
                    codigo.add("mov dx, OFFSET _buffer_len");
                    codigo.add("mov ah, 0Ah");
                    codigo.add("int 21h");
                    // copiar _buffer_data -> str1, usando la cantidad escrita en _buffer_cont
                    codigo.add("xor cx, cx");
                    codigo.add("mov cl, _buffer_cont");
                    codigo.add("mov si, OFFSET _buffer_data");
                    codigo.add("mov di, OFFSET " + terceto.getT2());
                    codigo.add("rep movsb");
                    codigo.add("mov byte ptr [di], '$'");
                    break;
                case "%WRITE":
                    String writeAux = terceto.getT2();
                    if(terceto.getT2().contains("\"")){
                        writeAux = "_" + writeAux.replace("\"","").replace(" ","_");
                    }
                    codigo.add("mov dx, OFFSET " + writeAux);
                    codigo.add("mov ah, 9");
                    codigo.add("int 21h");
                    codigo.add("newline 1");
                    break;
                case "%WRITE_NUMBER":
                    codigo.add("DisplayFloat " + terceto.getT2() + ", 1");
                    codigo.add("newline 1");
                default:
                    pilaOperandos.add(terceto.getT1());
                    System.out.println(pilaOperandos.toString());
                    break;
            }

            
        }

        for(String instruccion : codigo){
            listInst.add(instruccion);
        }

        listInst.add("\nMOV AX, 4C00h");
        listInst.add("INT 21h");
        listInst.add("END START");
        System.out.println("############# CODIGO ASSEMBLER #############");
        for(String instruccion : listInst){
            System.out.println(instruccion);

        }


    }

    public ArrayList<String> getListInst(){
        return listInst;
    }

public static String formato_correcto_salto(String salto){
        if(salto == "BLE")
            return "JBE";
        if(salto == "BGE")
            return "JAE";
        if(salto == "BLT")
            return "JB";
        if(salto == "BGT")
            return "JA";
        if(salto == "BE")
            return "JE";
        return "JNE";
}


    
}
