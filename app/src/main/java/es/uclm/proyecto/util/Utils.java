package es.uclm.proyecto.util;

import android.app.Activity;
import android.app.Application;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import es.uclm.proyecto.modelo.Resultado;
import es.uclm.proyecto.sqlite.ContratoBBDD;
import es.uclm.proyecto.util.BorderEvent;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by alber on 01/05/2016.
 */
public class Utils {
    private static final int BUFFER_SIZE = 1024 * 10;

    public static int calcularSubscore(Resultado resultado) {
        String icl="";
        String icr="";
        String lol="";
        String lor="";

        if (resultado.pp_ic_l.equals("I") || resultado.pp_ic_l.equals("E") || resultado.pp_ic_l.equals("R")) icl="R";
        if (resultado.pp_ic_r.equals("I") || resultado.pp_ic_r.equals("E") || resultado.pp_ic_r.equals("R")) icr="R";
        if (resultado.pp_lo_l.equals("I") || resultado.pp_lo_l.equals("E") || resultado.pp_lo_l.equals("R")) lol="R";
        if (resultado.pp_lo_r.equals("I") || resultado.pp_lo_r.equals("E") || resultado.pp_lo_r.equals("R")) lor="R";

        int puntos = 0;
        if (resultado.sp_l.equals("C")) puntos = puntos + 1;
        if (resultado.sp_r.equals("C")) puntos = puntos + 1;
        if (resultado.coord.equals("SOME")) puntos = puntos + 1;
        if (resultado.coord.equals("MOST")) puntos = puntos + 2;
        /*
        if (resultado.pp_ic_l.equals("P") && resultado.pp_lo_l.equals("P")) puntos = puntos + 2;
        if (resultado.pp_ic_l.equals("P") && !resultado.pp_lo_l.equals("P")) puntos = puntos + 1;
        if (!resultado.pp_ic_l.equals("P") && resultado.pp_lo_l.equals("P")) puntos = puntos + 1;
        if (resultado.pp_ic_r.equals("P") && resultado.pp_lo_r.equals("P")) puntos = puntos + 2;
        if (resultado.pp_ic_r.equals("P") && !resultado.pp_lo_r.equals("P")) puntos = puntos + 1;
        if (!resultado.pp_ic_r.equals("P") && resultado.pp_lo_r.equals("P")) puntos = puntos + 1;
        */
        if (resultado.pp_ic_l.equals("P") && resultado.pp_lo_l.equals("P")) puntos = puntos + 2;
        if (resultado.pp_ic_l.equals("P") && lol.equals("R")) puntos = puntos + 1;
        if (icl.equals("R") && resultado.pp_lo_l.equals("P")) puntos = puntos + 1;
        if (resultado.pp_ic_r.equals("P") && resultado.pp_lo_r.equals("P")) puntos = puntos + 2;
        if (resultado.pp_ic_r.equals("P") && lor.equals("R")) puntos = puntos + 1;
        if (icr.equals("R") && resultado.pp_lo_r.equals("P")) puntos = puntos + 1;

        if (resultado.ti.equals("MILD")) puntos = puntos + 1;
        if (resultado.ti.equals("NORMAL")) puntos = puntos + 2;
        if (resultado.tail.equals("UP")) puntos = puntos + 1;

        return puntos;
    }

