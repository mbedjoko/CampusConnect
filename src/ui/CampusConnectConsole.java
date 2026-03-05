package ui;

import core.actors.Enseignant;
import core.actors.Etudiant;
import core.courses.Cours;
import core.exceptions.CapaciteDepasseeException;
import core.exceptions.ConflitHoraireException;
import core.group.Groupe;
import core.planning.Planning;
import core.planning.Seance;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.util.List;

public class CampusConnectConsole extends Application {

    // ── Data ──────────────────────────────────────────────────────────────────
    private final ObservableList<Etudiant>   etudiantsList   = FXCollections.observableArrayList();
    private final ObservableList<Enseignant> enseignantsList = FXCollections.observableArrayList();
    private final ObservableList<Cours>      coursList       = FXCollections.observableArrayList();
    private final ObservableList<Groupe>     groupesList     = FXCollections.observableArrayList();
    private final ObservableList<Seance>     seancesList     = FXCollections.observableArrayList();
    private final Planning planning = new Planning();

    // ── UI state ──────────────────────────────────────────────────────────────
    private StackPane contentArea;
    private Button activeNavBtn = null;

    // ── Palette ───────────────────────────────────────────────────────────────
    private static final String C_DARK    = "#0F1923";
    private static final String C_SIDEBAR = "#152030";
    private static final String C_ACCENT  = "#00C9A7";
    private static final String C_BLUE    = "#0EA5E9";
    private static final String C_PURPLE  = "#A78BFA";
    private static final String C_DANGER  = "#F43F5E";
    private static final String C_WARNING = "#F59E0B";
    private static final String C_GREEN   = "#34D399";
    private static final String C_TEXT    = "#E2E8F0";
    private static final String C_MUTED   = "#64748B";
    private static final String C_CARD    = "#1E2D3D";
    private static final String C_BORDER  = "#243447";

    // ═════════════════════════════════════════════════════════════════════════
    //  START
    // ═════════════════════════════════════════════════════════════════════════
    @Override
    public void start(Stage stage) {
        chargerDonnees();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + C_DARK + ";");
        root.setTop(buildTopBar());
        root.setLeft(buildSidebar());

        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color:" + C_DARK + ";");
        contentArea.setAlignment(Pos.TOP_LEFT);
        root.setCenter(contentArea);

        showDashboard();

        Scene scene = new Scene(root, 1180, 720);
        stage.setTitle("CampusConnect – ENSPD");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  TOP BAR
    // ═════════════════════════════════════════════════════════════════════════
    private HBox buildTopBar() {
        HBox bar = new HBox();
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(0, 24, 0, 24));
        bar.setMinHeight(56);
        bar.setStyle("-fx-background-color:" + C_SIDEBAR
                + ";-fx-border-color:" + C_BORDER + ";-fx-border-width:0 0 1 0;");

        Label dot = new Label("◆");
        dot.setStyle("-fx-text-fill:" + C_ACCENT + ";-fx-font-size:16px;");

        Label title = new Label("  CampusConnect");
        title.setStyle("-fx-text-fill:" + C_TEXT + ";-fx-font-size:18px;"
                + "-fx-font-weight:bold;-fx-font-family:'Segoe UI';");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label sub = new Label("École Nationale Supérieure Polytechnique de Douala  ·  2025-2026");
        sub.setStyle("-fx-text-fill:" + C_MUTED + ";-fx-font-size:12px;"
                + "-fx-font-family:'Segoe UI';");

        bar.getChildren().addAll(dot, title, spacer, sub);
        return bar;
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  SIDEBAR
    // ═════════════════════════════════════════════════════════════════════════
    private VBox buildSidebar() {
        VBox side = new VBox(3);
        side.setPrefWidth(215);
        side.setPadding(new Insets(16, 10, 16, 10));
        side.setStyle("-fx-background-color:" + C_SIDEBAR
                + ";-fx-border-color:" + C_BORDER + ";-fx-border-width:0 1 0 0;");

        Button btnDash = navBtn("  ▣  Tableau de bord", this::showDashboard);
        Button btnEtu  = navBtn("  ◎  Étudiants",       this::showEtudiants);
        Button btnEns  = navBtn("  ◈  Enseignants",      this::showEnseignants);
        Button btnCrs  = navBtn("  ◉  Cours",           this::showCours);
        Button btnGrp  = navBtn("  ⊞  Groupes",         this::showGroupes);
        Button btnPln  = navBtn("  ◷  Séances",         this::showPlanning);

        side.getChildren().addAll(
            navSection("NAVIGATION"), btnDash,
            navSection("ACTEURS"),    btnEtu, btnEns,
            navSection("FORMATION"),  btnCrs, btnGrp,
            navSection("PLANNING"),   btnPln
        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Label ver = new Label("v1.0  ·  POO Java 22");
        ver.setStyle("-fx-text-fill:" + C_MUTED + ";-fx-font-size:11px;-fx-padding:0 0 0 6;");
        side.getChildren().addAll(spacer, ver);

        setActive(btnDash);
        return side;
    }

    private Button navBtn(String text, Runnable action) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle(navIdle());
        btn.setOnAction(e -> { setActive(btn); action.run(); });
        btn.setOnMouseEntered(e -> { if (btn != activeNavBtn) btn.setStyle(navHover()); });
        btn.setOnMouseExited(e  -> { if (btn != activeNavBtn) btn.setStyle(navIdle()); });
        return btn;
    }

