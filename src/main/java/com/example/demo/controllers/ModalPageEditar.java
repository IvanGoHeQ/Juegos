package com.example.demo.controllers;

import java.time.Year;

import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.example.demo.models.Juegos;
import com.example.demo.models.JuegosRepository;

public class ModalPageEditar extends WebPage {

	public ModalPageEditar(final PageReference modalWindowPage, final ModalWindow window, Juegos juego,
			JuegosRepository repository) {

		TextField titulo = new TextField<>("titulo", Model.of(juego.getTitulo()));

		NumberTextField<Integer> anyo = new NumberTextField<Integer>("anyo", Model.of(juego.getAnyo_publicacion()));
		anyo.setRequired(true);
		anyo.setMinimum(1800);
		anyo.setMaximum(3000);

		NumberTextField<Double> puntuacion = new NumberTextField<Double>("puntuacion", Model.of(juego.getPuntuacion()));
		puntuacion.setStep(0.1);
		puntuacion.setMinimum(0.0);
		puntuacion.setMaximum(10.0);
		puntuacion.setRequired(true);

		Form form = new Form("formulario") {
		};

		AjaxSubmitLink ajaxButton = new AjaxSubmitLink("botonEditar") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				juego.setTitulo(titulo.getValue());
				juego.setAnyo_publicacion(Integer.parseInt(anyo.getValue()));
				juego.setPuntuacion(Double.parseDouble(puntuacion.getValue()));
				repository.save(juego);

				window.close(target);
			}
		};
		form.add(ajaxButton);
		form.add(titulo.setRequired(true));
		form.add(anyo.setRequired(true));
		form.add(puntuacion.setRequired(true));
		add(form);

	}

}
