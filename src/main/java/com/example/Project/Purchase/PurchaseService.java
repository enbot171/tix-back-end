package com.example.Project.Purchase;

import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Project.Event.Event;
import com.example.Project.Event.EventRepository;
import com.example.Project.Ticket.Ticket;
import com.example.Project.Ticket.TicketRepository;
import com.example.Project.User.User;
import com.example.Project.User.UserRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class PurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepo;
    @Autowired
    private TicketRepository tickets;
    @Autowired
    private UserRepository users;
    @Autowired
    private EventRepository events;

    public void savePurchase(Purchase purchase) {
        purchaseRepo.save(purchase);
    }

    public List<Purchase> findAll() {
        return purchaseRepo.findAll();
    }

    public Purchase findSinglePurchase(String purchaseId) {
        return purchaseRepo.findById(purchaseId).get();
    }

    public boolean existsById(String purchaseId) {
        return purchaseRepo.existsById(purchaseId);
    }

    public void deletePurchase(Purchase purchase) {
        purchaseRepo.delete(purchase);
    }

    public List<Purchase> findByUserId(String userId) {
        List<Purchase> allPurchases = purchaseRepo.findAll();
        List<Purchase> allPurchasesByUser = new ArrayList<>();

        for (Purchase p : allPurchases) {
            if (p.getUserId().equals(userId)) {
                allPurchasesByUser.add(p);
            }
        }
        return allPurchasesByUser;
    }

    public Purchase findByEventNameDateAndUserId(Event event, String userId) {
        List<Purchase> allPurchasesByUser = findByUserId(userId);
        for (Purchase p : allPurchasesByUser) {
            Ticket t = tickets.findById(p.getTicketId()).get();
            if (t.getEventId().equals(event.getId())) {
                return p;
            }
        }
        return null;
    }

    public Purchase findByTicketId(String ticketId) {
        return purchaseRepo.findByTicketId(ticketId);
    }

    public void export(HttpServletResponse response, String purchaseID) throws IOException, WriterException {
        Purchase purchase = purchaseRepo.findById(purchaseID).get();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(30);

        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontHeader.setSize(20);

        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(14);

        Paragraph title = new Paragraph("E-TICKET", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        Paragraph space = new Paragraph(" ", fontParagraph);
        document.add(space);

        Paragraph header = new Paragraph("Event Information", fontHeader);
        header.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(header);

        String ticketID = purchase.getTicketId();
        Ticket ticket = tickets.findById(ticketID).get();
        Event event = events.findById(ticket.getEventId()).get();
        Paragraph body = new Paragraph("Event Name: " + event.getName(), fontParagraph);
        header.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(body);

        body = new Paragraph("Event Date: " + event.getDate(), fontParagraph);
        header.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(body);

        document.add(space);

        header = new Paragraph("Ticket Information", fontHeader);
        header.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(header);

        body = new Paragraph("Ticket ID: " + ticketID, fontParagraph);
        header.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(body);

        body = new Paragraph("Ticket Category: " + ticket.getCategory(), fontParagraph);
        header.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(body);

        body = new Paragraph("Seat Number: " + ticket.getSeatNum(), fontParagraph);
        header.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(body);

        body = new Paragraph("Price: $" + ticket.getPrice(), fontParagraph);
        header.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(body);

        document.add(space);

        header = new Paragraph("Purchaser", fontHeader);
        header.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(header);

        String userID = purchase.getUserId();
        User user = users.findById(userID).get();
        body = new Paragraph("Full Name: " + user.getFullname(), fontParagraph);
        body.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(body);

        body = new Paragraph("Email: " + user.getEmail(), fontParagraph);
        body.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(body);

        body = new Paragraph("Mobile: " + user.getMobile(), fontParagraph);
        body.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(body);

        byte[] pngData = getQRCode(purchaseID, 0, 0);
        Image image = Image.getInstance(pngData);
        image.scaleAbsolute(170f, 170f);
        image.setAlignment(Image.ALIGN_CENTER);
        document.add(image);

        document.close();
    }

    public byte[] getQRCode(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageConfig con = new MatrixToImageConfig(0xFF000002, 0xFF04B4AE);
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream, con);
        byte[] pngData = pngOutputStream.toByteArray();

        return pngData;
    }
}
