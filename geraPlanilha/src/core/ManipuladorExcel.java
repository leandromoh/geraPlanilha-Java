package core;

import java.io.*;
//biblioteca disponivel em http://poi.apache.org/
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFRegionUtil;

public class ManipuladorExcel {

    private Data d1;
    private HSSFWorkbook wb;
    private HSSFSheet sheet;
    private HSSFCellStyle styleTitulo;

    public ManipuladorExcel(String nome) {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet(nome);
        styleTitulo = wb.createCellStyle();
        styleTitulo.setFont(createFont(16, "Calibri", HSSFFont.BOLDWEIGHT_BOLD));
        styleTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    }

    public void criarCabecalhoAno(int i1, int i2, int j1, int j2, int ano) {
        createCell(sheet.createRow(i1), j1, "ESCALA DE FOLGAS " + ano, styleTitulo);
        merge(i1, i2, j1, j2);
    }

    public void criarCabecalhoMes(int i1, int i2, int j1, int j2, int mes) {
        createCell(sheet.createRow(i1), j1, Data.getMesExtenso(mes).toUpperCase(), styleTitulo);
        bordaRange(merge(i1, i2, j1, j2), CellStyle.BORDER_MEDIUM);
    }

    public void criarCabecalhoNomeGuardas(int i1, int j1, int j2, int j3, String g1, String g2, String g3) {
        HSSFCellStyle style = wb.createCellStyle();
        style.setFont(createFont(11, "Calibri", HSSFFont.BOLDWEIGHT_BOLD));
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        int[] vetInt = {j1, j2, j3};
        String[] vetStr = {"Manha: " + g1, "Tarde: " + g2, "Noite: " + g3};
        HSSFRow row = sheet.createRow(i1);

        for (int i = 0; i < vetInt.length; i++) {
            createCell(row, vetInt[i], vetStr[i], style);
            bordaRange(merge(i1, i1, vetInt[i], vetInt[i] + 2), CellStyle.BORDER_THIN);
        }
    }

    public int[] criarBlocoFolgaMes(int dia, int mes, int ano, int guarda, int i) {
        int i2 = i, j;
        boolean b = true;
        d1 = new Data(dia, mes, ano);
        dia = guarda == 1 ? 0 : guarda == 2 ? 2 : 4;
        d1.sub(dia);
        dia = dia == 0 ? -1 : dia + 2;
        mes = mes == 12 ? 1 : mes + 1;

        HSSFCellStyle style1 = createFullBorder(CellStyle.BORDER_THIN);
        style1.setFont(createFont(11, "Calibri", HSSFFont.BOLDWEIGHT_NORMAL));
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.cloneStyleFrom(style1);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        do {
            HSSFRow row = sheet.createRow(i);
            for (j = 1; j < 10; j += 2) {

                createCell(row, j, d1.getDiaString() + " - ", style1);
                createCell(row, ++j, " " + d1.getDiaSemanaExtenso(), style2);
                createCell(row, j + 1, "", wb.createCellStyle());
                bordaRange(merge(i, i, j, j + 1), CellStyle.BORDER_THIN);

                d1.soma(2);

                if (d1.getMes() == mes) {
                    b = false;
                    break;
                }
            }
            i++;
        } while (b);

        limpaPrimeiraLinhaBlocoFolgaMes(i2, dia);
        mesclaCellsUltimaLinhaBlocoFolgaMes(--i, j + 2);

        int[] termos = {d1.getDia(), d1.getMes(), d1.getAno(), j == 8 ? 1 : j == 5 ? 3 : 2, i};

        return termos;
    }

    private CellRangeAddress merge(int i1, int i2, int j1, int j2) {
        CellRangeAddress range = new CellRangeAddress(i1, i2, j1, j2);
        sheet.addMergedRegion(range);
        return range;
    }

    private void bordaRange(CellRangeAddress range, short tam) {
        HSSFRegionUtil.setBorderTop(tam, range, sheet, wb);
        HSSFRegionUtil.setBorderLeft(tam, range, sheet, wb);
        HSSFRegionUtil.setBorderRight(tam, range, sheet, wb);
        HSSFRegionUtil.setBorderBottom(tam, range, sheet, wb);
    }

    private void createCell(HSSFRow row, int column, String value, HSSFCellStyle style) {
        HSSFCell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private HSSFFont createFont(int tam, String family, short boldWeight) {
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) tam);
        font.setFontName(family);
        font.setBoldweight(boldWeight);
        return font;
    }

    private HSSFCellStyle createFullBorder(short type) {
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderTop(type);
        style.setBorderLeft(type);
        style.setBorderRight(type);
        style.setBorderBottom(type);
        return style;
    }

    public void larguraColuna(int col, int tam) {
        sheet.setColumnWidth(col, tam);
    }

    private void limpaPrimeiraLinhaBlocoFolgaMes(int i, int lim) {
        HSSFRow row = sheet.getRow(i);
        for (int j = 1; j < lim; j++) {
            row.getCell(j).setCellValue("");
        }
    }

    private void mesclaCellsUltimaLinhaBlocoFolgaMes(int i, int j) {
        HSSFCellStyle style = createFullBorder(CellStyle.BORDER_THIN);
        HSSFRow row = sheet.getRow(i);

        for (; j < 10;) {
            for (int n = 0; n < 3; n++) {
                createCell(row, j++, "", style);
            }
            bordaRange(merge(i, i, j - 2, j - 1), CellStyle.BORDER_THIN);
        }
    }

    public void salvar(String path) {
        try {
            FileOutputStream out = new FileOutputStream(path);
            wb.write(out);
            out.close();

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void abrirArquivo(String path) {
        try {
            String[] commands = {"cmd", "/c", "start", "\"DummyTitle\"", path};
            Runtime.getRuntime().exec(commands);
        } catch (Exception err) {
            err.getMessage();
        }
    }
}
