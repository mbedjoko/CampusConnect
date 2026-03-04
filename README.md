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

### 3️⃣ Convention de nommage des branches

| Type de branche       | Exemple         |
| --------------------- | --------------- |
| Feature principale    | MekemeB-feature |
| Correction de bug     | MekemeB-bugfix  |
| Interface utilisateur | MekemeB-ui      |

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