    public static int calcularScore(Resultado resultado, String pata) {
        int puntos = 0;
        String icl="";
        String icr="";
        String lol="";
        String lor="";

        if (resultado.pp_ic_l.equals("I") || resultado.pp_ic_l.equals("E") || resultado.pp_ic_l.equals("R")) icl="R";
        if (resultado.pp_ic_r.equals("I") || resultado.pp_ic_r.equals("E") || resultado.pp_ic_r.equals("R")) icr="R";
        if (resultado.pp_lo_l.equals("I") || resultado.pp_lo_l.equals("E") || resultado.pp_lo_l.equals("R")) lol="R";
        if (resultado.pp_lo_r.equals("I") || resultado.pp_lo_r.equals("E") || resultado.pp_lo_r.equals("R")) lor="R";

        if (pata.equals("L")) {
            if (resultado.am_l.equals("Ø")) puntos = 0;
            else if (resultado.am_l.equals("S")) puntos = 1;
            else if (resultado.am_l.equals("E") && resultado.pwo_l.equals("N") && resultado.pw_l.equals("N") && resultado.sd_l.equals("Ø"))
                puntos = 2;
            else if (resultado.sp_l.equals("Ø")) puntos = 3;
            else if (resultado.sp_l.equals("O")) puntos = 4;
            else if (((resultado.sp_l.equals("F") || resultado.sp_l.equals("C")) && resultado.coord.equals("Ø")) ||
                    ((resultado.sp_l.equals("F") || resultado.sp_l.equals("C")) && resultado.coord.equals("SOME")
                            && (icl == "R") && (lol == "R" || resultado.pp_lo_l.equals("P") ))
                    ) puntos = 5;
            else if (((resultado.sp_l.equals("F") || resultado.sp_l.equals("C")) &&
                    resultado.coord.equals("SOME") && resultado.pp_ic_l.equals("P") &&  (lol == "R" || resultado.pp_lo_l.equals("P") )) ||
                    ((resultado.sp_l.equals("F") || resultado.sp_l.equals("C")) && resultado.coord.equals("MOST")
//                            && (icl == "R") && (lol == "R")))
                            && (icl == "R") && (lol == "R" || resultado.pp_lo_l.equals("P") )))
                puntos = 6;
            else if (((resultado.sp_l.equals("F") || resultado.sp_l.equals("C")) && resultado.coord.equals("MOST")
                    && (resultado.pp_ic_l.equals("P"))
                    && (lol == "R")) ||
                    ((resultado.sp_l.equals("F") || resultado.sp_l.equals("C")) && resultado.coord.equals("MOST")
                            && (resultado.pp_ic_l.equals("P")) && (resultado.pp_lo_l.equals("P"))
                            && resultado.ti.equals("SEVERE"))) puntos = 7;
            else if (((resultado.sp_l.equals("F") || resultado.sp_l.equals("C")) && resultado.coord.equals("MOST")
                    && (resultado.pp_ic_l.equals("P")) && (resultado.pp_lo_l.equals("P"))
                    && resultado.ti.equals("MILD")) ||
                    ((resultado.sp_l.equals("F") || resultado.sp_l.equals("C")) && resultado.coord.equals("MOST")
                            && (resultado.pp_ic_l.equals("P")) && (resultado.pp_lo_l.equals("P"))
                            && resultado.ti.equals("NORMAL") && (resultado.tail.equals("DOWN") || resultado.tail.equals("UP AND DOWN")))
                    ) puntos = 8;
            else if ((resultado.sp_l.equals("F") || resultado.sp_l.equals("C")) && resultado.coord.equals("MOST")
                    && (resultado.pp_ic_l.equals("P")) && (resultado.pp_lo_l.equals("P"))
                    && resultado.ti.equals("NORMAL") && (resultado.tail.equals("UP"))) puntos = 9;
        } else if (pata.equals("R")) {
            if (resultado.am_r.equals("Ø")) puntos = 0;
            else if (resultado.am_r.equals("S")) puntos = 1;
            else if (resultado.am_r.equals("E") && resultado.pwo_r.equals("N") && resultado.pw_r.equals("N") && resultado.sd_r.equals("Ø"))
                puntos = 2;
            else if (resultado.sp_r.equals("Ø")) puntos = 3;
            else if (resultado.sp_r.equals("O")) puntos = 4;
            else if (((resultado.sp_r.equals("F") || resultado.sp_r.equals("C")) && resultado.coord.equals("Ø")) ||
                    ((resultado.sp_r.equals("F") || resultado.sp_r.equals("C")) && resultado.coord.equals("SOME")
                            && (icr == "R") && (lor == "R" || resultado.pp_lo_r.equals("P") ))
                    ) puntos = 5;
            else if (((resultado.sp_r.equals("F") || resultado.sp_r.equals("C")) &&
                    resultado.coord.equals("SOME") && resultado.pp_ic_r.equals("P") &&  (lor == "R" || resultado.pp_lo_r.equals("P") )) ||
                    ((resultado.sp_r.equals("F") || resultado.sp_r.equals("C")) && resultado.coord.equals("MOST")
                            && (icr == "R") && (lor == "R" || resultado.pp_lo_r.equals("P") )))
                puntos = 6;
            else if (((resultado.sp_r.equals("F") || resultado.sp_r.equals("C")) && resultado.coord.equals("MOST")
                    && (resultado.pp_ic_r.equals("P"))
                    && (lor == "R")) ||
                    ((resultado.sp_r.equals("F") || resultado.sp_r.equals("C")) && resultado.coord.equals("MOST")
                            && (resultado.pp_ic_r.equals("P")) && (resultado.pp_lo_r.equals("P"))
                            && resultado.ti.equals("SEVERE"))) puntos = 7;
            else if (((resultado.sp_r.equals("F") || resultado.sp_r.equals("C")) && resultado.coord.equals("MOST")
                    && (resultado.pp_ic_r.equals("P")) && (resultado.pp_lo_r.equals("P"))
                    && resultado.ti.equals("MILD")) ||
                    ((resultado.sp_r.equals("F") || resultado.sp_r.equals("C")) && resultado.coord.equals("MOST")
                            && (resultado.pp_ic_r.equals("P")) && (resultado.pp_lo_r.equals("P"))
                            && resultado.ti.equals("NORMAL") && (resultado.tail.equals("DOWN") || resultado.tail.equals("UP AND DOWN")))
                    ) puntos = 8;
            else if ((resultado.sp_r.equals("F") || resultado.sp_r.equals("C")) && resultado.coord.equals("MOST")
                    && (resultado.pp_ic_r.equals("P")) && (resultado.pp_lo_r.equals("P"))
                    && resultado.ti.equals("NORMAL") && (resultado.tail.equals("UP"))) puntos = 9;
        }

        return puntos;
    }

