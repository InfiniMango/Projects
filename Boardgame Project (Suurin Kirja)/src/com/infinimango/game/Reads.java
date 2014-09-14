package com.infinimango.game;

import java.io.File;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class Reads {
	String str;

	Reads() {
		try {
			File f = new File(
					"C:/Users/Miko/Dropbox/Java project/pakattuna/2011.02.19 Raamattu-trivian kysymyksiä.xls");
			Workbook wb = Workbook.getWorkbook(f);
			Sheet s = wb.getSheet(1);
			int row = s.getRows();
			int col = s.getColumns();

			Cell c = s.getCell(2, 6);
			str = c.getContents();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	String getContent() {
		return str;
	}

}
