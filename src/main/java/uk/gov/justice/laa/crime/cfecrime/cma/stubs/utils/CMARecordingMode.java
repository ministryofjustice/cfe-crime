package uk.gov.justice.laa.crime.cfecrime.cma.stubs.utils;

public class CMARecordingMode {
    public static Boolean recordingMode() {
        var record_mode = System.getenv("VCR_RECORD_MODE");
        if (record_mode == null) {
            return null;
        } else {
            return record_mode.equals("record");
        }
    }
}
