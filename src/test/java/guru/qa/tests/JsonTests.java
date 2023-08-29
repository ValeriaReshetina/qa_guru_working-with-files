package guru.qa.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.model.JsonModel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static com.codeborne.pdftest.assertj.Assertions.assertThat;

public class JsonTests {

    private static String jsonFile = "contacts.json";
    private static ClassLoader classLoader = ZipArchiveTests.class.getClassLoader();
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void jsonTest() throws IOException {
        try (InputStream stream = classLoader.getResourceAsStream(jsonFile)) {
            assert stream != null;
            try (Reader reader = new InputStreamReader(stream)) {

                JsonModel model = objectMapper.readValue(reader, JsonModel.class);

                assertThat(model.getFirstName()).isEqualTo("name");
                assertThat(model.getLastName()).isEqualTo("last name");
                assertThat(model.getMobileNumber()).isEqualTo("563835648");
            }
        }
    }
}
