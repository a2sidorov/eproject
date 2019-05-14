/*
 * MIT License
 *
 * Copyright (c) 2019 Andrei Sidorov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package dev.a2.estore.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dev.a2.estore.model.Address;
import dev.a2.estore.model.CompanyInfo;
import dev.a2.estore.model.Order;
import dev.a2.estore.model.PaymentStatus;
import dev.a2.estore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;

/**
 * This class provides implementation for PdfService interface.
 *
 * @author Andrei Sidorov
 */
@Service
public class PdfServiceImpl implements PdfService {

    /**
     * Injects CompanyInfo.
     */
    @Autowired
    private CompanyInfo companyInfo;

    /**
     * Currency symbol for an invoice.
     */
    private final String currencySymbol = "$";

    /**
     * The font for the word 'invoice'.
     */
    private static Font invoiceFont = new Font(FontFactory
            .getFont("Arial", 38, Font.NORMAL));

    /**
     * The font for titles.
     */
    private static Font titleFont = new Font(FontFactory
            .getFont("Arial", 16, Font.NORMAL));

    /**
     * The grey font for titles.
     */
    private static Font greyFont = new Font(FontFactory
            .getFont("Arial", 10, new BaseColor(128, 128, 128)));

    /**
     * The main font.
     */
    private static Font mainFont = new Font(FontFactory
            .getFont("Arial", 10, Font.NORMAL));

    /**
     * The font for the total price of an invoice.
     */
    private static Font totalPriceFont = new Font(FontFactory
            .getFont("Arial", 24, new BaseColor(80, 123, 218)));

