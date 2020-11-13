package mil.pusdalops.webui.dialogs;

import java.time.LocalDateTime;
import java.time.ZoneId;

import mil.pusdalops.domain.gmt.TimezoneInd;

public class DatetimeData {

	private String dialogWinTitle;
	
	private LocalDateTime localDateTime;
	
	private ZoneId zoneId;
	
	private TimezoneInd timezoneInd;

	@Override
	public String toString() {
		return "DatetimeData [localDateTime=" + localDateTime + ", zoneId=" + zoneId + ", timezoneInd=" + timezoneInd
				+ "]";
	}

	public ZoneId getZoneId() {
		return zoneId;
	}

	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}

	public TimezoneInd getTimezoneInd() {
		return timezoneInd;
	}

	public void setTimezoneInd(TimezoneInd timezoneInd) {
		this.timezoneInd = timezoneInd;
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}

	public String getDialogWinTitle() {
		return dialogWinTitle;
	}

	public void setDialogWinTitle(String dialogWinTitle) {
		this.dialogWinTitle = dialogWinTitle;
	}
	
	
}
