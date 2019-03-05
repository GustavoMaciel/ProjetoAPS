package gui;

import java.util.ArrayList;

import framework.Processo;
import framework.StatusProcesso;
import framework.TabelaResultante;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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

	/**
	 * Cria uma coluna de processos de acordo com os processos que estao na fila
	 * 
	 * @return
	 */
	private static VBox criaColunaDeProcessos() {
		VBox v = new VBox();
		if (listaProcessos.size() == 0)
			return v;

		Button btnProcesso;
		for (Processo p : listaProcessos) {
			btnProcesso = new Button(p.getProcessoID());
			v.getChildren().add(btnProcesso);
		}
		return v;
	}

	/**
	 * Cria o histórico dos processo enforma de tabela colorida
	 */
	private static VBox criaHistoricoColorido() {

		HBox HLinha = new HBox();
		// criaRegua();
		areaHistorico.getChildren().add(criaRegua());

		char[] linhas = tabela.resultado().toCharArray();
		// System.out.println(";"+linhas);
		Rectangle rect;

		for (int i = 0; i < linhas.length; i++) {

			rect = new Rectangle(20, 20);
			if (linhas[i] == StatusProcesso.NAOEXISTE.toString().charAt(0)) {// inexistente
				rect.setFill(Color.web("#211919"));
				HLinha.getChildren().add(rect);

			} else if (linhas[i] == StatusProcesso.RUNNING.toString().charAt(0)) { // running
				rect.setFill(Color.web("#77C938"));
				HLinha.getChildren().add(rect);

			} else if (linhas[i] == StatusProcesso.WAITING.toString().charAt(0)) { // esperando
				rect.setFill(Color.web("#FFFF19"));
				HLinha.getChildren().add(rect);

			} else if (linhas[i] == StatusProcesso.FINISHED.toString().charAt(0)) { // finalizado
				rect.setFill(Color.web("#EC0C0C"));
				HLinha.getChildren().add(rect);

				// adicionar o anterior
				areaHistorico.getChildren().add(HLinha);
				// criar novo HBox
				HLinha = new HBox();

			}
		}
		return areaHistorico;
	}

	/**
	 * Metodo obrigatorio a ser chamado
	 */
	public static void addResultado(TabelaResultante tabelaResultante) {
		tabela = tabelaResultante;
		System.out.println(tabela.resultado());
	}

	@Override
	public void start(Stage primaryStage) {
		inicializarComponentesGraficos();
		criaHistoricoColorido();

		// borderPane.setCenter(criaHistoricoColorido());

		borderPane.setPadding(new Insets(20, 20, 20, 20));
		VBox hTitle = new VBox();
		hTitle.getChildren().add(new Label("De Hero"));

		hTitle.setStyle("-fx-background-color: #336699;");
		borderPane.setLeft(hTitle);
		borderPane.setCenter(criaHistoricoColorido());
		Scene myScene = new Scene(borderPane, 500, 200);
		primaryStage.setScene(myScene);
		primaryStage.show();

	}

	public void run(String[] args) {
		launch(args);
	}

}