    private void setActive(Button btn) {
        if (activeNavBtn != null) activeNavBtn.setStyle(navIdle());
        activeNavBtn = btn;
        btn.setStyle("-fx-background-color:" + C_ACCENT + "20;-fx-text-fill:" + C_ACCENT
                + ";-fx-font-size:13px;-fx-font-family:'Segoe UI';-fx-font-weight:bold;"
                + "-fx-background-radius:7;-fx-padding:9 12 9 12;-fx-cursor:hand;"
                + "-fx-border-color:" + C_ACCENT + "40;-fx-border-radius:7;-fx-border-width:1;");
    }

    private String navIdle()  { return "-fx-background-color:transparent;-fx-text-fill:" + C_MUTED + ";-fx-font-size:13px;-fx-font-family:'Segoe UI';-fx-background-radius:7;-fx-padding:9 12 9 12;-fx-cursor:hand;"; }
    private String navHover() { return "-fx-background-color:" + C_BORDER + ";-fx-text-fill:" + C_TEXT  + ";-fx-font-size:13px;-fx-font-family:'Segoe UI';-fx-background-radius:7;-fx-padding:9 12 9 12;-fx-cursor:hand;"; }

    private Label navSection(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill:" + C_MUTED + ";-fx-font-size:10px;-fx-font-weight:bold;"
                + "-fx-padding:10 6 2 6;-fx-font-family:'Segoe UI';");
        return l;
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  DASHBOARD
    // ═════════════════════════════════════════════════════════════════════════
    private void showDashboard() {
        VBox page = page();
        page.getChildren().add(titleRow("▣  Tableau de bord", "Vue d'ensemble du système CampusConnect"));

        HBox cards = new HBox(14);
        cards.getChildren().addAll(
            statCard("Étudiants",   String.valueOf(etudiantsList.size()),   C_ACCENT,   "◎"),
            statCard("Enseignants", String.valueOf(enseignantsList.size()), C_BLUE,     "◈"),
            statCard("Cours",       String.valueOf(coursList.size()),       C_PURPLE,   "◉"),
            statCard("Groupes",     String.valueOf(groupesList.size()),     C_WARNING,  "⊞"),
            statCard("Séances",     String.valueOf(seancesList.size()),     C_GREEN,    "◷")
        );
        page.getChildren().add(cards);
        page.getChildren().add(sectionLabel("Séances planifiées"));
        page.getChildren().add(buildSeanceTable());
        setContent(page);
    }

