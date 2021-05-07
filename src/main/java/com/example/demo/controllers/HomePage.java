package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import com.example.demo.models.Juegos;
import com.example.demo.models.JuegosRepository;
import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import com.googlecode.wicket.jquery.ui.widget.progressbar.ProgressBar;

@WicketHomePage
public class HomePage extends WebPage {

	@SpringBean
	private JuegosRepository repository;

	// Metodo para recoger los juegos de la bd mediante el repositorio
	public List<Juegos> buscar() {

		Iterable<Juegos> it = repository.findAll();
		if (repository == null) {
			System.out.println("NULO");
		}
		repository.findAll();
		List<Juegos> juegos = new ArrayList<Juegos>();

		it.forEach(e -> juegos.add(e));

		return juegos;
	}
	
	 @Override
	  public void renderHead(IHeaderResponse response) {
	    PackageResourceReference cssFile = new PackageResourceReference(this.getClass(), "Homepage.css");
	    CssHeaderItem cssItem = CssHeaderItem.forReference(cssFile);

	    response.render(cssItem);
	  }

	public HomePage() {
		
		System.out.println("ASIDOAS");

		// Ventana para editar
		final ModalWindow modalEditar;
		add(modalEditar = new ModalWindow("modalEditar"));
	

		// Creacion y rellenar la tabla con los juegos
		IModel lista2 = new LoadableDetachableModel() {
			protected Object load() {
				return buscar();
			}
		};

		final CheckGroup<Juegos> group = new CheckGroup<Juegos>("group", new ArrayList<Juegos>());

		PageableListView<Juegos> lista = new PageableListView<Juegos>("listView", lista2, 6) {
			public void populateItem(ListItem<Juegos> item) {

				Juegos juego = (Juegos) item.getModelObject();
				Label titulo;
				Label anyo;
				Label puntuacion;
				Check<Juegos> check;
				int y = 0;

				item.add(anyo = new Label("anyoComponent", juego.getAnyo_publicacion()));
				item.add(puntuacion = new Label("puntuacionComponent", juego.getPuntuacion()));
				item.add(check = new Check<Juegos>("check", item.getModel()));
				String nota= juego.getPuntuacion().toString();
				nota= nota.replace(".", "");
				int nota1= Integer.parseInt(nota);
				item.add(new Label("barra", Model.of("")){
				    @Override
				    protected void onComponentTag(ComponentTag tag) {
				        super.onComponentTag(tag);
				        String style = "width:"+nota1+"%";
				        String clase= tag.getAttribute("class");
				        
				        if(juego.getPuntuacion()<5 && juego.getPuntuacion()>2.5) {
				        	clase= clase+" bg-warning";
				        	tag.getAttributes().put("class", clase);
				        }else if(juego.getPuntuacion()<2.5) {
				        	clase= clase+" bg-danger";
				        	tag.getAttributes().put("class", clase);
				        }else if(juego.getPuntuacion()>=9) {
				        	clase= clase+" bg-success";
				        	tag.getAttributes().put("class", clase);
				        }
				        tag.getAttributes().put("style", style);
				    }
				});
				anyo.setOutputMarkupId(true);
				puntuacion.setOutputMarkupId(true);
				check.setOutputMarkupId(true);

				item.add(new AjaxLink<Void>("link") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						modalEditar.setPageCreator(new ModalWindow.PageCreator() {

							@Override
							public Page createPage() {
								return new ModalPageEditar(HomePage.this.getPageReference(), modalEditar, juego, repository);
							}
						});
						modalEditar.setDefaultModel(Model.of(juego));
						modalEditar.show(target);
					}
				}.add(titulo = new Label("tituloComponent", juego.getTitulo())));
				titulo.setOutputMarkupId(true);

			}
		};

		lista.setOutputMarkupId(true);

		WebMarkupContainer listContainer = new WebMarkupContainer("container");

		group.setOutputMarkupId(true);
		listContainer.setOutputMarkupId(true);

		listContainer.add(lista);
		listContainer.add(new Label("tituloTitulo", "Título"));
		listContainer.add(new Label("anyoTitulo", "Año de publicación"));
		listContainer.add(new Label("puntuacionTitulo", "Puntuación"));
		listContainer.add(new Label("eliminarTitulo", "Eliminar"));

		Form formulario = new Form("formularioTabla");
		formulario.add(group);

		AjaxSubmitLink ajaxButton = new AjaxSubmitLink("buttonEliminar") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {

				if (group.getDefaultModelObject() != null) {
					ArrayList<Juegos> juegosSelected = (ArrayList<Juegos>) group.getDefaultModelObject();

					for (int i = 0; i < juegosSelected.size(); i++) {
						Long id = juegosSelected.get(i).getId_juego();
						repository.deleteById(id);
					}
				}

				setResponsePage(getPage());

			}
		};

		formulario.setOutputMarkupId(true);
		formulario.add(ajaxButton);
		group.add(new AjaxPagingNavigator("navigator", lista));
		group.add(listContainer);
		add(formulario);

		// Creacion ventana añadir
		final ModalWindow modalAnyadir;
		add(modalAnyadir = new ModalWindow("modalAnyadir"));

		modalAnyadir.setPageCreator(new ModalWindow.PageCreator() {

			@Override
			public Page createPage() {
				return new ModalPageAnyadir(HomePage.this.getPageReference(), modalAnyadir);
			}
		});

		modalAnyadir.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
			@Override
			public void onClose(AjaxRequestTarget target) {
				if (target != null) {
					target.add(listContainer);

				}
			}
		});

		modalEditar.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
			@Override
			public void onClose(AjaxRequestTarget target) {
				if (target != null) {

					target.add(listContainer);
				}
			}
		});

		add(new AjaxLink<Void>("showModalAnyadir") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modalAnyadir.show(target);
			}
		});

	}

}
