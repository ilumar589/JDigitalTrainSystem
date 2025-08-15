package inhouse.digital.trainsystem.cctvcreaterequests.ui.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import inhouse.digital.trainsystem.base.ui.component.ViewToolbar;
import inhouse.digital.trainsystem.cctvcreaterequests.ui.view.tabs.CreateByJourneyView;
import jakarta.annotation.security.PermitAll;

@Route("cctv-create-request")
@PageTitle("CCTV Create Requests")
@Menu(order = 1, icon = "vaadin:camera", title = "CCTV Create Requests")
@PermitAll // When security is enabled, allow all authenticated users
public final class CreateRequestRootView extends Main {

    public CreateRequestRootView() {
        add(new ViewToolbar("CCTV Create Requests", ViewToolbar.group()));

        final var tabSheet = new TabSheet();

        tabSheet.add("Create by journey", new CreateByJourneyView());
        tabSheet.add("Create by train",
                new Div(new Text("This is the Payment tab content")));
        tabSheet.add("Create by scenario",
                new Div(new Text("This is the Shipping tab content")));
        tabSheet.add("Create by station",
                new Div(new Text("This is the Shipping tab content")));
        tabSheet.add("Create by schedule",
                new Div(new Text("This is the Shipping tab content")));

        add(tabSheet);

    }
}
