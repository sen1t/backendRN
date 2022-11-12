package br.com.rnschedule.schedule.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import org.springframework.context.annotation.Bean;

public class DateUtils {
	
	
	public static String formatterDate(LocalDateTime data) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		
		return data.format(formatter);
		
	}
	
	
	public static LocalDateTime retornaLocalDateTime(String data) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		
		return LocalDateTime.parse(data, formatter);
		
	}
}
