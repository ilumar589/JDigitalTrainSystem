package inhouse.digital.trainsystem.businessdomains.videoprovider.cctvcreaterequests.ui.view.tabs;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import inhouse.digital.trainsystem.businessdomains.videoprovider.cctvcreaterequests.ui.mockdata.MockDataFactory;

import java.time.LocalDate;

public final class CreateByJourneyView extends VerticalLayout {

    private final ComboBox<String> journeySelectionBox = createJourneySelectionBox();


    public CreateByJourneyView() {
        setSizeFull(); //find out how to set the size of the tab content
        add(new Div(new Text("Pick a date to load trains *")));
        add(createDatePicker());
        add(journeySelectionBox);
    }


    private DatePicker createDatePicker() {
        final var datePicker = new DatePicker("Load journeys date");
        datePicker.addValueChangeListener(this::handleDatePickerValueChange);
        return datePicker;
    }

    private ComboBox<String> createJourneySelectionBox() {
        final var journeySelectionBox = new ComboBox<String>("Select Journey");
        journeySelectionBox.setWidth(50, Unit.PERCENTAGE);
        journeySelectionBox.setPlaceholder("Select Journey");
        journeySelectionBox.setRequired(true);
        journeySelectionBox.setVisible(false);
        return journeySelectionBox;
    }

    private void handleDatePickerValueChange(AbstractField.ComponentValueChangeEvent<DatePicker, LocalDate> event) {
        final LocalDate selectedDate = event.getValue();
        UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
            details.getRawTimezoneOffset();
        });
        journeySelectionBox.setVisible(event.getValue() != null);
        journeySelectionBox.setItems(MockDataFactory.getJourneyNamesForDropdown().castToList());
    }
}
