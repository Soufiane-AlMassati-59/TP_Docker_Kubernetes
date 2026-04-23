# Documentation - Plateforme de Billetterie Théâtre

## Documents disponibles

### ARCHITECTURE.md
Architecture technique complète du système : Backend Spring Boot, Frontend React, modèle de données, endpoints API REST, authentification JWT et patterns implémentés.

### DEMARRAGE.md
Guide de mise en route : prérequis, configuration PostgreSQL, scripts de démarrage automatiques, commandes manuelles, endpoints disponibles et dépannage.

### DOCUMENTATION_TECHNIQUE.md
Détails techniques : architecture 3-Tiers, modélisation MERISE et UML, design patterns, sécurité JWT/BCrypt, gestion du panier React Context.

### RAPPORT_SYNTHESE.md
Résumé du projet : objectifs, technologies utilisées, architecture, résultats obtenus et fonctionnalités implémentées.

### LIVRABLES.md
Liste des livrables :
- Inventaire complet des fichiers livrés (code, documentation, scripts)
- Structure backend et frontend détaillée
- Scripts de démarrage Windows et Linux
- Outils de test (TEST_API.html)
- Organisation des livrables
- État de complétion du projet (100%)
- Points forts et technologies utilisées
- Checklist de validation

---

## Parcours recommandé

### Pour démarrer rapidement
1. Lire le [README.md](../README.md) à la racine
2. Suivre le [DEMARRAGE.md](DEMARRAGE.md)
3. Consulter le [GUIDE_UTILISATION.md](GUIDE_UTILISATION.md)

### Pour comprendre le projet
1. Lire [ARCHITECTURE.md](ARCHITECTURE.md)
2. Consulter [ETAT_DU_PROJET.md](ETAT_DU_PROJET.md)
3. Parcourir [RAPPORT_SYNTHESE.md](RAPPORT_SYNTHESE.md)

### Pour déployer en production
1. Suivre [GUIDE_DEPLOIEMENT.md](GUIDE_DEPLOIEMENT.md)
2. Consulter [DOCUMENTATION_TECHNIQUE.md](DOCUMENTATION_TECHNIQUE.md)


## Scripts de démarrage

### Windows
- `START_ALL.bat` - Lance backend + frontend automatiquement
- `START_BACKEND.bat` - Lance uniquement le backend
- `START_FRONTEND.bat` - Lance uniquement le frontend
- `install-dependencies.bat` - Installe les dépendances Maven

### Linux/Mac
- `start-backend.sh` - Compile et lance le backend