    public static String generarPDF(Activity activity, Resultado resultado,
                                    String animalCodigo, String animalEspecie, String animalFechaCir, String strsFecha,
                                    String strsHora, String strsTiempo, String strInvestigador1, String strsScoreL, String strsScoreR, String subScore,
                                    String animalExperimento, String animalOrigen, String estudioComentarios) throws DocumentException, FileNotFoundException {
        Font boldFont = new Font(Font.FontFamily.UNDEFINED, 12, Font.BOLD);

        Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC);

        Document document = new Document();

        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar calobj = Calendar.getInstance();
        System.out.println(df.format(calobj.getTime()));


        File storageDir = new File(activity.getApplicationInfo().dataDir + "files/temp/");
        storageDir.mkdir();
        String fileName = df.format(calobj.getTime());
        String path = activity.getApplicationInfo().dataDir + "/files/" + fileName + ".pdf";


        //String path= activity.getApplicationInfo().dataDir + "/temp/pdftemp" + df.format(calobj.getTime()) +".pdf";
        //String path= activity.getExternalFilesDir(null) + "/temp/pdftemp" + df.format(calobj.getTime()) +".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(path));
        document.setPageSize(PageSize.A4);
        document.setMargins(40, 40, 40, 40);

        document.open();
        PdfPTable cabecera = new PdfPTable(5);

        PdfPCell cellcabecera;
        cabecera.getDefaultCell().setBorder(0);
        cabecera.setWidthPercentage(100);


        cellcabecera = new PdfPCell(new Phrase("Animal:" + animalCodigo));
        cellcabecera.setColspan(2);
        cellcabecera.setBorder(0);
        cabecera.addCell(cellcabecera);

        cellcabecera = new PdfPCell(new Phrase("Especie:" + animalEspecie));
        cellcabecera.setColspan(3);
        cellcabecera.setBorder(0);
        cabecera.addCell(cellcabecera);


        cellcabecera = new PdfPCell(new Phrase("Fecha Cirugia: " + animalFechaCir));
        cellcabecera.setColspan(2);
        cellcabecera.setBorder(0);
        cabecera.addCell(cellcabecera);

        cellcabecera = new PdfPCell(new Phrase("Fecha Estudio: " + strsFecha));
        cellcabecera.setColspan(2);
        cellcabecera.setBorder(0);
        cabecera.addCell(cellcabecera);
        cabecera.addCell("T: " + strsTiempo + " dpo");

        cellcabecera = new PdfPCell(new Phrase("Investigador: " + strInvestigador1));
        cellcabecera.setColspan(5);
        cellcabecera.setBorder(0);
        cabecera.addCell(cellcabecera);

        cellcabecera = new PdfPCell(new Phrase("Experimento: " + animalExperimento));
        cellcabecera.setColspan(5);
        cellcabecera.setBorder(0);
        cabecera.addCell(cellcabecera);
        cabecera.setTableEvent(new BorderEvent());

        cabecera.setSpacingAfter(30);
        document.add(cabecera);


        PdfPTable table = new PdfPTable(4);
        // the cell object
        PdfPCell cell;
        // we add a cell with colspan 3
        cell = new PdfPCell(new Phrase(""));
        cell.setColspan(2);
        table.addCell(cell);
        table.addCell("Left");
        table.addCell("Right");


        cell = new PdfPCell(new Phrase("Ankle Movement"));
        cell.setColspan(2);
        table.addCell(cell);
        table.addCell(resultado.am_l);
        table.addCell(resultado.am_r);


        cell = new PdfPCell(new Phrase("Plantar Placement W/O Support"));
        cell.setColspan(2);
        table.addCell(cell);
        table.addCell(resultado.pwo_l);
        table.addCell(resultado.pwo_r);

        cell = new PdfPCell(new Phrase("Plantar Placement With Support"));
        cell.setColspan(2);
        table.addCell(cell);
        table.addCell(resultado.pw_l);
        table.addCell(resultado.pw_r);


