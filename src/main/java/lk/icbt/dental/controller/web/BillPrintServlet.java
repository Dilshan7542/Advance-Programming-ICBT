package lk.icbt.dental.controller.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.icbt.dental.model.dto.BillView;
import lk.icbt.dental.model.service.ClinicFacade;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/billing/print")
public class BillPrintServlet extends HttpServlet {
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final ClinicFacade clinic = ClinicFacade.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int appointmentId = Integer.parseInt(request.getParameter("id"));
            BillView bill = clinic.generateBill(appointmentId);

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                    float y = 790;
                    y = write(content, PDType1Font.HELVETICA_BOLD, 18, 70, y,
                            "SUNRISE DENTAL CLINIC");
                    y = write(content, PDType1Font.HELVETICA, 11, 70, y,
                            "Patient Bill / Receipt");
                    y -= 10;
                    y = line(content, y);
                    y = write(content, PDType1Font.HELVETICA, 11, 70, y,
                            "Appointment No: " + bill.getAppointmentNo());
                    y = write(content, PDType1Font.HELVETICA, 11, 70, y,
                            "Patient: " + bill.getPatientName());
                    y = write(content, PDType1Font.HELVETICA, 11, 70, y,
                            "Contact: " + bill.getContactNumber());
                    y = write(content, PDType1Font.HELVETICA, 11, 70, y,
                            "Dentist: " + bill.getDentistName());
                    y = write(content, PDType1Font.HELVETICA, 11, 70, y,
                            "Treatment: " + bill.getTreatmentName());
                    y = write(content, PDType1Font.HELVETICA, 11, 70, y,
                            "Appointment: " + bill.getAppointmentDate() + " " + bill.getAppointmentTime());
                    y -= 10;
                    y = line(content, y);
                    y = write(content, PDType1Font.HELVETICA, 11, 70, y,
                            "Consultation Fee (LKR): " + bill.getConsultationFee());
                    y = write(content, PDType1Font.HELVETICA, 11, 70, y,
                            "Treatment Fee (LKR): " + bill.getTreatmentFee());
                    y = write(content, PDType1Font.HELVETICA_BOLD, 13, 70, y,
                            "Total Amount (LKR): " + bill.getTotalAmount());
                    y = write(content, PDType1Font.HELVETICA_BOLD, 11, 70, y,
                            "Payment Status: " + bill.getStatus());
                    if (bill.getPaidAt() != null) {
                        y = write(content, PDType1Font.HELVETICA, 11, 70, y,
                                "Paid At: " + bill.getPaidAt().format(DATE_TIME));
                    }
                    y -= 20;
                    write(content, PDType1Font.HELVETICA_OBLIQUE, 9, 70, y,
                            "Generated at " + LocalDateTime.now().format(DATE_TIME));
                }

                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition",
                        "inline; filename=clinic-bill-" + bill.getAppointmentNo() + ".pdf");
                document.save(response.getOutputStream());
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unable to generate bill PDF.");
        }
    }

    private float write(PDPageContentStream content, PDType1Font font, float size,
                        float x, float y, String text) throws IOException {
        content.beginText();
        content.setFont(font, size);
        content.newLineAtOffset(x, y);
        content.showText(sanitize(text));
        content.endText();
        return y - 22;
    }

    private float line(PDPageContentStream content, float y) throws IOException {
        content.moveTo(70, y);
        content.lineTo(525, y);
        content.stroke();
        return y - 24;
    }

    private String sanitize(String value) {
        return value == null ? "" : value.replaceAll("[^\\x20-\\x7E]", "?");
    }
}
