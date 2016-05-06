package core;

public class Data {

    private static int formato = 0;
    private static char sep = '/';
    private static int[] termos = {0, 1, 2};
    private int[] componentes = new int[3];
    private static String[] diaSemanaExtenso = {"Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira", "Sábado", "Domingo"};
    private static String[] mesExtenso = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

    public Data(int t0, int t1, int t2) {
        componentes[termos[0]] = t0;
        componentes[termos[1]] = t1;
        componentes[termos[2]] = t2;
    }

    public Data(String sd) {
        setDataFromString(sd);
    }

    public Data(Data d) {
        clona(d);
    }

    private void clona(Data d) {
        componentes[0] = d.componentes[0];
        componentes[1] = d.componentes[1];
        componentes[2] = d.componentes[2];
    }

    private void diasData(long dias) {
        componentes[0] = 1;
        componentes[1] = 1;
        componentes[2] = 1900;

        soma(dias);
    }

    public long dataDias() {
        long dias;

        dias = (componentes[2] - 1900) * 365;

        dias += contaAnosB(1900, componentes[2] - 1901);

        dias += diasAno(componentes[0], componentes[1], componentes[2]);

        return dias - 1;
    }

    // retorna o numero (posicao) de um dia no ano. Exemplo: 01/02 => 32° dia no ano
    public static int diasAno(int dia, int mes, int ano) {
        int diaAno;

        switch (mes) {
            case 1:
                diaAno = 0;
                break;
            case 2:
                diaAno = 31;
                break;
            case 3:
                diaAno = 59;
                break;
            case 4:
                diaAno = 90;
                break;
            case 5:
                diaAno = 120;
                break;
            case 6:
                diaAno = 151;
                break;
            case 7:
                diaAno = 181;
                break;
            case 8:
                diaAno = 212;
                break;
            case 9:
                diaAno = 243;
                break;
            case 10:
                diaAno = 273;
                break;
            case 11:
                diaAno = 304;
                break;
            case 12:
                diaAno = 334;
                break;
            default:
                return -1;
        }

        diaAno += dia;
        diaAno += ((mes > 2) && bissexto(ano)) ? 1 : 0;

        return diaAno;
    }

    public static boolean bissexto(int ano) {
        return (ano % 4 == 0) && ((ano % 100 != 0) || (ano % 400 == 0));
    }

    public static int diasMes(int mes, int ano) {
        switch (mes) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return bissexto(ano) ? 29 : 28;
            default:
                return 0;
        }
    }

    public static boolean consistirData(int t0, int t1, int t2) {
        Data d = new Data(t0, t1, t2);

        if (d.componentes[1] > 0 && d.componentes[1] < 13) {
            return (d.componentes[0] <= diasMes(d.componentes[1], d.componentes[2]));
        }

        return false;
    }

    public static boolean mudaFormato(int f) {
        switch (f) {
            case 0:
                setFormato(0, 1, 2, '/');
                break;
            case 1:
                setFormato(1, 0, 2, '/');
                break;
            case 2:
                setFormato(0, 1, 2, '-');
                break;
            case 3:
                setFormato(0, 1, 2, '.');
                break;
            case 4:
                setFormato(2, 1, 0, '.');
                break;
            default:
                return false;
        }

        return true;
    }

    private static void setFormato(int a, int b, int c, char d) {
        termos[0] = a;
        termos[1] = b;
        termos[2] = c;
        sep = d;
    }

    public String dataString() {
        String data = "";

        data += checkTime(componentes[termos[0]]) + sep;
        data += checkTime(componentes[termos[1]]) + sep;
        data += checkTime(componentes[termos[2]]);

        return data;
    }

    private String checkTime(int i) {
        return ((i < 10) ? "0" : "") + Integer.toString(i);
    }

    public boolean StringData(String s) {
        setDataFromString(s);

        return consistirData(componentes[0], componentes[1], componentes[2]);
    }

    private void setDataFromString(String s) {
        int pos1, pos2, i = 0;
        String sub1, sub2, sub3;

        for (; s.charAt(i) != sep; i++) ;
        pos1 = i++;

        for (; s.charAt(i) != sep; i++) ;
        pos2 = i;

        sub1 = s.substring(0, pos1);
        sub2 = s.substring(pos1 + 1, pos2);
        sub3 = s.substring(pos2 + 1);

        componentes[termos[0]] = Integer.parseInt(sub1);
        componentes[termos[1]] = Integer.parseInt(sub2);
        componentes[termos[2]] = Integer.parseInt(sub3);
    }

    //conta quantos anos bissextos tem em um intervalo de x anos a partir de um ano inicial y
    public static long contaAnosB(int anoAtual, int anosDicionais) {
        long anoLim = anoAtual + anosDicionais + 1;
        long anosB = 0;

        for (; anoAtual < anoLim; anoAtual++) {
            if (bissexto(anoAtual)) {
                anosB++;
            }
        }

        return anosB;
    }

    public void soma(long dias) {
        int diasCompMes = diasMes(componentes[1], componentes[2]) - componentes[0];
        componentes[0] = (dias > diasCompMes) ? 0 : componentes[0];

        while (dias > 0) {
            if (dias > diasCompMes) {
                dias -= diasCompMes;
                componentes[1]++;
                if (componentes[1] == 13) {
                    componentes[1] = 1;
                    componentes[2]++;
                }
            } else {
                componentes[0] += (int) dias;
                break;
            }
            diasCompMes = diasMes(componentes[1], componentes[2]);
        }
    }

    private void subMuitosDias(long dias){
        diasData(dataDias() - dias);
    }
    
    private void subPoucosDias(long dias) {
        while (dias > 0) {
            if (dias >= componentes[0]) {
                dias -= componentes[0];
                componentes[1]--;
                if (componentes[1] == 0) {
                    componentes[1] = 12;
                    componentes[2]--;
                }
            } else {
                componentes[0] -= (int) dias;
                break;
            }
            componentes[0] = diasMes(componentes[1], componentes[2]);
        }
    }

    public void sub(long dias) {
        if (dias > (dataDias() / 2))
            subMuitosDias(dias);
        else
            subPoucosDias(dias);
    }

    public long sub(Data d) {
        long dif = dataDias() - d.dataDias();

        return (dif < 0) ? dif * -1 : dif;
    }

    public String getDiaString() {
        return checkTime(componentes[0]);
    }

    public int getDia() {
        return componentes[0];
    }

    public int getMes() {
        return componentes[1];
    }

    public int getAno() {
        return componentes[2];
    }

    public String getDiaSemanaExtenso() {
        return diaSemanaExtenso[(int) (dataDias() % 7)];
    }

    public String getMesExtenso() {
        return mesExtenso[componentes[1] - 1];
    }

    public static String getMesExtenso(int mes) {
        return mesExtenso[mes - 1];
    }
}
