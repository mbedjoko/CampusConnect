🔹 README pour BongaD (Actors / gestion des étudiants)
# Branch: BongaD-feature
# Module: Actors / Gestion des étudiants

Salut BongaD 👋,

Voici tes instructions pour travailler sur ta branche :

## 📌 Règles principales

1. **Travaille uniquement sur ta branche `BongaD-feature`.**
2. **Ne touche jamais aux branches des autres membres.**
3. **Ne pousse jamais sur `main`.**
4. Consulte le README de `main` pour le workflow général.

## 🔹 Workflow quotidien

1. Récupère les dernières modifications de `main` avant de commencer :

```bash
git checkout BongaD-feature
git fetch origin
git merge origin/main

Développe les fonctionnalités de gestion des étudiants et acteurs.

Commit régulièrement avec un message clair :

git add .
git commit -m "Ajout / modification de <fonctionnalité>"

Pousse tes changements sur GitHub :

git push origin BongaD-feature

Crée une Pull Request vers main uniquement lorsque la fonctionnalité est terminée et testée.

⚠️ Rappel

Ne jamais toucher aux autres branches.

Si tu as un doute, contacte l’admin avant de merger ou de modifier.

#  CampusConnect - Guide de collaboration Git

# CampusConnect 
origin/main

> Système de gestion académique développé en Java — École Nationale Supérieure Polytechnique de Douala (ENSPD)  
> Projet POO · Année académique 2025-2026

---

## Table des matières

- [Aperçu](#aperçu)
- [Fonctionnalités](#fonctionnalités)
- [Architecture du projet](#architecture-du-projet)
- [Prérequis](#prérequis)
- [Lancer l'application](#lancer-lapplication)
- [Structure des packages](#structure-des-packages)
- [Équipe & branches Git](#équipe--branches-git)
- [Workflow Git](#workflow-git)
- [Bonnes pratiques](#bonnes-pratiques)

---

## Aperçu

**CampusConnect** est une application de bureau JavaFX permettant à une école de gérer ses étudiants, enseignants, cours, groupes de TD/TP et plannings de séances. Elle intègre un système de notes avec calcul de moyennes, détection de conflits horaires, et export PDF.

---

## Fonctionnalités

| Module | Description |
|--------|-------------|
| 👨‍🎓 **Étudiants** | Ajout, suppression, consultation des étudiants inscrits |
| 👨‍🏫 **Enseignants** | Gestion du corps enseignant (permanent / vacataire) |
| 📚 **Cours** | Création de cours avec code, capacité et enseignant responsable |
| 👥 **Groupes** | Groupes de type CM, TD ou TP avec inscription des étudiants |
| 🗓️ **Planning** | Planification des séances avec détection automatique des conflits (salle, enseignant, groupe) |
| ✏️ **Notes** | Saisie de notes coefficientées, calcul de moyennes, génération de relevés |
| 📄 **Export PDF** | Export des listes (étudiants, enseignants, cours, groupes, séances) et relevés de notes |

---

## Architecture du projet

```
CampusConnect/
└── src/
    ├── core/
    │   ├── actors/          # Personne, Etudiant, Enseignant, Inscription, Note, GestionnaireActeurs
    │   ├── courses/         # Cours, Salle
    │   ├── group/           # Groupe
    │   ├── planning/        # Planning, Seance, CreneauHoraire
    │   └── exceptions/      # NoteInvalideException, CapaciteDepasseeException,
    │                        # ConflitHoraireException, MoyenneIndisponibleException
    └── ui/
        └── CampusConnectConsole.java   # Interface JavaFX principale
```

### Diagramme de dépendances simplifié

```
Personne
  ├── Etudiant  ──→  Inscription  ──→  Note
  └── Enseignant ──→ Cours ──→ Groupe ──→ Seance
                                  ↑
                               Planning
```

---

## Prérequis

- **Java 17+**
- **JavaFX 17+** (à inclure dans le classpath si non bundlé)
- Un IDE tel qu'IntelliJ IDEA, Eclipse ou VS Code avec le plugin Java

---

## Lancer l'application

### Avec un IDE

1. Cloner le dépôt :
   ```bash
   git clone https://github.com/mbedjoko/CampusConnect.git
   cd CampusConnect
   ```

2. Ouvrir le projet dans votre IDE et configurer JavaFX dans le classpath.

3. Lancer la classe principale :
   ```
   ui.CampusConnectConsole
   ```

### En ligne de commande (avec JavaFX SDK)

```bash
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml \
      -d out $(find src -name "*.java")