    @Override
    public File createInvoice(final Order order) throws Exception {

        User user = order.getUser();
        Address address = user.getAddresses().get(0);

        // Creating the document
        Document document = new Document();
        File file = File.createTempFile("invoice", ".pdf");
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        PdfContentByte pdfContentByte = pdfWriter.getDirectContent();

        // Adding the title
        Paragraph titleParagraph = new Paragraph("INVOICE", invoiceFont);
        titleParagraph.setIndentationLeft(32f);
        document.add(titleParagraph);

        // Adding the barcode
        String format = String.format("%%0%dd", 13);
        String code = String.format(format, order.getId());
        BarcodeEAN barcode = new BarcodeEAN();
        barcode.setCodeType(Barcode.EAN13);
        barcode.setCode(code);
        Image image = barcode.createImageWithBarcode(pdfContentByte, null, null);
        image.setAbsolutePosition(500, 800);
        document.add(image);

        // Adding a blank line
        document.add(new Paragraph("\n"));

        // Adding the invoice number and the date of issue
        PdfPTable invoiceNumberDateTable = new PdfPTable(2);
        invoiceNumberDateTable.setWidthPercentage(50);
        invoiceNumberDateTable.addCell(getCell("INVOICE NUMBER", greyFont));
        invoiceNumberDateTable.addCell(getCell("DATE OF ISSUE", greyFont));
        invoiceNumberDateTable.addCell(getCell(order.getId().toString(), mainFont));
        invoiceNumberDateTable.addCell(getCell(LocalDate.now().toString(), mainFont));
        invoiceNumberDateTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        document.add(invoiceNumberDateTable);

        // Creating table for the buyer and seller information
        PdfPTable buyerSellerTable = new PdfPTable(2);
        buyerSellerTable.setWidthPercentage(100);

        // Addding the buyer information to the buyer seller table
        PdfPTable buyerInfoTable = new PdfPTable(1);
        buyerInfoTable.setWidthPercentage(50);
        buyerInfoTable.addCell(getCell(" ", greyFont));
        buyerInfoTable.addCell(getCell("BILLED TO", greyFont));
        buyerInfoTable.addCell(getCell(user.getFirstName() + " " +
                                       user.getLastName(),
                                       mainFont));
        buyerInfoTable.addCell(getCell(address.getHouse() + " " +
                                        address.getStreet() + " Apt. " +
                                        address.getApartment(), mainFont));
        buyerInfoTable.addCell(getCell(address.getCity() + ", " + address.getCountry().getName(), mainFont));
        buyerInfoTable.addCell(getCell(address.getPostalCode(), mainFont));
        buyerSellerTable.addCell(getCell(buyerInfoTable, 0f));

        // Adding the seller infomation to the buyer seller table
        PdfPTable sellerInfoTable = new PdfPTable(1);
        sellerInfoTable.setWidthPercentage(50);
        sellerInfoTable.addCell(getCell(companyInfo.getName(), titleFont));
        sellerInfoTable.addCell(getCell(companyInfo.getHouse() + " " +
                                        companyInfo.getStreet(), mainFont));
        sellerInfoTable.addCell(getCell(companyInfo.getCity() + ", " +
                                        companyInfo.getCountry() + ", " +
                                        companyInfo.getPostalCode(), mainFont));
        sellerInfoTable.addCell(getCell(companyInfo.getPhoneNumber(), mainFont));
        sellerInfoTable.addCell(getCell(companyInfo.getEmail(), mainFont));
        sellerInfoTable.addCell(getCell(companyInfo.getWebsite(), mainFont));
        buyerSellerTable.addCell(getCell(sellerInfoTable, 60f));

        // Adding the buyer seller table
        document.add(buyerSellerTable);

        // Adding a blank line
        document.add(new Paragraph("\n"));

        // Creating the main table.
        float[] columnWidths = {9, 3, 2, 2};
        PdfPTable mainTable = new PdfPTable(columnWidths);
        mainTable.setWidthPercentage(100);

        // Adding the header to the main table
        mainTable.addCell(getHeaderCell("DESCRIPTION", greyFont));
        mainTable.addCell(getHeaderCell("UNIT COST", greyFont));
        mainTable.addCell(getHeaderCell("QTY", greyFont));
        mainTable.addCell(getHeaderCell("AMOUNT", greyFont));

        // Adding rows to main table
        order.getOrderProducts().forEach(orderProduct -> {
            mainTable.addCell(getMainCell(orderProduct.getProduct().getName(), mainFont));
            mainTable.addCell(getMainCell(currencySymbol + orderProduct.getSellingPrice().toString(), mainFont));
            mainTable.addCell(getMainCell(orderProduct.getQuantity().toString(), mainFont));
            mainTable.addCell(getMainCell(currencySymbol + orderProduct.calculateTotalSellingPrice().toString(),
                    mainFont));
        });

        // Adding the total price cell
        mainTable.addCell(getCell(" ", mainFont));
        mainTable.addCell(getCell(" ", mainFont));
        mainTable.addCell(getCell("TOTAL", mainFont, PdfPCell.ALIGN_RIGHT));
        mainTable.addCell(getCell(currencySymbol + order.getTotalSellingPrice(), mainFont));
        document.add(mainTable);

        // Adding a blank line
        document.add(new Paragraph("\n"));

        // Add invoice total price
        PdfPTable invoiceTotalTable = new PdfPTable(1);
        invoiceTotalTable.setWidthPercentage(50);
        invoiceTotalTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        invoiceTotalTable.addCell(getCell("INVOICE TOTAL", greyFont));
        invoiceTotalTable.addCell(getCell(currencySymbol + order.getTotalSellingPrice(),
                totalPriceFont));
        document.add(invoiceTotalTable);

        // Add watermark PAID if order has been paid.
        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            PdfContentByte canvas = pdfWriter.getDirectContentUnder();
            canvas.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, false), 200);
            canvas.setColorFill(BaseColor.PINK);
            canvas.beginText();
            canvas.showTextAligned(Element.ALIGN_CENTER, "PAID", 250f, 320f, -45);
            canvas.endText();
            canvas.stroke();
        }
        document.close();
        return file;
    }

    /**
     * Creates a table cell for text.
     *
     * @param text the text in a cell.
     * @param font the font of the text in a cell.
     * @return a table cell.
     */
    private PdfPCell getCell(final String text, final Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(3);
        cell.setBorderWidthBottom(1);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    /**
     * Creates a table cell for text.
     *
     * @param text the text in a cell.
     * @param font the font of the text in a cell.
     * @param alignment the horizontal alignment of the text in a cell.
     * @return a table cell.
     */
    private PdfPCell getCell(final String text, final Font font, final int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(3);
        cell.setHorizontalAlignment(alignment);
        cell.setBorderWidthBottom(1);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    /**
     * Creates a table cell for another table.
     *
     * @param table the that needs to be nested in a cell.
     * @param paddingLeft the left padding of a nested table.
     * @return a cell with a nested table.
     */
    private PdfPCell getCell(final PdfPTable table, final float paddingLeft) {
        PdfPCell cell = new PdfPCell(table);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPaddingLeft(paddingLeft);
        return cell;
    }

    /**
     * Creates a table header cell.
     *
     * @param text the text in a cell.
     * @param font the font of the text in a cell.
     * @return a table header cell.
     */
    private PdfPCell getHeaderCell(final String text, final Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPaddingBottom(4);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderWidthBottom(1);
        cell.setBorderColor(BaseColor.BLACK);
        return cell;
    }

    /**
     * Creates a cell of a main table..
     *
     * @param text the text in a cell.
     * @param font the font of the text in a cell.
     * @return a main table cell.
     */
    private PdfPCell getMainCell(final String text, final Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, mainFont));
        cell.setPaddingBottom(3f);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(BaseColor.BLACK);
        return cell;
    }

}
