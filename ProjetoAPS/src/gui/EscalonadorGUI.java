package gui;

import java.util.ArrayList;

import framework.Processo;
import framework.StatusProcesso;
import framework.TabelaResultante;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

public class EscalonadorGUI extends Application {
	private static int TEMPO_EXECUCAO = 30;
	// private static ArrayList<String> tiposEscalonadores = new
	// ArrayList<String>();

	private static TabelaResultante tabela;
	private static VBox areaHistorico;
	private static BorderPane borderPane;
	private static ArrayList<Processo> listaProcessos;

	private final static String COLOR_NAO_EXISTE = "#211919";
	private final static String COLOR_EXECUTANTO = "#77C938";
	private final static String COLOR_ESPERANDO = "#FFFF19";
	private final static String COLOR_FINALIZADO = "#EC0C0C";

	private final static int WIDTH_WINDOWS = 1024; // largura
	private final static int HEIGHT_WINDOWS = 600; // altura

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
		// Rectangle transparent;
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
			label = new Label("" + i);
			l = new Text("|");
			l.setFont(Font.font("Monospaced", 10));
			text = new Text(50, 150, i + " ");
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

	public static void setFilaProcessos(ArrayList<Processo> listaDeProcessos) {
		listaProcessos = listaDeProcessos;
	}

	/**
	 * Cria uma coluna de processos de acordo com os processos que estao na fila
	 * 
	 * @return
	 */
	private static VBox criaColunaDeProcessos() {
		VBox v = new VBox();
		v.setPadding(new Insets(29, 0, 0, 0));
		HBox container;

		if (listaProcessos == null || listaProcessos.size() == 0)
			return v;

		System.out.println("entrei pocessos");
		for (Processo p : listaProcessos) {
			String str = new StringBuilder().append(p.getProcessoID()).append("(").append(p.getTempoInicio())
					.append(",").append(p.getTempoExec()).append(")").toString();

			container = new HBox();

			// cima, direita, baixo, esquerda
			container.setPadding(new Insets(1, 0, 0, 0));
			container.getChildren().add(new Button(str));
			v.getChildren().add(container);
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

			rect = new Rectangle(20, 25);
			HBox container = new HBox();
			container.getChildren().add(rect);

			// cima, direita, baixo, esquerda
			HLinha.setPadding(new Insets(2, 2, 0, 10));
			container.setPadding(new Insets(0, 2, 0, 0));

			if (linhas[i] == StatusProcesso.NAOEXISTE.toString().charAt(0)) {// inexistente
				rect.setFill(Color.web(COLOR_NAO_EXISTE));
				HLinha.getChildren().add(container);

			} else if (linhas[i] == StatusProcesso.RUNNING.toString().charAt(0)) { // running
				rect.setFill(Color.web(COLOR_EXECUTANTO));
				HLinha.getChildren().add(container);

			} else if (linhas[i] == StatusProcesso.WAITING.toString().charAt(0)) { // esperando
				rect.setFill(Color.web(COLOR_ESPERANDO));
				HLinha.getChildren().add(container);

			} else if (linhas[i] == StatusProcesso.FINISHED.toString().charAt(0)) { // finalizado
				rect.setFill(Color.web(COLOR_FINALIZADO));
				HLinha.getChildren().add(container);

				// adicionar o anterior
				areaHistorico.getChildren().add(HLinha);
				// criar novo HBox
				HLinha = new HBox();

			}
		}
		return areaHistorico;
	}

	private static HBox criaAreaDeConfiguracao() {
		// configs
		HBox areaConfig = new HBox();

		VBox colunaQuantum = new VBox();
		Label lbQuantum = new Label("Quantum");
		TextField tfQuantum = new TextField();

		// adicionando o label e o textfield posteriormente
		colunaQuantum.getChildren().add(lbQuantum);
		colunaQuantum.getChildren().add(tfQuantum);

		VBox colunaEscalonador = new VBox();

		// top, right, bottom, left
		colunaEscalonador.setPadding(new Insets(0, 0, 10, 20));

		Label lbEscalonadores = new Label("Tipos de Escalonadores");
		final ComboBox<String> escalonadoresComboBox = new ComboBox<String>();
		escalonadoresComboBox.getItems().addAll("Round Robin");

		// label e combobox
		colunaEscalonador.getChildren().add(lbEscalonadores);
		colunaEscalonador.getChildren().add(escalonadoresComboBox);

		areaConfig.getChildren().add(colunaQuantum);
		areaConfig.getChildren().add(colunaEscalonador);
		
		return areaConfig;
		
	}

	private static VBox criaLegendaDeCores() {
		VBox container = new VBox();
		// cima, direita, baixo, esquerda
		container.setPadding(new Insets(10, 0, 0, 0));
//		String cssLayout = "-fx-border-color: red;\n" +
//                "-fx-border-insets: 5;\n" +
//                "-fx-border-width: 3;\n" +
//                "-fx-border-style: dashed;\n";

		container.getChildren().add(new Separator());
		// container.getChildren().add(new Label("Legenda"));

		GridPane grid = new GridPane();
		grid.setVgap(5);
		grid.setHgap(2);

		Label legenda = new Label("*Legenda*");
		legenda.setPadding(new Insets(10, 0, 5, 0));
		grid.add(legenda, 2, 0);

		// column, row
		// Inexistente
		grid.add(new Rectangle(30, 30, Color.web(COLOR_NAO_EXISTE)), 0, 1);
		grid.add(new Label("Inexistente"), 1, 1);
		// Executando
		grid.add(new Rectangle(30, 30, Color.web(COLOR_EXECUTANTO)), 0, 2);
		grid.add(new Label("Executando"), 1, 2);
		// Esperando
		grid.add(new Rectangle(30, 30, Color.web(COLOR_ESPERANDO)), 3, 1);
		grid.add(new Label("Esperando"), 4, 1);
		// Finalizado
		grid.add(new Rectangle(30, 30, Color.web(COLOR_FINALIZADO)), 3, 2);
		grid.add(new Label("Finalizado"), 4, 2);

		// grid.setStyle(cssLayout);
		container.getChildren().add(grid);
		return container;
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
		VBox processos = criaColunaDeProcessos();
		VBox legendaDeCores = criaLegendaDeCores();
		HBox areaConfig = criaAreaDeConfiguracao();

		// borderPane.setCenter(criaHistoricoColorido());

		VBox hTitle = new VBox();
		hTitle.getChildren().add(new Label("De Hero"));
		hTitle.setStyle("-fx-background-color: #336699;");

		// configuracaoes do border
		borderPane.setPadding(new Insets(20, 20, 20, 20));
		borderPane.setLeft(processos);
		borderPane.setCenter(historico);
		borderPane.setBottom(legendaDeCores);
		borderPane.setTop(areaConfig);

		Scene myScene = new Scene(borderPane, WIDTH_WINDOWS, HEIGHT_WINDOWS);
		primaryStage.setScene(myScene);
		primaryStage.show();

	}
	
	private static Button addProcesso() {
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Adicionar novo processo");
		
		// set the button types
		ButtonType loginButtonType = new ButtonType("Adicionar", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
		
		
		
		
		
		
		return null;
	}

	public void run(String[] args) {
		launch(args);
	}

}