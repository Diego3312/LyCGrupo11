package lyc.compiler.files;

public class Terceto {
    private String operador;
    private String operandoIzq;
    private String operandoDer;
    private int nroTerceto;
    private static int siguiente = 0;

    public Terceto(String operador, String operandoIzq, String operandoDer) {
        this.operador = operador;
        this.operandoIzq = operandoIzq;
        this.operandoDer = operandoDer;
        this.nroTerceto = Terceto.siguiente;
        Terceto.siguiente++;
    }

    public String getOperador() {
        return operador;
    }

    public String getOperandoIzq() {
        return operandoIzq;
    }

    public String getOperandoDer() {
        return operandoDer;
    }

    public int getNroTerceto() {
        return nroTerceto;
    }

    public static int getSiguiente() {
        return siguiente;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public void setOperandoIzq(String operandoIzq) {
        this.operandoIzq = operandoIzq;
    }

    public void setOperandoDer(String operandoDer) {
        this.operandoDer = operandoDer;
    }

    @Override
    public String toString() {
        return "Terceto [operador=" + operador + ", operandoIzq=" + operandoIzq + ", operandoDer=" + operandoDer
                + ", nroTerceto=" + nroTerceto + "]";
    }

}
