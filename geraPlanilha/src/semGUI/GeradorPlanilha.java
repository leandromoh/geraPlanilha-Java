package semGUI;

import core.Data;
import core.ManipuladorExcel;

public final class GeradorPlanilha {

    private String Guard1 = "Nome_Guarda_1";
    private String Guard2 = "Nome_Guarda_2";
    private String Guard3 = "Nome_Guarda_3";
    private int Dia;
    private int Mes;
    private int Ano;
    private int NGuard = 0;
    private boolean DataValida = false;
    private int[] Termos = new int[5];

    public static enum DiaInicialMes {

        UM(1),
        DOIS(2);

        private final int Dia;

        public int getDia() {
            return Dia;
        }

        private DiaInicialMes(int dia) {
            Dia = dia;
        }

    }

    public static enum Guardas {

        PRIMEIRO(1),
        SEGUNDO(2),
        TERCEIRO(3);

        private final int Numero;

        public int getNumero() {
            return Numero;
        }

        private Guardas(int numero) {
            Numero = numero;
        }

    }

    public void setGuardasNome(String guarda1, String guarda2, String guarda3) {
        Guard1 = guarda1;
        Guard2 = guarda2;
        Guard3 = guarda3;
    }

    public boolean setData(DiaInicialMes dia, int mes, int ano) {

        if (ano > 1901 && ano < 2100) {
            if (Data.consistirData(dia.getDia(), mes, ano)) {
                Dia = dia.getDia();
                Mes = mes;
                Ano = ano;
                DataValida = true;
                return DataValida;
            }
        }
        DataValida = false;
        return DataValida;
    }

    public void setGuardaInicial(Guardas guarda) {
        NGuard = guarda.getNumero();
    }

    private boolean isGuardaInicialValido() {
        return NGuard != 0;
    }

    public void criaPlanilha() {

        if (DataValida && isGuardaInicialValido()) {
            ManipuladorExcel m1 = new ManipuladorExcel("Escala de Folga");

            Termos = new int[]{Dia, Mes, Ano, NGuard, 5};

            m1.criarCabecalhoAno(2, 2, 3, 7, Ano);

            for (int i = 0; i < 4; i++, Termos[4] += 2) {
                m1.criarCabecalhoMes(Termos[4], Termos[4], 1, 9, Termos[1]);
                m1.criarCabecalhoNomeGuardas(++Termos[4], 1, 4, 7, Guard1, Guard2, Guard3);
                Termos = m1.criarBlocoFolgaMes(Termos[0], Termos[1], Termos[2], Termos[3], ++Termos[4]);
            }

            for (int col : new int[]{1, 4, 7}) {
                m1.larguraColuna(col, 1900);
            }

            String path = System.getProperty("user.dir");
            path += "\\ESCALA " + Mes + "-" + Ano + ".xls";

            m1.salvar(path);
            m1.abrirArquivo(path);
        }
    }

}
