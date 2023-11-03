package uk.gov.justice.laa.crime.cfecrime.cma.stubs.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.Assessment;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiRequest;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiResponse;
import uk.gov.justice.laa.crime.cfecrime.utils.RemoteCmaService;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CMARecordAndPlayback {
    @Autowired
    private final RemoteCmaService remoteCmaService;

    @Autowired
    private final ObjectMapper objectMapper;

    public StatelessApiResponse record(StatelessApiRequest request) {
        log.info("Calling CMA with {}", request);
        final var filename = filenameFor(request.getAssessment());

        try (FileWriter recording = new FileWriter(filename)) {
            final String json_request = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            final StatelessApiResponse result = remoteCmaService.callCma(request);
            final String json_response = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);

            recording.write(String.format("{ \"request\": %s,\n", json_request));
            recording.write(String.format(" \"response\": %s }\n", json_response));

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public StatelessApiResponse playback(StatelessApiRequest request) {
        final var filename = filenameFor(request.getAssessment());

        JSONParser parser = new JSONParser();

        try (FileReader recording = new FileReader(filename)) {
            JSONObject playback = (JSONObject) parser.parse(recording);
            final String json_request = playback.get("request").toString();
            final String json_response = playback.get("response").toString();

            return objectMapper.readValue(json_response, StatelessApiResponse.class);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static String filenameFor(Assessment assessment) {
       return String.format("src/test/java/uk/gov/justice/laa/crime/cfecrime/test%s.json", assessment.getAssessmentDate().toLocalDate());
    }
}
