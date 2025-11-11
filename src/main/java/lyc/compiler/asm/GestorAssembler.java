package lyc.compiler.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import lyc.compiler.intermediateCodeG.Terceto;
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

        listInst.add(".MODEL LARGE");
        listInst.add(".386");
        listInst.add(".STACK 200h\n");

        listInst.add(".DATA\n");
        ///agrego la tabla de simbolos
        for(SimbolRow simbolo : TablaDeSimbolos){
            listInst.add(String.format("%-20s %-5s %-30s", simbolo.getId(),"dd", simbolo.getValor()));
        }

        //cabecera de instrucciones
        listInst.add("\n.CODE");
        listInst.add("\nMOV AX, @DATA");
        listInst.add("MOV DS, AX");
        listInst.add("MOV ES, AX\n");

        System.out.print("Lista de Tercetos\n");
        boolean esNum = false;
        int cantVariablesAuxiliares = 0;
        int cantEtiquetas = 0;
        String op2;
        String op1;
        String varAux;
        String etiqueta;
        for (Terceto terceto : listaTercetos) {
            System.out.println(terceto.toString());
            switch (terceto.getT1()) {
                case ":=":
                    op2 = pilaOperandos.pop();
                    try {
                        Integer.valueOf(op2);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op2 = "_"+op2;
                        esNum = false;
                    }
                    op1 = terceto.getT2();
                    try {
                        Integer.valueOf(op1);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op1 = "_"+op1;
                        esNum = false;
                    }
                    codigo.add("FLD " + op1);
                    codigo.add("FSTP " + op2);
                    codigo.add("");
                    break;
                case "+":
                    op2 = pilaOperandos.pop();
                    try {
                        Integer.parseInt(op2);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op2 = "_"+op2;
                        esNum = false;
                    }
                    op1 = pilaOperandos.pop();
                    try {
                        Integer.parseInt(op1);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op1 = "_"+op1;
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
                    break;
                case "-":
                    op2 = pilaOperandos.pop();
                    try {
                        Integer.parseInt(op2);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op2 = "_"+op2;
                        esNum = false;
                    }
                    op1 = pilaOperandos.pop();
                    try {
                        Integer.parseInt(op1);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op1 = "_"+op1;
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
                    break;
                case "/":
                    op2 = pilaOperandos.pop();
                    try {
                        Integer.parseInt(op2);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op2 = "_"+op2;
                        esNum = false;
                    }
                    op1 = pilaOperandos.pop();
                    try {
                        Integer.parseInt(op1);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op1 = "_"+op1;
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
                    break;
                case "*":
                    op2 = pilaOperandos.pop();
                    try {
                        Integer.parseInt(op2);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op2 = "_"+op2;
                        esNum = false;
                    }
                    op1 = pilaOperandos.pop();
                    try {
                        Integer.parseInt(op1);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op1 = "_"+op1;
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
                    break;
                case "CMP":
                    op2 = pilaOperandos.pop();
                    try {
                        Integer.parseInt(op2);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op2 = "_"+op2;
                        esNum = false;
                    }
                    op1 = pilaOperandos.pop();
                    try {
                        Integer.parseInt(op1);
                        esNum = true;
                    } catch (NumberFormatException excepcion) {
                        esNum = false;
                    }
                    if(esNum)
                    {
                        op1 = "_"+op1;
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
                case "BEQ":
                case "BNE":
                    etiqueta = "etiqueta" + (cantEtiquetas+1);
                    cantEtiquetas++;
                    pilaEtiquetas.add(etiqueta);
                    codigo.add(terceto.getT1() + " " + etiqueta);
                    codigo.add("");
                    break;
                case "JMP":
                    etiqueta = "etiqueta" + (cantEtiquetas+1); 
                    cantEtiquetas++;//2
                    pilaEtiquetas.add(etiqueta);
                    String aux = pilaEtiquetas.pop();
                    codigo.add("BI " + aux);
                    codigo.add("");
                    codigo.add(pilaEtiquetas.pop()+ ":");
                    pilaEtiquetas.add(aux);
                    break;
                case "ET":
                    etiqueta = "etiqueta" + (cantEtiquetas+1);
                    cantEtiquetas++;
                    pilaEtiquetas.add(etiqueta);
                    codigo.add(etiqueta + ":");
                    codigo.add("");
                    break;
                
                default:
                    pilaOperandos.add(terceto.getT1());
                    break;
            }

            
        }

        for(String instruccion : codigo){
            listInst.add(instruccion);
        }

        listInst.add("\nMOV AX, 4C00h");
        listInst.add("INT 21h");
        listInst.add("END");
        System.out.println("############# CODIGO ASSEMBLER #############");
        for(String instruccion : listInst){
            System.out.println(instruccion);

        }


    }

    public ArrayList<String> getListInst(){
        return listInst;
    }

    
}
