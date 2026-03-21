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
import java.io.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javafx.stage.FileChooser;

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

    // ── Palette — Bleu Professionnel (fond clair, texte sombre, doux pour les yeux) ──
    private static final String C_DARK    = "#F0F4F8";   // fond principal : gris-bleu très clair
    private static final String C_SIDEBAR = "#FFFFFF";   // sidebar : blanc pur
    private static final String C_ACCENT  = "#2563EB";   // accent principal : bleu franc
    private static final String C_BLUE    = "#3B82F6";   // bleu secondaire
    private static final String C_PURPLE  = "#7C3AED";   // violet doux
    private static final String C_DANGER  = "#DC2626";   // rouge sobre
    private static final String C_WARNING = "#D97706";   // ambre sobre
    private static final String C_GREEN   = "#059669";   // vert sobre
    private static final String C_TEXT    = "#1E293B";   // texte principal : quasi-noir doux
    private static final String C_MUTED   = "#64748B";   // texte secondaire : gris-bleu
    private static final String C_CARD    = "#FFFFFF";   // cartes : blanc
    private static final String C_BORDER  = "#CBD5E1";   // bordures : gris-bleu clair

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
        Button btnNts  = navBtn("  ✎  Notes",           this::showNotes);

        side.getChildren().addAll(
            navSection("NAVIGATION"), btnDash,
            navSection("ACTEURS"),    btnEtu, btnEns,
            navSection("FORMATION"),  btnCrs, btnGrp,
            navSection("PLANNING"),   btnPln,
            navSection("ÉVALUATION"), btnNts
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
                table.getColumns().add(col("ID",           90,  (Etudiant e) -> new SimpleStringProperty(e.getId())));
        table.getColumns().add(col("Matricule",    140, (Etudiant e) -> new SimpleStringProperty(e.getMatricule())));
        table.getColumns().add(col("Nom complet",  200, (Etudiant e) -> new SimpleStringProperty(e.getName())));
        table.getColumns().add(col("Email",        260, (Etudiant e) -> new SimpleStringProperty(e.getAdresseMail())));
        table.getColumns().add(col("Inscriptions", 120, (Etudiant e) -> new SimpleStringProperty(String.valueOf(e.getInscriptions().size()))));
        VBox.setVgrow(table, Priority.ALWAYS);

        GridPane form = form();
        TextField fNom     = field("Nom de famille");
        TextField fPrenom  = field("Prénom");
        TextField fEmail   = field("email@etu.enspd.cm");
        TextField fDate    = field("YYYY-MM-DD");
        TextField fNum     = field("Ex: 23G0001");
        TextField fNiveau  = field("Ex: Niveau 2");
        TextField fFiliere = field("Ex: Génie Logiciel");
        Label     fLbl     = formTitle("Ajouter un étudiant");
        formRow(form, 0, "Nom :",        fNom);
        formRow(form, 1, "Prénom :",     fPrenom);
        formRow(form, 2, "Email :",      fEmail);
        formRow(form, 3, "Naissance :",  fDate);
        formRow(form, 4, "Matricule :",  fNum);
        formRow(form, 5, "Niveau :",     fNiveau);
        formRow(form, 6, "Filière :",    fFiliere);

        Button btnAdd  = btn("➕  Ajouter",     C_ACCENT);
        Button btnDel  = btn("🗑  Supprimer",   C_DANGER);
        Button btnSave = btn("💾  Enregistrer",  C_ACCENT);
        Button btnCanc = btn("✖  Annuler",      C_MUTED);

        btnAdd.setOnAction(e -> { fLbl.setText("Ajouter un étudiant"); clearFields(fNom, fPrenom, fEmail, fDate, fNum, fNiveau, fFiliere); showForm(form); });
        btnDel.setOnAction(e -> {
            Etudiant sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { alert("Sélectionnez un étudiant."); return; }
            if (confirm("Supprimer « " + sel.getName() + " » ?")) etudiantsList.remove(sel);
        });
        btnSave.setOnAction(e -> {
            try {
                if (fNom.getText().trim().isEmpty() || fPrenom.getText().trim().isEmpty()) { alert("Nom et prénom requis."); return; }
                java.time.LocalDate date = java.time.LocalDate.parse(fDate.getText().trim());
                etudiantsList.add(new Etudiant(
                    fNom.getText().trim(), fPrenom.getText().trim(),
                    fEmail.getText().trim(), date,
                    fNum.getText().trim(), fNiveau.getText().trim(), fFiliere.getText().trim()));
                hideForm(form);
            } catch (java.time.format.DateTimeParseException ex) { alert("Date invalide. Format : YYYY-MM-DD"); }
              catch (IllegalArgumentException ex) { alert(ex.getMessage()); }
        });
        btnCanc.setOnAction(e -> hideForm(form));
        form.add(btnBar(btnSave, btnCanc), 0, 7, 4, 1);

        Button btnExport = btn("⬇  Exporter PDF", C_GREEN);
        btnExport.setOnAction(e -> {
            if (etudiantsList.isEmpty()) { alert("Aucun étudiant à exporter."); return; }
            FileChooser fc = new FileChooser();
            fc.setTitle("Exporter la liste des étudiants");
            fc.setInitialFileName("liste_etudiants.pdf");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            java.io.File dest = fc.showSaveDialog(contentArea.getScene().getWindow());
            if (dest == null) return;
            try {
                List<String> headers = java.util.Arrays.asList("ID", "Matricule", "Nom complet", "Email", "Niveau", "Filière", "Inscriptions");
                List<List<String>> rows = new java.util.ArrayList<>();
                for (Etudiant et : etudiantsList)
                    rows.add(java.util.Arrays.asList(et.getId(), et.getMatricule(), et.getName(), et.getAdresseMail(), et.getAnneeEtude(), et.getFiliere(), String.valueOf(et.getInscriptions().size())));
                exporterListePDF("LISTE DES ÉTUDIANTS", headers, rows, dest);
                alert("✅ PDF enregistré :\n" + dest.getAbsolutePath());
            } catch (Exception ex) { alert("Erreur PDF :\n" + ex.getMessage()); }
        });

        page.getChildren().addAll(btnBar(btnAdd, btnDel, btnExport), table, fLbl, form);
        setContent(page);
    }

    private void showEnseignants() {
        VBox page = page();
        page.getChildren().add(titleRow("◈  Enseignants", "Gestion du corps enseignant"));

        TableView<Enseignant> table = new TableView<>(enseignantsList);
        styleTable(table);
                table.getColumns().add(col("ID",          90,  (Enseignant e) -> new SimpleStringProperty(e.getId())));
        table.getColumns().add(col("Statut",      130, (Enseignant e) -> new SimpleStringProperty(e.getStatut())));
        table.getColumns().add(col("Nom",         210, (Enseignant e) -> new SimpleStringProperty(e.getName())));
        table.getColumns().add(col("Email",       260, (Enseignant e) -> new SimpleStringProperty(e.getAdresseMail())));
        table.getColumns().add(col("Département", 180, (Enseignant e) -> new SimpleStringProperty(e.getDepartement())));
        VBox.setVgrow(table, Priority.ALWAYS);

        GridPane form = form();
        TextField fNom   = field("Nom de famille");
        TextField fPrenom= field("Prénom");
        TextField fEmail = field("email@enspd.cm");
        TextField fDate  = field("YYYY-MM-DD");
        TextField fStatut= field("Permanent / Vacataire");
        TextField fDept  = field("Ex: Informatique");
        Label     fLbl   = formTitle("Ajouter un enseignant");
        formRow(form, 0, "Nom :",         fNom);
        formRow(form, 1, "Prénom :",      fPrenom);
        formRow(form, 2, "Email :",       fEmail);
        formRow(form, 3, "Naissance :",   fDate);
        formRow(form, 4, "Statut :",      fStatut);
        formRow(form, 5, "Département :", fDept);

        Button btnAdd  = btn("➕  Ajouter",    C_BLUE);
        Button btnDel  = btn("🗑  Supprimer",  C_DANGER);
        Button btnSave = btn("💾  Enregistrer", C_BLUE);
        Button btnCanc = btn("✖  Annuler",     C_MUTED);

        btnAdd.setOnAction(e -> { fLbl.setText("Ajouter un enseignant"); clearFields(fNom,fPrenom,fEmail,fDate,fStatut,fDept); showForm(form); });
        btnDel.setOnAction(e -> {
            Enseignant sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { alert("Sélectionnez un enseignant."); return; }
            if (confirm("Supprimer « " + sel.getName() + " » ?")) enseignantsList.remove(sel);
        });
        btnSave.setOnAction(e -> {
            try {
                if (fNom.getText().trim().isEmpty() || fPrenom.getText().trim().isEmpty()) { alert("Nom et prénom requis."); return; }
                java.time.LocalDate date = java.time.LocalDate.parse(fDate.getText().trim());
                enseignantsList.add(new Enseignant(
                    fNom.getText().trim(), fPrenom.getText().trim(),
                    fEmail.getText().trim(), date,
                    fStatut.getText().trim(), fDept.getText().trim()));
                hideForm(form);
            } catch (java.time.format.DateTimeParseException ex) { alert("Date invalide. Format : YYYY-MM-DD"); }
              catch (IllegalArgumentException ex) { alert(ex.getMessage()); }
        });
        btnCanc.setOnAction(e -> hideForm(form));
        form.add(btnBar(btnSave, btnCanc), 0, 6, 4, 1);

        Button btnExport = btn("⬇  Exporter PDF", C_GREEN);
        btnExport.setOnAction(e -> {
            if (enseignantsList.isEmpty()) { alert("Aucun enseignant à exporter."); return; }
            FileChooser fc = new FileChooser();
            fc.setTitle("Exporter la liste des enseignants");
            fc.setInitialFileName("liste_enseignants.pdf");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            java.io.File dest = fc.showSaveDialog(contentArea.getScene().getWindow());
            if (dest == null) return;
            try {
                List<String> headers = java.util.Arrays.asList("ID", "Nom complet", "Email", "Statut", "Département", "Cours");
                List<List<String>> rows = new java.util.ArrayList<>();
                for (Enseignant en : enseignantsList)
                    rows.add(java.util.Arrays.asList(en.getId(), en.getName(), en.getAdresseMail(), en.getStatut(), en.getDepartement(), String.valueOf(en.getCoursEnseignes().size())));
                exporterListePDF("LISTE DES ENSEIGNANTS", headers, rows, dest);
                alert("✅ PDF enregistré :\n" + dest.getAbsolutePath());
            } catch (Exception ex) { alert("Erreur PDF :\n" + ex.getMessage()); }
        });

        page.getChildren().addAll(btnBar(btnAdd, btnDel, btnExport), table, fLbl, form);
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
            if (groupesList.isEmpty()) { alert("Créez d'abord un groupe avant d'inscrire un étudiant."); return; }
            Etudiant etu = choiceDialog("Inscrire dans « " + sel.getTitle() + " » — Étape 1/2", "Étudiant :", etudiantsList);
            if (etu == null) return;
            Groupe grp = choiceDialog("Inscrire dans « " + sel.getTitle() + " » — Étape 2/2", "Groupe :", groupesList);
            if (grp == null) return;
            try {
                // 1. ajoute dans Cours.etudiants (contrôle capacité)
                sel.addEtudiant(etu);
                // 2. ajoute dans Groupe.etudiants
                grp.addEtudiant(etu);
                // 3. crée l'Inscription et la lie à l'étudiant → colonne Inscriptions se met à jour
                core.actors.Inscription ins = new core.actors.Inscription(etu, grp);
                etu.addInscription(ins);
                // 4. forcer refresh des tables
                int idxC = coursList.indexOf(sel);
                if (idxC >= 0) coursList.set(idxC, sel);
                int idxG = groupesList.indexOf(grp);
                if (idxG >= 0) groupesList.set(idxG, grp);
                int idxE = etudiantsList.indexOf(etu);
                if (idxE >= 0) etudiantsList.set(idxE, etu);
            }
            catch (CapaciteDepasseeException ex) { alert(ex.getMessage()); }
        });
        btnSave.setOnAction(e -> {
            try {
                if (fCode.getText().trim().isEmpty() || fTitle2.getText().trim().isEmpty()) { alert("Code et titre requis."); return; }
                int cap = Integer.parseInt(fCap.getText().trim());
                Cours c = new Cours(fCode.getText().trim(), fTitle2.getText().trim(), cap, cbEns.getValue());
                if (cbEns.getValue() != null) cbEns.getValue().addCourse(c);
                coursList.add(c);
                hideForm(form);
            } catch (NumberFormatException ex) { alert("Capacité invalide."); }
        });
        btnCanc.setOnAction(e -> hideForm(form));
        form.add(btnBar(btnSave, btnCanc), 0, 4, 4, 1);

        Button btnExportC = btn("⬇  Exporter PDF", C_GREEN);
        btnExportC.setOnAction(e -> {
            if (coursList.isEmpty()) { alert("Aucun cours à exporter."); return; }
            FileChooser fc = new FileChooser();
            fc.setTitle("Exporter la liste des cours");
            fc.setInitialFileName("liste_cours.pdf");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            java.io.File dest = fc.showSaveDialog(contentArea.getScene().getWindow());
            if (dest == null) return;
            try {
                List<String> headers = java.util.Arrays.asList("Code", "Titre", "Capacité", "Inscrits", "Enseignant");
                List<List<String>> rows = new java.util.ArrayList<>();
                for (Cours c : coursList)
                    rows.add(java.util.Arrays.asList(c.getCode(), c.getTitle(), String.valueOf(c.getCapacite()), String.valueOf(c.getEtudiants().size()), c.getEnseignantName()));
                exporterListePDF("LISTE DES COURS", headers, rows, dest);
                alert("✅ PDF enregistré :\n" + dest.getAbsolutePath());
            } catch (Exception ex) { alert("Erreur PDF :\n" + ex.getMessage()); }
        });

        page.getChildren().addAll(btnBar(btnAdd, btnDel, btnInscr, btnExportC), table, fLbl, form);
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
            try {
                sel.addEtudiant(etu);
                // créer l'Inscription et la lier à l'étudiant → met à jour la colonne "Inscriptions"
                core.actors.Inscription ins = new core.actors.Inscription(etu, sel);
                etu.addInscription(ins);
                // forcer le refresh des deux tables
                int idxG = groupesList.indexOf(sel);
                if (idxG >= 0) groupesList.set(idxG, sel);
                int idxE = etudiantsList.indexOf(etu);
                if (idxE >= 0) etudiantsList.set(idxE, etu);
            }
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

        Button btnExportG = btn("⬇  Exporter PDF", C_GREEN);
        btnExportG.setOnAction(e -> {
            if (groupesList.isEmpty()) { alert("Aucun groupe à exporter."); return; }
            FileChooser fc = new FileChooser();
            fc.setTitle("Exporter la liste des groupes");
            fc.setInitialFileName("liste_groupes.pdf");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            java.io.File dest = fc.showSaveDialog(contentArea.getScene().getWindow());
            if (dest == null) return;
            try {
                List<String> headers = java.util.Arrays.asList("ID Groupe", "Capacité max", "Inscrits", "Enseignant");
                List<List<String>> rows = new java.util.ArrayList<>();
                for (Groupe g : groupesList)
                    rows.add(java.util.Arrays.asList(g.getGroupId(), String.valueOf(g.getCapaciteMax()), String.valueOf(g.getNbEtudiants()), g.getEnseignantName()));
                exporterListePDF("LISTE DES GROUPES", headers, rows, dest);
                alert("✅ PDF enregistré :\n" + dest.getAbsolutePath());
            } catch (Exception ex) { alert("Erreur PDF :\n" + ex.getMessage()); }
        });

        page.getChildren().addAll(btnBar(btnAdd, btnDel, btnInscr, btnExportG), table, fLbl, form);
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
        conflictArea.setStyle("-fx-control-inner-background:#FEF3C7;-fx-text-fill:" + C_WARNING
                + ";-fx-font-size:12px;-fx-background-radius:8;-fx-border-color:" + C_WARNING
                + "44;-fx-border-radius:8;-fx-border-width:1;");

        GridPane form = form();
        TextField fId    = field("Ex: S004");
        TextField fSalle = field("Ex: Amphi A");
        TextField fJour  = field("Ex: 2026-03-23");
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
        formRow(form, 5, "Date :",         fJour);
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
                        sb.append("  Salle [").append(a.getId()).append("] & [").append(b.getId()).append("] – ").append(a.getSalle()).append("\n");
                        found = true;
                    }
                    if (a.getEnseignant() != null && b.getEnseignant() != null
                            && a.getEnseignant().getId() == b.getEnseignant().getId()) {
                        sb.append("  Enseignant [").append(a.getId()).append("] & [").append(b.getId()).append("] – ").append(a.getEnseignantName()).append("\n");
                        found = true;
                    }
                    if (a.getGroupe() != null && b.getGroupe() != null
                            && a.getGroupe().getGroupId().equals(b.getGroupe().getGroupId())) {
                        sb.append("  Groupe [").append(a.getId()).append("] & [").append(b.getId()).append("] – ").append(a.getGroupeId()).append("\n");
                        found = true;
                    }
                }
            }
            conflictArea.setText(found ? sb.toString() :   Aucun conflit détecté dans le planning.");
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
            try {
                java.time.LocalDate.parse(fJour.getText().trim());
            } catch (Exception ex) {
                alert("Date invalide. Format attendu : YYYY-MM-DD (ex: 2026-03-23).");
                return;
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

        Button btnExportP = btn("⬇  Exporter PDF", C_GREEN);
        btnExportP.setOnAction(e -> {
            if (seancesList.isEmpty()) { alert("Aucune séance à exporter."); return; }
            FileChooser fc = new FileChooser();
            fc.setTitle("Exporter le planning");
            fc.setInitialFileName("planning_seances.pdf");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            java.io.File dest = fc.showSaveDialog(contentArea.getScene().getWindow());
            if (dest == null) return;
            try {
                List<String> headers = java.util.Arrays.asList("ID", "Jour", "Début", "Fin", "Groupe", "Enseignant", "Cours", "Salle");
                List<List<String>> rows = new java.util.ArrayList<>();
                for (Seance s : seancesList)
                    rows.add(java.util.Arrays.asList(s.getId(), s.getJour(), s.getDebut(), s.getFin(), s.getGroupeId(), s.getEnseignantName(), s.getCoursTitle(), s.getSalle()));
                exporterListePDF("PLANNING DES SÉANCES", headers, rows, dest);
                alert("✅ PDF enregistré :\n" + dest.getAbsolutePath());
            } catch (Exception ex) { alert("Erreur PDF :\n" + ex.getMessage()); }
        });

        page.getChildren().addAll(btnBar(btnNew, btnDel, btnCheck, btnExportP), conflictArea, table, fLbl, form);
        setContent(page);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  NOTES
    // ═════════════════════════════════════════════════════════════════════════
    private void showNotes() {
        VBox page = page();
        page.getChildren().add(titleRow("✎  Notes", "Saisie des notes et consultation des relevés"));

        // ── Sélecteur d'étudiant ──────────────────────────────────────────────
        HBox selectorBar = new HBox(12);
        selectorBar.setAlignment(Pos.CENTER_LEFT);
        Label lblChoix = new Label("Étudiant :");
        lblChoix.setStyle("-fx-text-fill:" + C_MUTED + ";-fx-font-size:13px;"
                + "-fx-font-family:'Segoe UI';-fx-font-weight:bold;");
        ComboBox<Etudiant> cbEtu = combo(etudiantsList);
        cbEtu.setPromptText("Sélectionnez un étudiant...");
        cbEtu.setPrefWidth(300);
        Button btnReleve = btn("📄  Voir le relevé", C_PURPLE);
        Button btnPDF    = btn("⬇  Exporter PDF",   C_GREEN);
        selectorBar.getChildren().addAll(lblChoix, cbEtu, btnReleve, btnPDF);

        // ── Table inscriptions ────────────────────────────────────────────────
        ObservableList<core.actors.Inscription> inscrList = FXCollections.observableArrayList();
        TableView<core.actors.Inscription> tableIns = new TableView<>(inscrList);
        styleTable(tableIns);
        tableIns.setPrefHeight(180);
        tableIns.setMaxHeight(180);
        tableIns.getColumns().add(col("Groupe / Matière", 220,
            (core.actors.Inscription i) -> new SimpleStringProperty(i.getGroupe().getGroupId())));
        tableIns.getColumns().add(col("Nb notes", 90,
            (core.actors.Inscription i) -> new SimpleStringProperty(String.valueOf(i.getNotes().size()))));
        tableIns.getColumns().add(col("Moyenne", 120,
            (core.actors.Inscription i) -> {
                try { return new SimpleStringProperty(String.format("%.2f / 20", i.calculerMoyenne())); }
                catch (core.exceptions.MoyenneIndisponibleException ex) { return new SimpleStringProperty("—"); }
            }));

        // ── Table notes ───────────────────────────────────────────────────────
        ObservableList<core.actors.Note> notesList = FXCollections.observableArrayList();
        TableView<core.actors.Note> tableNotes = new TableView<>(notesList);
        styleTable(tableNotes);
        tableNotes.setPrefHeight(150);
        tableNotes.setMaxHeight(150);
        tableNotes.getColumns().add(col("Valeur", 150,
            (core.actors.Note n) -> new SimpleStringProperty(String.format("%.2f / 20", n.getValeur()))));
        tableNotes.getColumns().add(col("Coefficient", 150,
            (core.actors.Note n) -> new SimpleStringProperty(String.valueOf(n.getCoefficient()))));
        tableNotes.getColumns().add(col("Pondération", 200,
            (core.actors.Note n) -> new SimpleStringProperty(String.format("%.2f pts", n.getValeur() * n.getCoefficient()))));

        // ── Barre moyenne ─────────────────────────────────────────────────────
        Label lblMoyenne = new Label("Moyenne : —");
        lblMoyenne.setStyle("-fx-text-fill:" + C_ACCENT + ";-fx-font-size:15px;"
                + "-fx-font-weight:bold;-fx-font-family:'Segoe UI';"
                + "-fx-background-color:" + C_CARD + ";-fx-padding:10 16 10 16;"
                + "-fx-background-radius:8;-fx-border-color:" + C_BORDER
                + ";-fx-border-radius:8;-fx-border-width:1;");

        // ── Formulaire saisie note ────────────────────────────────────────────
        GridPane fNote  = form();
        TextField fVal  = field("Ex: 14.5  (entre 0 et 20)");
        TextField fCoef = field("Ex: 2");
        formRow(fNote, 0, "Valeur (0–20) :", fVal);
        formRow(fNote, 1, "Coefficient :",   fCoef);

        Button btnAddNote = btn("✔  Confirmer",  C_ACCENT);
        Button btnCanc    = btn("✖  Annuler",    C_MUTED);

        // helper : recharge notesList + moyenne depuis l'inscription sélectionnée
        Runnable refreshNotes = () -> {
            core.actors.Inscription sel = tableIns.getSelectionModel().getSelectedItem();
            notesList.clear();
            if (sel == null) {
                lblMoyenne.setText("Moyenne : —");
                lblMoyenne.setStyle("-fx-text-fill:" + C_ACCENT + ";-fx-font-size:15px;"
                        + "-fx-font-weight:bold;-fx-font-family:'Segoe UI';"
                        + "-fx-background-color:" + C_CARD + ";-fx-padding:10 16 10 16;"
                        + "-fx-background-radius:8;-fx-border-color:" + C_BORDER
                        + ";-fx-border-radius:8;-fx-border-width:1;");
                return;
            }
            notesList.addAll(sel.getNotes());
            try {
                double moy = sel.calculerMoyenne();
                String color = moy >= 10 ? C_GREEN : C_DANGER;
                lblMoyenne.setText(String.format("Moyenne : %.2f / 20", moy));
                lblMoyenne.setStyle("-fx-text-fill:" + color + ";-fx-font-size:15px;"
                        + "-fx-font-weight:bold;-fx-font-family:'Segoe UI';"
                        + "-fx-background-color:" + C_CARD + ";-fx-padding:10 16 10 16;"
                        + "-fx-background-radius:8;-fx-border-color:" + color
                        + ";-fx-border-radius:8;-fx-border-width:1;");
            } catch (core.exceptions.MoyenneIndisponibleException ex) {
                lblMoyenne.setText("Moyenne : aucune note");
                lblMoyenne.setStyle("-fx-text-fill:" + C_MUTED + ";-fx-font-size:15px;"
                        + "-fx-font-weight:bold;-fx-font-family:'Segoe UI';"
                        + "-fx-background-color:" + C_CARD + ";-fx-padding:10 16 10 16;"
                        + "-fx-background-radius:8;-fx-border-color:" + C_BORDER
                        + ";-fx-border-radius:8;-fx-border-width:1;");
            }
        };

        btnAddNote.setOnAction(e -> {
            core.actors.Inscription selIns = tableIns.getSelectionModel().getSelectedItem();
            if (selIns == null) { alert("Sélectionnez une inscription."); return; }
            try {
                double val  = Double.parseDouble(fVal.getText().trim());
                double coef = Double.parseDouble(fCoef.getText().trim());
                selIns.addNote(new core.actors.Note(val, coef));
                fVal.clear(); fCoef.clear();
                hideForm(fNote);
                // recharger la ligne inscription (colonne Nb notes + Moyenne)
                int idx = inscrList.indexOf(selIns);
                if (idx >= 0) inscrList.set(idx, selIns);
                // recharger la table notes + barre moyenne
                refreshNotes.run();
            } catch (NumberFormatException ex) {
                alert("Valeur et coefficient doivent être des nombres.");
            } catch (core.exceptions.NoteInvalideException ex) {
                alert(ex.getMessage());
            }
        });
        btnCanc.setOnAction(e -> { hideForm(fNote); fVal.clear(); fCoef.clear(); });
        fNote.add(btnBar(btnAddNote, btnCanc), 0, 2, 4, 1);

        Button btnShowForm = btn("✎  Saisir une note", C_ACCENT);
        btnShowForm.setOnAction(e -> {
            if (tableIns.getSelectionModel().getSelectedItem() == null) {
                alert("Sélectionnez d'abord une inscription dans la liste."); return;
            }
            fVal.clear(); fCoef.clear();
            showForm(fNote);
        });

        // listener sélection inscription → recharger notes
        tableIns.getSelectionModel().selectedItemProperty().addListener(
            (obs, o, n) -> { hideForm(fNote); refreshNotes.run(); });

        // listener sélection étudiant → recharger inscriptions
        cbEtu.setOnAction(e -> {
            Etudiant etu = cbEtu.getValue();
            inscrList.clear();
            notesList.clear();
            hideForm(fNote);
            lblMoyenne.setText("Moyenne : —");
            if (etu != null) inscrList.addAll(etu.getInscriptions());
        });

        // bouton relevé
        btnReleve.setOnAction(e -> {
            Etudiant etu = cbEtu.getValue();
            if (etu == null) { alert("Sélectionnez un étudiant."); return; }
            if (etu.getInscriptions().isEmpty()) { alert("Cet étudiant n'a aucune inscription."); return; }
            Alert dlg = new Alert(Alert.AlertType.INFORMATION);
            dlg.setTitle("Relevé — " + etu.getName());
            dlg.setHeaderText(null);
            TextArea ta = new TextArea(etu.genererReleve());
            ta.setEditable(false); ta.setPrefWidth(520); ta.setPrefHeight(380);
            ta.setStyle("-fx-control-inner-background:#F8FAFC;-fx-text-fill:" + C_TEXT
                    + ";-fx-font-family:'Courier New';-fx-font-size:13px;");
            dlg.getDialogPane().setContent(ta);
            dlg.getDialogPane().setStyle("-fx-background-color:" + C_CARD + ";");
            dlg.showAndWait();
        });

        // ── Assemblage dans un ScrollPane pour éviter le débordement ──────────
        VBox inner = new VBox(12);
        inner.getChildren().addAll(
            selectorBar,
            sectionLabel("Inscriptions de l'étudiant"),
            tableIns,
            btnBar(btnShowForm),
            fNote,
            sectionLabel("Notes de l'inscription sélectionnée"),
            tableNotes,
            lblMoyenne
        );

        ScrollPane scroll = new ScrollPane(inner);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color:" + C_DARK + ";-fx-background:" + C_DARK + ";");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        page.getChildren().add(scroll);

        // bouton export PDF
        btnPDF.setOnAction(e -> {
            Etudiant etu = cbEtu.getValue();
            if (etu == null) { alert("Sélectionnez un étudiant."); return; }
            if (etu.getInscriptions().isEmpty()) { alert("Cet étudiant n'a aucune inscription."); return; }
            FileChooser fc = new FileChooser();
            fc.setTitle("Enregistrer le relevé PDF");
            fc.setInitialFileName("releve_" + etu.getMatricule() + ".pdf");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            java.io.File dest = fc.showSaveDialog(contentArea.getScene().getWindow());
            if (dest == null) return;
            try {
                exporterPDF(etu, dest);
                alert("✅ PDF enregistré :\n" + dest.getAbsolutePath());
            } catch (Exception ex) {
                alert("Erreur lors de la génération du PDF :\n" + ex.getMessage());
            }
        });

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
        Enseignant e1 = new Enseignant("Noulapeu", "Armielle", "a.noulapeu@enspd.cm", java.time.LocalDate.of(1980, 5, 12), "Permanent",  "Informatique");
        Enseignant e2 = new Enseignant("Mballa",   "Jean",     "j.mballa@enspd.cm",   java.time.LocalDate.of(1975, 9, 3),  "Vacataire",  "Mathématiques");
        enseignantsList.addAll(e1, e2);

        Etudiant s1 = new Etudiant("Fouda",   "Alice",  "a.fouda@etu.enspd.cm",  java.time.LocalDate.of(2002, 3, 14), "21G0001", "Niveau 3", "Génie Logiciel");
        Etudiant s2 = new Etudiant("Nkomo",   "Boris",  "b.nkomo@etu.enspd.cm",  java.time.LocalDate.of(2001, 7, 22), "21G0002", "Niveau 3", "Génie Logiciel");
        Etudiant s3 = new Etudiant("Biyong",  "Carole", "c.biyong@etu.enspd.cm", java.time.LocalDate.of(2003, 1, 8),  "22G0001", "Niveau 2", "Réseau Télécom");
        etudiantsList.addAll(s1, s2, s3);

        Cours c1 = new Cours("INFO-301", "Programmation Orientée Objet", 50, e1);
        Cours c2 = new Cours("MATH-201", "Analyse Numérique",            30, e2);
        coursList.addAll(c1, c2);
        e1.addCourse(c1); e2.addCourse(c2);

        Groupe g1 = new Groupe("CM-INFO",   100, e1);
        Groupe g2 = new Groupe("TD1-INFO",   30, e1);
        Groupe g3 = new Groupe("TD1-MATH",   30, e2);
        try { 
            g1.addEtudiant(s1); g1.addEtudiant(s2); g1.addEtudiant(s3);
            g2.addEtudiant(s1); g2.addEtudiant(s2);
            g3.addEtudiant(s1); g3.addEtudiant(s3);
        }
        catch (CapaciteDepasseeException ignored) {}
        groupesList.addAll(g1, g2, g3);

        // créer les Inscriptions et les lier aux étudiants avec notes de démo
        try {
            // s1 - Alice : inscrite en INFO (g1 + g2) et MATH (g3)
            core.actors.Inscription ins1_g1 = new core.actors.Inscription(s1, g1);
            ins1_g1.addNote(new core.actors.Note(14.0, 2));
            ins1_g1.addNote(new core.actors.Note(16.5, 1));
            s1.addInscription(ins1_g1);

            core.actors.Inscription ins1_g2 = new core.actors.Inscription(s1, g2);
            ins1_g2.addNote(new core.actors.Note(12.0, 2));
            ins1_g2.addNote(new core.actors.Note(11.5, 2));
            s1.addInscription(ins1_g2);

            core.actors.Inscription ins1_g3 = new core.actors.Inscription(s1, g3);
            ins1_g3.addNote(new core.actors.Note(9.0, 3));
            s1.addInscription(ins1_g3);

            // s2 - Boris : inscrit en INFO (g1 + g2)
            core.actors.Inscription ins2_g1 = new core.actors.Inscription(s2, g1);
            ins2_g1.addNote(new core.actors.Note(18.0, 2));
            ins2_g1.addNote(new core.actors.Note(17.0, 1));
            s2.addInscription(ins2_g1);

            core.actors.Inscription ins2_g2 = new core.actors.Inscription(s2, g2);
            ins2_g2.addNote(new core.actors.Note(15.0, 2));
            s2.addInscription(ins2_g2);

            // s3 - Carole : inscrite en INFO (g1) et MATH (g3)
            core.actors.Inscription ins3_g1 = new core.actors.Inscription(s3, g1);
            ins3_g1.addNote(new core.actors.Note(8.5, 2));
            ins3_g1.addNote(new core.actors.Note(10.0, 1));
            s3.addInscription(ins3_g1);

            core.actors.Inscription ins3_g3 = new core.actors.Inscription(s3, g3);
            ins3_g3.addNote(new core.actors.Note(13.0, 3));
            ins3_g3.addNote(new core.actors.Note(14.5, 2));
            s3.addInscription(ins3_g3);

        } catch (core.exceptions.NoteInvalideException ignored) {}

        try {
            Seance se1 = new Seance("S001", g1, e1, c1, "Amphi A",    "2026-03-23", "08:00", "10:00");
            Seance se2 = new Seance("S002", g2, e1, c1, "Salle 402",  "2026-03-23", "10:30", "12:30");
            Seance se3 = new Seance("S003", g1, e2, c2, "Amphi A",    "2026-03-25", "08:00", "10:00");
            planning.addSeance(se1); planning.addSeance(se2); planning.addSeance(se3);
            seancesList.addAll(se1, se2, se3);
        } catch (ConflitHoraireException ignored) {}
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  EXPORT PDF  (pur Java — pas de librairie externe)
    // ═════════════════════════════════════════════════════════════════════════
    private void exporterPDF(Etudiant etu, java.io.File dest) throws Exception {

        // ── Dimensions page A4 en points (1 pt = 1/72 pouce) ─────────────────
        final int W = 595, H = 842;
        final int MARGIN = 50;
        final int LINE_H = 20;

        // ── Couleurs palette — thème Bleu Professionnel ───────────────────────
        Color cBg      = new Color(240, 244, 248);  // F0F4F8 fond clair
        Color cCard    = new Color(255, 255, 255);  // blanc
        Color cAccent  = new Color(37,  99,  235);  // 2563EB bleu
        Color cText    = new Color(30,  41,  59);   // 1E293B texte sombre
        Color cMuted   = new Color(100, 116, 139);  // 64748B gris-bleu
        Color cGreen   = new Color(5,   150, 105);  // 059669 vert sobre
        Color cDanger  = new Color(220, 38,  38);   // DC2626 rouge sobre
        Color cBorder  = new Color(203, 213, 225);  // CBD5E1 bordure claire

        // ── Rendu sur image haute résolution puis conversion PDF ──────────────
        // On génère l'image en 2x pour la qualité
        int SCALE = 2;
        BufferedImage img = new BufferedImage(W * SCALE, H * SCALE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,   RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.scale(SCALE, SCALE);

        // fond
        g.setColor(cBg);
        g.fillRect(0, 0, W, H);

        // ── Header ────────────────────────────────────────────────────────────
        g.setColor(cAccent);
        g.fillRect(MARGIN, 40, W - 2 * MARGIN, 4);

        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.setColor(cAccent);
        g.drawString("CampusConnect — ENSPD", MARGIN, 36);

        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g.setColor(cMuted);
        g.drawString("École Nationale Supérieure Polytechnique de Douala  ·  2025-2026", MARGIN, 58);

        // ── Bloc infos étudiant ───────────────────────────────────────────────
        g.setColor(cCard);
        g.fillRoundRect(MARGIN, 70, W - 2 * MARGIN, 75, 10, 10);
        g.setColor(cBorder);
        g.drawRoundRect(MARGIN, 70, W - 2 * MARGIN, 75, 10, 10);

        g.setFont(new Font("SansSerif", Font.BOLD, 15));
        g.setColor(cAccent);
        g.drawString("RELEVÉ DE NOTES", MARGIN + 14, 92);

        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.setColor(cText);
        g.drawString("Étudiant  : " + etu.getName(),      MARGIN + 14, 112);
        g.drawString("Matricule : " + etu.getMatricule(), MARGIN + 14, 128);
        g.drawString("Filière   : " + etu.getFiliere(),   MARGIN + 250, 112);
        g.drawString("Niveau    : " + etu.getAnneeEtude(),MARGIN + 250, 128);

        // ── Tableau des matières ──────────────────────────────────────────────
        int y = 170;
        int colW1 = 180, colW2 = 240, colW3 = 80;

        // en-tête tableau
        g.setColor(cCard);
        g.fillRoundRect(MARGIN, y - LINE_H + 4, W - 2 * MARGIN, LINE_H, 6, 6);
        g.setFont(new Font("SansSerif", Font.BOLD, 11));
        g.setColor(cAccent);
        g.drawString("MATIÈRE / GROUPE",     MARGIN + 8,           y);
        g.drawString("NOTES (valeur × coeff)", MARGIN + colW1 + 8, y);
        g.drawString("MOYENNE",              MARGIN + colW1 + colW2 + 8, y);

        y += 8;
        g.setColor(cAccent);
        g.fillRect(MARGIN, y, W - 2 * MARGIN, 1);
        y += 14;

        // lignes par inscription
        for (core.actors.Inscription ins : etu.getInscriptions()) {
            // alternance fond de ligne
            g.setColor(new Color(20, 33, 46));
            g.fillRect(MARGIN, y - 14, W - 2 * MARGIN, LINE_H);

            g.setFont(new Font("SansSerif", Font.BOLD, 11));
            g.setColor(cText);
            g.drawString(ins.getGroupe().getGroupId(), MARGIN + 8, y);

            // notes
            StringBuilder notesStr = new StringBuilder();
            for (core.actors.Note n : ins.getNotes()) {
                notesStr.append(String.format("%.1f×%.0f  ", n.getValeur(), n.getCoefficient()));
            }
            g.setFont(new Font("Monospaced", Font.PLAIN, 10));
            g.setColor(cMuted);
            g.drawString(notesStr.length() > 0 ? notesStr.toString() : "—", MARGIN + colW1 + 8, y);

            // moyenne
            try {
                double moy = ins.calculerMoyenne();
                Color mc = moy >= 10 ? cGreen : cDanger;
                g.setFont(new Font("SansSerif", Font.BOLD, 12));
                g.setColor(mc);
                g.drawString(String.format("%.2f / 20", moy), MARGIN + colW1 + colW2 + 8, y);
            } catch (core.exceptions.MoyenneIndisponibleException ex) {
                g.setColor(cMuted);
                g.drawString("—", MARGIN + colW1 + colW2 + 8, y);
            }

            y += LINE_H;
            // séparateur
            g.setColor(cBorder);
            g.fillRect(MARGIN, y - 6, W - 2 * MARGIN, 1);
        }

        // ── Moyenne générale ──────────────────────────────────────────────────
        y += 10;
        double moyGen = etu.calculerMoyenneGenerale();
        Color mcGen = moyGen >= 10 ? cGreen : cDanger;

        g.setColor(cCard);
        g.fillRoundRect(MARGIN, y, W - 2 * MARGIN, 36, 8, 8);
        g.setColor(mcGen);
        g.drawRoundRect(MARGIN, y, W - 2 * MARGIN, 36, 8, 8);

        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.setColor(cText);
        g.drawString("MOYENNE GÉNÉRALE", MARGIN + 14, y + 23);
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.setColor(mcGen);
        g.drawString(String.format("%.2f / 20", moyGen), W - MARGIN - 110, y + 24);

        // ── Mention ───────────────────────────────────────────────────────────
        y += 56;
        String mention = moyGen >= 16 ? "Très Bien" : moyGen >= 14 ? "Bien"
                       : moyGen >= 12 ? "Assez Bien" : moyGen >= 10 ? "Passable" : "Ajourné(e)";
        g.setFont(new Font("SansSerif", Font.ITALIC, 12));
        g.setColor(mcGen);
        g.drawString("Mention : " + mention, MARGIN + 14, y);

        // ── Footer ────────────────────────────────────────────────────────────
        g.setColor(cBorder);
        g.fillRect(MARGIN, H - 40, W - 2 * MARGIN, 1);
        g.setFont(new Font("SansSerif", Font.PLAIN, 9));
        g.setColor(cMuted);
        g.drawString("Document généré par CampusConnect  ·  " + java.time.LocalDate.now(), MARGIN, H - 24);
        g.drawString("Ce document est fourni à titre indicatif.", W - MARGIN - 180, H - 24);

        g.dispose();

        // ── Écrire le PDF minimal (1 page = 1 image JPEG dans un PDF) ─────────
        // Format PDF minimal écrit à la main (sans librairie)
        ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
        // Redimensionner à W×H pour le PDF
        BufferedImage page = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = page.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, W, H, null);
        g2.dispose();
        ImageIO.write(page, "JPEG", imgBytes);
        byte[] jpegData = imgBytes.toByteArray();

        // Construire le PDF minimal
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            writePDF(fos, jpegData, W, H);
        }
    }

    /** Exporte n'importe quelle liste sous forme de tableau PDF */
    private void exporterListePDF(String titre, List<String> headers, List<List<String>> rows, java.io.File dest) throws Exception {

        final int W = 842, H = 595; // A4 paysage pour les tableaux larges
        final int MARGIN = 40;
        final int ROW_H = 22;
        final int HEADER_H = 28;
        final int TOP = 110; // y de départ du tableau

        Color cBg     = new Color(240, 244, 248);  // F0F4F8 fond clair
        Color cCard   = new Color(255, 255, 255);  // blanc
        Color cAccent = new Color(37,  99,  235);  // 2563EB bleu
        Color cText   = new Color(30,  41,  59);   // 1E293B texte sombre
        Color cMuted  = new Color(100, 116, 139);  // 64748B gris-bleu
        Color cBorder = new Color(203, 213, 225);  // CBD5E1 bordure claire
        Color cAlt    = new Color(248, 250, 252);  // F8FAFC ligne alternée

        int SCALE = 2;
        BufferedImage img = new BufferedImage(W * SCALE, H * SCALE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.scale(SCALE, SCALE);

        // fond
        g.setColor(cBg);
        g.fillRect(0, 0, W, H);

        // header
        g.setColor(cAccent);
        g.fillRect(MARGIN, 42, W - 2 * MARGIN, 3);
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        g.setColor(cAccent);
        g.drawString("CampusConnect — ENSPD", MARGIN, 38);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.setColor(cText);
        g.drawString(titre, MARGIN, 68);
        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g.setColor(cMuted);
        g.drawString(rows.size() + " enregistrement(s)  ·  " + java.time.LocalDate.now(), MARGIN, 84);

        // calcul largeur colonnes
        int tableW = W - 2 * MARGIN;
        int nCols  = headers.size();
        int colW   = tableW / nCols;

        // en-tête colonnes
        g.setColor(cCard);
        g.fillRect(MARGIN, TOP - HEADER_H, tableW, HEADER_H);
        g.setColor(cAccent);
        g.drawRect(MARGIN, TOP - HEADER_H, tableW, HEADER_H);
        g.setFont(new Font("SansSerif", Font.BOLD, 10));
        g.setColor(cAccent);
        for (int i = 0; i < nCols; i++) {
            g.drawString(headers.get(i), MARGIN + i * colW + 6, TOP - HEADER_H + 17);
            if (i > 0) { g.setColor(cBorder); g.fillRect(MARGIN + i * colW, TOP - HEADER_H, 1, HEADER_H); g.setColor(cAccent); }
        }

        // lignes
        int maxRows = (H - TOP - 40) / ROW_H;
        int displayed = Math.min(rows.size(), maxRows);
        for (int r = 0; r < displayed; r++) {
            int y = TOP + r * ROW_H;
            g.setColor(r % 2 == 0 ? cAlt : cBg);
            g.fillRect(MARGIN, y, tableW, ROW_H);
            g.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g.setColor(cText);
            List<String> row = rows.get(r);
            for (int i = 0; i < nCols && i < row.size(); i++) {
                String cell = row.get(i);
                // tronquer si trop long
                if (cell != null && cell.length() > colW / 6) cell = cell.substring(0, colW / 6) + "…";
                g.drawString(cell != null ? cell : "—", MARGIN + i * colW + 6, y + 15);
                g.setColor(cBorder);
                g.fillRect(MARGIN + i * colW, y, 1, ROW_H);
                g.setColor(cText);
            }
            // ligne horizontale
            g.setColor(cBorder);
            g.fillRect(MARGIN, y + ROW_H - 1, tableW, 1);
        }
        // bordure tableau
        g.setColor(cBorder);
        g.drawRect(MARGIN, TOP - HEADER_H, tableW, HEADER_H + displayed * ROW_H);

        // note si trop de lignes
        if (rows.size() > maxRows) {
            g.setFont(new Font("SansSerif", Font.ITALIC, 9));
            g.setColor(cMuted);
            g.drawString("* " + (rows.size() - maxRows) + " enregistrement(s) supplémentaire(s) non affiché(s) — page unique.", MARGIN, H - 28);
        }

        // footer
        g.setColor(cBorder);
        g.fillRect(MARGIN, H - 20, W - 2 * MARGIN, 1);
        g.setFont(new Font("SansSerif", Font.PLAIN, 8));
        g.setColor(cMuted);
        g.drawString("Document généré par CampusConnect  ·  " + java.time.LocalDate.now(), MARGIN, H - 10);

        g.dispose();

        // écrire PDF
        BufferedImage page = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = page.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, W, H, null);
        g2.dispose();

        ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
        ImageIO.write(page, "JPEG", imgBytes);
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            writePDF(fos, imgBytes.toByteArray(), W, H);
        }
    }


    private void writePDF(OutputStream out, byte[] jpeg, int w, int h) throws IOException {
        // objets PDF
        // 1=catalog 2=pages 3=page 4=image 5=content 6=resources
        List<Long> offsets = new java.util.ArrayList<>();
        ByteArrayOutputStream buf = new ByteArrayOutputStream();

        write(buf, "%PDF-1.4\n");

        // obj 1 - catalog
        offsets.add((long) buf.size());
        write(buf, "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");

        // obj 2 - pages
        offsets.add((long) buf.size());
        write(buf, "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n");

        // obj 3 - page
        offsets.add((long) buf.size());
        write(buf, "3 0 obj\n<< /Type /Page /Parent 2 0 R"
                + " /MediaBox [0 0 " + w + " " + h + "]"
                + " /Contents 5 0 R /Resources 6 0 R >>\nendobj\n");

        // obj 4 - image XObject
        offsets.add((long) buf.size());
        write(buf, "4 0 obj\n<< /Type /XObject /Subtype /Image"
                + " /Width " + w + " /Height " + h
                + " /ColorSpace /DeviceRGB /BitsPerComponent 8"
                + " /Filter /DCTDecode /Length " + jpeg.length + " >>\nstream\n");
        buf.write(jpeg);
        write(buf, "\nendstream\nendobj\n");

        // obj 5 - content stream (dessine l'image sur toute la page)
        String stream = "q " + w + " 0 0 " + h + " 0 0 cm /Im1 Do Q\n";
        offsets.add((long) buf.size());
        write(buf, "5 0 obj\n<< /Length " + stream.length() + " >>\nstream\n"
                + stream + "endstream\nendobj\n");

        // obj 6 - resources
        offsets.add((long) buf.size());
        write(buf, "6 0 obj\n<< /XObject << /Im1 4 0 R >> >>\nendobj\n");

        // xref
        long xrefOffset = buf.size();
        write(buf, "xref\n0 7\n0000000000 65535 f \n");
        for (long off : offsets) {
            write(buf, String.format("%010d 00000 n \n", off));
        }
        write(buf, "trailer\n<< /Size 7 /Root 1 0 R >>\n");
        write(buf, "startxref\n" + xrefOffset + "\n%%EOF\n");

        out.write(buf.toByteArray());
    }

    private void write(OutputStream out, String s) throws IOException {
        out.write(s.getBytes(java.nio.charset.StandardCharsets.ISO_8859_1));
    }

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
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
        tf.setStyle("-fx-background-color:#F8FAFC;-fx-text-fill:" + C_TEXT
                + ";-fx-prompt-text-fill:" + C_MUTED
                + ";-fx-border-color:" + C_BORDER
                + ";-fx-border-radius:6;-fx-background-radius:6;-fx-padding:7 10 7 10;"
                + "-fx-font-family:'Segoe UI';");
        return tf;
    }

    private <T> ComboBox<T> combo(ObservableList<T> items) {
        ComboBox<T> cb = new ComboBox<>(items);
        cb.setPrefWidth(220);
        cb.setStyle("-fx-background-color:#F8FAFC;-fx-text-fill:" + C_TEXT
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