        cell = new PdfPCell(new Phrase("Stepping Plantar"));
        cell.setColspan(2);
        table.addCell(cell);
        table.addCell(resultado.sp_l);
        table.addCell(resultado.sp_r);


        cell = new PdfPCell(new Phrase("Stepping Dorsal"));
        cell.setColspan(2);
        table.addCell(cell);
        table.addCell(resultado.sd_l);
        table.addCell(resultado.sd_r);


        cell = new PdfPCell(new Phrase("Coordination"));
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(resultado.coord));
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Paw position IC"));
        cell.setColspan(2);
        table.addCell(cell);

        table.addCell(resultado.pp_ic_l);
        table.addCell(resultado.pp_ic_r);

        cell = new PdfPCell(new Phrase("Paw position LO"));
        cell.setColspan(2);
        table.addCell(cell);

        table.addCell(resultado.pp_lo_l);
        table.addCell(resultado.pp_lo_r);


        cell = new PdfPCell(new Phrase("Trunk Instability"));
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(resultado.ti));
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Tail"));
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(resultado.tail));
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Score"));
        cell.setColspan(2);
        table.addCell(cell);

        table.addCell(strsScoreL);
        table.addCell(strsScoreR);


        table.setSpacingAfter(15);
        document.add(table);


        document.add(new Paragraph("Comentarios: " + estudioComentarios));

        PdfPTable cabeceraSubscore = new PdfPTable(4);
        PdfPCell cabecerasubscorecell;
        cabeceraSubscore.setSpacingBefore(15);
        cabeceraSubscore.setWidthPercentage(55);
        cabecerasubscorecell = new PdfPCell(new Phrase("SUBSCORE TALLY", boldFont));
        cabecerasubscorecell.setColspan(4);
        cabeceraSubscore.addCell(cabecerasubscorecell);
        document.add(cabeceraSubscore);

        String puntos = "";
        PdfPTable subscore = new PdfPTable(4);
        subscore.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        subscore.setWidthPercentage(55);
        //subscore.getDefaultCell().setBorder(0);

        // the cell object
        PdfPCell subscorecell;
        // we add a cell with colspan 3


        subscorecell = new PdfPCell(new Phrase("Plantar Stepping", boldFont));
        subscorecell.setColspan(3);
        subscorecell.setBorder(0);
        subscorecell.setBorderWidthRight(1);
        subscore.addCell(subscorecell);


        //subscore.addCell(new Phrase("Score", boldFont));
        subscorecell = new PdfPCell(new Phrase("Score", boldFont));
        subscorecell.setColspan(1);
        subscorecell.setBorder(0);
        subscorecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subscore.addCell(subscorecell);

        subscorecell = new PdfPCell(new Phrase(" Freq = 0; Consistent = 1"));
        subscorecell.setColspan(3);
        subscorecell.setBorder(0);
        subscorecell.setBorderWidthRight(1);
        subscore.addCell(subscorecell);
        if (resultado.sp_l.equals("C")) puntos = "1";
        else puntos = "0";
        //subscore.addCell("L     " + puntos);

        subscorecell = new PdfPCell(new Phrase("L     " + puntos));
        subscorecell.setColspan(1);
        subscorecell.setBorder(0);
        subscorecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subscore.addCell(subscorecell);

        subscorecell = new PdfPCell(new Phrase(""));
        subscorecell.setColspan(3);
        subscorecell.setBorder(0);
        subscorecell.setBorderWidthRight(1);
        subscore.addCell(subscorecell);
        if (resultado.sp_r.equals("C")) puntos = "1";
        else puntos = "0";
        subscorecell = new PdfPCell(new Phrase("R     " + puntos));
        subscorecell.setColspan(1);
        subscorecell.setBorder(0);
        subscorecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subscore.addCell(subscorecell);

        subscorecell = new PdfPCell(new Phrase("Coordination", boldFont));
        subscorecell.setColspan(3);
        subscorecell.setBorder(0);
        subscorecell.setBorderWidthRight(1);
        subscore.addCell(subscorecell);
        subscore.addCell("");

        subscorecell = new PdfPCell(new Phrase(" None = 0; Some = 1; Most = 2"));
        subscorecell.setColspan(3);
        subscorecell.setBorder(0);
        subscorecell.setBorderWidthRight(1);
        subscore.addCell(subscorecell);
        if (resultado.coord.equals("Ø")) puntos = "0";
        else if (resultado.coord.equals("SOME")) puntos = "1";
        else if (resultado.coord.equals("MOST")) puntos = "2";
        //subscore.addCell("       " + puntos);
        subscorecell = new PdfPCell(new Phrase("       " + puntos));
        subscorecell.setColspan(1);
        subscorecell.setBorder(0);
        subscorecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subscore.addCell(subscorecell);


        subscorecell = new PdfPCell(new Phrase("Paw Position", boldFont));
        subscorecell.setColspan(3);
        subscorecell.setBorder(0);
        subscorecell.setBorderWidthRight(1);
        subscore.addCell(subscorecell);
        subscore.addCell("");

        subscorecell = new PdfPCell(new Phrase(" Rotated thru out = 0"));
        subscorecell.setColspan(3);
        subscorecell.setBorder(0);
        subscorecell.setBorderWidthRight(1);
        subscore.addCell(subscorecell);
        subscore.addCell("");


        String icl="";
        String icr="";
        String lol="";
        String lor="";

        if (resultado.pp_ic_l.equals("I") || resultado.pp_ic_l.equals("E") || resultado.pp_ic_l.equals("R")) icl="R";
        if (resultado.pp_ic_r.equals("I") || resultado.pp_ic_r.equals("E") || resultado.pp_ic_r.equals("R")) icr="R";
        if (resultado.pp_lo_l.equals("I") || resultado.pp_lo_l.equals("E") || resultado.pp_lo_l.equals("R")) lol="R";
        if (resultado.pp_lo_r.equals("I") || resultado.pp_lo_r.equals("E") || resultado.pp_lo_r.equals("R")) lor="R";


        subscorecell = new PdfPCell(new Phrase(" Parallel and rotated = 1"));
        subscorecell.setColspan(3);
        subscorecell.setBorder(0);
        subscorecell.setBorderWidthRight(1);
        subscore.addCell(subscorecell);
        if (resultado.pp_ic_l.equals("P") && resultado.pp_lo_l.equals("P")) puntos = "2";
        else if ((icl.equals("R") && resultado.pp_lo_l.equals("P")) || (resultado.pp_ic_l.equals("P") && lol.equals("R")))
            puntos = "1";
        else puntos = "0";
        subscorecell = new PdfPCell(new Phrase("L     " + puntos));
        subscorecell.setColspan(1);
        subscorecell.setBorder(0);
        subscorecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subscore.addCell(subscorecell);

        subscorecell = new PdfPCell(new Phrase(" Parallel thru out = 2"));
        subscorecell.setColspan(3);
        subscorecell.setBorder(0);
        subscorecell.setBorderWidthRight(1);
        subscore.addCell(subscorecell);
        if (resultado.pp_ic_r.equals("P") && resultado.pp_lo_r.equals("P")) puntos = "2";
        else if ((icr.equals("R") && resultado.pp_lo_r.equals("P")) || (resultado.pp_ic_r.equals("P") && lor.equals("R")))
            puntos = "1";
        else puntos = "0";
        subscorecell = new PdfPCell(new Phrase("R     " + puntos));
        subscorecell.setColspan(1);
        subscorecell.setBorder(0);
        subscorecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subscore.addCell(subscorecell);


        subscorecell = new PdfPCell(new Phrase("Trunk", boldFont));
        subscorecell.setColspan(3);
        subscorecell.setBorder(0);
        subscorecell.setBorderWidthRight(1);
        subscore.addCell(subscorecell);
        subscore.addCell("");

        subscorecell = new PdfPCell(new Phrase(" Severe = 0; Mild = 1; Normal = 2"));
        subscorecell.setColspan(3);
        subscorecell.setBorder(0);
        subscorecell.setBorderWidthRight(1);
        subscore.addCell(subscorecell);
        if (resultado.ti.equals("SEVERE")) puntos = "0";
        else if (resultado.ti.equals("MILD")) puntos = "1";
        else if (resultado.ti.equals("NORMAL")) puntos = "2";
        subscorecell = new PdfPCell(new Phrase("       " + puntos));
        subscorecell.setColspan(1);
        subscorecell.setBorder(0);
        subscorecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subscore.addCell(subscorecell);


        subscorecell = new PdfPCell(new Phrase("Tail", boldFont));
        subscorecell.setColspan(3);
        subscorecell.setBorder(0);
        subscorecell.setBorderWidthRight(1);
        subscore.addCell(subscorecell);
        subscore.addCell("");

        subscorecell = new PdfPCell(new Phrase(" DOWN, UP AND DOWN = 0; UP = 1"));
        subscorecell.setColspan(3);
        subscorecell.setBorder(0);
        subscorecell.setBorderWidthRight(1);
        subscore.addCell(subscorecell);
        if (resultado.tail.equals("UP")) puntos = "1";
        else puntos = "0";
        subscorecell = new PdfPCell(new Phrase("       " + puntos));
        subscorecell.setColspan(1);
        subscorecell.setBorder(0);
        subscorecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subscore.addCell(subscorecell);


        subscorecell = new PdfPCell(new Phrase("TOTAL SUBSCORE", boldFont));
        subscorecell.setColspan(3);
        subscorecell.setBorder(0);
        subscorecell.setBorderWidthRight(1);
        subscore.addCell(subscorecell);

        subscorecell = new PdfPCell(new Phrase("       " + subScore));
        subscorecell.setColspan(1);
        subscorecell.setBorder(0);
        subscorecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subscore.addCell(subscorecell);

        subscore.setTableEvent(new BorderEvent());

        subscore.setSpacingAfter(50);
        document.add(subscore);

        PdfPTable pie = new PdfPTable(3);
        // the cell object
        PdfPCell cellpie;
        pie.getDefaultCell().setBorder(0);


        cellpie = new PdfPCell(new Phrase("ANKLE MOV'T", boldFont));
        pie.addCell(cellpie);
        cellpie = new PdfPCell(new Phrase("STEPPING", boldFont));
        pie.addCell(cellpie);
        cellpie = new PdfPCell(new Phrase("PAW POSITION", boldFont));
        pie.addCell(cellpie);


        pie.addCell("Ø - No Mov't");
        pie.addCell("Ø - None");
        pie.addCell("IC - Initial Contact");

        pie.addCell("S - Slight Mov't");
        pie.addCell("O - Ocasional");
        pie.addCell("LO - Litf off");

        pie.addCell("E - Extensive Mov't");
        pie.addCell("F - Frequent");
        pie.addCell("I - Internal Rotation");

        pie.addCell("");
        pie.addCell("C - Consistent");
        pie.addCell("E - External Rotation");

        pie.addCell("");
        pie.addCell("");
        pie.addCell("P - Parallel");

        document.add(pie);

        document.close();

        return fileName;
    }

    public static void zip(String[] files, String zipFile) throws IOException {
        BufferedInputStream origin = null;
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        try {
            byte data[] = new byte[BUFFER_SIZE];

            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
                try {
                    ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                        out.write(data, 0, count);
                    }
                } finally {
                    origin.close();
                }
            }
        } finally {
            out.close();
        }
    }

    public static void unzip(String zipFile, String location) throws IOException {
        try {
            File f = new File(location);
            if (!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
            try {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + ze.getName();

                    if (ze.isDirectory()) {
                        File unzipFile = new File(path);
                        if (!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    } else {
                        FileOutputStream fout = new FileOutputStream(path, false);
                        try {
                            for (int c = zin.read(); c != -1; c = zin.read()) {
                                fout.write(c);
                            }
                            zin.closeEntry();
                        } finally {
                            fout.close();
                        }
                    }
                }
            } finally {
                zin.close();
            }
        } catch (Exception e) {
            Log.e("", "Unzip exception", e);
        }
    }


    public static String exportToExcel(Activity activity, String codigoAnimal, String idAnimal) {

        final String fileName = "" + codigoAnimal + ".xls";

        File dirPath = new File(activity.getApplicationInfo().dataDir, "files");
        File xlsFile = new File(dirPath, "" + codigoAnimal + ".xls");

        //create directory if not exist
        if (!dirPath.isDirectory()) {
            dirPath.mkdirs();
        }

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;
        Cursor cEstudioResultado = activity.getContentResolver().query(ContratoBBDD.Animales.crearUriParaEstudiosResultados(idAnimal), null, null, null, null);
        cEstudioResultado.moveToFirst();
        try {
            workbook = Workbook.createWorkbook(xlsFile, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Animal " + codigoAnimal, 0);

            try {
                sheet.addCell(new Label(0, 0, "Code")); // column and row
                sheet.addCell(new Label(1, 0, "Date"));
                sheet.addCell(new Label(2, 0, "dpo"));
                sheet.addCell(new Label(3, 0, "BMS Score L"));
                sheet.addCell(new Label(4, 0, "BMS Score R"));
                sheet.addCell(new Label(5, 0, "Subscore"));
                sheet.addCell(new Label(6, 0, "Ankle"));
                sheet.addCell(new Label(7, 0, "Placement W"));
                sheet.addCell(new Label(8, 0, "Placement WO"));
                sheet.addCell(new Label(9, 0, "Stepping D"));
                sheet.addCell(new Label(10, 0, "Stepping P"));
                sheet.addCell(new Label(11, 0, "Coordination"));
                sheet.addCell(new Label(12, 0, "PawPosition IC"));
                sheet.addCell(new Label(13, 0, "PawPosition LO"));
                sheet.addCell(new Label(14, 0, "Trunk"));
                sheet.addCell(new Label(15, 0, "Tail"));
                sheet.addCell(new Label(16, 0, "Notes"));
                if (cEstudioResultado.moveToFirst()) {
                    do {
                        String estudioCodigo = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("ID_ESTUDIO"));
                        String estudioFecha = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("fecha"));
                        String estudioTiempo = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("tiempo"));
                        String estudioResultado = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("resultado"));
                        String estudioScoreL = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("scorel"));
                        String estudioScoreR = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("scorer"));
                        String estudioComentarios = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("comentarios")).replace("\n", ";").replace("\r", ";");;

                        String resultadoaml = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("am_l"));
                        String resultadoamr = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("am_r"));
                        String resultadopwol = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pwo_l"));
                        String resultadopwor = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pwo_r"));
                        String resultadopwl = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pw_l"));
                        String resultadopwr = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pw_r"));
                        String resultadosdl = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("sd_l"));
                        String resultadosdr = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("sd_r"));
                        String resultadospl = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("sp_l"));
                        String resultadospr = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("sp_r"));
                        String resultadoppicl = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pp_ic_l"));
                        String resultadoppicr = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pp_ic_r"));
                        String resultadopplol = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pp_lo_l"));
                        String resultadopplor = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pp_lo_r"));
                        String resultadocoor = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("coord"));
                        String resultadoti = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("ti"));
                        String resultadotail = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("tail"));


                        int i = cEstudioResultado.getPosition() + 1;
                        sheet.addCell(new Label(0, i, estudioCodigo));
                        sheet.addCell(new Label(1, i, estudioFecha));
                        sheet.addCell(new Label(2, i, estudioTiempo));
                        sheet.addCell(new Label(3, i, estudioScoreL));
                        sheet.addCell(new Label(4, i, estudioScoreR));
                        sheet.addCell(new Label(5, i, estudioResultado));
                        sheet.addCell(new Label(6, i, resultadoaml + resultadoamr));
                        sheet.addCell(new Label(7, i, resultadopwl + resultadopwr));
                        sheet.addCell(new Label(8, i, resultadopwol + resultadopwor));
                        sheet.addCell(new Label(9, i, resultadosdl + resultadosdr));
                        sheet.addCell(new Label(10, i, resultadospl + resultadospr));
                        sheet.addCell(new Label(11, i, resultadocoor));
                        sheet.addCell(new Label(12, i, resultadoppicl + resultadoppicr));
                        sheet.addCell(new Label(13, i, resultadopplol + resultadopplor));
                        sheet.addCell(new Label(14, i, resultadoti));
                        sheet.addCell(new Label(15, i, resultadotail));
                        sheet.addCell(new Label(16, i, estudioComentarios));

                    } while (cEstudioResultado.moveToNext());
                }
                //closing cursor
                cEstudioResultado.close();
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

}

