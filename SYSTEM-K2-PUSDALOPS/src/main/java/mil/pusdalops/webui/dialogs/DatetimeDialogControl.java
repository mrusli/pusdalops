package mil.pusdalops.webui.dialogs;

import java.time.LocalDateTime;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.webui.common.GFCBaseController;

public class DatetimeDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6441942705927481032L;

	private Window datetimeDialogWin;
	private Datebox twDatebox;
	private Timebox twTimebox;
	private Combobox twZonaWaktuCombobox;
	
	private DatetimeData datetimeData;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// datetimeData
		setDatetimeData(
				(DatetimeData) Executions.getCurrent().getArg().get("datetimeData"));
	}	
	
	public void onCreate$datetimeDialogWin(Event event) throws Exception {
		// System.out.println("onCreate...");
		// System.out.println(getDatetimeData().toString());		
		
		// set the form dialog title
		datetimeDialogWin.setTitle(getDatetimeData().getDialogWinTitle());
		
		// set the current date and time
		setComponentLocaleTimezone();
		
		// setup zona waktu combobox
		setupTimezoneCombobox();
		
		// display info
		displayDateTimeInfo();
	}

	private void setComponentLocaleTimezone() {
		// Locale("id","ID") - Indonesia -- frm GFCBaseController
		twDatebox.setLocale(getLocale());
		twTimebox.setLocale(getLocale());

		// set component time zone
		twDatebox.setTimeZone(getDatetimeData().getZoneId().toString());
		twTimebox.setTimeZone(getDatetimeData().getZoneId().toString());
	}

	private void setupTimezoneCombobox() {
		Comboitem comboitem;
		for (TimezoneInd timezoneInd : TimezoneInd.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(timezoneInd.toString());
			comboitem.setValue(timezoneInd);
			comboitem.setParent(twZonaWaktuCombobox);
		}
		
	}

	private void displayDateTimeInfo() {
		twTimebox.setValueInLocalTime(getDatetimeData().getLocalDateTime().toLocalTime());
		twDatebox.setValueInLocalDate(getDatetimeData().getLocalDateTime().toLocalDate());

		for (Comboitem comboitem : twZonaWaktuCombobox.getItems()) {
			if (comboitem.getValue().equals(getDatetimeData().getTimezoneInd())) {
				twZonaWaktuCombobox.setSelectedItem(comboitem);
				
				break;
			}
		}		
	}

	public void onClick$rubahButton(Event event) throws Exception {
		TimezoneInd selTimezoneInd = twZonaWaktuCombobox.getSelectedItem().getValue();
		int timezoneIndOrdinal = selTimezoneInd.getValue();
		
		// set changes in data		
		getDatetimeData().setTimezoneInd(selTimezoneInd);
		getDatetimeData().setZoneId(selTimezoneInd.toZoneId(timezoneIndOrdinal));
		getDatetimeData().setLocalDateTime(
				LocalDateTime.of(twDatebox.getValueInLocalDate(), twTimebox.getValueInLocalTime()));
		
		// send event
		Events.sendEvent(Events.ON_CHANGE, datetimeDialogWin, getDatetimeData());
		
		datetimeDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		datetimeDialogWin.detach();
	}

	public DatetimeData getDatetimeData() {
		return datetimeData;
	}

	public void setDatetimeData(DatetimeData datetimeData) {
		this.datetimeData = datetimeData;
	}

}
