package com.example.demo.controllers;

import java.time.Year;
import java.util.Date;

import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.example.demo.models.Juegos;
import com.example.demo.models.JuegosRepository;

public class ModalPageAnyadir extends WebPage {

	@SpringBean
	private JuegosRepository repository;

	public ModalPageAnyadir(final PageReference modalWindowPage, final ModalWindow window) {

		TextField titulo = new TextField<>("titulo", Model.of(""));

		NumberTextField<Integer> anyo = new NumberTextField<Integer>("anyo", Model.of(Year.now().getValue()));
		anyo.setRequired(true);
		anyo.setMinimum(1800);
		anyo.setMaximum(3000);

		NumberTextField<Double> puntuacion = new NumberTextField<Double>("puntuacion", Model.of(0.0));
		puntuacion.setStep(0.1);
		puntuacion.setMinimum(0.0);
		puntuacion.setMaximum(10.0);
		puntuacion.setRequired(true);

		Form form = new Form("formulario") {
		};

		AjaxSubmitLink ajaxButton = new AjaxSubmitLink("botonEnviar") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				Juegos juego = new Juegos();
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
