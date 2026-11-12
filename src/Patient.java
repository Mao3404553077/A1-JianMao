/**
 * Part 4 - the patient.
 *
 * The brief asks whether patient should be its own class, and the answer here is
 * yes: an appointment only needs to know about a patient, and keeping the patient
 * details in one small class means the same patient can later book more than one
 * appointment without the details being copied into every appointment.
 */
public class Patient {

    private final String name;
    private final String mobileNumber;

    /**
     * @param name         the patient's full name
     * @param mobileNumber a 10-digit Australian mobile number, for example
     *                     "0412 345 678" or "0412345678"
     */
    public Patient(String name, String mobileNumber) {
        this.name = validateName(name);
        this.mobileNumber = validateMobileNumber(mobileNumber);
    }

    public String getName() {
        return name;
    }

    /** The mobile number as ten digits, which makes comparisons reliable. */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /** The mobile number in the usual Australian layout, for example 0412 345 678. */
    public String getFormattedMobileNumber() {
        return mobileNumber.substring(0, 4) + " " + mobileNumber.substring(4, 7) + " "
                + mobileNumber.substring(7);
    }

    /**
     * A tolerant comparison used by the Part 5 filtering: spaces, dashes and an
     * international prefix are ignored, so "0412 345 678", "0412345678" and
     * "+61 412 345 678" all match the same patient.
     */
    public boolean matchesMobileNumber(String candidate) {
        if (candidate == null) {
            return false;
        }
        String digits = candidate.replaceAll("[^0-9]", "");
        if (digits.startsWith("61") && digits.length() == 11) {
            digits = "0" + digits.substring(2);
        }
        return mobileNumber.equals(digits);
    }

    @Override
    public String toString() {
        return name + " (" + getFormattedMobileNumber() + ")";
    }

    private static String validateName(String name) {
        String trimmed = (name == null) ? "" : name.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("A patient must have a name.");
        }
        return trimmed;
    }

    private static String validateMobileNumber(String mobileNumber) {
        String digits = (mobileNumber == null) ? "" : mobileNumber.replaceAll("[^0-9]", "");
        if (digits.startsWith("61") && digits.length() == 11) {
            digits = "0" + digits.substring(2);
        }
        if (!digits.matches("04\\d{8}")) {
            throw new IllegalArgumentException("A patient mobile number must be a 10-digit "
                    + "Australian mobile number starting with 04, for example 0412 345 678, but was: "
                    + mobileNumber);
        }
        return digits;
    }
}
