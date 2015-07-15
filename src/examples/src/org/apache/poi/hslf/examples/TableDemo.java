/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.hslf.examples;

import org.apache.poi.hslf.usermodel.*;
import org.apache.poi.hslf.model.*;
import org.apache.poi.sl.usermodel.TextParagraph.TextAlign;
import org.apache.poi.sl.usermodel.VerticalAlignment;

import java.awt.*;
import java.io.FileOutputStream;

/**
 * Demonstrates how to create tables
 *
 * @author Yegor Kozlov
 */
public final class TableDemo {

    public static void main(String[] args) throws Exception {

        //test data for the first taable
        String[][] txt1 = {
            {"INPUT FILE", "NUMBER OF RECORDS"},
            {"Item File", "11,559"},
            {"Vendor File", "502"},
            {"Purchase History File - # of PO\u2019s\r(12/01/04 - 05/31/06)", "12,852"},
            {"Purchase History File - # of PO Lines\r(12/01/04 - 05/31/06)", "53,523" },
            {"Total PO History Spend", "$10,172,038"}
        };

        HSLFSlideShow ppt = new HSLFSlideShow();

        HSLFSlide slide = ppt.createSlide();

        //six rows, two columns
        HSLFTable table1 = new HSLFTable(6, 2);
        for (int i = 0; i < txt1.length; i++) {
            for (int j = 0; j < txt1[i].length; j++) {
                HSLFTableCell cell = table1.getCell(i, j);
                HSLFTextRun rt = cell.getTextParagraphs().get(0).getTextRuns().get(0);
                rt.setFontFamily("Arial");
                rt.setFontSize(10d);
                if(i == 0){
                    cell.getFill().setForegroundColor(new Color(227, 227, 227));
                } else {
                    rt.setBold(true);
                }
                cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
                cell.setHorizontalCentered(true);
                cell.setText(txt1[i][j]);
            }
        }

        Line border1 = table1.createBorder();
        border1.setLineColor(Color.black);
        border1.setLineWidth(1.0);
        table1.setAllBorders(border1);

        table1.setColumnWidth(0, 300);
        table1.setColumnWidth(1, 150);

        slide.addShape(table1);
        int pgWidth = ppt.getPageSize().width;
        table1.moveTo((pgWidth - table1.getAnchor().width)/2, 100);

        //test data for the second taable
        String[][] txt2 = {
            {"Data Source"},
            {"CAS Internal Metrics - Item Master Summary\r" +
             "CAS Internal Metrics - Vendor Summary\r" +
             "CAS Internal Metrics - PO History Summary"}
        };

        //two rows, one column
        HSLFTable table2 = new HSLFTable(2, 1);
        for (int i = 0; i < txt2.length; i++) {
            for (int j = 0; j < txt2[i].length; j++) {
                HSLFTableCell cell = table2.getCell(i, j);
                HSLFTextRun rt = cell.getTextParagraphs().get(0).getTextRuns().get(0);
                rt.setFontSize(10d);
                rt.setFontFamily("Arial");
                if(i == 0){
                    cell.getFill().setForegroundColor(new Color(0, 51, 102));
                    rt.setFontColor(Color.white);
                    rt.setBold(true);
                    rt.setFontSize(14d);
                    cell.setHorizontalCentered(true);
                } else {
                    rt.getTextParagraph().setBullet(true);
                    rt.setFontSize(12d);
                    rt.getTextParagraph().setAlignment(TextAlign.LEFT);
                    cell.setHorizontalCentered(false);
                }
                cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
                cell.setText(txt2[i][j]);
            }
        }
        table2.setColumnWidth(0, 300);
        table2.setRowHeight(0, 30);
        table2.setRowHeight(1, 70);

        Line border2 = table2.createBorder();
        table2.setOutsideBorders(border2);

        slide.addShape(table2);
        table2.moveTo(200, 400);

        FileOutputStream out = new FileOutputStream("hslf-table.ppt");
        ppt.write(out);
        out.close();

    }
}
