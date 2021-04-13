package com.example.demo.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Juegos implements Serializable {

	@Id
	@GeneratedValue
	private Long id_juego;
	private String titulo;
	private int anyo_publicacion;
	private Double puntuacion;
	
	
}
