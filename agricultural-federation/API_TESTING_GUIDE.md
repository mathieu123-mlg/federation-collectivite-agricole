# Guide de Test et Validation - Agricultural Federation API v0.0.3

## Endpoints Disponibles

### 1. Collectivités

#### 1.1 Créer des collectivités
```http
POST /collectivities
Content-Type: application/json

[
  {
    "location": "Antananarivo",
    "members": ["M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M10"],
    "federationApproval": true,
    "structure": {
      "president": {"memberId": "M1", "occupation": "PRESIDENT"},
      "vicePresident": {"memberId": "M2", "occupation": "VICE_PRESIDENT"},
      "treasurer": {"memberId": "M3", "occupation": "TREASURER"},
      "secretary": {"memberId": "M4", "occupation": "SECRETARY"}
    }
  }
]

Response: 201 Created
```

#### 1.2 Récupérer une collectivité
```http
GET /collectivities/{id}

Response: 200 OK
```

#### 1.3 Mettre à jour les informations d'une collectivité
```http
PUT /collectivities/{id}/informations
Content-Type: application/json

{
  "number": 1,
  "name": "Collectivité Centrale"
}

Response: 200 OK
```

#### 1.4 Créer les cotisations pour une collectivité
```http
POST /collectivities/{id}/membershipFees
Content-Type: application/json

[
  {
    "label": "Cotisation Mensuelle",
    "frequency": "MONTHLY",
    "amount": 50000.0,
    "eligibleFrom": "2026-05-01"
  }
]

Response: 200 OK
```

#### 1.5 Récupérer les cotisations d'une collectivité
```http
GET /collectivities/{id}/membershipFees

Response: 200 OK
```

#### 1.6 Récupérer les transactions d'une collectivité
```http
GET /collectivities/{id}/transactions?from=2026-04-01T00:00:00Z&to=2026-05-31T23:59:59Z

Response: 200 OK
```

#### 1.7 Récupérer les comptes financiers d'une collectivité
```http
GET /collectivities/{id}/financialAccounts?at=2026-05-06

Response: 200 OK
```

### 2. Membres

#### 2.1 Créer des membres
```http
POST /members
Content-Type: application/json

[
  {
    "firstName": "Jean",
    "lastName": "Dupont",
    "birthDate": "1990-01-15",
    "gender": "MALE",
    "address": "123 Rue Principale",
    "profession": "Agriculteur",
    "phoneNumber": "+261341234567",
    "email": "jean.dupont@example.com",
    "occupation": "JUNIOR",
    "collectivityIdentifier": "col-1",
    "referees": ["M1", "M2"],
    "registrationFeePaid": true,
    "membershipDuesPaid": true
  }
]

Response: 201 Created
```

#### 2.2 Créer des paiements pour un membre
```http
POST /members/{id}/payments
Content-Type: application/json

[
  {
    "amount": 50000,
    "membershipFeeIdentifier": "fee-1",
    "accountCreditedIdentifier": "acc-1",
    "paymentMode": "BANK_TRANSFER"
  }
]

Response: 201 Created
```

## Codes d'Erreur

| Code | Description |
|------|-------------|
| 201  | Ressource créée avec succès |
| 200  | Requête réussie |
| 400  | Requête invalide (validation échouée) |
| 401  | Non autorisé (approbation fédérale manquante, modification déjà effectuée) |
| 404  | Ressource non trouvée |

## Validations Implémentées

### Collectivités
- ✅ Approbation fédérale requise
- ✅ Structure obligatoire (Président, Trésorier, Secrétaire)
- ✅ Minimum 10 membres requis
- ✅ Identifiant peut être mis à jour une seule fois
- ✅ Cotisations validées (montant > 0, fréquence requise, label requis)

### Membres
- ✅ Collectivité doit exister
- ✅ Au moins un parrain requis
- ✅ Frais d'inscription doivent être payés
- ✅ Cotisations obligatoires doivent être payées
- ✅ Parrains doivent exister

### Paiements
- ✅ Montant doit être > 0
- ✅ Mode de paiement requis
- ✅ Compte destinataire doit exister

## Architecture et Design

### Single Responsibility Principle (SRP)
- **Validators** : Valident uniquement les entrées
- **Repositories** : Gèrent l'accès aux données
- **Services** : Encapsulent la logique métier
- **Controllers** : Routent et sérialisent les réponses
- **Mappers** : Convertissent les ResultSet en entités

### Avantages
- Code facilement testable
- Maintenance simplifiée
- Responsabilités claires
- Réutilisabilité du code

## Dépendances
- Spring Boot 4.0.5
- PostgreSQL 42.7.10
- JDBC pur (pas de Hibernate)
- dotenv-java 3.2.0

## Notes Importantes

⚠️ **JDBC Pur**
- Pas de Lombok
- Pas d'ORM
- Getters/Setters manuels
- Gestion manuelle des transactions

✅ **Conformité OAS v0.0.3**
- Tous les endpoints implémentés
- Tous les paramètres respectés
- Tous les status HTTP corrects
- Tous les formats de données conformes

## Exemple d'Utilisation Complète

### 1. Créer une collectivité
```bash
curl -X POST http://localhost:8080/collectivities \
  -H "Content-Type: application/json" \
  -d '[{
    "location": "Antananarivo",
    "members": ["M1", "M2", ...],
    "federationApproval": true,
    "structure": {...}
  }]'
```

### 2. Mettre à jour les informations
```bash
curl -X PUT http://localhost:8080/collectivities/col-1/informations \
  -H "Content-Type: application/json" \
  -d '{"number": 1, "name": "Collectivité Antananarivo"}'
```

### 3. Créer les cotisations
```bash
curl -X POST http://localhost:8080/collectivities/col-1/membershipFees \
  -H "Content-Type: application/json" \
  -d '[{"label": "Mensuelle", "frequency": "MONTHLY", "amount": 50000, "eligibleFrom": "2026-05-01"}]'
```

### 4. Créer des membres
```bash
curl -X POST http://localhost:8080/members \
  -H "Content-Type: application/json" \
  -d '[{
    "firstName": "Jean",
    "lastName": "Dupont",
    "birthDate": "1990-01-15",
    "gender": "MALE",
    "address": "123 Rue",
    "profession": "Agriculteur",
    "phoneNumber": "+261341234567",
    "email": "jean@example.com",
    "occupation": "JUNIOR",
    "collectivityIdentifier": "col-1",
    "referees": ["M1", "M2"],
    "registrationFeePaid": true,
    "membershipDuesPaid": true
  }]'
```

### 5. Créer les paiements
```bash
curl -X POST http://localhost:8080/members/M1/payments \
  -H "Content-Type: application/json" \
  -d '[{
    "amount": 50000,
    "membershipFeeIdentifier": "fee-1",
    "accountCreditedIdentifier": "acc-1",
    "paymentMode": "BANK_TRANSFER"
  }]'
```

## Développement Futur

- [ ] Ajouter les statistiques de la fédération (fonctionnalité H)
- [ ] Ajouter les tests unitaires
- [ ] Ajouter les tests d'intégration
- [ ] Documenter les modèles de base de données
- [ ] Implémenter la pagination
- [ ] Ajouter la validation des formats bancaires
- [ ] Ajouter les logs structurés
- [ ] Ajouter la sécurité (JWT/OAuth2)

---

**Date** : 6 Mai 2026
**Version API** : 0.0.3
**Conformité** : OAS 3.1.0
