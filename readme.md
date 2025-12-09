# WACDO – Gestionnaire des affectations

**WacDo** est une application interne permettant de gérer les collaborateurs, leurs affectations, les restaurants et les fonctions.  
Elle offre une interface simple pour suivre l’historique et les affectations en cours au sein du groupe WacDo.

---

## Fonctionnalités

- 👥 **Gestion des collaborateurs**
- 🍽️ **Gestion des restaurants**
- 🏷️ **Gestion des fonctions (Jobs)**
- 🔄 **Suivi des affectations** (actuelles & historiques)
- 🔍 **Filtrage / recherche**
- 🔐 **Authentification**
- 🧪 **Tests unitaires & d’intégration** (JUnit, Spring Boot Test, MockMvc)
- 📊 **Rapport de couverture de tests** (JaCoCo + Surefire)

---

## Prérequis

- **Java 17**
- **Maven**
- **MySQL 8.0+**

---

## Installation et lancement en local

### 1. Cloner le dépôt

```bash
  git clone https://github.com/Yann-vdv/wacdo
  cd wacdo
```

### 2. Installer la bdd avec docker
```
docker-compose up -d
```
### 3. Lancer l'application
```
mvn spring-boot:run
```

## Lancer les tests

### 1. Installer la bdd avec docker
```
mvn clean test
```

### 2. Générer les rapports jacoco et surefire
```
// -- Jacoco
mvn jacoco:report

// -- Surefire : 
mvn surefirce-report:report-only

// -- Les deux :
mvn jacoco:report surefirce-report:report-only
```
---
## Environnement de production

- URL de production : https://wacdo-k6cp.onrender.com/
- User de production : **userProd@gmail.com**
- Mot de passe de production : **12345678**


