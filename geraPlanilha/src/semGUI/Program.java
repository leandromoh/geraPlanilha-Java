package semGUI;

public class Program {

    public static void main(String[] args) {
        
        GeradorPlanilha g1 = new GeradorPlanilha();
        
        g1.setData(GeradorPlanilha.DiaInicialMes.DOIS, 6, 2014);
        g1.setGuardasNome("Jóse", "Mario", "Carlos");
        g1.setGuardaInicial(GeradorPlanilha.Guardas.TERCEIRO);
        g1.criaPlanilha();
    }
    
}
