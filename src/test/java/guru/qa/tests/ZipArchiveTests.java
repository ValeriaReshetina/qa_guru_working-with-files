package guru.qa.tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.pdftest.assertj.Assertions.assertThat;

public class ZipArchiveTests {
    private static ClassLoader classLoader = ZipArchiveTests.class.getClassLoader();

    private static String zipArchive = "zipForTests.zip";
    private static String pdfFileName = "contacts.pdf";
    private static String csvFileName = "contacts.csv";
    private static String xlsxFileName = "contacts.xlsx";
    private static String filesExpectedContent = "<list>";

    private InputStream getInputStreamForFileFromZip(String fileName) throws IOException {
        ZipInputStream is = new ZipInputStream(classLoader.getResourceAsStream(zipArchive));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {
            if (entry.getName().equals(fileName)) {
                return is;
            }
        }
        throw new IOException("File " + fileName + " is not found in the Zip-archive");
    }

    @Test
    @DisplayName("Test checks that a pdf-file starts with the correct line")
    void findContentInPdfInZip() throws IOException {
        try (InputStream inputStream = getInputStreamForFileFromZip(pdfFileName)) {
            PDF pdf = new PDF(inputStream);
            String pdfText = pdf.text;
            assertThat(pdfText).startsWith(filesExpectedContent);
        }
    }

    @Test
    @DisplayName("Test checks that a xlsx-file starts with the correct line")
    void findContentInXlsInZip() throws IOException {
        try (InputStream inputStream = getInputStreamForFileFromZip(xlsxFileName)) {
            XLS xls = new XLS(inputStream);
            String firstCellValue = xls.excel.getSheetAt(0).getRow(0)
                    .getCell(0).getStringCellValue();
            assertThat(firstCellValue).contains(filesExpectedContent);
        }
    }

    @Test
    @DisplayName("Test checks that a csv-file starts with the correct line")
    void findContentInCsvInZip() throws Exception {
        try (InputStream inputStream = getInputStreamForFileFromZip(csvFileName);
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String firstValue = reader.readNext()[0];
            assertThat(firstValue).contains(filesExpectedContent);
        }
    }
}
