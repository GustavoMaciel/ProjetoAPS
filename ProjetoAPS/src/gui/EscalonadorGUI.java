package gui;

import java.util.ArrayList;

import framework.Processo;
import framework.TabelaResultante;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EscalonadorGUI extends Application {

	private static TabelaResultante tabela;
	private static VBox areaHistorico;
	private static BorderPane borderPane;
	private static ArrayList<Processo> listaProcessos;

	/**
	 * Inicializacao dos componentes
	 */
	private static void inicializarComponentesGraficos() {
		borderPane = new BorderPane();
		areaHistorico = new VBox();
	}

	/**
	 * Cria a regua
	 */
	private static HBox criaRegua() {
		HBox regua = new HBox();
		Separator s;
		Label l;

		for (int i = 0; i < 20; i++) {
			l = new Label("" + (i));

			if (i < 10) {
				l = new Label("0" + (i));
			}

			s = new Separator();

			s.setMinWidth(20.0);
			s.setMaxWidth(20.0);

			l.setPadding(new Insets(-5, 0, 0, 0));
			regua.getChildren().add(l);
			regua.getChildren().add(s);

		}
		return regua;
	}

	@Override
	public void start(Stage primaryStage) {
		inicializarComponentesGraficos();

	}

}