/*if (pata.equals("L")) {
            if (resultado.am_l.equals("Ø")) puntos = 0;
            else if (resultado.am_l.equals("S")) puntos = 1;
            else if (resultado.am_l.equals("E") && resultado.pwo_l.equals("N") && resultado.pw_l.equals("N") && resultado.sd_l.equals("Ø"))
                puntos = 2;
            else if (resultado.sp_l.equals("Ø")) puntos = 3;
            else if (resultado.sp_l.equals("O")) puntos = 4;
            else if (((resultado.sp_l.equals("F") || resultado.sp_l.equals("C")) && resultado.coord.equals("Ø")) ||
                    ((resultado.sp_l.equals("F") || resultado.sp_l.equals("C")) && resultado.coord.equals("SOME")
                            && (resultado.pp_ic_l.equals("I") || resultado.pp_ic_l.equals("E"))
                            && (resultado.pp_lo_l.equals("I") || resultado.pp_lo_l.equals("E")))
                    ) puntos = 5;
            else if (((resultado.sp_l.equals("F") || resultado.sp_l.equals("C")) &&
                    resultado.coord.equals("SOME") && resultado.pp_ic_l.equals("P")) ||
                    ((resultado.sp_l.equals("F") || resultado.sp_l.equals("C")) && resultado.coord.equals("MOST")
                            && (resultado.pp_ic_l.equals("I") || resultado.pp_ic_l.equals("E"))
                            && (resultado.pp_lo_l.equals("I") || resultado.pp_lo_l.equals("E"))))
                puntos = 6;
            else if (((resultado.sp_l.equals("F") || resultado.sp_l.equals("C")) && resultado.coord.equals("MOST")
                    && (resultado.pp_ic_l.equals("P"))
                    && (resultado.pp_lo_l.equals("I") || resultado.pp_lo_l.equals("E"))) ||
                    ((resultado.sp_l.equals("F") || resultado.sp_l.equals("C")) && resultado.coord.equals("MOST")
                            && (resultado.pp_ic_l.equals("P")) && (resultado.pp_lo_l.equals("P"))
                            && resultado.ti.equals("SEVERE"))) puntos = 7;
            else if (((resultado.sp_l.equals("F") || resultado.sp_l.equals("C")) && resultado.coord.equals("MOST")
                    && (resultado.pp_ic_l.equals("P")) && (resultado.pp_lo_l.equals("P"))
                    && resultado.ti.equals("MILD")) ||
                    ((resultado.sp_l.equals("F") || resultado.sp_l.equals("C")) && resultado.coord.equals("MOST")
                            && (resultado.pp_ic_l.equals("P")) && (resultado.pp_lo_l.equals("P"))
                            && resultado.ti.equals("NORMAL") && (resultado.tail.equals("DOWN") || resultado.tail.equals("UP AND DOWN")))
                    ) puntos = 8;
            else if ((resultado.sp_l.equals("F") || resultado.sp_l.equals("C")) && resultado.coord.equals("MOST")
                    && (resultado.pp_ic_l.equals("P")) && (resultado.pp_lo_l.equals("P"))
                    && resultado.ti.equals("NORMAL") && (resultado.tail.equals("UP"))) puntos = 9;
        } else if (pata.equals("R")) {
            if (resultado.am_r.equals("Ø")) puntos = 0;
            else if (resultado.am_r.equals("S")) puntos = 1;
            else if (resultado.am_r.equals("E") && resultado.pwo_r.equals("N") && resultado.pw_r.equals("N") && resultado.sd_r.equals("Ø"))
                puntos = 2;
            else if (resultado.sp_r.equals("Ø")) puntos = 3;
            else if (resultado.sp_r.equals("O")) puntos = 4;
            else if (((resultado.sp_r.equals("F") || resultado.sp_r.equals("C")) && resultado.coord.equals("Ø")) ||
                    ((resultado.sp_r.equals("F") || resultado.sp_r.equals("C")) && resultado.coord.equals("SOME")
                            && (resultado.pp_ic_r.equals("I") || resultado.pp_ic_r.equals("E"))
                            && (resultado.pp_lo_r.equals("I") || resultado.pp_lo_r.equals("E")))
                    ) puntos = 5;
            else if (((resultado.sp_r.equals("F") || resultado.sp_r.equals("C")) &&
                    resultado.coord.equals("SOME") && resultado.pp_ic_r.equals("P")) ||
                    ((resultado.sp_r.equals("F") || resultado.sp_r.equals("C")) && resultado.coord.equals("MOST")
                            && (resultado.pp_ic_r.equals("I") || resultado.pp_ic_r.equals("E"))
                            && (resultado.pp_lo_r.equals("I") || resultado.pp_lo_r.equals("E"))))
                puntos = 6;
            else if (((resultado.sp_r.equals("F") || resultado.sp_r.equals("C")) && resultado.coord.equals("MOST")
                    && (resultado.pp_ic_r.equals("P"))
                    && (resultado.pp_lo_r.equals("I") || resultado.pp_lo_r.equals("E"))) ||
                    ((resultado.sp_r.equals("F") || resultado.sp_r.equals("C")) && resultado.coord.equals("MOST")
                            && (resultado.pp_ic_r.equals("P")) && (resultado.pp_lo_r.equals("P"))
                            && resultado.ti.equals("SEVERE"))) puntos = 7;
            else if (((resultado.sp_r.equals("F") || resultado.sp_r.equals("C")) && resultado.coord.equals("MOST")
                    && (resultado.pp_ic_r.equals("P")) && (resultado.pp_lo_r.equals("P"))
                    && resultado.ti.equals("MILD")) ||
                    ((resultado.sp_r.equals("F") || resultado.sp_r.equals("C")) && resultado.coord.equals("MOST")
                            && (resultado.pp_ic_r.equals("P")) && (resultado.pp_lo_r.equals("P"))
                            && resultado.ti.equals("NORMAL") && (resultado.tail.equals("DOWN") || resultado.tail.equals("UP AND DOWN")))
                    ) puntos = 8;
            else if ((resultado.sp_r.equals("F") || resultado.sp_r.equals("C")) && resultado.coord.equals("MOST")
                    && (resultado.pp_ic_r.equals("P")) && (resultado.pp_lo_r.equals("P"))
                    && resultado.ti.equals("NORMAL") && (resultado.tail.equals("UP"))) puntos = 9;
        }*/