    private VBox statCard(String label, String value, String color, String icon) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(18, 20, 18, 20));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color:" + C_CARD + ";-fx-background-radius:10;"
                + "-fx-border-color:" + C_BORDER + ";-fx-border-radius:10;-fx-border-width:1;");
        HBox.setHgrow(card, Priority.ALWAYS);
        Label ico = new Label(icon);
        ico.setStyle("-fx-text-fill:" + color + ";-fx-font-size:22px;");
        Label val = new Label(value);
        val.setStyle("-fx-text-fill:" + color + ";-fx-font-size:30px;-fx-font-weight:bold;-fx-font-family:'Segoe UI';");
        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill:" + C_MUTED + ";-fx-font-size:12px;-fx-font-family:'Segoe UI';");
        card.getChildren().addAll(ico, val, lbl);
        return card;
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  ÉTUDIANTS
    // ═════════════════════════════════════════════════════════════════════════
    private void showEtudiants() {
        VBox page = page();
        page.getChildren().add(titleRow("◎  Étudiants", "Gestion des étudiants inscrits"));

        TableView<Etudiant> table = new TableView<>(etudiantsList);
        styleTable(table);
                table.getColumns().add(col("ID",           90,  (Etudiant e) -> new SimpleStringProperty(String.valueOf(e.getId()))));
        table.getColumns().add(col("N° Étudiant",  140, (Etudiant e) -> new SimpleStringProperty(e.getStudentNumber())));
        table.getColumns().add(col("Nom complet",  200, (Etudiant e) -> new SimpleStringProperty(e.getName())));
        table.getColumns().add(col("Email",        260, (Etudiant e) -> new SimpleStringProperty(e.getEmail())));
        table.getColumns().add(col("Cours inscrits",120,(Etudiant e) -> new SimpleStringProperty(String.valueOf(e.getCoursInscrits().size()))));
        VBox.setVgrow(table, Priority.ALWAYS);

        GridPane form = form();
        TextField fId    = field("Ex: 104");
        TextField fName  = field("Nom complet");
        TextField fEmail = field("email@etu.enspd.cm");
        TextField fNum   = field("Ex: 23G0001");
        Label     fLbl   = formTitle("Ajouter un étudiant");
        formRow(form, 0, "ID (entier) :", fId);
        formRow(form, 1, "Nom complet :", fName);
        formRow(form, 2, "Email :",       fEmail);
        formRow(form, 3, "N° Étudiant :", fNum);

        Button btnAdd  = btn("➕  Ajouter",     C_ACCENT);
        Button btnDel  = btn("🗑  Supprimer",   C_DANGER);
        Button btnSave = btn("💾  Enregistrer",  C_ACCENT);
        Button btnCanc = btn("✖  Annuler",      C_MUTED);

        btnAdd.setOnAction(e -> { fLbl.setText("Ajouter un étudiant"); clearFields(fId, fName, fEmail, fNum); fId.setDisable(false); showForm(form); });
        btnDel.setOnAction(e -> {
            Etudiant sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { alert("Sélectionnez un étudiant."); return; }
            if (confirm("Supprimer « " + sel.getName() + " » ?")) etudiantsList.remove(sel);
        });
        btnSave.setOnAction(e -> {
            try {
                if (fName.getText().trim().isEmpty()) { alert("Le nom est requis."); return; }
                etudiantsList.add(new Etudiant(
                    Integer.parseInt(fId.getText().trim()),
                    fName.getText().trim(), fEmail.getText().trim(), fNum.getText().trim()));
                hideForm(form);
            } catch (NumberFormatException ex) { alert("L'ID doit être un entier."); }
        });
        btnCanc.setOnAction(e -> hideForm(form));
        form.add(btnBar(btnSave, btnCanc), 0, 4, 4, 1);

        page.getChildren().addAll(btnBar(btnAdd, btnDel), table, fLbl, form);
        setContent(page);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  ENSEIGNANTS
    // ═════════════════════════════════════════════════════════════════════════
    private void showEnseignants() {
        VBox page = page();
        page.getChildren().add(titleRow("◈  Enseignants", "Gestion du corps enseignant"));

        TableView<Enseignant> table = new TableView<>(enseignantsList);
        styleTable(table);
                table.getColumns().add(col("ID",          90,  (Enseignant e) -> new SimpleStringProperty(String.valueOf(e.getId()))));
        table.getColumns().add(col("Matricule",   130, (Enseignant e) -> new SimpleStringProperty(e.getEmployeeId())));
        table.getColumns().add(col("Nom",         210, (Enseignant e) -> new SimpleStringProperty(e.getName())));
        table.getColumns().add(col("Email",       260, (Enseignant e) -> new SimpleStringProperty(e.getEmail())));
        table.getColumns().add(col("Cours assignés",120,(Enseignant e) -> new SimpleStringProperty(String.valueOf(e.getAssignedCourses().size()))));
        VBox.setVgrow(table, Priority.ALWAYS);

        GridPane form = form();
        TextField fId    = field("Ex: 3");
        TextField fName  = field("Nom complet");
        TextField fEmail = field("email@enspd.cm");
        TextField fEmp   = field("Ex: EMP003");
        Label     fLbl   = formTitle("Ajouter un enseignant");
        formRow(form, 0, "ID (entier) :",   fId);
        formRow(form, 1, "Nom complet :",   fName);
        formRow(form, 2, "Email :",         fEmail);
        formRow(form, 3, "Matricule emp. :", fEmp);

        Button btnAdd  = btn("➕  Ajouter",    C_BLUE);
        Button btnDel  = btn("🗑  Supprimer",  C_DANGER);
        Button btnSave = btn("💾  Enregistrer", C_BLUE);
        Button btnCanc = btn("✖  Annuler",     C_MUTED);

        btnAdd.setOnAction(e -> { fLbl.setText("Ajouter un enseignant"); clearFields(fId,fName,fEmail,fEmp); fId.setDisable(false); showForm(form); });
        btnDel.setOnAction(e -> {
            Enseignant sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { alert("Sélectionnez un enseignant."); return; }
            if (confirm("Supprimer « " + sel.getName() + " » ?")) enseignantsList.remove(sel);
        });
        btnSave.setOnAction(e -> {
            try {
                if (fName.getText().trim().isEmpty()) { alert("Le nom est requis."); return; }
                enseignantsList.add(new Enseignant(
                    Integer.parseInt(fId.getText().trim()),
                    fName.getText().trim(), fEmail.getText().trim(), fEmp.getText().trim()));
                hideForm(form);
            } catch (NumberFormatException ex) { alert("L'ID doit être un entier."); }
        });
        btnCanc.setOnAction(e -> hideForm(form));
        form.add(btnBar(btnSave, btnCanc), 0, 4, 4, 1);

        page.getChildren().addAll(btnBar(btnAdd, btnDel), table, fLbl, form);
        setContent(page);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  COURS
    // ═════════════════════════════════════════════════════════════════════════
    private void showCours() {
        VBox page = page();
        page.getChildren().add(titleRow("◉  Cours", "Gestion de l'offre de formation"));

        TableView<Cours> table = new TableView<>(coursList);
        styleTable(table);
                table.getColumns().add(col("Code",         120, (Cours c) -> new SimpleStringProperty(c.getCode())));
        table.getColumns().add(col("Titre",        240, (Cours c) -> new SimpleStringProperty(c.getTitle())));
        table.getColumns().add(col("Capacité",     90,  (Cours c) -> new SimpleStringProperty(String.valueOf(c.getCapacite()))));
        table.getColumns().add(col("Inscrits",     90,  (Cours c) -> new SimpleStringProperty(String.valueOf(c.getEtudiants().size()))));
        table.getColumns().add(col("Enseignant",   210, (Cours c) -> new SimpleStringProperty(c.getEnseignantName())));
        VBox.setVgrow(table, Priority.ALWAYS);

        GridPane form = form();
        TextField fCode  = field("Ex: INFO-301");
        TextField fTitle2= field("Intitulé du cours");
        TextField fCap   = field("Ex: 50");
        ComboBox<Enseignant> cbEns = combo(enseignantsList);
        Label fLbl = formTitle("Ajouter un cours");
        formRow(form, 0, "Code :",       fCode);
        formRow(form, 1, "Titre :",      fTitle2);
        formRow(form, 2, "Capacité :",   fCap);
        formRow(form, 3, "Enseignant :", cbEns);

        Button btnAdd   = btn("➕  Ajouter",          C_PURPLE);
        Button btnDel   = btn("🗑  Supprimer",        C_DANGER);
        Button btnInscr = btn("🎓  Inscrire étudiant", C_BLUE);
        Button btnSave  = btn("💾  Enregistrer",       C_PURPLE);
        Button btnCanc  = btn("✖  Annuler",           C_MUTED);

        btnAdd.setOnAction(e -> { fLbl.setText("Ajouter un cours"); clearFields(fCode,fTitle2,fCap); showForm(form); });
        btnDel.setOnAction(e -> {
            Cours sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { alert("Sélectionnez un cours."); return; }
            if (confirm("Supprimer « " + sel.getTitle() + " » ?")) coursList.remove(sel);
        });
        btnInscr.setOnAction(e -> {
            Cours sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { alert("Sélectionnez un cours."); return; }
            Etudiant etu = choiceDialog("Inscrire dans « " + sel.getTitle() + " »", "Étudiant :", etudiantsList);
            if (etu == null) return;
            try { etu.inscrire(sel); table.refresh(); }
            catch (CapaciteDepasseeException ex) { alert(ex.getMessage()); }
        });
        btnSave.setOnAction(e -> {
            try {
                if (fCode.getText().trim().isEmpty() || fTitle2.getText().trim().isEmpty()) { alert("Code et titre requis."); return; }
                int cap = Integer.parseInt(fCap.getText().trim());
                Cours c = new Cours(fCode.getText().trim(), fTitle2.getText().trim(), cap, cbEns.getValue());
                if (cbEns.getValue() != null) cbEns.getValue().assignCourse(c);
                coursList.add(c);
                hideForm(form);
            } catch (NumberFormatException ex) { alert("Capacité invalide."); }
        });
        btnCanc.setOnAction(e -> hideForm(form));
        form.add(btnBar(btnSave, btnCanc), 0, 4, 4, 1);

        page.getChildren().addAll(btnBar(btnAdd, btnDel, btnInscr), table, fLbl, form);
        setContent(page);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  GROUPES
    // ═════════════════════════════════════════════════════════════════════════
    private void showGroupes() {
        VBox page = page();
        page.getChildren().add(titleRow("⊞  Groupes", "Gestion des groupes de cours"));

        TableView<Groupe> table = new TableView<>(groupesList);
        styleTable(table);
                table.getColumns().add(col("ID Groupe",  140, (Groupe g) -> new SimpleStringProperty(g.getGroupId())));
        table.getColumns().add(col("Capacité",   100, (Groupe g) -> new SimpleStringProperty(String.valueOf(g.getCapaciteMax()))));
        table.getColumns().add(col("Inscrits",   100, (Groupe g) -> new SimpleStringProperty(String.valueOf(g.getNbEtudiants()))));
        table.getColumns().add(col("Enseignant", 250, (Groupe g) -> new SimpleStringProperty(g.getEnseignantName())));
        VBox.setVgrow(table, Priority.ALWAYS);

        GridPane form = form();
        TextField fGid = field("Ex: TD2");
        TextField fCap = field("Ex: 30");
        ComboBox<Enseignant> cbEns = combo(enseignantsList);
        Label fLbl = formTitle("Créer un groupe");
        formRow(form, 0, "ID Groupe :",    fGid);
        formRow(form, 1, "Capacité max :", fCap);
        formRow(form, 2, "Enseignant :",   cbEns);

        Button btnAdd   = btn("➕  Créer groupe",     C_WARNING);
        Button btnDel   = btn("🗑  Supprimer",        C_DANGER);
        Button btnInscr = btn("🎓  Ajouter étudiant", C_ACCENT);
        Button btnSave  = btn("💾  Enregistrer",       C_WARNING);
        Button btnCanc  = btn("✖  Annuler",           C_MUTED);

        btnAdd.setOnAction(e -> { fLbl.setText("Créer un groupe"); clearFields(fGid, fCap); showForm(form); });
        btnDel.setOnAction(e -> {
            Groupe sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { alert("Sélectionnez un groupe."); return; }
            if (confirm("Supprimer « " + sel.getGroupId() + " » ?")) groupesList.remove(sel);
        });
        btnInscr.setOnAction(e -> {
            Groupe sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { alert("Sélectionnez un groupe."); return; }
            Etudiant etu = choiceDialog("Ajouter dans « " + sel.getGroupId() + " »", "Étudiant :", etudiantsList);
            if (etu == null) return;
            try { sel.addEtudiant(etu); table.refresh(); }
            catch (CapaciteDepasseeException ex) { alert(ex.getMessage()); }
        });
        btnSave.setOnAction(e -> {
            try {
                if (fGid.getText().trim().isEmpty()) { alert("L'ID groupe est requis."); return; }
                groupesList.add(new Groupe(fGid.getText().trim(),
                        Integer.parseInt(fCap.getText().trim()), cbEns.getValue()));
                hideForm(form);
            } catch (NumberFormatException ex) { alert("Capacité invalide."); }
        });
        btnCanc.setOnAction(e -> hideForm(form));
        form.add(btnBar(btnSave, btnCanc), 0, 3, 4, 1);

        page.getChildren().addAll(btnBar(btnAdd, btnDel, btnInscr), table, fLbl, form);
        setContent(page);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  PLANNING
    // ═════════════════════════════════════════════════════════════════════════
    private void showPlanning() {
        VBox page = page();
        page.getChildren().add(titleRow("◷  Séances", "Planning et détection des conflits horaires"));

        TableView<Seance> table = buildSeanceTable();
        VBox.setVgrow(table, Priority.ALWAYS);

        // Conflict display
        TextArea conflictArea = new TextArea();
        conflictArea.setEditable(false);
        conflictArea.setPrefHeight(80);
        conflictArea.setVisible(false);
        conflictArea.setManaged(false);
        conflictArea.setStyle("-fx-control-inner-background:#2D1B0E;-fx-text-fill:" + C_WARNING
                + ";-fx-font-size:12px;-fx-background-radius:8;-fx-border-color:" + C_WARNING
                + "44;-fx-border-radius:8;-fx-border-width:1;");

        GridPane form = form();
        TextField fId    = field("Ex: S004");
        TextField fSalle = field("Ex: Amphi A");
        TextField fJour  = field("Ex: Lundi");
        TextField fDebut = field("HH:mm");
        TextField fFin   = field("HH:mm");
        ComboBox<Groupe>     cbGrp = combo(groupesList);
        ComboBox<Enseignant> cbEns = combo(enseignantsList);
        ComboBox<Cours>      cbCrs = combo(coursList);
        Label fLbl = formTitle("Créer une séance");

        formRow(form, 0, "ID Séance :",    fId);
        formRow(form, 1, "Groupe :",        cbGrp);
        formRow(form, 2, "Enseignant :",    cbEns);
        formRow(form, 3, "Cours :",         cbCrs);
        formRow(form, 4, "Salle :",         fSalle);
        formRow(form, 5, "Jour :",          fJour);
        formRow(form, 6, "Début (HH:mm) :", fDebut);
        formRow(form, 7, "Fin   (HH:mm) :", fFin);

        Button btnNew   = btn("➕  Nouvelle séance",   C_GREEN);
        Button btnDel   = btn("🗑  Supprimer",         C_DANGER);
        Button btnCheck = btn("⚠  Vérifier conflits",  C_WARNING);
        Button btnSave  = btn("💾  Enregistrer",        C_GREEN);
        Button btnCanc  = btn("✖  Annuler",            C_MUTED);

        btnNew.setOnAction(e -> {
            fLbl.setText("Créer une séance");
            clearFields(fId, fSalle, fJour, fDebut, fFin);
            conflictArea.setVisible(false); conflictArea.setManaged(false);
            showForm(form);
        });

        btnDel.setOnAction(e -> {
            Seance sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { alert("Sélectionnez une séance."); return; }
            if (confirm("Supprimer la séance « " + sel.getId() + " » ?")) {
                planning.removeSeance(sel);
                seancesList.remove(sel);
            }
        });

        btnCheck.setOnAction(e -> {
            List<Seance> all = planning.getSeances();
            StringBuilder sb = new StringBuilder();
            boolean found = false;
            for (int i = 0; i < all.size(); i++) {
                for (int j = i + 1; j < all.size(); j++) {
                    Seance a = all.get(i), b = all.get(j);
                    if (!a.overlapsWith(b)) continue;
                    if (a.getSalle().equals(b.getSalle())) {
                        sb.append("❌ Salle [").append(a.getId()).append("] & [").append(b.getId()).append("] – ").append(a.getSalle()).append("\n");
                        found = true;
                    }
                    if (a.getEnseignant() != null && b.getEnseignant() != null
                            && a.getEnseignant().getId() == b.getEnseignant().getId()) {
                        sb.append("❌ Enseignant [").append(a.getId()).append("] & [").append(b.getId()).append("] – ").append(a.getEnseignantName()).append("\n");
                        found = true;
                    }
                    if (a.getGroupe() != null && b.getGroupe() != null
                            && a.getGroupe().getGroupId().equals(b.getGroupe().getGroupId())) {
                        sb.append("❌ Groupe [").append(a.getId()).append("] & [").append(b.getId()).append("] – ").append(a.getGroupeId()).append("\n");
                        found = true;
                    }
                }
            }
            conflictArea.setText(found ? sb.toString() : "✅ Aucun conflit détecté dans le planning.");
            conflictArea.setVisible(true); conflictArea.setManaged(true);
        });

        btnSave.setOnAction(e -> {
            if (fId.getText().trim().isEmpty() || fSalle.getText().trim().isEmpty()
                    || fJour.getText().trim().isEmpty() || fDebut.getText().trim().isEmpty() || fFin.getText().trim().isEmpty()) {
                alert("Tous les champs sont requis."); return;
            }
            if (fDebut.getText().compareTo(fFin.getText()) >= 0) {
                alert("L'heure de fin doit être après l'heure de début."); return;
            }
            Seance s = new Seance(fId.getText().trim(), cbGrp.getValue(), cbEns.getValue(),
                    cbCrs.getValue(), fSalle.getText().trim(),
                    fJour.getText().trim(), fDebut.getText().trim(), fFin.getText().trim());
            try {
                planning.addSeance(s);
                seancesList.add(s);
                hideForm(form);
                conflictArea.setVisible(false); conflictArea.setManaged(false);
            } catch (ConflitHoraireException ex) {
                conflictArea.setText("⚠ Conflit détecté !\n" + ex.getMessage());
                conflictArea.setVisible(true); conflictArea.setManaged(true);
            }
        });

        btnCanc.setOnAction(e -> { hideForm(form); conflictArea.setVisible(false); conflictArea.setManaged(false); });
        form.add(btnBar(btnSave, btnCanc), 0, 8, 4, 1);

        page.getChildren().addAll(btnBar(btnNew, btnDel, btnCheck), conflictArea, table, fLbl, form);
        setContent(page);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  SHARED SEANCE TABLE
    // ═════════════════════════════════════════════════════════════════════════
    private TableView<Seance> buildSeanceTable() {
        TableView<Seance> table = new TableView<>(seancesList);
        styleTable(table);
                table.getColumns().add(col("ID",          80,  (Seance s) -> new SimpleStringProperty(s.getId())));
        table.getColumns().add(col("Jour",        110, (Seance s) -> new SimpleStringProperty(s.getJour())));
        table.getColumns().add(col("Début",       80,  (Seance s) -> new SimpleStringProperty(s.getDebut())));
        table.getColumns().add(col("Fin",         80,  (Seance s) -> new SimpleStringProperty(s.getFin())));
        table.getColumns().add(col("Groupe",      110, (Seance s) -> new SimpleStringProperty(s.getGroupeId())));
        table.getColumns().add(col("Enseignant",  180, (Seance s) -> new SimpleStringProperty(s.getEnseignantName())));
        table.getColumns().add(col("Cours",       180, (Seance s) -> new SimpleStringProperty(s.getCoursTitle())));
        table.getColumns().add(col("Salle",       110, (Seance s) -> new SimpleStringProperty(s.getSalle())));
        return table;
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  SAMPLE DATA
    // ═════════════════════════════════════════════════════════════════════════
    private void chargerDonnees() {
        Enseignant e1 = new Enseignant(1, "Armielle Noulapeu", "a.noulapeu@enspd.cm", "EMP001");
        Enseignant e2 = new Enseignant(2, "Jean Mballa",       "j.mballa@enspd.cm",   "EMP002");
        enseignantsList.addAll(e1, e2);

        Etudiant s1 = new Etudiant(101, "Alice Fouda",   "a.fouda@etu.enspd.cm",  "21G0001");
        Etudiant s2 = new Etudiant(102, "Boris Nkomo",   "b.nkomo@etu.enspd.cm",  "21G0002");
        Etudiant s3 = new Etudiant(103, "Carole Biyong", "c.biyong@etu.enspd.cm", "22G0001");
        etudiantsList.addAll(s1, s2, s3);

        Cours c1 = new Cours("INFO-301", "Programmation Orientée Objet", 50, e1);
        Cours c2 = new Cours("MATH-201", "Analyse Numérique",            30, e2);
        coursList.addAll(c1, c2);
        e1.assignCourse(c1); e2.assignCourse(c2);

        Groupe g1 = new Groupe("CM-INFO",   100, e1);
        Groupe g2 = new Groupe("TD1-INFO",   30, e1);
        try { g1.addEtudiant(s1); g1.addEtudiant(s2); g2.addEtudiant(s1); }
        catch (CapaciteDepasseeException ignored) {}
        groupesList.addAll(g1, g2);

        try {
            Seance se1 = new Seance("S001", g1, e1, c1, "Amphi A",    "Lundi",    "08:00", "10:00");
            Seance se2 = new Seance("S002", g2, e1, c1, "Salle 402",  "Lundi",    "10:30", "12:30");
            Seance se3 = new Seance("S003", g1, e2, c2, "Amphi A",    "Mercredi", "08:00", "10:00");
            planning.addSeance(se1); planning.addSeance(se2); planning.addSeance(se3);
            seancesList.addAll(se1, se2, se3);
        } catch (ConflitHoraireException ignored) {}
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  UI HELPERS
    // ═════════════════════════════════════════════════════════════════════════

    private VBox page() {
        VBox v = new VBox(16);
        v.setPadding(new Insets(28));
        v.setStyle("-fx-background-color:" + C_DARK + ";");
        VBox.setVgrow(v, Priority.ALWAYS);
        return v;
    }

    private void setContent(Node node) { contentArea.getChildren().setAll(node); }

    private HBox titleRow(String title, String subtitle) {
        Label t = new Label(title);
        t.setStyle("-fx-text-fill:" + C_TEXT + ";-fx-font-size:22px;-fx-font-weight:bold;-fx-font-family:'Segoe UI';");
        Label s = new Label(subtitle);
        s.setStyle("-fx-text-fill:" + C_MUTED + ";-fx-font-size:13px;-fx-font-family:'Segoe UI';");
        VBox vb = new VBox(3, t, s);
        return new HBox(vb);
    }

    private Label sectionLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill:" + C_TEXT + ";-fx-font-size:15px;-fx-font-weight:bold;-fx-font-family:'Segoe UI';");
        return l;
    }

    private Label formTitle(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill:" + C_ACCENT + ";-fx-font-size:15px;-fx-font-weight:bold;"
                + "-fx-font-family:'Segoe UI';-fx-padding:10 0 4 0;");
        return l;
    }

    private <T> void styleTable(TableView<T> table) {
        table.setStyle("-fx-background-color:" + C_CARD + ";-fx-border-color:" + C_BORDER
                + ";-fx-border-radius:8;-fx-background-radius:8;-fx-table-cell-border-color:" + C_BORDER + ";");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN); // Java 22 API
        table.setFixedCellSize(38);
        table.setPrefHeight(260);
    }

    @FunctionalInterface
    interface ColFactory<T> { ObservableValue<String> get(T item); }

    private <T> TableColumn<T, String> col(String title, double w, ColFactory<T> f) {
        TableColumn<T, String> col = new TableColumn<>(title);
        col.setPrefWidth(w);
        col.setCellValueFactory(d -> f.get(d.getValue()));
        col.setStyle("-fx-text-fill:" + C_TEXT + ";-fx-font-family:'Segoe UI';");
        return col;
    }

    private GridPane form() {
        GridPane g = new GridPane();
        g.setHgap(14); g.setVgap(10);
        g.setVisible(false); g.setManaged(false);
        g.setStyle("-fx-background-color:" + C_CARD + ";-fx-padding:18;"
                + "-fx-background-radius:10;-fx-border-color:" + C_BORDER
                + ";-fx-border-radius:10;-fx-border-width:1;");
        return g;
    }

    private void formRow(GridPane form, int row, String label, javafx.scene.Node input) {
        Label l = new Label(label);
        l.setMinWidth(140);
        l.setStyle("-fx-text-fill:" + C_MUTED + ";-fx-font-size:12px;"
                + "-fx-font-family:'Segoe UI';-fx-font-weight:bold;");
        form.add(l, 0, row);
        form.add(input, 1, row);
        GridPane.setHgrow(input, Priority.ALWAYS);
    }

    private TextField field(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefWidth(220);
        tf.setStyle("-fx-background-color:#0F1923;-fx-text-fill:" + C_TEXT
                + ";-fx-prompt-text-fill:" + C_MUTED
                + ";-fx-border-color:" + C_BORDER
                + ";-fx-border-radius:6;-fx-background-radius:6;-fx-padding:7 10 7 10;"
                + "-fx-font-family:'Segoe UI';");
        return tf;
    }

    private <T> ComboBox<T> combo(ObservableList<T> items) {
        ComboBox<T> cb = new ComboBox<>(items);
        cb.setPrefWidth(220);
        cb.setStyle("-fx-background-color:#0F1923;-fx-text-fill:" + C_TEXT
                + ";-fx-border-color:" + C_BORDER
                + ";-fx-border-radius:6;-fx-background-radius:6;-fx-font-family:'Segoe UI';");
        return cb;
    }

    private Button btn(String text, String color) {
        String base = "-fx-background-color:" + color + "22;-fx-text-fill:" + color
                + ";-fx-border-color:" + color + "44;-fx-border-radius:6;"
                + "-fx-background-radius:6;-fx-padding:8 16 8 16;"
                + "-fx-font-family:'Segoe UI';-fx-font-weight:bold;-fx-cursor:hand;";
        String hover = "-fx-background-color:" + color + "44;-fx-text-fill:" + color
                + ";-fx-border-color:" + color + "88;-fx-border-radius:6;"
                + "-fx-background-radius:6;-fx-padding:8 16 8 16;"
                + "-fx-font-family:'Segoe UI';-fx-font-weight:bold;-fx-cursor:hand;";
        Button b = new Button(text);
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e  -> b.setStyle(base));
        return b;
    }

    private HBox btnBar(Button... buttons) {
        HBox hb = new HBox(10);
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.getChildren().addAll(buttons);
        return hb;
    }

    private void showForm(GridPane form) { form.setVisible(true);  form.setManaged(true);  }
    private void hideForm(GridPane form) { form.setVisible(false); form.setManaged(false); }

    private void clearFields(TextField... fields) { for (TextField f : fields) f.clear(); }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.getDialogPane().setStyle("-fx-background-color:" + C_CARD + ";-fx-text-fill:" + C_TEXT + ";");
        a.showAndWait();
    }

    private boolean confirm(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.YES, ButtonType.NO);
        a.setHeaderText(null);
        a.getDialogPane().setStyle("-fx-background-color:" + C_CARD + ";-fx-text-fill:" + C_TEXT + ";");
        return a.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    private <T> T choiceDialog(String title, String label, ObservableList<T> items) {
        if (items.isEmpty()) { alert("Aucun élément disponible."); return null; }
        ChoiceDialog<T> dlg = new ChoiceDialog<>(items.get(0), items);
        dlg.setTitle(title); dlg.setHeaderText(null); dlg.setContentText(label);
        dlg.getDialogPane().setStyle("-fx-background-color:" + C_CARD + ";");
        return dlg.showAndWait().orElse(null);
    }

    public static void main(String[] args) { launch(); }
}