package seedu.duke.command;

import seedu.duke.exceptions.InvalidFormatException;
import seedu.duke.generator.PatientIdManager;
import seedu.duke.record.Patient;
import seedu.duke.storage.PatientList;
import seedu.duke.storage.Storage;
import seedu.duke.ui.Ui;

import java.io.IOException;
import java.util.Map;

/**
 * Delete a patient's record with certain index in the patient's list.
 *
 * @author Nguyen Thanh Duc
 * @version 0.1
 * @since 2020-03-08
 */
public class DeletePatientCommand extends Command {

    public static final String COMMAND_WORD = "deletep";
    public static final String EXAMPLE = "deletep \\index 12";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Delete a patient from the list.\n"
            + "Example: " + EXAMPLE;
    private static final String PATIENT_INDEX = "index";
    private int patientIndex;

    /**
     * Constructor which pass a hash map with only 1 item containing the index of
     * the patient that needs to be removed.
     *
     * @param fieldsToChange a hash map with only 1 item which is a field called
     *                       "index" and the value of the index needed to delete
     */
    public DeletePatientCommand(Map<String, String> fieldsToChange) throws InvalidFormatException {

        try {
            this.patientIndex = Integer.parseInt(fieldsToChange.get(PATIENT_INDEX));
            if (patientIndex > PatientList.getTotalPatients() || patientIndex <= 0) {
                throw new IndexOutOfBoundsException();
            }

        } catch (NumberFormatException e) {
            Ui.showNumberError();

        } catch (IndexOutOfBoundsException e) {
            Ui.showIndexError();
        }
    }

    /**
     * Method to check if the right index is returned to the class.
     *
     * @return patientIndex index of patient that needs to be updated
     */
    public int getPatientIndex() {
        return patientIndex;
    }

    /**
     * Method to delete the patient from the list by getting that patient's index then
     * remove it and auto-save the changes.
     *
     * @param ui      the ui object which can be used to display text
     * @param storage the storage object for auto saving function
     * @throws IOException when there is error in the index's input
     * @see IOException
     * @see PatientList#getPatientRecord
     * @see Storage#savePatientList
     * @see Ui#showDeletePatientSuccess
     */
    @Override
    public void execute(Ui ui, Storage storage) throws IOException {
        try {
            // Get the patient's record based on its index from the list
            Patient patient = PatientList.getPatientRecord(patientIndex - 1);

            // Add back the patient id
            PatientIdManager.addBackPatientId(patient.getPatientID());

            // Get the original appointment's list size
            int originalSize = PatientList.getTotalPatients();

            // Remove the patient's information from the patient's list
            PatientList.getPatientList().remove(patient);

            // Check with assertions that the size has been decremented
            assert PatientList.getTotalPatients() == originalSize - 1;

            // Auto-save the changes
            storage.savePatientList();

            // Show deleted patient successfully message
            Ui.showDeletePatientSuccess();
        } catch (IndexOutOfBoundsException e) {
            return;
        }
    }
}
