package unb.cs2043.student_assistant.fxml;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.converter.LocalTimeStringConverter;

/**
 * Controls how the spinner values are exhibited and incremented 
 * @author Alexandre Carvalho
 */
public class TimeSpinnerValueFactory extends SpinnerValueFactory<LocalTime>{
	private final LocalTime MAX = LocalTime.of(22, 00);
	private final LocalTime MIN = LocalTime.of(07, 00);
	
	{
		setConverter(new LocalTimeStringConverter(DateTimeFormatter.ofPattern("HH:mm"), DateTimeFormatter.ofPattern("HH:mm")));
	}
	
	@Override
	public void decrement(int steps) {
        steps = 30;
    	if (getValue() == null)
            setValue(LocalTime.of(12, 00));
        else {
            LocalTime time = (LocalTime) getValue();
            if (time.equals(MIN) || time.minusMinutes(30).isBefore(MIN))
            	setValue(time);
            else
            	setValue(time.minusMinutes(steps));
        }
	}

	@Override
	public void increment(int steps) {
    	steps = 30;
        if (this.getValue() == null)
        	setValue(LocalTime.of(12, 00));
        else {
            LocalTime time = (LocalTime) getValue();
            if (time.equals(MAX) || time.plusMinutes(30).isAfter(MAX))
            	setValue(time);
            else
            	setValue(time.plusMinutes(steps));
        }
	}
}
