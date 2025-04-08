package com.sintaks.mushandi.service;


import com.sintaks.mushandi.exceptions.UnexpectedException;
import com.sintaks.mushandi.model.projections.MemberView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

@Slf4j
@Component
public class CSVWriter {
    public byte[] convertListToByteArray(List<MemberView> items, String header) {

    try (ByteArrayOutputStream output = new ByteArrayOutputStream();
         CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), CSVFormat.DEFAULT)) {

            printer.printRecord(header.toUpperCase());//put headers data

        printer.printRecord("Date Recruited","Surname","Firstname","Gender","DOB","NationalId","CellNumber","Email","Town","Branch", "Grade","Status","Institution","Weekly Deduction" );//put headers data

            printer.printRecord();//put headers data
            for (MemberView report : items) {

                printer.printRecord(report.getDateRecruited(),report.getSurname(),report.getFirstname(),report.getGender(),report.getDob(),report.getNationalId(),report.getCellNumber(),
                        report.getEmail(),report.getTown(),report.getBranch(),report.getGrade(),report.getStatus(),report.getInstitution(),report.getWeeklyDeduction());
            }
            printer.flush();


            return output.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new UnexpectedException("Error processing CSV");
        }
    }



}
