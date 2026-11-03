package com.pokemon.ui;

import com.pokemon.engine.BattleEngine;
import com.pokemon.model.ElementType;
import com.pokemon.model.Move;
import com.pokemon.model.Pokemon;
import com.pokemon.model.PokemonData;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PokemonBattleApp extends Application {

    private final BattleEngine engine = new BattleEngine();
    private final Random random = new Random();

    private BorderPane mainRoot;
    private final List<Pokemon> selectedTeam = new ArrayList<>();
    private final int MAX_TEAM_SIZE = 2;

    private List<Pokemon> playerTeam;
    private int activePlayerIndex = 0;
    private Pokemon activePlayerPokemon;
    private Pokemon enemyPokemon;

    private ProgressBar playerHpBar;
    private Label playerHpLabel;
    private Label playerNameLabel;
    private ImageView playerImageView;

    private ProgressBar enemyHpBar;
    private Label enemyHpLabel;
    private Label enemyNameLabel;
    private ImageView enemyImageView;

    private TextArea logArea;
    private GridPane movesGrid;
    private GridPane mainActionsGrid;
    private Button btnFight, btnBag, btnPokemon, btnRun;

    private boolean isTurnInProgress = false;

    @Override
    public void start(Stage primaryStage) {
        mainRoot = new BorderPane();
        mainRoot.setStyle("-fx-background-color: #2c3e50;");

        mainRoot.setCenter(createSelectionView());

        Scene scene = new Scene(mainRoot, 850, 650);
        primaryStage.setTitle("Simulador de Batalla Pokémon");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private VBox createSelectionView() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #1e272e;");

        Label titleLabel = new Label("¡ELIGE " + MAX_TEAM_SIZE + " POKÉMON PARA TU EQUIPO!");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f1c40f;");

        Label counterLabel = new Label("Seleccionados: 0 / " + MAX_TEAM_SIZE);
        counterLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #ffffff;");

        Button startBattleBtn = new Button("¡COMENZAR BATALLA!");
        startBattleBtn.setPrefSize(220, 45);
        startBattleBtn.setDisable(true);
        startBattleBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 8;");

        GridPane cardsGrid = new GridPane();
        cardsGrid.setHgap(15);
        cardsGrid.setVgap(15);
        cardsGrid.setAlignment(Pos.CENTER);

        List<Pokemon> availableList = PokemonData.getAvailablePokemons();

        for (int i = 0; i < availableList.size(); i++) {
            Pokemon p = availableList.get(i);
            VBox card = createPokemonCard(p, counterLabel, startBattleBtn);
            cardsGrid.add(card, i % 3, i / 3);
        }

        startBattleBtn.setOnAction(e -> startBattle());

        layout.getChildren().addAll(titleLabel, counterLabel, cardsGrid, startBattleBtn);
        return layout;
    }

    private VBox createPokemonCard(Pokemon pokemon, Label counterLabel, Button startBtn) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));
        card.setPrefSize(180, 200);
        card.setStyle("-fx-background-color: #34495e; -fx-background-radius: 10; -fx-cursor: hand; -fx-border-color: transparent; -fx-border-width: 3; -fx-border-radius: 10;");

        ImageView imgView = new ImageView();
        try {
            imgView.setImage(new Image(pokemon.getSpriteUrl(), 100, 100, true, true));
        } catch (Exception ignored) {}

        Label nameLbl = new Label(pokemon.getName());
        nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: white;");

        Label typeLbl = new Label("Tipo: " + pokemon.getType().getName());
        typeLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: " + pokemon.getType().getColorHex() + "; -fx-font-weight: bold;");

        Label hpLbl = new Label("HP: " + pokemon.getMaxHp() + " | ATK: " + pokemon.getAttack());
        hpLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: #bdc3c7;");

        card.getChildren().addAll(imgView, nameLbl, typeLbl, hpLbl);

        card.setOnMouseClicked(e -> {
            if (selectedTeam.contains(pokemon)) {
                selectedTeam.remove(pokemon);
                card.setStyle("-fx-background-color: #34495e; -fx-background-radius: 10; -fx-cursor: hand; -fx-border-color: transparent; -fx-border-width: 3; -fx-border-radius: 10;");
            } else {
                if (selectedTeam.size() < MAX_TEAM_SIZE) {
                    selectedTeam.add(pokemon);
                    card.setStyle("-fx-background-color: #2c3e50; -fx-background-radius: 10; -fx-cursor: hand; -fx-border-color: #f1c40f; -fx-border-width: 3; -fx-border-radius: 10;");
                }
            }

            counterLabel.setText("Seleccionados: " + selectedTeam.size() + " / " + MAX_TEAM_SIZE);
            startBtn.setDisable(selectedTeam.size() != MAX_TEAM_SIZE);
        });

        return card;
    }

    private void startBattle() {
        playerTeam = new ArrayList<>(selectedTeam);
        activePlayerIndex = 0;
        activePlayerPokemon = playerTeam.get(0);
        enemyPokemon = PokemonData.getEnemyPokemon();

        mainRoot.setCenter(createArenaView());
        mainRoot.setBottom(createBottomControl());

        updateUI();

        appendLog("¡Un " + enemyPokemon.getName() + " salvaje apareció!");
        appendLog("¡Ve " + activePlayerPokemon.getName() + "!");
    }

    private VBox createArenaView() {
        VBox arena = new VBox();
        arena.setPadding(new Insets(20));
        arena.setSpacing(20);
        arena.setStyle("-fx-background-color: linear-gradient(to bottom, #78ab46, #c2e59c);");

        HBox enemyRow = new HBox(30);
        enemyRow.setAlignment(Pos.CENTER_LEFT);
        VBox enemyHud = createHudCard(enemyPokemon.getName(), true);
        enemyImageView = createPokemonImageView(enemyPokemon.getSpriteUrl());
        enemyRow.getChildren().addAll(enemyHud, enemyImageView);

        HBox playerRow = new HBox(30);
        playerRow.setAlignment(Pos.CENTER_RIGHT);
        playerImageView = createPokemonImageView(activePlayerPokemon.getSpriteUrl());
        VBox playerHud = createHudCard(activePlayerPokemon.getName(), false);
        playerRow.getChildren().addAll(playerImageView, playerHud);

        arena.getChildren().addAll(enemyRow, playerRow);
        return arena;
    }

    private VBox createHudCard(String name, boolean isEnemy) {
        VBox hud = new VBox(5);
        hud.setPadding(new Insets(10));
        hud.setPrefWidth(260);
        hud.setStyle("-fx-background-color: #f1f2f6; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 2);");

        Label nameLbl = new Label(name + "  Niv. 50");
        nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2c3e50;");

        ProgressBar hpBar = new ProgressBar(1.0);
        hpBar.setPrefWidth(240);
        hpBar.setStyle("-fx-accent: #2ecc71;");

        Label hpLbl = new Label("HP: 100 / 100");
        hpLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: #34495e;");

        if (isEnemy) {
            enemyNameLabel = nameLbl;
            enemyHpBar = hpBar;
            enemyHpLabel = hpLbl;
        } else {
            playerNameLabel = nameLbl;
            playerHpBar = hpBar;
            playerHpLabel = hpLbl;
        }

        hud.getChildren().addAll(nameLbl, hpBar, hpLbl);
        return hud;
    }

    private ImageView createPokemonImageView(String url) {
        ImageView iv = new ImageView();
        try {
            iv.setImage(new Image(url, 160, 160, true, true));
        } catch (Exception ignored) {}
        iv.setFitWidth(160);
        iv.setFitHeight(160);
        return iv;
    }

    private VBox createBottomControl() {
        VBox bottom = new VBox(10);
        bottom.setPadding(new Insets(10));
        bottom.setStyle("-fx-background-color: #1e272e;");

        HBox splitBox = new HBox(15);

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setPrefSize(450, 120);
        logArea.setStyle("-fx-control-inner-background: #000000; -fx-text-fill: #00ff00; -fx-font-family: 'Monospaced'; -fx-font-size: 12px;");

        mainActionsGrid = new GridPane();
        mainActionsGrid.setHgap(10);
        mainActionsGrid.setVgap(10);
        mainActionsGrid.setAlignment(Pos.CENTER);

        btnFight = createStyledButton("LUCHAR", "#e74c3c");
        btnBag = createStyledButton("BOLSA", "#f39c12");
        btnPokemon = createStyledButton("POKÉMON", "#2ecc71");
        btnRun = createStyledButton("HUIR", "#95a5a6");

        btnFight.setOnAction(e -> showMovesMenu(true));
        btnBag.setOnAction(e -> handleUseItem());
        btnPokemon.setOnAction(e -> handleSwitchPokemon());
        btnRun.setOnAction(e -> handleRun());

        mainActionsGrid.add(btnFight, 0, 0);
        mainActionsGrid.add(btnBag, 1, 0);
        mainActionsGrid.add(btnPokemon, 0, 1);
        mainActionsGrid.add(btnRun, 1, 1);

        movesGrid = new GridPane();
        movesGrid.setHgap(10);
        movesGrid.setVgap(10);
        movesGrid.setAlignment(Pos.CENTER);
        movesGrid.setVisible(false);

        splitBox.getChildren().addAll(logArea, mainActionsGrid, movesGrid);
        bottom.getChildren().add(splitBox);
        return bottom;
    }

    private Button createStyledButton(String text, String colorHex) {
        Button btn = new Button(text);
        btn.setPrefSize(140, 50);
        btn.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 6;");
        return btn;
    }

    private void showMovesMenu(boolean show) {
        mainActionsGrid.setVisible(!show);
        mainActionsGrid.setManaged(!show);

        movesGrid.getChildren().clear();
        movesGrid.setVisible(show);
        movesGrid.setManaged(show);

        if (show) {
            List<Move> moves = activePlayerPokemon.getMoves();
            for (int i = 0; i < moves.size(); i++) {
                Move move = moves.get(i);
                Button mBtn = new Button(move.getName() + "\nPP: " + move.getCurrentPp() + "/" + move.getMaxPp());
                mBtn.setPrefSize(140, 50);
                mBtn.setStyle("-fx-background-color: " + move.getType().getColorHex() + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11px;");

                mBtn.setOnAction(e -> {
                    showMovesMenu(false);
                    handlePlayerTurn(move);
                });

                movesGrid.add(mBtn, i % 2, i / 2);
            }

            Button backBtn = new Button("VOLVER");
            backBtn.setPrefSize(290, 30);
            backBtn.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-font-weight: bold;");
            backBtn.setOnAction(e -> showMovesMenu(false));
            movesGrid.add(backBtn, 0, 2, 2, 1);
        }
    }

    private void handlePlayerTurn(Move selectedMove) {
        if (isTurnInProgress) return;
        isTurnInProgress = true;

        boolean playerFirst = activePlayerPokemon.getSpeed() >= enemyPokemon.getSpeed();

        if (playerFirst) {
            executePlayerAttack(selectedMove, () -> {
                if (!enemyPokemon.isFainted()) {
                    scheduleEnemyTurn();
                } else {
                    appendLog("¡El " + enemyPokemon.getName() + " enemigo se debilitó!");
                    appendLog("¡HAS GANADO LA BATALLA!");
                    disableAllControls();
                }
            });
        } else {
            executeEnemyAttack(() -> {
                if (!activePlayerPokemon.isFainted()) {
                    executePlayerAttack(selectedMove, () -> {
                        if (enemyPokemon.isFainted()) {
                            appendLog("¡El " + enemyPokemon.getName() + " enemigo se debilitó!");
                            appendLog("¡HAS GANADO LA BATALLA!");
                            disableAllControls();
                        } else {
                            isTurnInProgress = false;
                        }
                    });
                } else {
                    handlePlayerFainted();
                }
            });
        }
    }

    private void executePlayerAttack(Move move, Runnable onComplete) {
        BattleEngine.AttackResult result = engine.executeMove(activePlayerPokemon, enemyPokemon, move);
        appendLog(result.message);
        updateUI();

        PauseTransition delay = new PauseTransition(Duration.seconds(1.2));
        delay.setOnFinished(e -> onComplete.run());
        delay.play();
    }

    private void executeEnemyAttack(Runnable onComplete) {
        List<Move> enemyMoves = enemyPokemon.getMoves();
        Move enemyMove = enemyMoves.get(random.nextInt(enemyMoves.size()));

        BattleEngine.AttackResult result = engine.executeMove(enemyPokemon, activePlayerPokemon, enemyMove);
        appendLog(result.message);
        updateUI();

        PauseTransition delay = new PauseTransition(Duration.seconds(1.2));
        delay.setOnFinished(e -> {
            if (activePlayerPokemon.isFainted()) {
                handlePlayerFainted();
            } else {
                onComplete.run();
            }
        });
        delay.play();
    }

    private void scheduleEnemyTurn() {
        PauseTransition delay = new PauseTransition(Duration.seconds(0.8));
        delay.setOnFinished(e -> executeEnemyAttack(() -> isTurnInProgress = false));
        delay.play();
    }

    private void handleUseItem() {
        if (isTurnInProgress) return;
        if (activePlayerPokemon.getCurrentHp() == activePlayerPokemon.getMaxHp()) {
            appendLog("¡Los PS de " + activePlayerPokemon.getName() + " ya están al máximo!");
            return;
        }

        isTurnInProgress = true;
        String logMsg = engine.usePotion(activePlayerPokemon);
        appendLog(logMsg);
        updateUI();

        scheduleEnemyTurn();
    }

    private void handleSwitchPokemon() {
        if (isTurnInProgress) return;

        int nextIndex = (activePlayerIndex + 1) % playerTeam.size();
        Pokemon nextPokemon = playerTeam.get(nextIndex);

        if (nextPokemon.isFainted()) {
            appendLog("¡Ese Pokémon está debilitado y no puede luchar!");
            return;
        }

        isTurnInProgress = true;
        activePlayerIndex = nextIndex;
        activePlayerPokemon = nextPokemon;

        appendLog("¡Vuelve " + playerTeam.get(1 - nextIndex).getName() + "!");
        appendLog("¡Adelante " + activePlayerPokemon.getName() + "!");

        playerImageView.setImage(new Image(activePlayerPokemon.getSpriteUrl(), 160, 160, true, true));
        updateUI();

        scheduleEnemyTurn();
    }

    private void handleRun() {
        appendLog("¡Escapaste sin problemas!");
        disableAllControls();
    }

    private void handlePlayerFainted() {
        appendLog("¡" + activePlayerPokemon.getName() + " se ha debilitado!");

        boolean availableFound = false;
        for (int i = 0; i < playerTeam.size(); i++) {
            if (!playerTeam.get(i).isFainted()) {
                activePlayerIndex = i;
                activePlayerPokemon = playerTeam.get(i);
                appendLog("¡Adelante " + activePlayerPokemon.getName() + "!");
                playerImageView.setImage(new Image(activePlayerPokemon.getSpriteUrl(), 160, 160, true, true));
                updateUI();
                availableFound = true;
                isTurnInProgress = false;
                break;
            }
        }

        if (!availableFound) {
            appendLog("¡No te quedan Pokémon en buen estado!");
            appendLog("¡HAS PERDIDO LA BATALLA!");
            disableAllControls();
        }
    }

    private void updateUI() {
        double pRatio = (double) activePlayerPokemon.getCurrentHp() / activePlayerPokemon.getMaxHp();
        playerHpBar.setProgress(pRatio);
        playerHpLabel.setText("HP: " + activePlayerPokemon.getCurrentHp() + " / " + activePlayerPokemon.getMaxHp());
        playerNameLabel.setText(activePlayerPokemon.getName() + "  Niv. " + activePlayerPokemon.getLevel());
        updateBarColor(playerHpBar, pRatio);

        double eRatio = (double) enemyPokemon.getCurrentHp() / enemyPokemon.getMaxHp();
        enemyHpBar.setProgress(eRatio);
        enemyHpLabel.setText("HP: " + enemyPokemon.getCurrentHp() + " / " + enemyPokemon.getMaxHp());
        enemyNameLabel.setText(enemyPokemon.getName() + "  Niv. " + enemyPokemon.getLevel());
        updateBarColor(enemyHpBar, eRatio);
    }

    private void updateBarColor(ProgressBar bar, double ratio) {
        if (ratio > 0.5) {
            bar.setStyle("-fx-accent: #2ecc71;");
        } else if (ratio > 0.2) {
            bar.setStyle("-fx-accent: #f1c40f;");
        } else {
            bar.setStyle("-fx-accent: #e74c3c;");
        }
    }

    private void disableAllControls() {
        btnFight.setDisable(true);
        btnBag.setDisable(true);
        btnPokemon.setDisable(true);
        btnRun.setDisable(true);
    }

    private void appendLog(String text) {
        logArea.appendText(text + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}