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

import dev.a2.estore.model.CompanyInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * This class provides implementation for EmailService interface.
 *
 * @author Andrei Sidorov
 */
@Service
public class EmailServiceImpl implements EmailService {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(EmailService.class);

    /**
     * Injects JavaMailSender.
     */
    @Autowired
    private JavaMailSender emailSender;

    /**
     * Injects CompanyInfo.
     */
    @Autowired
    private CompanyInfo companyInfo;

    @Async
    @Override
    public void sendInvoice(final String to,
                            final String subject,
                            final File invoicePdfFile) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = null;
        helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("Dear customer,\n" +
                "\n" +
                "Thank you for your purchase.\n" +
                "\n" +
                "Attached you will find the invoice.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Best regards,\n" +
                "\n" +
                companyInfo.getName() + "\n" +
                companyInfo.getHouse() + " " + companyInfo.getStreet() + "\n" +
                companyInfo.getCity() + ", " + companyInfo.getCountry() + "\n" +
                "Phone: " + companyInfo.getPhoneNumber() + "\n" +
                "Fax: " + companyInfo.getFaxNumber() + "\n" +
                companyInfo.getWebsite());
        FileSystemResource file = new FileSystemResource(invoicePdfFile);

        helper.addAttachment("invoice.pdf", file);
        emailSender.send(message);
    }

}
