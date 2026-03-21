HEAD
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

## 📌 Workflow Git pour l'équipe

Chaque membre de l'équipe travaille sur sa **branche personnelle** pour éviter d’écraser le travail des autres.  
Les merges dans `main` se font **uniquement via Pull Request (PR)**.

### 1️⃣ Branches personnelles
Branches actives pour les membres :

| Membre | Branche |
|--------|---------|
| MekemeB | MekemeB-feature |
| BongaD  | BongaD-feature |
| KoagneD | KoagneD-feature |
| HenriF  | HenriF-feature |
| BriceK  | BriceK-feature |

---

### 2️⃣ Workflow quotidien

1. **Cloner le repo**

```bash
git clone https://github.com/mbedjoko/CampusConnect.git
cd CampusConnect
````

2. **Récupérer sa branche personnelle**

```bash
git fetch origin
git checkout -b <votre-branche> origin/<votre-branche>
```

> Exemple pour MekemeB :
>
> ```bash
> git checkout -b MekemeB-feature origin/MekemeB-feature
> ```

3. **Travailler sur sa branche**

```bash
git add .
git commit -m "Description claire de la modification"
git push origin <votre-branche>
```

4. **Mettre sa branche à jour avec main avant PR**

```bash
git checkout <votre-branche>
git fetch origin
git merge origin/main
git push origin <votre-branche>
```

5. **Créer une Pull Request (PR) vers `main`** sur GitHub

   * Vérifier le code
   * Merge uniquement après validation

6. **Supprimer les branches terminées**

```bash
git push origin --delete <votre-branche>
```

---

### 3️⃣ Branches et tâches assignées


| Membre    | Branche           | Tâches / Modules assignés            |
|-----------|-----------------|-------------------------------------|
| BongaD    | BongaD-feature  | Actors, gestion des étudiants       |
| HenriF   | HenriF-feature   | Courses, inscription aux cours et Group|
| BriceK   | BriceK-feature   | Planning, calendrier, organisation  |
| MekemeB  | MekemeB-feature  | UI, interface console, affichages   |
| KoagneD  | KoagneD-feature  | Exceptions, validations, erreurs    |

> Chaque branche correspond à **une personne ou une fonctionnalité spécifique**.

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
