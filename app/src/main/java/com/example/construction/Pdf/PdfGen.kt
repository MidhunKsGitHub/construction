package com.example.construction.Config

import com.example.construction.Model.Report.PageData
import com.example.construction.Model.SuperVisorReport.Reportsupervisor
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import java.io.IOException

object PdfGen {
    fun generatePdf(itemList: ArrayList<Reportsupervisor>, filePath: String?) {
        val document = Document()
        try {
            PdfWriter.getInstance(document, FileOutputStream(filePath))
            document.open()
            val table = PdfPTable(3) // 2 columns// 2 columns
            // Add table headers
            table.addCell("Name")
            table.addCell("Date")
            table.addCell("Time")


            // Add data rows
            for (item: Reportsupervisor in itemList) {
                table.addCell(item.name)
                table.addCell(item.date)
                table.addCell(item.time)
            }
            document.add(table)
            document.close()
        } catch (e: DocumentException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun generatePdfEmp(itemList: ArrayList<PageData>, filePath: String?) {
        val document = Document()
        try {
            PdfWriter.getInstance(document, FileOutputStream(filePath))
            document.open()
            val table = PdfPTable(2) // 2 columns// 2 columns
            // Add table headers
            table.addCell("Date")
            table.addCell("Time")


            // Add data rows
            for (item: PageData in itemList) {
                table.addCell(item.checkin_date)
                table.addCell(item.checkin_time)
            }
            document.add(table)
            document.close()
        } catch (e: DocumentException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}