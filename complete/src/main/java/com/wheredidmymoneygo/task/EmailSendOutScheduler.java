package com.wheredidmymoneygo.task;

import com.wheredidmymoneygo.dto.CategoryDetail;
import com.wheredidmymoneygo.dto.Expense;
import com.wheredidmymoneygo.dto.ExpenseStatitic;
import com.wheredidmymoneygo.service.ExpenseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class EmailSendOutScheduler {

    private ExpenseService expenseService;

    private JavaMailSender javaMailSender;

    //@Scheduled(cron = "*/30 * * * * *") //TEST
    @Scheduled(cron = "0 0 0 1 * *")
    public void sendMonthyReport() throws MessagingException {
        Instant from = Instant.now();

        LocalDate localDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        Instant till = localDate.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC);
        ExpenseStatitic expenseStatitic = expenseService.getExpensesStatitic(from, till);

        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        //TODO Set your email
        helper.setTo("example@web.de");
        helper.setSubject("Monthly Expense report");

        StringBuilder textBuilder = new StringBuilder();
        buildHtmlTable(expenseStatitic, textBuilder);
        buildHtmlTable(expenseStatitic.getAllExpenses(), textBuilder);
        helper.setText(textBuilder.toString(), true);

        javaMailSender.send(msg);

        log.info("SEND OUT at nanoTime: " + System.nanoTime());
    }

    private void buildHtmlTable(ExpenseStatitic expenseStatitic, StringBuilder textBuilder) {
        textBuilder.append("<h2>Categories</h2>" +
                "<table style=\"width:100%\">\n" +
                "          <tr>\n" +
                "            <th>" + "name" + "</th>\n" +
                "            <th>" + "amount" + "</th>\n" +
                "            <th>" + "percentage of total" + "</th>\n" +
                "          </tr>");


        for (CategoryDetail categoryDetail : expenseStatitic.getCategoryDetails()) {
            textBuilder.append("<tr>");
            textBuilder.append("<td>").append(categoryDetail.getCategoryName()).append("</td>");
            textBuilder.append("<td>").append(categoryDetail.getValue()).append("</td>");
            textBuilder.append("<td>").append(categoryDetail.getPercentageOfTotal()).append("</td>");
            textBuilder.append("</tr>");
        }
        textBuilder.append("</table>");
    }

    private void buildHtmlTable(List<Expense> expenses, StringBuilder textBuilder) {
        textBuilder.append("<h2>All Expenses</h2>" +
                "<table style=\"width:100%\">\n" +
                "          <tr>\n" +
                "            <th>" + "category" + "</th>\n" +
                "            <th>" + "wayMoneySpent" + "</th>\n" +
                "            <th>" + "amount" + "</th>\n" +
                "          </tr>");


        for (Expense expense : expenses) {
            textBuilder.append("<tr>");
            textBuilder.append("<td>").append(expense.getCategory()).append("</td>");
            textBuilder.append("<td>").append(expense.getWayMoneySpent()).append("</td>");
            textBuilder.append("<td>").append(expense.getValue()).append("</td>");
            textBuilder.append("</tr>");
        }
        textBuilder.append("</table>");
    }
}
