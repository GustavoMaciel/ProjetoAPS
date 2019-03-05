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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EscalonadorGUI extends Application {
	private static int TEMPO_EXECUCAO = 30;

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
	private static VBox criaRegua() {
		//Rectangle transparent;
		Text text;
		HBox numeros = new HBox();
		HBox regua = new HBox();
		// cima, direita, baixo, esquerda
		regua.setPadding(new Insets(0, 0, 0, 6));
		numeros.setPadding(new Insets(0, 0, 0, 4));
		
		Separator s;
		Text l;
		
		Label label;

		for (int i = 0; i < TEMPO_EXECUCAO; i++) {
			label = new Label(""+i);
			l = new Text("|");
			l.setFont(Font.font("Monospaced", 10));
			text = new Text(50, 150, i+" ");
			text.setFont(Font.font("Monospaced", 16));
			
			if (i < 9) {
				// cima, direita, baixo, esquerda
				label.setPadding(new Insets(0, 13, 0, 0));
			} else {
				// cima, direita, baixo, esquerda
				label.setPadding(new Insets(0, 5, 0, 0));
			}
			
			label.setMinWidth(10);
			label.setMaxHeight(10);
			numeros.getChildren().add(label);
			
			s = new Separator();

			s.setMinWidth(15);
			s.setMaxWidth(15);
			
			regua.getChildren().add(l);
			regua.getChildren().add(s);

		}
		
		VBox c = new VBox();
		c.getChildren().add(numeros);
		c.getChildren().add(regua);
		return c;
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
			HBox container = new HBox();
			container.getChildren().add(rect);
			
			
			// cima, direita, baixo, esquerda
			HLinha.setPadding(new Insets(2, 2, 0, 10));
			container.setPadding(new Insets(0, 2, 0, 0));
			
			if (linhas[i] == StatusProcesso.NAOEXISTE.toString().charAt(0)) {// inexistente
				rect.setFill(Color.web("#211919"));
				HLinha.getChildren().add(container);

			} else if (linhas[i] == StatusProcesso.RUNNING.toString().charAt(0)) { // running
				rect.setFill(Color.web("#77C938"));
				HLinha.getChildren().add(container);

			} else if (linhas[i] == StatusProcesso.WAITING.toString().charAt(0)) { // esperando
				rect.setFill(Color.web("#FFFF19"));
				HLinha.getChildren().add(container);

			} else if (linhas[i] == StatusProcesso.FINISHED.toString().charAt(0)) { // finalizado
				rect.setFill(Color.web("#EC0C0C"));
				HLinha.getChildren().add(container);

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
		VBox historico = criaHistoricoColorido();

		// borderPane.setCenter(criaHistoricoColorido());

		
		VBox hTitle = new VBox();
		hTitle.getChildren().add(new Label("De Hero"));
		hTitle.setStyle("-fx-background-color: #336699;");
		
		// configuracaoes do border
		borderPane.setPadding(new Insets(20, 20, 20, 20));
		borderPane.setLeft(hTitle);
		borderPane.setCenter(historico);
		
		
		Scene myScene = new Scene(borderPane, 500, 200);
		primaryStage.setScene(myScene);
		primaryStage.show();

	}

	public void run(String[] args) {
		launch(args);
	}

}