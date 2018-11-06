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
            setValue(time.plusMinutes(steps));
        }
	}
}