java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml \
     -cp out ui.CampusConnectConsole
```

---

## Structure des packages

### `core.actors`
| Classe | Rôle |
|--------|------|
| `Personne` | Classe abstraite de base (nom, prénom, email, date de naissance) |
| `Etudiant` | Étudiant avec matricule, filière, inscriptions |
| `Enseignant` | Enseignant avec statut et département |
| `Inscription` | Lien entre un étudiant et un groupe, avec ses notes |
| `Note` | Valeur (0–20) et coefficient |
| `GestionnaireActeurs` | Registre central des personnes du système |

### `core.courses`
| Classe | Rôle |
|--------|------|
| `Cours` | Cours avec code, intitulé, capacité et enseignant responsable |
| `Salle` | Salle avec identifiant, capacité et type (Amphi, Classique, TP_INFO) |

### `core.group`
| Classe | Rôle |
|--------|------|
| `Groupe` | Groupe de type CM/TD/TP, lié à un cours et une salle |

### `core.planning`
| Classe | Rôle |
|--------|------|
| `Planning` | Conteneur de séances avec détection de conflits |
| `Seance` | Séance planifiée (groupe, enseignant, cours, salle, créneau) |
| `CreneauHoraire` | Date, heure de début et heure de fin |

### `core.exceptions`
| Exception | Déclenchée quand |
|-----------|-----------------|
| `NoteInvalideException` | Note hors de l'intervalle [0, 20] |
| `CapaciteDepasseeException` | Inscription au-delà de la capacité du groupe ou du cours |
| `ConflitHoraireException` | Deux séances en conflit (salle, enseignant ou groupe) |
| `MoyenneIndisponibleException` | Calcul de moyenne demandé sans aucune note |

---

## Équipe & branches Git

| Membre | Branche | Module assigné |
|--------|---------|----------------|
| **MekemeB** | `MekemeB-feature` | UI JavaFX, interface, affichages |
| **BongaD** | `BongaD-feature` | Actors — Etudiant, Enseignant, GestionnaireActeurs |
| **HenriF** | `HenriF-feature` | Courses — Cours, Salle, Groupe, Inscription |
| **BriceK** | `bricek-feature` | Planning — Seance, CreneauHoraire, détection conflits |
| **KoagneD** | `KoagneD-feature` | Exceptions et validations |

---

## Workflow Git

### 1. Cloner et rejoindre sa branche

```bash
git clone https://github.com/mbedjoko/CampusConnect.git
cd CampusConnect
git fetch origin
git checkout -b <votre-branche> origin/<votre-branche>
```

### 2. Travailler et pousser

```bash
git add .
git commit -m "feat(module): description courte de la modification"
git push origin <votre-branche>
```

### 3. Se mettre à jour avec `main` avant une PR

```bash
git fetch origin
git merge origin/main
# Résoudre les conflits si nécessaire
git push origin <votre-branche>
```

### 4. Créer une Pull Request

- Aller sur GitHub → **Pull Requests** → **New Pull Request**
- Base : `main` ← Compare : `<votre-branche>`
- Ajouter une description claire et demander une review

---

## Bonnes pratiques

- Ne jamais pousser directement sur `main`
- Un commit = une modification logique, avec un message clair
- Toujours tirer `main` avant de commencer une session de travail
- Tester localement avant de créer une PR
- Résoudre les conflits de merge avant de soumettre
- Ne jamais modifier la branche d'un autre membre

---


### 4️⃣ Bonnes pratiques

* Ne jamais pousser directement sur `main`.
* Faire des commits clairs et réguliers.
* Tirer les dernières modifications de `main` avant de commencer.
* Les Pull Requests doivent être vérifiées avant merge.
* Mettre à jour le README et la documentation si nécessaire.

---

### 5️⃣ Évolutions possibles

* Ajouter **GitHub Actions** pour tester automatiquement les PR.
* Activer les **status checks** pour valider les merges automatiquement.
* Passer à une organisation GitHub Team pour appliquer les **Branch Protection Rules**.


 3abbf7bd83320d641b6206460d3ff9d92ba22845

*CampusConnect — Projet POO Java · ENSPD Douala · 2025-2026*
origin/main
